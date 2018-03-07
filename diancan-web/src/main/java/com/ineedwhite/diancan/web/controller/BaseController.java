package com.ineedwhite.diancan.web.controller;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc 基础controller
 */
public class BaseController {
    private Logger logger = Logger.getLogger(BaseController.class);

    /**
     * 返回resp
     * @param response
     * @param plainStr
     * @param encoding
     */
    protected void writeResult(HttpServletResponse response, String plainStr, String encoding) {
        logger.info("response:" + plainStr);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/plain;charset=" + encoding);
        try {
            response.getWriter().append(plainStr);
            response.getWriter().flush();
        } catch (IOException e) {
            logger.error("writerResult occurs IOException:" + e.getMessage(), e);
        }
    }

    protected void writeResultUtf8(HttpServletResponse response, String plainStr) {
        writeResult(response, plainStr, "UTF-8");
    }
}
