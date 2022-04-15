package net.jlxxw.wechat.dto.feign.menu;

import net.jlxxw.wechat.dto.menu.MenuDTO;

import java.util.List;

/**
 * 向微信传递的菜单对象
 * @author chunyang.leng
 * @date 2022-04-15 1:04 PM
 */
public class ButtonDTO {

    /**
     * 菜单列表
     */
    private List<MenuDTO> button;

    public List<MenuDTO> getButton() {
        return button;
    }

    public void setButton(List<MenuDTO> button) {
        this.button = button;
    }
}
