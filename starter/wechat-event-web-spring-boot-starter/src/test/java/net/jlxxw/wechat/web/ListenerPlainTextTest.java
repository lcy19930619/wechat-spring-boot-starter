package net.jlxxw.wechat.web;

import net.jlxxw.wechat.web.properties.WeChatEventWebProperties;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chunyang.leng
 * @date 2023-12-25 18:34
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = TestMain.class)
public class ListenerPlainTextTest {
    private static final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private WeChatEventWebProperties weChatEventWebProperties;
    @Autowired
    private ServerProperties serverProperties;

    /**
     * 事件明文测试
     */
    @Test
    public void startEventPlainTextTest() throws IOException, InterruptedException {

        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] eventMessageResources = pathMatchingResourcePatternResolver.getResources("mock/data/xml/event/*Message.xml");
        Assertions.assertNotNull(eventMessageResources, "测试事件资源不应该为空");
        String coreControllerUrl = weChatEventWebProperties.getCoreControllerUrl();
        String url = "http://127.0.0.1:" + serverProperties.getPort() + coreControllerUrl;
        for (Resource resource : eventMessageResources) {
            InputStream inputStream = resource.getInputStream();
            List<String> list = IOUtils.readLines(inputStream, "utf-8");
            String xmlData = String.join("\n", list);
            String response = send(url, xmlData);
            Assertions.assertTrue(StringUtils.isNotBlank(response), "测试结果不应该为空");
        }


    }

    /**
     * 消息明文测试
     */
    @Test
    public void startMessagePlainTextTest() throws IOException, InterruptedException {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] messageResources = pathMatchingResourcePatternResolver.getResources("mock/data/xml/message/*Message.xml");
        Assert.assertNotNull("测试消息资源不应该为空", messageResources);
        List<Resource> resourcesList = new ArrayList<>(Arrays.asList(messageResources));

        String coreControllerUrl = weChatEventWebProperties.getCoreControllerUrl();
        String url = "http://127.0.0.1:" + serverProperties.getPort() + coreControllerUrl;
        for (Resource resource : messageResources) {
            InputStream inputStream = resource.getInputStream();
            List<String> list = IOUtils.readLines(inputStream, "utf-8");
            String xmlData = String.join("\n", list);
            String response = send(url, xmlData);
            Assertions.assertTrue(StringUtils.isNotBlank(response), "测试结果不应该为空");
        }

    }




    private String send(String url, String xml) throws InterruptedException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        HttpEntity<String> formEntity = new HttpEntity<>(xml, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, formEntity, String.class);
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 200);

        return responseEntity.getBody();
    }

}
