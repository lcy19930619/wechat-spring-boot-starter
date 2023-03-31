package net.jlxxw.wechat.enums.pay.jsapi.v3;

/**
 * 交易类型，枚举值：
 * JSAPI：公众号支付
 * NATIVE：扫码支付
 * APP：APP支付
 * MICROPAY：付款码支付
 * MWEB：H5支付
 * FACEPAY：刷脸支付
 * @author chunyang.leng
 * @date 2023-03-31 18:32
 */
public enum TradeTypeEnum {
    /**
     * 公众号支付
     */
    JSAPI("公众号支付"),
    /**
     * NATIVE
     */
    NATIVE("NATIVE"),
    /**
     * APP支付
     */
    APP("APP支付"),
    /**
     * 付款码支付
     */
    MICROPAY("付款码支付"),

    /**
     * MWEB
     */
    MWEB("MWEB"),

    /**
     * 刷脸支付
     */
    FACEPAY("刷脸支付"),
    ;
    private final String description;

    TradeTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
