package com.ineedwhite.diancan.dao.domain;

import lombok.Data;

/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc  餐桌实体类
 */
@Data
public class BoardDo {
    private Integer board_id;
    private String board_name;
    private Integer board_people_number;
    private Integer board_type;
}
