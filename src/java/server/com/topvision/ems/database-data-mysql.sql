/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-3-31,module server
INSERT INTO FolderCategory(categoryId,name) values(10,'EPON拓扑图');
INSERT INTO FolderCategory(categoryId,name) values(20,'CMTS拓扑图');

INSERT INTO TopoFolder(folderId,superiorId,categoryId,name,type,note,createTime,modifyTime,displayGrid,backgroundImg,backgroundColor,backgroundFlag,backgroundPosition,linkColor,linkSelectedColor,linkWidth,linkStartArrow,linkEndArrow,linkShadow,x,y,zoom,refreshInterval,icon,url,path,fixed,entityLabel,linkLabel,displayLinkLabel,displayName,displayNoSnmp,displayRouter,displaySwitch,displayL3switch,displayServer,displayDesktop,displayOthers,entityForOrgin,depthForOrgin,extend1,extend2,subnetIp,subnetMask) values
                (10,1,10,'WorkBench.topology10',20,'',now(), now(), '0', '../images/background/world.jpg','#ffffff','1',2,'#000000',NULL,1,NULL,NULL,'1',NULL,NULL,1,60000,NULL,NULL,'1/5/','0','cpu','linkrate','1','2','1','1','1','1','1','1','1',0,1,NULL,NULL,NULL,NULL);
              
INSERT INTO FolderUserGroupRela(folderId,userGroupId,power) values
                (10,1,NULL);
                
INSERT INTO TopoLabel(labelId,displayName,value,color,module,folderId) values
                ('cpu0','80',80,'#FF0000','cpu',NULL),
                ('cpu1','70',70,'#FF8000','cpu',NULL),
                ('cpu2','60',60,'#88E4E5','cpu',NULL),
                ('cpu3','0',0,'#96e54d','cpu',NULL),
                ('linkflow0','80',80,'#FF0000','linkflow',NULL),
                ('linkflow1','50',50,'#FF8000','linkflow',NULL),
                ('linkflow2','5',5,'#88e4e5','linkflow',NULL),
                ('linkflow3','0',0,'#14a600','linkflow',NULL),
                ('linkrate0','100M',104857600,'#FF0000','linkrate',NULL),
                ('linkrate1','10M',10485760,'#FF8000','linkrate',NULL),
                ('linkrate2','1M',1048576,'#0000FF','linkrate',NULL),
                ('linkrate3','0',0,'#14a600','linkrate',NULL),
                ('mem0','80',80,'#FF0000','mem',NULL),
                ('mem1','50',50,'#FF8000','mem',NULL),
                ('mem2','30',30,'#88e4e5','mem',NULL),
                ('mem3','0',0,'#96e54d','mem',NULL);

INSERT INTO Services(name,port,timeout,protocol,note,scaned,type) values
                ('DNS',53,1,'tcp','Domain Name System','1',0),
                ('Echo',7,1,'tcp','Echo','1',0),
                ('Finger',79,1,'tcp','Finger','1',0),
                ('FTP',21,1,'tcp','FTP','1',0),
                ('Https',443,1,'tcp','Https','1',0),
                ('IMAP',143,1,'tcp','IMAP','1',3),
                ('LDAP',389,1,'tcp','LDAP','1',0),
                ('MSSQL',1433,1,'tcp','MS SQLServer','1',1),
                ('MySQL',3306,1,'tcp','MySQL','1',1),
                ('NNTP',119,1,'tcp','NNTP','1',0),
                ('Oracle',1521,1,'tcp','Oracle','1',1),
                ('POP3',110,1,'tcp','POP3','1',3),
                ('SMTP',25,1,'tcp','SMTP','1',3),
                ('SSH',22,1,'tcp','SSH','1',0),
                ('Telnet',23,1,'tcp','Telnet','1',0),
                ('Web',80,1,'tcp','Web','1',0),
                ('WebLogic',7001,1,'tcp','WebLogic','1',2);

/* 模板初始化 */
/* 公司 */
INSERT INTO EntityCorp(corpId, name, displayName, sysObjectID) VALUES
                (1, 'Unknown', '', '0'),
                (2, 'IBM', 'IBM', '1.3.6.1.4.1.2'),
                (4, 'Unix', 'Unix', '1.3.6.1.4.1.4'),
                (9,  'Cisco', '思科', '1.3.6.1.4.1.9'),
                (23,  'Novell', 'Novell', '1.3.6.1.4.1.23'),
                (28,  'Interlan', 'Interlan', '1.3.6.1.4.1.28'),
                (32,  'SCO', 'SCO', '1.3.6.1.4.1.32'),
                (42,  'Sun', 'Sun', '1.3.6.1.4.1.42'),
                (43,  '3Com', '3Com', '1.3.6.1.4.1.43'),
                (63,  'Apple', '苹果', '1.3.6.1.4.1.63'),
                (94,  'Nokia', '诺基亚', '1.3.6.1.4.1.94'),
                (111,  'Oracle', 'Oracle', '1.3.6.1.4.1.111'),
                (122,  'Sony', '索尼', '1.3.6.1.4.1.122'),
                (161,  'Motorola', '摩托罗拉', '1.3.6.1.4.1.161'),
                (171,  'DLink', 'DLink', '1.3.6.1.4.1.171'),
                (311,  'Microsoft', '微软', '1.3.6.1.4.1.311'),
                (674,  'Dell', '戴尔', '1.3.6.1.4.1.674'),
                (1991,  'Foundry', 'Foundry', '1.3.6.1.4.1.1991'),
                (2011, 'Huawei', '华为', '1.3.6.1.4.1.2011'),
                (2636,  'Juniper', 'Juniper', '1.3.6.1.4.1.2636'),
                (3955,  'Linksys', 'Linksys', '1.3.6.1.4.1.3955'),
                (6527,  'Alcatel-Lucent', '阿尔卡特朗讯', '1.3.6.1.4.1.6527'),
                (8072,  'NET-SNMP', 'NET-SNMP', '1.3.6.1.4.1.8072'),
                (12284,  'Lenovo', '联想', '1.3.6.1.4.1.12284'),
                (25506,  'H3C', 'H3C', '1.3.6.1.4.1.25506'),
                (32285,  'Sumavision', '数码视讯', '1.3.6.1.4.1.32285');

