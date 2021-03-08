package net.jlxxw.component.weixin.function.push;

/**
 * @author chunyang.leng
 * @date 2021-03-08 3:18 下午
 */

import net.jlxxw.component.weixin.dto.customer.CustomerMessageDTO;
import net.jlxxw.component.weixin.dto.customer.news.ArticlesDTO;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class PushCustomerTest {
    @Autowired
    public PushCustomer pushCustomer;
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;
    @Test
    public void pushCustomerTest(){
        String token = weiXinTokenManager.getTokenFromLocal();
        String openId = "xxx";
        ArticlesDTO articlesDTO = new ArticlesDTO();
        articlesDTO.setPicurl("测试图片url");
        articlesDTO.setTitle("这里一个测试的标题");
        articlesDTO.setUrl("测试跳转链接");
        articlesDTO.setDescription("测试描述信息");
        CustomerMessageDTO dto = CustomerMessageDTO.buildNews(openId,articlesDTO);
        pushCustomer.pushCustomer(dto,token);
    }

}
