/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2012-2-13 9:43:17                            */
/*==============================================================*/

-- version 1.0.0,build 2012-02-13,module cmc
/*==============================================================*/
/* Table: CmattributeExtCpeControl                              */
/*==============================================================*/
create table CmattributeExtCpeControl
(
   cmId                 bigint(20) not null,
   docsIfCmtsCmStatusIndex bigint(20),
   docsSubMgtCpeControlMaxCpeIp int(4),
   docsSubMgtCpeControlActive tinyint(2),
   docsSubMgtCpeControlLearnable tinyint(2),
   docsSubMgtCpeControlReset tinyint(2),
   primary key (cmId)
);

/*==============================================================*/
/* Table: CmcAttribute                                          */
/*==============================================================*/
create table CmcAttribute
(
   cmcId                bigint(20) not null,
   cmcDeviceStyle       int(1),
   topCcmtsSysDescr     varchar(255),
   topCcmtsSysObjectId  varchar(255),
   topCcmtsSysUpTime    bigint(20),
   topCcmtsSysContact   varchar(255),
   topCcmtsSysName      varchar(255),
   topCcmtsSysLocation  varchar(255),
   topCcmtsSysService   int(4),
   topCcmtsSysORLastChange bigint(20),
   topCcmtsDocsisBaseCapability bigint(20),
   topCcmtsSysRAMRatio  int(4),
   topCcmtsSysCPURatio  int(4),
   topCcmtsSysMacAddrLong bigint(20),
   topCcmtsSysFlashRatio int(4),
   topCcmtsCmNumTotal   int(4),
   topCcmtsCmNumOutline int(4),
   topCcmtsCmNumOnline  int(4),
   topCcmtsCmNumReg     int(4),
   topCcmtsCmNumRanged  int(4),
   topCcmtsCmNumRanging int(4),
   topCcmtsSysMacAddr   varchar(20),
   topCcmtsSysStatus    int(2),
   topCcmtsSysSwVersion varchar(255),
   primary key (cmcId)
);

/*==============================================================*/
/* Table: CmcCmRelation                                         */
/*==============================================================*/
create table CmcCmRelation
(
   cmId                 bigint(20) auto_increment not null,
   cmcId                bigint(20),
   upPortId             bigint(20),
   downPortId           bigint(20),
   mac                  varchar(20),
   entityId             bigint(20),
   docsIfCmtsCmStatusIndex bigint(20),
   primary key (cmId)
);

/*==============================================================*/
/* Table: CmcDownChannelBaseInfo                                */
/*==============================================================*/
create table CmcDownChannelBaseInfo
(
   cmcPortId            bigint(20) not null,
   cmcId                bigint(20),
   channelIndex         bigint(20),
   docsIfDownChannelId  Int(3),
   docsIfDownChannelFrequency Int(10),
   docsIfDownChannelWidth Int(8),
   docsIfDownChannelModulation Tinyint(2),
   docsIfDownChannelInterleave Tinyint(2),
   docsIfDownChannelPower Int(10),
   docsIfDownChannelAnnex Tinyint(2),
   docsIfDownChannelStorageType Tinyint(2),
   primary key (cmcPortId)
);

/*==============================================================*/
/* Table: CmcDownChannelStaticInfo                              */
/*==============================================================*/
create table CmcDownChannelStaticInfo
(
   cmcPortId            bigint(20) not null,
   cmcId                bigint(20),
   channelIndex         bigint(20),
   CtrId Int(3),
   CtrTotalBytes bigint(20),
   CtrUsedBytes bigint(20),
   CtrExtTotalBytes bigint(20),
   CtrExtUsedBytes bigint(20),
   primary key (cmcPortId)
);

/*==============================================================*/
/* Table: CmcEntityRelation                                     */
/*==============================================================*/
create table CmcEntityRelation
(
   cmcId                bigint(20) not null,
   virtualNetId         bigint(20),
   onuId                bigint(20),
   cmcIndex             bigint(20),
   cmcType              int,
   cmcEntityId          bigint(20),
   primary key (cmcId)
);

/*==============================================================*/
/* Table: CmcModulationProfileInfo                              */
/*==============================================================*/
create table CmcModulationProfileInfo
(
   cmcModId             bigint(20) auto_increment not null,
   entityId             bigint(20),
   ModIndex   bigint(20),
   ModIntervalUsageCode Tinyint(2),
   ModControl Tinyint(2),
   ModType    Tinyint(2),
   ModPreambleLen int(5),
   ModDifferentialEncoding Tinyint(2),
   ModFECErrorCorrection Tinyint(2),
   ModFECCodewordLength int(4),
   ModScramblerSeed int(6),
   ModMaxBurstSize int(4),
   ModGuardTimeSize bigint(20),
   ModLastCodewordShortened Tinyint(2),
   ModScrambler Tinyint(2),
   ModByteInterleaverDepth bigint(20),
   ModByteInterleaverBlockSize bigint(20),
   ModPreambleType Tinyint(2),
   ModTcmErrorCorrectionOn Tinyint(2),
   ModScdmaInterleaverStepSize tinyint(2),
   ModScdmaSpreaderEnable Tinyint(2),
   ModScdmaSubframeCodes int(4),
   ModChannelType Tinyint(2),
   ModStorageType Tinyint(2),
   primary key (cmcModId)
);

/*==============================================================*/
/* Table: CmcPortAttribute                                      */
/*==============================================================*/
create table CmcPortAttribute
(
   cmcPortId            bigint(20) not null,
   cmcId                bigint(20),
   ifIndex              bigint(20),
   ifDescr              varchar(255),
   ifType               varchar(20),
   ifMtu                varchar(20),
   ifSpeed              varchar(20),
   ifPhysAddress        varchar(17),
   ifAdminStatus        varchar(20),
   ifOperStatus         varchar(20),
   ifLastChange         int(11),
   ifName               varchar(32),
   primary key (cmcPortId)
);

/*==============================================================*/
/* Table: CmcPortPerf                                           */
/*==============================================================*/
create table CmcPortPerf
(
   cmcPortId            bigint(20) not null,
   cmcId                bigint(20),
   monitorId            bigint(20),
   ifIndex              bigint(20),
   ifInOctets           bigint(20),
   ifInUcastPkts        bigint(20),
   ifInNUcastPkts       bigint(20),
   ifInDiscards         bigint(20),
   ifInErrors           bigint(20),
   ifInUnknownProtos    bigint(20),
   ifOutOctets          bigint(20),
   ifOutUcastPkts       bigint(20),
   ifOutNUcastPkts      bigint(20),
   ifOutDiscards        bigint(20),
   ifOutErrors          bigint(20),
   ifOutQLen            bigint(20),
   primary key (cmcPortId)
);

/*==============================================================*/
/* Table: CmcPortRelation                                       */
/*==============================================================*/
create table CmcPortRelation
(
   cmcPortId            bigint(20)  auto_increment not null,
   cmcId                bigint(20),
   channellIndex        bigint(20),
   channelType          tinyint(2),
   primary key (cmcPortId)
);

/*==============================================================*/
/* Table: CmcServiceFlowRelation                                */
/*==============================================================*/
create table CmcServiceFlowRelation
(
   sId                  bigint(20) auto_increment not null,
   cmcId                bigint(20),
   serviceFlowId        bigint(20),
   primary key (sId)
);

/*==============================================================*/
/* Table: CmcUpChannelBaseInfo                                  */
/*==============================================================*/
create table CmcUpChannelBaseInfo
(
   cmcPortId            bigint(20) not null,
   cmcId                bigint(20),
   channelIndex         bigint(20),
   ChannelId    int(3),
   ChannelFrequency int(10),
   ChannelWidth Int(8),
   ChannelModulationProfile bigint(20),
   ChannelSlotSize Int(10),
   ChannelTxTimingOffset Int(10),
   ChannelRangingBackoffStart tinyint (2),
   ChannelRangingBackoffEnd tinyint (2),
   ChannelTxBackoffStart tinyint (2),
   ChannelTxBackoffEnd tinyint (2),
   ChannelScdmaActiveCodes Int(3),
   ChannelScdmaCodesPerSlot Int(2),
   ChannelScdmaFrameSize Int(2),
   ChannelScdmaHoppingSeed Int(5),
   ChannelType  tinyint (2),
   ChannelCloneFrom Int(10),
   ChannelUpdate tinyint (2),
   ChannelStatus tinyint (2),
   ChannelPreEqEnable tinyint (2),
   primary key (cmcPortId)
);

/*==============================================================*/
/* Table: CmcUpChannelSignalQualityInfo                         */
/*==============================================================*/
create table CmcUpChannelSignalQualityInfo
(
   cmcPortId            bigint(20) not null,
   cmcId                bigint(20),
   channelIndex         bigint(20),
   docsIfSigQIncludesContention tinyint(2),
   docsIfSigQUnerroreds Int(10),
   docsIfSigQCorrecteds Int(10),
   docsIfSigQUncorrectables Int(10),
   docsIfSigQSignalNoise Int(10),
   docsIfSigQMicroreflections Int(3),
   docsIfSigQEqualizationData varchar(20),
   docsIfSigQExtUnerroreds bigint(20),
   docsIfSigQExtCorrecteds bigint(20),
   docsIfSigQExtUncorrectables bigint(20),
   primary key (cmcPortId)
);

/*==============================================================*/
/* Table: CmcUpChannelStaticInfo                                */
/*==============================================================*/
create table CmcUpChannelStaticInfo
(
   cmcPortId            bigint(20) not null,
   cmcId                bigint(20),
   channelIndex         bigint(20),
   ctrId Int(3),
   CtrTotalMslots int(10),
   CtrUcastGrantedMslots int(10),
   CtrTotalCntnMslots int(10),
   CtrUsedCntnMslots int(10),
   CtrExtTotalMslots Bigint(20),
   CtrExtUcastGrantedMslots Bigint(20),
   CtrExtTotalCntnMslots Bigint(20),
   CtrExtUsedCntnMslots Bigint(20),
   CtrCollCntnMslots int(10),
   CtrTotalCntnReqMslots int(10),
   CtrUsedCntnReqMslots int(10),
   CtrCollCntnReqMslots int(10),
   CtrTotalCntnReqDataMslots int(10),
   CtrUsedCntnReqDataMslots int(10),
   CtrCollCntnReqDataMslots int(10),
   CtrTotalCntnInitMaintMslots int(10),
   CtrUsedCntnInitMaintMslots int(10),
   CtrCollCntnInitMaintMslots int(10),
   CtrExtCollCntnMslots Bigint(20),
   CtrExtTotalCntnReqMslots Bigint(20),
   CtrExtUsedCntnReqMslots Bigint(20),
   CtrExtCollCntnReqMslots Bigint(20),
   CtrExtTotalCntnReqDataMslots Bigint(20),
   CtrExtUsedCntnReqDataMslots Bigint(20),
   CtrExtCollCntnReqDataMslots Bigint(20),
   CtrExtTotalCntnInitMaintMslots Bigint(20),
   CtrExtUsedCntnInitMaintMslots Bigint(20),
   CtrExtCollCntnInitMaintMslots Bigint(20),
   primary key (cmcPortId)
);

/*==============================================================*/
/* Table: CpeAttribute                                          */
/*==============================================================*/
create table CpeAttribute
(
   cpeId                bigint(20)  not null,
   docsIfCmtsCmStatusIndex bigint(20),
   docsSubMgtCpeIpIndex smallint(4),
   docsSubMgtCpeIpAddr  Varchar(32),
   docsSubMgtCpeIpLearned tinyint(2),
   docsSubMgtCpeType    tinyint(2),
   primary key (cpeId)
);

/*==============================================================*/
/* Table: CpeCmRelation                                         */
/*==============================================================*/
create table CpeCmRelation
(
   cpeId                bigint(20) auto_increment not null,
   cmId                 bigint(20),
   docsSubMgtCpeIpIndex smallint(4),
   primary key (cpeId)
);

/*==============================================================*/
/* Table: DynamicServiceFlowStats                               */
/*==============================================================*/
create table DynamicServiceFlowStats
(
   cmcIndex             bigint(20),
   docsQosIfDirection   tinyint(2) not null,
   cmcId                bigint(20)  not null,
   docsQosDSAReqs       int(11),
   docsQosDSARsps       bigint(20),
   docsQosDSAAcks       bigint(20),
   docsQosDSCReqs       bigint(20),
   docsQosDSCRsps       bigint(20),
   docsQosDSCAcks       bigint(20),
   docsQosDSDReqs       bigint(20),
   docsQosDSDRsps       bigint(20),
   docsQosDynamicAdds   bigint(20),
   docsQosDynamicAddFails bigint(20),
   docsQosDynamicChanges bigint(20),
   docsQosDynamicChangeFails bigint(20),
   docsQosDynamicDeletes bigint(20),
   docsQosDynamicDeleteFails bigint(20),
   docsQosDCCReqs       bigint(20),
   docsQosDCCRsps       bigint(20),
   docsQosDCCAcks       bigint(20),
   docsQosDCCs          bigint(20),
   docsQosDCCFails      bigint(20),
   docsQosDCCRspDeparts bigint(20),
   docsQosDCCRspArrives bigint(20),
   currentServiceFlowNum int(4),
   collectTime          timestamp,
   primary key (docsQosIfDirection, cmcId)
);

/*==============================================================*/
/* Table: MacDomainBaseInfo                                     */
/*==============================================================*/
create table MacDomainBaseInfo
(
   cmcId                bigint(20)  not null,
   cmcIndex             bigint(20),
   docsIfCmtsCapabilities varchar(4),
   docsIfCmtsSyncInterval int(3),
   docsIfCmtsUcdInterval int(4),
   docsIfCmtsMaxServiceIds int(5),
   docsIfCmtsInsertionInterval int(10),
   InvitedRangingAttempts int(4),
   docsIfCmtsInsertInterval int(10),
   docsIfCmtsMacStorageType tinyint(2),
   primary key (cmcId)
);

/*==============================================================*/
/* Table: MacDomainStatusInfo                                   */
/*==============================================================*/
create table MacDomainStatusInfo
(
   cmcId                bigint(20)  not null,
   cmcIndex             bigint(20),
   InvalidRangeReqs int(10),
   RangingAborteds int(10),
   InvalidRegReqs int(10),
   FailedRegReqs int(10),
   InvalidDataReqs int(10),
   T5Timeouts int(10),
   primary key (cmcId)
);

/*==============================================================*/
/* Table: QosPktClass                                           */
/*==============================================================*/
create table QosPktClass
(
   servicePacketId      bigint(20)  not null,
   cmcId                bigint(20),
   sId                  bigint(20),
   cmcIndex             bigint(20),
   ServiceFlowId bigint(20),
   ClassId    int(6),
   ClassDirection tinyint(2),
   ClassPriority smallint(4),
   ClassIpTosLow varchar(20),
   ClassIpTosHigh varchar(20),
   ClassIpTosMask varchar(20),
   ClassIpProtocol smallint(4),
   ClassIpSourceAddr varchar(20),
   ClassIpSourceMask varchar(20),
   ClassIpDestAddr varchar(20),
   ClassIpDestMask varchar(20),
   ClassSourcePortStart int(6),
   ClassSourcePortEnd int(6),
   ClassDestPortStart int(6),
   ClassDestPortEnd int(6),
   ClassDestMacAddr varchar(20),
   ClassDestMacMask varchar(20),
   ClassSourceMacAddr varchar(20),
   ClassEnetProtocolType tinyint(2),
   ClassEnetProtocol int(6),
   ClassUserPriApplies tinyint(2),
   ClassUserPriLow tinyint(2),
   ClassUserPriHigh tinyint(2),
   ClassVlanId smallint(5),
   ClassState tinyint(2),
   ClassPkts  int(10),
   ClassBitMap varchar(12),
   ClassInetSourceAddrType tinyint(3),
   ClassInetSourceAddr varchar(64),
   ClassInetSourceMaskType tinyint(3),
   ClassInetSourceMask varchar(64),
   ClassInetDestAddrType tinyint(3),
   ClassInetDestAddr varchar(64),
   ClassInetDestMaskType tinyint(3),
   ClassInetDestMask varchar(64),
   primary key (servicePacketId)
);

/*==============================================================*/
/* Table: ServiceFlowAttribute                                  */
/*==============================================================*/
create table ServiceFlowAttribute
(
   sId                  bigint(20)  not null,
   cmcIndex             bigint(20),
   cmcId                bigint(20),
   docsQosServiceFlowId bigint(20),
   docsQosServiceFlowSID int(10),
   docsQosServiceFlowDirection tinyint(2),
   docsQosServiceFlowPrimary tinyint(2),
   primary key (sId)
);

/*==============================================================*/
/* Table: ServiceFlowCmRelation                                 */
/*==============================================================*/
create table ServiceFlowCmRelation
(
   sId                  bigint(20)  not null,
   cmId                 bigint(20),
   serviceFlowId        bigint(20),
   cmcIndex             bigint(20),
   mac                  varchar(20),
   primary key (sId)
);

/*==============================================================*/
/* Table: ServiceFlowParamSet                                   */
/*==============================================================*/
create table ServiceFlowParamSet
(
   serviceParamId       bigint(20)  not null,
   cmcId                bigint(20),
   sId                  bigint(20),
   cmcIndex             bigint(20),
   ServiceFlowId bigint(20),
   Type  tinyint(2),
   ServiceClassName varchar(64),
   Priority tinyint(2),
   MaxTrafficRate int(11),
   MaxTrafficBurst int(11),
   MinReservedRate int(11),
   MinReservedPkt int(6),
   ActiveTimeout int(6),
   AdmittedTimeout int(6),
   MaxConcatBurst int(6),
   SchedulingType varchar(64),
   NomPollInterval int(11),
   TolPollJitter int(11),
   UnsolicitGrantSize int(6),
   NomGrantInterval int(11),
   TolGrantJitter int(11),
   GrantsPerInterval int(3),
   TosAndMask varchar(20),
   TosOrMask varchar(20),
   MaxLatency int(12),
   RequestPolicyOct varchar(20),
   BitMap varchar(12),
   primary key (serviceParamId)
);

/*==============================================================*/
/* Table: ServiceFlowParamSetRelation                           */
/*==============================================================*/
create table ServiceFlowParamSetRelation
(
   serviceParamId       bigint(20) auto_increment not null,
   sId                  bigint(20),
   cmcId                bigint(20),
   serviceFlowId        bigint(20),
   docsQosParamSetType  int(2),
   primary key (serviceParamId)
);

/*==============================================================*/
/* Table: ServiceFlowPkgClassRelation                           */
/*==============================================================*/
create table ServiceFlowPkgClassRelation
(
   servicePacketId      bigint(20) auto_increment not null,
   sId                  bigint(20),
   cmcId                bigint(20),
   serviceFlowId        bigint(20),
   docsQosPktClassId    int(6),
   primary key (servicePacketId)
);

/*==============================================================*/
/* Table: ServiceFlowStatus                                     */
/*==============================================================*/
create table ServiceFlowStatus
(
   sId                  bigint(20) not null,
   cmcIndex             bigint(20),
   cmcId                bigint(20),
   FlowId bigint(20),
   FlowPkts bigint(20),
   FlowOctets bigint(20),
   FlowTimeCreated bigint(20),
   FlowTimeActive bigint(20),
   FlowPHSUnknowns bigint(20),
   FlowPolicedDropPkts bigint(20),
   FlowPolicedDelayPkts bigint(20),
   primary key (sId)
);

