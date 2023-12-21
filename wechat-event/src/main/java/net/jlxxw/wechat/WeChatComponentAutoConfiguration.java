package net.jlxxw.wechat;

import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chunyang.leng
 * @date 2021/1/18 9:44 下午
 */
@Configuration
@ComponentScan("net.jlxxw.wechat.event")
public class WeChatComponentAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(WeChatComponentAutoConfiguration.class);




}
