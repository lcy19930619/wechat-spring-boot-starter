package net.jlxxw.wechat.function.user;

import net.jlxxw.wechat.enums.LanguageEnum;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.user.SubscriptionResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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
        Assert.assertFalse("查询到的用户不应为空", CollectionUtils.isEmpty(all));
    }

    @Test
    public void findUserInfoTest() {
        List<String> openIdList = new ArrayList<>();
        openIdList.add(openId);
        List<SubscriptionResponse> userInfo = userManager.findUserInfo(openIdList, LanguageEnum.ZH_CN);
        Assert.assertFalse("查询到的用户信息不应为空", CollectionUtils.isEmpty(userInfo));

    }

    @Test
    public void getUserInfoTest() {
        SubscriptionResponse userInfo = userManager.getUserInfo(openId, LanguageEnum.ZH_CN);
        Assert.assertTrue("查询到的用户信息不应为空", Objects.nonNull(userInfo));
    }


}