/*Alert Levels*/
INSERT INTO Levels(levelId,name,note,active,icon,color,sound) values
                (6,'WorkBench.emergencyAlarm',NULL,'1','images/fault/level6.gif',NULL,NULL),
                (5,'WorkBench.seriousAlarm',NULL,'1','images/fault/level5.gif',NULL,NULL),
                (4,'WorkBench.mainAlarm',NULL,'1','images/fault/level4.gif',NULL,NULL),
                (3,'WorkBench.minorAlarm',NULL,'1','images/fault/level3.gif',NULL,NULL),
                (2,'WorkBench.generalAlarm',NULL,'1','images/fault/level2.gif',NULL,NULL),
                (1,'WorkBench.message',NULL,'1','images/fault/level1.gif',NULL,NULL);

/*Alert Comment*/
INSERT INTO AlertComment(commentId,name,note) values
                (1,'已经解决',NULL),
                (2,'网络问题',NULL),
                (3,'暂时维护',NULL);

/*Action Type*/
INSERT INTO ActionType(actionTypeId,name,displayName,enabled,actionClass,params) values
                (1,'Email','邮件',1,'com.topvision.platform.system.service.impl.MailActionServiceImpl',NULL),
                (2,'Sms','短信',1,'com.topvision.platform.system.service.impl.SmsActionServiceImpl',NULL),
                (3,'Trap','陷阱',1,'com.topvision.ems.fault.service.impl.TrapServiceImpl',NULL);

/*Data for the table AlertType */
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-10000,0,'Device Alert','ALERT.deviceAlert',0,'',0,0,'0','1',NULL,NULL),
              (-20000,0,'Qos Alert','ALERT.qosAlert',0,'',0,0,'0','1',NULL,NULL),
              (-30000,0,'Communication Alert','ALERT.communicationAlert',0,'',0,0,'0','1',NULL,NULL),
--            (-40000,0,'System Alert','ALERT.systemAlert',0,'',0,0,'0','1',NULL,NULL)
--            (-50000,0,'Handle Failed Alert','ALERT.handleFailedAlert',0,'',0,0,'0','1',NULL,NULL),
              (-60000,0,'Environment Alert','ALERT.environmentAlert',0,'',0,0,'0','1',NULL,NULL);
              

INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
--            (-101,-10000,'SNMP Alert','ALERT.snmpAlert',6,'',0,0,'0','1',NULL,NULL),
--            (-102,-10000,'New Device Found Notify','ALERT.newDeviceFoundNotify',1,'',0,0,'0','1',NULL,NULL),
              (-201,-20000,'Connectivity Delay Alert','ALERT.connectivityDelayAlert',1,'',0,0,'0','1',NULL,NULL),
              (-202,-20000,'CPU Alert','ALERT.cpuAlert',4,'',0,0,'1','1',NULL,NULL),
              (-203,-20000,'MEM Alert','ALERT.memAlert',4,'',0,0,'1','1',NULL,NULL),
              (-204,-20000,'DISK Alert','ALERT.diskAlert',4,'',0,0,'1','1',NULL,NULL),
              (-301,-30000,'DisConnectivity Alert','ALERT.disConnectivityAlert',6,'',0,0,'1','1',NULL,NULL),
              
              (-205,-20000,'Threshold Alert','ALERT.thresholdAlert',4,'',0,0,'1','1',NULL,NULL),

              (-601,-60000,'Power Alert','ALERT.powerAlert',4,'',0,0,'1','1',NULL,NULL),
              (-602,-60000,'Fan Alert','ALERT.fanAlert',4,'',0,0,'1','1',NULL,NULL),
              (-603,-60000,'Port Link Alert','ALERT.portLinkAlert',4,'',0,0,'1','1',NULL,NULL),
              (-604,-60000,'Temperature Alert','ALERT.temperatureAlert',4,'',0,0,'1','1',NULL,NULL);
              
--            (-401,-40000,'System Database','ALERT.systemDatabase',4,'',0,0,'0','1',NULL,NULL),
--            (-402,-40000,'System Config Error','ALERT.systemConfigError',1,'',0,0,'0','1',NULL,NULL),
--            (-403,-40000,'Authentication','ALERT.authentication',3,'',0,0,'0','1',NULL,NULL);


