package com.ineedwhite.diancan.biz;

import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-16
 * @desc 充值业务
 */
public interface RechargeService {
    /**
     * 充值业务
     * @param paraMap
     * @return
     */
    public Map<String, String> recharge(Map<String, String> paraMap);

    /**
     * 充值业务页面加载
     * @param paraMap
     * @return
     */
    public Map<String, String> rechargePageLoading(Map<String, String> paraMap);
}
