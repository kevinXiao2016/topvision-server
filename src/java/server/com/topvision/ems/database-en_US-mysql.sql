/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 2.6.8.0,build 2016-3-12,module server
UPDATE FolderCategory SET name = 'EPON' WHERE categoryId = 10;
UPDATE FolderCategory SET name = 'CMTS' WHERE categoryId = 20;

UPDATE EntityCorp SET displayName = 'Cisco' WHERE corpId = 9;
UPDATE EntityCorp SET displayName = 'Apple' WHERE corpId = 63;
UPDATE EntityCorp SET displayName = 'Nokia' WHERE corpId = 94;
UPDATE EntityCorp SET displayName = 'Sony' WHERE corpId = 122;
UPDATE EntityCorp SET displayName = 'Motorola' WHERE corpId = 161;
UPDATE EntityCorp SET displayName = 'Microsoft' WHERE corpId = 311;
UPDATE EntityCorp SET displayName = 'Dell' WHERE corpId = 674;
UPDATE EntityCorp SET displayName = 'Huawei' WHERE corpId = 2011;
UPDATE EntityCorp SET displayName = 'Alcatel-Lucent' WHERE corpId = 6527;
UPDATE EntityCorp SET displayName = 'Lenovo' WHERE corpId = 12284;
UPDATE EntityCorp SET displayName = 'Sumavision' WHERE corpId = 32285;

UPDATE AlertComment SET name = 'Fixed' WHERE commentId = 1;
UPDATE AlertComment SET name = 'Network problem' WHERE commentId = 2;
UPDATE AlertComment SET name = 'Maintenance' WHERE commentId = 3;

UPDATE ActionType SET displayName = 'Email' WHERE actionTypeId = 1;
UPDATE ActionType SET displayName = 'Sms' WHERE actionTypeId = 2;
UPDATE ActionType SET displayName = 'Trap' WHERE actionTypeId = 3;

UPDATE virtualNetwork SET virtualName = 'Default subnet' WHERE virtualNetId = 1;
/*-- version 2.6.8.0,build 2016-3-12,module server*/

