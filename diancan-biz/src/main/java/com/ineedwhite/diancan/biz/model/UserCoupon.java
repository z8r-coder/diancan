package com.ineedwhite.diancan.biz.model;

/**
 * @author ruanxin
 * @create 2018-03-16
 * @desc 用户界面的优惠券
 */
public class UserCoupon {
    private String couponId;
    private String start_time;
    private String consu_amt;
    private String expire_time;
    private String discount;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getConsu_amt() {
        return consu_amt;
    }

    public void setConsu_amt(String consu_amt) {
        this.consu_amt = consu_amt;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
