package net.jlxxw.component.weixin.function.push;

import net.jlxxw.component.weixin.dto.template.WxTemplate;
import net.jlxxw.component.weixin.enums.ColorEnums;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author chunyang.leng
 * @date 2021-03-08 3:25 下午
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PushTemplateTest {
    @Autowired
    private PushTemplate pushTemplate;

    @Test
    public void pushTemplateTest(){
        String token = "";
        String openId = "xxxxx";
        String url = "xxxxxx";
        String templateId = "xxxxx";

        WxTemplate wxTemplate = new WxTemplate();
        wxTemplate
                .buildToUser(openId)
                .buildUrl(url)
                .buildTemplateCode(templateId)
                .buildFirstData("first DATA的具体值", ColorEnums.BLUE)
                .buildKeyWord1Data("keyword1 DATA的具体值",null)
                .buildOtherData("abc","abc DATA的具体值",ColorEnums.ORANGE);
        pushTemplate.pushTemplate(wxTemplate,token);
    }
}
