package net.jlxxw.wechat.function.media;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.dto.media.UploadImageDTO;
import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.media.UploadImageResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class MediaUploadImageTest {

    @Autowired
    private MediaManager mediaManager;

    @Test
    public void test() throws IOException {
        UploadImageDTO uploadImageDTO = new UploadImageDTO();
        File file = new File("/Users/lcy/CursorProjects/wechat-spring-boot-starter/starter/wechat-function-spring-boot-starter/src/test/resources/mock/data/jpg/test.jpg");
        System.out.println(file.exists());
        uploadImageDTO.setMedia(file);

        UploadImageResponse uploadImageResponse = mediaManager.uploadImage(uploadImageDTO);
        Assert.assertTrue(uploadImageResponse.isSuccessful());
        System.out.println(JSON.toJSONString(uploadImageResponse));

    }

}
