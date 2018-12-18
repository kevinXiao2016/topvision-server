/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 2.0.0.1,build 2013-10-18,module cmc
/**modify by @bravin at 2013-6-16. */
DROP PROCEDURE IF EXISTS migrateSnrHourly;
CREATE PROCEDURE migrateSnrHourly()
BEGIN
	DECLARE NEVER_SUMMARIED int;
	DECLARE lastHour TIMESTAMP;
	DECLARE lastHourStart TIMESTAMP;
	DECLARE lastHourEnd TIMESTAMP;
	SELECT 0 INTO NEVER_SUMMARIED;
	SELECT DATE_SUB(now(), INTERVAL 1 HOUR) INTO lastHour;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:00:00") INTO lastHourStart;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:59:59") INTO lastHourEnd;
	-- migrate CCMTS's perfNoiseHis hourly
	INSERT INTO perfnoiseSummary(
		entityId,cmcId,ifindex,noise,noiseMin,dt,summarized)
	SELECT
		B.cmcentityId,A.cmcId,A.channelIndex,A.collectValue,A.collectValue,A.collectTime,NEVER_SUMMARIED
	FROM 
		perfcmcsnrquality A,cmcentityrelation B WHERE (A.collectTime BETWEEN lastHourStart AND lastHourEnd)
		AND A.cmcid=B.cmcId;
END$$

DROP PROCEDURE IF EXISTS summarySnrDaily;
CREATE PROCEDURE summarySnrDaily()
BEGIN
	DECLARE DAILY_SUMMARIED int;
	DECLARE HOURLY_SUMMARIED int;
	DECLARE NEVER_SUMMARIED int;
	DECLARE sevenDayAgo TIMESTAMP;
	DECLARE summaryStart TIMESTAMP;
	DECLARE summaryEnd TIMESTAMP;
	DECLARE monthAgo TIMESTAMP;
	DECLARE summaryMonthStart TIMESTAMP;
	DECLARE summaryMonthEnd TIMESTAMP;
	SELECT  2 INTO DAILY_SUMMARIED;
	SELECT  1 INTO HOURLY_SUMMARIED;
	SELECT  0 INTO NEVER_SUMMARIED;
	SELECT DATE_SUB(CURRENT_DATE,INTERVAL 7 DAY) INTO sevenDayAgo;
	SELECT DATE_FORMAT(sevenDayAgo,"%Y-%m-%d 00:00:00") INTO summaryStart;
	SELECT DATE_FORMAT(sevenDayAgo,"%Y-%m-%d 23:59:59") INTO summaryEnd;
	SELECT DATE_SUB(CURRENT_DATE,INTERVAL 30 DAY) INTO monthAgo;
	SELECT DATE_FORMAT(monthAgo,"%Y-%m-%d 00:00:00") INTO summaryMonthStart;
	SELECT DATE_FORMAT(monthAgo,"%Y-%m-%d 23:59:59") INTO summaryMonthEnd;
	-- init summary CCMTS's perfnoise hourly
	INSERT INTO perfnoiseSummary(
	entityId,cmcId,ifindex,noise,noiseMin,dt,summarized)
	SELECT 
	entityId,cmcId,ifindex,AVG(noise) noise,MIN(noiseMin) noiseMin,DATE_FORMAT(dt,"%Y-%m-%d %H:00:00") dt,HOURLY_SUMMARIED 
	FROM perfnoiseSummary WHERE (dt BETWEEN summaryStart AND summaryEnd) AND noise!=0
	GROUP BY cmcId,ifindex,DATE_FORMAT(dt,"%Y-%m-%d %H:00:00");
	DELETE FROM perfnoiseSummary WHERE (dt BETWEEN summaryStart AND  summaryEnd) AND summarized = NEVER_SUMMARIED;
	-- init summary CCMTS's perfnoise daily
	INSERT INTO perfnoiseSummary(
	entityId,cmcId,ifindex,noise,noiseMin,dt,summarized)
	SELECT
	entityId,cmcId,ifindex,AVG(noise) noise,MIN(noiseMin) noiseMin,DATE_FORMAT(dt,"%Y-%m-%d 00:00:00") dt,DAILY_SUMMARIED 
	FROM perfnoiseSummary WHERE (dt BETWEEN summaryMonthStart AND summaryMonthEnd) AND noise!=0
	GROUP BY cmcId,ifindex,DATE_FORMAT(dt,"%Y-%m-%d 00:00:00");
	DELETE FROM perfnoiseSummary WHERE (dt BETWEEN summaryMonthStart AND  summaryMonthEnd) AND summarized != DAILY_SUMMARIED;
