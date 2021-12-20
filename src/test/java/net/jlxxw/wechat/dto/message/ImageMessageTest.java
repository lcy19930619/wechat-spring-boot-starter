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
 * @date 2021-12-18 6:10 下午
 */
public class ImageMessageTest extends BaseTest {
    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/xml/ImageMessage.xml");
        File file = classPathResource.getFile();
        ImageMessage message = readXmlData(file, ImageMessage.class);
        Assert.assertNotNull(message);
    }

    @Test
    public void responseTest() throws IOException {
        String xmlData = readXmlData("mock/data/xml/ImageMessage.xml");
        WeChatMessageResponse response = nettyMessageSend(xmlData, WeChatMessageResponse.class);
        Assert.assertNotNull(response);

        WeChatMessageResponse.Image image = response.getImage();
        Assert.assertNotNull(image);

        String mediaId = image.getMediaId();
        Assert.assertEquals("预期的值为media_id，但发现的值为:"+mediaId,"media_id",mediaId);

    }

}
