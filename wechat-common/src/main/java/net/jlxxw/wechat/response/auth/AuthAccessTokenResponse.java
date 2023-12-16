package net.jlxxw.wechat.response.auth;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 微信 token 授权应答对象
 * @author chunyang.leng
 * @date 2022-08-18 3:43 PM
 */
public class AuthAccessTokenResponse extends WeChatResponse {

    /**
     * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     */
    @JSONField(name = "access_token")
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    @JSONField(name = "expires_in")
    @JsonProperty("expires_in")
    private Integer expiresIn;

    /**
     * 用户刷新access_token,refresh_token有效期为30天
     */
    @JSONField(name = "refresh_token")
    @JsonProperty("refresh_token")
    private String refreshToken;
    /**
     * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenId
     */
    @JSONField(name = "openid")
    @JsonProperty("openid")
    private String openId;
    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    @JSONField(name = "scope")
    @JsonProperty("scope")
    private String scope;
    /**
     * 是否为快照页模式虚拟账号，只有当用户是快照页模式虚拟账号是返回，值为1
     */
    @JSONField(name = "is_snapshotuser")
    @JsonProperty("is_snapshotuser")
    private Integer isSnapshotUser;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getIsSnapshotUser() {
        return isSnapshotUser;
    }

    public void setIsSnapshotUser(Integer isSnapshotUser) {
        this.isSnapshotUser = isSnapshotUser;
    }
}
