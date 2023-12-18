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
 * @date 2021-12-18 6:23 下午
 */
public class VoiceMessageTest extends BaseTest {

    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/xml/VoiceMessage.xml");
        File file = classPathResource.getFile();
        VoiceMessage subscribeEventMessage = readXmlData(file, VoiceMessage.class);
        Assert.assertNotNull(subscribeEventMessage);
    }

    @Test
    public void responseTest() throws IOException {
        String xmlData = readXmlData("mock/data/xml/VoiceMessage.xml");
        WeChatMessageResponse response = nettyMessageSend(xmlData, WeChatMessageResponse.class);
        Assert.assertNotNull(response);

        WeChatMessageResponse.Voice voice = response.getVoice();

        String mediaId = voice.getMediaId();
        Assert.assertEquals("预期的值为mediaId，但发现的值为:"+mediaId,"mediaId",mediaId);


    }
}
