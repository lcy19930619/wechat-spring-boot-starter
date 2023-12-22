package net.jlxxw.wechat.security.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import net.jlxxw.wechat.repository.ip.IpSegmentRepository;
import net.jlxxw.wechat.security.filter.SecurityFilterTemplate;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * netty 安全处理器
 */
public class SecurityHandler extends ChannelInboundHandlerAdapter implements SecurityFilterTemplate {

    private final IpSegmentRepository ipSegmentRepository;

    public SecurityHandler(IpSegmentRepository ipSegmentRepository) {
        this.ipSegmentRepository = ipSegmentRepository;
    }

    private ChannelHandlerContext channelHandlerContext;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channelHandlerContext = ctx;
        InetSocketAddress socketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        // 获取远程ip地址信息
        String ipAddress = socketAddress.getAddress().getHostAddress();
        boolean security = security(ipAddress);
        if (!security) {
            reject(ipAddress);
            return;
        }
        // 安全，可以继续转发
        super.channelActive(ctx);
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
        FullHttpResponse forbidden = response(Unpooled.copiedBuffer("IP FORBIDDEN", CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush(forbidden)
                .addListener(ChannelFutureListener.CLOSE);
    }

    private FullHttpResponse response(ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, content);
        response.headers().set("Content-Type", "application/xml;charset=UTF-8");
        response.headers().set("Content_Length", response.content().readableBytes());
        return response;
    }
}