/*==============================================================*/
/* Table: cmAttribute                                           */
/*==============================================================*/
create table cmAttribute
(
   cmId                 bigint(20)  not null,
   cmcId                bigint(20),
   StatusIndex bigint(20),
   StatusMacAddress varchar(20),
   StatusIpAddress varchar(20),
   StatusDownChannelIfIndex bigint(40),
   StatusUpChannelIfIndex bigint(40),
   StatusRxPower bigint(11),
   StatusTimingOffset int(11) unsigned,
   StatusEqualizationData varchar(64),
   StatusValue tinyint(2),
   StatusUnerroreds int(10) unsigned,
   StatusCorrecteds int(10) unsigned,
   StatusUncorrectables int(10) unsigned,
   StatusExtUnerroreds bigint(20) unsigned,
   StatusExtCorrecteds bigint(20) unsigned,
   StatusExtUncorrectables bigint(20) unsigned,
   StatusSignalNoise int(10),
   StatusMicroreflections int(10),
   StatusDocsisRegMode int(10),
   StatusModulationType int(10),
   StatusInetAddressType int(10),
   StatusInetAddress varchar(20),
   StatusValueLastUpdate int(10),
   StatusHighResolutionTO int(10) unsigned,
   primary key (cmId)
);

/*==============================================================*/
/* Table: cmcDhcpModeConfig                                     */
/*==============================================================*/
create table cmcDhcpModeConfig
(
   cmcId                bigint(20) not null,
   dhcpConfigMode       tinyint,
   primary key (cmcId)
);

/*==============================================================*/
/* Table: cmcEmsConfig                                          */
/*==============================================================*/
create table cmcEmsConfig
(
   cmcId                bigint(20) not null,
   docsDevNmAccessIndex bigint(20) not null,
   docsDevNmAccessIp    varchar(20),
   docsDevNmAccessIpMask varchar(20),
   docsDevNmAccessReadCommunity varchar(255),
   docsDevNmAccessControl tinyint,
   docsDevNmAccessWriteCommunity varchar(255),
   primary key (cmcId, docsDevNmAccessIndex)
);

/*==============================================================*/
/* Table: cmcExtAttributeB                                      */
/*==============================================================*/
create table cmcExtAttributeB
(
   cmcId                bigint(20) not null,
   extMangPhyMainInt    tinyint,
   extMangPhyBackupInt  tinyint,
   extMangPhySelModel   tinyint,
   primary key (cmcId)
);

/*==============================================================*/
/* Table: cmcIpManage                                           */
/*==============================================================*/
create table cmcIpManage
(
   cmcId                bigint(20) not null,
   topCcmtsEthIpAddr    varchar(30),
   topCcmtsEthIpMask    varchar(30),
   topCcmtsEthGateway   varchar(30),
   topCcmtsEthVlanId    bigint(20),
   topCcmtsEthVlanIpAddr varchar(30),
   topCcmtsEthVlanIpMask varchar(30),
   topCcmtsEthVlanGateway varchar(30),
   primary key (cmcId)
);

/*==============================================================*/
/* Table: dolsnmpparam                                          */
/*==============================================================*/
create table dolsnmpparam
(
   entityId             bigint(20) not null,
   ip                   varchar(20),
   version              int,
   timeout              char(10),
   retry                char(10),
   port                 char(10),
   mibs                 varchar(128),
   community            varchar(128),
   writecommunity       varchar(128),
   username             varchar(32),
   securityLevel        int,
   authProtocol         varchar(32),
   privProtocol         varchar(32),
   authPassword         varchar(128),
   privPassword         varchar(128),
   authoritativeEngineID varchar(128),
   contextName          varchar(128),
   contextId            varchar(128),
   primary key (entityId)
);

/*==============================================================*/
/* Table: serviceclass                                          */
/*==============================================================*/
create table serviceclass
(
   scId                 bigint(20) auto_increment not null,
   entityId             bigint(20),
   ClassName varchar(30),
   ClassStatus tinyint(2),
   ClassPriority tinyint(2),
   ClassMaxTrafficRate int(11),
   ClassMaxTrafficBurst int(11),
   ClassMinReservedRate int(11),
   ClassMinReservedPkt int(11),
   ClassMaxConcatBurst int(11),
   ClassNomPollInterval int(11),
   ClassTolPollJitter int(11),
   ClassUnsolicitGrantSize int(11),
   ClassNomGrantInterval int(11),
   ClassTolGrantJitter int(11),
   ClassGrantsPerInterval tinyint(2),
   ClassMaxLatency int(11),
   ClassActiveTimeout int(11),
   ClassAdmittedTimeout int(11),
   ClassSchedulingType tinyint(2),
   ClassRequestPolicy varchar(12),
   ClassTosAndMask varchar(2),
   ClassTosOrMask varchar(2),
   ClassDirection tinyint(2),
   primary key (scId)
);

alter table cmcentityrelation add constraint FK_entity_cmc_relation foreign key (cmcEntityId)
      references entity(entityId) on delete cascade on update cascade;

alter table CmattributeExtCpeControl add constraint FK_cpeControlcmRelation foreign key (cmId)
      references CmcCmRelation (cmId) on delete cascade on update cascade;

alter table CmcAttribute add constraint FK_8800_attr foreign key (cmcId)
      references CmcEntityRelation (cmcId) on delete cascade on update cascade;

alter table CmcCmRelation add constraint FK_cmc_cm foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;

alter table CmcDownChannelBaseInfo add constraint FK_downchannel_info foreign key (cmcPortId)
      references CmcPortRelation (cmcPortId) on delete cascade on update cascade;

alter table CmcDownChannelStaticInfo add constraint FK_downchannel_static foreign key (cmcPortId)
      references CmcPortRelation (cmcPortId) on delete cascade on update cascade;

alter table CmcPortAttribute add constraint FK_port_attr foreign key (cmcPortId)
      references CmcPortRelation (cmcPortId) on delete cascade on update cascade;

alter table CmcPortPerf add constraint FK_port_perf foreign key (cmcPortId)
      references CmcPortRelation (cmcPortId) on delete cascade on update cascade;

alter table CmcPortRelation add constraint FK_cmc_port foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;

alter table CmcServiceFlowRelation add constraint FK_cmc_serviceflow foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;

alter table CmcUpChannelBaseInfo add constraint FK_upchannel_info_mod foreign key (cmcPortId)
      references CmcPortRelation (cmcPortId) on delete cascade on update cascade;

alter table CmcUpChannelSignalQualityInfo add constraint FK_upchannel_signal foreign key (cmcPortId)
      references CmcPortRelation (cmcPortId) on delete cascade on update cascade;

alter table CmcUpChannelStaticInfo add constraint FK_upchannel_static foreign key (cmcPortId)
      references CmcPortRelation (cmcPortId) on delete cascade on update cascade;

alter table CpeAttribute add constraint FK_cpeTocpeCmRelation foreign key (cpeId)
      references CpeCmRelation (cpeId) on delete cascade on update cascade;

alter table CpeCmRelation add constraint FK_cpeCmTocmCmcRealtion foreign key (cmId)
      references CmcCmRelation (cmId) on delete cascade on update cascade;

alter table DynamicServiceFlowStats add constraint FK_cmc_dynamic foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;

alter table MacDomainBaseInfo add constraint FK_cmc_mac foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;

alter table MacDomainStatusInfo add constraint FK_cmc_macstatus foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;

alter table QosPktClass add constraint FK_pkgclass foreign key (servicePacketId)
      references ServiceFlowPkgClassRelation (servicePacketId) on delete cascade on update cascade;

alter table ServiceFlowAttribute add constraint FK_serviceflow_attr foreign key (sId)
      references CmcServiceFlowRelation (sId) on delete cascade on update cascade;

alter table ServiceFlowCmRelation add constraint FK_ServiceFlowCmRelation foreign key (sId)
      references CmcServiceFlowRelation (sId) on delete cascade on update cascade;

alter table ServiceFlowParamSet add constraint FK_param foreign key (serviceParamId)
      references ServiceFlowParamSetRelation (serviceParamId) on delete cascade on update cascade;

alter table ServiceFlowParamSetRelation add constraint FK_serviceFlow_param foreign key (sId)
      references CmcServiceFlowRelation (sId) on delete cascade on update cascade;

alter table ServiceFlowPkgClassRelation add constraint FK_serviceflow_pkg foreign key (sId)
      references CmcServiceFlowRelation (sId) on delete cascade on update cascade;

alter table ServiceFlowStatus add constraint FK_serviceflow_status foreign key (sId)
      references CmcServiceFlowRelation (sId) on delete cascade on update cascade;

alter table cmAttribute add constraint FK_cm_attr foreign key (cmId)
      references CmcCmRelation (cmId) on delete cascade on update cascade;

alter table cmcDhcpModeConfig add constraint FK_dhcpConfig_cmc foreign key (cmcId)
      references CmcEntityRelation (cmcId) on delete cascade on update cascade;

alter table cmcEmsConfig add constraint FK_emsConfig_cmc foreign key (cmcId)
      references CmcEntityRelation (cmcId) on delete cascade on update cascade;

alter table cmcExtAttributeB add constraint FK_ext_attribute_8800b foreign key (cmcId)
      references CmcEntityRelation (cmcId) on delete cascade on update cascade;

alter table cmcIpManage add constraint FK_mgmt_cmc foreign key (cmcId)
      references CmcEntityRelation (cmcId) on delete cascade on update cascade;
/* -- version 1.0.0,build 2012-02-13,module cmc */
-- version 1.6.6,build 2012-05-6,module cmc
create table PerfNoiseLast
(
   entityId             bigint(20),
   cmcId                 bigint(20),
   ifindex              bigint(20),
   noise int(3),
   dt timestamp
);
create table PerfNoiseHis
(
   entityId             bigint(20),
   cmcId                bigint(20),
   ifindex              bigint(20),
   noise int(3),
   dt timestamp
);
/* -- version 1.6.6,build 2012-05-6,module cmc */
-- version 1.6.6,build 2012-05-9,module cmc
create table PerfChannelUtilizationLast
(
   entityId             bigint(20),
   cmcId                 bigint(20),
   channelIndex              bigint(20),
   channelUtilization int(3),
   dt timestamp
);
create table PerfChannelUtilizationHis
(
   entityId             bigint(20),
   cmcId                bigint(20),
   channelIndex              bigint(20),
   channelUtilization int(3),
   dt timestamp
);
/* -- version 1.6.6,build 2012-05-9,module cmc */
-- version 1.6.6,build 2012-05-23,module cmc
/*==============================================================*/
/* Table: devevcontrol                                          */
/*==============================================================*/
create table devevcontrol
(
   entityId             bigint(20) not null,
   docsDevEvPriority    bigint(20) not null,
   docsDevEvReporting   varchar(11),
   primary key (entityId, docsDevEvPriority)
);
/* -- version 1.6.6,build 2012-05-23,module cmc */
-- version 1.6.6,build 2012-05-30,module cmc
alter table cmcdownchannelbaseinfo add column cmcChannelTotalCmNum int(11);
alter table cmcdownchannelbaseinfo add column cmcChannelOnlineCmNum int(11);
alter table cmcupchannelbaseinfo add column cmcChannelTotalCmNum int(11);
alter table cmcupchannelbaseinfo add column cmcChannelOnlineCmNum int(11);
/* -- version 1.6.6,build 2012-05-30,module cmc */
-- version 1.6.6,build 2012-07-14,module cmc
create table PerfChannelCmNumLast
(
   entityId             bigint(20),
   cmcId                bigint(20),
   channelIndex              bigint(20),
   channelType int(1),
   cmNumTotal int(11),
   cmNumOnline int(11),
   cmNumActive int(11),
   cmNumUnregistered int(11),
   cmNumOffline int(11),
   CmNumRregistered int(11),
   dt timestamp
);

create table PerfChannelCmNumHis
(
   entityId             bigint(20),
   cmcId                bigint(20),
   channelIndex              bigint(20),
   channelType int(1),
   cmNumTotal int(11),
   cmNumOnline int(11),
   cmNumActive int(11),
   cmNumUnregistered int(11),
   cmNumOffline int(11),
   CmNumRregistered int(11),
   dt timestamp
);

create table PerfUsBitErrorRateLast
(
   entityId             bigint(20),
   cmcId                bigint(20),
   channelIndex              bigint(20),
   bitErrorRate int(4),
   unBitErrorRate int(4),
   dt timestamp
);

create table PerfUsBitErrorRateHis
(
   entityId             bigint(20),
   cmcId                bigint(20),
   channelIndex              bigint(20),
   bitErrorRate int(4),
   unBitErrorRate int(4),
   dt timestamp
);

create table PerfChannelSpeedStaticLast
(
   entityId             bigint(20),
   cmcId                bigint(20),
   channelIndex              bigint(20),
   channelType int(1),
   channelInOctets              bigint(20),
   channelOutOctets              bigint(20),
   channelInOctetsRate decimal(16,4),
   channelOutOctetsRate decimal(16,4),
   channelOctetsRate decimal(16,4),
   dt timestamp
);

create table PerfChannelSpeedStaticHis
(
   entityId             bigint(20),
   cmcId                bigint(20),
   channelIndex              bigint(20),
   channelType int(1),
   channelInOctets              bigint(20),
   channelOutOctets              bigint(20),
   channelInOctetsRate decimal(16,4),
   channelOutOctetsRate decimal(16,4),
   channelOctetsRate decimal(16,4),
   dt timestamp
);

/* -- version 1.6.6,build 2012-7-14,module cmc */
-- version 1.7.4,build 2012-07-16,module cmc
alter table cmcattribute add column dt timestamp;
CREATE TABLE perfsystemhis
(
    cmcId               bigint(20),
    ifIndex             int(11),
    topCcmtsSysUpTime   bigint(20),
    cpuRatio            int(4),
    memRatio            int(4),
    dt                  timestamp
);
CREATE TABLE CmcEventTypeRelation
(
    deviceEventTypeId               bigint(20),
    emsEventTypeId          int(11)
);
/* -- version 1.7.4,build 2012-07-16,module cmc */
-- version 1.7.7,build 2012-08-28,module cmc
alter table cmcupchannelsignalqualityinfo add column docsIf3SignalPower bigint(20) default 0;
CREATE TABLE `cmcVlanPriIpmanage`
(
   `vlanAutoId` bigint(20) NOT NULL AUTO_INCREMENT,
   `cmcId` bigint(20) NOT NULL,
   `topCcmtsVifPriIpVlanId` int(11)  NOT NULL,
   `topCcmtsVifPriIpAddr` varchar(30) DEFAULT NULL,
   `topCcmtsVifPriIpMask` varchar(30) DEFAULT NULL,
   `topCcmtsVifPriIpStatus` int(11) DEFAULT NULL,
   PRIMARY KEY (`vlanAutoId`),
   CONSTRAINT `FK_vlanPriIpmanage_cmc` FOREIGN KEY (`cmcId`) REFERENCES `cmcentityrelation` (`cmcId`) ON DELETE CASCADE ON UPDATE CASCADE
 );

CREATE TABLE `CmcVlanPrimaryInterface`
(
   `cmcId` bigint(20) NOT NULL,
   `vlanPrimaryInterface` int(11)  NOT NULL,
   `vlanPrimaryDefaultRoute` varchar(30) DEFAULT NULL,
   PRIMARY KEY (`cmcId`),
   CONSTRAINT `FK_vlanint_cmc` FOREIGN KEY (`cmcId`) REFERENCES `cmcentityrelation` (`cmcId`) ON DELETE CASCADE ON UPDATE CASCADE
 );
/* -- version 1.7.7,build 2012-08-28,module cmc */

-- version 1.7.7,build 2012-08-30,module cmc
CREATE TABLE cmc8800BSniConfig
(
    cmcId                   bigint(20) NOT NULL,
    topCcmtsSniEthInt       tinyint,
    topCcmtsSniMainInt      tinyint,
    topCcmtsSniBackupInt    tinyint,
    PRIMARY KEY(cmcId)
);
alter table cmc8800BSniConfig add constraint FK_cmc8800BSniConfig foreign key (cmcId)
      references CmcEntityRelation (cmcId) on delete cascade on update cascade;
/* -- version 1.7.7,build 2012-08-30,module cmc */

-- version 1.7.7,build 2012-10-10,module cmc
alter table cmcAttribute add column cmcAuthCurrentInfo varchar(255);
/*-- version 1.7.7,build 2012-10-10,module cmc */
-- version 1.7.7,build 2012-10-29,module cmc
alter table cmccmrelation add column maclong bigint(20) after mac;
create index ix_cmccm_mac on CmcCmRelation
(
   cmcId,
   maclong
);
/* -- version 1.7.7,build 2012-10-29,module cmc */

-- version 1.7.7.5,build 2012-12-7,module cmc
alter table serviceflowparamset  modify column MaxTrafficRate bigint(12);
alter table serviceflowparamset  modify column MaxTrafficBurst bigint(12);
alter table serviceflowparamset  modify column MinReservedRate bigint(12);
alter table serviceflowparamset  modify column NomPollInterval bigint(12);
alter table serviceflowparamset  modify column TolPollJitter bigint(12);
alter table serviceflowparamset  modify column NomGrantInterval bigint(12);
alter table serviceflowparamset  modify column TolGrantJitter bigint(12);
/* -- version 1.7.7.5,build 2012-12-7,module cmc */

-- version 1.7.7.5,build 2012-12-12,module cmc
alter table ServiceFlowParamSetRelation modify column sId bigint(20) not null;
/* -- version 1.7.7.5,build 2012-12-12,module cmc */
-- version 1.7.9.0,build 2012-2-4,module cmc
create table CmImportInfo
(
   CmMacAddr            varchar(17) not null,
   CmAlias              varchar(80),
   CmClassified         varchar(10),
   importTime           bigint,
   primary key (CmMacAddr)
);
/* -- version 1.7.9.0,build 2012-2-4,module cmc */
-- version 1.7.12.0,build 2013-3-26,module cmc
alter table cmcattribute add NmName varchar(50) default null;

create table topLoadBalCfg
(
   cmcId                bigint(20) not null,
   topLoadBalConfigCmcIndex bigint(20),
   topLoadBalConfigEnable int(1),
   topLoadBalConfigInterval int(4),
   topLoadBalConfigMaxMoves int(4),
   topLoadBalConfigMethod int(2),
   topLoadBalConfigNumPeriod int(4),
   topLoadBalConfigPeriod int(4),
   topLoadBalConfigTriggerThresh int(4),
   topLoadBalConfigDiffThresh int(4),
   topLoadBalConfigDccInitAtdma int(4),
   topLoadBalConfigDccInitScdma int(4),
   topLoadBalConfigDbcInitAtdma int(4),
   topLoadBalConfigDbcInitScdma int(4),
   primary key (cmcId)
);

alter table topLoadBalCfg add constraint FK_topLoadBalCfg foreign key (cmcId)
      references cmcattribute (cmcId) on delete cascade on update cascade;


