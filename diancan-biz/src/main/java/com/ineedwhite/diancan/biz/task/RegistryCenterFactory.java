package com.ineedwhite.diancan.biz.task;

import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import com.ineedwhite.diancan.common.utils.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author ruanxin
 * @create 2018-03-13
 * @desc 分布式作业注册中心工厂类
 */
public class RegistryCenterFactory implements FactoryBean<CoordinatorRegistryCenter> {

    private static Logger logger = Logger.getLogger(RegistryCenterFactory.class);

    private static String zookeeperAddress;
    private static int baseSleepTimeMilliseconds = 1000;
    private static int maxSleepTimeMilliseconds = 3000;
    private static int maxRetries = 3;
    private static String jobNameSpace = "diancan-job";

    static {
        zookeeperAddress = PropertiesUtil.getStringValue("zookeeper.address");
        logger.info("ESJob配置-ElasticJob用的zookeeper.address=" + zookeeperAddress + ", jobNameSpace=" + jobNameSpace);
    }

    public CoordinatorRegistryCenter getObject() throws Exception {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(zookeeperAddress,
                jobNameSpace, baseSleepTimeMilliseconds, maxSleepTimeMilliseconds, maxRetries);

        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zkConfig);
        regCenter.init();
        return regCenter;
    }

    public Class<?> getObjectType() {
        return CoordinatorRegistryCenter.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
