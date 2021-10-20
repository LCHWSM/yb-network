CREATE TABLE `datastatistics` (
  `dataStatisticsId` int NOT NULL AUTO_INCREMENT COMMENT '数据统计信息',
  `organizationId` varchar(50) DEFAULT NULL COMMENT '公司ID',
  `paymentMethod` varchar(50) DEFAULT NULL COMMENT '下单方式',
  `companyOrderSum` decimal(20,2) DEFAULT NULL COMMENT '下单方式订单总金额',
  `companyOrderQuit` decimal(20,2) DEFAULT NULL COMMENT '下单方式订单退款总金额',
  `companyOrderTrun` decimal(20,2) DEFAULT NULL COMMENT '下单方式订单转销售金额',
  `orderSumNumber` bigint DEFAULT NULL COMMENT '订单总数量',
  `orderQuitNumber` bigint DEFAULT NULL COMMENT '订单退款商品订单数量',
  `orderTrunNumber` bigint DEFAULT NULL COMMENT '订单转销售订单数量',
  PRIMARY KEY (`dataStatisticsId`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;



CREATE TABLE `timedatasta` (
  `timedatasta` int NOT NULL AUTO_INCREMENT COMMENT '按时间数据统计',
  `organizationId` varchar(20) DEFAULT NULL COMMENT '公司ID',
  `datatime` varchar(50) DEFAULT NULL COMMENT '时间',
  `paymentMethod` varchar(50) DEFAULT NULL COMMENT '下单方式',
  `companyOrderSum` decimal(20,2) DEFAULT NULL COMMENT '该时间总金额',
  `companyOrderQuit` decimal(20,2) DEFAULT NULL COMMENT '该时间段退款金额',
  `companyOrderTrun` decimal(20,2) DEFAULT NULL COMMENT '该时间段转销售金额',
  `orderSumNumber` bigint DEFAULT NULL COMMENT '该时间段订单总数量',
  `orderQuitNumber` bigint DEFAULT NULL COMMENT '该时间段退款总数量',
  `orderTrunNumber` bigint DEFAULT NULL COMMENT '该时间段转销售总数量',
  PRIMARY KEY (`timedatasta`)
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=utf8;

insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (159,'数据统计-查看数据统计','/dataStatistics/findAll',NULL,700);

insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (160,'商品管理-导出商品','/file/exportProduct',NULL,201);

insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (161,'订单中心-录入退款订单','/file/uploadRefund',NULL,127);
