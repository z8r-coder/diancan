package com.ineedwhite.diancan.biz.model;

/**
 * @author ruanxin
 * @create 2018-03-14
 * @desc 购物车中的food bean
 */
public class ShoppingCartFood {

    private String foodId;
    private String foodName;
    private float price;
    private float vipPrice;
    private int num;
    private float total;
    private float vipTotal;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public float getVipTotal() {
        return vipTotal;
    }

    public void setVipTotal(float vipTotal) {
        this.vipTotal = vipTotal;
    }

    public float getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(float vipPrice) {
        this.vipPrice = vipPrice;
    }
}
