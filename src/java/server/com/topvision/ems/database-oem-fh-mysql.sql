/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 2.9.1.1,build 2016-3-12,module server
DELETE FROM EntityType WHERE typeId in(33, 34, 36, 37, 38, 40, 48, 65, 68, 71, 10001, 10002, 10006, 30000, 30001, 30002, 30004, 30005, 30006, 30007, 30010, 30011, 30012, 30013, 30014, 30015, 30020, 30021, 30022, 30023, 40000, 41100, 42100, 43100, 60000, 70000, 80000, 90000, 100000, 110000, 120000);

UPDATE EntityType SET displayName='FHL1128B' WHERE typeId = 10003;
UPDATE EntityType SET displayName='FHL1016' WHERE typeId = 10005;
/*-- version 2.9.1.1,build 2016-3-12,module server*/

