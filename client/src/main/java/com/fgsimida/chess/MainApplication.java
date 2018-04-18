package com.fgsimida.chess;
import com.fgsimida.chess.config.IMainApplication;
import com.fgsimida.chess.launch.ServiceLaucher;

/**
 * Created by fgsimida on 2018/4/9.
 */
public class MainApplication extends IMainApplication {
    public static void main(String[] args) {
        init(MainApplication.class.getProtectionDomain().getCodeSource().getLocation());
        ServiceLaucher.clusterVertx();
    }
}
