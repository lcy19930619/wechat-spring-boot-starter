package net.jlxxw.component.weixin.component.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import net.jlxxw.component.weixin.component.EventBus;
import net.jlxxw.component.weixin.properties.WeiXinProperties;
import net.jlxxw.component.weixin.security.WeiXinServerSecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

import static io.netty.buffer.Unpooled.copiedBuffer;
/**
 *  netty微信回调处理接口
 * @author chunyang.leng
 * @date 2021/1/25 9:46 上午
 */
@Component
@ChannelHandler.Sharable
public class WeiXinChannel extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinChannel.class);
    @Autowired
    private EventBus eventBus;
    @Autowired(required = false)
    private WeiXinServerSecurityCheck weiXinServerSecurityCheck;
    @Autowired
    private WeiXinProperties weiXinProperties;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.debug("netty 开始处理");
        if(weiXinProperties.isEnableWeiXinCallBackServerSecurityCheck() && weiXinServerSecurityCheck != null){
            logger.debug("微信回调ip安全检查时执行");
            // 开启微信回调ip安全检查时执行
            InetSocketAddress socketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
            String ipAddress = socketAddress.getAddress().getHostAddress();
            if(!weiXinServerSecurityCheck.isSecurity(ipAddress)){
                logger.warn("非法ip，不予处理:{}",ipAddress);
                // 非法ip，不予处理
                channelHandlerContext.writeAndFlush(responseOK(HttpResponseStatus.FORBIDDEN,copiedBuffer("IP FORBIDDEN", CharsetUtil.UTF_8))).addListener(ChannelFutureListener.CLOSE);
                return;
            }
        }
        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String uri = fullHttpRequest.uri();
        final String resultData = eventBus.dispatcher(reqContent,uri);
        // 发送响应
        ByteBuf responseData = copiedBuffer(resultData, CharsetUtil.UTF_8);

        FullHttpResponse response = responseOK(HttpResponseStatus.OK, responseData);
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private FullHttpResponse responseOK(HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        if (content != null) {
            response.headers().set("Content-Type", "application/xml;charset=UTF-8");
            response.headers().set("Content_Length", response.content().readableBytes());
        }
        return response;
    }


}
