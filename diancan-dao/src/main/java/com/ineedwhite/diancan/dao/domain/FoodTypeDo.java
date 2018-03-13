package com.ineedwhite.diancan.dao.domain;


/**
 * @author ruanxin
 * @create 2018-03-09
 * @desc 菜系类型,可配置
 */

public class FoodTypeDo {
    private Integer foodtype_id;
    private String foodtype_name;

    public Integer getFoodtype_id() {
        return foodtype_id;
    }

    public void setFoodtype_id(Integer foodtype_id) {
        this.foodtype_id = foodtype_id;
    }

    public String getFoodtype_name() {
        return foodtype_name;
    }

    public void setFoodtype_name(String foodtype_name) {
        this.foodtype_name = foodtype_name;
    }
}
