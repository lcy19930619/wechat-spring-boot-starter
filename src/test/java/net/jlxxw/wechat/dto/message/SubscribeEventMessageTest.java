package net.jlxxw.wechat.dto.message;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.dto.message.event.SubscribeEventMessage;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-18 5:40 下午
 */
public class SubscribeEventMessageTest extends BaseTest {

    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/event/SubscribeEventMessage.xml");
        File file = classPathResource.getFile();
        SubscribeEventMessage subscribeEventMessage = readXmlData(file, SubscribeEventMessage.class);
        Assert.assertNotNull(subscribeEventMessage);
    }


}
