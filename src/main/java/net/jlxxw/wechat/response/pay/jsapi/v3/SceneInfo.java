package net.jlxxw.wechat.response.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 支付场景描述
 *
 * @author chunyang.leng
 * @date 2023-03-31 18:52
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml">文档地址</a>
 */
public class SceneInfo {
    /**
     * 商户端设备号（发起扣款请求的商户服务器设备号）。
     * 示例值：013467007045764
     * 长度 1-32
     */
    @JSONField(name = "device_id")
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
