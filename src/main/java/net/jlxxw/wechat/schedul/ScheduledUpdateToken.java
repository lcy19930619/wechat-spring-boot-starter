package net.jlxxw.wechat.schedul;

import java.time.LocalDateTime;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * token更新 定时器
 * @author chunyang.leng
 * @date 2021/1/20 2:46 下午
 */
@Configuration
@ConditionalOnProperty(prefix = "we-chat", name = "enable-default-token-manager", havingValue = "true")
public class ScheduledUpdateToken {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledUpdateToken.class);
    private final WeChatTokenManager weChatTokenManager;

    public ScheduledUpdateToken( WeChatTokenManager weChatTokenManager) {
        this.weChatTokenManager = weChatTokenManager;
    }

    /**
     * 每30分钟执行一次，更新数据库 token 和 jsapi ticket
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void updateToken() throws WeChatException {
        String token = weChatTokenManager.getTokenFromWeiXin();
        weChatTokenManager.saveToken(token);
        LoggerUtils.info(logger,"更新token成功，当前时间:{}", LocalDateTime.now());


        // 更新ticket
        String ticket = weChatTokenManager.getJsApiTicketFromWeiXin();
        weChatTokenManager.saveJsApiTicket(ticket);
        LoggerUtils.info(logger,"更新jsApiTicket成功,当前时间:{}", LocalDateTime.now());

    }
}
