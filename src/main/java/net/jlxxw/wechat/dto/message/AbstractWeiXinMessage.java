package net.jlxxw.wechat.dto.message;

/**
 * 微信信息
 * @author chunyang.leng
 * @date 2021/1/20 11:15 上午
 */
public abstract class AbstractWeiXinMessage {
    /**
     * 开发者微信号
     */
    private String toUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private Long createTime;

    /**
     * 消息类型
     * 文本为text
     * 图片为image
     * 语音为voice
     * 视频为video
     * 小视频为shortvideo
     * 地理位置为location
     * 链接为link
	 * 事件为event
     */
    private String msgType;

    /**
     * 消息id，64位整型
     */
    private Long msgId;

	/**
	 * 事件
	 */
	private String event;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
