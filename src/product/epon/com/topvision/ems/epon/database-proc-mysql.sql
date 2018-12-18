/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 2.0.0.1,build 2013-10-18,module epon
DROP PROCEDURE IF EXISTS migrateFlowHourly;
CREATE PROCEDURE migrateFlowHourly()
BEGIN 
	DECLARE NEVER_SUMMARIED int;
	DECLARE lastHour TIMESTAMP;
	DECLARE lastHourStart TIMESTAMP;
	DECLARE lastHourEnd TIMESTAMP;
	SELECT 0 INTO NEVER_SUMMARIED;
	SELECT DATE_SUB(now(), INTERVAL 1 HOUR) INTO lastHour;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:00:00") INTO lastHourStart;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:59:59") INTO lastHourEnd;
	-- migrate OLT's perfStats15Table into perfstats15tableSummary hourly
	INSERT INTO perfstats15tableSummary(
		entityId,portIndex,stats15InOctets,stats15OutOctets,stats15InOctetsMax,stats15OutOctetsMax,
		stats15EndTime, summarized)
	SELECT
		entityId,portIndex, portInOctets, portOutOctets,portInOctets,portOutOctets,
		collectTime,NEVER_SUMMARIED
	FROM 
		perfeponflowquality WHERE (collectTime BETWEEN lastHourStart AND lastHourEnd);
END$$

DROP PROCEDURE IF EXISTS summaryFlowDaily;
CREATE PROCEDURE summaryFlowDaily()
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
	-- summary OLT's perfStats15Table hourly
	INSERT INTO perfstats15tableSummary(
		entityId,portIndex,stats15InOctets,stats15OutOctets,stats15InOctetsMax,stats15OutOctetsMax,
		stats15EndTime,summarized)
	SELECT 
		entityId,portIndex,AVG(stats15InOctets) perfstats15tableSummary,AVG(stats15OutOctets) stats15OutOctets,MAX(stats15InOctetsMax) stats15InOctetsMax,MAX(stats15OutOctetsMax) stats15OutOctetsMax,
		DATE_FORMAT(stats15EndTime,"%Y-%m-%d %H:00:00") stats15EndTime,HOURLY_SUMMARIED
	FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryStart AND summaryEnd) and summarized != HOURLY_SUMMARIED
	GROUP BY entityId,portIndex,DATE_FORMAT(stats15EndTime,"%Y-%m-%d %H:00:00");
	DELETE FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryStart AND  summaryEnd) AND summarized = NEVER_SUMMARIED;
	-- summary OLT's perfStats15Table daily
	INSERT INTO perfstats15tableSummary(
		entityId,portIndex,stats15InOctets,stats15OutOctets,stats15InOctetsMax,stats15OutOctetsMax,
		stats15EndTime,summarized)
	SELECT
		entityId,portIndex,AVG(stats15InOctets) stats15InOctets,AVG(stats15OutOctets) stats15OutOctets,MAX(stats15InOctetsMax) stats15InOctetsMax,MAX(stats15OutOctetsMax) stats15OutOctetsMax,
		DATE_FORMAT(stats15EndTime,"%Y-%m-%d 00:00:00") stats15EndTime,DAILY_SUMMARIED
	FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryMonthStart AND summaryMonthEnd) and summarized != DAILY_SUMMARIED
	GROUP BY entityId,portIndex,DATE_FORMAT(stats15EndTime,"%Y-%m-%d %00:00:00");
	DELETE FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryMonthStart AND  summaryMonthEnd) AND summarized = HOURLY_SUMMARIED;
END$$


DROP PROCEDURE IF EXISTS initFlowSummary;
/* -- version 2.0.0.1,build 2013-10-18,module epon */


