package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;

/**
 * 模版消息事件
 * @link https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html#%E4%BA%8B%E4%BB%B6%E6%8E%A8%E9%80%81
 * @author chunyang.leng
 * @date 2021/1/25 1:35 下午
 */
public class TemplateEventMessage extends AbstractWeiXinMessage {
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
