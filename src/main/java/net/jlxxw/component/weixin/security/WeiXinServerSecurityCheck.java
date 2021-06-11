package net.jlxxw.component.weixin.security;

import net.jlxxw.component.weixin.schedul.ScheduledUpdateWeiXinServerIp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 微信服务器安全检查，（ip白名单的过滤）
 * @author chunyang.leng
 * @date 2021/1/25 4:20 下午
 */
public class WeiXinServerSecurityCheck {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinServerSecurityCheck.class);

    /**
     * 微信服务器ip白名单地址
     */
    private final Set<String> ipWhitelist = new HashSet<>();

    /**
     * 检查ip地址是否在微信白名单中
     * @param requestIp 要检查的ip地址
     * @return 是否安全
     */
    public boolean isSecurity(String requestIp){
        return ipWhitelist.contains(requestIp);
    }

    /**
     * 获取微信服务器IP地址,并添加到白名单列表中，由定时任务控制
     * @see ScheduledUpdateWeiXinServerIp#update()
     */
    public void updateWeiXinServerIp(List<String> ipList){
        logger.info("更新ip白名单：{}",ipList);
        ipWhitelist.addAll(ipList);
    }
}
