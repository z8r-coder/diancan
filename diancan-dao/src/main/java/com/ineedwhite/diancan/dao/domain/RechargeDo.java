package com.ineedwhite.diancan.dao.domain;

/**
 * @author ruanxin
 * @create 2018-03-16
 * @desc
 */
public class RechargeDo {
    private String recharge_id;
    private float recharge_amt;
    private String recharge_date;
    private String recharge_user;

    public String getRecharge_id() {
        return recharge_id;
    }

    public void setRecharge_id(String recharge_id) {
        this.recharge_id = recharge_id;
    }

    public float getRecharge_amt() {
        return recharge_amt;
    }

    public void setRecharge_amt(float recharge_amt) {
        this.recharge_amt = recharge_amt;
    }

    public String getRecharge_date() {
        return recharge_date;
    }

    public void setRecharge_date(String recharge_date) {
        this.recharge_date = recharge_date;
    }

    public String getRecharge_user() {
        return recharge_user;
    }

    public void setRecharge_user(String recharge_user) {
        this.recharge_user = recharge_user;
    }
}
