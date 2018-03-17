package com.ineedwhite.diancan.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc 预定餐桌时间段映射
 */
public enum TimeMapping {
    morning("0", "morning", "上午", "9:00", "12:00"),
    noon("1", "noon", "中午", "13:00", "16:00"),
    dinner("2", "dinner" ,"晚上", "17:00", "22:00"),
    ;

    /**
     * 时间映射
     */
    public static Map<String, TimeMapping> timeMappingMap = new HashMap<String, TimeMapping>();
    /**
     * 入库标识
     */
    private String tflag;
    /**
     * 时间段
     */
    private String time;
    /**
     * 描述
     */
    private String desc;
    /**
     * 开始
     */
    private String begin;
    /**
     * 结束
     */
    private String end;

    TimeMapping(String tflag, String time, String desc, String begin, String end) {
        this.tflag = tflag;
        this.time = time;
        this.desc = desc;
        this.begin = begin;
        this.end = end;
    }
    static {
        //初始化映射表
        for (TimeMapping timeMapping : TimeMapping.values()) {
            timeMappingMap.put(timeMapping.getTflag(), timeMapping);
        }
    }

    public String getTflag() {
        return tflag;
    }

    public void setTflag(String tflag) {
        this.tflag = tflag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