END$$

DROP PROCEDURE IF EXISTS initSnrSummary;

DROP PROCEDURE IF EXISTS migrateChannelCmHourly;
CREATE PROCEDURE migrateChannelCmHourly()
BEGIN
	DECLARE NEVER_SUMMARIED int;
	DECLARE lastHour TIMESTAMP;
	DECLARE lastHourStart TIMESTAMP;
	DECLARE lastHourEnd TIMESTAMP;
	SELECT 0 INTO NEVER_SUMMARIED;
	SELECT DATE_SUB(now(), INTERVAL 2 HOUR) INTO lastHour;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:00:00") INTO lastHourStart;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:59:59") INTO lastHourEnd;
	-- migrate CCMTS's perfNoiseHis hourly
	INSERT INTO perfchannelcmnumSummary(
		entityId,cmNumTotal,cmNumTotalMax,cmNumOnline,cmNumOnlineMax,dt,summarized)
	SELECT
		entityId,(onlineNum+otherNum+offlineNum),(onlineNum+otherNum+offlineNum),onlineNum,onlineNum,realtime, 
		NEVER_SUMMARIED
	FROM 
		usernumhisdevice WHERE (realtime BETWEEN lastHourStart AND lastHourEnd);
END$$

DROP PROCEDURE IF EXISTS summaryChannelCmDaily;
CREATE PROCEDURE summaryChannelCmDaily()
BEGIN
	DECLARE DAILY_SUMMARIED int;
	DECLARE HOURLY_SUMMARIED int;
	DECLARE NEVER_SUMMARIED int;
	DECLARE sevenDayAgo TIMESTAMP;
	DECLARE summaryStart TIMESTAMP;
	DECLARE summaryEnd TIMESTAMP;
	DECLARE monthAgo TIMESTAMP;
	DECLARE summaryMonthStart TIMESTAMP;
	DECLARE summaryMonthEnd TIMESTAMP;
	SELECT  2 INTO DAILY_SUMMARIED;
	SELECT  1 INTO HOURLY_SUMMARIED;
	SELECT  0 INTO NEVER_SUMMARIED;
	SELECT DATE_SUB(CURRENT_DATE,INTERVAL 7 DAY) INTO sevenDayAgo;
	SELECT DATE_FORMAT(sevenDayAgo,"%Y-%m-%d 00:00:00") INTO summaryStart;
	SELECT DATE_FORMAT(sevenDayAgo,"%Y-%m-%d 23:59:59") INTO summaryEnd;
	SELECT DATE_SUB(CURRENT_DATE,INTERVAL 30 DAY) INTO monthAgo;
	SELECT DATE_FORMAT(monthAgo,"%Y-%m-%d 00:00:00") INTO summaryMonthStart;
	SELECT DATE_FORMAT(monthAgo,"%Y-%m-%d 23:59:59") INTO summaryMonthEnd;
	-- summary CCMTS's perfnoise hourly
	INSERT INTO perfchannelcmnumSummary(
		entityId,cmNumTotal,cmNumTotalMax,cmNumOnline,cmNumOnlineMax,dt,summarized)
	SELECT 
		entityId,AVG(cmNumTotal),MAX(cmNumTotalMax),AVG(cmNumOnline),MAX(cmNumOnlineMax),DATE_FORMAT(dt,"%Y-%m-%d %H:00:00") dt,HOURLY_SUMMARIED 
	FROM perfchannelcmnumSummary WHERE (dt BETWEEN summaryStart AND summaryEnd)
	GROUP BY entityId,DATE_FORMAT(dt,"%Y-%m-%d %H:00:00");
	DELETE FROM perfchannelcmnumSummary WHERE (dt BETWEEN summaryStart AND  summaryEnd) AND summarized=NEVER_SUMMARIED;
	-- summary CCMTS's perfnoise daily
	INSERT INTO perfchannelcmnumSummary(
		entityId,cmNumTotal,cmNumTotalMax,cmNumOnline,cmNumOnlineMax,dt,summarized)
	SELECT 
		entityId,AVG(cmNumTotal),MAX(cmNumTotalMax),AVG(cmNumOnline),MAX(cmNumOnlineMax),DATE_FORMAT(dt,"%Y-%m-%d 00:00:00") dt,DAILY_SUMMARIED 
	FROM perfchannelcmnumSummary WHERE (dt BETWEEN summaryMonthStart AND summaryMonthEnd)
	GROUP BY entityId,DATE_FORMAT(dt,"%Y-%m-%d 00:00:00");
	DELETE FROM perfchannelcmnumSummary WHERE (dt BETWEEN summaryMonthStart AND  summaryMonthEnd) AND summarized != DAILY_SUMMARIED;
