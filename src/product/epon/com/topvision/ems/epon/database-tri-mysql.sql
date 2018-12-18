/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-12-14,module epon
CREATE TRIGGER tri_epon_snap AFTER UPDATE ON oltslotstatus
FOR EACH ROW BEGIN
declare v_slotId varchar(50);
declare v_count int(10);
set v_slotId=NEW.slotId;
set v_count=(select count(*) from oltslotstatus A , oltslotattribute B where A.slotId = B.slotId and A.slotId=v_slotId and B.bAttribute=1);
if v_count > 0 then 
INSERT INTO entitysnap(entityId,cpu,mem,vmem,usedMem,disk,usedDisk,diskio,delayTime) values (NEW.entityId,
 NEW.topSysBdCPUUseRatio/100,
(NEW.topSysBdlMemSize-NEW.topSysBdFreeMemSize)/NEW.topSysBdlMemSize,
 NEW.topSysBdFreeFlashOctets/1024/1024,
 NEW.topSysBdlMemSize-NEW.topSysBdFreeMemSize,
(NEW.topTotalFlashOctets- NEW.topSysBdFreeFlashOctets)/ NEW.topTotalFlashOctets,
 NEW.topTotalFlashOctets- NEW.topSysBdFreeFlashOctets,
 NEW.topTotalFlashOctets,
 now()
)
ON DUPLICATE KEY update 
cpu = NEW.topSysBdCPUUseRatio/100, 
mem = (NEW.topSysBdlMemSize-NEW.topSysBdFreeMemSize) / NEW.topSysBdlMemSize,
vmem =  NEW.topSysBdFreeFlashOctets/1024/1024,
usedMem =  NEW.topSysBdlMemSize-NEW.topSysBdFreeMemSize,
disk = (NEW.topTotalFlashOctets- NEW.topSysBdFreeFlashOctets)/ NEW.topTotalFlashOctets,
usedDisk =  NEW.topTotalFlashOctets- NEW.topSysBdFreeFlashOctets,
diskio =  NEW.topTotalFlashOctets,
delayTime = now() ;
end if;	 
END$$ 
/* -- version 1.0.0,build 2011-12-14,module epon */


-- version 1.7.10.0,build 2013-2-26,module epon 
CREATE TRIGGER tri_epon_reStart BEFORE INSERT ON oltDeviceUpTime 
FOR EACH ROW BEGIN
declare v_lastCount bigint(20);
declare v_lastDeviceUpTime bigint(20);
declare v_lastCollectTime bigint(20);
declare v_reStartTime bigint(20);
set v_lastCount = (select count(*) from oltDeviceUpTime where entityId = NEW.entityId and onuId = NEW.onuId);
if (v_lastCount > 0) then 
set v_lastDeviceUpTime = (select deviceUpTime from oltDeviceUpTime where entityId = NEW.entityId and onuId = NEW.onuId order by collectTime desc limit 0,1);
set v_lastCollectTime = (select collectTime from oltDeviceUpTime where entityId = NEW.entityId and onuId = NEW.onuId order by collectTime desc limit 0,1);
if (NEW.deviceUpTime - v_lastDeviceUpTime) > 60000 then 
set v_reStartTime = NEW.deviceUpTime;
insert into oltDeviceReStartTime(entityId, onuId, deviceLastOnlineTime, deviceReStartTime, collectTime) values 
(NEW.entityId, NEW.onuId, v_lastDeviceUpTime, v_reStartTime, NEW.collectTime);
end if;
end if;
END$$
/* -- version 1.7.10.0,build 2013-2-26,module epon */


-- version 1.7.14.0,build 2013-9-7,module epon
CREATE TRIGGER tri_epon_flow_top10 AFTER INSERT ON perfeponflowquality
FOR EACH ROW BEGIN
insert into perfeponflowqualitylast
	(entityId, portIndex, portInOctets, portOutOctets, 
	portInSpeed, 
	portOutSpeed, 
	collectTime
	)
	values
	(NEW.entityId, NEW.portIndex, NEW.portInOctets, NEW.portOutOctets, NEW.portInSpeed, 
	NEW.portOutSpeed, 
	NEW.collectTime
	)
ON DUPLICATE KEY update 
portInOctets=NEW.portInOctets, 
portOutOctets=NEW.portOutOctets, 
portInSpeed=NEW.portInSpeed,
portOutSpeed=NEW.portOutSpeed, 
collectTime=NEW.collectTime; 
END$$ 


