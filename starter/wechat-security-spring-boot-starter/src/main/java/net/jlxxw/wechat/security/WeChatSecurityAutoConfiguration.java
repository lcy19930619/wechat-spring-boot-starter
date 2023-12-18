package net.jlxxw.wechat.security;

import net.jlxxw.wechat.security.properties.WeChatSecurityProperties;
import net.jlxxw.wechat.security.store.EmbeddedIpSegmentStore;
import net.jlxxw.wechat.security.store.IpSegmentStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
    @ConfigurationProperties("wechat.security")
    public WeChatSecurityProperties weChatSecurityProperties(){
        return new WeChatSecurityProperties();
    }


    @Bean
    public IpSegmentStore ipSegmentStore() {
        return new EmbeddedIpSegmentStore();
    }
}
