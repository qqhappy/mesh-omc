

DROP TABLE IF EXISTS `alarm_def`;
CREATE TABLE `alarm_def` (
  `alarm_def_id` bigint(20) NOT NULL,
  `mo_type` int(11) DEFAULT NULL,
  `alarm_level` int(11) DEFAULT NULL,
  `alarm_name_zh` varchar(512) DEFAULT NULL,
  `alarm_name_en` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`alarm_def_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;


DROP TABLE IF EXISTS `alarm_current`;
CREATE TABLE `alarm_current` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT 0,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_ALARM_CURRENT_IDX_2` (`moId`,`alarmDefId`,`entityType`,`entityOid`),
  KEY `PK_ALARM_CURRENT_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_CURRENT_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_0` */

DROP TABLE IF EXISTS `alarm_history_0`;

CREATE TABLE `alarm_history_0` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_ALARM_HISTORY_0_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_0_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_1` */

DROP TABLE IF EXISTS `alarm_history_1`;

CREATE TABLE `alarm_history_1` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_ALARM_HISTORY_1_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_1_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) 
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_2` */

DROP TABLE IF EXISTS `alarm_history_2`;

CREATE TABLE `alarm_history_2` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_ALARM_HISTORY_2_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_2_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) 
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_3` */

DROP TABLE IF EXISTS `alarm_history_3`;

CREATE TABLE `alarm_history_3` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_ALARM_HISTORY_3_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_3_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) 
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_4` */

DROP TABLE IF EXISTS `alarm_history_4`;

CREATE TABLE `alarm_history_4` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_ALARM_HISTORY_4_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_4_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) 
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_5` */

DROP TABLE IF EXISTS `alarm_history_5`;

CREATE TABLE `alarm_history_5` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_ALARM_HISTORY_5_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_5_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_6` */

DROP TABLE IF EXISTS `alarm_history_6`;

