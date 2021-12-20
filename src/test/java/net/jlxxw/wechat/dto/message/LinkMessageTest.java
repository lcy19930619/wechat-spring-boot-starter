package net.jlxxw.wechat.dto.message;

import net.jlxxw.wechat.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

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
}
