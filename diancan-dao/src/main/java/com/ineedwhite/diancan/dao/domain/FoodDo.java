package com.ineedwhite.diancan.dao.domain;

import lombok.Data;

/**
 * @author ruanxin
 * @create 2018-03-10
 * @desc 菜品
 */
@Data
public class FoodDo {
    private Integer food_id;
    private String food_name;
    private Integer food_type_id;
    private float food_price;
    private String food_remark;
    private Integer food_grounding;
    private Integer food_monthlysales;
    private float food_vip_price;
}
