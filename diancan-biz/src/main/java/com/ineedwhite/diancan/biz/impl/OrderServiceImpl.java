package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.DianCanConfigService;
import com.ineedwhite.diancan.biz.FoodService;
import com.ineedwhite.diancan.biz.OrderService;
import com.ineedwhite.diancan.biz.UserService;
import com.ineedwhite.diancan.biz.model.ShoppingCartCoupon;
import com.ineedwhite.diancan.biz.model.ShoppingCartFood;
import com.ineedwhite.diancan.biz.transaction.TransactionHelper;
import com.ineedwhite.diancan.biz.utils.OrderUtils;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.LevelMappingEnum;
import com.ineedwhite.diancan.common.OrderStatus;
import com.ineedwhite.diancan.common.constants.BizOptions;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.DateUtil;
import com.ineedwhite.diancan.common.utils.RedisUtil;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.CouponDo;
import com.ineedwhite.diancan.dao.domain.FoodDo;
import com.ineedwhite.diancan.dao.domain.OrderDo;
import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;

    @Resource
    private TransactionHelper transactionHelper;

    public Map<String, String> checkOut(Map<String, String> paraMap) throws Exception {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String orderId = paraMap.get("order_id");
        String couponId = paraMap.get("coupon_id");

        if (!OrderUtils.getCacheOrder(orderId)) {
            //过期
            logger.warn("该订单不存在或已过期 OrderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00013);
            return resp;
        }

        try {
            OrderDo orderDo = orderDao.selectOrdByOrdIdAndSts(orderId, OrderStatus.UD.getOrderStatus());
            if (orderDo != null) {
                //幂等
                logger.warn("该订单已经支付成功! orderId:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);
                return resp;
            }
            orderDo = orderDao.selectOrdByOrdIdAndSts(orderId, OrderStatus.UM.getOrderStatus());
            if (orderDo == null) {
                //无效或不存在
                logger.warn("该订单无效或不存在 OrderId:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00015);
                return resp;
            }

            UserDo userDo = orderDao.selectUserInfoByOrdId(orderId);
            //总计
            float totalAmt = orderDo.getOrder_total_amount();
            float orderPaid;

            //优惠券
            String userCoupon = userDo.getUser_coupon();
            List<String> couponList = new ArrayList<String>(Arrays.asList(userCoupon.split("\\|")));
            if (couponId == null) {
                //不使用优惠券
                if(StringUtils.equals(userDo.getMember_level(), LevelMappingEnum.NVIP.getVflag())) {
                    //不是VIP
                    orderPaid = totalAmt;
                } else {
                    //是VIP
                    String foodIdStr = orderDo.getOrder_food();
                    String foodNumStr = orderDo.getOrder_food_num();
                    List<String> foodIdList = Arrays.asList(foodIdStr.split("\\|"));
                    List<String> foodNumList = Arrays.asList(foodNumStr.split("\\|"));
                    float sumVipFood = 0;
                    for (int i = 0; i < foodIdList.size();i++) {
                        int foodId = Integer.parseInt(foodIdList.get(i));
                        int foodNum = Integer.parseInt(foodNumList.get(i));
                        FoodDo foodDo = dianCanConfigService.getFoodById(foodId);
                        sumVipFood = sumVipFood + (foodNum * foodDo.getFood_vip_price());
                    }
                    orderPaid = sumVipFood;
                }
            } else {
                //使用优惠券
                int indexCoupon = couponList.indexOf(couponId);
                CouponDo couponDo = dianCanConfigService.getCouponById(Integer.parseInt(couponId));
                if (couponDo == null) {
                    logger.warn("该卡券不存在,couponId:" + couponId);
                    BizUtils.setRspMap(resp, ErrorCodeEnum.DC00016);
                    return resp;
                }
                String exTime = couponDo.getExpiry_time();
                exTime = exTime.replace("-", "").replace(" ","").replace(":","").replace(".","");
                exTime = exTime.substring(0, exTime.length() - 1);
                String nowTime = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
                if (DateUtil.compareTime(nowTime,exTime, DateUtil.DEFAULT_PAY_FORMAT)) {
                    //过期
                    couponList.remove(indexCoupon);
                    logger.warn("该卡券已过期,couponId:" + couponId);
                    BizUtils.setRspMap(resp, ErrorCodeEnum.DC00017);
                    return resp;
                }

                float consu = couponDo.getConsumption_amount();
                float discard = couponDo.getDiscount();
                if (consu > totalAmt) {
                    logger.warn("金额未达到指定金额,不能使用该优惠券, couponId:" + couponId);
                    BizUtils.setRspMap(resp, ErrorCodeEnum.DC00018);
                    return resp;
                }
                orderPaid = totalAmt - discard;
                //use the coupon
                couponList.remove(indexCoupon);
            }

            float balance = userDo.getBalance();
            if (balance < orderPaid) {
                logger.warn("账户余额不足，请充值: userId:" + userDo.getUser_id());
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00020);
                return resp;
            }
            float newBalance = balance - orderPaid;

            //获得积分
            int getAccumuPoint = (int) (orderPaid / 10);
            int newAccumuPoint = userDo.getAccumulate_points() + getAccumuPoint;
            resp.put("vip", LevelMappingEnum.NVIP.getVflag());

            String isVip = LevelMappingEnum.NVIP.getVflag();

            if (newAccumuPoint >= BizOptions.BECOME_VIP) {
                //成为会员
                resp.put("vip", LevelMappingEnum.VIP.getVflag());
                isVip = LevelMappingEnum.VIP.getVflag();
            }
            //拼凑优惠券列表
            StringBuilder cpIdsb = new StringBuilder();
            for (String cpId : couponList) {
                cpIdsb.append(cpId + "|");
            }
            if (couponId != null && couponId.length() != 0) {
                userCoupon = cpIdsb.toString();
                userCoupon = userCoupon.substring(0, userCoupon.length() - 1);

            }
            //事务更新订单表和用户表
            transactionHelper.updateOrdAndUser(userDo,String.valueOf(newAccumuPoint),String.valueOf(newBalance),
                    isVip,userCoupon,couponId,String.valueOf(orderPaid), orderId);

            resp.put("accumulate_points", String.valueOf(getAccumuPoint));

        } catch (Exception ex) {
            logger.error("shoppingCartAddMinus occurs exception", ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        }

        return resp;
    }

    public Map<String, String> shoppingCartAddMinus(Map<String, String> paraMap) throws Exception {
        Map<String,String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String foodId = paraMap.get("food_id");
        String foodNum = paraMap.get("food_num");
        String orderId = paraMap.get("order_id");
        String couponId = paraMap.get("coupon_id");

        if (foodNum.length() > 2) {
            logger.error("菜品数量过多，最多100道");
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00013);
            return resp;
        }
        if (!OrderUtils.getCacheOrder(orderId)) {
            //过期
            logger.error("该订单已经过期,orderId:" + orderId);
            BizUtils.setRspMap(resp,ErrorCodeEnum.DC00013);
            return resp;
        }

        //更新购物车中菜品的缓存
        OrderUtils.setFoodNumCache(orderId, foodId, foodNum);
        //更新库中的菜品字段
        try {
            OrderDo orderDo = orderDao.selectOrdByOrdIdAndSts(orderId, OrderStatus.UM.getOrderStatus());
            String orderFood = orderDo.getOrder_food();
            String orderFoodNum = orderDo.getOrder_food_num();

            List<String> orderFoodList = new ArrayList<String>(Arrays.asList(orderFood.split("\\|")));
            List<String> orderFoodNumList = new ArrayList<String>(Arrays.asList(orderFoodNum.split("\\|")));

            int indexFood = orderFoodList.indexOf(foodId);
            if (StringUtils.equals(foodNum, "0")) {
                orderFoodList.remove(indexFood);
                orderFoodNumList.remove(indexFood);
            } else {
                orderFoodNumList.set(indexFood, foodNum);
            }
            StringBuilder newFoodSb = new StringBuilder();
            StringBuilder newFoodNumSb = new StringBuilder();
            for (int i = 0; i < orderFoodList.size();i++) {
                newFoodSb.append(orderFoodList.get(i) + "|");
                newFoodNumSb.append(orderFoodNumList.get(i) + "|");
            }
            String newFoodIdStr = newFoodSb.toString();
            String newFoodNumStr = newFoodNumSb.toString();

            //新的菜品ID和菜品数量入库字段
            newFoodIdStr = newFoodIdStr.substring(0, newFoodIdStr.length() - 1);
            newFoodNumStr = newFoodNumStr.substring(0, newFoodNumStr.length() - 1);

            List<ShoppingCartFood> cartFoodList = new ArrayList<ShoppingCartFood>();
            for (String newFoodId : orderFoodList) {
                String redfoodNum = OrderUtils.getFoodNumCache(orderId, newFoodId);

                FoodDo foodDo = dianCanConfigService.getFoodById(Integer.parseInt(newFoodId));
                ShoppingCartFood cartFood = new ShoppingCartFood();

                float total_money = foodDo.getFood_price() * Integer.parseInt(redfoodNum);
                float total_vip_money = foodDo.getFood_vip_price() * Integer.parseInt(redfoodNum);

                cartFood.setTotal(total_money);
                cartFood.setVipTotal(total_vip_money);

                cartFoodList.add(cartFood);
            }
            //在购物车中修改商品后属性更改
            FoodDo foodDo = dianCanConfigService.getFoodById(Integer.parseInt(foodId));
            float foodTotalMoney = foodDo.getFood_price() * Integer.parseInt(foodNum);
            resp.put("mod_food_single_sum", String.valueOf(foodTotalMoney));
            //支付总计
            float sumFood = 0;
            //VIP支付总计
            float vipSumFood = 0;
            for (ShoppingCartFood food : cartFoodList) {
                sumFood += food.getTotal();
                vipSumFood += food.getVipTotal();
            }
            resp.put("food_sum_money", String.valueOf(sumFood));
            resp.put("vip_food_money", String.valueOf(vipSumFood));

            UserDo userDo = orderDao.selectUserInfoByOrdId(orderId);
            if (userDo == null) {
                logger.warn("该用户被删除或已注销,user_id:" + userDo.getUser_id());
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }

            if (couponId == null) {
                //不用优惠券
                if (StringUtils.equals(userDo.getMember_level(), LevelMappingEnum.NVIP.getVflag())) {
                    //非VIP
                    resp.put("total_food_money", String.valueOf(sumFood));
                } else {
                    //vip
                    resp.put("total_food_money", String.valueOf(vipSumFood));
                }
            } else {
                //使用优惠券
                CouponDo couponDo = dianCanConfigService.getCouponById(Integer.parseInt(couponId));
                float totalFoodAmt = sumFood - couponDo.getDiscount();
                resp.put("total_food_money", String.valueOf(totalFoodAmt));

                if (couponDo.getConsumption_amount() > sumFood) {
                    logger.warn("金额未达到指定金额,不能使用该优惠券, couponId:" + couponId);
                    BizUtils.setRspMap(resp, ErrorCodeEnum.DC00018);
                    totalFoodAmt = totalFoodAmt + couponDo.getDiscount();
                    resp.put("total_food_money", String.valueOf(totalFoodAmt));
                    return resp;
                }
            }

            int affectRows = orderDao.updateOrdFoodAndFoodNumByOrdId(orderId, newFoodIdStr, newFoodNumStr);

            if (affectRows <= 0) {
                logger.warn("更新订单出错:orderId:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
                return resp;
            }
        } catch (Exception ex) {
            logger.error("shoppingCartAddMinus occurs exception", ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        }
        return resp;
    }

    public Map<String, String> useCoupon(Map<String, String> paraMap) {
        Map<String,String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String couponId = paraMap.get("coupon_id");
        String orderId = paraMap.get("order_id");
        CouponDo couponDo = dianCanConfigService.getCouponById(Integer.parseInt(couponId));
        if (couponDo == null) {
            logger.warn("该卡券不存在,couponId:" + couponId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00016);
            return resp;
        }
        String exTime = couponDo.getExpiry_time();
        exTime = exTime.replace("-", "").replace(" ","").replace(":","").replace(".","");
        exTime = exTime.substring(0, exTime.length() - 1);
        String nowTime = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
        if (DateUtil.compareTime(nowTime,exTime, DateUtil.DEFAULT_PAY_FORMAT)) {
            //过期
            logger.warn("该卡券已过期,couponId:" + couponId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00017);
            return resp;
        }
        float consu = couponDo.getConsumption_amount();
        float discount = couponDo.getDiscount();
        try {
            OrderDo orderDo = orderDao.selectOrderById(orderId);
            float total = orderDo.getOrder_total_amount();
            if (consu > total) {
                logger.warn("金额未达到指定金额,不能使用该优惠券, couponId:" + couponId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00018);
                return resp;
            }

            float couponAmt = total - discount;
            resp.put("coupon_Amt", String.valueOf(couponAmt));
        } catch (Exception e) {
            logger.error("use coupon occurs exception", e);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        }
        return resp;
    }

    public Map<String, String> getCouponList(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String userId = paraMap.get("user_id");
        String orderId = paraMap.get("order_id");
        try {
            String coupon = userDao.selectCouponByUsrId(userId);
            if (StringUtils.isEmpty(coupon)) {
                logger.warn("该用户没有可用优惠券! user_id:" + userId);
                BizUtils.setRspMap(paraMap, ErrorCodeEnum.DC00014);
                return resp;
            }
            OrderDo orderDo = orderDao.selectOrderById(orderId);
            String ordSts = orderDo.getOrder_status();
            if (!StringUtils.equals(ordSts, OrderStatus.UM.getOrderStatus())) {
                //状态不一致
                logger.error("the order status is wrong! orderId:" + orderId);
                BizUtils.setRspMap(paraMap, ErrorCodeEnum.DC00015);
                return resp;
            }
            List<String> couponList = Arrays.asList(coupon.split("\\|"));
            if (couponList == null || couponList.size() == 0) {
                logger.warn("该用户没有可用优惠券! user_id:" + userId);
                BizUtils.setRspMap(paraMap, ErrorCodeEnum.DC00014);
                return resp;
            }
            Map<Integer, CouponDo> couponMap = dianCanConfigService.getAllCouponDo();
            List<ShoppingCartCoupon> canUseCouponNameList = new ArrayList<ShoppingCartCoupon>();
            float totalMoney = orderDo.getOrder_total_amount();

            for (String couponId : couponList) {
                CouponDo cp = couponMap.get(Integer.parseInt(couponId));
                if (totalMoney > cp.getConsumption_amount()) {
                    //可用
                    ShoppingCartCoupon cartCoupon = new ShoppingCartCoupon();
                    cartCoupon.setCouponId(couponId);
                    cartCoupon.setRemark(cp.getRemark());
                    canUseCouponNameList.add(cartCoupon);
                }
            }
            if (canUseCouponNameList.size() == 0) {
                logger.warn("该用户没有可用优惠券! user_id:" + userId);
                BizUtils.setRspMap(paraMap, ErrorCodeEnum.DC00014);
                return resp;
            }
            String couponListStr = JSON.toJSONString(canUseCouponNameList);
            resp.put("coupon_List", couponListStr);
        } catch (Exception e) {
            logger.error("select coupon occurs exception", e);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        }
        return resp;
    }

    public Map<String, String> addFoodToShoppingCart(Map<String, String> paraMap) {
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

        //菜品队列
        List<String> foodIds = OrderUtils.getFoodIdList(orderId);
        if (foodIds == null || foodIds.size() == 0) {
            logger.error("菜品未空, orderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00019);
            return resp;
        }

        //菜品字符串
        StringBuilder foodSb = new StringBuilder();
        //菜品数量字符串
        StringBuilder foodNumSb = new StringBuilder();

        Map<Integer, FoodDo> foodDoMap = dianCanConfigService.getAllFood();
        List<ShoppingCartFood> needToPayFood = new ArrayList<ShoppingCartFood>();
        List<Float> vipPriceList = new ArrayList<Float>();

        UserDo userDo = orderDao.selectUserInfoByOrdId(orderId);
        if (userDo == null) {
            logger.error("该下单用户已被注销或不存在orderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
            return resp;
        }

        for (String foodId : foodIds) {
            String foodNum = OrderUtils.getFoodNumCache(orderId, foodId);
            if (StringUtils.equals(foodNum, "0") || StringUtils.equals(foodNum, null)) {
                //数量为0不加入购物车
                continue;
            }
            //构建菜品字符串
            foodSb.append(foodId + "|");
            //构建菜品数目字符串
            foodNumSb.append(foodNum + "|");

            FoodDo foodDo = foodDoMap.get(Integer.parseInt(foodId));
            ShoppingCartFood cartFood = new ShoppingCartFood();
            cartFood.setFoodName(foodDo.getFood_name());
            cartFood.setNum(Integer.parseInt(foodNum));
            cartFood.setPrice(foodDo.getFood_price());
            cartFood.setVipPrice(foodDo.getFood_vip_price());
            cartFood.setFoodId(foodId);

            float total = cartFood.getNum() * cartFood.getPrice();
            cartFood.setTotal(total);
            float vipTotal = cartFood.getNum() * cartFood.getVipPrice();
            cartFood.setVipTotal(vipTotal);

            needToPayFood.add(cartFood);

            //vip price list
            vipPriceList.add(foodDo.getFood_vip_price());
        }
        String foodStr = foodSb.toString();
        String foodNumStr = foodNumSb.toString();
        //菜品入库参数
        foodStr = foodStr.substring(0, foodStr.length() - 1);
        //菜品数量入库参数
        foodNumStr = foodNumStr.substring(0, foodNumStr.length() - 1);
        //购物车中的菜品
        String retNeedToPayFoodStr = JSON.toJSONString(needToPayFood);
        resp.put("pay_food_all", retNeedToPayFoodStr);

        //支付总计
        float sumFood = 0;
        //VIP支付总计
        float vipSumFood = 0;
        for (ShoppingCartFood food : needToPayFood) {
            sumFood += food.getTotal();
            vipSumFood += food.getVipTotal();
        }
        resp.put("food_sum_money", String.valueOf(sumFood));

        if (StringUtils.equals(userDo.getMember_level(), LevelMappingEnum.VIP.getVflag())) {
            //是VIP
            resp.put("vip_food_money", String.valueOf(vipSumFood));
            resp.put("total_food_money", String.valueOf(vipSumFood));
            resp.put("is_vip_user", LevelMappingEnum.VIP.getVflag());
        } else {
            //非VIP
            resp.put("vip_food_money", String.valueOf(sumFood));
            resp.put("total_food_money", String.valueOf(sumFood));
            resp.put("is_vip_user", LevelMappingEnum.NVIP.getVflag());
        }

        int affectRows = orderDao.updateOrderInfoByOrdId(sumFood, OrderStatus.UM.getOrderStatus(),
                foodStr, foodNumStr, orderId);
        if (affectRows <= 0) {
            logger.warn("更新订单出错:orderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        }
        return resp;
    }
}
