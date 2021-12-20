package net.jlxxw.wechat.dto.menu;

/**
 * 个性化菜单
 * @author chunyang.leng
 * @date 2021-12-20 3:48 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Personalized_menu_interface.html">文档地址</a>
 */
public class PersonalizedMenuDTO extends MenuDTO{

    /**
     * 菜单匹配规则
     */
    private MatchRule matchrule;

    public MatchRule getMatchrule() {
        return matchrule;
    }

    public void setMatchrule(MatchRule matchrule) {
        this.matchrule = matchrule;
    }
}
