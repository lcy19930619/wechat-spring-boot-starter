package net.jlxxw.wechat.function.token;

import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * @author chunyang.leng
 * @date 2023-12-17 12:03
 */
@Component
public class TokenManager {
    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 从微信获取 token
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">文档地址</a>
     * @return
     * @throws WeChatException
     */
    public WeChatTokenResponse getTokenFromWeiXin() throws WeChatException {

        String url = MessageFormat.format(UrlConstant.TOKEN_URL,weChatProperties.getAppId(),weChatProperties.getSecret());
        String response = restTemplate.getForObject(url, String.class);

        WeChatTokenResponse weChatTokenResponse = JSONObject.parseObject(response, WeChatTokenResponse.class);

        if(!weChatTokenResponse.isSuccessful()){
            throw new WeChatException(weChatTokenResponse);
        }
        return weChatTokenResponse;

    }
}
