package net.jlxxw.wechat.function.auth;

import net.jlxxw.wechat.dto.auth.AuthScope;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.UnsupportedEncodingException;

/**
 * @author chunyang.leng
 * @date 2022-08-25 1:19 PM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class WebPageAuthorizationManagerTest {
    @Autowired
    private WebPageAuthorizationManager webPageAuthorizationManager;

    @Test
    public void getAuthorizeUrlTest() {
        try {
            String url = webPageAuthorizationManager.getAuthorizeUrl("http://www.jlxxw.net", AuthScope.BASE, null);
            Assertions.assertNotNull(url);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAuthorizeUrlExceptionTest() {
        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            webPageAuthorizationManager.getAuthorizeUrl(null, null, null);
        });
    }

    @Test
    public void getAuthorizeAccessTokenExceptionTest() {
        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            webPageAuthorizationManager.getAuthorizeAccessToken(null);
        });

    }


    @Test
    public void refreshTokenExceptionTest() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            webPageAuthorizationManager.refreshToken(null);
        });
    }


    @Test
    public void getUserInfoExceptionTest() {
        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            webPageAuthorizationManager.getUserInfo(null, null, null);

        });
    }


    @Test
    public void checkAccessTokenExceptionTest() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            webPageAuthorizationManager.checkAccessToken(null, null);
        });
    }
}
