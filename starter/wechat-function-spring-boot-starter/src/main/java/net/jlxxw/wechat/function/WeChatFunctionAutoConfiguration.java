package net.jlxxw.wechat.function;

import net.jlxxw.wechat.aop.ParamCheckAOP;
import net.jlxxw.wechat.component.BatchExecutor;
import net.jlxxw.wechat.function.ai.AiBotFunction;
import net.jlxxw.wechat.function.ai.properties.WeChatAiBotProperties;
import net.jlxxw.wechat.function.api.OpenApiManager;
import net.jlxxw.wechat.function.auth.WebPageAuthorizationManager;
import net.jlxxw.wechat.function.ip.IpManager;
import net.jlxxw.wechat.function.material.MaterialManager;
import net.jlxxw.wechat.function.material.PermanentMaterialManager;
import net.jlxxw.wechat.function.material.TempMaterialManager;
import net.jlxxw.wechat.function.menu.MenuManager;
import net.jlxxw.wechat.function.menu.PersonalizedMenuManager;
import net.jlxxw.wechat.function.push.SyncPushCustomer;
import net.jlxxw.wechat.function.push.SyncPushTemplate;
import net.jlxxw.wechat.function.qrcode.QrcodeManager;
import net.jlxxw.wechat.function.tag.TagManager;
import net.jlxxw.wechat.function.token.TokenManager;

import net.jlxxw.wechat.function.user.UserManager;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 微信公众号函数模块自动装配
 *
 * @author lcy
 */
@Configuration
@ComponentScan("net.jlxxw.wechat.function")
@ConditionalOnProperty(value = "wechat.function.enable", havingValue = "true")
public class WeChatFunctionAutoConfiguration implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(WeChatFunctionAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
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
        logger.info("初始化 RestTemplate");
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


    @Bean
    public ParamCheckAOP paramCheckAOP() {
        return new ParamCheckAOP();
    }

    @Bean
    @ConditionalOnBean(WeChatAiBotProperties.class)
    public AiBotFunction aiBotFunction(RestTemplate restTemplate,
                                       WeChatAiBotProperties aiBotProperties) {
        return new AiBotFunction(aiBotProperties, restTemplate);
    }


    @Bean
    @ConditionalOnBean(value = {WeChatProperties.class, WeChatTokenRepository.class})
    public OpenApiManager openApiManager(RestTemplate restTemplate,
                                         WeChatProperties weChatProperties,
                                         WeChatTokenRepository weChatTokenRepository) {
        return new OpenApiManager(weChatProperties, weChatTokenRepository, restTemplate);
    }


    @Bean
    @ConditionalOnBean(WeChatProperties.class)
    public WebPageAuthorizationManager webPageAuthorizationManager(RestTemplate restTemplate,
                                                                   WeChatProperties weChatProperties) {
        return new WebPageAuthorizationManager(restTemplate, weChatProperties);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public MaterialManager materialManager(RestTemplate restTemplate,
                                           WeChatTokenRepository weChatTokenRepository) {
        return new MaterialManager(restTemplate, weChatTokenRepository);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public PermanentMaterialManager permanentMaterialManager(RestTemplate restTemplate,
                                                             WeChatTokenRepository weChatTokenRepository) {
        return new PermanentMaterialManager(restTemplate, weChatTokenRepository);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public TempMaterialManager tempMaterialManager(RestTemplate restTemplate,
                                                   WeChatTokenRepository weChatTokenRepository) {
        return new TempMaterialManager(restTemplate, weChatTokenRepository);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public MenuManager menuManager(RestTemplate restTemplate,
                                   WeChatTokenRepository weChatTokenRepository) {
        return new MenuManager(weChatTokenRepository, restTemplate);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public PersonalizedMenuManager personalizedMenuManager(RestTemplate restTemplate,
                                                           WeChatTokenRepository weChatTokenRepository) {
        return new PersonalizedMenuManager(weChatTokenRepository, restTemplate);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public SyncPushCustomer syncPushCustomer(RestTemplate restTemplate,
                                             BatchExecutor batchExecutor,
                                             WeChatTokenRepository weChatTokenRepository) {
        return new SyncPushCustomer(restTemplate, batchExecutor, weChatTokenRepository);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public SyncPushTemplate syncPushTemplate(RestTemplate restTemplate,
                                             BatchExecutor batchExecutor,
                                             WeChatTokenRepository weChatTokenRepository) {
        return new SyncPushTemplate(restTemplate, batchExecutor, weChatTokenRepository);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public QrcodeManager qrcodeManager(RestTemplate restTemplate,
                                       WeChatTokenRepository weChatTokenRepository) {
        return new QrcodeManager(weChatTokenRepository, restTemplate);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public TagManager tagManager(RestTemplate restTemplate,
                                 WeChatTokenRepository weChatTokenRepository) {
        return new TagManager(restTemplate, weChatTokenRepository);
    }

    @Bean
    @ConditionalOnBean(WeChatProperties.class)
    public TokenManager tokenManager(RestTemplate restTemplate,
                                     WeChatProperties weChatProperties) {
        return new TokenManager(restTemplate, weChatProperties);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public UserManager userManager(RestTemplate restTemplate,
                                   WeChatTokenRepository weChatTokenRepository,
                                   BatchExecutor batchExecutor) {
        return new UserManager(restTemplate, weChatTokenRepository, batchExecutor);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public IpManager ipManager(RestTemplate restTemplate,
                               WeChatTokenRepository weChatTokenRepository) {
        return new IpManager(restTemplate, weChatTokenRepository);
    }

}
