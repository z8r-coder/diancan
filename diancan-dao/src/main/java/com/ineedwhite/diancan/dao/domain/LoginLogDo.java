package com.ineedwhite.diancan.dao.domain;


/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc 用户登陆日志
 */
public class LoginLogDo {
    private Integer ll_id;
    private String ll_login_date;
    private String ll_ip;
    private String ll_req_url;

    public Integer getLl_id() {
        return ll_id;
    }

    public void setLl_id(Integer ll_id) {
        this.ll_id = ll_id;
    }

    public String getLl_login_date() {
        return ll_login_date;
    }

    public void setLl_login_date(String ll_login_date) {
        this.ll_login_date = ll_login_date;
    }

    public String getLl_ip() {
        return ll_ip;
    }

    public void setLl_ip(String ll_ip) {
        this.ll_ip = ll_ip;
    }

    public String getLl_req_url() {
        return ll_req_url;
    }

    public void setLl_req_url(String ll_req_url) {
        this.ll_req_url = ll_req_url;
    }
}
