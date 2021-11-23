package net.jlxxw.component.weixin.component.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import net.jlxxw.component.weixin.properties.WeiXinNettyServerProperties;
import net.jlxxw.component.weixin.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 通过netty接口提升微信核心接口性能
 *
 * @author chunyang.leng
 * @date 2021/1/25 9:31 上午
 */
@Component
public class WeiXinCoreComponent {
    private static final ServerBootstrap BOOTSTRAP = new ServerBootstrap();
    private static final Logger logger = LoggerFactory.getLogger(WeiXinCoreComponent.class);
    @Autowired
    private WeiXinNettyServerProperties weiXinNettyServerProperties;
    @Autowired
    private WeiXinChannel weiXinChannel;

    @PostConstruct
    public void postConstruct() {
        if (!weiXinNettyServerProperties.getEnableNetty()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                LoggerUtils.info(logger,"初始化 netty 组件");
                //new 一个主线程组
                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                //new 一个工作线程组
                EventLoopGroup workGroup = new NioEventLoopGroup(weiXinNettyServerProperties.getMaxThreadSize());
                BOOTSTRAP
                        .group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                // 请求解码器
                                socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                                LoggerUtils.info(logger,"初始化 netty 请求解码器 成功");

                                // 将HTTP消息的多个部分合成一条完整的HTTP消息
                                socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                                LoggerUtils.info(logger,"初始化 netty http聚合器 成功");

                                // 响应转码器
                                socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                                LoggerUtils.info(logger,"初始化 netty 响应编码器 成功");

                                // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                                socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                                LoggerUtils.info(logger,"初始化 netty 分块写入处理程序 成功");

                                // 自定义处理handler
                                socketChannel.pipeline().addLast("http-server", weiXinChannel);
                                LoggerUtils.info(logger,"初始化 netty 微信协议处理器 成功");

                            }
                        })
                        .localAddress(weiXinNettyServerProperties.getNettyPort())
                        //设置队列大小
                        .option(ChannelOption.SO_BACKLOG, weiXinNettyServerProperties.getQueueSize())
                        // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                //绑定端口,开始接收进来的连接
                try {
                    ChannelFuture future = BOOTSTRAP.bind(weiXinNettyServerProperties.getNettyPort()).sync();
                    LoggerUtils.info(logger,"微信netty服务启动，开始监听端口: {}", weiXinNettyServerProperties.getNettyPort());
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    LoggerUtils.error(logger,"微信netty服务启动失败！！！", e);
                    System.exit(0);
                } finally {
                    //关闭主线程组
                    bossGroup.shutdownGracefully();
                    //关闭工作线程组
                    workGroup.shutdownGracefully();
                }
            }
        }.start();
    }

}
