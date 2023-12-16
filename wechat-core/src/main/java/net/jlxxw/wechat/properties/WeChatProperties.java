package net.jlxxw.wechat.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置类
 *
 * @author chunyang.leng
 * @date 2021/1/19 5:31 下午
 */
@Configuration
@ConfigurationProperties("we-chat")
public class WeChatProperties {

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
     * 绑定开发者服务器用的验证token,即公众平台上，开发者设置的token
     */
    private String verifyToken;

    /**
     * 公众平台上，开发者设置的EncodingAesKey
     */
    private String encodingAesKey;

    /**
     * 是否启用消息加解密,启用加解密时，必须配置encodingAESKey
     */
    private boolean enableMessageEnc = false;
    /**
     * 是否启用默认的token管理器
     */
    private boolean enableDefaultTokenManager = true;

    /**
     * 是否开启微信回调服务器安全检查，防止非法ip调用回信回调接口
     */
    private boolean enableWeChatCallBackServerSecurityCheck = true;

    /**
     * 验证微信 token 的URL
     */
    private String verifyTokenUrl="verifyToken";

    /**
     * 未使用 netty 的情况下
     * web 控制器的url
     */
    private String coreControllerUrl="weChat";

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

    public boolean isEnableWeChatCallBackServerSecurityCheck() {
        return enableWeChatCallBackServerSecurityCheck;
    }

    public void setEnableWeChatCallBackServerSecurityCheck(boolean enableWeChatCallBackServerSecurityCheck) {
        this.enableWeChatCallBackServerSecurityCheck = enableWeChatCallBackServerSecurityCheck;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public boolean isEnableMessageEnc() {
        return enableMessageEnc;
    }

    public void setEnableMessageEnc(boolean enableMessageEnc) {
        this.enableMessageEnc = enableMessageEnc;
    }

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
