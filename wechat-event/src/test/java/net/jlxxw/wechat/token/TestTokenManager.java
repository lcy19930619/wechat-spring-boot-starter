package net.jlxxw.wechat.token;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * @author chunyang.leng
 * @date 2022-08-12 3:58 PM
 */
@Component("weChatTokenManager")
@ConditionalOnProperty(prefix = "we-chat", name = "enable-default-token-manager", havingValue = "false")
public class TestTokenManager extends BaseTest implements WeChatTokenManager {

    private static final Logger logger = LoggerFactory.getLogger(TestTokenManager.class);
    @PostConstruct
    private void init(){
        logger.info("使用测试环境token管理器");
    }

    /**
     * 保存token
     *
     * @param token
     */
    @Override
    public void saveToken(String token) {

    }

    /**
     * 定时从微信获取token
     *
     * @return token
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">文档地址</a>
     */
    @Override
    public String getTokenFromWeiXin() throws WeChatException {
        return null;
    }

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    @Override
    public String getTokenFromLocal() {
        return getToken();
    }

    /**
     * 保存JsApiTicket
     *
     * @param jsApiTicket
     */
    @Override
    public void saveJsApiTicket(String jsApiTicket) {

    }

    /**
     * 定时从微信获取JsApiTicket
     *
     * @return JsApiTicket
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html">文档地址</a>
     */
    @Override
    public String getJsApiTicketFromWeiXin() throws WeChatException {
        return null;
    }

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    @Override
    public String getJsApiTicketFromLocal() {
        return null;
    }
}
