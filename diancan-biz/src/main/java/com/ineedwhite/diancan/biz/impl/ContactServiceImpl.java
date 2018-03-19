package com.ineedwhite.diancan.biz.impl;

import com.ineedwhite.diancan.biz.ContactService;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.dao.dao.ContactDao;
import com.ineedwhite.diancan.dao.domain.ContactDo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-19
 * @desc
 */
@Service
public class ContactServiceImpl implements ContactService {

    private Logger logger = Logger.getLogger(ContactServiceImpl.class);

    @Resource
    private ContactDao contactDao;

    public Map<String, String> getContact(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        try {
            ContactDo contactDo = contactDao.findContactInfo();
            resp = BizUtils.bean2Map(contactDo);
            resp.remove("contact_id");
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);
        } catch (Exception ex) {
            logger.error("method:usrInfo op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }
}
