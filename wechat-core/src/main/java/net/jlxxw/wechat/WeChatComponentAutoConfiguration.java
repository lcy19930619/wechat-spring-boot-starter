package net.jlxxw.wechat;

import java.util.concurrent.ThreadPoolExecutor;
import net.jlxxw.wechat.controller.WeChatMessageController;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.mapper.TokenMapper;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.schedul.ScheduledUpdateToken;
import net.jlxxw.wechat.schedul.ScheduledUpdateWeChatServerIp;
import net.jlxxw.wechat.security.WeChatServerSecurityCheck;
import net.jlxxw.wechat.util.LoggerUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

/**
 * @author chunyang.leng
 * @date 2021/1/18 9:44 下午
 */
@Configuration
@ComponentScan("net.jlxxw.wechat")
@EnableScheduling
@MapperScan("net.jlxxw.wechat.mapper")
public class WeChatComponentAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(WeChatComponentAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate(){
        int defaultMaxPerRoute = 200;
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        //设置整个连接池最大连接数
        connectionManager.setMaxTotal(2 * defaultMaxPerRoute);
        //路由是对maxTotal的细分
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        RequestConfig requestConfig = RequestConfig.custom()
                //服务器返回数据(response)的时间，超过该时间抛出read timeout
                .setSocketTimeout(10000)
                //连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
                .setConnectTimeout(5000)
                //从连接池中获取连接的超时时间，超过该时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .setConnectionRequestTimeout(1000)
                .build();
        CloseableHttpClient build = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                // 如果不配置connectionManager，则默认使用System.getProperty获取配置参数
                .setConnectionManager(connectionManager)
                .build();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(build));
        LoggerUtils.info(logger,"初始化 RestTemplate");
        return restTemplate;
    }



    /**
     * 批量执行线程池
     *
     * @return
     */
    @Bean("batchExecuteThreadPool")
    public ThreadPoolTaskExecutor batchExecuteThreadPool() {
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
     * 事件总线线程池,用于处理微信回调
     * @see WeChatMessageController#coreController(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
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
    @ConditionalOnProperty(prefix = "we-chat", name = "enable-default-token-manager", havingValue = "true")
    public ScheduledUpdateToken weiXinTokenManager(WeChatTokenManager weChatTokenManager) {
        LoggerUtils.info(logger, "初始化默认token管理器");
        return new ScheduledUpdateToken( weChatTokenManager);
    }

    @Bean
    @ConditionalOnProperty(prefix = "we-chat", name = "enable-we-chat-call-back-server-security-check", havingValue = "true")
    public ScheduledUpdateWeChatServerIp scheduledUpdateWeChatServerIp(
            WeChatTokenManager weChatTokenManager,
            RestTemplate restTemplate,
            WeChatServerSecurityCheck weChatServerSecurityCheck,
            WeChatProperties weChatProperties) {
        LoggerUtils.info(logger, "初始化微信安全检查组件");
        return new ScheduledUpdateWeChatServerIp(weChatTokenManager, restTemplate, weChatServerSecurityCheck, weChatProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "we-chat", name = "enable-we-chat-call-back-server-security-check", havingValue = "true")
    public WeChatServerSecurityCheck weChatServerSecurityCheck() {
        LoggerUtils.info(logger, "启用微信回调ip白名单管理器");
        return new WeChatServerSecurityCheck();
    }
}
