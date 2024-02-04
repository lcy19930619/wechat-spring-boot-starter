package net.jlxxw.wechat.schedule;

import java.time.LocalDateTime;
import net.jlxxw.wechat.exception.WeChatException;

import net.jlxxw.wechat.function.token.TokenManager;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import net.jlxxw.wechat.log.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * token更新 定时器
 * @author chunyang.leng
 * @date 2021/1/20 2:46 下午
 */
public class ScheduledUpdateToken {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledUpdateToken.class);
    private final WeChatTokenRepository weChatTokenRepository;

    private final TokenManager tokenManager;

    public ScheduledUpdateToken(WeChatTokenRepository weChatTokenRepository, TokenManager tokenManager) {
        this.weChatTokenRepository = weChatTokenRepository;
        this.tokenManager = tokenManager;
    }

    /**
     * 每30分钟执行一次，更新数据库 token
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void updateToken() throws WeChatException {
        WeChatTokenResponse tokenFromWeiXin = tokenManager.getTokenFromWeiXin();

        weChatTokenRepository.save(tokenFromWeiXin.getAccessToken());
        LoggerUtils.info(logger,"更新token成功，当前时间:{}", LocalDateTime.now());
    }
}
