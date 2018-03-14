package com.ineedwhite.diancan.web.controller;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.*;
import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.constants.DcException;
import com.ineedwhite.diancan.common.constants.MustNeedPara;
import com.ineedwhite.diancan.common.utils.BizUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc 对外暴露接口
 */
@Controller
@RequestMapping("/outerApi")
public class OuterController extends BaseController {
    private Logger logger = Logger.getLogger(OuterController.class);

    @Autowired
    private UserService user;

    @Autowired
    private BoardService board;

    @Autowired
    private FoodTypeService foodType;

    @Autowired
    private FoodService food;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ResponseBody
    public String version() {
        return "20180307";
    }

    @RequestMapping(value = "/shoppingCart", method = RequestMethod.POST)
    public void shoppingCart(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap,MustNeedPara.SHOPPING_CART);
            retMap = orderService.shoppingCart(paraMap);
            returnStr = JSON.toJSONString(retMap);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ex);
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ErrorCodeEnum.DC00003);
            returnStr = JSON.toJSONString(retMap);
        }
        writeResultUtf8(response, returnStr);
    }

    @RequestMapping(value = "/addFood", method = RequestMethod.POST)
    public void addFood(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.ADD_FOOD);
            retMap = orderService.addFoodToShoppingCart(paraMap);
            returnStr = JSON.toJSONString(retMap);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ex);
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ErrorCodeEnum.DC00003);
            returnStr = JSON.toJSONString(retMap);
        }
        writeResultUtf8(response, returnStr);
    }

    @RequestMapping(value = "/getUsrInfo", method = RequestMethod.POST)
    public void getUsrInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.GET_USR_INFO);
            retMap = user.userInfo(paraMap);
            returnStr = JSON.toJSONString(retMap);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ex);
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ErrorCodeEnum.DC00003);
            returnStr = JSON.toJSONString(retMap);
        }
        writeResultUtf8(response, returnStr);
    }
    @RequestMapping(value = "/getFoodByType", method = RequestMethod.POST)
    public void getFoodByType(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.GET_FOOD_PARAM);
            retMap = food.getFoodByType(paraMap);
            returnStr = JSON.toJSONString(retMap);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ex);
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ErrorCodeEnum.DC00003);
            returnStr = JSON.toJSONString(retMap);
        }
        writeResultUtf8(response, returnStr);
    }

    @RequestMapping(value = "/getAllFoodType", method = RequestMethod.POST)
    public void getAllFoodType(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            retMap = foodType.getAllFoodType();
            returnStr = JSON.toJSONString(retMap);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ex);
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ErrorCodeEnum.DC00003);
            returnStr = JSON.toJSONString(retMap);
        }
        writeResultUtf8(response, returnStr);
    }

    @RequestMapping(value = "/reserveBoard", method = RequestMethod.POST)
    public void reserveBoard(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.RESERVE_BOARD_PARAM);
            retMap = board.reserveBoard(paraMap);
            returnStr = JSON.toJSONString(retMap);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ex);
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ErrorCodeEnum.DC00003);
            returnStr = JSON.toJSONString(retMap);
        }
        writeResultUtf8(response, returnStr);
    }

    @RequestMapping(value = "/getAvailableBoard", method = RequestMethod.POST)
    public void getAvailableBoard(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.GET_BOARD_PARAM);
            retMap = board.getAvailableBoard(paraMap);
            returnStr = JSON.toJSONString(retMap);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ex);
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ErrorCodeEnum.DC00003);
            returnStr = JSON.toJSONString(retMap);
        }
        writeResultUtf8(response, returnStr);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void userLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.LOGIN_MUST_PARAM);
            retMap = user.login(paraMap);
            returnStr = JSON.toJSONString(retMap);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ex);
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ErrorCodeEnum.DC00003);
            returnStr = JSON.toJSONString(retMap);
        }
        writeResultUtf8(response, returnStr);
    }

    @RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    public void userRegister(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.REG_MUST_PARAM);
            retMap = user.register(paraMap);
            returnStr = JSON.toJSONString(retMap);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ex);
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap = new HashMap<String, String>();
            BizUtils.setRspMap(retMap, ErrorCodeEnum.DC00003);
            returnStr = JSON.toJSONString(retMap);
        }
        writeResultUtf8(response, returnStr);
    }
}
