package net.jlxxw.wechat.response.ai;

import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 获取签名返回对象
 * @see <a href="https://developers.weixin.qq.com/doc/aispeech/confapi/INTERFACEDOCUMENT.html">接口文档</a>
 * @author chunyang.leng
 * @date 2023-04-11 14:53
 */
public class WeChatAiBotSignatureResponse extends WeChatResponse {

    /**
     * 请求接口的signature,长度500个字节左右
     */
    private String signature;

    /**
     * 过期时间
     */
    private Integer expiresIn;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }
}
