package net.jlxxw.wechat.dto.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 优惠功能
 *
 * @author chunyang.leng
 * @date 2021-04-08 4:16 下午
 */
public class DetailDTO {

    /**
     * 订单原价
     * <p>
     * 1、商户侧一张小票订单可能被分多次支付，订单原价用于记录整张小票的交易金额。
     * 2、当订单原价与支付金额不相等，则不享受优惠。
     * 3、该字段主要用于防止同一张小票分多次支付，以享受多次优惠的情况，正常支付订单不必上传此参数。
     * <p>
     * 必填：否
     * 示例值：608800
     */
    @JSONField(name = "cost_price")
    private Integer costPrice;

    /**
     * 商品小票ID
     * <p>
     * 长度:1-32
     * 必填：否
     * 示例值：微信123
     */
    @JSONField(name = "invoice_id")
    private String invoiceId;

    /**
     * 单品列表信息
     * <p>
     * 必填：否
     * 条目个数限制：【1，6000
     */
    @JSONField(name = "goods_detail")
    private List<GoodDetailDTO> goodDetailDTOList;


    public Integer getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Integer costPrice) {
        this.costPrice = costPrice;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public List<GoodDetailDTO> getGoodDetailDTOList() {
        return goodDetailDTOList;
    }

    public void setGoodDetailDTOList(List<GoodDetailDTO> goodDetailDTOList) {
        this.goodDetailDTOList = goodDetailDTOList;
    }
}
