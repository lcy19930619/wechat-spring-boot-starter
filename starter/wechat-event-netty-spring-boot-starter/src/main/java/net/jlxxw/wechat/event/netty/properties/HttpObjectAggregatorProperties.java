package net.jlxxw.wechat.event.netty.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("wechat.event.server.netty.http.object.aggregator")
public class HttpObjectAggregatorProperties {

    private int maxContentLength = 65535;

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }
}
