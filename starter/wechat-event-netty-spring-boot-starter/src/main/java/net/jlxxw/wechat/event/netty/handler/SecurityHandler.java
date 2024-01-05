package net.jlxxw.wechat.event.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import net.jlxxw.wechat.repository.ip.IpSegmentRepository;
import net.jlxxw.wechat.security.blacklist.BlackList;
import net.jlxxw.wechat.security.template.SecurityFilterTemplate;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * 简单的 netty 安全处理器,仅用于检测数据是否来自微信服务器，
 * 由 wechat-security-spring-boot-starter 模块进行装配
 * @author lcy
 */
@ChannelHandler.Sharable
public class SecurityHandler extends ChannelInboundHandlerAdapter implements SecurityFilterTemplate {

    private static final Logger logger = LoggerFactory.getLogger(SecurityHandler.class);

    private final IpSegmentRepository ipSegmentRepository;
    private final BlackList blackList;

    /**
     * 构建一个简单的安全处理器
     * @param ipSegmentRepository 可信ip存储器
     * @param blackList 黑名单列表模块
     */
    public SecurityHandler(IpSegmentRepository ipSegmentRepository, BlackList blackList) {
        this.ipSegmentRepository = ipSegmentRepository;
        this.blackList = blackList;
        LoggerUtils.info(logger,"公众号组件 ---> netty模式 ip 安全检查器已启动");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        // 获取远程ip地址信息
        String ipAddress = socketAddress.getAddress().getHostAddress();
        LoggerUtils.debug(logger,"公众号组件 ---> netty模式 ip 安全检查,发现请求ip地址:{}",ipAddress);

        LoggerUtils.debug(logger,"公众号组件 ---> netty模式 ip 安全检查,发现请求ip地址:{},开始进行安全检查",ipAddress);
        boolean security = security(ipAddress);
        LoggerUtils.debug(logger,"公众号组件 ---> netty模式 ip 安全检查,发现请求ip地址:{},安全检查结束,是否允许通过:{}",ipAddress,security);

        if (!security) {
            // 拒绝本次调用，并记录ip地址
            reject(ipAddress);
            // 返回 403
            FullHttpResponse forbidden = response(Unpooled.copiedBuffer("IP FORBIDDEN", CharsetUtil.UTF_8));
            ctx.writeAndFlush(forbidden)
                    .addListener(ChannelFutureListener.CLOSE);
            return;
        }
        // 安全，可以继续转发
        super.channelActive(ctx);
    }

    /**
     * 判断此ip是否在黑名单列表中
     *
     * @param ip 目标ip
     * @return true 在黑名单中, false 不在黑名单中
     */
    @Override
    public boolean blacklisted(String ip) {
        return blackList.include(ip);
    }

    /**
     * 加载全部ip段信息
     *
     * @return 全部可信任的ip段
     */
    @Override
    public Set<String> loadAllIpSegments() {
        return ipSegmentRepository.findAll();
    }

    /**
     * 拒绝此ip链接
     *
     * @param ip ip
     */
    @Override
    public void reject(String ip) {
        LoggerUtils.info(logger,"公众号组件 ---> netty模式 ip 安全检查,发现请求ip地址:{},执行拒绝处理",ip);
        blackList.add(ip);
    }


    private FullHttpResponse response(ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, content);
        response.headers().set("Content-Type", "application/xml;charset=UTF-8");
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }
}
