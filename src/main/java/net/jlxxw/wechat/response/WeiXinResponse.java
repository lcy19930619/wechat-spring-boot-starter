package net.jlxxw.wechat.response;

/**
 * 微信应答对象
 * @author chunyang.leng
 * @date 2020/11/11 13:08
 */
public class WeiXinResponse {
    /**
     * 正常时是0
     */
    private Integer errcode;
    /**
     * 正常是OK
     */
    private String errmsg;

    /**
     * 返回的token
     */
    private String access_token;

    /**
     * 过期时间
     */
    private String expires_in;


    /**
     * 被推送用户的openId
     */
    private String openId;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }
}
