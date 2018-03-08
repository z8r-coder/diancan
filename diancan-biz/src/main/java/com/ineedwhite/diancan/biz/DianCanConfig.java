package com.ineedwhite.diancan.biz;

import com.ineedwhite.diancan.dao.domain.BoardDo;

import java.util.Map;

public interface DianCanConfig {

    /**
     * 从数据库缓存
     * @return
     */
    boolean refreshConfig();

    /**
     * 返回所有桌位缓存
     */
    Map<Integer,BoardDo> getAllBoard();
}
