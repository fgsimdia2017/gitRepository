package com.fgsimida.chess.util;

import com.fgsimida.chess.constant.Const;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.shareddata.AsyncMap;
import rx.Single;

/**
 * Created by fgsimida on 2018/4/10.
 */
public class CacheServiceUtil {
    public static final Logger logger = LoggerFactory.getLogger(CacheServiceUtil.class);
    private static AsyncMap<String, JsonObject> cacheObject;

    /**
     * 初始化缓存对象
     */
    public static void initCacheObj() {
        VertxUtils.getVertx().sharedData().<String, JsonObject>rxGetClusterWideMap(Const.cacheDefaultKey)
                .subscribe(asyncMap -> cacheObject = asyncMap);
    }


    /**
     * 放入缓存对象
     *
     * @param k
     * @param v
     */
    public static Single<Void> rxPutCacheObject(String k, JsonObject v) {
        return cacheObject.rxPut(k, v);
    }


    /**
     * 移除缓存对象
     *
     * @param k
     */
    public static Single<JsonObject> rxRemoveCacheObject(String k) {
        return cacheObject.rxRemove(k);
    }


    /**
     * 获取缓存对象
     *
     * @param k
     * @return
     */
    public static Single<JsonObject> rxGetCacheObject(String k) {
        return cacheObject.rxGet(k);
    }
}
