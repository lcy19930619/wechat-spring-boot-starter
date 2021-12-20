package net.jlxxw.wechat.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:19 下午
 */
@Repository
public interface TokenMapper {

    /**
     * 创建一个token管理表
     *
     * @return
     */
    @Insert("create table if not exists wei_xin_token\n" +
            "(\n" +
            "    id          bigint(20) auto_increment primary key comment '数据自增主键',\n" +
            "    token       varchar(255) not null comment '从微信获取的token',\n" +
            "    create_time      datetime default now() comment '数据插入时间'\n" +
            ")\n" +
            "charset = utf8\n"
    )
    int createTable();

    /**
     * 从数据库中获取token
     *
     * @return
     */
    @Select("select token from wei_xin_token order by id desc limit 1")
    String getToken();

    /**
     * 向数据库中存储token
     *
     * @param token
     * @return
     */
    @Insert("insert into wei_xin_token(token) values (#{token})")
    int insert(@Param("token") String token);

    /**
     * 锁表
     *
     * @return
     */
    @Select("select max(create_time) from wei_xin_token for update")
    Date lockSelectMaxDate();
}
