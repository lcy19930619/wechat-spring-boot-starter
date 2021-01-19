package net.jlxxw.component.weixin.exception;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:43 下午
 */
public class WeiXinException extends RuntimeException{

    public WeiXinException() {
        super();
    }
    public WeiXinException(String message) {
        super(message);
    }
}
