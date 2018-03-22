package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.DianCanConfigService;
import com.ineedwhite.diancan.biz.FoodService;
import com.ineedwhite.diancan.biz.OrderService;
import com.ineedwhite.diancan.biz.UserService;
import com.ineedwhite.diancan.biz.model.OrderFoodInfo;
import com.ineedwhite.diancan.biz.model.ShoppingCartCoupon;
import com.ineedwhite.diancan.biz.model.ShoppingCartFood;
import com.ineedwhite.diancan.biz.transaction.TransactionHelper;
import com.ineedwhite.diancan.biz.utils.OrderUtils;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.LevelMappingEnum;
import com.ineedwhite.diancan.common.OrderStatus;
import com.ineedwhite.diancan.common.TimeMapping;
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
import javax.crypto.MacSpi;
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

    public Map<String, String> orderInfo(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String userId = paraMap.get("user_id");
        try {
            OrderDo orderDo = orderDao.selectOrdByUsrIdAndUDSts(userId);
            if (orderDo == null) {
                //订单不存在
                logger.warn("无支付成功的订单 userId:" + userId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00023);
                return resp;
            }

            resp.put("order_id", orderDo.getOrder_id());
            resp.put("order_paid", String.valueOf(orderDo.getOrder_paid()));
            resp.put("order_total_money", String.valueOf(orderDo.getOrder_total_amount()));
            resp.put("order_discount", String.valueOf(orderDo.getOrder_paid()));
            resp.put("order_people_num", String.valueOf(orderDo.getOrder_people_number()));
            //用餐时间
            String beginTime = TimeMapping.timeMappingMap.get(orderDo.getOrder_board_time_interval()).getBegin();
            String endTime = TimeMapping.timeMappingMap.get(orderDo.getOrder_board_time_interval()).getEnd();
            resp.put("order_date", orderDo.getOrder_board_date() + "  "
                    + beginTime + "-" + endTime);
            resp.put("order_board_id", String.valueOf(orderDo.getBoard_id()));

            List<String> foodIdList = Arrays.asList(orderDo.getOrder_food().split("\\|"));
            List<String> foodNumList = Arrays.asList(orderDo.getOrder_food_num().split("\\|"));

            List<OrderFoodInfo> orderFoodInfoList = new ArrayList<OrderFoodInfo>();
            for (int i = 0; i < foodIdList.size();i++) {
                String foodId = foodIdList.get(i);
                String foodNum = foodNumList.get(i);
                FoodDo foodDo = dianCanConfigService.getHistoryFoodById(Integer.parseInt(foodId));
                float totalMoney = foodDo.getFood_price() * Integer.parseInt(foodNum);
                OrderFoodInfo orderFoodInfo = new OrderFoodInfo();
                orderFoodInfo.setFoodName(foodDo.getFood_name());
                orderFoodInfo.setFoodNum(foodNum);
                orderFoodInfo.setFoodImg(foodDo.getFood_img());
                orderFoodInfo.setTotalMoney(String.valueOf(totalMoney));
                orderFoodInfo.setUnitPrice(String.valueOf(foodDo.getFood_price()));
                orderFoodInfo.setFoodImg(foodDo.getFood_img());

                orderFoodInfoList.add(orderFoodInfo);
            }
            String orderFoodInfoStr = JSON.toJSONString(orderFoodInfoList);
            resp.put("food_info", orderFoodInfoStr);
        } catch (Exception ex) {
            logger.error("orderInfo occurs exception", ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        }
        return resp;
    }
    public Map<String, String> orderWithoutFinish(Map<String, String> paraMap){
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String usrId = paraMap.get("user_id");
        try{
            OrderDo orderDo = orderDao.selectOrdWithoutFinishByUsrId(usrId);
            if (orderDo == null) {
                //该用户已经完成了所有的订单
                return resp;
            }

            if (!OrderUtils.getCacheOrder(orderDo.getOrder_id())) {
                //订单已经过期
                return resp;
            }
            BizUtils.setRspMap(resp,ErrorCodeEnum.DC00025);
            resp.put("order_id", orderDo.getOrder_id());
            resp.put("order_status", orderDo.getOrder_status());
        } catch (Exception ex) {
            logger.error("shoppingCartAddMinus occurs exception", ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        }
        return resp;
    }

    public Map<String, String> historyOrder(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String usrId = paraMap.get("user_id");
        String currYearBegin = DateUtil.getCurrYearBegin();
        String nextYearBegin = DateUtil.getNextYearBegin();
        try {
            List<OrderDo> orderDoList = orderDao.selectOrdTimeAndAmtByUsrIdAndOrdStsAndBeginTimeAndEndTime(OrderStatus.UD.getOrderStatus(),
                    usrId,currYearBegin,nextYearBegin);
            int comsuNum = orderDoList.size();
            float sumAmt = 0;
            for (OrderDo orderDo : orderDoList) {
                sumAmt += orderDo.getOrder_paid();
            }
            String orderAll = JSON.toJSONString(orderDoList);
            resp.put("comsu_num", String.valueOf(comsuNum));
            resp.put("sum_amt", String.valueOf(sumAmt));
            resp.put("order_all", orderAll);
        } catch (Exception ex) {
            logger.error("shoppingCartAddMinus occurs exception", ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        }
        return resp;

    }
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
            OrderDo orderDo = orderDao.selectOrderById(orderId);
            if (orderDo == null) {
                //无效或不存在
                logger.warn("该订单无效或不存在 OrderId:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00015);
                return resp;
            }
            if (StringUtils.equals(orderDo.getOrder_status(), OrderStatus.UD.getOrderStatus())) {
                //幂等
                logger.warn("该订单已经支付成功! orderId:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);
                return resp;
            }


            UserDo userDo = orderDao.selectUserInfoByOrdId(orderId);
            //总计
            float totalAmt = orderDo.getOrder_total_amount();
            float orderPaid;

            //优惠券
            String userCoupon = userDo.getUser_coupon();

            List<String> couponList;
            if (StringUtils.isEmpty(userCoupon)) {
                couponList = new ArrayList<String>();
            } else {
                couponList = new ArrayList<String>(Arrays.asList(userCoupon.split("\\|")));
            }

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
//                int indexCoupon = couponList.indexOf(couponId);
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
                    couponList.remove(couponId);
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
                //use the coupon 在订桌的时候再除去
//                couponList.remove(indexCoupon);
            }

//            float balance = userDo.getBalance();
//            if (balance < orderPaid) {
//                logger.warn("账户余额不足，请充值: userId:" + userDo.getUser_id());
//                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00020);
//                return resp;
//            }
//            float newBalance = balance - orderPaid;

            //获得积分
//            int getAccumuPoint = (int) (orderPaid / 10);
//            int newAccumuPoint = userDo.getAccumulate_points() + getAccumuPoint;
//            resp.put("vip", LevelMappingEnum.NVIP.getVflag());
//
//            String isVip = LevelMappingEnum.NVIP.getVflag();
//
//            if (newAccumuPoint >= BizOptions.BECOME_VIP &&
//                    StringUtils.equals(LevelMappingEnum.NVIP.getVflag(), userDo.getMember_level())) {
//                //成为会员
//                resp.put("vip", LevelMappingEnum.VIP.getVflag());
//                isVip = LevelMappingEnum.VIP.getVflag();
//            } else if (StringUtils.equals(LevelMappingEnum.VIP.getVflag(), userDo.getMember_level())) {
//                //如果是VIP将该字段改回VIP
//                isVip = LevelMappingEnum.VIP.getVflag();
//            }

            //拼凑优惠券列表
//            StringBuilder cpIdsb = new StringBuilder();
//            for (String cpId : couponList) {
//                cpIdsb.append(cpId + "|");
//            }
//            userCoupon = cpIdsb.toString();
//            if (couponList != null && couponList.size() != 0) {
//                userCoupon = userCoupon.substring(0, userCoupon.length() - 1);
//
//            }
//            transactionHelper.updateOrdAndUser(userDo,String.valueOf(newAccumuPoint),String.valueOf(newBalance),
//                    isVip,userCoupon,couponId,String.valueOf(orderPaid), orderId);

            //更新订单表，将状态改成UM，要使用的优惠券ID,需要支付的金额
            int affectRows = orderDao.updateOrdStsAndCpIdOrdPaidByOrdId(OrderStatus.UM.getOrderStatus(),
                    couponId, String.valueOf(orderPaid), orderId);
            if (affectRows <= 0) {
                logger.warn("更新订单出错:orderId:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
                return resp;
            }
            //支付成功后删除购物车缓存
//            OrderUtils.deleteCacheFoodList(orderId);

//            resp.put("accumulate_points", String.valueOf(getAccumuPoint));
//            resp.put("order_paid", String.valueOf(orderPaid));
        } catch (Exception ex) {
            logger.error("checkout occurs exception", ex);
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
            OrderDo orderDo = orderDao.selectOrdByOrdIdAndUKUMSts(orderId);
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
            if (newFoodIdStr.length() == 0) {
                //菜品减少为0
                logger.error("购物车中没有菜品");
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00019);
                return resp;
            }

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

            int affectRows = orderDao.updateOrdFoodAndFoodNumByOrdId(orderId, newFoodIdStr, newFoodNumStr, String.valueOf(sumFood));

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
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00014);
                return resp;
            }
            OrderDo orderDo = orderDao.selectOrderById(orderId);
            String ordSts = orderDo.getOrder_status();
            if (!StringUtils.equals(ordSts, OrderStatus.UK.getOrderStatus()) &&
            !StringUtils.equals(ordSts, OrderStatus.UM.getOrderStatus())) {
                //状态不一致
                logger.error("the order status is wrong! orderId:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00015);
                return resp;
            }
            List<String> couponList = Arrays.asList(coupon.split("\\|"));
            if (couponList == null || couponList.size() == 0) {
                logger.warn("该用户没有可用优惠券! user_id:" + userId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00014);
                return resp;
            }
            Map<Integer, CouponDo> couponMap = dianCanConfigService.getAllCouponDo();
            List<ShoppingCartCoupon> canUseCouponNameList = new ArrayList<ShoppingCartCoupon>();
            float totalMoney = orderDo.getOrder_total_amount();

            for (String couponId : couponList) {
                CouponDo cp = couponMap.get(Integer.parseInt(couponId));
                if (totalMoney >= cp.getConsumption_amount()) {
                    //可用
                    ShoppingCartCoupon cartCoupon = new ShoppingCartCoupon();
                    cartCoupon.setCouponId(couponId);
                    cartCoupon.setRemark(cp.getRemark());
                    canUseCouponNameList.add(cartCoupon);
                }
            }
            if (canUseCouponNameList.size() == 0) {
                logger.warn("该用户没有可用优惠券! user_id:" + userId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00014);
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

            if (!StringUtils.isEmpty(orderId) && !OrderUtils.getCacheOrder(orderId)) {
                logger.error("该订单已过期 orderId:" + orderId);
                BizUtils.setRspMap(paraMap, ErrorCodeEnum.DC00013);
                return resp;
            }
            if (StringUtils.isEmpty(orderId)) {
                //未传orderId，该订单还未生成,此处生成订单不持久化
                orderId = UUID.randomUUID().toString().replace("-", "");
                resp.put("order_id", orderId);
                //缓存订单
                OrderUtils.addCacheOrder(orderId);
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

        //此时订单还未持久化
        String orderId = paraMap.get("order_id");
        String userId = paraMap.get("user_id");

        OrderDo exOrd = orderDao.selectOrderById(orderId);
        UserDo userDo = userDao.selectUserByUsrId(userId);
        if (exOrd != null) {
            //已持久化
            if (StringUtils.equals(exOrd.getOrder_status(), OrderStatus.UD.getOrderStatus())) {
                logger.error("该订单已经支付成功，请重新选菜:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00013);
                return resp;
            }

            if (!OrderUtils.getCacheOrder(orderId)) {
                logger.error("该订单不存在或已过期 orderId:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00013);
                return resp;
            }
        }

        //菜品队列
        List<String> foodIds = OrderUtils.getFoodIdList(orderId);
        if (foodIds == null || foodIds.size() == 0) {
            logger.error("菜品为空, orderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00019);
            return resp;
        }
        boolean isFoodEmpty = true;
        for (String foodId : foodIds) {
            if (!StringUtils.equals(OrderUtils.getFoodNumCache(orderId, foodId), "0")) {
                isFoodEmpty = false;
            }
        }
        if (isFoodEmpty) {
            logger.error("菜品为空, orderId:" + orderId);
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

        //持久化订单
        OrderDo orderDo = new OrderDo();
        orderDo.setOrder_id(orderId);
        orderDo.setUser_id(userId);
        orderDo.setOrder_status(OrderStatus.UK.getOrderStatus());
        String currOrderDate = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
        orderDo.setOrder_date(currOrderDate);
        orderDo.setOrder_food(foodStr);
        orderDo.setOrder_food_num(foodNumStr);
        orderDo.setOrder_total_amount(sumFood);

        try {
            if (exOrd == null) {
                //如果订单不存在，则插入
                orderDao.insertOrderInfo(orderDo);
            } else {
                //如果订单存在，则更新
                orderDao.updateOrdFoodByOrdIdAndUKUMSts(orderDo.getOrder_food(), orderDo.getOrder_food_num(), String.valueOf(sumFood), orderId);
            }

        } catch (Exception e) {
            logger.error("OrderService add food occurs exception", e);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
        }
        return resp;
    }
}
