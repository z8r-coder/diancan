package com.ineedwhite.diancan.common.utils;

import com.ineedwhite.diancan.common.TypeCast;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author ruanxin
 * @create 2018-03-09
 * @desc 配置文件解析
 */
public class PropertiesUtil {

    private final static Logger logger = Logger.getLogger(PropertiesUtil.class);

    private final static Map<String, String> properCache = new HashMap<String, String>();

    private static Properties properties;
    static {

    }
    public static void loadPropertiesFromSrc() {
        InputStream in = null;

        try {
            in = PropertiesUtil.class.getClassLoader().getResourceAsStream("env.properties");
            if (in != null) {
                properties = new Properties();
                properties.load(in);
            }
            loadProperties(properties);
        } catch (Exception e) {
            logger.error("Read properties Error", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void loadProperties (Properties properties) {

        String redisIp = properties.getProperty("redis.ip");
        if (!StringUtils.isBlank(redisIp)) {
            properCache.put("redis.ip", redisIp.trim());
        }
        String redisPort = properties.getProperty("redis.port");
        if (!StringUtils.isBlank(redisPort)) {
            properCache.put("redis.port", redisPort);
        }
    }

    public static TypeCast getValue(String text){
        return new TypeCast(text);
    }

    public static String getStringValue (String text) {
        return null;
    }
}
