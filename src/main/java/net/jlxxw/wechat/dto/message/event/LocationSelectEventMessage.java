package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.dto.message.event.other.SendLocationInfo;

/**
 * 弹出地理位置选择器的事件推送
 *
 * @author chunyang.leng
 * @date 2021-12-19 6:12 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#7">文档地址</a>
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/User_Management/Gets_a_users_location.html">文档地址</a>
 */
public class LocationSelectEventMessage extends AbstractWeChatMessage {

    /**
     * 事件KEY值，由开发者在创建菜单时设定
     */
    private String eventKey;

    /**
     * 发送的位置信息
     */
    private SendLocationInfo sendLocationInfo;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public SendLocationInfo getSendLocationInfo() {
        return sendLocationInfo;
    }

    public void setSendLocationInfo(SendLocationInfo sendLocationInfo) {
        this.sendLocationInfo = sendLocationInfo;
    }
}
