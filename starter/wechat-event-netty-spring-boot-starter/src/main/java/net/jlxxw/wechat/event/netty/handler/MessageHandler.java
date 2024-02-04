package net.jlxxw.wechat.event.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.CharsetUtil;
import net.jlxxw.wechat.event.component.EventBus;
import net.jlxxw.wechat.event.netty.properties.WeChatEventNettyServerProperties;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.log.util.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * netty 微信回调处理接口，用于处理 netty 模式中接受到到 微信数据，包括用户输入到消息、微信发送的事件等
 *
 * @author chunyang.leng
 * @date 2021/1/25 9:46 上午
 */

@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private final EventBus eventBus;
    private final WeChatEventNettyServerProperties weChatEventNettyServerProperties;

    private final WeChatProperties weChatProperties;

    /**
     * 构建一个消息处理器，用于处理微信消息
     * @param eventBus 事件总线
     * @param weChatEventNettyServerProperties netty 模式相关配置信息
     * @param weChatProperties 微信公众号基础信息配置信息
     */
    public MessageHandler(EventBus eventBus,
                          WeChatEventNettyServerProperties weChatEventNettyServerProperties,
                          WeChatProperties weChatProperties) {
        this.eventBus = eventBus;
        this.weChatEventNettyServerProperties = weChatEventNettyServerProperties;
        this.weChatProperties = weChatProperties;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        // 获取请求的uri
        String uri = fullHttpRequest.uri();

        String coreControllerUrl = weChatEventNettyServerProperties.getCoreControllerUrl();
        if (!uri.contains(coreControllerUrl)) {
            // 不属于自己这个channel处理，丢给下一个channel
            channelHandlerContext.fireChannelRead(fullHttpRequest.copy());
            return;
        }

        HttpMethod method = fullHttpRequest.method();
        if (method.equals(HttpMethod.GET)) {
            // get 请求只有验证签名

            int index = uri.indexOf("?");
            String urlData = uri.substring(index + 1);

            String[] split = urlData.split("&");
            Map<String,String> map = new HashMap<>(16);

            for (String line : split) {
                String[] data = line.split("=");
                if (data.length >=2) {
                    map.put(data[0], data[1]);
                }
            }

            String msgSignature = map.get("signature");
            String msgTimestamp = map.get("timestamp");
            String msgNonce = map.get("nonce");
            String echostr = map.get("echostr");
            LoggerUtils.info(logger,"接收到微信请求：signature={},timestamp={},nonce={},echostr={}", msgSignature, msgTimestamp, msgNonce, echostr);
            if (verify(msgSignature, msgTimestamp, msgNonce)) {
                // 微信接口验证通过
                LoggerUtils.info(logger,"验证通过");
                // 切换直接内存写入
                ByteBuf byteBuf = Unpooled.directBuffer(echostr.length());
                byteBuf.writeCharSequence(echostr, CharsetUtil.UTF_8);
                FullHttpResponse response = response(byteBuf,HttpResponseStatus.OK);

                channelHandlerContext.writeAndFlush(response)
                        .addListener(ChannelFutureListener.CLOSE);
                return;
            }
            // 微信接口验证失败，返回空白字符串
            ByteBuf byteBuf = Unpooled.directBuffer("".length());
            byteBuf.writeCharSequence("", CharsetUtil.UTF_8);
            FullHttpResponse response = response(byteBuf,HttpResponseStatus.INTERNAL_SERVER_ERROR);
            channelHandlerContext.writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE);
            LoggerUtils.info(logger,"验证失败,收到微信请求:{}",uri);

            return;
        }

        // 通道id，仅用于日志中使用
        String channelId = channelHandlerContext.channel().id().asShortText();
        LoggerUtils.debug(logger, "公众号组件 ---> netty 消息处理器，开始处理数据,channelId:{}", channelId);

        // 获取请求体数据缓存
        ByteBuf content = fullHttpRequest.content();
        // 请求体数据转byte数组
        byte[] reqContent = new byte[content.readableBytes()];
        // 缓存数据加载至byte数组中
        content.readBytes(reqContent);

        // 事件总线开始执行处理逻辑
        LoggerUtils.debug(logger, "公众号组件 ---> netty 消息处理器，事件总线开始执行处理逻辑,channelId:{}", channelId);
        eventBus.dispatcher(reqContent, uri, (resultData) -> {
            LoggerUtils.debug(logger, "公众号组件 ---> netty 消息处理器，事件总线处理数据结束,channelId:{}", channelId);
            // 响应数据刷新到缓冲区
            // ByteBuf responseData = copiedBuffer(resultData, CharsetUtil.UTF_8);

            // 切换直接内存写入
            ByteBuf byteBuf = Unpooled.directBuffer(resultData.length());
            byteBuf.writeCharSequence(resultData, CharsetUtil.UTF_8);
            // 包装响应结果
            FullHttpResponse response = response(byteBuf);
            // 发送响应,应答数据采用直接写入方式,减少 pipeline 处理流程,提升效率
            // 如果要采用全部 pipeline 处理，应改为 channelHandlerContext.channel().writeAndFlush()
            channelHandlerContext
                    // 发送应答数据
                    .writeAndFlush(response)
                    // 处理完毕，关闭连接
                    .addListener(ChannelFutureListener.CLOSE);
            LoggerUtils.debug(logger, "公众号组件 ---> netty 消息处理器，处理数据结束,channelId:{}", channelId);

        });

    }

    /**
     * 异常信息记录
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            // 15秒未接收到服务器信息，自动断开链接
            ctx.close();
            return;
        }
        // 记录异常日志
        LoggerUtils.error(logger, "wechat-netty-thread 发生未知异常", cause);
        // 关闭异常连接
        ctx.close();
    }

    /**
     * 包装响应结果，使用http1.1协议格式
     *
     * @param content 响应内容
     * @return 包装后到对象
     */
    private FullHttpResponse response(ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set("Content-Type", "application/xml;charset=UTF-8");
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }







    /**
     * 验证Token
     *
     * @param msgSignature 签名串，对应URL参数的signature
     * @param timeStamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @return 是否为安全签名
     */
    private boolean verify(String msgSignature, String timeStamp, String nonce) throws NoSuchAlgorithmException {
        String signature = sha1Sign(weChatProperties.getVerifyToken(), timeStamp, nonce);
        if (!signature.equals(msgSignature)) {
            throw new RuntimeException("token认证失败");
        }
        return true;
    }

    /**
     * 进行 sha1 签名运算
     * @param token 项目中配置的 微信验证token
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String sha1Sign(String token, String timestamp, String nonce) throws NoSuchAlgorithmException {
        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("verify-token不能为空");
        }
        String[] array = new String[]{token, timestamp, nonce};
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (int i = 0; i < 3; i++) {
            sb.append(array[i]);
        }
        String str = sb.toString();
        // SHA1签名生成
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(str.getBytes());
        byte[] digest = md.digest();

        StringBuffer hexstr = new StringBuffer();
        String shaHex = "";
        for (int i = 0; i < digest.length; i++) {
            shaHex = Integer.toHexString(digest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexstr.append(0);
            }
            hexstr.append(shaHex);
        }
        return hexstr.toString();

    }

    private FullHttpResponse response(ByteBuf content,HttpResponseStatus responseStatus) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, responseStatus, content);
        response.headers().set("Content-Type", "txt/plain;charset=UTF-8");
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }
}
