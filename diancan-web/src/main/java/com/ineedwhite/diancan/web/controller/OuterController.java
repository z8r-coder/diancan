package com.ineedwhite.diancan.web.controller;

import com.ineedwhite.diancan.common.constants.DcException;
import com.ineedwhite.diancan.common.constants.MustNeedPara;
import com.ineedwhite.diancan.common.utils.BizUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc 对外暴露接口
 */
@Controller
@RequestMapping("/outerApi")
public class OuterController {
    private Logger logger = Logger.getLogger(OuterController.class);

    @RequestMapping(value = "/userRegister")
    public void userRegister(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> retMap = null;
        String returnStr;
        String encoding = "UTF-8";
        try {
            Map<String, String> paraMap = BizUtils.getMapFromRequestMap(request.getParameterMap());
            BizUtils.checkMustParam(paraMap, MustNeedPara.REG_MUST_PARAM);
        } catch (DcException ex) {
            logger.error("occur exception " + ex.getErrorCode() + ":" + ex.getErrorMsg(), ex);

        } catch (Throwable t) {

        }
    }
}
