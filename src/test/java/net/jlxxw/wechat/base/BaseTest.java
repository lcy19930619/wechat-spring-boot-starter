package net.jlxxw.wechat.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.jlxxw.wechat.TestApplication;
import net.jlxxw.wechat.function.token.WeiXinTokenManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author chunyang.leng
 * @date 2021-06-09 1:21 下午
 */
@SpringBootTest(classes = TestApplication.class )
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {
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
    private WeiXinTokenManager weiXinTokenManager;

    protected static  XmlMapper xmlMapper = new XmlMapper();
    protected static  ObjectMapper objectMapper = new ObjectMapper();

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

    protected String getToken(){
        return weiXinTokenManager.getTokenFromLocal();
    }


    /**
     * 读取Xml 转换为指定对象
     * @param xmlFile
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    protected static <T> T readXmlData(File xmlFile, Class<T> clazz) throws IOException {
        String xml = FileUtils.readFileToString(xmlFile, "utf-8");
        byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
        // jackson会自动关闭流，不需要手动关闭
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Reader reader = new InputStreamReader(inputStream);
        return xmlMapper.readValue(reader, clazz);
    }
}
