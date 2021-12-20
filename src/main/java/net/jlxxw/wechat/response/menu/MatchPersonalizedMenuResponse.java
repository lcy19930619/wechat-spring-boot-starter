package net.jlxxw.wechat.response.menu;

import net.jlxxw.wechat.response.WeChatResponse;

import java.util.List;

/**
 * 个性化菜单匹配结果
 * @author chunyang.leng
 * @date 2021-12-20 4:18 下午
 */
public class MatchPersonalizedMenuResponse extends WeChatResponse {
    /**
     * 菜单数据
     */
    private List<MatchPersonalizedMenuButton> button;

    public List<MatchPersonalizedMenuButton> getButton() {
        return button;
    }

    public void setButton(List<MatchPersonalizedMenuButton> button) {
        this.button = button;
    }
}
