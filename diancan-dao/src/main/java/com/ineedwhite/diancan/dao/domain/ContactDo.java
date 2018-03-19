package com.ineedwhite.diancan.dao.domain;

/**
 * @author ruanxin
 * @create 2018-03-19
 * @desc
 */
public class ContactDo {
    private Integer contact_id;
    private String contact_phone;
    private String contact_email;
    private String contact_address;
    private String contact_workTime;

    public Integer getContact_id() {
        return contact_id;
    }

    public void setContact_id(Integer contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }

    public String getContact_workTime() {
        return contact_workTime;
    }

    public void setContact_workTime(String contact_workTime) {
        this.contact_workTime = contact_workTime;
    }
}
