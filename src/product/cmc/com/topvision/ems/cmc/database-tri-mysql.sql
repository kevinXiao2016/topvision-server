/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-12-14,module cmc
CREATE TRIGGER tri_onu_cmc AFTER DELETE on oltonurelation 
for each row 
BEGIN 
declare v_onuId bigint(20);
set v_onuId=OLD.onuId; 
delete from cmcentityrelation where onuId=v_onuId;
END;
$$
/* -- version 1.0.0,build 2011-12-14,module cmc */
-- version 1.7.7.5,build 2013-1-23,module cmc
CREATE TRIGGER tri_cmcentityrelation After DELETE ON cmcentityrelation
FOR EACH ROW delete from entity where (typeId = 30001 or typeId = 30005) and parentId not in (
        select cmcEntityId from cmcentityrelation);
$$
/* -- version 1.7.7.5,build 2013-1-23,module cmc */

-- version 1.7.14.0,build 2013-5-22,module cmc

CREATE TRIGGER tri_cmcAttributeUpdate AFTER UPDATE on cmcattribute 
FOR EACH ROW BEGIN

		update entity set sysName=NEW.topCcmtsSysName ,sysDescr=NEW.topccmtsSysDescr ,
 sysContact=NEW.topCcmtsSysContact ,sysLocation=NEW.topCcmtsSysLocation 
 where entityId = NEW.cmcId;
END;
$$

create trigger tri_cmcAttributeInsert AFTER INSERT on cmcattribute
for each row BEGIN
update entity set sysName=NEW.topCcmtsSysName ,sysDescr=NEW.topccmtsSysDescr ,
 sysContact=NEW.topCcmtsSysContact ,sysLocation=NEW.topCcmtsSysLocation 
 where entityId = NEW.cmcId;
END;
$$

/* -- version 1.7.14.0,build 2013-5-22,module cmc */

-- version 1.7.18.1,build 2013-9-7,module cmc
CREATE TRIGGER tri_cmc_flow_top10 AFTER INSERT ON perfcmcflowquality
FOR EACH ROW BEGIN
insert into perfcmcflowqualitylast(cmcId,channelIndex,
        channelInOctets,channelOutOctets,channelOctets,
        channelInSpeed,channelOutSpeed,channelUtilization,collectTime)  
	values
	(NEW.cmcId, NEW.channelIndex, NEW.channelInOctets, NEW.channelOutOctets, NEW.channelOctets, 
	NEW.channelInSpeed, 
	NEW.channelOutSpeed, 
	NEW.channelUtilization, 
	NEW.collectTime
	)
ON DUPLICATE KEY update 
	channelInOctets=NEW.channelOutOctets, 
	channelOutOctets=NEW.channelOctets, 
	channelOctets=NEW.channelOctets,
	channelInSpeed=NEW.channelInSpeed, 
	channelOutSpeed=NEW.channelOutSpeed,
	channelUtilization=NEW.channelUtilization,
	collectTime=NEW.collectTime; 
END;
$$

CREATE TRIGGER tri_cmc_service_top10 AFTER INSERT ON perfcmcservicequality
FOR EACH ROW BEGIN
insert into perfcmcservicequalitylast 
	(cmcId, targetName, targetValue, collectTime)
	values
	(NEW.cmcId, NEW.targetName, NEW.targetValue, NEW.collectTime)
ON DUPLICATE KEY UPDATE
	targetValue=NEW.targetValue,
	collectTime=NEW.collectTime;
END;
$$

CREATE TRIGGER tri_cmc_link_top10 AFTER INSERT ON perfcmclinkquality
FOR EACH ROW BEGIN
insert into perfcmclinkqualitylast 
	(cmcId, portIndex, optTxPower, optRePower, optCurrent, 
	optVoltage, 
	optTemp, 
	collectTime
	)
	values
	(NEW.cmcId, NEW.portIndex, NEW.optTxPower, NEW.optRePower, NEW.optCurrent, NEW.optVoltage, 
	NEW.optTemp, 
	NEW.collectTime
	)
