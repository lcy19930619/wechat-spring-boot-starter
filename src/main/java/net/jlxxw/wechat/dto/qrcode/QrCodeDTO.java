package net.jlxxw.wechat.dto.qrcode;

/**
 * 永久二维码创建结果
 * @author chunyang.leng
 * @date 2021-11-23 11:32 上午
 */
public class QrCodeDTO {
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
