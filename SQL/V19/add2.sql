



ALTER TABLE `orders` ADD COLUMN `proposer` varchar(50) DEFAULT NULL COMMENT '申请人';
ALTER TABLE `orders` ADD COLUMN `dingNumber` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '钉钉单号';



