package net.jlxxw.wechat.repository.mysql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author chunyang.leng
 * @date 2024-01-05 16:24
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("table")
@SpringBootTest(classes=WeChatRepositoryMySqlAutoConfiguration.class)
public class CreateTableTest {

    @Autowired
    private BeanFactory beanFactory;

    @Test
    public void testTokenTableExists() throws SQLException {
        DataSource bean = beanFactory.getBean(DataSource.class);
        Connection connection = bean.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select TABLE_NAME from information_schema.TABLES where TABLE_NAME = 'wechat_js_api_ticket'");
        boolean next = resultSet.next();
        Assertions.assertTrue(next,"应该有结果");
        String string = resultSet.getString(1);
        Assertions.assertTrue(StringUtils.isNotBlank(string), "数据库表应该已经被创建");

        ResultSet resultSet2 = statement.executeQuery("select TABLE_NAME from information_schema.TABLES where TABLE_NAME = 'wechat_token'");
        boolean next2 = resultSet2.next();
        Assertions.assertTrue(next2,"应该有结果");
        String string2 = resultSet2.getString(1);
        Assertions.assertTrue(StringUtils.isNotBlank(string2), "数据库表应该已经被创建");
    }


}
