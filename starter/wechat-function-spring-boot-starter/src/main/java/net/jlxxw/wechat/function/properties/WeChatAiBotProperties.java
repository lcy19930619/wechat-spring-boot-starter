package net.jlxxw.wechat.function.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信机器人相关配置（目前开放，不收费）
 *
 * @see <a href="https://developers.weixin.qq.com/doc/aispeech/confapi/mp/getbindlink.html">开发文档</a>
 * @see <a href="https://chatbot.weixin.qq.com/">ai平台</a>
 * @author chunyang.leng
 * @date 2023-04-11 14:39
 */
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties("wechat.function.aibot")
public class WeChatAiBotProperties {

    /**
     * 机器人应用id，注意，不是公众号的，需要单独申请
     */
    private String appid;
    /**
     * 机器人应用token，注意，不是公众号的，需要单独申请
     */
    private String token;
    /**
     * 机器人应用aes key，注意，不是公众号的，需要单独申请
     */
    private String encodingAesKey;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }
}
