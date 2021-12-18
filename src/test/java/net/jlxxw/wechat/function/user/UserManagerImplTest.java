package net.jlxxw.wechat.function.user;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.dto.user.SubscriptionUser;
import net.jlxxw.wechat.enums.LanguageEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhanxiumei
 */

public class UserManagerImplTest extends BaseTest {

    @Autowired
    public UserManagerImpl userManagerImpl;


    @Test
    public void findAllTest(){
        Set<String> all = userManagerImpl.findAll();
        Assert.assertFalse("查询到的用户不应为空",CollectionUtils.isEmpty(all));
    }

    @Test
    public void findUserInfoTest(){
        List<String> openIdList =new ArrayList<>();
        openIdList.add(openId);
        List<SubscriptionUser> userInfo = userManagerImpl.findUserInfo(openIdList, LanguageEnum.ZH_CN);
        Assert.assertFalse("查询到的用户信息不应为空", CollectionUtils.isEmpty(userInfo));

    }

    @Test
    public void  getUserInfoTest(){
        SubscriptionUser userInfo = userManagerImpl.getUserInfo(openId, LanguageEnum.ZH_CN);
        Assert.assertTrue("查询到的用户信息不应为空", Objects.nonNull(userInfo));
    }



}