/*Data for the table EventType */
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-10000,0,'DeviceEvent','EVENT.deviceEvent',''),
                (-20000,0,'QosEvent','EVENT.serviceQualityEvent',''),
                (-30000,0,'CommunicationEvent','EVENT.communicationEvents',''),
                (-40000,0,'SystemEvent','EVENT.systemEvent','');
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-101,-10000,'Snmp Support','EVENT.UnableToGetDeviceSNMPInfo',''),
                (-102,-10000,'New Device found','EVENT.newDeviceDiscovery',''),
                (-201,-20000,'Connectivity Delay','EVENT.correspondingDelay',''),
                (-202,-20000,'CPU Usage','EVENT.deviceCpuUtilization',''),
                (-203,-20000,'Memory Usage','EVENT.deviceMemUtilization',''),
                (-204,-20000,'Disk Usage','EVENT.deviceDiskUtilization',''),
                (-301,-30000,'DisConnectivity','EVENT.deviceUnableConnectivity',''),
                (-302,-30000,'Connectivity','EVENT.deviceConnectivity',''),
                (-401,-40000,'System Database Event','EVENT.systemDBEvent',''),
                (-402,-40000,'System Config Error','EVENT.systemConfigError',''),
                (-403,-40000,'Authentication','EVENT.authenticate','');

/*Data for the table Event2Alert */
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
--                (-101,-101,1),
--                (-102,-102,1),
                  (-201,-201,1),
                  (-202,-202,1),
                  (-203,-203,1),
                  (-204,-204,1),
                  (-301,-301,1),
                  (-302,-301,0);
--                (-401,-401,1),
--                (-402,-402,1),
--                (-403,-403,1);
/* -- version 1.0.0,build 2011-3-31,module server */

-- version 1.0.0,build 2012-2-24,module server

insert into virtualNetwork(virtualNetId,folderId,virtualName,virtualType,visiable) values(1,10,'默认子网',1,0);

/* -- version 1.0.0,build 2012-2-24,module server */

/* modify by Rod Authority Default View*/
-- version 1.7.12.1,build 2013-04-10,module server
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (-1,  'UNKNOWN', 'UNKNOWN', '', '',  '', 'network/unknown_16.png', 'network/unknown_32.png', 'network/unknown_48.png', 'network/unknown_64.png', 1);
CREATE TABLE t_entity_10(entityId bigint(20),primary key(entityId),constraint fk_authority_t_entity_10 foreign key(entityId) references entity(entityId) on delete cascade on update cascade);
CREATE VIEW v_topo_10 as select * from topofolder where find_in_set(folderId, topoFolderFun('10'));             
/* -- version 1.7.12.1,build 2013-04-10,module server */

-- version 1.7.15.0,build 2013-06-20,module server
UPDATE ReportTemplate SET taskable = 0 WHERE templateId = 10000;
UPDATE ReportTemplate SET taskable = 0 WHERE templateId = 20000;
UPDATE ReportTemplate SET taskable = 0 WHERE templateId = 30000;
/* -- version 1.7.15.0,build 2013-06-20,module server */

-- version 1.7.15.0,build 2013-06-28,module server
DELETE from ReportTemplate;

INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) values
(10000,-1,'resourceReport','report.resourceReport',NULL,'asset_16.gif','asset_48.gif','core',NULL,NULL,'0');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) values
(20000,-1,'performanceReport','report.performanceReport',NULL,'asset_16.gif','asset_48.gif','core',NULL,NULL,'0');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) values
(30000,-1,'alertReport','report.alertReport',NULL,'asset_16.gif','asset_48.gif','core',NULL,NULL,'0');
/* -- version 1.7.15.0,build 2013-06-28,module server */

-- version 1.7.15.0,build 2013-09-16,module server
delete from alertType where typeId = -50000;
delete from alertType where typeId = -202;
delete from alertType where typeId = -203;
delete from alertType where typeId = -204;
delete from alertType where typeId = -205;

delete from eventType where typeId = -50000;
delete from eventType where typeId = -202;
delete from eventType where typeId = -203;
delete from eventType where typeId = -204;

INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-50000,0,'Threshold Alert','ALERT.thresholdAlert',0,'',0,0,'0','1',NULL,NULL);              
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
              (-50000,0,'DeviceEvent','EVENT.thresholdEvent','');
/* -- version 1.7.15.0,build 2013-09-16,module server */

