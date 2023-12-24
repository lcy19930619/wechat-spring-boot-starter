package net.jlxxw.wechat.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;

import net.jlxxw.wechat.function.ip.IpManager;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.repository.ip.IpSegmentRepository;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.ip.IpListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

import java.util.List;

/**
 * 定时更新微信服务器ip地址
 *
 * @author chunyang.leng
 * @date 2021/1/25 4:25 下午
 */
public class ScheduledUpdateWeChatServerIp {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledUpdateWeChatServerIp.class);
    private final IpSegmentRepository ipSegmentRepository;
    private final IpManager ipManager;

    public ScheduledUpdateWeChatServerIp(IpSegmentRepository ipSegmentRepository, IpManager ipManager) {
        this.ipSegmentRepository = ipSegmentRepository;
        this.ipManager = ipManager;
    }

    /**
     * 每间隔三小时去更新一次服务器回调接口ip白名单
     */
    @Scheduled(cron = "00 00 3,6,9,12,15,18,21 * * ?")
    public void update() {

        IpListResponse callbackIp = ipManager.getCallbackIp();
        List<String> ipList = callbackIp.getIpList();
        if (!CollectionUtils.isEmpty(ipList)) {
            ipSegmentRepository.add(ipList);
        }
    }
}
