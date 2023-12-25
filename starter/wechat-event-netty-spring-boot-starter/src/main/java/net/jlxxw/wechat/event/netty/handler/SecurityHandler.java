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

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * netty 安全处理器,由 security 模块进行装配
 */
@ChannelHandler.Sharable
public class SecurityHandler extends ChannelInboundHandlerAdapter implements SecurityFilterTemplate {

    private final IpSegmentRepository ipSegmentRepository;
    private final BlackList blackList;
    public SecurityHandler(IpSegmentRepository ipSegmentRepository, BlackList blackList) {
        this.ipSegmentRepository = ipSegmentRepository;
        this.blackList = blackList;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        // 获取远程ip地址信息
        String ipAddress = socketAddress.getAddress().getHostAddress();
        boolean security = security(ipAddress);
        if (!security) {
            reject(ipAddress);
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
        blackList.add(ip);
    }


    private FullHttpResponse response(ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, content);
        response.headers().set("Content-Type", "application/xml;charset=UTF-8");
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }
}
