package com.ineedwhite.diancan.web.controller.test;

import com.ineedwhite.diancan.biz.Board;
import com.ineedwhite.diancan.biz.User;
import com.ineedwhite.diancan.dao.dao.TestDao;
import com.ineedwhite.diancan.dao.domain.TestDo;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
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

    @Autowired
    private Board board;

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
        Map<String, String> map = user.register(paraMap);
        System.out.println(map);
    }

    /**
     * 登陆
     */
    @org.junit.Test
    public void login() {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("user_phone", "15884812382");
        paraMap.put("user_password", "15884812382");
        Map<String, String> map = user.login(paraMap);
        System.out.println(map);
    }

    @org.junit.Test
    public void getAvailBoard() {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("order_board_date", "20180306");
        paraMap.put("order_board_time_interval", "0");
        paraMap.put("board_type", "1");
        paraMap.put("order_people_number", "2");
        Map<String, String> map = board.getAvailableBoard(paraMap);
        System.out.println(map);
    }
}
