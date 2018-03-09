package com.ineedwhite.diancan.dao.dao;

import com.ineedwhite.diancan.dao.domain.FoodTypeDo;

import java.util.List;

/**
 * 菜系表数据库操作
 */
public interface FoodTypeDao {
    /**
     * 查找所有已配置的菜系
     * @return
     */
    List<FoodTypeDo> findAllFoodType();
}
