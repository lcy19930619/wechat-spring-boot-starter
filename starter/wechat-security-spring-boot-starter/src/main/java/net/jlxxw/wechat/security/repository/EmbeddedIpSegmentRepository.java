package net.jlxxw.wechat.security.repository;

import net.jlxxw.wechat.repository.ip.IpSegmentRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 内嵌 ip 段存储器
 * @author chunyang.leng
 * @date 2023-12-19 03:52
 */
public class EmbeddedIpSegmentRepository implements IpSegmentRepository {
    /**
     * 存储器
     */
    private static final Set<String> STORE = new LinkedHashSet<>();

    /**
     * 新增一组微信的服务器ip段
     *
     * @param ipRangeList 微信服务器ip段
     */
    @Override
    public void add(List<String> ipRangeList) {
        STORE.addAll(ipRangeList);
    }

    /**
     * 查询全部可信任 ip 段
     *
     * @return
     */
    @Override
    public Set<String> findAll() {
        return STORE;
    }
}
