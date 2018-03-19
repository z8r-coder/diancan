package com.ineedwhite.diancan.biz;

import java.util.Map;

public interface ContactService {

    /**
     * 获取联系我们
     * @param paraMap
     * @return
     */
    public Map<String, String> getContact(Map<String, String> paraMap);
}
