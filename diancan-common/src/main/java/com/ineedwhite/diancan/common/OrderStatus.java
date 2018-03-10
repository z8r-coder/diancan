package com.ineedwhite.diancan.common;

/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc 订单状态
 */
public enum OrderStatus {
    UK("UK","订单初始化"),
    UM("UM", "更新了菜单"),
    UD("UD", "结账，支付成功"),
    UR("UR", "退款"),
    UC("UC", "超时关单"),
    UF("UF", "支付失败")
    ;

    /**
     * 订单管理
     */
    private String orderStatus;

    /**
     * 描述
     */
    private String desc;

    OrderStatus(String orderStatus, String desc) {
        this.orderStatus = orderStatus;
        this.desc = desc;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
