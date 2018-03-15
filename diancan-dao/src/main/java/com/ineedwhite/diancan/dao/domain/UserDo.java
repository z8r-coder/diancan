package com.ineedwhite.diancan.dao.domain;


/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */

public class UserDo {
    private String user_id;
    private String user_name;
    private String user_phone;
    private Integer accumulate_points;
    private float balance;
    private String member_level;
    private Integer user_is_del;
    private String user_password;
    private String user_coupon;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public Integer getAccumulate_points() {
        return accumulate_points;
    }

    public void setAccumulate_points(Integer accumulate_points) {
        this.accumulate_points = accumulate_points;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getMember_level() {
        return member_level;
    }

    public void setMember_level(String member_level) {
        this.member_level = member_level;
    }

    public Integer getUser_is_del() {
        return user_is_del;
    }

    public void setUser_is_del(Integer user_is_del) {
        this.user_is_del = user_is_del;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_coupon() {
        return user_coupon;
    }

    public void setUser_coupon(String user_coupon) {
        this.user_coupon = user_coupon;
    }
}