create table topLoadBalExcludeCm
(
   excRangId            bigint(20) not null auto_increment,
   cmcId                bigint(20),
   topLoadBalExcludeCmIndex int(4),
   topLoadBalExcludeCmMacRang varchar(100),
   primary key (excRangId)
);

alter table topLoadBalExcludeCm add constraint FK_topLoadBalExcludeCm foreign key (cmcId)
      references cmcattribute (cmcId) on delete cascade on update cascade;

create table docLoadBalGrp
(
   grpId                bigint(20) not null auto_increment,
   cmcId                bigint(20),
   groupName            varchar(100),
   docsLoadBalGrpId     bigint(20),
   primary key (grpId)
);

alter table docLoadBalGrp add constraint FK_docLoadBalGrp foreign key (cmcId)
      references cmcattribute (cmcId) on delete cascade on update cascade;

create table topLoadBalRestrictCm
(
   rangId               bigint(20) not null auto_increment,
   grpId                bigint(20),
   topLoadBalRestrictCmIndex int(4),
   topLoadBalRestrictCmMacRang varchar(100),
   primary key (rangId)
);

alter table topLoadBalRestrictCm add constraint FK_topLoadBalRestrictCm foreign key (grpId)
      references docLoadBalGrp (grpId) on delete cascade on update cascade;

create table docLoadBalChannel
(
   channelId            bigint(20) not null auto_increment,
   grpId                bigint(20),
   docsLoadBalChannelIfIndex int(4),
   primary key (channelId)
);

alter table docLoadBalChannel add constraint FK_docLoadBalChannel foreign key (grpId)
      references docLoadBalGrp (grpId) on delete cascade on update cascade;

create table docsLoadBalBasicRule
(
   ruleId               bigint(20) not null auto_increment,
   entityId             bigint(20),
   docsLoadBalBasicRuleId bigint(20),
   docsLoadBalBasicRuleEnable int(4),
   docsLoadBalBasicRuleDisStart int(4),
   docsLoadBalBasicRuleDisPeriod int(4),
   primary key (ruleId)
);

alter table docsLoadBalBasicRule add constraint FK_docsLoadBalBasicRule foreign key (entityId)
      references entity (entityId) on delete cascade on update cascade;

create table docsLoadBalPolicy
(
   policyId             bigint(20) not null auto_increment,
   entityId             bigint(20),
   docsLoadBalPolicyId  bigint(20),
   primary key (policyId)
);

alter table docsLoadBalPolicy add constraint FK_docsLoadBalPolicy foreign key (entityId)
      references entity (entityId) on delete cascade on update cascade;

create table policyRuleRef
(
   ruleId               bigint(20),
   policyId             bigint(20)
);

alter table policyRuleRef add constraint FK_policyRuleRef_policy foreign key (policyId)
      references docsLoadBalPolicy (policyId) on delete cascade on update cascade;

alter table policyRuleRef add constraint FK_policyRuleRef_rule foreign key (ruleId)
      references docsLoadBalBasicRule (ruleId) on delete cascade on update cascade;

create table topCcmtsLoadBalPolicy
(
   cmcId                bigint(20),
   policyId             bigint(20)
);

alter table topCcmtsLoadBalPolicy add constraint FK_topCcmtsLoadBalPolicy_policy foreign key (policyId)
      references docsLoadBalPolicy (policyId) on delete cascade on update cascade;

alter table topCcmtsLoadBalPolicy add constraint FK_topCcmtsLoadBalPolicy_cmc foreign key (cmcId)
      references cmcattribute (cmcId) on delete cascade on update cascade;

create table docsLoadBalBasicRuleTpl
(
   ruleTplId            bigint(20) not null auto_increment,
   docsLoadBalBasicRuleEnable int(4),
   docsLoadBalBasicRuleDisStart int(4),
   docsLoadBalBasicRuleDisPeriod int(4),
   primary key (ruleTplId)
);

create table docsLoadBalPolicyTpl
(
   policyTplId          bigint(20) not null auto_increment,
   policyName           varchar(100),
   primary key (policyTplId)
);

create table policyRuleTplRef
(
   ruleTplId            bigint(20),
   policyTplId          bigint(20)
);

alter table policyRuleTplRef add constraint FK_policyRuleTplRef_policy foreign key (policyTplId)
      references docsLoadBalPolicyTpl (policyTplId) on delete cascade on update cascade;

alter table policyRuleTplRef add constraint FK_policyRuleTplRef_rule foreign key (ruleTplId)
      references docsLoadBalBasicRuleTpl (ruleTplId) on delete cascade on update cascade;
/*-- version 1.7.12.0,build 2013-3-26,module cmc*/

-- version 1.7.12.0,build 2013-4-23,module cmc

/*==============================================================*/
/* Table: CmcSniConfig                                          */
/*==============================================================*/
create table CmcSniConfig
(
   cmcId                bigint(20) not null,
   topCcmtsSniUplinkLoopbackStatus tinyint,
   primary key (cmcId)
);
alter table CmcSniConfig add constraint FK_cmc_sniConfig foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: cmcSniPhyConfig                                       */
/*==============================================================*/
create table cmcSniPhyConfig
(
   cmcId                bigint(20) not null,
   phyIndex             tinyint not null,
   topCcmtsSniInt       tinyint,
   primary key (cmcId, phyIndex)
);
alter table cmcSniPhyConfig add constraint FK_cmc_phyconfig foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: CmcSniCpuPortRateLimit                                */
/*==============================================================*/
create table CmcSniCpuPortRateLimit
(
   cmcId                bigint(20) not null,
   topCcmtsRateLimiteCpuPortEgressArp int,
   topCcmtsRateLimiteCpuPortEgressUni int,
   topCcmtsRateLimiteCpuPortEgressUdp int,
   topCcmtsRateLimiteCpuPortEgressDhcp int,
   topCcmtsRateLimiteUplinkEgressIcmp int,
   topCcmtsRateLimiteUplinkEgressIgmp int,
   primary key (cmcId)
);
alter table CmcSniCpuPortRateLimit add constraint FK_cmc_snicpuportratelimit foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;

