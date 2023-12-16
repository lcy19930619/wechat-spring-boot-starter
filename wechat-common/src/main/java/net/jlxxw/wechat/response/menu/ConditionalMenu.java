package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 个性化菜单
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Getting_Custom_Menu_Configurations.html">文档地址</a>
 * @author chunyang.leng
 * @date 2021-12-20 7:07 下午
 */
public class ConditionalMenu {

    /**
     * 个性化菜单列表
     */
    @JSONField(name = "button")
    private List<MatchPersonalizedMenuButton> button;

    /**
     * 个性化菜单匹配规则
     */
    @JSONField(name = "matchrule")
    private MatchRule matchrule;

    /**
     * 菜单ID
     */
    @JSONField(name = "menuid")
    private Integer menuid;

    public List<MatchPersonalizedMenuButton> getButton() {
        return button;
    }

    public void setButton(List<MatchPersonalizedMenuButton> button) {
        this.button = button;
    }

    public MatchRule getMatchrule() {
        return matchrule;
    }

    public void setMatchrule(MatchRule matchrule) {
        this.matchrule = matchrule;
    }

    public Integer getMenuid() {
        return menuid;
    }

    public void setMenuid(Integer menuid) {
        this.menuid = menuid;
    }
}