END$$

DROP PROCEDURE IF EXISTS initChannelcmSummary;
/* -- version 2.0.0.1,build 2013-10-18,module cmc */
-- version 2.0.3.0,build 2014-1-13,module cmc
/*每三天清除一次cmflaphis表历史数据*/
drop procedure if exists delCmFlapHis;
CREATE PROCEDURE delCmFlapHis()
begin
	delete from cmflaphis	
	where DATEDIFF(NOW(),collectTime)>3;
end;
$$
drop event if exists threeDaysFlapHisDel;
CREATE EVENT threeDaysFlapHisDel   
ON SCHEDULE EVERY 3 DAY STARTS NOW() 
DO call delCmFlapHis();
$$

set global event_scheduler = on;  

/*-- version 2.0.3.0,build 2014-1-13,module cmc*/

-- version 2.3.1.0,build 2014-6-19,module cmc
DROP PROCEDURE IF EXISTS migrateSnrHourly;
CREATE PROCEDURE migrateSnrHourly()
BEGIN
	DECLARE NEVER_SUMMARIED int;
	DECLARE lastHour TIMESTAMP;
	DECLARE lastHourStart TIMESTAMP;
	DECLARE lastHourEnd TIMESTAMP;
	SELECT 0 INTO NEVER_SUMMARIED;
	SELECT DATE_SUB(now(), INTERVAL 1 HOUR) INTO lastHour;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:00:00") INTO lastHourStart;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:59:59") INTO lastHourEnd;
	-- migrate CCMTS's perfNoiseHis hourly
	INSERT INTO perfnoiseSummary(
		entityId,cmcId,ifindex,noise,noiseMin,dt,summarized)
	SELECT
		B.cmcentityId,A.cmcId,A.channelIndex,A.collectValue,A.collectValue,A.collectTime,NEVER_SUMMARIED
	FROM 
		perfcmcsnrquality A,cmcentityrelation B WHERE (A.collectTime BETWEEN lastHourStart AND lastHourEnd)
		AND A.cmcid=B.cmcId;
END$$
/*-- version 2.3.1.0,build 2014-6-19,module cmc*/

-- version 2.4.1.0,build 2014-10-15,module cmc
drop procedure IF EXISTS sp_perfcmcsnrsummary;
create procedure sp_perfcmcsnrsummary()
begin
DECLARE _error INTEGER DEFAULT 0;   
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _error=1;  
start transaction;
insert ignore into PerfcmcSnrqualitySummary(cmcId, channelIndex, snrAvg, snrMin, snrMax, summarizeTime)
select cmcId,channelIndex,avg(targetValue),min(targetValue),max(targetValue),collectTime
from perfcmcsnrqualitysummaryorigin where targetValue !=0 and collectTime < date(sysdate())
group by cmcId,channelIndex,collectTime;
delete from perfcmcsnrqualitysummaryorigin where collectTime < date(sysdate());
if _error = 1 then
rollback;
else 
commit;
end if;
end$$
 
