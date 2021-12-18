package net.jlxxw.wechat.security;

import net.jlxxw.wechat.context.SpringContextHolder;
import net.jlxxw.wechat.schedul.ScheduledUpdateWeiXinServerIp;
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
 * @author chunyang.leng
 * @date 2021/1/25 4:20 下午
 */
public class WeiXinServerSecurityCheck {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinServerSecurityCheck.class);

    /**
     * ip存储器
     */
    private WeiXinSecurityIpStore weiXinSecurityIpStore = null;

    @Autowired
    private SpringContextHolder springContextHolder;

    @PostConstruct
    private void init(){
        try{
            weiXinSecurityIpStore = springContextHolder.getBean(WeiXinSecurityIpStore.class);
            Class clazz = weiXinSecurityIpStore.getClass();
            if(AopUtils.isAopProxy(weiXinSecurityIpStore)){
                clazz = AopUtils.getTargetClass(weiXinSecurityIpStore);
            }
            LoggerUtils.info(logger,"初始化微信安全ip存储器，使用外置存储,{}",clazz.getName());
        }catch (NoSuchBeanDefinitionException e){
            LoggerUtils.info(logger,"初始化微信安全ip存储器，使用内置存储");
                // 没有这个bean，使用内部类
            weiXinSecurityIpStore = new WeiXinSecurityIpStore() {
                /**
                 * 微信服务器ip白名单地址
                 */
                private final Set<String> ipWhitelist = new HashSet<>();

                /**
                 * 新增一个微信的服务器ip
                 *
                 * @param ip 微信服务器ip
                 */
                @Override
                public void addSecurityIp(String ip) {
                    ipWhitelist.add(ip);
                }

                /**
                 * 判断当前访问ip是否是安全的
                 *
                 * @param ip
                 * @return
                 */
                @Override
                public boolean isSecurityIp(String ip) {
                    return ipWhitelist.contains(ip);
                }

                /**
                 * 新增一组微信的服务器ip
                 *
                 * @param ipList 微信服务器ip
                 */
                @Override
                public void addSecurityIp(List<String> ipList) {
                    ipWhitelist.addAll(ipList);
                }
            };
        }
    }

    /**
     * 检查ip地址是否在微信白名单中
     * @param requestIp 要检查的ip地址
     * @return 是否安全
     */
    public boolean isSecurity(String requestIp){
        return weiXinSecurityIpStore.isSecurityIp(requestIp);
    }

    /**
     * 获取微信服务器IP地址,并添加到白名单列表中，由定时任务控制
     * @see ScheduledUpdateWeiXinServerIp#update()
     */
    public void updateWeiXinServerIp(List<String> ipList){
        logger.info("更新ip白名单：{}",ipList);
        weiXinSecurityIpStore.addSecurityIp(ipList);
    }
}
