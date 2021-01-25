package net.jlxxw.component.weixin.component.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.jlxxw.component.weixin.properties.WeiXinNettyServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 通过netty接口提升微信核心接口性能
 * todo
 * @author chunyang.leng
 * @date 2021/1/25 9:31 上午
 */
@Component
public class WeiXinCoreComponent {
    private static ServerBootstrap bootstrap = new ServerBootstrap();
    private static final Logger logger = LoggerFactory.getLogger(WeiXinCoreComponent.class);
    @Autowired
    private WeiXinNettyServerProperties weiXinNettyServerProperties;

    @PostConstruct
    public void postConstruct(){
        if(!weiXinNettyServerProperties.getEnableNetty()){
            return;
        }
        //new 一个主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //new 一个工作线程组
        EventLoopGroup workGroup = new NioEventLoopGroup(200);
        bootstrap
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WeiXinChannel())
                .localAddress(weiXinNettyServerProperties.getNettyPort())
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        //绑定端口,开始接收进来的连接
        try {
            ChannelFuture future = bootstrap.bind(weiXinNettyServerProperties.getNettyPort()).sync();
            logger.info("服务器启动开始监听端口: {}", weiXinNettyServerProperties.getNettyPort());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭主线程组
            bossGroup.shutdownGracefully();
            //关闭工作线程组
            workGroup.shutdownGracefully();
        }
    }
}
