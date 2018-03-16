package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.RechargeDo;
import org.apache.ibatis.annotations.Param;

/**
 * @author ruanxin
 * @create 2018-03-16
 * @desc
 */
public interface RechargeDao {
    /**
     * 插入充值记录　
     * @param rechargeDo
     */
    public void insertRecharge(@Param("recharge") RechargeDo rechargeDo);
}
