package net.jlxxw.wechat.function.mock.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.menu.PersonalizedMenuDTO;
import net.jlxxw.wechat.function.menu.PersonalizedMenuManager;
import net.jlxxw.wechat.function.mock.MockBaseTest;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.menu.PersonalizedMenuResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

import static org.mockito.Mockito.when;

/**
 * @author chunyang.leng
 * @date 2022-08-26 1:25 PM
 */
public class PersonalizedMenuManagerMockTest extends MockBaseTest {
    @InjectMocks
    private PersonalizedMenuManager personalizedMenuManager;
    @Mock
    private RestTemplate restTemplate;
    @Spy
    private WeChatTokenRepository weChatTokenRepository;

    @Test
    public void createMenuMockTest() {

        String inPutJson = getJson("./mock/data/json/menu/PersonalizedMenuDTO.json");
        PersonalizedMenuDTO inputParam = JSONObject.parseObject(inPutJson, PersonalizedMenuDTO.class);

        String url = MessageFormat.format(UrlConstant.CREATE_PERSONALIZED_MENU, weChatTokenRepository.get());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(inputParam), headers);
        when(restTemplate.postForEntity(url, request, String.class)).thenReturn(buildMockResponseEntity("{\"menuid\":\"208379533\"}"));

        PersonalizedMenuResponse menu = personalizedMenuManager.createMenu(inputParam);
        Assert.assertTrue("测试不应该失败", menu.isSuccessful());

    }

    @Test
    public void deleteMenuMockTest() {
        String menuId = "xxxxxx";
        String url = MessageFormat.format(UrlConstant.DELETE_PERSONALIZED_MENU, weChatTokenRepository.get());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menuid", menuId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toJSONString(), headers);

        when(restTemplate.postForEntity(url, request, String.class)).thenReturn(buildMockResponseEntity(JSON.toJSONString(new WeChatResponse())));
        WeChatResponse menu = personalizedMenuManager.deleteMenu(menuId);
        Assert.assertTrue("测试不应该失败", menu.isSuccessful());
    }

    @Test
    public void tryMatchMockTest() {
        String uid = "xxxxx";
        String url = MessageFormat.format(UrlConstant.TRY_MATCH_PERSONALIZED_MENU, weChatTokenRepository.get());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", uid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toJSONString(), headers);
        String json = getJson("./mock/data/json/menu/MatchPersonalizedMenuResponse.json");

        when(restTemplate.postForEntity(url, request, String.class)).thenReturn(buildMockResponseEntity(json));
        WeChatResponse menu = personalizedMenuManager.tryMatch(uid);
        Assert.assertTrue("测试不应该失败", menu.isSuccessful());
    }

}
