package net.jlxxw.wechat.web.properties;

import net.jlxxw.wechat.event.enums.Codec;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lcy
 */
@Configuration
@ConfigurationProperties("wechat.event.server.web")
public class WeChatEventWebProperties {

    /**
     * 核心控制器 url
     */
    private String coreControllerUrl = "/weChat";

    /**
     * 编解码方式
     */
    private Codec codec = Codec.PLAIN_TEXT;

    public Codec getCodec() {
        return codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    public String getCoreControllerUrl() {
        return coreControllerUrl;
    }

    public void setCoreControllerUrl(String coreControllerUrl) {
        this.coreControllerUrl = coreControllerUrl;
    }
}
