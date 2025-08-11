package net.jlxxw.wechat.function;

import net.jlxxw.wechat.aop.ParamCheckAOP;
import net.jlxxw.wechat.function.ai.AiBotFunction;
import net.jlxxw.wechat.function.api.OpenApiManager;
import net.jlxxw.wechat.function.auth.WebPageAuthorizationManager;
import net.jlxxw.wechat.function.draft.DraftManager;
import net.jlxxw.wechat.function.freepublish.FreePublishManager;
import net.jlxxw.wechat.function.ip.IpManager;
import net.jlxxw.wechat.function.material.MaterialManager;
import net.jlxxw.wechat.function.material.PermanentMaterialManager;
import net.jlxxw.wechat.function.material.TempMaterialManager;
import net.jlxxw.wechat.function.menu.MenuManager;
import net.jlxxw.wechat.function.menu.PersonalizedMenuManager;
import net.jlxxw.wechat.function.properties.WeChatAiBotProperties;
import net.jlxxw.wechat.function.push.SyncPushCustomer;
import net.jlxxw.wechat.function.push.SyncPushTemplate;
import net.jlxxw.wechat.function.qrcode.QrcodeManager;
import net.jlxxw.wechat.function.tag.TagManager;
import net.jlxxw.wechat.function.token.TokenManager;
import net.jlxxw.wechat.function.user.UserManager;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.repository.aibot.WeChatAiBotTokenRepository;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.log.util.LoggerUtils;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 微信公众号函数模块自动装配
 *
 * @author lcy
 */
@Configuration
@ComponentScan("net.jlxxw.wechat.function")
public class WeChatFunctionAutoConfiguration  {
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
        LoggerUtils.info(logger,"公众号组件 ---> 初始化 RestTemplate");
        return restTemplate;
    }

    @Bean
    public ParamCheckAOP paramCheckAOP() {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化 函数参数检查切面");
        return new ParamCheckAOP();
    }

    @Bean
    @ConditionalOnBean(WeChatAiBotProperties.class)
    public AiBotFunction aiBotFunction(RestTemplate restTemplate,
                                       WeChatAiBotTokenRepository weChatAiBotTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 aiBotFunction");
        return new AiBotFunction( restTemplate,weChatAiBotTokenRepository);
    }


    @Bean
    @ConditionalOnBean(value = {WeChatProperties.class, WeChatTokenRepository.class})
    public OpenApiManager openApiManager(RestTemplate restTemplate,
                                         WeChatProperties weChatProperties,
                                         WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 OpenApiManager");
        return new OpenApiManager(weChatProperties, weChatTokenRepository, restTemplate);
    }


    @Bean
    @ConditionalOnBean(WeChatProperties.class)
    public WebPageAuthorizationManager webPageAuthorizationManager(RestTemplate restTemplate,
                                                                   WeChatProperties weChatProperties) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 WebPageAuthorizationManager");
        return new WebPageAuthorizationManager(restTemplate, weChatProperties);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public MaterialManager materialManager(RestTemplate restTemplate,
                                           WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 MaterialManager");
        return new MaterialManager(restTemplate, weChatTokenRepository);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public PermanentMaterialManager permanentMaterialManager(RestTemplate restTemplate,
                                                             WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 PermanentMaterialManager");
        return new PermanentMaterialManager(restTemplate, weChatTokenRepository);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public TempMaterialManager tempMaterialManager(RestTemplate restTemplate,
                                                   WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 TempMaterialManager");
        return new TempMaterialManager(restTemplate, weChatTokenRepository);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public MenuManager menuManager(RestTemplate restTemplate,
                                   WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 MenuManager");
        return new MenuManager(weChatTokenRepository, restTemplate);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public PersonalizedMenuManager personalizedMenuManager(RestTemplate restTemplate,
                                                           WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 PersonalizedMenuManager");
        return new PersonalizedMenuManager(weChatTokenRepository, restTemplate);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public SyncPushCustomer syncPushCustomer(RestTemplate restTemplate,
                                             WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 SyncPushCustomer");
        return new SyncPushCustomer(restTemplate,  weChatTokenRepository);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public SyncPushTemplate syncPushTemplate(RestTemplate restTemplate,
                                             WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 SyncPushTemplate");
        return new SyncPushTemplate(restTemplate,  weChatTokenRepository);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public QrcodeManager qrcodeManager(RestTemplate restTemplate,
                                       WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 QrcodeManager");
        return new QrcodeManager(weChatTokenRepository, restTemplate);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public TagManager tagManager(RestTemplate restTemplate,
                                 WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 TagManager");
        return new TagManager(restTemplate, weChatTokenRepository);
    }

    @Bean
    @ConditionalOnBean(WeChatProperties.class)
    public TokenManager tokenManager(RestTemplate restTemplate,
                                     WeChatProperties weChatProperties) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 TokenManager");
        return new TokenManager(restTemplate, weChatProperties);
    }


    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public UserManager userManager(RestTemplate restTemplate,
                                   WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 UserManager");
        return new UserManager(restTemplate, weChatTokenRepository);
    }

    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public IpManager ipManager(RestTemplate restTemplate,
                               WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 IpManager");
        return new IpManager(restTemplate, weChatTokenRepository);
    }

    /**
     * 默认的 ai 机器人 token 存储器
     * @param weChatAiBotProperties
     * @return
     */
    @Bean
    @ConditionalOnBean(WeChatAiBotProperties.class)
    public WeChatAiBotTokenRepository weChatAiBotTokenRepository(WeChatAiBotProperties weChatAiBotProperties) {
        return weChatAiBotProperties::getToken;
    }

    /**
     * 草稿管理
     * @param restTemplate
     * @param weChatTokenRepository
     * @return
     */
    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public DraftManager draftManager(RestTemplate restTemplate,
                                     WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 IpManager");
        return new DraftManager(restTemplate, weChatTokenRepository);
    }

    /**
     * 发布管理
     * @param restTemplate
     * @param weChatTokenRepository
     * @return
     */
    @Bean
    @ConditionalOnBean(WeChatTokenRepository.class)
    public FreePublishManager freePublishManager(RestTemplate restTemplate,
                                                 WeChatTokenRepository weChatTokenRepository) {
        LoggerUtils.info(logger,"公众号组件 ---> 初始化函数 IpManager");
        return new FreePublishManager(restTemplate, weChatTokenRepository);
    }

}
