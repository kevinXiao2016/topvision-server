/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

/** -- version 2.0.6.0,build 2014-4-17,module cmts **/
/** Modify by Victor@20150725 由于是在V2.4以后从cmc移过来，从V2.4会被重复执行，合入到后面的配置块（V2.4.5.0-2014-11-13）防止重复执行**/
-- version 2.4.5.0,build 2014-11-13,module cmts
drop trigger if exists tri_cmts_snr_top10;

CREATE TRIGGER tri_cmts_snr_top10 AFTER INSERT ON perfcmtssnrquality
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


drop trigger if exists tri_cmts_errorcode_top10;
CREATE TRIGGER tri_cmts_errorcode_top10 AFTER INSERT ON perfcmtserrorcodequality
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


drop trigger if exists tri_cmts_flow_top10;
CREATE TRIGGER tri_cmts_flow_top10 AFTER INSERT ON perfcmtsflowquality
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
END$$ 
/* -- version 2.0.6.0,build 2014-4-17,module cmts */

drop TRIGGER if exists tri_cmts_errorcode_top10;
CREATE TRIGGER tri_cmts_errorcode_top10 AFTER INSERT ON perfcmtserrorcodequality
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
/* -- version 2.4.5.0,build 2014-11-13,module cmts */

/*Move by Victor@20150625 from database-table-mysql.sql由于重新初始化是按照table，data，tri的顺序，table创建后会先执行drop，后面data和tri关于这个表的语句就会抛错，所以drop都移到tri文件中*/
-- version 2.6.0.0,build 2015-06-16,module cmts
drop table perfuplinkspeedstatichis;
drop table perfuplinkspeedstaticlast;
drop table perfuplinkutilizationhis;
drop table perfuplinkutilizationlast;
drop table perfusbiterrorratehis;
drop table perfusbiterrorratelast;
/* -- version 2.6.0.0,build 2015-06-16,module cmts */

-- version 2.6.6.0,build 2015-11-27,module cmts
drop TRIGGER IF EXISTS tri_cmts_flow_top10;
create trigger tri_cmts_flow_top10 AFTER INSERT on perfcmtsflowquality 
for each row BEGIN 
insert into perfcmcflowqualitylast
(cmcId,channelIndex, channelInOctets,channelOutOctets,channelOctets, channelInSpeed,channelOutSpeed,channelUtilization,collectTime,portBandWidth) values
(NEW.cmcId, NEW.channelIndex, NEW.channelInOctets, NEW.channelOutOctets, NEW.channelOctets, NEW.channelInSpeed, NEW.channelOutSpeed, NEW.channelUtilization, NEW.collectTime,NEW.portBandWidth ) 
ON DUPLICATE KEY update channelInOctets=NEW.channelInOctets, channelOutOctets=NEW.channelOutOctets, channelOctets=NEW.channelOctets, channelInSpeed=NEW.channelInSpeed, channelOutSpeed=NEW.channelOutSpeed, channelUtilization=NEW.channelUtilization, collectTime=NEW.collectTime,portBandWidth=NEW.portBandWidth;
insert into perfcmtsflowqualitysummaryorigin(cmcId, channelIndex, channelInSpeed, channelOutSpeed, channelUtilization,collectTime) 
values(NEW.cmcId, NEW.channelIndex, NEW.channelInSpeed, NEW.channelOutSpeed, NEW.channelUtilization,DATE_FORMAT(NEW.collectTime,"%Y-%m-%d 00:00:00"));
END;
$$

drop trigger if exists tri_cmts_snr_top10;
CREATE TRIGGER tri_cmts_snr_top10 AFTER INSERT ON perfcmtssnrquality
FOR EACH ROW BEGIN
insert into perfcmcsnrqualitylast (cmcId, channelIndex, collectValue, collectTime) values(NEW.cmcId, NEW.channelIndex, NEW.collectValue, NEW.collectTime)
ON DUPLICATE KEY UPDATE collectValue=NEW.collectValue,collectTime=NEW.collectTime;
insert into perfcmtssnrqualitysummaryorigin(cmcId,channelIndex,targetValue,collectTime) 
values(NEW.cmcId,NEW.channelIndex,NEW.collectValue,DATE_FORMAT(NEW.collectTime,"%Y-%m-%d 00:00:00"));
END$$
/* -- version 2.6.6.0,build 2015-11-27,module cmts */
