package net.jlxxw.wechat.response.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 单品列表
 *
 * @author chunyang.leng
 * @date 2023-03-31 18:55
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml">文档地址</a>
 */
public class Good {

    /**
     * 商品编码
     * 示例值：M1006
     * 长度 1-32
     */
    @JSONField(name = "goods_id")
    private String goodsId;

    /**
     * 用户购买的数量
     * 示例值：1
     */
    @JSONField(name = "quantity")
    private Integer quantity;

    /**
     * 商品单价，单位为分
     * 示例值：100
     */
    @JSONField(name = "unit_price")
    private Integer unitPrice;

    /**
     * 商品优惠金额
     * 示例值：0
     */
    @JSONField(name = "discount_amount")
    private Integer discountAmount;

    /**
     * 商品备注信息
     * 示例值：商品备注信息
     */
    @JSONField(name = "goods_remark")
    private String goodsRemark;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
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

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getGoodsRemark() {
        return goodsRemark;
    }

    public void setGoodsRemark(String goodsRemark) {
        this.goodsRemark = goodsRemark;
    }
}