-- version 2.3.2.0,build 2014-10-15,module epon
create procedure sp_perfeponflowsummary()
begin
DECLARE _error INTEGER DEFAULT 0;   
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _error=1;  
start transaction;
insert ignore into perfeponflowqualitySummary(entityId, portIndex, portType, portInSpeedAvg, portInSpeedMin, portInSpeedMax,
portOutSpeedAvg,portOutSpeedMin,portOutSpeedMax,
portSpeedAvg,portSpeedMin,portSpeedMax,
summarizeTime
)
select A.entityId, A.portIndex, B.portType, portInSpeedAvg,portInSpeedMin,portInSpeedMax,portOutSpeedAvg,portOutSpeedMin,portOutSpeedMax,
portSpeedAvg,portSpeedMin,portSpeedMax,
collectTime from(
select entityId,portIndex,avg(portInSpeed) portInSpeedAvg ,min(portInSpeed) portInSpeedMin, max(portInSpeed) portInSpeedMax,
avg(portOutSpeed) portOutSpeedAvg,min(portOutSpeed) portOutSpeedMin,max(portOutSpeed) portOutSpeedMax, 
avg(portInSpeed+portOutSpeed) portSpeedAvg,min(portInSpeed+portOutSpeed) portSpeedMin,max(portInSpeed+portOutSpeed) portSpeedMax, 
collectTime 
from perfeponflowqualitysummaryorigin where collectTime < date(sysdate())
group by entityId,portIndex,collectTime) A inner join epon_portIndex_type B on A.entityId = B.entityId and A.portIndex = B.portIndex;
delete from perfeponflowqualitysummaryorigin where collectTime < date(sysdate());
if _error = 1 then
rollback;
else 
commit;
end if;
end;
$$
/* -- version 2.3.2.0,build 2014-10-15,module epon */

-- version 2.4.5.0,build 2014-11-11,module epon
DROP PROCEDURE IF EXISTS migrateFlowHourly;
CREATE PROCEDURE migrateFlowHourly()
BEGIN 
	DECLARE NEVER_SUMMARIED int;
	DECLARE lastHour TIMESTAMP;
	DECLARE lastHourStart TIMESTAMP;
	DECLARE lastHourEnd TIMESTAMP;
	SELECT 0 INTO NEVER_SUMMARIED;
	SELECT DATE_SUB(now(), INTERVAL 1 HOUR) INTO lastHour;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:00:00") INTO lastHourStart;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:59:59") INTO lastHourEnd;
	-- migrate OLT's perfStats15Table into perfstats15tableSummary hourly
	INSERT INTO perfstats15tableSummary(
		entityId,portIndex,stats15InOctets,stats15OutOctets,stats15InOctetsMax,stats15OutOctetsMax,
		stats15EndTime,portInSpeed,portInSpeedMax,portOutSpeed,portOutSpeedMax,
		portInUse,portInUseMax,portOutUse,portOutUseMax,portBandWidth,summarized)
	SELECT
		entityId,portIndex, portInOctets, portOutOctets,portInOctets,portOutOctets,
		collectTime,portInSpeed,portInSpeed,portOutSpeed,portOutSpeed,portOutSpeed,
		portInUse,portInUse,portOutUse,portOutUse,portBandWidth,NEVER_SUMMARIED
	FROM 
		perfeponflowquality WHERE (collectTime BETWEEN lastHourStart AND lastHourEnd);
END$$

