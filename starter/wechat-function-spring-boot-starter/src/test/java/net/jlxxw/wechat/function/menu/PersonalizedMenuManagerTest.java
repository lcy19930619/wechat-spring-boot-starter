package net.jlxxw.wechat.function.menu;

import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

    @Test
    public void createMenuMockExceptionTest(){

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            personalizedMenuManager.createMenu(null);
        });
    }


    @Test
    public void deleteMenuMockExceptionTest() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            personalizedMenuManager.deleteMenu(null);
        });
    }

    @Test
    public void tryMatchMockExceptionTest() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            personalizedMenuManager.tryMatch(null);
        });
    }
}
