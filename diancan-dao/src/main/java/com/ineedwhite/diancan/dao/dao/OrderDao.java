package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.OrderDo;
import com.ineedwhite.diancan.dao.domain.UserDo;
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

    /**
     * 更新固定订单的订单状态
     * @param orderIdList
     * @param ordSts
     * @return
     */
    int updateOrderStsById(@Param("OrderIdList")List<String> orderIdList,
                           @Param("ordSts")String ordSts);

    /**
     * 根据订单号获取订单
     * @param orderId
     */
    OrderDo selectOrderById(@Param("OrderId") String orderId);

    /**
     * 通过订单号获取优惠卡券号
     * @param orderId
     * @return
     */
    UserDo selectUserInfoByOrdId(@Param("OrderId") String orderId);

    /**
     * 更新订单的
     * @param ordTotalAmt  总计金额
     * @param ordSts       订单状态
     * @param ordFood      订单食物
     * @param ordFoodNum   食物数量
     * @return
     */
    int updateOrderInfoByOrdId(@Param("order_total_amount")float ordTotalAmt,
                               @Param("order_status")String ordSts,
                               @Param("order_food")String ordFood,
                               @Param("order_food_num")String ordFoodNum,
                               @Param("order_id")String ordId);

    /**
     * 通过订单ID和状态查找订单
     * @param orderId
     * @param orderStatus
     * @return
     */
    OrderDo selectOrdByOrdIdAndSts(@Param("order_id")String orderId,
                                   @Param("order_status")String orderStatus);

    /**
     * 通过订单号更改订单中菜品和菜品数量字段
     * @param orderId
     * @param food
     * @param foodNum
     * @return
     */
    int updateOrdFoodAndFoodNumByOrdId(@Param("order_id")String orderId,
                                       @Param("food")String food,
                                       @Param("foodNum")String foodNum);
}
