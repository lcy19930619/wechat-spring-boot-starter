package net.jlxxw.wechat.event.netty.listener.message;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.dto.message.TextMessage;
import net.jlxxw.wechat.enums.WeChatMessageTypeEnum;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2021-12-18 6:19 下午
 */
@Component
public class TextMessageListener extends AbstractWeChatMessageListener {
    /**
     * 支持的消息事件类型
     *
     * @return
     */
    @Override
    public WeChatMessageTypeEnum supportMessageType() {
        return WeChatMessageTypeEnum.TEXT;
    }

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param abstractWeChatMessage
     * @return
     */
    @Override
    public WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage) {
        // 类型转化参考 supportMessageType() 方法中枚举定义
        TextMessage textMessage = (TextMessage)abstractWeChatMessage;
        // 用户发送的内容
        String content = textMessage.getContent();
        // 用户openId
        String fromUserName = textMessage.getFromUserName();


        Assert.assertNotNull("接收到的信息不应为空", abstractWeChatMessage);
        return WeChatMessageResponse.buildText("content");
    }
}
