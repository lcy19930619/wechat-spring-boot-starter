package net.jlxxw.wechat.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信支付异常
 *
 * @author chunyang.leng
 * @date 2021-04-10 8:06 下午
 */
public class WeiXinPayException extends RuntimeException {

    /**
     * 微信返回的错误码
     */
    private final String errorCode;

    /**
     * 错误描述信息
     */
    private final String description;

    /**
     * 错误信息详情,json格式
     */
    private final JSONObject detail;

    public WeiXinPayException(String errorCode, String description, JSONObject detail) {
        super("微信支付产生了一个错误信息，错误码：" + errorCode + " 错误信息描述：" + description + " 错误信息详情：" + detail);
        this.errorCode = errorCode;
        this.description = description;
        this.detail = detail;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }

    public JSONObject getDetail() {
        return detail;
    }
}
