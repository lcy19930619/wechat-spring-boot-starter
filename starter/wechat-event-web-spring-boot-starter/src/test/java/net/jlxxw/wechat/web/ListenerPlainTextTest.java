package net.jlxxw.wechat.web;

import jakarta.annotation.PostConstruct;
import net.jlxxw.wechat.web.properties.WeChatEventWebProperties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;

/**
 * @author chunyang.leng
 * @date 2023-12-25 18:34
 */
@TestPropertySource("classpath:application-*.yml")
@ActiveProfiles("plain")
@SpringBootTest(classes = WeChatEventWebAutoConfiguration.class)
public class ListenerPlainTextTest {
    @Autowired
    private WeChatEventWebProperties weChatEventWebProperties;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    @PostConstruct
    public void before() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    /**
     * 事件明文测试
     */
    @Test
    public void startEventPlainTextTest() throws Exception {

        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] eventMessageResources = pathMatchingResourcePatternResolver.getResources("mock/data/xml/event/*Message.xml");
        Assertions.assertNotNull(eventMessageResources, "测试事件资源不应该为空");
        String coreControllerUrl = weChatEventWebProperties.getCoreControllerUrl();
        for (Resource resource : eventMessageResources) {
            InputStream inputStream = resource.getInputStream();
            List<String> list = IOUtils.readLines(inputStream, "utf-8");
            String xmlData = String.join("\n", list);

            MockHttpServletRequestBuilder content = MockMvcRequestBuilders
                    .post(new URI(coreControllerUrl))
                    .contentType(MediaType.APPLICATION_XML)
                    .content(xmlData);

            ResultActions perform = mockMvc.perform(content);
            MvcResult mvcResult = perform.andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            int status = response.getStatus();
            Assertions.assertEquals(200,status);
            String contentAsString = response.getContentAsString();
            System.out.println(contentAsString);
            Assertions.assertTrue(StringUtils.isNotBlank(contentAsString),"应答结果不应该为null");
        }


    }

    /**
     * 消息明文测试
     */
    @Test
    public void startMessagePlainTextTest() throws Exception {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] messageResources = pathMatchingResourcePatternResolver.getResources("mock/data/xml/message/*Message.xml");
        Assertions.assertNotNull(messageResources, "测试消息资源不应该为空");
        List<Resource> resourcesList = new ArrayList<>(Arrays.asList(messageResources));

        String coreControllerUrl = weChatEventWebProperties.getCoreControllerUrl();
        for (Resource resource : resourcesList) {
            InputStream inputStream = resource.getInputStream();
            List<String> list = IOUtils.readLines(inputStream, "utf-8");
            String xmlData = String.join("\n", list);

            MockHttpServletRequestBuilder content = MockMvcRequestBuilders
                    .post(new URI(coreControllerUrl))
                    .contentType(MediaType.APPLICATION_XML)
                    .content(xmlData);

            ResultActions perform = mockMvc.perform(content);
            MvcResult mvcResult = perform.andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            int status = response.getStatus();
            Assertions.assertEquals(200,status);
            String contentAsString = response.getContentAsString();
            System.out.println(contentAsString);
            Assertions.assertTrue(StringUtils.isNotBlank(contentAsString),"应答结果不应该为null");
        }

    }

}
