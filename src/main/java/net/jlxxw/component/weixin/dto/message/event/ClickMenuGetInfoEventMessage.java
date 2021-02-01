package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.WeiXinMessage;

/**
 * 点击菜单拉取消息时的事件推送
 * @author chunyang.leng
 * @date 2021/1/22 7:25 下午
 */
public class ClickMenuGetInfoEventMessage extends WeiXinMessage {

    /**
     * 事件KEY值，与自定义菜单接口中KEY值对应
     */
    private String eventKey;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
}