DROP PROCEDURE IF EXISTS summaryFlowDaily;
CREATE PROCEDURE summaryFlowDaily()
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
	-- summary OLT's perfStats15Table hourly
	INSERT INTO perfstats15tableSummary(
		entityId,portIndex,stats15InOctets,stats15OutOctets,stats15InOctetsMax,stats15OutOctetsMax,
		portInSpeed,portInSpeedMax,portOutSpeed,portOutSpeedMax,
		portInUse,portInUseMax,portOutUse,portOutUseMax,portBandWidth,
		stats15EndTime,summarized)
	SELECT 
		entityId,portIndex,AVG(stats15InOctets) stats15InOctets,AVG(stats15OutOctets) stats15OutOctets,MAX(stats15InOctetsMax) stats15InOctetsMax,MAX(stats15OutOctetsMax) stats15OutOctetsMax,
		AVG(portInSpeed) portInSpeed,MAX(portInSpeed) portInSpeedMax,AVG(portOutSpeed) portOutSpeed,MAX(portOutSpeed) portOutSpeedMax,
		AVG(portInUse) portInUse,MAX(portInUse) portInUseMax,AVG(portOutUse) portOutUse,MAX(portOutUse) portOutUseMax,portBandWidth,
		DATE_FORMAT(stats15EndTime,"%Y-%m-%d %H:00:00") stats15EndTime,HOURLY_SUMMARIED
	FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryStart AND summaryEnd) and summarized != HOURLY_SUMMARIED
	GROUP BY entityId,portIndex,DATE_FORMAT(stats15EndTime,"%Y-%m-%d %H:00:00");
	DELETE FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryStart AND  summaryEnd) AND summarized = NEVER_SUMMARIED;
	-- summary OLT's perfStats15Table daily
	INSERT INTO perfstats15tableSummary(
		entityId,portIndex,stats15InOctets,stats15OutOctets,stats15InOctetsMax,stats15OutOctetsMax,
		portInSpeed,portInSpeedMax,portOutSpeed,portOutSpeedMax,
		portInUse,portInUseMax,portOutUse,portOutUseMax,portBandWidth,
		stats15EndTime,summarized)
	SELECT
		entityId,portIndex,AVG(stats15InOctets) stats15InOctets,AVG(stats15OutOctets) stats15OutOctets,MAX(stats15InOctetsMax) stats15InOctetsMax,MAX(stats15OutOctetsMax) stats15OutOctetsMax,
		AVG(portInSpeed) portInSpeed,MAX(portInSpeed) portInSpeedMax,AVG(portOutSpeed) portOutSpeed,MAX(portOutSpeed) portOutSpeedMax,
		AVG(portInUse) portInUse,MAX(portInUse) portInUseMax,AVG(portOutUse) portOutUse,MAX(portOutUse) portOutUseMax,portBandWidth,
		DATE_FORMAT(stats15EndTime,"%Y-%m-%d 00:00:00") stats15EndTime,DAILY_SUMMARIED
	FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryMonthStart AND summaryMonthEnd) and summarized != DAILY_SUMMARIED
	GROUP BY entityId,portIndex,DATE_FORMAT(stats15EndTime,"%Y-%m-%d %00:00:00");
	DELETE FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryMonthStart AND  summaryMonthEnd) AND summarized = HOURLY_SUMMARIED;
END$$
/* -- version 2.4.5.0,build 2014-11-11,module epon */

-- version 2.4.9.0,build 2015-04-09,module epon
DROP PROCEDURE IF EXISTS migrateFlowHourly;
CREATE PROCEDURE migrateFlowHourly()
BEGIN 
	DECLARE NEVER_SUMMARIED int;
	DECLARE lastHour TIMESTAMP;
	DECLARE lastHourStart TIMESTAMP;
	DECLARE lastHourEnd TIMESTAMP;
	SELECT 0 INTO NEVER_SUMMARIED;
	SELECT DATE_SUB(now(), INTERVAL 1 HOUR) INTO lastHour;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:00:00") INTO lastHourStart;
	SELECT DATE_FORMAT(lastHour,"%Y-%m-%d %H:59:59") INTO lastHourEnd;
	-- migrate OLT's perfStats15Table into perfstats15tableSummary hourly
	INSERT INTO perfstats15tableSummary(
		entityId,portIndex,stats15InOctets,stats15OutOctets,stats15InOctetsMax,stats15OutOctetsMax,
		stats15EndTime,portInSpeed,portInSpeedMax,portOutSpeed,portOutSpeedMax,
		portInUsed,portInUsedMax,portOutUsed,portOutUsedMax,portBandWidth,summarized)
	SELECT
		entityId,portIndex, portInOctets, portOutOctets,portInOctets,portOutOctets,
		collectTime,portInSpeed,portInSpeed,portOutSpeed,portOutSpeed,portOutSpeed,
		portInUsed,portInUsed,portOutUsed,portOutUsed,portBandWidth,NEVER_SUMMARIED
	FROM 
		perfeponflowquality WHERE (collectTime BETWEEN lastHourStart AND lastHourEnd);
