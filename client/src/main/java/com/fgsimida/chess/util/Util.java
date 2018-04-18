package com.fgsimida.chess.util;


import com.fgsimida.chess.constant.Const;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.util.Collection;

/**
 * 结果返回
 */
public class Util {

    private static final JsonObject UserDump = new JsonObject().put(Const.retCode, -100)
            .put(Const.message, "user dump");

    private static final JsonObject WrongData = new JsonObject().put(Const.retCode, -200)
            .put(Const.message, "Wrong Request Data");

    private static final JsonObject OK = new JsonObject().put(Const.retCode, 0)
            .put(Const.message, "success");

    private static final JsonObject Fail = new JsonObject().put(Const.retCode, -1)
            .put(Const.message, "Failed");

    /***
     *  部分成功
     */
    private static final JsonObject Half_OK = new JsonObject().put(Const.retCode, 1)
            .put(Const.message, "half success");

    private static void common(JsonObject data, Object payload, RoutingContext rc) {
        if (rc != null) {
            JsonObject result = new JsonObject();
            result.put(Const.retCode, data.getInteger(Const.retCode)).put(Const.message, data.getString(Const.message)).put(Const.data, payload);
            rc.response().end(result.encode());
        }
    }

    public static JsonObject getOk() {
        return OK;
    }

    public static JsonObject getOkObject(Object obj) {
        JsonObject result = new JsonObject();
        result.put(Const.retCode, 0).put(Const.message, Const.success).put(Const.data, obj);
        return result;
    }

    public static JsonObject getFail() {
        return Fail;
    }

    public static void OK(Object payload, RoutingContext rc) {
        common(OK, payload, rc);
    }

    public static void OK(RoutingContext rc) {
        common(OK, null, rc);
    }

    public static void OKList(Collection payload, RoutingContext rc) {
        common(OK, new JsonObject().put(Const.size, payload.size()).put(Const.data, payload), rc);
    }

    public static void halfOK(RoutingContext rc) {
        common(Half_OK, null, rc);
    }

    public static void halfOK(String payload, RoutingContext rc) {
        common(Half_OK, payload, rc);
    }


    public static void FAIL(RoutingContext rc) {
        common(Fail, null, rc);
    }

    public static void FAIL(String errMsg, RoutingContext rc) {
        common(Fail, new JsonObject().put(Const.errMsg, errMsg), rc);
    }

    public static void FAIL(JsonObject data, RoutingContext rc) {
        common(Fail, data, rc);
    }

    public static void FAIL(Throwable err, RoutingContext rc) {
        common(Fail, new JsonObject().put(Const.errMsg, err instanceof
                NullPointerException ? "NULL" : err.getMessage()), rc);
    }

    public static void FAIL(JsonObject data, Throwable err, RoutingContext rc) {
        common(Fail, data.put(Const.errMsg, err instanceof
                NullPointerException ? "NULL" : err.getMessage()), rc);
    }


    public static void userDump(RoutingContext rc) {
        common(UserDump, null, rc);
    }

    public static void wrongData(RoutingContext rc) {
        common(WrongData, null, rc);
    }


}
