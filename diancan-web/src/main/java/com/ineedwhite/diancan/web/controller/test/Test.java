package com.ineedwhite.diancan.web.controller.test;

import com.ineedwhite.diancan.biz.User;
import com.ineedwhite.diancan.dao.dao.TestDao;
import com.ineedwhite.diancan.dao.domain.TestDo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-05
 * @desc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class Test {
    @Autowired
    private TestDao testDao;

    @Autowired
    private User user;

    @org.junit.Test
    public void testMysql() {
        List<TestDo> list =  testDao.queryAll();
        for (TestDo testDo : list) {
            System.out.println(testDo.getTest_author());
        }
    }

    /**
     * 注册功能
     */
    @org.junit.Test
    public void register() {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("user_password", "15884812382");
        paraMap.put("user_name", "ruanxin");
        paraMap.put("user_phone", "15884812382");
        user.register(paraMap);
    }
}
