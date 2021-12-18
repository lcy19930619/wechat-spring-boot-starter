package net.jlxxw.wechat.dto.message;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.dto.message.event.SubscribeQrsceneEventMessage;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-18 5:44 下午
 */
public class SubscribeQrsceneEventMessageTest extends BaseTest {
    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/SubscribeQrsceneEventMessage.xml");
        File file = classPathResource.getFile();
        SubscribeQrsceneEventMessage subscribeEventMessage = readXmlData(file, SubscribeQrsceneEventMessage.class);
        Assert.assertNotNull(subscribeEventMessage);
    }

}
