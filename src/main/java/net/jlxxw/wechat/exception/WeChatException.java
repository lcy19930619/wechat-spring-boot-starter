package net.jlxxw.wechat.exception;

/**
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

    public WeChatException(String message) {
        super(message);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
