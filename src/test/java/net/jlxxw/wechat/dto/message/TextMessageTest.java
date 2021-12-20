package net.jlxxw.wechat.dto.message;

import net.jlxxw.wechat.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-18 6:19 下午
 */
public class TextMessageTest extends BaseTest {
    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/xml/TextMessage.xml");
        File file = classPathResource.getFile();
        TextMessage subscribeEventMessage = readXmlData(file, TextMessage.class);
        Assert.assertNotNull(subscribeEventMessage);
    }
}
