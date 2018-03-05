package com.ineedwhite.diancan.web.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ruanxin
 * @create 2018-03-05
 * @desc
 */
@Controller
@RequestMapping("/test")
public class RxTest {

    @ResponseBody
    @RequestMapping("/test")
    public String version() {
        return "20180305";
    }
}
