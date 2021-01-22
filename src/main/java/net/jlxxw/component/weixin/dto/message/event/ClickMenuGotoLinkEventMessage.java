package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.WeiXinMessage;

/**
 * 点击菜单跳转链接时的事件推送
 * @author chunyang.leng
 * @date 2021/1/22 7:25 下午
 */
public class ClickMenuGotoLinkEventMessage extends WeiXinMessage {

    /**
     * 事件KEY值，设置的跳转URL
     */
    private String EventKey;

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }
}
