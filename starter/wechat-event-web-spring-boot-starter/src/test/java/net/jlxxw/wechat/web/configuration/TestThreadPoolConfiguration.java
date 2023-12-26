package net.jlxxw.wechat.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

@Configuration
public class TestThreadPoolConfiguration {
    @Bean
    Executor eventBusThreadPool(){
        return Runnable::run;
    }
}
