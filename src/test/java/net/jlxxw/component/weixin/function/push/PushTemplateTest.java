package net.jlxxw.component.weixin.function.push;

import com.alibaba.fastjson.JSON;
import net.jlxxw.component.weixin.base.BaseTest;
import net.jlxxw.component.weixin.dto.template.WxTemplate;
import net.jlxxw.component.weixin.enums.ColorEnums;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import net.jlxxw.component.weixin.response.WeiXinResponse;
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
public class PushTemplateTest extends BaseTest {

    /**
     * 需要替换真实数据
     */
    private static final String openId = "xxxxx";
    /**
     * 需要替换真实数据
     */
    private static final String templateId = "xxxxx";

    @Autowired
    private PushTemplate pushTemplate;
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;

    /**
     * 多线程共享token
     */
    private volatile Supplier<String>  volatileToken;

    @Test
    public void pushTemplateTest(){
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = "xxxxxx";

        WxTemplate wxTemplate = new WxTemplate();
        wxTemplate
                .buildToUser(openId)
                .buildUrl(url)
                .buildTemplateCode(templateId)
                .buildFirstData("first DATA的具体值", ColorEnums.BLUE)
                .buildKeyWord1Data("keyword1 DATA的具体值",null)
                .buildOtherData("abc","abc DATA的具体值",ColorEnums.ORANGE);
        WeiXinResponse weiXinResponse = pushTemplate.pushTemplate(wxTemplate, token);

        Assert.assertEquals("微信返回状态错误，当前为：" + JSON.toJSONString(weiXinResponse),0L, (int) weiXinResponse.getErrcode());
    }

    /**
     * 批量推送
     */
    @Test
    public void pushTemplateListTest(){
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = "xxxxxx";

        WxTemplate wxTemplate = new WxTemplate();
        wxTemplate
                .buildToUser(openId)
                .buildUrl(url)
                .buildTemplateCode(templateId)
                .buildFirstData("first DATA的具体值", ColorEnums.BLUE)
                .buildKeyWord1Data("keyword1 DATA的具体值",null)
                .buildOtherData("abc","abc DATA的具体值",ColorEnums.ORANGE);
        List<WxTemplate> templateList =new ArrayList<>();
        templateList.add(wxTemplate);
        volatileToken.get();
        List<WeiXinResponse>  weiXinResponse = pushTemplate.pushTemplate(templateList, token,volatileToken);
        Assert.assertTrue(!CollectionUtils.isEmpty(weiXinResponse));
    }


}
