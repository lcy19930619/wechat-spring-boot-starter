package net.jlxxw.wechat.event.netty.listener;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import net.jlxxw.wechat.event.netty.WeChatEventNettyAutoConfiguration;
import net.jlxxw.wechat.event.netty.properties.WeChatEventNettyServerProperties;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author chunyang.leng
 * @date 2023-12-25 18:34
 */
@TestPropertySource("classpath:application-plain.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatEventNettyAutoConfiguration.class)
public class ListenerPlainTextTest {

    @Autowired
    private WeChatEventNettyServerProperties weChatEventNettyServerProperties;

    /**
     * 事件明文测试
     */
    @Test
    public void startEventPlainTextTest() throws IOException, InterruptedException {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] eventMessageResources = pathMatchingResourcePatternResolver.getResources("mock/data/xml/event/*Message.xml");
        Assertions.assertNotNull(eventMessageResources, "测试事件资源不应该为空");
        CountDownLatch countDownLatch = new CountDownLatch(eventMessageResources.length);
        String coreControllerUrl = weChatEventNettyServerProperties.getCoreControllerUrl();
        for (Resource resource : eventMessageResources) {
            InputStream inputStream = resource.getInputStream();
            List<String> list = IOUtils.readLines(inputStream, "utf-8");
            String xmlData = String.join("\n", list);

            send(coreControllerUrl,xmlData,(response)->{
                System.out.println(response);
                Assertions.assertTrue(StringUtils.isNotBlank(response), "测试结果不应该为空");
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }

    /**
     * 消息明文测试
     */
    @Test
    public void startMessagePlainTextTest() throws IOException, InterruptedException {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        String coreControllerUrl = weChatEventNettyServerProperties.getCoreControllerUrl();

        Resource[] messageResources = pathMatchingResourcePatternResolver.getResources("mock/data/xml/message/*Message.xml");
        Assert.assertNotNull("测试消息资源不应该为空", messageResources);
        List<Resource> resourcesList = new ArrayList<>(Arrays.asList(messageResources));

        CountDownLatch countDownLatch = new CountDownLatch(resourcesList.size());
        for (Resource resource : resourcesList) {
            InputStream inputStream = resource.getInputStream();
            List<String> list = IOUtils.readLines(inputStream, "utf-8");
            String xmlData = String.join("\n", list);

            send(coreControllerUrl,xmlData,(response)->{
                System.out.println(response);
                Assertions.assertTrue(StringUtils.isNotBlank(response), "测试结果不应该为空");
                countDownLatch.countDown();
            });

        }
        countDownLatch.await();

    }




    private void send(String url, String xml, Consumer<String> responseConsumer) throws InterruptedException {
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

        ByteBuf byteBuf = Unpooled.directBuffer(xml.length());
        byteBuf.writeCharSequence(xml, CharsetUtil.UTF_8);

        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,url, byteBuf);

        request.headers().set(HttpHeaders.Names.HOST, "127.0.0.1");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
                request.content().readableBytes());
        request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/xml");

        future.channel().writeAndFlush(request);
    }

}
