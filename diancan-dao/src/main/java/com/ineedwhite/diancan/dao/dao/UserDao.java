package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表数据库操作
 */
public interface UserDao {
    /**
     * 插入用户信息
     * @param userDo
     */
    void insertUser(@Param("User")UserDo userDo);

    /**
     * 通过手机号查询用户信息
     * @param user_phone
     */
    UserDo selectUserByPhone(@Param("user_phone")String user_phone);

    /**
     * 通过用户ID查找用户
     * @param user_id
     * @return
     */
    UserDo selectUserByUsrId(@Param("user_id")String user_id);

    /**
     * 通过用户ID查找优惠券
     * @param user_id
     * @return
     */
    String selectCouponByUsrId(@Param("user_id")String user_id);

    /**
     * 更新用户表
     * @param user_id
     * @param accumulate_points
     * @param balance
     * @param member_level
     * @param user_coupon
     * @return
     */
    int updateUsrAcptAndBcAndmemLvlCp(@Param("user_id")String user_id,
                                      @Param("accumulate_points")String accumulate_points,
                                      @Param("balance")String balance,
                                      @Param("member_level")String member_level,
                                      @Param("user_coupon")String user_coupon);

    /**
     * 根据卡号更新基本信息
     * @param userName
     * @param userGender
     * @param userBirth
     * @param userPhone
     * @param userId
     * @return
     */
    int updateUsrGdrAndNmAndBirAndPhoneById(@Param("user_name")String userName,
                                            @Param("user_gender")String userGender,
                                            @Param("user_birth")String userBirth,
                                            @Param("user_phone")String userPhone,
                                            @Param("user_id")String userId);

    /**
     * 根据用户号更新积分和用户余额
     * @param user_balance
     * @param user_id
     * @param accumulate_points
     * @return
     */
    int updateUsrBalanceById(@Param("user_balance")String user_balance,
                             @Param("user_id")String user_id,
                             @Param("accumulate_points")String accumulate_points,
                             @Param("user_vip")String user_vip);

    /**
     * 通过用户号更新优惠券列表
     * @param user_phone
     * @param user_coupon
     * @return
     */
    int updateUsrCouponById(@Param("user_phone")String user_phone,
                            @Param("user_coupon")String user_coupon);
}
