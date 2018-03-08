package com.ineedwhite.diancan.common;

/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc 桌子类型映射
 */
public enum BoardTypMapping {
    WR("2", "西餐厅"),
    CR("1", "中餐厅"),
    ;

    /**
     * 餐桌类型
     */
    String bType;

    /**
     * 餐桌描述
     */
    String desc;

    BoardTypMapping(String bType, String desc) {
        this.bType = bType;
        this.desc = desc;
    }
}
