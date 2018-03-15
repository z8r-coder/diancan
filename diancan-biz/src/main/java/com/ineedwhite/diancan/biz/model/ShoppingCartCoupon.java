package com.ineedwhite.diancan.biz.model;

/**
 * @author ruanxin
 * @create 2018-03-15
 * @desc 购物车中的优惠券
 */
public class ShoppingCartCoupon {
    private String couponId;
    private String remark;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
