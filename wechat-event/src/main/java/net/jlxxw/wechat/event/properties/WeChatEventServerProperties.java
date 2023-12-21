package net.jlxxw.wechat.event.properties;


/**
 * 微信事件服务器配置类
 * @author chunyang.leng
 * @date 2023-12-19 00:26
 */
public class WeChatEventServerProperties {
    /**
     * 是否启用消息编解码器
     */
    private boolean enableCodec;

    public boolean isEnableCodec() {
        return enableCodec;
    }

    public void setEnableCodec(boolean enableCodec) {
        this.enableCodec = enableCodec;
    }
}
