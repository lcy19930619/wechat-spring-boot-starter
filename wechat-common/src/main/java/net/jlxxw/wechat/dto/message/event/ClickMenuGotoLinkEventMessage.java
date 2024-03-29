package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;

/**
 * 点击菜单跳转链接时的事件推送
 *
 * @author chunyang.leng
 * @date 2021/1/22 7:25 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#1">文档地址</a>
 */
public class ClickMenuGotoLinkEventMessage extends AbstractWeChatMessage {

    /**
     * 事件KEY值，设置的跳转URL
     */
    private String eventKey;

    /**
     * 指菜单ID，如果是个性化菜单，则可以通过这个字段，知道是哪个规则的菜单被点击了。
     */
    private String menuId;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
