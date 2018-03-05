package com.ineedwhite.diancan.web.controller.test;

import com.ineedwhite.diancan.dao.dao.TestDao;
import com.ineedwhite.diancan.dao.domain.TestDo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;

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

    @org.junit.Test
    public void testMysql() {
        List<TestDo> list =  testDao.queryAll();
        for (TestDo testDo : list) {
            System.out.println(testDo.getTest_author());
        }
    }
}
