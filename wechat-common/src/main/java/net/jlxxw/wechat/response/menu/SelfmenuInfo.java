package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 菜单返回数据
 *
 * @author chunyang.leng
 * @date 2021-12-20 11:01 上午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Querying_Custom_Menus.html">文档地址</a>
 */
public class SelfmenuInfo {

    /**
     * 菜单按钮
     */
    @JSONField(name = "button")
    private List<Button> button;

    public List<Button> getButton() {
        return button;
    }

    public void setButton(List<Button> button) {
        this.button = button;
    }
}
