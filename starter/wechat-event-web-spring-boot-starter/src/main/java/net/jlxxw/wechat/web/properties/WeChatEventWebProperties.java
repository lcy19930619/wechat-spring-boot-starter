package net.jlxxw.wechat.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("wechat.event.server.web")
public class WeChatEventWebProperties {

    /**
     * 核心控制器 url
     */
    private String coreControllerUrl = "weChat";

    /**
     * 部署验证token url
     */
    private String verifyTokenUrl = "verifyToken";

    public String getVerifyTokenUrl() {
        return verifyTokenUrl;
    }

    public void setVerifyTokenUrl(String verifyTokenUrl) {
        this.verifyTokenUrl = verifyTokenUrl;
    }

    public String getCoreControllerUrl() {
        return coreControllerUrl;
    }

    public void setCoreControllerUrl(String coreControllerUrl) {
        this.coreControllerUrl = coreControllerUrl;
    }
}
