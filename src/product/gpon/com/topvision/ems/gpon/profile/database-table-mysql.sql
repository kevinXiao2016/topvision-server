-- version 2.9.0.0,build 2016-12-16,module gpon
CREATE TABLE  GponDbaProfileInfo(
	entityId  bigint(20),
	ProfileId  int(10),
	ProfileName  varchar(255),
	ProfileType  int(10),
	ProfileAssureRate  int(10),
	ProfileFixRate  int(10),
	ProfileMaxRate  int(10),
	ProfileBindNum  int(10),
	PRIMARY KEY  (entityId,ProfileId),
	CONSTRAINT FK_GponDbaProfileInfo FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponTrafficProfileInfo(
	entityId  bigint(20),
	ProfileId  int(10),
	ProfileName  varchar(255),
	ProfileCfgCir  int(10),
	ProfileCfgPir  int(10),
	ProfileCfgCbs  int(10),
	ProfileCfgPbs  int(10),
	ProfileCfgPriority  int(10),
	ProfileBindNum  int(10),
	PRIMARY KEY (entityId,ProfileId),
	CONSTRAINT FK_GponTrafficProfileInfo FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponLineProfileInfo(
	entityId  bigint(20),
	ProfileId  int(10),
	ProfileName  varchar(255),
	ProfileUpstreamFECMode  int(10),
	ProfileMappingMode  int(10),
	ProfileTcontNum  int(10),
	ProfileGemNum  int(10),
	ProfileBindNum  int(10),
	PRIMARY KEY  (entityId,ProfileId),
	CONSTRAINT FK_GponLineProfileInfo FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponLineProfileTcont(
	entityId  bigint(20),
	ProfileTcontProfileIndex  int(10),
	ProfileTcontIndex  int(10),
	ProfileTcontDbaProfileId  int(10),
	PRIMARY KEY  (entityId,ProfileTcontProfileIndex,ProfileTcontIndex),
	CONSTRAINT FK_GponLineProfileTcont FOREIGN KEY (entityId,ProfileTcontProfileIndex) 
		REFERENCES GponLineProfileInfo (entityId,ProfileId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponLineProfileGem(
	entityId  bigint(20),
	ProfileGemProfileIndex  int(10),
	ProfileGemIndex  int(10),
	ProfileGemEncrypt  int(10),
	ProfileGemTcontId  int(10),
	ProfileGemQueuePri  int(10),
	ProfileGemUpCar  int(10),
	ProfileGemDownCar  int(10),
	ProfileGemMapNum  int(10),
	PRIMARY KEY  (entityId,ProfileGemProfileIndex,ProfileGemIndex),
	CONSTRAINT FK_GponLineProfileGem FOREIGN KEY (entityId,ProfileGemProfileIndex) 
		REFERENCES GponLineProfileInfo (entityId,ProfileId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponLineProfileGemMap(
	entityId  bigint(20),
	ProfileGemMapProfileIndex  int(10),
	ProfileGemMapGemIndex  int(10),
	ProfileGemMapIndex  int(10),
	ProfileGemMapVlan  int(10),
	ProfileGemMapPriority  int(10),
	ProfileGemMapPortType int(10),
	ProfileGemMapPortId  int(10),
	PRIMARY KEY  (entityId,ProfileGemMapProfileIndex,ProfileGemMapGemIndex,ProfileGemMapIndex),
	CONSTRAINT FK_GponLineProfileGemMap FOREIGN KEY (entityId,ProfileGemMapProfileIndex,ProfileGemMapGemIndex) 
		REFERENCES GponLineProfileGem (entityId,ProfileGemProfileIndex,ProfileGemIndex) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponSrvProfileInfo(
	entityId  bigint(20),
	ProfileId  int(10),
	ProfileName  varchar(255),
	ProfileBindNum  int(10),
	PRIMARY KEY  (entityId,ProfileId),
	CONSTRAINT FK_GponSrvProfileInfo FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponSrvProfileCfg(
	entityId  bigint(20),
	ProfileIndex  int(10),
	ProfileMacLearning  int(10),
	ProfileMacAgeSeconds  int(10),
	ProfileLoopbackDetectCheck  int(10),
	ProfileMcMode  int(10),
	ProfileMcFastLeave  int(10),
	ProfileUpIgmpFwdMode  int(10),
	ProfileUpIgmpTCI  int(10),
	PRIMARY KEY  (entityId,ProfileIndex),
	CONSTRAINT FK_GponSrvProfileCfg FOREIGN KEY (entityId,ProfileIndex) 
		REFERENCES GponSrvProfileInfo (entityId,ProfileId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponSrvProfilePortNumProfile(
	entityId  bigint(20),
	ProfilePortNumProfileIndex  int(10),
	ProfileEthNum  int(10),
	ProfileCatvNum  int(10),
	ProfileWlanNum  int(10),
	ProfileVeipNum  int(10),
	PRIMARY KEY  (entityId,ProfilePortNumProfileIndex),
	CONSTRAINT FK_GponSrvProfilePortNumProfile FOREIGN KEY (entityId,ProfilePortNumProfileIndex) 
		REFERENCES GponSrvProfileInfo (entityId,ProfileId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponSrvProfileEthPortConfig(
	entityId  bigint(20),
	ProfileEthPortProfileIndex  int(10),
	ProfileEthPortIdIndex  int(10),
	ProfileEthPortMacLimited  int(10),
	ProfileEthPortMtu  int(10),
	ProfileEthPortFlowCtrl  int(10),
	ProfileEthPortInTrafficProfileId  int(10),
	ProfileEthPortOutTrafficProfileId  int(10),
	ProfileEthPortMcMaxGroup  int(10),
	ProfileEthPortDnMcMode  int(10),
	ProfileEthPortDnMcTCI  int(10),
	ProfileEthPortMcMvlanList  varchar(255),
	PRIMARY KEY  (entityId,ProfileEthPortProfileIndex,ProfileEthPortIdIndex),
	CONSTRAINT FK_GponSrvProfileEthPortConfig FOREIGN KEY (entityId,ProfileEthPortProfileIndex) 
		REFERENCES GponSrvProfileInfo (entityId,ProfileId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponSrvProfilePortVlanCfg(
	entityId  bigint(20),
	ProfilePortVlanProfileIndex  int(10),
	ProfilePortVlanPortTypeIndex  int(10),
	ProfilePortVlanPortIdIndex  int(10),
	ProfilePortVlanPvid  int(10),
	ProfilePortVlanPvidPri  int(10),
	ProfilePortVlanMode  int(10),
	PRIMARY KEY  (entityId,ProfilePortVlanProfileIndex,ProfilePortVlanPortTypeIndex,ProfilePortVlanPortIdIndex),
	CONSTRAINT FK_GponSrvProfilePortVlanCfg FOREIGN KEY (entityId,ProfilePortVlanProfileIndex) 
		REFERENCES GponSrvProfileInfo (entityId,ProfileId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponSrvProfilePortVlanTranslation(
	entityId  bigint(20),
	ProfilePortVlanTransProfileIndex  int(10),
	ProfilePortVlanTransPortTypeIndex  int(10),
	ProfilePortVlanTransPortIdIndex  int(10),
	ProfilePortVlanTransVlanIndex  int(10),
	ProfilePortVlanTransNewVlan  int(10),
	PRIMARY KEY  (entityId,ProfilePortVlanTransProfileIndex,ProfilePortVlanTransPortTypeIndex,ProfilePortVlanTransPortIdIndex,ProfilePortVlanTransVlanIndex),
	CONSTRAINT FK_GponSrvProfilePortVlanTranslation FOREIGN KEY (entityId,ProfilePortVlanTransProfileIndex,ProfilePortVlanTransPortTypeIndex,ProfilePortVlanTransPortIdIndex) 
		REFERENCES GponSrvProfilePortVlanCfg (entityId,ProfilePortVlanProfileIndex,ProfilePortVlanPortTypeIndex,ProfilePortVlanPortIdIndex) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponSrvProfilePortVlanAggregation(
	entityId  bigint(20),
	ProfilePortVlanAggrProfileIndex  int(10),
	ProfilePortVlanAggrPortTypeIndex  int(10),
	ProfilePortVlanAggrPortIdIndex  int(10),
	ProfilePortVlanAggrVlanIndex  int(10),
	ProfilePortVlanAggrVlanList  varchar(255),
	PRIMARY KEY  (entityId,ProfilePortVlanAggrProfileIndex,ProfilePortVlanAggrPortTypeIndex,ProfilePortVlanAggrPortIdIndex,ProfilePortVlanAggrVlanIndex),
	CONSTRAINT FK_GponSrvProfilePortVlanAggregation FOREIGN KEY (entityId,ProfilePortVlanAggrProfileIndex,ProfilePortVlanAggrPortTypeIndex,ProfilePortVlanAggrPortIdIndex) 
		REFERENCES GponSrvProfilePortVlanCfg (entityId,ProfilePortVlanProfileIndex,ProfilePortVlanPortTypeIndex,ProfilePortVlanPortIdIndex) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  GponSrvProfilePortVlanTrunk(
	entityId  bigint(20),
	ProfilePortVlanTrunkProfileIndex  int(10),
	ProfilePortVlanTrunkPortTypeIndex  int(10),
	ProfilePortVlanTrunkPortIdIndex  int(10),
	ProfilePortVlanTrunkVlanList  varchar(255),
	PRIMARY KEY  (entityId,ProfilePortVlanTrunkProfileIndex,ProfilePortVlanTrunkPortTypeIndex,ProfilePortVlanTrunkPortIdIndex),
	CONSTRAINT FK_GponSrvProfilePortVlanTrunk FOREIGN KEY (entityId,ProfilePortVlanTrunkProfileIndex,ProfilePortVlanTrunkPortTypeIndex,ProfilePortVlanTrunkPortIdIndex) 
		REFERENCES GponSrvProfilePortVlanCfg (entityId,ProfilePortVlanProfileIndex,ProfilePortVlanPortTypeIndex,ProfilePortVlanPortIdIndex) ON DELETE CASCADE ON UPDATE CASCADE
);

/**-- version 2.9.0.0,build 2016-12-16,module gpon*/

-- version 2.9.0.4,build 2017-6-16 16:50,module gpon
CREATE TABLE  topgponsrvprofile(
    entityId  bigint(20),
    topGponSrvProfileIndex  int(10),
    topGponSrvProfilePotsNum  int(10),
    PRIMARY KEY  (entityId,topGponSrvProfileIndex),
    CONSTRAINT FK_topgponsrvprofile FOREIGN KEY (entityId,topGponSrvProfileIndex) 
        REFERENCES GponSrvProfileInfo (entityId,ProfileId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  topGponSrvPotsInfo(
	entityId  bigint(20),
	topGponSrvPotsInfoProfIdx  int(10),
	topGponSrvPotsInfoPotsIdx  int(10),
	
	topGponSrvPotsInfoSIPAgtId  int(10),
	topGponSrvPotsInfoVoipMediaId  int(10),
	topGponSrvPotsInfoIpIdx  int(10),
	PRIMARY KEY  (entityId,topGponSrvPotsInfoProfIdx,topGponSrvPotsInfoPotsIdx),
	CONSTRAINT FK_topGponSrvPotsInfo FOREIGN KEY (entityId,topGponSrvPotsInfoProfIdx) 
		REFERENCES GponSrvProfileInfo (entityId,ProfileId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  topsipagentprofinfo(
    entityId  bigint(20),
    topSIPAgtProfIdx  int(10),
    topSIPAgtProfName  varchar(32),
    
    topSIPAgtProxyAddr  varchar(64),
    topSIPAgtProxyPort  int(10),
    topSIPAgtSecProxyAddr  varchar(64),
    topSIPAgtSecProxyPort  int(10),
    
    topSIPAgtOutboundAddr  varchar(64),
    topSIPAgtOutboundPort  int(10),
    topSIPAgtSecOutboundAddr  varchar(64),
    topSIPAgtSecOutboundPort  int(10),
    
    topSIPAgtRegAddr  varchar(64),
    topSIPAgtRegPort  int(10),
    topSIPAgtSecRegAddr  varchar(64),
    topSIPAgtSecRegPort  int(10),
    
    topSIPAgtReqExpTime int(10),
    topSIPAgtBindCnt int(10),
    
    PRIMARY KEY (entityId,topSIPAgtProfIdx),
    CONSTRAINT FK_topsipagentprofinfo FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  topVoipMediaProfInfo(
    entityId  bigint(20),
    topVoipMediaProfIdx  int(10),
    topVoipMediaProfName  varchar(32),
    
    topVoipMediaFaxmode  int(10),
    topVoipMediaNegotiate  int(10),
    topVoipMediaBindCnt  int(10),
    PRIMARY KEY (entityId,topVoipMediaProfIdx),
    CONSTRAINT FK_topVoipMediaProfInfo FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  topSIPSrvProfInfo(
    entityId  bigint(20),
    topSIPSrvProfIdx  int(10),
    topSIPSrvProfName  varchar(32),
    
    topSIPSrvProfCallWait  int(10),
    topSIPSrvProf3Way  int(10),
    topSIPSrvProfCallTransfer  int(10),
    topSIPSrvProfCallHold  int(10),
    topSIPSrvProfDND  int(10),
    topSIPSrvProfHotline  int(10),
    topSIPSrvProfHotlineNum  varchar(32),
    topSIPSrvProfHotDelay  int(10),
    topSIPSrvProfBindCnt  int(10),
    PRIMARY KEY (entityId,topSIPSrvProfIdx),
    CONSTRAINT FK_topSIPSrvProfInfo FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE  topDigitMapProfInfo(
    entityId  bigint(20),
    topDigitMapProfIdx  int(10),
    topDigitMapProfName  varchar(32),
    
    topDigitMapCirtDialTime  int(10),
    topDigitMapPartDialTime  int(10),
    topDigitMapDialPlanToken  varchar(1024),
    topDigitMapBindCnt  int(10),
    PRIMARY KEY (entityId,topDigitMapProfIdx),
    CONSTRAINT FK_topDigitMapProfInfo FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/**-- version 2.9.0.4,build 2017-6-16 16:50,module gpon*/