CREATE TABLE cmcIpSubVlanScalar
(
    cmcId                   bigint(20) NOT NULL,
    topCcmtsIpSubVlanCfi    tinyint,
    topCcmtsIpSubVlanTpid   int,
    PRIMARY KEY(cmcId),
    CONSTRAINT FK_cmcIpSubVlanScalar FOREIGN KEY (cmcId) REFERENCES CmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE cmcIpSubVlanCfgEntry
(
    cmcId                           bigint(20) NOT NULL,
    topCcmtsIpSubVlanIfIndex        bigint,
    topCcmtsIpSubVlanIpIndex        varchar(20),
    topCcmtsIpSubVlanIpMaskIndex    varchar(20),
    topCcmtsIpSubVlanVlanId         int,
    topCcmtsIpSubVlanPri            tinyint,
    PRIMARY KEY(cmcId,topCcmtsIpSubVlanIpIndex,topCcmtsIpSubVlanIpMaskIndex),
    CONSTRAINT FK_cmcIpSubVlanCfgEntry FOREIGN KEY (cmcId) REFERENCES CmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE CmcVlanConfigEntry
(
    cmcId                           bigint(20) NOT NULL,
    topCcmtsVlanIndex               int,
    topCcmtsVlanName                varchar(20),
    topCcmtsTaggedPort              varchar(20),
    topCcmtsUntaggedPort            varchar(20),
    topCcmtsVlanFloodMode           tinyint,
    topCcmtsVifPriIpAddr            varchar(20),
    topCcmtsVifPriIpMask            varchar(20),
    topCcmtsVlanDhcpAlloc           tinyint,
    topCcmtsOption60                varchar(20),
    topCcmtsVlanDhcpAllocIpAddr     varchar(20),
    topCcmtsVlanDhcpAllocIpMask     varchar(20),
    PRIMARY KEY(cmcId,topCcmtsVlanIndex),
    CONSTRAINT FK_CmcVlanConfigEntry FOREIGN KEY (cmcId) REFERENCES CmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE CmcVifSubIpEntry
(
    cmcId                           bigint(20) NOT NULL,
    topCcmtsVifSubIpVlanIdx         int,
    topCcmtsVifSubIpSeqIdx          tinyint,
    topCcmtsVifSubIpAddr            varchar(20),
    topCcmtsVifSubIpMask            varchar(20),
    PRIMARY KEY(cmcId,topCcmtsVifSubIpVlanIdx,topCcmtsVifSubIpSeqIdx),
    CONSTRAINT FK_CmcVifSubIpEntry FOREIGN KEY (cmcId, topCcmtsVifSubIpVlanIdx) REFERENCES CmcVlanConfigEntry (cmcId, topCcmtsVlanIndex) on update cascade on delete cascade
);

/*-- version 1.7.12.0,build 2013-4-23,module cmc*/
-- version 1.7.12.0,build 2013-4-22,module cmc
CREATE TABLE cmcdhcpbundle
(
    entityId               bigint(20) not null,
    topCcmtsDhcpBundleInterface varchar(10),
    topCcmtsDhcpBundlePolicy    tinyint,
    cableSourceVerify tinyint,
    virtualPrimaryIpAddr varchar(32),
    virtualPrimaryIpMask varchar(32),
    primary key (entityId, topCcmtsDhcpBundleInterface),
    CONSTRAINT FK_dhcpbundle_entityId FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE cmcdhcpOption60
(
    entityId               bigint(20) not null,
    option60Id          bigint(20) auto_increment not null,
    topCcmtsDhcpBundleInterface varchar(10) not null,
    topCcmtsDhcpOption60DeviceType    tinyint not null,
    topCcmtsDhcpOption60Index         tinyint not null,
    topCcmtsDhcpOption60Str           varchar(100),
    primary key (option60Id),
    index(entityId,topCcmtsDhcpBundleInterface),
    CONSTRAINT FK_dhcpOption60_bundle FOREIGN KEY (entityId, topCcmtsDhcpBundleInterface) REFERENCES cmcdhcpbundle (entityId,topCcmtsDhcpBundleInterface) ON DELETE CASCADE ON UPDATE CASCADE
);
create table cmcDhcpGiaddrConfig
(
   entityId                bigint(20) not null,
   giaddrId             bigint(20) auto_increment not null,
   topCcmtsDhcpBundleInterface varchar(10) not null,
   topCcmtsDhcpGiAddrDeviceType tinyint not null,
   topCcmtsDhcpGiAddress varchar(15),
   topCcmtsDhcpGiAddrMask varchar(15),
   primary key (giaddrId),
   index(entityId,topCcmtsDhcpBundleInterface),
   CONSTRAINT FK_dhcpGiaddr_bundle FOREIGN KEY (entityId,topCcmtsDhcpBundleInterface) REFERENCES cmcdhcpbundle (entityId,topCcmtsDhcpBundleInterface) ON DELETE CASCADE ON UPDATE CASCADE
);
create table cmcdhcpserverconfig
(
   entityId                bigint(20) not null,
   helperId             bigint(20) auto_increment not null,
   topCcmtsDhcpBundleInterface varchar(10) not null,
   topCcmtsDhcpHelperDeviceType tinyint not null,
   topCcmtsDhcpHelperIndex tinyint not null,
   topCcmtsDhcpHelperIpAddr varchar(15),
   primary key (helperId),
   index(entityId,topCcmtsDhcpBundleInterface),
   CONSTRAINT FK_dhcpServer_bundle FOREIGN KEY (entityId,topCcmtsDhcpBundleInterface) REFERENCES cmcdhcpbundle (entityId,topCcmtsDhcpBundleInterface) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE cmcDhcpBaseConfig
(
   entityId bigint(20) NOT NULL,
   cableSourceVerify int(11)  NOT NULL,
   topCcmtsDhcpAlloc int(11) DEFAULT NULL,
   topCcmtsDhcpMode varchar(4),
   topCcmtsDhcpAllocOption60 varchar(100) DEFAULT NULL,
   PRIMARY KEY (entityId),
   CONSTRAINT FK_dhcpBase_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
 );
create table cmcDhcpIntIpTable
(
   entityId                bigint(20) not null,
   topCcmtsDhcpBundleInterface  varchar(10) not null,
   topCcmtsDhcpIntIpIndex int(20) not null,
   topCcmtsDhcpIntIpAddr varchar(30),
   topCcmtsDhcpIntIpMask varchar(30),
   primary key (entityId, topCcmtsDhcpBundleInterface, topCcmtsDhcpIntIpIndex),
   CONSTRAINT FK_cmcDhcpBundle_cmcDhcpIntIp FOREIGN KEY (entityId,topCcmtsDhcpBundleInterface) REFERENCES cmcdhcpbundle (entityId,topCcmtsDhcpBundleInterface) ON DELETE CASCADE ON UPDATE CASCADE
);
 create table cmcDhcpPacketVlanTable
(
   packetVlanId             bigint(20) auto_increment not null,
   entityId                bigint(20) not null,
   topCcmtsDhcpBundleInterface varchar(10) not null,
   topCcmtsDhcpDeviceType  tinyint not null,
   topCcmtsDhcpTagVlan int(20) not null,
   topCcmtsDhcpTagPriority varchar(30),
   primary key(packetVlanId),
   CONSTRAINT FK_dhcpVlan_bundle FOREIGN KEY (entityId,topCcmtsDhcpBundleInterface) REFERENCES cmcdhcpbundle (entityId,topCcmtsDhcpBundleInterface) ON DELETE CASCADE ON UPDATE CASCADE
);
/*==============================================================*/
/* Table: cmFlap                                                */
/*==============================================================*/
create table cmFlap
(
   cmId                 bigint(20)  not null,
   cmcId                bigint(20),
   topCmFlapMacAddr   varchar(20),
   topCmFlapInsertionFailNum int,
   primary key (cmId)
);
alter table cmFlap add constraint FK_cm_flap foreign key (cmId)
      references cmAttribute (cmId) on delete CASCADE on update CASCADE;

/*-- version 1.7.12.0,build 2013-4-22,module cmc*/
-- version 1.7.12.0,build 2013-4-24,module cmc
alter table CmcUpChannelBaseInfo add channelMode tinyint (2);
/*-- version 1.7.12.0,build 2013-4-24,module cmc*/

-- version 1.7.12.0,build 2013-4-25,module cmc
create table cmcSyslogServerEntry(
   entityId                     bigint(20) not null,
   topCcmtsSyslogServerIndex    int(1),
   topCcmtsSyslogServerIpAddr   varchar(30),
   topCcmtsSyslogServerIpPort   int(5),
   primary key (entityId, topCcmtsSyslogServerIndex),
   CONSTRAINT FK_cmcSyslogServer_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

alter table cmcAttribute add column topCcmtsSysSerialNumber varchar(255);
/*==============================================================*/
/* Table: cmcSnmpConfig                                */
/*==============================================================*/
create table cmcSnmpConfig(
   entityId bigint(20),
   readCommunity varchar(255),
   writeCommunity varchar(255),
   PRIMARY KEY (entityId),
   CONSTRAINT FK_cmc_snmpConfig FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*==============================================================*/
/* Table: cmcSystemIpInfo                                */
/*==============================================================*/

create table cmcSystemIpInfo(
   entityId bigint(20),
   topCcmtsEthIpAddr    varchar(255),
   topCcmtsEthIpMask    varchar(255),
   topCcmtsEthGateway   varchar(255),
   topCcmtsEthVlanId    bigint(255),
   topCcmtsEthVlanIpAddr varchar(255),
   topCcmtsEthVlanIpMask varchar(255),
   topCcmtsEthVlanGateway varchar(255),
   PRIMARY KEY (entityId),
   CONSTRAINT FK_cmc_systemIpInfo FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE cmctrapserver (
  entityId bigint(20) NOT NULL,
  topCcmtsTrapServerIndex int(11) DEFAULT NULL,
  topCcmtsTrapServerIpAddr varchar(255) NOT NULL,
  topCcmtsTrapServerStatus int(11) DEFAULT NULL,
  topCcmtsTrapServerIpPort int(11) DEFAULT NULL,
  topCcmtsTrapCommunityName varchar(255) DEFAULT NULL,
  PRIMARY KEY (entityId,topCcmtsTrapServerIndex),
  CONSTRAINT FK_ENTITY_TRAPSERVER FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*==============================================================*/
/* Table: cmcpe                                                 */
/*==============================================================*/
create table cmcpe
(
   cmId                 bigint(20),
   entityId             bigint(20),
   topCmCpeMacAddress   varchar(20),
   topCmCpeType         int,
   topCmCpeIpAddress    varchar(20),
   topCmCpeCcmtsIfIndex bigint(20),
   topCmCpeCmStatusIndex bigint(20),
   topCmCpeToCmMacAddr  varchar(20)
);
alter table cmcpe add constraint FK_cm_cpe foreign key (cmId)
      references cmAttribute (cmId) on delete CASCADE on update CASCADE;

/*==============================================================*/
/* Table: cmstaticip                                            */
/*==============================================================*/
create table cmstaticip
(
   cmId                 bigint(20),
   entityId             bigint(20),
   topCcmtsCmStaticIP   varchar(32),
   docsIfCmtsCmStatusIndex bigint(20)
);
alter table cmstaticip add constraint FK_cm_staticip foreign key (cmId)
      references cmAttribute (cmId) on delete CASCADE on update CASCADE;
/*-- version 1.7.12.0,build 2013-4-25,module cmc*/

-- version 1.7.12.0,build 2013-4-26,module cmc

/*==============================================================*/
/* Table: cmcSyslogRecordEventLevel                                */
/*==============================================================*/
CREATE TABLE cmcsyslogrecordeventlevel(
    entityId bigint(20) NOT NULL,
    topCcmtsSyslogRecordType varchar(32) NOT NULL,
    topCcmtsSyslogMinEvtLvl int NOT NULL,
    PRIMARY KEY (entityId,topCcmtsSyslogRecordType),
    CONSTRAINT FK_ENTITY_SYSLOGEVTLVL FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE cmcsyslogConfig(
    entityId bigint(20) NOT NULL,
    topCcmtsSyslogSwitch tinyint,
    topCcmtsSyslogMaxnum int,
    topCcmtsSyslogTrotInvl int,
    topCcmtsSyslogTrotTrhd int,
    topCcmtsSyslogTrotMode int,
    PRIMARY KEY (entityId),
    CONSTRAINT FK_ENTITY_SYSLOGCONFIG FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*==============================================================*/
/* Table: docsIf3CmtsCmUsStatus                                 */
/*==============================================================*/
create table docsIf3CmtsCmUsStatus
(
   cmId                 bigint(20),
   cmRegStatusId        bigint(20),
   cmUsStatusChIfIndex  bigint(20),
   cmUsStatusModulationType int,
   cmUsStatusRxPower    int,
   cmUsStatusSignalNoise int,
   cmUsStatusMicroreflections int,
   cmUsStatusEqData     varchar(32),
   cmUsStatusUnerroreds int,
   cmUsStatusCorrecteds int,
   cmUsStatusUncorrectables int,
   cmUsStatusHighResolutionTimingOffset int,
   cmUsStatusIsMuted    int,
   cmUsStatusRangingStatus int
);
alter table docsIf3CmtsCmUsStatus add constraint FK_cm_usStatus foreign key (cmId)
      references cmAttribute (cmId) on delete CASCADE on update CASCADE;

/*-- version 1.7.12.0,build 2013-4-26,module cmc*/
-- version 1.7.12.0,build 2013-4-27,module cmc
/*==============================================================*/
/* Table: cmcAclDefAct                                          */
/*==============================================================*/
create table cmcAclDefAct(
   entityId bigint(20) NOT NULL,
   uplinkIngress int,
   uplinkEgress int,
   cableEgress int,
   cabelIngress int,
   PRIMARY KEY (entityId),
   CONSTRAINT FK_ENTITY_CMCACLDEFACT FOREIGN KEY (entityId)
   REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE);
/*==============================================================*/
/* Table: cmcAclConfig                                          */
/*==============================================================*/
create table cmcAclConfig(
   entityId bigint(20) NOT NULL,
   topCcmtsAclListIndex int not null,
   topCcmtsAclDesc  varchar(100),
   topCcmtsAclPrio int,
   topMatchlSrcMac char(17),
   topMatchSrcMacMask char(17),
   topMatchDstMac char(17),
   topMatchDstMacMask char(17),
   topMatchVlanId int,
   topMatchVlanCos int,
   topMatchEtherType int,
   topMatchDscp int,
   topMatchIpProtocol int,
   topMatchSrcIp varchar(15),
   topMatchSrcIpMask varchar(15),
   topMatchDstIp varchar(15),
   topMatchDstIpMask varchar(15),
   topMatchSrcPort int,
   topMatchDstPort int,
   topActionMask char(11),
   topActionAddVlanId int,
   topActionAddVlanCos int,
   topActionNewVlanCos int,
   installPosition char(4),
   PRIMARY KEY (entityId,topCcmtsAclListIndex),
   CONSTRAINT FK_ENTITY_CMCACLCONFIG FOREIGN KEY (entityId)
   REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE);
/*-- version 1.7.12.0,build 2013-4-27,module cmc*/
-- version 1.7.12.0,build 2013-5-3,module cmc
CREATE TABLE cmcdocsisconfig (
  entityId bigint(20) NOT NULL,
  ifIndex int(11) DEFAULT NULL,
  ccmtsMddInterval int(11) DEFAULT NULL,
  ccmtsMdfEnabled tinyint(4) DEFAULT NULL,
  PRIMARY KEY (entityId),
  CONSTRAINT FK_ENTITY_DOCSISCONFIG FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*-- version 1.7.12.0,build 2013-5-3,module cmc*/

-- version 1.7.13.0,build 2013-5-15,module cmc
alter table cmcupchannelsignalqualityinfo change docsIfSigQUnerroreds docsIfSigQUnerroreds bigint(20);
alter table cmcupchannelsignalqualityinfo change docsIfSigQCorrecteds docsIfSigQCorrecteds bigint(20);
alter table cmcupchannelsignalqualityinfo change docsIfSigQUncorrectables docsIfSigQUncorrectables bigint(20);
/*-- version 1.7.13.0,build 2013-5-15,module cmc*/

-- version 1.7.14.0,build 2013-5-24,module cmc
alter table cmcsnicpuportratelimit add uplinkIngressBroadcast int(11);
alter table cmcsnicpuportratelimit add uplinkIngressMulticast int(11);
/*-- version 1.7.14.0,build 2013-5-24,module cmc*/

-- version 1.7.14.0,build 2013-6-16,module cmc
create table cmcSpectrumLast
(
   cmcId                 bigint(20),
   frequencyIndex        bigint(20),
   databuffer       varchar(4300),
   dt timestamp
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
create table cmcFftMonitorCfg
(   
    cmcId                 bigint(20),   
    fftMonitorCmcMacIndex varchar(20),   
    fftMonitorTimeInterval int(10),   
    fftMonitorFreqInterval int(10),   
    fftMonitorFreqStart int(10),   
    fftMonitorFreqEnd int(10),   
    fftMonitorPollingStatus int(1)
);
/*-- version 1.7.14.0,build 2013-6-16,module cmc*/

-- version 1.7.16.0,build 2013-6-20,module cmc
/*==============================================================*/
/* Table: CmAction                                              */
/*==============================================================*/
create table CmAction
(
   entityId             bigint,
   cmIndex              bigint,
   cmmac                bigint,
   cmip                 bigint,
   action               int,
   time                 timestamp,
   realtime             timestamp
);


/*==============================================================*/
/* Table: CpeAction                                             */
/*==============================================================*/
create table CpeAction
(
   entityId             bigint,
   cmIndex              bigint,
   cmmac                bigint,
   cpemac               bigint,
   cpeip                bigint,
   action               int,
   time                 timestamp,
   realtime             timestamp
);


/*==============================================================*/
/* Table: allcmmac                                              */
/*==============================================================*/
create table allcmmac
(
   mac                  bigint not null,
   realtime             timestamp,
   primary key (mac)
)
ENGINE = MyISAM;


/*==============================================================*/
/* Table: initialDataCmAction                                   */
/*==============================================================*/
create table initialDataCmAction
(
 entityId             bigint,
 cmIndex              bigint,
 upChannelIfIndex     bigint,
 downChannelIfIndex   bigint,
 cmmac                bigint,
 cmip                 bigint,
 state                int,
 realtime             timestamp
);


/*==============================================================*/
/* Table: initialDataCpeAction                                  */
/*==============================================================*/
create table initialDataCpeAction
(
 entityId             bigint,
 cmmac                bigint,
 cpemac               bigint,
 cpeip                bigint,
 cpetype              int,
 realtime             timestamp
);


/*==============================================================*/
/* Table: userNumHisAll                                         */
/*==============================================================*/
create table userNumHisAll
(
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp
)
engine = MyISAM;


/*==============================================================*/
/* Table: userNumHisArea                                        */
/*==============================================================*/
create table userNumHisArea
(
  areaId               bigint,
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp
)
engine = MyISAM;


/*==============================================================*/
/* Table: userNumHisCcmts                                       */
/*==============================================================*/
create table userNumHisCcmts
(
  entityId             bigint,
  ccIfIndex            bigint,
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp
)
engine = MyISAM;


/*==============================================================*/
/* Table: userNumHisDevice                                      */
/*==============================================================*/
create table userNumHisDevice
(
  entityId             bigint,
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp
)
engine = MyISAM;


/*==============================================================*/
/* Table: userNumHisPort                                        */
/*==============================================================*/
create table userNumHisPort
(
  entityId             bigint,
  ccIfIndex            bigint,
  portIfIndex          bigint,
  portType             tinyint,
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp
)
engine = MyISAM;

/*-- version 1.7.16.0,build 2013-6-20,module cmc*/

-- version 1.7.16.0,build 2013-6-28,module cmc
CREATE TABLE perfnoiseSummary (
    entityId  bigint(20) NOT NULL,
    cmcId  bigint(20) NOT NULL,
    ifindex  bigint(20) NOT NULL,
    noise  int(3) NOT NULL,
    noiseMin  int(3) NOT NULL,
    dt  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    summarized int(1) default 0,
    primary key (cmcId,ifindex, dt,summarized)
);
CREATE TABLE perfchannelcmnumSummary (
    entityId  bigint(20) NOT NULL,
    cmcId  bigint(20) NOT NULL,
    channelIndex  bigint(20) NOT NULL,
    channelType  int(1) NOT NULL,
    cmNumTotal  int(11) NOT NULL,
    cmNumTotalMax int(11) NOT NULL,
    cmNumOnline  int(11) NOT NULL,
    cmNumOnlineMax  int(11) NOT NULL,
    cmNumActive  int(11) NOT NULL,
    cmNumUnregistered  int(11) NOT NULL,
    cmNumOffline  int(11) NOT NULL,
    CmNumRregistered  int(11) NOT NULL,
    dt  timestamp DEFAULT CURRENT_TIMESTAMP,
    summarized int(1)  default 0,
    primary key (cmcId,channelIndex, dt,summarized)
);
/*-- version 1.7.16.0,build 2013-6-28,module cmc*/

-- version 1.7.16.0,build 2013-7-1,module cmc
alter table cmcsnicpuportratelimit add cableIngressBroadcast int(11);
alter table cmcsnicpuportratelimit add cableIngressMulticast int(11);
/*-- version 1.7.16.0,build 2013-7-1,module cmc*/

-- version 1.7.17.0,build 2013-7-11,module cmc
/*==============================================================*/
/* Index: IX_CM_ACT_EID                                         */
/*==============================================================*/
create index IX_CM_ACT_EID on CmAction
(
   entityId
);

/*==============================================================*/
/* Index: IX_CM_ACT_EID_MAC                                     */
/*==============================================================*/
create index IX_CM_ACT_EID_MAC on CmAction
(
   entityId,
   cmmac
);

/*==============================================================*/
/* Index: IX_CPE_ACT_EID                                        */
/*==============================================================*/
create index IX_CPE_ACT_EID on CpeAction
(
   entityId
);

/*==============================================================*/
/* Index: IX_CPE_ACT_EID_CMMAC                                  */
/*==============================================================*/
create index IX_CPE_ACT_EID_CMMAC on CpeAction
(
   entityId,
   cmmac
);

/*==============================================================*/
/* Index: IX_CPE_ACT_EID_CPEMAC                                 */
/*==============================================================*/
create index IX_CPE_ACT_EID_CPEMAC on CpeAction
(
   entityId,
   cpemac
);

/*==============================================================*/
/* Index: IX_CM_INIT_RT                                         */
/*==============================================================*/
create index IX_CM_INIT_RT on initialDataCmAction
(
   realtime
);

/*==============================================================*/
/* Index: IX_CPE_INIT_RT                                        */
/*==============================================================*/
create index IX_CPE_INIT_RT on initialDataCpeAction
(
   realtime
);

/*==============================================================*/
/* Index: IX_ALL_NUM_RT                                         */
/*==============================================================*/
create index IX_ALL_NUM_RT on userNumHisAll
(
   realtime
);

/*==============================================================*/
/* Index: IX_AREA_NUM_AID_RT                                    */
/*==============================================================*/
create index IX_AREA_NUM_AID_RT on userNumHisArea
(
   areaId,
   realtime
);

/*==============================================================*/
/* Index: IX_C_NUM_EID_CI_RT                                    */
/*==============================================================*/
create index IX_C_NUM_EID_CI_RT on userNumHisCcmts
(
   entityId,
   ccIfIndex,
   realtime
);

/*==============================================================*/
/* Index: IX_D_NUM_EID_RT                                       */
/*==============================================================*/
create index IX_D_NUM_EID_RT on userNumHisDevice
(
   entityId,
   realtime
);

/*==============================================================*/
/* Index: IX_P_NUM_EID_PI                                       */
/*==============================================================*/
create index IX_P_NUM_EID_PI on userNumHisPort
(
   entityId,
   portIfIndex,
   realtime
);

/*-- version 1.7.17.0,build 2013-7-11,module cmc*/

-- version 1.7.17.0,build 2013-7-11,module cmc
alter table cmcvlanprimaryinterface add vlanPrimaryDefaultIpAddr varchar(16);
alter table cmcvlanprimaryinterface add vlanPrimaryDefaultIpMask varchar(16);
/*-- version 1.7.17.0,build 2013-7-11,module cmc*/

-- version 1.7.17.0,build 2013-7-13,module cmc
alter table cmcpe add updateTime timestamp;
/*-- version 1.7.17.0,build 2013-7-13,module cmc*/

-- version 1.7.17.0,build 2013-7-17,module cmc
alter table cmcsniphyconfig add topCcmtsSniCurrentMedia tinyint(2);
alter table cmcsniphyconfig add topCcmtsSniDuplexMod tinyint(2);
create table cmcsystemtimeconfig
(
   entityId                bigint(20) not null,
   topCcmtsSysTime  int(11) not null,
   topCcmtsSysTimeZone int(11) not null,
   topCcmtsNtpserverAddress varchar(153),
   topCcmtsSysTimeSynInterval int(11),
   collectTime bigint(20),
   primary key (entityId),
   CONSTRAINT FK_entityId_cmcsystemtimeconfig FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 1.7.17.0,build 2013-7-17,module cmc*/

-- version 1.7.17.1,build 2013-7-20,module cmc
ALTER TABLE `cmcportattribute` ADD INDEX `ix_cmcport_ifindex` (`ifIndex`) USING BTREE ;
/*-- version 1.7.17.1,build 2013-7-20,module cmc*/

-- version 1.7.17.1,build 2013-7-22,module cmc
alter table cmcupchannelsignalqualityinfo change docsIfSigQEqualizationData docsIfSigQEqualizationData text;
/*-- version 1.7.17.1,build 2013-7-22,module cmc*/

-- version 1.7.17.1,build 2013-7-22,module cmc
create table cmcSysCfg
(
    cmcId                       bigint(20) not null,
    topCcmtsSysCfgPiggyback     tinyint(2),
    primary key(cmcId),
    CONSTRAINT FK_cmcAttribute_cmcSysCfg FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
create table cmcShareSecretConfig
(
    cmcId                       bigint(20) not null,
    sharedSecretEnabled         tinyint(2),
    sharedSecretEncrypted       tinyint(2),
    sharedSecretAuthStr         varchar(16),
    sharedSecretCipherStr       varchar(32),
    primary key(cmcId),
    CONSTRAINT FK_cmcAttribute_cmcShareSecretConfig FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 1.7.17.1,build 2013-7-22,module cmc*/

-- version 1.7.17.1,build 2013-7-27,module cmc
ALTER TABLE cmcAclConfig ADD topActionNewVlanTpid int;
ALTER TABLE cmcAclConfig ADD topActionNewVlanCfi int;
ALTER TABLE cmcAclConfig ADD topActionNewVlanId int;
ALTER TABLE cmcAclConfig ADD topActionAddVlanTpid int;
ALTER TABLE cmcAclConfig ADD topActionAddVlanCfi int;
ALTER TABLE cmcAclConfig ADD topActionNewIpTos int;
ALTER TABLE cmcAclConfig ADD topActionNewIpDscp int;
/*-- version 1.7.17.1,build 2013-7-27,module cmc*/

-- version 1.7.17.1,build 2013-7-31,module cmc
/* 这里使用drop命令是由于表结构完全换了，合并时请不要删除  */
DROP TABLE IF EXISTS perfchannelcmnumsummary;
CREATE TABLE perfchannelcmnumsummary (
    entityId  bigint(20)  ,
    cmNumTotal  int(11) ,
    cmNumTotalMax  int(11) ,
    cmNumOnline  int(11) ,
    cmNumOnlineMax  int(11) ,
    dt  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    summarized  int(1) NOT NULL DEFAULT 0
);
ALTER TABLE perfchannelcmnumsummary  ADD UNIQUE INDEX IX_ENT_DT (entityId,dt);
/*-- version 1.7.17.1,build 2013-7-31,module cmc*/

-- version 1.7.17.1,build 2013-08-02,module cmc
/*==============================================================*/
/* Table: ccmtsspectrumgpglobal                                 */
/*==============================================================*/
create table ccmtsspectrumgpglobal
(
   entityId             bigint(20) not null comment '设备Id',
   globalAdminStatus    int(11) comment '全局开关',
   snrQueryPeriod       int(11) comment '监控周期',
   hopHisMaxCount       int(11) comment '最大记录数',
   primary key (entityId)
);

alter table ccmtsspectrumgpglobal comment '设备自动跳频功能全局配置表';

alter table ccmtsspectrumgpglobal add constraint FK_Global_entityId foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: ccmtsspectrumgp                                       */
/*==============================================================*/
create table ccmtsspectrumgp
(
   entityId             bigint(20) not null comment '设备id',
   groupId              int(11) not null comment '设备跳频组Id',
   hopPeriod            int(11) comment 'Hop周期',
   snrThres1            int(11) comment 'SNR第一阀值',
   snrThres2            int(11) comment 'SNR第二阀值',
   fecThresCorrect1     int(11) comment '第一可纠错码',
   fecThresCorrect2     int(11) comment '第二可纠错码',
   fecThresUnCorrect1   int(11) comment '第一不可纠错码',
   fecThresUnCorrect2   int(11) comment '第二不可纠错码',
   adminStatus          int(11) comment '设备跳频组开关',
   maxHopLimit          int(11) comment '最大Hop次数限制',
   groupPolicy          int(11) comment '跳频策略',
   groupPriority1st     int(11) comment '第一优先级',
   groupPriority2st     int(11) comment '第二优先级',
   groupPriority3st     int(11) comment '第三优先级',
   primary key (entityId, groupId)
);

alter table ccmtsspectrumgp comment '设备跳频组表';

alter table ccmtsspectrumgp add constraint FK_Group_entityId foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: ccmtsspectrumgpfreq                                   */
/*==============================================================*/
create table ccmtsspectrumgpfreq
(
   entityId             bigint(20) not null comment '设备id',
   groupId              int(11) not null comment '设备跳频组Id',
   freqIndex            int(11) not null comment '成员频点索引',
   freqFrequency        int(11) comment '中心频点',
   freqMaxWidth         int(11) comment '最大带宽',
   freqPower            int(11) comment '接收电平',
   primary key (entityId, groupId, freqIndex)
);

alter table ccmtsspectrumgpfreq comment '设备成员频点表';

alter table ccmtsspectrumgpfreq add constraint FK_Freq_group foreign key (entityId, groupId)
      references ccmtsspectrumgp (entityId, groupId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: ccmtsspectrumgpchnl                                   */
/*==============================================================*/
create table ccmtsspectrumgpchnl
(
   entityId             bigint(20) not null comment '设备Id',
   channelIndex         bigint(20) not null comment '信道索引',
   chnlId               int(11) comment '上行信道id',
   chnlCmcMac           varchar(32) comment '上行信道Mac地址',
   chnlGroupId          int(11) comment '跳频组Id',
   chnlSecondaryProf    int(11) comment '第一备份调制方式',
   chnlTertiaryProf     int(11) comment '第二备份调制方式',
   primary key (entityId, channelIndex)
);

alter table ccmtsspectrumgpchnl comment '上行信道跳频组关联信息表';

alter table ccmtsspectrumgpchnl add constraint FK_Chanel_entityId foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: ccmtsspectrumgphophis                                 */
/*==============================================================*/
create table ccmtsspectrumgphophis
(
   entityId             bigint(20) not null comment '设备Id',
   channelIndex         bigint(20) not null comment '信道索引',
   cmcMac               varchar(32) not null comment '上行信道Mac地址',
   chnlId               int(11) not null comment '信道id',
   hisIndex             int(11) not null comment '历史记录索引',
   hisSelect1st         int(11) comment '第一优先级选择',
   hisSelect2st         int(11) comment '第二优先级选择',
   hisSelect3st         int(11) comment '第三优先级选择',
   hisPolicy            int(11) comment '跳频策略',
   hisGroupId           int(11) comment '跳频组Id',
   hisMaxHop            int(11) comment '最大Hop次数',
   hisFrequency         int(11) comment '中心频点',
   hisWidth             int(11) comment '带宽',
   hisPower             int(11) comment '接收电平',
   hisSnr               int(11) comment 'SNR阀值',
   hisCorrect           int(11) comment '可纠错码',
   hisUnCorrect         int(11) comment '不可纠错码',
   lastHopTimeYMD       bigint(20) comment '上次跳频时间',
   primary key (cmcMac, chnlId, hisIndex)
);

alter table ccmtsspectrumgphophis comment '信道跳频历史记录表';

alter table ccmtsspectrumgphophis add constraint FK_ChnlHis_entityId foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: emsccmtsspectrumgp                                    */
/*==============================================================*/
create table emsccmtsspectrumgp
(
   emsGroupId           bigint(20) auto_increment not null comment '网管全局跳频组Id',
   emsGroupName         varchar(64) not null comment '设备跳频组Id',
   hopPeriod            int(11) comment 'Hop周期',
   snrThres1            int(11) comment 'SNR第一阀值',
   snrThres2            int(11) comment 'SNR第二阀值',
   fecThresCorrect1     int(11) comment '第一可纠错码',
   fecThresCorrect2     int(11) comment '第二可纠错码',
   fecThresUnCorrect1   int(11) comment '第一不可纠错码',
   fecThresUnCorrect2   int(11) comment '第二不可纠错码',
   adminStatus          int(11) comment '设备跳频组开关',
   maxHopLimit          int(11) comment '最大Hop次数限制',
   groupPolicy          int(11) comment '跳频策略',
   groupPriority1st     int(11) comment '第一优先级',
   groupPriority2st     int(11) comment '第二优先级',
   groupPriority3st     int(11) comment '第三优先级',
   primary key (emsGroupId)
);

alter table emsccmtsspectrumgp comment '全局自动跳频组表';

/*==============================================================*/
/* Table: emsccmtsspectrumgpfreq                                */
/*==============================================================*/
create table emsccmtsspectrumgpfreq
(
   emsGroupId           bigint(20) not null comment '设备id',
   freqIndex            int(11) not null comment '成员频点索引',
   freqFrequency        int(11) comment '中心频点',
   freqMaxWidth         int(11) comment '最大带宽',
   freqPower            int(11) comment '接收电平',
   primary key (emsGroupId, freqIndex)
);

alter table emsccmtsspectrumgpfreq comment '全局自动跳频组成员频点表';

alter table emsccmtsspectrumgpfreq add constraint FK_Freq_GlobalGp foreign key (emsGroupId)
      references emsccmtsspectrumgp (emsGroupId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: ccmtsspectrumgptemplate                               */
/*==============================================================*/
create table ccmtsspectrumgptemplate
(
   tempLateId           bigint(20) auto_increment not null comment '自动跳频模板Id',
   templateName         varchar(64),
   globalAdminStatus    int(11) comment '全局开关',
   snrQueryPeriod       int(11) comment '监控周期',
   hopHisMaxCount       int(11) comment '最大记录数',
   chnlSecondaryProf    int(11) comment '第一备份调制方式',
   chnlTertiaryProf     int(11) comment '第二备份调制方式',
   gpForUpChannel1      int(11) comment '上行信道1关联跳频组',
   gpForUpChannel2      int(11) comment '上行信道2关联跳频组',
   gpForUpChannel3      int(11) comment '上行信道3关联跳频组',
   gpForUpChannel4      int(11) comment '上行信道4关联跳频组',
   primary key (tempLateId)
);

alter table ccmtsspectrumgptemplate comment '自动跳频模板';

/*==============================================================*/
/* Table: spectrumtempgprelation                                */
/*==============================================================*/
create table spectrumtempgprelation
(
   tempLateId           bigint(20) not null comment '自动跳频模板Id',
   emsGroupId           bigint(20) not null comment '网管全局跳频组Id',
   groupId              int(11) not null comment '设备跳频组Id',
   primary key (tempLateId, emsGroupId, groupId)
);

alter table spectrumtempgprelation comment '自动跳频模板-全局跳频组关联表';

alter table spectrumtempgprelation add constraint FK_Template_emsGroupId foreign key (emsGroupId)
      references emsccmtsspectrumgp (emsGroupId) on delete cascade on update cascade;

alter table spectrumtempgprelation add constraint FK_Template_templateId foreign key (tempLateId)
      references ccmtsspectrumgptemplate (tempLateId) on delete cascade on update cascade;

/*-- version 1.7.17.1,build 2013-08-02,module cmc*/

-- version 1.7.18.0,build 2013-8-5,module cmc
ALTER TABLE `cmccmrelation` ADD INDEX `ix_ccr_entityId` (`entityId`) USING BTREE;
ALTER TABLE `cmccmrelation` ADD INDEX `ix_ccr_maclong` (`maclong`) USING BTREE ;
ALTER TABLE `cmccmrelation` ADD INDEX `ix_ccr_csi` (`docsIfCmtsCmStatusIndex`) USING BTREE ;
ALTER TABLE `cmcpe` ADD INDEX `ix_cmcpe_cpemac` (`topCmCpeMacAddress`) USING BTREE ;
/*-- version 1.7.18.0,build 2013-8-5,module cmc*/

-- version 1.7.18.0,build 2013-8-5,module cmc
create table perfcmcservicequality(
    cmcId bigint(20) not null,
    targetName varchar(100),
    targetValue decimal(12,2),
    collectTime timestamp,
    CONSTRAINT FK_perfcmcservicequality FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfcmcSignalQuality(
    cmcId bigint(20) not null,
    channelIndex bigint(20) not null,
    targetName varchar(100),
    targetValue decimal(12,2),
    collectTime timestamp,
    CONSTRAINT FK_perfcmcSignalQuality FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcLinkquality(
    cmcId bigint(20) not null,
    portIndex bigint(20) not null,
    optTxPower decimal(12,2),
    optRePower decimal(12,2),
    optCurrent decimal(12,2),
    optVoltage decimal(12,2),
    optTemp    decimal(12,2),
    collectTime timestamp,
    CONSTRAINT FK_perfCmcLinkquality FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcTempquality(
    cmcId bigint(20) not null,
    cmcIndex bigint(20) not null,
    memTemp int(10),
    rfTemp int(10),
    upTemp int(10),
    bcmTemp int(10),
    power1Temp  int(10),
    power2Temp  int(10),
    power3Temp  int(10),
    collectTime timestamp,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfCmcTempquality FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcFlowQuality(
    cmcId bigint(20) not null,
    channelIndex bigint(20) not null,
    channelInOctets bigint(20) not null,
    channelOutOctets bigint(20) not null,
    channelOctets bigint(20) not null,
    channelInSpeed decimal(12,2),
    channelOutSpeed decimal(12,2),
    channelUtilization decimal(12,2),
    collectTime timestamp not null,
    CONSTRAINT FK_perfCmcFlowQuality FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*-- version 1.7.18.0,build 2013-8-5,module cmc*/

-- version 1.7.18.0,build 2013-8-5,module cmc
ALTER TABLE `cmattribute` ADD INDEX `ix_ca_cmcId` (`cmcId`) USING BTREE ;
ALTER TABLE `perfsystemhis` ADD INDEX `ix_psh_cmcId` (`cmcId`) USING BTREE ;
ALTER TABLE `perfusbiterrorratehis` ADD INDEX `ix_puh_entityId` (`entityId`) USING BTREE ;
ALTER TABLE `perfusbiterrorratehis` ADD INDEX `ix_puh_cmcId` (`cmcId`) USING BTREE ;
ALTER TABLE `perfusbiterrorratehis` ADD INDEX `ix_puh_channelIndex` (`channelIndex`) USING BTREE ;
ALTER TABLE `perfnoisehis` ADD INDEX `ix_pnh_entityId` (`entityId`) USING BTREE ;
ALTER TABLE `perfnoisehis` ADD INDEX `ix_pnh_cmcId` (`cmcId`) USING BTREE ;
ALTER TABLE `perfnoisehis` ADD INDEX `ix_pnh_ifIndex` (`ifindex`) USING BTREE ;
/*-- version 1.7.18.0,build 2013-8-5,module cmc*/

-- version 1.7.18.1,build 2013-8-17,module cmc      
create table spectrumtempconfiglog
(
   configLogId          bigint(20) auto_increment not null comment '日志记录主键',
   tempLateId           bigint(20) comment '跳频组模板Id',
   templateName         varchar(64) comment '跳频模板名称',
   deviceNum            int(11) comment '所选设备数量',
   userId               bigint(20) comment '用户Id',
   userName             varchar(32) comment '用户名',
   configTime           timestamp comment '下发时间',
   configStatus         int(11) comment '配置状态',
   primary key (configLogId)
);
alter table spectrumtempconfiglog comment '自动跳频模板批量配置日志记录';
create table spectrumtempconfiglogDetail
(
   configDetailId       bigint(20) auto_increment not null comment '批量配置日志详细记录主键id',
   configLogId          bigint(20) comment '批量配置日志id',
   templateName         varchar(64) comment '跳频模板名称',
   configUnit           varchar(64) comment '配置单元',
   configOperation      varchar(128) comment '配置项',
   configTime           timestamp comment '配置时间',
   configResult         int(11) comment '配置结果',
   primary key (configDetailId)
);
alter table spectrumtempconfiglogDetail comment '批量配置日志记录详细信息';
alter table spectrumtempconfiglogDetail add constraint FK_FK_configDetail_configLogId foreign key (configLogId)
      references spectrumtempconfiglog (configLogId) on delete cascade on update cascade;
/*-- version 1.7.18.1,build 2013-8-17,module cmc*/

-- version 1.7.18.1,build 2013-9-7,module cmc
create table perfcmcservicequalitylast(
	cmcId bigint(20) not null,
	targetName varchar(100),
	targetValue decimal(12,2),
	collectTime timestamp,
	primary key(cmcId, targetName),
	CONSTRAINT FK_perfCmcServiceQualityLast FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfcmcSignalQualitylast(
	cmcId bigint(20) not null,
	channelIndex bigint(20) not null,
	targetName varchar(100),
	targetValue decimal(12,2),
	collectTime timestamp,
    primary key(cmcId,channelIndex,targetName),
	CONSTRAINT FK_perfcmcSignalQualityLast FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcLinkqualitylast(
	cmcId bigint(20) not null,
	portIndex bigint(20) not null,
	optTxPower decimal(12,2),
	optRePower decimal(12,2),
	optCurrent decimal(12,2),
	optVoltage decimal(12,2),
	optTemp    decimal(12,2),
	collectTime timestamp,
	primary key(cmcId,portIndex),
	CONSTRAINT FK_perfCmcLinkqualityLast FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcTempqualitylast(
	cmcId bigint(20) not null,
	cmcIndex bigint(20) not null,
	memTemp int(10),
	rfTemp int(10),
	upTemp int(10),
	bcmTemp int(10),
	power1Temp  int(10),
	power2Temp  int(10),
	power3Temp  int(10),
	collectTime timestamp,
	primary key(cmcId,cmcIndex),
	CONSTRAINT FK_perfCmcTempqualityLast FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcFlowQualitylast(
	cmcId bigint(20) not null,
	channelIndex bigint(20) not null,
	channelInOctets bigint(20) not null,
	channelOutOctets bigint(20) not null,
	channelOctets bigint(20) not null,
	channelInSpeed decimal(12,2),
	channelOutSpeed decimal(12,2),
	channelUtilization decimal(12,2),
	collectTime timestamp not null,
	primary key(cmcId, channelIndex),	
	CONSTRAINT FK_perfCmcFlowQualityLast FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 1.7.18.1,build 2013-9-7,module cmc*/
-- version 2.0.0.0,build 2013-10-1,module cmc
create table cmc8800bDSIpqamBaseInfo(
   cmcPortId            bigint(20) not null,
   cmcId                bigint(20),
   channelType          Tinyint(2),
   docsIfDownChannelId  int(3),
   docsIfDownChannelSymRate Int(1),
   ipqamTranspStreamID Int(4),
   ipqamOriginalNetworkID Int(4),
   ipqamQAMManager Tinyint(2),
   ipqamQAMGroupName varchar(10),
   ipqamAtten varchar(10),
   ipqamDtsAdjust  Tinyint(2),
   primary key (cmcPortId)
);
/*-- version 2.0.0.0,build 2013-10-1,module cmc*/
-- version 2.0.0.0,build 2013-10-20,module cmc
create table Cmc8800BDSIpqamStatusInfo(
		cmcPortId	bigint(20) ,
		cmcId                	bigint(20),
		channelType          	Tinyint(2),
		docsIfDownChannelId  	int(3),
		ipqamOutputQAMChannel	int(3),
		ipqamFrequency	varchar(7),
		ipqamUsedUDPPorts	int(3),
		ipqamUsedBandwidth	varchar(7),
		ipqamBandwidthCapacity	varchar(7),
		ipqamPercent	decimal(6,3),
		ipqamAtten	int(11),
		ipqamSymbolRate	varchar(7),
		ipqamModulation	varchar(10)
);
create table Cmc8800BDSIpqamMappings(
	mappingId	bigint(20)  ,
	ipqamOutputQAMChannel	int(3),
	ipqamPidMapString	varchar(20),
	ipqamDestinationIPAddress	varchar(15),
	ipqamUDPPort	int,
	ipqamActive	int,
	ipqamStreamType	int,
	ipqamProgramNumberInput	int,
	ipqamProgramNumberOutput	int,
	ipqamPMV	int,
	ipqamDataRateEnable	int,
	ipqamDataRate	int
);
create table Cmc8800BIpQamISInfo(
	isProgramId	bigint(20),
	ipqamDestinationIP	varchar(15),
	ipqamSendMode	int,
	ipqamSourceIP	varchar(15),
	ipqamSourcePort	int,
	ipqamUDPPort	int,
	ipqamProgType	int,
	ipqamSYNC	int,
	ipqamType	int,
	ipqamInputProgramNumber	int,
	ipqamInputPMTID	int,
	ipqamInputPCRID	int,
	ipqamTotalESPIDs	int,
	ipqamInputBitrate	decimal(6,3)
);
create table Cmc8800BIpQamOSInfo(
	osProgramId	bigint(20),
	ipqamType	int,
	ipqamQAMManager	int,
	ipqamOutputQAMChannel	int,
	ipqamDestinationIP	varchar(15),
	ipqamUDPPort	int,
	ipqamSYNC	int,
	ipqamOutputProgramNumber	int,
	ipqamOutputPMTID	int,
	ipqamOutputPCRID	int,
	ipqamOutputBitrate	decimal(6,3),
	ipqamActive	int
);
/*-- version 2.0.0.0,build 2013-10-20,module cmc*/
-- version 2.0.0.0,build 2013-10-24,module cmc
	/*delete some columns from Cmc8800BdsIpqamStatusInfo */
alter table Cmc8800BdsIpqamStatusInfo drop column docsIfDownChannelId;
alter table Cmc8800BdsIpqamStatusInfo drop column channelType;
	/*add some column for tables */
alter table Cmc8800BDSIpqamMappings add column cmcId  bigint(20);
alter table Cmc8800BIpQamISInfo add column cmcId  bigint(20);
alter table Cmc8800BIpQamOsInfo add column cmcId  bigint(20);
	/*add Constraints for tables*/
alter table Cmc8800BdsIpqamStatusInfo add  primary key(cmcPortId); 
alter table Cmc8800BDSIpqamMappings add primary key(mappingId);
alter table Cmc8800BIpQamISInfo add primary key(isProgramId);
alter table Cmc8800BIpQamOSInfo add primary key(osProgramId);
	/*modify Constraints for tables */
alter table cmc8800bdsipqamstatusinfo modify cmcportId bigint(20) auto_increment not null; 
alter table Cmc8800BDSIpqamMappings modify mappingId bigint(20) auto_increment not null;
alter table Cmc8800BIpQamISInfo modify isProgramId bigint(20) auto_increment not null;
alter table Cmc8800BIpQamOSInfo modify osProgramId bigint(20) auto_increment not null;
	/*modify the length of the column*/
alter table Cmc8800BDSIpqamMappings modify column ipqamPidMapString text;
/*-- version 2.0.0.0,build 2013-10-24,module cmc*/
/*-- version 2.0.1.0,build 2013-10-31,module cmc*/
create table cmSignal
(
   cmId bigint(20) not null,
   downChannelSnr varchar(10),
   downChannelTx varchar(10),
   downChannelFrequency varchar(10),
   upChannelTx varchar(10),
   upChannelFrequency varchar(10),
   collectTime timestamp,
   primary key (cmId)
);
alter table cmc8800BDSIpqamBaseInfo add constraint FK_8800Bipqam_attr foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;
alter table Cmc8800BDSIpqamStatusInfo add constraint FK_8800BipqmStatus_attr foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;
alter table Cmc8800BDSIpqamMappings add constraint FK_8800BipqamMappings_attr foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;
alter table Cmc8800BIpQamISInfo add constraint FK_8800BipqamIs_attr foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;
alter table Cmc8800BIpQamOSInfo add constraint FK_8800BipqamOs_attr foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;
/**
 * FPGA 
 */
create table Cmc8800BFpgaIsAvailableCheck(
    fpgaId bigint(20) auto_increment not null,
    cmcId bigint(20) not null,
    fpgaIpqamChannelCount varchar(20) not null,
    itemNum tinyint(1),
    primary key(fpgaId)

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='检查设备是否支持该项功能，根据设备上的信息来判断';

alter table Cmc8800BFpgaIsAvailableCheck add constraint FK_8800Fpga_CmcAttr foreign key (cmcId)
      references CmcAttribute (cmcId) on delete cascade on update cascade;
/*-- version 2.0.1.0,build 2013-10-31,module cmc*/
-- version 2.0.1.0,build 2013-11-07,module cmc
alter table cmc8800BDSIpqamBaseInfo modify column  ipqamQAMGroupName varchar(20);
/*-- version 2.0.1.0,build 2013-11-07,module cmc*/
-- version 2.0.1.0,build 2013-11-19,module cmc
/*Type is not allowed to appear in table name*/
rename table cmc8800bdsipqambaseinfo to CmcDsIpqambaseinfo;
rename table cmc8800bdsipqammappings to CmcDsIpqammappings;
rename table cmc8800bdsipqamstatusinfo to CmcDsIpqamstatusinfo;
rename table cmc8800bipqamisinfo to CmcDsIpqamisinfo;
rename table cmc8800bipqamosinfo to CmcDsIpqamosinfo;
/**
 * 废弃FPGA原表,重写该表，以做FPGA支持信息
 */
rename table cmc8800bfpgaisavailablecheck to cmcfpgainfo;

alter table cmcfpgainfo change fpgaIpqamChannelCount ipqamChannelCount int; 
alter table  cmcfpgainfo add column	subnetVlanCount int;         
alter table  cmcfpgainfo add column	aclUplinkEgress int;         
alter table  cmcfpgainfo add column	aclUplinkIngress int;        
alter table  cmcfpgainfo add column	aclCableEgress int;          
alter table  cmcfpgainfo add column	aclCableIngress int;         
alter table  cmcfpgainfo add column	aclUplinkEgressCopy2cpu int; 
alter table  cmcfpgainfo add column	aclUplinkIngressCopy2cpu int;
alter table  cmcfpgainfo add column	aclCableEgressCopy2cpu int;  
alter table  cmcfpgainfo add column	aclCableIngressCopy2cpu int; 
alter table  cmcfpgainfo add column	vipSupportCount int;         
alter table  cmcfpgainfo add column	cosCmCount int;              
alter table  cmcfpgainfo add column	cosCountPerCm int;           
alter table  cmcfpgainfo add column	macTblHashLen int;           
alter table  cmcfpgainfo add column	macTblHashDeep int;          
alter table  cmcfpgainfo add column	srcVerifyHashLen int;        
alter table  cmcfpgainfo add column	srcVerifyHashDeep int;

/*-- version 2.0.1.0,build 2013-11-19,module cmc*/

-- version 2.0.1.0,build 2013-12-4,module cmc
alter table cmcfftmonitorcfg add constraint FK_fftmonitor_rela foreign key (cmcId)
      references cmcentityrelation (cmcId) on delete cascade on update cascade;

ALTER TABLE `cmstaticip` DROP FOREIGN KEY `FK_cm_staticip`;
ALTER TABLE `cmstaticip` ADD CONSTRAINT `FK_cm_staticip` FOREIGN KEY (`cmId`) REFERENCES `cmccmrelation` (`cmId`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `docsif3cmtscmusstatus` DROP FOREIGN KEY `FK_cm_usStatus`;
ALTER TABLE `docsif3cmtscmusstatus` ADD CONSTRAINT `FK_cm_usStatus` FOREIGN KEY (`cmId`) REFERENCES `cmccmrelation` (`cmId`) ON DELETE CASCADE ON UPDATE CASCADE;
/*--version 2.0.1.0,build 2013-12-4,module cmc*/

--version 2.0.1.0,build 2013-12-5,module cmc
create table cmflaphis(

    cmMac varchar(17) not null,
    cmcId   bigint(20) not null,
    collectTime datetime not null,
    lastFlapTime varchar(20),
    insFailNum int(11) not null,
    hitNum int(11) not null,
    missNum int(11) not null,
    crcErrorNum int(11) not null,
    powerAdjLowerNum int(11) not null,
    powerAdjHigherNum int(11) not null

)partition by range (
/* 按照年+周来分区，一年分成52个分区;less than 值必须大于前一个*/
yearweek(collectTime)
)
(   
    partition p201350 values less than (201350),
    partition p201351 values less than (201351),
    partition p201352 values less than (201352),
                                               
    partition p201401 values less than (201401),
    partition p201402 values less than (201402),
    partition p201403 values less than (201403),
    partition p201404 values less than (201404),
    partition p201405 values less than (201405),
    partition p201406 values less than (201406),
    partition p201407 values less than (201407),
    partition p201408 values less than (201408),
    partition p201409 values less than (201409),
                                               
    partition p201410 values less than (201410),
    partition p201411 values less than (201411),
    partition p201412 values less than (201412),
    partition p201413 values less than (201413),
    partition p201414 values less than (201414),
    partition p201415 values less than (201415),
    partition p201416 values less than (201416),
    partition p201417 values less than (201417),
    partition p201418 values less than (201418),
    partition p201419 values less than (201419)
);


alter table cmflaphis add index index_FlapMacAndTime(cmMac,collectTime);


alter table cmflap add column   lastFlapTime varchar(20);
alter table cmflap add column   hitNum int(11) not null;
alter table cmflap add column   missNum int(11) not null;
alter table cmflap add column   crcErrorNum int(11) not null;
alter table cmflap add column   powerAdjLowerNum int(11) not null;
alter table cmflap add column   powerAdjHigherNum int(11) not null;
/*--version 2.0.1.0,build 2013-12-5,module cmc*/

--version 2.0.2.0,build 2013-12-5,module cmc
create table ClearCcRecord
(
	onuId bigint(20) not null,
	oltId bigint(20),
	oltName varchar(64),
	oltIp varchar(32),
	onuName varchar(64),
	onuMac varchar(32),
	ponIndex bigint(40),
	offlineTime timestamp,
	clearTime timestamp,
	clearMode varchar(1),
	clearResult varchar(1),
	primary key (onuId)
);
/*--version 2.0.2.0,build 2013-12-5,module cmc*/

--version 2.0.2.0,build 2013-12-18,module cmc
/*==============================================================*/
/* Table: CmcOpReceiverDcPower                                  */
/*==============================================================*/
create table CmcOpReceiverDcPower 
(
   id                   bigint not null AUTO_INCREMENT,
   cmcId                bigint,
   powerIndex           int,
   powerVoltage         int,
   dt                   bigint,
   primary key(id),
   CONSTRAINT FK_cmcAttr_CmcOpReceiverDcPower FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*==============================================================*/
/* Table: CmcOpReceiverAcPower                             */
/*==============================================================*/
create table CmcOpReceiverAcPower 
(
   id                   bigint not null AUTO_INCREMENT,
   cmcId                bigint,
   powerIndex           int,
   powerVoltage1        int,
   dt                   bigint,
   primary key(id),
   CONSTRAINT FK_cmcAttr_CmcOpReceiverAcPower FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*==============================================================*/
/* Table: CmcOpReceiverInputInfo                                */
/*==============================================================*/
create table CmcOpReceiverInputInfo 
(
   id                   bigint not null AUTO_INCREMENT,
   cmcId                bigint,
   inputIndex           int,
   inputPower           int,
   dt                   bigint,
   primary key(id),
   CONSTRAINT FK_cmcAttr_CmcOpReceiverInputInfo FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*==============================================================*/
/* Table: CmcOpReceiverRfCfg                                    */
/*==============================================================*/
create table CmcOpReceiverRfCfg 
(
   id                   bigint not null AUTO_INCREMENT,
   cmcId                bigint,
   outputIndex          int,
   outputControl        tinyint,
   outputGainType       tinyint,
   outputLevel          int,
   outputAGCOrigin      int,
   outputRFlevelatt     int,
   dt                   bigint,
   primary key(id),
   CONSTRAINT FK_cmcAttr_CmcOpReceiverRfCfg FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*==============================================================*/
/* Table: CmcOpReceiverSwitchCfg                                */
/*==============================================================*/
create table CmcOpReceiverSwitchCfg 
(
   id                   bigint not null AUTO_INCREMENT,
   cmcId                bigint,
   switchIndex          int,
   switchState          tinyint,
   switchControl        tinyint,
   switchThres          int,
   primary key(id),
   CONSTRAINT FK_cmcAttr_CmcOpReceiverSwitchCfg FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE PerfCmcOpReceiverInputPowerHis (
    cmcId bigint(20) NOT NULL,
    inputIndex           int,
    inputPower int DEFAULT NULL,
    collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY FK_perfCmcOpReceiverInput (cmcId),
    CONSTRAINT FK_PerfCmcOpReceiverInputPower FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE CmcOpReceiverRfPortInfo(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    cmcId          BIGINT(20) NOT NULL,
    rfPortIndex    INT NOT NULL,
    rfOutputLevel  INT,
    collectTime    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    KEY FK_CmcOpReceiverRfPortInfo(cmcId),
    CONSTRAINT FK_CmcOpReceiverRfPortInfo FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE CmcOpReceiverChannelNumInfo(
    id                   BIGINT NOT NULL AUTO_INCREMENT,
    cmcId                BIGINT(20) NOT NULL,
    channelNumIndex      INT NOT NULL,
    channelNum           INT,
    collectTime          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    KEY FK_CmcOpReceiverChannelNumInfo(cmcId),
    CONSTRAINT FK_CmcOpReceiverChannelNumInfo FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*--version 2.0.2.0,build 2013-12-18,module cmc*/

--version 2.0.2.0,build 2013-12-19,module cmc
ALTER TABLE cmcAttribute ADD COLUMN statusChangeTime TIMESTAMP NOT NULL;
/*--version 2.0.2.0,build 2013-12-19,module cmc*/

-- version 2.0.3.0,build 2013-12-23,module cmc
 CREATE TABLE cmcipqaminfo (                                                                                                   
    cmcId bigint(20) NOT NULL,                                                                                                  
    ipqamIpAddr varchar(32) DEFAULT NULL,                                                                                       
    ipqamIpMask varchar(32) DEFAULT NULL,                                                                                       
    ipqamGw varchar(32) DEFAULT NULL,                                                                                           
    ipqamMacAddr varchar(20) DEFAULT NULL,                                                                                      
    PRIMARY KEY (cmcId),                                                                                                        
    CONSTRAINT FK_cmcIpqamIpinfo FOREIGN KEY (cmcId) REFERENCES cmcattribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE  
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
/*-- version 2.0.3.0,build 2013-12-23,module cmc*/

-- version 2.0.3.0,build 2013-12-24,module cmc
alter table cmflaphis drop column cmcId;
/*-- version 2.0.3.0,build 2013-12-24,module cmc*/

-- version 2.0.3.0,build 2014-1-7,module cmc
alter table cmflap
add column increaseInsNum int (11) default  0  after topCmFlapInsertionFailNum,
add column increaseHitPercent decimal(10,2) default 0  after missNum,
add column increasePowerAdjNum int (11) default 0  after powerAdjHigherNum;


alter table cmflaphis
add column increaseInsNum int (11) default 0  after insFailNum,
add column increaseHitPercent decimal(10,2) default 0.00  after missNum,
add column increasePowerAdjNum int (11) default 0 after powerAdjHigherNum;
/*-- version 2.0.3.0,build 2014-1-7,module cmc*/

-- version 2.0.3.0,build 2014-1-9,module cmc
alter table cmflaphis add column cmId bigint(20)  NOT NULL  first;
alter table cmflaphis add column cmcId bigint(20) NOT NULL  after cmId;
alter table cmflap add column dt timestamp   NULL  after increasePowerAdjNum;
/*-- version 2.0.3.0,build 2014-1-9,module cmc*/

-- version 2.0.3.0,build 2014-1-13,module cmc
/*取消cmflaphis表中未用到的分区，改为使用默认分区。根据讨论意见，该表中的历史数据只存储3天，超过则删除配合tri文件中语句使用*/
alter table cmflaphis drop partition p201405;
alter table cmflaphis drop partition p201406;
alter table cmflaphis drop partition p201407;
alter table cmflaphis drop partition p201408;
alter table cmflaphis drop partition p201409;
alter table cmflaphis drop partition p201410;
alter table cmflaphis drop partition p201411;
alter table cmflaphis drop partition p201412;
alter table cmflaphis drop partition p201413;
alter table cmflaphis drop partition p201414;
alter table cmflaphis drop partition p201415;
alter table cmflaphis drop partition p201416;
alter table cmflaphis drop partition p201417;
alter table cmflaphis drop partition p201418;
alter table cmflaphis drop partition p201419;
alter TABLE cmflaphis ADD PARTITION(PARTITION pcatchall values LESS THAN MAXVALUE);
/*-- version 2.0.3.0,build 2014-1-13,module cmc*/

-- version 2.0.3.0,build 2014-2-26,module cmc
alter table perfCmcFlowQuality modify column channelInSpeed decimal(20,2) DEFAULT NULL;
alter table perfCmcFlowQuality modify column channelOutSpeed decimal(20,2) DEFAULT NULL;
alter table perfCmcFlowQualitylast modify column channelInSpeed decimal(20,2) DEFAULT NULL;
alter table perfCmcFlowQualitylast modify column channelOutSpeed decimal(20,2) DEFAULT NULL;
/* -- version 2.0.3.0,build 2014-2-26,module cmc */

-- version 2.0.6.4,build 2014-3-21,module cmc

/*==============================================================*/
/* Table: userNumHisPon                                       */
/*==============================================================*/

alter table userNumHisAll add column cpeInteractiveNum int;
alter table userNumHisAll add column cpeBroadbandNum int;
alter table userNumHisAll add column cpeMtaNum int;
alter table userNumHisArea add column cpeInteractiveNum int;
alter table userNumHisArea add column cpeBroadbandNum int;
alter table userNumHisArea add column cpeMtaNum int;
alter table userNumHisDevice add column cpeInteractiveNum int;
alter table userNumHisDevice add column cpeBroadbandNum int;
alter table userNumHisDevice add column cpeMtaNum int;
alter table userNumHisCcmts add column cpeInteractiveNum int;
alter table userNumHisCcmts add column cpeBroadbandNum int;
alter table userNumHisCcmts add column cpeMtaNum int;
alter table userNumHisPort add column cpeInteractiveNum int;
alter table userNumHisPort add column cpeBroadbandNum int;
alter table userNumHisPort add column cpeMtaNum int;
create table userNumHisPon
(
  entityId             bigint,
  ponIndex            bigint,
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp,
  cpeInteractiveNum       int,
  cpeBroadbandNum         int,
  cpeMtaNum               int
)
engine = MyISAM;



/*==============================================================*/
/* Table: userNumLastPon                                         */
/*==============================================================*/
create table userNumLastPon
(
  entityId             bigint,
  ponIndex            bigint,
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp,
  cpeInteractiveNum       int,
  cpeBroadbandNum         int,
  cpeMtaNum               int
);

/*==============================================================*/
/* Table: userNumLastCcmts                                       */
/*==============================================================*/
create table userNumLastCcmts
(
  entityId             bigint,
  ccIfIndex            bigint,
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp,
  cpeInteractiveNum       int,
  cpeBroadbandNum         int,
  cpeMtaNum               int
);


/*==============================================================*/
/* Table: userNumLastDevice                                      */
/*==============================================================*/
create table userNumLastDevice
(
  entityId             bigint,
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp,
  cpeInteractiveNum       int,
  cpeBroadbandNum         int,
  cpeMtaNum               int
);


/*==============================================================*/
/* Table: userNumLastPort                                        */
/*==============================================================*/
create table userNumLastPort
(
  entityId             bigint,
  ccIfIndex            bigint,
  portIfIndex          bigint,
  portType             tinyint,
  time                 timestamp,
  onlineNum            int,
  otherNum             int,
  offlineNum           int,
  interactiveNum       int,
  broadbandNum         int,
  mtaNum               int,
  integratedNum        int,
  cpeNum               int,
  realtime             timestamp,
  cpeInteractiveNum       int,
  cpeBroadbandNum         int,
  cpeMtaNum               int
);
/* -- version 2.0.6.4,build 2014-3-21,module cmc */


-- version 2.0.6.2,build 2014-3-13,module cmc

create table perfCmcCpuQuality(
    cmcId bigint(20) not null,
    collectValue decimal(10,2) not null,
    collectTime timestamp not null,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfcmccpuquality_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcMemQuality(
    cmcId bigint(20) not null,
    collectValue decimal(10,2) not null,
    collectTime timestamp not null,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfcmcMemquality_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcFlashQuality(
    cmcId bigint(20) not null,
    collectValue decimal(10,2) not null,
    collectTime timestamp not null,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfcmcflashquality_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcSnrQuality(
    cmcId bigint(20) not null,
    channelIndex bigint(20) not null,
    collectValue int(11) not null,
    collectTime timestamp not null,
    primary key(cmcId,channelIndex,collectTime),
    CONSTRAINT FK_perfcmcsnrquality_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcSnrQualityLast(
	cmcId bigint(20) not null,
    channelIndex bigint(20) not null,
    collectValue int(11) not null,
    collectTime timestamp not null,
	primary key(cmcId, channelIndex),
	CONSTRAINT FK_perfcmcsnrqualityLast FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcErrorCodeQuality(
    cmcId bigint(20) not null,
    channelIndex bigint(20) not null,
    ccerCode int(11) not null,
    ucerCode int(11) not null,
    ccerRate decimal(5,2) not null,
    ucerRate decimal(5,2) not null,
    collectTime timestamp not null,
    primary key(cmcId,channelIndex,collectTime),
    CONSTRAINT FK_perfcmcecquality_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcErrorCodeQualityLast(
    cmcId bigint(20) not null,
    channelIndex bigint(20) not null,
    ccerCode int(11) not null,
    ucerCode int(11) not null,
    ccerRate decimal(5,2) not null,
    ucerRate decimal(5,2) not null,
    collectTime timestamp not null,
    primary key(cmcId,channelIndex),
	CONSTRAINT FK_perfcmcecqualitylast_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.0.6.2,build 2014-3-13,module cmc */

-- version 2.0.6.6,build 2014-5-13,module cmc
create table CommonConfig
(
config     varchar(1024) comment '一行一条命令'
);
/* -- version 2.0.6.6,build 2014-5-13,module cmc */

-- version 2.0.6.6,build 2014-6-14,module cmc
alter table docsif3cmtscmusstatus modify column cmUsStatusEqData varchar(1024);
alter table cmattribute modify column StatusTimingOffset bigint(20);
alter table cmattribute modify column StatusEqualizationData varchar(1024);
alter table cmattribute modify column StatusUnerroreds bigint(20);
alter table cmattribute modify column StatusCorrecteds bigint(20);
alter table cmattribute modify column StatusUncorrectables bigint(20);
alter table cmattribute modify column StatusExtUnerroreds bigint(20);
alter table cmattribute modify column StatusExtCorrecteds bigint(20);
alter table cmattribute modify column StatusExtUncorrectables bigint(20);
alter table cmattribute modify column StatusMicroreflections bigint(20);
alter table cmattribute modify column StatusDocsisRegMode bigint(20);
alter table cmattribute modify column StatusModulationType bigint(20);
alter table cmattribute modify column statusvaluelastupdate bigint(20);
alter table cmattribute modify column StatusHighResolutionTO bigint(20);
/* -- version 2.0.6.6,build 2014-6-14,module cmc */

-- version 2.0.6.6,build 2014-7-22,module cmc
DROP TABLE IF EXISTS CommonConfig;
/* -- version 2.0.6.6,build 2014-7-22,module cmc */

-- version 2.3.1.0,build 2014-8-9,module cmc
/*==============================================================*/
/* Table: CmcOpReceiverType                                    */
/*==============================================================*/
create table cmcopreceivertype 
(
   id                   bigint not null AUTO_INCREMENT,
   cmcId                bigint,
   dorDevTypeIndex      int,
   dorDevType           tinyint,
   primary key(id),
   CONSTRAINT FK_cmcAttr_cmcopreceivertype FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.3.1.0,build 2014-8-9,module cmc */

-- version 2.3.1.0,build 2014-8-11,module cmc
DROP TABLE IF EXISTS perfCmcTempquality;
create table perfCmcTempquality(
    cmcId bigint(20) not null,
    cmcIndex bigint(20) not null,
    usTemp int(10),
    dsTemp int(10),
    outsideTemp int(10),
    insideTemp int(10),
    collectTime timestamp,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfCmcTempquality FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS perfCmcTempqualitylast;
create table perfCmcTempqualitylast(
	cmcId bigint(20) not null,
    cmcIndex bigint(20) not null,
    usTemp int(10),
    dsTemp int(10),
    outsideTemp int(10),
    insideTemp int(10),
    collectTime timestamp,
	primary key(cmcId,cmcIndex),
	CONSTRAINT FK_perfCmcTempqualityLast FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.3.1.0,build 2014-8-11,module cmc */



-- version 2.4.0.0,build 2014-8-14,module cmc
alter table cmcupchannelbaseinfo add column cmtsChannelModulationProfile bigint(20);
/* -- version 2.4.0.0,build 2014-8-14,module cmc */

-- version 2.4.1.0,build 2014-8-18,module cmc
alter table perfcmcerrorcodequality add column noerCode int(11) not null;
alter table perfcmcerrorcodequality add column noerRate decimal(5,2) not null;
alter table perfcmcerrorcodequalitylast add column noerCode int(11) not null;
alter table perfcmcerrorcodequalitylast add column noerRate decimal(5,2) not null;
/* -- version 2.4.1.0,build 2014-8-18,module cmc */
-- version 2.4.1.0,build 2014-8-21,module cmc
DROP TABLE IF EXISTS perfCmcTempquality;
create table perfCmcTempquality(
    cmcId bigint(20) not null,
    cmcIndex bigint(20) not null,
    usTemp int(10),
    dsTemp int(10),
    outsideTemp int(10),
    insideTemp int(10),
    collectTime timestamp,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfCmcTempquality FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS perfCmcTempqualitylast;
create table perfCmcTempqualitylast(
	cmcId bigint(20) not null,
    cmcIndex bigint(20) not null,
    usTemp int(10),
    dsTemp int(10),
    outsideTemp int(10),
    insideTemp int(10),
    collectTime timestamp,
	primary key(cmcId,cmcIndex),
	CONSTRAINT FK_perfCmcTempqualityLast FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.4.1.0,build 2014-8-21,module cmc */

-- version 2.4.1.0,build 2014-10-13,module cmc
drop table IF EXISTS cmcfftmonitorcfg;
drop table IF EXISTS cmcspectrumlast;
  
create table spectrumIILast 
(
   cmcId                bigint(20),
   freqIndex            bigint(20),
   data                 varchar(4300),
   dt                   timestamp
);

create table spectrumIISwitchOlt 
(
   entityId             bigint(20),
   collectSwitch        int,
   constraint PK_SPECTRUMIISWITCHOLT primary key clustered (entityId)
);
alter table spectrumIISwitchOlt add constraint entity_oltfftmonitor foreign key (entityId)
      references Entity (entityId) on update cascade on delete cascade;
      
create table spectrumIIConfig 
(
   cmcId                bigint(20),
   macIndex             varchar(20),
   freqInterval         int,
   freqStart            int,
   freqEnd              int,
   pollingStatus        int,
   constraint PK_SPECTRUMIICONFIG primary key clustered (cmcId)
);
alter table spectrumIIConfig add constraint cmc_fftconfig foreign key (cmcId)
      references CmcEntityRelation (cmcId) on update cascade on delete cascade;

create table spectrumIISwitchCmts 
(
   cmcId                bigint(20),
   hisVideoSwitch       int,
   collectSwitch        int,
   constraint PK_SPECTRUMIISWITCHCMTS primary key clustered (cmcId)
);
alter table spectrumIISwitchCmts add constraint cmc_cmcfftmonitor foreign key (cmcId)
      references CmcEntityRelation (cmcId) on update cascade on delete cascade;

create table spectrumIIVideo 
(
   videoId              bigint(20) auto_increment not null,
   videoName            varchar(64),
   startTime            timestamp DEFAULT '0000-00-00 00:00:00',
   endTime              timestamp DEFAULT '0000-00-00 00:00:00',
   videoType			int,
   entityIp             varchar(32),
   entityType           int,
   oltAlias             varchar(64),
   cmtsAlias            varchar(64),
   userName             varchar(32),
   terminalIp           varchar(32),
   timeInterval			int,
   url                  varchar(256),
   constraint PK_SPECTRUMVIDEO primary key clustered (videoId)
);

create table spectrumIICmstRela 
(
   cmcId                bigint(20),
   videoId              bigint(20)
);
alter table spectrumIICmstRela add constraint cmc_spectrumrela foreign key (cmcId)
      references CmcEntityRelation (cmcId) on update cascade on delete cascade;
alter table spectrumIICmstRela add constraint spectrumrela_video foreign key (videoId)
      references spectrumIIVideo (videoId) on update cascade on delete cascade;
alter table spectrumiivideo add column firstFrameTime timestamp;
/*-- version 2.4.1.0,build 2014-10-13,module cmc*/

-- version 2.4.1.0,build 2014-10-15,module cmc
CREATE TABLE perfcmcsnrqualitysummaryorigin (                                                                                                 
      cmcId bigint(20) NOT NULL,                                                                                                            
      channelIndex bigint(20) NOT NULL,                                                                                                                                                                                        
      targetValue decimal(12,2) DEFAULT NULL,                                                                                               
      collectTime timestamp NOT NULL,
      index ix_cmcsnr_origin (cmcId,channelIndex,collectTime)                                           
) ENGINE=InnoDB DEFAULT CHARSET=utf8;    

CREATE TABLE perfcmcsnrqualitysummary (                                                                                                 
     cmcId bigint(20) NOT NULL,                                                                                                            
     channelIndex bigint(20) NOT NULL,                                                                                                     
     snrAvg decimal(12,2),                                                                                          
     snrMin decimal(12,2),                                                                                               
     snrMax decimal(12,2),     
     summarizeTime timestamp,
     PRIMARY KEY (cmcId,channelIndex,summarizeTime)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;     

CREATE TABLE perfcmcflowqualitysummaryorigin (                                                                                                 
     cmcId bigint(20) NOT NULL,                                                                                                          
     channelIndex bigint(20) NOT NULL,                                                                                                                                                                                                  
     channelInSpeed decimal(20,2) DEFAULT NULL,                                                                                          
     channelOutSpeed decimal(20,2) DEFAULT NULL,                                                                                         
     channelUtilization decimal(12,2) DEFAULT NULL,                                                                                      
     collectTime timestamp NOT NULL,
     index ix_cmcflow_origin (cmcId,channelIndex,collectTime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;     
 
CREATE TABLE perfcmcflowqualitysummary (                                                                                                 
     cmcId bigint(20) NOT NULL,                                                                                                            
     channelIndex bigint(20) NOT NULL,
     channelInSpeedAvg decimal(20,2),                                                                                                    
     channelInSpeedMin decimal(20,2),                                                                                          
     channelInSpeedMax decimal(20,2),                                                                                               
     channelOutSpeedAvg decimal(20,2),
     channelOutSpeedMin decimal(20,2),
     channelOutSpeedMax decimal(20,2),  
     channelUtilizationAvg decimal(12,2),
     channelUtilizationMin decimal(12,2),
     channelUtilizationMax decimal(12,2),
     summarizeTime timestamp,
     PRIMARY KEY (cmcId, channelIndex, summarizeTime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;      

CREATE TABLE perfcmtssnrqualitysummaryorigin (                                                                                                 
      cmcId bigint(20) NOT NULL,                                                                                                            
      channelIndex bigint(20) NOT NULL,                                                                                                                                                                                        
      targetValue decimal(12,2) DEFAULT NULL,                                                                                               
      collectTime timestamp NOT NULL,
      index ix_cmtssnr_origin (cmcId,channelIndex,collectTime)                                           
) ENGINE=InnoDB DEFAULT CHARSET=utf8;    

CREATE TABLE perfcmtssnrqualitysummary (                                                                                                 
     cmcId bigint(20) NOT NULL,                                                                                                            
     channelIndex bigint(20) NOT NULL,                                                                                                     
     snrAvg decimal(12,2),                                                                                          
     snrMin decimal(12,2),                                                                                               
     snrMax decimal(12,2),     
     summarizeTime timestamp,
     PRIMARY KEY (cmcId,channelIndex,summarizeTime)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;     

CREATE TABLE perfcmtsflowqualitysummaryorigin (                                                                                                 
     cmcId bigint(20) NOT NULL,                                                                                                          
     channelIndex bigint(20) NOT NULL,                                                                                                                                                                                                  
     channelInSpeed decimal(20,2) DEFAULT NULL,                                                                                          
     channelOutSpeed decimal(20,2) DEFAULT NULL,                                                                                         
     channelUtilization decimal(12,2) DEFAULT NULL,                                                                                      
     collectTime timestamp NOT NULL,
     index ix_cmtsflow_origin (cmcId,channelIndex,collectTime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;     
 
CREATE TABLE perfcmtsflowqualitysummary (                                                                                                 
     cmcId bigint(20) NOT NULL,                                                                                                            
     channelIndex bigint(20) NOT NULL,
     channelInSpeedAvg decimal(20,2),                                                                                                    
     channelInSpeedMin decimal(20,2),                                                                                          
     channelInSpeedMax decimal(20,2),                                                                                               
     channelOutSpeedAvg decimal(20,2),
     channelOutSpeedMin decimal(20,2),
     channelOutSpeedMax decimal(20,2),  
     channelUtilizationAvg decimal(12,2),
     channelUtilizationMin decimal(12,2),
     channelUtilizationMax decimal(12,2),
     summarizeTime timestamp,
     PRIMARY KEY (cmcId, channelIndex, summarizeTime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8; 

CREATE TABLE usernumhisccmtssummaryorigin (                                                    
     entityId bigint(20) DEFAULT NULL,                                               
     ccIfIndex bigint(20) DEFAULT NULL,                                               
     onlineNum int(11) DEFAULT NULL,                                                 
     otherNum int(11) DEFAULT NULL,                                                  
     offlineNum int(11) DEFAULT NULL,                                                
     interactiveNum int(11) DEFAULT NULL,                                            
     broadbandNum int(11) DEFAULT NULL,                                              
     mtaNum int(11) DEFAULT NULL,                                                    
     integratedNum int(11) DEFAULT NULL,                                             
     cpeNum int(11) DEFAULT NULL,                                                    
     cpeInteractiveNum int(11) DEFAULT NULL,                                         
     cpeBroadbandNum int(11) DEFAULT NULL,                                           
     cpeMtaNum int(11) DEFAULT NULL,
     realtime timestamp NOT NULL,    
     index ix_usernumccmts_origin (entityId, ccIfIndex, realtime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;  

CREATE TABLE usernumhisccmtssummary (                                                    
     entityId bigint(20),  
     cmcId bigint(20),   
     cmcType int, 
     ccIfIndex bigint(20),
     onlineNumAvg int(11),onlineNumMax int(11),onlineNumMin int(11),                                                 
     otherNumAvg int(11), otherNumMax int(11),otherNumMin int(11),                                             
     offlineNumAvg int(11), offlineNumMax int(11),offlineNumMin int(11),  
     totalNumAvg int(11), totalNumMax int(11), totalNumMin int(11),
     interactiveNumAvg int(11), interactiveNumMax int(11), interactiveNumMin int(11),                                            
     broadbandNumAvg int(11),  broadbandNumMax int(11), broadbandNumMin int(11),                                             
     mtaNumAvg int(11), mtaNumMax int(11), mtaNumMin int(11),                                              
     integratedNumAvg int(11),integratedNumMax int(11), integratedNumMin int(11),                                                
     cpeNumAvg int(11), cpeNumMax int(11), cpeNumMin int(11),                                                       
     cpeInteractiveNumAvg int(11),  cpeInteractiveNumMax int(11), cpeInteractiveNumMin int(11),                                          
     cpeBroadbandNumAvg int(11),  cpeBroadbandNumMax int(11), cpeBroadbandNumMin int(11),                                          
     cpeMtaNumAvg int(11), cpeMtaNumMax int(11), cpeMtaNumMin int(11),       
     summarizeTime timestamp NOT NULL,
     PRIMARY KEY (cmcId, summarizeTime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usernumhisdevicesummaryorigin (                                                   
     entityId bigint(20) DEFAULT NULL,                                               
     onlineNum int(11) DEFAULT NULL,                                                 
     otherNum int(11) DEFAULT NULL,                                                  
     offlineNum int(11) DEFAULT NULL,                                                
     interactiveNum int(11) DEFAULT NULL,                                            
     broadbandNum int(11) DEFAULT NULL,                                              
     mtaNum int(11) DEFAULT NULL,                                                    
     integratedNum int(11) DEFAULT NULL,                                             
     cpeNum int(11) DEFAULT NULL,                                                                 
     cpeInteractiveNum int(11) DEFAULT NULL,                                         
     cpeBroadbandNum int(11) DEFAULT NULL,                                           
     cpeMtaNum int(11) DEFAULT NULL,
     realtime timestamp NOT NULL,
     index ix_usernumdevice_origin (entityId, realtime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;   

CREATE TABLE usernumhisdevicesummary (                                                    
     entityId bigint(20),                                                                                          
     onlineNumAvg int(11),onlineNumMax int(11), onlineNumMin int(11),                                                 
     otherNumAvg int(11), otherNumMax int(11),otherNumMin int(11),                                             
     offlineNumAvg int(11),  offlineNumMax int(11),offlineNumMin int(11), 
     totalNumAvg int(11), totalNumMax int(11), totalNumMin int(11),
     interactiveNumAvg int(11), interactiveNumMax int(11), interactiveNumMin int(11),                                            
     broadbandNumAvg int(11),  broadbandNumMax int(11), broadbandNumMin int(11),                                             
     mtaNumAvg int(11),       mtaNumMax int(11), mtaNumMin int(11),                                              
     integratedNumAvg int(11),integratedNumMax int(11),   integratedNumMin int(11),                                                
     cpeNumAvg int(11),   cpeNumMax int(11),     cpeNumMin int(11),                                                       
     cpeInteractiveNumAvg int(11),  cpeInteractiveNumMax int(11),   cpeInteractiveNumMin int(11),                                          
     cpeBroadbandNumAvg int(11),  cpeBroadbandNumMax int(11), cpeBroadbandNumMin int(11),                                          
     cpeMtaNumAvg int(11),  cpeMtaNumMax int(11),  cpeMtaNumMin int(11),       
     summarizeTime timestamp NOT NULL,
     PRIMARY KEY (entityId, summarizeTime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usernumhisponsummaryorigin (                                                   
     entityId bigint(20) DEFAULT NULL, 
     ponIndex bigint(20) DEFAULT NULL,                                             
     onlineNum int(11) DEFAULT NULL,                                                 
     otherNum int(11) DEFAULT NULL,                                                  
     offlineNum int(11) DEFAULT NULL,                                                
     interactiveNum int(11) DEFAULT NULL,                                            
     broadbandNum int(11) DEFAULT NULL,                                              
     mtaNum int(11) DEFAULT NULL,                                                    
     integratedNum int(11) DEFAULT NULL,                                             
     cpeNum int(11) DEFAULT NULL,                                                                 
     cpeInteractiveNum int(11) DEFAULT NULL,                                         
     cpeBroadbandNum int(11) DEFAULT NULL,                                           
     cpeMtaNum int(11) DEFAULT NULL,
     realtime timestamp NOT NULL,
     index ix_usernumpon_origin (entityId, ponIndex, realtime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;    

CREATE TABLE usernumhisponsummary (                                                    
     entityId bigint(20),       
     ponIndex bigint(20),                                                                                               
     onlineNumAvg int(11),onlineNumMax int(11), onlineNumMin int(11),                                                 
     otherNumAvg int(11), otherNumMax int(11),otherNumMin int(11),                                             
     offlineNumAvg int(11),  offlineNumMax int(11),offlineNumMin int(11),
     totalNumAvg int(11), totalNumMax int(11), totalNumMin int(11),
     interactiveNumAvg int(11), interactiveNumMax int(11), interactiveNumMin int(11),                                            
     broadbandNumAvg int(11),  broadbandNumMax int(11), broadbandNumMin int(11),                                             
     mtaNumAvg int(11),  mtaNumMax int(11), mtaNumMin int(11),                                              
     integratedNumAvg int(11),integratedNumMax int(11),   integratedNumMin int(11),                                                
     cpeNumAvg int(11),  cpeNumMax int(11),     cpeNumMin int(11),                                                       
     cpeInteractiveNumAvg int(11),  cpeInteractiveNumMax int(11),   cpeInteractiveNumMin int(11),                                          
     cpeBroadbandNumAvg int(11),  cpeBroadbandNumMax int(11), cpeBroadbandNumMin int(11),                                          
     cpeMtaNumAvg int(11),  cpeMtaNumMax int(11),  cpeMtaNumMin int(11),       
     summarizeTime timestamp NOT NULL,
     PRIMARY KEY (entityId,ponIndex, summarizeTime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usernumhisportsummaryorigin (                                                   
     entityId bigint(20) DEFAULT NULL, 
     ccIfIndex bigint(20) DEFAULT NULL,                                              
     portIfIndex bigint(20) DEFAULT NULL,                                            
     portType tinyint(4) DEFAULT NULL,                                       
     onlineNum int(11) DEFAULT NULL,                                                 
     otherNum int(11) DEFAULT NULL,                                                  
     offlineNum int(11) DEFAULT NULL,                                                
     interactiveNum int(11) DEFAULT NULL,                                            
     broadbandNum int(11) DEFAULT NULL,                                              
     mtaNum int(11) DEFAULT NULL,                                                    
     integratedNum int(11) DEFAULT NULL,                                             
     cpeNum int(11) DEFAULT NULL,                                                                 
     cpeInteractiveNum int(11) DEFAULT NULL,                                         
     cpeBroadbandNum int(11) DEFAULT NULL,                                           
     cpeMtaNum int(11) DEFAULT NULL,
     realtime timestamp NOT NULL,
     index ix_usernumport_origin (entityId, ccIfIndex, portIfIndex, portType, realtime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;    

CREATE TABLE usernumhisportsummary (                                                    
     entityId bigint(20),       
     ccIfIndex bigint(20) DEFAULT NULL,                                              
     portIfIndex bigint(20) DEFAULT NULL,                                            
     portType tinyint(4) DEFAULT NULL,                                                                                       
     onlineNumAvg int(11),onlineNumMax int(11), onlineNumMin int(11),                                                 
     otherNumAvg int(11), otherNumMax int(11),otherNumMin int(11),
     totalNumAvg int(11), totalNumMax int(11), totalNumMin int(11),
     offlineNumAvg int(11), offlineNumMax int(11),offlineNumMin int(11),                                              
     interactiveNumAvg int(11), interactiveNumMax int(11), interactiveNumMin int(11),                                            
     broadbandNumAvg int(11), broadbandNumMax int(11), broadbandNumMin int(11),                                             
     mtaNumAvg int(11), mtaNumMax int(11), mtaNumMin int(11),                                              
     integratedNumAvg int(11),integratedNumMax int(11), integratedNumMin int(11),                                                
     cpeNumAvg int(11), cpeNumMax int(11), cpeNumMin int(11),                                                       
     cpeInteractiveNumAvg int(11), cpeInteractiveNumMax int(11), cpeInteractiveNumMin int(11),                                          
     cpeBroadbandNumAvg int(11), cpeBroadbandNumMax int(11), cpeBroadbandNumMin int(11),                                          
     cpeMtaNumAvg int(11),  cpeMtaNumMax int(11),  cpeMtaNumMin int(11),       
     summarizeTime timestamp NOT NULL,
     PRIMARY KEY (entityId,ccIfIndex,portIfIndex,portType,summarizeTime)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/* -- version 2.4.1.0,build 2014-10-15,module cmc */
-- version 2.4.1.0,build 2014-10-21,module cmc
create table staticCpeNum (
cmId BIGINT,
cpeNum INT
);
/*-- version 2.4.1.0,build 2014-10-21,module cmc*/

-- version 2.4.5.0,build 2015-1-26,module cmc
alter table perfcmcflowquality add column portBandWidth  bigint(20);
alter table perfcmcflowqualitylast add column portBandWidth  bigint(20);
/*-- version 2.4.5.0,build 2015-1-26,module cmc*/

-- version 2.4.8.0,build 2015-03-23,module cmc
alter table SpectrumiiVideo  modify column videoName varchar(256);
/*-- version 2.4.8.0,build 2015-03-23,module cmc*/

-- version 2.5.2.0,build 2015-4-20,module cmc
CREATE TABLE cmlistboss(
	cmMac varchar(20) NOT NULL,
	userId VARCHAR(50),
	userName VARCHAR(100),
	userAddr VARCHAR(200),
	userPhoneNo VARCHAR(30),
	packageType VARCHAR(50),
	effectTime TIMESTAMP,
	expirationTime TIMESTAMP,
	configFile VARCHAR(100),
	extension VARCHAR(100),
	PRIMARY KEY (cmMac)
);
/*-- version 2.5.2.0,build 2015-4-20,module cmc*/

-- version 2.4.11.0,build 2015-05-15,module cmc
create index ix_staticcpenum_cmId on staticcpenum(cmId);
create index id_cmcport_cmcId_ifIndex on cmcportattribute(cmcId,ifIndex);
/*-- version 2.4.11.0,build 2015-05-15,module cmc*/

-- version 2.4.11.0,build 2015-6-3,module cmc
ALTER TABLE perfcmcflowquality ADD PRIMARY KEY (cmcId,channelIndex,collectTime);
ALTER TABLE perfcmclinkquality ADD PRIMARY KEY (cmcId,portIndex,collectTime);
ALTER TABLE perfcmcopreceiverinputpowerhis ADD PRIMARY KEY (cmcId,inputIndex,collectTime);
/*-- version 2.4.11.0,build 2015-6-3,module cmc*/

-- version 2.4.11.0,build 2015-6-4,module cmc
ALTER TABLE cmattribute ADD displaystatus tinyint not null default '0';
ALTER TABLE cmattribute ADD displayIp varchar(20);
ALTER TABLE cmattribute ADD INDEX cm_mac(statusMacAddress);
ALTER TABLE cmattribute ADD INDEX cm_upsnr(StatusSignalNoise);
ALTER TABLE cmattribute ADD INDEX cm_status(displaystatus);
ALTER TABLE cmattribute ADD INDEX cm_ip(displayIp);
/*-- version 2.4.11.0,build 2015-6-4,module cmc*/

-- version 2.6.0.0,build 2015-6-23,module cmc
drop table IF EXISTS cmlistboss;
drop table IF EXISTS cmbossinfo;
CREATE TABLE cmbossinfo(
	cmMac varchar(20) NOT NULL,
	userId VARCHAR(50),
	userName VARCHAR(100),
	userAddr VARCHAR(200),
	userPhoneNo VARCHAR(30),
	fiberNode VARCHAR(50),
	fiberName VARCHAR(50),
	expDate varchar(15),
	effDate varchar(15),
	offerName VARCHAR(255),
	configFile VARCHAR(100),
	stbMac varchar(20),
	extension VARCHAR(100),
	PRIMARY KEY (cmMac)
);
ALTER TABLE `cmflaphis` ADD PRIMARY KEY (`cmMac`, `collectTime`);
/*-- version 2.6.0.0,build 2015-6-23,module cmc*/

-- version 2.6.1.1,build 2015-7-27,module cmc
ALTER TABLE cmcdocsisconfig DROP PRIMARY KEY ,ADD PRIMARY KEY ( `entityId` ,`ifIndex`);
/*-- version 2.6.1.1,build 2015-7-27,module cmc*/


-- version 2.6.1.1,build 2015-08-01,module cmc
alter table cmcentityrelation drop column virtualNetId;
alter table cmcentityrelation add constraint FK_ENTITY_CMC foreign key(cmcId) references entity(entityId) on delete cascade on update cascade;
/*-- version 2.6.1.1,build 2015-08-01,module cmc */

-- version 2.6.4.0,build 2015-10-10,module server
ALTER TABLE cmbossinfo ADD COLUMN classification VARCHAR(50);
ALTER TABLE cmbossinfo ADD COLUMN importTime bigint;
/* -- version 2.6.4.0,build 2015-10-10,module server  */ 

-- version 2.6.9.0,build 2016-5-10,module server
create table CmcEqamProgram
(
cmcId bigint(20) not null,
portId bigint(20) not null,
mpegVideoSessionIndex bigint(20),
mpegVideoSessionBitRate bigint(20),
mpegVideoSessionID varchar(64)
);

create table CmcEqamStatus
(
cmcId bigint(20) not null,
portId bigint(20) not null,
ifIndex bigint(20),
ifDescr varchar(256),
ifOperStatus int(2),
qamChannelFrequency int(6),
qamChannelModulationFormat int(4),
qamChannelInterleaverMode  int(4),
qamChannelPower int(6),
qamChannelSquelch int(4),
qamChannelContWaveMode int(4),
qamChannelAnnexMode int(4),
qamChannelCommonOutputBw int(10),
qamChannelCommonUtilization int(4),
qamChannelSymbolRate int(10)
);
create table ProgramIn
(
cmcId bigint(20) not null,
portId bigint(20),
sessionId int(10),
mpegInputUdpOriginationIndex bigint(20),
mpegInputUdpOriginationId int(4),
mpegInputUdpOriginationSrcInetAddr varchar(64),
mpegInputUdpOriginationDestInetAddr varchar(64),
mpegInputUdpOriginationDestPort int(6),
mpegInputUdpOriginationActive int(4),
mpegInputUdpOriginationPacketsDetected int(10)
);
create table ProgramOut
(
cmcId bigint(20) not null,
portId bigint(20),
sessionId int(10),
mpegOutputTSIndex bigint(20),
mpegOutputProgIndex bigint(20),
mpegOutputProgNo int(10),
mpegOutputProgPmtVersion varchar(64),
mpegOutputProgPmtPid int(10),
mpegOutputProgPcrPid int(10),
mpegOutputProgNumElems int(10)
);
/* -- version 2.6.9.0,build 2016-5-10,module server  */ 

-- version 2.6.9.1,build 2016-6-22,module server
ALTER TABLE cmcattribute ADD COLUMN topCcmtsSysDorType VARCHAR(32);
/* -- version 2.6.9.1,build 2016-6-22,module server  */

-- version 2.6.9.4,build 2016-7-29,module server
alter table cmcportattribute modify ifLastChange bigint(20);
alter table cmcsystemtimeconfig modify topCcmtsSysTime bigint(20);
/* -- version 2.6.9.4,build 2016-7-29,module server  */ 

-- version 2.6.9.5,build 2016-9-14,module server
alter table docsIf3CmtsCmUsStatus modify cmUsStatusUnerroreds bigint(20);
alter table docsIf3CmtsCmUsStatus modify cmUsStatusCorrecteds bigint(20);
/* -- version 2.6.9.5,build 2016-9-14,module server  */ 

-- version 2.7.1.0,build 2016-6-28,module cmc
CREATE TABLE `cm3signal` (
  `cmId` bigint(20) NOT NULL,
  `channelId` int(2) NOT NULL,
  `channelType` tinyint(1) NOT NULL,
  `downChannelSnr` varchar(10) DEFAULT NULL,
  `downChannelTx` varchar(10) DEFAULT NULL,
  `upChannelTx` varchar(10) DEFAULT NULL,
  `upChannelSnr` varchar(10) DEFAULT NULL,
  `collectTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cmId`,`channelId`,`channelType`)
);
ALTER TABLE cmsignal ADD COLUMN upChannelSnr varchar(10);
/* -- version 2.7.1.0,build 2016-6-28,module cmc  */

-- version 2.7.2.0,build 2016-8-6,module cmc
alter table cmAttribute modify column displayIp varchar (50);
alter table cmAttribute modify column StatusIpAddress varchar (50);
alter table cmattribute modify column StatusInetAddress varchar(50);
alter table cmcpe modify column topCmCpeIpAddress varchar (50);
/* -- version 2.7.2.0,build 2016-8-6,module cmc  */

-- version 2.7.5.0,build 2016-11-03,module cmc
CREATE TABLE `cmServiceType` (
  `fileName` varchar(128) NOT NULL,
  `serviceType` varchar(64) NOT NULL,
  PRIMARY KEY (`fileName`)
);
ALTER TABLE cmattribute add column fileName varchar(128) DEFAULT '';
ALTER TABLE cmattribute add column v6FileName varchar(128) DEFAULT '';
CREATE TABLE `cmFileNameChangeLog` (
  `cmId` bigint(20) NOT NULL,
  `cmMac` varchar(20) NOT NULL,
  `oldFileName` varchar(128) NOT NULL,
  `newFileName` varchar(128) NOT NULL,
  `changeTime`  timestamp
);
/* -- version 2.7.5.0,build 2016-11-03,module cmc  */

-- version 2.7.5.0,build 2016-9-14,module cmc
create table TopCcmtsDorDCPower 
(
   cmcId                  bigint(20) NOT NULL,
   dcPowerIndex           bigint(20) NOT NULL,
   dcPowerVoltage         int,
   primary key(cmcId,dcPowerIndex),
   CONSTRAINT FK_cmcAttr_TopCcmtsDorDCPower FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table TopCcmtsDorLinePower 
(
   cmcId                	bigint(20) NOT NULL,
   linePowerIndex           bigint(20) NOT NULL,
   linePowerVoltage1        int,
   primary key(cmcId,linePowerIndex),
   CONSTRAINT FK_cmcAttr_TopCcmtsDorLinePower FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table TopCcmtsOpRxOutput 
(
   cmcId                bigint(20) NOT NULL,
   outputIndex          bigint(20) NOT NULL,
   outputControl        int,
   configurationOutputRFlevelatt   int,
   configurationAGCRg          int,
   primary key(cmcId,outputIndex),
   CONSTRAINT FK_cmcAttr_TopCcmtsOpRxOutput FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table TopCcmtsDorABSwitch 
(
   cmcId                  bigint(20) NOT NULL,
   abSwitchIndex          bigint(20) NOT NULL,
   abSwitchState          int,
   primary key(cmcId,abSwitchIndex),
   CONSTRAINT FK_cmcAttr_TopCcmtsDorABSwitch FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TopCcmtsOpRxInput
(
    cmcId                bigint(20) NOT NULL,
    inputIndex           bigint(20) NOT NULL,
    inputPower           int,
    primary key(cmcId,inputIndex),
    CONSTRAINT FK_TopCcmtsOpRxInput FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TopCcmtsDorRFPort(
    cmcId                bigint(20) NOT NULL,
    rfPortIndex          bigint(20) NOT NULL,
    rfPortOutputRFLevel  int,
    PRIMARY KEY(cmcId,rfPortIndex),
    CONSTRAINT FK_TopCcmtsDorRFPort FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TopCcmtsDorChannelNum(
    cmcId                bigint(20) NOT NULL,
    channelNumIndex      bigint(20) NOT NULL,
    channelNum           int,
    PRIMARY KEY(cmcId,channelNumIndex),
    CONSTRAINT FK_TopCcmtsDorChannelNum FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TopCcmtsDorDevParams(
    cmcId                bigint(20) NOT NULL,
    devIndex             bigint(20) NOT NULL,
    platSN               varchar(30),
    frxNum               int,
    optNodeTemp               int,
    fwdEQ0                    int,
    fwdATT0                   int,
    revEQ                     int,
    rtxState                  int,
    rtxLaserPower             int,
    revATTUS                  int,
    revATTRTX                 int,
    rtxLaserCurrent           int,
    catvInLevel               int,
    catvInputState            int,
    ftxOptPower               int,
    ftxLaserCurrent           int,
    PRIMARY KEY(cmcId,devIndex),
    CONSTRAINT FK_TopCcmtsDorDevParams FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TopCcmtsDorFwdAtt(
    cmcId                bigint(20) NOT NULL,
    fwdAttIndex          bigint(20) NOT NULL,
    fwdAtt               int,
    PRIMARY KEY(cmcId,fwdAttIndex),
    CONSTRAINT FK_TopCcmtsDorFwdAtt FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TopCcmtsDorFwdEq(
    cmcId                bigint(20) NOT NULL,
    fwdEqIndex           bigint(20) NOT NULL,
    fwdEq                int,
    PRIMARY KEY(cmcId,fwdEqIndex),
    CONSTRAINT FK_TopCcmtsDorFwdEq FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TopCcmtsDorRevAtt(
    cmcId                 bigint(20) NOT NULL,
    revAttIndex           bigint(20) NOT NULL,
    revAtt                int,
    PRIMARY KEY(cmcId,revAttIndex),
    CONSTRAINT FK_TopCcmtsDorRevAtt FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TopCcmtsDorRRXOptPow(
    cmcId                 bigint(20) NOT NULL,
    rrxOptPowIndex        bigint(20) NOT NULL,
    rrxOptPow             int,
    PRIMARY KEY(cmcId,rrxOptPowIndex),
    CONSTRAINT FK_TopCcmtsDorRRXOptPow FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.7.5.0,build 2016-9-14,module cmc  */

-- version 2.8.0.0,build 2016-10-8,module cmc
CREATE TABLE cmcsyslogrecordeventlevelII(
    entityId bigint(20) NOT NULL,
    evPriority int NOT NULL,
    evReporting varchar(10) NOT NULL,
    PRIMARY KEY (entityId,evPriority),
    CONSTRAINT FK_ENTITY_SYSLOGEVTLVLII FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.8.0.0,build 2016-10-8,module cmc  */

-- version 2.8.0.0,build 2016-10-17,module cmc
CREATE TABLE txPowerLimit(
    entityId bigint(20) NOT NULL,
    cmcIndex bigint(20) NOT NULL,
    channelNum int NOT NULL,
    maxPowerTenthdBmV int NOT NULL,
    minPowerTenthdBmV int NOT NULL,
    PRIMARY KEY (entityId,cmcIndex,channelNum)
);
/* -- version 2.8.0.0,build 2016-10-17,module cmc  */

-- version 2.8.0.0,build 2016-11-8 16:21:00,module cmc
ALTER TABLE cmattribute ADD partialUpChannels varchar(40);
ALTER TABLE cmattribute ADD partialDownChannels varchar(40);
/*-- version 2.8.0.0,build 2016-11-8 16:21:00,module cmc*/

-- version 2.9.0.0,build 2017-3-14 9:49:00,module cmc
ALTER TABLE cmcSyslogServerEntry modify topCcmtsSyslogServerIpAddr varchar(255);
/*-- version 2.9.0.0,build 2017-3-14 9:49:00,module cmc*/

-- version 2.9.0.8,build 2017-5-27 17:40:00,module cmc
CREATE TABLE cmcclearcmtime(
    deviceId bigint(20) NOT NULL,
    clearTime int(6) DEFAULT 0,
    PRIMARY KEY (deviceId),
    CONSTRAINT FK_CMCCLEARTIME FOREIGN KEY (deviceId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 2.9.0.8,build 2017-5-27 17:40:00,module cmc*/

-- version 2.9.1.7,build 2017-8-14 9:49:00,module cmc
ALTER TABLE cmccmrelation ADD UNIQUE(`mac`);
/*-- version 2.9.1.7,build 2017-8-14 9:49:00,module cmc*/

-- version 2.9.1.7,build 2017-8-7,module cmc
ALTER TABLE cmbossinfo modify column userAddr varchar(256);
/*-- version 2.9.1.7,build 2017-8-7,module cmc*/

-- version 2.9.1.7,build 2017-08-10 17:30:00,module cmc
CREATE TABLE cmtsinfosummary(
    cmcId bigint(20) NOT NULL,
    collectTime timestamp NOT NULL,
    upSnrOutRange DOUBLE,
    downSnrOutRange DOUBLE,
    upTxOutRange DOUBLE,
    downReOutRange DOUBLE,
    upSnrAvg DOUBLE,
    downSnrAvg DOUBLE,
    upTxAvg DOUBLE,
    downReAvg DOUBLE,
    onlineCmNum DOUBLE,
    totalCmNum DOUBLE,
    PRIMARY KEY (cmcId,collectTime)
);
CREATE TABLE cmtsinfosummarylast(
    cmcId bigint(20) NOT NULL,
    collectTime timestamp NOT NULL,
    upSnrOutRange DOUBLE,
    downSnrOutRange DOUBLE,
    upTxOutRange DOUBLE,
    downReOutRange DOUBLE,
    upSnrAvg DOUBLE,
    downSnrAvg DOUBLE,
    upTxAvg DOUBLE,
    downReAvg DOUBLE,
    onlineCmNum DOUBLE,
    totalCmNum DOUBLE,
    PRIMARY KEY (cmcId),
    CONSTRAINT FK_CMTSINFOSUMMARYLAST FOREIGN KEY (cmcId) REFERENCES cmcattribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE cmtsinfothreshold(
    upSnrMin int(11),
    upSnrMax int(11),
    downSnrMin int(11),
    downSnrMax int(11),
    upPowerMin int(11),
    upPowerMax int(11),
    downPowerMin int(11),
    downPowerMax int(11)
);
/*-- version 2.9.1.7,build 2017-08-10 17:30:00,module cmc*/

-- version 2.9.1.14,build 2017-10-24,module cmc
create table perfCmcDorOptNodeTempquality(
    cmcId bigint(20) not null,
    cmcIndex bigint(20) not null,
    dorOptNodeTemp int(10),
    collectTime timestamp,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfCmcDorOptNodeTempquality FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcDorOptNodeTempqualitylast(
	cmcId bigint(20) not null,
	cmcIndex bigint(20) not null,
	dorOptNodeTemp int(10),
	collectTime timestamp,
	primary key(cmcId,cmcIndex),
	CONSTRAINT FK_perfCmcDorOptNodeTempqualityLast FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcDorLinePowerquality(
    cmcId bigint(20) not null,
    cmcIndex bigint(20) not null,
    dorLinePower int(10),
    collectTime timestamp,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfCmcDorLinePowerquality FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmcDorLinePowerqualitylast(
	cmcId bigint(20) not null,
	cmcIndex bigint(20) not null,
	dorLinePower int(10),
	collectTime timestamp,
	primary key(cmcId,cmcIndex),
	CONSTRAINT FK_perfCmcDorLinePowerqualityLast FOREIGN KEY (cmcId) REFERENCES cmcAttribute (cmcId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 2.9.1.14,build 2017-10-24,module cmc*/

-- version 2.10.0.13,build 2018-09-04,module cmc
alter table cmceqamprogram add primary key(cmcId,portId,mpegVideoSessionIndex); 
alter table ProgramIn add primary key(cmcId,portId,sessionId,mpegInputUdpOriginationIndex,mpegInputUdpOriginationId); 
alter table programout add primary key(cmcId,portId,sessionId,mpegOutputTSIndex,mpegOutputProgIndex);
/*-- version 2.10.0.13,build 2018-09-04,module cmc*/
-- version 2.10.0.1,build 2018-01-03 11:30:00,module cmc
ALTER TABLE cmcattribute ADD topCcmtsEqamSupport int(1) NOT NULL DEFAULT 2;
/*-- version 2.10.0.1,build 2018-01-03 11:30:00,module cmc*/
