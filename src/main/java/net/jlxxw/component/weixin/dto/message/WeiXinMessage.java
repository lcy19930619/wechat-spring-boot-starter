package net.jlxxw.component.weixin.dto.message;

/**
 * 微信信息
 * @author chunyang.leng
 * @date 2021/1/20 11:15 上午
 */
public abstract class WeiXinMessage {
    /**
     * 开发者微信号
     */
    private String ToUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    private String FromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private Long CreateTime;

    /**
     * 消息类型，
     * 文本为text
     * 图片为image
     * 语音为voice
     * 视频为video
     * 小视频为shortvideo
     * 地理位置为location
     * 链接为link
     */
    private String MsgType;

    /**
     * 消息id，64位整型
     */
    private Long MsgId;





    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public Long getMsgId() {
        return MsgId;
    }

    public void setMsgId(Long msgId) {
        MsgId = msgId;
    }
}
