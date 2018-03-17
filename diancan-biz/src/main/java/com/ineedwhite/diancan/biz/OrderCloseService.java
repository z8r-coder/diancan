package com.ineedwhite.diancan.biz;

import com.ineedwhite.diancan.biz.utils.OrderUtils;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc 关单服务
 */
@Service("orderCloseService")
public class OrderCloseService {

    private Logger logger = Logger.getLogger(OrderCloseService.class);

    @Resource
    private OrderDao orderDao;
    /**
     * 执行异步关单
     */
    public void doTask() throws Exception {
        List<String> orders = orderDao.selectOrderByStatusWithoutClose();

        List<String> needCloseOrd = new ArrayList<String>();
        for (String orderId : orders) {
            if (!OrderUtils.getCacheOrder(orderId)) {
                //该订单超时
                needCloseOrd.add(orderId);
            }
        }
        if (CollectionUtils.isEmpty(needCloseOrd)) {
            return;
        }
        int affectRows = orderDao.updateOrderStsById(needCloseOrd, "UC");
        if (affectRows <= 0) {
            logger.warn("关单task 更新数据库失败!");
        }
        for (String orderId : orders) {
            //删除购物车缓存队列
            OrderUtils.deleteCacheFoodList(orderId);
        }
    }
}
