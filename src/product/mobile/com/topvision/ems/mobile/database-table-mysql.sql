/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2012-2-13 9:43:17                            */
/*==============================================================*/

-- version 2.4.6.0,build 2014-12-25,module mobile
create table mobileDeviceType (
typeId bigint(20) auto_increment not null,
deviceType varchar(255),
corporation varchar(255),
frequency varchar(1024),
powerlevel varchar(512),
primary key (typeId)
);
create table mobileDefaultDeviceType (
typeId bigint(20)
);
/*-- version 2.4.6.0,build 2014-12-25,module mobile*/

-- version 2.9.0.8,build 2017-7-20,module mobile
CREATE TABLE onupreconfiginfo (
  uniqueId varchar(255) NOT NULL,
  name varchar(50) DEFAULT NULL,
  wanId tinyint(4) DEFAULT NULL,
  pppoeName varchar(50) DEFAULT NULL,
  pppoePwd varchar(50) DEFAULT NULL,
  ssid tinyint(4) DEFAULT NULL,
  wifiName varchar(50) DEFAULT NULL,
  wifiPwd varchar(50) DEFAULT NULL,
  contact varchar(50) DEFAULT NULL,
  phoneNo varchar(50) DEFAULT NULL,
  address varchar(255) DEFAULT NULL,
  longitude decimal(17,14) DEFAULT NULL,
  latitude decimal(17,14) DEFAULT NULL,
  time timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  modifyTime timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  preconfig tinyint(1) DEFAULT NULL,
  current tinyint(1) DEFAULT NULL,
  PRIMARY KEY (uniqueId)
);
CREATE TABLE onuopenreport (
  uniqueId varchar(50) NOT NULL,
  pppoeParamSet varchar(255) DEFAULT NULL,
  wifiParamSet varchar(255) DEFAULT NULL,
  onuOnlineState varchar(255) DEFAULT NULL,
  onuOptical varchar(255) DEFAULT NULL,
  wanState varchar(255) DEFAULT NULL,
  internetConnect varchar(255) DEFAULT NULL,
  wifiState varchar(255) DEFAULT NULL,
  nm3000Connect varchar(255) DEFAULT NULL,
  nm3000Control varchar(255) DEFAULT NULL,
  time timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (uniqueId)
);
CREATE TABLE terminalpageview (
  id bigint(255) NOT NULL AUTO_INCREMENT,
  uuid char(36) NOT NULL,
  url varchar(255) DEFAULT NULL,
  startTime timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  closedTime timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  opener varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE terminalvisitview (
  uuid char(36) NOT NULL,
  startupTime timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  endTime timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  lastView varchar(50) DEFAULT NULL,
  deviceId varchar(50) NOT NULL,
  deviceModel varchar(50) DEFAULT NULL,
  deviceVendor varchar(50) DEFAULT NULL,
  deviceVersion varchar(50) DEFAULT NULL,
  PRIMARY KEY (uuid)
);
/*-- version 2.9.0.8,build 2017-7-20,module mobile*/

-- version 2.10.0.0,build 2018-5-30,module mobile
CREATE TABLE `cmprogramimg` (
  `mac` varchar(20) NOT NULL,
  `url` varchar(100) NOT NULL,
  PRIMARY KEY (`mac`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*-- version 2.10.0.0,build 2018-5-30,module mobile*/