-- version 2.0.0.0,build 2013-09-16,module server
DELETE FROM reporttemplate;
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10000, -1, 'resourceReport', 'report.resourceReport', NULL, 'folder', 'asset_48.gif', 'core', NULL, NULL, '0');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10001, 10000, 'oltDeviceListReportCreator', 'report.oltDeviceList', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, 'epon/showEponDeviceAsset.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10002, 10000, 'oltBoardReportCreator', 'report.oltBoardReport', NULL, 'icoG9', 'device_48.gif', 'board', NULL, 'epon/showEponBoardReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10003, 10000, 'oltSniPortReportCreator', 'report.oltSniPortReport', NULL, 'icoG7', 'device_48.gif', 'port', NULL, 'epon/showEponSniPortReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10004, 10000, 'oltPonPortReportCreator', 'report.oltPonPortReport', NULL, 'icoG7', 'device_48.gif', 'port', NULL, 'epon/showEponPonPortReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10005, 10000, 'onuDeviceListReportCreator', 'report.onuDeviceListReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, 'epon/showOnuDeviceAsset.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10006, 10000, 'ccmtsDeviceListReportCreator', 'report.ccmtsDeviceListReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmc/showCcmtsDeviceAsset.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10007, 10000, 'cmReportCreator', 'report.cmReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmcreport/showCmDeviceAsset.tv', '1');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10008, 10000, 'ccmtsChannelListReportCreator', 'report.ccmtsChannelListReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmcreport/showCcmtsChannelAsset.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20000, -1, 'performanceReport', 'report.performanceReport', NULL, 'folder', 'asset_48.gif', 'core', NULL, NULL, '0');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20001, 20000, 'oltCpuReportCreator', 'report.oltCpuReport', NULL, 'icoG5', 'device_48.gif', 'perf', NULL, 'epon/showEponCpuRankReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20002, 20000, 'oltMemReportCreator', 'report.oltMemReport', NULL, 'icoG6', 'device_48.gif', 'perf', NULL, 'epon/showEponMemRankReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20003, 20000, 'oltSniPortFlowReportCreator', 'report.oltSniPortFlowReport', NULL, 'icoG1', 'device_48.gif', 'perf', NULL, '/epon/report/querySniFlowStastic.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20004, 20000, 'oltPonPortFlowReportCreator', 'report.oltPonPortFlowReport', NULL, 'icoG1', 'device_48.gif', 'perf', NULL, '/epon/report/queryPonFlowStastic.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20005, 20000, 'oltResponseReportCreator', 'report.oltResponseReport', NULL, 'icoG8', 'device_48.gif', 'perf', NULL, 'epon/showEponDelayRankReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20006, 20000, 'cmcSnrReportCreator', 'report.cmcSnrReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmcreport/showCcmtsUsSnrStatistics.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20007, 20000, 'cmcUserFlowReportCreator', 'report.cmcUserFlowRepor', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmcreport/showCcmtsUserFlowStatistics.tv', '1');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20008, 20000, 'cmRealTimeUserStaticReportCreator', 'report.cmRealTimeUserStaticReportCreator', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmcreport/showCmRealTimeUserStatic.tv', '1');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20009, 20000, 'cmDailyNumStaticReportCreator', 'report.cmDailyNumStaticReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmcreport/showCmDailyNumStatic.tv', '1');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20010,20000,'oltRunningStatusReportCreator', 'report.oltRunningStatusReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/epon/report/queryOltRunningStatus.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (30000, -1, 'alertReport', 'report.alertReport', NULL, 'folder', 'asset_48.gif', 'core', NULL, NULL, '0');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (30001, 30000, 'currentAlarmReportCreator', 'report.currentAlarmReport', NULL, 'icoG3', 'device_48.gif', 'alert', NULL, '/epon/showCurAlertReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (30002, 30000, 'historyAlarmReportCreator', 'report.historyAlarmReport', NULL, 'icoG4', 'device_48.gif', 'alert', NULL, '/epon/showHistoryAlarmReport.tv', '1');
/* -- version 2.0.0.0,build 2013-09-16,module server */

-- version 2.0.0.0,build 2013-10-17,module server
update reporttemplate set taskable=0 where templateId=20009;
/* -- version 2.0.0.0,build 2013-10-17,module server */

-- version 2.0.0.0,build 2013-11-20,module server
DELETE FROM reporttemplate;
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10000, -1, 'resourceReport', 'report.resourceReport', NULL, 'folder', 'asset_48.gif', 'core', NULL, NULL, '0');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20000, -1, 'performanceReport', 'report.performanceReport', NULL, 'folder', 'asset_48.gif', 'core', NULL, NULL, '0');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (30000, -1, 'alertReport', 'report.alertReport', NULL, 'folder', 'asset_48.gif', 'core', NULL, NULL, '0');
/* -- version 2.0.0.0,build 2013-11-20,module server */

