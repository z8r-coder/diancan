package com.ineedwhite.diancan.dao.domain;


import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc
 */

public class OrderDo {
    private String order_id;
    private Integer board_id;
    private Integer order_people_number;
    private String order_date;
    private String order_board_date;
    private String order_board_time_interval;
    private float order_total_amount;
    private String order_status;
    private String user_id;
    private String coupon_id;
    private String order_food;
    private String order_food_num;
    private float order_paid;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Integer getBoard_id() {
        return board_id;
    }

    public void setBoard_id(Integer board_id) {
        this.board_id = board_id;
    }

    public Integer getOrder_people_number() {
        return order_people_number;
    }

    public void setOrder_people_number(Integer order_people_number) {
        this.order_people_number = order_people_number;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_board_date() {
        return order_board_date;
    }

    public void setOrder_board_date(String order_board_date) {
        this.order_board_date = order_board_date;
    }

    public String getOrder_board_time_interval() {
        return order_board_time_interval;
    }

    public void setOrder_board_time_interval(String order_board_time_interval) {
        this.order_board_time_interval = order_board_time_interval;
    }

    public float getOrder_total_amount() {
        return order_total_amount;
    }

    public void setOrder_total_amount(float order_total_amount) {
        this.order_total_amount = order_total_amount;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getOrder_food() {
        return order_food;
    }

    public void setOrder_food(String order_food) {
        this.order_food = order_food;
    }

    public String getOrder_food_num() {
        return order_food_num;
    }

    public void setOrder_food_num(String order_food_num) {
        this.order_food_num = order_food_num;
    }

    public float getOrder_paid() {
        return order_paid;
    }

    public void setOrder_paid(float order_paid) {
        this.order_paid = order_paid;
    }
}
