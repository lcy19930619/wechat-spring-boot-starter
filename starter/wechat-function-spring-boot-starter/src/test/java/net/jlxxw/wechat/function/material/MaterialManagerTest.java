package net.jlxxw.wechat.function.material;

import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.material.MaterialCountResponse;
import org.junit.Assert;
import org.junit.Test;
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
        Assert.assertNotNull("统计信息不应为空",materialCountResponse);
    }
}
