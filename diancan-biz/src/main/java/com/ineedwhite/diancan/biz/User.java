package com.ineedwhite.diancan.biz;

import java.util.Map;

/**
 * 用户业务类
 */
public interface User {
    /**
     * 用户注册
     * @param paraMap
     */
    public Map<String, String> register(Map<String, String> paraMap);

    public Map<String, String> login(Map<String, String> paraMap);
}
