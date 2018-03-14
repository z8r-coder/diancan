package com.ineedwhite.diancan.biz;

import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-10
 * @desc
 */
public interface FoodService {
    /**
     * 通过菜系类型来查找菜品
     * @param paraMap
     * @return
     */
    Map<String, String> getFoodByType(Map<String, String> paraMap) throws Exception;
}
