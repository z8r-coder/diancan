package com.ineedwhite.diancan.biz;

import java.util.Map;

/**
 * 用户业务类
 */
public interface UserService {
    /**
     * 个人详细信息
     * @param paraMap
     * @return
     */
    public Map<String, String> getUserDetailInfo(Map<String, String> paraMap);
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

    /**
     * 更改用户信息
     * @param paraMap
     * @return
     */
    public Map<String, String> modifiedUserInfo(Map<String, String> paraMap);

    /**
     * 注销用户
     * @param paraMap
     * @return
     */
    public Map<String, String> cancellation(Map<String, String> paraMap) throws Exception;
}
