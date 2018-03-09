package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.Board;
import com.ineedwhite.diancan.biz.DianCanConfig;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.constants.DcException;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.DateUtil;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.domain.BoardDo;
import com.ineedwhite.diancan.dao.domain.OrderDo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc
 */
@Service
public class BoardImpl implements Board{

    private Logger logger = Logger.getLogger(BoardImpl.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private DianCanConfig dianCanConfig;

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

    // TODO: 2018/3/8 需要添加分布式锁
    public Map<String, String> reserveBoard(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        // TODO: 2018/3/9 用餐人数设计讨论
        OrderDo orderDo = new OrderDo();
        String boardId = paraMap.get("board_id");
        BoardDo boardDo = dianCanConfig.getBoardById(Integer.parseInt(boardId));
        if (boardDo == null) {
            logger.error("board_id:" + boardId + " doesn't exist!");
            throw new DcException(ErrorCodeEnum.DC00005);
        }
        String orderId = UUID.randomUUID().toString().replace("-", "");
        Integer orderPeopleNum = boardDo.getBoard_people_number();
        String orderDate = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
        String orderBoardDate = paraMap.get("order_board_date");
        String orderTimeInterval = paraMap.get("order_board_time_interval");

        orderDo.setOrder_id(orderId);
        orderDo.setBoard_id(Integer.parseInt(boardId));
        orderDo.setOrder_date(orderDate);
        orderDo.setOrder_people_number(orderPeopleNum);
        orderDo.setOrder_board_date(orderBoardDate);
        orderDo.setOrder_board_time_interval(orderTimeInterval);
        orderDo.setOrder_status("UK");

        try {
            orderDao.insertOrderInfo(orderDo);
        } catch (Exception ex) {
            logger.error("method:reserveBoard op order table occur exception:" + ex.getMessage(), ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }
}
