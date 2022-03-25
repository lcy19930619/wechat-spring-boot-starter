package net.jlxxw.wechat.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  日志打印器
 * @author chunyang.leng
 * @date 2022-03-25 4:35 PM
 */
public interface AllLoggers {

    /**
     * 应用日志
     */
    Logger COMPONENT = LoggerFactory.getLogger("COMPONENT");

    /**
     * 异常日志
     */
    Logger EXCEPTION = LoggerFactory.getLogger("EXCEPTION");

    /**
     * 应用日志
     */
    Logger NETTY = LoggerFactory.getLogger("NETTY");

}
