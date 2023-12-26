package net.jlxxw.wechat.event.netty.properties;

import io.netty.handler.logging.LogLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("wechat.event.server.netty.log")
public class NettyLogProperties {

    /**
     * 是否启动netty 日志
     */
    private boolean enable = false;

    /**
     * 日志级别
     */
    private LogLevel level = LogLevel.INFO;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }
}
