package net.jlxxw.wechat.dto.menu;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import net.jlxxw.wechat.aop.check.group.Delete;
import net.jlxxw.wechat.aop.check.group.Inster;
import net.jlxxw.wechat.aop.check.group.Select;

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
    @NotNull(groups = Inster.class,message = "菜单匹配规则不能为空")
    @JSONField(name = "matchrule")
    @JsonProperty("matchrule")
    private MatchRule matchRule;

    /**
     * 菜单id
     */
    @NotBlank(groups = Delete.class,message = "菜单id不能为空")
    @JSONField(name = "menuid")
    @JsonProperty("menuid")
    private String menuId;


    /**
     * 可以是粉丝的OpenID，也可以是粉丝的微信号。
     */
    @NotBlank(groups = Select.class,message = "用户id不能为空")
    @JSONField(name = "user_id")
    @JsonProperty("user_id")
    private String userId;

    public MatchRule getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(MatchRule matchRule) {
        this.matchRule = matchRule;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
