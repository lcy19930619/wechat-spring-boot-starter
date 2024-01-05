package net.jlxxw.wechat.repository.mysql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author chunyang.leng
 * @date 2024-01-05 16:24
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("table")
@DataJdbcTest
//@SpringBootTest(classes=WeChatRepositoryMySqlAutoConfiguration.class)
public class CreateTableTest {

    @Autowired
    private JdbcClient jdbcClient;
    @Test
    public void testTokenTableExists(){

    }
}
