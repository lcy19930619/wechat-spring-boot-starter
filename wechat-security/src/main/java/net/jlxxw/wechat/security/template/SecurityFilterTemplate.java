package net.jlxxw.wechat.security.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/**
 * 安全过滤器模板
 */
public interface SecurityFilterTemplate {

    Logger logger = LoggerFactory.getLogger(SecurityFilterTemplate.class);
    /**
     * 判断一个ip是否是可信的
     * @param ip
     * @return
     */
    default boolean security(String ip) {
        boolean blacklisted = blacklisted(ip);
        if (blacklisted) {
            return false;
        }
        Set<String> all = loadAllIpSegments();
        if (Objects.isNull(all)) {
            logger.warn("安全过滤器未能发现ip段存储，已允许访问,客户端ip：{}",ip);
            return true;
        }
        return all.parallelStream().anyMatch(ipSegment -> segment(ip, ipSegment));
    }

    /**
     * 判断此ip是否在黑名单列表中
     * @param ip 目标ip
     * @return true 在黑名单中, false 不在黑名单中
     */
    boolean blacklisted(String ip);

    /**
     * 加载全部ip段信息
     * @return 全部可信任的ip段
     */
    Set<String> loadAllIpSegments();

    /**
     * 拒绝此ip链接
     * @param ip ip
     */
    void reject(String ip);

    /**
     * 判断ip是否在范围内
     * @param ip 要检测的ip
     * @param segment 要检测段ip段
     * @return true 属于同一个网段，false 不属于同一个网段
     */
    static boolean segment(String ip, String segment) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8)
                | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(segment.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = segment.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);

        return (ipAddr & mask) == (cidrIpAddr & mask);
    }
}
