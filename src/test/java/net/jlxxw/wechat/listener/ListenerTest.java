package net.jlxxw.wechat.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.exception.AesException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.util.SHA1;
import net.jlxxw.wechat.util.WechatMessageCrypt;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author chunyang.leng
 * @date 2021-12-18 7:00 下午
 */
public class ListenerTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(ListenerTest.class);
    @Autowired
    private RestTemplate restTemplate;
    @Value("${we-chat.netty.server.netty-port}")
    private int nettyPort;

    @Autowired
    private WeChatTokenManager weChatTokenManager;

    @Autowired
    private WeChatProperties weChatProperties;

    private WechatMessageCrypt wechatMessageCrypt = null;
    @PostConstruct
    private void init() {
        if (weChatProperties.isEnableMessageEnc()){
            wechatMessageCrypt = new WechatMessageCrypt(weChatProperties.getEncodingAesKey(), weChatProperties.getAppId());
        }
    }

    @Test
    public void allListenerTest() throws IOException {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] messageResources = pathMatchingResourcePatternResolver.getResources("mock/data/*/*Message.xml");
        Assert.assertNotNull("测试消息资源不应该为空", messageResources);
        List<Resource> resourcesList = new ArrayList<>(Arrays.asList(messageResources));

        Resource[] eventMessageResources = pathMatchingResourcePatternResolver.getResources("mock/data/*/*Message.xml");
        Assert.assertNotNull("测试事件资源不应该为空", eventMessageResources);
        resourcesList.addAll(Arrays.asList(eventMessageResources));

        for (Resource resource : resourcesList) {
            try (InputStream inputStream = resource.getInputStream();) {
                List<String> list = IOUtils.readLines(inputStream, "utf-8");
                String xmlData = String.join("\n", list);
                String encParameters = "";
                if (weChatProperties.isEnableMessageEnc()){
                    String randomStr = wechatMessageCrypt.getRandomStr();
                    String encrypt = wechatMessageCrypt.encrypt(randomStr, xmlData);
                    long timeMillis = System.currentTimeMillis();
                    String signature = SHA1.getSHA1(weChatProperties.getVerifyToken(), String.valueOf(timeMillis) , randomStr,encrypt);
                    encParameters = "nonce="+randomStr + "&timestamp="+timeMillis + "&msg_signature=" +signature;
                    xmlData = "<xml><Encrypt>" + encrypt + "</Encrypt><ToUserName>" + openId +"</ToUserName></xml>";
                }
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_XML);

                HttpEntity<String> formEntity = new HttpEntity<>(xmlData.toString(), headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:" + nettyPort + "?" +encParameters, formEntity, String.class);
                Assert.assertEquals(responseEntity.getStatusCode().value(), 200);
            } catch (AesException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
