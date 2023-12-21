package net.jlxxw.wechat.event.netty;

import net.jlxxw.wechat.event.component.EventBus;
import net.jlxxw.wechat.event.codec.WeChatMessageCodec;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatMessageListener;
import net.jlxxw.wechat.exception.AesException;
import net.jlxxw.wechat.properties.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 微信公众号事件处理器自动装配
 */
@Configuration
@ComponentScan(value = {
        "net.jlxxw.wechat.event",
        "net.jlxxw.wechat.properties"
})
public class WeChatEventNettyAutoConfiguration {


    @Bean
    public EventBus eventBus(WeChatProperties weChatProperties,
                             ThreadPoolTaskExecutor eventBusThreadPool,
                             @Autowired(required = false) List<AbstractWeChatMessageListener> abstractWeChatMessageListeners,
                             @Autowired(required = false) List<AbstractWeChatEventListener> abstractWeChatEventListeners,
                             @Autowired(required = false) WeChatMessageCodec weChatMessageCodec,
                             @Autowired(required = false) UnKnowWeChatEventListener unKnowWeChatEventListener,
                             @Autowired(required = false) UnKnowWeChatMessageListener unKnowWeChatMessageListener) {
        return new EventBus(weChatProperties,
                eventBusThreadPool,
                abstractWeChatMessageListeners,
                abstractWeChatEventListeners,
                weChatMessageCodec,
                unKnowWeChatEventListener,
                unKnowWeChatMessageListener);
    }

    @Bean("eventBusThreadPool")
    @ConditionalOnMissingBean(name = "eventBusThreadPool")
    public ThreadPoolTaskExecutor eventBusThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //获取到服务器的cpu内核
        int i = Runtime.getRuntime().availableProcessors();
        //核心池大小
        executor.setCorePoolSize(i * 2 - 1);
        //最大线程数
        executor.setMaxPoolSize(i * 4);
        //队列长度
        executor.setQueueCapacity(1000);
        //线程空闲时间
        executor.setKeepAliveSeconds(1000);
        //线程前缀名称
        executor.setThreadNamePrefix("eventBus-execute-pool-");
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }


    @Bean
    public WeChatMessageCodec weChatMsgCodec(WeChatProperties weChatProperties) throws AesException {
        return new WeChatMessageCodec(weChatProperties);
    }
}
