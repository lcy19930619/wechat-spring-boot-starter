//package net.jlxxw.wechat.web;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.*;
//import io.netty.util.CharsetUtil;
//import jakarta.annotation.PostConstruct;
//import net.jlxxw.wechat.event.codec.WeChatMessageCodec;
//import net.jlxxw.wechat.event.util.SHA1;
//import net.jlxxw.wechat.exception.AesException;
//import net.jlxxw.wechat.properties.WeChatProperties;
//import net.jlxxw.wechat.web.listener.util.WechatMessageCrypt;
//import org.apache.commons.io.IOUtils;
//import org.junit.Assert;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.platform.commons.util.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.web.ServerProperties;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.function.Consumer;
//
///**
// * 加密模式测试
// * @author chunyang.leng
// * @date 2023-12-25 18:50
// */
//@TestPropertySource("classpath:application-cipher.yml")
//@ActiveProfiles("cipher")
//@SpringBootTest(classes = WeChatEventWebAutoConfiguration.class)
//public class ListenerCipherTextTest {
//
//    private static final String openId = "FromUser";
//
//    @Autowired
//    private ServerProperties serverProperties;
//    @Autowired
//    private WeChatProperties weChatProperties;
//    @Autowired
//    private WeChatMessageCodec weChatMessageCodec;
//
//    private WechatMessageCrypt wechatMessageCrypt;
//
//    @PostConstruct
//    private void initialize() {
//        wechatMessageCrypt = new WechatMessageCrypt(weChatProperties.getEncodingAesKey(), weChatProperties.getAppId());
//    }
//
//
//    /**
//     * 事件明文测试
//     */
//    @Test
//    public void startEventPlainTextTest() throws IOException, InterruptedException, AesException {
//        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
//
//        Resource[] eventMessageResources = pathMatchingResourcePatternResolver.getResources("mock/data/xml/event/*Message.xml");
//        Assertions.assertNotNull(eventMessageResources, "测试事件资源不应该为空");
//        CountDownLatch countDownLatch = new CountDownLatch(eventMessageResources.length);
//        String coreControllerUrl = weChatEventNettyServerProperties.getCoreControllerUrl();
//        for (Resource resource : eventMessageResources) {
//            InputStream inputStream = resource.getInputStream();
//            List<String> list = IOUtils.readLines(inputStream, "utf-8");
//            String xml = String.join("\n", list);
//
//
//
//            String randomStr = wechatMessageCrypt.getRandomStr();
//            String encrypt = wechatMessageCrypt.encrypt(randomStr, xml);
//            long timeMillis = System.currentTimeMillis();
//            String signature = SHA1.getSHA1(weChatProperties.getVerifyToken(), String.valueOf(timeMillis) , randomStr,encrypt);
//            String encParameters = "nonce="+randomStr + "&timestamp="+timeMillis + "&msg_signature=" +signature;
//            String xmlData = "<xml><Encrypt><![CDATA[" + encrypt + "]]></Encrypt><ToUserName>" + openId +"</ToUserName></xml>";
//            String url = coreControllerUrl + "?"+encParameters;
//            send(url,xmlData,(response)->{
//                Assertions.assertTrue(StringUtils.isNotBlank(response), "测试结果不应该为空");
//                try {
//                    String plainXML = wechatMessageCrypt.decryptXML(response);
//                    Assertions.assertTrue(StringUtils.isNotBlank(plainXML), "解密结果不应该为空");
//                    System.out.println(plainXML);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }finally {
//                    countDownLatch.countDown();
//                }
//            });
//        }
//        countDownLatch.await();
//    }
//
//    /**
//     * 消息明文测试
//     */
//    @Test
//    public void startMessagePlainTextTest() throws IOException, InterruptedException, AesException {
//        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
//        String coreControllerUrl = weChatEventNettyServerProperties.getCoreControllerUrl();
//
//        Resource[] messageResources = pathMatchingResourcePatternResolver.getResources("mock/data/xml/message/*Message.xml");
//        Assert.assertNotNull("测试消息资源不应该为空", messageResources);
//        List<Resource> resourcesList = new ArrayList<>(Arrays.asList(messageResources));
//
//        CountDownLatch countDownLatch = new CountDownLatch(resourcesList.size());
//        for (Resource resource : resourcesList) {
//            InputStream inputStream = resource.getInputStream();
//            List<String> list = IOUtils.readLines(inputStream, "utf-8");
//            String xml = String.join("\n", list);
//
//
//            String randomStr = wechatMessageCrypt.getRandomStr();
//            String encrypt = wechatMessageCrypt.encrypt(randomStr, xml);
//            long timeMillis = System.currentTimeMillis();
//            String signature = SHA1.getSHA1(weChatProperties.getVerifyToken(), String.valueOf(timeMillis) , randomStr,encrypt);
//            String encParameters = "nonce="+randomStr + "&timestamp="+timeMillis + "&msg_signature=" +signature;
//            String xmlData = "<xml><Encrypt>" + encrypt + "</Encrypt><ToUserName>" + openId +"</ToUserName></xml>";
//
//            String url = coreControllerUrl + "?"+encParameters;
//            send(url,xmlData,(response)->{
//                Assertions.assertTrue(StringUtils.isNotBlank(response), "测试结果不应该为空");
//                try {
//                    String plainXML = wechatMessageCrypt.decryptXML(response);
//                    Assertions.assertTrue(StringUtils.isNotBlank(plainXML), "解密结果不应该为空");
//                    System.out.println(plainXML);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }finally {
//                    countDownLatch.countDown();
//                }
//            });
//
//        }
//        countDownLatch.await();
//
//    }
//
//
//
//
//    private void send(String url, String xml, Consumer<String> responseConsumer) throws InterruptedException {
//        Bootstrap bootstrap = new Bootstrap();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        bootstrap.group(workerGroup);
//        bootstrap.channel(NioSocketChannel.class);
//        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
//        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
//            @Override
//            protected void initChannel(SocketChannel ch) throws Exception {
//                ch.pipeline().addLast(new HttpClientCodec());
//                ch.pipeline().addLast(new HttpContentDecompressor());
//                ch.pipeline().addLast(new HttpObjectAggregator(65536));
//                ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
//                    @Override
//                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
//                        ByteBuf content = msg.content();
//                        responseConsumer.accept(content.toString(StandardCharsets.UTF_8));
//                    }
//                });
//            }
//        });
//
//        ChannelFuture future = bootstrap.connect("127.0.0.1", weChatEventNettyServerProperties.getPort()).sync();
//
//        ByteBuf byteBuf = Unpooled.directBuffer(xml.length());
//        byteBuf.writeCharSequence(xml, CharsetUtil.UTF_8);
//
//        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,url, byteBuf);
//
//        request.headers().set(HttpHeaders.Names.HOST, "127.0.0.1");
//        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
//                request.content().readableBytes());
//        request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/xml");
//
//        future.channel().writeAndFlush(request);
//    }
//
//
//}