ON DUPLICATE KEY UPDATE
	optTxPower=NEW.optTxPower,
	optRePower=NEW.optRePower,
	optCurrent=NEW.optCurrent,
	optVoltage=NEW.optVoltage,
	optTemp=NEW.optTemp,
	collectTime=NEW.collectTime;
END;
$$

CREATE TRIGGER tri_cmc_signal_top10 AFTER INSERT ON perfcmcsignalquality
FOR EACH ROW BEGIN
insert into perfcmcsignalqualitylast 
	(cmcId, channelIndex, targetName, targetValue, collectTime
	)
	values
	(NEW.cmcId, NEW.channelIndex, NEW.targetName, NEW.targetValue, NEW.collectTime
	)
ON DUPLICATE KEY UPDATE
	targetValue=NEW.targetValue,
	collectTime=NEW.collectTime;
END;
$$

/*-- version 1.7.18.1,build 2013-9-7,module cmc*/

-- version 1.7.18.1,build 2013-9-9,module cmc
drop TRIGGER tri_cmcentityrelation;
CREATE TRIGGER tri_cmcentityrelationDelete After DELETE ON cmcentityrelation
FOR EACH ROW delete from entity where entityId=OLD.cmcId;
$$

/* -- version 1.7.18.1,build 2013-9-9,module cmc */

-- version 2.0.0.1,build 2013-12-2,module cmc
drop TRIGGER IF EXISTS tri_cmc_flow_top10;
CREATE TRIGGER tri_cmc_flow_top10 AFTER INSERT ON perfcmcflowquality
FOR EACH ROW BEGIN
insert into perfcmcflowqualitylast(cmcId,channelIndex,
        channelInOctets,channelOutOctets,channelOctets,
        channelInSpeed,channelOutSpeed,channelUtilization,collectTime)  
	values
	(NEW.cmcId, NEW.channelIndex, NEW.channelInOctets, NEW.channelOutOctets, NEW.channelOctets, 
	NEW.channelInSpeed, 
	NEW.channelOutSpeed, 
	NEW.channelUtilization, 
	NEW.collectTime
	)
ON DUPLICATE KEY update 
channelInOctets=NEW.channelInOctets, 
channelOutOctets=NEW.channelOutOctets, 
channelOctets=NEW.channelOctets,
channelInSpeed=NEW.channelInSpeed, 
channelOutSpeed=NEW.channelOutSpeed,
channelUtilization=NEW.channelUtilization,
collectTime=NEW.collectTime; 
END;
$$

/* -- version 2.0.0.1,build 2013-12-2,module cmc */
-- version 2.0.3.0,build 2014-1-9,module cmc
drop TRIGGER IF EXISTS tri_cm_flap_top10;
CREATE TRIGGER tri_cm_flap_top10 AFTER INSERT ON cmflaphis
FOR EACH ROW BEGIN
insert into cmflap 
	(cmId,cmcId, topCmFlapMacAddr, topCmFlapInsertionFailNum, 
	increaseInsNum, 
	lastFlapTime, 
	hitNum, 
	missNum, 
	increaseHitPercent, 
	crcErrorNum, 
	powerAdjLowerNum, 
	powerAdjHigherNum, 
	increasePowerAdjNum,
	dt
	)
	values
	(NEW.cmId,NEW.cmcId, NEW.cmMac, NEW.insFailNum, 
	NEW.increaseInsNum, 
	NEW.lastFlapTime, 
	NEW.hitNum, 
	NEW.missNum, 
	NEW.increaseHitPercent, 
	NEW.crcErrorNum, 
	NEW.powerAdjLowerNum, 
	NEW.powerAdjHigherNum, 
	NEW.increasePowerAdjNum,
	NEW.collectTime
	)
ON DUPLICATE KEY UPDATE
	topCmFlapInsertionFailNum = NEW.insFailNum, 
	increaseInsNum=NEW.increaseInsNum,     
	lastFlapTime=NEW.lastFlapTime,       
	hitNum=NEW.hitNum,             
	missNum=NEW.missNum,            
	increaseHitPercent=NEW.increaseHitPercent, 
	crcErrorNum=NEW.crcErrorNum,        
	powerAdjLowerNum=NEW.powerAdjLowerNum,   
	powerAdjHigherNum=NEW.powerAdjHigherNum,  
	increasePowerAdjNum=NEW.increasePowerAdjNum,
	dt=NEW.collectTime;
