package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.AbstractWeiXinMessage;

/**
 * 用户未关注时，进行关注后的事件推送
 * @author chunyang.leng
 * @date 2021/1/22 7:20 下午
 */
public class SubscribeQrsceneEventMessage extends AbstractWeiXinMessage {

    /**
     * 事件KEY值，qrscene_为前缀，后面为二维码的参数值
     */
    private String eventKey;

    /**
     *  二维码的ticket，可用来换取二维码图片
     */
    private String ticket;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}
