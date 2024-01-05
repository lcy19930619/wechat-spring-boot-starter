package net.jlxxw.wechat.event.netty.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * netty channel 空闲检测
 * @author lcy
 */
@Configuration
@ConfigurationProperties("wechat.event.server.netty.channel.idle")
public class IdleStateProperties {
    /**
     * 读空闲，单位：秒
     */
    private int readerIdleTimeSeconds = 15 ;
    /**
     * 写空闲，单位：秒
     */
    private int writerIdleTimeSeconds = 15 ;
    /**
     * 全部空闲，单位：秒
     */
    private int allIdleTimeSeconds = 15 ;

    public int getReaderIdleTimeSeconds() {
        return readerIdleTimeSeconds;
    }

    public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
        this.readerIdleTimeSeconds = readerIdleTimeSeconds;
    }

    public int getWriterIdleTimeSeconds() {
        return writerIdleTimeSeconds;
    }

    public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
        this.writerIdleTimeSeconds = writerIdleTimeSeconds;
    }

    public int getAllIdleTimeSeconds() {
        return allIdleTimeSeconds;
    }

    public void setAllIdleTimeSeconds(int allIdleTimeSeconds) {
        this.allIdleTimeSeconds = allIdleTimeSeconds;
    }
}