-- version 2.0.0.0,build 2014-3-4,module server
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(1,   'baseType',  'Base Type',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(2,   'categoryType',  'Category Type',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(50000,   'IP Entity',  'IP Entity',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
insert into entitytyperelation (type, typeId) values (2,50000);
insert into entitytyperelation (type, typeId) values (1,-1);
/* -- version 2.0.0.0,build 2014-3-4,module server */
-- version 2.0.0.0,build 2014-3-8,module server
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(3,   'licenseGroupType',  'License Group Type',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(4,   'deviceModelsType',  'Device Models Type',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
/* -- version 2.0.0.0,build 2014-3-8,module server */

-- version 2.0.0.0,build 2014-03-17,module server
delete from eventtype where typeId = -403;
/* -- version 2.0.0.0,build 2014-03-17,module server */

-- version 2.0.0.0,build 2014-03-12,module server
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
            (-101,-10000,'SNMP Alert','ALERT.snmpAlert',6,'',0,0,'0','1',NULL,NULL);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
            (-101,-101,1);
            
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CONNECTIVITY', 1, 'Performance.ResponseDelay', 'Performance.cmc_deviceStatus','ms');
/* -- version 2.0.0.0,build 2014-03-12,module server */

-- version 2.0.0.0,build 2014-05-12,module server
INSERT INTO batchautodiscoveryperiod(strategyType,periodStart,period,active) VALUES(1,10800,86400,1);
insert into systempreferences values('Snmp.topo.retries', 0, 'Snmp');
insert into systempreferences values('Snmp.topo.timeout', 1000, 'Snmp');
/* -- version 2.0.0.0,build 2014-05-12,module server */ 

-- version 2.2.2.0,build 2014-05-12,module server
insert into UserCustomlization(userId,functionName,functionAction,icon) values (2,'WorkBench.entityList','showResource()','icoB2');
insert into UserCustomlization(userId,functionName,functionAction,icon) values (2,'WorkBench.showAlert','showAlert()','icoF2');
insert into UserCustomlization(userId,functionName,functionAction,icon) values (2,'WorkBench.topo','showTopology()','icoB13');
insert into UserCustomlization(userId,functionName,functionAction,icon) values (2,'MODULE.googleMap','showGoogle()','icoC1');
insert into UserCustomlization(userId,functionName,functionAction,icon) values (2,'WorkBench.deviceView','showEntityView()','icoB1');
insert into UserCustomlization(userId,functionName,functionAction,icon) values (2,'WorkBench.ipSegmentView','showIpSegmentView()','icoE4');
insert into UserCustomlization(userId,functionName,functionAction,icon) values (2,'WorkBench.global','showGlobal()','icoH1');
/* -- version 2.2.2.0,build 2014-05-12,module server */ 

-- version 2.2.2.0,build 2014-05-13,module server
insert into batchautodiscoverysnmpconfig(name,readCommunity,writeCommunity,version) values('default','public','private',1);
/* -- version 2.2.2.0,build 2014-05-13,module server */ 

-- version 2.2.2.0,build 2014-05-19,module server
insert into systempreferences(name,value,module) values('telnetUserName','admin','telnet');
insert into systempreferences(name,value,module) values('telnetPassword','admin','telnet');
insert into systempreferences(name,value,module) values('telnetEnablePassword','admin','telnet');
/* -- version 2.2.2.0,build 2014-05-19,module server */ 

-- version 2.2.2.0,build 2014-07-16,module server
insert into systempreferences(name,value,module) values('sendCommandInterval','500','telnet');
insert into systempreferences(name,value,module) values('pollInterval','30','telnet');
/* -- version 2.2.2.0,build 2014-07-16,module server */ 
-- version 2.2.2.0,build 2014-07-21,module server
update systempreferences set value = '1800000' where module = 'telnet' and name = 'pollInterval';
/* -- version 2.2.2.0,build 2014-07-21,module server */ 
-- version 2.3.2.0,build 2014-07-22,module server
delete from ActionType where actionTypeId = 3;
/* -- version 2.3.2.0,build 2014-07-22,module server */ 
-- version 2.3.2.0,build 2014-07-28,module server
insert into entitytyperelation (type, typeId) values (-1,-1);
/* -- version 2.3.2.0,build 2014-07-28,module server */ 
-- version 2.3.2.0,build 2014-8-04,module server
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000010,'sendConfigFileManage',4000000,'sendConfig.fileManage');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000010);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000007,'entityTelnetConfig',4000000,'telnet.entityTelnetConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000007);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000008,'sendConfigParameterconfig',4000000,'batchtopo.parameterconfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000008);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000009,'sendConfigEntityList',4000000,'sendConfig.entityList');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000009);
/* -- version 2.3.2.0,build 2014-8-04,module server */

-- version 2.3.2.0,build 2014-08-14,module server			
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES (-100,-10000,'Snmp Normal','EVENT.snmpNormal','');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (-100,-101,0);
/* -- version 2.3.2.0,build 2014-08-14,module server */ 

-- version 2.3.2.0,build 2014-8-25,module server
delete from usercustomlization where userId = 2 and functionName = 'MODULE.googleMap';
/* -- version 2.3.2.0,build 2014-8-25,module server */

-- version 2.3.2.0,build 2014-08-26,module server
delete from batchautodiscoveryperiod;
INSERT INTO batchautodiscoveryperiod(strategyType,periodStart,period,active) VALUES(2,10800,3600,1);
/* -- version 2.3.2.0,build 2014-08-26,module server */ 

-- version 2.3.2.0,build 2014-09-23,module server
set global event_scheduler = 'ON';
/* -- version 2.3.2.0,build 2014-09-23,module server */

-- version 2.4.3.0,build 2014-10-30,module server
insert into AutoRefreshConfig values(1,1800000);
/* -- version 2.4.3.0,build 2014-10-30,module server */

-- version 2.4.3.0,build 2014-11-06,module server
INSERT INTO userauthfolder(userId, folderId) values(1, 10);
INSERT INTO userauthfolder(userId, folderId) values(2, 10);
INSERT INTO userauthfolder(userId, folderId) values(3, 10);
/* -- version 2.4.3.0,build 2014-11-06,module server */

-- version 2.4.6.0,build 2014-12-06,module server
insert into systempreferences(name,value,module) values('retryTimes','3','upgrade');
insert into systempreferences(name,value,module) values('retryInterval','1800000','upgrade');
insert into systempreferences(name,value,module) values('writeConfig','0','upgrade');
/* -- version 2.4.6.0,build 2014-12-06,module server */

