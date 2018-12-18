/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 2.6.8.0, build 2016-3-15, module cmc
update EntityType set displayName ='CC8800E' where typeId = 30010;
update EntityType set displayName ='CC8800C-E' where typeId = 30011;
update EntityType set displayName ='CC8800D-E' where typeId = 30012;
update EntityType set displayName ='CC8800C-10G' where typeId = 30020;

update EntityType set displayName ='CC8800E(Centralized)' where typeId = 30013;
update EntityType set displayName ='CC8800C-E(Centralized)' where typeId = 30014;
update EntityType set displayName ='CC8800D-E(Centralized)' where typeId = 30015;

update EntityType set displayName ='CC8800C-10G(Centralized)' where typeId = 30021;
/* -- version 2.6.8.0, build 2016-3-15, module cmc */


-- version 2.6.8.0,build 2016-3-27,module cmc
update nbiperfgroup set displayName ='CMTS service quality' where groupId = 7;
update nbiperfgroup set displayName ='CMTS temperature' where groupId = 8;
update nbiperfgroup set displayName ='CMTS Link optical' where groupId = 9;
update nbiperfgroup set displayName ='CMTS signal' where groupId = 10;
update nbiperfgroup set displayName ='CMTS Optical receive power' where groupId = 11;
update nbiperfgroup set displayName ='CMTS channel rate' where groupId = 12;
update nbiperfgroup set displayName ='CMTS device rate' where groupId = 13;
update nbiperfgroup set displayName ='CM number' where groupId = 14;

update nbiperfgroupindex set perfIndexDisplayName ='CPU utilization' where perfIndex = 20;
update nbiperfgroupindex set perfIndexDisplayName ='Memory utilization' where perfIndex = 21;
update nbiperfgroupindex set perfIndexDisplayName ='Flash utilization' where perfIndex = 22;
update nbiperfgroupindex set perfIndexDisplayName ='Upstream  amplifier module' where perfIndex = 23;
update nbiperfgroupindex set perfIndexDisplayName ='Downstream  amplifier module' where perfIndex = 24;
update nbiperfgroupindex set perfIndexDisplayName ='DOCSIS MAC Temperature' where perfIndex = 25;
update nbiperfgroupindex set perfIndexDisplayName ='Optical Transmission power' where perfIndex = 26;
update nbiperfgroupindex set perfIndexDisplayName ='Optical receiving power' where perfIndex = 27;
update nbiperfgroupindex set perfIndexDisplayName ='Optical voltage' where perfIndex = 28;
update nbiperfgroupindex set perfIndexDisplayName ='Optical Temperature' where perfIndex = 29;
update nbiperfgroupindex set perfIndexDisplayName ='Optical bias current' where perfIndex = 30;
update nbiperfgroupindex set perfIndexDisplayName ='SNR' where perfIndex = 31;
update nbiperfgroupindex set perfIndexDisplayName ='Error-correcting code' where perfIndex = 32;
update nbiperfgroupindex set perfIndexDisplayName ='non-error-correcting Code' where perfIndex = 33;
update nbiperfgroupindex set perfIndexDisplayName ='Optical receive inputPower' where perfIndex = 34;
update nbiperfgroupindex set perfIndexDisplayName ='Channel rate' where perfIndex = 35;
update nbiperfgroupindex set perfIndexDisplayName ='Channel utilization' where perfIndex = 36;
update nbiperfgroupindex set perfIndexDisplayName ='MAC domain in rate' where perfIndex = 37;
update nbiperfgroupindex set perfIndexDisplayName ='MAC domain out rate' where perfIndex = 38;
update nbiperfgroupindex set perfIndexDisplayName ='MAC domain used' where perfIndex = 39;
update nbiperfgroupindex set perfIndexDisplayName ='Uplink in rate' where perfIndex = 40;
update nbiperfgroupindex set perfIndexDisplayName ='Uplink out rate' where perfIndex = 41;
update nbiperfgroupindex set perfIndexDisplayName ='Uplink in used' where perfIndex = 42;
update nbiperfgroupindex set perfIndexDisplayName ='Uplink out used' where perfIndex = 43;
update nbiperfgroupindex set perfIndexDisplayName ='Online Cm number' where perfIndex = 44;
update nbiperfgroupindex set perfIndexDisplayName ='Offline Cm number' where perfIndex = 45;
update nbiperfgroupindex set perfIndexDisplayName ='Total Cm number' where perfIndex = 46;
update nbiperfgroupindex set perfIndexDisplayName ='Other Cm number' where perfIndex = 47;
/* -- version 2.6.8.0,build 2016-3-27,module cmc */

-- version 2.9.0.8, build 2017-6-7, module cmc
update EntityType set displayName ='CC8800F' where typeId = 30022;
update EntityType set displayName ='CC8800F(Centralized)' where typeId = 30023;
/* -- version 2.9.0.8, build 2017-6-7, module cmc */
