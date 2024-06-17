package _dev;

import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.KBean;
import dev.jeka.plugins.springboot.SpringbootKBean;

@JkInjectClasspath("dev.jeka:springboot-plugin")
class Build extends KBean {

    @Override
    protected void init() {
       load(SpringbootKBean.class);
    }
}
