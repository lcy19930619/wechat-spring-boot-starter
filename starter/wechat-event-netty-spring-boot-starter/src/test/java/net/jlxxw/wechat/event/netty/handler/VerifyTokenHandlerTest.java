package net.jlxxw.wechat.event.netty.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import net.jlxxw.wechat.event.netty.WeChatEventNettyAutoConfiguration;
import net.jlxxw.wechat.event.netty.properties.WeChatEventNettyServerProperties;
import net.jlxxw.wechat.properties.WeChatProperties;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author chunyang.leng
 * @date 2023-12-26 15:58
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatEventNettyAutoConfiguration.class)
public class VerifyTokenHandlerTest {
    @Autowired
    private WeChatEventNettyServerProperties weChatEventNettyServerProperties;

    @Autowired
    private WeChatProperties weChatProperties;

    @Test
    public void test() throws InterruptedException, NoSuchAlgorithmException {
        String verifyTokenUrl = weChatEventNettyServerProperties.getVerifyTokenUrl();
        String randomStr = getRandomStr();
        long timeMillis = System.currentTimeMillis();
        String timestamp = String.valueOf(timeMillis);

        String sha1Sign = sha1Sign(weChatProperties.getVerifyToken(), timestamp, randomStr);

        String uuid = UUID.randomUUID().toString();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        String url = verifyTokenUrl+"?signature="+sha1Sign+"&timestamp="+timestamp+"&nonce="+randomStr+"&echostr="+uuid;
        send(url,(response)->{
            Assertions.assertTrue(StringUtils.isNotBlank(response),"测试结果不应该是空的");
            Assertions.assertEquals(response,uuid,"测试值应该相同");
            System.out.println(response);
            countDownLatch.countDown();
        });

        countDownLatch.await();
    }

    public String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    private void send(String url, Consumer<String> responseConsumer) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new HttpClientCodec());
                ch.pipeline().addLast(new HttpContentDecompressor());
                ch.pipeline().addLast(new HttpObjectAggregator(65536));
                ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
                        ByteBuf content = msg.content();
                        responseConsumer.accept(content.toString(StandardCharsets.UTF_8));
                    }
                });
            }
        });

        ChannelFuture future = bootstrap.connect("127.0.0.1", weChatEventNettyServerProperties.getPort()).sync();


        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,url);

        request.headers().set(HttpHeaders.Names.HOST, "127.0.0.1");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
                request.content().readableBytes());
        request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");

        future.channel().writeAndFlush(request);
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
}
