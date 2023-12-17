package net.jlxxw.wechat.function.auth;

import java.io.UnsupportedEncodingException;
import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.dto.auth.AuthScope;
import net.jlxxw.wechat.exception.ParamCheckException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2022-08-25 1:19 PM
 */
public class WebPageAuthorizationManagerTest extends BaseTest {
    @Autowired
    private WebPageAuthorizationManager webPageAuthorizationManager;

    @Test
    public void getAuthorizeUrlTest(){
        try {
            String url = webPageAuthorizationManager.getAuthorizeUrl("http://www.jlxxw.net", AuthScope.BASE, null);
            Assert.assertNotNull(url);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = ParamCheckException.class)
    public void getAuthorizeUrlExceptionTest(){
        try {
             webPageAuthorizationManager.getAuthorizeUrl(null, null, null);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = ParamCheckException.class)
    public void getAuthorizeAccessTokenExceptionTest(){
        webPageAuthorizationManager.getAuthorizeAccessToken(null);
    }


    @Test(expected = ParamCheckException.class)
    public void refreshTokenExceptionTest(){
        webPageAuthorizationManager.refreshToken(null);
    }


    @Test(expected = ParamCheckException.class)
    public void getUserInfoExceptionTest(){
        webPageAuthorizationManager.getUserInfo(null, null, null);
    }



    @Test(expected = ParamCheckException.class)
    public void checkAccessTokenExceptionTest(){
        webPageAuthorizationManager.checkAccessToken(null, null);
    }
}
