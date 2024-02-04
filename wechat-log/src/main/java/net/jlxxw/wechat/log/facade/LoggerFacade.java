package net.jlxxw.wechat.log.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 冷春阳
 * @date 2024-02-04 17:26
 */
public interface LoggerFacade {

    Logger DEFAULT_LOGGER = LoggerFactory.getLogger(LoggerFacade.class);

    /**
     * 载入配置模块
     */
    void loadLogConfiguration();

    /**
     * 获取日志模块
     * @return 日志模块
     */
    default Logger getLogger() {
        return DEFAULT_LOGGER;
    }

}
