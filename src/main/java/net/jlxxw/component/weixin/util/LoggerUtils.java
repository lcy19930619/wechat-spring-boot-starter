package net.jlxxw.component.weixin.util;

import org.slf4j.Logger;

import java.util.Objects;

/**
 * @author chunyang.leng
 * @date 2021-03-05 5:38 下午
 */
public class LoggerUtils {

    public static void debug(Logger logger, String message, String... args){
        if(logger.isDebugEnabled()){
            logger.debug(message,args);
        }
    }

    public static void info(Logger logger, String message, Objects... args){
        if(logger.isInfoEnabled()){
            logger.info(message,args);
        }
    }
    public static void error(Logger logger, String message,Throwable throwable){
        if(logger.isErrorEnabled()){
            logger.error(message,throwable);
        }
    }
}
