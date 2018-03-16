package com.ineedwhite.diancan.web.controller.test;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oracle.jrockit.jfr.ContentType.Timestamp;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */
public class MainTest {

    public static void main(String[] args) {
        testPhone();
    }

    public static void testPhone() {
        String phone = "15884812382";
        System.out.println(phone.substring(phone.length() - 6, phone.length()));
    }

    public static void testMov() {
        int test = 2 << 2;
        System.out.println(test);
    }
    public static void testSplit() {
        String test = "1|2|3|4";
        List<String> list = Arrays.asList(test.split("\\|"));
    }
    public static void testBean2Map() {
        Map<String, String> map = new HashMap<String, String>();
        TestBean testBean = new TestBean();
        map.put("name", "ruanxin");
        map.put("phone", "15884812382");
        try {
            TestBean bean = TestBean.class.newInstance();
            BeanUtils.populate(bean, map);
            System.out.println(bean.getId() + ":" + bean.getName() + ":" + bean.getPhone());

            Map<String, String> map1 = BeanUtils.describe(bean);
            for (String key : map1.keySet()) {
                System.out.println(key + ":" + map1.get(key));
            }
            System.out.println(map1.get("class"));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
