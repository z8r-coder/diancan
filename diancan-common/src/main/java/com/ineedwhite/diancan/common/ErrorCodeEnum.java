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
    DC00006("DC00006", "桌位号不存在")
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
