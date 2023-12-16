package net.jlxxw.wechat.response.api;

import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 微信 openApi接口返回对象
 *
 * @author chunyang.leng
 * @date 2021-11-23 3:39 下午
 */
public class ApiResponse extends WeChatResponse {
    private Quota quota;

    public Quota getQuota() {
        return quota;
    }

    public void setQuota(Quota quota) {
        this.quota = quota;
    }
}
