package com.ineedwhite.diancan.common;

/**
 * @author ruanxin
 * @create 2018-03-17
 * @desc 0 男 1 女
 */
public enum GenderMapping {
    male("0", "男"),
    female("1","女")
    ;

    /**
     * 标识
     */
    private String gflag;

    /**
     * 描述
     */
    private String desc;

    GenderMapping(String gflag, String desc) {
        this.gflag = gflag;
        this.desc = desc;
    }

    public String getGflag() {
        return gflag;
    }

    public void setGflag(String gflag) {
        this.gflag = gflag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
