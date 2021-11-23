package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.AbrstractWeiXinMessage;

/**
 * 用户已关注时的事件推送
 * @author chunyang.leng
 * @date 2021/1/22 7:17 下午
 */
public class SubscribeScanEventMessageAbrstract extends AbrstractWeiXinMessage {

    /**
     * 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
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
