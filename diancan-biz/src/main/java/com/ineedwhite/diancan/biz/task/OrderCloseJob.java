package com.ineedwhite.diancan.biz.task;

import com.ineedwhite.diancan.biz.OrderCloseService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc
 */
@Service
public class OrderCloseJob extends AbstractElasticJob{

    private static final Logger logger = Logger.getLogger(OrderCloseJob.class);

    @Resource
    private OrderCloseService orderCloseService;

    public void doTask() throws Exception {
        try {
            logger.info("关单task开始！");
            orderCloseService.doTask();
            logger.error("关单task结束");
        }catch (Exception ex) {
            logger.error("关单异常",ex);
            throw ex;
        }
    }
}
