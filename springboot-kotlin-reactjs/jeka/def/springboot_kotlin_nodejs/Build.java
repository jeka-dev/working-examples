package springboot_kotlin_nodejs;

import dev.jeka.core.api.depmanagement.JkRepoProperties;
import dev.jeka.core.api.file.JkPathTree;
import dev.jeka.core.api.project.JkProject;
import dev.jeka.core.api.system.JkLog;
import dev.jeka.core.api.system.JkProperties;
import dev.jeka.core.tool.JkBean;
import dev.jeka.core.tool.JkDoc;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.plugins.nodejs.NodeJsJkBean;
import dev.jeka.plugins.springboot.SpringbootJkBean;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

@JkInjectClasspath("dev.jeka:springboot-plugin")
@JkInjectClasspath("dev.jeka:nodejs-plugin")
class Build extends JkBean {

    @JkDoc("If true, Springboot jar will embed the client application")
    public boolean packClient = true;

    final SpringbootJkBean springbootBean = getBean(SpringbootJkBean.class);

    final NodeJsJkBean nodeJsBean = getBean(NodeJsJkBean.class);

    public void browse() throws IOException {
        Desktop.getDesktop().browse(URI.create("http://localhost:8080"));
    }


    Build() {
        springbootBean.setSpringbootVersion("2.7.4");
        springbootBean.projectBean().configure(this::configure);
        nodeJsBean.setWorkingDir("client");
        nodeJsBean.version = "18.12.0";
    }

    private void configure(JkProject project) {

        // we don't need java src here
        project.getCompilation().getLayout().emptySources();
        project.getTesting().getCompilation().getLayout().emptySources();

        // Configure project to compile Kotlin
        JkProperties props = this.getRuntime().getProperties();
        KotlinProjectConfigurator configurator = new KotlinProjectConfigurator();
        configurator.downloadRepos = JkRepoProperties.of(props).getDownloadRepos();
        configurator.kotlinVersion = props.get("jeka.kotlin.version");
        configurator.configure(project);

        // Configure project for nodeJs
        if (!packClient) {
            return;
        }
        JkPathTree clientPathTree = JkPathTree.of(getBaseDir().resolve("client/build"));
        project.getCompilation().getPostCompileActions().append("client pack", () -> {
            nodeJsBean.npm("run build");
            clientPathTree.copyTo(
                    project.getCompilation().getLayout().getClassDirPath().resolve("static"));
        });
        project.getCleanExtraActions().append(() -> {
            JkLog.info("Clean dir " + clientPathTree.getRoot());
            clientPathTree.deleteContent();
        });
    }


}