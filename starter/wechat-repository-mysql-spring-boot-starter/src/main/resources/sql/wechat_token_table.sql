create table if not exists wechat_token
(
    id          bigint(20) auto_increment primary key comment '数据自增主键',
    token       varchar(255) not null comment '从微信获取的token',
    create_time datetime default now() comment '数据插入时间'
) charset = utf8 comment '微信 access_token 记录表'