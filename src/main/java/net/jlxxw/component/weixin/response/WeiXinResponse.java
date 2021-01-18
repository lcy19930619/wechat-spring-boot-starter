package net.jlxxw.component.weixin.response;

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
}
