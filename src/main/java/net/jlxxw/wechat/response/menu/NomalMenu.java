package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author chunyang.leng
 * @date 2021-12-20 7:14 下午
 */
public class NomalMenu {

    /**
     * 菜单ID
     */
    @JSONField(name = "menuid")
    private Integer menuid;

    /**
     * 普通菜单列表
     */
    @JSONField(name = "button")
    private List<MatchPersonalizedMenuButton> button;

    public List<MatchPersonalizedMenuButton> getButton() {
        return button;
    }

    public void setButton(List<MatchPersonalizedMenuButton> button) {
        this.button = button;
    }

    public Integer getMenuid() {
        return menuid;
    }

    public void setMenuid(Integer menuid) {
        this.menuid = menuid;
    }
}
