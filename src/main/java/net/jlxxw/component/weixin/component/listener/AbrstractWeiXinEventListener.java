package net.jlxxw.component.weixin.component.listener;

import net.jlxxw.component.weixin.dto.message.AbrstractWeiXinMessage;
import net.jlxxw.component.weixin.enums.WeiXinEventTypeEnum;
import net.jlxxw.component.weixin.response.WeiXinMessageResponse;

/**
 * 微信事件处理模版
 * @author chunyang.leng
 * @date 2021/1/22 7:07 下午
 */
public abstract class AbrstractWeiXinEventListener {


    /**
     * 支持的事件类型
     * @return
     */
    public abstract WeiXinEventTypeEnum supportEventType();

    /**
     * 处理微信消息 ,return null时，会转换为 "" 返回到微信服务器
     * @param abrstractWeiXinMessage
     * @return
     */
    public abstract WeiXinMessageResponse handler(AbrstractWeiXinMessage abrstractWeiXinMessage);
}
