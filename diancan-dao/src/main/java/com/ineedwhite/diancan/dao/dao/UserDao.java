package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.UserDo;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    void userRegister(@Param("User")UserDo userDo);
}
