import dev.jeka.core.api.file.JkPathTree
import dev.jeka.core.api.kotlin.JkKotlinCompiler
import dev.jeka.core.api.project.JkProject
import dev.jeka.core.api.system.JkLog
import dev.jeka.core.tool.JkBean
import dev.jeka.core.tool.JkDoc
import dev.jeka.plugins.kotlin.KotlinJvmJkBean
import dev.jeka.plugins.nodejs.NodeJsJkBean
import dev.jeka.plugins.springboot.SpringbootJkBean
import java.awt.Desktop
import java.net.URI

class Build : JkBean() {

    @JkDoc("If true, Springboot jar will embed the client application")
    var packClient = false

    val springboot = getBean(SpringbootJkBean::class.java)

    val nodejs = getBean(NodeJsJkBean::class.java)

    val kotlin = getBean(KotlinJvmJkBean::class.java)

    init {
        springboot.projectBean.configure(this::configure)
        kotlin.configureCompiler(this::configureKotlinCompiler)
    }

    private fun configure(project: JkProject) {
        if (!packClient) {
            return
        }
        // includes client build (via npm) into the main build
        val clientBuild: JkPathTree<*> = JkPathTree.of(nodejs.getWorkingDir().resolve("build"))
        val serverStatic = project.prodCompilation.layout.classDirPath.resolve("static")
        project.prodCompilation.postCompileActions.append("client pack") {
            buildClient()
            clientBuild.copyTo(serverStatic)
        }
        project.cleanExtraActions.append {
            clientBuild.deleteContent()
        }
    }

    fun buildClient() {
        nodejs.npx("yarn install")
        nodejs.npm("run build")
    }

    fun configureKotlinCompiler(compiler : JkKotlinCompiler) {
        compiler
            .addPlugin("org.jetbrains.kotlin:kotlin-allopen")
            .addPluginOption("org.jetbrains.kotlin.allopen", "preset", "spring")
    }

}