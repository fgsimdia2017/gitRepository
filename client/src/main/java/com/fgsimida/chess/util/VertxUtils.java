package com.fgsimida.chess.util;

import io.vertx.rxjava.core.Vertx;

/**
 * Created by fgsimida on 2018/4/10.
 */
public class VertxUtils {
    private static Vertx vertx;

    public static Vertx getVertx() {
        return vertx;
    }

    public static void setVertx(Vertx vertx) {
        VertxUtils.vertx = vertx;
    }
}
