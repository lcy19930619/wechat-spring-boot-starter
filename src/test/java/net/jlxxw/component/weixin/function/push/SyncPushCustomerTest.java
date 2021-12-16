package net.jlxxw.component.weixin.function.push;

/**
 * @author chunyang.leng
 * @date 2021-03-08 3:18 下午
 */

import com.alibaba.fastjson.JSON;
import net.jlxxw.component.weixin.base.BaseTest;
import net.jlxxw.component.weixin.dto.customer.CustomerMessageDTO;
import net.jlxxw.component.weixin.dto.customer.news.ArticlesDTO;
import net.jlxxw.component.weixin.response.WeiXinResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class SyncPushCustomerTest extends BaseTest {

    @Autowired
    public SyncPushCustomer syncPushCustomer;
    @Test
    public void pushCustomerTest(){
        ArticlesDTO articlesDTO = new ArticlesDTO();
        articlesDTO.setPicurl("测试图片url");
        articlesDTO.setTitle("这里一个测试的标题");
        articlesDTO.setUrl("测试跳转链接");
        articlesDTO.setDescription("测试描述信息");
        CustomerMessageDTO dto = CustomerMessageDTO.buildNews(openId,articlesDTO);
        WeiXinResponse weiXinResponse = syncPushCustomer.pushCustomer(dto, token);

        Assert.assertEquals("微信返回状态错误，当前为：" + JSON.toJSONString(weiXinResponse),0L, (int) weiXinResponse.getErrcode());

    }

    /**
     * 批量推送
     */
    public void pushCustomerListTest(){
        ArticlesDTO articlesDTO = new ArticlesDTO();
        articlesDTO.setPicurl("测试图片url");
        articlesDTO.setTitle("这里一个测试的标题");
        articlesDTO.setUrl("测试跳转链接");
        articlesDTO.setDescription("测试描述信息");
        List<CustomerMessageDTO> dtoList =new ArrayList<>();
        CustomerMessageDTO dto = CustomerMessageDTO.buildNews(openId,articlesDTO);
        dtoList.add(dto);
        List<WeiXinResponse> weiXinResponse = syncPushCustomer.pushCustomer(dtoList, token);
        Assert.assertFalse("测试结果不应为空", CollectionUtils.isEmpty(weiXinResponse));
    }
}
