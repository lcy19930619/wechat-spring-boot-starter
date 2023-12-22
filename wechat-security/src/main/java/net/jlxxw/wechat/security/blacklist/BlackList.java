package net.jlxxw.wechat.security.blacklist;

public interface BlackList {

    /**
     * 是否在黑名单中
     * @param ip
     * @return
     */
    boolean include(String ip);

    /**
     * 加入到黑名单中
     * @param ip
     */
    void add(String ip);
}