END$$ 
/*-- version 2.0.3.0,build 2014-1-9,module cmc*/


-- version 2.0.6.2,build 2014-3-13,module cmc
CREATE TRIGGER tri_cmc_snr_top10 AFTER INSERT ON perfcmcsnrquality
FOR EACH ROW BEGIN
insert into perfcmcsnrqualitylast 
	(cmcId, channelIndex, collectValue, collectTime
	)
	values
	(NEW.cmcId, NEW.channelIndex, NEW.collectValue, NEW.collectTime
	)
ON DUPLICATE KEY UPDATE
collectValue=NEW.collectValue,
collectTime=NEW.collectTime;
END$$

CREATE TRIGGER tri_cmc_errorcode_top10 AFTER INSERT ON perfcmcerrorcodequality
FOR EACH ROW BEGIN
insert into perfcmcerrorcodequalitylast 
	(cmcId, channelIndex, ccerCode, ucerCode, ccerRate, ucerRate, collectTime
	)
	values
	(NEW.cmcId, NEW.channelIndex, NEW.ccerCode, NEW.ucerCode, NEW.ccerRate, NEW.ucerRate, NEW.collectTime
	)
ON DUPLICATE KEY UPDATE
ccerCode=NEW.ccerCode,
ucerCode=NEW.ucerCode,
ccerRate=NEW.ccerRate,
ucerRate=NEW.ucerRate,
collectTime=NEW.collectTime;
END$$

/* -- version 2.0.6.2,build 2014-3-13,module cmc */

-- version 2.3.1.0,build 2014-8-11,module cmc
CREATE TRIGGER tri_cmc_tmp_top10 AFTER INSERT ON perfcmctempquality
FOR EACH ROW BEGIN
insert into perfcmctempqualitylast 
	(cmcId, cmcIndex, usTemp, dsTemp, outsideTemp, insideTemp, 
	collectTime
	)
	values
	(NEW.cmcId, NEW.cmcIndex, NEW.usTemp, NEW.dsTemp, NEW.outsideTemp, NEW.insideTemp,
	NEW.collectTime
	)
ON DUPLICATE KEY UPDATE
usTemp=NEW.usTemp,
dsTemp=NEW.dsTemp,
outsideTemp=NEW.outsideTemp,
insideTemp=NEW.insideTemp,
collectTime=NEW.collectTime;
END$$
/* -- version 2.3.1.0,build 2014-8-11,module cmc */

-- version 2.4.1.0,build 2014-8-18,module cmc
drop TRIGGER tri_cmc_errorcode_top10;
CREATE TRIGGER tri_cmc_errorcode_top10 AFTER INSERT ON perfcmcerrorcodequality
FOR EACH ROW BEGIN
insert into perfcmcerrorcodequalitylast 
	(cmcId, channelIndex, ccerCode, ucerCode, ccerRate, ucerRate, collectTime, noerCode, noerRate
	)
	values
	(NEW.cmcId, NEW.channelIndex, NEW.ccerCode, NEW.ucerCode, NEW.ccerRate, NEW.ucerRate, NEW.collectTime, NEW.noerCode, NEW.noerRate
	)
ON DUPLICATE KEY UPDATE
ccerCode=NEW.ccerCode,
ucerCode=NEW.ucerCode,
ccerRate=NEW.ccerRate,
ucerRate=NEW.ucerRate,
noerCode=NEW.noerCode,
noerRate=NEW.noerRate,
collectTime=NEW.collectTime;
END$$
/* -- version 2.4.1.0,build 2014-8-18,module cmc */

