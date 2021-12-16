package net.jlxxw.component.weixin.function.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.component.weixin.constant.UrlConstant;
import net.jlxxw.component.weixin.dto.menu.MenuDTO;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import net.jlxxw.component.weixin.response.WeiXinResponse;
import net.jlxxw.component.weixin.util.LoggerUtils;
import net.jlxxw.component.weixin.util.WebClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author chunyang.leng
 * @date 2021-12-13 2:04 下午
 */
@DependsOn({"weiXinProperties","weiXinTokenManager","webClientUtils"})
@Component
public class AsyncMenuManager {

    private static final Logger logger = LoggerFactory.getLogger(AsyncMenuManager.class);
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;
    @Autowired
    private WebClientUtils webClientUtils;

    /**
     * 创建菜单
     * @link https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Creating_Custom-Defined_Menu.html
     * @param list 菜单列表
     * @param callBack 回调函数
     */
    public void createMenu(List<MenuDTO> list, Consumer<WeiXinResponse> callBack) {
        if(CollectionUtils.isEmpty(list)){
            LoggerUtils.debug(logger,"创建公众号菜单，输入列表信息为空");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("button",list);
        String json = JSON.toJSONString(jsonObject);
        LoggerUtils.debug(logger,"创建公众号菜单，请求参数:{}",json);
        String url = MessageFormat.format(UrlConstant.CREATE_MENU_URL,weiXinTokenManager.getTokenFromLocal());
        Mono<WeiXinResponse> mono = webClientUtils.sendPostJSON(url, json, WeiXinResponse.class);
        mono.subscribe(callBack);
    }
}
