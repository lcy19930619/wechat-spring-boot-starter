package net.jlxxw.wechat.schedul;

import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.mapper.TokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author chunyang.leng
 * @date 2021/1/20 2:46 下午
 */
@Configuration
@ConditionalOnProperty(prefix = "we-chat", name = "enable-default-token-manager", havingValue = "true")
public class ScheduledUpdateToken {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledUpdateToken.class);
    private TokenMapper tokenMapper;
    private WeChatTokenManager weChatTokenManager;

    public ScheduledUpdateToken(TokenMapper tokenMapper, WeChatTokenManager weChatTokenManager) {
        this.tokenMapper = tokenMapper;
        this.weChatTokenManager = weChatTokenManager;
    }

    /**
     * 每两个小时执行一次，更新数据库token
     */
    @Scheduled(cron = "0 0 */2 * * ?")
    public void updateToken() throws WeChatException {
        Date date = tokenMapper.lockSelectMaxDate();
        if (date == null) {
            // 第一次操作的时候，数据库没数据
            String token = weChatTokenManager.getTokenFromWeiXin();
            weChatTokenManager.saveToken(token);
            logger.info("更新token成功，当前时间:{}", LocalDateTime.now().toString());
            return;
        }
        long dbTime = date.getTime();
        long current = System.currentTimeMillis();
        if ((current - dbTime) < 3 * 60 * 60) {
            // 误差时间小于3分钟时，不做任何处理
            return;
        }
        String token = weChatTokenManager.getTokenFromWeiXin();
        weChatTokenManager.saveToken(token);
        logger.info("更新token成功，当前时间:{}", LocalDateTime.now().toString());
    }
}
