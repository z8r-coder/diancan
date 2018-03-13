package com.ineedwhite.diancan.dao.domain;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc
 */
@Data
public class OrderDo {
    private String order_id;
    private Integer board_id;
    private Integer order_people_number;
    private String order_date;
    private String order_board_date;
    private String order_board_time_interval;
    private Integer order_total_amount;
    private String order_status;
    private String user_id;
    private String coupon_id;
    private String order_food;
    private String order_food_num;

}
