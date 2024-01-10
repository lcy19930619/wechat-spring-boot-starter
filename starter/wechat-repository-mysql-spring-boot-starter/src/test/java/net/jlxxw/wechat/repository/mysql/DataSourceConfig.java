package net.jlxxw.wechat.repository.mysql;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author chunyang.leng
 * @date 2024-01-10 13:10
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource () {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE&serverTimezone=Asia/Shanghai&connectTimeout=300000&socketTimeout=300000");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("123456");
        return hikariDataSource;
    }
}
