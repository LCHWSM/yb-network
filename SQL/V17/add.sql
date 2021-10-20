






ALTER TABLE `goods` ADD COLUMN `returnNumber` int DEFAULT NULL COMMENT '退货归还数量';
ALTER TABLE `goods` ADD COLUMN `sellNumber` int DEFAULT NULL COMMENT '转销售订单数量';
ALTER TABLE `orders` ADD COLUMN `refundAmount` decimal(10,2) DEFAULT NULL COMMENT '退款金额';
ALTER TABLE `clearinglog` ADD COLUMN `marketOrderId`varchar(50) DEFAULT NULL COMMENT '转销售订单ID';


insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (157,'订单中心-借样订单转销售','/order/turnSell',NULL,126);