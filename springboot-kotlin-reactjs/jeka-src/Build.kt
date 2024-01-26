import dev.jeka.core.tool.KBean
import dev.jeka.plugins.kotlin.KotlinJvmKBean

/**
 * Customize parts that cannot be configured using properties
 */
class Build : KBean() {

    override fun init() {

        // configure kotlin compiler
        load(KotlinJvmKBean::class.java).kotlinJvm.kotlinCompiler
            .addPlugin("org.jetbrains.kotlin:kotlin-allopen")
            .addPluginOption("org.jetbrains.kotlin.allopen", "preset", "spring")
    }

}