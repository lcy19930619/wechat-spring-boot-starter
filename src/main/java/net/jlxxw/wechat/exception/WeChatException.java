package net.jlxxw.wechat.exception;

import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 微信错误信息异常汇总
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Global_Return_Code.html">全局异常状态码文档</a>
 * @author chunyang.leng
 * @date 2021/1/19 5:43 下午
 */
public class WeChatException extends RuntimeException {
    /**
     * 微信返回的错误码
     */
    private Integer errorCode;

    public WeChatException() {
        super();
    }
    public WeChatException(Integer code,String message) {
        super(message);
        this.errorCode = code;
    }
    public WeChatException(String message) {
        super(message);
    }

    public WeChatException(WeChatResponse response) {
        this(response.getErrcode(),response.getErrmsg());
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
