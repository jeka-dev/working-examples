package _dev;

import dev.jeka.core.tool.JkDep;
import dev.jeka.core.tool.JkInjectClasspath;
import dev.jeka.core.tool.JkPostInit;
import dev.jeka.core.tool.KBean;
import dev.jeka.core.tool.builtins.base.BaseKBean;
import dev.jeka.plugins.springboot.SpringbootKBean;

@JkDep("dev.jeka:springboot-plugin")
class Build extends KBean {

    @JkPostInit(required = true)
    private void postInit(BaseKBean baseKBean) {}


}
