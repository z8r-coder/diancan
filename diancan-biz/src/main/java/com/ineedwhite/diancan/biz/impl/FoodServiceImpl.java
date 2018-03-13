package com.ineedwhite.diancan.biz.impl;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.DianCanConfigService;
import com.ineedwhite.diancan.biz.FoodService;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.constants.BizOptions;
import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.ParserUtil;
import com.ineedwhite.diancan.dao.domain.FoodDo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ruanxin
 * @create 2018-03-10
 * @desc
 */
@Service
public class FoodServiceImpl implements FoodService {

    private final static Logger logger = Logger.getLogger(FoodServiceImpl.class);

    @Resource
    private DianCanConfigService dianCanConfig;

    public Map<String, String> getFoodByType(Map<String, String> paraMap) {
        Map<String, String> resp = new HashMap<String, String>();
        BizUtils.setRspMap(resp, ErrorCodeEnum.DC00000);

        String foodType_id = paraMap.get("food_type_id");
        int foodPage = Integer.parseInt(paraMap.get("food_page"));

        Map<Integer, FoodDo> foods = dianCanConfig.getAllFood();

        Map<Integer, FoodDo> foodByTyp = new TreeMap<Integer, FoodDo>();
        for (Integer foodId : foods.keySet()) {
            FoodDo food = foods.get(foodId);
            if (StringUtils.equals(foodType_id, food.getFood_type_id().toString())) {
                foodByTyp.put(foodId, food);
            }
        }
        List<FoodDo> foodList = new ArrayList<FoodDo>();
        for (Integer foodId : foodByTyp.keySet()){
            foodList.add(foodByTyp.get(foodId));
        }

        //paging
        int foodSum = foodByTyp.size();
        int pageNum;
        if (foodSum % 4 == 0) {
            pageNum = (foodSum == 0 ?1:foodSum / BizOptions.FOOD_PAGING);
        } else {
            pageNum = foodSum / BizOptions.FOOD_PAGING + 1;
        }

        if (foodPage > pageNum) {
            logger.error("传入的页数大于实际页数! paraPage:" + foodPage);
            BizUtils.setRspMap(resp, ErrorCodeEnum.DC00003);
            return resp;
        }

        int start = (foodPage - 1) << 2;
        List<List<FoodDo>> needFoods = new ArrayList<List<FoodDo>>();
        for (int i = start; i < (pageNum == foodPage?foodSum:start + 4);i++) {
            FoodDo food = foodList.get(i);
            List<FoodDo> list = new ArrayList<FoodDo>();
            list.add(food);
            needFoods.add(list);
        }

        String resFood = JSON.toJSONString(needFoods);
        resFood = ParserUtil.JsonHandler(resFood);
        resp.put("food_all", resFood);
        resp.put("food_page_num", String.valueOf(pageNum));
        return resp;
    }
}
