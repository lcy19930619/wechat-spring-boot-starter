package net.jlxxw.wechat.function.api;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.response.api.ApiResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2022-08-16 1:53 PM
 */
public class OpenApiManagerTest extends BaseTest {
    @Autowired
    private OpenApiManager openApiManager;

    @Test
    public void selectQuotaTest(){
        ApiResponse response = openApiManager.selectQuota("/cgi-bin/message/custom/send");
        Assert.assertNotNull("查询结果不应该为空",response);
    }

    @Test(expected = ParamCheckException.class)
    public void selectQuotaExceptionTest(){
        openApiManager.selectQuota(null);
    }



    @Test(expected = ParamCheckException.class)
    public void selectRidExceptionTest(){
        openApiManager.selectRid(null);
    }

}
