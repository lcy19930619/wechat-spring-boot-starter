package net.jlxxw.component.weixin.schedul;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.component.weixin.constant.UrlConstant;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import net.jlxxw.component.weixin.properties.WeiXinProperties;
import net.jlxxw.component.weixin.security.WeiXinServerSecurityCheck;
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
public class ScheduledUpdateWeiXinServerIp {
    private WeiXinTokenManager weiXinTokenManager;
    private RestTemplate restTemplate;
    private WeiXinServerSecurityCheck weiXinServerSecurityCheck;
    private WeiXinProperties weiXinProperties;

    public ScheduledUpdateWeiXinServerIp(WeiXinTokenManager weiXinTokenManager, RestTemplate restTemplate, WeiXinServerSecurityCheck weiXinServerSecurityCheck, WeiXinProperties weiXinProperties) {
        this.weiXinTokenManager = weiXinTokenManager;
        this.restTemplate = restTemplate;
        this.weiXinServerSecurityCheck = weiXinServerSecurityCheck;
        this.weiXinProperties = weiXinProperties;
    }

    /**
     * 每间隔三小时去更新一次服务器回调接口ip白名单
     */
    @PostConstruct
    @Scheduled(cron = "00 00 3,6,9,12,15,18,21 * * ?")
    public void update() {
        if(!weiXinProperties.isEnableWeiXinCallBackServerSecurityCheck()){
            return;
        }
        if(weiXinTokenManager == null){
            throw new BeanCreationException("检查微信白名单时，必须配置WeiXinTokenManager的实现类，或配置weixin.enable-default-token-manager: true");
        }
        String tokenFromLocal = weiXinTokenManager.getTokenFromLocal();
        String forObject = restTemplate.getForObject(UrlConstant.WEIXIN_CALL_BACK_SERVER_IP_PREFIX + tokenFromLocal, String.class);
        JSONObject jsonObject = JSONObject.parseObject(forObject);
        final JSONArray ipList = jsonObject.getJSONArray("ip_list");
        if (!CollectionUtils.isEmpty(ipList)) {
            weiXinServerSecurityCheck.updateWeiXinServerIp(ipList.toJavaList(String.class));
        }
    }
}
