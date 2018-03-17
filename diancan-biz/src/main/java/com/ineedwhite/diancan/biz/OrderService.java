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
     * 展示购物车页面
     * @param paraMap
     * @return
     * @throws Exception
     */
    Map<String, String> shoppingCart(Map<String, String> paraMap) throws Exception;

    /**
     * 获取优惠券列表
     * @param paraMap
     * @return
     * @throws Exception
     */
    Map<String, String> getCouponList(Map<String, String> paraMap) throws Exception;

    /**
     * 使用优惠券
     * @param paraMap
     * @return
     * @throws Exception
     */
    Map<String, String> useCoupon(Map<String, String> paraMap) throws Exception;

    /**
     * 购物车界面的对菜品的增减
     * @param paraMap
     * @return
     * @throws Exception
     */
    Map<String, String> shoppingCartAddMinus(Map<String, String> paraMap) throws Exception;

    /**
     * 结账
     * @param paraMap
     * @return
     * @throws Exception
     */
    Map<String, String> checkOut(Map<String, String> paraMap) throws Exception;

    /**
     * 历史订单
     * @param paraMap
     * @return
     * @throws Exception
     */
    Map<String, String> historyOrder(Map<String, String> paraMap) throws Exception;

    /**
     * 未完成的订单
     * @param paraMap
     * @return
     * @throws Exception
     */
    Map<String, String> orderWithoutFinish(Map<String, String> paraMap) throws Exception;

    /**
     * 订单信息
     * @param paraMap
     * @return
     * @throws Exception
     */
    Map<String, String> orderInfo(Map<String, String> paraMap) throws Exception;
}

