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
}
