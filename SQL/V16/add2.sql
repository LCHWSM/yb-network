
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (143,'订单中心-新增发票附件（所有订单）','/orderInvoice/saveInvoice',NULL,126);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (144,'订单中心-修改发票附件（所有订单）','/orderInvoice/updateInvoice',NULL,126);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (145,'订单中心-取消订单与发票附件关联（所有订单）','/orderInvoice/cancelRelevance',NULL,126);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (146,'订单中心-查询未关联发票附件订单（所有订单）','/orderInvoice/findNotOrder',NULL,126);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (147,'订单中心-查询已关联发票附件订单（所有订单）','/orderInvoice/relevanceInvoiceId',NULL,126);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (148,'订单中心-订单关联发票附件（所有订单）','/orderInvoice/relevanceOrderId',NULL,126);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (149,'订单中心-删除发票附件（所有订单）','/orderInvoice/deleteInvoice',NULL,126);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (150,'供应商-新增发票附件（所有订单）','/supplierInvoice/saveInvoice',NULL,675);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (151,'供应商-修改发票附件（所有订单）','/supplierInvoice/updateInvoice',NULL,676);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (152,'供应商-取消订单与发票附件关联（所有订单）','/supplierInvoice/cancelRelevance',NULL,677);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (153,'供应商-查询未关联发票附件订单（所有订单）','/supplierInvoice/findNotOrder',NULL,678);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (154,'供应商-查询已关联发票附件订单（所有订单）','/supplierInvoice/relevanceInvoiceId',NULL,679);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (155,'供应商-订单关联发票附件（所有订单）','/supplierInvoice/relevanceOrderId',NULL,680);
insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (156,'供应商-删除发票附件（所有订单）','/supplierInvoice/deleteInvoice',NULL,681);




CREATE TABLE `orderinvoice` (
  `orderInvoiceId` int NOT NULL AUTO_INCREMENT COMMENT '发票Id',
  `orderInvoiceName` varchar(100) DEFAULT NULL COMMENT '发票名称',
  `orderInvoiceNumber` varchar(100) DEFAULT NULL COMMENT '发票编号',
  `addUser` varchar(50) DEFAULT NULL COMMENT '添加人ID',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  `updateUser` varchar(50) DEFAULT NULL COMMENT '修改人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `orderInvoiceSite` text COMMENT '发票附件路径',
  `remarks` varchar(500) DEFAULT NULL COMMENT '发票备注',
  PRIMARY KEY (`orderInvoiceId`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

CREATE TABLE `supplierinvoice` (
  `supplierInvoiceId` int NOT NULL AUTO_INCREMENT,
  `supplierInvoiceName` varchar(100) DEFAULT NULL COMMENT '供应商发票附件名字',
  `supplierInvoiceNum` varchar(100) DEFAULT NULL COMMENT '供应商发票附件编号',
  `addUser` varchar(50) DEFAULT NULL,
  `addTime` datetime DEFAULT NULL,
  `updateUser` varchar(50) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `supplierInvoiceSite` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '发票路径',
  `remarks` varchar(500) DEFAULT NULL COMMENT '发票备注',
  PRIMARY KEY (`supplierInvoiceId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;


ALTER TABLE `orders` ADD COLUMN `orderInvoiceId` int DEFAULT '0' COMMENT '发票附件ID';

ALTER TABLE `supplierorder` ADD COLUMN `supplierInvoiceId` int DEFAULT '0' COMMENT '供应商发票附件';


update orders set orderInvoiceId=0;