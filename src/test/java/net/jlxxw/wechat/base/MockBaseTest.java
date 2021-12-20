package net.jlxxw.wechat.base;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author chunyang.leng
 * @date 2021-12-20 11:25 上午
 */
@RunWith(MockitoJUnitRunner.class)
public class MockBaseTest {

    protected String getJson(String classPathName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(classPathName);
        InputStream inputStream = classPathResource.getInputStream();
        List<String> list = IOUtils.readLines(inputStream, "utf-8");
        return String.join("", list);
    }
}
