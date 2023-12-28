package net.jlxxw.wechat.function.menu;

import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author chunyang.leng
 * @date 2022-08-26 1:25 PM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class PersonalizedMenuManagerTest {
    @Autowired
    private PersonalizedMenuManager personalizedMenuManager;

    @Test(expected = ParamCheckException.class)
    public void createMenuMockExceptionTest(){
        personalizedMenuManager.createMenu(null);
    }


    @Test(expected = ParamCheckException.class)
    public void deleteMenuMockExceptionTest() {
        personalizedMenuManager.deleteMenu(null);
    }

    @Test(expected = ParamCheckException.class)
    public void tryMatchMockExceptionTest() {
        personalizedMenuManager.tryMatch(null);
    }
}
