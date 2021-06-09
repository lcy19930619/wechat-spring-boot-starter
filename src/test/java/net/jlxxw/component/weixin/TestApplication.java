package net.jlxxw.component.weixin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chunyang.leng
 * @date 2021-06-09 1:18 下午
 */
@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TestApplication.class);
        springApplication.run(args);
    }
}
