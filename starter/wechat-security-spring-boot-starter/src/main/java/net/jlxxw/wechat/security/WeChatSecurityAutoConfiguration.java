package net.jlxxw.wechat.security;

import net.jlxxw.wechat.repository.ip.IpSegmentRepository;
import net.jlxxw.wechat.security.repository.EmbeddedIpSegmentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 安全服务自动装配
 * @author chunyang.leng
 * @date 2023-12-19 04:19
 */
@Configuration
@ConditionalOnProperty(value = "wechat.security.enable",havingValue = "true")
@ComponentScan("net.jlxxw.wechat.security")
public class WeChatSecurityAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(IpSegmentRepository.class)
    public IpSegmentRepository ipSegmentRepository() {
        return new EmbeddedIpSegmentRepository();
    }
}
