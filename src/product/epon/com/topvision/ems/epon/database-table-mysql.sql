-- version 1.0.0,build 2011-9-22,module epon
/*==============================================================*/
/* Table: OltAttribute                                          */
/*==============================================================*/
create table OltAttribute
(
   entityId             bigint(20) not null comment '系统内id 自增长',
   oltName              varchar(255) comment 'OLT名称',
   oltType              varchar(255) comment 'OLT类型',
   oltDeviceUpTime      bigint(20) comment 'OLT在线时长',
   oltDeviceNumOfTotalServiceSlot int(2) comment '业务板个数',
   oltDeviceNumOfTotalPowerSlot int(2) comment '电源板卡个数',
   oltDeviceNumOfTotalFanSlot int(2) comment '风扇板卡个数',
   oltDeviceStyle       int(1) comment '设备形态',
   inbandIpAddress      bigint(20),
   inbandIpSubnetMask   bigint(20),
   inbandPortIndex      bigint(20),
   outbandIpSubnetMask  bigint(20),
   outbandIpGateway     bigint(20),
   outbandIpAddress     bigint(20),
   inbandIpGateway      bigint(20),
   inbandMacAddress     bigint(20),
   outbandMacAddress    bigint(20),
   inbandVlanId         int(4),
   systemOUI            varchar(255),
   vendorName           varchar(255),
   topSysSnmpHostIp     bigint(20),
   topSysSnmpHostIpMask bigint(20),
   topSysSnmpVersion    int(1),
   topSysReadCommunity  varchar(63),
   topSysWriteCommunity varchar(63),
   topSysSnmpPort       int(5),
   topSysInBandMaxBW    int(6) comment 'Description: Max bandwidth, unit:kbps  Default Value: 500',
   topSysOltRackNum     int(3),
   topSysOltFrameNum    int(3),
   onuAuthenticationPolicy int(1),
   sniMacAddrTableAgingTime int(8),
   primary key (entityId)
);

alter table OltAttribute comment 'OLT设备属性表';

/*==============================================================*/
/* Table: OltAuthentication                                     */
/*==============================================================*/
create table OltAuthentication
(
   authId               bigint(20) auto_increment not null comment '系统内id 自增长',
   ponId                bigint(20),
   onuIndex             bigint(20) comment 'mib可用的index',
   authType             int(1) comment '取值 1 mac认证  2 sn认证',
   authAction           int(1) comment '取值 1 accept  2 reject',
   onuAuthenMacAddress  bigint(15),
   onuSnMode            int(1) comment 'logicSnPlusPwd(1), logicSnOnly(2)',
   topOnuAuthLogicSn    varchar(24) comment 'llid认证的sn',
   topOnuAuthPassword   varchar(255) comment 'llid认证需要的密码',
   entityId             bigint(20),
   primary key (authId)
);

alter table OltAuthentication comment '该表将MAC认证和SN认证都整合在了一起，一行只有一个认证方式，只有对应的认证方式的参数是有效的，例如认证方式为mac ';

/*==============================================================*/
/* Table: OltDhcpBaseConfig                                     */
/*==============================================================*/
create table OltDhcpBaseConfig
(
   entityId             bigint(20) not null,
   topOltPPPOEPlusEnable tinyint(1),
   topOltDHCPRelayMode  tinyint(1),
   topOltDHCPDyncIPMACBind tinyint(1),
   primary key (entityId)
);

alter table OltDhcpBaseConfig comment 'DHCP全局配置表';

/*==============================================================*/
/* Table: OltDhcpIpMacDynamic                                   */
/*==============================================================*/
create table OltDhcpIpMacDynamic
(
   entityId             bigint(20),
   topOltDHCPDynIpAddr  bigint(10),
   topOltDHCPDynMacAddr bigint(10),
   topOltDHCPDynOnuMacAddr bigint(10),
   topOltDHCPDynIpMacIdx int not null,
   primary key (topOltDHCPDynIpMacIdx)
);

alter table OltDhcpIpMacDynamic comment 'OltDhcp动态绑定信息表';

/*==============================================================*/
/* Table: OltDhcpIpMacStatic                                    */
/*==============================================================*/
create table OltDhcpIpMacStatic
(
   entityId             bigint(20),
   topOltDHCPIpMacIdx   int not null,
   topOltDHCPIpAddr     bigint(10),
   topOltDHCPMacAddr    bigint(10),
   topOltDHCPOnuMacAddr bigint(10)
);

alter table OltDhcpIpMacStatic comment 'OltDhcp静态绑定全局配置表';

/*==============================================================*/
/* Table: OltFanAttribute                                       */
/*==============================================================*/
create table OltFanAttribute
(
   fanCardId            bigint(20) not null,
   fanCardName          varchar(32) comment '风扇板名称',
   topSysFanSpeedControl int(1) comment ' fan speed level autoSpeed(1), lowSpeed(2), middleSpeed(3), highSpeed(4)',
   entityId             bigint(20),
   primary key (fanCardId)
);

alter table OltFanAttribute comment '风扇属性表';

/*==============================================================*/
/* Table: OltFanRelation                                        */
/*==============================================================*/
create table OltFanRelation
(
   fanCardId            bigint(20) auto_increment not null comment '系统内id 自增长',
   entityId             bigint(20)  not null,
   fanCardIndex         bigint(40) comment 'mib可用的index',
   primary key (fanCardId)
);

alter table OltFanRelation comment '风扇关系表';

/*==============================================================*/
/* Table: OltFanStatus                                          */
/*==============================================================*/
create table OltFanStatus
(
   fanCardId            bigint(20) not null,
   topSysFanSpeed       int(5) comment '风扇转速',
   fanCardAlarmStatus   int(1) comment '管理状态',
   fanCardPresenceStatus int(1) comment '插入状态',
   fanCardOperationStatus int(1) comment '操作状态',
   changeTime           timestamp comment '更新时间',
   entityId             bigint(20),
   primary key (fanCardId)
);

alter table OltFanStatus comment '风扇实时状态表';

/*==============================================================*/
/* Table: OltOnuAttribute                                       */
/*==============================================================*/
create table OltOnuAttribute
(
   onuId                bigint(20) not null,
   onuName              varchar(255) comment 'onu名称',
   onuType              int(3) comment 'onu类型  将扩展成为具体类型',
   onuMacAddress        bigint(20),
   onuOperationStatus   int(1) comment '操作状态',
   onuAdminStatus       int(1) comment '管理状态',
   onuChipVendor        varchar(255) comment '芯片生产商',
   onuChipType          varchar(255) comment '芯片类型',
   onuChipVersion       varchar(255) comment '芯片版本',
   onuSoftwareVersion   varchar(255) comment '软件版本',
   onuFirmwareVersion   varchar(255) comment '固件版本',
   onuTestDistance      int(5) comment 'onu测距 距离 Units: Meter',
   onuLlidId            int(3) comment 'llid',
   onuTimeSinceLastRegister bigint(20) comment '注册到现在的秒数',
   onuGePortNum         int(2),
   onuGePortBitMap      text,
   onuFePortNum         int(2),
   onuFePortBitMap      text,
   onuQueueNumUpLink    int(2),
   onuMaxQueueNumUpLink int(2),
   onuQueueNumDownLink  int(2),
   onuMaxQueueNumDownLink int(2),
   onuFecEnable         tinyint,
   onuEncryptMode       tinyint,
   onuEncryptKeyExchangeTime bigint(20),
   CapDeregister  tinyint,
   CapAddrMaxQuantity int(2),
   PonPerfStats15minuteEnable tinyint,
   PonPerfStats24hourEnable tinyint,
   TemperatureDetectEnable tinyint,
   CurrentTemperature int,
   onuIsolationEnable int,
   topOnuAction int,
   topOnuHardwareVersion varchar(255),
   RstpBridgeMode tinyint,
   VoipEnable     tinyint,
   CatvEnable     tinyint,
   entityId             bigint(20),
   onuIcon              varchar(255),
   changeTime           timestamp comment '更新时间',
   primary key (onuId)
);

alter table OltOnuAttribute comment 'Onu属性表';

/*==============================================================*/
/* Table: OltOnuPonAttribute                                    */
/*==============================================================*/
create table OltOnuPonAttribute
(
   onuPonId             bigint(20) not null,
   onuReceivedOpticalPower int(4) comment '接收光功率',
   onuTramsmittedOpticalPower int(4) comment '发送光功率',
   onuBiasCurrent       int(4) comment '偏置电流',
   onuWorkingVoltage    int(4) comment '工作电压',
   onuWorkingTemperature int(3) comment '工作温度',
   entityId             bigint(20),
   primary key (onuPonId)
);

alter table OltOnuPonAttribute comment 'Onu的Pon口的光传输属性表';

/*==============================================================*/
/* Table: OltOnuPonRelation                                     */
/*==============================================================*/
create table OltOnuPonRelation
(
   onuPonId             bigint(20) auto_increment not null comment '系统内id 自增长',
   onuId                bigint(20) not null,
   onuPonIndex          bigint(40) comment 'mib可用的index',
   entityId             bigint(20),
   constraint uk_onupon_relation unique(onuPonIndex, entityId),
   primary key (onuPonId)
);

alter table OltOnuPonRelation comment 'Onu的Pon口关系表';

/*==============================================================*/
/* Table: OltOnuRelation                                        */
/* Modify by Rod On Authority                                   */
/*==============================================================*/
create table if not exists OltOnuRelation
(
   onuId                bigint(20) not null comment '系统内id 自增长',
   ponId                bigint(20) not null,
   onuIndex             bigint(40) not null comment 'mib可用的index',
   entityId             bigint(20),
   constraint uk_onu_relation unique(onuIndex, entityId),
   primary key (onuId)
);

alter table OltOnuRelation comment 'ONU关系表';

/*==============================================================*/
/* Table: OltOperType                                           */
/*==============================================================*/
create table OltOperType
(
   operType             int(4) not null,
   operDesc             varchar(255),
   primary key (operType)
);

alter table OltOperType comment 'OLT操作类型';

/*==============================================================*/
/* Table: OltOperatorHistory                                    */
/*==============================================================*/
create table OltOperatorHistory
(
   operId               bigint(20) not null,
   operType             int(4),
   userId               bigint(20),
   status               int(1),
   operTime             timestamp,
   primary key (operId)
);

alter table OltOperatorHistory comment 'OLT设备历史操作表';

/*==============================================================*/
/* Table: OltPonAttribute                                       */
/*==============================================================*/
create table OltPonAttribute
(
   ponId                bigint(20) not null,
   ponPortType          int(1) comment 'PON端口类型 ge-epon(1), tenge-epon(2), gpon(3), other(4)',
   ponOperationStatus   int(1) comment '操作状态',
   ponPortAdminStatus   int(1) comment '管理状态',
   ponPortMaxOnuNumSupport int(3) comment 'pon口下最大支持onu个数',
   ponPortUpOnuNum      int(3) comment 'pon口下在线onu个数',
   ponPortEncryptMode   int(1) comment '加密模式 aes128(1), ctcTripleChurning(2), other(3) ',
   ponPortEncryptKeyExchangeTime int(4) comment '密锁交换时间',
   ponPortIsolationEnable tinyint comment '隔离使能',
   maxDsBandwidth       int(10) comment '最大下行带宽',
   actualDsBandwidthInUse int(10) comment '实际下行使用带宽',
   remainDsBandwidth    int(10) comment '剩余下行使用带宽',
   perfStats15minuteEnable tinyint comment '15分钟性能统计使能',
   perfStats24hourEnable tinyint comment '24小时性能统计使能',
   ponPortMacAddrLearnMaxNum int(5) comment 'Mac地址学习最大个数',
   maxUsBandwidth       int(10) comment '最大上行带宽',
   actualUsBandwidthInUse int(10) comment '实际上行使用带宽',
   remainUsBandwidth    int(10) comment '剩余上行使用带宽',
   entityId             bigint(20),
   primary key (ponId)
);

alter table OltPonAttribute comment 'PON口属性表';

/*==============================================================*/
/* Table: OltPonQinQ                                            */
/*==============================================================*/
create table OltPonQinQ
(
   ponIndex             bigint(20),
   pqStartVlanId        int(4) not null,
   pqEndVlanId          int(4) not null,
   pqSVlanId            int(4) not null,
   pqSTagCosDetermine   tinyint,
   pqSTagCosNewValue    tinyint,
   ponId                bigint(20) not null,
   entityId             bigint(20),
   primary key (pqStartVlanId, pqEndVlanId, pqSVlanId, ponId)
);

alter table OltPonQinQ comment 'OltPonQinQ';

/*==============================================================*/
/* Table: OltPonRelation                                        */
/*==============================================================*/
create table OltPonRelation
(
   ponId                bigint(20) auto_increment not null comment '系统内id 自增长',
   slotId               bigint(20) not null,
   ponIndex             bigint(40) not null comment 'mib可用的index',
   constraint uk_pon_relation unique(ponIndex, entityId),
   entityId             bigint(20),
   primary key (ponId)
);

alter table OltPonRelation comment 'PON口关系表';

/*==============================================================*/
/* Table: OltPonStormInfo                                       */
/*==============================================================*/
create table OltPonStormInfo
(
   ponId                bigint(20) not null,
   unicastStormEnable   tinyint,
   unicastStormInPacketRate int,
   unicastStormOutPacketRate int,
   multicastStormEnable tinyint,
   multicastStormInPacketRate int,
   multicastStormOutPacketRate int,
   broadcastStormEnable tinyint,
   broadcastStormInPacketRate int,
   broadcastStormOutPacketRate int,
   entityId             bigint(20),
   primary key (ponId)
);

alter table OltPonStormInfo comment 'Pon口广播风暴抑制表';

/*==============================================================*/
/* Table: OltPortMirrorConfig                                   */
/*==============================================================*/
create table OltPortMirrorConfig
(
   entityId             bigint(20) not null,
   portMirrorGroupIndex int not null,
   portMirrorGroupName  text,
   portMirrorGroupDstPortList text,
   portMirrorGroupSrcInPortList text,
   portMirrorGroupSrcOutPortList text,
   primary key (entityId, portMirrorGroupIndex)
);

alter table OltPortMirrorConfig comment '端口Mirror配置表';

/*==============================================================*/
/* Table: OltPortTrunkConfig                                    */
/*==============================================================*/
create table OltPortTrunkConfig
(
   entityId             bigint(20) not null,
   portType             varchar(5),
   portTrunkGroupConfigName varchar(255),
   portTrunkGroupConfigMember text,
   portTrunkGroupConfigPolicy tinyint,
   portTrunkGroupConfigIndex int not null,
   portTrunkGroupOperationStatus int,
   portTrunkGroupActualSpeed int,
   portTrunkGroupAdminStatus int,
   primary key (entityId, portTrunkGroupConfigIndex)
);

alter table OltPortTrunkConfig comment '端口Trunk配置表';

/*==============================================================*/
/* Table: OltPortVlan                                           */
/*==============================================================*/
create table OltPortVlan
(
   entityId             bigint(20),
   sniId                bigint(20) not null comment '系统内id 自增长',
   sniIndex             bigint(20),
   vlanTagTpid          varchar(10),
   vlanTagCfi           tinyint,
   vlanTagPriority      int(4),
   vlanPVid             int(4),
   vlanMode             tinyint,
   primary key (sniId)
);

alter table OltPortVlan comment 'OltPortVlan';

/*==============================================================*/
/* Table: OltPowerAttribute                                     */
/*==============================================================*/
create table OltPowerAttribute
(
   powerCardId          bigint(20) not null,
   topSysPowerSupplyType int comment 'powerDC(1), powerAC(2)',
   topSysPowerSupplyACVoltage int(3) comment 'AC power voltage',
   powerCardName        varchar(32) comment '电源板名称',
   entityId             bigint(20),
   primary key (powerCardId)
);

alter table OltPowerAttribute comment '电源属性表';

/*==============================================================*/
/* Table: OltPowerRelation                                      */
/*==============================================================*/
create table OltPowerRelation
(
   powerCardId          bigint(20) auto_increment not null comment '系统内id 自增长',
   entityId             bigint(20)  not null,
   powerCardIndex       bigint(40) comment 'mib可用的index',
   primary key (powerCardId)
);

alter table OltPowerRelation comment '电源关系表';

/*==============================================================*/
/* Table: OltPowerStatus                                        */
/*==============================================================*/
create table OltPowerStatus
(
   powerCardId          bigint(20) not null,
   powerCardAlarmStatus int(1) comment '电源板告警状态 critical(0), major(1), minor(2), warning(3)',
   topSysPowerSupplyACTemperature int(3) comment '电源温度',
   powerCardPresenceStatus int(1) comment '插入状态',
   changeTime           timestamp comment '更新时间',
   entityId             bigint(20),
   primary key (powerCardId)
);

alter table OltPowerStatus comment '电源实时状态表';

/*==============================================================*/
/* Table: OltSlotAttribute                                      */
/*==============================================================*/
create table OltSlotAttribute
(
   slotId               bigint(20) not null,
   topSysBdPreConfigType int(3) comment '期望板卡状态',
   topSysBdActualType   int(3) comment '实际板类型',
   topSysBdTempDetectEnable int(1) comment '板卡温度探测使能',
   bOperationStatus     int(1) comment '板卡服务状态',
   bAttribute           int(1) comment '板卡属性 - 用于描述主控卡和交换卡在主备冗余方式下的状态
               active(1), standby(2), standalone(3), notApplicable(4)',
   bHardwareVersion     varchar(255) comment '硬件版本',
   bFirmwareVersion     varchar(255) comment '固件版本',
   bSoftwareVersion     varchar(255) comment '软件版本',
   bUpTime              int(10) comment '板卡在线时间',
   bSerialNumber        varchar(255) comment '板卡序列号',
   bName                varchar(32) comment '板卡名称',
   bPresenceStatus      int(1) comment '插入状态',
   bAdminStatus         int(1),
   entityId             bigint(20),
   primary key (slotId)
);

alter table OltSlotAttribute comment 'SLOT属性表';

/*==============================================================*/
/* Table: OltSlotRelation                                       */
/*==============================================================*/
create table OltSlotRelation
(
   slotId               bigint(20) auto_increment not null comment '系统内id 自增长',
   entityId             bigint(20)  not null,
   slotIndex            bigint(40) comment 'mib可用的index',
   constraint uk_slot_relation unique(slotIndex, entityId),
   primary key (slotId)
);

alter table OltSlotRelation comment '槽位关系表  程序保证设备id和slotindex的唯一性';

/*==============================================================*/
/* Table: OltSlotStatus                                         */
/*==============================================================*/
create table OltSlotStatus
(
   slotId               bigint(20) not null,
   topSysBdCPUUseRatio  int(3) comment '板卡cpu利用率',
   topSysBdlMemSize     int(5) comment '板卡内存大小  MB',
   topSysBdFreeMemSize  int(5) comment '空闲内存大小 byte',
   topTotalFlashOctets  int(10) comment 'The total size of the flash in MCU or LPU,in units of kilo byte.',
   topSysBdFreeFlashOctets int(10) comment '剩余flash容量kilo byte',
   topSysBdCurrentTemperature int(3) comment '板卡温度',
   topSysBdLampStatus   int(10) comment 'led灯状态',
   bAlarmStatus         tinyint comment '板卡告警状态 critical(0), major(1), minor(2), warning(3)',
   changeTime           timestamp comment '更新时间',
   entityId             bigint(20),
   primary key (slotId)
);

alter table OltSlotStatus comment 'SLOT实时状态表';

