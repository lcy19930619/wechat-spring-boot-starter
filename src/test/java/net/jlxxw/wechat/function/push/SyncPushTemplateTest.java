package net.jlxxw.wechat.function.push;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.dto.template.WxTemplate;
import net.jlxxw.wechat.enums.ColorEnums;
import net.jlxxw.wechat.function.token.WeiXinTokenManager;
import net.jlxxw.wechat.response.WeiXinResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author chunyang.leng
 * @date 2021-03-08 3:25 下午
 */
public class SyncPushTemplateTest extends BaseTest {


    @Autowired
    private SyncPushTemplate syncPushTemplate;
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;

    /**
     * 多线程共享token
     */
    private final Supplier<String> volatileToken = this::getToken;

    @Test
    public void pushTemplateTest() {
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = "xxxxxx";

        WxTemplate wxTemplate = new WxTemplate();
        wxTemplate
                .buildToUser(openId)
                .buildUrl(url)
                .buildTemplateCode(templateId)
                .buildFirstData("first DATA的具体值", ColorEnums.BLUE)
                .buildKeyWord1Data("keyword1 DATA的具体值", null)
                .buildOtherData("abc", "abc DATA的具体值", ColorEnums.ORANGE);
        WeiXinResponse weiXinResponse = syncPushTemplate.pushTemplate(wxTemplate);

        Assert.assertEquals("微信返回状态错误，当前为：" + JSON.toJSONString(weiXinResponse), 0L, (int) weiXinResponse.getErrcode());
    }

    /**
     * 批量推送
     */
    @Test
    public void pushTemplateListTest() {
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = "xxxxxx";

        WxTemplate wxTemplate = new WxTemplate();
        wxTemplate
                .buildToUser(openId)
                .buildUrl(url)
                .buildTemplateCode(templateId)
                .buildFirstData("first DATA的具体值", ColorEnums.BLUE)
                .buildKeyWord1Data("keyword1 DATA的具体值", null)
                .buildOtherData("abc", "abc DATA的具体值", ColorEnums.ORANGE);
        List<WxTemplate> templateList = new ArrayList<>();
        templateList.add(wxTemplate);
        List<WeiXinResponse> weiXinResponse = syncPushTemplate.pushTemplate(templateList);
        Assert.assertFalse("模版推送结果不应为空", CollectionUtils.isEmpty(weiXinResponse));
    }


}
