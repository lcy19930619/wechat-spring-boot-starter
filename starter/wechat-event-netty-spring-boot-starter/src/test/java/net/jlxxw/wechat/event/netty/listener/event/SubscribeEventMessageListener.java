package net.jlxxw.wechat.event.netty.listener.event;

import net.jlxxw.wechat.dto.message.event.SubscribeEventMessage;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatEventTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;

import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-19 7:08 下午
 */
@Component
public class SubscribeEventMessageListener extends AbstractWeChatEventListener {
    /**
     * 支持的事件类型
     *
     * @return
     */
    @Override
    public WeChatEventTypeEnum supportEventType() {
        return WeChatEventTypeEnum.SUBSCRIBE;
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
        // 类型转化参考 supportMessageType() 方法中枚举定义
        SubscribeEventMessage subscribeEventMessage = (SubscribeEventMessage)abstractWeChatMessage;
        // 用户关注 event key
        String eventKey = subscribeEventMessage.getEventKey();
        // 用户openId
        String fromUserName = subscribeEventMessage.getFromUserName();


        return WeChatMessageResponse.buildText(supportEventType().getDescription() + " done");
    }
}