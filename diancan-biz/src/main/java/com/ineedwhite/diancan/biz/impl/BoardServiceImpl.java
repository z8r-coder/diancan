package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.BoardService;
import com.ineedwhite.diancan.biz.DianCanConfigService;
import com.ineedwhite.diancan.biz.utils.OrderUtils;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.OrderStatus;
import com.ineedwhite.diancan.common.constants.DcException;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.DateUtil;
import com.ineedwhite.diancan.common.utils.RedLock;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.dao.UserDao;
import com.ineedwhite.diancan.dao.domain.BoardDo;
import com.ineedwhite.diancan.dao.domain.OrderDo;
import com.ineedwhite.diancan.dao.domain.UserDo;
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

    public Map<String, String> reserveBoard(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String boardId = paraMap.get("board_id");

        if (!RedLock.lockDefaultTime(boardId)) {  // 命中悲观锁
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00008);
            return resp;
        }

        OrderDo orderDo = new OrderDo();

        BoardDo boardDo = dianCanConfig.getBoardById(Integer.parseInt(boardId));
        if (boardDo == null) {
            logger.error("board_id:" + boardId + " doesn't exist!");
            throw new DcException(ErrorCodeEnum.DC00005);
        }
        try {
            String orderId = UUID.randomUUID().toString().replace("-", "");
            Integer orderPeopleNum = boardDo.getBoard_people_number();
            String orderDate = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
            String orderBoardDate = paraMap.get("order_board_date");
            String orderTimeInterval = paraMap.get("order_board_time_interval");
            String usrId = paraMap.get("user_id");

            UserDo usr = userDao.selectUserByUsrId(usrId);
            if (usr == null) {
                logger.error("the user:" + usrId + " have not register!");
                BizUtils.setRspMap(resp, ErrorCodeEnum.DC00010);
                return resp;
            }

            orderDo.setUser_id(usrId);
            orderDo.setOrder_id(orderId);
            orderDo.setBoard_id(Integer.parseInt(boardId));
            orderDo.setOrder_date(orderDate);
            orderDo.setOrder_people_number(orderPeopleNum);
            orderDo.setOrder_board_date(orderBoardDate);
            orderDo.setOrder_board_time_interval(orderTimeInterval);
            orderDo.setOrder_status(OrderStatus.UK.getOrderStatus());

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

            orderDao.insertOrderInfo(orderDo);
            //cache orderId
            OrderUtils.addCacheOrder(orderId);
            resp.put("order_id", orderId);
        } catch (Exception ex) {
            logger.error("method:reserveBoard op order table occur exception:" + ex.getMessage(), ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        } finally {
            // TODO: 2018/3/14 何时加锁有待讨论
            RedLock.unLock(boardId);
        }
        return resp;
    }
}
