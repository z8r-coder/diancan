package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.LoginLogDo;
import org.apache.ibatis.annotations.Param;

/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc
 */
public interface LoginLogDao {
    void insertLoginLog(@Param("loginLogDo") LoginLogDo loginLogDo);
}
