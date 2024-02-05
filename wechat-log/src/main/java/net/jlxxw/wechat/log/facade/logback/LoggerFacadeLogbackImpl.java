package net.jlxxw.wechat.log.facade.logback;

import ch.qos.logback.classic.LoggerContext;
import net.jlxxw.wechat.log.enums.LoggerPropertiesKey;
import net.jlxxw.wechat.log.facade.AbstractLoggerFacade;
import net.jlxxw.wechat.log.util.ResourceUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * @see <a href="https://github.com/alibaba/nacos/blob/develop/client/src/main/java/com/alibaba/nacos/client/logging/logback/LogbackNacosLogging.java">配置方式参考 nacos 装配</a>
 * @author 冷春阳
 * @date 2024-02-04 17:47
 */
public class LoggerFacadeLogbackImpl extends AbstractLoggerFacade {

    private static final String LOG_NAME = "net.jlxxw.wechat.logger";
    private static Logger logger = LoggerFactory.getLogger(LoggerFacadeLogbackImpl.class);

    public LoggerFacadeLogbackImpl() {
        setProperties(LoggerPropertiesKey.WECHAT_LOG_CONFIG_LOCATION,"classpath:wechat-logback.xml");
    }

    /**
     * 载入配置模块
     */
    @Override
    public void loadLogConfiguration() {
        String location = System.getProperty(LoggerPropertiesKey.WECHAT_LOG_CONFIG_LOCATION.getKey());
        if (StringUtils.isBlank(location)) {
            // 找不到配置文件
            return;
        }

        if (!defaultConfigEnabled()) {
            // 禁用 默认 logback
            return;
        }

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        WeChatLogbackAdapter adapter = new WeChatLogbackAdapter();
        try {
            adapter.setContext(loggerContext);
            URL resourceUrl = ResourceUtils.getResourceUrl(location);
            adapter.configure(resourceUrl);
            logger = LoggerFactory.getLogger(LOG_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
