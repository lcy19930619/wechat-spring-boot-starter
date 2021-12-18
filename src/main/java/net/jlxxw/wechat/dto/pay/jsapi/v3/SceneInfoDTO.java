package net.jlxxw.wechat.dto.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 支付场景描述
 *
 * @author chunyang.leng
 * @date 2021-04-08 4:30 下午
 */
public class SceneInfoDTO {

    /**
     * 用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。
     * <p>
     * 必填：是
     * 长度：1-45
     * 示例值：14.23.150.211
     */
    @JSONField(name = "payer_client_ip")
    private String payerClientIp;

    /**
     * 商户端设备号（门店号或收银设备ID）
     * <p>
     * 长度：1-32
     * 必填：否
     * 示例值：013467007045764
     */
    @JSONField(name = "device_id")
    private String deviceId;

    /**
     * 商户门店信息
     * <p>
     * 必填：否
     */
    @JSONField(name = "store_info")
    private List<StoreInfoDTO> storeInfoList;

    public String getPayerClientIp() {
        return payerClientIp;
    }

    public void setPayerClientIp(String payerClientIp) {
        this.payerClientIp = payerClientIp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<StoreInfoDTO> getStoreInfoList() {
        return storeInfoList;
    }

    public void setStoreInfoList(List<StoreInfoDTO> storeInfoList) {
        this.storeInfoList = storeInfoList;
    }
}
