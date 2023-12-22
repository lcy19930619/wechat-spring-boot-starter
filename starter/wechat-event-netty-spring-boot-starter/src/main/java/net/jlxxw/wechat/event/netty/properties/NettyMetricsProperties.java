package net.jlxxw.wechat.event.netty.properties;

public class NettyMetricsProperties {
    /**
     * 是否启用 netty metrics
     */
    private boolean enable = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
