package com.ineedwhite.diancan.common.utils;

import com.ineedwhite.diancan.common.TypeCast;

/**
 * @author ruanxin
 * @create 2018-03-09
 * @desc 配置文件解析
 */
public class PropertiesUtil {

    public static TypeCast getValue(String text){
        return new TypeCast(text);
    }

    public static String getStringValue (String text) {
        return null;
    }
}