drop procedure IF EXISTS sp_perfcmcflowsummary;
create procedure sp_perfcmcflowsummary()
begin
DECLARE _error INTEGER DEFAULT 0;   
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _error=1;  
start transaction;
insert ignore into perfcmcflowqualitySummary(cmcId, channelIndex, channelInSpeedAvg, channelInSpeedMin, channelInSpeedMax,
channelOutSpeedAvg,channelOutSpeedMin,channelOutSpeedMax,
channelUtilizationAvg,channelUtilizationMin,channelUtilizationMax,summarizeTime
) 
select cmcId,channelIndex,avg(channelInSpeed),min(channelInSpeed),max(channelInSpeed),
avg(channelOutSpeed),min(channelOutSpeed),max(channelOutSpeed),
avg(channelUtilization),min(channelUtilization),max(channelUtilization), collectTime
from perfcmcflowqualitysummaryorigin where collectTime < date(sysdate())
group by cmcId,channelIndex,collectTime;
delete from perfcmcflowqualitysummaryorigin where collectTime < date(sysdate());
if _error = 1 then
rollback;
else 
commit;
end if;
end$$

drop procedure IF EXISTS sp_perfcmtssnrsummary;
create procedure sp_perfcmtssnrsummary()
begin
DECLARE _error INTEGER DEFAULT 0;   
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _error=1;  
start transaction;
insert ignore into PerfcmtsSnrqualitySummary(cmcId, channelIndex, snrAvg, snrMin, snrMax, summarizeTime)
select cmcId,channelIndex,avg(targetValue),min(targetValue),max(targetValue),collectTime
from perfcmtssnrqualitysummaryorigin where targetValue !=0 and collectTime < date(sysdate())
group by cmcId,channelIndex,collectTime;
delete from perfcmtssnrqualitysummaryorigin where collectTime < date(sysdate());
if _error = 1 then
rollback;
else 
commit;
end if;
end$$
 
drop procedure IF EXISTS sp_perfcmtsflowsummary;
create procedure sp_perfcmtsflowsummary()
begin
DECLARE _error INTEGER DEFAULT 0;   
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _error=1;  
start transaction;
insert ignore into perfcmtsflowqualitySummary(cmcId, channelIndex, channelInSpeedAvg, channelInSpeedMin, channelInSpeedMax,
channelOutSpeedAvg,channelOutSpeedMin,channelOutSpeedMax,
channelUtilizationAvg,channelUtilizationMin,channelUtilizationMax,summarizeTime
) 
select cmcId,channelIndex,avg(channelInSpeed),min(channelInSpeed),max(channelInSpeed),
avg(channelOutSpeed),min(channelOutSpeed),max(channelOutSpeed),
avg(channelUtilization),min(channelUtilization),max(channelUtilization), collectTime
from perfcmtsflowqualitysummaryorigin where collectTime < date(sysdate())
group by cmcId,channelIndex,collectTime;
delete from perfcmtsflowqualitysummaryorigin where collectTime < date(sysdate());
if _error = 1 then
rollback;
else 
commit;
end if;
end$$

drop procedure IF EXISTS sp_perfusernumhisccmtssummary;
create procedure sp_perfusernumhisccmtssummary()
begin
DECLARE _error INTEGER DEFAULT 0;   
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _error=1;  
start transaction;
insert ignore into usernumhisccmtssummary (entityId, cmcId, cmcType, ccIfIndex, onlineNumAvg, onlineNumMax, onlineNumMin, 
otherNumAvg, otherNumMax, otherNumMin, offlineNumAvg, offlineNumMax, offlineNumMin, 
totalNumAvg, totalNumMax, totalNumMin,
interactiveNumAvg, interactiveNumMax, interactiveNumMin, broadbandNumAvg, broadbandNumMax, broadbandNumMin, 
mtaNumAvg, mtaNumMax, mtaNumMin, 
integratedNumAvg, integratedNumMax, integratedNumMin, 
cpeNumAvg, cpeNumMax, cpeNumMin, 
cpeInteractiveNumAvg, cpeInteractiveNumMax, cpeInteractiveNumMin, 
cpeBroadbandNumAvg, cpeBroadbandNumMax, cpeBroadbandNumMin, 
cpeMtaNumAvg, cpeMtaNumMax, cpeMtaNumMin, summarizeTime)
select entityId, ifnull((select cmcId from cmcentityrelation where cmcIndex= ccIfIndex and cmcentityId = entityId), entityId) _cmcId,
(select cmcType from cmcentityrelation where cmcId = _cmcId) cmcType,
ccIfIndex,avg(onlineNum),max(onlineNum),min(onlineNum),
avg(otherNum),max(otherNum),min(otherNum),
avg(offlineNum),max(offlineNum),min(offlineNum),
avg(onlineNum+otherNum+offlineNum),max(onlineNum+otherNum+offlineNum),min(onlineNum+otherNum+offlineNum),
avg(interactiveNum),max(interactiveNum),min(interactiveNum),
avg(broadbandNum),max(broadbandNum),min(broadbandNum),
avg(mtaNum),max(mtaNum),min(mtaNum),
avg(integratedNum),max(integratedNum),min(integratedNum),
avg(cpeNum),max(cpeNum),min(cpeNum),
avg(cpeInteractiveNum),max(cpeInteractiveNum),min(cpeInteractiveNum),
avg(cpeBroadbandNum),max(cpeBroadbandNum),min(cpeBroadbandNum),
avg(cpeMtaNum),max(cpeMtaNum),min(cpeMtaNum), realtime from usernumhisccmtssummaryorigin where realtime < date(sysdate())
group by entityId,ccIfIndex,realtime;
delete from usernumhisccmtssummaryorigin where realtime < date(sysdate());
if _error = 1 then
rollback;
else 
commit;
end if;
end$$
 
