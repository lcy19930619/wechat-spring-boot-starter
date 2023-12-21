package net.jlxxw.wechat.repository.mysql.token;

import net.jlxxw.wechat.repository.mysql.mapper.TokenMapper;
import net.jlxxw.wechat.repository.jsapi.WeChatJsApiTicketRepository;

public class WeChatJsApiTicketRepositoryImpl implements WeChatJsApiTicketRepository {

    private final TokenMapper tokenMapper;

    public WeChatJsApiTicketRepositoryImpl(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    /**
     * 存储数据
     *
     * @param jsApiTicket
     */
    @Override
    public void save(String jsApiTicket) {
        tokenMapper.insertJsApiTicket(jsApiTicket);
    }

    /**
     * 读取数据
     *
     * @return
     */
    @Override
    public String get() {
        return tokenMapper.getJsApiTicket();
    }
}
