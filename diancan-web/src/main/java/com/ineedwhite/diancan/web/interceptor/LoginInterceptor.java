package com.ineedwhite.diancan.web.interceptor;

import com.ineedwhite.diancan.common.utils.BizUtils;
import com.ineedwhite.diancan.common.utils.DateUtil;
import com.ineedwhite.diancan.dao.dao.LoginLogDao;
import com.ineedwhite.diancan.dao.domain.LoginLogDo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc 访问拦截器拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(LoginInterceptor.class);

    @Autowired
    private LoginLogDao loginLogDao;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String loginDate = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
        String usrIp = BizUtils.getClientIpAddress(httpServletRequest);
        StringBuffer sb = httpServletRequest.getRequestURL();
        String reqUrl;
        if (sb == null) {
            reqUrl = "";
        } else {
            reqUrl = sb.toString();
        }
        LoginLogDo loginLogDo = new LoginLogDo();
        loginLogDo.setLl_ip(usrIp);
        loginLogDo.setLl_login_date(loginDate);
        loginLogDo.setLl_req_url(reqUrl);
        try {
            loginLogDao.insertLoginLog(loginLogDo);
        } catch (Exception ex) {
            logger.error("interceptor occurs exception", ex);
            return false;
        }
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
