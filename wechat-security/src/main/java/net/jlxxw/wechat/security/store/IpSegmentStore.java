package net.jlxxw.wechat.security.store;

import java.util.List;

/**
 *
 * @author chunyang.leng
 * @date 2021-11-23 3:22 下午
 */
public interface IpSegmentStore {

    /**
     * 新增一个微信的服务器ip段
     *
     * @param ipRange 微信服务器ip段
     */
    void addSecurityIpRange(String ipRange);

    /**
     * 判断当前访问ip是否是安全的
     *
     * @param ip
     * @return
     */
    boolean security(String ip);

    /**
     * 新增一组微信的服务器ip段
     *
     * @param ipRangeList 微信服务器ip段
     */
    void addSecurityIpRange(List<String> ipRangeList);
}
