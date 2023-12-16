package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;
import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 菜单返回数据
 *
 * @author chunyang.leng
 * @date 2021-12-20 10:57 上午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Querying_Custom_Menus.html">文档地址</a>
 */

public class MenuResponse extends WeChatResponse {

    /**
     * 菜单是否开启，0代表未开启，1代表开启
     */
    @JSONField(name = "is_menu_open")
    private Integer isMenuOpen;

    /**
     * 菜单信息
     */
    @JSONField(name = "selfmenu_info")
    private SelfmenuInfo selfmenuInfo;

    public Integer getIsMenuOpen() {
        return isMenuOpen;
    }

    public void setIsMenuOpen(Integer isMenuOpen) {
        this.isMenuOpen = isMenuOpen;
    }

    public SelfmenuInfo getSelfmenuInfo() {
        return selfmenuInfo;
    }

    public void setSelfmenuInfo(SelfmenuInfo selfmenuInfo) {
        this.selfmenuInfo = selfmenuInfo;
    }
}
