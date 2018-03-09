package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.CouponDo;

import java.util.List;

public interface CouponDao {
    /**
     * 查找所有的卡券信息
     * @return
     */
    List<CouponDo> findAllCoupon();
}
