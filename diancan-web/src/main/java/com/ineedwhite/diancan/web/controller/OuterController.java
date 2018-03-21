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

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private ContactService contactService;

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ResponseBody
    public String version() {
        return "20180307";
    }

    @RequestMapping(value = "/cancellation", method = RequestMethod.POST)
    public void cancellation(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.CANCELLATION);
            retMap = user.cancellation(paraMap);
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
    @RequestMapping(value = "/loadBoardPage", method = RequestMethod.POST)
    public void loadBoardPage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.LOAD_BOARD_PAGE);
            retMap = board.loadBoardPage(paraMap);
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
    @RequestMapping(value = "/ContactInfo", method = RequestMethod.POST)
    public void ContactInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            retMap = contactService.getContact(paraMap);
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

    @RequestMapping(value = "/orderInfo", method = RequestMethod.POST)
    public void orderInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.ORDER_INFO);
            retMap = orderService.orderInfo(paraMap);
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
    @RequestMapping(value = "/orderWithoutFinish", method = RequestMethod.POST)
    public void orderWithoutFinish(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.ORDER_WITHOUT_FINISH);
            retMap = orderService.orderWithoutFinish(paraMap);
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
    @RequestMapping(value = "/rechargePageLoading", method = RequestMethod.POST)
    public void rechargePageLoading(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.RECHARGE_PAGE_LOADING);
            retMap = rechargeService.rechargePageLoading(paraMap);
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
    @RequestMapping(value = "/historyOrder", method = RequestMethod.POST)
    public void historyOrder(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.HISTORY_ORDER);
            retMap = orderService.historyOrder(paraMap);
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

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public void recharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.RECHARGE);
            retMap = rechargeService.recharge(paraMap);
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
    @RequestMapping(value = "/modifiedUserInfo", method = RequestMethod.POST)
    public void modifiedUserInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.MOD_USER_INFO);
            retMap = user.modifiedUserInfo(paraMap);
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

    @RequestMapping(value = "/getUserDetailInfo", method = RequestMethod.POST)
    public void getUserDetailInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.GET_USER_DETAIL_INFO);
            retMap = user.getUserDetailInfo(paraMap);
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
    @RequestMapping(value = "/getUserCoupon", method = RequestMethod.POST)
    public void getUserCoupon(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.GET_USER_COUPON);
            retMap = user.getUserCoupon(paraMap);
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
    @RequestMapping(value = "/checkOut", method = RequestMethod.POST)
    public void checkOut(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.CHECK_OUT);
            retMap = orderService.checkOut(paraMap);
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
    @RequestMapping(value = "/shoppingCartAddMinus", method = RequestMethod.POST)
    public void shoppingCartAddMinus(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.SHOPPING_CARD_ADD_MINUS);
            retMap = orderService.shoppingCartAddMinus(paraMap);
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
    @RequestMapping(value = "/useCoupon", method = RequestMethod.POST)
    public void useCoupon(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.USE_COUPON);
            retMap = orderService.useCoupon(paraMap);
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

    @RequestMapping(value = "/getCouponList", method = RequestMethod.POST)
    public void getCouponList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.GET_COUPON_LIST);
            retMap = orderService.getCouponList(paraMap);
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
