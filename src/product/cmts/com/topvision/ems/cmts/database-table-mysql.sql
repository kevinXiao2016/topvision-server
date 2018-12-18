-- version 1.7.19.0,build 2013-08-17,module cmts
create table PerfUplinkSpeedStaticLast
(
   entityId             bigint(20),
   ifIndex              bigint(20),
   ifInOctets              bigint(20),
   ifOutOctets              bigint(20),
   ifInOctetsRate decimal(16,4),
   ifOutOctetsRate decimal(16,4),
   ifOctetsRate decimal(16,4),
   dt timestamp
);

create table PerfUplinkSpeedStaticHis
(
   entityId             bigint(20),
   ifIndex              bigint(20),
   ifInOctets              bigint(20),
   ifOutOctets              bigint(20),
   ifInOctetsRate decimal(16,4),
   ifOutOctetsRate decimal(16,4),
   ifOctetsRate decimal(16,4),
   dt timestamp
);

create table PerfUplinkUtilizationLast
(
   entityId             bigint(20),
   ifIndex              bigint(20),
   ifUtilization decimal(5,2),
   dt timestamp
);
create table PerfUplinkUtilizationHis
(
   entityId             bigint(20),
   ifIndex              bigint(20),
   ifUtilization decimal(5,2),
   dt timestamp
);
/*--version 1.7.19.0,build 2013-08-17,module cmts*/

-- version 2.0.6.0,build 2014-04-17,module cmts
create table perfCmtsSnrQuality(
    cmcId bigint(20) not null,
    channelIndex bigint(20) not null,
    collectValue int(11) not null,
    collectTime timestamp not null,
    primary key(cmcId,channelIndex,collectTime),
    CONSTRAINT FK_perfcmtssnrquality_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmtsErrorCodeQuality(
    cmcId bigint(20) not null,
    channelIndex bigint(20) not null,
    ccerCode int(11) not null,
    ucerCode int(11) not null,
    ccerRate decimal(5,2) not null,
    ucerRate decimal(5,2) not null,
    collectTime timestamp not null,
    primary key(cmcId,channelIndex,collectTime),
    CONSTRAINT FK_perfcmtsecquality_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfCmtsFlowQuality(
    cmcId bigint(20) not null,
    channelIndex bigint(20) not null,
    channelInOctets bigint(20) not null,
    channelOutOctets bigint(20) not null,
    channelOctets bigint(20) not null,
    channelInSpeed decimal(12,2),
    channelOutSpeed decimal(12,2),
    channelUtilization decimal(12,2),
    collectTime timestamp not null,
    CONSTRAINT FK_perfCmtsFlowQuality FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*--version 2.0.6.0,build 2014-04-17,module cmts*/

-- version 2.4.5.0,build 2014-11-13,module cmts
alter table perfcmtserrorcodequality add column noerCode int(11) not null;
alter table perfcmtserrorcodequality add column noerRate decimal(5,2) not null;
/* -- version 2.4.5.0,build 2014-11-13,module cmts */

-- version 2.4.5.0,build 2015-1-26,module cmts
alter table perfcmtsflowquality add column portBandWidth  bigint(20);
/*-- version 2.4.5.0,build 2015-1-26,module cmts*/

-- version 2.4.11.0,build 2015-6-3,module cmts
alter table perfcmtsflowquality add primary key(cmcId,channelIndex,collectTime);
/*-- version 2.4.11.0,build 2015-6-3,module cmts*/

-- version 2.6.0.0,build 2015-05-28,module cmts
create table perfcmtscpuquality(
    cmcId bigint(20) not null,
    collectValue decimal(12,4) not null,
    collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfcmtscpuquality_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfcmtsmemquality(
    cmcId bigint(20) not null,
    collectValue decimal(12,4) not null,
    collectTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key(cmcId,collectTime),
    CONSTRAINT FK_perfcmtsmemquality_entity FOREIGN KEY (cmcId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/*-- version 2.6.0.0,build 2015-05-28,module cmts*/
