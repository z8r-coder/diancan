package com.ineedwhite.diancan.biz.task;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import org.apache.log4j.Logger;

/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc 抽象JOB
 */
public abstract class AbstractElasticJob extends AbstractSimpleElasticJob {

    private static Logger logger = Logger.getLogger(AbstractElasticJob.class);

    public abstract void doTask() throws Exception;

    @Override
    public void process(JobExecutionMultipleShardingContext shardingContext) {
        String jobName = shardingContext.getJobName();
        logger.info("Starting Job:" + jobName);
        try {
            doTask();
        } catch (Throwable ex) {
            logger.error("JobError:" + jobName, ex);
        } finally {
            logger.info("Exiting Job:" + jobName);
        }
    }
}
