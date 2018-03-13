package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.DianCanConfigService;
import com.ineedwhite.diancan.biz.FoodTypeService;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.dao.domain.FoodTypeDo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-09
 * @desc
 */
@Service
public class FoodTypeServieImpl implements FoodTypeService{
    @Resource
    DianCanConfigService dianCanConfig;

    public Map<String, String> getAllFoodType() {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        Map<Integer,FoodTypeDo> foodTypeMap = dianCanConfig.getAllFoodType();

        List<Map<String, String>> resList = new ArrayList<Map<String, String>>();

        for (Integer foodTypeId : foodTypeMap.keySet()) {
            Map tmpMap = new HashMap();
            tmpMap.put("food_id",foodTypeId);
            tmpMap.put("food_name", foodTypeMap.get(foodTypeId).getFoodtype_name());
            resList.add(tmpMap);
        }

        String retJsonStr = JSON.toJSONString(resList);
        resp.put("food_type", retJsonStr);
        return resp;
    }
}
