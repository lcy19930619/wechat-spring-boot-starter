package net.jlxxw.wechat.log.facade;

import net.jlxxw.wechat.log.enums.LoggerPropertiesKey;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Objects;

/**
 * @author 冷春阳
 * @date 2024-02-04 18:21
 */
public abstract class AbstractLoggerFacade implements LoggerFacade {

    static {
        String loggingPath = System.getProperty(LoggerPropertiesKey.WECHAT_LOG_CONFIG_STORE_PATH.getKey());
        if (StringUtils.isBlank(loggingPath)) {
            String userHome = System.getProperty("user.home");
            System.setProperty(LoggerPropertiesKey.WECHAT_LOG_CONFIG_STORE_PATH.getKey(), userHome + File.separator + "logs");
        }


        String maxIndex = System.getProperty(LoggerPropertiesKey.WECHAT_LOG_MAX_INDEX.getKey());
        if (StringUtils.isBlank(maxIndex)) {
            System.setProperty(LoggerPropertiesKey.WECHAT_LOG_MAX_INDEX.getKey(), "15");
        }


        String maxFileSize = System.getProperty(LoggerPropertiesKey.WECHAT_LOG_MAX_FILE_SIZE.getKey());
        if (StringUtils.isBlank(maxFileSize)) {
            System.setProperty(LoggerPropertiesKey.WECHAT_LOG_MAX_FILE_SIZE.getKey(), "100MB");
        }


        String level = System.getProperty(LoggerPropertiesKey.WECHAT_LOG_LEVEL.getKey());
        if (StringUtils.isBlank(level)) {
            System.setProperty(LoggerPropertiesKey.WECHAT_LOG_LEVEL.getKey(), "info");
        }
    }

    /**
     * 设置相关属性
     * @param key
     * @param value
     */
    public static void setProperties(LoggerPropertiesKey key,String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        System.setProperty(key.getKey(), value);
    }

    /**
     * 获取属性
     * @param key
     * @return
     */
    public static String getProperties(LoggerPropertiesKey key) {
        Objects.requireNonNull(key);
        return System.getProperty(key.getKey());
    }

    /**
     * 是否启用默认配置文件
     * @return 默认值，true 启用
     */
    public boolean defaultConfigEnabled() {
        String property = System.getProperty(LoggerPropertiesKey.WECHAT_LOG_ENABLE_DEFAULT_LOG_CONFIG.getKey());
        if (StringUtils.isBlank(property)) {
            return false;
        }
        return "true".equalsIgnoreCase(property.trim());
    }
}
