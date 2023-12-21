package net.jlxxw.wechat.repository.token;


/**
 * @author chunyang.leng
 * @date 2021/1/19 5:28 下午
 */
public interface WeChatTokenRepository {

    /**
     * 保存token
     *
     * @param token
     */
    void save(String token);


    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    String get();


}
