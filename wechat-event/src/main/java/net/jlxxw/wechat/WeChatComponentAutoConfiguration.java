package net.jlxxw.wechat;

import net.jlxxw.wechat.event.security.WeChatServerSecurityCheck;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chunyang.leng
 * @date 2021/1/18 9:44 下午
 */
@Configuration
@ComponentScan("net.jlxxw.wechat.event")
public class WeChatComponentAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(WeChatComponentAutoConfiguration.class);



    /**
     * 事件总线线程池,用于处理微信回调
     * @return
     */
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

//
//    @Bean
//    @ConditionalOnProperty(prefix = "we-chat", name = "enable-default-token-manager", havingValue = "true")
//    public ScheduledUpdateToken weiXinTokenManager(WeChatTokenManager weChatTokenManager) {
//        LoggerUtils.info(logger, "初始化默认token管理器");
//        return new ScheduledUpdateToken( weChatTokenManager);
//    }
//
//    @Bean
//    @ConditionalOnProperty(prefix = "we-chat", name = "enable-we-chat-call-back-server-security-check", havingValue = "true")
//    public ScheduledUpdateWeChatServerIp scheduledUpdateWeChatServerIp(
//            WeChatTokenManager weChatTokenManager,
//            RestTemplate restTemplate,
//            WeChatServerSecurityCheck weChatServerSecurityCheck,
//            WeChatProperties weChatProperties) {
//        LoggerUtils.info(logger, "初始化微信安全检查组件");
//        return new ScheduledUpdateWeChatServerIp(weChatTokenManager, restTemplate, weChatServerSecurityCheck, weChatProperties);
//    }

    @Bean
    @ConditionalOnProperty(prefix = "we-chat", name = "enable-we-chat-call-back-server-security-check", havingValue = "true")
    public WeChatServerSecurityCheck weChatServerSecurityCheck() {
        LoggerUtils.info(logger, "启用微信回调ip白名单管理器");
        return new WeChatServerSecurityCheck();
    }
}
