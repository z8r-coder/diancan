package com.ineedwhite.diancan.dao.domain;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */
public class UserDo {
    private Integer user_id;
    private String user_name;
    private Integer user_phone;
    private Integer accumulate_points;
    private Integer balance;
    private String member_level;
    private Integer user_is_del;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(Integer user_phone) {
        this.user_phone = user_phone;
    }

    public Integer getAccumulate_points() {
        return accumulate_points;
    }

    public void setAccumulate_points(Integer accumulate_points) {
        this.accumulate_points = accumulate_points;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
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
}
