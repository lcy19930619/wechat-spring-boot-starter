package net.jlxxw.wechat.event.netty;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LoggingHandler;
import jakarta.annotation.PostConstruct;
import net.jlxxw.wechat.event.codec.WeChatCiphertextWeChatMessageCodec;
import net.jlxxw.wechat.event.codec.WeChatMessageCodec;
import net.jlxxw.wechat.event.codec.WeChatPlaintextWeChatMessageCodec;
import net.jlxxw.wechat.event.component.EventBus;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatMessageListener;
import net.jlxxw.wechat.event.netty.handler.DefaultHandler;
import net.jlxxw.wechat.event.netty.handler.MessageHandler;
import net.jlxxw.wechat.event.netty.invoke.OtherHttpRequestHandler;
import net.jlxxw.wechat.event.netty.properties.*;
import net.jlxxw.wechat.event.netty.server.WeChatEventNettyServer;
import net.jlxxw.wechat.exception.AesException;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.log.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * @author lcy
 */
@Configuration
@ComponentScan(value = {
        "net.jlxxw.wechat.event",
        "net.jlxxw.wechat.properties"
})
public class WeChatEventNettyAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WeChatEventNettyAutoConfiguration.class);

    @PostConstruct
    private void initialize() {
        LoggerUtils.info(logger,"公众号组件 ---> 事件处理器，netty模式自动装配");
    }

    @Bean
    public EventBus eventBus(@Qualifier("eventBusThreadPool") ThreadPoolTaskExecutor eventBusThreadPool,
                             @Autowired(required = false) List<AbstractWeChatMessageListener> abstractWeChatMessageListeners,
                             @Autowired(required = false) List<AbstractWeChatEventListener> abstractWeChatEventListeners,
                             @Autowired(required = false) UnKnowWeChatEventListener unKnowWeChatEventListener,
                             @Autowired(required = false) UnKnowWeChatMessageListener unKnowWeChatMessageListener,
                             WeChatMessageCodec weChatMessageCodec) {
        LoggerUtils.debug(logger, "公众号组件 ---> EventBus 加载完毕");
        return new EventBus(
                eventBusThreadPool,
                abstractWeChatMessageListeners,
                abstractWeChatEventListeners,
                unKnowWeChatEventListener,
                unKnowWeChatMessageListener,
                weChatMessageCodec);
    }

    /**
     * netty 模式不需要使用虚拟线程，即可达到足够的性能要求，如果使用虚拟线程，反而可能存在性能问题
     * @param eventThreadPoolProperties
     * @return
     */
    @Bean("eventBusThreadPool")
    @ConditionalOnMissingBean(name = "eventBusThreadPool")
    public ThreadPoolTaskExecutor eventBusThreadPool(EventThreadPoolProperties eventThreadPoolProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心池大小
        executor.setCorePoolSize(eventThreadPoolProperties.getCore());
        //最大线程数
        executor.setMaxPoolSize(eventThreadPoolProperties.getMaxCore());
        //队列长度
        executor.setQueueCapacity(eventThreadPoolProperties.getQueueSize());
        //线程空闲时间
        executor.setKeepAliveSeconds(eventThreadPoolProperties.getKeepAliveSeconds());
        //线程前缀名称
        executor.setThreadNamePrefix(eventThreadPoolProperties.getThreadNamePrefix());
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        LoggerUtils.debug(logger, "公众号组件 ---> 事件处理线程池 加载完毕");
        return executor;
    }


    @Bean
    @ConditionalOnProperty(value = "wechat.event.server.netty.codec", havingValue = "CIPHER_TEXT")
    public WeChatMessageCodec weChatCiphertextMessageCodec(WeChatProperties weChatProperties) throws AesException {
        LoggerUtils.info(logger, "公众号组件 ---> 编解码器密文模式 加载完毕");
        return new WeChatCiphertextWeChatMessageCodec(weChatProperties);
    }

    /**
     * 默认明文编码
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public WeChatMessageCodec weChatPlaintextMessageCodec() {
        LoggerUtils.info(logger, "公众号组件 ---> 编解码器明文模式 加载完毕");
        return new WeChatPlaintextWeChatMessageCodec();
    }


    @Bean
    public WeChatEventNettyServer weChatEventNettyServer(WeChatEventNettyServerProperties weChatEventNettyServerProperties,
                                                         List<ChannelHandler> channelHandlerList,
                                                         HttpObjectAggregatorProperties httpObjectAggregatorProperties,
                                                         HttpRequestDecoderProperties httpRequestDecoderProperties,
                                                         IdleStateProperties idleStateProperties) {
        LoggerUtils.info(logger, "公众号组件 ---> Netty 服务器 加载完毕");
        return new WeChatEventNettyServer(weChatEventNettyServerProperties,
                                            channelHandlerList,
                                            httpObjectAggregatorProperties,
                                            httpRequestDecoderProperties,
                                            idleStateProperties);
    }

    @Bean
    @Order(0)
    @ConditionalOnProperty(value = "wechat.event.server.netty.log.enable", havingValue = "true")
    public ChannelHandler loggingHandler(NettyLogProperties nettyLogProperties) {
        LoggerUtils.debug(logger, "公众号组件 ---> Netty 日志跟踪服务 加载完毕,level:{}", nettyLogProperties.getLevel().name());
        return new LoggingHandler(nettyLogProperties.getLevel());
    }


    @Bean
    @Order(3)
    public ChannelHandler messageHandler(EventBus eventBus,
                                         WeChatEventNettyServerProperties weChatEventNettyServerProperties,
                                         WeChatProperties weChatProperties) {
        LoggerUtils.info(logger, "公众号组件 ---> Netty 核心消息处理器 加载完毕");
        return new MessageHandler(eventBus,weChatEventNettyServerProperties,weChatProperties);
    }

    @Bean
    @Order(4)
    public ChannelHandler defaultHandler(OtherHttpRequestHandler otherHttpRequestHandler) {
        LoggerUtils.info(logger, "公众号组件 ---> Netty 消息兜底处理器 加载完毕");
        return new DefaultHandler(otherHttpRequestHandler);
    }

    @Bean
    @ConditionalOnMissingBean(OtherHttpRequestHandler.class)
    public OtherHttpRequestHandler otherHttpRequestHandler() {
        LoggerUtils.warn(logger, "公众号组件 ---> Netty 默认 http 兜底处理器 加载完毕");
        return new OtherHttpRequestHandler() {

            private final Logger logger = LoggerFactory.getLogger(OtherHttpRequestHandler.class);
            @Override
            public FullHttpResponse invoke(FullHttpRequest request) {
                String uri = request.uri();
                LoggerUtils.warn(logger, "公众号组件 ---> 发现不支持的请求,method:{},url:{}，返回 404",request.method().name(), uri);
                return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            }
        };
    }


}
