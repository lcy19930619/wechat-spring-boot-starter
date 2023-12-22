package net.jlxxw.wechat.security.repository;

import net.jlxxw.wechat.security.blacklist.BlackList;
import net.jlxxw.wechat.security.properties.BlackListProperties;

public class BlackListRepository implements BlackList {

    private BlackListProperties blackListProperties;

    public BlackListRepository(BlackListProperties blackListProperties) {
        this.blackListProperties = blackListProperties;
    }

    /**
     * 是否在黑名单中
     *
     * @param ip
     * @return
     */
    @Override
    public boolean include(String ip) {
        return false;
    }

    /**
     * 加入到黑名单中
     *
     * @param ip
     */
    @Override
    public void add(String ip) {

    }
}
