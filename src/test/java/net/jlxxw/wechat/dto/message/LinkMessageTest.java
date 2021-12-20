package net.jlxxw.wechat.dto.message;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author chunyang.leng
 * @date 2021-12-18 6:15 下午
 */
public class LinkMessageTest extends BaseTest {

    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/xml/LinkMessage.xml");
        File file = classPathResource.getFile();
        LinkMessage message = readXmlData(file, LinkMessage.class);
        Assert.assertNotNull(message);
    }

    @Test
    public void responseTest() throws IOException {
        String xmlData = readXmlData("mock/data/xml/LinkMessage.xml");
        WeChatMessageResponse response = nettyMessageSend(xmlData, WeChatMessageResponse.class);
        Assert.assertNotNull(response);

        Integer articleCount = response.getArticleCount();
        Assert.assertEquals("预期值为2", new Integer(2), articleCount);
        List<WeChatMessageResponse.Article> articles = response.getArticles();
        Assert.assertNotNull("数据不应为空", articles);

        articles.forEach(o -> {
            String description = o.getDescription();
            String picUrl = o.getPicUrl();
            String title = o.getTitle();
            String url = o.getUrl();
            Assert.assertEquals("预期的值为description，但发现的值为:" + description, "Description", description);
            Assert.assertEquals("预期的值为picUrl，但发现的值为:" + picUrl, "PicUrl", picUrl);
            Assert.assertEquals("预期的值为title，但发现的值为:" + title, "Title", title);
            Assert.assertEquals("预期的值为url，但发现的值为:" + url, "Url", url);

        });

    }
}
