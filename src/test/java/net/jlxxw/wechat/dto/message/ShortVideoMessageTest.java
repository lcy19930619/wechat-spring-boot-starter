package net.jlxxw.wechat.dto.message;

import net.jlxxw.wechat.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-18 6:18 下午
 */
public class ShortVideoMessageTest extends BaseTest {
    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/ShortVideoMessage.xml");
        File file = classPathResource.getFile();
        ShortVideoMessage message = readXmlData(file, ShortVideoMessage.class);
        Assert.assertNotNull(message);
    }

}
