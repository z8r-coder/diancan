package com.ineedwhite.diancan.web.controller.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import org.omg.CORBA.MARSHAL;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */
public class MainTest {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("111", "222");
        map.put("333", "444");
        map.put("555", "666");
        System.out.println(JSON.toJSONString(map));
    }

}
