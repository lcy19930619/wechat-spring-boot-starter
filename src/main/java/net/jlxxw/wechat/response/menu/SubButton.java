package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 子按钮数据
 * @author chunyang.leng
 * @date 2021-12-20 11:01 上午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Querying_Custom_Menus.html">文档地址</a>
 */
public class SubButton {

    /**
     * 菜单按钮
     */
    @JSONField(name = "list")
    private List<Button> list;

    public List<Button> getList() {
        return list;
    }

    public void setList(List<Button> list) {
        this.list = list;
    }
}
