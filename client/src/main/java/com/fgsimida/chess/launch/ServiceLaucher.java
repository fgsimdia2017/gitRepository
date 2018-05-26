package com.fgsimida.chess.launch;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fgsimida.chess.util.Config;
import com.fgsimida.chess.util.VertxUtils;
import com.fgsimida.chess.verticle.WebServerVerticle;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.rxjava.core.Vertx;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fgsimida on 2018/4/10.
 */
public class ServiceLaucher {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLaucher.class);
    private static Map<String, String> deployMap = new HashMap<>();//用于存储部署的verticle信息

    public static Map<String, String> getDeployMap() {
        return deployMap;
    }

    public static void setDeployMap(Map<String, String> deployMap) {
        ServiceLaucher.deployMap = deployMap;
    }

    /**
     * 启动 vertx默认的hazelcast集群
     * */
    public static void clusterVertx() {
        System.setProperty("hazelcast.logging.type", "slf4j");
        System.setProperty("hazelcast.heartbeat.interval.seconds", "15");
        com.hazelcast.config.Config config = new com.hazelcast.config.Config();
        config.setNetworkConfig(new NetworkConfig()
                .setPort(Integer.parseInt(Config.property("cluster.port")))
                .setJoin(new JoinConfig()
                        .setMulticastConfig(new MulticastConfig()
                                .setEnabled(false)).setTcpIpConfig(new TcpIpConfig()
                                .setEnabled(true)
                                .setMembers(Arrays.asList(Config.property("cluster.server.ip").split(","))))));
        VertxOptions options = new VertxOptions().setClusterManager(new HazelcastClusterManager(config));
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                startServer(vertx);
            }else logger.error("HazelcastClusterManager part start fail!");
        });
    }

    /**
     * 启动vertx的verticle,初始化db,client等
     *
     * @param vertx
     */
    private static void startServer(final Vertx vertx) {
        Json.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        VertxUtils.setVertx(vertx);
        deployVerticle(vertx);

    }

    /**
     * 部署verticle
     * @param vertx
     */
    private static void deployVerticle(final Vertx vertx) {
        System.out.println("deploy the verticle start->{}");
        vertx.rxDeployVerticle(WebServerVerticle.class.getCanonicalName()).subscribe(id -> deployMap
                .put("WebServerVerticle", id));
    }
}
