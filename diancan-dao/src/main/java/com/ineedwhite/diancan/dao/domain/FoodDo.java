package com.ineedwhite.diancan.dao.domain;


/**
 * @author ruanxin
 * @create 2018-03-10
 * @desc 菜品
 */
public class FoodDo {
    private Integer food_id;
    private String food_name;
    private Integer food_type_id;
    private float food_price;
    private String food_remark;
    private Integer food_grounding;
    private Integer food_monthlysales;
    private float food_vip_price;
    private String food_img;

    public Integer getFood_id() {
        return food_id;
    }

    public void setFood_id(Integer food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public Integer getFood_type_id() {
        return food_type_id;
    }

    public void setFood_type_id(Integer food_type_id) {
        this.food_type_id = food_type_id;
    }

    public float getFood_price() {
        return food_price;
    }

    public void setFood_price(float food_price) {
        this.food_price = food_price;
    }

    public String getFood_remark() {
        return food_remark;
    }

    public void setFood_remark(String food_remark) {
        this.food_remark = food_remark;
    }

    public Integer getFood_grounding() {
        return food_grounding;
    }

    public void setFood_grounding(Integer food_grounding) {
        this.food_grounding = food_grounding;
    }

    public Integer getFood_monthlysales() {
        return food_monthlysales;
    }

    public void setFood_monthlysales(Integer food_monthlysales) {
        this.food_monthlysales = food_monthlysales;
    }

    public float getFood_vip_price() {
        return food_vip_price;
    }

    public void setFood_vip_price(float food_vip_price) {
        this.food_vip_price = food_vip_price;
    }

    public String getFood_img() {
        return food_img;
    }

    public void setFood_img(String food_img) {
        this.food_img = food_img;
    }
}
