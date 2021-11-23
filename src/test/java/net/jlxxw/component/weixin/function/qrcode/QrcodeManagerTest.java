package net.jlxxw.component.weixin.function.qrcode;

import net.jlxxw.component.weixin.base.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2021-11-23 1:27 下午
 */
public class QrcodeManagerTest extends BaseTest {
    @Autowired
    public QrcodeManager qrcodeManager;

    @Test
    public void createTempStringQrcodeTest(){
        qrcodeManager.createStringQrcode("a",(o)->{
            String ticket = o.getTicket();
        });
    }
}
