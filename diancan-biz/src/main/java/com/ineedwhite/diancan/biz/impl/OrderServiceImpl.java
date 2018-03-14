package com.ineedwhite.diancan.biz.impl;

import com.ineedwhite.diancan.biz.DianCanConfigService;
import com.ineedwhite.diancan.biz.OrderService;
import com.ineedwhite.diancan.biz.utils.OrderUtils;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.constants.BizOptions;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.RedisUtil;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.domain.OrderDo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-14
 * @desc
 */
@Service
public class OrderServiceImpl implements OrderService {

    private Logger logger = Logger.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderDao orderDao;

    public Map<String, String> addFoodToShoppingCart(Map<String, String> paraMap) throws Exception {
        Map<String, String> resp = new HashMap<String, String>();
        String orderId = paraMap.get("order_id");
        String foodId = paraMap.get("food_id");
        String foodNum = paraMap.get("food_num");

        try {
            if (!OrderUtils.getCacheOrder(orderId)) {
                logger.error("该订单已过期 orderId" + orderId);
                BizUtils.setRspMap(paraMap, ErrorCodeEnum.DC00013);
                return resp;
            }

            Long totalFoodNum = OrderUtils.getOrdFoodListLen(orderId);
            if (totalFoodNum > BizOptions.FOOD_LIMIT) {
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00012);
                return resp;
            }
            RedisUtil.beginTransaction();
            OrderUtils.addFoodIdList(orderId, foodId);
            OrderUtils.addFoodNumList(orderId, foodNum);
        } catch (Exception e) {
            RedisUtil.discard();
            logger.error("OrderService add food occurs exception", e);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        } finally {
            RedisUtil.commitTransaction();
        }
        return resp;
    }
}
