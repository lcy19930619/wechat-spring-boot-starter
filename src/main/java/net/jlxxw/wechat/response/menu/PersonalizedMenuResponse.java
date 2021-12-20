package net.jlxxw.wechat.response.menu;

import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 个性化菜单返回结果
 * @author chunyang.leng
 * @date 2021-12-20 3:58 下午
 */
public class PersonalizedMenuResponse extends WeChatResponse {

    /**
     * 菜单ID
     */
    private String menuid;

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }
}
