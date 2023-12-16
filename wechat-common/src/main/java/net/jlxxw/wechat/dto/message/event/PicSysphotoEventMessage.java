package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.dto.message.event.other.SendPicsInfo;

/**
 * 弹出系统拍照发图的事件推送 todo
 *
 * @author chunyang.leng
 * @date 2021-12-17 8:15 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#4">文档地址</a>
 */
public class PicSysphotoEventMessage extends AbstractWeChatMessage {

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
