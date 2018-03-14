package com.ineedwhite.diancan.biz.utils;

import com.ineedwhite.diancan.common.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;

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
     * 订单菜品ID列表
     */
    private static final String ORDER_FOOD_ID = "ORDER_FOOD_ID";

    /**
     * 订单菜品数量列表
     */
    private static final String ORDER_FOOD_NUM = "ORDER_FOOD_NUM";

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

    public static Long getOrdFoodListLen(String orderId) throws Exception {
        if (StringUtils.isEmpty(orderId)) {
            return Long.valueOf(0);
        }
        String fieldKey = makeFoodIdKey(orderId);
        return RedisUtil.getListLength(fieldKey);
    }

    public static void addFoodIdList(String orderId, String foodId) throws Exception {
        if (StringUtils.isEmpty(orderId)) {
            return;
        }
        String fieldKey = makeFoodIdKey(orderId);
        RedisUtil.addIntoList(fieldKey, foodId);
    }

    public static void addFoodNumList(String orderId, String foodNum) throws Exception{
        if (StringUtils.isEmpty(orderId)){
            return;
        }
        String fieldKey = makeFoodNumKey(orderId);
        RedisUtil.addIntoList(fieldKey, foodNum);
    }

    private static String makeKey(String orderId) {
        return ORD_FUNC_NAME + orderId;
    }

    private static String makeFoodIdKey(String orderId) {
        return ORDER_FOOD_ID + orderId;
    }

    private static String makeFoodNumKey(String orderId) {
        return ORDER_FOOD_NUM + orderId;
    }
}
