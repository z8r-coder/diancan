package com.ineedwhite.diancan.common;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc 错误码
 */
public enum ErrorCodeEnum {
    DC00000("DC00000", "成功"),
    DC00001("DC00001", "缺少必要参数")
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
