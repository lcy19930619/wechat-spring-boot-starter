package net.jlxxw.wechat.repository.mysql;

import net.jlxxw.wechat.repository.aibot.WeChatAiBotTokenRepository;
import net.jlxxw.wechat.repository.jsapi.WeChatJsApiTicketRepository;
import net.jlxxw.wechat.repository.mysql.properties.WeChatMySqlProperties;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;


/**
 * mysql 模块自动装配
 * 使用 jdbc client 操作数据
 * @author lcy
 */
@Configuration
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
@ConditionalOnClass({ DataSource.class, JdbcTemplate.class })
@ConditionalOnSingleCandidate(DataSource.class)
@ComponentScan("net.jlxxw.wechat.repository.mysql")
@ConditionalOnProperty(value = "wechat.repository.mysql.enable",havingValue = "true")
public class WeChatRepositoryMySqlAutoConfiguration implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(WeChatRepositoryMySqlAutoConfiguration.class);
    @Autowired
    private WeChatMySqlProperties weChatMysqlProperties;
    //@Autowired
    //private TokenMapper tokenMapper;
    @Autowired
    private JdbcClient jdbcClient;
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
            jdbcClient.sql("create table if not exists wechat_js_api_ticket\n" +
                    "(\n" +
                    "    id          bigint(20) auto_increment primary key comment '数据自增主键',\n" +
                    "    ticket       varchar(255) not null comment '从微信获取的ticket',\n" +
                    "    create_time      datetime default now() comment '数据插入时间'\n" +
                    ")\n" +
                    "charset = utf8\n")
                    .update();

           // tokenMapper.createJsApiTicketTable();
            logger.info( "已自动创建 token 表");
        }

        if (weChatMysqlProperties.isEnableAutoCreateTokenTable()) {
            jdbcClient.sql("create table if not exists wechat_token\n" +
                            "(\n" +
                            "    id          bigint(20) auto_increment primary key comment '数据自增主键',\n" +
                            "    token       varchar(255) not null comment '从微信获取的token',\n" +
                            "    create_time      datetime default now() comment '数据插入时间'\n" +
                            ")\n" +
                            "charset = utf8\n")
                    .update();
           // tokenMapper.createTokenTable();
            logger.info( "已自动创建 js_api 表");
        }

    }

    @Bean
    public WeChatTokenRepository weChatTokenRepository() {
        return new WeChatTokenRepository(){
            /**
             * 保存token
             *
             * @param token
             */
            @Override
            public void save(String token) {
                jdbcClient
                        .sql("insert into wechat_token (token) values (:token)")
                        .param("token",token)
                        .update();
            }

            /**
             * 获取保存在本地的token
             *
             * @return 保存在本地的token
             */
            @Override
            public String get() {
                JdbcClient.MappedQuerySpec<String> query = jdbcClient
                        .sql("select token from wechat_token order by id desc limit 1")
                        .query(String.class);
                return query.single();
            }
        };
    }


    @Bean
    public WeChatJsApiTicketRepository weChatJsApiTicketRepository() {
        return new WeChatJsApiTicketRepository(){
            /**
             * 存储数据
             *
             * @param jsApiTicket
             */
            @Override
            public void save(String jsApiTicket) {
                jdbcClient
                        .sql("insert into wechat_js_api_ticket (ticket) values (:ticket)")
                        .param("ticket",jsApiTicket)
                        .update();
            }

            /**
             * 读取数据
             *
             * @return
             */
            @Override
            public String get() {
                JdbcClient.MappedQuerySpec<String> query = jdbcClient
                        .sql("select ticket from wechat_js_api_ticket order by id desc limit 1")
                        .query(String.class);
                return query.single();
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(WeChatAiBotTokenRepository.class)
    public WeChatAiBotTokenRepository weChatAiBotTokenRepository() {
        // 默认返回 null
        return ()-> null;
    }

}
