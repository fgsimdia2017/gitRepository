package com.fgsimida.chess;
import com.fgsimida.chess.config.IMainApplication;
import com.fgsimida.chess.launch.ServiceLaucher;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fgsimida on 2018/4/9.
 */
public class MainApplication extends IMainApplication {
    public static void main(String[] args) {
        init(MainApplication.class.getProtectionDomain().getCodeSource().getLocation());
        ServiceLaucher.clusterVertx();
    }
}
