package com.ineedwhite.diancan.dao.domain;

import lombok.Data;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */
@Data
public class UserDo {
    private String user_id;
    private String user_name;
    private String user_phone;
    private Integer accumulate_points;
    private Integer balance;
    private String member_level;
    private Integer user_is_del;
    private String user_password;
}
