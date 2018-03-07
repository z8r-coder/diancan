package com.ineedwhite.diancan.biz.impl;

import com.ineedwhite.diancan.biz.User;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */
@Service
public class UserImpl implements User {

    private Logger logger = Logger.getLogger(UserImpl.class);

    @Autowired
    private UserDao userDao;

    public Map<String, String> register(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();

        UserDo userDo = new UserDo();
        userDo.setUser_id(paraMap.get("user_id"));
        userDo.setUser_name(paraMap.get("user_name"));
        userDo.setUser_phone(paraMap.get("user_phone"));
        userDo.setAccumulate_points(0);
        userDo.setBalance(0);
        userDo.setMember_level("0");
        userDo.setUser_is_del(0);
        try {
            userDao.userRegister(userDo);
            resp.put("rspCode", ErrorCodeEnum.DC00000.getCode());
            resp.put("rspMsg", ErrorCodeEnum.DC00000.getDesc());
        } catch (Exception ex) {
            logger.error("op user table occur exception:" + ex);
            resp.put("rspCode", ErrorCodeEnum.DC00002.getCode());
            resp.put("rspMsg", ErrorCodeEnum.DC00002.getDesc());
        }
        return resp;
    }
}
