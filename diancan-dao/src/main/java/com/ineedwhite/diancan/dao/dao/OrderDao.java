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
     * @param orderStatusFirst
     * @param oderStatusSecond
     * @return
     */
    OrderDo selectOrdByOrdIdAndSts(@Param("order_id")String orderId,
                                   @Param("order_status_first")String orderStatusFirst,
                                   @Param("order_status_second")String oderStatusSecond);

    /**
     * 通过订单号更改订单中菜品和菜品数量字段
     * @param orderId
     * @param food
     * @param foodNum
     * @return
     */
    int updateOrdFoodAndFoodNumByOrdId(@Param("order_id")String orderId,
                                       @Param("food")String food,
                                       @Param("foodNum")String foodNum,
                                       @Param("order_total_amt")String order_total_amt);

    /**
     * 通过订单ID更新订单状态，卡券ID，实付金额
     * @param orderSts
     * @param couponId
     * @param order_paid
     * @param order_id
     * @return
     */
    int updateOrdStsAndCpIdOrdPaidByOrdId(@Param("ord_sts")String orderSts,
                                          @Param("coupon_id")String couponId,
                                          @Param("order_paid")String order_paid,
                                          @Param("order_id")String order_id);

    /**
     * 通过时间段来查找订单
     * @param ordSts
     * @param user_id
     * @param beginTime
     * @param endTime
     * @return
     */
    List<OrderDo> selectOrdTimeAndAmtByUsrIdAndOrdStsAndBeginTimeAndEndTime(@Param("ord_sts")String ordSts,
                                                                            @Param("user_id")String user_id,
                                                                            @Param("beginTime")String beginTime,
                                                                            @Param("endTime")String endTime);

    /**
     * 通过订单号和订单状态更新订单状态
     * @param ordId
     * @param oldOrdSts
     * @param newOrdSts
     * @return
     */
    int updateOrdStsByIdAndSts(@Param("ordId")String ordId,
                               @Param("oldOrdSts")String oldOrdSts,
                               @Param("newOrdSts")String newOrdSts);

    /**
     * 通过用户号和订单状态选择订单
     * @param user_id
     * @return
     */
    OrderDo selectOrdWithoutFinishByUsrId(@Param("user_id")String user_id);

    /**
     * 通过用户号查询最近的一条订单
     * @param user_id
     * @return
     */
    OrderDo selectOrdByUsrIdAndUDSts(@Param("user_id")String user_id);

    /**
     * 通过订单号和状态(UM,UK)更新食物和食物数量
     * @param food
     * @param foodNum
     * @return
     */
    int updateOrdFoodByOrdIdAndUKUMSts(@Param("food")String food,
                                       @Param("foodNum")String foodNum,
                                       @Param("food_total_amt")String food_total_amt,
                                       @Param("order_id")String order_id);

    /**
     * 根据order_id查找状态为UK和UM的订单
     * @param order_id
     * @return
     */
    OrderDo selectOrdByOrdIdAndUKUMSts(@Param("order_id")String order_id);

    /**
     * 根据订单ID更新订单中的订桌信息
     * @param order_id
     * @param orderPeopleNum
     * @param orderDateBoard
     * @param orderBoardTimeIntervel
     * @return
     */
    int updateOrdBoardAndStsByOrderIdAndOrdSts(@Param("order_id")String order_id,
                                               @Param("order_people_number")String orderPeopleNum,
                                               @Param("order_date_board")String orderDateBoard,
                                               @Param("order_board_time_interval")String orderBoardTimeIntervel,
                                               @Param("order_board_id")String boardId);

    /**
     * 通过订单ID更新订单状态
     * @param order_id
     * @param order_sts
     * @return
     */
    int updateOrderStsByOrderId(@Param("order_id")String order_id,
                                @Param("order_sts")String order_sts);

    /**
     * 更新订单号的菜品和菜品数量
     * @param food
     * @param foodNum
     * @param orderId
     * @return
     */
    int updateOrderFoodAndNumByOrdId(@Param("food")String food,
                                     @Param("food_num")String foodNum,
                                     @Param("order_id")String orderId);

    /**
     * 查看某个用户最近的订单记录
     * @param userId
     * @return
     */
    OrderDo selectTheRecentOrdByUserId(@Param("user_id")String userId);
}
