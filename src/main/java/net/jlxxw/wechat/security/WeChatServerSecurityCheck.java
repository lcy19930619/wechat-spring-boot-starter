package net.jlxxw.wechat.security;

import net.jlxxw.wechat.context.SpringContextHolder;
import net.jlxxw.wechat.schedul.ScheduledUpdateWeChatServerIp;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 微信服务器安全检查，（ip白名单的过滤）
 *
 * @author chunyang.leng
 * @date 2021/1/25 4:20 下午
 */
public class WeChatServerSecurityCheck {
    private static final Logger logger = LoggerFactory.getLogger(WeChatServerSecurityCheck.class);

    /**
     * ip存储器
     */
    private WeChatSecurityIpStore weChatSecurityIpStore = null;

    @Autowired
    private SpringContextHolder springContextHolder;

    @PostConstruct
    private void init() {
        try {
            weChatSecurityIpStore = springContextHolder.getBean(WeChatSecurityIpStore.class);
            Class clazz = weChatSecurityIpStore.getClass();
            if (AopUtils.isAopProxy(weChatSecurityIpStore)) {
                clazz = AopUtils.getTargetClass(weChatSecurityIpStore);
            }
            LoggerUtils.info(logger, "初始化微信安全ip存储器，使用外置存储,{}", clazz.getName());
        } catch (NoSuchBeanDefinitionException e) {
            LoggerUtils.info(logger, "初始化微信安全ip存储器，使用内置存储");
            // 没有这个bean，使用内部类
            weChatSecurityIpStore = new WeChatSecurityIpStore() {

                /**
                 * 微信服务器ip段白名单地址
                 */
                private final Set<String> ipRangeWhitelist = new HashSet<>();

                /**
                 * 新增一个微信的服务器ip段
                 *
                 * @param ipRange 微信服务器ip段
                 */
                @Override
                public void addSecurityIpRange(String ipRange) {
                    ipRangeWhitelist.add(ipRange);
                }

                /**
                 * 判断当前访问ip是否是安全的
                 *
                 * @param ip
                 * @return
                 */
                @Override
                public boolean isSecurityIp(String ip) {
                    boolean security = false;
                    for (String ipRange : ipRangeWhitelist) {
                        if ( inRange(ip,ipRange) ){
                            return true;
                        }
                    }
                    return security;
                }

                /**
                 * 新增一组微信的服务器ip段
                 *
                 * @param ipRangeList 微信服务器ip段
                 */
                @Override
                public void addSecurityIpRange(List<String> ipRangeList) {
                    ipRangeWhitelist.addAll(ipRangeList);
                }
            };
        }
    }

    /**
     * 检查ip地址是否在微信白名单中
     *
     * @param requestIp 要检查的ip地址
     * @return 是否安全
     */
    public boolean isSecurity(String requestIp) {
        return weChatSecurityIpStore.isSecurityIp(requestIp);
    }

    /**
     * 获取微信服务器IP段地址,并添加到白名单列表中，由定时任务控制
     *
     * @see ScheduledUpdateWeChatServerIp#update()
     */
    public void updateWeiXinServerIpRange(List<String> ipRangeList) {
        logger.info("更新ip白名单：{}", ipRangeList);
        weChatSecurityIpStore.addSecurityIpRange(ipRangeList);
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
