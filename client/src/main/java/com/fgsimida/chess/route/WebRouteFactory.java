package com.fgsimida.chess.route;

import com.fgsimida.chess.util.ClassUtil;
import io.vertx.rxjava.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.Set;

/**
 * Created by fgsimida on 2018/4/10.
 */
public class WebRouteFactory {
    private static final Logger logger = LoggerFactory.getLogger(WebRouteFactory.class);

    public static void buildRoute(final Router router) {
        Set<Class<?>> classSet = ClassUtil.findClassByIntefaceAndPackage("com.fgsimida.chess.route", Route.class);
        Observable.from(classSet)
                .map(sClass -> {
                    Route it = null;
                    try {
                        it = (Route) sClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return it;
                }).sorted((route1, route2) -> Integer.compare(route1.getOrder(), route2.getOrder())).subscribe(route ->
                route.route(router));
    }
}
