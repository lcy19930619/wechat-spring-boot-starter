package net.jlxxw.wechat.repository.mysql;

import net.jlxxw.wechat.repository.jsapi.WeChatJsApiTicketRepository;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author chunyang.leng
 * @date 2024-01-05 16:19
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatRepositoryMySqlAutoConfiguration.class)
public class WeChatRepositoryMySqlAutoConfigurationTest {
    @Autowired
    private WeChatJsApiTicketRepository weChatJsApiTicketRepository;

    @Autowired
    private WeChatTokenRepository weChatTokenRepository;

    @Test
    @Rollback(value = true)
    public void testCreateTokenTableTest() throws Exception {

    }
}
