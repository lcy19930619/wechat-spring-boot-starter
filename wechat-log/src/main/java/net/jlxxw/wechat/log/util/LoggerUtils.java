package net.jlxxw.wechat.log.util;

import net.jlxxw.wechat.log.facade.LoggerFacade;
import net.jlxxw.wechat.log.facade.log4j2.LoggerFacadeLog4j2Impl;
import net.jlxxw.wechat.log.facade.logback.LoggerFacadeLogbackImpl;
import org.slf4j.Logger;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chunyang.leng
 * @date 2021-03-05 5:38 下午
 */
public class LoggerUtils {

    private static Logger logger = null;

    static {
        LoggerFacade facade = null;
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            facade = new LoggerFacadeLogbackImpl();
        } catch (ClassNotFoundException e) {
            // log4j2 init
            facade = new LoggerFacadeLog4j2Impl();
        }
        facade.loadLogConfiguration();
        logger = facade.getLogger();
    }

    public static void debug(Logger logger, String message, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }

    public static void info(Logger logger, String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    public static void warn(Logger logger, String message, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, args);
        }
    }

    public static void error(Logger logger, String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            if (throwable instanceof NullPointerException) {
                // npe 情况下，堆栈信息单独打印
                StackTraceElement[] stackTrace = throwable.getStackTrace();
                String collect = Stream.of(stackTrace).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
                logger.error(message + collect);
                return;
            }
            logger.error(message, throwable);
        }
    }


    public static void debug(String message, Object... args) {
        debug(logger,message,args);
    }

    public static void info( String message, Object... args) {
        info(logger,message,args);
    }

    public static void warn(String message, Object... args) {
        warn(logger,message,args);
    }

    public static void error( String message, Throwable throwable) {
        error(logger,message,throwable);
    }
}