-- version 2.4.6.0,build 2015-1-14,module server
delete from perftarget where targetType = 1;
/* -- version 2.4.6.0,build 2015-1-14,module server */
-- version 2.6.0.0,build 2015-05-21,module server
insert into alertsound(id,name,description,deletable) values(1,'level0.wav','Alert.description0',0);
insert into alertsound(id,name,description,deletable) values(2,'level1.wav','Alert.description1',0);
insert into alertsound(id,name,description,deletable) values(3,'level2.wav','Alert.description2',0);
insert into alertsound(id,name,description,deletable) values(4,'level3.wav','Alert.description3',0);
insert into alertsound(id,name,description,deletable) values(5,'level4.wav','Alert.description4',0);
insert into alertsound(id,name,description,deletable) values(6,'level5.wav','Alert.description5',0);
insert into alertsound(id,name,description,deletable) values(7,'silence.wav','Alert.description6',0);

UPDATE levels SET soundId=levelId;
/*-- version 2.6.0.0,build 2015-05-21,module server */
-- version 2.5.2.0,build 2015-4-27,module server
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(130000,   'CommandSend Entity',  'CommandSend Entity',  'network',  '/network',   '',   '',  '',  '',  '',  1);
insert into entitytyperelation (type, typeId) values (130000,33);
insert into entitytyperelation (type, typeId) values (130000,34);
insert into entitytyperelation (type, typeId) values (130000,36);
insert into entitytyperelation (type, typeId) values (130000,37);
insert into entitytyperelation (type, typeId) values (130000,40);
insert into entitytyperelation (type, typeId) values (130000,48);
insert into entitytyperelation (type, typeId) values (130000,65);
insert into entitytyperelation (type, typeId) values (130000,68);
insert into entitytyperelation (type, typeId) values (130000,71);
insert into entitytyperelation (type, typeId) values (130000,255);
insert into entitytyperelation (type, typeId) values (130000,10001);
insert into entitytyperelation (type, typeId) values (130000,10002);
insert into entitytyperelation (type, typeId) values (130000,10003);
insert into entitytyperelation (type, typeId) values (130000,30001);
insert into entitytyperelation (type, typeId) values (130000,30002);
insert into entitytyperelation (type, typeId) values (130000,30004);
insert into entitytyperelation (type, typeId) values (130000,30005);
insert into entitytyperelation (type, typeId) values (130000,30006);
insert into entitytyperelation (type, typeId) values (130000,30007);
/* -- version 2.5.2.0,build 2015-4-27,module server */

-- version 2.5.2.0,build 2015-4-28,module server
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000012,'upgradeVersionManage',4000000,'userPower.versionManage');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000012);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000013,'upgradeParameter',4000000,'userPower.parameterconfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000013);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000014,'upgradeJob',4000000,'userPower.job');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000014);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000015,'upgradeRecord',4000000,'userPower.record');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000015);
/* -- version 2.5.2.0,build 2015-4-28,module server */

-- version 2.6.0.0,build 2015-05-21,module server
delete from AlertConfirmConfig;
insert into AlertConfirmConfig values(1);
/* -- version 2.6.0.0,build 2015-05-21,module server */

-- version 2.6.3.0,build 2015-08-11,module server
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (5000026,'performanceBatchConfig',5000000,'Performance.batchConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,5000026);
/* -- version 2.6.3.0,build 2015-08-11,module server */

-- version 2.6.0.0,build 2015-7-25,module server
INSERT INTO perftargetcategory VALUES (10000, 'olt_onlineStatus', 'onlinePerf');
INSERT INTO perftargetcategory VALUES (10000, 'olt_cpuUsed', 'eponServiceQualityPerf');
INSERT INTO perftargetcategory VALUES (10000, 'olt_memUsed', 'eponServiceQualityPerf');
INSERT INTO perftargetcategory VALUES (10000, 'olt_flashUsed', 'eponServiceQualityPerf');
INSERT INTO perftargetcategory VALUES (10000, 'olt_boardTemp', 'eponServiceQualityPerf');
INSERT INTO perftargetcategory VALUES (10000, 'olt_fanSpeed', 'eponServiceQualityPerf');
INSERT INTO perftargetcategory VALUES (10000, 'olt_optLink', 'eponLinkQualityPerf');
INSERT INTO perftargetcategory VALUES (10000, 'olt_sniFlow', 'eponFlowQualityPerf');
INSERT INTO perftargetcategory VALUES (10000, 'olt_ponFlow', 'eponFlowQualityPerf');
INSERT INTO perftargetcategory VALUES (10000, 'onuPonFlow', 'eponFlowQualityPerf');
INSERT INTO perftargetcategory VALUES (10000, 'uniFlow', 'eponFlowQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_onlineStatus', 'onlinePerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_cpuUsed', 'cmcServiceQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_memUsed', 'cmcServiceQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_flashUsed', 'cmcServiceQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_optLink', 'cmcLinkQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_macFlow', 'cmcFlowQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_channelSpeed', 'cmcFlowQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_upLinkFlow', 'cmcFlowQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_ber', 'cmcSignalQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_snr', 'cmcSignalQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_moduleTemp', 'cmcTempQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_opticalReceiver', 'cmcOpticalReceiverPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_cmflap', 'cmFlapMonitor');
INSERT INTO perftargetcategory VALUES (40000, 'sysUptime', 'cmcServiceQualityPerf');
INSERT INTO perftargetcategory VALUES (40000, 'cmc_channelSpeed', 'cmtsFlowQualityPerf');
INSERT INTO perftargetcategory VALUES (40000, 'cmc_upLinkFlow', 'cmtsFlowQualityPerf');
INSERT INTO perftargetcategory VALUES (40000, 'cmc_ber', 'cmtsSignalQualityPerf');
INSERT INTO perftargetcategory VALUES (40000, 'cmc_snr', 'cmtsSignalQualityPerf');
INSERT INTO perftargetcategory VALUES (40000, 'cmc_onlineStatus', 'cmtsSignalQualityPerf');
INSERT INTO perftargetcategory VALUES (40000, 'cmc_cpuUsed', 'cmtsSystemPerf');
INSERT INTO perftargetcategory VALUES (40000, 'cmc_memUsed', 'cmtsSystemPerf');
INSERT INTO perftargetcategory VALUES (13000, 'onu_onlineStatus', 'onuOnlinePerf');
INSERT INTO perftargetcategory VALUES (13000, 'onu_optLink', 'onuLinkQualityPerf');
INSERT INTO perftargetcategory VALUES (13000, 'onu_portFlow', 'onuFlowQualityPerf');
/*-- version 2.6.0.0,build 2015-7-25,module server*/

-- version 2.6.4.0,build 2015-9-9,module server
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (10000007,'refreshDevice',2,'rolePower.refreshDevice');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,10000007);
/*-- version 2.6.4.0,build 2015-9-9,module server*/
     
