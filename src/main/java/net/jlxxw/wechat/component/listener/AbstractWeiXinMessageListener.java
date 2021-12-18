package net.jlxxw.wechat.component.listener;

import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;
import net.jlxxw.wechat.enums.WeiXinMessageTypeEnum;
import net.jlxxw.wechat.response.WeiXinMessageResponse;

/**
 * 微信信息处理监听器
 * @author chunyang.leng
 * @date 2021/1/20 11:48 上午
 */
public abstract class AbstractWeiXinMessageListener {

    /**
     * 支持的消息事件类型
     * @return
     */
    public abstract WeiXinMessageTypeEnum supportMessageType();

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     * @param abstractWeiXinMessage
     * @return
     */
    public abstract WeiXinMessageResponse handler(AbstractWeiXinMessage abstractWeiXinMessage);
}
