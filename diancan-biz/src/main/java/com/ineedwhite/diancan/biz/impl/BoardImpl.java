package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.Board;
import com.ineedwhite.diancan.biz.DianCanConfig;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.domain.BoardDo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // TODO: 2018/3/8 需要添加悲观索
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
                    freeBoardId.add(boardId);
                }
            }

            String jsonBoardId = JSON.toJSONString(freeBoardId);
            resp.put("board_id_set", jsonBoardId);
        } catch (Exception ex) {
            logger.error("method:register op user table occur exception:" + ex);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00002);
        }
        return resp;
    }
}
