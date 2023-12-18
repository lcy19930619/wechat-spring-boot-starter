package net.jlxxw.wechat.event.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信事件服务器配置类
 * @author chunyang.leng
 * @date 2023-12-19 00:26
 */
@Configuration
@ConfigurationProperties("wechat.event.server")
public class WeChatEventServerProperties {
}
