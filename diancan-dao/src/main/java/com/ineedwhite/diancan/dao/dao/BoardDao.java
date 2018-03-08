package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.BoardDo;

import java.util.List;

public interface BoardDao {
    /**
     * 查找所有的桌位信息
     * @return
     */
    List<BoardDo> findAllBoardInfo();
}
