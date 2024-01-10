package net.jlxxw.wechat.repository.mysql;

import net.jlxxw.wechat.repository.jsapi.WeChatJsApiTicketRepository;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author chunyang.leng
 * @date 2024-01-05 16:19
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("table")
@SpringBootTest(classes = WeChatRepositoryMySqlAutoConfiguration.class)
public class WeChatRepositoryMySqlAutoConfigurationTest {
    @Autowired
    private WeChatJsApiTicketRepository weChatJsApiTicketRepository;

    @Autowired
    private WeChatTokenRepository weChatTokenRepository;

    @Test
    @Rollback(value = true)
    @Transactional
    public void tokenTest() throws Exception {
        String token = UUID.randomUUID().toString();
        weChatTokenRepository.save(token);
        String fromDB = weChatTokenRepository.get();
        Assertions.assertEquals(token, fromDB,"两条信息应该是一致的");
    }


    @Test
    @Rollback(value = true)
    @Transactional
    public void ticketTest() throws Exception {
        String ticket = UUID.randomUUID().toString();
        weChatJsApiTicketRepository.save(ticket);
        String fromDB = weChatJsApiTicketRepository.get();
        Assertions.assertEquals(ticket, fromDB,"两条信息应该是一致的");
    }
}
