package com.ineedwhite.diancan.common;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc 会员等级映射
 */
public enum LevelMappingEnum {
    NVIP("0", "非VIP账户"),
    VIP("1", "VIP账户");

    /**
     * 数据库标识
     */
    private String vflag;
    private String desc;


    LevelMappingEnum(String vflag, String desc) {
        this.vflag = vflag;
        this.desc = desc;
    }

    public String getVflag() {
        return vflag;
    }

    public void setVflag(String vflag) {
        this.vflag = vflag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
