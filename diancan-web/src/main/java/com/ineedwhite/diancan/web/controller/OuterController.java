package com.ineedwhite.diancan.web.controller;

import com.alibaba.fastjson.JSON;
import com.ineedwhite.diancan.biz.User;
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
import java.io.IOException;
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
    private User user;

    @RequestMapping(value = "/version", method = RequestMethod.POST)
    @ResponseBody
    public String version() {
        return "20180307";
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
            retMap.put("rspCode", ex.getErrorCode());
            retMap.put("rspMsg", ex.getErrorMsg());
            returnStr = JSON.toJSONString(retMap);
        } catch (Throwable t) {
            logger.error("occurs Throwable exception:", t);
            retMap.put("rspCode", ErrorCodeEnum.DC00003.getCode());
            retMap.put("rspMsg", ErrorCodeEnum.DC00003.getDesc());
            returnStr = JSON.toJSONString(retMap);
        }

        writeResultUtf8(response, returnStr);
    }
}
