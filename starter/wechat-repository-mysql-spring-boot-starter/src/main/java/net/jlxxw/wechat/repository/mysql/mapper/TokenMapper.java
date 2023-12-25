package net.jlxxw.wechat.repository.mysql.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author chunyang.leng
 * @date 2021/1/19 5:19 下午
 */
@Mapper
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
    int createTokenTable();


    /**
     * 从数据库中获取token
     *
     * @return token
     */
    @Select("select token from wei_xin_token order by id desc limit 1")
    String getToken();

    /**
     * 向数据库中存储token
     *
     * @param token 从微信获取的token
     * @return
     */
    @Insert("insert into wei_xin_token(token) values (#{token})")
    int insertToken(@Param("token") String token);


    /**
     * 创建一个JsApiTicket管理表
     *
     * @return
     */
    @Insert("create table if not exists wei_xin_js_api_ticket\n" +
            "(\n" +
            "    id          bigint(20) auto_increment primary key comment '数据自增主键',\n" +
            "    ticket       varchar(255) not null comment '从微信获取的ticket',\n" +
            "    create_time      datetime default now() comment '数据插入时间'\n" +
            ")\n" +
            "charset = utf8\n"
    )
    int createJsApiTicketTable();



    /**
     * 从数据库中获取JsApiTicket
     *
     * @return JsApiTicket
     */
    @Select("select ticket from wei_xin_js_api_ticket order by id desc limit 1")
    String getJsApiTicket();

    /**
     * 向数据库中存储 JsApiTicket
     *
     * @param ticket
     * @return
     */
    @Insert("insert into wei_xin_js_api_ticket(ticket) values (#{ticket})")
    int insertJsApiTicket(@Param("ticket") String ticket);

}