-- version 2.4.1.0,build 2014-10-15,module cmc
drop TRIGGER IF EXISTS tri_cmc_snr_top10;
create trigger tri_cmc_snr_top10 AFTER INSERT on perfcmcsnrquality
for each row 
BEGIN
insert into perfcmcsnrqualitylast (cmcId, channelIndex, collectValue, collectTime ) values 
(NEW.cmcId, NEW.channelIndex, NEW.collectValue, NEW.collectTime ) 
ON DUPLICATE KEY UPDATE collectValue=NEW.collectValue, collectTime=NEW.collectTime;
insert into perfcmcsnrqualitysummaryorigin(cmcId,channelIndex,targetValue,collectTime) 
values(NEW.cmcId, NEW.channelIndex, NEW.collectValue, DATE_FORMAT(NEW.collectTime,"%Y-%m-%d %H:00:00"));
END;
$$ 

drop TRIGGER IF EXISTS tri_cmc_flow_top10;
create trigger tri_cmc_flow_top10 AFTER INSERT on perfcmcflowquality 
for each row BEGIN 
insert into perfcmcflowqualitylast
(cmcId,channelIndex, channelInOctets,channelOutOctets,channelOctets, channelInSpeed,channelOutSpeed,channelUtilization,collectTime) values
(NEW.cmcId, NEW.channelIndex, NEW.channelInOctets, NEW.channelOutOctets, NEW.channelOctets, NEW.channelInSpeed, NEW.channelOutSpeed, NEW.channelUtilization, NEW.collectTime ) 
ON DUPLICATE KEY update channelInOctets=NEW.channelInOctets, channelOutOctets=NEW.channelOutOctets, channelOctets=NEW.channelOctets, channelInSpeed=NEW.channelInSpeed, channelOutSpeed=NEW.channelOutSpeed, channelUtilization=NEW.channelUtilization, collectTime=NEW.collectTime;
insert into perfcmcflowqualitysummaryorigin(cmcId, channelIndex, channelInSpeed, channelOutSpeed, channelUtilization,collectTime) 
values(NEW.cmcId, NEW.channelIndex, NEW.channelInSpeed, NEW.channelOutSpeed, NEW.channelUtilization,DATE_FORMAT(NEW.collectTime,"%Y-%m-%d %H:00:00"));
END;
$$

drop TRIGGER IF EXISTS tri_usernumhisccmts_summary;
create trigger tri_usernumhisccmts_summary AFTER INSERT on usernumhisccmts
for each row BEGIN 
insert into usernumhisccmtssummaryorigin(entityId,ccIfIndex,onlineNum,otherNum, offlineNum,interactiveNum,broadbandNum,
mtaNum,integratedNum,cpeNum,cpeInteractiveNum,cpeBroadbandNum,cpeMtaNum,realtime) 
values(NEW.entityId, NEW.ccIfIndex, NEW.onlineNum, NEW.otherNum,NEW.offlineNum, NEW.interactiveNum,
NEW.broadbandNum,NEW.mtaNum,NEW.integratedNum,NEW.cpeNum,
NEW.cpeInteractiveNum,NEW.cpeBroadbandNum,NEW.cpeMtaNum,
DATE_FORMAT(NEW.realtime,"%Y-%m-%d %H:00:00"));
END;
$$

drop TRIGGER IF EXISTS tri_usernumhisdevice_summary;
create trigger tri_usernumhisdevice_summary AFTER INSERT on usernumhisdevice
for each row BEGIN 
insert into usernumhisdevicesummaryorigin(entityId,onlineNum,otherNum, offlineNum,interactiveNum,broadbandNum,
mtaNum,integratedNum,cpeNum,cpeInteractiveNum,cpeBroadbandNum,cpeMtaNum,realtime) 
values(NEW.entityId, NEW.onlineNum, NEW.otherNum,NEW.offlineNum, NEW.interactiveNum,
NEW.broadbandNum,NEW.mtaNum,NEW.integratedNum,NEW.cpeNum,
NEW.cpeInteractiveNum,NEW.cpeBroadbandNum,NEW.cpeMtaNum,
DATE_FORMAT(NEW.realtime,"%Y-%m-%d %H:00:00"));
END;
$$

