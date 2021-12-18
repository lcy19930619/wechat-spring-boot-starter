package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-18 6:40 下午
 */
public class SubscribeScanEventMessageTest extends BaseTest {

    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/event/SubscribeScanEventMessage.xml");
        File file = classPathResource.getFile();
        SubscribeScanEventMessage message = readXmlData(file,SubscribeScanEventMessage.class);
        Assert.assertNotNull(message);
    }
}
