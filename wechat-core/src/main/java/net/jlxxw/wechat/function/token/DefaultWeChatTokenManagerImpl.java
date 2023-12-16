package net.jlxxw.wechat.function.token;

import com.alibaba.fastjson.JSONObject;
import java.text.MessageFormat;
import javax.annotation.PostConstruct;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.mapper.TokenMapper;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:30 下午
 */
@DependsOn({"tokenMapper", "weChatProperties"})
@ConditionalOnProperty(prefix = "we-chat", value = "enable-default-token-manager", havingValue = "true")
@Component(WeChatTokenManager.BEAN_NAME)
public class DefaultWeChatTokenManagerImpl implements WeChatTokenManager {
    private static final Logger logger = LoggerFactory.getLogger(DefaultWeChatTokenManagerImpl.class);
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void postConstruct() {
        tokenMapper.createTokenTable();
        LoggerUtils.info(logger, "已自动创建 token 表");
        tokenMapper.createJsApiTicketTable();
        LoggerUtils.info(logger, "已自动创建 js_api 表");
    }

    /**
     * 保存token
     *
     * @param token
     */
    @Override
    public void saveToken(String token) {
        tokenMapper.insertToken(token);
    }

    /**
     * 定时从微信获取token
     *
     * @return token
     * @throws WeChatException 微信异常
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">文档地址</a>
     */
    @Override
    public String getTokenFromWeiXin() throws WeChatException {

        String url = MessageFormat.format(UrlConstant.TOKEN_URL,weChatProperties.getAppId(),weChatProperties.getSecret());
        String response = restTemplate.getForObject(url, String.class);

        WeChatTokenResponse weChatTokenResponse = JSONObject.parseObject(response, WeChatTokenResponse.class);

        if(!weChatTokenResponse.isSuccessful()){
            throw new WeChatException(weChatTokenResponse);
        }
        return weChatTokenResponse.getAccessToken();

    }

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    @Override
    public String getTokenFromLocal() {
        return tokenMapper.getToken();
    }

    /**
     * 保存JsApiTicket
     *
     * @param jsApiTicket
     */
    @Override
    public void saveJsApiTicket(String jsApiTicket) {
        tokenMapper.insertJsApiTicket(jsApiTicket);
    }

    /**
     * 定时从微信获取JsApiTicket
     *
     * @return JsApiTicket
     * @throws WeChatException 微信异常
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html">文档地址</a>
     */
    @Override
    public String getJsApiTicketFromWeiXin() throws WeChatException {

        String url = MessageFormat.format(UrlConstant.GET_JS_API_TICKET_URL, getTokenFromLocal());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String body = response.getBody();
        WeChatTokenResponse weChatTokenResponse = JSONObject.parseObject(body, WeChatTokenResponse.class);
        if (!weChatTokenResponse.isSuccessful()) {
            throw new WeChatException(weChatTokenResponse);
        }
        return weChatTokenResponse.getTicket();
    }

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    @Override
    public String getJsApiTicketFromLocal() {
        return tokenMapper.getJsApiTicket();
    }
}
