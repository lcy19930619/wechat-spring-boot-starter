package net.jlxxw.wechat.event.netty;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import net.jlxxw.wechat.event.codec.WeChatMessageCodec;
import net.jlxxw.wechat.event.codec.WeChatPlaintextWeChatMessageCodec;
import net.jlxxw.wechat.event.component.EventBus;
import net.jlxxw.wechat.event.codec.WeChatCiphertextWeChatMessageCodec;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatMessageListener;
import net.jlxxw.wechat.event.netty.handler.MetricsHandler;
import net.jlxxw.wechat.event.netty.properties.*;
import net.jlxxw.wechat.event.netty.server.WeChatEventNettyServer;
import net.jlxxw.wechat.exception.AesException;
import net.jlxxw.wechat.properties.WeChatProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 微信公众号事件处理器自动装配
 */
@Configuration
@ComponentScan(value = {
        "net.jlxxw.wechat.event",
        "net.jlxxw.wechat.properties"
})
public class WeChatEventNettyAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WeChatEventNettyAutoConfiguration.class);

    @Bean
    public EventBus eventBus(ThreadPoolTaskExecutor eventBusThreadPool,
                             @Autowired(required = false) List<AbstractWeChatMessageListener> abstractWeChatMessageListeners,
                             @Autowired(required = false) List<AbstractWeChatEventListener> abstractWeChatEventListeners,
                             @Autowired(required = false) UnKnowWeChatEventListener unKnowWeChatEventListener,
                             @Autowired(required = false) UnKnowWeChatMessageListener unKnowWeChatMessageListener,
                             WeChatMessageCodec weChatMessageCodec) {
        return new EventBus(
                eventBusThreadPool,
                abstractWeChatMessageListeners,
                abstractWeChatEventListeners,
                unKnowWeChatEventListener,
                unKnowWeChatMessageListener,
                weChatMessageCodec);
    }

    @Bean("eventBusThreadPool")
    @ConditionalOnMissingBean(name = "eventBusThreadPool")
    public ThreadPoolTaskExecutor eventBusThreadPool(WeChatEventNettyServerProperties weChatEventNettyServerProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        EventThreadPoolProperties eventThreadPool = weChatEventNettyServerProperties.getEventThreadPool();
        //获取到服务器的cpu内核
        int i = Runtime.getRuntime().availableProcessors();
        //核心池大小
        executor.setCorePoolSize(eventThreadPool.getCore());
        //最大线程数
        executor.setMaxPoolSize(eventThreadPool.getMaxCore());
        //队列长度
        executor.setQueueCapacity(eventThreadPool.getQueueSize());
        //线程空闲时间
        executor.setKeepAliveSeconds(eventThreadPool.getKeepAliveSeconds());
        //线程前缀名称
        executor.setThreadNamePrefix(eventThreadPool.getThreadNamePrefix());
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }


    @Bean
    @ConditionalOnProperty(value = "wechat.codec",havingValue = "CIPHER_TEXT")
    public WeChatMessageCodec weChatCiphertextMessageCodec(WeChatProperties weChatProperties) throws AesException {
        return new WeChatCiphertextWeChatMessageCodec(weChatProperties);
    }

    @Bean
    @ConditionalOnProperty(value = "wechat.codec",havingValue = "PLAIN_TEXT")
    public WeChatMessageCodec weChatPlaintextMessageCodec() {
        return new WeChatPlaintextWeChatMessageCodec();
    }


    @Bean
    public WeChatEventNettyServer weChatEventNettyServer(WeChatEventNettyServerProperties weChatEventNettyServerProperties,
                                                         List<ChannelHandler> channelHandlerList) {
        return new WeChatEventNettyServer(weChatEventNettyServerProperties,channelHandlerList);
    }

    @Bean
    @Order(0)
    @ConditionalOnProperty(value = "wechat.event.server.netty.log.enable",havingValue = "true")
    public ChannelHandler loggingHandler(WeChatEventNettyServerProperties weChatEventNettyServerProperties) {
        NettyLogProperties nettyLog = weChatEventNettyServerProperties.getLog();
        return new LoggingHandler(nettyLog.getLevel());
    }

    /**
     * 指标采集监控
     * @param weChatEventNettyServerProperties
     * @return
     */
    @Bean
    @Order(1)
    @ConditionalOnProperty(value = "wechat.event.server.netty.metrics.enable",havingValue = "true")
    public ChannelHandler metricsHandler(WeChatEventNettyServerProperties weChatEventNettyServerProperties) {
        NettyMetricsProperties metrics = weChatEventNettyServerProperties.getMetrics();
        return new MetricsHandler(metrics);
    }


    /**
     * 请求解码器
     * @param weChatEventNettyServerProperties
     * @return
     */
    @Bean
    @Order(2)
    public ChannelHandler httpRequestDecoder(WeChatEventNettyServerProperties weChatEventNettyServerProperties) {
        HttpRequestDecoderProperties decoder = weChatEventNettyServerProperties.getHttpRequestDecoder();
        return new HttpRequestDecoder(decoder.getMaxInitialLineLength(),decoder.getMaxHeaderSize(),decoder.getMaxChunkSize());
    }


    /**
     * 聚合器
     * @param weChatEventNettyServerProperties
     * @return
     */
    @Bean
    @Order(3)
    public ChannelHandler httpObjectAggregator(WeChatEventNettyServerProperties weChatEventNettyServerProperties) {
        HttpObjectAggregatorProperties httpObjectAggregator = weChatEventNettyServerProperties.getHttpObjectAggregator();
        return new HttpObjectAggregator(httpObjectAggregator.getMaxContentLength());
    }

    /**
     * 应答编码器
     * @return
     */
    @Bean
    @Order(4)
    public ChannelHandler httpResponseEncoder() {
        return new HttpResponseEncoder();
    }

    /**
     * 分块
     * @return
     */
    @Bean
    @Order(5)
    public ChannelHandler chunkedWriteHandler() {
        return new ChunkedWriteHandler();
    }


    /**
     * 空闲检测
     * @param weChatEventNettyServerProperties
     * @return
     */
    @Bean
    @Order(6)
    public ChannelHandler idleStateHandler(WeChatEventNettyServerProperties weChatEventNettyServerProperties) {
        IdleStateProperties idleState = weChatEventNettyServerProperties.getIdleState();
        return new IdleStateHandler(idleState.getReaderIdleTimeSeconds(),idleState.getWriterIdleTimeSeconds(),idleState.getAllIdleTimeSeconds());
    }



}
