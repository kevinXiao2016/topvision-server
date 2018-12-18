/*
*SQLyog Community Edition- MySQL GUI v5.21
*Host - 5.0.19-nt-log : Database - ems
*********************************************************************
*Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-3-31,module server

/*************************************************/
/*注意：必须保证在后面几个表的alert后面执行，且要保证执行，建议这一段总在最后*/
/*AUTO_INCREMENT改变必须在表格创建完成，数据初始化之前*/
/*************************************************/
alter table MapNode                 AUTO_INCREMENT=10000000000;
alter table Entity                  AUTO_INCREMENT=30000000000;
alter table Monitor                 AUTO_INCREMENT=60000000000;

alter table EntityType              AUTO_INCREMENT=10000000000;
set global event_scheduler = "ON";
/* -- version 1.0.0,build 2011-3-31,module server */

-- version 2.7.1.0,build 2016-8-10,module server
INSERT INTO  ThresholdAlertLastValue (entityId,alertEventId,source,levelId) 
SELECT A.entityId,A.typeId,A.source,A.levelId FROM Alert A, AlertType B 
WHERE A.typeId=B.typeId AND FIND_IN_SET(A.typeId,topoEventFun(-50000,'alerttype'))
AND NOT EXISTS (SELECT 1 FROM ThresholdAlertLastValue C WHERE A.entityId=C.entityId AND A.source=C.source AND A.typeId=C.alertEventId);
/** -- version 2.7.1.0,build 2016-8-10,module server */

-- version 2.8.0.6,build 2017-1-3,module server
TRUNCATE TABLE Event;
TRUNCATE TABLE InitialDataCmAction;
TRUNCATE TABLE InitialDataCpeAction;
/** -- version 2.8.0.6,build 2017-1-3,module server */