package net.jlxxw.wechat.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置类
 *
 * @author chunyang.leng
 * @date 2021/1/19 5:31 下午
 */
@Configuration
@ConfigurationProperties("we-chat.netty.server")
public class WeChatNettyServerProperties {
    /**
     * 是否启用netty作为微信的核心接口处理器
     */
    private Boolean enableNetty = true;

    /**
     * netty的端口
     */
    private Integer nettyPort = 19191;

    /**
     * 队列大小
     */
    private Integer queueSize = 500;

    /**
     * 工作组线程数
     */
    private Integer maxThreadSize = 200;

    public Boolean getEnableNetty() {
        return enableNetty;
    }

    public void setEnableNetty(Boolean enableNetty) {
        this.enableNetty = enableNetty;
    }

    public Integer getNettyPort() {
        return nettyPort;
    }

    public void setNettyPort(Integer nettyPort) {
        this.nettyPort = nettyPort;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getMaxThreadSize() {
        return maxThreadSize;
    }

    public void setMaxThreadSize(Integer maxThreadSize) {
        this.maxThreadSize = maxThreadSize;
    }
}
