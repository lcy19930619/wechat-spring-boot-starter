package net.jlxxw.wechat.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.jlxxw.wechat.TestApplication;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author chunyang.leng
 * @date 2021-06-09 1:21 下午
 */
@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {

    protected static XmlMapper xmlMapper = new XmlMapper();
    protected static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 测试用的openId
     */
    protected String openId = "";
    /**
     * 测试用的模版Id
     */
    protected String templateId = "";

    /**
     * 测试用的token
     */
    protected String token = "";

    @Autowired
    private WeChatTokenManager weChatTokenManager;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${we-chat.netty.server.netty-port}")
    private int nettyPort;

    static {
        // 初始化xmlMapper相关配置
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        // 初始化objectMapper相关配置
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

    }

    @Before
    public void setOpenId() {
        Assert.assertTrue("测试openId不能为空", StringUtils.isBlank(openId));
        Assert.assertTrue("测试模版Id不能为空", StringUtils.isBlank(templateId));
        token = getToken();
        Assert.assertTrue("测试token不能为空", StringUtils.isBlank(token));
    }

    protected String getToken() {
        return weChatTokenManager.getTokenFromLocal();
    }


    /**
     * 读取Xml 转换为指定对象
     *
     * @param xmlFile
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    protected static <T> T readXmlData(File xmlFile, Class<T> clazz) throws IOException {
        String xml = FileUtils.readFileToString(xmlFile, StandardCharsets.UTF_8);
        byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
        // jackson会自动关闭流，不需要手动关闭
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Reader reader = new InputStreamReader(inputStream);
        ObjectNode jsonNodes = xmlMapper.readValue(reader, ObjectNode.class);
        return objectMapper.readValue(jsonNodes.toString(), clazz);
    }


    protected <T> T nettyMessageSend(String xmlData,Class<T> clazz) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> formEntity = new HttpEntity<>(xmlData, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:" + nettyPort, formEntity, String.class);
        Assert.assertEquals(responseEntity.getStatusCode().value(), 200);
        String xml = responseEntity.getBody();
        Assert.assertNotNull("返回值不应为null",xml);

        byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
        // jackson会自动关闭流，不需要手动关闭
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Reader reader = new InputStreamReader(inputStream);
        return xmlMapper.readValue(reader, clazz);
    }


    protected static String readXmlData(String classPathName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(classPathName);
        File file = classPathResource.getFile();
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

}
