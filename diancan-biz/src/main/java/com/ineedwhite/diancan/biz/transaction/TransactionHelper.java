package com.ineedwhite.diancan.biz.transaction;

import com.ineedwhite.diancan.common.OrderStatus;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.dao.RechargeDao;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.RechargeDo;
import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author ruanxin
 * @create 2018-03-15
 * @desc 事务处理类
 */
@Service
public class TransactionHelper {
    private final static Logger logger = Logger.getLogger(TransactionHelper.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;

    @Resource
    private RechargeDao rechargeDao;

    @Transactional
    public void updateOrdAndUser(UserDo userDo, String newAccumuPoint, String newBalance,
                                 String isVip, String userCoupon, String orderId) {
        int userAffectRows = userDao.updateUsrAcptAndBcAndmemLvlCp(userDo.getUser_id(), newAccumuPoint,newBalance,
                isVip, userCoupon);
        if (userAffectRows <= 0) {
            throw new UnexpectedRollbackException("更新user表失败");
        }
        int orderAffectRows = orderDao.updateOrderStsByOrderId(orderId, OrderStatus.UD.getOrderStatus());
        if (orderAffectRows <= 0) {
            throw new UnexpectedRollbackException("更新order表失败");
        }
    }

    @Transactional
    public void updateRechargeAndUser(String newBalance, String user_id, String newAccPoints, String isVip,
                                      RechargeDo rechargeDo) {
        int affectRows = userDao.updateUsrBalanceById(newBalance, user_id, newAccPoints, isVip);
        if (affectRows <= 0) {
            throw new UnexpectedRollbackException("更新user表失败");
        }
        rechargeDao.insertRecharge(rechargeDo);
    }
}
