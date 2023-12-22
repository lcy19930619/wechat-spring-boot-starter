package net.jlxxw.wechat.repository.ip;

import java.util.List;
import java.util.Set;

/**
 *
 * @author chunyang.leng
 * @date 2021-11-23 3:22 下午
 */
public interface IpSegmentRepository {

    /**
     * 新增一组微信的服务器ip段
     *
     * @param ipRangeList 微信服务器ip段
     */
    void add(List<String> ipRangeList);

    /**
     * 查询全部可信任 ip 段
     * @return
     */
    Set<String> findAll();
}
