package net.jlxxw.wechat.component.listener;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.enums.WeChatEventTypeEnum;
import net.jlxxw.wechat.response.WeChatMessageResponse;

/**
 * 微信事件处理模版
 *
 * @author chunyang.leng
 * @date 2021/1/22 7:07 下午
 */
public abstract class AbstractWeiXinEventListener {


    /**
     * 支持的事件类型
     *
     * @return
     */
    public abstract WeChatEventTypeEnum supportEventType();

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     *
     * @param abstractWeChatMessage
     * @return
     */
    public abstract WeChatMessageResponse handler(AbstractWeChatMessage abstractWeChatMessage);
}
