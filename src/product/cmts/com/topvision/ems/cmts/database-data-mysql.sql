-- version 1.0.0.0,build 2013-07-20,module cmts
insert into entitytype(typeId,name,displayName, module, modulePath) values(40000,'cmts','CMTS','cmts','/cmts');
insert into entitytype(typeId,name,displayName, module, modulePath, sysObjectId) values(40001,'cmts','BSR2000','cmts','/cmts','1.3.6.1.4.1.4981.4.1');
/*--version 1.0.0.0,build 2013-7-20,module cmts*/

-- version 1.7.18.1,build 2013-08-13,module cmts
insert into entitytype(typeId,name,displayName, module, modulePath, sysObjectId) values(40002,'cmts','UBR7225','cmts','/cmts','1.3.6.1.4.1.9.1.827');
/*--version 1.7.18.1,build 2013-08-13,module cmts*/
  
-- version 1.7.19.0,build 2013-08-17,module cmts
update entitytype set
icon16 = 'network/cmts/cmts_16.png',
icon32 = 'network/cmts/cmts_32.png',
icon48 = 'network/cmts/cmts_48.png',
icon64 = 'network/cmts/cmts_64.png',
corpId = '1'
where typeId = '40000';
update entitytype set
icon16 = 'network/cmts/cmts_16.png',
icon32 = 'network/cmts/cmts_32.png',
icon48 = 'network/cmts/cmts_48.png',
icon64 = 'network/cmts/cmts_64.png',
corpId = '1'
where typeId = '40001';
update entitytype set
icon16 = 'network/cmts/cmts_16.png',
icon32 = 'network/cmts/cmts_32.png',
icon48 = 'network/cmts/cmts_48.png',
icon64 = 'network/cmts/cmts_64.png',
corpId = '1'
where typeId = '40002';
/*--version 1.7.19.0,build 2013-08-17,module cmts*/

-- version 1.7.19.0,build 2013-8-25,module cmts
INSERT INTO GlobalCollectTimeTable(perftargetName,interval_,targetGroup,type,enable) VALUES
                ('upLinkFlow',15,'flow',3,1),
                ('channelSpeed',15,'flow',3,1),
                ('sysUptime',15,'service',3,1),
				('snr',15,'signalQuality',3,1),
				('ber',15,'signalQuality',3,1);
/* -- version 1.7.19.0,build 2013-8-25,module cmts */
-- version 1.7.19.0,build 2013-08-29,module cmts
update entitytype set
icon16 = 'network/cmts/cmts_16.gif',
icon32 = 'network/cmts/cmts_32.gif',
icon48 = 'network/cmts/cmts_48.gif',
icon64 = 'network/cmts/cmts_64.gif',
corpId = '1'
where typeId = '40000';
update entitytype set
icon16 = 'network/cmts/cmts_16.gif',
icon32 = 'network/cmts/cmts_32.gif',
icon48 = 'network/cmts/cmts_48.gif',
icon64 = 'network/cmts/cmts_64.gif',
corpId = '1'
where typeId = '40002';
/*--version 1.7.19.0,build 2013-08-29,module cmts*/

-- version 1.7.19.0,build 2013-09-23,module cmts
INSERT INTO perfglobal(flag,isPerfOn,isRelationWithDefaultTemplate,defaultTemplateId,isPerfThreshold,defaultCollectTime) values ('CMTS', 1,1,3,1, 15);

INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-50004,-50000,'CMTS Event','EVENT.cmtsThreshold','');
            
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-900,-50004,'CMTS_UpLink_InSpeedEvent','DB.eventType.e900',''),
                (-901,-50004,'CMTS_UpLink_InSpeedEvent','DB.eventType.e901',''),
                (-902,-50004,'CMTS_UpLink_OutSpeedEvent','DB.eventType.e902',''),
                (-903,-50004,'CMTS_UpLink_OutSpeedEvent','DB.eventType.e903',''),
                (-904,-50004,'CMTS_Uplink_UsedEvent','DB.eventType.e904',''),
                (-905,-50004,'CMTS_Uplink_UsedEvent','DB.eventType.e905',''),
                
                (-906,-50004,'CMTSUtilizationEvent','DB.eventType.e906',''),
                (-907,-50004,'CMTSUtilizationEvent','DB.eventType.e907',''),
                (-908,-50004,'CMTSSpeedEvent','DB.eventType.e908',''),
                (-909,-50004,'CMTSSpeedEvent','DB.eventType.e909',''),
                
                (-910,-50004,'CMTSNoiseEvent','DB.eventType.e910',''),
                (-911,-50004,'CMTSNoiseEvent','DB.eventType.e911',''),
                (-912,-50004,'CMTSErrorCodeEvent','DB.eventType.e912',''),
                (-913,-50004,'CMTSErrorCodeEvent','DB.eventType.e913',''),
                (-914,-50004,'CMTSUnErrorCodeEvent','DB.eventType.e914',''),
                (-915,-50004,'CMTSUnErrorCodeEvent','DB.eventType.e915',''),
                
                (-916,-50004,'CMTS_Cm_NumberEvent','DB.eventType.e916',''),
                (-917,-50004,'CMTS_Cm_NumberEvent','DB.eventType.e917','');   
                
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-50004,-50000,'Threshold Alert','ALERT.cmtsThreshold',0,'',0,0,'0','1',NULL,NULL);  
              
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
                (-900,-50004,'CMTS_UpLink_InSpeed','DB.alertType.a900',5,'',0,  0,  '0',  '1',  '',  ''),
                (-902,-50004,'CMTS_UpLink_OutSpeed','DB.alertType.a902',5,'',0,  0,  '0',  '1',  '',  ''),
                (-904,-50004,'CMTS_Uplink_Used','DB.alertType.a904',5,'',0,  0,  '0',  '1',  '',  ''),
                
                (-906,-50004,'CMTSUtilization','DB.alertType.a906',5,'',0,  0,  '0',  '1',  '',  ''),
                (-908,-50004,'CMTSSpeed','DB.alertType.a908',5,'',0,  0,  '0',  '1',  '',  ''),
                
                (-910,-50004,'CMTSNoise','DB.alertType.a910',5,'',0,  0,  '0',  '1',  '',  ''),
                (-912,-50004,'CMTSErrorCode','DB.alertType.a912',5,'',0,  0,  '0',  '1',  '',  ''),
                (-914,-50004,'CMTSUnErrorCode','DB.alertType.a914',5,'',0,  0,  '0',  '1',  '',  ''),
                
                (-916,-50004,'CMTS_Cm_Number','DB.alertType.a916',5,'',0,  0,  '0',  '1',  '',  '');
              
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
                (-900,-900,1),
                (-901,-900,0),
                (-902,-902,1),
                (-903,-902,0),
                (-904,-904,1),
                (-905,-904,0),
                
                (-906,-906,1),
                (-907,-906,0),
                (-908,-908,1),
                (-909,-908,0),
                
                (-910,-910,1),
                (-911,-910,0),
                (-912,-912,1),
                (-913,-912,0),
                (-914,-914,1),
                (-915,-914,0),
                
                (-916,-916,1),
                (-917,-916,0);                
/*--version 1.7.19.0,build 2013-09-23,module cmts*/
                
-- version 1.7.19.0,build 2013-10-22,module cmts
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000010,'cmtsPerfParamConfig',5000000,'userPower.cmtsPerfParamConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000010);

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000011,'cmtsGlobalConfig',5000000,'userPower.cmtsGlobalConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000011);

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000012,'cmtsPerfMgmt',5000000,'userPower.cmtsPerfMgmt');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000012);
/*--version 1.7.19.0,build 2013-10-22,module cmts*/

