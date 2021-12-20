package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;
import net.jlxxw.wechat.dto.message.event.other.SendPicsInfo;

/**
 * 弹出拍照或者相册发图的事件推送
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#5">文档地址</a>
 * @author chunyang.leng
 * @date 2021-12-19 6:06 下午
 */
public class PicPhotoOrAlbumEventMessage extends AbstractWeiXinMessage {

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