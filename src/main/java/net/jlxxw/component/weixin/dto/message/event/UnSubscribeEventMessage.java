package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.AbstractWeiXinMessage;

/**
 * 取消关注事件
 * @author chunyang.leng
 * @date 2021/1/22 7:17 下午
 */
public class UnSubscribeEventMessage extends AbstractWeiXinMessage {

    /**
     * 事件类型
     */
    private String eventKey;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
}
