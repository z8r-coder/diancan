package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    /**
     * 用户注册
     * @param userDo
     */
    void userRegister(@Param("User")UserDo userDo);

    /**
     * 用户登陆
     * @param user_phone
     */
    UserDo userLogin(@Param("user_phone")String user_phone);
}
