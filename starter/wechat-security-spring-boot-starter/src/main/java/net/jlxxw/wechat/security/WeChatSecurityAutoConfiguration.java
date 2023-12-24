package net.jlxxw.wechat.security;

import jakarta.servlet.http.HttpServletRequest;
import net.jlxxw.wechat.event.netty.WeChatEventNettyAutoConfiguration;
import net.jlxxw.wechat.event.netty.handler.SecurityHandler;
import net.jlxxw.wechat.repository.ip.IpSegmentRepository;
import net.jlxxw.wechat.security.blacklist.BlackList;
import net.jlxxw.wechat.security.properties.WeChatSecurityProperties;
import net.jlxxw.wechat.security.repository.BlackListRepository;
import net.jlxxw.wechat.security.repository.EmbeddedIpSegmentRepository;
import net.jlxxw.wechat.security.template.SecurityFilterTemplate;
import net.jlxxw.wechat.web.filter.WeChatSecurityFilter;
import net.jlxxw.wechat.web.properties.WeChatEventWebProperties;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * spring boot 3
     * @param blackList
     * @param ipSegmentRepository
     * @return
     */
    @Bean
    @ConditionalOnClass(jakarta.servlet.http.HttpServletRequest.class)
    public SecurityFilterTemplate weChatSecurityFilter(BlackList blackList, IpSegmentRepository ipSegmentRepository) {
        return new WeChatSecurityFilter(blackList,ipSegmentRepository);
    }


    @Bean
    @ConditionalOnWebApplication
    public FilterRegistrationBean<WeChatSecurityFilter> weChatSecutityFilterRegistrationBean(SecurityFilterTemplate weChatSecurityFilter,
                                                                                             WeChatEventWebProperties weChatEventWebProperties) {

        String coreControllerUrl = weChatEventWebProperties.getCoreControllerUrl();
        FilterRegistrationBean<WeChatSecurityFilter> registration = new FilterRegistrationBean<>();
        WeChatSecurityFilter filter = (WeChatSecurityFilter)weChatSecurityFilter;
        registration.setFilter(filter);
        if (coreControllerUrl.startsWith("/")) {
            registration.addUrlPatterns(coreControllerUrl);
        }else {
            registration.addUrlPatterns("/"+coreControllerUrl);
        }
        // 优先级
        registration.setOrder(-1);
        return registration;

    }

}