/*==============================================================*/
/* Table: OltSniAttribute                                       */
/*==============================================================*/
create table OltSniAttribute
(
   sniId                bigint(20) not null,
   sniPortName          varchar(255) comment 'sni端口名',
   sniOperationStatus   int(1) comment '操作状态',
   sniAdminStatus       int(1) comment '管理状态',
   sniMediaType         int(1) comment 'twistedPair(1), fiber(2), other(3)',
   sniAutoNegotiationStatus int(2) comment '自协商状态',
   sniAutoNegotiationMode int(2) comment '自协商模式',
   sniPerfStats15minuteEnable tinyint comment '15分钟性能统计使能',
   sniPerfStats24hourEnable tinyint comment '24小时性能统计使能',
   sniLastStatusChangeTime bigint(20) comment '系统启动到该端口状态变化的sysUpTime值',
   sniMacAddrLearnMaxNum int(5) comment 'Mac地址学习最大个数',
   sniIsolationEnable   tinyint comment '隔离使能',
   topSniAttrFlowCtrlEnable tinyint comment '流控使能',
   topSniAttrIngressRate int(10),
   topSniAttrEgressRate int(10),
   topSniAttrActualSpeed bigint(20),
   topSniAttrPortType   tinyint,
   entityId             bigint(20),
   primary key (sniId)
);

alter table OltSniAttribute comment 'SNI口属性表';

/*==============================================================*/
/* Table: OltSniMacAddressManagement                            */
/*==============================================================*/
create table OltSniMacAddressManagement
(
   entityId             bigint(20) not null,
   sniId                bigint(20),
   sniMacAddrIndex      varchar(255) not null,
   sniMacAddrVlanIdIndex int(4) not null,
   sniMacAddrType       tinyint,
   sniMacAddrPortId     bigint(20),
   primary key (entityId, sniMacAddrIndex, sniMacAddrVlanIdIndex)
);

alter table OltSniMacAddressManagement comment 'Sni口Mac地址管理表';

/*==============================================================*/
/* Table: OltSniRedirect                                        */
/*==============================================================*/
create table OltSniRedirect
(
   sniId                bigint(20) not null,
   topSniRedirectGroupDirection tinyint,
   topSniRedirectGroupName varchar(255),
   topSniRedirectGroupDstPortId bigint(20),
   entityId             bigint(20),
   primary key (sniId)
);

alter table OltSniRedirect comment 'Sni口重定向表';

/*==============================================================*/
/* Table: OltSniRelation                                        */
/*==============================================================*/
create table OltSniRelation
(
   sniId                bigint(20) auto_increment not null comment '系统内id 自增长',
   slotId               bigint(20) not null,
   sniIndex             bigint(40) not null comment 'mib可用的index',
   constraint uk_sni_relation unique(sniIndex, entityId),
   entityId             bigint(20),
   primary key (sniId)
);

alter table OltSniRelation comment 'Sni口关系表';

/*==============================================================*/
/* Table: OltSniStormInfo                                       */
/*==============================================================*/
create table OltSniStormInfo
(
   sniId                bigint(20) not null,
   sniUnicastStormEnable tinyint,
   sniUnicastStormInPacketRate int,
   sniMulticastStormEnable tinyint,
   sniMulticastStormInPacketRate int,
   sniBroadcastStormEnable tinyint,
   sniBroadcastStormInPacketRate int,
   entityId             bigint(20),
   primary key (sniId)
);

alter table OltSniStormInfo comment 'Sni口广播风暴抑制表';

/*==============================================================*/
/* Table: OltTopVlanAgg                                         */
/*==============================================================*/
create table OltTopVlanAgg
(
   ponId                bigint(20) not null,
   llidVlanAggMacAddr   varchar(20) not null,
   llidVlanAfterAggVid  int(4) not null,
   llidVlanBeforeAggVidList text,
   llidVlanAggCosMode   tinyint,
   llidVlanAggNewCos    tinyint,
   llidVlanAggNewTpid   int(4),
   ponIndex             bigint(20),
   entityId             bigint(20),
   primary key (ponId, llidVlanAggMacAddr, llidVlanAfterAggVid)
);

alter table OltTopVlanAgg comment 'OltTopVlanAgg';

/*==============================================================*/
/* Table: OltTopVlanQinQ                                        */
/*==============================================================*/
create table OltTopVlanQinQ
(
   ponId                bigint(20) not null,
   topLqVlanMacIndex    varchar(20) not null,
   topLqVlanStartCVid   int(4) not null,
   topLqVlanEndCVid     int(4) not null,
   topLqVlanSVlan       int(4) not null,
   topLqVlanCosMode     tinyint,
   topLqVlanSCos        tinyint,
   topLqVlanOuterTpid   int(4),
   ponIndex             bigint(20),
   entityId             bigint(20),
   primary key (ponId, topLqVlanMacIndex, topLqVlanStartCVid, topLqVlanEndCVid, topLqVlanSVlan)
);

alter table OltTopVlanQinQ comment 'OltTopVlanQinQ';

/*==============================================================*/
/* Table: OltTopVlanTrans                                       */
/*==============================================================*/
create table OltTopVlanTrans
(
   ponId                bigint(20) not null,
   topLlidTransDevMac   varchar(20) not null,
   topLlidTransVidIdx   int(4) not null,
   topLlidTransNewVid   int(4),
   topLlidTransCosMode  tinyint,
   topLlidTransNewCos   tinyint,
   topLlidTransNewTpid  int(4),
   ponIndex             bigint(20),
   entityId             bigint(20),
   primary key (ponId, topLlidTransDevMac, topLlidTransVidIdx)
);

alter table OltTopVlanTrans comment 'OltTopVlanTrans';

/*==============================================================*/
/* Table: OltTopVlanTrunk                                       */
/*==============================================================*/
create table OltTopVlanTrunk
(
   ponId                bigint(20) not null,
   llidVlanTrunkVidBmp  text,
   llidVlanTrunkMacIdx  varchar(20) not null,
   ponIndex             bigint(20),
   entityId             bigint(20),
   primary key (ponId, llidVlanTrunkMacIdx)
);

alter table OltTopVlanTrunk comment 'OltTopVlanTrunk';

/*==============================================================*/
/* Table: OltUniAttribute                                       */
/*==============================================================*/
create table OltUniAttribute
(
   uniId                bigint(20) not null,
   uniOperationStatus   int(1) comment '操作状态',
   uniAdminStatus       int(1) comment '管理状态',
   uniAutoNegotiationEnable int(1) comment '自协商使能状态',
   uniAutoNegLocalTechAbility int(2) comment '自协商状态',
   uniAttrFlowCtrl      int(1) comment '流控使能状态',
   FlowCtrl   tinyint,
   PerfStats15minuteEnable tinyint,
   PerfStats24hourEnable tinyint,
   LastChangeTime bigint(10),
   IsolationEnable tinyint,
   MacAddrLearnMaxNum int,
   AutoNegAdvertisedTechAbility text,
   MacAddrClearByPort tinyint,
   entityId             bigint(20),
   macAge     int,
   primary key (uniId)
);

alter table OltUniAttribute comment 'Uni口属性表';

/*==============================================================*/
/* Table: OltUniRateLimit                                       */
/*==============================================================*/
create table OltUniRateLimit
(
   uniId                bigint(20) not null,
   uniPortInCIR         int,
   uniPortInCBS         int,
   uniPortInEBS         int,
   uniPortOutPIR        int,
   uniPortOutCIR        int,
   uniPortInRateLimitEnable tinyint,
   uniPortOutRateLimitEnable tinyint,
   entityId             bigint(20),
   primary key (uniId)
);

alter table OltUniRateLimit comment 'Uni口限速表';

/*==============================================================*/
/* Table: OltUniRelation                                        */
/*==============================================================*/
create table OltUniRelation
(
   uniId                bigint(20) auto_increment not null comment '系统内id 自增长',
   onuId                bigint(20) not null,
   uniIndex             bigint(40) not null comment 'mib可用的index',
   entityId             bigint(20),
   constraint uk_uni_relation unique(uniIndex, entityId),
   primary key (uniId)
);

alter table OltUniRelation comment 'Uni口关系表';

/*==============================================================*/
/* Table: OltUniStormInfo                                       */
/*==============================================================*/
create table OltUniStormInfo
(
   uniId                bigint(20) not null,
   uniUnicastStormEnable tinyint,
   uniUnicastStormInPacketRate int,
   uniUnicastStormOutPacketRate int,
   uniMulticastStormEnable tinyint,
   uniMulticastStormInPacketRate int,
   uniMulticastStormOutPacketRate int,
   uniBroadcastStormEnable tinyint,
   uniBroadcastStormInPacketRate int,
   uniBroadcastStormOutPacketRate int,
   entityId             bigint(20),
   primary key (uniId)
);

alter table OltUniStormInfo comment 'Uni口广播风暴抑制表';

/*==============================================================*/
/* Table: OltVlanAgg                                            */
/*==============================================================*/
create table OltVlanAgg
(
   entityId             bigint(20),
   ponId                bigint(20) not null comment '系统内id 自增长',
   portAggregationVidIndex bigint(20) not null,
   aggregationVidList   text,
   ponIndex             bigint(20),
   primary key (ponId, portAggregationVidIndex)
);

alter table OltVlanAgg comment ' Vlan_Olt_Agg';

/*==============================================================*/
/* Table: OltVlanConfig                                         */
/*==============================================================*/
create table OltVlanConfig
(
   entityId             bigint(20) not null,
   oltVlanIndex         int(4) not null,
   oltVlanName          varchar(128),
   topMcFloodMode       int(4),
   taggedPort           text,
   untaggedPort         text,
   primary key (entityId, oltVlanIndex)
);

alter table OltVlanConfig comment 'VLAN全局配置表';

/*==============================================================*/
/* Table: OltVlanGlobalInfo                                     */
/*==============================================================*/
create table OltVlanGlobalInfo
(
   entityId             bigint(20) not null,
   maxVlanId            int(4),
   maxSupportVlans      int(4),
   createdVlanNumber    int(4),
   primary key (entityId)
);

alter table OltVlanGlobalInfo comment 'OltVlan基本属性';

/*==============================================================*/
/* Table: OltVlanTranslation                                    */
/*==============================================================*/
create table OltVlanTranslation
(
   entityId             bigint(20),
   ponId                bigint(20) not null comment '系统内id 自增长',
   ponIndex             bigint(20),
   vlanIndex            int(4) not null,
   translationNewVid    int(4),
   primary key (ponId, vlanIndex)
);

alter table OltVlanTranslation comment 'OltVlanTranslation';

/*==============================================================*/
/* Table: OltVlanTrunk                                          */
/*==============================================================*/
create table OltVlanTrunk
(
   entityId             bigint(20),
   ponId                bigint(20) not null comment '系统内id 自增长',
   ponIndex             bigint(20),
   trunkVidList         text,
   primary key (ponId)
);

alter table OltVlanTrunk comment 'OltVlanTrunk';

/*==============================================================*/
/* Table: OnuBlockAuthen                                        */
/*==============================================================*/
create table OnuBlockAuthen
(
   ponId                bigint(20) not null,
   onuAuthenBlockMacAddress bigint(20),
   topOnuAuthBlockedExtLogicSn varchar(24),
   topOnuAuthBlockedExtPwd varchar(12),
   authenTime           bigint,
   onuIndex             bigint(20),
   entityId             bigint(20)
);

alter table OnuBlockAuthen comment 'Onu阻塞表';

/*==============================================================*/
/* Table: OnuPortVlan                                           */
/*==============================================================*/
create table OnuPortVlan
(
   uniId                bigint(20) not null,
   vlanTagTpid          varchar(10),
   vlanTagCfi           tinyint,
   vlanTagPriority      int(4),
   vlanPVid             int(4),
   vlanMode             tinyint,
   uniIndex             bigint(20),
   entityId             bigint(20),
   primary key (uniId)
);

alter table OnuPortVlan comment 'OnuPortVlan';

/*==============================================================*/
/* Table: OnuVlanAgg                                            */
/*==============================================================*/
create table OnuVlanAgg
(
   uniId                bigint(20) not null,
   portAggregationVidIndex int(4) not null,
   aggregationVidList   text,
   uniIndex             bigint(20),
   entityId             bigint(20),
   primary key (uniId, portAggregationVidIndex)
);

alter table OnuVlanAgg comment 'Vlan_Onu_Agg';

/*==============================================================*/
/* Table: OnuVlanTranslation                                    */
/*==============================================================*/
create table OnuVlanTranslation
(
   uniId                bigint(20) not null,
   vlanIndex            int(4) not null,
   translationNewVid    int(4),
   uniIndex             bigint(20),
   entityId             bigint(20),
   primary key (uniId, vlanIndex)
);

alter table OnuVlanTranslation comment 'OnuVlanTranslation';

/*==============================================================*/
/* Table: OnuVlanTrunk                                          */
/*==============================================================*/
create table OnuVlanTrunk
(
   uniId                bigint(20) not null,
   trunkVidList         text,
   uniIndex             bigint(20),
   entityId             bigint(20),
   primary key (uniId)
);

alter table OnuVlanTrunk comment 'OnuVlanTrunk';

alter table OltAttribute add constraint FK_olt_attr foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltAuthentication add constraint FK_pon_auth foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltDhcpBaseConfig add constraint FK_dhcp_base_config foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltDhcpIpMacDynamic add constraint FK_dhcp_dynamic_config foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltDhcpIpMacStatic add constraint FK_dhcp_static_config foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;
      
alter table OltFanAttribute add constraint FK_fan_attr foreign key (fanCardId)
      references OltFanRelation (fanCardId) on delete cascade on update cascade;

alter table OltFanRelation add constraint FK_entity_fanCard foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltFanStatus add constraint FK_fan_cur foreign key (fanCardId)
      references OltFanRelation (fanCardId) on delete cascade on update cascade;

alter table OltOnuAttribute add constraint FK_onu_attr foreign key (onuId)
      references OltOnuRelation (onuId) on delete cascade on update cascade;

alter table OltOnuPonAttribute add constraint FK_onu_pon_attr foreign key (onuPonId)
      references OltOnuPonRelation (onuPonId) on delete cascade on update cascade;

alter table OltOnuPonRelation add constraint FK_onu_pon foreign key (onuId)
      references OltOnuRelation (onuId) on delete cascade on update cascade;

alter table OltOnuRelation add constraint FK_pon_onu foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltOperatorHistory add constraint FK_oper_type foreign key (operType)
      references OltOperType (operType) on delete cascade on update cascade;

alter table OltOperatorHistory add constraint FK_oper_user foreign key (userId)
      references Users (userId) on update cascade;

alter table OltPonAttribute add constraint FK_pon_attr foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltPonQinQ add constraint FK_vlan_pon_qinq foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltPonRelation add constraint FK_slot_pon foreign key (slotId)
      references OltSlotRelation (slotId) on delete cascade on update cascade;

alter table OltPonStormInfo add constraint FK_pon_strom foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltPortMirrorConfig add constraint FK_olt_port_mirror foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltPortTrunkConfig add constraint FK_olt_trunk_config foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltPortVlan add constraint FK_vlan_olt_port foreign key (sniId)
      references OltSniRelation (sniId) on delete cascade on update cascade;

alter table OltPowerAttribute add constraint FK_power_attr foreign key (powerCardId)
      references OltPowerRelation (powerCardId) on delete cascade on update cascade;

alter table OltPowerRelation add constraint FK_entity_powerCard foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltPowerStatus add constraint FK_power_cur foreign key (powerCardId)
      references OltPowerRelation (powerCardId) on delete cascade on update cascade;

alter table OltSlotAttribute add constraint FK_slot_attr foreign key (slotId)
      references OltSlotRelation (slotId) on delete cascade on update cascade;

alter table OltSlotRelation add constraint FK_entity_slot foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltSlotStatus add constraint FK_slot_cur foreign key (slotId)
      references OltSlotRelation (slotId) on delete cascade on update cascade;

alter table OltSniAttribute add constraint FK_sni_attr foreign key (sniId)
      references OltSniRelation (sniId) on delete cascade on update cascade;

alter table OltSniMacAddressManagement add constraint FK_olt_sni_macManage foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltSniRedirect add constraint FK_sni_dsc_redirect foreign key (topSniRedirectGroupDstPortId)
      references OltSniRelation (sniId) on delete set null on update set null;

alter table OltSniRedirect add constraint FK_sni_src_redirect foreign key (sniId)
      references OltSniRelation (sniId) on delete cascade on update cascade;

alter table OltSniRelation add constraint FK_slot_sni foreign key (slotId)
      references OltSlotRelation (slotId) on delete cascade on update cascade;

alter table OltSniStormInfo add constraint FK_sni_storm foreign key (sniId)
      references OltSniRelation (sniId) on delete cascade on update cascade;

alter table OltTopVlanAgg add constraint FK_vlan_top_agg foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltTopVlanQinQ add constraint FK_vlan_top_qinq foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltTopVlanTrans add constraint FK_vlan_top_trans foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltTopVlanTrunk add constraint FK_vlan_top_trunk foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltUniAttribute add constraint FK_uni_attr foreign key (uniId)
      references OltUniRelation (uniId) on delete cascade on update cascade;

alter table OltUniRateLimit add constraint FK_uni_rateLimit foreign key (uniId)
      references OltUniRelation (uniId) on delete cascade on update cascade;

alter table OltUniRelation add constraint FK_onu_uni foreign key (onuId)
      references OltOnuRelation (onuId) on delete cascade on update cascade;

alter table OltUniStormInfo add constraint FK_uni_storm foreign key (uniId)
      references OltUniRelation (uniId) on delete cascade on update cascade;

alter table OltVlanAgg add constraint FK_vlan_olt_agg foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltVlanConfig add constraint FK_vlan_config foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltVlanGlobalInfo add constraint FK_olt_VlanGloalInfo foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltVlanTranslation add constraint FK_vlan_olt_translation foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltVlanTrunk add constraint FK_vlan_olt_trunk foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OnuBlockAuthen add constraint FK_onu_block foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OnuPortVlan add constraint FK_vlan_onu_port foreign key (uniId)
      references OltUniRelation (uniId) on delete cascade on update cascade;

alter table OnuVlanAgg add constraint FK_vlan_onu_agg foreign key (uniId)
      references OltUniRelation (uniId) on delete cascade on update cascade;

alter table OnuVlanTranslation add constraint FK_vlan_onu_translation foreign key (uniId)
      references OltUniRelation (uniId) on delete cascade on update cascade;

alter table OnuVlanTrunk add constraint FK_vlan_onu_trunk foreign key (uniId)
      references OltUniRelation (uniId) on delete cascade on update cascade;



/* -- version 1.0.0,build 2011-9-22,module epon */

-- version 1.0.0,build 2011-11-21,module epon
/*==============================================================*/
/* Table: OltTopAlarmCodeMask                                   */
/*==============================================================*/
create table OltTopAlarmCodeMask
(
   entityId             bigint(20) not null,
   topAlarmCodeMaskIndex bigint(20) not null,
   topAlarmCodeMaskEnable tinyint(1),
   primary key (entityId, topAlarmCodeMaskIndex)
);

alter table OltTopAlarmCodeMask comment 'OLT告警类型屏蔽';

/*==============================================================*/
/* Table: OltTopAlarmInstanceMask                               */
/*==============================================================*/
create table OltTopAlarmInstanceMask
(
   entityId             bigint(20) not null,
   topAlarmInstanceMaskIndex bigint(20) not null,
   topAlarmInstanceMaskEnable tinyint(1),
   primary key (entityId, topAlarmInstanceMaskIndex)
);

alter table OltTopAlarmInstanceMask comment 'OLT告警实体屏蔽';

/*==============================================================*/
/* Table: OltTrapConfig                                         */
/*==============================================================*/
create table OltTrapConfig
(
   entityId             bigint(20) not null,
   eponManagementAddrName varchar(80) not null,
   eponManagementAddrTAddress varchar(80),
   eponManagementAddrCommunity varchar(100),
   primary key (entityId, eponManagementAddrName)
);