drop procedure IF EXISTS sp_perfusernumhisdevicesummary;
create procedure sp_perfusernumhisdevicesummary()
begin
DECLARE _error INTEGER DEFAULT 0;   
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _error=1;  
start transaction;
insert ignore into usernumhisdevicesummary (entityId, onlineNumAvg, onlineNumMax, onlineNumMin, 
otherNumAvg, otherNumMax, otherNumMin, offlineNumAvg, offlineNumMax, offlineNumMin, 
totalNumAvg, totalNumMax, totalNumMin,
interactiveNumAvg, interactiveNumMax, interactiveNumMin, broadbandNumAvg, broadbandNumMax, broadbandNumMin, 
mtaNumAvg, mtaNumMax, mtaNumMin, 
integratedNumAvg, integratedNumMax, integratedNumMin, 
cpeNumAvg, cpeNumMax, cpeNumMin, 
cpeInteractiveNumAvg, cpeInteractiveNumMax, cpeInteractiveNumMin, 
cpeBroadbandNumAvg, cpeBroadbandNumMax, cpeBroadbandNumMin, 
cpeMtaNumAvg, cpeMtaNumMax, cpeMtaNumMin, summarizeTime)
select entityId,avg(onlineNum),max(onlineNum),min(onlineNum),
avg(otherNum),max(otherNum),min(otherNum),
avg(offlineNum),max(offlineNum),min(offlineNum),
avg(onlineNum+otherNum+offlineNum),max(onlineNum+otherNum+offlineNum),min(onlineNum+otherNum+offlineNum),
avg(interactiveNum),max(interactiveNum),min(interactiveNum),
avg(broadbandNum),max(broadbandNum),min(broadbandNum),
avg(mtaNum),max(mtaNum),min(mtaNum),
avg(integratedNum),max(integratedNum),min(integratedNum),
avg(cpeNum),max(cpeNum),min(cpeNum),
avg(cpeInteractiveNum),max(cpeInteractiveNum),min(cpeInteractiveNum),
avg(cpeBroadbandNum),max(cpeBroadbandNum),min(cpeBroadbandNum),
avg(cpeMtaNum),max(cpeMtaNum),min(cpeMtaNum), realtime from 
usernumhisdevicesummaryorigin A where realtime < date(sysdate())
group by entityId,realtime;
delete from usernumhisdevicesummaryorigin where realtime < date(sysdate());
if _error = 1 then
rollback;
else 
commit;
end if;
end$$

