package net.jlxxw.wechat.repository.mysql;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author chunyang.leng
 * @date 2024-01-05 16:24
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("table")
@SpringBootTest(classes=WeChatRepositoryMySqlAutoConfiguration.class)
public class CreateTableTest {

    @Test
    public void testTokenTableExists(){

    }
}
