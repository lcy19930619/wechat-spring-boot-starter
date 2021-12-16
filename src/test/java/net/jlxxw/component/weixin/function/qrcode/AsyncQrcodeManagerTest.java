package net.jlxxw.component.weixin.function.qrcode;

import net.jlxxw.component.weixin.base.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2021-11-23 1:27 下午
 */
public class AsyncQrcodeManagerTest extends BaseTest {
    @Autowired
    public AsyncQrcodeManager asyncQrcodeManager;

    @Test
    public void createTempStringQrcodeTest(){
        asyncQrcodeManager.createStringQrcode("a",(o)->{
            String ticket = o.getTicket();
        });
    }
}
