package com.ineedwhite.diancan.biz.utils;

import com.ineedwhite.diancan.common.utils.RedisUtil;

/**
 * @author ruanxin
 * @create 2018-03-10
 * @desc 订单方法
 */
public class OrderUtils {
    /**
     * 订单缓存 prefix
     */
    private static final String ORD_FUNC_NAME = "RS_ORDER_";

    /**
     * 默认订单过期时间
     */
    private static final int DEFAULT_EXP_TIME = 60 * 60;

    /**
     * 缓存订单号
     * @param orderId
     * @param expire
     * @throws Exception
     */
    public static void addCacheOrder(String orderId, int expire) throws Exception {
        String fieldKey = makeKey(orderId);
        if (RedisUtil.getStr(fieldKey) == null) {
            RedisUtil.setWithExpire(fieldKey, "0", expire);
        }
    }

    /**
     * 订单是否存在
     * @param orderId
     * @return
     * @throws Exception
     */
    public static boolean getCacheOrder(String orderId) throws Exception {
        String fieldKey = makeKey(orderId);
        if (RedisUtil.getStr(fieldKey) != null) {
            //存在
            return true;
        }
        return false;
    }
    public static void addCacheOrder(String orderId) throws Exception {
        addCacheOrder(orderId, DEFAULT_EXP_TIME);
    }

    private static String makeKey(String orderId) {
        return ORD_FUNC_NAME + orderId;
    }
}