alter table OltTrapConfig comment 'OLT Trap配置表';

alter table OltTopAlarmCodeMask add constraint FK_alarm_code_mask_entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltTopAlarmInstanceMask add constraint FK_alarm_Instance_mask_entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltTrapConfig add constraint FK_trap_server_entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

/* -- version 1.0.0,build 2011-11-21,module epon */


-- version 1.0.0,build 2011-11-22,module epon
/*==============================================================*/
/* Table: OltPerfThreshold                                      */
/*==============================================================*/
create table OltPerfThreshold
(
   entityId             bigint(20)  not null comment '系统内id 自增长',
   thresholdType        varchar(11),
   perfThresholdTypeIndex int not null,
   perfThresholdObject  int not null,
   perfThresholdUpper   varchar(30),
   perfThresholdLower  varchar(30),
   primary key (entityId, perfThresholdTypeIndex, perfThresholdObject)
);

alter table OltPerfThreshold comment '门限值配置表';

/*==============================================================*/
/* Table: PerfStatCycle                                         */
/*==============================================================*/
create table PerfStatCycle
(
   entityId             bigint(20)  not null comment '系统内id 自增长',
   topPerfStatOLTCycle  int,
   topPerfStatONUCycle  int,
   topPerfOLTTemperatureCycle int,
   topPerfONUTemperatureCycle int,
   primary key (entityId)
);

alter table PerfStatCycle comment '保存全局的轮询周期值';

/*==============================================================*/
/* Table: PerfStats15Table                                      */
/*==============================================================*/
create table PerfStats15Table
(
   entityId             bigint not null,
   portIndex            bigint not null,
   stats15InOctets      bigint,
   stats15InPkts        bigint,
   stats15InBroadcastPkts bigint,
   stats15InMulticastPkts bigint,
   stats15InPkts64Octets bigint,
   stats15InPkts65to127Octets bigint,
   stats15InPkts128to255Octets bigint,
   stats15InPkts256to511Octets bigint,
   stats15InPkts512to1023Octets bigint,
   stats15InPkts1024to1518Octets bigint,
   stats15InPkts1519to1522Octets bigint,
   stats15InUndersizePkts bigint,
   stats15InOversizePkts bigint,
   stats15InFragments   bigint,
   stats15InMpcpFrames  bigint,
   stats15InMpcpOctets  bigint,
   stats15InOAMFrames   bigint,
   stats15InOAMOctets   bigint,
   stats15InCRCErrorPkts bigint,
   stats15InDropEvents  bigint,
   stats15InJabbers     bigint,
   stats15InCollision   bigint,
   stats15OutOctets     bigint,
   stats15OutPkts       bigint,
   stats15OutBroadcastPkts bigint,
   stats15OutMulticastPkts bigint,
   stats15OutPkts64Octets bigint,
   stats15OutPkts65to127Octets bigint,
   stats15OutPkts128to255Octets bigint,
   stats15OutPkts256to511Octets bigint,
   stats15OutPkts512to1023Octets bigint,
   stats15OutPkts1024to1518Octets bigint,
   stats15OutPkts1519to1522Octets bigint,
   stats15OutUndersizePkts bigint,
   stats15OutOversizePkts bigint,
   stats15OutFragments  bigint,
   stats15OutMpcpFrames bigint,
   stats15OutMpcpOctets bigint,
   stats15OutOAMFrames  bigint,
   stats15OutOAMOctets  bigint,
   stats15OutCRCErrorPkts bigint,
   stats15OutDropEvents bigint,
   stats15OutJabbers    bigint,
   stats15OutCollision  bigint,
   stats15EndTime       timestamp not null,
   primary key (entityId, portIndex, stats15EndTime)
);

alter table PerfStats15Table comment '15分钟历史性能表';

/*==============================================================*/
/* Table: PerfStats24Table                                      */
/*==============================================================*/
create table PerfStats24Table
(
   entityId             bigint not null,
   portIndex            bigint not null,
   stats24InOctets      bigint,
   stats24InPkts        bigint,
   stats24InBroadcastPkts bigint,
   stats24InMulticastPkts bigint,
   stats24InPkts64Octets bigint,
   stats24InPkts65to127Octets bigint,
   stats24InPkts128to255Octets bigint,
   stats24InPkts256to511Octets bigint,
   stats24InPkts512to1023Octets bigint,
   stats24InPkts1024to1518Octets bigint,
   stats24InPkts1519to1522Octets bigint,
   stats24InUndersizePkts bigint,
   stats24InOversizePkts bigint,
   stats24InFragments   bigint,
   stats24InMpcpFrames  bigint,
   stats24InMpcpOctets  bigint,
   stats24InOAMFrames   bigint,
   stats24InOAMOctets   bigint,
   stats24InCRCErrorPkts bigint,
   stats24InDropEvents  bigint,
   stats24InJabbers     bigint,
   stats24InCollision   bigint,
   stats24OutOctets     bigint,
   stats24OutPkts       bigint,
   stats24OutBroadcastPkts bigint,
   stats24OutMulticastPkts bigint,
   stats24OutPkts64Octets bigint,
   stats24OutPkts65to127Octets bigint,
   stats24OutPkts128to255Octets bigint,
   stats24OutPkts256to511Octets bigint,
   stats24OutPkts512to1023Octets bigint,
   stats24OutPkts1024to1518Octets bigint,
   stats24OutPkts1519to1522Octets bigint,
   stats24OutUndersizePkts bigint,
   stats24OutOversizePkts bigint,
   stats24OutFragments  bigint,
   stats24OutMpcpFrames bigint,
   stats24OutMpcpOctets bigint,
   stats24OutOAMFrames  bigint,
   stats24OutOAMOctets  bigint,
   stats24OutCRCErrorPkts bigint,
   stats24OutDropEvents bigint,
   stats24OutJabbers    bigint,
   stats24OutCollision  bigint,
   stats24EndTime       timestamp not null,
   primary key (entityId, portIndex, stats24EndTime)
);

alter table PerfStats24Table comment '24小时历史性能表';

/*==============================================================*/
/* Table: PerfStatsGlobalSet                                    */
/*==============================================================*/
create table PerfStatsGlobalSet
(
   entityId             bigint(20)  not null comment '系统内id 自增长',
   perfStats15MinMaxRecord int,
   perfStats24HourMaxRecord int,
   primary key (entityId)
);

alter table PerfStatsGlobalSet comment '记录15分钟和24小时历史记录保存个数';

alter table OltPerfThreshold add constraint FK_olt_threshold_entityId foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table PerfStatCycle add constraint FK_olt_cycle_entityId foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table PerfStatsGlobalSet add constraint FK_olt_record_entityId foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

/* -- version 1.0.0,build 2011-11-22,module epon */

-- version 1.0.0,build 2011-11-24,module epon
/*==============================================================*/
/* Table: OltAclListTable                                       */
/*==============================================================*/
create table OltAclListTable
(
   entityId             bigint(20) not null,
   topAclListIndex      int not null,
   topAclDescription    varchar(64),
   topAclRuleNum        int,
   topAclRulePriority   int,
   primary key (entityId, topAclListIndex)
);

alter table OltAclListTable comment 'OltAclListTable';

/*==============================================================*/
/* Table: OltAclPortACLListTable                                */
/*==============================================================*/
create table OltAclPortACLListTable
(
   entityId             bigint(20) not null,
   portIndex            bigint(20) not null,
   topPortAclListIndex  int not null,
   topAclPortDirection  tinyint not null,
   primary key (entityId, portIndex, topPortAclListIndex, topAclPortDirection)
);

alter table OltAclPortACLListTable comment 'OltAclPortACLListTable';

/*==============================================================*/
/* Table: OltAclRuleEntry                                       */
/*==============================================================*/
create table OltAclRuleEntry
(
   entityId             bigint(20) not null,
   topAclRuleListIndex  int not null,
   topAclRuleIndex      int not null,
   topMatchedFieldSelection varchar(255),
   topMatchedSrcMac     bigint(10),
   topMatchedSrcMacMask bigint(10),
   topMatchedDstMac     bigint(10),
   topMatchedDstMacMask bigint(10),
   topMatchedStartSVid  int,
   topMatchedEndSVid    int,
   topMatchedStartCVid  int,
   topMatchedEndCVid    int,
   topMatchedOuterCos   tinyint,
   topMatchedInnerCos   tinyint,
   topMatchedOuterTpid  int,
   topMatchedInnerTpid  int,
   topMatchedEthernetType int,
   topMatchedSrcIP      bigint(10),
   topMatchedSrcIPMask  bigint(10),
   topMatchedDstIP      bigint(10),
   topMatchedDstIPMask  bigint(10),
   topMatchedL3ProtocolClass tinyint,
   topMatchedIpProtocol int,
   topMatchedDscp       tinyint,
   topMatchedTos        tinyint,
   topMatchedStartSrcPort int,
   topMatchedEndSrcPort int,
   topMatchedStartDstPort int,
   topMatchedEndDstPort int,
   topAclAction         varchar(255),
   topAclActionParameter varchar(255),
   primary key (entityId, topAclRuleListIndex, topAclRuleIndex)
);

alter table OltAclRuleEntry comment 'topAclRuleEntry';

alter table OltAclListTable add constraint FK_topAclList_Entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltAclPortACLListTable add constraint FK_list_AclList foreign key (entityId, topPortAclListIndex)
      references OltAclListTable (entityId, topAclListIndex) on delete cascade on update cascade;

alter table OltAclPortACLListTable add constraint FK_topPortACLList_Entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltAclRuleEntry add constraint FK_list_Rule foreign key (entityId, topAclRuleListIndex)
      references OltAclListTable (entityId, topAclListIndex) on delete cascade on update cascade;

alter table OltAclRuleEntry add constraint FK_topAclRuleEntry_Entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

/* -- version 1.0.0,build 2011-11-24,module epon */

-- version 1.0.0,build 2011-11-25,module epon
/*==============================================================*/
/* Table: OltQosDeviceBaseQosMapEntry                           */
/*==============================================================*/
create table OltQosDeviceBaseQosMapEntry
(
   entityId             bigint(20) not null,
   onuIndex             bigint(20) not null,
   deviceBaseQosMapRuleIndex tinyint not null,
   deviceBaseQosMapOctet text,
   primary key (entityId, onuIndex, deviceBaseQosMapRuleIndex)
);

alter table OltQosDeviceBaseQosMapEntry comment 'oltDeviceBaseQosMapEntry';

/*==============================================================*/
/* Table: OltQosDeviceBaseQosPolicyEntry                        */
/*==============================================================*/
create table OltQosDeviceBaseQosPolicyEntry
(
   entityId             bigint(20) not null,
   onuIndex             bigint(20) not null,
   policyMode tinyint,
   WeightOctet text,
   SpBandwidthRange text,
   primary key (entityId, onuIndex)
);

alter table OltQosDeviceBaseQosPolicyEntry comment 'oltDeviceBaseQosPolicyEntry';

/*==============================================================*/
/* Table: OltQosPortBaseQosMapEntry                             */
/*==============================================================*/
create table OltQosPortBaseQosMapEntry
(
   entityId             bigint(20) not null,
   portIndex            bigint(20) not null,
   portBaseQosMapRuleIndex tinyint,
   portBaseQosMapOctet  text,
   primary key (entityId, portIndex)
);

alter table OltQosPortBaseQosMapEntry comment 'oltPortBaseQosMapEntry';

/*==============================================================*/
/* Table: OltQosPortBaseQosPolicyEntry                          */
/*==============================================================*/
create table OltQosPortBaseQosPolicyEntry
(
   entityId             bigint(20) not null,
   portIndex            bigint(20) not null,
   policyMode tinyint,
   WeightOctet text,
   SpBandwidthRange text,
   primary key (entityId, portIndex)
);

alter table OltQosPortBaseQosPolicyEntry comment 'oltPortBaseQosPolicyEntry';

/*==============================================================*/
/* Table: OltSlaTable                                           */
/*==============================================================*/
create table OltSlaTable
(
   entityId             bigint(20)  not null comment '系统内id 自增长',
   onuIndex             bigint(20)  not null,
   slaDsFixedBW         int,
   slaDsPeakBW          int,
   slaDsCommittedBW     int,
   slaUsFixedBW         int,
   slaUsPeakBW          int,
   slaUsCommittedBW     int,
   primary key (entityId, onuIndex)
);

alter table OltSlaTable comment 'OltSlaTable';

alter table OltQosDeviceBaseQosMapEntry add constraint FK_deviceQosMap_Entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltQosDeviceBaseQosPolicyEntry add constraint FK_deviceQosPolicy_Entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltQosPortBaseQosMapEntry add constraint FK_portQosMap_Entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltQosPortBaseQosPolicyEntry add constraint FK_portQosPolicy_Entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltSlaTable add constraint FK_sla_entityId foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;



/* -- version 1.0.0,build 2011-11-25,module epon */

-- version 1.0.0,build 2011-11-26,module epon
/*==============================================================*/
/* Table: OltIgmpCtrlMPackgEntry                */
/*==============================================================*/
create table OltIgmpCtrlMPackgEntry
(
    entityId             bigint(20) not null,
    cmIndex              int not null,
    cmName               varchar(255),
    cmChName             varchar(255),
    cmProxyList          text,
    multicastUserAuthority tinyint,
    maxRequestChannelNum int,
    singlePreviewTime    int,
    totalPreviewTime     int,
    previewResetTime     int,
    previewCount         int,
    previewInterval      int,
    primary key (entityId, cmIndex)
);

alter table OltIgmpCtrlMPackgEntry comment 'OltIgmpCtrlMPackgEntry';

/*==============================================================*/
/* Table: OltIgmpCtrlMUserAuthEntry          */
/*==============================================================*/
create table OltIgmpCtrlMUserAuthEntry
(
    entityId             bigint(20) not null,
    portIndex            bigint(20) not null,
    multicastPackageList text,
    igmpGlobalBW         bigint(20),
    igmpGlobalBWUsed     bigint(20),
    primary key (entityId, portIndex)
);

alter table OltIgmpCtrlMUserAuthEntry comment 'OltIgmpCtrlMUserAuthEntry';

/*==============================================================*/
/* Table: OltIgmpEntityEntry                                    */
/*==============================================================*/
create table OltIgmpEntityEntry
(
    entityId             bigint(20) not null,
    deviceIndex          tinyint not null,
    igmpMode             tinyint,
    maxQueryResponseTime int,
    robustVariable       int,
    queryInterval        int,
    lastMemberQueryInterval int,
    lastMemberQueryCount int,
    igmpVersion          tinyint,
    primary key (entityId, deviceIndex)
);

alter table OltIgmpEntityEntry comment 'oltIgmpEntityEntry';

/*==============================================================*/
/* Table: OltIgmpForwardingEntry                                */
/*==============================================================*/
create table OltIgmpForwardingEntry
(
    entityId             bigint(20) not null,
    deviceIndex          tinyint not null,
    groupVlanIndex       int,
    macLongDB            bigint(20),
    groupPortList        text,
    primary key (entityId, deviceIndex)
);

alter table OltIgmpForwardingEntry comment 'oltIgmpForwardingEntry';

/*==============================================================*/
/* Table: OltIgmpProxyParaEntry                                 */
/*==============================================================*/
create table OltIgmpProxyParaEntry
(
    entityId             bigint(20)  not null comment '系统内id 自增长',
    proxyIndex           int not null,
    proxyName            varchar(255),
    proxyChName          varchar(255),
    proxySrcIPAddress    varchar(100),
    proxyMulticastVID    int,
    proxyMulticastIPAddress varchar(100),
    multicastAssuredBW   bigint(20),
    multicastMaxBW       bigint(20),
    primary key (entityId, proxyIndex)
);

alter table OltIgmpProxyParaEntry comment 'oltIgmpProxyParaEntry';

/*==============================================================*/
/* Table: OltIgmpTopControlledMcCdrEntry                        */
/*==============================================================*/
create table OltIgmpTopControlledMcCdrEntry
(
    entityId             bigint(20) not null,
    SequenceIndex int not null,
    CdrUniIndex bigint(20),
    CdrIgmpReqType tinyint,
    CdrIgmpReqTime bigint(20),
    CdrIgmpReqChannel varchar(10),
    CdrIgmpReqRight tinyint,
    CdrIgmpReqResult tinyint,
    CdrLeaveType tinyint,
    CdrRecordTime bigint(20),
    primary key (entityId, SequenceIndex)
);

alter table OltIgmpTopControlledMcCdrEntry comment 'oltTopControlledMcCdrEntry';

/*==============================================================*/
/* Table: OltIgmpTopMcOltConfMgmtObjs                      */
/*==============================================================*/
create table OltIgmpTopMcOltConfMgmtObjs
(
    entityId             bigint(20) not null,
    topMcMaxGroupNum     int,
    topMcMaxBw                   bigint(10),
    topMcSnoopingAgingTime     int,
    topMcMVlan     text,
    primary key (entityId)
);

alter table OltIgmpTopMcOltConfMgmtObjs comment 'oltTopMcOltConfigMgmtObjects';

/*==============================================================*/
/* Table: OltIgmpTopMcOnuEntry                                  */
/*==============================================================*/
create table OltIgmpTopMcOnuEntry
(
    entityId             bigint(20) not null,
    onuIndex             bigint(10) not null,
    topMcOnuMode         tinyint,
    topMcOnuFastLeave    tinyint,
    primary key (entityId, onuIndex)
);

alter table OltIgmpTopMcOnuEntry comment 'oltTopMcOnuEntry';

/*==============================================================*/
/* Table: OltIgmpTopMcOnuVlanTransEntry                         */
/*==============================================================*/
create table OltIgmpTopMcOnuVlanTransEntry
(
    entityId             bigint(20) not null,
    topMcOnuVlanTransIndex tinyint not null,
    topMcOnuVlanTransOldVidList varchar(100),
    topMcOnuVlanTransNewVidList varchar(100),
    primary key (entityId, topMcOnuVlanTransIndex)
);

alter table OltIgmpTopMcOnuVlanTransEntry comment 'oltTopMcOnuVlanTransEntry';

/*==============================================================*/
/* Table: OltIgmpTopMcSniConfMgmtObjs                      */
/*==============================================================*/
create table OltIgmpTopMcSniConfMgmtObjs
(
    entityId             bigint(20) not null,
    topMcSniPortType     tinyint,
    topMcSniPort         varchar(10),
    topMcSniAggPort      int,
    topMcSniAgingTime    int,
    primary key (entityId)
);

alter table OltIgmpTopMcSniConfMgmtObjs comment ' oltTopMcSniConfigMgmtObjects';

/*==============================================================*/
/* Table: OltIgmpTopMcUniConfigEntry                            */
/*==============================================================*/
create table OltIgmpTopMcUniConfigEntry
(
    entityId             bigint(20) not null,
    uniIndex             bigint(20) not null,
    topMcUniMaxGroupQuantity tinyint,
    topMcUniVlanMode     tinyint,
    topMcUniVlanTransIdx tinyint,
    topMcUniVlanList     text,
    primary key (entityId, uniIndex)
);

alter table OltIgmpTopMcUniConfigEntry comment 'oltTopMcUniConfigEntry';

alter table OltIgmpCtrlMPackgEntry add constraint FK_IgmpCtrlMPackg_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpCtrlMUserAuthEntry add constraint FK_IgmpCtrlMUserAuth_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpEntityEntry add constraint FK_oltIgmpEntityEntry_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpForwardingEntry add constraint FK_oltIgmpForwardingEntry_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpProxyParaEntry add constraint FK_oltIgmpProxyParaEntry foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpTopControlledMcCdrEntry add constraint FK_oltTopControlledMcCdrEntry_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpTopMcOltConfMgmtObjs add constraint FK_IgmpMcOltConf_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpTopMcOnuEntry add constraint FK_oltTopMcOnuEntry_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpTopMcOnuVlanTransEntry add constraint FK_oltTopMcOnuVlanTransEntry_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpTopMcSniConfMgmtObjs add constraint FK_IgmpMcSniConf_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;

