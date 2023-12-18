package net.jlxxw.wechat.function.qrcode;

import java.util.Random;
import java.util.UUID;
import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.response.qrcode.QrCodeResponse;
import net.jlxxw.wechat.response.qrcode.TempQrCodeResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chunyang.leng
 * @date 2022-08-16 2:37 PM
 */
public class QrcodeManagerTest extends BaseTest {

    @Autowired
    private QrcodeManager qrcodeManager;


    @Test(expected = ParamCheckException.class)
    public void createTempQrcodeExceptionTest1(){
        qrcodeManager.createTempStringQrcode(null, Long.MAX_VALUE);
    }
    @Test(expected = ParamCheckException.class)
    public void createTempQrcodeExceptionTest2(){
        qrcodeManager.createTempStringQrcode("xx", null);
    }


    @Test(expected = ParamCheckException.class)
    public void createTempQrcodeExceptionTest3(){
        qrcodeManager.createTempIdQrcode(null, Long.MAX_VALUE);
    }
    @Test(expected = ParamCheckException.class)
    public void createTempQrcodeExceptionTest4(){
        qrcodeManager.createTempIdQrcode(1L, null);
    }


    @Test
    public void createTempStringQrcodeTest(){
        String uuid = UUID.randomUUID().toString();
        TempQrCodeResponse qrcode = qrcodeManager.createTempStringQrcode(uuid, 200L);
        Assert.assertNotNull("创建二维码返回结果不应为空",qrcode);
    }

    @Test
    public void createTempIdQrcodeTest(){
        long l = new Random().nextLong();
        TempQrCodeResponse qrcode = qrcodeManager.createTempIdQrcode(l, 200L);
        Assert.assertNotNull("创建二维码返回结果不应为空",qrcode);
    }

    @Test
    public void createStringQrcodeTest(){
        String uuid = UUID.randomUUID().toString();
        QrCodeResponse qrcode = qrcodeManager.createStringQrcode(uuid);
        Assert.assertNotNull("创建二维码返回结果不应为空",qrcode);
    }

    @Test
    public void createIdQrcodeTest(){
        long l = new Random().nextLong();
        QrCodeResponse qrcode = qrcodeManager.createIdQrcode(l);
        Assert.assertNotNull("创建二维码返回结果不应为空",qrcode);
    }

    @Test(expected = ParamCheckException.class)
    public void createIdQrcodeExceptionTest(){
        qrcodeManager.createIdQrcode(null);
    }


    @Test(expected = ParamCheckException.class)
    public void createStringQrcodeExceptionTest(){
        qrcodeManager.createStringQrcode(null);
    }
}
