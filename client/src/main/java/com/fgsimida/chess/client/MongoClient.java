package com.fgsimida.chess.client;

import com.fgsimida.chess.util.Config;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import rx.Single;

/**
 * Created by fgsimida on 2018/4/10.
 */
public class MongoClient {
    public static io.vertx.rxjava.ext.mongo.MongoClient client;


    public static void init(Vertx vertx) {
        client = io.vertx.rxjava.ext.mongo.MongoClient.createShared(vertx,
                new JsonObject().put("connection_string",
                        Config.property("mongo.uri")));
    }

    public static JsonObject byId(String id) {
        return new JsonObject().put("_id", id);
    }

    /**
     * 获取总数量
     *@param collection
     * @param query
     * @return
     */
    public static Single<Integer> rxGetTotalNumber(String collection, JsonObject query) {
        return MongoClient.client.rxCount(collection, query).map(count -> count.intValue());
    }

    /**
     * 统计总数量
     * @param collection
     * @param query
     * @return
     */
    public static Single<Long> rxGetTotalCount(String collection, JsonObject query) {
        return MongoClient.client.rxCount(collection, query) ;
    }
}
