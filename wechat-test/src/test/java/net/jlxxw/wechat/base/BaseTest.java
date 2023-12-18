package net.jlxxw.wechat.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import jakarta.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.jlxxw.wechat.TestApplication;
import net.jlxxw.wechat.component.WeChatMsgCodec;
import net.jlxxw.wechat.enums.AesExceptionEnum;
import net.jlxxw.wechat.exception.AesException;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.util.SHA1;
import net.jlxxw.wechat.util.WechatMessageCrypt;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

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
    protected String openId = "oFNc6s4hmaVobZhVQ0H4NUysNEmw";
    /**
     * 测试用的模版Id
     */
    protected String templateId = "UB7TV635X6AGDAhZzAiCNkOa7V74hUkl9Yb5kY-Yxvw";

    /**
     * 测试用的token
     */
    protected String token = "x";

    @Autowired
    private RestTemplate restTemplate;
    @Value("${we-chat.netty.server.netty-port}")
    private int nettyPort;

    @Autowired
    private WeChatProperties weChatProperties;

    private WechatMessageCrypt wechatMessageCrypt = null;
    /**
     * 有可能未启用微信加解密功能
     */
    @Autowired(required = false)
    private WeChatMsgCodec weChatMsgCodec;

    @PostConstruct
    private void init() {
        if (weChatProperties.isEnableMessageEnc()){
            wechatMessageCrypt = new WechatMessageCrypt(weChatProperties.getEncodingAesKey(), weChatProperties.getAppId());
        }
    }
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
        Assert.assertTrue("测试openId不能为空", StringUtils.isNotBlank(openId));
        Assert.assertTrue("测试模版Id不能为空", StringUtils.isNotBlank(templateId));
        Assert.assertTrue("测试token不能为空", StringUtils.isNotBlank(token));
    }

    protected String getToken() {
        return token;
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
        String encParameters = "";
        if (weChatProperties.isEnableMessageEnc()){
            try {
                String randomStr = wechatMessageCrypt.getRandomStr();
                String encrypt = wechatMessageCrypt.encrypt(randomStr, xmlData);
                long timeMillis = System.currentTimeMillis();
                String signature = SHA1.getSHA1(weChatProperties.getVerifyToken(), String.valueOf(timeMillis) , randomStr,encrypt);
                encParameters = "nonce="+randomStr + "&timestamp="+timeMillis + "&msg_signature=" +signature;
                xmlData = "<xml><Encrypt>" + encrypt + "</Encrypt><ToUserName>" + openId +"</ToUserName></xml>";
            }catch (AesException e) {
                throw new RuntimeException(e);
            }

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> formEntity = new HttpEntity<>(xmlData, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:" + nettyPort + "?"+encParameters, formEntity, String.class);
        Assert.assertEquals(responseEntity.getStatusCode().value(), 200);
        String xml = responseEntity.getBody();
        Assert.assertNotNull("返回值不应为null",xml);

        byte[] data = xml.getBytes(StandardCharsets.UTF_8);
        if (weChatProperties.isEnableMessageEnc()) {
            try {
                String uri = "?" +encParameters;

                // 收到返回的xml
                String inputXML = new String(data, StandardCharsets.UTF_8);

                // 获取签名
                String msgSignature = null;
                // 获取时间戳
                String timestamp =null;
                // 获取随机串
                String nonce = null;
                try {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    StringReader sr = new StringReader(inputXML);
                    InputSource is = new InputSource(sr);
                    Document document = db.parse(is);

                    Element root = document.getDocumentElement();
                    inputXML = root.getElementsByTagName("Encrypt").item(0).getTextContent();
                    timestamp = root.getElementsByTagName("TimeStamp").item(0).getTextContent();
                    nonce = root.getElementsByTagName("Nonce").item(0).getTextContent();
                    msgSignature = root.getElementsByTagName("MsgSignature").item(0).getTextContent();

                } catch (Exception e) {
                    throw new AesException(AesExceptionEnum.PARSE_XML_ERROR, e);
                }

                // 验证安全签名
                String signature = SHA1.getSHA1(weChatProperties.getVerifyToken(), timestamp, nonce, inputXML);

                // 和URL中的签名比较是否相等
                if (!signature.equals(msgSignature)) {
                    throw new AesException(AesExceptionEnum.VALIDATE_SIGNATURE_ERROR);
                }
                // 将解密后的数据，转换为byte数组，用于协议的具体处理
                data = weChatMsgCodec.decrypt(inputXML).getBytes(StandardCharsets.UTF_8);
            } catch (AesException e) {
                throw new RuntimeException(e);
            }

        }

        // jackson会自动关闭流，不需要手动关闭
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Reader reader = new InputStreamReader(inputStream);
        return xmlMapper.readValue(reader, clazz);
    }


    protected static String readXmlData(String classPathName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(classPathName);
        File file = classPathResource.getFile();
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

}
