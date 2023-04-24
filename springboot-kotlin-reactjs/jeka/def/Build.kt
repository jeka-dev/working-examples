import com.fkorotkov.kubernetes.*
import com.fkorotkov.kubernetes.apps.newDeployment
import com.google.cloud.tools.jib.api.*
import dev.jeka.core.api.file.JkPathTree
import dev.jeka.core.api.kotlin.JkKotlinCompiler
import dev.jeka.core.api.project.JkProject
import dev.jeka.core.tool.JkBean
import dev.jeka.core.tool.JkDoc
import dev.jeka.core.tool.JkInjectClasspath
import dev.jeka.plugins.kotlin.KotlinJvmJkBean
import dev.jeka.plugins.nodejs.NodeJsJkBean
import dev.jeka.plugins.springboot.SpringbootJkBean
import io.fabric8.kubernetes.api.model.Container
import io.fabric8.kubernetes.api.model.ContainerBuilder
import io.fabric8.kubernetes.api.model.ContainerFluent
import io.fabric8.kubernetes.api.model.IntOrString
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder
import io.fabric8.kubernetes.client.KubernetesClientBuilder

@JkInjectClasspath("com.google.cloud.tools:jib-core:0.23.0")
@JkInjectClasspath("io.fabric8:kubernetes-client:6.5.1")
@JkInjectClasspath("io.fabric8:kubernetes-client-api:6.5.1")
@JkInjectClasspath("io.fabric8:kubernetes-httpclient-jdk:6.5.1")
@JkInjectClasspath("io.fabric8:kubernetes-model:6.5.1")
@JkInjectClasspath("com.github.fkorotkov:k8s-kotlin-dsl:3.2.0")
class Build : JkBean() {

    @JkDoc("If true, Springboot jar will embed the client application")
    var packClient = false

    val springboot = getBean(SpringbootJkBean::class.java)

    val nodejs = getBean(NodeJsJkBean::class.java)

    val kotlin = getBean(KotlinJvmJkBean::class.java)

    private val imagePath = outputDir.resolve("image.tar");

    private val imageRepository = "myhub/coffeeshop"

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
        val serverStatic = project.compilation.layout.classDirPath.resolve("static")
        project.compilation.postCompileActions.append("client pack") {
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

    fun makeImage() {
        val project = springboot.projectBean.project;

        //val image = DockerDaemonImage.named("my-jib-image");
        val image: TarImage = TarImage.at(imagePath).named(imageRepository)
        val containerizer = Containerizer.to(image).addEventHandler(LogEvent::class.java)
            { logEvent: LogEvent ->
                System.out.println(logEvent.level.toString() + ": " + logEvent.message)
            }
        Jib.from("openjdk:17")
            .addLayer(project.packaging.resolveRuntimeDependencies().files.entries, "/app/libs")
            .addLayer(listOf(project.compilation.layout.resolveClassDir()), "/app")
            .setEntrypoint("java", "-cp", "/app/classes:/app/libs/*", "hellp.Application")
            .containerize(containerizer)
    }

    fun pushImage() {
        val tarImage = TarImage.at(imagePath);
        val dockerDaemonImage = DockerDaemonImage.named(imageRepository);
        Jib.from(tarImage)
            .containerize(Containerizer.to(dockerDaemonImage));

    }

    // see https://github.com/fabric8io/kubernetes-client
    // https://learnk8s.io/spring-boot-kubernetes-guide
    // https://github.com/fabric8io/kubernetes-client/blob/master/doc/CHEATSHEET.md
    @Throws(Exception::class)
    fun applyKube() {
        val deployment1 = DeploymentBuilder()
            .withNewMetadata()
                .withName("deployment1")
                .addToLabels("app", "coffeeshop")
            .endMetadata()
            .withNewSpec()
                .withReplicas(1)
                .withNewTemplate()
                    .withNewMetadata()
                        .addToLabels("app", "coffeeshop").endMetadata()
                    .withNewSpec()
                        .addNewContainer()
                            .withName("app-coffeshop")
                            .withImage(imageRepository + ":latest")
                        .endContainer()
                    .endSpec()
                .endTemplate()
                .withNewSelector()
                    .addToMatchLabels("app", "coffeeshop")
                .endSelector()
            .endSpec()
            .build()
        println(deployment1)
        val client = KubernetesClientBuilder().build()
        client.apps().deployments().inNamespace("default").createOrReplace(deployment1)
    }

}