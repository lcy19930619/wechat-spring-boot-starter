package net.jlxxw.wechat.mysql.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库 mysql 层相关适配属性
 * @author lcy
 */
@Configuration
@ConfigurationProperties("wechat.default.mysql")
public class WeChatMySqlProperties {
    /**
     * 是否启动此模块
     */
    private boolean enable = true;
    /**
     * 是否自动创建token表，默认：自动创建
     */
    private boolean enableAutoCreateTokenTable = true;
    /**
     * 是否自动创建js api 表，默认：自动创建
     */
    private boolean enableAutoCreateJsApiTable = true;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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