-- version 1.7.19.0,build 2013-11-20,module cmts
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) values(10009,10000,'cmtsDeviceListReportCreator','report.cmtsDeviceListReport',NULL,'icoG1','device_48.gif','asset',NULL,'/cmts/report/showCmtsDeviceAsset.tv','1');
/*--version 1.7.19.0,build 2013-11-20,module cmts*/

-- version 1.7.19.0,build 2014-1-6,module cmts
UPDATE perfthresholdrule SET thresholds = '5_20_3' WHERE targetId = 'CMTS_NOISE' and templateId = 40000;
/* -- version 1.7.19.0,build 2014-1-6,module cmts */

-- version 1.7.19.0,build 2014-2-9,module cmts
UPDATE perfTarget SET unit = '%' where targetId = 'CMTS_ERRORCODE';
UPDATE perfTarget SET unit = '%' where targetId = 'CMTS_UNERRORCODE';
UPDATE perfthresholdrule SET thresholds = '1_20_3' WHERE targetId = 'CMTS_ERRORCODE' and templateId = 40000;
UPDATE perfthresholdrule SET thresholds = '1_20_3' WHERE targetId = 'CMTS_UNERRORCODE' and templateId = 40000;
/* -- version 1.7.19.0,build 2014-2-9,module cmts */

-- version 2.0.6.0,build 2014-2-13,module cmts
insert into entitytype(typeId,name,displayName, module, modulePath, sysObjectId) values(40005,'casa-c2100','CASA-C2100','cmts','/cmts','1.3.6.1.4.1.20858.2.1');

update entitytype set icon16 = 'network/cmts/casa_16.png',icon32 = 'network/cmts/casa_32.png',icon48 = 'network/cmts/casa_48.png',icon64 = 'network/cmts/casa_64.png',corpId = '1' where typeId = '40005';
update entitytype set icon16 = 'network/cmts/bsr2000_16.png',icon32 = 'network/cmts/bsr2000_32.png',icon48 = 'network/cmts/bsr2000_48.png',icon64 = 'network/cmts/bsr2000_64.png',corpId = '1' where typeId = '40001';
/* -- version 2.0.6.0,build 2014-2-13,module cmts */

