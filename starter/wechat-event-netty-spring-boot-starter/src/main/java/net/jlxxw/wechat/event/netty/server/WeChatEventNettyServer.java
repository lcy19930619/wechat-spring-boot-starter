package net.jlxxw.wechat.event.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.jlxxw.wechat.event.netty.properties.WeChatEventNettyServerProperties;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 通过netty接口提升微信核心接口性能
 *
 * @author chunyang.leng
 * @date 2021/1/25 9:31 上午
 */
@Component
public class WeChatEventNettyServer implements ApplicationRunner {
    private static final ServerBootstrap BOOTSTRAP = new ServerBootstrap();
    private static final Logger logger = LoggerFactory.getLogger(WeChatEventNettyServer.class);
    private final WeChatEventNettyServerProperties weChatEventNettyServerProperties;

    private final List<ChannelHandler> channelHandlerList;

    public WeChatEventNettyServer(WeChatEventNettyServerProperties weChatEventNettyServerProperties, List<ChannelHandler> channelHandlerList) {
        this.weChatEventNettyServerProperties = weChatEventNettyServerProperties;
        this.channelHandlerList = channelHandlerList;
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
            logger.info("用户配置关闭 wechat event netty 服务器");
            return;
        }
        Thread t = new Thread(() -> {
            LoggerUtils.info(logger, "初始化 netty 组件");
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
                            for (ChannelHandler channelHandler : channelHandlerList) {
                                socketChannel.pipeline().addLast( channelHandler);
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
                LoggerUtils.info(logger, "微信netty服务启动，开始监听端口: {}", weChatEventNettyServerProperties.getPort());
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                LoggerUtils.error(logger, "微信netty服务启动失败！！！", e);
                System.exit(0);
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
