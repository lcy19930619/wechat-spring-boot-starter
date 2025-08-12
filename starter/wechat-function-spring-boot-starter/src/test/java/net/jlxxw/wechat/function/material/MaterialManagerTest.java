package net.jlxxw.wechat.function.material;

import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.material.MaterialCountResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author chunyang.leng
 * @date 2022-08-12 6:03 PM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class MaterialManagerTest  {
    @Autowired
    private MaterialManager materialManager;

    @Test
    public void materialCountTest(){
        MaterialCountResponse materialCountResponse = materialManager.materialCount();
        Assertions.assertNotNull(materialCountResponse, "统计信息不应为空");
    }
}
