package net.jlxxw.wechat.event.netty;

import net.jlxxw.wechat.event.codec.WeChatMessageCodec;
import net.jlxxw.wechat.event.codec.WeChatPlaintextWeChatMessageCodec;
import net.jlxxw.wechat.event.component.EventBus;
import net.jlxxw.wechat.event.codec.WeChatCiphertextWeChatMessageCodec;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatMessageListener;
import net.jlxxw.wechat.event.netty.properties.EventThreadPoolProperties;
import net.jlxxw.wechat.event.netty.properties.WeChatEventNettyServerProperties;
import net.jlxxw.wechat.exception.AesException;
import net.jlxxw.wechat.properties.WeChatProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    private static final Logger logger = LoggerFactory.getLogger(WeChatEventNettyAutoConfiguration.class);

    @Bean
    public EventBus eventBus(ThreadPoolTaskExecutor eventBusThreadPool,
                             @Autowired(required = false) List<AbstractWeChatMessageListener> abstractWeChatMessageListeners,
                             @Autowired(required = false) List<AbstractWeChatEventListener> abstractWeChatEventListeners,
                             @Autowired(required = false) UnKnowWeChatEventListener unKnowWeChatEventListener,
                             @Autowired(required = false) UnKnowWeChatMessageListener unKnowWeChatMessageListener,
                             WeChatMessageCodec weChatMessageCodec) {
        return new EventBus(
                eventBusThreadPool,
                abstractWeChatMessageListeners,
                abstractWeChatEventListeners,
                unKnowWeChatEventListener,
                unKnowWeChatMessageListener,
                weChatMessageCodec);
    }

    @Bean("eventBusThreadPool")
    @ConditionalOnMissingBean(name = "eventBusThreadPool")
    public ThreadPoolTaskExecutor eventBusThreadPool(WeChatEventNettyServerProperties weChatEventNettyServerProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        EventThreadPoolProperties eventThreadPool = weChatEventNettyServerProperties.getEventThreadPool();
        //获取到服务器的cpu内核
        int i = Runtime.getRuntime().availableProcessors();
        //核心池大小
        executor.setCorePoolSize(eventThreadPool.getCore());
        //最大线程数
        executor.setMaxPoolSize(eventThreadPool.getMaxCore());
        //队列长度
        executor.setQueueCapacity(eventThreadPool.getQueueSize());
        //线程空闲时间
        executor.setKeepAliveSeconds(eventThreadPool.getKeepAliveSeconds());
        //线程前缀名称
        executor.setThreadNamePrefix(eventThreadPool.getThreadNamePrefix());
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }


    @Bean
    @ConditionalOnProperty(value = "wechat.codec",havingValue = "CIPHER_TEXT")
    public WeChatMessageCodec weChatCiphertextMessageCodec(WeChatProperties weChatProperties) throws AesException {
        return new WeChatCiphertextWeChatMessageCodec(weChatProperties);
    }

    @Bean
    @ConditionalOnProperty(value = "wechat.codec",havingValue = "PLAIN_TEXT")
    public WeChatMessageCodec weChatPlaintextMessageCodec() {
        return new WeChatPlaintextWeChatMessageCodec();
    }
}