-- version 2.6.4.0,build 2015-9-24,module server
INSERT INTO cmbossinfo(cmMac, userAddr, classification, importTime) 
SELECT cmMacAddr, cmAlias, cmClassified, importTime FROM cmimportinfo
ON DUPLICATE KEY UPDATE
userAddr = cmAlias,
classification = cmClassified,
importTime = cmimportinfo.importTime;
/* -- version 2.6.4.0,build 2015-9-24,module server  */

-- version 2.6.4.0,build 2015-11-5,module server
insert into systempreferences values('nbi.alarm.ipaddress', '0.0.0.0', 'nbi.alarm');
insert into systempreferences values('nbi.alarm.port', 162, 'nbi.alarm');
insert into systempreferences values('nbi.alarm.community', 'public', 'nbi.alarm');
insert into systempreferences values('nbi.alarm.heartbeatswitch', 'false', 'nbi.alarm');
insert into systempreferences values('nbi.alarm.heartbeatinterval', 60, 'nbi.alarm');
insert into systempreferences values('nbi.alarm.heartbeatlabel', 'SNMP AGENT', 'nbi.alarm');
/* -- version 2.6.4.0,build 2015-11-5,module server  */

-- version 2.6.5.0,build 2015-11-7,module server
INSERT INTO ActionType(actionTypeId,name,displayName,enabled,actionClass,params) values
                (3,'Trap','SNMP陷阱',1,'com.topvision.ems.fault.service.impl.TrapServiceImpl',NULL);
INSERT INTO Action(actionTypeId,name,params,enabled) values(3,'Trap',NULL,1);
/* -- version 2.6.5.0,build 2015-11-7,module server  */


-- version 2.6.5.2,build 2015-11-25,module server
update perftargetcategory set category = 'onlinePerf' where typeId = 40000 and perfIndex = 'cmc_onlineStatus';
delete from perftargetcategory where typeId = 10000 and perfIndex = 'onuPonFlow';
delete from perftargetcategory where typeId = 10000 and perfIndex = 'uniFlow';
delete from perftargetcategory where typeId = 40000 and perfIndex = 'sysUptime';
/*-- version 2.6.5.2,build 2015-11-25,module server*/

-- version 2.6.8.0,build 2016-3-12,module server
update event2alert set alertTypeId = 16423 where eventTypeId = 16423 and alertTypeId = -604 and type = 1;
update event2alert set alertTypeId = 16424 where eventTypeId = 16424 and alertTypeId = -604 and type = 1;
update event2alert set alertTypeId = 16423 where eventTypeId = 16425 and alertTypeId = -604 and type = 0;
insert into event2alert(eventTypeId,alertTypeId,type) values(16425,16424,0);
/*-- version 2.6.5.2,build 2016-3-12,module server*/

-- version 2.6.8.0,build 2016-3-26,module server
INSERT INTO nbiexportconfig(five_enable,ten_enable,fifteen_enable,thirty_enable,sixty_enable) values (1,0,0,0,0);
/* -- version 2.6.8.0,build 2016-3-26,module server  */

-- version 2.7.1.0,build 2016-7-18,module server
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-200,-20000,'Connectivity Normal','EVENT.connectDelayNormal','');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (-200,-201,0);
/* -- version 2.7.1.0,build 2016-7-18,module server  */

-- version 2.7.1.0,build 2016-8-10,module server
INSERT INTO  ThresholdAlertLastValue (entityId,alertEventId,source,levelId) 
SELECT A.entityId,A.typeId,A.source,A.levelId FROM Alert A, AlertType B WHERE A.typeId=B.typeId AND FIND_IN_SET(A.typeId,topoEventFun(-50000,'alerttype'))
/** -- version 2.7.1.0,build 2016-8-09,module server */

