package com.fgsimida.chess.route;

import com.fgsimida.chess.util.Util;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.Router;

/**
 * Created by fgsimida on 2018/4/10.
 */
public class TestRoute implements Route {
    @Override
    public Integer getOrder() {
        return 2;
    }

    @Override
    public void route(Router router) {
        router.get("/chess/test").handler(rc -> {
            Util.OK(new JsonObject().put("fgsimida","TEST"),rc);
        });
    }
}
