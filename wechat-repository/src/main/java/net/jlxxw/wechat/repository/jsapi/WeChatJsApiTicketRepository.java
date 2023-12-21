package net.jlxxw.wechat.repository.jsapi;

public interface WeChatJsApiTicketRepository {

    /**
     * 存储数据
     * @param jsApiTicket
     */
    void save(String jsApiTicket);

    /**
     * 读取数据
     * @return
     */
    String get();
}