-- version 2.7.3.0,build 2016-8-18,module server
insert into systempreferences(name,value,module) values('telnetIsAAA','0','telnet');
/* -- version 2.7.3.0,build 2016-8-18,module server  */

-- version 2.8.0.1,build 2016-11-21 14:12:00,module server
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES (1904649,-8,'CM_PARTIAL_SERVICE','DB.eventType.e1904649','');
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES (1904650,-8,'CM_PARTIAL_SERVICE_RECOVERY','DB.eventType.e1904650','');
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (1904649,-8,'CM_PARTIAL_SERVICE','DB.alertType.a1904649',3,'',0,0,'0','1',NULL,NULL);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (1904649,1904649,1);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (1904650,1904649,0);
/* -- version 2.8.0.1,build 2016-11-21 14:12:00,module server  */

-- version 2.7.3.0,build 2016-11-18,module server
delete from systempreferences where module = 'autoClear';
insert into systempreferences(name,value,module) values('autoClearCmtsPeriod','0','autoClear');
insert into systempreferences(name,value,module) values('autoClearOnuPeriod','0','autoClear');
/* -- version 2.7.3.0,build 2016-11-18,module server  */

-- version 2.9.0.0,build 2017-01-16 17:30,module server
insert into systempreferences(name, value, module) values('clientType', 1, 'TelnetClient');
insert into systempreferences(name, value, module) values('recordState', 1, 'TelnetClient');
insert into FunctionItem(functionId,name,superiorId,displayName) values
        (8000030,'telnetClient',8000000,'rolePower.telnetClient');
INSERT INTO RoleFunctionRela(roleId,functionId) values(1,8000030);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000030);
/* -- version 2.9.0.0,build 2017-01-16 17:30,module server */

-- version 2.9.0.0,build 2017-01-17 13:40,module server
insert into systempreferences(name, value, module) values('timeout', 5000, 'TelnetClient');
/* -- version 2.9.0.0,build 2017-01-17 13:40,module server */

-- version 2.9.0.0,build 2017-01-23,module server
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (8000031,'cmServiceType',8000000,'cm.cmServiceType');
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,8000031);
INSERT INTO RoleFunctionRela(roleId,functionId) values (1,8000031);
/* -- version 2.9.0.0,build 2017-01-23,module server */

-- version 2.9.0.5,build 2017-05-12,module server
delete from batchautodiscoveryperiod;
INSERT INTO batchautodiscoveryperiod(strategyType,periodStart,period,active) VALUES(2,10800,86400,1);
/* -- version 2.9.0.5,build 2017-05-12,module server */ 

-- version 2.9.0.8,build 2017-05-24,module server
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (2000012,'cmCpeList',2000000,'rolePower.cmCpeList');
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,2000012);
INSERT INTO RoleFunctionRela(roleId,functionId) values (3,2000012);
/* -- version 2.9.0.8,build 2017-05-24,module server */
-- version 2.8.1.4,build 2017-6-23,module server
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (1016439,-8,'CMTS_DELETE','DB.eventType.e1016439','');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (1016439,1016439,1);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (16434,1016439,0);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (1016434,1016439,0);
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (1016439,-8,'CMTS_DELETE','DB.eventType.e1016439',3,'',0,0,'0','1',NULL,NULL);
/* -- version 2.8.1.4,build 2017-6-23,module server */
              
-- version 2.9.1.11,build 2017-09-06,module server
insert into systempreferences values('Ping.batchtopo.timeout', 4000, 'Ping');
/* -- version 2.9.1.11,build 2017-09-06,module server */               
-- version 2.9.1.10,build 2017-9-5,module server
INSERT INTO systempreferences VALUES('failAutoSendConfigSwitch', 'false', 'sendConfig');
/* -- version 2.9.1.10,build 2017-9-5,module server */

-- version 2.9.1.11,build 2017-09-27,module server
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
    (-103,-10000,'Device MAC duplicate','EVENT.DeviceMacDuplicate','');
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
    (-103,-10000,'Device MAC duplicate','ALERT.DeviceMacDuplicate',3,'',0,0,'1','1',NULL,NULL);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
    (-103,-103,1);
/*-- version 2.9.1.11,build 2017-09-27,module server*/

-- version 2.9.1.11,build 2017-10-26,module server
INSERT INTO perftargetcategory VALUES (30000, 'cmc_dorOptTemp', 'cmcDorOptTempQualityPerf');
INSERT INTO perftargetcategory VALUES (30000, 'cmc_dorLinePower', 'cmcDorLinePowerQualityPerf');
/*-- version 2.9.1.11,build 2017-10-26,module server*/

-- version 2.9.1.20,build 2018-01-30,module server
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (9000001,'operationMgmt',1,'rolePower.operationMgmt');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (9000002,'attentionAlarm',9000001,'rolePower.attentionAlarm');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (9000003,'setAttentionAlarm',9000001,'rolePower.setAttentionAlarm');
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,9000001);
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,9000002);
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,9000003);
/*-- version 2.9.1.20,build 2018-01-30,module server*/-- version 2.9.1.14,build 2017-12-12,module server
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(8000033,'tagManagement',8000000,'userPower.tagManagement');
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,8000033);
INSERT INTO RoleFunctionRela(roleId,functionId) values (1,8000033);
/* -- version 2.9.1.14,build 2017-12-12,module server */