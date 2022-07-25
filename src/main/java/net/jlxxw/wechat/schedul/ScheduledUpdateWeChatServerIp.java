package net.jlxxw.wechat.schedul;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.security.WeChatServerSecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * 定时更新微信服务器ip地址
 *
 * @author chunyang.leng
 * @date 2021/1/25 4:25 下午
 */
public class ScheduledUpdateWeChatServerIp {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledUpdateWeChatServerIp.class);
    private WeChatTokenManager weChatTokenManager;
    private RestTemplate restTemplate;
    private WeChatServerSecurityCheck weChatServerSecurityCheck;
    private WeChatProperties weChatProperties;

    public ScheduledUpdateWeChatServerIp(WeChatTokenManager weChatTokenManager, RestTemplate restTemplate, WeChatServerSecurityCheck weChatServerSecurityCheck, WeChatProperties weChatProperties) {
        this.weChatTokenManager = weChatTokenManager;
        this.restTemplate = restTemplate;
        this.weChatServerSecurityCheck = weChatServerSecurityCheck;
        this.weChatProperties = weChatProperties;
    }

    /**
     * 每间隔三小时去更新一次服务器回调接口ip白名单
     */
    @PostConstruct
    @Scheduled(cron = "00 00 3,6,9,12,15,18,21 * * ?")
    public void update() {
        if (!weChatProperties.isEnableWeChatCallBackServerSecurityCheck()) {
            return;
        }
        if (weChatTokenManager == null) {
            throw new BeanCreationException("检查微信白名单时，必须配置WeiXinTokenManager的实现类，或配置weixin.enable-default-token-manager: true");
        }
        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        String forObject = restTemplate.getForObject(UrlConstant.WEIXIN_CALL_BACK_SERVER_IP_PREFIX + tokenFromLocal, String.class);
        JSONObject jsonObject = JSONObject.parseObject(forObject);
        final JSONArray ipList = jsonObject.getJSONArray("ip_list");
        if (!CollectionUtils.isEmpty(ipList)) {
            weChatServerSecurityCheck.updateWeiXinServerIpRange(ipList.toJavaList(String.class));
        }
    }
}
