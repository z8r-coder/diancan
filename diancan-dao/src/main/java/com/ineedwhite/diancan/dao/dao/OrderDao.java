package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.OrderDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 桌子表数据库操作
 */
public interface OrderDao {
    /**
     * 通过时间段来查找已经被预定的桌id
     * @param order_board_time
     * @param order_time_interval
     * @return
     */
    List<Integer> selectBoardIdByTime(@Param("order_board_time")String order_board_time,
                                      @Param("order_time_interval")String order_time_interval);

    /**
     * 通过时间段和桌位号来查找订单
     * @param order_board_time
     * @param order_time_interval
     * @param board_id
     * @return
     */
    List<String> selectOrderByTimeAndBoardId(@Param("order_board_time")String order_board_time,
                                              @Param("order_time_interval")String order_time_interval,
                                              @Param("board_id")String board_id);

    /**
     * 通过时间段和桌位还有订单状态来查找订单
     * @param order_board_time
     * @param order_time_interval
     * @param board_id
     * @param order_status
     * @return
     */
    List<String> selectOrderByTimeAndBoardIdAndStatus(@Param("order_board_time")String order_board_time,
                                             @Param("order_time_interval")String order_time_interval,
                                             @Param("board_id")String board_id,
                                             @Param("order_status")String order_status);
    /**
     * 在订单表里面插入订单
     * @param orderDo
     */
    void insertOrderInfo(@Param("Order")OrderDo orderDo);

    /**
     * 查找出所有没有关单的订单
     * @return
     */
    List<String> selectOrderByStatusWithoutClose();

    int updateOrderStsById(@Param("OrderIdList")List<String> orderIdList,
                           @Param("ordSts")String ordSts);
}
