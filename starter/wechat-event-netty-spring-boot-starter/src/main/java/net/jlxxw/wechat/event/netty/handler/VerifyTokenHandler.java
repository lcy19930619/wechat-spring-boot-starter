package net.jlxxw.wechat.event.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import net.jlxxw.wechat.event.netty.properties.WeChatEventNettyServerProperties;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.util.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * token 验证处理
 * @author chunyang.leng
 * @date 2023-12-26 14:55
 */
@ChannelHandler.Sharable
public class VerifyTokenHandler  extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(VerifyTokenHandler.class);

    private final WeChatProperties weChatProperties;

    private final WeChatEventNettyServerProperties weChatEventNettyServerProperties;


    public VerifyTokenHandler(WeChatProperties weChatProperties, WeChatEventNettyServerProperties weChatEventNettyServerProperties) {
        this.weChatProperties = weChatProperties;
        this.weChatEventNettyServerProperties = weChatEventNettyServerProperties;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        HttpMethod method = msg.method();
        if (!method.equals(HttpMethod.GET)) {
            // 交给下个通道处理
            ctx.fireChannelRead(msg.copy());
            return;
        }
        String uri = msg.uri();
        String verifyTokenUrl = weChatEventNettyServerProperties.getVerifyTokenUrl();

        int prefix = uri.indexOf(verifyTokenUrl);
        if (prefix < 0) {
            // 交给下个通道处理
            ctx.fireChannelRead(msg.copy());
            return;
        }

        int index = uri.indexOf("?");
        String urlData = uri.substring(index + 1);

        String substring = uri.substring(0, index);
        if (!substring.equals(verifyTokenUrl)) {
            // 交给下个通道处理
            ctx.fireChannelRead(msg.copy());
            return;
        }


        String[] split = urlData.split("&");
        Map<String,String> map = new HashMap<>(16);

        for (String line : split) {
            String[] data = line.split("=");
            if (data.length > 1) {
                map.put(data[0], data[1]);
            }
        }

        String msgSignature = map.get("signature");
        String msgTimestamp = map.get("timestamp");
        String msgNonce = map.get("nonce");
        String echostr = map.get("echostr");
        LoggerUtils.info(logger,"接收到微信请求：signature={},timestamp={},nonce={},echostr={}", msgSignature, msgTimestamp, msgNonce, echostr);
        if (verify(msgSignature, msgTimestamp, msgNonce)) {
            LoggerUtils.info(logger,"验证通过");
            // 切换直接内存写入
            ByteBuf byteBuf = Unpooled.directBuffer(echostr.length());
            byteBuf.writeCharSequence(echostr, CharsetUtil.UTF_8);
            FullHttpResponse response = response(byteBuf,HttpResponseStatus.OK);

            ctx.writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE);
            return;
        }
        ByteBuf byteBuf = Unpooled.directBuffer("".length());
        byteBuf.writeCharSequence("", CharsetUtil.UTF_8);
        FullHttpResponse response = response(byteBuf,HttpResponseStatus.INTERNAL_SERVER_ERROR);
        ctx.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
        LoggerUtils.info(logger,"验证失败,收到微信请求:{}",uri);

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
