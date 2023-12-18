package net.jlxxw.wechat.function.menu;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.exception.ParamCheckException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2022-08-26 1:25 PM
 */
public class PersonalizedMenuManagerTest extends BaseTest {
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
