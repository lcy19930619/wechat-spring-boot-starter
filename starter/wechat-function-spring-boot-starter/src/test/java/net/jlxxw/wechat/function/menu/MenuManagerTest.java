package net.jlxxw.wechat.function.menu;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.menu.MenuResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author chunyang.leng
 * @date 2022-08-25 1:35 PM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class MenuManagerTest {

    @Autowired
    private MenuManager menuManager;

    @Test
    public void getMenuTest(){
        MenuResponse menu = menuManager.getMenu();
        System.out.println(JSON.toJSONString(menu));
        Assertions.assertNotNull(menu, "返回值不应为null");
    }
}
