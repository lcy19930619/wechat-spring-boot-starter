package net.jlxxw.component.weixin.dto.pay.jsapi.v3;

import net.jlxxw.component.weixin.enums.MoneyTypeEnum;

/**
 * 订单金额信息
 *
 * @author chunyang.leng
 * @date 2021-04-08 4:07 下午
 */
public class AmountDTO {

    /**
     * 订单总金额，单位为分。
     * 必填：是
     */
    private Integer total;

    /**
     * 货币类型
     * CNY：人民币，境内商户号仅支持人民币。
     *
     * @see MoneyTypeEnum
     * 必填：否
     * 长度：1-16
     */
    private String currency;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
