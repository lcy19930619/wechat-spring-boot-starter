package net.jlxxw.wechat.event.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import net.jlxxw.wechat.event.netty.properties.HttpObjectAggregatorProperties;
import net.jlxxw.wechat.event.netty.properties.HttpRequestDecoderProperties;
import net.jlxxw.wechat.event.netty.properties.IdleStateProperties;
import net.jlxxw.wechat.event.netty.properties.WeChatEventNettyServerProperties;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 公众号开发组件
 * 事件模式入口 -- netty 模式
 *
 * @author chunyang.leng
 * @date 2021/1/25 9:31 上午
 */
@Component
public class WeChatEventNettyServer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(WeChatEventNettyServer.class);
    private final WeChatEventNettyServerProperties weChatEventNettyServerProperties;

    private final List<ChannelHandler> channelHandlerList;

    private final HttpObjectAggregatorProperties httpObjectAggregatorProperties;

    private final HttpRequestDecoderProperties httpRequestDecoderProperties;

    private final IdleStateProperties idleStateProperties;

    public WeChatEventNettyServer(WeChatEventNettyServerProperties weChatEventNettyServerProperties,
                                  List<ChannelHandler> channelHandlerList,
                                  HttpObjectAggregatorProperties httpObjectAggregatorProperties,
                                  HttpRequestDecoderProperties httpRequestDecoderProperties,
                                  IdleStateProperties idleStateProperties) {
        this.weChatEventNettyServerProperties = weChatEventNettyServerProperties;
        this.channelHandlerList = channelHandlerList;
        this.httpObjectAggregatorProperties = httpObjectAggregatorProperties;
        this.httpRequestDecoderProperties = httpRequestDecoderProperties;
        this.idleStateProperties = idleStateProperties;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!weChatEventNettyServerProperties.getEnable()) {
            logger.warn("公众号组件 ---> 用户配置关闭 netty 服务器");
            return;
        }
        Thread t = new Thread(() -> {
            ServerBootstrap BOOTSTRAP = new ServerBootstrap();
            LoggerUtils.info(logger, "公众号组件 ---> 初始化 netty 监听线程");
            //new 一个主线程组
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            //new 一个工作线程组
            EventLoopGroup workGroup = new NioEventLoopGroup(weChatEventNettyServerProperties.getMaxThreadSize());
            BOOTSTRAP
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // http 请求解码器
                            HttpRequestDecoder httpRequestDecoder = new HttpRequestDecoder(httpRequestDecoderProperties.getMaxInitialLineLength(), httpRequestDecoderProperties.getMaxHeaderSize(), httpRequestDecoderProperties.getMaxChunkSize());
                            socketChannel.pipeline().addLast(httpRequestDecoder);

                            // http 聚合器
                            HttpObjectAggregator httpObjectAggregator = new HttpObjectAggregator(httpObjectAggregatorProperties.getMaxContentLength());
                            socketChannel.pipeline().addLast(httpObjectAggregator);

                            // http 应答编码器
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                            // http 分块
                            socketChannel.pipeline().addLast(new ChunkedWriteHandler());

                            // 空闲检测
                            IdleStateHandler idleStateHandler = new IdleStateHandler(idleStateProperties.getReaderIdleTimeSeconds(), idleStateProperties.getWriterIdleTimeSeconds(), idleStateProperties.getAllIdleTimeSeconds());
                            socketChannel.pipeline().addLast(idleStateHandler);


                            for (ChannelHandler channelHandler : channelHandlerList) {
                                socketChannel.pipeline().addLast( channelHandler);
                                LoggerUtils.debug(logger,"公众号组件 ---> 装配:{}",channelHandler.getClass());
                            }
                        }
                    })
                    .localAddress(weChatEventNettyServerProperties.getPort())
                    //设置队列大小
                    .option(ChannelOption.SO_BACKLOG, weChatEventNettyServerProperties.getQueueSize())
                    // 不保持长链接
                    .childOption(ChannelOption.SO_KEEPALIVE, false);
            //绑定端口,开始接收进来的连接
            try {
                ChannelFuture future = BOOTSTRAP.bind(weChatEventNettyServerProperties.getPort()).sync();
                LoggerUtils.info(logger, "公众号组件 ---> netty 服务启动完毕，开始监听端口: {}", weChatEventNettyServerProperties.getPort());
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                LoggerUtils.error(logger, "公众号组件 ---> netty 服务收到中断，netty 服务关闭！！！", e);
                //关闭主线程组
                bossGroup.shutdownGracefully();
                //关闭工作线程组
                workGroup.shutdownGracefully();
            } finally {
                //关闭主线程组
                bossGroup.shutdownGracefully();
                //关闭工作线程组
                workGroup.shutdownGracefully();
            }
        });
        t.setName(weChatEventNettyServerProperties.getMainThreadName());
        t.setDaemon(false);
        t.start();
    }
}
