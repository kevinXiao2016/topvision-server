/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 2.4.6.0,build 2014-12-25,module mobile
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000011,'mobileDeviceConfig',4000000,'mobileConfig.mobileDeviceConfig');
/* -- version 2.4.6.0,build 2014-12-25,module mobile */
-- version 2.4.6.0,build 2015-1-13,module mobile
INSERT INTO mobileDeviceType(typeId,deviceType,corporation,frequency,powerlevel) values
        (1,'DS5103-MAX60','DEVISER','8/34/60','90/95/100/105');
INSERT INTO mobileDeviceType(typeId,deviceType,corporation,frequency,powerlevel) values
        (2,'DS5103-MAX64','DEVISER','8/34/64','90/95/100/105');
INSERT INTO mobileDeviceType(typeId,deviceType,corporation,frequency,powerlevel) values
        (3,'DS5103-MAX51','DEVISER','8/34/51','90/95/100/105');
INSERT INTO mobileDeviceType(typeId,deviceType,corporation,frequency,powerlevel) values
        (4,'DS5103-MAX40','DEVISER','20/40','90/95/100/105');

INSERT INTO mobileDefaultDeviceType(typeId) values(1);
/* -- version 2.4.6.0,build 2015-1-13,module mobile */
-- version 2.4.6.0,build 2015-1-19,module mobile
insert into systempreferences values('Mobile.DefaultPowerLevel','75','mobile');
insert into rolefunctionrela values(2,4000011);
update functionitem set displayName = 'sys.mobileConfig' where functionId = 4000011;
/* -- version 2.4.6.0,build 2015-1-19,module mobile */

-- version 2.5.2.0,build 2015-4-28,module mobile
delete from functionItem where functionId = 4000011;
insert into FunctionItem(functionId,name,superiorId,displayName) values
        (8000026,'mobileDeviceConfig',8000000,'mobileConfig.mobileDeviceConfig');
insert into rolefunctionrela values(2,8000026);
insert into rolefunctionrela values(1,8000026);
/* -- version 2.5.2.0,build 2015-4-28,module mobile */