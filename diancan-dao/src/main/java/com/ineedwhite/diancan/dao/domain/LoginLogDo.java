package com.ineedwhite.diancan.dao.domain;

import lombok.Data;

/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc 用户登陆日志
 */
@Data
public class LoginLogDo {
    private Integer ll_id;
    private String ll_login_date;
    private String ll_ip;
    private String ll_req_url;

}
