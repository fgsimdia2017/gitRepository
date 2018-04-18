package com.fgsimida.chess.route;

import io.vertx.rxjava.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fgsimida on 2018/4/10.
 */
public interface Route {
    Logger logger= LoggerFactory.getLogger(Route.class);
    /**
     * 路由顺序，Web根据路由顺序加载路由表
     * @return
     */
    Integer getOrder();

    /**
     * 通过webRouteFactory类扫描所有的包,将router实例传入到该route的接口中,方便实现类的使用
     * @param router
     */
    void route(Router router);
}
