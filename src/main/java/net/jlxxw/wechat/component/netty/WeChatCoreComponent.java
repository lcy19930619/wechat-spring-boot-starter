package net.jlxxw.wechat.component.netty;

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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.jlxxw.wechat.properties.WeChatNettyServerProperties;
import net.jlxxw.wechat.util.LoggerUtils;
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
public class WeChatCoreComponent {
    private static final ServerBootstrap BOOTSTRAP = new ServerBootstrap();
    private static final Logger logger = LoggerFactory.getLogger(WeChatCoreComponent.class);
    @Autowired
    private WeChatNettyServerProperties weChatNettyServerProperties;
    @Autowired
    private WeChatChannel weChatChannel;

    @PostConstruct
    public void postConstruct() {
        if (!weChatNettyServerProperties.getEnableNetty()) {
            return;
        }
        Thread t = new Thread() {
            @Override
            public void run() {
                LoggerUtils.info(logger, "初始化 netty 组件");
                //new 一个主线程组
                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                //new 一个工作线程组
                EventLoopGroup workGroup = new NioEventLoopGroup(weChatNettyServerProperties.getMaxThreadSize());
                BOOTSTRAP
                        .group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                if (weChatNettyServerProperties.isEnableMetrics()){
                                    // 指标采集监控
                                    socketChannel.pipeline().addLast(new MetricsHandler());
                                }

                                // 请求解码器
                                socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                                LoggerUtils.debug(logger, "初始化 netty 请求解码器 成功");

                                // 将HTTP消息的多个部分合成一条完整的HTTP消息
                                socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(weChatNettyServerProperties.getHttpAggregatorMaxLength()));
                                LoggerUtils.debug(logger, "初始化 netty http聚合器 成功");

                                // 响应转码器
                                socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                                LoggerUtils.debug(logger, "初始化 netty 响应编码器 成功");

                                // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                                socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                                LoggerUtils.debug(logger, "初始化 netty 分块写入处理程序 成功");

                                // 读空闲检测，超时时间，默认 15 秒,作为微信客户端，只需要检测读超时即可
                                socketChannel.pipeline().addLast(new ReadTimeoutHandler(weChatNettyServerProperties.getChannelTimeout()));

                                if (weChatNettyServerProperties.isEnableLog()){
                                    // 日志调试，info 级别
                                    socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                                }

                                // 自定义处理handler
                                socketChannel.pipeline().addLast("http-server", weChatChannel);
                                LoggerUtils.debug(logger, "初始化 netty 微信协议处理器 成功");

                            }
                        })
                        .localAddress(weChatNettyServerProperties.getNettyPort())
                        //设置队列大小
                        .option(ChannelOption.SO_BACKLOG, weChatNettyServerProperties.getQueueSize())
                        // 不保持长链接
                        .childOption(ChannelOption.SO_KEEPALIVE, false);
                //绑定端口,开始接收进来的连接
                try {
                    ChannelFuture future = BOOTSTRAP.bind(weChatNettyServerProperties.getNettyPort()).sync();
                    LoggerUtils.info(logger, "微信netty服务启动，开始监听端口: {}", weChatNettyServerProperties.getNettyPort());
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
            }
        };
        t.setName(weChatNettyServerProperties.getMainThreadName());
        t.setDaemon(false);
        t.start();
    }

}
