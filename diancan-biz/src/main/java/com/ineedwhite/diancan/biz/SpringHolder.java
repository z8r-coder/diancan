package com.ineedwhite.diancan.biz;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author ruanxin
 * @create 2018-03-17
 * @desc
 */
public class SpringHolder implements ApplicationContextAware {
    private static ApplicationContext springContext = new ClassPathXmlApplicationContext("applicationContext.xml");

    public SpringHolder() {
        super();
    }

    public static ApplicationContext getApplicationContext() {
        return springContext;
    }


    public static <T> T getBean(Class<T> type) throws BeansException {
        return springContext.getBean(type);
    }

    public static Object getBean(String name) throws BeansException {
        return springContext.getBean(name);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;
    }
}
