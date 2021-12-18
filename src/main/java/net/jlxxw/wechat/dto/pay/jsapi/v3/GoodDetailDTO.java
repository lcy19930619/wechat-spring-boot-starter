package net.jlxxw.wechat.dto.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 单品列表
 *
 * @author chunyang.leng
 * @date 2021-04-08 4:19 下午
 */
public class GoodDetailDTO {

    /**
     * 商户侧商品编码
     * 由半角的大小写字母、数字、中划线、下划线中的一种或几种组成。
     * <p>
     * 必填：是
     * 长度：1-32
     * 示例值：商品编码
     */
    @JSONField(name = "merchant_goods_id")
    private String merchantGoodsId;

    /**
     * 微信侧商品编码
     * 微信支付定义的统一商品编号（没有可不传）
     * <p>
     * 必填：否
     * 长度：1-32
     * 示例值：1001
     */
    @JSONField(name = "wechatpay_goods_id")
    private String wechatPayGoodsId;

    /**
     * 商品名称
     * 商品的实际名称
     * <p>
     * 必填：否
     * 长度：1-256
     * 示例值：iPhoneX 256G
     */
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 用户购买的数量
     * <p>
     * 必填：是
     * 示例值：1
     */
    private Integer quantity;

    /**
     * 商品单价，单位为分
     * <p>
     * 必填：是
     * 示例值：828800
     */
    @JSONField(name = "unit_price")
    private Integer unitPrice;

    public String getMerchantGoodsId() {
        return merchantGoodsId;
    }

    public void setMerchantGoodsId(String merchantGoodsId) {
        this.merchantGoodsId = merchantGoodsId;
    }


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getWechatPayGoodsId() {
        return wechatPayGoodsId;
    }

    public void setWechatPayGoodsId(String wechatPayGoodsId) {
        this.wechatPayGoodsId = wechatPayGoodsId;
    }
}
