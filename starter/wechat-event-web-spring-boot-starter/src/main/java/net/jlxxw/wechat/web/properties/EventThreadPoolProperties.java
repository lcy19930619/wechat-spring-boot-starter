package net.jlxxw.wechat.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 事件线程池配置
 * @author lcy
 */
@Configuration
@ConfigurationProperties("wechat.event.server.web.event.pool")
public class EventThreadPoolProperties {
    /**
     * 线程池核心数
     */
    private int core = Runtime.getRuntime().availableProcessors();

    /**
     * 线程池最大核心数
     */
    private int maxCore = core * 4;
    /**
     * 线程名称前缀
     */
    private String threadNamePrefix = "wechat-netty-event-pool";

    /**
     * 线程池队列大小
     */
    private int queueSize = 1000;

    /**
     * 线程活动时间
     */
    private int keepAliveSeconds = 1000;

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getCore() {
        return core;
    }

    public void setCore(int core) {
        this.core = core;
    }

    public int getMaxCore() {
        return maxCore;
    }

    public void setMaxCore(int maxCore) {
        this.maxCore = maxCore;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
