CREATE TABLE `t_app` (
  `app_id` varchar(255) NOT NULL,
  `app_secret` varchar(255) DEFAULT NULL,
  `registered_redirect_uri` varchar(255) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `register_date` datetime DEFAULT NULL,
  PRIMARY KEY (`app_id`)
);


CREATE TABLE `t_group` (
  `id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_user_id` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL
);


CREATE TABLE `t_role` (
  `id` varchar(32)  NOT NULL DEFAULT '',
  `role_name` varchar(50)  NOT NULL,
  `description` varchar(255)  DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `enable` int(11) NOT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `last_modified_user_id` varchar(255) DEFAULT NULL,
  `app_id` varchar(255)  DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `t_user` (
  `id` bigint(32) NOT NULL,
  `username` varchar(50)  NOT NULL COMMENT '用户名',
  `nickname` varchar(50)  DEFAULT NULL,
  `email` varchar(50)  DEFAULT NULL,
  `mobile` varchar(11)  DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `password` varchar(32)  NOT NULL,
  `enable` int(11) NOT NULL,
  `head_image_id` char(32)  DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `t_user_app` (
  `user_id` bigint(20) NOT NULL,
  `app_id` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`,`app_id`)
);


CREATE TABLE `t_user_group` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`)
);


CREATE TABLE `t_user_role` (
  `role_id` varchar(32)  NOT NULL DEFAULT '',
  `user_id` varchar(32)  NOT NULL DEFAULT '',
  PRIMARY KEY (`role_id`,`user_id`)
);

CREATE SEQUENCE seq_worker_node_id
     INCREMENT BY 1   -- 每次加几个
     START WITH 1     -- 从1开始计数
     NOMAXVALUE       -- 不设置最大值
     NOCYCLE          -- 一直累加，不循环
     CACHE 10;

