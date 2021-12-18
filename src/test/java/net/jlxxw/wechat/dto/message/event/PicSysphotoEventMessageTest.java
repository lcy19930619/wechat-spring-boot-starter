package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author chunyang.leng
 * @date 2021-12-18 4:20 下午
 */
public class PicSysphotoEventMessageTest extends BaseTest {

    @Test
    public void convertTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("mock/data/event/pic_sysphoto.xml");
        File file = classPathResource.getFile();
        PicSysphotoEventMessage picSysphotoEventMessage = readXmlData(file, PicSysphotoEventMessage.class);
        Assert.assertNotNull(picSysphotoEventMessage);
    }
}
