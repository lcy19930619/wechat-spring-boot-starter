package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.dto.message.event.other.SendPicsInfo;

/**
 * @author chunyang.leng
 * @date 2021-12-19 6:10 下午
 */
public class PicWeChatEventMessage extends AbstractWeChatMessage {

    /**
     * 事件KEY值，由开发者在创建菜单时设定
     */
    private String eventKey;

    /**
     * 发送的图片信息
     */
    private SendPicsInfo sendPicsInfo;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public SendPicsInfo getSendPicsInfo() {
        return sendPicsInfo;
    }

    public void setSendPicsInfo(SendPicsInfo sendPicsInfo) {
        this.sendPicsInfo = sendPicsInfo;
    }

}
