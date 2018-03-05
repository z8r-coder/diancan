package com.ineedwhite.diancan.web.controller.test;

import com.ineedwhite.diancan.dao.dao.TestDao;
import com.ineedwhite.diancan.dao.domain.TestDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ruanxin
 * @create 2018-03-05
 * @desc
 */
@Controller
@RequestMapping("/test")
public class RxTest {
    @Autowired
    TestDao testDao;

    @ResponseBody
    @RequestMapping("/test")
    public String version() {
        return "20180305";
    }

    @ResponseBody
    @RequestMapping("/testMysql")
    public String testMysql() {
        List<TestDo> list =  testDao.queryAll();
        return list.get(0).getTest_author();
    }
}
