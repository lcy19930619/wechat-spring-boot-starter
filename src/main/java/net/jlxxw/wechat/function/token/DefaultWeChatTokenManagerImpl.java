package net.jlxxw.wechat.function.token;

import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.feign.WechatFeignClient;
import net.jlxxw.wechat.mapper.TokenMapper;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
    private TokenMapper tokenMapper;
    @Autowired
    private WechatFeignClient wechatFeignClient;
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
        WeChatTokenResponse tokenResponse = wechatFeignClient.getToken(weChatProperties.getAppId(), weChatProperties.getSecret());
        if(tokenResponse.isSuccessful()){
            return tokenResponse.getAccessToken();
        }
        throw new WeChatException(tokenResponse.getErrcode(),tokenResponse.getErrmsg());
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
        WeChatTokenResponse tokenResponse = wechatFeignClient.getTicket(getTokenFromLocal());
        if(tokenResponse.isSuccessful()){
            return tokenResponse.getTicket();
        }
        throw new WeChatException(tokenResponse.getErrcode(),tokenResponse.getErrmsg());
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
