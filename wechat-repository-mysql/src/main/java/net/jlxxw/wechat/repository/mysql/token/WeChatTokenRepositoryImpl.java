package net.jlxxw.wechat.repository.mysql.token;

import net.jlxxw.wechat.repository.mysql.mapper.TokenMapper;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;

public class WeChatTokenRepositoryImpl implements WeChatTokenRepository {

    private final TokenMapper tokenMapper;

    public WeChatTokenRepositoryImpl(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    /**
     * 保存token
     *
     * @param token
     */
    @Override
    public void save(String token) {
        tokenMapper.insertToken(token);
    }

    /**
     * 获取保存在本地的token
     *
     * @return 保存在本地的token
     */
    @Override
    public String get() {
        return tokenMapper.getToken();
    }
}
