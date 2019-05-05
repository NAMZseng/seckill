create database seckill;
use seckill;

-- 秒杀库存表
create table seckill(
  `seckill_id` bigint not null auto_increment comment '商品库存ID',
  `name` varchar(120) not null comment '商品名称',
  `number` int not null comment '库存数量',
  `start_time` timestamp not null comment '秒杀开始时间',
  `end_time` timestamp not null comment '秒杀结束时间',
  `create_time` timestamp not null default current_timestamp comment '商品创建时间',
  primary key(seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)engine=innodb auto_increment=1000 default charset=utf8 comment'秒杀库存表';

-- 初始化数据
INSERT into seckill(name,number,start_time,end_time)
VALUES
  ('1000元秒杀iphoneX',100,'2019-05-01 00:00:00','2019-05-05 00:00:00'),
  ('800元秒杀ipad',200,'2019-05-01 00:00:00','2019-05-15 00:00:00'),
  ('6600元秒杀mac book pro',300,'2019-05-06 00:00:00','2019-05-07 00:00:00'),
  ('7000元秒杀iMac',400,'2019-05-07 00:00:00','2019-05-09 00:00:00');


-- 秒杀成功明细表
-- 用户登陆认证相关信息简化为手机号
create table success_killed(
  `seckill_id` bigint not null comment '秒杀商品ID',
  `user_phone` bigint not null comment '用户手机号',
  `state` tinyint not null default -1 comment '状态标识：-1:无效 0:成功 1:已付款 2:已发货',
  `create_time` timestamp not null default current_timestamp comment '订单创建时间',
  -- 联合主键，防止用户对同一商品重复秒杀
  primary key(seckill_id, user_phone),
  key idx_create_time(create_time)
)engine=innodb default charset=utf8 comment='秒杀成功明细表';

-- source sql文件路径名;
-- show create table seckill\G;

