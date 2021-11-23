package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.AbrstractWeiXinMessage;

/**
 * 点击菜单跳转链接时的事件推送
 * @author chunyang.leng
 * @date 2021/1/22 7:25 下午
 */
public class ClickMenuGotoLinkEventMessageAbrstract extends AbrstractWeiXinMessage {

    /**
     * 事件KEY值，设置的跳转URL
     */
    private String eventKey;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
}
