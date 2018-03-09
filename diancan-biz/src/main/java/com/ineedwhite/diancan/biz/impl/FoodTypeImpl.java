package com.ineedwhite.diancan.biz.impl;

import com.ineedwhite.diancan.biz.DianCanConfig;
import com.ineedwhite.diancan.biz.FoodType;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.dao.domain.FoodTypeDo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-09
 * @desc
 */
@Service
public class FoodTypeImpl implements FoodType{
    @Resource
    DianCanConfig dianCanConfig;

    public Map<String, String> getAllFoodType() {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        Map<Integer,FoodTypeDo> foodTypeMap = dianCanConfig.getAllFoodType();
        for (Integer foodTypeId : foodTypeMap.keySet()) {
            resp.put(foodTypeId.toString(), foodTypeMap.get(foodTypeId).getFoodtype_name());
        }
        return resp;
    }
}
