package net.jlxxw.wechat.function.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("wechat.function")
public class WeChatFunctionProperties {
    /**
     * 是否启用此模块
     */
    private boolean enable = true;
}
