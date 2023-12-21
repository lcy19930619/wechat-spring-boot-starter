package net.jlxxw.wechat.event.properties;


import net.jlxxw.wechat.event.enums.Codec;

/**
 * 微信事件服务器配置类
 * @author chunyang.leng
 * @date 2023-12-19 00:26
 */
public class WeChatEventServerProperties {

    /**
     * 编解码器选择,默认明文方式
     */
    private Codec codec = Codec.PLAIN_TEXT;

    public Codec getCodec() {
        return codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

}
