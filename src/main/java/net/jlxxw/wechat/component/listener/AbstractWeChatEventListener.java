package net.jlxxw.wechat.component.listener;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatEventTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;

/**
 * 抽象的微信事件处理监听器
 *
 * @author chunyang.leng
 * @date 2021/1/22 7:07 下午
 */
public abstract class AbstractWeChatEventListener {


    /**
     * 支持的微信事件类型
     *
     * @return 微信事件类型
     */
    public abstract WeChatEventTypeEnum supportEventType();

    /**
     * 处理微信消息
     *
     * @param abstractWeChatMessage 微信服务器发送的消息
     * @return 返回给微信服务器的消息,return null时，会转换为 "" 返回到微信服务器
     */
    public abstract WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage);
}
