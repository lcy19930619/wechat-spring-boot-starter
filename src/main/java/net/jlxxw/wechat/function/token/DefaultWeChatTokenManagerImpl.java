package net.jlxxw.wechat.function.token;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.mapper.TokenMapper;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.Objects;

import static net.jlxxw.wechat.constant.UrlConstant.GET_JS_API_TICKET_URL;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:30 下午
 */
@DependsOn({"tokenMapper", "weChatProperties"})
@ConditionalOnProperty(prefix = "we-chat", value = "enable-default-token-manager", havingValue = "true")
@Component("weChatTokenManager")
public class DefaultWeChatTokenManagerImpl implements WeChatTokenManager {
    private static final Logger logger = LoggerFactory.getLogger(DefaultWeChatTokenManagerImpl.class);
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TokenMapper tokenMapper;
    @PostConstruct
    public void postConstruct() {
        tokenMapper.createTokenTable();
        tokenMapper.createJsApiTicketTable();
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
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">文档地址</a>
     */
    @Override
    public String getTokenFromWeiXin() throws WeChatException {
        String url = MessageFormat.format(UrlConstant.TOKEN_URL, weChatProperties.getAppId(), weChatProperties.getSecret());
        WeChatTokenResponse response = restTemplate.getForObject(url, WeChatTokenResponse.class);
        if (Objects.nonNull(response.getErrcode()) && 0 != response.getErrcode()) {
            logger.error("微信获取token返回值:{}", JSON.toJSONString(response));
            WeChatException weChatException = new WeChatException(JSON.toJSONString(response));
            weChatException.setErrorCode(response.getErrcode());
            throw weChatException;
        }
        return response.getAccessToken();
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
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html">文档地址</a>
     */
    @Override
    public String getJsApiTicketFromWeiXin() throws WeChatException {
        String url = MessageFormat.format(GET_JS_API_TICKET_URL,getTokenFromLocal());
        ResponseEntity<WeChatTokenResponse> response = restTemplate.getForEntity(url, WeChatTokenResponse.class);
        WeChatTokenResponse body = response.getBody();
        if (Objects.nonNull(body.getErrcode()) && 0 != body.getErrcode()) {
            logger.error("微信获取token返回值:{}", JSON.toJSONString(response));
            WeChatException weChatException = new WeChatException(JSON.toJSONString(response));
            weChatException.setErrorCode(body.getErrcode());
            throw weChatException;
        }
        return body.getTicket();
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
