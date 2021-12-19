package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;

/**
 * 点击菜单跳转小程序的事件推送
 *
 * @author chunyang.leng
 * @date 2021-12-19 6:18 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#8">文档地址</>
 */
public class ViewMiniProgramEventMessage extends AbstractWeiXinMessage {

    /**
     * 事件KEY值，跳转的小程序路径
     */
    private String eventKey;

    /**
     * 菜单ID，如果是个性化菜单，则可以通过这个字段，知道是哪个规则的菜单被点击了
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
