package net.jlxxw.wechat.event.netty.properties;

import io.netty.util.NettyRuntime;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置类
 *
 * @author chunyang.leng
 * @date 2021/1/19 5:31 下午
 */
@Configuration
@ConfigurationProperties("wechat.event.netty.server")
public class WeChatEventNettyServerProperties {
    /**
     * 是否启用netty作为微信的核心接口处理器
     */
    private Boolean enable = true;

    /**
     * netty的端口
     */
    private Integer port = 19191;

    /**
     * 队列大小
     */
    private Integer queueSize = 500;

    /**
     * 工作组线程数
     */
    private Integer maxThreadSize = NettyRuntime.availableProcessors() * 2;

    /**
     * 是否开启日志监控
     */
    private boolean enableLog = false;

    /**
     * 是否开启指标分析
     */
    private boolean enableMetrics = false;

    /**
     * 通道连接超时时间，单位：秒
     */
    private int channelTimeout = 15;

    /**
     * 聚合内容的最大长度，单位：字节
     */
    private int httpAggregatorMaxLength = 65535;

    /**
     * netty 主线程名称
     */
    private String mainThreadName = "WeChat-netty-thread-listener";

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public boolean isEnableLog() {
        return enableLog;
    }

    public void setEnableLog(boolean enableLog) {
        this.enableLog = enableLog;
    }

    public boolean isEnableMetrics() {
        return enableMetrics;
    }

    public void setEnableMetrics(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
    }

    public int getChannelTimeout() {
        return channelTimeout;
    }

    public void setChannelTimeout(int channelTimeout) {
        this.channelTimeout = channelTimeout;
    }

    public int getHttpAggregatorMaxLength() {
        return httpAggregatorMaxLength;
    }

    public void setHttpAggregatorMaxLength(int httpAggregatorMaxLength) {
        this.httpAggregatorMaxLength = httpAggregatorMaxLength;
    }

    public String getMainThreadName() {
        return mainThreadName;
    }

    public void setMainThreadName(String mainThreadName) {
        this.mainThreadName = mainThreadName;
    }
}
