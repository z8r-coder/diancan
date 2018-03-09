package com.ineedwhite.diancan.biz;

import com.ineedwhite.diancan.dao.domain.BoardDo;
import com.ineedwhite.diancan.dao.domain.CouponDo;

import java.util.Map;

public interface DianCanConfig {

    /**
     * 从数据库缓存
     * @return
     */
    boolean refreshConfig();

    /**
     * 返回所有桌位缓存
     */
    Map<Integer,BoardDo> getAllBoard();

    /**
     * 通过桌位号获取桌位信息
     * @param boardId
     * @return
     */
    BoardDo getBoardById(Integer boardId);

    /**
     * 返回卡券缓存
     */
    Map<Integer, CouponDo> getAllCouponDo();

    /**
     * 通过卡券号获取卡券信息
     * @return
     */
    CouponDo getCouponById(Integer couponId);
}
