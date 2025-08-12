package net.jlxxw.wechat.function.push;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.dto.template.WeChatTemplateDTO;
import net.jlxxw.wechat.enums.ColorEnums;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.WeChatResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author chunyang.leng
 * @date 2021-03-08 3:25 下午
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class SyncPushTemplateTest  {


    @Value("${wechat.test.openid}")
    private  String openId;


    @Value("${wechat.test.templateId}")
    private  String templateId;
    @Autowired
    private SyncPushTemplate syncPushTemplate;

    @Test
    public void pushTemplateTest() {
        String url = "xxxxxx";

        WeChatTemplateDTO weChatTemplateDTO = new WeChatTemplateDTO();
        weChatTemplateDTO
                .buildToUser(openId)
                .buildUrl(url)
                .buildTemplateCode(templateId)
                .buildFirstData("first DATA的具体值", ColorEnums.BLUE)
                .buildKeyWord1Data("keyword1 DATA的具体值", null)
                .buildOtherData("abc", "abc DATA的具体值", ColorEnums.ORANGE);
        WeChatResponse weChatResponse = syncPushTemplate.pushTemplate(weChatTemplateDTO);

        Assertions.assertEquals(0L, (int) weChatResponse.getErrcode(), "微信返回状态错误，当前为：" + JSON.toJSONString(weChatResponse));
    }




}
