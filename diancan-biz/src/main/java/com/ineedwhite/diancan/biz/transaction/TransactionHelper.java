package com.ineedwhite.diancan.biz.transaction;

import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.dao.UserDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
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
    public void updateOrdAndUser() {

    }
}
