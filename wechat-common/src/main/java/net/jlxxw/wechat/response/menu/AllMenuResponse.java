package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 全部菜单
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Getting_Custom_Menu_Configurations.html">文档地址</a>
 * @author chunyang.leng
 * @date 2021-12-20 7:13 下午
 */
public class AllMenuResponse {

    /**
     * 普通菜单信息
     */
    @JsonProperty("menu")
    private NomalMenu menu;

    /**
     * 个性化菜单信息
     */
    @JSONField(name = "conditionalmenu")
    private List<ConditionalMenu> conditionalMenu;

    public NomalMenu getMenu() {
        return menu;
    }

    public void setMenu(NomalMenu menu) {
        this.menu = menu;
    }

    public List<ConditionalMenu> getConditionalMenu() {
        return conditionalMenu;
    }

    public void setConditionalMenu(List<ConditionalMenu> conditionalMenu) {
        this.conditionalMenu = conditionalMenu;
    }
}
