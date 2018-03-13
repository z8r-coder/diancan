package com.ineedwhite.diancan.biz;

import com.ineedwhite.diancan.dao.dao.OrderDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc 关单服务
 */
@Service
public class OrderCloseService {

    private Logger logger = Logger.getLogger(OrderCloseService.class);

    private OrderDao orderDao;
    /**
     * 执行异步关单
     */
    public void doTask(){
        try {
            List<String> orders = orderDao.selectOrderByStatusWithoutClose();
            for (String orderId : orders) {

            }
        } catch (Exception ex) {

        }
    }
}
