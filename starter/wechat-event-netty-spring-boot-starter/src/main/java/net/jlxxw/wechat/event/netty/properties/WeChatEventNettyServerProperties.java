package net.jlxxw.wechat.event.netty.properties;

import io.netty.util.NettyRuntime;
import net.jlxxw.wechat.event.enums.Codec;
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
     * 编解码器选择,默认明文方式
     */
    private Codec codec = Codec.PLAIN_TEXT;

    /**
     * 工作组线程数
     */
    private Integer maxThreadSize = NettyRuntime.availableProcessors() * 2;


    /**
     * netty 主线程名称
     */
    private String mainThreadName = "WeChat-netty-thread-listener";

    public String getMainThreadName() {
        return mainThreadName;
    }

    public void setMainThreadName(String mainThreadName) {
        this.mainThreadName = mainThreadName;
    }

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

    public Codec getCodec() {
        return codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    public Integer getMaxThreadSize() {
        return maxThreadSize;
    }

    public void setMaxThreadSize(Integer maxThreadSize) {
        this.maxThreadSize = maxThreadSize;
    }
}
