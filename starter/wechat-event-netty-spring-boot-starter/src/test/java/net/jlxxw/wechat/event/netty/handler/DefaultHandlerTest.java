package net.jlxxw.wechat.event.netty.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import net.jlxxw.wechat.event.netty.WeChatEventNettyAutoConfiguration;
import net.jlxxw.wechat.event.netty.properties.WeChatEventNettyServerProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author chunyang.leng
 * @date 2023-12-26 16:20
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatEventNettyAutoConfiguration.class)
public class DefaultHandlerTest {

    @Autowired
    private WeChatEventNettyServerProperties weChatEventNettyServerProperties;


    @Test
    public void test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String url = UUID.randomUUID().toString().replace("-","");
        send(url,(response)->{
            Assertions.assertNotNull(response,"测试结果不应该是空的");
            HttpResponseStatus status = response.status();
            Assertions.assertEquals(HttpResponseStatus.NOT_FOUND,status,"应该是  404 ");
            countDownLatch.countDown();
        });

        countDownLatch.await();
    }


    private void send(String url, Consumer<FullHttpResponse> responseConsumer) throws InterruptedException {
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
                        responseConsumer.accept(msg);
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
        request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/xml");

        future.channel().writeAndFlush(request);
    }

}
