package net.jlxxw.wechat.log.facade.logback;

import net.jlxxw.wechat.log.facade.AbstractLoggerFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 冷春阳
 * @date 2024-02-04 17:47
 */
public class LoggerFacadeLogbackImpl extends AbstractLoggerFacade {

    private static final String LOG_NAME = "net.jlxxw.wechat.logger";
    private static final Logger logger = LoggerFactory.getLogger(LOG_NAME);


    /**
     * 载入配置模块
     */
    @Override
    public void loadLogConfiguration() {
        String location = getLocation();
        if (StringUtils.isBlank(location)) {
            return;
        }
// todo 初始化 logback
//        try {
//            LoggerContext loggerContext = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
//            new ContextInitializer(loggerContext).configureByResource(ResourceUtils.getResourceUrl(location));
//        } catch (Exception e) {
//            throw new IllegalStateException("无法初始化 logback 配置文件:" + location, e);
//        }
    }

    /**
     * 获取日志模块
     *
     * @return 日志模块
     */
    @Override
    public Logger getLogger() {
        return logger;
    }
}