drop TRIGGER IF EXISTS tri_usernumhispon_summary;
create trigger tri_usernumhispon_summary AFTER INSERT on usernumhispon
for each row BEGIN 
insert into usernumhisponsummaryorigin(entityId,ponIndex,onlineNum,otherNum, offlineNum,interactiveNum,broadbandNum,
mtaNum,integratedNum,cpeNum,cpeInteractiveNum,cpeBroadbandNum,cpeMtaNum,realtime) 
values(NEW.entityId, NEW.ponIndex, NEW.onlineNum, NEW.otherNum,NEW.offlineNum, NEW.interactiveNum,
NEW.broadbandNum,NEW.mtaNum,NEW.integratedNum,NEW.cpeNum,
NEW.cpeInteractiveNum,NEW.cpeBroadbandNum,NEW.cpeMtaNum,
DATE_FORMAT(NEW.realtime,"%Y-%m-%d %H:00:00"));
END;
$$

drop TRIGGER IF EXISTS tri_usernumhisport_summary;
create trigger tri_usernumhisport_summary AFTER INSERT on usernumhisport
for each row BEGIN 
insert into usernumhisportsummaryorigin(entityId,ccIfIndex,portIfIndex,portType,onlineNum,otherNum, offlineNum,interactiveNum,broadbandNum,
mtaNum,integratedNum,cpeNum,cpeInteractiveNum,cpeBroadbandNum,cpeMtaNum,realtime) 
values(NEW.entityId, NEW.ccIfIndex,NEW.portIfIndex,NEW.portType, NEW.onlineNum, NEW.otherNum,NEW.offlineNum, NEW.interactiveNum,
NEW.broadbandNum,NEW.mtaNum,NEW.integratedNum,NEW.cpeNum,
NEW.cpeInteractiveNum,NEW.cpeBroadbandNum,NEW.cpeMtaNum,
DATE_FORMAT(NEW.realtime,"%Y-%m-%d %H:00:00"));
END;
$$
/*-- version 2.4.1.0,build 2014-10-15,module cmc*/

-- version 2.4.12.0,build 2015-6-5,module cmc
drop TRIGGER IF exists tri_cmattribute_i;
create TRIGGER tri_cmattribute_i BEFORE insert on cmattribute
FOR EACH ROW 
begin
if NEW.statusValue = 6 then set NEW.displaystatus = 1;
else set NEW.displaystatus = 0;
end if;
if NEW.StatusIpAddress is not null then set NEW.displayIp = NEW.StatusIpAddress;
elseif NEW.StatusInetAddress is not null then set NEW.displayIp = NEW.StatusInetAddress;
end if;
end;
$$

drop TRIGGER IF exists tri_cmattribute_u;
create TRIGGER tri_cmattribute_u BEFORE update on cmattribute
FOR EACH ROW 
begin
    if NEW.statusValue = 6 then set NEW.displaystatus = 1;
	else set NEW.displaystatus = 0;
	end if;
	if NEW.StatusIpAddress is not null then set NEW.displayIp = NEW.StatusIpAddress;
	elseif NEW.StatusInetAddress is not null then set NEW.displayIp = NEW.StatusInetAddress;
	end if;
end;
$$

/*-- version 2.4.12.0,build 2015-6-5,module cmc*/

-- version 2.4.5.0,build 2015-1-26,module cmc
drop TRIGGER IF EXISTS tri_cmc_flow_top10;
create trigger tri_cmc_flow_top10 AFTER INSERT on perfcmcflowquality 
for each row BEGIN 
insert into perfcmcflowqualitylast
(cmcId,channelIndex, channelInOctets,channelOutOctets,channelOctets, channelInSpeed,channelOutSpeed,channelUtilization,collectTime,portBandWidth) values
(NEW.cmcId, NEW.channelIndex, NEW.channelInOctets, NEW.channelOutOctets, NEW.channelOctets, NEW.channelInSpeed, NEW.channelOutSpeed, NEW.channelUtilization, NEW.collectTime,NEW.portBandWidth ) 
ON DUPLICATE KEY update channelInOctets=NEW.channelInOctets, channelOutOctets=NEW.channelOutOctets, channelOctets=NEW.channelOctets, channelInSpeed=NEW.channelInSpeed, channelOutSpeed=NEW.channelOutSpeed, channelUtilization=NEW.channelUtilization, collectTime=NEW.collectTime,portBandWidth=NEW.portBandWidth;
insert into perfcmcflowqualitysummaryorigin(cmcId, channelIndex, channelInSpeed, channelOutSpeed, channelUtilization,collectTime) 
values(NEW.cmcId, NEW.channelIndex, NEW.channelInSpeed, NEW.channelOutSpeed, NEW.channelUtilization,DATE_FORMAT(NEW.collectTime,"%Y-%m-%d %H:00:00"));
END;
$$
/*-- version 2.4.5.0,build 2015-1-26,module cmc*/

