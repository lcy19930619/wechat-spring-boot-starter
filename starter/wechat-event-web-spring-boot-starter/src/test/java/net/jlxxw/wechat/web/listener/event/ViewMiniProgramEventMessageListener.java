package net.jlxxw.wechat.web.listener.event;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatEventTypeEnum;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.response.WeChatMessageResponse;

import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-19 7:09 下午
 */
@Component
public class ViewMiniProgramEventMessageListener extends AbstractWeChatEventListener {
    /**
     * 支持的事件类型
     *
     * @return
     */
    @Override
    public WeChatEventTypeEnum supportEventType() {
        return WeChatEventTypeEnum.VIEW_MINIPROGRAM;
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
        return WeChatMessageResponse.buildText(supportEventType().getDescription() + " done");
    }
}