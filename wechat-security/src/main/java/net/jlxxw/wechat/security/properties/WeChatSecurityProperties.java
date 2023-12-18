package net.jlxxw.wechat.security.properties;

/**
 * 安全服务配置
 * @author chunyang.leng
 * @date 2023-12-19 04:13
 */
public class WeChatSecurityProperties {
    /**
     * 是否启用安全检查,需要调度更新服务器ip地址段
     */
    private boolean enable = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
