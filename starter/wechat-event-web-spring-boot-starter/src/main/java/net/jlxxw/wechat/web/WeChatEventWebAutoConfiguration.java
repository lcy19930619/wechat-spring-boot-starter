package net.jlxxw.wechat.web;

import net.jlxxw.wechat.event.codec.WeChatCiphertextWeChatMessageCodec;
import net.jlxxw.wechat.event.codec.WeChatMessageCodec;
import net.jlxxw.wechat.event.codec.WeChatPlaintextWeChatMessageCodec;
import net.jlxxw.wechat.event.component.EventBus;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatMessageListener;
import net.jlxxw.wechat.exception.AesException;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author chunyang.leng
 * @date 2023-12-19 00:00
 */
@Configuration
@ComponentScan(value = {
        "net.jlxxw.wechat.web",
        "net.jlxxw.wechat.event",
        "net.jlxxw.wechat.properties",
})
public class WeChatEventWebAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(WeChatEventWebAutoConfiguration.class);

    @Bean
    public EventBus eventBus(@Autowired(required = false) List<AbstractWeChatMessageListener> abstractWeChatMessageListeners,
                             @Autowired(required = false) List<AbstractWeChatEventListener> abstractWeChatEventListeners,
                             @Autowired(required = false) UnKnowWeChatEventListener unKnowWeChatEventListener,
                             @Autowired(required = false) UnKnowWeChatMessageListener unKnowWeChatMessageListener,
                             WeChatMessageCodec weChatMessageCodec) {
        LoggerUtils.info(logger, "公众号组件 ---> 事件总线 加载完毕");

        return new EventBus(
                null,
                abstractWeChatMessageListeners,
                abstractWeChatEventListeners,
                unKnowWeChatEventListener,
                unKnowWeChatMessageListener,
                weChatMessageCodec);
    }

    @Bean
    @ConditionalOnProperty(value = "wechat.event.server.web.codec", havingValue = "CIPHER_TEXT")
    public WeChatMessageCodec weChatCiphertextMessageCodec(WeChatProperties weChatProperties) throws AesException {
        LoggerUtils.info(logger, "公众号组件 ---> 编解码器密文模式 加载完毕");
        return new WeChatCiphertextWeChatMessageCodec(weChatProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeChatMessageCodec weChatPlaintextMessageCodec() {
        LoggerUtils.info(logger, "公众号组件 ---> 编解码器明文模式 加载完毕");
        return new WeChatPlaintextWeChatMessageCodec();
    }

}
