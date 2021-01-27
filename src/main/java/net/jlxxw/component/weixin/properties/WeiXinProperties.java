package net.jlxxw.component.weixin.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置类
 * @author chunyang.leng
 * @date 2021/1/19 5:31 下午
 */
@Configuration
@ConfigurationProperties("weixin")
public class WeiXinProperties {

    /**
     * 第三方用户唯一凭证
     */
    private String appId;

    /**
     * 第三方用户唯一凭证密钥，即appSecret
     */
    private String secret;

    /**
     * 获取access_token填写client_credential
     */
    private String grantType;

    /**
     * 绑定开发者服务器用的验证token
     */
    private String verifyToken;
    /**
     * 是否启用默认的token管理器
     */
    private boolean enableDefaultTokenManager = true;

    /**
     * 是否开启微信回调服务器安全检查，防止非法ip调用回信回调接口
     */
    private boolean enableWeiXinCallBackServerSecurityCheck = true;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public boolean isEnableDefaultTokenManager() {
        return enableDefaultTokenManager;
    }

    public void setEnableDefaultTokenManager(boolean enableDefaultTokenManager) {
        this.enableDefaultTokenManager = enableDefaultTokenManager;
    }

    public boolean isEnableWeiXinCallBackServerSecurityCheck() {
        return enableWeiXinCallBackServerSecurityCheck;
    }

    public void setEnableWeiXinCallBackServerSecurityCheck(boolean enableWeiXinCallBackServerSecurityCheck) {
        this.enableWeiXinCallBackServerSecurityCheck = enableWeiXinCallBackServerSecurityCheck;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }
}
