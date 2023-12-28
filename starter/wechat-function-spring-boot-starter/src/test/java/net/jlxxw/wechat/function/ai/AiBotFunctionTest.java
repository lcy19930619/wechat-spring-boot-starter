package net.jlxxw.wechat.function.ai;

import net.jlxxw.wechat.enums.AiBotEnvEnum;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.ai.ChatResponse;
import net.jlxxw.wechat.response.ai.WeChatAiBotSignatureResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author chunyang.leng
 * @date 2023-04-11 16:27
 */

@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class AiBotFunctionTest  {

    private static final Logger logger = LoggerFactory.getLogger(AiBotFunction.class);
    @Autowired(required = false)
    private AiBotFunction aiBotFunction;

    @Test
    public void chatTest(){
        if (aiBotFunction == null) {
            logger.warn("尚未启动 ai bot 装配，本次测试终止");
            return;
        }
        String openId = "test_user";
        String query = "hello world";
        WeChatAiBotSignatureResponse signatureResponse = aiBotFunction.signature(openId);
        String signature = signatureResponse.getSignature();
        Assertions.assertTrue(StringUtils.isNotBlank(signature), "签名数据不应该为空");

        ChatResponse chatResponse = aiBotFunction.chat(signature, query, AiBotEnvEnum.DEBUG, null, null);
        String answer = chatResponse.getAnswer();
        Assertions.assertTrue(StringUtils.isNotBlank(answer), "ai 应答结果数据不应该为空");
    }


    @Test
    public void signatureExceptionTest1(){
        if (aiBotFunction == null) {
            logger.warn("尚未启动 ai bot 装配，本次测试终止");

            return;
        }
        Assertions.assertThrows(ParamCheckException.class,()->{
            aiBotFunction.signature(null);
        });

    }


    @Test
    public void signatureExceptionTest2(){
        if (aiBotFunction == null) {
            logger.warn("尚未启动 ai bot 装配，本次测试终止");

            return;
        }
        Assertions.assertThrows(ParamCheckException.class,()->{
            aiBotFunction.chat(null,"11",null,null,null);
        });

    }

    @Test
    public void signatureExceptionTest3(){
        if (aiBotFunction == null) {
            logger.warn("尚未启动 ai bot 装配，本次测试终止");

            return;
        }
        Assertions.assertThrows(ParamCheckException.class,()->{
            aiBotFunction.chat("11",null,null,null,null);
        });

    }

}
