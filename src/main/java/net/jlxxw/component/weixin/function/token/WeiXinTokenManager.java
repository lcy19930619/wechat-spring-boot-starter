package net.jlxxw.component.weixin.function.token;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:28 下午
 */
public interface WeiXinTokenManager {

    /**
     * 保存token
     * @param token
     */
    void saveToken(String token);

    /**
     * 定时从微信获取token
     * @return token
     */
    String getTokenFromWeiXin();

    /**
     * 获取保存在本地的token
     * @return 保存在本地的token
     */
    String getTokenFromLocal();
}
