package net.jlxxw.wechat.function.push;

import java.util.ArrayList;
import java.util.List;
import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.dto.customer.ArticlesDTO;
import net.jlxxw.wechat.dto.customer.CustomerMessageDTO;
import net.jlxxw.wechat.response.WeChatResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
/**
 * 单元测试
 * @author chunyang.leng
 * @date 2021-03-08 3:18 下午
 */
public class SyncPushCustomerTest extends BaseTest {

    @Autowired
    public SyncPushCustomer syncPushCustomer;

    @Test
    public void pushCustomerTest() {
        ArticlesDTO articlesDTO = new ArticlesDTO();
        articlesDTO.setPicUrl("测试图片url");
        articlesDTO.setTitle("这里一个测试的标题");
        articlesDTO.setUrl("测试跳转链接");
        articlesDTO.setDescription("测试描述信息");
        CustomerMessageDTO dto = CustomerMessageDTO.buildNews(openId, articlesDTO);
        WeChatResponse weChatResponse = syncPushCustomer.pushCustomer(dto);
        Assert.assertTrue("微信返回结果应该为成功",weChatResponse.isSuccessful());

    }

    /**
     * 批量推送
     */
    @Test
    public void pushCustomerListTest() {
        ArticlesDTO articlesDTO = new ArticlesDTO();
        articlesDTO.setPicUrl("测试图片url");
        articlesDTO.setTitle("这里一个测试的标题");
        articlesDTO.setUrl("测试跳转链接");
        articlesDTO.setDescription("测试描述信息");
        List<CustomerMessageDTO> dtoList = new ArrayList<>();
        CustomerMessageDTO dto = CustomerMessageDTO.buildNews(openId, articlesDTO);
        dtoList.add(dto);
        List<WeChatResponse> weChatResponse = syncPushCustomer.pushCustomer(dtoList);
        Assert.assertFalse("测试结果不应为空", CollectionUtils.isEmpty(weChatResponse));
    }


    @Test
    public void pushCustomerTextTest() {
        String text = "这是一条测试用的信息\n" +
            "<a href='https://www.jlxxw.net'>点我查看主页</a>";
        CustomerMessageDTO dto = CustomerMessageDTO.buildText(openId, text);
        WeChatResponse weChatResponse = syncPushCustomer.pushCustomer(dto);
        Assert.assertTrue("微信返回结果应该为成功",weChatResponse.isSuccessful());

    }
}