-- version 2.0.6.0,build 2014-3-4,module cmts
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(90000,   'BSR',  'BSR CMTS',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(100000,   'UBR',  'UBR CMTS',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(110000,   'CASA',  'CASA CMTS',  'network',  '/network',   '',   '',  '',  '',  '',  1);
insert into entitytyperelation (type, typeId) values (1,40001);
insert into entitytyperelation (type, typeId) values (1,40002);
insert into entitytyperelation (type, typeId) values (2,40000);
insert into entitytyperelation (type, typeId) values (2,90000);
insert into entitytyperelation (type, typeId) values (2,100000);
insert into entitytyperelation (type, typeId) values (2,110000);
insert into entitytyperelation (type, typeId) values (40000,40001);
insert into entitytyperelation (type, typeId) values (40000,40002);
insert into entitytyperelation (type, typeId) values (50000,40001);
insert into entitytyperelation (type, typeId) values (50000,40002);
insert into entitytyperelation (type, typeId) values (60000,40001);
insert into entitytyperelation (type, typeId) values (60000,40002);
insert into entitytyperelation (type, typeId) values (90000,40001);
insert into entitytyperelation (type, typeId) values (100000,40002);
insert into entitytyperelation (type, typeId) values (40001,40001);
insert into entitytyperelation (type, typeId) values (40002,40002);
/* -- version 2.0.6.0,build 2014-3-4,module cmts */
-- version 2.0.6.0,build 2014-3-8,module cmts
update entitytype set name='bsr2000' where typeId = 40001;
update entitytype set name='bsr7225' where typeId = 40002;
insert into entitytyperelation (type, typeId) values (4,40000);
insert into entitytyperelation (type, typeId) values (1,40005);
insert into entitytyperelation (type, typeId) values (40000,40005);
insert into entitytyperelation (type, typeId) values (50000,40005);
insert into entitytyperelation (type, typeId) values (60000,40005);
insert into entitytyperelation (type, typeId) values (110000,40005);
insert into entitytyperelation (type, typeId) values (40005,40005);
/* -- version 2.0.6.0,build 2014-3-8,module cmts */

-- version 2.0.6.0,build 2014-4-14,module cmts
delete from reporttemplate where templateId = 10009;
/* -- version 2.0.6.0,build 2014-4-14,module cmts */

-- version 2.0.6.0,build 2014-4-23,module cmts
delete from perfthresholdrule  where templateId = 40000;
delete from perfTarget  where targetType = 40000;
delete from perfthresholdtemplate where templateId = 40000;
delete from perfglobal where flag = 'CMTS';
/* -- version 2.0.6.0,build 2014-4-14,module cmts */

-- version 2.0.6.0,build 2014-4-23,module cmts
delete from FunctionItem where functionId = 5000010;
delete from FunctionItem where functionId = 5000011;
delete from FunctionItem where functionId = 5000012;

delete from RoleFunctionRela where roleId = 2 and functionId = 5000010;
delete from RoleFunctionRela where roleId = 2 and functionId = 5000011;
delete from RoleFunctionRela where roleId = 2 and functionId = 5000012;
/* -- version 2.0.6.0,build 2014-4-23,module cmts */

-- version 2.0.6.0,build 2014-4-27,module cmts
delete from EventType where parentId = -50004;
delete from EventType where typeId = -50004;
delete from AlertType where category = -50004;
delete from AlertType where typeId = -50004;
/* -- version 2.0.6.0,build 2014-4-27,module cmts */

-- version 2.0.6.0,build 2014-5-12,module cmts
INSERT INTO batchautodiscoveryentitytype(typeId) values(40001);
INSERT INTO batchautodiscoveryentitytype(typeId) values(40002);
INSERT INTO batchautodiscoveryentitytype(typeId) values(40005);
/* -- version 2.0.6.0,build 2014-5-12,module cmts */

-- version 2.0.6.0,build 2014-7-09,module cmts
update entitytype set name='ubr7225' where typeId = 40002;
/* -- version 2.0.6.0,build 2014-7-09,module cmts */
-- version 2.2.6.0,build 2014-8-15,module cmts
update entityType set name='bsr2000' where typeId = 40001;
update entityType set name='ubr7225' where typeId = 40002;
/* -- version 2.2.6.0,build 2014-8-15,module cmts */

-- version 2.2.6.0,build 2014-8-26,module cmts
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'CMTS_UPLINK_INSPEED' and templateId = 40000;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'CMTS_UPLINK_OUTSPEED' and templateId = 40000;
UPDATE perfthresholdrule SET thresholds = '1_50_3#1_70_4#1_85_5' WHERE targetId = 'CMTS_UPLINK_UESD' and templateId = 40000;
UPDATE perfthresholdrule SET thresholds = '1_45_3' WHERE targetId = 'CMTS_SPEED' and templateId = 40000;
UPDATE perfthresholdrule SET thresholds = '1_75_3#1_90_4' WHERE targetId = 'CMTS_UTILIZATION' and templateId = 40000;
UPDATE perfthresholdrule SET thresholds = '5_14_4' WHERE targetId = 'CMTS_NOISE' and templateId = 40000;
UPDATE perfthresholdrule SET thresholds = '1_15_3#1_30_4' WHERE targetId = 'CMTS_UNERRORCODE' and templateId = 40000;
UPDATE perfthresholdrule SET thresholds = '1_80_3' WHERE targetId = 'CMTS_UNUSUAL_CMNUM' and templateId = 40000;
/* -- version 2.2.6.0,build 2014-8-26,module cmts */

-- version 2.4.3.0,build 2014-10-18,module cmts
insert into EntityTypeRelation values(3, 40000);
insert into EntityTypeRelation values(3, 40001);
insert into EntityTypeRelation values(3, 40002);
insert into EntityTypeRelation values(3, 40005);
/* -- version 2.4.3.0,build 2014-10-18,module cmts */

-- version 2.5.2.0,build 2015-04-07,module cmts
/* 增加公司类型 */
insert into EntityCorp(corpId,sysObjectID,name,displayName) values (4981,'1.3.6.1.4.1.4981','Motorola','摩托罗拉');
insert into EntityCorp(corpId,sysObjectID,name,displayName) values (20858,'1.3.6.1.4.1.20858','Casa','Casa');

/* Moto BSR2000 */
update EntityType set typeId = 41100, corpId = 4981 where typeId = 40001;
update EntityTypeRelation set typeId = 41100 where typeId = 40001;
update EntityTypeRelation set type = 41100 where type = 40001;
update BatchAutoDiscoveryEntityType set typeId = 41100 where typeId = 40001;
update DevicePerfTarget set typeId = 41100 where typeId = 40001;

update Entity set typeId = 41100 where typeId = 40001;
update DevicePerfCollecttime set typeId = 41100 where typeId = 40001;

/* Casa C2100 */
update EntityType set typeId = 43100, corpId = 20858 where typeId = 40005;
update EntityTypeRelation set typeId = 43100 where typeId = 40005;
update EntityTypeRelation set type = 43100 where type = 40005;
update BatchAutoDiscoveryEntityType set typeId = 43100 where typeId = 40005;
update DevicePerfTarget set typeId = 43100 where typeId = 40005;

update Entity set typeId = 43100 where typeId = 40005;
update DevicePerfCollecttime set typeId = 43100 where typeId = 40005;

/* Cisco UBR7225 */
update EntityType set typeId = 42100, corpId = 9 where typeId = 40002;
update EntityTypeRelation set typeId = 42100 where typeId = 40002;
update EntityTypeRelation set type = 42100 where type = 40002;
update BatchAutoDiscoveryEntityType set typeId = 42100 where typeId = 40002;
update DevicePerfTarget set typeId = 42100 where typeId = 40002;

update Entity set typeId = 42100 where typeId = 40002;
update DevicePerfCollecttime set typeId = 42100 where typeId = 40002;
/* -- version 2.5.2.0,build 2015-04-07,module cmts */

-- version 2.5.3.1,build 2015-05-12,module cmts
insert into deviceperftarget VALUES 
			('cmc_cpuUsed', 60000, 40000, 41100, 'cmc_service', 2),
			('cmc_memUsed', 60000, 40000, 41100, 'cmc_service', 2),
			
			('cmc_cpuUsed', 60000, 40000, 43100, 'cmc_service', 2),
			('cmc_memUsed', 60000, 40000, 43100, 'cmc_service', 2);

INSERT INTO globalperfcollecttime VALUES
			('cmc_cpuUsed', 60000, 40000, 15, 1, 'cmc_service', 2),
			('cmc_memUsed', 60000, 40000, 15, 1, 'cmc_service', 2);
/* -- version 2.5.3.1,build 2015-05-12,module cmts */

-- version 2.5.3.1,build 2015-05-19,module cmts			
insert into deviceperftarget VALUES 
			('cmc_cpuUsed', 60000, 40000, 42100, 'cmc_service', 2),
			('cmc_memUsed', 60000, 40000, 42100, 'cmc_service', 2);
/* -- version 2.5.3.1,build 2015-05-19,module cmts */
			
-- version 2.4.5.0,build 2015-06-04,module cmts
delete from deviceperftarget where entityType = 40000;
insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 
			('cmc_onlineStatus', 60000, 40000, 41100, 'cmc_deviceStatus', 1),
			('cmc_snr', 60000, 40000, 41100, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 40000, 41100, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 40000, 41100, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 40000, 41100, 'cmc_flow', 4),
			('cmc_cpuUsed', 60000, 40000, 41100, 'cmc_service', 2),
			('cmc_memUsed', 60000, 40000, 41100, 'cmc_service', 2),
			
			('cmc_onlineStatus', 60000, 40000, 42100, 'cmc_deviceStatus', 1),
			('cmc_snr', 60000, 40000, 42100, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 40000, 42100, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 40000, 42100, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 40000, 42100, 'cmc_flow', 4),
			('cmc_cpuUsed', 60000, 40000, 42100, 'cmc_service', 2),
			('cmc_memUsed', 60000, 40000, 42100, 'cmc_service', 2),
			
			('cmc_onlineStatus', 60000, 40000, 43100, 'cmc_deviceStatus', 1),
			('cmc_snr', 60000, 40000, 43100, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 40000, 43100, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 40000, 43100, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 40000, 43100, 'cmc_flow', 4),
			('cmc_cpuUsed', 60000, 40000, 43100, 'cmc_service', 2),
			('cmc_memUsed', 60000, 40000, 43100, 'cmc_service', 2);

delete from globalperfcollecttime where entityType = 40000;
INSERT INTO globalperfcollecttime(perfTargetName, parentType, entityType, globalInterval, globalEnable, targetGroup, groupPriority) VALUES
			('cmc_onlineStatus', 60000, 40000, 5, 1, 'cmc_deviceStatus', 1),
			('cmc_snr', 60000, 40000, 15, 1, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 40000, 15, 1, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 40000, 15, 1, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 40000, 15, 1, 'cmc_flow', 4),
			('cmc_cpuUsed', 60000, 40000, 15, 1, 'cmc_service', 2),
			('cmc_memUsed', 60000, 40000, 15, 1, 'cmc_service', 2);	
/* -- version 2.4.5.0,build 2015-06-04,module cmts */			
			
-- version 2.6.0.0,build 2015-06-10,module cmts
update entitytype set sysObjectId = '' where typeId = 40000;
/* -- version 2.6.0.0,build 2015-06-10,module cmts */	

-- version 2.6.0.0,build 2015-06-18,module cmts
insert into systempreferences(name,value,module) values('sampleInterval',30,'cmtsFlowCollect');
insert into systempreferences(name,value,module) values('flowCollectType','43200,43300,43100','cmtsFlowCollect');
/* -- version 2.6.0.0,build 2015-06-18,module cmts */

-- version 2.6.0.0,build 2015-06-19,module cmts
update globalperfcollecttime set globalEnable = 1 where entityType = 40000 and targetGroup = 'cmc_flow';
/* -- version 2.6.0.0,build 2015-06-19,module cmts */

-- version 2.6.0.0,build 2015-06-23,module cmts
update entitytype set discoveryBean='cmtsDiscoveryService' where typeId in (41100,42100,43100);
/* -- version 2.6.0.0,build 2015-06-23,module cmts */

-- version 2.6.9.4,build 2016-07-27,module cmts
update entitytype set icon16 = 'network/cmts/cmts_16.png',icon32 = 'network/cmts/cmts_32.png',icon48 = 'network/cmts/cmts_48.png',icon64 = 'network/cmts/cmts_64.png' where icon16='network/cmts/cmts_16.gif';
/* -- version 2.6.9.4,build 2016-07-27,module cmts */

-- version 2.9.0.5,build 2017-05-09,module cmts
/* 增加公司类型 */
insert into EntityCorp(corpId,sysObjectID,name,displayName) values (4998,'1.3.6.1.4.1.4998','Arris','Arris');
insert into EntityCorp(corpId,sysObjectID,name,displayName) values (37737,'1.3.6.1.4.1.37737','DongYan','东研');
insert into EntityCorp(corpId,sysObjectID,name,displayName) values (3715,'1.3.6.1.4.1.3715','Teleste','Teleste');
/* -- version 2.9.0.5,build 2017-05-09,module cmts */
-- version 2.9.1.5,build 2017-07-31,module cmts
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(140000,   'ARRIS',  'ARRIS CMTS',  'network',  '/network',   '',   '',  '',  '',  '',  1);
update EntityTypeRelation set type = 140000 where type =  100000 and typeId = 44100;
/* -- version 2.9.1.5,build 2017-07-31,module cmts */