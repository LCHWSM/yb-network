/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.19 : Database - data_ybnetwork3
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


/*Table structure for table `audit` */

DROP TABLE IF EXISTS `audit`;

CREATE TABLE `audit` (
  `auditId` int NOT NULL AUTO_INCREMENT COMMENT '用户审核ID',
  `auditUser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '可以审核该消息用户',
  `sponsorAuditId` int DEFAULT NULL COMMENT '发起审批信息Id',
  `auditOpinion` int DEFAULT NULL COMMENT '审核意见（4待审核，1审核通过2，审核不通过,3审核撤销，5，系统自动审核通过）',
  `auditRemarks` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核备注',
  `auditTime` datetime DEFAULT NULL COMMENT '审核时间',
  `auditflow` int DEFAULT NULL COMMENT '审核节点ID',
  `processGroupId` int DEFAULT NULL COMMENT '公司所使用流程组ID',
  `startTime` datetime DEFAULT NULL COMMENT '审核创建时间',
  PRIMARY KEY (`auditId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `audit` */

/*Table structure for table `auditflow` */

DROP TABLE IF EXISTS `auditflow`;

CREATE TABLE `auditflow` (
  `auditFlowId` int NOT NULL AUTO_INCREMENT COMMENT '审批流程ID',
  `auditFlowName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '节点名字',
  `auditFlowGrade` int DEFAULT NULL COMMENT '节点排序顺序',
  `auditFlowWay` int DEFAULT NULL COMMENT '审批方式（1:主管审核,2:为正常审核流程,3:为不需要审核）',
  `auditFlowCourse` int DEFAULT NULL COMMENT '审批过程（1:依次审批,2:会签,3:或签,4:分支判断,5:主管审核,6:分级主管审核）',
  `auditUser` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '审核人ID',
  `organizationId` int DEFAULT NULL COMMENT '公司ID',
  `addUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人ID',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  `processGroupId` int DEFAULT NULL COMMENT '所属组ID',
  PRIMARY KEY (`auditFlowId`) USING BTREE,
  KEY `fulltext_article` (`auditFlowGrade`,`processGroupId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `auditflow` */

/*Table structure for table `auditflow_grouping` */

DROP TABLE IF EXISTS `auditflow_grouping`;

CREATE TABLE `auditflow_grouping` (
  `agId` int NOT NULL AUTO_INCREMENT,
  `auditFlowId` int DEFAULT NULL COMMENT '节点ID',
  `groupingId` int DEFAULT NULL COMMENT '组ID',
  `auditUserId` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审批人Id',
  PRIMARY KEY (`agId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `auditflow_grouping` */

/*Table structure for table `auditlog` */

DROP TABLE IF EXISTS `auditlog`;

CREATE TABLE `auditlog` (
  `auditLogId` int NOT NULL AUTO_INCREMENT,
  `auditLogOrder` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核订单',
  `auditFlowName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核节点名称',
  `auditCondition` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核意见',
  `auditOpinion` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核备注',
  `auditUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核人',
  `auditLogTime` datetime DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`auditLogId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `auditlog` */

/*Table structure for table `classify` */

DROP TABLE IF EXISTS `classify`;

CREATE TABLE `classify` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分类名字',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  `addUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `updateUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
  `sort` int DEFAULT NULL COMMENT '排序字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE,
  UNIQUE KEY `name_2` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `classify` */

insert  into `classify`(`id`,`name`,`addTime`,`addUser`,`updateTime`,`updateUser`,`sort`) values (1,'纪念币','2020-08-15 11:19:38','1588931801239',NULL,NULL,1),(2,'红包','2020-08-15 11:19:48','1588931801239',NULL,NULL,3),(3,'工艺品','2020-08-15 11:19:44','1588931801239',NULL,NULL,2);

/*Table structure for table `clearinglog` */

DROP TABLE IF EXISTS `clearinglog`;

CREATE TABLE `clearinglog` (
  `clearingLogId` int NOT NULL AUTO_INCREMENT COMMENT '直接购买结算日志',
  `clearingLogName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '具体操作',
  `clearingLogUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作人ID',
  `clearingLogTime` datetime DEFAULT NULL COMMENT '操作时间',
  `clearingRemark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作备注',
  `clearingLogOrderId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作订单ID',
  `consignee` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '修改的收货人信息',
  `classify` int DEFAULT NULL COMMENT '日志分类',
  PRIMARY KEY (`clearingLogId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `clearinglog` */

/*Table structure for table `colog` */

DROP TABLE IF EXISTS `colog`;

CREATE TABLE `colog` (
  `coLogId` int NOT NULL AUTO_INCREMENT,
  `coLogName` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '处理情况',
  `coLogTime` datetime DEFAULT NULL COMMENT '处理时间',
  `operationUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作人',
  `operationTime` datetime DEFAULT NULL COMMENT '操作时间',
  `coId` int DEFAULT NULL COMMENT '定制订单ID',
  `coRemark` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '修改备注',
  `coClassify` int DEFAULT NULL COMMENT '处理分类',
  PRIMARY KEY (`coLogId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `colog` */

/*Table structure for table `company` */

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '快递公司ID',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '快递公司名字',
  `companyOnly` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '快递公司标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `company` */

insert  into `company`(`id`,`name`,`companyOnly`) values (1,'申通快递','STO'),(2,'顺丰','SF'),(3,'圆通','YTO'),(4,'中通','ZTO'),(5,'汇通快递','HTKY'),(6,'韵达','YUNDA'),(7,'京东','JD'),(8,'EMS','EMS'),(9,'中通快运','ZTO56'),(10,'安能快递','ANEEX'),(11,'百世快递','HTKY'),(12,'百世快运','BSKY'),(13,'天天','TTKDEX'),(14,'优速','UC56'),(15,'邮政包裹','CHINAPOST'),(16,'中铁快运','CRE'),(17,'中铁物流','ZTKY'),(18,'品骏快递','品骏快递'),(19,'邮政国际包裹','INTMAIL'),(20,'联邦快递','FEDEX');

/*Table structure for table `contract` */

DROP TABLE IF EXISTS `contract`;

CREATE TABLE `contract` (
  `contractId` int NOT NULL AUTO_INCREMENT COMMENT '合同ID',
  `contractName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '合同名字',
  `contractNumber` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '合同编号',
  `addUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '合同上传人',
  `addTime` datetime DEFAULT NULL COMMENT '合同上传时间',
  `contractSite` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '合同路径',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '合同备注',
  `updateUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `organizationId` int DEFAULT NULL COMMENT '公司ID',
  PRIMARY KEY (`contractId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `contract` */

/*Table structure for table `customizedorder` */

DROP TABLE IF EXISTS `customizedorder`;

CREATE TABLE `customizedorder` (
  `coId` int NOT NULL AUTO_INCREMENT,
  `coName` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
  `coPhone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
  `coEmail` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电子邮箱',
  `coDemand` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '定制需求',
  `coAddTime` datetime DEFAULT NULL COMMENT '创建时间',
  `coFlag` int DEFAULT '1' COMMENT '处理状态（1待处理，2处理中，3已完成，4已取消）',
  `updateUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最后修改人',
  `updateTime` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`coId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `customizedorder` */

/*Table structure for table `department` */

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `departmentId` int NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `departmentName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '部门名字',
  `organizationId` int DEFAULT NULL COMMENT '所属公司',
  `departmentNumber` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '部门编号',
  `addUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人ID',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  `updateUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人ID',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`departmentId`) USING BTREE,
  UNIQUE KEY `agd` (`organizationId`,`departmentNumber`) USING BTREE,
  UNIQUE KEY `od` (`organizationId`,`departmentName`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `department` */

insert  into `department`(`departmentId`,`departmentName`,`organizationId`,`departmentNumber`,`addUser`,`addTime`,`updateUser`,`updateTime`) values (3,'金予',2,'JINYU',NULL,NULL,'1600917384877','2020-10-29 15:18:50');

/*Table structure for table `express` */

DROP TABLE IF EXISTS `express`;

CREATE TABLE `express` (
  `expressId` int NOT NULL AUTO_INCREMENT COMMENT '发货ID',
  `expressSign` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '快递公司标识',
  `expressNumbers` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '快递单号',
  `orderId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单ID',
  `successTime` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发货成功时间',
  `expressName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '快递公司名字',
  `practicalTime` datetime DEFAULT NULL COMMENT '借样订单实际归还时间',
  `retreatName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '退货公司名字',
  `retreatNumbers` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '退货单号',
  `retreatTime` datetime DEFAULT NULL COMMENT '退货时间',
  PRIMARY KEY (`expressId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `express` */

/*Table structure for table `expresstest` */

DROP TABLE IF EXISTS `expresstest`;

CREATE TABLE `expresstest` (
  `expressId` int NOT NULL AUTO_INCREMENT,
  `expressName` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `expressNumber` varbinary(20) DEFAULT NULL,
  `orderId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`expressId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `expresstest` */

/*Table structure for table `goods` */

DROP TABLE IF EXISTS `goods`;

CREATE TABLE `goods` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品编号ID',
  `goodsId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品ID',
  `goodsName` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名字',
  `goodsPrice` decimal(8,2) DEFAULT NULL COMMENT '商品价格',
  `goodsNumber` int DEFAULT NULL COMMENT '商品数量',
  `goodsCore` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品编码',
  `goodsImages` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品主图',
  `goodsDescription` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '商品详情页',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `goods` */

/*Table structure for table `goodstest` */

DROP TABLE IF EXISTS `goodstest`;

CREATE TABLE `goodstest` (
  `goodsId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `goodsName` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `goodsPrice` double(6,2) DEFAULT NULL,
  `goodsNumber` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `goodstest` */

/*Table structure for table `grouping` */

DROP TABLE IF EXISTS `grouping`;

CREATE TABLE `grouping` (
  `groupingId` int NOT NULL AUTO_INCREMENT COMMENT '分组ID',
  `groupingName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分组名字',
  `departmentId` int DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`groupingId`) USING BTREE,
  UNIQUE KEY `gd` (`groupingName`,`departmentId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `grouping` */

insert  into `grouping`(`groupingId`,`groupingName`,`departmentId`) values (7,'金予',3);

/*Table structure for table `invoice` */

DROP TABLE IF EXISTS `invoice`;

CREATE TABLE `invoice` (
  `invoiceId` int NOT NULL AUTO_INCREMENT COMMENT '发票信息ID',
  `unitName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '单位名称',
  `invoiceNumber` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '税号',
  `unitSite` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '单位地址',
  `unitPhone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系电话',
  `bankDeposit` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '开户行',
  `bankAccount` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '银行账号',
  `orderId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单Id',
  `flag` int DEFAULT NULL COMMENT '标记(1:个人2：公司)',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `invoiceType` int DEFAULT NULL COMMENT '标记什么类型发票（1：普票2：专票）',
  `ticketSite` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收票地址',
  PRIMARY KEY (`invoiceId`) USING BTREE,
  KEY `orderId` (`orderId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `invoice` */

/*Table structure for table `invoicerecord` */

DROP TABLE IF EXISTS `invoicerecord`;

CREATE TABLE `invoicerecord` (
  `invoicerecordId` int NOT NULL AUTO_INCREMENT COMMENT '发票记录ID',
  `unitName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '单位名称',
  `invoiceNumber` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '税号',
  `unitSite` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '单位地址',
  `unitPhone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系电话',
  `bankDeposit` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '开户行',
  `bankAccount` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '银行账号',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `userId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户ID',
  `ticketSite` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收票地址',
  `flag` int DEFAULT NULL COMMENT '标记(1:个人2：公司)',
  `invoiceType` int DEFAULT NULL COMMENT '标记什么类型发票（1：普票2：专票）',
  PRIMARY KEY (`invoicerecordId`) USING BTREE,
  KEY `userId` (`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `invoicerecord` */

/*Table structure for table `log` */

DROP TABLE IF EXISTS `log`;

CREATE TABLE `log` (
  `logId` int NOT NULL AUTO_INCREMENT,
  `logName` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '具体操作',
  `logUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '具体操作',
  `logTime` datetime DEFAULT NULL COMMENT '操作时间',
  `logRemarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '具体操作',
  `logClassify` int DEFAULT NULL COMMENT '日志分类',
  `logSubject` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '具体操作',
  `logCore` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品编码',
  `logOrderId` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '操作订单ID',
  PRIMARY KEY (`logId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `log` */

/*Table structure for table `nodedraft` */

DROP TABLE IF EXISTS `nodedraft`;

CREATE TABLE `nodedraft` (
  `nodeDraftId` int NOT NULL AUTO_INCREMENT COMMENT '节点草稿ID',
  `nodeDraftName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '节点草稿名字',
  `nodeDraftGrade` int DEFAULT NULL COMMENT '节点草稿排序顺序',
  `nodeDraftWay` int DEFAULT NULL COMMENT '审批方式（1:主管审核,2:为正常审核流程,3:为不需要审核）',
  `nodeDraftCourse` int DEFAULT NULL COMMENT '审批过程（1:依次审批,2:会签,3:或签,4:分支判断,5:主管审核,6:分级主管审核）',
  `nodeDraftUser` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '审核人ID',
  `organizationId` int DEFAULT NULL COMMENT '所属公司ID',
  `addUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`nodeDraftId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `nodedraft` */

/*Table structure for table `nodedraft_grouping` */

DROP TABLE IF EXISTS `nodedraft_grouping`;

CREATE TABLE `nodedraft_grouping` (
  `ngid` int NOT NULL AUTO_INCREMENT,
  `nodeDraftId` int DEFAULT NULL COMMENT '节点ID',
  `groupingId` int DEFAULT NULL COMMENT '组ID',
  `nodeDraftUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核人ID',
  PRIMARY KEY (`ngid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `nodedraft_grouping` */

/*Table structure for table `order_goods` */

DROP TABLE IF EXISTS `order_goods`;

CREATE TABLE `order_goods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `orderId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `goodsId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_name` (`orderId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `order_goods` */

/*Table structure for table `order_goodstest` */

DROP TABLE IF EXISTS `order_goodstest`;

CREATE TABLE `order_goodstest` (
  `id` int NOT NULL AUTO_INCREMENT,
  `orderId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `goodsId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `order_goodstest` */

/*Table structure for table `orders` */

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `orderId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单ID',
  `orderTime` datetime DEFAULT NULL COMMENT '订单时间',
  `customerName` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
  `customerMobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
  `receiverName` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人姓名',
  `receiverMobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人手机号',
  `receiverProvince` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人地址（省）',
  `receiverCity` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人地址（市）',
  `receiverDistrict` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人地址（县）',
  `receiverStreet` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人地址（区）',
  `receiverAddress` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人地址（详情地址）',
  `receiverPostcode` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮编',
  `expressOnly` int DEFAULT '0' COMMENT '发货标识',
  `site` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收件人地址汇总',
  `addUser` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单创建人',
  `addCompany` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '下单公司id',
  `audit` int DEFAULT NULL COMMENT '审核状态（2审核不通过  1审核通过   3 审核中 4 审核撤销，5，订单已取消）',
  `sumMoney` decimal(10,2) DEFAULT NULL COMMENT '订单总金额',
  `contractId` int DEFAULT '0' COMMENT '合同ID',
  `paymentStatus` int DEFAULT NULL COMMENT '1：未付款，2：已付款，3：紧急发货，4：未归还，5：已归还，6：退款中',
  `paymentMethod` int DEFAULT NULL COMMENT '下单方式1额度下单2借样3直接购买',
  `returnTime` datetime DEFAULT NULL COMMENT '借样预计归还时间',
  `actualMoney` decimal(10,2) DEFAULT NULL COMMENT '订单实收金额',
  `freight` decimal(5,2) DEFAULT '0.00' COMMENT '订单运费',
  `retreatCargo` int DEFAULT '0' COMMENT '退货标识（1，退货中2，已退货）',
  `orderRemark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单备注',
  `settlementAmount` decimal(10,2) DEFAULT '0.00' COMMENT '已结算金额',
  `orderSign` int DEFAULT NULL COMMENT '为1时为机器销售信息',
  `serveCost` decimal(10,2) DEFAULT NULL COMMENT '服务费',
  PRIMARY KEY (`orderId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `orders` */

/*Table structure for table `orderstest` */

DROP TABLE IF EXISTS `orderstest`;

CREATE TABLE `orderstest` (
  `orderId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `orderTime` datetime DEFAULT NULL,
  `customerName` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `customerMobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `receiverName` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `receiverMobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `receiverProvince` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `receiverCity` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `receiverDistrict` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `receiverStreet` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `receiverAddress` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `receiverPostcode` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `expressOnly` int DEFAULT '0',
  PRIMARY KEY (`orderId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `orderstest` */

/*Table structure for table `organization` */

DROP TABLE IF EXISTS `organization`;

CREATE TABLE `organization` (
  `organizationId` int NOT NULL AUTO_INCREMENT,
  `organizationName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '机构名字',
  `organizationLimit` decimal(15,2) DEFAULT '0.00' COMMENT '机构额度',
  `useBalance` decimal(15,2) DEFAULT '0.00' COMMENT '已用额度',
  `availableBalance` decimal(15,2) DEFAULT '0.00' COMMENT '可用额度',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  `addUser` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人',
  `updateTime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `updateUser` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最后修改人',
  `processGroupId` int DEFAULT NULL COMMENT '标记该公司目前使用的流程组',
  `settingTime` int DEFAULT NULL COMMENT '设定该公司借样预期归还时间',
  `way` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '设定该公司的下单方式',
  PRIMARY KEY (`organizationId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `organization` */

insert  into `organization`(`organizationId`,`organizationName`,`organizationLimit`,`useBalance`,`availableBalance`,`addTime`,`addUser`,`updateTime`,`updateUser`,`processGroupId`,`settingTime`,`way`) values (2,'金予','1.00','0.00','1.00','2020-10-29 15:15:49','1600917384877','2020-10-29 16:31:30','1588931801239',27888989,30,'23');

/*Table structure for table `organization_supplier` */

DROP TABLE IF EXISTS `organization_supplier`;

CREATE TABLE `organization_supplier` (
  `osId` int NOT NULL AUTO_INCREMENT,
  `organizationId` int DEFAULT NULL COMMENT '公司ID',
  `supplierId` int DEFAULT NULL COMMENT '供应商ID',
  PRIMARY KEY (`osId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `organization_supplier` */

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限名字',
  `url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限路径',
  `buttonName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '按键名字',
  `sequence` int DEFAULT NULL COMMENT '排序顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `permission` */

insert  into `permission`(`id`,`name`,`url`,`buttonName`,`sequence`) values (1,'订单中心-查询所有订单','/order/findAll','findAllOrder',102),(2,'用户管理-创建用户','/main/saveUser','saveUser',130),(3,'用户管理-查询用户','/main/findByUser','findUser',131),(6,'订单中心-拉取订单','/order/findOrderByDate','findOrderByDate',113),(7,'订单中心-发货','/express/saveByExpress','savaByExpress',120),(8,'用户管理-修改用户','/main/updateUser','updateUser',133),(9,'用户管理-查询用户已拥有角色','/main/findByRole','findRole',132),(10,'用户管理-用户角色修改','/role/updateRoleByUId','updateRoleByUid',133),(11,'角色管理-删除角色','/role/deleteRoleById','deleteRoleById',137),(12,'角色管理-查询角色','/role/findAll','findAllRole',135),(13,'角色管理-添加角色','/role/saveRole','saveRole',134),(14,'角色管理-修改角色','/role/updateRole','updateRole',136),(15,'商品管理-新增商品','/product/saveProduct','saveProduct',201),(16,'商品管理-删除商品','/product/deleteProduct','deleteProduct',204),(17,'商品管理-修改商品','/product/updateProduct','updateProduct',203),(18,'商品管理-查看商品','/product/findAll','findAllProduct',202),(20,'商品分类管理-新增商品分类','/classify/saveClassify','saveClassify',205),(21,'商品分类管理-删除商品分类','/classify/deleteById','deleteById',207),(22,'商品分类管理-修改商品分类','/classify/updateClassify','updateClassify',206),(23,'公司管理-查询公司','/organization/findAll','findAllOrganization',302),(24,'公司管理-新增公司','/organization/saveOrganization','saveOrganization',301),(25,'公司管理-删除公司','/organization/deleteById','deleteOrganization',304),(26,'公司管理-修改公司','/organization/updateById','updateOrganization',303),(27,'库存管理-更新库存','/product/updateWarehouse','updateWarehouse',209),(28,'公司额度管理-额度调整','/organization/updateLimit','updateLimit',305),(29,'查看日志','/log/findAll','findAllLog',401),(30,'库存管理-修改通用库存预警','/universal/updateWarning','updateWarning',210),(32,'公司额度管理-核销额度','/organization/updateUsable','updateUsable',307),(33,'订单中心-下单','/order/saveOrder','saveOrder',101),(49,'订单中心-修改发货信息','/express/updateExpress',NULL,121),(50,'合同管理-查看合同列表','/contract/findAll',NULL,331),(51,'合同管理-上传合同信息','saveContract',NULL,330),(52,'合同管理-删除合同信息','/contract/deleteContract',NULL,333),(53,'订单中心-查询已审核待发货订单','/order/findByAudit',NULL,103),(54,'合同管理-订单关联合同','/contract/saveContractId',NULL,334),(55,'合同管理-修改合同信息','/contract/updateContract',NULL,332),(56,'合同管理-取消订单与合同关联','/contract/updateConByOrderId',NULL,335),(57,'合同管理-查询合同已关联订单','/order/findByOrder',NULL,336),(58,'商品分类管理-更改分类排序顺序','/classify/updateSort',NULL,208),(59,'公司额度管理-查询待结算订单（额度购买）','/order/findByUnpaid',NULL,105),(60,'公司额度管理-校验已使用额度','/organization/verifyLimit',NULL,306),(61,'订单中心-直接购买订单结算','/organization/paymentStatus',NULL,119),(64,'本公司借样订单待归还提醒','returnRemind',NULL,482),(65,'本公司额度不足提醒','limitRemind',NULL,481),(67,'公司管理-新增公司部门','/department/saveDepartment',NULL,313),(68,'公司管理-查询公司部门','/department/findByOId',NULL,314),(69,'公司管理-修改公司部门','/department/updateDepartment',NULL,315),(70,'订单中心-查询待结算订单（直接购买）','/order/findOrder',NULL,108),(71,'公司管理-删除公司部门','/department/deleteDepartment',NULL,316),(72,'公司管理-为部门创建分组','/grouping/saveGrouping',NULL,317),(73,'公司管理-修改分组信息','/grouping/updateGrouping',NULL,318),(74,'公司管理-删除分组信息','/grouping/deleteGrouping',NULL,319),(75,'流程管理-发布审核流程','/auditFlow/saveAuditFlow',NULL,501),(77,'流程管理-新增审批节点草稿','/nodeDraft/saveNodeDraft',NULL,503),(78,'流程管理-查询审批节点草稿','/nodeDraft/findAll',NULL,504),(79,'流程管理-修改审批节点草稿','/nodeDraft/updateNodeDraft',NULL,505),(80,'流程管理-删除审批节点草稿','/nodeDraft/deleteByNodeDraftId',NULL,506),(81,'流程管理-查询所有流程组','/processGroup/findAll',NULL,507),(82,'流程管理-切换公司使用流程','/processGroup/saveOrganization',NULL,508),(83,'订单中心-审核','/audit/auditResult',NULL,509),(84,'订单中心-取消订单','/order/cancelOrder',NULL,114),(85,'订单中心-查询已取消订单','/order/findCancelOrder',NULL,109),(86,'订单中心-查询本公司订单','/order/findCompanyOrder',NULL,110),(87,'订单中心-查询已发货订单','/order/findShipments',NULL,111),(88,'订单中心-查询借样订单','/order/findBorrow',NULL,112),(89,'订单中心-完成退款','/order/refund',NULL,113),(90,'订单中心-修改订单信息','/order/updateOrder',NULL,114),(91,'商品分类管理-查询所有分类','/classify/findClassify',NULL,205),(92,'库存管理-查询商品库存','/product/findInventory',NULL,209),(93,'订单中心-填写退货信息','/express/refundCargo',NULL,116),(94,'订单中心-修改退货信息','/express/updateRefundCargo',NULL,117),(95,'订单中心-完成退货','/express/refundCargoSucceed',NULL,118),(96,'定制订单-查询定制订单','/customizedOrder/findAll',NULL,601),(97,'定制订单-反馈客户记录','/customizedOrder/updateFlag',NULL,602),(98,'定制订单-内部处理记录','/log/saveCoLog',NULL,603),(99,'定制订单-定制订单修改','/customizedOrder/updateOrder',NULL,604),(101,'流程管理-修改流程组名字','/processGroup/updateName',NULL,508),(102,'公司额度管理-查询公司额度','/organization/findLimit',NULL,309),(103,'供应商-新增供应商信息','/supplier/saveSupplier',NULL,650),(104,'供应商-查询供应商信息','/supplier/findAll',NULL,651),(105,'供应商-修改供应商信息','/supplier/updateSupplier',NULL,652),(106,'供应商用户-新增供应商用户','/supplierUser/saveSupplierUser',NULL,653),(107,'供应商用户-查询供应商用户','/supplierUser/supplierByUser',NULL,654),(108,'供应商用户-修改供应商用户','/supplierUser/updateSByUser',NULL,655),(109,'供应商用户-查看用户拥有角色','/supplierUser/findByRole',NULL,656),(110,'供应商-删除供应商信息','/supplier/deleteById',NULL,657),(111,'供应商用户-修改用户角色','/supplierUser/updateUidByRid',NULL,658),(112,'供应商-向供应商下单','/supplierOrder/saveOrder',NULL,659),(113,'供应商-我下的供应商订单','/supplierOrder/findUIdOrder',NULL,660),(114,'供应商-修改供应商订单','/supplierOrder/updateOrderById',NULL,661),(115,'供应商-向我下的供应商订单','/supplierOrder/findBySupplier',NULL,662),(116,'供应商-修改订单报价','/supplierOrder/updateQuotation',NULL,663),(117,'供应商-修改锁定报价状态','/supplierOrder/lockQuotation',NULL,664),(118,'供应商-修改供应商反馈','/supplierOrder/updateDispose',NULL,665),(119,'供应商-结算本人订单','/supplierOrder/closeSum',NULL,666),(121,'供应商-查询本公司内订单','/supplierOrder/findByOrganId',NULL,668),(122,'订单中心-常规订单','generalOrder',NULL,122),(123,'供应商-公司订单结算','/supplierOrder/companyCloseSum',NULL,666),(124,'公司管理-配置可下单的供应商','/organization/saveSupplier',NULL,310),(125,'供应商-查询所有供应商订单','/supplierOrder/findAll',NULL,667),(126,'订单中心-导入订单','/file/doImportExcel',NULL,121),(128,'订单中心-删除订单','/order/deleteOrder',NULL,123),(129,'订单中心-查看本人导入记录','/log/findLeadOrder',NULL,123),(130,'订单中心-查看所有导入记录','/log/findLeadOrderAll',NULL,123),(131,'订单中心-删除导入订单','/order/deleteLeadOrder',NULL,124),(132,'订单中心-导出订单','/file/complete',NULL,125),(133,'下单-更改商品价格','updatePrice',NULL,102),(134,'订单中心-外部用户下单显示提示','externalShow',NULL,102),(135,'订单中心-外部用户下单不显示提示','externalNoShow',NULL,102),(136,'供应商-新增合同（所有订单）','/supplierContract/saveContract',NULL,668),(137,'供应商-删除合同（所有订单）','/supplierContract/deleteContract',NULL,669),(138,'供应商-修改合同（所有订单）','/supplierContract/updateContract',NULL,670),(139,'供应商-查询未关联合同订单（所有订单）','/supplierContract/findSROrderAll',NULL,671),(140,'供应商-合同关联订单（所有订单）','/supplierContract/relevanceSRAll',NULL,672),(141,'供应商-解除合同与订单关联（所有订单）','/supplierContract/updateSOId',NULL,673),(142,'供应商-查询合同相关联的订单（所有订单）','/supplierContract/findByCId',NULL,674);

/*Table structure for table `processgroup` */

DROP TABLE IF EXISTS `processgroup`;

CREATE TABLE `processgroup` (
  `processGroupId` int NOT NULL COMMENT '流程组ID',
  `processGroupName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '流程组名字',
  `processGroupUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发布人Id',
  `processGroupTime` datetime DEFAULT NULL COMMENT '发布时间',
  `organizationId` int DEFAULT NULL COMMENT '所属公司ID',
  PRIMARY KEY (`processGroupId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `processgroup` */

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名字',
  `core` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品编码',
  `price` decimal(10,2) NOT NULL COMMENT '销售价格',
  `cost` decimal(10,2) DEFAULT NULL COMMENT '成本价格',
  `param` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '类型',
  `status` int DEFAULT '0' COMMENT '是否启用',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '详情',
  `images` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品主图',
  `addTime` datetime DEFAULT NULL COMMENT '创建时间',
  `addUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `updateUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
  `classify_id` int DEFAULT NULL COMMENT '分类ID',
  `warehouse` int DEFAULT '0' COMMENT '商品库存',
  `red` int DEFAULT NULL COMMENT '库存红色预警',
  `yellow` int DEFAULT NULL COMMENT '库存黄色预警',
  `universal` int DEFAULT '1' COMMENT '是否使用通用属性',
  UNIQUE KEY `name` (`name`) USING BTREE,
  UNIQUE KEY `core` (`core`) USING BTREE,
  KEY `Id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=503 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `product` */

insert  into `product`(`id`,`name`,`core`,`price`,`cost`,`param`,`status`,`description`,`images`,`addTime`,`addUser`,`updateTime`,`updateUser`,`classify_id`,`warehouse`,`red`,`yellow`,`universal`) values (319,'2008鼠年贺岁纪念币','G0003','158.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/3/10.jpg\" style=\"max-width:100%;\"><br></p>','image/3/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(320,'2009牛年贺岁纪念币','G0004','68.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/4/10.jpg\" style=\"max-width:100%;\"><br></p>','image/4/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(321,'2010虎年贺岁纪念币','G0005','62.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/5/10.jpg\" style=\"max-width:100%;\"><br></p>','image/5/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9999,NULL,NULL,1),(322,'2011兔年贺岁纪念币','G0006','66.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/6/10.jpg\" style=\"max-width:100%;\"><br></p>','image/6/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9999,NULL,NULL,1),(323,'2012龙年贺岁纪念币','G0007','50.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/7/10.jpg\" style=\"max-width:100%;\"><br></p>','image/7/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9999,NULL,NULL,1),(324,'2013蛇年贺岁纪念币','G0008','48.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/8/10.jpg\" style=\"max-width:100%;\"><br></p>','image/8/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9999,NULL,NULL,1),(325,'2014马年贺岁纪念币','G0009','48.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/9/10.jpg\" style=\"max-width:100%;\"><br></p>','image/9/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9999,NULL,NULL,1),(326,'2015年熊猫银币','G00010','238.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/10/10.jpg\" style=\"max-width:100%;\"><br></p>','image/10/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(327,'2015羊年贺岁纪念币','G00011','148.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/11/10.jpg\" style=\"max-width:100%;\"><br></p>','image/11/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(328,'2016年熊猫银币','G00012','238.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/12/10.jpg\" style=\"max-width:100%;\"><br></p>','image/12/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(329,'2016猴年贺岁纪念币','G00013','58.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/13/10.jpg\" style=\"max-width:100%;\"><br></p>','image/13/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(330,'2017年熊猫银币','G00014','238.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/14/10.jpg\" style=\"max-width:100%;\"><br></p>','image/14/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(331,'2017鸡年贺岁纪念币','G00015','58.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/15/10.jpg\" style=\"max-width:100%;\"><br></p>','image/15/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9997,NULL,NULL,1),(332,'2018年3元硬币','G00016','196.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/16/1.jpg\" style=\"max-width:100%;\"><br></p>','image/16/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:32:36','1588931801239',1,10000,NULL,NULL,1),(333,'2018年熊猫银币','G00017','238.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/17/10.jpg\" style=\"max-width:100%;\"><br></p>','image/17/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(334,'2018狗年贺岁纪念币','G00018','58.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/18/10.jpg\" style=\"max-width:100%;\"><br></p>','image/18/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(335,'2019年3元贺岁币','G00019','208.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/19/1.jpg\" style=\"max-width:100%;\"><br></p>','image/19/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:31:58','1588931801239',1,10000,NULL,NULL,1),(336,'2019年熊猫银币','G00020','238.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/20/10.jpg\" style=\"max-width:100%;\"><br></p>','image/20/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(337,'2020年熊猫银币','G00021','238.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/21/10.jpg\" style=\"max-width:100%;\"><br></p>','image/21/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9988,NULL,NULL,1),(476,'2020年贺岁银质纪念币（8克）','G00166','198.00',NULL,NULL,1,'<p><br></p>','2020/10/19/f57cb6ac-5628-4f14-a2a9-ce2e98c7c40e.jpg','2020-10-19 18:51:00','1597456924771',NULL,NULL,1,99,NULL,NULL,1),(479,'2020款BTV手工景泰蓝春碗165克','G00171','9800.00',NULL,NULL,1,'<p><br></p>','2020/10/19/bcf9bb35-6298-4f31-8707-9abb2bcfb812.JPG','2020-10-19 19:02:19','1597456924771',NULL,NULL,3,0,NULL,NULL,1),(478,'2020款BTV手工花丝景泰蓝春碗190克','G00170','29800.00',NULL,NULL,1,'<p><br></p>','2020/10/19/a2ee0a1c-e536-4f63-9e0f-a563e71de05a.JPG','2020-10-19 19:00:26','1597456924771',NULL,NULL,3,0,NULL,NULL,1),(338,'2020生肖鼠流通纪念币','G00022','38.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/22/10.jpg\" style=\"max-width:100%;\"><br></p>','image/22/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9976,NULL,NULL,1),(477,'2020鼠年流通纪念币康银阁卡币','G00169','33.00',NULL,NULL,1,'<p><br></p>','2020/10/21/50808082-74b6-4102-9a70-5fe158dac955.png','2020-10-19 18:58:10','1597456924771','2020-10-21 14:57:39','1597456924771',1,0,NULL,NULL,1),(339,'2020鼠年贺岁金条1克','G00023','598.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/23/10.jpg\" style=\"max-width:100%;\"><br></p>','image/23/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(502,'2021牛年贺岁足银台历','G00184','399.00',NULL,NULL,1,'<p><br></p>','2020/11/12/8cbc05e0-b428-4888-9f70-e3b13dbd3237.png','2020-11-12 17:02:15','1597456924771',NULL,NULL,3,0,NULL,NULL,1),(340,'BTV手工景泰蓝春碗','G00024','9800.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/24/10.jpg\" style=\"max-width:100%;\"><br></p>','image/24/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(341,'BTV黄金红包—鼠你最幸福','G00025','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/25/10.jpg\" style=\"max-width:100%;\"><br></p>','image/25/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:33:07','1588931801239',2,10000,NULL,NULL,1),(499,'《匙来运转》2020年六福银套装','G00181','2088.00',NULL,NULL,1,'<p><br></p>','2020/11/11/5e063ae5-8fc5-4625-832d-198e8f76ece3.png','2020-11-11 11:30:56','1597456850777',NULL,NULL,3,47,NULL,NULL,1),(342,'一筒财富百国硬币','G00026','2380.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/26/10.jpg\" style=\"max-width:100%;\"><br></p>','image/26/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:32:08','1588931801239',1,10000,NULL,NULL,1),(483,'世界文化遗产·紫禁城600周年法国纪念币100枚套装100克银1克金','G00161','9900.00',NULL,NULL,1,'<p><br></p>','2020/10/22/418f95ed-fa9a-496c-8c35-e002234e7f60.jpg','2020-10-22 09:49:56','1597456850777',NULL,NULL,1,0,NULL,NULL,1),(482,'世界文化遗产·紫禁城600周年法国纪念币20枚套装20克银0.2克金','G00160','2380.00',NULL,NULL,1,'<p><br></p>','2020/10/21/b0702408-03ed-4d74-9f9b-6ae77e03f3aa.png','2020-10-21 09:27:17','1597456850777','2020-10-21 09:27:50','1597456850777',1,967,NULL,NULL,1),(475,'世界文化遗产·紫禁城600周年法国纪念币套装1克银','G000159','128.00',NULL,NULL,1,'<p><br></p>','2020/10/19/db1c65e5-3243-4689-ac92-41a7cfc1fa1a.jpg','2020-10-19 15:55:26','1597456850777','2020-10-21 09:26:04','1597456850777',1,563,NULL,NULL,1),(480,'世界文化遗产流通纪念币10枚套装（方圆封装版）','G00173','888.00',NULL,NULL,1,'<p><br></p>','2020/10/21/0fc59e1d-bdf0-4914-8848-e1d752bea735.png','2020-10-19 19:05:06','1597456924771','2020-10-21 14:52:00','1597456924771',1,50,NULL,NULL,1),(343,'世界文化遗产紫禁城纪念币','G00027','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/27/10.jpg\" style=\"max-width:100%;\"><br></p>','image/27/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9962,NULL,NULL,1),(497,'中国珍稀野生动物系列纪念币（方圆封装版）','G00179','688.00',NULL,NULL,1,'<p><br></p>','2020/11/10/9867866e-c432-4e22-b38a-225d5dcde6cd.png','2020-11-10 15:44:56','1597456850777',NULL,NULL,1,49,NULL,NULL,1),(344,'中国纪念币珍藏典藏版','G00028','129800.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/28/10.jpg\" style=\"max-width:100%;\"><br></p>','image/28/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:32:17','1588931801239',1,10000,NULL,NULL,1),(345,'中国高铁流通纪念币','G00029','58.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/29/10.jpg\" style=\"max-width:100%;\"><br></p>','image/29/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:32:22','1588931801239',1,9999,NULL,NULL,1),(346,'伟人胸章','G00030','1580.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/30/10.jpg\" style=\"max-width:100%;\"><br></p>','image/30/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(347,'党员徽章','G00031','2580.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/31/10.jpg\" style=\"max-width:100%;\"><br></p>','image/31/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(481,'八大伟人流通纪念币套装（方圆封装版）','G00175','688.00',NULL,NULL,1,'<p><br></p>','2020/10/21/c6770b6a-d6a0-452b-81e6-7ddf647471f2.png','2020-10-19 19:10:32','1597456924771','2020-10-21 14:57:07','1597456924771',1,1,NULL,NULL,1),(348,'六字箴言石榴石','G00032','410.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/32/10.jpg\" style=\"max-width:100%;\"><br></p>','image/32/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(350,'十二生肖纪念币套装','G00034','1100.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/34/10.jpg\" style=\"max-width:100%;\"><br></p>','image/34/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9999,NULL,NULL,1),(351,'十二生肖黄金红包兔','G00035','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/35/10.jpg\" style=\"max-width:100%;\"><br></p>','image/35/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9988,NULL,NULL,1),(352,'十二生肖黄金红包牛','G00036','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/36/10.jpg\" style=\"max-width:100%;\"><br></p>','image/36/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9975,NULL,NULL,1),(353,'十二生肖黄金红包狗','G00037','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/37/10.jpg\" style=\"max-width:100%;\"><br></p>','image/37/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9990,NULL,NULL,1),(354,'十二生肖黄金红包猪','G00038','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/38/10.jpg\" style=\"max-width:100%;\"><br></p>','image/38/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9974,NULL,NULL,1),(355,'十二生肖黄金红包猴','G00039','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/39/10.jpg\" style=\"max-width:100%;\"><br></p>','image/39/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9979,NULL,NULL,1),(356,'十二生肖黄金红包羊','G00040','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/40/10.jpg\" style=\"max-width:100%;\"><br></p>','image/40/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9996,NULL,NULL,1),(357,'十二生肖黄金红包虎','G00041','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/41/10.jpg\" style=\"max-width:100%;\"><br></p>','image/41/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9985,NULL,NULL,1),(358,'十二生肖黄金红包蛇','G00042','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/42/10.jpg\" style=\"max-width:100%;\"><br></p>','image/42/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9986,NULL,NULL,1),(359,'十二生肖黄金红包马','G00043','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/43/10.jpg\" style=\"max-width:100%;\"><br></p>','image/43/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9983,NULL,NULL,1),(360,'十二生肖黄金红包鸡','G00044','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/44/10.jpg\" style=\"max-width:100%;\"><br></p>','image/44/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9985,NULL,NULL,1),(361,'十二生肖黄金红包鼠','G00045','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/45/10.jpg\" style=\"max-width:100%;\"><br></p>','image/45/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9962,NULL,NULL,1),(362,'十二生肖黄金红包龙','G00046','128.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/46/10.jpg\" style=\"max-width:100%;\"><br></p>','image/46/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9988,NULL,NULL,1),(363,'南瓜车红绳','G00047','360.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/47/1.jpg\" style=\"max-width:100%;\"><br></p>','image/47/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(364,'吉祥如意金手串','G00048','780.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/48/10.jpg\" style=\"max-width:100%;\"><br></p>','image/48/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(365,'和字书法纪念币套装','G00049','448.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/49/10.jpg\" style=\"max-width:100%;\"><br></p>','image/49/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9999,NULL,NULL,1),(366,'喜事连连','G00050','0.20',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/50/1.jpg\" style=\"max-width:100%;\"><br></p>','image/50/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(367,'国泰民安纪念币套装','G00051','158.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/51/10.jpg\" style=\"max-width:100%;\"><br></p>','image/51/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9990,NULL,NULL,1),(368,'大玫瑰花红玛瑙','G00052','398.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/52/10.jpg\" style=\"max-width:100%;\"><br></p>','image/52/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(369,'大貔貅金曜石','G00053','546.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/53/10.jpg\" style=\"max-width:100%;\"><br></p>','image/53/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(370,'天安门徽章','G00054','59.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/54/10.jpg\" style=\"max-width:100%;\"><br></p>','image/54/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(371,'天长地久小蛮腰套链','G00055','99.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/55/10.jpg\" style=\"max-width:100%;\"><br></p>','image/55/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,9994,NULL,NULL,1),(372,'天长地久系列镶钻手镯','G00056','99.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/56/10.jpg\" style=\"max-width:100%;\"><br></p>','image/56/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,9992,NULL,NULL,1),(373,'宝宝金红包','G00057','398.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/57/10.jpg\" style=\"max-width:100%;\"><br></p>','image/57/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:32:56','1588931801239',2,10000,NULL,NULL,1),(494,'宝岛台湾流通纪念币5枚套装（方圆封装版）','G00176','388.00',NULL,NULL,1,'<p><br></p>','2020/11/10/d2e384da-1edc-47cd-bd26-f2efbb6dc71b.png','2020-11-10 10:56:58','1597456850777',NULL,NULL,1,1,NULL,NULL,1),(374,'小心心红绳','G00058','198.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/58/1.jpg\" style=\"max-width:100%;\"><br></p>','image/58/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(375,'小玫瑰花红玛瑙','G00059','346.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/59/1.jpg\" style=\"max-width:100%;\"><br></p>','image/59/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(376,'小马红绳','G00060','338.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/60/1.jpg\" style=\"max-width:100%;\"><br></p>','image/60/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(377,'平安金钱','G00061','576.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/61/10.jpg\" style=\"max-width:100%;\"><br></p>','image/61/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(385,'建党90周年纪念币','G00069','58.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/69/10.jpg\" style=\"max-width:100%;\"><br></p>','image/69/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9996,NULL,NULL,1),(386,'建军90周年纪念币','G00070','58.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/70/10.jpg\" style=\"max-width:100%;\"><br></p>','image/70/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,10000,NULL,NULL,1),(387,'建国70周年30克银币','G00071','1550.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/71/1.jpg\" style=\"max-width:100%;\"><br></p>','image/71/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:32:27','1588931801239',1,10000,NULL,NULL,1),(388,'建国70周年纪念币','G00072','60.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/72/10.jpg\" style=\"max-width:100%;\"><br></p>','image/72/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9915,NULL,NULL,1),(500,'建国三十五周年纪念币套装','G00182','1000.00',NULL,NULL,1,'<p><br></p>','2020/11/11/6001ddc1-ce17-45c0-b88a-51d55d282e7e.png','2020-11-11 11:36:28','1597456850777',NULL,NULL,1,49,NULL,NULL,1),(391,'心心红绳','G00075','238.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/75/1.jpg\" style=\"max-width:100%;\"><br></p>','image/75/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(392,'房子红绳','G00076','360.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/76/1.jpg\" style=\"max-width:100%;\"><br></p>','image/76/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(393,'招财貔貅金手串','G00077','860.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/77/10.jpg\" style=\"max-width:100%;\"><br></p>','image/77/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(394,'故宫口红','G00078','288.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/78/1.jpg\" style=\"max-width:100%;\"><br></p>','image/78/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(397,'泰山普通纪念币','G00081','68.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/81/10.jpg\" style=\"max-width:100%;\"><br></p>','image/81/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,4404,NULL,NULL,1),(407,'火神转运珠金','G00091','880.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/91/10.jpg\" style=\"max-width:100%;\"><br></p>','image/91/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(408,'火神转运珠银','G00092','198.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/92/10.jpg\" style=\"max-width:100%;\"><br></p>','image/92/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(409,'火神黄金守御香囊','G00093','198.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/93/10.jpg\" style=\"max-width:100%;\"><br></p>','image/93/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,9993,NULL,NULL,1),(413,'猪年贺岁流通币','G00097','58.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/97/10.jpg\" style=\"max-width:100%;\"><br></p>','image/97/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9982,NULL,NULL,1),(498,'环境保护系列全套流通纪念币（方圆封装版）','G00180','88.00',NULL,NULL,1,'<p><br></p>','2020/11/10/50ce416e-dfa1-450d-9555-cd20be83a003.png','2020-11-10 15:45:44','1597456850777',NULL,NULL,1,49,NULL,NULL,1),(415,'生日金10日','G00099','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/99/10.jpg\" style=\"max-width:100%;\"><br></p>','image/99/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9997,NULL,NULL,1),(416,'生日金11日','G000100','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/100/10.jpg\" style=\"max-width:100%;\"><br></p>','image/100/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9997,NULL,NULL,1),(417,'生日金12日','G000101','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/101/10.jpg\" style=\"max-width:100%;\"><br></p>','image/101/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(418,'生日金13日','G000102','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/102/10.jpg\" style=\"max-width:100%;\"><br></p>','image/102/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9999,NULL,NULL,1),(419,'生日金14日','G000103','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/103/10.jpg\" style=\"max-width:100%;\"><br></p>','image/103/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(420,'生日金15日','G000104','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/104/10.jpg\" style=\"max-width:100%;\"><br></p>','image/104/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(421,'生日金16日','G000105','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/105/10.jpg\" style=\"max-width:100%;\"><br></p>','image/105/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(422,'生日金17日','G000106','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/106/10.jpg\" style=\"max-width:100%;\"><br></p>','image/106/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(423,'生日金18日','G000107','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/107/10.jpg\" style=\"max-width:100%;\"><br></p>','image/107/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(424,'生日金19日','G000108','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/108/10.jpg\" style=\"max-width:100%;\"><br></p>','image/108/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9999,NULL,NULL,1),(425,'生日金1日','G000109','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/109/10.jpg\" style=\"max-width:100%;\"><br></p>','image/109/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9993,NULL,NULL,1),(426,'生日金20日','G000110','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/110/10.jpg\" style=\"max-width:100%;\"><br></p>','image/110/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9998,NULL,NULL,1),(427,'生日金21日','G000111','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/111/10.jpg\" style=\"max-width:100%;\"><br></p>','image/111/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(428,'生日金22日','G000112','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/112/10.jpg\" style=\"max-width:100%;\"><br></p>','image/112/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(429,'生日金23日','G000113','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/113/10.jpg\" style=\"max-width:100%;\"><br></p>','image/113/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(430,'生日金24日','G000114','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/114/10.jpg\" style=\"max-width:100%;\"><br></p>','image/114/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(431,'生日金25日','G000115','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/115/10.jpg\" style=\"max-width:100%;\"><br></p>','image/115/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(432,'生日金26日','G000116','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/116/10.jpg\" style=\"max-width:100%;\"><br></p>','image/116/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(433,'生日金27日','G000117','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/117/10.jpg\" style=\"max-width:100%;\"><br></p>','image/117/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(434,'生日金28日','G000118','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/118/10.jpg\" style=\"max-width:100%;\"><br></p>','image/118/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(435,'生日金29日','G000119','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/119/10.jpg\" style=\"max-width:100%;\"><br></p>','image/119/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(436,'生日金2日','G000120','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/120/10.jpg\" style=\"max-width:100%;\"><br></p>','image/120/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9999,NULL,NULL,1),(437,'生日金30日','G000121','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/121/10.jpg\" style=\"max-width:100%;\"><br></p>','image/121/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9999,NULL,NULL,1),(438,'生日金31日','G000122','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/122/10.jpg\" style=\"max-width:100%;\"><br></p>','image/122/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(439,'生日金3日','G000123','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/123/10.jpg\" style=\"max-width:100%;\"><br></p>','image/123/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9999,NULL,NULL,1),(440,'生日金4日','G000124','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/124/10.jpg\" style=\"max-width:100%;\"><br></p>','image/124/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(441,'生日金5日','G000125','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/125/10.jpg\" style=\"max-width:100%;\"><br></p>','image/125/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9998,NULL,NULL,1),(442,'生日金6日','G000126','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/126/10.jpg\" style=\"max-width:100%;\"><br></p>','image/126/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,10000,NULL,NULL,1),(443,'生日金7日','G000127','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/127/10.jpg\" style=\"max-width:100%;\"><br></p>','image/127/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9999,NULL,NULL,1),(444,'生日金8日','G000128','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/128/10.jpg\" style=\"max-width:100%;\"><br></p>','image/128/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9997,NULL,NULL,1),(445,'生日金9日','G000129','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/129/10.jpg\" style=\"max-width:100%;\"><br></p>','image/129/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9998,NULL,NULL,1),(446,'百福金挂件','G000130','228.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/130/10.jpg\" style=\"max-width:100%;\"><br></p>','image/130/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(447,'百财金手串','G000131','1280.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/131/10.jpg\" style=\"max-width:100%;\"><br></p>','image/131/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(448,'皇冠狗红绳','G000132','286.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/132/1.jpg\" style=\"max-width:100%;\"><br></p>','image/132/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(449,'相知相伴银手镯20g','G000133','298.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/133/10.jpg\" style=\"max-width:100%;\"><br></p>','image/133/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(450,'福到财到红包','G000134','198.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/134/10.jpg\" style=\"max-width:100%;\"><br></p>','image/134/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:33:01','1588931801239',2,10000,NULL,NULL,1),(451,'福字投资金条','G000135','288.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/135/10.jpg\" style=\"max-width:100%;\"><br></p>','image/135/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(501,'福字贺岁银币套装（2016年）','G00183','328.00',NULL,NULL,1,'<p><br></p>','2020/11/11/bbd45070-ca8c-41ec-b63a-806a15c7afe5.png','2020-11-11 14:14:53','1597456850777',NULL,NULL,1,49,NULL,NULL,1),(484,'紫禁城600周年法国纪念币（光币）','G00162','30.00',NULL,NULL,1,'<p><br></p>','2020/10/29/4c0fb655-edc4-4c5a-a9b3-991d6421c5dc.jpg','2020-10-29 18:05:26','1597456850777',NULL,NULL,1,8000,NULL,NULL,1),(452,'红包套装','G000136','1480.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/136/10.jpg\" style=\"max-width:100%;\"><br></p>','image/136/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,2,9989,NULL,NULL,1),(453,'花好月圆金挂件','G000137','228.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/137/10.jpg\" style=\"max-width:100%;\"><br></p>','image/137/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(454,'苏维埃国家银行成立纪念币','G000138','592.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/138/10.jpg\" style=\"max-width:100%;\"><br></p>','image/138/1.jpg','2020-08-14 15:26:54','1588931801239','2020-08-15 17:32:31','1588931801239',1,10000,NULL,NULL,1),(455,'苹果红绳','G000139','418.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/139/10.jpg\" style=\"max-width:100%;\"><br></p>','image/139/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(456,'貔貅红绳','G000140','180.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/140/1.jpg\" style=\"max-width:100%;\"><br></p>','image/140/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(457,'超有心意','G000141','1986.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/141/1.jpg\" style=\"max-width:100%;\"><br></p>','image/141/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(495,'辉煌时刻纪念币（一）方圆封装版','G00177','299.00',NULL,NULL,1,'<p><br></p>','2020/11/10/aeddafb7-f875-421d-80b5-d79b07a7d972.png','2020-11-10 11:08:18','1597456850777','2020-11-10 11:13:50','1597456924771',1,6,NULL,NULL,1),(496,'辉煌时刻纪念币（二）方圆封装版','G00178','188.00',NULL,NULL,1,'<p><br></p>','2020/11/10/d0656837-5a3a-4ab1-bfc2-a60176336030.png','2020-11-10 15:43:50','1597456850777',NULL,NULL,1,3,NULL,NULL,1),(458,'金元宝手串','G000142','860.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/142/10.jpg\" style=\"max-width:100%;\"><br></p>','image/142/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(459,'金房子粉晶手链','G000143','508.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/143/10.jpg\" style=\"max-width:100%;\"><br></p>','image/143/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(460,'金鱼紫晶','G000144','396.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/144/1.jpg\" style=\"max-width:100%;\"><br></p>','image/144/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(461,'金鱼红绳','G000145','218.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/145/1.jpg\" style=\"max-width:100%;\"><br></p>','image/145/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(462,'钞有盛世第四套套装','G000146','2980.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/146/10.jpg\" style=\"max-width:100%;\"><br></p>','image/146/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(463,'钞有祝福第三套套装','G000147','9800.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/147/10.jpg\" style=\"max-width:100%;\"><br></p>','image/147/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(464,'雷神转运珠金','G000148','880.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/148/10.jpg\" style=\"max-width:100%;\"><br></p>','image/148/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(465,'雷神转运珠银','G000149','198.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/149/10.jpg\" style=\"max-width:100%;\"><br></p>','image/149/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(466,'雷神黄金守御香囊','G000150','198.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/150/10.jpg\" style=\"max-width:100%;\"><br></p>','image/150/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,9992,NULL,NULL,1),(467,'顺意金100g','G000151','30000.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/151/1.jpg\" style=\"max-width:100%;\"><br></p>','image/151/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(468,'顺意金10g','G000152','3000.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/152/1.jpg\" style=\"max-width:100%;\"><br></p>','image/152/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(469,'顺意金50g','G000153','15000.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/153/1.jpg\" style=\"max-width:100%;\"><br></p>','image/153/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(470,'高铁航天纪念币套装','G000154','138.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/154/10.jpg\" style=\"max-width:100%;\"><br></p>','image/154/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,1,9999,NULL,NULL,1),(318,'鼠年留香年味香水礼盒','G0002','398.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/2/10.jpg\" style=\"max-width:100%;\"><br></p>','image/2/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(471,'鼠年贺岁金条1克','G000155','598.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/155/10.jpg\" style=\"max-width:100%;\"><br></p>','image/155/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(472,'鼠年贺岁金条3克','G000156','1780.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/156/10.jpg\" style=\"max-width:100%;\"><br></p>','image/156/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(473,'鼠年贺岁银条9克','G000157','298.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/157/10.jpg\" style=\"max-width:100%;\"><br></p>','image/157/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1),(474,'龙晴金鱼','G000158','928.00',NULL,NULL,1,'<p><img src=\"http://file-server.ybtech.gold/yb_network_ftp/prd/image/158/10.jpg\" style=\"max-width:100%;\"><br></p>','image/158/1.jpg','2020-08-14 15:26:54','1588931801239',NULL,NULL,3,10000,NULL,NULL,1);

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `addTime` datetime DEFAULT NULL COMMENT '创建时间',
  `roleDesc` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
  `addUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `updateUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `role` */

insert  into `role`(`id`,`name`,`addTime`,`roleDesc`,`addUser`,`updateTime`,`updateUser`) values (1,'超级管理员','2020-04-27 09:32:46','拥有所有权限','1588931801239','2020-11-05 12:00:58','1588931801239'),(53,'供应链中心审核人员','2020-08-15 11:23:31',NULL,'1588931801239','2020-11-02 16:37:59','1588931801239'),(54,'供应链中心发货人员','2020-08-15 11:23:59',NULL,'1588931801239','2020-10-22 16:34:44','1588931801239'),(55,'供应链中心主管','2020-08-15 11:24:46',NULL,'1588931801239','2020-10-23 00:39:05','1588931801239'),(56,'下单人员','2020-08-15 11:28:25',NULL,'1588931801239','2020-11-05 12:01:21','1588931801239'),(57,'部门主管','2020-08-15 11:29:08',NULL,'1588931801239','2020-10-23 00:38:09','1588931801239'),(58,'财务人员','2020-08-15 16:19:25',NULL,'1588931801239','2020-09-21 17:52:42','1588931801239'),(59,'定制订单管理员','2020-09-04 15:52:58',NULL,'1588931801239',NULL,NULL),(60,'定制订单跟进人','2020-09-04 15:53:19',NULL,'1588931801239',NULL,NULL),(61,'人资查看数据','2020-10-10 16:23:05',NULL,'1588931801239','2020-10-15 17:15:02','1588931801239'),(62,'拉取订单','2020-10-21 14:16:54',NULL,'1600917384877','2020-10-22 18:16:13','1588931801239'),(63,'向供应商下单人员','2020-10-26 16:55:12',NULL,'1588931801239',NULL,NULL),(64,'供应商人员','2020-10-26 16:58:23',NULL,'1588931801239','2020-10-26 16:59:52','1588931801239'),(65,'下单人员(外部用户)','2020-10-29 15:37:58',NULL,'1600917384877','2020-11-05 12:01:31','1588931801239');

/*Table structure for table `role_permission` */

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rId` int DEFAULT NULL,
  `pId` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2652 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `role_permission` */

insert  into `role_permission`(`id`,`rId`,`pId`) values (1608,59,96),(1609,59,97),(1610,59,98),(1611,59,99),(1612,60,98),(2135,58,1),(2136,58,59),(2137,58,70),(2138,58,85),(2139,58,87),(2140,58,88),(2141,58,89),(2142,58,84),(2143,58,61),(2144,58,28),(2145,58,60),(2146,58,32),(2147,58,50),(2148,58,57),(2149,58,29),(2150,58,102),(2213,61,1),(2214,61,3),(2215,61,29),(2217,54,1),(2218,54,53),(2219,54,85),(2220,54,87),(2221,54,88),(2222,54,84),(2223,54,93),(2224,54,95),(2225,54,7),(2226,54,92),(2227,54,29),(2228,62,1),(2229,62,29),(2230,57,29),(2231,57,83),(2232,57,122),(2236,55,1),(2237,55,53),(2238,55,85),(2239,55,87),(2240,55,88),(2241,55,84),(2242,55,93),(2243,55,94),(2244,55,7),(2245,55,49),(2246,55,27),(2247,55,92),(2248,55,30),(2249,55,50),(2250,55,57),(2251,55,29),(2252,55,83),(2253,55,122),(2376,63,112),(2377,63,113),(2378,63,117),(2379,63,119),(2387,64,115),(2388,64,116),(2389,64,118),(2508,53,33),(2509,53,1),(2510,53,85),(2511,53,87),(2512,53,88),(2513,53,90),(2514,53,122),(2515,53,15),(2516,53,18),(2517,53,17),(2518,53,16),(2519,53,92),(2520,53,27),(2521,53,30),(2522,53,51),(2523,53,50),(2524,53,55),(2525,53,52),(2526,53,54),(2527,53,56),(2528,53,57),(2529,53,29),(2530,53,83),(2531,53,93),(2532,53,94),(2533,1,33),(2534,1,1),(2535,1,53),(2536,1,59),(2537,1,70),(2538,1,85),(2539,1,86),(2540,1,87),(2541,1,88),(2542,1,89),(2543,1,6),(2544,1,90),(2545,1,84),(2546,1,93),(2547,1,94),(2548,1,95),(2549,1,61),(2550,1,7),(2551,1,49),(2552,1,126),(2553,1,122),(2554,1,128),(2555,1,129),(2556,1,130),(2557,1,131),(2558,1,132),(2559,1,2),(2560,1,3),(2561,1,9),(2562,1,8),(2563,1,10),(2564,1,13),(2565,1,12),(2566,1,14),(2567,1,11),(2568,1,15),(2569,1,18),(2570,1,17),(2571,1,16),(2572,1,91),(2573,1,20),(2574,1,22),(2575,1,21),(2576,1,58),(2577,1,27),(2578,1,92),(2579,1,30),(2580,1,24),(2581,1,23),(2582,1,26),(2583,1,25),(2584,1,28),(2585,1,60),(2586,1,32),(2587,1,102),(2588,1,124),(2589,1,67),(2590,1,68),(2591,1,69),(2592,1,71),(2593,1,72),(2594,1,73),(2595,1,74),(2596,1,51),(2597,1,50),(2598,1,55),(2599,1,52),(2600,1,54),(2601,1,56),(2602,1,57),(2603,1,29),(2604,1,65),(2605,1,64),(2606,1,75),(2607,1,77),(2608,1,78),(2609,1,79),(2610,1,80),(2611,1,81),(2612,1,82),(2613,1,101),(2614,1,83),(2615,1,96),(2616,1,97),(2617,1,98),(2618,1,99),(2619,1,103),(2620,1,104),(2621,1,105),(2622,1,106),(2623,1,107),(2624,1,108),(2625,1,109),(2626,1,110),(2627,1,111),(2628,1,112),(2629,1,113),(2630,1,114),(2631,1,115),(2632,1,116),(2633,1,117),(2634,1,118),(2635,1,119),(2636,1,123),(2637,1,125),(2638,1,121),(2639,1,133),(2640,56,33),(2641,56,126),(2642,56,122),(2643,56,129),(2644,56,131),(2645,56,132),(2646,56,29),(2647,56,133),(2648,65,33),(2649,65,122),(2650,65,29),(2651,65,134);

/*Table structure for table `sorecord` */

DROP TABLE IF EXISTS `sorecord`;

CREATE TABLE `sorecord` (
  `sorecordId` int NOT NULL AUTO_INCREMENT COMMENT '供应商下单记录',
  `sorecordNum` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品编号',
  `sorecordName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品名称',
  `specification` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '规格',
  `amount` int DEFAULT NULL COMMENT '商品数量',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `userId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户ID',
  `supplierId` int DEFAULT NULL COMMENT '供应商ID',
  PRIMARY KEY (`sorecordId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `sorecord` */

/*Table structure for table `sponsoraudit` */

DROP TABLE IF EXISTS `sponsoraudit`;

CREATE TABLE `sponsoraudit` (
  `sponsorAuditId` int NOT NULL AUTO_INCREMENT COMMENT '发起审核ID',
  `sponsorAuditUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发起人ID',
  `sponsorAuditOrder` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发起的订单ID',
  `sponsorAuditTime` date DEFAULT NULL COMMENT '发起时间',
  `audit` int DEFAULT NULL COMMENT '审核状态（2审核不通过  1审核通过,3 审核中 4 审核撤销）',
  `auditNode` int DEFAULT NULL COMMENT '标志该发起的审核目前在第几步',
  `processGroupId` int DEFAULT NULL COMMENT '标志该审核节点是属于哪个组的',
  PRIMARY KEY (`sponsorAuditId`) USING BTREE,
  FULLTEXT KEY `sponsorAuditUser` (`sponsorAuditUser`),
  FULLTEXT KEY `sponsorAuditUser_` (`sponsorAuditUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `sponsoraudit` */

/*Table structure for table `supplier` */

DROP TABLE IF EXISTS `supplier`;

CREATE TABLE `supplier` (
  `supplierId` int NOT NULL AUTO_INCREMENT COMMENT '供应商Id',
  `supplierName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '供应商名字',
  `addUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `addTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateUser` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `supplierPhone` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '供应商电话',
  `supplierSite` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '供应商地址',
  PRIMARY KEY (`supplierId`) USING BTREE,
  UNIQUE KEY `supplierName` (`supplierName`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `supplier` */

/*Table structure for table `suppliercontract` */

DROP TABLE IF EXISTS `suppliercontract`;

CREATE TABLE `suppliercontract` (
  `supplierContractId` int NOT NULL AUTO_INCREMENT COMMENT '供应商合同ID',
  `supplierContractName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '供应商合同名字',
  `supplierContractNumber` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '供应商合同编号',
  `addUser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  `supplierContractSite` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '合同附件地址',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `updateUser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `organizationId` int DEFAULT NULL COMMENT '公司ID',
  PRIMARY KEY (`supplierContractId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `suppliercontract` */

/*Table structure for table `supplierlog` */

DROP TABLE IF EXISTS `supplierlog`;

CREATE TABLE `supplierlog` (
  `supplierlogId` int NOT NULL AUTO_INCREMENT COMMENT '供应商相关相关操作日志ID',
  `logName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '具体操作',
  `logUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作人',
  `logTime` datetime DEFAULT NULL COMMENT '操作时间',
  `logRemarks` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作备注',
  `logOrderId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作订单ID',
  `classify` int DEFAULT NULL COMMENT '日志分类',
  `operationTime` datetime DEFAULT NULL COMMENT '结算时间',
  PRIMARY KEY (`supplierlogId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `supplierlog` */

/*Table structure for table `supplierorder` */

DROP TABLE IF EXISTS `supplierorder`;

CREATE TABLE `supplierorder` (
  `supplierOrderId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商订单ID',
  `productNum` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品编号',
  `productName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品名称',
  `specification` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '规格',
  `amount` int DEFAULT NULL COMMENT '数量',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `supplierId` int DEFAULT NULL COMMENT '供应商ID',
  `addUser` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人ID',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  `dispose` int DEFAULT NULL COMMENT '供应商反馈（1待处理 2处理中  3不受理,4已完成）',
  `quotation` decimal(15,2) DEFAULT NULL COMMENT '报价',
  `flag` int DEFAULT NULL COMMENT '是否锁定报价1未锁定2已锁定',
  `settlement` int DEFAULT NULL COMMENT '结算状态（1未结算2,部分结算3已结算）',
  `settlementAmount` decimal(15,2) DEFAULT '0.00' COMMENT '已结算金额',
  `organizationId` int DEFAULT NULL COMMENT '公司id',
  `disposeLog` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '供应商反馈备注',
  `supplierContractId` int DEFAULT '0' COMMENT '供应商合同ID',
  PRIMARY KEY (`supplierOrderId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `supplierorder` */

/*Table structure for table `universal` */

DROP TABLE IF EXISTS `universal`;

CREATE TABLE `universal` (
  `universalId` int NOT NULL AUTO_INCREMENT,
  `red` int DEFAULT NULL COMMENT '通用预警红色预警',
  `yellow` int DEFAULT NULL COMMENT '通用预警黄色预警',
  PRIMARY KEY (`universalId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `universal` */

insert  into `universal`(`universalId`,`red`,`yellow`) values (1,100,400);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `uId` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '上级ID',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  `flag` int DEFAULT '0' COMMENT '是否启用',
  `addUser` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '添加人',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `updateUser` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
  `organizationId` int DEFAULT NULL COMMENT '所属机构',
  `userCount` int DEFAULT '0' COMMENT '账户冻结次数',
  `name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户姓名',
  `userPhone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户手机号',
  `departmentId` int DEFAULT '0' COMMENT '部门ID',
  `groupingId` int DEFAULT '0' COMMENT '分组ID',
  `sign` int DEFAULT NULL COMMENT '（1：公司2：供应商）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`password`,`email`,`uId`,`addTime`,`flag`,`addUser`,`updateTime`,`updateUser`,`organizationId`,`userCount`,`name`,`userPhone`,`departmentId`,`groupingId`,`sign`) values ('1588931801239','sysadmin','92dempvunj34efsb2fda3bbqb4vqv5qs','','','2020-05-08 17:56:41',0,'1588931801239','2020-11-20 14:29:34','1588931801239',2,1,'系统管理员','13552277889',3,7,1);

/*Table structure for table `user_customizedorder` */

DROP TABLE IF EXISTS `user_customizedorder`;

CREATE TABLE `user_customizedorder` (
  `uoId` int NOT NULL AUTO_INCREMENT,
  `userId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '跟进人ID',
  `coId` int DEFAULT NULL COMMENT '定制订单ID',
  PRIMARY KEY (`uoId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `user_customizedorder` */

/*Table structure for table `user_order` */

DROP TABLE IF EXISTS `user_order`;

CREATE TABLE `user_order` (
  `uoId` int NOT NULL AUTO_INCREMENT,
  `userId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户ID',
  `orderId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单ID',
  PRIMARY KEY (`uoId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `user_order` */

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `rId` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `user_role` */

insert  into `user_role`(`id`,`uId`,`rId`) values (1,'1588931801239',1);

/*Table structure for table `usersite` */

DROP TABLE IF EXISTS `usersite`;

CREATE TABLE `usersite` (
  `userSiteId` int NOT NULL AUTO_INCREMENT COMMENT '用户地址ID',
  `receiverName` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人姓名',
  `receiverMobile` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人手机号',
  `receiverProvince` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人地址（省）',
  `receiverCity` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '市',
  `receiverDistrict` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '区（县）',
  `receiverStreet` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人地址（区）',
  `receiverAddress` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人地址（详情地址）',
  `site` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收件人地址汇总',
  `userId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户ID',
  `updateTime` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`userSiteId`) USING BTREE,
  KEY `userId_index` (`userId`) USING BTREE,
  FULLTEXT KEY `userId_fulltext` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `usersite` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
