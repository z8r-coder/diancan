package com.ineedwhite.diancan.dao.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author ruanxin
 * @create 2018-03-09
 * @desc 卡券 bean
 */
@Data
public class CouponDo {
    private Integer coupon_id;
    private Integer consumption_amount;
    private Integer discount;
    private Date expiry_time;

}
