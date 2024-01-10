create table if not exists wechat_js_api_ticket
(
    id          bigint(20) auto_increment primary key comment '数据自增主键',
    ticket      varchar(255) not null comment '从微信获取的ticket',
    create_time datetime default now() comment '数据插入时间'
) charset = utf8 comment '微信 js api ticket 记录表'