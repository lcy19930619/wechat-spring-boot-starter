package net.jlxxw.wechat.dto.message;

import net.jlxxw.wechat.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-18 6:21 下午
 */
public class VideoMessageTest extends BaseTest {
    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/xml/VideoMessage.xml");
        File file = classPathResource.getFile();
        VideoMessage subscribeEventMessage = readXmlData(file, VideoMessage.class);
        Assert.assertNotNull(subscribeEventMessage);
    }
}
