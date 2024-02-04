package net.jlxxw.wechat.log;

import net.jlxxw.wechat.log.facade.LoggerFacade;
import net.jlxxw.wechat.log.facade.LoggerFacadeLogbackImpl;
import net.jlxxw.wechat.log.util.LoggerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author 冷春阳
 * @date 2024-02-04 17:49
 */
@Component
@ComponentScan("net.jlxxw.wechat.log")
public class WeChatLogbackAutoConfiguration {
    private static final LoggerFacade facade = new LoggerFacadeLogbackImpl();

    static {
        facade.loadLogConfiguration();
        LoggerUtils.init(facade.getLogger());
    }

    @Bean
    public LoggerFacade loggerFacade() {
        return facade;
    }
}
