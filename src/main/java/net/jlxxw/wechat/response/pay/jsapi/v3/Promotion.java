package net.jlxxw.wechat.response.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;

/**
 * 优惠功能
 *
 * @author chunyang.leng
 * @date 2023-03-31 18:53
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml">文档地址</a>
 */
public class Promotion {

    /**
     * 券ID
     * 示例值：109519
     * 长度 1-32
     */
    @JSONField(name = "coupon_id")
    private String couponId;

    /**
     * 优惠名称
     * 示例值：单品惠-6
     */
    @JSONField(name = "name")
    private String name;

    /**
     * GLOBAL：全场代金券
     * SINGLE：单品优惠
     * 示例值：GLOBAL
     */
    @JSONField(name = "scope")
    private String scope;
    /**
     * CASH：充值型代金券
     * NOCASH：免充值型代金券
     * 示例值：CASH
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 优惠券面额
     * 示例值：100
     */
    @JSONField(name = "amount")
    private Integer amount;

    /**
     * 活动ID
     * 示例值：931386
     */
    @JSONField(name = "stock_id")
    private String stockId;

    /**
     * 微信出资，单位为分
     * 示例值：0
     */
    @JSONField(name = "wechatpay_contribute")
    private Integer wechatpayContribute;

    /**
     * 商户出资，单位为分
     * 示例值：0
     */
    @JSONField(name = "merchant_contribute")
    private Integer merchantContribute;

    /**
     * 其他出资，单位为分
     * 示例值：0
     */
    @JSONField(name = "other_contribute")
    private Integer otherContribute;

    /**
     * 优惠币种
     * CNY：人民币，境内商户号仅支持人民币。
     * 示例值：CNY
     */
    @JSONField(name = "currency")
    private String currency;

    /**
     * 单品列表信息
     */
    @JSONField(name = "goods_detail")
    private List<Good> goodsDetail;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Integer getWechatpayContribute() {
        return wechatpayContribute;
    }

    public void setWechatpayContribute(Integer wechatpayContribute) {
        this.wechatpayContribute = wechatpayContribute;
    }

    public Integer getMerchantContribute() {
        return merchantContribute;
    }

    public void setMerchantContribute(Integer merchantContribute) {
        this.merchantContribute = merchantContribute;
    }

    public Integer getOtherContribute() {
        return otherContribute;
    }

    public void setOtherContribute(Integer otherContribute) {
        this.otherContribute = otherContribute;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Good> getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(List<Good> goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
}
