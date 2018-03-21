package com.ineedwhite.diancan.common;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc 错误码
 */
public enum ErrorCodeEnum {
    DC00000("DC00000", "成功"),
    DC00001("DC00001", "缺少必要参数"),
    DC00002("DC00002", "操作数据库失败"),
    DC00003("DC00003", "系统错误"),
    DC00004("DC00004", "用户登陆密码错误"),
    DC00005("DC00005", "该手机号已注册"),
    DC00006("DC00006", "桌位号不存在"),
    DC00007("DC00007", "该手机号未注册"),
    DC00008("DC00008", "系统繁忙，请稍后再试"),
    DC00009("DC00009", "该桌位已被预定，请重新选择桌位"),
    DC00010("DC00010", "该用户已被注销或不存在,请联系管理员"),
    DC00011("DC00011", "已是最后一页"),
    DC00012("DC00012", "亲，菜品最多只能点100道哟!"),
    DC00013("DC00013", "该订单已过期，请重试"),
    DC00014("DC00014", "该用户没有可用的优惠券"),
    DC00015("DC00015", "该订单已经无效，请重新下单"),
    DC00016("DC00016", "卡券不存在"),
    DC00017("DC00017", "卡券已过期"),
    DC00018("DC00018", "金额未达到，不能使用优惠券"),
    DC00019("DC00019", "亲，您还没有点菜哟"),
    DC00020("DC00020", "支付失败,账户余额不足，请充值"),
    DC00021("DC00021", "手机号不正确，请重新试"),
    DC00022("DC00022", "该订单已支付成功，请重新下单"),
    DC00023("DC00023", "该订单不存在，请重新下单"),
    DC00024("DC00024", "您的账户最多只能存放100万"),
    DC00025("DC00025", "亲，您还有订单未支付，请及时支付"),
    DC00026("DC00026", "该订单未支付成功"),
    DC00027("DC00027", "该订单状态无效，请重试"),
    DC00028("DC00028", "该订单未选桌")
    ;

    /**
     * error code
     */
    private String code;

    /**
     * description
     */
    private String desc;

    ErrorCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
