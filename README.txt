# 任务
create table groupTask
(
    id          int auto_increment comment '主键id'
        primary key,
    platform    varchar(20)   null comment '平台（抖音：douyin；视频号：weixin）',
    content_url varchar(1000) not null comment '群控内容url',
    user_id     varchar(50)   null comment '任务发起人id',
    user_name   varchar(20)   null comment '任务发起人名称',
    status      int(2)        null comment '状态（1：初始化，2：执行中，3：执行完成，4：执行失败）',
    create_time datetime      null comment '创建时间',
    update_time datetime      null comment '更新时间'
)
    comment '任务';


# 事件
create table groupEvent
(
    id           int auto_increment comment '主键id'
        primary key,
    event_type   int(2)        null comment '群控类型（1：点赞；2：评论；3：浏览；4：转发；5：踩）',
    task_id      int           null comment '所属任务id',
    android_id   varchar(50)   null comment '执行机器id',
    status       int(2)        null comment '状态（1：初始化；2：执行中；3：执行完成；4：执行失败）',
    comment_text varchar(1000) null comment '评论内容（评论事件有）',
    constraint groupEvent_groupTask_id_fk
        foreign key (task_id) references groupTask (id)
)
    comment '每个任务下包含的事件详情';


# 点赞记录表
create table supportRecord
(
    id          int auto_increment comment '主键id',
    android_id  varchar(30)   not null comment '点赞设备id',
    content_url varchar(1000) not null comment '点赞的视频路径',
    content_md5 varchar(32)   not null comment '视频路径的md5值',
    constraint support_record_pk
        primary key (id)
)
    comment '点赞记录表';