package net.jlxxw.wechat.mysql.token;

import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.TokenManager;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.mysql.mapper.TokenMapper;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:30 下午
 */
public class DefaultWeChatTokenManagerImpl implements WeChatTokenManager {
    private static final Logger logger = LoggerFactory.getLogger(DefaultWeChatTokenManagerImpl.class);
    private final TokenMapper tokenMapper;
    private final TokenManager tokenManager;

    public DefaultWeChatTokenManagerImpl(TokenMapper tokenMapper, TokenManager tokenManager) {
        this.tokenMapper = tokenMapper;
        this.tokenManager = tokenManager;
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

        WeChatTokenResponse weChatTokenResponse = tokenManager.getTokenFromWeiXin();

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

        WeChatTokenResponse weChatTokenResponse = tokenManager.getJsTicketFromWeiXin();
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
