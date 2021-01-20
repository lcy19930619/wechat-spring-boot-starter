package net.jlxxw.component.weixin.component.listener;

import net.jlxxw.component.weixin.enums.WeiXinMessageTypeEnum;

/**
 * 微信信息处理监听器
 * @author chunyang.leng
 * @date 2021/1/20 11:48 上午
 */
public abstract class WeiXinMessageListener {

    /**
     * 支持的事件类型
     * @return
     */
    public abstract WeiXinMessageTypeEnum supportMessageType();


}
