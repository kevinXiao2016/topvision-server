
-- version 2.7.5.0,build 2016-10-17,module gpon

create table gpon_onucapability(
   onuId bigint(20) not null,
   onuIndex bigint(20) not null,
   entityId bigint(20) not null,
   onuOMCCVersion int,
   onuTotalEthNum int,
   onuTotalWlanNum int,
   onuTotalCatvNum int,
   onuTotalVeipNum int,
   onuIpHostNum int,
   onuTrafficMgmtOption int,
   onuTotalGEMPortNum int,
   onuTotalTContNum int,
   onuConnectCapbility varchar(10),
   onuQosFlexibility varchar(10),
   PRIMARY KEY (onuId),                                                                                                                                                                            
   CONSTRAINT `FK_gpon_onuCapability` FOREIGN KEY (`onuId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);


create table gpon_onuiphost(
   onuId bigint(20) not null,
   onuIndex bigint(20) not null,
   entityId bigint(20) not null,
   onuIpHostIndex int,
   onuIPHostAddressConfigMode int,
   onuIpHostAddress varchar(20),
   onuIpHostSubnetMask varchar(20),
   onuIpHostGateway varchar(20),
   onuIpHostPrimaryDNS varchar(20),
   onuIpHostSecondaryDNS varchar(20),
   onuIpHostVlanTagPriority int,
   onuIpHostVlanPVid int,
   onuIpHostMacAddress varchar(20),
   PRIMARY KEY (onuId, onuIpHostIndex),                                                                                                                                                                            
   CONSTRAINT `FK_gpon_onuiphost` FOREIGN KEY (`onuId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);


create table gpon_onuinfosoftware(
   onuId bigint(20) not null,
   onuIndex bigint(20) not null,
   entityId bigint(20) not null,
   onuSoftware0Version varchar(255),
   onuSoftware0Valid int,
   onuSoftware0Active int,
   onuSoftware0Commited int,
   onuSoftware1Version varchar(255),
   onuSoftware1Valid int,
   onuSoftware1Active int,
   onuSoftware1Commited int,
   PRIMARY KEY (onuId),                                                                                                                                                                            
   CONSTRAINT `FK_gpon_onuinfosoftware` FOREIGN KEY (`onuId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE 
);
/*-- version 2.7.5.0,build 2016-10-17,module gpon*/

-- version 2.9.0.4,build 2017-06-10,module gpon 
alter table gpon_onucapability add column onuTotalPotsNum int(2) after onuIpHostNum;
alter table gpon_onucapability add column onuCapabilityStr varchar(50) default null;
/*-- version 2.9.0.4,build 2017-06-10,module gpon */
