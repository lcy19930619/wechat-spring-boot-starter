package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.WeiXinMessage;

/**
 * 取消关注事件
 * @author chunyang.leng
 * @date 2021/1/22 7:17 下午
 */
public class UnSubscribeEventMessage extends WeiXinMessage {

    /**
     * 事件类型
     */
    private String EventKey;

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }
}
