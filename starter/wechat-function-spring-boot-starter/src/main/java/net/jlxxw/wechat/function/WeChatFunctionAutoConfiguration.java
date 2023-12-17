package net.jlxxw.wechat.function;

import net.jlxxw.wechat.util.LoggerUtils;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 微信公众号函数模块自动装配
 */
@Configuration
@ComponentScan("net.jlxxw.wechat.function")
@ConditionalOnProperty(value = "wechat.function.enable",havingValue = "true")
public class WeChatFunctionAutoConfiguration implements ApplicationRunner, BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(WeChatFunctionAutoConfiguration.class);

    private BeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

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
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

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
}
