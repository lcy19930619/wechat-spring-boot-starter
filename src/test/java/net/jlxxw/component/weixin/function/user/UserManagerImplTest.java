package net.jlxxw.component.weixin.function.user;

import net.jlxxw.component.weixin.base.BaseTest;
import net.jlxxw.component.weixin.enums.LanguageEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanxiumei
 */

public class UserManagerImplTest extends BaseTest {

    @Autowired
    public UserManagerImpl userManagerImpl;

    /**
     * 需要替换真实数据
     */
    private static final String openId = "xxxxx";
    @Test
    public void findAllTest(){
        userManagerImpl.findAll();
    }

    @Test
    public void findUserInfoTest(){
        List<String> openIdList =new ArrayList<>();
        openIdList.add(openId);
        userManagerImpl.findUserInfo(openIdList,LanguageEnum.ZH_CN);
        Assert.assertTrue(!CollectionUtils.isEmpty(openIdList));
    }

    @Test
    public void  getUserInfoTest(){
        userManagerImpl.getUserInfo(openId,LanguageEnum.ZH_CN);

    }



}
