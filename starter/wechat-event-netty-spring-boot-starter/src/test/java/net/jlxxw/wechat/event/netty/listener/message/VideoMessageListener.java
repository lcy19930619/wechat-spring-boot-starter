package net.jlxxw.wechat.event.netty.listener.message;

import net.jlxxw.wechat.event.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatMessageTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;

import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-18 6:21 下午
 */
@Component
public class VideoMessageListener extends AbstractWeChatMessageListener {
    /**
     * 支持的消息事件类型
     *
     * @return
     */
    @Override
    public WeChatMessageTypeEnum supportMessageType() {
        return WeChatMessageTypeEnum.VIDEO;
    }

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param abstractWeChatMessage
     * @return
     */
    @Override
    public WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage) {
        Assertions.assertNotNull(abstractWeChatMessage,"接收到的信息不应为空");

        return WeChatMessageResponse.buildVideo("mediaId","title","description");

    }
}
