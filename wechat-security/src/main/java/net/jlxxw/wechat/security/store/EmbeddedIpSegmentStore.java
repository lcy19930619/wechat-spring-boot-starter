package net.jlxxw.wechat.security.store;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 内嵌 ip 段存储器
 * @author chunyang.leng
 * @date 2023-12-19 03:52
 */
public class EmbeddedIpSegmentStore implements IpSegmentStore {
    /**
     * 存储器
     */
    private static final Set<String> STORE = new LinkedHashSet<>();
    /**
     * 新增一个微信的服务器ip段
     *
     * @param ipRange 微信服务器ip段
     */
    @Override
    public void addSecurityIpRange(String ipRange) {
        STORE.add(ipRange);
    }

    /**
     * 新增一组微信的服务器ip段
     *
     * @param ipRangeList 微信服务器ip段
     */
    @Override
    public void addSecurityIpRange(List<String> ipRangeList) {
        STORE.addAll(ipRangeList);
    }


    /**
     * 判断当前访问ip是否是安全的
     *
     * @param ip
     * @return
     */
    @Override
    public boolean security(String ip) {
        if (STORE.isEmpty()) {
            return false;
        }
        return STORE.parallelStream().anyMatch(range-> inRange(ip,range));
    }



    /**
     * 判断ip是否在范围内
     * @param ip 要检测的ip
     * @param ipRange 要检测段ip段
     * @return
     */
    public static boolean inRange(String ip, String ipRange) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(ipRange.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = ipRange.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);

        return (ipAddr & mask) == (cidrIpAddr & mask);
    }
}
