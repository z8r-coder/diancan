package com.ineedwhite.diancan.biz.task;

import org.apache.log4j.Logger;

/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc
 */
public class OrderCloseJob extends AbstractElasticJob{

    private static final Logger logger = Logger.getLogger(OrderCloseJob.class);

    public void doTask() throws Exception {
        logger.info("begin to close order!");

    }
}
