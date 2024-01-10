package net.jlxxw.wechat.repository.mysql.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库 mysql 层相关适配属性
 * @author lcy
 */
@Configuration
@ConfigurationProperties("wechat.repository.mysql")
public class WeChatMySqlProperties {

    /**
     * 是否自动创建token表，默认：自动创建
     */
    private boolean enableAutoCreateTokenTable = true;
    /**
     * 是否自动创建js api 表，默认：自动创建
     */
    private boolean enableAutoCreateJsApiTable = true;

    /**
     * 数据源 bean 名称，在多数据源环境下，可以使用此参数，指定数据源
     * 默认： dataSource
     */
    private String dataSourceBeanName = "dataSource";

    public String getDataSourceBeanName() {
        return dataSourceBeanName;
    }

    public void setDataSourceBeanName(String dataSourceBeanName) {
        this.dataSourceBeanName = dataSourceBeanName;
    }

    public boolean isEnableAutoCreateTokenTable() {
        return enableAutoCreateTokenTable;
    }

    public void setEnableAutoCreateTokenTable(boolean enableAutoCreateTokenTable) {
        this.enableAutoCreateTokenTable = enableAutoCreateTokenTable;
    }

    public boolean isEnableAutoCreateJsApiTable() {
        return enableAutoCreateJsApiTable;
    }

    public void setEnableAutoCreateJsApiTable(boolean enableAutoCreateJsApiTable) {
        this.enableAutoCreateJsApiTable = enableAutoCreateJsApiTable;
    }
}
