package net.jlxxw.wechat.security;

import java.util.List;

/**
 * @author chunyang.leng
 * @date 2021-11-23 3:22 下午
 */
public interface WeChatSecurityIpStore {

    /**
     * 新增一个微信的服务器ip
     *
     * @param ip 微信服务器ip
     */
    void addSecurityIp(String ip);

    /**
     * 判断当前访问ip是否是安全的
     *
     * @param ip
     * @return
     */
    boolean isSecurityIp(String ip);

    /**
     * 新增一组微信的服务器ip
     *
     * @param ipList 微信服务器ip
     */
    void addSecurityIp(List<String> ipList);
}
