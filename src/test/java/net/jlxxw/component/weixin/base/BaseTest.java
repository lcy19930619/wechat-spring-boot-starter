package net.jlxxw.component.weixin.base;

import net.jlxxw.component.weixin.TestApplication;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author chunyang.leng
 * @date 2021-06-09 1:21 下午
 */
@SpringBootTest(classes = TestApplication.class )
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {
    /**
     * 测试用的openId
     */
    protected String openId = "";
    /**
     * 测试用的模版Id
     */
    protected String templateId = "";

    /**
     * 测试用的token
     */
    protected String token = "";

    @Autowired
    private WeiXinTokenManager weiXinTokenManager;
    @Before
    public void setOpenId() {
        Assert.assertTrue("测试openId不能为空", StringUtils.isBlank(openId));
        Assert.assertTrue("测试模版Id不能为空", StringUtils.isBlank(templateId));
        token = getToken();
        Assert.assertTrue("测试token不能为空", StringUtils.isBlank(token));
    }

    protected String getToken(){
        return weiXinTokenManager.getTokenFromLocal();
    }
}
