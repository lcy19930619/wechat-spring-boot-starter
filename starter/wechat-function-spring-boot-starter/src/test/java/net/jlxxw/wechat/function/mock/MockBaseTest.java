package net.jlxxw.wechat.function.mock;

import net.jlxxw.wechat.function.WeChatFunctionAutoConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author chunyang.leng
 * @date 2021-12-20 11:25 上午
 */
@SpringBootTest(classes = WeChatFunctionAutoConfiguration.class)
public class MockBaseTest {

    protected String getJson(String classPathName) {

        ClassPathResource classPathResource = new ClassPathResource(classPathName);
        InputStream inputStream = null;
        try {
            inputStream = classPathResource.getInputStream();
            List<String> list = IOUtils.readLines(inputStream, "utf-8");
            return String.join("", list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> ResponseEntity<T> buildMockResponseEntity(T data){
        return ResponseEntity.ok(data);
    }
}
