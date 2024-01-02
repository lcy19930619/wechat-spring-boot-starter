package net.jlxxw.wechat.function.mock.auth;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.enums.LanguageEnum;
import net.jlxxw.wechat.function.auth.WebPageAuthorizationManager;
import net.jlxxw.wechat.function.mock.MockBaseTest;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.auth.AuthAccessTokenResponse;
import net.jlxxw.wechat.response.auth.AuthUserInfoResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

import static org.mockito.Mockito.when;

/**
 * @author chunyang.leng
 * @date 2022-08-25 2:08 PM
 */
public class WebPageAuthorizationManagerMockTest extends MockBaseTest {
    @InjectMocks
    private WebPageAuthorizationManager webPageAuthorizationManager;
    @Mock
    private RestTemplate restTemplate;
    @Spy
    private WeChatProperties weChatProperties;

    @Test
    public void getAuthorizeAccessTokenMockTest() {
        String code = "test_code";
        String url = MessageFormat.format(UrlConstant.OAUTH2_ACCESS_TOKEN_URL, weChatProperties.getAppId(), weChatProperties.getSecret(), code);

        String webPageAuthTokenJson = getJson("mock/data/json/auth/WebPageAuthToken.json").replaceAll("\\s", "");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(webPageAuthTokenJson, HttpStatus.OK);
        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        AuthAccessTokenResponse token = webPageAuthorizationManager.getAuthorizeAccessToken(code);
        Assert.assertTrue("测试不应该失败", token.isSuccessful());
    }

    @Test
    public void refreshTokenMockTest() {
        String token = "xxx";
        String url = MessageFormat.format(UrlConstant.REFRESH_ACCESS_TOKEN_URL, weChatProperties.getAppId(), token);
        String webPageAuthTokenJson = getJson("mock/data/json/auth/WebPageAuthToken.json").replaceAll("\\s", "");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(webPageAuthTokenJson, HttpStatus.OK);
        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);
        AuthAccessTokenResponse response = webPageAuthorizationManager.refreshToken(token);
        Assert.assertTrue("测试不应该失败", response.isSuccessful());
    }

    @Test
    public void getUserInfoTest() {
        String code = "test_code";
        String url = MessageFormat.format(UrlConstant.OAUTH2_ACCESS_TOKEN_URL, weChatProperties.getAppId(), weChatProperties.getSecret(), code);
        String webPageAuthTokenJson = getJson("mock/data/json/auth/WebPageAuthToken.json").replaceAll("\\s", "");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(webPageAuthTokenJson, HttpStatus.OK);
        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);
        AuthAccessTokenResponse tokenResponse = webPageAuthorizationManager.getAuthorizeAccessToken(code);
        Assert.assertTrue("token测试不应该失败", tokenResponse.isSuccessful());
        String token = tokenResponse.getAccessToken();

        String openId = "xxxxxxx";

        String getUserInfoUrl = MessageFormat.format(UrlConstant.GET_USER_INFO_BY_OAUTH2_ACCESS_TOKEN_URL, token, openId, LanguageEnum.ZH_CN.getCode());

        String userInfoResponseJson = getJson("mock/data/json/auth/UserInfo.json").replaceAll("\\s", "");
        ResponseEntity<String> userInfoResponseEntity = new ResponseEntity<>(userInfoResponseJson, HttpStatus.OK);
        when(restTemplate.getForEntity(getUserInfoUrl, String.class)).thenReturn(userInfoResponseEntity);

        AuthUserInfoResponse userInfoResponse = webPageAuthorizationManager.getUserInfo(openId, token, LanguageEnum.ZH_CN);
        Assert.assertTrue("getUserInfo测试不应该失败", userInfoResponse.isSuccessful());

    }
    @Test
    public void checkAccessTokenTest() {
        String code = "xxxxxxx";
        String openId = "xxxxxxx";

        String url = MessageFormat.format(UrlConstant.OAUTH2_ACCESS_TOKEN_URL, weChatProperties.getAppId(), weChatProperties.getSecret(), code);
        String webPageAuthTokenJson = getJson("mock/data/json/auth/WebPageAuthToken.json").replaceAll("\\s", "");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(webPageAuthTokenJson, HttpStatus.OK);
        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);
        AuthAccessTokenResponse tokenResponse = webPageAuthorizationManager.getAuthorizeAccessToken(code);
        Assert.assertTrue("token测试不应该失败", tokenResponse.isSuccessful());
        String token = tokenResponse.getAccessToken();


        String checkAccessTokenUrl = MessageFormat.format(UrlConstant.CHECK_OAUTH2_ACCESS_TOKEN_URL, token, openId);
        WeChatResponse weChatResponse = new WeChatResponse();
        ResponseEntity<String> checkAccessTokenEntity = new ResponseEntity<>(JSON.toJSONString(weChatResponse), HttpStatus.OK);
        when(restTemplate.getForEntity(checkAccessTokenUrl, String.class)).thenReturn(checkAccessTokenEntity);

        WeChatResponse testResponse = webPageAuthorizationManager.checkAccessToken(openId, token);
        Assert.assertTrue("check token测试不应该失败", testResponse.isSuccessful());

    }

}