alter table OltIgmpTopMcUniConfigEntry add constraint FK_oltTopMcUniConfigEntry_Entity foreign key (entityId)
    references Entity (entityId) on delete cascade on update cascade;
/* -- version 1.0.0,build 2011-11-26,module epon */

-- version 1.0.0,build 2011-11-27,module epon
/*==============================================================*/
/* Table: OltOnuUpgrade                                         */
/*==============================================================*/
create table OltOnuUpgrade
(
   onuUpgradeId         bigint(20) not null auto_increment,
   topOnuUpgradeTransactionIndex int(8),
   topOnuUpgradeSlotNum bigint(20),
   topOnuUpgradeOnuType int(11),
   topOnuUpgradeFileName varchar(100),
   topOnuUpgradeFileType tinyint(4),
   topOnuUpgradeMode    tinyint(1),
   topOnuUpgradeOnuList text,
   topOnuUpgradeOperAction tinyint(1),
   topOnuUpgradeStatus  text,
   entityId             bigint(20),
   primary key (onuUpgradeId)
);

alter table OltOnuUpgrade comment 'ONu升级表';

alter table OltOnuUpgrade add constraint FK_onu_upgrade_entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;


/* -- version 1.0.0,build 2011-11-27,module epon */

-- version 1.0.0,build 2011-11-30,module epon
/*==============================================================*/
/* Table: OltStpGlobalSetTable                                  */
/*==============================================================*/
create table OltStpGlobalSetTable
(
   entityId             bigint(20) not null,
   Version  tinyint,
   Priority int,
   TimeSinceTopologyChange bigint(11),
   TopChanges bigint(11),
   DesignatedRoot varchar(255),
   RootCost int,
   RootPort varchar(255),
   MaxAge   bigint(11),
   HelloTime bigint(11),
   HoldTime int,
   ForwardDelay bigint(11),
   BridgeMaxAge int,
   BridgeHelloTime int,
   BridgeForwardDelay int,
   RstpTxHoldCount tinyint,
   Enable   tinyint,
   primary key (entityId)
);

alter table OltStpGlobalSetTable comment 'Stp全局配置表';

/*==============================================================*/
/* Table: OltStpPortTable                                       */
/*==============================================================*/
create table OltStpPortTable
(
   entityId             bigint(20) not null,
   sniId                bigint(20),
   stpPortStatus        tinyint,
   stpPortPriority      int,
   stpPortPathCost      int(11),
   stpPortDesignatedRoot varchar(255),
   stpPortDesignatedCost int,
   stpPortDesignatedBridge varchar(255),
   stpPortDesignatedPort int(11),
   stpPortForwardTransitions int(11),
   stpPortRstpProtocolMigration tinyint,
   stpPortRstpAdminEdgePort tinyint,
   stpPortRstpOperEdgePort tinyint,
   stpPortPointToPointAdminStatus tinyint,
   stpPortPointToPointOperStatus tinyint,
   stpPortEnabled       tinyint,
   primary key (entityId, sniId)
);

alter table OltStpPortTable comment 'Stp端口配置表';

alter table OltStpGlobalSetTable add constraint FK_stp_global_entity foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;

alter table OltStpPortTable add constraint FK_stp_port_sni foreign key (sniId)
      references OltSniRelation (sniId) on delete cascade on update cascade;

/* -- version 1.0.0,build 2011-11-30,module epon */


-- version 1.0.0,build 2011-12-13,module epon
/*==============================================================*/
/* Table: OltPonProtect                                         */
/*==============================================================*/
create table OltPonProtect
(
   entityId             bigint(20),
   protectId            int not null,
   ponIdMaster          bigint(20),
   ponIdReserve         bigint(20),
   protectName          varchar(255),
   primary key (protectId)
);

alter table OltPonProtect comment 'PON口保护表';

alter table OltPonProtect add constraint FK_pon_protect_master foreign key (ponIdMaster)
      references OltPonRelation (ponId) on delete cascade on update cascade;

alter table OltPonProtect add constraint FK_pon_protect_reserve foreign key (ponIdReserve)
      references OltPonRelation (ponId) on delete cascade on update cascade;
/* -- version 1.0.0,build 2011-12-13,module epon */

-- version 1.0.0,build 2011-12-21,module epon
/*==============================================================*/
/* Table: OltOnuTypeInfo                                        */
/*==============================================================*/
create table OltOnuTypeInfo
(
   onuTypeName          varchar(255),
   onuTypeId            int not null,
   gePortNum            tinyint,
   fePortNum            tinyint,
   onuPonNum            tinyint,
   onuDesc              varchar(255),
   onuIcon              varchar(255),
   primary key (onuTypeId)
);

alter table OltOnuTypeInfo comment 'Onu类型表';

/*==============================================================*/
/* Table: OltMonitorValue                                        */
/*==============================================================*/
create table OltMonitorValue
(
   fieldText  varchar(255),
   fieldTable varchar(255),
   fieldOid  varchar(255),
   primaryKey varchar(255),
   primaryKeyValue bigint(20)
);

