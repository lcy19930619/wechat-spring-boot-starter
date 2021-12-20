package net.jlxxw.wechat.function.token;

import net.jlxxw.wechat.exception.WeChatException;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:28 下午
 */
public interface WeChatTokenManager {

    /**
     * 保存token
     *
     * @param token
     */
    void saveToken(String token);

    /**
     * 定时从微信获取token
     *
     * @return token
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">文档地址</a>
     */
    String getTokenFromWeiXin() throws WeChatException;

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    String getTokenFromLocal();


    /**
     * 保存JsApiTicket
     *
     * @param jsApiTicket
     */
    void saveJsApiTicket(String jsApiTicket);

    /**
     * 定时从微信获取JsApiTicket
     *
     * @return JsApiTicket
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html">文档地址</a>
     */
    String getJsApiTicketFromWeiXin() throws WeChatException;

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    String getJsApiTicketFromLocal();
}
