package net.jlxxw.wechat.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-04-23 9:49 上午
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public <T> T getBean(Class<T> clazz, String beanName) {
        return applicationContext.getBean(clazz, beanName);
    }

    /**
     * 发布一个事件
     *
     * @param applicationEvent
     */
    public void publishEvent(ApplicationEvent applicationEvent) {
        applicationContext.publishEvent(applicationEvent);
    }
}
