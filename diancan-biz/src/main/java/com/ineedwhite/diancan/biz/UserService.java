package com.ineedwhite.diancan.biz;

import java.util.Map;

/**
 * 用户业务类
 */
public interface UserService {
    /**
     * 用户注册
     * @param paraMap
     */
    public Map<String, String> register(Map<String, String> paraMap);

    /**
     * 用户登陆
     * @param paraMap
     * @return
     */
    public Map<String, String> login(Map<String, String> paraMap);

    /**
     * 用户信息
     * @param paraMap
     * @return
     */
    public Map<String, String> userInfo(Map<String, String> paraMap);

    /**
     * 获取用户的优惠券列表
     * @param paraMap
     * @return
     */
    public Map<String, String> getUserCoupon (Map<String, String> paraMap);
}
