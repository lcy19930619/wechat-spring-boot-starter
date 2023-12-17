package net.jlxxw.wechat.mysql;

import net.jlxxw.wechat.function.token.TokenManager;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.mysql.mapper.TokenMapper;
import net.jlxxw.wechat.mysql.properties.WeChatMySqlProperties;
import net.jlxxw.wechat.mysql.token.DefaultWeChatTokenManagerImpl;
import net.jlxxw.wechat.util.LoggerUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * mysql 模块自动装配
 */
@Configuration
@MapperScan("net.jlxxw.wechat.mysql.mapper")
@ComponentScan("net.jlxxw.wechat.mysql")
@ConditionalOnProperty(value = "wechat.mysql.enable",havingValue = "true")
public class WeChatDefaultMySqlAutoConfiguration implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(WeChatDefaultMySqlAutoConfiguration.class);
    @Autowired
    private WeChatMySqlProperties weChatMysqlProperties;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private BeanFactory beanFactory;


    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (weChatMysqlProperties.isEnableAutoCreateJsApiTable()) {
            // 自动创建 js api 表
            tokenMapper.createJsApiTicketTable();
            LoggerUtils.info(logger, "已自动创建 token 表");
        }

        if (weChatMysqlProperties.isEnableAutoCreateTokenTable()) {
            tokenMapper.createTokenTable();
            LoggerUtils.info(logger, "已自动创建 js_api 表");
        }

    }

    @Bean
    public WeChatTokenManager weChatTokenManager() {
        return new DefaultWeChatTokenManagerImpl(tokenMapper,tokenManager);
    }


}
