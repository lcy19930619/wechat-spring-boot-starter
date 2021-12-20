package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;

/**
 * 点击菜单拉取消息时的事件推送
 *
 * @author chunyang.leng
 * @date 2021/1/22 7:25 下午
 * @see <a href=" https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#0">文档地址</a>
 */
public class ClickMenuGetInfoEventMessage extends AbstractWeiXinMessage {

    /**
     * 事件KEY值，与自定义菜单接口中KEY值对应
     */
    private String eventKey;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
