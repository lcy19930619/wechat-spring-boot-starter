package net.jlxxw.wechat.response.token;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

/**
 * @author chunyang.leng
 * @date 2021-12-20 11:46 上午
 */
public class WeChatTokenResponse extends WeChatResponse {


    /**
     * 返回的token
     */
    @JSONField(name = "access_token")
    @JsonProperty(value = "access_token")
    private String accessToken;

    /**
     * 过期时间
     */
    @JSONField(name = "expires_in")
    @JsonProperty(value = "expires_in")
    private String expiresIn;

    /**
     * js_api_tocket
     */
    private String ticket;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
