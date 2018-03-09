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
        loadPropertiesFromSrc();
    }
    public static void loadPropertiesFromSrc() {
        InputStream in = null;

        try {
            in = PropertiesUtil.class.getClassLoader().getResourceAsStream("environment.properties");
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
            properCache.put("redis.port", redisPort.trim());
        }
        String testOnBorrow = properties.getProperty("cache.r.testonborrow");
        if (!StringUtils.isBlank(testOnBorrow)) {
            properCache.put("cache.r.testonborrow", testOnBorrow.trim());
        }
        String maxIdle = properties.getProperty("cache.r.maxidle");
        if (!StringUtils.isBlank(maxIdle)) {
            properCache.put("cache.r.maxidle", maxIdle.trim());
        }
        String minIdle = properties.getProperty("cache.r.minidle");
        if (!StringUtils.isBlank(minIdle)) {
            properCache.put("cache.r.minidle", minIdle.trim());
        }
        String idleTime = properties.getProperty("cache.r.softMinEvictableIdleTime");
        if (!StringUtils.isBlank(idleTime)) {
            properCache.put("cache.r.softMinEvictableIdleTime", idleTime.trim());
        }
        String maxTotal = properties.getProperty("cache.r.maxtotal");
        if (!StringUtils.isBlank(maxTotal)) {
            properCache.put("cache.r.maxtotal", maxTotal.trim());
        }
        String maxWaitMil = properties.getProperty("cache.r.maxwaitmillis");
        if (!StringUtils.isBlank(maxWaitMil)) {
            properCache.put("cache.r.maxwaitmillis", maxTotal.trim());
        }
    }

    public static TypeCast getValue(String key){
        String value = properCache.get(key);
        return new TypeCast(value);
    }

    public static String getStringValue (String key) {
        return properCache.get(key);
    }

    public static void main(String[] args) {
        String test = PropertiesUtil.getStringValue("cache.r.maxwaitmillis");
        System.out.println(test);
    }
}
