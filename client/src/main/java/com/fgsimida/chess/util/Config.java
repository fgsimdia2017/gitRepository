package com.fgsimida.chess.util;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by fgsimida on 2018/4/10.
 */
public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    public static String systenPath;
    public static final String APP_ROOT="system.rootPath";
    public static int CPU_NUM;

    private static final Properties properties = new Properties();

    private static Map<String, Object> configMap ;


    public static void loadConfig(final String configPath){
        String configFile=configPath.concat("/config.properties");
        try {
            logger.info("start load the properties file->{}");
            Config.init(configFile);
            initConfigMap(properties);
        } catch (IOException e) {
            System.exit(-1);
        }
        String log4jConfigFile=configPath.concat("/log4j.properties");
        PropertyConfigurator.configure(log4jConfigFile);
        Logger logger = LoggerFactory.getLogger(Config.class);
        logger.info("[{}]Finish init Config","sys");
    }

    private static void initConfigMap(Properties properties) {
        Map<String, Object> map = new HashMap<>();
        properties.forEach((key, value) -> {
            if(key instanceof String) {
                map.put((String)key, value) ;
            } else {
                System.out.println("key is not instanceof String...");
            }
            configMap = Collections.unmodifiableMap(map);
        });
    }


    private static void init(String path) throws IOException {
        properties.load(new InputStreamReader(new FileInputStream(new File
                (path)),"UTF-8"));
    }

    /**
     * 返回properties， 配置信息的内容
     * @return
     */
    public static Map<String, Object> getConfig() {
        return configMap ;
    }

    /**
     * 获取配置信息的内容
     * @param key
     * @return
     */
    public static String get(String key) {
        Object value = configMap.get(key);
        if(value instanceof String) return (String) value;
        return null ;
    }

    /**
     * 获取配置信息的内容， null 返回 defValue
     * @param key
     * @param defValue
     * @return
     */
    public static String getOrDef(String key, String defValue) {
        String value = get(key);
        return value != null ? value: defValue ;
    }



    public static String property(String... key) {
        String v = properties.getProperty(key[0]);
        if(key.length>1){
            String vf = key[1] == null ? null : key[1];
            return v == null ? vf : v;
        }else{
            return v;
        }
    }


    public static String property(String key,String defaultValue) {
        return properties.getProperty(key,defaultValue);
    }
}
