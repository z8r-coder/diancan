package com.ineedwhite.diancan.biz.impl;

import com.ineedwhite.diancan.biz.UserService;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    public Map<String, String> getUserCoupon(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String user_id = paraMap.get("user_id");
        try {
            UserDo userDo = userDao.selectUserByUsrId(user_id);
            String couponId = userDo.getUser_coupon();
            List<String> couponList = new ArrayList<String>(Arrays.asList(couponId.split("\\|")));

        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }
    public Map<String, String> register(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();

        String phone = paraMap.get("user_phone");
        try {
            UserDo oldUsr = userDao.selectUserByPhone(phone);
            if (oldUsr != null) {
                //have register
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00005);
                return resp;
            }

            String usrId = UUID.randomUUID().toString().replace("-", "");
            UserDo userDo = new UserDo();

            String cardNo = BizUtils.getUsrCardNo(phone);

            userDo.setUser_id(usrId);
            userDo.setUser_name(paraMap.get("user_name"));
            userDo.setUser_phone(paraMap.get("user_phone"));
            userDo.setUser_password(paraMap.get("user_password"));
            userDo.setAccumulate_points(0);
            userDo.setBalance(0);
            userDo.setMember_level("0");
            userDo.setUser_is_del(0);
            userDo.setUser_card_no(cardNo);

            resp.put("user_id", usrId);

            userDao.insertUser(userDo);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);
        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> login(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String user_phone = paraMap.get("user_phone");
        String user_password = paraMap.get("user_password");
        try {
            UserDo userDo = userDao.selectUserByPhone(user_phone);
            if (userDo == null) {
                //have not register
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00007);
                return resp;
            }

            if (!StringUtils.equals(user_password, userDo.getUser_password())) {
                //password wrong
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00004);
                return resp;
            }
            //password right
            resp.put("user_id", userDo.getUser_id());
            resp.put("user_name", userDo.getUser_name());
            resp.put("user_phone", userDo.getUser_phone());
            resp.put("accumulate_points", userDo.getAccumulate_points().toString());
            resp.put("balance", String.valueOf(userDo.getBalance()));
            resp.put("member_level", userDo.getMember_level());
        } catch (Exception ex) {
            logger.error("method:login op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> userInfo(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();

        String usrId = paraMap.get("user_id");
        try {
            UserDo userDo = userDao.selectUserByUsrId(usrId);
            resp = BizUtils.bean2Map(userDo);
            String couponId = userDo.getUser_coupon();

            int couponNum = 0;
            if (!StringUtils.isEmpty(couponId)) {
                List<String> couponIdList = new ArrayList<String>(Arrays.asList("\\|"));
                couponNum = couponIdList.size();
            }
            resp.put("coupon_num", String.valueOf(couponNum));
            resp.remove("user_is_del");
            resp.remove("user_password");
            resp.remove("user_coupon");
            BizUtils.setRspMap(resp,ErrorCodeEnum.DC00000);
        } catch (Exception ex) {
            logger.error("method:usrInfo op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }
}
