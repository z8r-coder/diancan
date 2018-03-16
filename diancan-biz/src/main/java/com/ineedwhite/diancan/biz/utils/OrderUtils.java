package com.ineedwhite.diancan.biz.utils;

import com.ineedwhite.diancan.common.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author ruanxin
 * @create 2018-03-10
 * @desc 订单方法
 */
public class OrderUtils {

    private static final Logger logger = Logger.getLogger(OrderUtils.class);

    /**
     * 订单缓存 prefix
     */
    private static final String ORD_FUNC_NAME = "RS_ORDER_";

    /**
     * 订单菜品ID列表
     */
    private static final String ORDER_FOOD_ID = "ORDER_FOOD_ID_";

    /**
     * 订单菜品数量列表
     */
    private static final String ORDER_FOOD_NUM = "ORDER_FOOD_NUM_";

    /**
     * 默认订单过期时间
     */
    private static final int DEFAULT_EXP_TIME = 24 * 60 * 60;

    public static void deleteCacheFoodList(String orderId) throws Exception {
        if (StringUtils.isEmpty(orderId)) {
            return;
        }
        String fieldKey = makeFoodIdKey(orderId);
        logger.info("delete food list key:" + fieldKey);
        RedisUtil.delete(fieldKey);
    }

    public static void addFoodIdList(String orderId, String foodId) throws Exception {
        if (StringUtils.isEmpty(orderId)) {
            return;
        }
        String fieldKey = makeFoodIdKey(orderId);
        logger.info("food id list key:" + fieldKey);
        RedisUtil.addIntoList(fieldKey, foodId);
    }

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

    public static String getFoodNumCache(String orderId, String foodId) throws Exception {
        if (StringUtils.isEmpty(foodId) || StringUtils.isEmpty(orderId)) {
            return null;
        }
        String fieldKey = makeHashFoodNumKey(orderId, foodId);
        return RedisUtil.getStr(fieldKey);
    }

    public static void setFoodNumCache(String orderId, String foodId, String foodNum) throws Exception {
        if (StringUtils.isEmpty(foodId)) {
            return;
        }
        String fieldKey = makeHashFoodNumKey(orderId, foodId);
        RedisUtil.setWithExpire(fieldKey, foodNum, DEFAULT_EXP_TIME);
    }

    public static Long getOrdFoodListLen(String orderId) throws Exception {
        if (StringUtils.isEmpty(orderId)) {
            return Long.valueOf(0);
        }
        String fieldKey = makeFoodIdKey(orderId);
        return RedisUtil.getListLength(fieldKey);
    }

    public static void addFoodNumList(String orderId, String foodNum) throws Exception{
        if (StringUtils.isEmpty(orderId)){
            return;
        }
        String fieldKey = makeFoodNumKey(orderId);
        logger.info("food num list key:" + fieldKey);
        RedisUtil.addIntoList(fieldKey, foodNum);
    }

    public static List<String> getFoodNumList(String orderId) throws Exception {
        if (StringUtils.isEmpty(orderId)) {
            return Collections.emptyList();
        }
        String fieldKey = makeFoodNumKey(orderId);
        return RedisUtil.getAllList(fieldKey);
    }

    public static List<String> getFoodIdList(String orderId) throws Exception {
        if (StringUtils.isEmpty(orderId)) {
            return Collections.emptyList();
        }
        String fieldKey = makeFoodIdKey(orderId);
        return RedisUtil.getAllList(fieldKey);
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

    private static String makeHashFoodNumKey(String orderId, String foodId) {
        return ORDER_FOOD_NUM + orderId + "_" + foodId;
    }
}
