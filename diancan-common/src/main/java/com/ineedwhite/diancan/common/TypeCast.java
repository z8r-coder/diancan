package com.ineedwhite.diancan.common;

/**
 * @author ruanxin
 * @create 2018-03-09
 * @desc 类型装欢类
 */
public class TypeCast {
    private String message;

    public TypeCast(String message) {
        this.message = message;
    }

    public Integer toInt() {
        return Integer.parseInt(message);
    }
    public Boolean toBoolean() {
        return Boolean.parseBoolean(message);
    }
    public Long toLong() {
        return Long.parseLong(message);
    }
    public Double toDouble() {
        return Double.parseDouble(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
