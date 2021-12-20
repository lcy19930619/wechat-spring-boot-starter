package net.jlxxw.wechat.function.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.menu.MenuDTO;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.WeiXinResponse;
import net.jlxxw.wechat.response.menu.MenuResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import net.jlxxw.wechat.util.WebClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.List;

/**
 * 公众号菜单管理 todo 未完成
 *
 * @author chunyang.leng
 * @date 2021-12-13 2:04 下午
 */
@DependsOn({"weChatProperties", "weChatTokenManager", "webClientUtils"})
@Component
public class AsyncMenuManager {

    private static final Logger logger = LoggerFactory.getLogger(AsyncMenuManager.class);
    @Autowired
    private WeChatTokenManager weChatTokenManager;
    @Autowired
    private WebClientUtils webClientUtils;

    /**
     * 创建菜单
     *
     * @param list 菜单列表
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Creating_Custom-Defined_Menu.html">文档地址</a>
     */
    public Mono<WeiXinResponse> createMenu(List<MenuDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            LoggerUtils.debug(logger, "创建公众号菜单，输入列表信息为空");
            return Mono.error(new NullPointerException("创建公众号菜单，输入列表信息为空"));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("button", list);
        String json = JSON.toJSONString(jsonObject);
        LoggerUtils.debug(logger, "创建公众号菜单，请求参数:{}", json);
        String url = MessageFormat.format(UrlConstant.CREATE_MENU_URL, weChatTokenManager.getTokenFromLocal());
        return webClientUtils.sendPostJSON(url, json, WeiXinResponse.class);
    }

    /**
     * 删除菜单
     *
     * @return 正确应为 {"errcode":0,"errmsg":"ok"}
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Deleting_Custom-Defined_Menu.html">文档地址</a>
     */
    public Mono<WeiXinResponse> deleteMenu() {
        String url = MessageFormat.format(UrlConstant.DELETE_MENU_URL, weChatTokenManager.getTokenFromLocal());
        return webClientUtils.sendGet(url, WeiXinResponse.class);
    }

    /**
     * 获取全部菜单
     *
     * @return
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Querying_Custom_Menus.html">文档地址</a>
     */
    public Mono<MenuResponse> getMenu() {
        String url = MessageFormat.format(UrlConstant.GET_MENU_URL, weChatTokenManager.getTokenFromLocal());
        return webClientUtils.sendGet(url, MenuResponse.class);
    }
}
