package com.fgsimida.chess.verticle;

import com.fgsimida.chess.client.MongoClient;
import com.fgsimida.chess.constant.Const;
import com.fgsimida.chess.route.WebRouteFactory;
import com.fgsimida.chess.util.CacheServiceUtil;
import com.fgsimida.chess.util.Config;
import com.fgsimida.chess.util.Util;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.CorsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by fgsimida on 2018/4/10.
 */
//注解日志加入
public class WebServerVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(WebServerVerticle.class);

    @Override
    public void start() throws Exception {
       // startWebInteface();
        startRoute("*");
        // 初始化缓存对象
        CacheServiceUtil.initCacheObj();
    }

    /**
     * 用于 cors的拦截或者说配置,暂时不使用.
     */
    private void startWebInteface() {
        MongoClient.client.rxFindOne(Const.COL_Server, MongoClient.byId("1"), new JsonObject().put("accessDomain", 1))
                .subscribe
                (jsonObject -> {
                    if (jsonObject == null) {
                        logger.error("AccessDomain No Found");
                        startRoute("*");
                    } else {
                        JsonArray accessDomain = jsonObject.getJsonArray("accessDomain");
                        if (accessDomain == null || accessDomain.isEmpty()) {
                            logger.error("AccessDomain No Found");
                            startRoute("*");
                        } else {
                            StringBuilder sb = new StringBuilder();
                            accessDomain.forEach(s -> sb.append(s).append("|"));
                            sb.deleteCharAt(sb.lastIndexOf("|"));
                            logger.info("AccessDomain Found:{}", accessDomain);
                            startRoute(sb.toString());
                        }

                    }
                });
    }

    private void startRoute(String accessDomain) {
        Router router = Router.router(vertx);
        //CORS
        String allowMethod = Config.property("CORS_AllowMethod");
        String allowHeaders = Config.property("CORS_AllowHeaders");
        Integer maxAgeSecond = Integer.parseInt(Config.property("CORS_MaxAgeSecond"));
        CorsHandler corsHandler = CorsHandler.create(accessDomain)
                .allowedHeaders(new HashSet<>(Arrays.asList(allowHeaders.split(","))))
                .maxAgeSeconds(maxAgeSecond);
        Arrays.asList(allowMethod.split(",")).forEach(m -> corsHandler.allowedMethod(HttpMethod.valueOf(m)));
        router.route().handler(corsHandler);  //check cross domain request
          /*
        router.route("/crm/ap/info/edit")
                .handler(BodyHandler.create().setUploadsDirectory("file-uploads/logo").setBodyLimit(2 * 1048576L));
        router.route().handler(BodyHandler.create());
        router.route().handler(LoggerHandler.create(LoggerFormat.TINY));
        router.route().handler(CookieHandler.create());
        Create a clustered session store using defaults
        SessionStore store = ClusteredSessionStore.create(vertx);
        SessionHandler sessionHandler = SessionHandler.create(store);
        router.route("/crm/check/validCode").handler(sessionHandler);
        router.route("/crm/generation/validCode").handler(sessionHandler);
        router.route("/crm/pwdReset/validCode").handler(sessionHandler);
        router.route("/file-uploads/*").handler(StaticHandler.create("file-uploads"));
        router.route("/static/*").handler(StaticHandler.create("static"));
        router.route("/emailTempl/*").handler(StaticHandler.create("templ/emailTempl"));
        router.route("/crm/ap/img/upload").handler(BodyHandler.create().setBodyLimit(2 * 1048576L));
        */
        router.route().failureHandler(rc -> {
            Throwable err = rc.failure();
            if (err == null) {
                rc.response().setStatusCode(rc.statusCode());
                rc.response().end(rc.response().getStatusMessage());
            } else {
                logger.info("routing handler exception -> {}", err);
                Util.FAIL(err.getMessage() != null ? err.getMessage() : "Error happen", rc);
            }
        });
        WebRouteFactory.buildRoute(router);
        router.getRoutes().forEach(route -> logger.info(route.getPath()));
        vertx.createHttpServer().requestHandler(router::accept)
                .listen(Integer.parseInt(Config.property("server.port")));
    }
}
