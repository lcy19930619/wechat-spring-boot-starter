package net.jlxxw.wechat.repository.mysql;

import net.jlxxw.wechat.repository.jsapi.WeChatJsApiTicketRepository;
import net.jlxxw.wechat.repository.mysql.mapper.TokenMapper;
import net.jlxxw.wechat.repository.mysql.properties.WeChatMySqlProperties;
import net.jlxxw.wechat.repository.mysql.token.WeChatJsApiTicketRepositoryImpl;
import net.jlxxw.wechat.repository.mysql.token.WeChatTokenRepositoryImpl;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * mysql 模块自动装配
 * @author lcy
 */
@Configuration
@MapperScan("net.jlxxw.wechat.repository.mysql.mapper")
@ComponentScan("net.jlxxw.wechat.repository.mysql")
@ConditionalOnProperty(value = "wechat.repository.mysql.enable",havingValue = "true")
public class WeChatRepositoryMySqlAutoConfiguration implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(WeChatRepositoryMySqlAutoConfiguration.class);
    @Autowired
    private WeChatMySqlProperties weChatMysqlProperties;
    @Autowired
    private TokenMapper tokenMapper;


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
            logger.info( "已自动创建 token 表");
        }

        if (weChatMysqlProperties.isEnableAutoCreateTokenTable()) {
            tokenMapper.createTokenTable();
            logger.info( "已自动创建 js_api 表");
        }

    }

    @Bean
    public WeChatTokenRepository weChatTokenRepository() {
        return new WeChatTokenRepositoryImpl(tokenMapper);
    }


    @Bean
    public WeChatJsApiTicketRepository weChatJsApiTicketRepository() {
        return new WeChatJsApiTicketRepositoryImpl(tokenMapper);
    }


}