/*Move by Victor@20150625 from database-table-mysql.sql由于重新初始化是按照table，data，tri的顺序，table创建后会先执行drop，后面data和tri关于这个表的语句就会抛错，所以drop都移到tri文件中*/
-- version 2.6.0.0,build 2015-6-16,module cmc
drop table perfchannelspeedstatichis;
drop table perfchannelspeedstaticlast;
drop table perfchannelutilizationhis;
drop table perfchannelutilizationlast;
drop table perfcmcservicequality;
drop table perfcmcservicequalitylast;
drop table perfcmcsignalquality;
drop table perfcmcsignalqualitylast;
drop table perfnoisehis;
drop table perfnoiselast;
/*-- version 2.6.0.0,build 2015-6-16,module cmc*/


-- version 2.6.1.1,build 2015-08-01,module cmc
drop trigger tri_onu_cmc;
drop trigger tri_cmcentityrelationDelete;
/*-- version 2.6.1.1,build 2015-08-01,module cmc*/

-- version 2.6.4.0,build 2015-11-27,module cmc
drop TRIGGER IF EXISTS tri_cmc_snr_top10;
create trigger tri_cmc_snr_top10 AFTER INSERT on perfcmcsnrquality
for each row 
BEGIN
insert into perfcmcsnrqualitylast (cmcId, channelIndex, collectValue, collectTime ) values 
(NEW.cmcId, NEW.channelIndex, NEW.collectValue, NEW.collectTime ) 
ON DUPLICATE KEY UPDATE collectValue=NEW.collectValue, collectTime=NEW.collectTime;
insert into perfcmcsnrqualitysummaryorigin(cmcId,channelIndex,targetValue,collectTime) 
values(NEW.cmcId, NEW.channelIndex, NEW.collectValue, DATE_FORMAT(NEW.collectTime,"%Y-%m-%d 00:00:00"));
END;
$$ 

drop TRIGGER IF EXISTS tri_cmc_flow_top10;
create trigger tri_cmc_flow_top10 AFTER INSERT on perfcmcflowquality 
for each row BEGIN 
insert into perfcmcflowqualitylast
(cmcId,channelIndex, channelInOctets,channelOutOctets,channelOctets, channelInSpeed,channelOutSpeed,channelUtilization,collectTime,portBandWidth) values
(NEW.cmcId, NEW.channelIndex, NEW.channelInOctets, NEW.channelOutOctets, NEW.channelOctets, NEW.channelInSpeed, NEW.channelOutSpeed, NEW.channelUtilization, NEW.collectTime,NEW.portBandWidth ) 
ON DUPLICATE KEY update channelInOctets=NEW.channelInOctets, channelOutOctets=NEW.channelOutOctets, channelOctets=NEW.channelOctets, channelInSpeed=NEW.channelInSpeed, channelOutSpeed=NEW.channelOutSpeed, channelUtilization=NEW.channelUtilization, collectTime=NEW.collectTime,portBandWidth=NEW.portBandWidth;
insert into perfcmcflowqualitysummaryorigin(cmcId, channelIndex, channelInSpeed, channelOutSpeed, channelUtilization,collectTime) 
values(NEW.cmcId, NEW.channelIndex, NEW.channelInSpeed, NEW.channelOutSpeed, NEW.channelUtilization,DATE_FORMAT(NEW.collectTime,"%Y-%m-%d 00:00:00"));
END;
$$

