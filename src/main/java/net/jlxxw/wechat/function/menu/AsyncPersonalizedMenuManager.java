package net.jlxxw.wechat.function.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.menu.PersonalizedMenuDTO;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.menu.MatchPersonalizedMenuResponse;
import net.jlxxw.wechat.response.menu.PersonalizedMenuResponse;
import net.jlxxw.wechat.util.WebClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

/**
 * 个性化菜单接口
 *
 * @author chunyang.leng
 * @date 2021-12-20 3:35 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Getting_Custom_Menu_Configurations.html">文档地址</a>
 */
@Component
public class AsyncPersonalizedMenuManager {
    @Autowired
    private WebClientUtils webClientUtils;
    @Autowired
    private WeChatTokenManager weChatTokenManager;

    /**
     * 创建个性化菜单
     *
     * @param personalizedMenuDTO 个性化菜单数据
     * @return 正确时的返回JSON数据包{"menuid":"208379533"}
     * 错误时的返回码请见接口返回码说明。
     */
    public Mono<PersonalizedMenuResponse> createMenu(PersonalizedMenuDTO personalizedMenuDTO) {
        String url = MessageFormat.format(UrlConstant.CREATE_PERSONALIZED_MENU, weChatTokenManager.getTokenFromLocal());
        return webClientUtils.sendPostJSON(url, JSON.toJSONString(personalizedMenuDTO), PersonalizedMenuResponse.class);
    }

    /**
     * 删除菜单数据
     *
     * @param menuId 要删除的个性化菜单条件id
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Personalized_menu_interface.html">文档地址</a>
     * @return 正确时的返回JSON数据包 {"errcode":0,"errmsg":"ok"}，错误时的返回码请见接口返回码说明。
     */
    public Mono<WeChatResponse> deleteMenu(String menuId) {
        String url = MessageFormat.format(UrlConstant.DELETE_PERSONALIZED_MENU, weChatTokenManager.getTokenFromLocal());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menuid", menuId);
        return webClientUtils.sendPostJSON(url,jsonObject.toJSONString(), WeChatResponse.class);
    }


    /**
     * 尝试匹配用户信息
     * @param uid 可以是粉丝的OpenID，也可以是粉丝的微信号。
     * @return 菜单信息列表
     */
    public Mono<MatchPersonalizedMenuResponse> tryMatch(String uid){
        String url = MessageFormat.format(UrlConstant.TRY_MATCH_PERSONALIZED_MENU, weChatTokenManager.getTokenFromLocal());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", uid);
        return webClientUtils.sendPostJSON(url,jsonObject.toJSONString(), MatchPersonalizedMenuResponse.class);
    }
}
