package net.jlxxw.wechat.function.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.validation.constraints.NotEmpty;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.menu.MenuDTO;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.menu.MenuResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;

/**
 * 公众号菜单管理
 *
 * @author chunyang.leng
 * @date 2021-12-13 2:04 下午
 */
public class MenuManager {

    private static final Logger logger = LoggerFactory.getLogger(MenuManager.class);
    private WeChatTokenManager weChatTokenManager;
    private RestTemplate restTemplate;

    public MenuManager(WeChatTokenManager weChatTokenManager, RestTemplate restTemplate) {
        this.weChatTokenManager = weChatTokenManager;
        this.restTemplate = restTemplate;
    }

    /**
     * 创建菜单
     *
     * @param list 菜单列表
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Creating_Custom-Defined_Menu.html">文档地址</a>
     */
    public WeChatResponse createMenu(
        @NotEmpty(message = "创建菜单输入参数不能为空") List<MenuDTO> list) throws WeChatException, ParamCheckException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("button", list);
        String json = JSON.toJSONString(jsonObject);
        LoggerUtils.debug(logger, "创建公众号菜单，请求参数:{}", json);
        String url = MessageFormat.format(UrlConstant.CREATE_MENU_URL, weChatTokenManager.getTokenFromLocal());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger, "创建公众号菜单，应答结果:{}", body);
        WeChatResponse weChatResponse = JSONObject.parseObject(body, WeChatResponse.class);
        if (StringUtils.isNotBlank(body) && !weChatResponse.isSuccessful()) {
            throw new WeChatException(weChatResponse);
        }
        return weChatResponse;
    }

    /**
     * 删除菜单
     *
     * @return 正确应为 {"errcode":0,"errmsg":"ok"}
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Deleting_Custom-Defined_Menu.html">文档地址</a>
     */
    public WeChatResponse deleteMenu() throws WeChatException {
        String url = MessageFormat.format(UrlConstant.DELETE_MENU_URL, weChatTokenManager.getTokenFromLocal());
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        String body = entity.getBody();
        LoggerUtils.debug(logger, "删除公众号菜单，应答结果:{}", body);
        WeChatResponse response = JSONObject.parseObject(body, WeChatResponse.class);
        if (StringUtils.isNotBlank(body) && !response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }

    /**
     * 获取全部菜单
     *
     * @return 菜单列表
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Querying_Custom_Menus.html">文档地址</a>
     */
    public MenuResponse getMenu() throws WeChatException {
        String url = MessageFormat.format(UrlConstant.GET_MENU_URL, weChatTokenManager.getTokenFromLocal());
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        String body = entity.getBody();
        LoggerUtils.debug(logger, "获取公众号菜单，应答结果:{}", body);
        MenuResponse response = JSONObject.parseObject(body, MenuResponse.class);
        if (StringUtils.isNotBlank(body) && !response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }
}
