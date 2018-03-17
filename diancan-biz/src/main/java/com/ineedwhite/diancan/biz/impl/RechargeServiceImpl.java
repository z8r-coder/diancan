package com.ineedwhite.diancan.biz.impl;

import com.ineedwhite.diancan.biz.RechargeService;
import com.ineedwhite.diancan.biz.transaction.TransactionHelper;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.LevelMappingEnum;
import com.ineedwhite.diancan.common.constants.BizOptions;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.DateUtil;
import com.ineedwhite.diancan.dao.dao.RechargeDao;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.RechargeDo;
import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ruanxin
 * @create 2018-03-16
 * @desc
 */
@Service
public class RechargeServiceImpl implements RechargeService{

    private Logger logger = Logger.getLogger(RechargeServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Resource
    private TransactionHelper transactionHelper;

    public Map<String, String> recharge(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String user_id = paraMap.get("user_id");
        String recharge_amt = paraMap.get("recharge_amt");

        try {
            UserDo userDo = userDao.selectUserByUsrId(user_id);
            if (userDo == null) {
                logger.warn("该用户不存在或被注销 user_id:" + user_id);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }
            String rechargeDate = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
            String rechargeId = UUID.randomUUID().toString().replace("-", "");
            RechargeDo rechargeDo = new RechargeDo();
            rechargeDo.setRecharge_id(rechargeId);
            rechargeDo.setRecharge_amt(Float.parseFloat(recharge_amt));
            rechargeDo.setRecharge_date(rechargeDate);
            rechargeDo.setRecharge_user(user_id);

            float newBalance = userDo.getBalance() + Float.parseFloat(recharge_amt);
            if (newBalance > BizOptions.RECHARGE_LIMIT) {
                //最多100万
                logger.warn("充值超额，rechargeId:" + rechargeId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00024);
                return resp;
            }

            int getAccPoints = Integer.parseInt(recharge_amt) / 100;
            int newAccPoints = userDo.getAccumulate_points() + getAccPoints;
            String isVip = LevelMappingEnum.NVIP.getVflag();
            if (StringUtils.equals(userDo.getMember_level(), LevelMappingEnum.VIP.getVflag())) {
                //是VIP
                isVip = LevelMappingEnum.VIP.getVflag();
            } else {
                //不是VIP
                if (newAccPoints >= BizOptions.BECOME_VIP) {
                    //成为VIP
                    isVip = LevelMappingEnum.VIP.getVflag();
                }
            }
            transactionHelper.updateRechargeAndUser(String.valueOf(newBalance), user_id,
                    String.valueOf(newAccPoints),isVip, rechargeDo);
            if (StringUtils.equals(userDo.getMember_level(), LevelMappingEnum.NVIP.getVflag()) &&
                    newAccPoints >= BizOptions.BECOME_VIP) {
                //本来不是VIP，成为VIP
                resp.put("vip", "1");
            } else {
                resp.put("vip", "0");
            }
            resp.put("user_balance", String.valueOf(newBalance));
            resp.put("get_accumulate_points", String.valueOf(getAccPoints));

        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> rechargePageLoading(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String usrId = paraMap.get("user_id");
        try {
            UserDo userDo = userDao.selectUserByUsrId(usrId);
            if (userDo == null) {
                //have not register
                logger.warn("该用户被注销或不存在 userId:" + usrId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }
            float balance = userDo.getBalance();
            resp.put("balance", String.valueOf(balance));
        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }
}
