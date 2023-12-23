package net.jlxxw.wechat.security;

import net.jlxxw.wechat.event.netty.WeChatEventNettyAutoConfiguration;
import net.jlxxw.wechat.event.netty.handler.SecurityHandler;
import net.jlxxw.wechat.repository.ip.IpSegmentRepository;
import net.jlxxw.wechat.security.blacklist.BlackList;
import net.jlxxw.wechat.security.properties.WeChatSecurityProperties;
import net.jlxxw.wechat.security.repository.BlackListRepository;
import net.jlxxw.wechat.security.repository.EmbeddedIpSegmentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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

    /**
     * netty 安全处理器
     * @param ipSegmentRepository
     * @return
     */
    @Bean
    @ConditionalOnBean(WeChatEventNettyAutoConfiguration.class)
    public SecurityHandler securityHandler(IpSegmentRepository ipSegmentRepository, BlackList blackList){
        return new SecurityHandler(ipSegmentRepository,blackList);
    }

    @Bean
    @ConditionalOnMissingBean(BlackList.class)
    public BlackList blackList(WeChatSecurityProperties weChatSecurityProperties) {
        return new BlackListRepository(weChatSecurityProperties.getBlackList());
    }


}
