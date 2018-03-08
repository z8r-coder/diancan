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
    private Timestamp order_date;
    private Date order_board_date;
    private String order_board_time_interval;
    private Integer order_total_amount;
    private Integer order_status;

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

    public Timestamp getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Timestamp order_date) {
        this.order_date = order_date;
    }

    public Date getOrder_board_date() {
        return order_board_date;
    }

    public void setOrder_board_date(Date order_board_date) {
        this.order_board_date = order_board_date;
    }

    public String getOrder_board_time_interval() {
        return order_board_time_interval;
    }

    public void setOrder_board_time_interval(String order_board_time_interval) {
        this.order_board_time_interval = order_board_time_interval;
    }

    public Integer getOrder_total_amount() {
        return order_total_amount;
    }

    public void setOrder_total_amount(Integer order_total_amount) {
        this.order_total_amount = order_total_amount;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }
}
