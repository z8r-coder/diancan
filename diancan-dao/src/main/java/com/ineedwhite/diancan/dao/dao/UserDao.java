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
}
