package net.jlxxw.wechat.repository.mysql;

import net.jlxxw.wechat.repository.aibot.WeChatAiBotTokenRepository;
import net.jlxxw.wechat.repository.jsapi.WeChatJsApiTicketRepository;
import net.jlxxw.wechat.repository.mysql.properties.WeChatMySqlProperties;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * mysql 模块自动装配
 * 使用 jdbc  操作数据
 *
 * @author lcy
 */
@Configuration
@ComponentScan("net.jlxxw.wechat.repository.mysql")
public class WeChatRepositoryMySqlAutoConfiguration implements ApplicationRunner, BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(WeChatRepositoryMySqlAutoConfiguration.class);
    @Autowired
    private WeChatMySqlProperties weChatMysqlProperties;
    private DataSource dataSource;
    private BeanFactory beanFactory;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;

        String dataSourceBeanName = weChatMysqlProperties.getDataSourceBeanName();

        String[] beanNames = defaultListableBeanFactory.getBeanNamesForType(DataSource.class);
        if (beanNames.length == 0) {
            throw new BeanCreationException("未能发现数据源");
        }
        logger.info("发现数据源:{}",String.join(",",beanNames));
        if (beanNames.length > 1) {
            for (String beanName : beanNames) {
                if (beanName.equals(dataSourceBeanName)) {
                    dataSourceBeanName = beanName;
                    break;
                }
            }
        }
        // 可能抛出 找不到bean
        dataSource = defaultListableBeanFactory.getBean(dataSourceBeanName, DataSource.class);
        logger.info("使用数据源:{}",dataSourceBeanName);


        if (weChatMysqlProperties.isEnableAutoCreateJsApiTable()) {
            String sql = load("sql/wechat_js_api_ticket_table.sql");
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            connection.close();
            logger.info("已自动创建 wechat_js_api_ticket 表");
        }

        if (weChatMysqlProperties.isEnableAutoCreateTokenTable()) {
            String sql = load("sql/wechat_token_table.sql");
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            connection.close();
            logger.info("已自动创建 wechat_token 表");
        }

    }

    @Bean
    public WeChatTokenRepository weChatTokenRepository() {
        return new WeChatTokenRepository() {
            private static final String insertSQL = "INSERT INTO wechat_token (`token`) values(?)";

            private static final String selectSQL = "SELECT `token` FROM wechat_token order by id desc limit 1";

            /**
             * 保存token
             *
             * @param token
             */
            @Override
            public void save(String token) {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                try {
                    connection = dataSource.getConnection();
                    connection.setAutoCommit(true);
                    preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, token);
                    preparedStatement.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (SQLException ignore) {

                    }
                }
            }

            /**
             * 获取保存在本地的token
             *
             * @return 保存在本地的token
             */
            @Override
            public String get() {
                try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    boolean next = resultSet.next();
                    if (!next) {
                        throw new IllegalStateException("数据库中不存在 token");
                    }
                    return resultSet.getString(1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }


    @Bean
    public WeChatJsApiTicketRepository weChatJsApiTicketRepository() {
        return new WeChatJsApiTicketRepository() {
            private static final String insertSQL = "INSERT INTO wechat_js_api_ticket (`ticket`) values(?)";

            private static final String selectSQL = "SELECT `ticket` FROM wechat_js_api_ticket order by id desc limit 1";

            /**
             * 存储数据
             *
             * @param jsApiTicket
             */
            @Override
            public void save(String jsApiTicket) {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                try {
                    connection = dataSource.getConnection();
                    connection.setAutoCommit(true);
                    preparedStatement = connection.prepareStatement(insertSQL);
                    preparedStatement.setString(1, jsApiTicket);
                    preparedStatement.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (SQLException ignore) {

                    }
                }
            }

            /**
             * 读取数据
             *
             * @return
             */
            @Override
            public String get() {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    boolean next = resultSet.next();
                    if (!next) {
                        throw new IllegalStateException("数据库中不存在 ticket");
                    }
                    return resultSet.getString(1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(WeChatAiBotTokenRepository.class)
    public WeChatAiBotTokenRepository weChatAiBotTokenRepository() {
        // 默认返回 null
        return () -> null;
    }


    private String load(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        InputStream inputStream = classPathResource.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<String> lines = new ArrayList<String>();

        String line = bufferedReader.readLine();
        while (line != null) {
            lines.add(line);
            line = bufferedReader.readLine();
        }
        return String.join("", lines);
    }
}
