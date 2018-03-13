package com.ineedwhite.diancan.dao.domain;


/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc  餐桌实体类
 */
public class BoardDo {
    private Integer board_id;
    private String board_name;
    private Integer board_people_number;
    private Integer board_type;

    public Integer getBoard_id() {
        return board_id;
    }

    public void setBoard_id(Integer board_id) {
        this.board_id = board_id;
    }

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    public Integer getBoard_people_number() {
        return board_people_number;
    }

    public void setBoard_people_number(Integer board_people_number) {
        this.board_people_number = board_people_number;
    }

    public Integer getBoard_type() {
        return board_type;
    }

    public void setBoard_type(Integer board_type) {
        this.board_type = board_type;
    }
}
