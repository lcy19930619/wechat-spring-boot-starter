package net.jlxxw.wechat.vo.jsapi.v3;

/**
 * 微信支付返回对象
 *
 * @author chunyang.leng
 * @date 2021-04-10 8:29 下午
 */
public class PayResultVO {
    /**
     * 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
     */
    private String prePayId;

    /**
     * 微信支付给每个接收到的请求分配了一个唯一标示。
     * 当需要微信支付帮助时，提供请求的唯一标示
     */
    private String requestId;

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
