package net.jlxxw.wechat.event.netty.properties;

import io.netty.util.NettyRuntime;
import net.jlxxw.wechat.event.properties.WeChatEventServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置类
 *
 * @author chunyang.leng
 * @date 2021/1/19 5:31 下午
 */
@Configuration
@ConfigurationProperties("wechat.event.server.netty")
public class WeChatEventNettyServerProperties extends WeChatEventServerProperties {
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
     * 事件处理线程池配置
     */
    private EventThreadPoolProperties eventThreadPool;

    /**
     * netty 日志
     */
    private NettyLogProperties log;
    /**
     * netty 指标
     */
    private NettyMetricsProperties metrics;

    /**
     * http 请求解码器配置
     */
    private HttpRequestDecoderProperties httpRequestDecoder;

    /**
     * http 聚合器
     */
    private HttpObjectAggregatorProperties httpObjectAggregator;

    /**
     * 读取超时配置
     */
    private IdleStateProperties idleState;

    public IdleStateProperties getIdleState() {
        return idleState;
    }

    public void setIdleState(IdleStateProperties idleState) {
        this.idleState = idleState;
    }

    public HttpObjectAggregatorProperties getHttpObjectAggregator() {
        return httpObjectAggregator;
    }

    public void setHttpObjectAggregator(HttpObjectAggregatorProperties httpObjectAggregator) {
        this.httpObjectAggregator = httpObjectAggregator;
    }

    public HttpRequestDecoderProperties getHttpRequestDecoder() {
        return httpRequestDecoder;
    }

    public void setHttpRequestDecoder(HttpRequestDecoderProperties httpRequestDecoder) {
        this.httpRequestDecoder = httpRequestDecoder;
    }

    public NettyLogProperties getLog() {
        return log;
    }

    public void setLog(NettyLogProperties log) {
        this.log = log;
    }

    public NettyMetricsProperties getMetrics() {
        return metrics;
    }

    public void setMetrics(NettyMetricsProperties metrics) {
        this.metrics = metrics;
    }

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

    public EventThreadPoolProperties getEventThreadPool() {
        return eventThreadPool;
    }

    public void setEventThreadPool(EventThreadPoolProperties eventThreadPool) {
        this.eventThreadPool = eventThreadPool;
    }
}
