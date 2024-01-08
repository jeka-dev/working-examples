import dev.jeka.core.api.depmanagement.JkDependencySet
import dev.jeka.core.api.file.JkPathTree
import dev.jeka.core.api.kotlin.JkKotlinCompiler
import dev.jeka.core.api.project.JkProject
import dev.jeka.core.tool.JkDoc
import dev.jeka.core.tool.JkInjectClasspath
import dev.jeka.core.tool.KBean
import dev.jeka.core.tool.builtins.project.ProjectKBean
import dev.jeka.plugins.kotlin.KotlinJvmKBean
import dev.jeka.plugins.nodejs.JkNodeJs
import dev.jeka.plugins.springboot.SpringbootKBean

@JkInjectClasspath("dev.jeka:kotlin-plugin")
@JkInjectClasspath("dev.jeka:nodejs-plugin")
@JkInjectClasspath("dev.jeka:springboot-plugin")
class Build : KBean() {

    @JkDoc("If true, Springboot jar will embed the client application")
    var packClient = true

    val springbootKBean = load(SpringbootKBean::class.java)

    val projectKBean = load(ProjectKBean::class.java)

    val nodejs = JkNodeJs.ofVersion("18.12.0");

    val kotlinKBean = load(KotlinJvmKBean::class.java)

    override fun init() {
        configure(projectKBean.project)
        configureKotlinCompiler(kotlinKBean.compiler)
        nodejs.setWorkingDir(baseDir.resolve("client"))
    }

    private fun configure(project: JkProject) {
        project.flatFacade()
            .configureCompileDependencies(this::compileDeps)
            .configureRuntimeDependencies(this::runtimeDeps)
            .configureTestDependencies(this::testDeps)
        if (!packClient) {
            return
        }

        // includes client build (via npm) into the main build
        val clientBuild: JkPathTree = JkPathTree.of(baseDir.resolve("client/build"))
        val serverStatic = project.compilation.layout.classDirPath.resolve("static")
        project.compilation.postCompileActions.append("client pack") {
            buildClient()
            clientBuild.copyTo(serverStatic)
        }
        project.cleanExtraActions.append ({
            clientBuild.deleteContent()
        })

    }

    private fun buildClient() {
        nodejs.npx("yarn install")
        nodejs.npm("run build")
    }

    private fun configureKotlinCompiler(compiler : JkKotlinCompiler) {
        compiler
            .addPlugin("org.jetbrains.kotlin:kotlin-allopen")
            .addPluginOption("org.jetbrains.kotlin.allopen", "preset", "spring")
    }

    private fun compileDeps(deps : JkDependencySet) : JkDependencySet {
        return deps
            .and("org.springframework.boot:spring-boot-starter-web")
            .and("org.springframework.boot:spring-boot-starter-data-jpa")
            .and("org.springframework.boot:spring-boot-starter-data-rest")
    }

    private fun runtimeDeps(deps : JkDependencySet) : JkDependencySet {
        return deps.and("com.h2database:h2:2.1.214")
    }

    private fun testDeps(deps : JkDependencySet) : JkDependencySet {
        return deps.and("org.springframework.boot:spring-boot-starter-web")
    }

}