drop TRIGGER IF EXISTS tri_usernumhisccmts_summary;
create trigger tri_usernumhisccmts_summary AFTER INSERT on usernumhisccmts
for each row BEGIN 
insert into usernumhisccmtssummaryorigin(entityId,ccIfIndex,onlineNum,otherNum, offlineNum,interactiveNum,broadbandNum,
mtaNum,integratedNum,cpeNum,cpeInteractiveNum,cpeBroadbandNum,cpeMtaNum,realtime) 
values(NEW.entityId, NEW.ccIfIndex, NEW.onlineNum, NEW.otherNum,NEW.offlineNum, NEW.interactiveNum,
NEW.broadbandNum,NEW.mtaNum,NEW.integratedNum,NEW.cpeNum,
NEW.cpeInteractiveNum,NEW.cpeBroadbandNum,NEW.cpeMtaNum,
DATE_FORMAT(NEW.realtime,"%Y-%m-%d 00:00:00"));
END;
$$

drop TRIGGER IF EXISTS tri_usernumhisdevice_summary;
create trigger tri_usernumhisdevice_summary AFTER INSERT on usernumhisdevice
for each row BEGIN 
insert into usernumhisdevicesummaryorigin(entityId,onlineNum,otherNum, offlineNum,interactiveNum,broadbandNum,
mtaNum,integratedNum,cpeNum,cpeInteractiveNum,cpeBroadbandNum,cpeMtaNum,realtime) 
values(NEW.entityId, NEW.onlineNum, NEW.otherNum,NEW.offlineNum, NEW.interactiveNum,
NEW.broadbandNum,NEW.mtaNum,NEW.integratedNum,NEW.cpeNum,
NEW.cpeInteractiveNum,NEW.cpeBroadbandNum,NEW.cpeMtaNum,
DATE_FORMAT(NEW.realtime,"%Y-%m-%d 00:00:00"));
END;
$$

drop TRIGGER IF EXISTS tri_usernumhispon_summary;
create trigger tri_usernumhispon_summary AFTER INSERT on usernumhispon
for each row BEGIN 
insert into usernumhisponsummaryorigin(entityId,ponIndex,onlineNum,otherNum, offlineNum,interactiveNum,broadbandNum,
mtaNum,integratedNum,cpeNum,cpeInteractiveNum,cpeBroadbandNum,cpeMtaNum,realtime) 
values(NEW.entityId, NEW.ponIndex, NEW.onlineNum, NEW.otherNum,NEW.offlineNum, NEW.interactiveNum,
NEW.broadbandNum,NEW.mtaNum,NEW.integratedNum,NEW.cpeNum,
NEW.cpeInteractiveNum,NEW.cpeBroadbandNum,NEW.cpeMtaNum,
DATE_FORMAT(NEW.realtime,"%Y-%m-%d 00:00:00"));
END;
$$

drop TRIGGER IF EXISTS tri_usernumhisport_summary;
create trigger tri_usernumhisport_summary AFTER INSERT on usernumhisport
for each row BEGIN 
insert into usernumhisportsummaryorigin(entityId,ccIfIndex,portIfIndex,portType,onlineNum,otherNum, offlineNum,interactiveNum,broadbandNum,
mtaNum,integratedNum,cpeNum,cpeInteractiveNum,cpeBroadbandNum,cpeMtaNum,realtime) 
values(NEW.entityId, NEW.ccIfIndex,NEW.portIfIndex,NEW.portType, NEW.onlineNum, NEW.otherNum,NEW.offlineNum, NEW.interactiveNum,
NEW.broadbandNum,NEW.mtaNum,NEW.integratedNum,NEW.cpeNum,
NEW.cpeInteractiveNum,NEW.cpeBroadbandNum,NEW.cpeMtaNum,
DATE_FORMAT(NEW.realtime,"%Y-%m-%d 00:00:00"));
END;
$$
/*-- version 2.6.4.0,build 2015-11-27,module cmc*/

-- version 2.6.6.0,build 2015-12-05,module cmc
CREATE TRIGGER tri_statuschangetimeupdate BEFORE UPDATE ON cmcattribute
FOR EACH ROW 
BEGIN 
	IF(NEW.topCcmtsSysStatus != OLD.topCcmtsSysStatus) THEN SET NEW.statusChangeTime = NOW();
	END if;
END;
$$
/*-- version 2.6.6.0,build 2015-12-05,module cmc*/

