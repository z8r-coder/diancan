package com.ineedwhite.diancan.biz;

import java.util.Map;

/**
 * 订单业务
 */
public interface OrderService {
    /**
     * 添加菜品至购物车
     * @param paraMap
     * @return
     */
    Map<String, String> addFoodToShoppingCart(Map<String, String> paraMap) throws Exception;

    /**
     * 购物车页面刷新
     * @param paraMap
     * @return
     * @throws Exception
     */
    Map<String, String> shoppingCart(Map<String, String> paraMap) throws Exception;
}

