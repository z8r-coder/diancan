package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.DianCanConfigService;
import com.ineedwhite.diancan.biz.OrderService;
import com.ineedwhite.diancan.biz.model.ShoppingCartFood;
import com.ineedwhite.diancan.biz.utils.OrderUtils;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.constants.BizOptions;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.RedisUtil;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.domain.FoodDo;
import com.ineedwhite.diancan.dao.domain.OrderDo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private DianCanConfigService dianCanConfigService;

    public Map<String, String> addFoodToShoppingCart(Map<String, String> paraMap) throws Exception {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);
        String orderId = paraMap.get("order_id");
        String foodId = paraMap.get("food_id");
        String foodNum = paraMap.get("food_num");

        try {
            if (foodNum.length() > 2) {
                logger.error("菜品数量过多，最多100道");
                BizUtils.setRspMap(paraMap, ErrorCodeEnum.DC00013);
                return resp;
            }

            if (!OrderUtils.getCacheOrder(orderId)) {
                logger.error("该订单已过期 orderId:" + orderId);
                BizUtils.setRspMap(paraMap, ErrorCodeEnum.DC00013);
                return resp;
            }

            Long totalFoodNum = OrderUtils.getOrdFoodListLen(orderId);
            if (totalFoodNum > BizOptions.FOOD_LIMIT) {
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00012);
                return resp;
            }
            RedisUtil.beginTransaction();
            if (OrderUtils.getFoodNumCache(orderId, foodId) == null) {
                //若该菜品不存在
                OrderUtils.addFoodIdList(orderId, foodId);
            }
            OrderUtils.setFoodNumCache(orderId, foodId, foodNum);
        } catch (Exception e) {
            RedisUtil.discard();
            logger.error("OrderService add food occurs exception", e);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        } finally {
            RedisUtil.commitTransaction();
        }
        return resp;
    }

    public Map<String, String> shoppingCart(Map<String, String> paraMap) throws Exception {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String orderId = paraMap.get("order_id");
        if (!OrderUtils.getCacheOrder(orderId)) {
            logger.error("该订单不存在或已过期 orderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00013);
            return resp;
        }

        List<String> foodIds = OrderUtils.getFoodIdList(orderId);
        Map<Integer, FoodDo> foodDoMap = dianCanConfigService.getAllFood();
        List<ShoppingCartFood> needToPayFood = new ArrayList<ShoppingCartFood>();

        for (String foodId : foodIds) {
            String foodNum = OrderUtils.getFoodNumCache(orderId, foodId);
            if (StringUtils.equals(foodNum, "0") || StringUtils.equals(foodNum, null)) {
                //数量为0不加入购物车
                continue;
            }
            FoodDo foodDo = foodDoMap.get(foodId);
            ShoppingCartFood cartFood = new ShoppingCartFood();
            cartFood.setFoodName(foodDo.getFood_name());
            cartFood.setNum(Integer.parseInt(foodNum));
            cartFood.setPrice(foodDo.getFood_price());

            float total = cartFood.getNum() * cartFood.getPrice();
            cartFood.setTotal(total);
            needToPayFood.add(cartFood);
        }

        String retNeedToPayFoodStr = JSON.toJSONString(needToPayFood);
        resp.put("pay_food_all", retNeedToPayFoodStr);

        float sumFood = 0;
        for (ShoppingCartFood food : needToPayFood) {
            sumFood += food.getTotal();
        }
        resp.put("total_food_money", String.valueOf(sumFood));
        return resp;
    }
}
