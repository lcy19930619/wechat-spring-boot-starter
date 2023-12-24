package net.jlxxw.wechat.schedule;

import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.TokenManager;
import net.jlxxw.wechat.repository.jsapi.WeChatJsApiTicketRepository;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * token更新 定时器
 * @author chunyang.leng
 * @date 2021/1/20 2:46 下午
 */
public class ScheduledUpdateJsApiTicket {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledUpdateJsApiTicket.class);
    private final WeChatTokenRepository weChatTokenRepository;

    private final TokenManager tokenManager;

    private final WeChatJsApiTicketRepository weChatJsApiTicketRepository;

    public ScheduledUpdateJsApiTicket(WeChatTokenRepository weChatTokenRepository,
                                      TokenManager tokenManager,
                                      WeChatJsApiTicketRepository weChatJsApiTicketRepository) {
        this.weChatTokenRepository = weChatTokenRepository;
        this.tokenManager = tokenManager;
        this.weChatJsApiTicketRepository = weChatJsApiTicketRepository;
    }

    /**
     * 每30分钟执行一次，更新jsapi ticket
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void updateToken() throws WeChatException {
        String token = weChatTokenRepository.get();
        WeChatTokenResponse jsTicketFromWeiXin = tokenManager.getJsTicketFromWeiXin(token);
        weChatJsApiTicketRepository.save(jsTicketFromWeiXin.getTicket());
        LoggerUtils.info(logger,"更新jsApiTicket成功,当前时间:{}", LocalDateTime.now());
    }
}
