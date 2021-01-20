package net.jlxxw.component.weixin.component.listener;

import net.jlxxw.component.weixin.dto.message.WeiXinMessage;
import net.jlxxw.component.weixin.enums.WeiXinMessageTypeEnum;
import net.jlxxw.component.weixin.response.WeiXinMessageResponse;

/**
 * 微信信息处理监听器
 * @author chunyang.leng
 * @date 2021/1/20 11:48 上午
 */
public abstract class WeiXinMessageListener {

    /**
     * 支持的消息事件类型
     * @return
     */
    public abstract WeiXinMessageTypeEnum supportMessageType();

    /**
     * 处理微信消息
     * @param weiXinMessage
     * @return
     */
    public abstract WeiXinMessageResponse handler(WeiXinMessage weiXinMessage);
}
