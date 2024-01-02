package net.jlxxw.wechat.function.mock.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.menu.MenuDTO;
import net.jlxxw.wechat.function.menu.MenuManager;
import net.jlxxw.wechat.function.mock.MockBaseTest;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;

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
    private WeChatTokenRepository weChatTokenRepository;

    @Test
    public void deleteMenuMockTest() {

        String url = MessageFormat.format(UrlConstant.DELETE_MENU_URL, weChatTokenRepository.get());
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
        String url = MessageFormat.format(UrlConstant.CREATE_MENU_URL, weChatTokenRepository.get());
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
        String url = MessageFormat.format(UrlConstant.CREATE_MENU_URL, weChatTokenRepository.get());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> mockResponse = buildMockResponseEntity(JSON.toJSONString(new WeChatResponse()));
        when(restTemplate.postForEntity(url, request, String.class)).thenReturn(mockResponse);

        WeChatResponse menu = menuManager.createMenu(button);
        Assert.assertTrue("测试不应该失败",menu.isSuccessful());
    }

}
