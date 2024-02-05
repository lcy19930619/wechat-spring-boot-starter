package net.jlxxw.wechat.log.enums;

/**
 * 日志相关配置路径参数
 * @author 冷春阳
 * @date 2024-02-05 14:25
 */
public enum LoggerPropertiesKey {

    /**
     * 配置文件默认装配位置
     * value :
     *  logback: classpath:wechat-logback.xml
     *  log4j2: classpath:wechat-log4j2.xml
     */
    WECHAT_LOG_CONFIG_LOCATION("wechat.log.config"),
    /**
     * 是否启用默认日志配置
     * value: true 或者 false
     */
    WECHAT_LOG_ENABLE_DEFAULT_LOG_CONFIG("wechat.log.config.default.enable"),

    /**
     * 日志文件存储路径
     * value: ${user.home}/log/wechat/wechat.log
     */
    WECHAT_LOG_CONFIG_STORE_PATH("wechat.log.config.store.path"),

    /**
     * 最大滚动日志文件数
     * value: 15
     */
    WECHAT_LOG_MAX_INDEX("wechat.log.config.max.index"),
    /**
     * 每个文件大小
     * value: 100MB
     */
    WECHAT_LOG_MAX_FILE_SIZE("wechat.log.config.max.file.size"),

    /**
     * 日志等级
     * value: info
     */
    WECHAT_LOG_LEVEL("wechat.log.config.level"),

    ;
    private final String key;

    LoggerPropertiesKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
