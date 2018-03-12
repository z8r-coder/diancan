package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.DianCanConfig;
import com.ineedwhite.diancan.biz.Food;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.dao.dao.FoodDao;
import com.ineedwhite.diancan.dao.domain.FoodDo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-10
 * @desc
 */
@Service
public class FoodImpl implements Food {

    @Resource
    private DianCanConfig dianCanConfig;

    public Map<String, String> getFoodByType(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String foodType_id = paraMap.get("foodtype_id");
        Map<Integer, FoodDo> foods = dianCanConfig.getAllFood();
        List<List<FoodDo>> needFoods = new ArrayList<List<FoodDo>>();

        for (Integer foodId : foods.keySet()) {
            FoodDo food = foods.get(foodId);
            if (StringUtils.equals(foodType_id, food.getFood_type_id().toString())) {
                List<FoodDo> list = new ArrayList<FoodDo>();
                list.add(food);
                needFoods.add(list);
            }
        }

        String resFood = JSON.toJSONString(needFoods);
        resp.put("food_all", resFood);
        return resp;
    }
}
