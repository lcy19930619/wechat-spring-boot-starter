package net.jlxxw.wechat.function.api;

import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.api.ApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author chunyang.leng
 * @date 2022-08-16 1:53 PM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class OpenApiManagerTest {
    @Autowired
    private OpenApiManager openApiManager;

    @Test
    public void selectQuotaTest(){
        ApiResponse response = openApiManager.selectQuota("/cgi-bin/message/custom/send");
        Assertions.assertNotNull(response, "查询结果不应该为空");
    }

    @Test
    public void selectQuotaExceptionTest(){
        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            openApiManager.selectQuota(null);
        });
    }


    @Test
    public void selectRidExceptionTest(){
        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            openApiManager.selectRid(null);
        });
    }

}
