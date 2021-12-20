package net.jlxxw.wechat.response.qrcode;

import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 永久二维码创建结果
 *
 * @author chunyang.leng
 * @date 2021-11-23 11:32 上午
 */
public class QrCodeResponse extends WeChatResponse {
    /**
     * 微信票据
     */
    private String ticket;
    /**
     * 二维码链接地址
     */
    private String url;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
