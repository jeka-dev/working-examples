import dev.jeka.core.tool.JkPostInit
import dev.jeka.core.tool.KBean
import dev.jeka.plugins.kotlin.KotlinJvmKBean

/**
 * Customize parts that cannot be configured using properties
 */
class Build : KBean() {

    @JkPostInit
    private fun postInit(kbean: dev.jeka.plugins.kotlin.KotlinJvmKBean) {
        kbean.kotlinJvm.kotlinCompiler
            .addPlugin("org.jetbrains.kotlin:kotlin-allopen")
            .addPluginOption("org.jetbrains.kotlin.allopen", "preset", "spring")
    }

}