/*==============================================================*/
/* Table: OltPonOnuAuthMode                                     */
/*==============================================================*/
create table OltPonOnuAuthMode
(
   ponId                bigint(20) not null,
   topPonOnuAuthModeMode tinyint,
   entityId             bigint(20)
);
alter table OltPonOnuAuthMode comment 'PON口ONU认证模式表';
alter table OltPonOnuAuthMode add constraint FK_pon_auth_mode foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: OltPonOnuAutoAuthMode                                     */
/*==============================================================*/
create table OltPonOnuAutoAuthMode
(
   ponId                bigint(20) not null,
   topPonOnuAutoAuthEnable tinyint,
   entityId             bigint(20)
);
alter table OltPonOnuAutoAuthMode comment 'PON口ONU认证使能表';
alter table OltPonOnuAutoAuthMode add constraint FK_pon_auth_enable foreign key (ponId)
        references OltPonRelation (ponId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: OltVifPriIpConfig                                     */
/*==============================================================*/
create table OltVifPriIpConfig
(
   entityId             bigint(20)  not null comment '系统内id 自增长',
   topOltVifPriIpVlanIdx int(4) not null,
   topOltVifPriIpAddr   varchar(20),
   topOltVifPriIpMask   varchar(20),
   primary key (entityId, topOltVifPriIpVlanIdx)
);

alter table OltVifPriIpConfig comment 'VLAN虚接口主IP配置表';

alter table OltVifPriIpConfig add constraint FK_entity_vifPriIp foreign key (entityId)
      references Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE;

/*==============================================================*/
/* Table: OltVifSubIpConfig                                     */
/*==============================================================*/
create table OltVifSubIpConfig
(
   entityId             bigint(20)  not null comment '系统内id 自增长',
   topOltVifSubIpVlanIdx int(4) not null,
   topOltVifSubIpSeqIdx int(1) not null,
   topOltVifSubIpAddr   varchar(20),
   topOltVifSubIpMask   varchar(20),
   primary key (entityId, topOltVifSubIpVlanIdx, topOltVifSubIpSeqIdx)
);

alter table OltVifSubIpConfig comment 'VLAN虚接口子IP配置表';

alter table OltVifSubIpConfig add constraint FK_entity_vifSubIp foreign key (entityId)
      references Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE;

/*==============================================================*/
/* Table: OltIgmpMcForwardingSlotTable                          */
/*==============================================================*/
create table OltIgmpMcForwardingSlotTable
(
   entityId             bigint(20)  not null comment '系统内id 自增长',
   proxyIndex           int not null,
   topMcForwardingSlotCount int(4),
   topMcForwardingSlotList int(4),
   primary key (entityId, proxyIndex)
);

alter table OltIgmpMcForwardingSlotTable comment '组播组槽位映射表';

alter table OltIgmpMcForwardingSlotTable add constraint FK_proxy_slot foreign key (entityId, proxyIndex)
      references OltIgmpProxyParaEntry (entityId, proxyIndex) ON DELETE CASCADE ON UPDATE CASCADE;

/*==============================================================*/
/* Table: OltIgmpMcForwardingPortTable                          */
/*==============================================================*/
create table OltIgmpMcForwardingPortTable
(
   entityId             bigint(20)  not null comment '系统内id 自增长',
   proxyIndex           int not null,
   topMcForwardingPortSlotIndex int not null,
   topMcForwardingPortCount int(4),
   topMcForwardingPortList int(4),
   primary key (entityId, proxyIndex, topMcForwardingPortSlotIndex)
);

alter table OltIgmpMcForwardingPortTable comment '组播组端口映射表';

alter table OltIgmpMcForwardingPortTable add constraint FK_proxy_port foreign key (entityId, proxyIndex)
      references OltIgmpProxyParaEntry (entityId, proxyIndex) ON DELETE CASCADE ON UPDATE CASCADE;

/*==============================================================*/
/* Table: OltIgmpMcForwardingOnuTable                           */
/*==============================================================*/
create table OltIgmpMcForwardingOnuTable
(
   entityId             bigint(20)  not null comment '系统内id 自增长',
   proxyIndex           int not null,
   topMcForwardingOnuSlotIndex int not null,
   topMcForwardingOnuPortIndex int not null,
   topMcForwardingOnuCount int(4),
   topMcForwardingOnuList text,
   primary key (entityId, proxyIndex, topMcForwardingOnuSlotIndex, topMcForwardingOnuPortIndex)
);

alter table OltIgmpMcForwardingOnuTable comment '组播组ONU映射表';

alter table OltIgmpMcForwardingOnuTable add constraint FK_proxy_onu foreign key (entityId, proxyIndex)
      references OltIgmpProxyParaEntry (entityId, proxyIndex) ON DELETE CASCADE ON UPDATE CASCADE;

/*==============================================================*/
/* Table: monitorbaseinfo                                       */
/*==============================================================*/
create table monitorbaseinfo
(
   monitorId            bigint(20) auto_increment not null,
   monitorName          varchar(64),
   monitorDesc          varchar(128),
   monitorType          int,
   monitorTick          int,
   primary key (monitorId)
);

alter table monitorbaseinfo comment '性能监视器表';

/*==============================================================*/
/* Table: monitorportrelation                                   */
/*==============================================================*/
create table monitorportrelation
(
   monitorId            bigint(20),
   entityId             bigint(20),
   entityIp             varchar(32),
   portId               bigint(20),
   portIndex            bigint(20)
);

alter table monitorportrelation comment '监视器端口映射表';

alter table monitorportrelation add constraint FK_monitor_port foreign key (monitorId)
      references monitorbaseinfo (monitorId) ON DELETE CASCADE ON UPDATE CASCADE;

/*==============================================================*/
/* Table: monitorindexrelation                                  */
/*==============================================================*/
create table monitorindexrelation
(
   monitorId            bigint(20),
   monitorIndex         int
);

alter table monitorindexrelation comment '监视器指标映射表';

alter table monitorindexrelation add constraint FK_monitor_index foreign key (monitorId)
      references monitorbaseinfo (monitorId) ON DELETE CASCADE ON UPDATE CASCADE;

/* -- version 1.0.0,build 2011-12-21,module epon */

-- version 1.6.6,build 2012-5-16,module epon
alter table OltSlotAttribute ADD slotNo bigint(20) not null;

/*==============================================================*/
/* Table: EponCurrentStats                           */
/*==============================================================*/
create table EponCurrentStats(
        entityId bigint(20) NOT NULL,
        portIndex bigint(20) NOT NULL,
        curInOctets bigint(20) NOT NULL,
        curInPkts bigint(20) NOT NULL,
        curInBroadcastPkts bigint(20) NOT NULL,
        curInMulticastPkts bigint(20) NOT NULL,
        curInPkts64Octets bigint(20) NOT NULL,
        curInPkts65to127Octets bigint(20) NOT NULL,
        curInPkts128to255Octets bigint(20) NOT NULL,
        curInPkts256to511Octets bigint(20) NOT NULL,
        curInPkts512to1023Octets bigint(20) NOT NULL,
        curInPkts1024to1518Octets bigint(20) NOT NULL,
        curInPkts1519to1522Octets bigint(20) NOT NULL,
        curInUndersizePkts bigint(20) NOT NULL,
        curInOversizePkts bigint(20) NOT NULL,
        curInFragments bigint(20) NOT NULL,
        curInMpcpFrames bigint(20) NOT NULL,
        curInMpcpOctets bigint(20) NOT NULL,
        curInOAMFrames bigint(20) NOT NULL,
        curInOAMOctets bigint(20) NOT NULL,
        curInCRCErrorPkts bigint(20) NOT NULL,
        curInDropEvents bigint(20) NOT NULL,
        curInJabbers bigint(20) NOT NULL,
        curInCollision bigint(20) NOT NULL,
        curOutOctets bigint(20) NOT NULL,
        curOutPkts bigint(20) NOT NULL,
        curOutBroadcastPkts bigint(20) NOT NULL,
        curOutMulticastPkts bigint(20) NOT NULL,
        curOutPkts64Octets bigint(20) NOT NULL,
        curOutPkts65to127Octets bigint(20) NOT NULL,
        curOutPkts128to255Octets bigint(20) NOT NULL,
        curOutPkts256to511Octets bigint(20) NOT NULL,
        curOutPkts512to1023Octets bigint(20) NOT NULL,
        curOutPkts1024to1518Octets bigint(20) NOT NULL,
        curOutPkts1519to1522Octets bigint(20) NOT NULL,
        curOutUndersizePkts bigint(20) NOT NULL,
        curOutOversizePkts bigint(20) NOT NULL,
        curOutFragments bigint(20) NOT NULL,
        curOutMpcpFrames bigint(20) NOT NULL,
        curOutMpcpOctets bigint(20) NOT NULL,
        curOutOAMFrames bigint(20) NOT NULL,
        curOutOAMOctets bigint(20) NOT NULL,
        curOutCRCErrorPkts bigint(20) NOT NULL,
        curOutDropEvents bigint(20) NOT NULL,
        curOutJabbers bigint(20) NOT NULL,
        curOutCollision bigint(20) NOT NULL,
        curStatsTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY (entityId,portIndex)
);


/*==============================================================*/
/* Table: EponStatsRecord                           */
/*==============================================================*/
create table EponStatsRecord(
  entityId  bigint(20),
  portIndex bigint(20),
  collector int,
  primary key(entityId, portIndex)
);


/*Table structure for table EventTypeRelation */
create table EponEventTypeRelation(
   deviceEventTypeId int(11) not null,
   emsEventTypeId int(11) not null,
   module varchar(100) not null,
   type tinyint(1) not null,
   primary key(emsEventTypeId,type)
);


/* -- version 1.6.6,build 2012-5-16,module epon */

-- version 1.7.2,build 2012-6-13,module epon
alter table oltonuattribute add column onuPreType varchar(10) after onuType;
alter table oltauthentication add column onuPreType varchar(10) after onuIndex;
/* -- version 1.7.2,build 2012-6-13,module epon */

-- version 1.7.3,build 2012-7-20,module epon
create table batchConfigRecords(
  operationName  varchar(128),
  entityId  bigint(20),
  userId bigint(20),
  clientIp varchar(20),
  result varchar(64),
  fileName varchar(64),
  operationTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
/* -- version 1.7.3,build 2012-7-20,module epon */


-- version 1.7.4,build 2012-8-20,module epon
alter table oltattribute change inbandPortIndex inbandPortIndex varchar(255);
/* -- version 1.7.4,build 2012-8-20,module epon */



-- version 1.7.5,build 2012-9-6,module epon
/*==============================================================*/
/* Table: OltVlanTransparent                                    */
/*==============================================================*/
create table OltVlanTransparent
(
   entityId             bigint(20),
   ponId                bigint(20) not null comment '系统内id 自增长',
   ponIndex             bigint(20),
   transparentId        text,
   transparentMode      text,
   primary key (ponId)
);

alter table OltVlanTransparent comment 'OltVlanTransparent';
alter table OltVlanTransparent add constraint FK_vlan_olt_transparent foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;
/* -- version 1.7.5,build 2012-9-6,module epon */

-- version 1.7.5,build 2012-9-20,module epon
/*==============================================================*/
/* Table: OnuAutoUpgProfile && OnuAutoUpgBand                   */
/*==============================================================*/
create table OnuAutoUpgProfile
(
   entityId             bigint(20),
   profileId            int(2) not null,
   proName              varchar(32),
   proOnuType           int(4),
   proHwVersion         varchar(10),
   proMode                              tinyint,
   proNewVersion                varchar(32),
   proUpgTime                   varchar(12),
   boot                                 varchar(255),
   app                                  varchar(255),
   webs                                 varchar(255),
   other                                varchar(255),
   pers                                 varchar(255),
   proPri                               int(2),
   proBandStat                  tinyint,
   primary key (profileId)
);

alter table OnuAutoUpgProfile comment 'OnuAutoUpgProfile';

create table OnuAutoUpgBand
(
   entityId             bigint(20),
   profileId            int(2) not null,
   ponId                                bigint(20),
   ponIndex             bigint(20),
   installStat                  tinyint
);
alter table OnuAutoUpgBand comment 'OnuAutoUpgBand';
alter table OnuAutoUpgBand add constraint FK_epon_onu_autoUpg_profileId foreign key (profileId)
      references OnuAutoUpgProfile (profileId) on delete cascade on update cascade;
alter table OnuAutoUpgBand add constraint FK_epon_onu_autoUpg_ponId foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

/* -- version 1.7.5,build 2012-9-20,module epon */

-- version 1.7.7.2,build 2012-10-10,module epon
-- ----------------------------
-- Table structure for topponprotectparamtemplate
-- ----------------------------
CREATE TABLE topponprotectparamtemplate (
  ponPSGrpIndex int(11) NOT NULL DEFAULT '0',
  ponPSWorkPort bigint(20) DEFAULT NULL,
  ponPSStandbyPort bigint(20) DEFAULT NULL,
  entityId bigint(20) DEFAULT NULL,
  alias varchar(256) DEFAULT NULL,
  PRIMARY KEY (ponPSGrpIndex)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/* -- version 1.7.7.2,build 2012-9-20,module epon */
-- version 1.7.7.3,build 2012-11-7,module epon
-- ----------------------------
-- Table structure for topponpsgrpconfig
-- ----------------------------
CREATE TABLE topponpsgrpconfig (
  topPonPSGrpIndex int(5) NOT NULL DEFAULT 0,
  entityId bigint(20) NOT NULL DEFAULT 0,
  alias varchar(20) DEFAULT NULL,
  topPonPsGrpAdmin int(2) DEFAULT NULL,
  topPonPSWorkPortIndex bigint(20) DEFAULT NULL,
  topPonPSWorkPortItem varchar(20) DEFAULT NULL,
  topPonPsWorkPortStatus int(2) DEFAULT NULL,
  topPonPSStandbyPortItem varchar(20) DEFAULT NULL,
  topPonPSStandbyPortIndex bigint(20) DEFAULT NULL,
  topPonPsStandbyPortStatus int(2) DEFAULT NULL,
  topPonPsTimes int(11) DEFAULT NULL,
  topPonPsLastSwitchTime bigint(20) DEFAULT NULL,
  topPonPsReason varchar(20) DEFAULT NULL,
  PRIMARY KEY (topPonPSGrpIndex,entityId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/* -- version 1.7.7.3,build 2012-11-7,module epon */

-- version 1.7.7.3,build 2012-11-9,module epon
/*==============================================================*/
/* Table: OltPonOptical && OltSniOptical && OnuPonOptical       */
/*==============================================================*/
create table OltPonOptical
(
   entityId             bigint(20),
   ponId                bigint(20) not null comment '系统内id 自增长',
   portIndex            bigint(20),
   identifier           int,
   vendorName                   varchar(16),
   waveLength                   varchar(16),
   vendorPN                             varchar(16),
   vendorsN                             varchar(16),
   dateCode                             varchar(32),
   workingTemp          int,
   workingVoltage       int,
   biasCurrent          int,
   txPower                      int,
   rxPower                  int,
   primary key (ponId)
);

alter table OltPonOptical comment 'OltPonOptical';
alter table OltPonOptical add constraint FK_optical_pon foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;

create table OltSniOptical
(
   entityId             bigint(20),
   sniId                bigint(20) not null comment '系统内id 自增长',
   portIndex            bigint(20),
   identifier           int,
   vendorName                   varchar(16),
   waveLength                   varchar(16),
   vendorPN                             varchar(16),
   vendorsN                             varchar(16),
   dateCode                             varchar(32),
   workingTemp          int,
   workingVoltage       int,
   biasCurrent          int,
   txPower                      int,
   rxPower                  int,
   primary key (sniId)
);

alter table OltSniOptical comment 'OltSniOptical';
alter table OltSniOptical add constraint FK_optical_sni foreign key (sniId)
      references OltSniRelation (sniId) on delete cascade on update cascade;

create table OnuPonOptical
(
   entityId             bigint(20),
   onuPonId             bigint(20) not null comment '系统内id 自增长',
   onuPonIndex          bigint(20),
   workingTemp          int,
   workingVoltage       int,
   biasCurrent          int,
   txPower                      int,
   rxPower                  int,
   primary key (onuPonId)
);

alter table OnuPonOptical comment 'OnuPonOptical';
alter table OnuPonOptical add constraint FK_optical_onupon foreign key (onuPonId)
      references OltOnuPonRelation (onuPonId) on delete cascade on update cascade;
/* -- version 1.7.7.3,build 2012-11-9,module epon */

-- version 1.7.7.4,build 2012-11-20,module epon
alter table OltUniAttribute add uniDSLoopBackEnable int;
alter table OltUniAttribute add uniUSUtgPri int;

alter table OltPonAttribute add ponBandMax int;

alter table OltUniStormInfo change uniUnicastStormInPacketRate uniUnicastStormInPacketRate bigint(20);
alter table OltUniStormInfo change uniUnicastStormOutPacketRate uniUnicastStormOutPacketRate bigint(20);
alter table OltUniStormInfo change uniMulticastStormInPacketRate uniMulticastStormInPacketRate bigint(20);
alter table OltUniStormInfo change uniMulticastStormOutPacketRate uniMulticastStormOutPacketRate bigint(20);
alter table OltUniStormInfo change uniBroadcastStormInPacketRate uniBroadcastStormInPacketRate bigint(20);
alter table OltUniStormInfo change uniBroadcastStormOutPacketRate uniBroadcastStormOutPacketRate bigint(20);


/*==============================================================*/
/* Table: OltOnuComVlanConfig                                        */
/*==============================================================*/
create table OltOnuComVlanConfig(
entityId bigint(20),
onuComVlan int,
primary key(entityId)
);

alter table OltOnuComVlanConfig add constraint FK_ComVlan_Entity foreign key (entityId) references Entity (entityId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: OltOnuComRelation                                        */
/*==============================================================*/
create table OltOnuComRelation(
   onuComId             bigint(20) auto_increment not null,
   onuId                bigint(20) not null,
   onuComIndex          bigint(20) not null,
   entityId             bigint(20),
   constraint uk_onucom_relation unique(onuComIndex, entityId),
   primary key (onuComId)
);
alter table OltOnuComRelation add constraint FK_onu_com foreign key (onuId)
      references OltOnuRelation (onuId) on delete cascade on update cascade;

/*==============================================================*/
/* Table: OltOnuComAttribute                                     */
/*==============================================================*/
create table OltOnuComAttribute(
   entityId bigint(20),
   onuComId bigint(20),
   onuComIndex bigint(20),
   onuComInfoComDesc varchar(100),
   onuComInfoComType tinyint,
   onuComInfoBuad tinyint,
   onuComInfoDataBits int,
   onuComInfoStartBits int,
   onuComInfoStopBits int,
   onuComInfoParityType tinyint,
   onuComInfoMainRemoteIp varchar(20),
   onuComInfoMainRemotePort int,
   onuComInfoBackRemoteIp varchar(20),
   onuComInfoBackRemotePort int,
   onuComInfoSrvType tinyint,
   onuComInfoSrvPort int,
   onuComInfoClientNum int,
   primary key(onuComId)
);
alter table OltOnuComAttribute add constraint FK_onu_comatt foreign key (onuComId)
      references OltOnuComRelation (onuComId) on delete cascade on update cascade;

alter table oltonuattribute add topOnuMgmtIp varchar(20);
alter table oltonuattribute add topOnuNetMask varchar(20);
alter table oltonuattribute add topOnuGateway varchar(20);

/*==============================================================*/
/* Table: OltOnuMacMgmt                                        */
/*==============================================================*/
create table OltOnuMacMgmt(
   onuId                bigint(20) not null,
   onuIndex             bigint(20) not null,
   entityId             bigint(20),
   topOnuMacList                varchar(384),
   topOnuMacMark        int,
   mgmtEnable           int,
   primary key (onuId)
);
alter table OltOnuMacMgmt add constraint FK_onu_macMgmt foreign key (onuId)
      references OltOnuRelation (onuId) on delete cascade on update cascade;
/* -- version 1.7.7.4,build 2012-11-20,module epon */

-- version 1.7.7.4,build 2012-12-23,module epon
CREATE TABLE topigmpforwardingsnooping (
  entityId bigint(20),
  vid int(11),
  portIndex bigint(20),
  lastchangetime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
alter table topigmpforwardingsnooping comment 'IGMP SNOOPING表';
alter table topigmpforwardingsnooping add constraint FK_igmp_forwardingsnooping foreign key (entityId)
      references Entity (entityId) on delete cascade;
/* -- version 1.7.7.4,build 2012-12-23,module epon */

-- version 1.7.7.4,build 2012-12-28,module epon
CREATE TABLE oltmacaddresslearn (
  entityId bigint(20),
  topSysMacAddr varchar(254),
  topSysMacVid int(11),
  topSysMacSlot int(11),
  topSysMacPort int(11),
  topSysMacFlag int(11),
  lastChangeTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
alter table oltmacaddresslearn add constraint FK_mac_address foreign key (entityId)
      references Entity (entityId) on delete cascade;
/* -- version 1.7.7.4,build 2012-12-28,module epon */

-- version 1.7.8.0,build 2013-1-29,module epon
alter table oltonuupgrade add column upgradeTime timestamp after topOnuUpgradeStatus;
/* -- version 1.7.8.0,build 2013-1-29,module epon */


-- version 1.7.10.0,build 2013-2-26,module epon
create table oltDeviceUpTime(
  entityId bigint(20) NOT NULL,
  onuId bigint(20),
  deviceUpTime bigint(20) NOT NULL,
  collectTime bigint(20) NOT NULL
);
alter table oltDeviceUpTime add constraint FK_entity_oltDeviceUpTime foreign key (entityId)
      references Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE;

create table oltDeviceReStartTime(
  entityId bigint(20) NOT NULL,
  onuId bigint(20),
  deviceLastOnlineTime bigint(20) NOT NULL,
  deviceReStartTime bigint(20) NOT NULL,
  collectTime bigint(20) NOT NULL
);
alter table oltDeviceReStartTime add constraint FK_entity_oltDeviceReStartTime foreign key (entityId)
      references Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE;
/* -- version 1.7.10.0,build 2013-2-26,module epon */

-- version 1.7.13.3,build 2013-5-9,module epon
CREATE TABLE oltdhcprelaybaseconfig
(
    entityId                    bigint(20) not null,
    topCcmtsDhcpRelaySwitch     tinyint,
    primary key (entityId),
    CONSTRAINT FK_oltdhcpbase_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE oltdhcpbundle
(
    entityId               bigint(20) not null,
    topCcmtsDhcpBundleInterface varchar(10),
    topCcmtsDhcpBundlePolicy    tinyint,
    cableSourceVerify tinyint,
    virtualPrimaryIpAddr varchar(32),
    virtualPrimaryIpMask varchar(32),
    topCcmtsDhcpBundleVlanMap   varchar(1536),
    primary key (entityId, topCcmtsDhcpBundleInterface),
    CONSTRAINT FK_oltdhcpbundle_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE oltdhcpOption60
(
    entityId               bigint(20) not null,
    option60Id          bigint(20) auto_increment not null,
    topCcmtsDhcpBundleInterface varchar(10) not null,
    topCcmtsDhcpOption60DeviceType    tinyint,
    topCcmtsDhcpOption60Index         tinyint not null,
    topCcmtsDhcpOption60Str           varchar(100),
    deviceTypeStr                     varchar(20),
    primary key (option60Id),
    index(entityId,topCcmtsDhcpBundleInterface),
    CONSTRAINT FK_oltdhcpOption60_bundle FOREIGN KEY (entityId, topCcmtsDhcpBundleInterface) REFERENCES oltdhcpbundle (entityId,topCcmtsDhcpBundleInterface) ON DELETE CASCADE ON UPDATE CASCADE
);
create table oltdhcpgiaddrconfig
(
   entityId                bigint(20) not null,
   giaddrId             bigint(20) auto_increment not null,
   topCcmtsDhcpBundleInterface varchar(10) not null,
   topCcmtsDhcpGiAddrDeviceType tinyint,
   topCcmtsDhcpGiAddress varchar(15),
   topCcmtsDhcpGiAddrMask varchar(15),
   deviceTypeStr                     varchar(20),
   primary key (giaddrId),
   index(entityId,topCcmtsDhcpBundleInterface),
   CONSTRAINT FK_oltdhcpGiaddr_bundle FOREIGN KEY (entityId,topCcmtsDhcpBundleInterface) REFERENCES oltdhcpbundle (entityId,topCcmtsDhcpBundleInterface) ON DELETE CASCADE ON UPDATE CASCADE
);
create table oltdhcpserverconfig
(
   entityId                bigint(20) not null,
   helperId             bigint(20) auto_increment not null,
   topCcmtsDhcpBundleInterface varchar(10) not null,
   topCcmtsDhcpHelperDeviceType tinyint,
   topCcmtsDhcpHelperIndex tinyint not null,
   topCcmtsDhcpHelperIpAddr varchar(15),
   deviceTypeStr                     varchar(20),
   primary key (helperId),
   index(entityId,topCcmtsDhcpBundleInterface),
   CONSTRAINT FK_oltdhcpServer_bundle FOREIGN KEY (entityId,topCcmtsDhcpBundleInterface) REFERENCES oltdhcpbundle (entityId,topCcmtsDhcpBundleInterface) ON DELETE CASCADE ON UPDATE CASCADE
);
 create table oltDhcpIntIpTable
(
   entityId                bigint(20) not null,
   topCcmtsDhcpBundleInterface  varchar(10) not null,
   topCcmtsDhcpIntIpIndex int(20) not null,
   topCcmtsDhcpIntIpAddr varchar(30),
   topCcmtsDhcpIntIpMask varchar(30),
   primary key (entityId, topCcmtsDhcpBundleInterface, topCcmtsDhcpIntIpIndex),
   CONSTRAINT FK_oltDhcpIntIpTable_bundle FOREIGN KEY (entityId,topCcmtsDhcpBundleInterface) REFERENCES oltdhcpbundle (entityId,topCcmtsDhcpBundleInterface) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 1.7.13.3,build 2013-5-9,module epon */

-- version 1.7.13.3,build 2013-6-13,module epon
create table OltPonPortSpeed
(
   entityId             bigint(20),
   slotId                               bigint(20),
   ponId                bigint(20) not null comment '系统内id 自增长',
   ponIndex             bigint(20),
   ponPortSpeedMod          int,
   primary key (ponId)
);
alter table OltPonPortSpeed add constraint FK_speed_pon foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;
/* -- version 1.7.13.3,build 2013-6-13,module epon */

-- version 1.7.13.3,build 2013-06-28,module epon
CREATE TABLE perfstats15tableSummary(
        entityId bigint(20) NOT NULL,
        portIndex bigint(20) NOT NULL,
        stats15InOctets bigint(20),
        stats15OutOctets bigint(20),
        stats15InOctetsMax bigint(20),
        stats15OutOctetsMax bigint(20),
        stats15EndTime timestamp DEFAULT CURRENT_TIMESTAMP,
        summarized int(1) default 0,
        primary key (entityId, portIndex, stats15EndTime,summarized)
);
/* -- version 1.7.13.3,build 2013-06-28,module epon */

-- version 1.7.13.3,build 2013-7-5,module epon
alter table oltponoptical add column bitRate int after rxPower;
alter table OltSniOptical add column bitRate int after rxPower;
/* -- version 1.7.13.3,build 2013-7-5,module epon */

-- version 1.7.13.3,build 2013-7-20,module epon
create table perfeponservicequality(
	entityId bigint(20) not null,
	slotIndex bigint(20) not null,
	targetName varchar(10) not null,
	targetValue decimal(12,2),
	collectTime timestamp,
	CONSTRAINT FK_perfeponservicequality_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE perfeponflowquality (                                                      
    entityId bigint(20) NOT NULL,                                                         
    portIndex bigint(20) NOT NULL,   
    portInOctets decimal(12,2) DEFAULT NULL,                                                 
    portOutOctets decimal(12,2) DEFAULT NULL,  
    portInSpeed decimal(12,2) DEFAULT NULL,
    portOutSpeed decimal(12,2) DEFAULT NULL,
    collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key(entityId,portIndex,collectTime),
    CONSTRAINT FK_perfeponflowquality_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);  

create table perfeponlinkquality(
	entityId bigint(20) not null,
	portIndex bigint(20) not null,
	optTxPower decimal(12,2),
	optRePower decimal(12,2),
	optCurrent decimal(12,2),
	optVoltage decimal(12,2),
	optTemp    decimal(12,2),
	collectTime timestamp,
	CONSTRAINT FK_perfeponlinkquality_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/* -- version 1.7.13.3,build 2013-7-20,module epon */

-- version 1.7.13.3,build 2013-8-28,module epon
alter table oltattribute add column topSysArpAgingTime int after sniMacAddrTableAgingTime ;
alter table oltuniattribute add column topUniLoopDetectEnable int after macAge ;
CREATE TABLE oltSysOpticalAlarm (
	entityId bigint(20),
	topSysOptAlarmIndex  int(4),
	topSysOptAlarmSwitch  tinyint(1) ,
	topSysOptAlarmSoapTime  int(4),
	PRIMARY KEY (entityId,topSysOptAlarmIndex)
);
CREATE TABLE oltPonOpticalAlarm (
	entityId bigint(20),
	topPonOptCardIndex  int(2),
	topPonOptPonIndex  int(11),
	topPonOptAlarmIndex  int(4),
	topPonOptThresholdValue  int(11),
	topPonOptAlarmState  int(11),
	topPonOptAlarmCount  int(11),
	PRIMARY KEY (entityId,topPonOptCardIndex, topPonOptPonIndex, topPonOptAlarmIndex)
);
CREATE TABLE oltOnuOpticalAlarm (
	entityId bigint(20),
	topOnuOptCardIndex  int(11) ,
	topOnuOptPonIndex  int(11) ,
	topOnuOptOnuIndex  int(11) ,
	topOnuOptAlarmIndex  int(11) ,
	topOnuOptThresholdValue  int(11),
	topOnuOptAlarmState  int(11),
	topOnuOptAlarmCount  int(11),
	PRIMARY KEY (entityId,topOnuOptCardIndex, topOnuOptPonIndex, topOnuOptOnuIndex, topOnuOptAlarmIndex)
);
/*-- version 1.7.13.3,build 2013-8-28,module epon*/

-- version 1.7.14.0,build 2013-9-7,module epon
create table perfeponservicequalitylast(
	entityId bigint(20) not null,
	slotIndex bigint(20) not null,
	targetName varchar(10) not null,
	targetValue decimal(12,2),
	collectTime timestamp,
	primary key(entityId,slotIndex,targetName),
	CONSTRAINT FK_perfeponservicequality_entity_last FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE perfeponflowqualitylast (                                                      
    entityId bigint(20) NOT NULL,                                                         
    portIndex bigint(20) NOT NULL,   
    portInOctets decimal(12,2) DEFAULT NULL,                                                 
    portOutOctets decimal(12,2) DEFAULT NULL,  
    portInSpeed decimal(12,2) DEFAULT NULL,
    portOutSpeed decimal(12,2) DEFAULT NULL,
    collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key(entityId,portIndex),
    CONSTRAINT FK_perfeponflowquality_entity_last FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);  

create table perfeponlinkqualitylast(
	entityId bigint(20) not null,
	portIndex bigint(20) not null,
	optTxPower decimal(12,2),
	optRePower decimal(12,2),
	optCurrent decimal(12,2),
	optVoltage decimal(12,2),
	optTemp    decimal(12,2),
	collectTime timestamp,
	primary key(entityId,portIndex),
	CONSTRAINT FK_perfeponlinkquality_entity_last FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 1.7.14.0,build 2013-9-7,module epon */

-- version 2.0.0.1,build 2013-11-30,module epon
create table oltIpRoute(
    entityId bigint(20),
    ipAddressDst varchar(20),
    ipMaskDst varchar(20),
    nextHop varchar(20),
    distance int,
    track tinyint,
    routeType tinyint,
    flag tinyint,
    primary key(entityId,ipAddressDst,ipMaskDst,nextHop),
    CONSTRAINT FK_IpRoute_ENTITY FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE 
);

create table oltloopbackconfig(
    entityId bigint(20),
    loopbackIndex int,
    loopbackPriIpAddr varchar(20),
    loopbackPriMask varchar(20),
    loopbackPriMac varchar(20),
    primary key(entityId,loopbackIndex),
    CONSTRAINT FK_LoopBackConfig_ENTITY FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE 
);

create table oltloopbacksub(
    entityId bigint(20),
    loopbackSubIpIndex int,
    loopbackSubIpSeqIndex int,
    loopbackSubIpAddr varchar(20),
    loopbackSubMask varchar(20),
    loopbackSubMac varchar(20),
    primary key(entityId,loopbackSubIpIndex,loopbackSubIpSeqIndex),
    CONSTRAINT FK_LoopBackSubIndex_ENTITY FOREIGN KEY (entityId,loopbackSubIpIndex) REFERENCES oltloopbackconfig(entityId,loopbackIndex) ON DELETE CASCADE ON UPDATE CASCADE 
);

/* -- version 2.0.0.1 ,build 2013-11-30,module epon */

-- version 2.0.0.1,build 2013-11-30,module epon
create table uniVlanProfile(
    entityId bigint(20),
    profileId int,
    profileRefCnt int,
    profileName varchar(32),
    profileMode int,
    primary key(entityId,profileId),
    CONSTRAINT FK_UniVlanProfile_ENTITY FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE 
);

create table uniVlanRuleTable(
    entityId bigint(20),
    ruleProfileIndex int,
    ruleIndex int,
    ruleMode int,
    ruleCvlan varchar(64),
    ruleSvlan int,
    primary key(entityId,ruleProfileIndex,ruleIndex),
    CONSTRAINT FK_UniVlanRuleTable_ProfileIndex FOREIGN KEY (entityId,ruleProfileIndex) REFERENCES uniVlanProfile(entityId,profileId) ON DELETE CASCADE ON UPDATE CASCADE 
);

create table UniVlanBindTable(
    entityId bigint(20),
    uniIndex bigint(20),
    bindPvid int,
    bindProfileId int,
    primary key(entityId,uniIndex),
    CONSTRAINT FK_UniVlanBindTable_ENTITY FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_UniVlanBindTableId_UniIndex FOREIGN KEY (uniIndex) REFERENCES oltunirelation(uniIndex) ON DELETE CASCADE ON UPDATE CASCADE 
);
/* -- version 2.0.0.1 ,build 2013-11-30,module epon */

-- version 2.0.0.1,build 2013-12-3,module epon
create table CcmtsFftMonitorScalar
(
   entityId             bigint(20) auto_increment,
   fftMonitorGlbStatus  int,
   primary key (entityId)
);
alter table CcmtsFftMonitorScalar add constraint FK_olt_spectrum foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;
/* -- version 2.0.0.1,build 2013-12-3,module epon */

-- version 2.0.0.1,build 2013-12-10,module epon
CREATE TABLE camerafilter (
	entityid  bigint(20),
	eponIndex  bigint(20),
	cameraIndex  int,
	ip  varchar(255),
	mac  varchar(255),
	PRIMARY KEY (entityid, eponIndex, cameraIndex),
	CONSTRAINT FK_CAMERA_ENTITY FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 2.0.0.1,build 2013-12-10,module epon*/

-- version 2.0.3.0,build 2013-12-10,module epon
alter table OltDeviceRestartTime add column deviceUptime bigint(20) default 0;
alter table OltDeviceRestartTime add column isOffline tinyint(1) default 0;
/* -- version 2.0.3.0,build 2013-12-10,module epon */

-- version 2.0.3.0,build 2013-12-16,module epon
CREATE TABLE oltFileDir (
	entityid  bigint(20),
	fileDirType  int,
	fileDirPath  varchar(255),
	fileDirAttr  int,
	PRIMARY KEY (entityid, fileDirType),
	CONSTRAINT FK_OltFileDir_ENTITY FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 2.0.3.0,build 2013-12-16,module epon*/
-- version 2.0.3.0,build 2013-12-17,module epon
CREATE TABLE batchdeployrecords (
	batchDeployId  int(11)  AUTO_INCREMENT ,
	typeId  int(11)  ,
	entityId  bigint(20)  DEFAULT 0 ,
	operator  varchar(255),
	sucessCount  int(11) ,
	failureCount  int(11),
	duration  bigint(20),
	matches  varchar(255),
	startTime  timestamp  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
	comment  varchar(255),
	PRIMARY KEY (batchDeployId, typeId, entityId),
	INDEX(typeId,entityId),
	CONSTRAINT FK_BATCH_ENTITY FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE batchdeploydetail (
	batchDeployId  int(11)  DEFAULT 100000 ,
	success  blob,
	failures  blob,
	PRIMARY KEY (batchDeployId),
	CONSTRAINT FK_BATCH_ID FOREIGN KEY (batchDeployId) REFERENCES batchdeployrecords(batchDeployId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 2.0.3.0,build 2013-12-17,module epon*/

-- version 2.0.3.0,build 2013-12-27,module epon
CREATE TABLE cameraphysicalinfo (
  mac varchar(255),
  type varchar(255),
  note varchar(255),
  PRIMARY KEY (mac)
);
CREATE TABLE cameraplan (
  cameraNo varchar(255),
  location varchar(255),
  ip varchar(255),
  PRIMARY KEY (ip),
  UNIQUE INDEX UNI_IDX_CNO (cameraNo)
);
/*-- version 2.0.3.0,build 2013-12-27,module epon*/

-- version 2.0.3.0,build 2014-1-16,module epon
drop table OnuAutoUpgBand;
drop table OnuAutoUpgProfile;
create table OnuAutoUpgProfile
(
   entityId             bigint(20),
   profileId            int(2) not null,
   proName              varchar(32),
   proOnuType           int(4),
   proHwVersion         varchar(10),
   proMode                              tinyint,
   proNewVersion                varchar(32),
   proUpgTime                   varchar(12),
   boot                                 varchar(255),
   app                                  varchar(255),
   webs                                 varchar(255),
   other                                varchar(255),
   pers                                 varchar(255),
   proPri                               int(2),
   proBandStat                  tinyint,
   primary key (entityId,profileId)
);
alter table OnuAutoUpgProfile comment 'OnuAutoUpgProfile';
alter table OnuAutoUpgProfile add constraint FK_epon_utoUpg_profileId foreign key (entityId)
      references entity (entityId) on delete cascade on update cascade;

create table OnuAutoUpgBand
(
   entityId             bigint(20),
   profileId            int(2) not null,
   ponId                bigint(20),
   ponIndex             bigint(20),
   installStat          tinyint
);
alter table OnuAutoUpgBand comment 'OnuAutoUpgBand';
alter table OnuAutoUpgBand add constraint FK_epon_onu_autoUpg_profileId foreign key (entityId,profileId)
      references OnuAutoUpgProfile (entityId,profileId) on delete cascade on update cascade;
alter table OnuAutoUpgBand add constraint FK_epon_onu_autoUpg_ponId foreign key (ponId)
      references OltPonRelation (ponId) on delete cascade on update cascade;
/*-- version 2.0.3.0,build 2014-1-16,module epon*/

-- version 2.0.3.0,build 2014-2-8,module epon
alter table perfeponflowquality modify column portInOctets bigint(20) DEFAULT NULL;
alter table perfeponflowquality modify column portOutOctets bigint(20) DEFAULT NULL;
alter table perfeponflowqualitylast modify column portInOctets bigint(20) DEFAULT NULL;
alter table perfeponflowqualitylast modify column portOutOctets bigint(20) DEFAULT NULL;
alter table perfeponflowquality modify column portInSpeed decimal(20,2) DEFAULT NULL;
alter table perfeponflowquality modify column portOutSpeed decimal(20,2) DEFAULT NULL;
alter table perfeponflowqualitylast modify column portInSpeed decimal(20,2) DEFAULT NULL;
alter table perfeponflowqualitylast modify column portOutSpeed decimal(20,2) DEFAULT NULL;
/* -- version 2.0.3.0,build 2014-2-8,module epon */


-- version 2.0.3.0,build 2014-3-13,module epon

create table perfEponCpuQuality(
entityId bigint(20) not null,
slotIndex bigint(20) not null,
collectValue decimal(10,2) not null,
collectTime timestamp not null,
primary key(entityId,slotIndex,collectTime),
CONSTRAINT FK_perfeponcpuquality_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);


create table perfEponMemQuality(
entityId bigint(20) not null,
slotIndex bigint(20) not null,
collectValue decimal(10,2) not null,
collectTime timestamp not null,
primary key(entityId,slotIndex,collectTime),
CONSTRAINT FK_perfeponmemquality_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfEponFlashQuality(
entityId bigint(20) not null,
slotIndex bigint(20) not null,
collectValue decimal(10,2) not null,
collectTime timestamp not null,
primary key(entityId,slotIndex,collectTime),
CONSTRAINT FK_perfeponflashquality_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfEponBoardTempQuality(
entityId bigint(20) not null,
slotIndex bigint(20) not null,
collectValue decimal(10,2) not null,
collectTime timestamp not null,
primary key(entityId,slotIndex,collectTime),
CONSTRAINT FK_perfeponboardtempquality_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfEponFanSpeedQuality(
entityId bigint(20) not null,
slotIndex bigint(20) not null,
collectValue decimal(10,2) not null,
collectTime timestamp not null,
primary key(entityId,slotIndex,collectTime),
CONSTRAINT FK_perfeponfanspeedquality_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/* -- version 2.0.3.0,build 2014-3-13,module epon */

-- version 2.0.3.0,build 2014-5-15,module epon
CREATE TABLE uniratelimittemplate (
  templateId int(11) NOT NULL AUTO_INCREMENT,
  templateName varchar(64) NOT NULL,
  entityId bigint(20) NOT NULL,
  portInLimitEnable tinyint(4) NOT NULL,
  portInCIR int(11) DEFAULT NULL,
  portInCBS int(11) DEFAULT NULL,
  portInEBS int(11) DEFAULT NULL,
  portOutLimtEnable tinyint(4) NOT NULL,
  portOutCIR int(11) DEFAULT NULL,
  portOutPIR int(11) DEFAULT NULL,
  createTime datetime DEFAULT NULL,
  updateTime datetime DEFAULT NULL,
  PRIMARY KEY (templateId),
  KEY FK_uniRateTemplate (entityId),
  CONSTRAINT FK_uniRateTemplate FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.0.3.0,build 2014-5-15,module epon */

-- version 2.3.2.0,build 2014-9-25,module epon
CREATE TABLE oltslotmaptable (
  entityId bigint(20) NOT NULL,
  slotPhyNo int(11) NOT NULL,
  slotLogNo int(11) NOT NULL,
  PRIMARY KEY (entityId,slotPhyNo),
  CONSTRAINT FK_slotMap_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.3.2.0,build 2014-9-25,module epon */

-- version 2.3.2.0,build 2014-10-15,module epon
CREATE TABLE perfeponflowqualitysummaryorigin (
  entityId bigint(20) NOT NULL,
  portIndex bigint(20) NOT NULL,
  portInSpeed decimal(20,2) DEFAULT NULL,
  portOutSpeed decimal(20,2) DEFAULT NULL,
  collectTime timestamp NOT NULL,       
  index ix_eponflow_origin (entityId, portIndex, collectTime)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
CREATE TABLE perfeponflowqualitySummary (
  entityId bigint(20) NOT NULL,
  portIndex bigint(20) NOT NULL,
  portType tinyint NOT NULL,
  portInSpeedAvg decimal(20,2),
  portInSpeedMin decimal(20,2),
  portInSpeedMax decimal(20,2),
  portOutSpeedAvg decimal(20,2),
  portOutSpeedMin decimal(20,2),
  portOutSpeedMax decimal(20,2),
  portSpeedAvg decimal(20,2),
  portSpeedMin decimal(20,2),
  portSpeedMax decimal(20,2),
  summarizeTime timestamp,
  PRIMARY KEY (entityId, portIndex, summarizeTime) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;  
  
create view epon_portIndex_type as 
select entityId,sniIndex portIndex,1 portType from oltsnirelation
union 
select entityId,ponIndex portIndex,2 portType from oltponrelation;
/* -- version 2.3.2.0,build 2014-10-15,module epon */

-- version 2.4.2.0,build 2014-10-18,module epon
alter table oltslotstatus modify topSysBdlMemSize bigint(20);
alter table oltslotstatus modify topSysBdFreeMemSize bigint(20);
/* -- version 2.4.2.0,build 2014-10-18,module epon */

-- version 2.4.3.0,build 2014-10-24,module epon
alter table perfeponflowquality add column portInUsed decimal(12,4);
alter table perfeponflowquality add column portOutUsed decimal(12,4);
alter table perfeponflowquality add column portBandwidth bigint(20);
alter table perfeponflowqualitylast add column portInUsed decimal(12,4);
alter table perfeponflowqualitylast add column portOutUsed decimal(12,4);
alter table perfeponflowqualitylast add column portBandwidth bigint(20);
/* -- version 2.4.3.0,build 2014-10-24,module epon */

-- version 2.4.5.0,build 2014-11-11,module epon
alter table perfstats15tableSummary add column portInSpeed decimal(20,2);
alter table perfstats15tableSummary add column portInSpeedMax decimal(20,2);
alter table perfstats15tableSummary add column portOutSpeed decimal(20,2);
alter table perfstats15tableSummary add column portOutSpeedMax decimal(20,2);
alter table perfstats15tableSummary add column portInUsed decimal(12,4);
alter table perfstats15tableSummary add column portInUsedMax decimal(12,4);
alter table perfstats15tableSummary add column portOutUsed decimal(12,4);
alter table perfstats15tableSummary add column portOutUsedMax decimal(12,4);
alter table perfstats15tableSummary add column portBandwidth bigint(20);
/* -- version 2.4.5.0,build 2014-11-11,module epon */

-- version 2.4.5.0,build 2014-12-06,module epon
ALTER TABLE oltonuattribute ADD onuMac VARCHAR(32) DEFAULT NULL;
/* -- version 2.4.5.0,build 2014-12-06,module epon */

-- version 2.4.5.0,build 2014-12-20,module epon
CREATE TABLE portisolationgroup(
  entityId bigint(20) NOT NULL,
  groupIndex int(11) NOT NULL,
  groupDesc varchar(32) DEFAULT NULL,
  PRIMARY KEY (entityId,groupIndex),
  CONSTRAINT FK_isolationGrp FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE portisolationmember (
  entityId bigint(20) NOT NULL,
  portIndex bigint(20) NOT NULL,
  groupIndex int(11) NOT NULL,
  PRIMARY KEY (entityId,portIndex,groupIndex),
  KEY FK_grpMember (entityId,groupIndex),
  CONSTRAINT FK_grpMember FOREIGN KEY (entityId, groupIndex) REFERENCES portisolationgroup (entityId, groupIndex) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* -- version 2.4.5.0,build 2014-12-20,module epon */

-- version 2.5.2.0,build 2015-4-27,module epon
CREATE TABLE perfonulinkquality(
  entityId bigint(20) NOT NULL,
  onuId bigint(20) NOT NULL,
  onuIndex bigint(20) NOT NULL,
  onuPonRevPower decimal(12,2) DEFAULT NULL,
  onuPonTransPower decimal(12,2) DEFAULT NULL,
  oltPonRevPower decimal(12,2) DEFAULT NULL,
  collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (onuId,collectTime),
  CONSTRAINT FK_perfOnulinkQuality FOREIGN KEY (onuId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE perfonulinkqualitylast (
  entityId bigint(20) NOT NULL,
  onuId bigint(20) NOT NULL,
  onuIndex bigint(20) NOT NULL,
  onuPonRevPower decimal(12,2) DEFAULT NULL,
  onuPonTransPower decimal(12,2) DEFAULT NULL,
  oltPonRevPower decimal(12,2) DEFAULT NULL,
  collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (onuId),
  CONSTRAINT FK_perfOnulinkqualitylast FOREIGN KEY (onuId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE onuonlinestatus(
	entityId bigint(20) NOT NULL,
	onuId bigint(20) NOT NULL,
	onuIndex bigint(20) NOT NULL,
	onlineStatus int DEFAULT NULL,
	collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (onuId,collectTime),
	CONSTRAINT FK_onuonlinestatus FOREIGN KEY (onuId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE perfonuflowquality (
  onuId bigint(20) NOT NULL,
  portIndex bigint(20) NOT NULL,
  entityId bigint(20) NOT NULL,
  portInOctets bigint(20) DEFAULT NULL,
  portOutOctets bigint(20) DEFAULT NULL,
  portInSpeed decimal(20,2) DEFAULT NULL,
  portOutSpeed decimal(20,2) DEFAULT NULL,
  collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (onuId,portIndex,collectTime),
  CONSTRAINT FK_perfonuflowquality FOREIGN KEY (onuId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE perfonuflowqualitylast (
  onuId bigint(20) NOT NULL,
  portIndex bigint(20) NOT NULL,
  entityId bigint(20) NOT NULL,
  portInOctets bigint(20) DEFAULT NULL,
  portOutOctets bigint(20) DEFAULT NULL,
  portInSpeed decimal(20,2) DEFAULT NULL,
  portOutSpeed decimal(20,2) DEFAULT NULL,
  collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (onuId,portIndex),
  CONSTRAINT FK_perfonuflowqualitylast FOREIGN KEY (onuId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/* -- version 2.5.2.0,build 2015-4-27,module epon */

-- version 2.5.2.0,build 2015-4-30,module epon
alter table onublockauthen modify onuAuthenBlockMacAddress varchar(20);
alter table oltauthentication modify onuAuthenMacAddress varchar(20);
/* -- version 2.5.2.0,build 2015-4-30,module epon */

-- version 2.4.11.0,build 2015-6-3,module epon
ALTER TABLE perfeponlinkquality ADD PRIMARY KEY (entityId,portIndex,collectTime);
/*-- version 2.4.11.0,build 2015-6-3,module epon*/

-- version 2.6.0.0,build 2015-5-19,module epon
alter table perfeponservicequality add primary key(entityId,slotIndex,targetName,collectTime);
/* -- version 2.6.0.0,build 2015-5-19,module epon */

-- version 2.6.0.0,build 2015-08-01,module epon
alter table oltonurelation add constraint FK_ENTITY_ONU foreign key(onuId) references entity(entityId) on delete cascade on update cascade;
/* -- version 2.6.0.0,build 2015-08-01,module epon */

-- version 2.6.4.0,build 2015-11-12,module epon
drop table batchConfigRecords;
/* -- version 2.6.4.0,build 2015-11-12,module epon */

-- version 2.6.6.0,build 2015-12-8,module epon
CREATE TABLE onusrvprofile (
  srvProfileId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  srvProfileName varchar(32) DEFAULT NULL,
  srvImgpMode int(11) DEFAULT NULL,
  srvIgmpFastLeave int(10) DEFAULT NULL,
  srvBindCap int(11) DEFAULT '0',
  srvBindCnt int(11) DEFAULT '0',
  PRIMARY KEY (srvProfileId,entityId),
  CONSTRAINT FK_onusrvprofile FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE onuportVlanprofile (
  vlanProfileId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  srvPortId bigint(20) NOT NULL,
  bindVlanProfile int(11) DEFAULT NULL,
  profileVlanPvid int(11) DEFAULT NULL,
  PRIMARY KEY (vlanProfileId,entityId,srvPortId),
  CONSTRAINT FK_onuportVlanprofile FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE onuigmpprofile (
  igmpProfileId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  igmpPortId int(11) DEFAULT NULL,
  igmpMaxGroup int(11) DEFAULT NULL,
  igmpVlanMode int(11) DEFAULT '0',
  igmpTransId int(11) DEFAULT '0',
  igmpVlanList varchar(256) DEFAULT NULL,
  PRIMARY KEY (igmpProfileId,entityId,igmpPortId),
  CONSTRAINT FK_onuigmpprofile FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE onuCapability (
  capabilityId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  gePortNum int(11) DEFAULT 0,
  fePortNum int(11) DEFAULT 0,
  potsPortNum int(11) DEFAULT 0,
  e1PortNum int(11) DEFAULT 0,
  wlanPortNum int(11) DEFAULT 0,
  catvPortNum int(11) DEFAULT 0,
  uartPortNum int(11) DEFAULT 0,
  PRIMARY KEY (capabilityId,entityId),
  CONSTRAINT FK_onuCapability FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/* -- version 2.6.6.0,build 2015-12-8,module epon  */

-- version 2.6.6.0,build 2016-01-07,module epon
ALTER TABLE UniVlanBindTable ADD COLUMN bindProfAttr tinyint(4) default '2' COMMENT '1:onu业务模板自动下发;2:离散配置';
/* -- version 2.6.6.0,build 2016-01-07,module epon  */

-- version 2.6.6.0,build 2016-02-17,module epon
CREATE TABLE oltslotrecord(entityId bigint(20), slotId bigint(20), databasePreConfig int, collectPreConfig int, collectTime timestamp);
/* -- version 2.6.6.0,build 2016-02-17,module epon  */

-- version 2.6.8.0,build 2016-03-03,module epon
alter table OltSlotAttribute modify bUptime bigint(20);
/* -- version 2.6.8.0,build 2016-03-03,module epon  */

-- version 2.6.8.0,build 2016-03-18,module epon
ALTER TABLE eponeventtyperelation DROP PRIMARY KEY;
ALTER TABLE eponeventtyperelation ADD PRIMARY KEY(deviceEventTypeId, type);
/* -- version 2.6.8.0,build 2016-03-18,module epon  */

-- version 2.6.8.3,build 2016-04-13,module epon
ALTER TABLE oltsnioptical
ADD (
	inCurrentRate int(11) NOT NULL DEFAULT '-1',
  outCurrentRate int(11) NOT NULL DEFAULT '-1',
  modifyTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE oltponoptical
ADD (
	inCurrentRate int(11) NOT NULL DEFAULT '-1',
  outCurrentRate int(11) NOT NULL DEFAULT '-1',
  modifyTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
/* -- version 2.6.8.3,build 2016-04-13,module epon  */



-- version 2.7.0.0,build 2016-05-03,module epon
create table oltponvlan
(
   entityId             bigint(20),
   ponId                bigint(20),
   ponIndex             bigint(20),
   vlanTagTpid          varchar(20),
   vlanTagCfi           tinyint,
   vlanTagPriority      int(4),
   vlanPVid             int(4),
   vlanMode             tinyint,
   primary key (ponId),
   CONSTRAINT FK_vlan_pon FOREIGN KEY (ponId) REFERENCES oltponrelation (ponId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.7.0.0,build 2016-05-03,module epon  */

-- version 2.7.0.0,build 2016-05-05,module epon
alter table onuautoupgprofile modify column proUpgTime varchar(32);
/* -- version 2.7.0.0,build 2016-05-05,module epon  */

-- version 2.7.0.0,build 2016-05-07,module epon
CREATE TABLE onucpelocatoin (
  macLocation varchar(32) NOT NULL,
  slotLocation tinyint(4) NOT NULL,
  portLocation tinyint(4) NOT NULL,
  onuLocation tinyint(4) NOT NULL,
  uniLocation tinyint(4) NOT NULL,
  entityId bigint(20) NOT NULL,
  onuId bigint(20) DEFAULT NULL,
  PRIMARY KEY (macLocation),
  KEY FK_entityId (entityId),
  KEY FK_onuId (onuId),
  CONSTRAINT FK_entityId FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
)
/* -- version 2.7.0.0,build 2016-05-07,module epon  */

-- version 2.6.8.3,build 2016-05-30,module epon
CREATE TABLE onuwanconfig (                                                                                            
   onuId bigint(20) NOT NULL,                                                                                              
   wanEnnable  int(2),                                                                              
   softVersion  varchar(128),                                                       
   hardVersion   varchar(128),                                                         
   channelId  int(2),                                               
   workMode   varchar(8),                                                   
   channelWidth    int(2),                                                   
   sendPower int(2),                                                                                      
   PRIMARY KEY (onuId),                                                                                                    
   CONSTRAINT `FK_onu_wan` FOREIGN KEY (onuId) REFERENCES oltonurelation (`onuId`) ON DELETE CASCADE ON UPDATE CASCADE  
);
CREATE TABLE onuwanssid (                                                                                            
   onuId bigint(20) NOT NULL,                                                                                              
   ssid int(2),                                         
   ssidName varchar(64),                                                                                    
   encryptMode int(2),                                                                           
   password  varchar(128),                                                       
   ssidEnnable  int(2),                                                      
   ssidBroadcastEnnable  int(2),                                               
   ssidMaxUser   int(4),                                                                                                                                       
   PRIMARY KEY (onuId, ssid),                                                                                                    
   CONSTRAINT `FK_onu_wan_ssid` FOREIGN KEY (onuId) REFERENCES oltonurelation (`onuId`) ON DELETE CASCADE ON UPDATE CASCADE  
);
CREATE TABLE onuwanconnect (                                                                                            
   onuId bigint(20) NOT NULL,                                                                                              
   connectId int(2),                                         
   connectName varchar(64),                                                                                    
   connectMtu int(6),                                                                           
   vlanId int(6),                                                  
   vlanPriority   int(2),    
   connectMode  int(2),  
   ipMode   int(2),
   pppoeUserName varchar(64),
   pppoePassword varchar(64),
   ipv4Address varchar(64),
   ipv4Mask varchar(64),
   ipv4Gateway varchar(64),
   ipv4Dns varchar(64),
   ipv4DnsAlternative varchar(64),
   serviceMode int(2),                                                                           
   bindInterface varchar(32),   
   PRIMARY KEY (onuId, connectId),                                                                                                    
   CONSTRAINT `FK_onu_wan_connect` FOREIGN KEY (onuId) REFERENCES oltonurelation (`onuId`) ON DELETE CASCADE ON UPDATE CASCADE  
);
CREATE TABLE onuwanconnectstatus (                                                                                            
   onuId bigint(20) NOT NULL,                                                                                              
   connectId int(2),                                         
   connectName varchar(64),                                                                                    
   connectMode  int(2),   
   connectStatus  int(2), 
   connectErrorCode  int(2), 
   ipv4Address varchar(64),
   ipv4Mask varchar(64),
   ipv4Gateway varchar(64),
   ipv4Dns varchar(64),
   ipv4DnsAlternative varchar(64),
   PRIMARY KEY (onuId, connectId),                                                                                                    
   CONSTRAINT `FK_onu_wan_status` FOREIGN KEY (onuId) REFERENCES oltonurelation (`onuId`) ON DELETE CASCADE ON UPDATE CASCADE  
);
/* -- version 2.6.8.3,build 2016-05-30,module epon  */

-- version 2.7.1.0,build 2016-05-30,module epon
create table OnuCatvConfig
(
   onuId    bigint(20) not null,
   entityId  bigint(20) not null,
   onuIndex  bigint(20) not null,
   onuCatvOrConfigSwitch  int(11),
   onuCatvOrConfigGainControlType  int(11),
   onuCatvOrConfigAGCUpValue    int(11),
   onuCatvOrConfigAGCRange    int(11),
   onuCatvOrConfigMGCTxAttenuation    int(11),
   onuCatvOrConfigInputLO    int(11),
   onuCatvOrConfigInputHI    int(11),
   onuCatvOrConfigOutputLO    int(11),
   onuCatvOrConfigOutputHI    int(11),
   onuCatvOrConfigVoltageHI    int(11),
   onuCatvOrConfigVoltageLO    int(11),
   onuCatvOrConfigTemperatureHI    int(11),
   onuCatvOrConfigTemperatureLO    int(11),
   collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   primary key (onuId),
   CONSTRAINT FK_catvconfig FOREIGN KEY (onuId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE perfonucatvquality (                                                        
   entityId bigint(20) NOT NULL,                                                          
   onuId bigint(20) NOT NULL,                                                             
   onuIndex bigint(20) NOT NULL,                                                          
   onuCatvOrInfoRxPower int default null,                                             
   onuCatvOrInfoRfOutVoltage int default null,                                       
   onuCatvOrInfoVoltage int default null,  
   onuCatvOrInfoTemperature int default null,                                             
   collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  
   PRIMARY KEY (onuId,collectTime)                                           
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8; 

CREATE TABLE perfonucatvqualitylast (                                                        
   entityId bigint(20) NOT NULL,                                                          
   onuId bigint(20) NOT NULL,                                                             
   onuIndex bigint(20) NOT NULL,                                                          
   onuCatvOrInfoRxPower int default null,                                             
   onuCatvOrInfoRfOutVoltage int default null,                                       
   onuCatvOrInfoVoltage int default null,  
   onuCatvOrInfoTemperature int default null,                                             
   collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  
   PRIMARY KEY (onuId)                                                      
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/* -- version 2.7.1.0,build 2016-05-30,module epon  */

-- version 2.7.1.0,build 2016-06-08,module epon
CREATE TABLE oltPortVlanRelation (
	entityId  bigint(20) NOT NULL ,
	portIndex  bigint(20) NOT NULL ,
	vlanIndex  int(20) NULL DEFAULT NULL ,
	type  int(2) NULL DEFAULT NULL COMMENT 'tag/untag' ,
	INDEX PORT_VLAN_RELATION_INDEX (entityId, vlanIndex),
	INDEX PORT_VLAN_MODE_RELATION_INDEX (entityId, type),
	INDEX PORT_VLAN_INDEX (entityId, portIndex) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*-- version 2.7.1.0,build 2016-06-08,module epon*/

-- version 2.7.1.0,build 2016-06-14,module epon
alter table OltPortVlan drop FOREIGN KEY FK_vlan_olt_port;
alter table OltPortVlan drop Primary Key;
alter table oltportvlan ADD PRIMARY KEY (entityId, sniIndex);
/*-- version 2.7.1.0,build 2016-06-14,module epon*/

-- version 2.7.1.0,build 2016-06-23,module epon
alter table oltsniattribute add sniDisplayName varchar(20) DEFAULT NULL;
/* -- version 2.7.1.0,build 2016-06-23,module epon  */


-- version 2.7.1.0,build 2016-07-01,module epon
CREATE TABLE igmpglobalparam (
  entityId bigint(20) NOT NULL,
  igmpVersion tinyint(4) NOT NULL COMMENT 'IGMP版本',
  igmpMode tinyint(4) NOT NULL COMMENT 'IGMP模式',
  v2Timeout int(11) NOT NULL COMMENT '组播IGMPv3兼容模式退回到v2模式的允许时长',
  v3RespTime int(11) NOT NULL COMMENT 'V3查询报文响应时间',
  v2RespTime int(11) NOT NULL COMMENT 'V2查询报文响应时间',
  commonInterval int(11) NOT NULL COMMENT '下行通用组查询间隔',
  specialInterval int(11) NOT NULL COMMENT '下行特定组查询间隔',
  squeryNum int(11) NOT NULL COMMENT '下行特定组查询次数',
  squeryRespV3 int(11) NOT NULL COMMENT 'V3特定组查询报文响应时间',
  squeryRespV2 int(11) NOT NULL COMMENT 'V2特定组查询报文响应时间',
  robustVariable int(11) NOT NULL COMMENT '健壮系数',
  querySrcIp varchar(32) NOT NULL COMMENT '查询报文源ip',
  globalBW int(11) NOT NULL COMMENT 'IGMP最大带宽（允许的带宽之和）',
  snpAgingTime int(11) NOT NULL COMMENT 'snooping 模式转发表项的老化时间',
  PRIMARY KEY (entityId),
  CONSTRAINT FK_globalparam FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpcascadeport (
  entityId bigint(20) NOT NULL,
  portIndex bigint(20) NOT NULL,
  portType tinyint(4) NOT NULL,
  slotNo tinyint(4) NOT NULL,
  portNo tinyint(4) NOT NULL,
  PRIMARY KEY (entityId,portIndex),
  CONSTRAINT FK_cascadeentity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpsnpuplinkport (
  entityId bigint(20) NOT NULL,
  portType tinyint(4) NOT NULL,
  slotNo tinyint(4) NOT NULL,
  portNo tinyint(4) NOT NULL,
  uplinkAggId tinyint(4) NOT NULL COMMENT '上行端口聚合组ID',
  PRIMARY KEY (entityId),
  CONSTRAINT FK_snpuplink FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpsnpstaticfwd (
  entityId bigint(20) NOT NULL,
  portIndex bigint(20) NOT NULL,
  portType tinyint(4) NOT NULL,
  groupIp varchar(32) NOT NULL COMMENT '静态转发组组IP地址',
  groupVlan int(11) NOT NULL COMMENT '静态转发组Vlan',
  groupSrcIp varchar(32) NOT NULL COMMENT '静态转发组源IP地址',
  PRIMARY KEY (entityId,portIndex,portType,groupIp,groupVlan,groupSrcIp),
  CONSTRAINT FK_staticFwd FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpvlaninfo (
  vlanId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  portType tinyint(4) NOT NULL,
  slotNo tinyint(4) NOT NULL,
  portNo tinyint(4) NOT NULL,
  uplinkAggId tinyint(4) NOT NULL COMMENT '组播vlan上行端口聚合组ID',
  vlanVrfName varchar(32) DEFAULT NULL COMMENT '组播vlan所属VRF name',
  PRIMARY KEY (vlanId,entityId),
  KEY Fk_igmpVlan (entityId),
  CONSTRAINT Fk_igmpVlan FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpvlangroup (
  groupId int(11) NOT NULL,
  vlanId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  groupDesc varchar(32) NOT NULL,
  groupIp varchar(32) NOT NULL,
  groupSrcIp varchar(32) NOT NULL,
  groupMaxBW int(11) NOT NULL,
  joinMode tinyint(4) NOT NULL,
  PRIMARY KEY (groupId,entityId),
  KEY FK_igmpGroup (entityId),
  KEY FK_groupVlan (vlanId),
  CONSTRAINT FK_igmpGroup FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_groupVlan FOREIGN KEY (vlanId) REFERENCES igmpvlaninfo (vlanId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpgroupname (
  groupId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  groupName varchar(64) DEFAULT NULL,
  PRIMARY KEY (groupId,entityId),
  KEY FK_groupName_entity (entityId),
  CONSTRAINT FK_groupName_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpglobalgroup (
  groupId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  groupIp varchar(32) NOT NULL,
  vlanId int(11) NOT NULL,
  groupSrcIp varchar(32) NOT NULL,
  groupState tinyint(4) NOT NULL,
  portList blob DEFAULT NULL,
  PRIMARY KEY (groupId,entityId),
  KEY FK_globalGroup (entityId),
  CONSTRAINT FK_globalGroup FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpctcparam (
  entityId bigint(20) NOT NULL,
  ctcEnable tinyint(4) NOT NULL COMMENT 'CTC组播功能使能',
  cdrInterval int(11) NOT NULL COMMENT 'CDR记录主动上报的间隔',
  cdrNum int(11) NOT NULL COMMENT 'CDR记录主动上报的数量',
  cdrReport tinyint(4) NOT NULL COMMENT '手动上报组播CDR日志到服务器',
  autoResetTime int(11) NOT NULL COMMENT '组播预览次数自动清零的时刻',
  recognitionTime int(11) NOT NULL COMMENT '组播预览的标识时间',
  onuFwdMode tinyint(4) NOT NULL COMMENT 'ONU转发模式',
  PRIMARY KEY (entityId),
  CONSTRAINT FK_ctcParam FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpctcprofile (
  profileId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  profileDesc varchar(32) NOT NULL,
  profileAuth tinyint(4) NOT NULL,
  previewTime int(11) NOT NULL,
  previewInterval int(11) NOT NULL,
  previewCount int(11) NOT NULL,
  PRIMARY KEY (profileId,entityId),
  KEY FK_ctcProfile (entityId),
  CONSTRAINT FK_ctcProfile FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpprofilename (
  profileId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  profileName varchar(64) DEFAULT NULL,
  PRIMARY KEY (profileId,entityId),
  KEY FK_profileName_entity (entityId),
  CONSTRAINT FK_profileName_entity FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpctcprofilegrouprela (
  profileId int(11) NOT NULL,
  groupId int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  PRIMARY KEY (profileId,groupId,entityId),
  CONSTRAINT FK_ctcprofilegroup FOREIGN KEY (profileId) REFERENCES igmpctcprofile (profileId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpunibindctcprofile (
  uniIndex bigint(20) NOT NULL,
  entityId bigint(20) NOT NULL,
  profileId int(11) NOT NULL,
  portType tinyint(4) NOT NULL,
  PRIMARY KEY (uniIndex,entityId,profileId),
  KEY FK_uniBindOnu (entityId),
  CONSTRAINT FK_uniBindOnu FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpctcrecord (
  cdrSequence int(11) NOT NULL,
  entityId bigint(20) NOT NULL,
  cdrType tinyint(4) NOT NULL,
  cdrSlot tinyint(4) NOT NULL,
  cdrPon tinyint(4) NOT NULL,
  cdrOnu tinyint(4) NOT NULL,
  cdrUni tinyint(4) NOT NULL,
  cdrReqType tinyint(4) NOT NULL,
  cdrReqTime int(11) NOT NULL,
  cdrReqGrpId int(11) NOT NULL,
  cdrGrpAuth tinyint(4) NOT NULL,
  cdrReqResult tinyint(4) NOT NULL,
  cdrLeaveType tinyint(4) NOT NULL,
  cdrRecordTime int(11) NOT NULL,
  PRIMARY KEY (cdrSequence,entityId),
  KEY FK_ctcRecord (entityId),
  CONSTRAINT FK_ctcRecord FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmponuconfig (
  onuIndex bigint(20) NOT NULL,
  entityId bigint(20) NOT NULL,
  portType tinyint(4) NOT NULL,
  onuMode tinyint(4) NOT NULL,
  onuFastLeave tinyint(4) NOT NULL,
  PRIMARY KEY (onuIndex, entityId),
  CONSTRAINT FK_onuIgmp FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpuniconfig (
  uniIndex bigint(20) NOT NULL,
  entityId bigint(20) NOT NULL,
  portType tinyint(4) NOT NULL,
  maxGroupNum int(11) NOT NULL,
  vlanList varchar(256) NOT NULL,
  vlanMode tinyint(4) NOT NULL,
  PRIMARY KEY (uniIndex,entityId),
  KEY FK_uniIgmp (entityId),
  CONSTRAINT FK_uniIgmp FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE igmpunivlantrans (
  transIndex tinyint(4) NOT NULL,
  entityId bigint(20) NOT NULL,
  uniIndex bigint(20) NOT NULL,
  portType tinyint(4) NOT NULL,
  transOldVlan int(11) NOT NULL,
  transNewVlan int(11) NOT NULL,
  PRIMARY KEY (transIndex,entityId,uniIndex),
  KEY FK_uniVlanTrans (entityId),
  CONSTRAINT FK_uniVlanTrans FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* -- version 2.7.1.0,build 2016-07-01,module epon  */

-- version 2.7.1.0,build 2016-07-12,module epon
create table onucpecountlast(
	entityId  bigint(20),
	uniIndex  bigint(20),
	uniNo int,
	cpecount int,
	realtime timestamp,
	PRIMARY KEY (entityId, uniIndex)
);
create table onucpelistlast(
	entityId  bigint(20),
	uniIndex  bigint(20),
	mac  varchar(20),
	type  int(11),
	vlan  int(11),
	realtime  timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
	PRIMARY KEY (entityId, uniIndex, mac)
);
/* -- version 2.7.1.0,build 2016-07-12,module epon  */


-- version 2.7.1.0,build 2016-07-13,module epon 
CREATE TABLE onuofflinealarm (
	entityId  bigint(20),
	mac  varchar(255),
	onuIndex  bigint(255),
	alertType  int(20),
	onuType  int(255),
	onuAlias  varchar(255),
	message  varchar(255),
	firetime  timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
	PRIMARY KEY (entityId, onuIndex, firetime),
	FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE RESTRICT
);
/*-- version 2.7.1.0,build 2016-07-13,module epon*/

-- version 2.7.2.0,build 2016-08-02,module epon 
CREATE TABLE onuIgmpVlanTrans (
  	entityId bigint(20) NOT NULL,
  	igmpProfileId int(11) NOT NULL,
  	igmpPortId int(11) NOT NULL,
  	igmpVlanTransId int(11) NOT NULL,
  	transOldVlan int(11) DEFAULT NULL,
  	transNewVlan int(11) DEFAULT NULL,
	PRIMARY KEY (entityId, igmpProfileId, igmpPortId,igmpVlanTransId),
	FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE RESTRICT
);
/*-- version 2.7.2.0,build 2016-08-02,module epon*/

-- version 2.7.1.0,build 2016-08-02,module epon 
alter table oltonuattribute add column topOnuExtAttr varchar(50);

create table toponuglobalcfgmgmt
(
   entityId   bigint(20) not null,
   itemIndex  int(2),
   cfgMgmtValue  varchar(20),
   primary key (entityId,itemIndex)
);
alter table OltAttribute add constraint FK_olt_globalcfgmgmt foreign key (entityId)
      references Entity (entityId) on delete cascade on update cascade;
/*-- version 2.7.1.0,build 2016-08-02,module epon*/
      
-- version 2.7.1.0,build 2016-09-14,module epon    
alter table onuportvlanprofile add profileVlanPvidPri int default null;
/*-- version 2.7.1.0,build 2016-09-14,module epon*/
     
      
-- version 2.8.0.0,build 2016-09-27,module epon 
ALTER TABLE batchdeploydetail MODIFY  success mediumblob;
ALTER TABLE batchdeploydetail MODIFY  failures mediumblob;
/*-- version 2.8.0.0,build 2016-09-27,module epon */

-- version 2.8.0.0,build 2016-10-15,module epon 
create table oltlogicInterface(
    entityId bigint(20),
    interfaceType int,
    interfaceId bigint(20),
    interfaceIndex bigint(20) not null,
    interfaceName varchar(20),
    interfaceDesc varchar(32),
    interfaceAdminStatus int,
    interfaceOperateStatus int,
    interfaceMac varchar(20),
    primary key(entityId,interfaceType,interfaceId),
    CONSTRAINT FK_oltlogicInterface_ENTITY FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE 
);

create table oltInterfaceIpV4Config(
    entityId bigint(20),
    ipV4ConfigIndex int,
    ipV4Addr varchar(20),
    ipV4NetMask varchar(20),
    ipV4AddrType int(20),
    primary key(entityId,ipV4ConfigIndex,ipV4Addr),
    CONSTRAINT FK_oltInterfaceIpV4Config_ENTITY FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE 
);
/*-- version 2.8.0.0,build 2016-10-15,module epon */

-- version 2.7.1.0,build 2016-09-17,module epon
ALTER  TABLE onucpelistlast ADD COLUMN ipAddress varchar(20);
ALTER  TABLE onucpelistlast ADD COLUMN cpeType varchar(2);
/*-- version 2.7.1.0,build 2016-09-17,module epon */

-- version 2.8.0.0,build 2016-11-17,module epon 
alter table oltonuattribute add column lastDeregisterTime timestamp null default null;
/*-- version 2.8.0.0,build 2016-11-17,module epon */

-- version 2.8.0.0,build 2017-03-14,module epon 
alter table oltonuattribute add column onuUniqueIdentification varchar(500) default null after onuId;
alter table oltonuattribute add column onuSerialNum varchar(500) default null after onuMacAddress;
alter table oltonuattribute add column onuEorG char(1) default 'E';
alter table oltuniattribute add column uniDuplexRate int after uniAdminStatus;
/*-- version 2.8.0.0,build 2017-03-14,module epon */

-- version 2.9.0.5,build 2017-05-15,module epon 
CREATE TABLE `perfonuqualityhistory` (
  `entityId` bigint(20) NOT NULL,
  `onuId` bigint(20) NOT NULL,
  `onuIndex` bigint(20) NOT NULL,
  `minOnuPonRevPower` decimal(12,2) DEFAULT NULL COMMENT 'PON-ONU 24h最低收光功率',
  `minPowerTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'PON-ONU 24h最低收光功率对应的时间（timestamp格式）',
  `minPowerTimeStr` varchar(60) DEFAULT NULL COMMENT 'PON-ONU 24h最低收光功率对应的时间（字符串格式）',
  `minCatvRevPower` int(11) DEFAULT NULL COMMENT 'CATV 24h最低收光功率',
  `minCatvTime` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'CATV 24h最低收光功率对应的时间（timestamp格式）',
  `minCatvTimeStr` varchar(60) DEFAULT NULL COMMENT 'CATV 24h最低收光功率（string格式）',
  `collectTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '汇总时间',
  PRIMARY KEY (`onuId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*-- version 2.9.0.5,build 2017-05-15,module epon */

-- version 2.9.0.4,build 2017-06-10,module epon 
alter table oltonuattribute add column topOnuCapabilityStr varchar(50) default null;
/*-- version 2.9.0.4,build 2017-06-10,module epon */


-- version 2.9.0.5,build 2017-06-19,module epon 
alter table oltattribute add column systemRogueCheck int(2) NOT NULL DEFAULT 1;
alter table oltonuattribute add column rogueOnu int(2) NOT NULL DEFAULT 0;

create table oltPonRogueInfo(
    entityId bigint(20) NOT NULL,
    ponId  bigint(20) NOT NULL,
    rogueSwitch int(2),
    rogueOnuList varchar(256),
    rogueOnuCheck varchar(256),
    primary key(ponId),
    CONSTRAINT FK_oltPonRogueInfo_PONID FOREIGN KEY (ponId) REFERENCES oltponrelation (ponId) ON DELETE CASCADE ON UPDATE CASCADE 
);

create table onuLaser(
    onuId  bigint(20) NOT NULL,
    laserSwitch int(2),
    primary key(onuId),
    CONSTRAINT FK_onuLaser_PONID FOREIGN KEY (onuId)  REFERENCES OltOnuRelation (onuId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 2.9.0.5,build 2017-06-19,module epon */

-- version 2.9.1.8,build 2017-09-18,module epon 
ALTER TABLE oltonuattribute MODIFY COLUMN lastDeregisterTime timestamp NULL DEFAULT CURRENT_TIMESTAMP;
/*-- version 2.9.1.8,build 2017-09-18,module epon */

-- version 2.9.0.5,build 2017-06-15,module epon 
CREATE TABLE `onuonoffrecord` (
  `entityId` bigint(20) NOT NULL,
  `onuId` bigint(20) NOT NULL,
  `onuIndex` bigint(20) NOT NULL,
  `onTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上线时间',
  `offTime` timestamp NULL DEFAULT NULL COMMENT '下线时间，可能还没下线，所以没有下线时间',
  `offReason` tinyint(1) DEFAULT NULL COMMENT '下线原因，有0,1,2,3,4,5几种值',
  `collectTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `onOffRecordByteList` char(40) NOT NULL COMMENT '从设备获取的上下线记录的对应字节（此处保存拆开后的单条记录对应的字节）',
  PRIMARY KEY (`onuId`,`onTime`),
  CONSTRAINT `fk_onuonoffrecord_entity` FOREIGN KEY (`onuId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*-- version 2.9.0.5,build 2017-06-15,module epon */
-- version 2.8.0.0,build 2017-04-13,module epon 
alter table oltonuattribute add column onuLevel int default 0;
/*-- version 2.8.0.0,build 2017-04-13,module epon */

-- version 2.9.1.14,build 2017-12-22,module epon 
CREATE TABLE onutag
(
  id int(11) NOT NULL auto_increment,
  tagName varchar(50) NOT NULL,
  tagLevel tinyint(4) default 0,
  PRIMARY KEY (id)
);
CREATE TABLE `onutagrelation` (
  `onuId` bigint(20) NOT NULL,
  `tagId` int(11),
  PRIMARY KEY (`onuId`),
  CONSTRAINT `fk_relation_onuattribute` FOREIGN KEY (`tagId`) REFERENCES `onutag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*-- version 2.9.1.14,build 2017-12-22,module epon */

-- version 2.10.0.1,build 2017-11-17,module epon 
create table TopOltDhcpCpeInfo(
   entityId bigint(20) not null,
   topOltDhcpCpeIpIndex varchar(20),
   topOltDhcpCpeMac varchar(20),
   topOltDhcpCpeVlan int,
   topOltDhcpCpePortType tinyint,
   topOltDhcpCpeSlot tinyint,
   topOltDhcpCpePort tinyint,
   topOltDhcpCpeOnu tinyint,
   topOltDhcpCpeRemainingTime int,
   remainingTimeStr varchar(32),
   PRIMARY KEY (entityId,topOltDhcpCpeIpIndex),                                                                                                                                                                            
   CONSTRAINT `FK_oltdhcp_cpeinfo` FOREIGN KEY (`entityId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);

create table TopOltDhcpGlobalObjects(
   entityId bigint(20) not null,
   topOltDhcpEnable tinyint,
   topOltDhcpOpt82Enable tinyint,
   topOltDhcpOpt82Policy tinyint,
   topOltDhcpOpt82Format varchar(400),
   topOltDhcpSourceVerifyEnable tinyint,
   topOltPPPoEPlusEnable tinyint,
   topOltPPPoEPlusPolicy tinyint,
   topOltPPPoEPlusFormat varchar(400),
   PRIMARY KEY (entityId),                                                                                                                                                                            
   CONSTRAINT `FK_oltdhcp_global` FOREIGN KEY (`entityId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);

create table TopOltDhcpPortAttribute(
   entityId bigint(20) not null,
   topOltDhcpPortProtIndex tinyint,
   topOltDhcpPortTypeIndex tinyint,
   topOltDhcpSlotIndex tinyint,
   topOltDhcpPortIndex tinyint,
   topOltDhcpPortCascade tinyint,
   topOltDhcpPortTrans tinyint,
   topOltDhcpPortTrust tinyint,
   PRIMARY KEY (entityId,topOltDhcpPortProtIndex,topOltDhcpPortTypeIndex,topOltDhcpSlotIndex,topOltDhcpPortIndex),                                                                                                                                                                            
   CONSTRAINT `FK_oltdhcp_portattribute` FOREIGN KEY (`entityId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);

create table TopOltDhcpServerGroup(
   entityId bigint(20) not null,
   topOltDhcpServerGroupIndex int,
   topOltDhcpServerIpList varchar(64),
   topOltDhcpServerBindNum int,
   PRIMARY KEY (entityId,topOltDhcpServerGroupIndex),                                                                                                                                                                            
   CONSTRAINT `FK_oltdhcp_servergroup` FOREIGN KEY (`entityId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);

create table TopOltDhcpStaticIp(
   entityId bigint(20) not null,
   ipIndex  varchar(20),
   maskIndex varchar(20),
   topOltDhcpStaticIpSlot tinyint,
   topOltDhcpStaticIpPort tinyint,
   topOltDhcpStaticIpOnu tinyint,
   PRIMARY KEY (entityId,ipIndex,maskIndex),                                                                                                                                                                            
   CONSTRAINT `FK_oltdhcp_staticip` FOREIGN KEY (`entityId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);

create table TopOltDhcpStatisticsObjects(
   entityId bigint(20) not null,
   topOltDhcpStatRDiscover  bigint(20),
   topOltDhcpStatRRequest  bigint(20),
   topOltDhcpStatROffer  bigint(20),
   topOltDhcpStatRAck  bigint(20),
   topOltDhcpStatROther  bigint(20),
   topOltDhcpStatDDiscover  bigint(20),
   topOltDhcpStatDRequest  bigint(20),
   topOltDhcpStatDOffer  bigint(20),
   topOltDhcpStatDAck  bigint(20),
   topOltDhcpStatDOther  bigint(20),
   topOltDhcpStatDFlood  bigint(20),
   topOltDhcpStatDUnknown  bigint(20),
   topOltDhcpStatDCongestion  bigint(20),
   topOltDhcpStatTDiscover  bigint(20),
   topOltDhcpStatTRequest  bigint(20),
   topOltDhcpStatTOffer  bigint(20),
   topOltDhcpStatTAck  bigint(20),
   topOltDhcpStatTOther  bigint(20),
   PRIMARY KEY (entityId),                                                                                                                                                                            
   CONSTRAINT `FK_oltdhcp_statistics` FOREIGN KEY (`entityId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);

create table TopOltDhcpVifCfg(
   entityId bigint(20) not null,
   topOltDhcpVifIndex  int,
   topOltDhcpVifOpt60StrIndex varchar(100),
   topOltDhcpVifAgentAddr varchar(30),
   topOltDhcpVifServerGroup tinyint,
   topOltDhcpStaticIpOnu tinyint,
   PRIMARY KEY (entityId,topOltDhcpVifIndex,topOltDhcpVifOpt60StrIndex),                                                                                                                                                                            
   CONSTRAINT `FK_oltdhcp_vifcfg` FOREIGN KEY (`entityId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);

create table TopOltDhcpVLANCfg(
   entityId bigint(20) not null,
   topOltDhcpVLANIndex  int,
   topOltDhcpVLANMode tinyint,
   topOltDhcpVLANRelayMode tinyint,
   PRIMARY KEY (entityId,topOltDhcpVLANIndex),                                                                                                                                                                            
   CONSTRAINT `FK_oltdhcp_vlancfg` FOREIGN KEY (`entityId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);


create table TopOltPppoeStatisticsObjects(
   entityId bigint(20) not null,
   topOltPppoeStatReceive  bigint(20),
   topOltPppoeStatDrop  bigint(20),
   topOltPppoeStatTransmit  bigint(20),
   PRIMARY KEY (entityId),                                                                                                                                                                            
   CONSTRAINT `FK_oltpppoe_statistics` FOREIGN KEY (`entityId`) REFERENCES `entity` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE  
);
/*-- version 2.10.0.1,build 2017-11-17,module epon */

-- version 2.9.1.10,build 2017-10-13,module epon
CREATE TABLE `ofaAlarmThreshold` (
  `entityId` bigint(20) NOT NULL,
  `alarmThresholdIndex` int(11) DEFAULT NULL,
  `inputAlarmUp` int(11) DEFAULT NULL COMMENT 'OFA输入光功率告警阈值上限',
  `inputAlarmLow` int(11) DEFAULT NULL COMMENT 'OFA 输入光功率告警阈值下限',
  `outputAlarmUp` int(11) DEFAULT NULL COMMENT 'OFA输出光功率告警阈值上限',
  `outputAlarmLow` int(11) DEFAULT NULL COMMENT 'OFA输出光功率告警阈值下限',
  `pump1BiasAlarmUp` int(11) DEFAULT NULL COMMENT 'OFA泵浦1偏置电流告警阈值上限',
  `pump1BiasAlarmLow` int(11) DEFAULT NULL COMMENT 'OFA泵浦1偏置电流告警阈值下限',
  `pump1TempAlarmUp` int(11) DEFAULT NULL COMMENT 'OFA泵浦1温度告警阈值上限',
  `pump1TempAlarmLow` int(11) DEFAULT NULL COMMENT 'OFA泵浦1温度告警阈值下限',
  `pump1TecAlarmUp` int(11) DEFAULT NULL COMMENT 'OFA泵浦1温控电流告警阈值上限',
  `pump1TecAlarmLow` int(11) DEFAULT NULL COMMENT 'OFA泵浦1温控电流告警阈值下限',
  `pump2BiasAlarmUp` int(11) DEFAULT NULL COMMENT 'OFA泵浦2偏置电流告警阈值上限',
  `pump2BiasAlarmLow` int(11) DEFAULT NULL COMMENT 'OFA泵浦2偏置电流告警阈值下限',
  `pump2TempAlarmUp` int(11) DEFAULT NULL COMMENT 'OFA泵浦2温度告警阈值上限',
  `pump2TempAlarmLow` int(11) DEFAULT NULL COMMENT 'OFA泵浦2温度告警阈值下限',
  `voltage5AlarmUp` int(11) DEFAULT NULL COMMENT 'OFA 5V电源电压告警阈值上限',
  `voltage5AlarmLow` int(11) DEFAULT NULL COMMENT 'OFA 5V电源电压告警阈值下限',
  `voltage12AlarmUp` int(11) DEFAULT NULL COMMENT 'OFA 12V电源电压告警阈值上限',
  `voltage12AlarmLow` int(11) DEFAULT NULL COMMENT 'OFA 12V电源电压告警阈值下限',
  PRIMARY KEY (`entityId`, `alarmThresholdIndex`),
  CONSTRAINT `FK_oltAtt_ofa_on_entityId` FOREIGN KEY (`entityId`) REFERENCES `oltattribute` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*-- version 2.9.1.10,build 2017-10-13,module epon */

-- version 2.9.1.11,build 2017-10-18,module epon
CREATE TABLE `ofaBasicInfo` (
  `entityId` bigint(20) NOT NULL,
  `deviceIndex` int(20) DEFAULT NULL,
  `platSN` varchar(20) DEFAULT NULL COMMENT '平台序列号',
  `hWVer` varchar(20) DEFAULT NULL COMMENT '硬件版本号',
  `sWVer` varchar(20) DEFAULT NULL COMMENT '软件版本号',
  `moduleType` varchar(20) DEFAULT NULL COMMENT '模块型号',
  `inputPower` int(11) DEFAULT NULL COMMENT '输入光功率',
  `outputPower` int(11) DEFAULT NULL COMMENT '输出光功率',
  `pump1BiasCurr` int(11) DEFAULT NULL COMMENT '泵浦1偏置电流',
  `pump1Temp` int(11) DEFAULT NULL COMMENT '泵浦1温度',
  `pump1Tec` int(11) DEFAULT NULL COMMENT '泵浦1温控电流',
  `pump2BiasCurr` int(11) DEFAULT NULL COMMENT '泵浦2偏置电流',
  `pump2Temp` int(11) DEFAULT NULL COMMENT '泵浦2温度',
  `pump2Tec` int(11) DEFAULT NULL COMMENT '泵浦2温控电流',
  `voltage5v` int(11) DEFAULT NULL COMMENT '5V OFA电源电压',
  `voltage12v` int(11) DEFAULT NULL COMMENT '12V OFA电源电压',
  `systemTemp` int(11) DEFAULT NULL COMMENT '系统温度',
  `outputAtt` int(11) DEFAULT NULL COMMENT '输出衰减',
  PRIMARY KEY (`entityId`,`deviceIndex`),
  CONSTRAINT `FK_oltAtt_ofabasic_on_entityId` FOREIGN KEY (`entityId`) REFERENCES `oltattribute` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `commonOptiTrans` (
  `entityId` bigint(20) NOT NULL,
  `deviceIndex` int(11) DEFAULT NULL,
  `deviceType` varchar(20) DEFAULT NULL COMMENT '设备类型',
  `deviceName` varchar(20) DEFAULT NULL COMMENT '设备名称',
  `vendorName` varchar(20) DEFAULT NULL COMMENT '生产厂商',
  `modelNumber` varchar(20) DEFAULT NULL COMMENT '设备型号',
  `serialNumber` varchar(20) DEFAULT NULL COMMENT '设备序列号',
  `ipAddress` varchar(20) DEFAULT NULL COMMENT '设备IP',
  `macAddress` varchar(20) DEFAULT NULL COMMENT '设备MAC',
  `deviceAcct` int(11) DEFAULT NULL COMMENT '生产日期',
  `deviceMFD` varchar(20) DEFAULT NULL COMMENT '连续运行时长',
  PRIMARY KEY (`entityId`,`deviceIndex`),
  CONSTRAINT `FK_oltAtt_common_on_entityId` FOREIGN KEY (`entityId`) REFERENCES `oltattribute` (`entityId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*-- version 2.9.1.11,build 2017-10-18,module epon */

-- version 2.9.1.12,build 2018-03-13,module epon
ALTER TABLE oltponoptical MODIFY COLUMN vendorName VARCHAR(100);
ALTER TABLE oltponoptical MODIFY COLUMN vendorPN VARCHAR(100);
ALTER TABLE oltponoptical MODIFY COLUMN vendorsN VARCHAR(100);
/*-- version 2.9.1.12,build 2018-03-13,module epon */

-- version 2.10.0.1,build 2018-4-4,module epon
ALTER TABLE topoltdhcpvifcfg MODIFY topOltDhcpVifOpt60StrIndex VARCHAR(128);
/*-- version 2.10.0.1,build 2018-4-4,module epon */

-- version 2.10.2.0,build 2018-4-24,module epon
ALTER TABLE onutag MODIFY tagName VARCHAR(64);
/*-- version 2.10.2.0,build 2018-4-24,module epon */

-- version 2.10.2.0,build 2018-6-1,module epon
ALTER TABLE onucpelocatoin MODIFY COLUMN onuLocation int;
/*-- version 2.10.2.0,build 2018-6-1,module epon */

-- version 2.10.2.0,build 2018-6-20,module epon
alter table oltonuattribute add column onuDeactive int;
/*-- version 2.10.2.0,build 2018-6-20,module epon */

-- version 2.10.2.0,build 2018-6-21,module epon
alter table oltponqinq modify column pqSTagCosNewValue SMALLINT(5);
/*-- version 2.10.2.0,build 2018-6-21,module epon */
-- version 2.10.2.0,build 2018-8-3,module epon
alter table oltsniattribute add column sniPortType tinyint(4) DEFAULT NULL;
/*-- version 2.10.2.0,build 2018-8-3,module epon */