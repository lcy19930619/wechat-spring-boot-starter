package net.jlxxw.wechat.event.component.listener;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatMessageTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;

/**
 * 抽象的微信信息处理监听器
 *
 * @author chunyang.leng
 * @date 2021/1/20 11:48 上午
 */
public abstract class AbstractWeChatMessageListener {

    /**
     * 支持的消息事件类型
     *
     * @return 微信消息类型
     */
    public abstract WeChatMessageTypeEnum supportMessageType();

    /**
     * 处理微信消息
     *
     * @param abstractWeChatMessage 微信服务器发送的消息
     * @return 返回给微信服务器的消息,return null时，会转换为 "" 返回到微信服务器
     */
    public abstract WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage);
}
