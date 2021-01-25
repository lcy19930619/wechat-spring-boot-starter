package net.jlxxw.component.weixin.component.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import net.jlxxw.component.weixin.component.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static io.netty.buffer.Unpooled.copiedBuffer;
/**
 *  netty微信回调处理接口
 * @author chunyang.leng
 * @date 2021/1/25 9:46 上午
 */
public class WeiXinChannel extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinChannel.class);
    private EventBus eventBus;

    public WeiXinChannel(EventBus eventBus){
        this.eventBus = eventBus;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        // 微信发送进来的xml
        String inputXML = new String(reqContent, StandardCharsets.UTF_8);
        logger.info("接收到微信到请求数据:{}",inputXML);
        final String resultData = eventBus.dispatcher(reqContent);
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
