package net.jlxxw.wechat.function.user;

import net.jlxxw.wechat.enums.LanguageEnum;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.user.SubscriptionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @author zhanxiumei
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class UserManagerTest  {

    @Value("${wechat.test.openid}")
    private  String openId;

    @Autowired
    public UserManager userManager;


   // @Test
    public void findAllTest() {
        Set<String> all = userManager.findAll();
        Assertions.assertFalse(CollectionUtils.isEmpty(all), "查询到的用户不应为空");
    }


    @Test
    public void getUserInfoTest() {
        SubscriptionResponse userInfo = userManager.getUserInfo(openId, LanguageEnum.ZH_CN);
        Assertions.assertTrue(Objects.nonNull(userInfo), "查询到的用户信息不应为空");
    }


}
