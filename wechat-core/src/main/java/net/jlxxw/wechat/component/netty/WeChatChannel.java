package net.jlxxw.wechat.component.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.CharsetUtil;
import net.jlxxw.wechat.component.EventBus;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.security.WeChatServerSecurityCheck;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Objects;

import static io.netty.buffer.Unpooled.copiedBuffer;

/**
 * netty微信回调处理接口
 *
 * @author chunyang.leng
 * @date 2021/1/25 9:46 上午
 */
@Component
@ChannelHandler.Sharable
public class WeChatChannel extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(WeChatChannel.class);
    @Autowired
    private EventBus eventBus;
    @Autowired(required = false)
    private WeChatServerSecurityCheck weChatServerSecurityCheck;
    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        LoggerUtils.debug(logger, "netty 开始处理");
        if (weChatProperties.isEnableWeChatCallBackServerSecurityCheck() && weChatServerSecurityCheck != null) {
            // 开启微信回调ip安全检查时执行

            // 获取远程socket信息
            InetSocketAddress socketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
            // 获取远程ip地址信息
            String ipAddress = socketAddress.getAddress().getHostAddress();
            LoggerUtils.debug(logger, "微信回调ip安全检查执行,远程ip:{}", ipAddress);
            if (Objects.nonNull(weChatServerSecurityCheck) && !weChatServerSecurityCheck.isSecurity(ipAddress)) {
                LoggerUtils.warn(logger, "非法ip，不予处理:{}", ipAddress);
                // 非法ip，不予处理
                channelHandlerContext.writeAndFlush(responseOK(HttpResponseStatus.FORBIDDEN, copiedBuffer("IP FORBIDDEN", CharsetUtil.UTF_8))).addListener(ChannelFutureListener.CLOSE);
                return;
            }
        }
        // 获取请求体数据缓存
        ByteBuf content = fullHttpRequest.content();
        // 请求体数据转byte数组
        byte[] reqContent = new byte[content.readableBytes()];
        // 缓存数据加载至byte数组中
        content.readBytes(reqContent);
        // 获取请求的uri
        String uri = fullHttpRequest.uri();
        // 事件总线开始执行处理逻辑
        final String resultData = eventBus.dispatcher(reqContent, uri);
        // 响应数据刷新到缓冲区
        // ByteBuf responseData = copiedBuffer(resultData, CharsetUtil.UTF_8);

        // 切换直接内存写入
        ByteBuf byteBuf = Unpooled.directBuffer(resultData.length());
        byteBuf.writeCharSequence(resultData, CharsetUtil.UTF_8);
        // 包装响应结果
        FullHttpResponse response = responseOK(HttpResponseStatus.OK, byteBuf);
        // 发送响应,应答数据采用直接写入方式,减少 pipeline 处理流程,提升效率
        // 如果要采用全部 pipeline 处理，应改为 channelHandlerContext.channel().writeAndFlush()
        channelHandlerContext
                // 发送应答数据
                .writeAndFlush(response)
                // 处理完毕，关闭连接
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 包装响应结果，使用http1.1协议格式
     *
     * @param status  响应状态码
     * @param content 响应内容
     * @return 包装后到对象
     */
    private FullHttpResponse responseOK(HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        response.headers().set("Content-Type", "application/xml;charset=UTF-8");
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }

    /**
     * 异常信息记录
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException){
            // 15秒未接收到服务器信息，自动断开链接
            ctx.close();
            return;
        }
        // 记录异常日志
        LoggerUtils.error(logger,"wechat-netty-thread 发生未知异常",cause);
        // 关闭异常连接
        ctx.close();
    }
}
