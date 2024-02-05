package net.jlxxw.wechat.log.facade.log4j2;

import net.jlxxw.wechat.log.enums.LoggerPropertiesKey;
import net.jlxxw.wechat.log.facade.AbstractLoggerFacade;
import net.jlxxw.wechat.log.util.ResourceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * @see <a href="https://github.com/alibaba/nacos/blob/develop/client/src/main/java/com/alibaba/nacos/client/logging/log4j2/Log4J2NacosLogging.java">配置方式参考 nacos 装配</a>
 * @author 冷春阳
 * @date 2024-02-05 09:52
 */
public class LoggerFacadeLog4j2Impl extends AbstractLoggerFacade {

    private static Logger logger = LoggerFactory.getLogger(LoggerFacadeLog4j2Impl.class);
    private static final String LOGGER_NAME = "net.jlxxw.wechat.logger";

    public LoggerFacadeLog4j2Impl() {
        setProperties(LoggerPropertiesKey.WECHAT_LOG_CONFIG_LOCATION,"classpath:wechat-log4j2.xml");
    }


    /**
     * 载入配置模块
     */
    @Override
    public void loadLogConfiguration() {

        String location = getProperties(LoggerPropertiesKey.WECHAT_LOG_CONFIG_LOCATION);
        if (StringUtils.isBlank(location)) {
            return;
        }

        if (!defaultConfigEnabled()) {
            return;
        }

        final LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        final Configuration contextConfiguration = loggerContext.getConfiguration();

        Configuration configuration = loadConfiguration(loggerContext, location);
        configuration.start();

        Map<String, Appender> appenders = configuration.getAppenders();
        for (Appender appender : appenders.values()) {
            contextConfiguration.addAppender(appender);
        }
        Map<String, LoggerConfig> loggers = configuration.getLoggers();
        for (String name : loggers.keySet()) {
            if (name.equals(LOGGER_NAME)) {
                contextConfiguration.addLogger(name, loggers.get(name));
            }
        }

        loggerContext.updateLoggers();

        logger = LoggerFactory.getLogger(LOGGER_NAME);
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


    private Configuration loadConfiguration(LoggerContext loggerContext, String location) {
        try {
            URL url = ResourceUtils.getResourceUrl(location);
            ConfigurationSource source = getConfigurationSource(url);
            return ConfigurationFactory.getInstance().getConfiguration(loggerContext, source);
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize Log4J2 logging from " + location, e);
        }
    }

    private ConfigurationSource getConfigurationSource(URL url) throws IOException {
        InputStream stream = url.openStream();
        if ("file".equals(url.getProtocol())) {
            return new ConfigurationSource(stream, ResourceUtils.getResourceAsFile(url));
        }
        return new ConfigurationSource(stream, url);
    }
}
