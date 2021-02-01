package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.WeiXinMessage;

/**
 * @author chunyang.leng
 * @date 2021/1/25 1:35 下午
 */
public class TemplateEventMessage extends WeiXinMessage {
	/**
	 * 消息id
	 */
	private Integer msgID;

	/**
	 * 发送状态为成功
	 */
	private String status;

	public Integer getMsgID() {
		return msgID;
	}

	public void setMsgID(Integer msgID) {
		this.msgID = msgID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
