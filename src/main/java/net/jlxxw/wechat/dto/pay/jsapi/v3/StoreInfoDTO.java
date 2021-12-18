package net.jlxxw.wechat.dto.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 商户门店信息
 *
 * @author chunyang.leng
 * @date 2021-04-10 1:01 下午
 */
public class StoreInfoDTO {

    /**
     * 门店编号
     * <p>
     * 必填：是
     * 长度：1-32
     * 示例值：0001
     */
    private String id;

    /**
     * 门店名称
     * 必填：否
     * 长度：1-256
     * 示例值：腾讯大厦分店
     */
    private String name;

    /**
     * 地区编码
     * <p>
     * 地区编码，详细请见省市区编号对照表。
     * https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml
     * 必填：否
     * 长度：1-32
     * 示例值：440305
     */
    @JSONField(name = "area_code")
    private String areaCode;

    /**
     * 详细的商户门店地址
     * <p>
     * 必填：否
     * 长度：1-512
     * 示例值：广东省深圳市南山区科技中一道10000号
     */
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
