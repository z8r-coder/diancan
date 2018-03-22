package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.FoodDo;

import java.util.List;

/**
 * @author ruanxin
 * @create 2018-03-10
 * @desc 菜品数据库操作
 */
public interface FoodDao {
    /**
     * 查找所有菜品信息
     * @return
     */
    List<FoodDo> findAllFood();

    /**
     * 查找历史所有的菜品信息
     * @return
     */
    List<FoodDo> findHistoryFood();

}
