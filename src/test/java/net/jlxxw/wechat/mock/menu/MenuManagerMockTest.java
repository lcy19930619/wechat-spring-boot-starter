package net.jlxxw.wechat.mock.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.text.MessageFormat;
import java.util.List;
import net.jlxxw.wechat.base.MockBaseTest;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.menu.MenuDTO;
import net.jlxxw.wechat.function.menu.MenuManager;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.WeChatResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;

/**
 * @author chunyang.leng
 * @date 2022-08-25 3:16 PM
 */
public class MenuManagerMockTest extends MockBaseTest {
    @InjectMocks
    private MenuManager menuManager;
    @Mock
    private RestTemplate restTemplate;
    @Spy
    private WeChatTokenManager weChatTokenManager;

    @Test
    public void deleteMenuMockTest() {

        String url = MessageFormat.format(UrlConstant.DELETE_MENU_URL, weChatTokenManager.getTokenFromLocal());
        String webPageAuthTokenJson = JSON.toJSONString(new WeChatResponse()).replaceAll("\\s", "");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(webPageAuthTokenJson, HttpStatus.OK);
        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);
        WeChatResponse weChatResponse = menuManager.deleteMenu();
        Assert.assertTrue("测试不应该失败",weChatResponse.isSuccessful());

    }

    @Test
    public void createMenu1(){
        String mockInputJson = getJson("./mock/data/json/menu/CreateMenuInfo-1.json").replaceAll("\\s", "");
        JSONObject jsonObject = JSONObject.parseObject(mockInputJson);
        List<MenuDTO> button = jsonObject.getJSONArray("button").toJavaList(MenuDTO.class);

        JSONObject data = new JSONObject();
        data.put("button", button);
        String json = JSON.toJSONString(data);
        String url = MessageFormat.format(UrlConstant.CREATE_MENU_URL, weChatTokenManager.getTokenFromLocal());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> mockResponse = buildMockResponseEntity(JSON.toJSONString(new WeChatResponse()));
        when(restTemplate.postForEntity(url, request, String.class)).thenReturn(mockResponse);

        WeChatResponse menu = menuManager.createMenu(button);
        Assert.assertTrue("测试不应该失败",menu.isSuccessful());
    }

    @Test
    public void createMenu2(){
        String mockInputJson = getJson("./mock/data/json/menu/CreateMenuInfo-2.json").replaceAll("\\s", "");
        JSONObject jsonObject = JSONObject.parseObject(mockInputJson);
        List<MenuDTO> button = jsonObject.getJSONArray("button").toJavaList(MenuDTO.class);



        JSONObject data = new JSONObject();
        data.put("button", button);
        String json = JSON.toJSONString(data);
        String url = MessageFormat.format(UrlConstant.CREATE_MENU_URL, weChatTokenManager.getTokenFromLocal());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> mockResponse = buildMockResponseEntity(JSON.toJSONString(new WeChatResponse()));
        when(restTemplate.postForEntity(url, request, String.class)).thenReturn(mockResponse);

        WeChatResponse menu = menuManager.createMenu(button);
        Assert.assertTrue("测试不应该失败",menu.isSuccessful());
    }

}
