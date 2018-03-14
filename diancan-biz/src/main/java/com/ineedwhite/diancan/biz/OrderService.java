package com.ineedwhite.diancan.biz;

import java.util.Map;

/**
 * 订单业务
 */
public interface OrderService {
    /**
     * 添加菜品
     * @param paraMap
     * @return
     */
    Map<String, String> addFood(Map<String, String> paraMap) throws Exception;
}