-- version 2.7.5.0,build 2016-11-03,module cmc
drop trigger if exists tri_cmfilenamechange;
create trigger tri_cmfilenamechange AFTER update on cmattribute for each row 
	begin 
	IF NEW.fileName!=OLD.fileName then
	insert into cmFileNameChangeLog(cmId,cmMac,oldFileName,newFileName,changeTime) values (NEW.cmId,NEW.StatusMacAddress,OLD.fileName,NEW.fileName,NOW());
	end if;
end$$
/*-- version 2.7.5.0,build 2016-11-03,module cmc*/

-- version 2.8.0.0,build 2016-11-17,module cmc
drop TRIGGER IF EXISTS tri_statuschangetimeupdate;
CREATE TRIGGER tri_statuschangetimeupdate BEFORE UPDATE ON cmcattribute
FOR EACH ROW 
BEGIN 
IF(NEW.topCcmtsSysStatus != OLD.topCcmtsSysStatus) THEN SET NEW.statusChangeTime = NOW();
END if;
IF(NEW.topCcmtsSysStatus != OLD.topCcmtsSysStatus) AND (select 1 from information_schema.tables where table_name = 'oltonuattribute')
THEN update oltonuattribute set onuOperationStatus = NEW.topCcmtsSysStatus where onuId = NEW.cmcId;
END if; 
END;
$$
/*-- version 2.8.0.0,build 2016-11-17,module cmc*/

-- version 2.8.0.1,build 2017-1-16,module cmc
drop TRIGGER IF EXISTS tri_cmcAttributeUpdate;
drop TRIGGER IF EXISTS tri_cmcAttributeInsert;

CREATE TRIGGER tri_cmcAttributeUpdate AFTER UPDATE on cmcattribute 
FOR EACH ROW BEGIN
update entity set sysName=NEW.topCcmtsSysName ,sysDescr=NEW.topccmtsSysDescr ,
sysContact=NEW.topCcmtsSysContact ,sysLocation=NEW.topCcmtsSysLocation,
contact=ifnull(contact,NEW.topCcmtsSysContact), location=ifnull(location,NEW.topCcmtsSysLocation)
where entityId = NEW.cmcId;
END;
$$

create trigger tri_cmcAttributeInsert AFTER INSERT on cmcattribute
for each row BEGIN
update entity set sysName=NEW.topCcmtsSysName ,sysDescr=NEW.topccmtsSysDescr ,
sysContact=NEW.topCcmtsSysContact ,sysLocation=NEW.topCcmtsSysLocation,
contact=ifnull(contact,NEW.topCcmtsSysContact), location=ifnull(location,NEW.topCcmtsSysLocation)
where entityId = NEW.cmcId;
END;
$$

/*-- version 2.8.0.1,build 2017-1-16,module cmc*/

-- version 2.9.1.14,build 2017-10-24,module cmc
drop TRIGGER IF EXISTS tri_cmc_dorNodeTemp_top10;
drop TRIGGER IF EXISTS tri_cmc_dorLinePower_top10;

create trigger tri_cmc_dorNodeTemp_top10 AFTER INSERT on perfCmcDorOptNodeTempquality
for each row 
BEGIN
insert into perfCmcDorOptNodeTempqualitylast (cmcId, cmcIndex, dorOptNodeTemp, collectTime ) values 
(NEW.cmcId, NEW.cmcIndex, NEW.dorOptNodeTemp, NEW.collectTime ) 
ON DUPLICATE KEY UPDATE dorOptNodeTemp=NEW.dorOptNodeTemp, collectTime=NEW.collectTime;
END;
$$ 

create trigger tri_cmc_dorLinePower_top10 AFTER INSERT on perfCmcDorLinePowerquality
for each row 
BEGIN
insert into perfCmcDorLinePowerqualitylast (cmcId, cmcIndex, dorLinePower, collectTime ) values 
(NEW.cmcId, NEW.cmcIndex, NEW.dorLinePower, NEW.collectTime ) 
ON DUPLICATE KEY UPDATE dorLinePower=NEW.dorLinePower, collectTime=NEW.collectTime;
END;
$$ 
/*-- version 2.9.1.14,build 2017-10-24,module cmc*/
