package net.jlxxw.wechat.schedule;

import net.jlxxw.wechat.function.ip.IpManager;
import net.jlxxw.wechat.function.token.TokenManager;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.repository.ip.IpSegmentRepository;
import net.jlxxw.wechat.repository.jsapi.WeChatJsApiTicketRepository;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class WeChatScheduleAutoConfiguration {


    @Bean
    public ScheduledUpdateToken scheduledUpdateToken(WeChatTokenRepository weChatTokenRepository,
                                                   TokenManager tokenManager) {
        return new ScheduledUpdateToken(weChatTokenRepository, tokenManager);
    }

    @Bean
    public ScheduledUpdateWeChatServerIp scheduledUpdateWeChatServerIp(
            IpSegmentRepository ipSegmentRepository,
            IpManager ipManager) {
        return new ScheduledUpdateWeChatServerIp(ipSegmentRepository,ipManager);
    }

    @Bean
    public ScheduledUpdateJsApiTicket scheduledUpdateJsApiTicket(WeChatTokenRepository weChatTokenRepository,
                                                                 TokenManager tokenManager,
                                                                 WeChatJsApiTicketRepository weChatJsApiTicketRepository) {
        return new ScheduledUpdateJsApiTicket(weChatTokenRepository, tokenManager,weChatJsApiTicketRepository);
    }

}