CREATE TABLE `alarm_history_6` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_ALARM_HISTORY_6_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_6_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) 
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_7` */

DROP TABLE IF EXISTS `alarm_history_7`;

CREATE TABLE `alarm_history_7` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),  
  KEY `PK_ALARM_HISTORY_7_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_7_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) 
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_8` */

DROP TABLE IF EXISTS `alarm_history_8`;

CREATE TABLE `alarm_history_8` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_ALARM_HISTORY_8_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_8_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) 
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `alarm_history_9` */

DROP TABLE IF EXISTS `alarm_history_9`;

CREATE TABLE `alarm_history_9` (
  `id` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `moName` varchar(64) NOT NULL,
  `entityType` varchar(256) NOT NULL,
  `entityOid` varchar(256) NOT NULL,
  `alarmDefId` bigint(20) NOT NULL,
  `alarmContent` varchar(1024) NOT NULL,
  `alarmLevel` bigint(20) NOT NULL,
  `alarmState` bigint(20) NOT NULL,
  `firstAlarmTime` bigint(20) NOT NULL,
  `lastAlarmTime` bigint(20) NOT NULL,
  `alarmTimes` bigint(20) NOT NULL,
  `restoredTime` bigint(20) DEFAULT NULL,
  `restoreUser` varchar(20) DEFAULT NULL,
  `restoreFlag` bigint(20) DEFAULT NULL,
  `alarmType` bigint(20) DEFAULT NULL,
  `confirmState` bigint(20) DEFAULT NULL,
  `confirmUser` varchar(20) DEFAULT NULL,
  `confirmTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),   
  KEY `PK_ALARM_HISTORY_9_IDX_3` (`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) ,
  KEY `PK_ALARM_HISTORY_9_IDX_4` (`moId`,`lastAlarmTime`, `alarmLevel`, `alarmState`, `confirmState`) 
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `inms_pub_sequence` */

DROP TABLE IF EXISTS `inms_pub_sequence`;

CREATE TABLE `inms_pub_sequence` (
  `SEQUENCE_NAME` varchar(16) NOT NULL DEFAULT '',
  `MIN_VALUE` bigint(20) DEFAULT NULL,
  `MAX_VALUE` bigint(20) DEFAULT NULL,
  `CACHE_NUM` bigint(20) DEFAULT NULL,
  `CURRENT_VALUE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`SEQUENCE_NAME`),
  KEY `PK_INMS_PUB_SEQUENCE` (`SEQUENCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `location_area` */

DROP TABLE IF EXISTS `location_area`;

CREATE TABLE `location_area` (
  `idx` bigint(20) NOT NULL,
  `operator` varchar(11) NOT NULL,
  `subnet` varchar(11) NOT NULL,
  `lac` varchar(11) NOT NULL,
  `locAreaName` varchar(20) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_acl` */

DROP TABLE IF EXISTS `mcbts_acl`;

CREATE TABLE `mcbts_acl` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `protocol` tinyint(3) NOT NULL,
  `sourceIP` bigint(20) NOT NULL,
  `sourceIPMask` bigint(20) NOT NULL,
  `sourcePort` int(11) NOT NULL,
  `sourceOper` tinyint(3) NOT NULL,
  `desIp` bigint(20) NOT NULL,
  `desIPMask` bigint(20) NOT NULL,
  `desPort` int(11) NOT NULL,
  `desOper` tinyint(3) NOT NULL,
  `permission` tinyint(3) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_airlink` */

DROP TABLE IF EXISTS `mcbts_airlink`;

CREATE TABLE `mcbts_airlink` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `sequenceId` int(11) NOT NULL,
  `preambleScale` int(11) NOT NULL,
  `maxScale` int(11) NOT NULL,
  `totalTS` int(11) NOT NULL,
  `downlinkTS` int(11) NOT NULL,
  `scgMask` int(11) NOT NULL,
  `tchForbidden` varchar(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_airlink_subscgchannel` */

DROP TABLE IF EXISTS `mcbts_airlink_subscgchannel`;

CREATE TABLE `mcbts_airlink_subscgchannel` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `channelType` int(11) NOT NULL,
  `tsIndex` int(11) NOT NULL,
  `scgIndex` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_airlink_pecch` */

DROP TABLE IF EXISTS `mcbts_airlink_pecch`;

CREATE TABLE `mcbts_airlink_pecch` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `scgIndex` int(11) NOT NULL,
  `pchCount` int(11) NOT NULL,
  `pchIndex` int(11) NOT NULL,
  `rarchCount` int(11) NOT NULL,
  `rrchCount` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_airlink_subscgscale` */

DROP TABLE IF EXISTS `mcbts_airlink_subscgscale`;

CREATE TABLE `mcbts_airlink_subscgscale` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `tsIndex` int(11) NOT NULL,
  `bchScale` int(11) NOT NULL,
  `tchScale` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_airlink_subw0` */

DROP TABLE IF EXISTS `mcbts_airlink_subw0`;

CREATE TABLE `mcbts_airlink_subw0` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `antennaIndex` int(11) NOT NULL,
  `w0I` float(20,3) NOT NULL,
  `w0Q` float(20,3) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_basic` */

DROP TABLE IF EXISTS `mcbts_basic`;

CREATE TABLE `mcbts_basic` (
  `moId` bigint(20) NOT NULL,
  `typeId` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `manageState` int(11) NOT NULL,
  `btsId` bigint(20) NOT NULL,
  `btsType` int(11) NOT NULL,
  `btsFreqType` int(11) NOT NULL,
  `locationAreaId` bigint(20) NOT NULL,
  `networkId` int(11) NOT NULL,
  `bootSource` int(11) NOT NULL,
  `natAPKey` int(11) NOT NULL,
  `sagVlanUsedFlag` int(11) NOT NULL,
  `sagVlanId` int(11) NOT NULL,
  `sagBtsIp` varchar(32) NOT NULL,
  `restrictAreaFlag` int(11) DEFAULT NULL,
  `sagDeviceId` bigint(20) NOT NULL,
  `sagVoiceIp` varchar(32) NOT NULL,
  `sagSignalIp` varchar(32) NOT NULL,
  `sagDefaultGateway` varchar(32) NOT NULL,
  `sagSubNetMask` varchar(32) NOT NULL,
  `btsMediaPort` int(11) NOT NULL,
  `sagMediaPort` int(11) NOT NULL,
  `btsSignalPort` int(11) NOT NULL,
  `sagSignalPort` int(11) NOT NULL,
  `btsSignalPointCode` int(11) NOT NULL,
  `antennaType` int(11) NOT NULL,
  `antennaAngle` int(4) NOT NULL,
  `sagSignalPointCode` int(11) NOT NULL,
  `templateId` bigint(20) NOT NULL,
  `districtId` bigint(20) NOT NULL,
  `btsConfigIp` VARCHAR(32) NOT NULL,
  `voiceDirectConnFlag` int(4) NOT NULL,
  PRIMARY KEY (`moId`),
  UNIQUE KEY `PK_BTS_ID` (`btsId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_calibantdataconfig` */

DROP TABLE IF EXISTS `mcbts_calibantdataconfig`;

CREATE TABLE `mcbts_calibantdataconfig` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `antennaIndex` int(11) NOT NULL,
  `dataType` int(11) NOT NULL,
  `detailInfo` blob,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_calibgenconfig` */

DROP TABLE IF EXISTS `mcbts_calibgenconfig`;

CREATE TABLE `mcbts_calibgenconfig` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `synRxGain` int(11) NOT NULL,
  `synTxGain` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_calibgenconfig_subitem` */

DROP TABLE IF EXISTS `mcbts_calibgenconfig_subitem`;

CREATE TABLE `mcbts_calibgenconfig_subitem` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `antennaIndex` int(11) NOT NULL,
  `rxGain` int(11) NOT NULL,
  `txGain` int(11) NOT NULL,
  `calibrationResult` int(11) NOT NULL,
  `predH` varchar(24) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_calibrationtype` */

DROP TABLE IF EXISTS `mcbts_calibrationtype`;

CREATE TABLE `mcbts_calibrationtype` (
  `idx` bigint(20) NOT NULL DEFAULT '0',
  `moId` bigint(20) NOT NULL,
  `calibPeriod` int(11) NOT NULL,
  `calibType` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_chargeparamconfig` */

DROP TABLE IF EXISTS `mcbts_chargeparamconfig`;

CREATE TABLE `mcbts_chargeparamconfig` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `ChargeSwitch` bigint(20) NOT NULL,
  `CDRPeriod` bigint(20) NOT NULL,
  `ChargeIP` varchar(20) NOT NULL,
  `CDRDataPeriod` bigint(20) NOT NULL,
  `CB3000IP` varchar(20) DEFAULT '0.0.0.0',
  `Port` bigint(20) DEFAULT '0',
  `RealTimeChargeSwitch` bigint(20) DEFAULT '0',
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_dataoperation` */

DROP TABLE IF EXISTS `mcbts_dataoperation`;

CREATE TABLE `mcbts_dataoperation` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `RoutingareaID` bigint(20) NOT NULL,
  `Mobility` bigint(20) NOT NULL,
  `AccessControl` bigint(20) NOT NULL,
  `Learnedbridgetimer` bigint(20) NOT NULL,
  `PeertoPeBridging` bigint(20) NOT NULL,
  `EgressARPProxy` bigint(20) NOT NULL,
  `EgressBroadFiltering` bigint(20) NOT NULL,
  `PPPKeepTimerLength` bigint(20) NOT NULL,
  `BTSIDAsDHCP` bigint(20) NOT NULL,
  `EIDAsDHCP` bigint(20) NOT NULL,
  `EIDAsPPPoE` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_dbsserver` */

DROP TABLE IF EXISTS `mcbts_dbsserver`;

CREATE TABLE `mcbts_dbsserver` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `DBSServerIP` varchar(20) NOT NULL,
  `DBSServerPort` bigint(20) NOT NULL,
  `BSDataBroadcastPort` bigint(20) NOT NULL,
  `NatApKey` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_energysaving` */

DROP TABLE IF EXISTS `mcbts_energysaving`;

CREATE TABLE `mcbts_energysaving` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `EnergySavingSwitch` bigint(20) NOT NULL,
  `keep1Channel` bigint(20) NOT NULL,
  `keep1Users` bigint(20) NOT NULL,
  `keep2Channel` bigint(20) NOT NULL,
  `keep2Users` bigint(20) NOT NULL,
  `open1FanTemp` bigint(20) NOT NULL,
  `open2FanTemp` bigint(20) NOT NULL,
  `open3FanTemp` bigint(20) NOT NULL,
  `open4FanTemp` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_freqset` */

DROP TABLE IF EXISTS `mcbts_freqset`;

CREATE TABLE `mcbts_freqset` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `freqCount` int(11) NOT NULL,
  `freqIndex` int(11) NOT NULL,
  `freqSpan` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_gpsclock` */

DROP TABLE IF EXISTS `mcbts_gpsclock`;

CREATE TABLE `mcbts_gpsclock` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `GPSServerIP` varchar(20) NOT NULL,
  `GPSServerPort` bigint(20) NOT NULL,
  `GPSReportingPeriod` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_gpsdata` */

DROP TABLE IF EXISTS `mcbts_gpsdata`;

CREATE TABLE `mcbts_gpsdata` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `Latitude` bigint(20) NOT NULL,
  `Longitude` bigint(20) NOT NULL,
  `Height` bigint(20) NOT NULL,
  `GMTOffset` bigint(20) NOT NULL,
  `MinimumTrackingsatellite` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_l1general` */

DROP TABLE IF EXISTS `mcbts_l1general`;

CREATE TABLE `mcbts_l1general` (
  `idx` bigint(20) NOT NULL DEFAULT '0',
  `moId` bigint(20) NOT NULL,
  `antennaMask` bigint(20) NOT NULL,
  `syncSrc` bigint(20) NOT NULL,
  `gpsOffset` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_lightalarmconfig` */

DROP TABLE IF EXISTS `mcbts_lightalarmconfig`;

CREATE TABLE `mcbts_lightalarmconfig` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `LiPresAlarmShreahold` bigint(20) NOT NULL,
  `LiCurrAlarmShreahold` bigint(20) NOT NULL,
  `LiEmiPowerAlarmShreahold` bigint(20) NOT NULL,
  `LiInceptPowerAlarmShreahold` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_load_balance` */

DROP TABLE IF EXISTS `mcbts_load_balance`;

CREATE TABLE `mcbts_load_balance` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `algorithm_switch` int(11) NOT NULL,
  `load_high_threshold` int(11) NOT NULL,
  `load_msg_send_period` int(11) NOT NULL,
  `load_diff_threshold` int(11) NOT NULL,
  `neighbor_bts_power_over_num` int(11) NOT NULL,
  `load_balance_signal_remains` int(11) NOT NULL,
  `ut_load_balance_period` int(11) NOT NULL,
  `user_algorithm_param` int(11) NOT NULL,
  `arithmetic_switch` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_n1config` */

DROP TABLE IF EXISTS `mcbts_n1config`;

CREATE TABLE `mcbts_n1config` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `NAlgorithmSwitch` bigint(20) NOT NULL,
  `CiJumpdetection` bigint(20) NOT NULL,
  `UTPowerLockTimerTh` bigint(20) NOT NULL,
  `BTSPowerLockTimerTh` bigint(20) NOT NULL,
  `PairListSize` bigint(20) NOT NULL,
  `ProportionThreshold` bigint(20) NOT NULL,
  `RAMAXNUM` bigint(20) NOT NULL,
  `ProfileMsgTimerTh` bigint(20) NOT NULL,
  `RleaseHoldTimerTh` bigint(20) NOT NULL,
  `HCAPWindow` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_neighbour` */

DROP TABLE IF EXISTS `mcbts_neighbour`;

CREATE TABLE `mcbts_neighbour` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `neighbourMoId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  KEY `moIdIndex` (`moId`),
  KEY `neighbourMoIdIndex` (`neighbourMoId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_append_neighbor`*/

DROP TABLE IF EXISTS `mcbts_append_neighbor`;

CREATE TABLE `mcbts_append_neighbor` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `appendNeighborMoId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  KEY `moIdIndex` (`moId`),
  KEY `appendNeighborMoIdIndex` (`appendNeighborMoId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_neighbor_failed`*/

DROP TABLE IF EXISTS `mcbts_neighbor_failed`;

CREATE TABLE `mcbts_neighbor_failed` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `updateTime` timestamp NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;


/*Table structure for table `mcbts_perflog` */

DROP TABLE IF EXISTS `mcbts_perflog`;

CREATE TABLE `mcbts_perflog` (
`idx`  bigint(20) NOT NULL ,
`moId`  bigint(20) NOT NULL ,
`ftpServerIp`  varchar(20) NOT NULL ,
`ftpServerPort`  int(11) NOT NULL ,
`userName`  varchar(10) NOT NULL ,
`password`  varchar(10) NOT NULL ,
`reportInterval`  smallint(6) NOT NULL ,
`collectInterval`  smallint(6) NOT NULL ,
`realTimeInterval`  smallint(6) NULL DEFAULT NULL ,
PRIMARY KEY (`idx`),
UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_poticalfrequ` */

DROP TABLE IF EXISTS `mcbts_poticalfrequ`;

CREATE TABLE `mcbts_poticalfrequ` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `ConfigRFFlag` bigint(20) NOT NULL,
  `GTIME1` bigint(20) NOT NULL,
  `GTIME2` bigint(20) NOT NULL,
  `DATAGW1` varchar(20) NOT NULL,
  `DATAGW2` varchar(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_rangingconfig` */

DROP TABLE IF EXISTS `mcbts_rangingconfig`;

CREATE TABLE `mcbts_rangingconfig` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `RangingSwitch` bigint(20) NOT NULL,
  `EnableShreashold` bigint(20) NOT NULL,
  `DisableShreahold` bigint(20) NOT NULL,
  `percent` bigint(20) NOT NULL,
  `SNRShreahold` bigint(20) NOT NULL,
  `RangingExcurShreahold` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_repeater` */

DROP TABLE IF EXISTS `mcbts_repeater`;

CREATE TABLE `mcbts_repeater` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `offset` INT(10) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_rfconfig` */

DROP TABLE IF EXISTS `mcbts_rfconfig`;

CREATE TABLE `mcbts_rfconfig` (
  `idx` bigint(20) NOT NULL DEFAULT '0',
  `moId` bigint(20) NOT NULL,
  `freqOffset` int(11) NOT NULL,
  `antennaPower` int(11) NOT NULL,
  `rxSensitivity` int(11) NOT NULL,
  `cableLoss` int(11) NOT NULL,
  `psLoss` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_rfconfig_subps` */

DROP TABLE IF EXISTS `mcbts_rfconfig_subps`;

CREATE TABLE `mcbts_rfconfig_subps` (
  `idx` bigint(20) NOT NULL DEFAULT '0',
  `moId` bigint(20) NOT NULL,
  `antennaIndex` int(11) NOT NULL,
  `psNormX` int(11) NOT NULL,
  `psNormY` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_rru_status` */

DROP TABLE IF EXISTS `mcbts_rru_status`;

CREATE TABLE `mcbts_rru_status` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `current` double(10,10) NOT NULL,
  `temperature` double(10,10) NOT NULL,
  `timeDelay` double(10,10) NOT NULL,
  `freqOffset` double(10,10) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_rruenvparamconfig` */

DROP TABLE IF EXISTS `mcbts_rruenvparamconfig`;

CREATE TABLE `mcbts_rruenvparamconfig` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `tempAlarmUpLimit` int(11) NOT NULL,
  `tempAlarmDownLimit` int(11) NOT NULL,
  `curAlarmUpLimit` int(11) NOT NULL,
  `curAlarmDownLimit` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_sn` */

DROP TABLE IF EXISTS `mcbts_sn`;

CREATE TABLE `mcbts_sn` (
  `idx` bigint(20) NOT NULL DEFAULT '0',
  `moId` bigint(20) NOT NULL,
  `dsbPanel` varchar(50),
  `bbPanel` varchar(50) NOT NULL,
  `synPanel` varchar(50) NOT NULL,
  `rfPanel1` varchar(50) NOT NULL,
  `rfPanel2` varchar(50) NOT NULL,
  `rfPanel3` varchar(50) NOT NULL,
  `rfPanel4` varchar(50) NOT NULL,
  `rfPanel5` varchar(50) NOT NULL,
  `rfPanel6` varchar(50) NOT NULL,
  `rfPanel7` varchar(50) NOT NULL,
  `rfPanel8` varchar(50) NOT NULL,
  `timeStamp` datetime NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_supported_biz` */

DROP TABLE IF EXISTS `mcbts_supported_biz`;

CREATE TABLE `mcbts_supported_biz` (
  `idx` bigint(20) NOT NULL,
  `btsType` int(11) NOT NULL,
  `softwareVersion` varchar(20) NOT NULL,
  `moc` int(20) NOT NULL,
  `support` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_telnet` */

DROP TABLE IF EXISTS `mcbts_telnet`;

CREATE TABLE `mcbts_telnet` (
  `idx` bigint(20) NOT NULL DEFAULT '0',
  `moId` bigint(20) DEFAULT NULL,
  `username` varchar(16) DEFAULT NULL,
  `password` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_template` */

DROP TABLE IF EXISTS `mcbts_template`;

CREATE TABLE `mcbts_template` (
  `moId` bigint(20) NOT NULL COMMENT 'template id',
  `name` varchar(50) NOT NULL COMMENT 'template name',
  `backupId` bigint(20) NOT NULL COMMENT 'backup id',
  `referredTemplateId` bigint(20) NOT NULL COMMENT 'referred template id',
  `referredTemplateName` varchar(50) NOT NULL COMMENT 'referred template name',
  `operationName` varchar(1024) DEFAULT NULL,
  `addedTime` datetime NOT NULL COMMENT 'template added time',
  `lastModifiedTime` datetime NOT NULL COMMENT 'template last modified time',
  PRIMARY KEY (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_tempwatch` */

DROP TABLE IF EXISTS `mcbts_tempwatch`;

CREATE TABLE `mcbts_tempwatch` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `AlarmHighTemperature` bigint(20) NOT NULL,
  `AlarmLowTemperature` bigint(20) NOT NULL,
  `ShutdownHighTemperature` bigint(20) NOT NULL,
  `ShutdownLowTemperature` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_tos` */

DROP TABLE IF EXISTS `mcbts_tos`;

CREATE TABLE `mcbts_tos` (
  `idx` bigint(20) NOT NULL,
  `serviceLevel` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_trunkconfig` */

DROP TABLE IF EXISTS `mcbts_trunkconfig`;

CREATE TABLE `mcbts_trunkconfig` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `Flag` bigint(20) NOT NULL,
  `SleepPeriod` bigint(20) NOT NULL,
  `RsvChResourseNum` bigint(20) NOT NULL,
  `VideoChResourseNum` bigint(20) NOT NULL,
  `Rsv2` bigint(20) DEFAULT '0',
  `Rsv3` bigint(20) DEFAULT '0',
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_ut_biz_param` */

DROP TABLE IF EXISTS `mcbts_ut_biz_param`;

CREATE TABLE `mcbts_ut_biz_param` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `class` smallint(6) NOT NULL,
  `upLinkMaxBandwidth` int(11) NOT NULL,
  `upLinkMinBandwidth` int(11) NOT NULL,
  `downLinkMaxBandwidth` int(11) NOT NULL,
  `downLinkMinBandwidth` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_ut_telnet_conf` */

DROP TABLE IF EXISTS `mcbts_ut_telnet_conf`;

CREATE TABLE `mcbts_ut_telnet_conf` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `userName` bigint(20) NOT NULL,
  `password` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_ut_temper_listen_param` */

DROP TABLE IF EXISTS `mcbts_ut_temper_listen_param`;

CREATE TABLE `mcbts_ut_temper_listen_param` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `alarmHighTemperature` int(11) NOT NULL,
  `alarmLowTemperature` int(11) NOT NULL,
  `shutdownHighTemperature` int(11) NOT NULL,
  `shutdownLowTemperature` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_version` */

DROP TABLE IF EXISTS `mcbts_version`;

CREATE TABLE `mcbts_version` (
  `idx` bigint(20) NOT NULL,
  `versionName` varchar(50) NOT NULL,
  `btsType` int(11) NOT NULL,
  `fileName` varchar(50) NOT NULL,
  `fileType` int(11) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_version_dl_history` */

DROP TABLE IF EXISTS `mcbts_version_dl_history`;

CREATE TABLE `mcbts_version_dl_history` (
  `idx` int(11) NOT NULL,
  `btsId` int(11) NOT NULL,
  `btsType` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `actionResult` int(2) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_vlan` */

DROP TABLE IF EXISTS `mcbts_vlan`;

CREATE TABLE `mcbts_vlan` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `vlanGroup` int(11) NOT NULL,
  `vlanID` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `mcbts_vlan_attach` */

/*DROP TABLE IF EXISTS `mcbts_vlan_attach`; */

/*CREATE TABLE `mcbts_vlan_attach` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `enableQinq` int(11) NOT NULL,
  `etype` varchar(20) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312; */

/*Table structure for table `secu_user` */

DROP TABLE IF EXISTS `secu_user`;

CREATE TABLE `secu_user` (
  `username` varchar(32) NOT NULL,
  `userpwd` varchar(256) NOT NULL,
  `userdesc` varchar(256) DEFAULT NULL,
  `roleId` bigint(20) NOT NULL,
  `ispermanentuser` bigint(20) NOT NULL,
  `validtime` bigint(20) DEFAULT NULL,
  `canuse` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `sys_properties` */

DROP TABLE IF EXISTS `sys_properties`;

CREATE TABLE `sys_properties` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(64) NOT NULL,
  `sub_category` varchar(64) DEFAULT NULL,
  `property` varchar(64) NOT NULL,
  `value` varchar(64) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_alg_param` */

DROP TABLE IF EXISTS `t_conf_alg_param`;

CREATE TABLE `t_conf_alg_param` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `m_ho_pwr_thd` int(11) NOT NULL,
  `m_ho_pwr_offset1` int(11) NOT NULL,
  `m_ho_pwr_offset2` int(11) NOT NULL,
  `m_cpe_cm_ho_period` int(11) NOT NULL,
  `m_ho_pwr_filtercoef_stat` int(11) NOT NULL,
  `m_ho_pwr_filtercoef_mobile` int(11) NOT NULL,
  `time_to_trigger` int(11) NOT NULL,
  `strictArea_pwr_thd` int(11) NOT NULL,
  `strictArea_time_to_trigger` int(11) NOT NULL,
  `strictArea_ho_pwr_offset` int(11) NOT NULL,
  `strictArea_2_normal_pwr_thres` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_antijamming_param` */

DROP TABLE IF EXISTS `t_conf_antijamming_param`;

CREATE TABLE `t_conf_antijamming_param` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `jumpFreqSwitch` int(11) NOT NULL,
  `jumpFreqCIMaxThreshold` int(11) NOT NULL,
  `jumpFreqCIAvgThreshold` int(11) NOT NULL,
  `jumpFreqOverThresholdTime` int(11) NOT NULL,
  `fallLevelSwitch` int(11) NOT NULL,
  `fallLevelMaxThreshold` int(11) NOT NULL,
  `fallLevelCIAvgThreshold` int(11) NOT NULL,
  `fallLevelOverThresholdTime` int(11) NOT NULL,
  `lowestLevel` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_backup_sag` */

DROP TABLE IF EXISTS `t_conf_backup_sag`;

CREATE TABLE `t_conf_backup_sag` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `sAGID` bigint(20) NOT NULL,
  `sAGIPforVoice` bigint(20) NOT NULL,
  `sAGIPforsignal` bigint(20) NOT NULL,
  `bSForceUseJitterbuffer` int(11) NOT NULL,
  `zModelUseJitterbuffer` int(11) NOT NULL,
  `jitterbufferSize` int(11) NOT NULL,
  `jitterbufferPackageThreshold` int(11) NOT NULL,
  `sAGVoiceTOS` int(11) NOT NULL,
  `sAGRxPortForVoice` int(11) NOT NULL,
  `sAGTxPortForVoice` int(11) NOT NULL,
  `sAGRxPortForSignal` int(11) NOT NULL,
  `sAGTxPortForSignal` int(11) NOT NULL,
  `locationAreaID` bigint(20) NOT NULL,
  `sAGSignalPointCode` int(11) NOT NULL,
  `bTSSignalPointCode` int(11) NOT NULL,
  `natAPKey` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_dn_info` */

DROP TABLE IF EXISTS `t_conf_dn_info`;

CREATE TABLE `t_conf_dn_info` (
  `parentid` bigint(20) NOT NULL,
  `rowindex` int(11) NOT NULL,
  `dn_prefix` varchar(11) NOT NULL,
  `dn_len` int(11) NOT NULL,
  PRIMARY KEY (`parentid`,`rowindex`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_fault_switch` */

DROP TABLE IF EXISTS `t_conf_fault_switch`;

CREATE TABLE `t_conf_fault_switch` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `flag` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_mbms_bts` */

DROP TABLE IF EXISTS `t_conf_mbms_bts`;

CREATE TABLE `t_conf_mbms_bts` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `flag` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_rcpe` */

DROP TABLE IF EXISTS `t_conf_rcpe`;

CREATE TABLE `t_conf_rcpe` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `work_mode` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_rcpe_item` */

DROP TABLE IF EXISTS `t_conf_rcpe_item`;

CREATE TABLE `t_conf_rcpe_item` (
  `parent_id` bigint(20) NOT NULL,
  `row_index` int(11) NOT NULL,
  `rcpe_uid` bigint(20) NOT NULL,
  `uid_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`parent_id`,`row_index`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_rcpe_item_copy` */

DROP TABLE IF EXISTS `t_conf_rcpe_item_copy`;

CREATE TABLE `t_conf_rcpe_item_copy` (
  `parent_id` bigint(20) NOT NULL,
  `row_index` int(11) NOT NULL,
  `rcpe_uid` bigint(20) NOT NULL,
  `uid_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`parent_id`,`row_index`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_remote_bts` */

DROP TABLE IF EXISTS `t_conf_remote_bts`;

CREATE TABLE `t_conf_remote_bts` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `flag` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_resmanagement` */

DROP TABLE IF EXISTS `t_conf_resmanagement`;

CREATE TABLE `t_conf_resmanagement` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `bandwidth_requst_interval` int(11) NOT NULL,
  `session_release_threshold` int(11) NOT NULL,
  `sustained_min_ul_signal_strength` int(11) NOT NULL,
  `max_dl_power_per_user` int(11) NOT NULL,
  `sustained_ul_bandwidth_per_user` int(11) NOT NULL,
  `sustained_dl_bandwidth_per_user` int(11) NOT NULL,
  `reserved_power_for_pc` int(11) NOT NULL,
  `pc_range` int(11) NOT NULL,
  `reassignment_step_size` int(11) NOT NULL,
  `max_simultaneous_user` int(11) NOT NULL,
  `reserved_tch_for_access` int(11) NOT NULL,
  `ul_modulation_mask` int(11) NOT NULL,
  `dl_modulation_mask` int(11) NOT NULL,
  `bandwidthClass0` int(11) DEFAULT NULL,
  `bandwidthClass1` int(11) DEFAULT NULL,
  `bandwidthClass2` int(11) DEFAULT NULL,
  `bandwidthClass3` int(11) DEFAULT NULL,
  `bandwidthClass4` int(11) DEFAULT NULL,
  `bandwidthClass5` int(11) DEFAULT NULL,
  `bandwidthClass6` int(11) DEFAULT NULL,
  `bandwidthClass7` int(11) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_system_freq` */

DROP TABLE IF EXISTS `t_conf_system_freq`;

CREATE TABLE `t_conf_system_freq` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `freqType` bigint(20) NOT NULL,
  `freq` bigint(20) NOT NULL,
  `doubleType` decimal(10,2) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_system_freqswitch` */

DROP TABLE IF EXISTS `t_conf_system_freqswitch`;

CREATE TABLE `t_conf_system_freqswitch` (
  `idx` bigint(20) NOT NULL,
  `freqVolInd` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_wcpe` */

DROP TABLE IF EXISTS `t_conf_wcpe`;

CREATE TABLE `t_conf_wcpe` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `work_mode` int(11) NOT NULL,
  `primary_wcpe` varchar(20) NOT NULL,
  `standby_wcpe` varchar(20) NOT NULL,
  `sac_mac` varchar(20) NOT NULL,
  `sac_mac2` varchar(20) NOT NULL DEFAULT '0',
  `sac_mac3` varchar(20) NOT NULL DEFAULT '0',
  `sac_mac4` varchar(20) NOT NULL DEFAULT '0',
  `sac_mac5` varchar(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_conf_weak_voice_fault` */

DROP TABLE IF EXISTS `t_conf_weak_voice_fault`;

CREATE TABLE `t_conf_weak_voice_fault` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `flag` int(11) NOT NULL,
  `voice_user_list_file` int(11) NOT NULL,
  `voice_user_list_file2` int(11) NOT NULL,
  `trunk_list_file` int(11) NOT NULL,
  `multi_call_idle_time` int(11) NOT NULL,
  `voice_max_time` int(11) NOT NULL,
  `multi_call_max_time` int(11) NOT NULL,
  `delay_interval` int(11) NOT NULL,
  `division_code` varchar(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_mo` */

DROP TABLE IF EXISTS `t_mo`;

CREATE TABLE `t_mo` (
  `moId` bigint(20) NOT NULL DEFAULT '0',
  `typeId` int(11) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `manageStateCode` int(11) DEFAULT NULL,
  PRIMARY KEY (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_mo_region` */

DROP TABLE IF EXISTS `t_mo_region`;

CREATE TABLE `t_mo_region` (
  `mo_id` bigint(20) NOT NULL DEFAULT '0',
  `region_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`mo_id`,`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `t_region` */

DROP TABLE IF EXISTS `t_region`;

CREATE TABLE `t_region` (
  `REGION_ID` bigint(20) NOT NULL,
  `NAME` varchar(64) NOT NULL,
  `DESCRIPTION` varchar(256) DEFAULT NULL,
  `PARENT_REGION_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`REGION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `terminal_param` */

DROP TABLE IF EXISTS `terminal_param`;

CREATE TABLE `terminal_param` (
  `idx` bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `userClass` int(11) NOT NULL,
  `upMaxBandWidth` int(11) NOT NULL,
  `upMinBandWidth` int(11) NOT NULL,
  `downMaxBandWidth` int(11) NOT NULL,
  `downMinBandWidth` int(11) NOT NULL,
  `minMaintainBWSwitch` int(11) NOT NULL,
  `upMinMaintainBW` int(11) NOT NULL,
  `downMinMaintainBW` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `terminal_version` */

DROP TABLE IF EXISTS `terminal_version`;

CREATE TABLE `terminal_version` (
  `typeId` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `fileName` varchar(50) NOT NULL,
  `fileType` varchar(50) NOT NULL,
  `update_dependency` int(1) DEFAULT NULL,
  `update_condition` int(1) DEFAULT NULL,
  PRIMARY KEY (`typeId`,`version`,`fileType`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `terminal_version_dl_history` */

DROP TABLE IF EXISTS `terminal_version_dl_history`;

CREATE TABLE `terminal_version_dl_history` (
  `id` int(11) NOT NULL,
  `btsId` int(11) NOT NULL,
  `typeId` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `fileType` int(10) NOT NULL DEFAULT 1,
  `actionResult` int(2) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312 ROW_FORMAT=DYNAMIC;

/*Table structure for table `terminal_version_type` */

DROP TABLE IF EXISTS `terminal_version_type`;

CREATE TABLE `terminal_version_type` (
  `id` int(11) NOT NULL,
  `type_name_zh` varchar(128) DEFAULT NULL,
  `type_name_en` varchar(128) DEFAULT NULL,
  `desc_zh` varchar(128) DEFAULT NULL,
  `desc_en` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `zk_basic` */

DROP TABLE IF EXISTS `zk_basic`;

CREATE TABLE `zk_basic` (
  `id` bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `host` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `zk_backup_task` */

DROP TABLE IF EXISTS `zk_backup_task`;

CREATE TABLE `zk_backup_task` (
  `id` int(11) NOT NULL,
  `intval` varchar(20) NOT NULL,
  `lastTime` varchar(128) DEFAULT NULL,
  `state` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=gb2312;


DROP TABLE IF EXISTS `sxc_basic`;

CREATE TABLE `sxc_basic` (
  `moId` bigint(20) NOT NULL,
  `sagName` varchar(32) NOT NULL,
  `sagId` bigint(20) NOT NULL,
  `sagSignalPointCode` bigint(20) NOT NULL,
  `sagVoiceIp` varchar(32) NOT NULL,
  `sagSignalIp` varchar(32) NOT NULL,
  `sagDefaultGateway` varchar(32) NOT NULL,
  `sagSubnetMask` varchar(32) NOT NULL,
  PRIMARY KEY (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

DROP TABLE IF EXISTS `mcbts_antenna_lock`;

CREATE TABLE `mcbts_antenna_lock` (
`idx`  bigint(20) NOT NULL ,
`moId`  bigint(20) NOT NULL ,
`flag`  tinyint(4) NOT NULL ,
 PRIMARY KEY (`idx`),
 UNIQUE KEY `moIdIndex` (`moId`)
)
ENGINE=InnoDB DEFAULT CHARSET=gb2312;

DROP TABLE IF EXISTS `microbts_signal_send`;

CREATE TABLE `microbts_signal_send` (
  `idx` bigint(20) NOT NULL ,
  `moId` bigint(20) DEFAULT NULL,
  `sendMode` tinyint(4) DEFAULT NULL,
  `antIndex` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

DROP TABLE IF EXISTS `mcbts_ethernet2opration`;

CREATE TABLE `mcbts_ethernet2opration` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `RoutingareaID2` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

DROP TABLE IF EXISTS `mcbts_ipv6operation`;

CREATE TABLE `mcbts_ipv6operation` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `learnedBridgeTimer` bigint(20) NOT NULL,
  `ingressNDProxy` bigint(20) NOT NULL,
  `egressNDProxy` bigint(20) NOT NULL,
  `egressBroadCastFilter` bigint(20) NOT NULL,
   `ipv6Switch` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;


DROP TABLE IF EXISTS `mcbts_sdma`;
CREATE TABLE `mcbts_sdma` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `sdmaSwitch` int(10) NOT NULL,
  `voiceSwitch` int(10) NOT NULL,
  `videoSwitch` int(10) NOT NULL,
  `subChType` int(10) NOT NULL,
  `inThreshold` int(10) NOT NULL,
  `outThreshold` int(10) NOT NULL,
  `userMaxMatcher` int(10) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

DROP TABLE IF EXISTS `mcbts_data_package_filter`;
CREATE TABLE `mcbts_data_package_filter` (
  `idx` bigint(20) NOT NULL,
  `ip` varchar(20) default NULL,
  `port` int(10) default NULL,
  `filterType` int(20) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;


/*Table structure for table `mcbts_switchoptimize` */

DROP TABLE IF EXISTS `mcbts_switchoptimize`;

CREATE TABLE `mcbts_switchoptimize` (
`idx`  bigint(20) NOT NULL ,
`switchFlag`  tinyint(4) NOT NULL ,
PRIMARY KEY (`idx`)
)ENGINE=InnoDB DEFAULT CHARSET=gb2312;


/*Table structure for table `mcbts_upgrade_info` */

DROP TABLE IF EXISTS `mcbts_upgrade_info`;

CREATE TABLE `mcbts_upgrade_info` (
  `idx` BIGINT(20) NOT NULL,
  `moId` BIGINT(20) NOT NULL,
  `btsId` VARCHAR(10) NOT NULL,
  `btsType` INT(4) NOT NULL,
  `status` INT(4) NOT NULL,
  `downloadVersion` VARCHAR(20) NOT NULL,
  `version` VARCHAR(20) NOT NULL,
  `mcuVersion` VARCHAR(20) DEFAULT NULL,
  `fpgaVersion` VARCHAR(20) DEFAULT NULL,
  `target` INT(2) DEFAULT NULL,
  `startSign` INT(2) NOT NULL,
  `transactionId` bigint(20) NOT NULL,
  `scheduledTime` DATETIME NOT NULL,
  `startTime` DATETIME DEFAULT NULL,
  `endTime` DATETIME DEFAULT NULL,
  `errorCode` INT(4) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;


/*Table structure for table `mcbts_upgrade_info_archive` */

DROP TABLE IF EXISTS `mcbts_upgrade_info_archive`;

CREATE TABLE `mcbts_upgrade_info_archive` (
  `idx` BIGINT(20) NOT NULL,
  `moId` BIGINT(20) NOT NULL,
  `btsId` VARCHAR(10) NOT NULL,
  `btsType` INT(4) NOT NULL,
  `status` INT(4) NOT NULL,
  `downloadVersion` VARCHAR(20) NOT NULL,
  `version` VARCHAR(20) NOT NULL,
  `mcuVersion` VARCHAR(20) DEFAULT NULL,
  `fpgaVersion` VARCHAR(20) DEFAULT NULL,
  `target` INT(2) DEFAULT NULL,
  `startSign` INT(2) NOT NULL,
  `scheduledTime` DATETIME NOT NULL,
  `startTime` DATETIME DEFAULT NULL,
  `endTime` DATETIME DEFAULT NULL,
  `errorCode` INT(4) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

DROP TABLE IF EXISTS `terminal_type_mgmt`;
CREATE TABLE `terminal_type_mgmt` (
  `idx` int(11) NOT NULL,
  `terminalType` varchar(50) NOT NULL,
  `terminalTypeDesc` varchar(50) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `district` */

DROP TABLE IF EXISTS `district`;

CREATE TABLE `district` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `type` INT(1) NOT NULL,
  `parentId` BIGINT(20) NOT NULL,
  `description` VARCHAR(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/*Table structure for table `simulcast` */

DROP TABLE IF EXISTS `simulcast`;

CREATE TABLE `simulcast` (
  `idx` BIGINT(20) NOT NULL,
  `districtId` BIGINT(20) NOT NULL,
  `freqType` INT(10) NOT NULL,
  `freqOffset` INT(10) NOT NULL,
  `freq` DECIMAL(10,2) NOT NULL,
  `minNum` INT(2) NOT NULL,
  `maxNum` INT(2) NOT NULL,
  `maxMbmsNum` INT(2) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `minas_operation`
-- ----------------------------
DROP TABLE IF EXISTS `minas_operation`;
CREATE TABLE `minas_operation` (
  `operName` varchar(64) NOT NULL,
  `operDesc` varchar(512) NOT NULL,
  PRIMARY KEY (`operName`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `minas_oper_action`
-- ----------------------------
DROP TABLE IF EXISTS `minas_oper_action`;
CREATE TABLE `minas_oper_action` (
  `facade` varchar(256) NOT NULL,
  `method` varchar(64) NOT NULL,
  `operName` varchar(64) NOT NULL,
  `action` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `minas_biz_operation`
-- ----------------------------
DROP TABLE IF EXISTS `minas_biz_operation`;
CREATE TABLE `minas_biz_operation` (
  `operName` varchar(64) NOT NULL,
  `bizName` varchar(64) NOT NULL,
  PRIMARY KEY (`bizName`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `minas_role`
-- ----------------------------
DROP TABLE IF EXISTS `minas_role`;
CREATE TABLE `minas_role` (
  `roleId` int(11) NOT NULL,
  `roleName` varchar(20) NOT NULL,
  `roleDesc` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `role_privilege`
-- ----------------------------
DROP TABLE IF EXISTS `role_privilege`;
CREATE TABLE `role_privilege` (
  `roleId` int(11) NOT NULL,
  `operName` varchar(64) NOT NULL,
  `actions` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`roleId`,`operName`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;


-- ----------------------------
-- Table structure for `mcbts_ut_debug_upload`
-- ----------------------------
DROP TABLE IF EXISTS `mcbts_ut_debug_upload`;

CREATE TABLE `mcbts_ut_debug_upload` (
  `idx` bigint(20) NOT NULL,
  `pid` bigint(20) NOT NULL,
  `switchFlag` int(11) NOT NULL,
  `distance` bigint(20) NOT NULL,
  `lastStartTime` timestamp NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;


-- ----------------------------
-- Table structure for system_order
-- ----------------------------
DROP TABLE IF EXISTS `xw_system_order`;

CREATE TABLE `xw_system_order` (
  `order_id` varchar(64) NOT NULL,
  `order_type` varchar(32) DEFAULT NULL,
  `order_cmd` varchar(64) DEFAULT NULL,
  `customer_id` int(15) DEFAULT NULL,
  `subscriber_id` int(15) DEFAULT NULL,
  `run_status` int(8) DEFAULT NULL,
  `order_result` int(8) DEFAULT NULL,
  `is_schedule` int(8) DEFAULT NULL,
  `schedule_param` varchar(64) DEFAULT NULL,
  `operator_name` varchar(64) DEFAULT NULL,
  `channel` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `place` varchar(128) DEFAULT NULL,
  `update_datetime` datetime DEFAULT NULL,
  `remark` varchar(10240) DEFAULT NULL,
  `total_price` double(22,2) DEFAULT NULL,
  `parent_id` varchar(64) DEFAULT NULL,
  `cmd_object` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

DROP TABLE IF EXISTS `xw_sequence`;

CREATE TABLE `xw_sequence` (
  `seq_name` varchar(64) NOT NULL,
  `init_value` int(16) NOT NULL,
  `max_value` int(16) NOT NULL,
  `current_value` int(16) NOT NULL,
  `seq_step` int(8) NOT NULL,
  `table_circle_size` int(8) DEFAULT NULL,
  PRIMARY KEY (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

/* Procedure of create StatData tables */

DELIMITER $$

DROP PROCEDURE IF EXISTS createStatDataTables$$

CREATE PROCEDURE createStatDataTables () 
BEGIN
  DECLARE TABLE_NAME VARCHAR (50) ;
  DECLARE dropStr VARCHAR (100) ;
  DECLARE createStr VARCHAR (1000) ;
  DECLARE tableIndex INT ;
  DECLARE timeIndex INT ;
  DECLARE timeArray VARCHAR (100) ;
  DECLARE timeName VARCHAR (10) ;
  SET @tableIndex = 0 ;
  SET @timeIndex = 1 ;
  SET @timeArray = 'realtime,daily,week,month,year' ;
  WHILE
    @timeIndex <= 5 DO SET @tableIndex = 0 ;
    WHILE
      @tableIndex < 5 DO SET @timeName = SUBSTRING_INDEX(
        SUBSTRING_INDEX(@timeArray, ',', @timeIndex),
        ',',
        - 1
      ) ;
      SET @TABLE_NAME = CONCAT(
        'mcbts_stat_data_',
        @timeName,
        '_',
        @tableIndex
      ) ;
      SET @dropStr = CONCAT(
        'DROP TABLE IF EXISTS ',
        @TABLE_NAME,
        ';'
      ) ;
      SET @createStr = CONCAT(
        'CREATE TABLE ',
        @TABLE_NAME,
        '(
  `btsId` bigint(20) NOT NULL,
  `collectTime` bigint(20) NOT NULL,
  `intval` bigint(20) NOT NULL,
  `timeType` int(11) NOT NULL,
  `item0` double DEFAULT NULL,
  `item1` double DEFAULT NULL,
  `item2` double DEFAULT NULL,
  `item3` double DEFAULT NULL,
  `item4` double DEFAULT NULL,
  `item5` double DEFAULT NULL,
  `item6` double DEFAULT NULL,
  `item7` double DEFAULT NULL,
  `item8` double DEFAULT NULL,
  `item9` double DEFAULT NULL,
  `item10` double DEFAULT NULL,
  `item11` double DEFAULT NULL,
  `item12` double DEFAULT NULL,
  `item13` double DEFAULT NULL,
  `item14` double DEFAULT NULL,
  `item15` double DEFAULT NULL,
  `item21` double DEFAULT NULL,
  `item22` double DEFAULT NULL,
  `item23` double DEFAULT NULL,
  `item24` double DEFAULT NULL,
  `item25` double DEFAULT NULL,
  `item31` double DEFAULT NULL,
  `item32` double DEFAULT NULL,
  `item33` double DEFAULT NULL,
  `item34` double DEFAULT NULL,
  `item35` double DEFAULT NULL,
  `item41` double DEFAULT NULL,
  `item42` double DEFAULT NULL,
  `item43` double DEFAULT NULL,
  `item44` double DEFAULT NULL,
  `item45` double DEFAULT NULL,
  `item51` double DEFAULT NULL,
  `item52` double DEFAULT NULL,
  `item53` double DEFAULT NULL,
  `item54` double DEFAULT NULL,
  `item55` double DEFAULT NULL,
  `item61` double DEFAULT NULL,
  `item62` double DEFAULT NULL,
  `item63` double DEFAULT NULL,
  `item64` double DEFAULT NULL,
  `item65` double DEFAULT NULL,
  `item71` double DEFAULT NULL,
  `item72` double DEFAULT NULL,
  `item73` double DEFAULT NULL,
  `item74` double DEFAULT NULL,
  `item75` double DEFAULT NULL,
  `item81` double DEFAULT NULL,
  `item82` double DEFAULT NULL,
  `item83` double DEFAULT NULL,
  `item84` double DEFAULT NULL,
  `item85` double DEFAULT NULL,
  `reserve1` double DEFAULT NULL,
  `reserve2` double DEFAULT NULL,
  `reserve3` double DEFAULT NULL,
  `reserve4` double DEFAULT NULL,
  `reserve5` double DEFAULT NULL,
  `reserve6` double DEFAULT NULL,
  `reserve7` double DEFAULT NULL,
  `reserve8` double DEFAULT NULL,
  `reserve9` double DEFAULT NULL,
  `reserve10` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;'
      ) ;
      PREPARE droptable FROM @dropStr ;
      PREPARE sqltable FROM @createStr ;
      EXECUTE droptable ;
      EXECUTE sqltable ;
      SET @tableIndex = @tableIndex + 1 ;
    END WHILE ;
    SET @timeIndex = @timeIndex + 1 ;
  END WHILE ;
END $$

DELIMITER ;

CALL createStatDataTables () ;

-- ----------------------------
-- Table structure for `mcbts_stat_detail`
-- ----------------------------
DROP TABLE IF EXISTS `mcbts_stat_detail`;
CREATE TABLE `mcbts_stat_detail` (
  `timeType` int(11) NOT NULL,
  `statTime` bigint(20) NOT NULL,
  `intval` bigint(20) NOT NULL,
  `flag` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `mcbts_commonchannel_failed`
-- ----------------------------
DROP TABLE IF EXISTS `mcbts_commonchannel_failed`;
CREATE TABLE `mcbts_commonchannel_failed` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `synTimes` int(11) NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `moIdIndex` (`moId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312; 

-- ----------------------------
-- Table structure for `mcbts_ut_upgrade_batch_result`
-- ----------------------------
DROP TABLE IF EXISTS `mcbts_ut_upgrade_batch_result`;
CREATE TABLE `mcbts_ut_upgrade_batch_result` (
  `idx` bigint(20) NOT NULL,
  `pid` varchar(64) NOT NULL,
  `bts_id` bigint(20) NOT NULL,
  `upgrade_time` timestamp NULL DEFAULT NULL,
  `ut_type` varchar(64) DEFAULT NULL,
  `ut_target_version` varchar(64) DEFAULT NULL,
  `ut_upgrade_flag` int(1) NOT NULL,
  `ut_upgrade_desc` varchar(128) DEFAULT NULL,
  `ut_upgrade_progress` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `power_supply`
-- ----------------------------
DROP TABLE IF EXISTS `power_supply`;
CREATE TABLE `power_supply` (
  `idx` bigint(20) NOT NULL,
  `ipAddress` varchar(32) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `currentType` int(11) DEFAULT NULL,
  `factory` int(11) DEFAULT NULL,
  `pollInterval` int(11) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `mcbts_powerconfig_relative`
-- ----------------------------
DROP TABLE IF EXISTS `mcbts_powerconfig_relative`;
CREATE TABLE `mcbts_powerconfig_relative` (
  `idx` bigint(20) NOT NULL,
  `power_id` bigint(20) DEFAULT NULL,
  `mo_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312; 


-- ----------------------------
-- Table structure for `enb_basic`
-- ----------------------------
DROP TABLE IF EXISTS `enb_basic`;
CREATE TABLE `enb_basic` (
  `moId` bigint(20) NOT NULL,
  `enbId` bigint(20) NOT NULL,
  `name` varchar(64) NOT NULL,
  `protocolVersion` varchar(20) NOT NULL,
  `privateIp` varchar(32) DEFAULT NULL,
  `manageState` int(11) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `enbType` int(11) NOT NULL,
  `typeId` int(11) NOT NULL,
  `syncDirection` int(11) NOT NULL,
  `monitorState` tinyint(4) NOT NULL,
  PRIMARY KEY (`moId`),
  UNIQUE KEY `PK_ENB_ID` (`enbId`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `enb_biz_table`
-- ----------------------------
DROP TABLE IF EXISTS `enb_biz_table`;
CREATE TABLE `enb_biz_table` (
  `mo_id` bigint(20) NOT NULL,
  `biz_name` varchar(128) NOT NULL,
  `biz_key` varchar(256) NOT NULL,
  `biz_parameter` varchar(8192) NOT NULL,
  PRIMARY KEY (`mo_id`,`biz_name`,`biz_key`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `enb_full_table_config`
-- ----------------------------
DROP TABLE IF EXISTS `enb_full_table_config`;
CREATE TABLE `enb_full_table_config` (
  `idx` BIGINT(20) NOT NULL,
  `moId` BIGINT(20) DEFAULT NULL,
  `configureStatus` INT(11) DEFAULT NULL,
  `startConfigTime` TIMESTAMP NULL DEFAULT NULL,
  `errorMessage` VARCHAR(256) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;


-- ----------------------------
-- Table structure for `enb_kpi_config`
-- ----------------------------
DROP TABLE IF EXISTS `enb_kpi_config`;
CREATE TABLE `enb_kpi_config` (
  `kpiId` int(11) NOT NULL,
  `statObject` varchar(32) NOT NULL,
  `perfType` varchar(32) NOT NULL,
  `kpiName_zh` varchar(256) NOT NULL,
  `kpiName_en` varchar(256) NOT NULL,
  `kpiDesc_zh` varchar(512) NOT NULL,
  `kpiDesc_en` varchar(512) NOT NULL,
  `unit` varchar(32) NOT NULL,
  `dataType` varchar(32) NOT NULL,
  `timeType` varchar(32) NOT NULL,
  `spaceType` varchar(32) NOT NULL,
  `exp` varchar(256) DEFAULT NULL,
  `defaultValue` decimal(50,4) NOT NULL,
  `remark` int(11) DEFAULT NULL,
  PRIMARY KEY (`kpiId`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;


-- ----------------------------
-- Table structure for `enb_counter_config`
-- ----------------------------
DROP TABLE IF EXISTS `enb_counter_config`;
CREATE TABLE `enb_counter_config` (
  `counterId` int(11) NOT NULL,
  `statObject` varchar(32) NOT NULL,
  `reportSystem` varchar(32) NOT NULL,
  `measureType` varchar(32) NOT NULL,
  `measureEvent` varchar(64) NOT NULL,
  `counterName_zh` varchar(256) NOT NULL,
  `counterName_en` varchar(256) NOT NULL,
  `counterDesc_zh` varchar(512) NOT NULL,
  `counterDesc_en` varchar(512) NOT NULL,
  `unit` varchar(32) NOT NULL,
  `dataType` varchar(32) NOT NULL,
  `timeType` varchar(32) NOT NULL,
  `spaceType` varchar(32) NOT NULL,
  `exp` varchar(256) DEFAULT NULL,
  `remark` int(11) DEFAULT NULL,
  PRIMARY KEY (`counterId`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `enb_study_dataconfig`
-- ----------------------------
DROP TABLE IF EXISTS `enb_study_dataconfig`;
CREATE TABLE `enb_study_dataconfig` (
  `version` varchar(20) NOT NULL,
  `dataconfig` mediumtext NOT NULL,
  PRIMARY KEY (`version`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `enb_biz_template`
-- ----------------------------
DROP TABLE IF EXISTS `enb_biz_template`;
CREATE TABLE `enb_biz_template` (
  `enb_type` int(11) NOT NULL default 0,
  `version` varchar(20) NOT NULL,
  `biz_name` varchar(128) NOT NULL,
  `biz_key` varchar(256) NOT NULL,
  `biz_parameter` varchar(8192) NOT NULL,
  PRIMARY KEY (`enb_type`, `version`,`biz_name`,`biz_key`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;


-- ----------------------------
-- Table structure for `enb_realtime_config`
-- ----------------------------
DROP TABLE IF EXISTS `enb_realtime_config`;
CREATE TABLE `enb_realtime_config` (
  `itemId` int(11) NOT NULL,
  `tagId` int(11) NOT NULL,
  `statObject` varchar(32) NOT NULL,
  `reportSystem` varchar(32) DEFAULT NULL,
  `measureType` varchar(32) DEFAULT NULL,
  `measureEvent` varchar(64) DEFAULT NULL,
  `name_zh` varchar(256) NOT NULL,
  `name_en` varchar(256) NOT NULL,
  `desc_zh` varchar(512) DEFAULT NULL,
  `desc_en` varchar(512) DEFAULT NULL,
  `exp` varchar(32) DEFAULT NULL,
  `unit` varchar(32) DEFAULT NULL,
  `dataType` varchar(32) NOT NULL,
  `timeType` varchar(32) NOT NULL,
  `spaceType` varchar(32) NOT NULL,
  PRIMARY KEY (`itemId`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `ta_table`
-- ----------------------------
DROP TABLE IF EXISTS `ta_table`;
CREATE TABLE `ta_table` (
  `id` int(11) NOT NULL,
  `code` varchar(16) NOT NULL,
  `remark` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `enb_neighbour_relation`
-- ----------------------------
DROP TABLE IF EXISTS `enb_neighbour_relation`;
CREATE TABLE `enb_neighbour_relation` (
  `idx` bigint(20) NOT NULL,
  `moId` bigint(20) NOT NULL,
  `srvCellId` int(11) NOT NULL,
  `enbId` bigint(20) NOT NULL,
  `cellId` int(11) NOT NULL,
  `isNeighbour` tinyint(4) NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `enb_biz_cell`
-- ----------------------------
DROP TABLE IF EXISTS `enb_cell_basic`;
CREATE TABLE `enb_cell_basic` (
  `mo_id` bigint(20) NOT NULL,
  `cid` int(10) NOT NULL,
  `biz_parameter` varchar(8192) NOT NULL,
  PRIMARY KEY (`mo_id`,`cid`)
) ENGINE=INNODB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Table structure for `enb_asset_table`
-- ----------------------------
DROP TABLE IF EXISTS `enb_asset_table`;
CREATE TABLE `enb_asset_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `production_sn` varchar(40) NOT NULL,
  `enbId` bigint(20) NOT NULL,
  `hardware_version` varchar(40) NOT NULL,
  `status` int(1) NOT NULL,
  `node_type` int(4) NOT NULL,
  `location_info` varchar(10) NOT NULL,
  `provider_name` varchar(20) DEFAULT NULL,
  `manufacture_date` bigint(16) DEFAULT '0',
  `start_time` bigint(16) NOT NULL,
  `stop_time` bigint(16) DEFAULT '0',
  `last_serve_time` bigint(16) DEFAULT NULL,
  `remark` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=gb2312;
ALTER TABLE `enb_asset_table` ADD INDEX index_enbid(`enbId`);
ALTER TABLE `enb_asset_table` ADD INDEX index_production_sn(`production_sn`);


-- ----------------------------
-- Table structure for `enb_asset_history_table`
-- ----------------------------
DROP TABLE IF EXISTS `enb_asset_history_table`;
CREATE TABLE `enb_asset_history_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `production_sn` varchar(40) NOT NULL,
  `enbId` bigint(20) NOT NULL,
  `hardware_version` varchar(40) NOT NULL,
  `node_type` int(4) NOT NULL,
  `location_info` varchar(10) NOT NULL,
  `provider_name` varchar(20) DEFAULT NULL,
  `manufacture_date` bigint(16) DEFAULT '0',
  `start_time` bigint(16) NOT NULL,
  `stop_time` bigint(16) DEFAULT '0',
  `confirm_stop_time` bigint(16) NOT NULL,
  `confirm_user` varchar(32) NOT NULL,
  `last_serve_time` bigint(16) DEFAULT NULL,
  `remark` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=gb2312;
ALTER TABLE `enb_asset_history_table` ADD INDEX index_enbid(`enbId`);
ALTER TABLE `enb_asset_history_table` ADD INDEX index_production_sn(`production_sn`);




