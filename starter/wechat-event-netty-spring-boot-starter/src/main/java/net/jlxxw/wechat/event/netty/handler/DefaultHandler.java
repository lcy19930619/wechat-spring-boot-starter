package net.jlxxw.wechat.event.netty.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import net.jlxxw.wechat.event.netty.invoke.OtherHttpRequestHandler;

/**
 * 兜底消息处理器
 * 能执行到这里的，只能是不支持的url，return 404
 *
 * @author chunyang.leng
 * @date 2023-12-26 14:56
 */
@ChannelHandler.Sharable
public class DefaultHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    private final OtherHttpRequestHandler otherHttpRequestHandler;


    public DefaultHandler(OtherHttpRequestHandler otherHttpRequestHandler) {
        this.otherHttpRequestHandler = otherHttpRequestHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        FullHttpResponse invoke = otherHttpRequestHandler.invoke(msg);
        ctx.writeAndFlush(invoke)
                .addListener(ChannelFutureListener.CLOSE);

    }


}
