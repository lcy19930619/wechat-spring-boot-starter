package net.jlxxw.wechat.dto.message;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.response.WeChatMessageResponse;
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

    @Test
    public void responseTest() throws IOException {
        String xmlData = readXmlData("mock/data/xml/TextMessage.xml");
        WeChatMessageResponse response = nettyMessageSend(xmlData, WeChatMessageResponse.class);
        Assert.assertNotNull(response);
        String content = response.getContent();
        Assert.assertEquals("预期的值为content，但发现的值为:"+content,"content",content);
    }
}
