/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 2.6.8.0,build 2016-03-27,module epon
update nbiperfgroup set displayName ='OLT service quality' where groupId = 1;
update nbiperfgroup set displayName ='Optical link quality' where groupId = 2;
update nbiperfgroup set displayName ='Port Rate' where groupId = 3;
update nbiperfgroup set displayName ='Device delay' where groupId = 4;
update nbiperfgroup set displayName ='ONU Rate' where groupId = 5;
update nbiperfgroup set displayName ='ONU Optical link' where groupId = 6;

update nbiperfgroupindex set perfIndexDisplayName ='Board Temperature' where perfIndex = 1;
update nbiperfgroupindex set perfIndexDisplayName ='CPU utilization' where perfIndex = 2;
update nbiperfgroupindex set perfIndexDisplayName ='Flash utilization' where perfIndex = 3;
update nbiperfgroupindex set perfIndexDisplayName ='Memory utilization' where perfIndex = 4;
update nbiperfgroupindex set perfIndexDisplayName ='Fan speed' where perfIndex = 5;
update nbiperfgroupindex set perfIndexDisplayName ='Optical Transmission power' where perfIndex = 6;
update nbiperfgroupindex set perfIndexDisplayName ='Optical bias current' where perfIndex = 7;
update nbiperfgroupindex set perfIndexDisplayName ='Optical voltage' where perfIndex = 8;
update nbiperfgroupindex set perfIndexDisplayName ='Optical Temperature' where perfIndex = 9;
update nbiperfgroupindex set perfIndexDisplayName ='Port in rate' where perfIndex = 10;
update nbiperfgroupindex set perfIndexDisplayName ='Port in used' where perfIndex = 11;
update nbiperfgroupindex set perfIndexDisplayName ='Port out rate' where perfIndex = 12;
update nbiperfgroupindex set perfIndexDisplayName ='Port out used' where perfIndex = 13;
update nbiperfgroupindex set perfIndexDisplayName ='Device delay' where perfIndex = 14;
update nbiperfgroupindex set perfIndexDisplayName ='ONU port in rate' where perfIndex = 15;
update nbiperfgroupindex set perfIndexDisplayName ='ONU port out rate' where perfIndex = 16;
update nbiperfgroupindex set perfIndexDisplayName ='ONU Transmission power' where perfIndex = 17;
update nbiperfgroupindex set perfIndexDisplayName ='ONU receiving power' where perfIndex = 18;
update nbiperfgroupindex set perfIndexDisplayName ='LLID receiving powerutilization' where perfIndex = 19;           
/**-- version 2.6.8.0,build 2016-03-27,module epon*/

