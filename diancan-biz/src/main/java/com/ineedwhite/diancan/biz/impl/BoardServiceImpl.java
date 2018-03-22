package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.BoardService;
import com.ineedwhite.diancan.biz.DianCanConfigService;
import com.ineedwhite.diancan.biz.transaction.TransactionHelper;
import com.ineedwhite.diancan.biz.utils.OrderUtils;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.LevelMappingEnum;
import com.ineedwhite.diancan.common.OrderStatus;
import com.ineedwhite.diancan.common.constants.BizOptions;
import com.ineedwhite.diancan.common.constants.DcException;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.RedLock;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.BoardDo;
import com.ineedwhite.diancan.dao.domain.FoodDo;
import com.ineedwhite.diancan.dao.domain.OrderDo;
import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc
 */
@Service
public class BoardServiceImpl implements BoardService{

    private Logger logger = Logger.getLogger(BoardServiceImpl.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;

    @Resource
    private DianCanConfigService dianCanConfig;

    @Resource
    private TransactionHelper transactionHelper;

    public Map<String, String> loadBoardPage(Map<String, String> paraMap) throws Exception {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String orderId = paraMap.get("order_id");
        OrderDo orderDo = orderDao.selectOrderById(orderId);
        if (orderDo == null) {
            logger.error("该订单不存在，请重新下单，orderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00023);
            return resp;
        }
        if (!OrderUtils.getCacheOrder(orderId)) {
            //过期
            logger.error("该订单已过期, orderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00013);
            return resp;
        }
        if (orderDo.getBoard_id() == null) {
            //未有点桌操作
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00028);
            return resp;
        }
        //选过桌
        Integer boardType = dianCanConfig.getBoardById(orderDo.getBoard_id()).getBoard_type();
        String orderBoardDate = orderDo.getOrder_board_date();
        Integer orderPeopleNum = orderDo.getOrder_people_number();
        String orderTimeIntervel = orderDo.getOrder_board_time_interval();
        resp.put("board_type", String.valueOf(boardType));
        resp.put("order_board_date", orderBoardDate);
        resp.put("order_people_num", String.valueOf(orderPeopleNum));
        resp.put("order_time_intervel", orderTimeIntervel);
        resp.put("board_id", String.valueOf(orderDo.getBoard_id()));
        return resp;
    }

    public Map<String, String> getAvailableBoard(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp,ErrorCodeEnum.DC00000);

        List<Integer> freeBoardId = new ArrayList<Integer>();

        String orderBoardTime = paraMap.get("order_board_date");
        String orderBoardInterval = paraMap.get("order_board_time_interval");
        int orderNum = Integer.parseInt(paraMap.get("order_people_number"));
        int boardType = Integer.parseInt(paraMap.get("board_type"));

        Map<Integer, BoardDo> boardCache = dianCanConfig.getAllBoard();
        Map<Integer, BoardDo> newBoardMap = new HashMap<Integer, BoardDo>();

        newBoardMap.putAll(boardCache);
        try {
            List<Integer> boardIds = orderDao.selectBoardIdByTime(orderBoardTime, orderBoardInterval);
            for (Integer boardId : boardIds) {
                newBoardMap.remove(boardId);
            }
            for (Integer boardId : newBoardMap.keySet()) {
                BoardDo boardDo = newBoardMap.get(boardId);
                if (boardDo.getBoard_people_number() == orderNum &&
                        boardDo.getBoard_type() == boardType) {
                    //满足人数，餐桌类型限制
                    freeBoardId.add(boardId);
                }
            }

            String jsonBoardId = JSON.toJSONString(freeBoardId);
            resp.put("board_id_set", jsonBoardId);
        } catch (Exception ex) {
            logger.error("method:getAvailableBoard op board table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }

    public Map<String, String> reserveBoard(Map<String, String> paraMap) throws Exception {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String boardId = paraMap.get("board_id");
        String orderId = paraMap.get("order_id");
        String usrId = paraMap.get("user_id");

        if (!RedLock.lockDefaultTime(boardId)) {  // 命中悲观锁
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00008);
            return resp;
        }
        OrderDo orderDo = orderDao.selectOrderById(orderId);
        if (orderDo == null) {
            logger.error("该订单不存在，请重新下单，orderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00023);
            return resp;
        }
        if (!OrderUtils.getCacheOrder(orderId)) {
            //过期
            logger.error("该订单已过期, orderId:" + orderId);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00013);
            return resp;
        }
        if (!StringUtils.equals(orderDo.getOrder_status(), OrderStatus.UM.getOrderStatus())) {
            //订单状态不对
            logger.error("该订单状态错误, orderId:" + orderId + " orderSts:" + orderDo.getOrder_status());
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00027);
            return resp;
        }

        BoardDo boardDo = dianCanConfig.getBoardById(Integer.parseInt(boardId));
        if (boardDo == null) {
            logger.error("board_id:" + boardId + " doesn't exist!");
            throw new DcException(ErrorCodeEnum.DC00005);
        }
        try {
            Integer orderPeopleNum = boardDo.getBoard_people_number();
            String orderBoardDate = paraMap.get("order_board_date");
            String orderTimeInterval = paraMap.get("order_board_time_interval");

            UserDo userDo = userDao.selectUserByUsrId(usrId);
            if (userDo == null) {
                logger.error("the user:" + usrId + " have not register!");
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }

            resp.put("board_id", boardId);

            List<String> selectOrdByStatus = orderDao.selectOrderByTimeAndBoardIdAndStatus(orderBoardDate,
                    orderTimeInterval,boardId, OrderStatus.UD.getOrderStatus());
            if (selectOrdByStatus.size() != 0) {
                //该桌号存在结账成功的订单
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00009);
                return resp;
            }

            List<String> selectOrd = orderDao.selectOrderByTimeAndBoardId(orderBoardDate,
                    orderTimeInterval, boardId);

            for (String ordId : selectOrd) {
                if (OrderUtils.getCacheOrder(ordId)) {
                    //存在未过期的订单
                    logger.warn("orderId:" + orderId + "have order the board:" + boardId + "!");
                    BizUtils.setRspMap(resp, ErrorCodeEnum.DC00009);
                    return resp;
                }
            }
            //结账时使用的couponId
            String userCouponList = userDo.getUser_coupon();
            List<String> couponList;
            if (StringUtils.isEmpty(userCouponList)) {
                couponList = new ArrayList<String>();
            } else {
                couponList = new ArrayList<String>(Arrays.asList(userCouponList.split("\\|")));
            }

            String couponId = orderDo.getCoupon_id();
            if (!StringUtils.isEmpty(couponId)) {
                //不为空,移除使用的优惠券
                couponList.remove(couponId);
            }

            float orderPaid = orderDo.getOrder_paid();
            float balance = userDo.getBalance();

            //在判断账户余额之前，把桌子数据更进数据库
            int orderAffectRows = orderDao.updateOrdBoardAndStsByOrderIdAndOrdSts(orderId
                    , String.valueOf(orderPeopleNum),orderBoardDate, orderTimeInterval, boardId);

            if (orderAffectRows <= 0) {
                logger.error("更新订单表失败，orderId:" + orderId);
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
                return resp;
            }
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

            if (newAccumuPoint >= BizOptions.BECOME_VIP &&
                    StringUtils.equals(LevelMappingEnum.NVIP.getVflag(), userDo.getMember_level())) {
                //成为会员
                resp.put("vip", LevelMappingEnum.VIP.getVflag());
                isVip = LevelMappingEnum.VIP.getVflag();
            } else if (StringUtils.equals(LevelMappingEnum.VIP.getVflag(), userDo.getMember_level())) {
                //如果是VIP将该字段改回VIP
                isVip = LevelMappingEnum.VIP.getVflag();
            }
            //拼凑优惠券列表
            StringBuilder cpIdsb = new StringBuilder();
            for (String cpId : couponList) {
                cpIdsb.append(cpId + "|");
            }
            userCouponList = cpIdsb.toString();
            if (couponList != null && couponList.size() != 0) {
                userCouponList = userCouponList.substring(0, userCouponList.length() - 1);
            }

//            //算月销
//            String foodId = orderDo.getOrder_food();
//            String foodNum = orderDo.getOrder_food_num();
//            List<String> foodIdList = Arrays.asList(foodId.split("\\|"));
//            List<String> foodNumList = Arrays.asList(foodNum.split("\\|"));
//            for (String fdId : foodIdList) {
//                FoodDo foodDo = dianCanConfig.getFoodById(Integer.valueOf(fdId));
//
//            }
            //事务更新用户表和订单表
            transactionHelper.updateOrdAndUser(userDo,String.valueOf(newAccumuPoint),String.valueOf(newBalance),
                    isVip,userCouponList, orderId);
            //支付成功后删除购物车缓存
            OrderUtils.deleteCacheFoodList(orderId);

            resp.put("order_paid", String.valueOf(orderPaid));
            resp.put("accumulate_points", String.valueOf(getAccumuPoint));
        } catch (Exception ex) {
            logger.error("method:reserveBoard op order table occur exception:" + ex.getMessage(), ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        } finally {
            RedLock.unLock(boardId);
        }
        return resp;
    }
}
