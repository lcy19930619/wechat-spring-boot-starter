package net.jlxxw.wechat.function.material;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.response.material.MaterialCountResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2022-08-12 6:03 PM
 */
public class MaterialManagerTest extends BaseTest {
    @Autowired
    private MaterialManager materialManager;

    @Test
    public void materialCountTest(){
        MaterialCountResponse materialCountResponse = materialManager.materialCount();
        Assert.assertNotNull("统计信息不应为空",materialCountResponse);
    }
}
