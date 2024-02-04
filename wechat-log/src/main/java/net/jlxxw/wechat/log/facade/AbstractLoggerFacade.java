package net.jlxxw.wechat.log.facade;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author 冷春阳
 * @date 2024-02-04 18:21
 */
public class AbstractLoggerFacade implements LoggerFacade {


    /**
     * 载入配置模块
     */
    @Override
    public void loadLogConfiguration() {

    }

    /**
     * 日志配置文件位置
     */
    private static final String WECHAT_LOG_CONFIG_LOCATION = "wechat.log.config";
    /**
     * 是否启用默认日志配置文件，默认值:true ,启用
     */
    private static final String WECHAT_LOG_ENABLE_DEFAULT_LOG_CONFIG = "wechat.log.config.default.enable";

    /**
     * 日志文件存储路径
     */
    private static final String WECHAT_LOG_CONFIG_STORE_PATH = "wechat.log.config.store.path";

    private static final String WECHAT_LOG_MAX_INDEX = "wechat.log.config.max.index";


    private static final String WECHAT_LOG_MAX_FILE_SIZE = "wechat.log.config.max.file.size";


    private static final String WECHAT_LOG_LEVEL = "info";


    static {
        String loggingPath = System.getProperty(WECHAT_LOG_CONFIG_STORE_PATH);
        if (StringUtils.isBlank(loggingPath)) {
            String userHome = System.getProperty("user.home");
            System.setProperty(WECHAT_LOG_CONFIG_STORE_PATH, userHome + File.separator + "logs");
        }


        String maxIndex = System.getProperty(WECHAT_LOG_MAX_INDEX);
        if (StringUtils.isBlank(maxIndex)) {
            System.setProperty(WECHAT_LOG_MAX_INDEX, "15");
        }


        String maxFileSize = System.getProperty(WECHAT_LOG_MAX_FILE_SIZE);
        if (StringUtils.isBlank(maxFileSize)) {
            System.setProperty(WECHAT_LOG_MAX_FILE_SIZE, "100MB");
        }


        String level = System.getProperty(WECHAT_LOG_LEVEL);
        if (StringUtils.isBlank(level)) {
            System.setProperty(WECHAT_LOG_LEVEL, "info");
        }



        String configPath = System.getProperty(WECHAT_LOG_CONFIG_LOCATION);
        if (StringUtils.isBlank(configPath)) {
            System.setProperty(WECHAT_LOG_CONFIG_LOCATION, "info");
        }

    }

    /**
     * 获取配置文件存储路径
     * @return 默认返回 classpath:wechat-logback.xml
     */
    protected String getLocation() {
        String location = System.getProperty(WECHAT_LOG_CONFIG_LOCATION);
        if (StringUtils.isBlank(location)) {
            if (defaultConfigEnabled()) {
                return "classpath:wechat-logback.xml";
            }
            return null;
        }
        return location;
    }


    /**
     * 是否启用默认配置文件
     * @return 默认值，true 启用
     */
    private boolean defaultConfigEnabled() {
        String property = System.getProperty(WECHAT_LOG_ENABLE_DEFAULT_LOG_CONFIG);
        return property == null || "true".equalsIgnoreCase(property);
    }
}
