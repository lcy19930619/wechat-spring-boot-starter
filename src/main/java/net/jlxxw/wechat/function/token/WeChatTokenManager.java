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
     */
    String getTokenFromWeiXin() throws WeChatException;

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    String getTokenFromLocal();
}
