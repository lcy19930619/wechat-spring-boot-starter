package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-19 5:25 下午
 */
public class ClickMenuGetInfoEventMessageTest extends BaseTest {

    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/xml/event/ClickMenuGetInfoEventMessage.xml");
        File file = classPathResource.getFile();
        ClickMenuGetInfoEventMessage message = readXmlData(file, ClickMenuGetInfoEventMessage.class);
        Assert.assertNotNull(message);
    }

    @Test
    public void sendTest() throws IOException {
        String xmlData = readXmlData("mock/data/xml/event/ClickMenuGetInfoEventMessage.xml");
        WeChatMessageResponse response = nettyMessageSend(xmlData, WeChatMessageResponse.class);
        Assert.assertNotNull("返回值不应为null",response);
    }
}
