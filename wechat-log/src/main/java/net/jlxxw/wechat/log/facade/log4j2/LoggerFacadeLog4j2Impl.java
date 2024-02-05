package net.jlxxw.wechat.log.facade.log4j2;

import net.jlxxw.wechat.log.facade.AbstractLoggerFacade;
import org.slf4j.Logger;

/**
 * @author 冷春阳
 * @date 2024-02-05 09:52
 */
public class LoggerFacadeLog4j2Impl extends AbstractLoggerFacade {


    /**
     * 载入配置模块
     */
    @Override
    public void loadLogConfiguration() {

    }

    /**
     * 获取日志模块
     *
     * @return 日志模块
     */
    @Override
    public Logger getLogger() {
        return super.getLogger();
    }
}
