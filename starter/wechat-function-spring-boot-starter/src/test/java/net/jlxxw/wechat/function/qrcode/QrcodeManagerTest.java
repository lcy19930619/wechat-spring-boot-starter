package net.jlxxw.wechat.function.qrcode;

import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.response.qrcode.QrCodeResponse;
import net.jlxxw.wechat.response.qrcode.TempQrCodeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Random;
import java.util.UUID;

/**
 * @author chunyang.leng
 * @date 2022-08-16 2:37 PM
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class QrcodeManagerTest {

    @Autowired
    private QrcodeManager qrcodeManager;


    @Test
    public void createTempQrcodeExceptionTest1() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            qrcodeManager.createTempStringQrcode(null, Long.MAX_VALUE);
        });
    }

    @Test
    public void createTempQrcodeExceptionTest2() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            qrcodeManager.createTempStringQrcode("xx", null);
        });
    }


    @Test
    public void createTempQrcodeExceptionTest3() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            qrcodeManager.createTempIdQrcode(null, Long.MAX_VALUE);
        });
    }

    @Test
    public void createTempQrcodeExceptionTest4() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            qrcodeManager.createTempIdQrcode(1L, null);
        });
    }


    @Test
    public void createTempStringQrcodeTest() {
        String uuid = UUID.randomUUID().toString();
        TempQrCodeResponse qrcode = qrcodeManager.createTempStringQrcode(uuid, 200L);
        Assertions.assertNotNull(qrcode, "创建二维码返回结果不应为空");
    }

    @Test
    public void createTempIdQrcodeTest() {
        long l = new Random().nextLong();
        TempQrCodeResponse qrcode = qrcodeManager.createTempIdQrcode(l, 200L);
        Assertions.assertNotNull(qrcode, "创建二维码返回结果不应为空");
    }

    @Test
    public void createStringQrcodeTest() {
        String uuid = UUID.randomUUID().toString();
        QrCodeResponse qrcode = qrcodeManager.createStringQrcode(uuid);
        Assertions.assertNotNull(qrcode, "创建二维码返回结果不应为空");
    }

    @Test
    public void createIdQrcodeTest() {
        long l = new Random().nextLong();
        QrCodeResponse qrcode = qrcodeManager.createIdQrcode(l);
        Assertions.assertNotNull(qrcode, "创建二维码返回结果不应为空");
    }

    @Test
    public void createIdQrcodeExceptionTest() {

        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            qrcodeManager.createIdQrcode(null);
        });
    }


    @Test
    public void createStringQrcodeExceptionTest() {
        Assertions.assertThrowsExactly(ParamCheckException.class, () -> {
            qrcodeManager.createStringQrcode(null);
        });
    }
}
