package net.jlxxw.wechat.response.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import net.jlxxw.wechat.enums.pay.jsapi.v3.TradeStateEnum;
import net.jlxxw.wechat.enums.pay.jsapi.v3.TradeTypeEnum;
import org.hibernate.validator.constraints.Length;

/**
 * 订单支付返回值
 *
 * @author chunyang.leng
 * @date 2023-03-31 18:17
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml">文档地址</a>
 */
public class OrderResponse {
    /**
     * 应用ID
     * 直连商户申请的公众号或移动应用appid。
     * 示例值：wxd678efh567hg6787
     * 长度：1-32
     */
    @Length(min = 1, max = 32, message = "长度应在1-32")
    @JSONField(name = "appid")
    private String appid;
    /**
     * 直连商户号
     * 直连商户的商户号，由微信支付生成并下发。
     * 示例值：1230000109
     * 长度：1-32
     */
    @Length(min = 1, max = 32, message = "长度应在1-32")
    @JSONField(name = "mchid")
    private String mchid;

    /**
     * 商户订单号
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一，详见【商户订单号】。
     * 示例值：1217752501201407033233368018
     * 长度：6-32
     */
    @Length(min = 6, max = 32, message = "长度应在1-32")
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 微信支付订单号
     * 微信支付系统生成的订单号。
     * 示例值：1217752501201407033233368018
     */
    @JSONField(name = "transaction_id")
    private String transactionId;

    /**
     * 交易类型，枚举值：
     * JSAPI：公众号支付
     * NATIVE：扫码支付
     * APP：APP支付
     * MICROPAY：付款码支付
     * MWEB：H5支付
     * FACEPAY：刷脸支付
     * 示例值：MICROPAY
     */
    @JSONField(name = "trade_type")
    private TradeTypeEnum tradeType;
    /**
     * 交易状态，枚举值：
     * SUCCESS：支付成功
     * REFUND：转入退款
     * NOTPAY：未支付
     * CLOSED：已关闭
     * REVOKED：已撤销（仅付款码支付会返回）
     * USERPAYING：用户支付中（仅付款码支付会返回）
     * PAYERROR：支付失败（仅付款码支付会返回）
     * 示例值：SUCCESS
     */
    @JSONField(name = "trade_state")
    private TradeStateEnum tradeState;

    /**
     * 交易状态描述
     * 示例值：支付成功
     * 长度 1-256
     */
    @JSONField(name = "trade_state_desc")
    private String tradeStateDesc;

    /**
     * 付款银行
     * 银行类型，采用字符串类型的银行标识。银行标识请参考《银行类型对照表》
     * 示例值：CMC
     * 长度 1-32
     *
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml#part-6">银行对照表</a>
     */
    @JSONField(name = "bank_type")
    private String bankType;

    /**
     * 附加数据
     * 在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
     * 示例值：自定义数据
     * 长度 1-128
     */
    @JSONField(name = "attach")
    private String attach;
    /**
     * 支付完成时间
     * 长度 1-64
     * 支付完成时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2018-06-08T10:34:56+08:00
     */
    @JSONField(name = "success_time")
    private String successTime;

    /**
     * 支付者信息
     */
    @JSONField(name = "payer")
    private Payer payer;

    /**
     * 订单金额信息，当支付成功时返回该字段。
     */
    @JSONField(name = "amount")
    private Amount amount;

    /**
     * 场景信息
     */
    @JSONField(name = "scene_info")
    private SceneInfo sceneInfo;

    /**
     * 优惠功能
     */
    @JSONField(name = "promotion_detail")
    private List<Promotion> promotionDetail;

    public SceneInfo getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        this.sceneInfo = sceneInfo;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public List<Promotion> getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(List<Promotion> promotionDetail) {
        this.promotionDetail = promotionDetail;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TradeTypeEnum getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeTypeEnum tradeType) {
        this.tradeType = tradeType;
    }

    public TradeStateEnum getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeStateEnum tradeState) {
        this.tradeState = tradeState;
    }
}