CREATE TRIGGER tri_epon_link_top10 AFTER INSERT ON perfeponlinkquality
FOR EACH ROW BEGIN
insert into perfeponlinkqualitylast
	(entityId, portIndex, optTxPower, optRePower, optCurrent, 
	optVoltage, 
	optTemp, 
	collectTime
	)
	values
	(NEW.entityId, NEW.portIndex, NEW.optTxPower, NEW.optRePower, NEW.optCurrent, 
	NEW.optVoltage, 
	NEW.optTemp, 
	NEW.collectTime
	)
ON DUPLICATE KEY update 
optTxPower=NEW.optTxPower, 
optRePower=NEW.optRePower, 
optCurrent=NEW.optCurrent,
optVoltage=NEW.optVoltage, 
optTemp=NEW.optTemp,
collectTime=NEW.collectTime; 
END$$ 

CREATE TRIGGER tri_epon_service_top10 AFTER INSERT ON perfeponservicequality
FOR EACH ROW BEGIN
insert into perfeponservicequalitylast 
	(entityId, slotIndex, targetName, targetValue, collectTime
	)
	values
	(NEW.entityId, NEW.slotIndex, NEW.targetName, NEW.targetValue, NEW.collectTime
	)
ON DUPLICATE KEY UPDATE
targetValue=NEW.targetValue,
collectTime=NEW.collectTime;
END$$

/* -- version 1.7.14.0,build 2013-9-7,module epon */

-- version 2.0.3.0,build 2014-3-13,module epon
DROP TRIGGER IF EXISTS tri_epon_snap;
/* -- version 2.0.3.0,build 2014-3-13,module epon */


-- version 2.2.3.0,build 2014-8-12,module epon
DROP TRIGGER IF EXISTS tri_epon_flow_top10;
create trigger tri_epon_flow_top10 AFTER INSERT on perfeponflowquality
for each row
BEGIN 
insert into perfeponflowqualitylast (entityId, portIndex, portInOctets, portOutOctets, portInSpeed, portOutSpeed, collectTime) values (NEW.entityId, NEW.portIndex, NEW.portInOctets, NEW.portOutOctets, NEW.portInSpeed, NEW.portOutSpeed, NEW.collectTime )
ON DUPLICATE KEY update portInOctets=NEW.portInOctets, portOutOctets=NEW.portOutOctets, portInSpeed=NEW.portInSpeed, portOutSpeed=NEW.portOutSpeed, collectTime=NEW.collectTime; 
insert into perfeponflowqualitysummaryorigin(entityId, portIndex, portInSpeed, portOutSpeed, collectTime) 
values(NEW.entityId, NEW.portIndex, NEW.portInSpeed, NEW.portOutSpeed, DATE_FORMAT(NEW.collectTime,"%Y-%m-%d %H:00:00"));
END;
$$
/* -- version 2.2.3.0,build 2014-8-12,module epon */

-- version 2.3.2.0,build 2014-10-15,module epon
DROP TRIGGER IF EXISTS tri_epon_flow_top10;
create trigger tri_epon_flow_top10 AFTER INSERT on perfeponflowquality
for each row
BEGIN 
insert into perfeponflowqualitylast (entityId, portIndex, portInOctets, portOutOctets, portInSpeed, portOutSpeed, collectTime) values (NEW.entityId, NEW.portIndex, NEW.portInOctets, NEW.portOutOctets, NEW.portInSpeed, NEW.portOutSpeed, NEW.collectTime )
ON DUPLICATE KEY update portInOctets=NEW.portInOctets, portOutOctets=NEW.portOutOctets, portInSpeed=NEW.portInSpeed, portOutSpeed=NEW.portOutSpeed, collectTime=NEW.collectTime; 
insert into perfeponflowqualitysummaryorigin(entityId, portIndex, portInSpeed, portOutSpeed, collectTime) 
values(NEW.entityId, NEW.portIndex, NEW.portInSpeed, NEW.portOutSpeed, DATE_FORMAT(NEW.collectTime,"%Y-%m-%d %H:00:00"));
END;
$$
/* -- version 2.3.2.0,build 2014-10-15,module epon */

-- version 2.4.3.0,build 2014-10-25,module epon
DROP TRIGGER IF EXISTS tri_epon_flow_top10;
create trigger tri_epon_flow_top10 AFTER INSERT on perfeponflowquality
for each row
BEGIN 
insert into perfeponflowqualitylast (entityId, portIndex, portInOctets, portOutOctets, portInSpeed, portOutSpeed, collectTime,portInUsed,portOutUsed,portBandwidth) values (NEW.entityId, NEW.portIndex, NEW.portInOctets, NEW.portOutOctets, NEW.portInSpeed, NEW.portOutSpeed, NEW.collectTime,NEW.portInUsed,NEW.portOutUsed,NEW.portBandwidth)
ON DUPLICATE KEY update portInOctets=NEW.portInOctets, portOutOctets=NEW.portOutOctets, portInSpeed=NEW.portInSpeed, portOutSpeed=NEW.portOutSpeed, collectTime=NEW.collectTime,portInUsed=NEW.portInUsed,portOutUsed=NEW.portOutUsed,portBandwidth=NEW.portBandwidth; 
insert into perfeponflowqualitysummaryorigin(entityId, portIndex, portInSpeed, portOutSpeed, collectTime) 
values(NEW.entityId, NEW.portIndex, NEW.portInSpeed, NEW.portOutSpeed, DATE_FORMAT(NEW.collectTime,"%Y-%m-%d %H:00:00"));
END;
$$
/* -- version 2.4.3.0,build 2014-10-25,module epon */

