package com.ineedwhite.diancan.dao.domain;

import java.util.Date;

/**
 * @author ruanxin
 * @create 2018-03-09
 * @desc 卡券 bean
 */
public class CouponDo {
    private Integer coupon_id;
    private Integer consumption_amount;
    private Integer discount;
    private Date expiry_time;

    public Integer getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Integer coupon_id) {
        this.coupon_id = coupon_id;
    }

    public Integer getConsumption_amount() {
        return consumption_amount;
    }

    public void setConsumption_amount(Integer consumption_amount) {
        this.consumption_amount = consumption_amount;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Date getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(Date expiry_time) {
        this.expiry_time = expiry_time;
    }
}
