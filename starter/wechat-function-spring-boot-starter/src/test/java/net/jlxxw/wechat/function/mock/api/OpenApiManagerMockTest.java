package net.jlxxw.wechat.function.mock.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.function.api.OpenApiManager;
import net.jlxxw.wechat.function.mock.MockBaseTest;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.api.ApiRequestRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

import static org.mockito.Mockito.when;

/**
 * @author chunyang.leng
 * @date 2022-08-25 3:00 PM
 */
public class OpenApiManagerMockTest extends MockBaseTest {
    @InjectMocks
    private OpenApiManager openApiManager;
    @Spy
    private WeChatProperties weChatProperties;
    @Spy
    private WeChatTokenRepository weChatTokenRepository;
    @Mock
    private RestTemplate restTemplate;

    @Test
    public void cleanMockTest(){
        String appId = weChatProperties.getAppId();
        String token = weChatTokenRepository.get();

        String url = MessageFormat.format(UrlConstant.OPEN_API_CLEAN, token);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", appId);
        String json = jsonObject.toJSONString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        String mockResponse = JSON.toJSONString(new WeChatResponse());
        ResponseEntity<String> responseEntity = buildMockResponseEntity(mockResponse);
        when(restTemplate.postForEntity(url, request, String.class)).thenReturn(responseEntity);

        WeChatResponse clean = openApiManager.clean();
        Assertions.assertTrue(clean.isSuccessful(), "测试不应该失败");

    }

    @Test
    public void selectRidTest(){
        String rid = "61725984-6126f6f9-040f19c4";
        String token = weChatTokenRepository.get();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rid", rid);

        String url = MessageFormat.format(UrlConstant.OPEN_API_SELECT_RID, token);
        String json = jsonObject.toJSONString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        String mockResponse = getJson("./mock/data/json/api/RidResponse.json").replaceAll("\\s", "");
        ResponseEntity<String> responseEntity = buildMockResponseEntity(mockResponse);
        when(restTemplate.postForEntity(url, request, String.class)).thenReturn(responseEntity);


        ApiRequestRecord record = openApiManager.selectRid(rid);
        Assertions.assertNotNull(record, "返回值不应为null");

    }
}
