package net.jlxxw.component.weixin.function.user;

import net.jlxxw.component.weixin.base.BaseTest;
import net.jlxxw.component.weixin.dto.user.SubscriptionUser;
import net.jlxxw.component.weixin.enums.LanguageEnum;
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
        Assert.assertTrue("查询到的用户不应为空",CollectionUtils.isEmpty(all));
    }

    @Test
    public void findUserInfoTest(){
        List<String> openIdList =new ArrayList<>();
        openIdList.add(openId);
        List<SubscriptionUser> userInfo = userManagerImpl.findUserInfo(openIdList, LanguageEnum.ZH_CN);
        Assert.assertTrue("查询到的用户信息不应为空",CollectionUtils.isEmpty(userInfo));

    }

    @Test
    public void  getUserInfoTest(){
        SubscriptionUser userInfo = userManagerImpl.getUserInfo(openId, LanguageEnum.ZH_CN);
        Assert.assertTrue("查询到的用户信息不应为空", Objects.isNull(userInfo));
    }



}
