package net.jlxxw.wechat.event.netty.invoke;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 处理其他的 http 请求
 * @author chunyang.leng
 * @date 2023-12-26 16:42
 */
public interface OtherHttpRequestHandler {

    /**
     * 用于处理其他 http 请求
     * @param request
     * @return
     */
    FullHttpResponse invoke(FullHttpRequest request);

}
