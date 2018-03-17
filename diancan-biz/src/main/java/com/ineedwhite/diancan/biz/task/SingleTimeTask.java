package com.ineedwhite.diancan.biz.task;

import com.ineedwhite.diancan.biz.OrderCloseService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ruanxin
 * @create 2018-03-16
 * @desc 单机定时任务
 */
@Service("singleTimeTask")
public class SingleTimeTask {

    private final static Logger logger = Logger.getLogger(SingleTimeTask.class);

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    @Resource
    private OrderCloseService orderCloseService;

    public void init() {
        //关单TASK
        executorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    OrderCloseTask();
                } catch (Exception e) {
                    logger.error("orderCloseService occurs exception!",e);
                }
            }
        },0,5, TimeUnit.MINUTES);
    }

    private void OrderCloseTask() throws Exception{
        try {
            logger.info("关单task开始！");
            orderCloseService.doTask();
            logger.info("关单task结束");
        }catch (Exception ex) {
            logger.error("关单异常",ex);
            throw ex;
        }
    }
}