-- version 2.5.2.0,build 2015-04-24,module epon
DROP TRIGGER IF EXISTS tri_onu_link_top10;
create trigger tri_onu_link_top10 AFTER INSERT ON perfonulinkquality
for each row
BEGIN 
insert into perfonulinkqualitylast (entityId, onuId, onuIndex, onuPonRevPower, onuPonTransPower, oltPonRevPower, collectTime) values (NEW.entityId, NEW.onuId, NEW.onuIndex, NEW.onuPonRevPower, NEW.onuPonTransPower, NEW.oltPonRevPower, NEW.collectTime)
ON DUPLICATE KEY update onuPonRevPower=NEW.onuPonRevPower, onuPonTransPower=NEW.onuPonTransPower, oltPonRevPower=NEW.oltPonRevPower, collectTime=NEW.collectTime; 
END;
$$

DROP TRIGGER IF EXISTS tri_onu_flow_top10;
create trigger tri_onu_flow_top10 AFTER INSERT on perfonuflowquality
for each row
BEGIN 
insert into perfonuflowqualitylast (onuId, portIndex, entityId, portInOctets, portOutOctets, portInSpeed, portOutSpeed, collectTime) values (NEW.onuId, NEW.portIndex, NEW.entityId, NEW.portInOctets, NEW.portOutOctets, NEW.portInSpeed, NEW.portOutSpeed, NEW.collectTime)
ON DUPLICATE KEY update portInOctets=NEW.portInOctets, portOutOctets=NEW.portOutOctets, portInSpeed=NEW.portInSpeed, portOutSpeed=NEW.portOutSpeed, collectTime=NEW.collectTime;
END;
$$
/* -- version 2.5.2.0,build 2015-04-24,module epon */

/*Move by Victor@20150625 from database-table-mysql.sql由于重新初始化是按照table，data，tri的顺序，table创建后会先执行drop，后面data和tri关于这个表的语句就会抛错，所以drop都移到tri文件中*/
-- version 2.6.0.0,build 2015-6-16,module epon
drop table perfeponservicequality;
drop table perfeponservicequalitylast;
/*-- version 2.6.0.0,build 2015-6-16,module epon*/


-- version 2.7.1.0,build 2016-6-1,module epon
DROP TRIGGER IF EXISTS tri_onu_catv_top10;
create trigger tri_onu_catv_top10 AFTER INSERT ON perfonucatvquality
for each row
BEGIN 
insert into perfonucatvqualitylast (entityId, onuId, onuIndex, onuCatvOrInfoRxPower, onuCatvOrInfoRfOutVoltage, onuCatvOrInfoVoltage, onuCatvOrInfoTemperature,collectTime) 
values (NEW.entityId, NEW.onuId, NEW.onuIndex, NEW.onuCatvOrInfoRxPower, NEW.onuCatvOrInfoRfOutVoltage, NEW.onuCatvOrInfoVoltage, NEW.onuCatvOrInfoTemperature,NEW.collectTime)
ON DUPLICATE KEY update onuCatvOrInfoRxPower=NEW.onuCatvOrInfoRxPower, onuCatvOrInfoRfOutVoltage=NEW.onuCatvOrInfoRfOutVoltage, onuCatvOrInfoVoltage=NEW.onuCatvOrInfoVoltage, onuCatvOrInfoTemperature=NEW.onuCatvOrInfoTemperature,collectTime=NEW.collectTime; 
END;
$$
/*-- version 2.7.1.0,build 2016-6-1,module epon*/

-- version 2.8.0.0,build 2016-11-17,module epon
DROP TRIGGER IF EXISTS tri_oltonuattribute;
CREATE TRIGGER tri_oltonuattribute BEFORE UPDATE ON oltonuattribute
FOR EACH ROW 
BEGIN 
IF(NEW.onuOperationStatus != OLD.onuOperationStatus) AND (NEW.onuOperationStatus = 2)
THEN SET NEW.lastDeregisterTime = NOW();
END if;
END;
$$
/*-- version 2.8.0.0,build 2016-11-17,module epon*/
