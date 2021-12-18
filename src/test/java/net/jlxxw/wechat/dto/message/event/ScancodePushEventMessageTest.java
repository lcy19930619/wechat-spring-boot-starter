package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-17 7:02 下午
 */
public class ScancodePushEventMessageTest extends BaseTest {

    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/scancode_push.xml");
        File file = classPathResource.getFile();
        ScancodePushEventMessage message = readXmlData(file,ScancodePushEventMessage.class);
        Assert.assertNotNull(message);
    }


}
