package com.ineedwhite.diancan.biz.impl;

import com.ineedwhite.diancan.biz.RechargeService;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.DateUtil;
import com.ineedwhite.diancan.dao.dao.RechargeDao;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-16
 * @desc
 */
public class RechargeServiceImpl implements RechargeService{

    private Logger logger = Logger.getLogger(RechargeServiceImpl.class);

    @Resource
    private RechargeDao rechargeDao;

    @Resource
    private UserDao userDao;

    public Map<String, String> recharge(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String user_id = paraMap.get("user_id");
        String rechage_amt = paraMap.get("recharge_amt");

        try {
            UserDo userDo = userDao.selectUserByUsrId(user_id);
            if (userDo == null) {
                logger.warn("该用户不存在或被注销 user_id:" + user_id);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }
            String rechageDate = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
            
        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }
}
