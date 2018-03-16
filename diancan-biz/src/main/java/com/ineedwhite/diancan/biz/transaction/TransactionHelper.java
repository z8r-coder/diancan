package com.ineedwhite.diancan.biz.transaction;

import com.ineedwhite.diancan.common.OrderStatus;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author ruanxin
 * @create 2018-03-15
 * @desc
 */
@Service
public class TransactionHelper {
    private final static Logger logger = Logger.getLogger(TransactionHelper.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;

    @Transactional
    public void updateOrdAndUser(UserDo userDo, String newAccumuPoint, String newBalance,
                                 String isVip, String userCoupon, String couponId,
                                 String orderPaid, String orderId) {
        int userAffectRows = userDao.updateUsrAcptAndBcAndmemLvlCp(userDo.getUser_id(), newAccumuPoint,newBalance,
                isVip, userCoupon);
        if (userAffectRows <= 0) {
            throw new UnexpectedRollbackException("更新user表失败");
        }
        int orderAffectRows = orderDao.updateOrdStsAndCpIdOrdPaidByOrdId(OrderStatus.UD.getOrderStatus(), couponId,String.valueOf(orderPaid), orderId);
        if (orderAffectRows <= 0) {
            throw new UnexpectedRollbackException("更新order表失败");
        }
    }
}
