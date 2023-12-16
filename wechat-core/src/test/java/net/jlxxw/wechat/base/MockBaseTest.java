package net.jlxxw.wechat.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.jlxxw.wechat.TestApplication;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author chunyang.leng
 * @date 2021-12-20 11:25 上午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class MockBaseTest {
    private static final Logger logger = LoggerFactory.getLogger(MockBaseTest.class);
    @Autowired
    private WebApplicationContext applicationContext;

    public MockMvc mockMvc;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        logger.info("init mockMvc");
    }

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