drop procedure IF EXISTS sp_perfusernumhisponsummary;
create procedure sp_perfusernumhisponsummary()
begin
DECLARE _error INTEGER DEFAULT 0;   
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _error=1;  
start transaction;
insert ignore into usernumhisponsummary (entityId, ponIndex, onlineNumAvg, onlineNumMax, onlineNumMin, 
otherNumAvg, otherNumMax, otherNumMin, offlineNumAvg, offlineNumMax, offlineNumMin, 
totalNumAvg, totalNumMax, totalNumMin,
interactiveNumAvg, interactiveNumMax, interactiveNumMin, broadbandNumAvg, broadbandNumMax, broadbandNumMin, 
mtaNumAvg, mtaNumMax, mtaNumMin, 
integratedNumAvg, integratedNumMax, integratedNumMin, 
cpeNumAvg, cpeNumMax, cpeNumMin, 
cpeInteractiveNumAvg, cpeInteractiveNumMax, cpeInteractiveNumMin, 
cpeBroadbandNumAvg, cpeBroadbandNumMax, cpeBroadbandNumMin, 
cpeMtaNumAvg, cpeMtaNumMax, cpeMtaNumMin, summarizeTime)
select entityId, ponIndex, avg(onlineNum),max(onlineNum),min(onlineNum),
avg(otherNum),max(otherNum),min(otherNum),
avg(offlineNum),max(offlineNum),min(offlineNum),
avg(onlineNum+otherNum+offlineNum),max(onlineNum+otherNum+offlineNum),min(onlineNum+otherNum+offlineNum),
avg(interactiveNum),max(interactiveNum),min(interactiveNum),
avg(broadbandNum),max(broadbandNum),min(broadbandNum),
avg(mtaNum),max(mtaNum),min(mtaNum),
avg(integratedNum),max(integratedNum),min(integratedNum),
avg(cpeNum),max(cpeNum),min(cpeNum),
avg(cpeInteractiveNum),max(cpeInteractiveNum),min(cpeInteractiveNum),
avg(cpeBroadbandNum),max(cpeBroadbandNum),min(cpeBroadbandNum),
avg(cpeMtaNum),max(cpeMtaNum),min(cpeMtaNum), realtime from 
usernumhisponsummaryorigin A where realtime < date(sysdate())
group by entityId,ponIndex,realtime;
delete from usernumhisponsummaryorigin where realtime < date(sysdate());
if _error = 1 then
rollback;
else 
commit;
end if;
end$$

drop procedure IF EXISTS sp_perfusernumhisportsummary;
create procedure sp_perfusernumhisportsummary()
begin
DECLARE _error INTEGER DEFAULT 0;   
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _error=1;  
start transaction;
insert ignore into usernumhisportsummary (entityId, ccIfIndex, portIfIndex, portType, onlineNumAvg, onlineNumMax, onlineNumMin, 
otherNumAvg, otherNumMax, otherNumMin, offlineNumAvg, offlineNumMax, offlineNumMin, 
totalNumAvg, totalNumMax, totalNumMin,
interactiveNumAvg, interactiveNumMax, interactiveNumMin, broadbandNumAvg, broadbandNumMax, broadbandNumMin, 
mtaNumAvg, mtaNumMax, mtaNumMin, 
integratedNumAvg, integratedNumMax, integratedNumMin, 
cpeNumAvg, cpeNumMax, cpeNumMin, 
cpeInteractiveNumAvg, cpeInteractiveNumMax, cpeInteractiveNumMin, 
cpeBroadbandNumAvg, cpeBroadbandNumMax, cpeBroadbandNumMin, 
cpeMtaNumAvg, cpeMtaNumMax, cpeMtaNumMin, summarizeTime)
select entityId, ccIfIndex, portIfIndex, portType, avg(onlineNum),max(onlineNum),min(onlineNum),
avg(otherNum),max(otherNum),min(otherNum),
avg(offlineNum),max(offlineNum),min(offlineNum),
avg(onlineNum+otherNum+offlineNum),max(onlineNum+otherNum+offlineNum),min(onlineNum+otherNum+offlineNum),
avg(interactiveNum),max(interactiveNum),min(interactiveNum),
avg(broadbandNum),max(broadbandNum),min(broadbandNum),
avg(mtaNum),max(mtaNum),min(mtaNum),
avg(integratedNum),max(integratedNum),min(integratedNum),
avg(cpeNum),max(cpeNum),min(cpeNum),
avg(cpeInteractiveNum),max(cpeInteractiveNum),min(cpeInteractiveNum),
avg(cpeBroadbandNum),max(cpeBroadbandNum),min(cpeBroadbandNum),
avg(cpeMtaNum),max(cpeMtaNum),min(cpeMtaNum), realtime from 
usernumhisportsummaryorigin where realtime < date(sysdate())
group by entityId,ccIfIndex,portIfIndex,portType,realtime;
delete from usernumhisportsummaryorigin where realtime < date(sysdate());
if _error = 1 then
rollback;
else 
commit;
end if;
end$$

/*-- version 2.4.1.0,build 2014-10-15,module cmc*/
