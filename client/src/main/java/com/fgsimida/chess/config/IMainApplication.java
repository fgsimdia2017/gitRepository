package com.fgsimida.chess.config;

import com.fgsimida.chess.util.Config;

import java.net.URL;

/**
 * Created by fgsimida on 2018/4/9.
 */
public abstract class IMainApplication {
    protected static void init(URL url) {
        //设置Logger 库
        System.setProperty("vertx.logger-delegate-factory-class-name",
                "io.vertx.core.logging.SLF4JLogDelegateFactory");
        Config.systenPath=url.getPath().substring(0,url.getPath().lastIndexOf("/"));
        System.setProperty(Config.APP_ROOT,Config.systenPath);
        Config.CPU_NUM=Runtime.getRuntime().availableProcessors();
        String configPath=Config.systenPath.concat("/config");
        Config.loadConfig(configPath);
    }
}
