package com.ineedwhite.diancan.common.utils;

/**
 * @author ruanxin
 * @create 2018-03-09
 * @desc
 */
public class Preconditions {

    public static void checkArgument(boolean status, String msg) throws Exception {
        if (!status) {
            throw new Exception(msg);
        }
    }
}
