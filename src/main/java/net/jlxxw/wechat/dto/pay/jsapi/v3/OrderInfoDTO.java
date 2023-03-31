package net.jlxxw.wechat.dto.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 订单信息
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_1.shtml">实体类信息</a>
 * @author chunyang.leng
 * @date 2021-04-08 3:35 下午
 */
public class OrderInfoDTO {
    /**
     * 由微信生成的应用ID，全局唯一。请求基础下单接口时请注意APPID的应用属性，例如公众号场景下，需使用应用属性为公众号的服务号APPID
     * 示例值：wxd678efh567hg6787
     *
     */
    @JSONField(name = "appid")
    private String appId;
    /**
     * 直连商户的商户号，由微信支付生成并下发。
     * 示例值：1230000109
     */
    @JSONField(name = "mchid")
    private String mchId;
    /**
     * 服务商应用ID
     * 服务商申请的公众号appid。
     * <p>
     * 必填：是
     * 长度:1-32
     * 示例值：wx8888888888888888
     */
    @JSONField(name = "sp_appid")
    private String spAppId;
    /**
     * 服务商应用ID
     * 服务商户号，由微信支付生成并下发
     * <p>
     * 必填：是
     * 长度:1-32
     * 示例值：1230000109
     */
    @JSONField(name = "sp_mchid")
    private String spMchId;

    /**
     * 子商户应用ID
     * 子商户申请的公众号appid。
     * 若sub_openid有传的情况下，sub_appid必填，且sub_appid需与sub_openid对应
     * <p>
     * 必填：否
     * 长度:1-32
     * 示例值：wxd678efh567hg6999
     */
    @JSONField(name = "sub_appid")
    private String subAppId;

    /**
     * 子商户号
     * 商户的商户号，由微信支付生成并下发。
     * <p>
     * 必填：是
     * 长度:1-32
     * 示例值：1900000109
     */
    @JSONField(name = "sub_mchid")
    private String subMchId;

    /**
     * 商品描述
     * <p>
     * 必填：是
     * 长度:1-127
     * 示例值：Image形象店-深圳腾大-QQ公仔
     */
    private String description;

    /**
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。
     * <p>
     * 必填：是
     * 长度:6-32
     * 示例值：1217752501201407033233368018
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;


    /**
     * 交易结束时间
     * 订单失效时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，
     * YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，
     * TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。
     * 例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒
     * <p>
     * 必填：否
     * 长度:1-64
     * 示例值：2018-06-08T10:34:56+08:00
     */
    @JSONField(name = "time_expire")
    private String timeExpire;

    /**
     * 附加数据
     * 在查询API和支付通知中原样返回，可作为自定义参数使用
     * <p>
     * 必填：否
     * 长度:1-128
     * 示例值：xxxxxxx
     */
    private String attach;

    /**
     * 通知URL必须为直接可访问的URL，不允许携带查询串。
     * 格式：URL
     * <p>
     * 必填：是
     * 长度:1-256
     * 示例值：https://www.weixin.qq.com/wxpay/pay.php
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

    /**
     * 订单优惠标记
     * <p>
     * 必填：否
     * 长度:1-32
     * 示例值：WXG
     */
    @JSONField(name = "goods_tag")
    private String goodsTag;
    /**
     * 结算信息
     * <p>
     * 必填:否
     */
    @JSONField(name = "settle_info")
    private SettleInfoDTO settleInfoDTO;

    /**
     * 订单金额
     * <p>
     * 必填:是
     */
    @JSONField(name = "amount")
    private AmountDTO amountDTO;

    /**
     * 支付者信息
     * <p>
     * 必填:是
     */
    @JSONField(name = "payer")
    private PayerDTO payerDTO;

    public String getSpAppId() {
        return spAppId;
    }

    public void setSpAppId(String spAppId) {
        this.spAppId = spAppId;
    }

    public String getSpMchId() {
        return spMchId;
    }

    public void setSpMchId(String spMchId) {
        this.spMchId = spMchId;
    }

    public String getSubAppId() {
        return subAppId;
    }

    public void setSubAppId(String subAppId) {
        this.subAppId = subAppId;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public SettleInfoDTO getSettleInfoDTO() {
        return settleInfoDTO;
    }

    public void setSettleInfoDTO(SettleInfoDTO settleInfoDTO) {
        this.settleInfoDTO = settleInfoDTO;
    }

    public AmountDTO getAmountDTO() {
        return amountDTO;
    }

    public void setAmountDTO(AmountDTO amountDTO) {
        this.amountDTO = amountDTO;
    }

    public PayerDTO getPayerDTO() {
        return payerDTO;
    }

    public void setPayerDTO(PayerDTO payerDTO) {
        this.payerDTO = payerDTO;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
}
