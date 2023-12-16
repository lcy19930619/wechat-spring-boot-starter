package net.jlxxw.wechat.component.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 性能统计
 *
 * @author chunyang.leng
 * @date 2022/4/22 9:46 上午
 */
public class MetricsHandler extends ChannelDuplexHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsHandler.class);

    /**
     * 采集是否开始
     */
    private static final AtomicBoolean START_TASK = new AtomicBoolean(false);
    /**
     * 在线的 channel 总数
     */
    private static final AtomicLong CHANNEL_COUNT = new AtomicLong(0);
    /**
     * 总共读取字节数
     */
    private static final AtomicLong TOTAL_READ_BYTES = new AtomicLong(0);
    /**
     * 总共写出字节数
     */
    private static final AtomicLong TOTAL_WRITE_BYTES = new AtomicLong(0);
    /**
     * ChannelGroup用来保存所有已经连接的Channel
     */
    private final static ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private static final ScheduledExecutorService statService = new ScheduledThreadPoolExecutor(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("netty-metrics-thread");
        thread.setDaemon(false);
        return thread;
    });

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CHANNEL_COUNT.incrementAndGet();
        if (START_TASK.compareAndSet(false, true)) {
            statService.scheduleAtFixedRate(() -> {
                LOG.info("----------------性能指标采集开始-------------------");
                /*目前有多少在线Channel*/
                LOG.info("目前在线Channel数：" + CHANNEL_COUNT.get());

                // I/O线程池待处理队列大小
                for (EventExecutor eventExecutor : ctx.executor().parent()) {
                    SingleThreadEventExecutor executor =
                            (SingleThreadEventExecutor) eventExecutor;
                    int size = executor.pendingTasks();
                    if (executor == ctx.executor()) {
                        LOG.info(ctx.channel() + ":" + executor + "待处理队列大小 :  " + size);
                    } else {
                        LOG.info(executor + " 待处理队列大小 : " + size);
                    }
                }
                // 发送队列积压字节数
                for (Channel channel : CHANNEL_GROUP) {
                    if (channel instanceof ServerChannel) {
                        continue;
                    }
                    LOG.info(channel + "发送缓存积压字节数：" + channel.unsafe().outboundBuffer().totalPendingWriteBytes());
                }

                LOG.info("读取速率(字节/秒)：" + TOTAL_READ_BYTES.getAndSet(0));
                LOG.info("写出速率(字节/秒)：" + TOTAL_WRITE_BYTES.getAndSet(0));

                LOG.info("----------------性能指标采集结束-------------------");
            }, 0,   1, TimeUnit.SECONDS);
        }
        CHANNEL_GROUP.add(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int readableBytes = ((ByteBuf) msg).readableBytes();
        TOTAL_READ_BYTES.getAndAdd(readableBytes);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        int writeableBytes = ((ByteBuf) msg).readableBytes();
        TOTAL_WRITE_BYTES.getAndAdd(writeableBytes);
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        CHANNEL_COUNT.decrementAndGet();
        CHANNEL_GROUP.remove(ctx.channel());
        super.channelInactive(ctx);
    }
}
