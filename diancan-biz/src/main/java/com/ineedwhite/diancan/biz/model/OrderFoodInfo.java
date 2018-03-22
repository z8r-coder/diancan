package com.ineedwhite.diancan.biz.model;

/**
 * @author ruanxin
 * @create 2018-03-17
 * @desc 订单信息中的菜品信息
 */
public class OrderFoodInfo {
    private String foodName;
    private String foodNum;
    private String totalMoney;
    private String unitPrice;
    private String food_img;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodNum() {
        return foodNum;
    }

    public void setFoodNum(String foodNum) {
        this.foodNum = foodNum;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getFoodImg() {
        return food_img;
    }

    public void setFoodImg(String foodImg) {
        this.food_img = foodImg;
    }
}
