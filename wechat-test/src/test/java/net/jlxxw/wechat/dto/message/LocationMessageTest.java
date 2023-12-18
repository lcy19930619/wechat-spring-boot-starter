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
 * @date 2021-12-18 5:50 下午
 */
public class LocationMessageTest extends BaseTest {

    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/xml/LocationMessage.xml");
        File file = classPathResource.getFile();
        LocationMessage message = readXmlData(file, LocationMessage.class);
        Assert.assertNotNull(message);
    }

    @Test
    public void responseTest() throws IOException {
        String xmlData = readXmlData("mock/data/xml/LocationMessage.xml");
        WeChatMessageResponse response = nettyMessageSend(xmlData, WeChatMessageResponse.class);
        Assert.assertNotNull(response);


        WeChatMessageResponse.Music music = response.getMusic();
        Assert.assertNotNull(music);



        String musicUrl = music.getMusicUrl();
        String hqMusicUrl = music.getHqMusicUrl();
        String description = music.getDescription();
        String title = music.getTitle();
        String thumbMediaId = music.getThumbMediaId();

        Assert.assertEquals("预期的值为musicUrl，但发现的值为:"+musicUrl,"musicUrl",musicUrl);
        Assert.assertEquals("预期的值为title，但发现的值为:"+title,"title",title);
        Assert.assertEquals("预期的值为description，但发现的值为:"+description,"description",description);
        Assert.assertEquals("预期的值为hqMusicUrl，但发现的值为:"+hqMusicUrl,"hqMusicUrl",hqMusicUrl);
        Assert.assertEquals("预期的值为thumbMediaId，但发现的值为:"+thumbMediaId,"thumbMediaId",thumbMediaId);

    }
}
