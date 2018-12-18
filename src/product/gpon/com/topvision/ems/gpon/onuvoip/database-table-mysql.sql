-- version 2.9.0.4,build 2017-6-17,module gpon
create table topsippstnuser(
   onuId bigint(20) not null,
   entityId bigint(20) not null,
   onuIndex bigint(20) not null,
   
   topSIPPstnUserSlotIdx int not null,
   topSIPPstnUserPortIndx int not null,
   topSIPPstnUserOnuIdx int not null,
   topSIPPstnUserPotsIdx int not null,
   
   topSIPPstnUserTelno varchar(64),
   topSIPPstnUserName varchar(32),
   topSIPPstnUserPwd varchar(32),
   
   topSIPPstnUserForwardType int,
   topSIPPstnUserTransferNum varchar(64),
   topSIPPstnUserForwardTime int,
   topSIPPstnUserDigitmapId int,
   topSIPPstnUserSipsrvId int,
   PRIMARY KEY (onuId,topSIPPstnUserPotsIdx),                                                                                                                                                                            
   CONSTRAINT `FK_gpon_topsippstnuser` FOREIGN KEY (`onuId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);

create table topvoiplinestatus(
   onuId bigint(20) not null,
   entityId bigint(20) not null,
   onuIndex bigint(20) not null,
   topVoIPLineSlotIdx int not null,
   topVoIPLinePortIndx int not null,
   topVoIPLineOnuIdx int not null,
   topVoIPLinePotsIdx int not null,
   topVoIPLineCodec int,
   topVoIPLineServStatus int,
   topVoIPLineSessType int,
   topVoIPLineState int,
   PRIMARY KEY (onuId,topVoIPLinePotsIdx),                                                                                                                                                                            
   CONSTRAINT `FK_gpon_topvoiplinestatus` FOREIGN KEY (`onuId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);

create table toponuifpotsinfo(
   onuId bigint(20) not null,
   entityId bigint(20) not null,
   onuIndex bigint(20) not null,
   topOnuIfPotsSlotIdx int not null,
   topOnuIfPotsPortIndx int not null,
   topOnuIfPotsOnuIdx int not null,
   topOnuIfPotsPotsIdx int not null,
   topOnuIfPotsAdminState int,
   PRIMARY KEY (onuId,topOnuIfPotsPotsIdx),                                                                                                                                                                            
   CONSTRAINT `FK_gpon_toponuifpotsinfo` FOREIGN KEY (`onuId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);
/* -- version 2.9.0.4,build 2017-6-17,module gpon  */