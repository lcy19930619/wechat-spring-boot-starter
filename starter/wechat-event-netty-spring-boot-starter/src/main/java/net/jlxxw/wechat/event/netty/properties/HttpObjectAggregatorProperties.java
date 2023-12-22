package net.jlxxw.wechat.event.netty.properties;

public class HttpObjectAggregatorProperties {

    private int maxContentLength = 65535;

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }
}
