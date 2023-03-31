package net.jlxxw.wechat.enums.pay.jsapi.v3;

/**
 * 交易状态，枚举值：
 * SUCCESS：支付成功
 * REFUND：转入退款
 * NOTPAY：未支付
 * CLOSED：已关闭
 * REVOKED：已撤销（仅付款码支付会返回）
 * USERPAYING：用户支付中（仅付款码支付会返回）
 * PAYERROR：支付失败（仅付款码支付会返回）
 * @author chunyang.leng
 * @date 2023-03-31 18:36
 */
public enum TradeStateEnum {
    /**
     * 支付成功
     */
    SUCCESS("支付成功"),
    /**
     * 转入退款
     */
    REFUND("转入退款"),
    /**
     * 未支付
     */
    NOTPAY("未支付"),
    /**
     * 已关闭
     */
    CLOSED("已关闭"),
    /**
     * 已撤销（仅付款码支付会返回）
     */
    REVOKED("已撤销（仅付款码支付会返回）"),
    /**
     * 用户支付中（仅付款码支付会返回）
     */
    USERPAYING("用户支付中（仅付款码支付会返回）"),
    /**
     * 支付失败（仅付款码支付会返回）
     */
    PAYERROR("支付失败（仅付款码支付会返回）"),


    ;

    private final String description;

    TradeStateEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
