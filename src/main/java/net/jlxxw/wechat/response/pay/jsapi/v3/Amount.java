package net.jlxxw.wechat.response.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 订单金额信息，当支付成功时返回该字段。
 *
 * @author chunyang.leng
 * @date 2023-03-31 18:23
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml">文档地址</a>
 */
public class Amount {
    /**
     * 订单总金额，单位为分。
     * 示例值：100
     */
    @JSONField(name = "total")
    private Integer total;
    /**
     * 用户支付金额，单位为分。（指使用优惠券的情况下，这里等于总金额-优惠券金额）
     * 示例值：100
     */
    @JSONField(name = "payer_total")
    private Integer payerTotal;

    /**
     * CNY：人民币，境内商户号仅支持人民币。
     * 示例值：CNY
     * 长度 1-16
     */
    @JSONField(name = "currency")
    private String currency;

    /**
     * 用户支付币种
     * 示例值：CNY
     * 长度 1-16
     */
    @JSONField(name = "payer_currency")
    private String payerCurrency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayerCurrency() {
        return payerCurrency;
    }

    public void setPayerCurrency(String payerCurrency) {
        this.payerCurrency = payerCurrency;
    }

    public Integer getPayerTotal() {
        return payerTotal;
    }

    public void setPayerTotal(Integer payerTotal) {
        this.payerTotal = payerTotal;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
