package net.jlxxw.wechat.web;

import jakarta.annotation.PostConstruct;
import net.jlxxw.wechat.event.codec.WeChatMessageCodec;
import net.jlxxw.wechat.event.util.SHA1;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.web.listener.util.WechatMessageCrypt;
import net.jlxxw.wechat.web.properties.WeChatEventWebProperties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 加密模式测试
 * @author chunyang.leng
 * @date 2023-12-25 18:50
 */
@TestPropertySource("classpath:application-cipher.yml")
@ActiveProfiles("cipher")
@SpringBootTest(classes = WeChatEventWebAutoConfiguration.class)
public class ListenerCipherTextTest {

    private static final String openId = "FromUser";
    @Autowired
    private WeChatEventWebProperties weChatEventWebProperties;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private WeChatMessageCodec weChatMessageCodec;
    private MockMvc mockMvc;

    private WechatMessageCrypt wechatMessageCrypt;

    @PostConstruct
    private void initialize() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        wechatMessageCrypt = new WechatMessageCrypt(weChatProperties.getEncodingAesKey(), weChatProperties.getAppId());
    }


    /**
     * 事件加密传输测试
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
            String xml = String.join("\n", list);

            String randomStr = wechatMessageCrypt.getRandomStr();
            String encrypt = wechatMessageCrypt.encrypt(randomStr, xml);
            long timeMillis = System.currentTimeMillis();
            String signature = SHA1.getSHA1(weChatProperties.getVerifyToken(), String.valueOf(timeMillis) , randomStr,encrypt);
            String encParameters = "nonce="+randomStr + "&timestamp="+timeMillis + "&msg_signature=" +signature;
            String xmlData = "<xml><Encrypt><![CDATA[" + encrypt + "]]></Encrypt><ToUserName>" + openId +"</ToUserName></xml>";
            String url = coreControllerUrl + "?"+encParameters;

            MockHttpServletRequestBuilder content = MockMvcRequestBuilders
                    .post(new URI(url))
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

            String decrypt = wechatMessageCrypt.decryptXML(contentAsString);
            System.out.println(decrypt);
        }


    }

    /**
     * 消息加密传输测试
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
            String xml = String.join("\n", list);

            String randomStr = wechatMessageCrypt.getRandomStr();
            String encrypt = wechatMessageCrypt.encrypt(randomStr, xml);
            long timeMillis = System.currentTimeMillis();
            String signature = SHA1.getSHA1(weChatProperties.getVerifyToken(), String.valueOf(timeMillis) , randomStr,encrypt);
            String encParameters = "nonce="+randomStr + "&timestamp="+timeMillis + "&msg_signature=" +signature;
            String xmlData = "<xml><Encrypt><![CDATA[" + encrypt + "]]></Encrypt><ToUserName>" + openId +"</ToUserName></xml>";
            String url = coreControllerUrl + "?"+encParameters;

            MockHttpServletRequestBuilder content = MockMvcRequestBuilders
                    .post(new URI(url))
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

            String decrypt = wechatMessageCrypt.decryptXML(contentAsString);
            System.out.println(decrypt);
        }

    }


}