END$$

DROP PROCEDURE IF EXISTS summaryFlowDaily;
CREATE PROCEDURE summaryFlowDaily()
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
	-- summary OLT's perfStats15Table hourly
	INSERT INTO perfstats15tableSummary(
		entityId,portIndex,stats15InOctets,stats15OutOctets,stats15InOctetsMax,stats15OutOctetsMax,
		portInSpeed,portInSpeedMax,portOutSpeed,portOutSpeedMax,
		portInUsed,portInUsedMax,portOutUsed,portOutUsedMax,portBandWidth,
		stats15EndTime,summarized)
	SELECT 
		entityId,portIndex,AVG(stats15InOctets) stats15InOctets,AVG(stats15OutOctets) stats15OutOctets,MAX(stats15InOctetsMax) stats15InOctetsMax,MAX(stats15OutOctetsMax) stats15OutOctetsMax,
		AVG(portInSpeed) portInSpeed,MAX(portInSpeed) portInSpeedMax,AVG(portOutSpeed) portOutSpeed,MAX(portOutSpeed) portOutSpeedMax,
		AVG(portInUsed) portInUsed,MAX(portInUsed) portInUsedMax,AVG(portOutUsed) portOutUsed,MAX(portOutUsed) portOutUsedMax,portBandWidth,
		DATE_FORMAT(stats15EndTime,"%Y-%m-%d %H:00:00") stats15EndTime,HOURLY_SUMMARIED
	FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryStart AND summaryEnd) and summarized != HOURLY_SUMMARIED
	GROUP BY entityId,portIndex,DATE_FORMAT(stats15EndTime,"%Y-%m-%d %H:00:00");
	DELETE FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryStart AND  summaryEnd) AND summarized = NEVER_SUMMARIED;
	-- summary OLT's perfStats15Table daily
	INSERT INTO perfstats15tableSummary(
		entityId,portIndex,stats15InOctets,stats15OutOctets,stats15InOctetsMax,stats15OutOctetsMax,
		portInSpeed,portInSpeedMax,portOutSpeed,portOutSpeedMax,
		portInUsed,portInUsedMax,portOutUsed,portOutUsedMax,portBandWidth,
		stats15EndTime,summarized)
	SELECT
		entityId,portIndex,AVG(stats15InOctets) stats15InOctets,AVG(stats15OutOctets) stats15OutOctets,MAX(stats15InOctetsMax) stats15InOctetsMax,MAX(stats15OutOctetsMax) stats15OutOctetsMax,
		AVG(portInSpeed) portInSpeed,MAX(portInSpeed) portInSpeedMax,AVG(portOutSpeed) portOutSpeed,MAX(portOutSpeed) portOutSpeedMax,
		AVG(portInUsed) portInUsed,MAX(portInUsed) portInUsedMax,AVG(portOutUsed) portOutUsed,MAX(portOutUsed) portOutUsedMax,portBandWidth,
		DATE_FORMAT(stats15EndTime,"%Y-%m-%d 00:00:00") stats15EndTime,DAILY_SUMMARIED
	FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryMonthStart AND summaryMonthEnd) and summarized != DAILY_SUMMARIED
	GROUP BY entityId,portIndex,DATE_FORMAT(stats15EndTime,"%Y-%m-%d %00:00:00");
	DELETE FROM perfstats15tableSummary WHERE (stats15EndTime BETWEEN summaryMonthStart AND  summaryMonthEnd) AND summarized = HOURLY_SUMMARIED;
END$$
/* -- version 2.4.9.0,build 2015-04-09,module epon */