package net.jlxxw.wechat.log.facade.logback;

import ch.qos.logback.classic.LoggerContext;
import net.jlxxw.wechat.log.facade.AbstractLoggerFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author 冷春阳
 * @date 2024-02-04 17:47
 */
public class LoggerFacadeLogbackImpl extends AbstractLoggerFacade {

    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final String LOG_NAME = "net.jlxxw.wechat.logger";
    private static Logger logger = LoggerFactory.getLogger(LoggerFacadeLogbackImpl.class);


    /**
     * 载入配置模块
     */
    @Override
    public void loadLogConfiguration() {
        String location = getLocation();
        if (StringUtils.isBlank(location)) {
            return;
        }

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        WeChatLogbackAdapter adapter = new WeChatLogbackAdapter();
        try {
            adapter.setContext(loggerContext);
            adapter.configure(getResourceUrl(location));
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


    /**
     * Returns the URL of the resource on the classpath.
     *
     * @param resource The resource to find
     * @return The resource
     * @throws IOException If the resource cannot be found or read
     */
    public static URL getResourceUrl(String resource) throws IOException {
        if (resource.startsWith(CLASSPATH_PREFIX)) {
            String path = resource.substring(CLASSPATH_PREFIX.length());

            ClassLoader classLoader = LoggerFacadeLogbackImpl.class.getClassLoader();

            URL url = (classLoader != null ? classLoader.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                throw new FileNotFoundException("Resource [" + resource + "] does not exist");
            }

            return url;
        }

        try {
            return new URL(resource);
        } catch (MalformedURLException ex) {
            return new File(resource).toURI().toURL();
        }
    }
}
