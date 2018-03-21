package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.DianCanConfigService;
import com.ineedwhite.diancan.biz.UserService;
import com.ineedwhite.diancan.biz.model.UserCoupon;
import com.ineedwhite.diancan.biz.utils.OrderUtils;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.GenderMapping;
import com.ineedwhite.diancan.common.OrderStatus;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.DateUtil;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.CouponDo;
import com.ineedwhite.diancan.dao.domain.OrderDo;
import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private DianCanConfigService dianCanConfigService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    public Map<String, String> modifiedUserInfo(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String user_id = paraMap.get("user_id");
        String user_name = paraMap.get("user_name");
        String user_gender = paraMap.get("user_gender");
        String user_birth = paraMap.get("user_birth");
        String user_phone = paraMap.get("user_phone");
        if (StringUtils.isEmpty(user_birth)) {
            user_birth = null;
        }
        try {
            UserDo userDo = userDao.selectUserByUsrId(user_id);
            if (userDo == null) {
                logger.warn("该用户不存在或被注销 user_id:" + user_id);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }

            int affectRows = userDao.updateUsrGdrAndNmAndBirAndPhoneById(user_name,user_gender,user_birth,user_phone, user_id);
            if (affectRows <= 0) {
                logger.warn("更新订单出错:userId:" + user_id);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
            }
        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> cancellation(Map<String, String> paraMap) throws Exception {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String orderId = paraMap.get("order_id");
        String userId = paraMap.get("user_id");
        if (orderId == null) {
            //未生成订单
            return resp;
        }
        if (!OrderUtils.getCacheOrder(orderId)) {
            //过期
            return resp;
        }

        List<String> orderFoodIdList = OrderUtils.getFoodIdList(orderId);
        StringBuilder foodIdSb = new StringBuilder();
        StringBuilder foodNumSb = new StringBuilder();
        for (String foodId : orderFoodIdList) {
            foodIdSb.append(foodId + "|");
            String foodNum = OrderUtils.getFoodNumCache(orderId, foodId);
            foodNumSb.append(foodNum + "|");
        }
        String foodIdStr = foodIdSb.toString();
        String foodNumStr = foodNumSb.toString();
        if (orderFoodIdList.size() > 0) {
            foodIdStr.substring(0, foodIdStr.length() - 1);
            foodNumStr.substring(0, foodNumStr.length() - 1);
        }
        OrderDo newOrder = new OrderDo();
        newOrder.setOrder_id(orderId);
        newOrder.setOrder_food_num(foodNumStr);
        newOrder.setOrder_food(foodIdStr);
        newOrder.setUser_id(userId);
        newOrder.setOrder_date(DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT));
        newOrder.setOrder_status(OrderStatus.UK.getOrderStatus());

        try {
            //未过期
            OrderDo orderDo = orderDao.selectOrderById(orderId);
            if (orderDo == null) {
                //还未持久化
                //获取菜品队列
                orderDao.insertOrderInfo(newOrder);
            } else {
                //已经持久化了
                if (StringUtils.equals(OrderStatus.UD.getOrderStatus(), orderDo.getOrder_status())) {
                    //已经成功支付，直接注销
                    BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);
                    return resp;
                }

                int affectRows = orderDao.updateOrderFoodAndNumByOrdId(foodIdStr, foodNumStr, orderId);
                if (affectRows <= 0) {
                    BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
                    return resp;
                }
            }
        } catch (Exception ex) {
            logger.error("method:cancellation op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> getUserDetailInfo(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String user_id = paraMap.get("user_id");
        try {
            UserDo userDo = userDao.selectUserByUsrId(user_id);
            if (userDo == null) {
                logger.warn("该用户不存在或被注销 user_id:" + user_id);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }
            String user_name = userDo.getUser_name();
            Integer user_gender_flag = userDo.getUser_gender();
            if (user_gender_flag == null) {
                resp.put("user_gender", null);
            } else {
                if (StringUtils.equals(GenderMapping.male.getGflag(), String.valueOf(user_gender_flag))) {
                    //男
                    resp.put("user_gender", "男");
                } else {
                    //女
                    resp.put("user_gender", "女");
                }
            }
            String user_birth = userDo.getUser_birth();
            String phone = userDo.getUser_phone();
            resp.put("user_name", user_name);
            resp.put("user_birth", user_birth);
            resp.put("phone", phone);
        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> getUserCoupon(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String user_id = paraMap.get("user_id");
        try {
            UserDo userDo = userDao.selectUserByUsrId(user_id);
            if (userDo == null) {
                logger.warn("该用户不存在或被注销 user_id:" + user_id);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }
            String couponIdStr = userDo.getUser_coupon();
            List<String> couponList = Arrays.asList(couponIdStr.split("\\|"));
            int coupon_num = couponList.size();

            List<UserCoupon> userCouponList = new ArrayList<UserCoupon>();
            for (String couponId : couponList) {
                CouponDo couponDo = dianCanConfigService.getCouponById(Integer.parseInt(couponId));
                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setConsu_amt(String.valueOf(couponDo.getConsumption_amount()));
                userCoupon.setCouponId(couponId);
                userCoupon.setDiscount(String.valueOf(couponDo.getDiscount()));
                userCoupon.setStart_time(couponDo.getStart_time());
                userCoupon.setExpire_time(couponDo.getExpiry_time());

                userCouponList.add(userCoupon);
            }
            String couponListStr = JSON.toJSONString(userCouponList);
            resp.put("coupon_num", String.valueOf(coupon_num));
            resp.put("coupon_list", couponListStr);
        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> register(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();

        String phone = paraMap.get("user_phone");
        try {
            UserDo oldUsr = userDao.selectUserByPhone(phone);
            if (oldUsr != null) {
                //have register
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00005);
                return resp;
            }

            String usrId = UUID.randomUUID().toString().replace("-", "");
            UserDo userDo = new UserDo();

            String cardNo = BizUtils.getUsrCardNo(phone);
            String userRegisterTime = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);

            userDo.setUser_id(usrId);
            userDo.setUser_name(paraMap.get("user_name"));
            userDo.setUser_phone(paraMap.get("user_phone"));
            userDo.setUser_password(paraMap.get("user_password"));
            userDo.setAccumulate_points(0);
            userDo.setBalance(0);
            userDo.setMember_level("0");
            userDo.setUser_is_del(0);
            userDo.setUser_card_no(cardNo);
            userDo.setUser_register_time(userRegisterTime);

            resp.put("user_id", usrId);

            userDao.insertUser(userDo);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);
        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> login(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String user_phone = paraMap.get("user_phone");
        String user_password = paraMap.get("user_password");
        try {
            UserDo userDo = userDao.selectUserByPhone(user_phone);
            if (userDo == null) {
                //have not register
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00007);
                return resp;
            }

            if (!StringUtils.equals(user_password, userDo.getUser_password())) {
                //password wrong
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00004);
                return resp;
            }
            //更新优惠券过期情况
            String userCoupon = userDo.getUser_coupon();
            if (!StringUtils.isEmpty(userCoupon)) {
                //拥有优惠券
                List<String> couPonList = new ArrayList<String>(Arrays.asList(userCoupon.split("\\|")));
                //未过期的优惠券
                List<String> canUseCoupon = new ArrayList<String>();
                for (String couponId:couPonList) {
                    CouponDo couponDo = dianCanConfigService.getCouponById(Integer.parseInt(couponId));
                    String exTime = couponDo.getExpiry_time();
                    exTime = exTime.replace("-", "").replace(" ","").replace(":","").replace(".","");
                    exTime = exTime.substring(0, exTime.length() - 1);
                    String nowTime = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
                    if (!DateUtil.compareTime(nowTime,exTime, DateUtil.DEFAULT_PAY_FORMAT)) {
                        //未过期
                        canUseCoupon.add(couponId);
                    }
                }
                StringBuilder cpSb = new StringBuilder();
                for (String couponId : canUseCoupon) {
                    cpSb.append(couponId + "|");
                }
                String canUseCpStr = cpSb.toString();
                if (canUseCpStr.length() > 0) {
                    canUseCpStr = canUseCpStr.substring(0, canUseCpStr.length() - 1);
                }
                int affectRows = userDao.updateUsrCouponById(user_phone, canUseCpStr);
                if (affectRows <= 0) {
                    logger.warn("更新用户表出错:user_id:" + userDo.getUser_id());
                    BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
                    return resp;
                }
            }

            //password right
            resp.put("user_id", userDo.getUser_id());
            resp.put("user_name", userDo.getUser_name());
            resp.put("user_phone", userDo.getUser_phone());
            resp.put("accumulate_points", userDo.getAccumulate_points().toString());
            resp.put("balance", String.valueOf(userDo.getBalance()));
            resp.put("member_level", userDo.getMember_level());

            OrderDo orderDo = orderDao.selectTheRecentOrdByUserId(userDo.getUser_id());
            if (orderDo == null) {
                //无感登陆
                return resp;
            }
            if (StringUtils.equals(OrderStatus.UK.getOrderStatus(), orderDo.getOrder_status()) ||
                    StringUtils.equals(OrderStatus.UM.getOrderStatus(), orderDo.getOrder_status())) {
                //这两个状态被认为支付未完成，需跳转页面重新支付，并且传回orderId
                resp.put("order_id", orderDo.getOrder_id());
                resp.put("order_status", orderDo.getOrder_status());
            }
        } catch (Exception ex) {
            logger.error("method:login op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> userInfo(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();

        String usrId = paraMap.get("user_id");
        try {
            UserDo userDo = userDao.selectUserByUsrId(usrId);
            if (userDo == null) {
                logger.warn("该用户不存在或被注销 user_id:" + usrId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }
            resp = BizUtils.bean2Map(userDo);
            String couponId = userDo.getUser_coupon();

            int couponNum = 0;
            if (!StringUtils.isEmpty(couponId)) {
                List<String> couponIdList = Arrays.asList(couponId.split("\\|"));
                couponNum = couponIdList.size();
            }
            resp.put("coupon_num", String.valueOf(couponNum));
            resp.remove("user_is_del");
            resp.remove("user_password");
            resp.remove("user_coupon");
            BizUtils.setRspMap(resp,ErrorCodeEnum.DC00000);
        } catch (Exception ex) {
            logger.error("method:usrInfo op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }
}
