package net.jlxxw.component.weixin.vo.jsapi.v3;

/**
 * 调起支付时，需要返回给前端的对象
 * @author chunyang.leng
 * @date 2021-04-10 9:53 下午
 */
public class ExecutePayVO {
    /**
     * 公众号ID
     */
    private String appId;
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * 随机串
     */
    private String nonceStr;
    /**
     * 预支付id
     */
    private String prepayId;
    /**
     * 签名方式，目前仅支持RSA
     */
    private String signType;
    /**
     * 签名串
     */
    private String paySign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
}
