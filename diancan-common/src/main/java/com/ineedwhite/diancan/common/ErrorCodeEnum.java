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
    DC00015("DC00015", "该订单无效")
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
