package net.jlxxw.wechat.schedule;

import net.jlxxw.wechat.function.token.TokenManager;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeChatScheduleAutoConfiguration {


    @Bean
    @ConditionalOnProperty(prefix = "we-chat", name = "enable-default-token-manager", havingValue = "true")
    public ScheduledUpdateToken weiXinTokenManager(WeChatTokenRepository weChatTokenRepository,
                                                   TokenManager tokenManager) {
        return new ScheduledUpdateToken(weChatTokenRepository, tokenManager);
    }

    @Bean
    @ConditionalOnProperty(prefix = "we-chat", name = "enable-we-chat-call-back-server-security-check", havingValue = "true")
    public ScheduledUpdateWeChatServerIp scheduledUpdateWeChatServerIp(
            WeChatTokenRepository weChatTokenRepository,
            RestTemplate restTemplate,
            WeChatProperties weChatProperties) {
        return new ScheduledUpdateWeChatServerIp(weChatTokenRepository, restTemplate, weChatServerSecurityCheck, weChatProperties);
    }
}
