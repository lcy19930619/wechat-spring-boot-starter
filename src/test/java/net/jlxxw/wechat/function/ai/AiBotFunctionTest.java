package net.jlxxw.wechat.function.ai;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.enums.AiBotEnvEnum;
import net.jlxxw.wechat.response.ai.ChatResponse;
import net.jlxxw.wechat.response.ai.WeChatAiBotSignatureResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2023-04-11 16:27
 */
public class AiBotFunctionTest extends BaseTest {
    @Autowired
    private AiBotFunction aiBotFunction;

    @Test
    public void chatTest(){
        String openId = "test_user";
        String query = "hello world";
        WeChatAiBotSignatureResponse signatureResponse = aiBotFunction.signature(openId);
        String signature = signatureResponse.getSignature();
        Assert.assertTrue("签名数据不应该为空", StringUtils.isNotBlank(signature));

        ChatResponse chatResponse = aiBotFunction.chat(signature, query, AiBotEnvEnum.DEBUG, null, null);
        String answer = chatResponse.getAnswer();
        Assert.assertTrue("ai 应答结果数据不应该为空", StringUtils.isNotBlank(answer));
    }
}
