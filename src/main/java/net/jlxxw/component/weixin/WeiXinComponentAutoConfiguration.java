package net.jlxxw.component.weixin;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import net.jlxxw.component.weixin.mapper.TokenMapper;
import net.jlxxw.component.weixin.properties.WeiXinProperties;
import net.jlxxw.component.weixin.schedul.ScheduledUpdateToken;
import net.jlxxw.component.weixin.schedul.ScheduledUpdateWeiXinServerIp;
import net.jlxxw.component.weixin.security.WeiXinServerSecurityCheck;
import org.apache.http.impl.client.HttpClientBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chunyang.leng
 * @date 2021/1/18 9:44 下午
 */
@Configuration
@ComponentScan("net.jlxxw.component.weixin")
@EnableScheduling
@MapperScan("net.jlxxw.component.weixin.mapper")
public class WeiXinComponentAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinComponentAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build()));
    }


    @Bean("pushThreadPool")
    public Executor pushThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //获取到服务器的cpu内核
        int i = Runtime.getRuntime().availableProcessors();
        //核心池大小
        executor.setCorePoolSize(i *2);
        //最大线程数
        executor.setMaxPoolSize(i * 4);
        //队列长度
        executor.setQueueCapacity(60000);
        //线程空闲时间
        executor.setKeepAliveSeconds(1000);
        //线程前缀名称
        executor.setThreadNamePrefix("push-pool-");
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }


    /**
     * 批量执行线程池
     *
     * @return
     */
    @Bean("batchExecuteThreadPool")
    public Executor batchExecuteThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //获取到服务器的cpu内核
        int i = Runtime.getRuntime().availableProcessors();
        //核心池大小
        executor.setCorePoolSize(i);
        //最大线程数
        executor.setMaxPoolSize(i * 2);
        //队列长度
        executor.setQueueCapacity(100000);
        //线程空闲时间
        executor.setKeepAliveSeconds(1000);
        //线程前缀名称
        executor.setThreadNamePrefix("batch-execute-pool-");
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 事件总线线程池
     *
     * @return
     */
    @Bean("eventBusThreadPool")
    public ThreadPoolTaskExecutor eventBusThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //获取到服务器的cpu内核
        int i = Runtime.getRuntime().availableProcessors();
        //核心池大小
        executor.setCorePoolSize(i * 2 - 1);
        //最大线程数
        executor.setMaxPoolSize(i * 4);
        //队列程度
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
    @ConditionalOnProperty(prefix = "weixin", name = "enable-default-token-manager", havingValue = "true")
    public ScheduledUpdateToken weiXinTokenManager(TokenMapper tokenMapper,
                                                   WeiXinTokenManager weiXinTokenManager) {
        logger.info("初始化默认token管理器");
        return new ScheduledUpdateToken(tokenMapper, weiXinTokenManager);
    }

    @Bean
    @ConditionalOnProperty(prefix = "weixin", name = "enable-wei-xin-call-back-server-security-check", havingValue = "true")
    public ScheduledUpdateWeiXinServerIp scheduledUpdateWeiXinServerIp(
            WeiXinTokenManager weiXinTokenManager,
            RestTemplate restTemplate,
            WeiXinServerSecurityCheck weiXinServerSecurityCheck,
            WeiXinProperties weiXinProperties) {
        logger.info("初始化微信安全检查组件");
        return new ScheduledUpdateWeiXinServerIp(weiXinTokenManager, restTemplate, weiXinServerSecurityCheck, weiXinProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "weixin", name = {"enable-wei-xin-call-back-server-security-check"}, havingValue = "true")
    public WeiXinServerSecurityCheck weiXinServerSecurityCheck() {
        logger.info("启用微信回调ip白名单管理器");
        return new WeiXinServerSecurityCheck();
    }



    @Bean
    @ConditionalOnMissingBean(WebClient.class)
    public WebClient webClient(){
        logger.info("初始化WebClient");
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                });
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024)
                        )
                        .build())
                .build();
    }
}
