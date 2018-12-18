-- version 2.5.1.0,build 2015-03-25,module cm
CREATE TABLE dispersionhistory(
	opticalNodeId bigint(20) NOT NULL,
	collectTime TIMESTAMP NOT NULL,
	cmNum INT,
	upSnrAvg DOUBLE,
	upSnrStd DOUBLE,
	upSnrDist VARCHAR(1000),
	upPowerAvg DOUBLE,
	upPowerStd DOUBLE,
	upPowerDist VARCHAR(1000),
	PRIMARY KEY (opticalNodeId, collectTime),
	INDEX nodeId_index (opticalNodeId, collectTime) USING BTREE
);

CREATE TABLE dispersionlast(
	opticalNodeId bigint(20) NOT NULL,
	collectTime TIMESTAMP NOT NULL,
	cmNum INT,
	upSnrAvg DOUBLE,
	upSnrStd DOUBLE,
	upSnrDist VARCHAR(1000),
	upPowerAvg DOUBLE,
	upPowerStd DOUBLE,
	upPowerDist VARCHAR(1000),
	PRIMARY KEY (opticalNodeId),
	INDEX node_last_index (opticalNodeId) USING BTREE
);
/* -- version 2.5.1.0,build 2015-03-25,module cm */

-- version 2.5.2.0,build 2015-04-09,module cm
CREATE TABLE cmHistory(
	cmId BIGINT(20),
	collectTime TIMESTAMP,
    statusValue TINYINT(2),
    checkStatus TINYINT(2),
    upChannelId VARCHAR(128),
    downChannelId VARCHAR(128),
	upChannelFreq VARCHAR(1024),
	downChannelFreq VARCHAR(1024),
	upRecvPower VARCHAR(1024),
	upSnr VARCHAR(1024),
	downSnr VARCHAR(1024),
	upSendPower VARCHAR(1024),
	downRecvPower VARCHAR(1024),
    PRIMARY KEY(cmId,collectTime));
/* -- version 2.5.2.0,build 2015-04-09,module cm */
    
-- version 2.6.0.0,build 2015-05-28,module cm
CREATE TABLE ccmtsmaintain
(
	cmcId bigint(20) NOT NULL,
	collectTime TIMESTAMP NOT NULL,
	allChlNum INT,
	allStdNum INT,
	avgSnrStdNum INT,
	lowSnrStdNum INT,
	bigPowerStdNum INT,
	chlWidthStdNum INT,
	PRIMARY KEY (cmcId),
	INDEX maintain_index (cmcId) USING BTREE
);
/* -- version 2.6.0.0,build 2015-05-28,module cm */

-- version 2.6.0.0,build 2016-12-08,module cm
CREATE TABLE cmupgradeconfig (
     configid int(11) auto_increment,  
     modulNum varchar(100) DEFAULT NULL, 
     softversion varchar(100) DEFAULT NULL, 
     versionFileName varchar(100) DEFAULT NULL,  
     filesize int(11) DEFAULT NULL,              
     PRIMARY KEY (configid)
);

create table cmsoftversioninfo(entityId bigint(20), statusIndex bigint(20), modulNum varchar(200), softversion varchar(200));
/* -- version 2.6.0.0,build 2015-12-08,module cm */

-- version 2.9.0.0,build 2017-01-14,module cm
CREATE TABLE SpecifiedCmList(
  mac VARCHAR(20)
);

CREATE TABLE SpecifiedCmListLast(
	cmId BIGINT(20),
	collectTime TIMESTAMP,
    statusValue TINYINT(2),
    checkStatus TINYINT(2),
    upChannelId VARCHAR(128),
    downChannelId VARCHAR(128),
	upChannelFreq VARCHAR(1024),
	downChannelFreq VARCHAR(1024),
	upRecvPower VARCHAR(1024),
	upSnr VARCHAR(1024),
	downSnr VARCHAR(1024),
	upSendPower VARCHAR(1024),
	downRecvPower VARCHAR(1024),
    PRIMARY KEY(cmId));
/* -- version 2.9.0.0,build 2017-01-14,module cm */
    
    
-- version 2.10.0.0,build 2017-08-09,module cm
CREATE TABLE PnmpMiddleIntervalCm(
  entityId BIGINT(20),
  cmcId BIGINT(20),
  cmmac VARCHAR(20),
  PRIMARY KEY(entityId,cmcId,cmMac)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE PnmpHighIntervalCm(
  entityId BIGINT(20),
  cmcId BIGINT(20),
  cmmac VARCHAR(20),
  PRIMARY KEY(entityId,cmcId,cmMac)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE PnmpCmtsReport(
  cmcId BIGINT(20),
  totalCmNum int(11),
  onlineCmNum int(11),
  offlineCmNum int(11),
  healthCmNum int(11),
  marginalCmNum int(11),
  badCmNum int(11),
  updateTime TIMESTAMP,
  PRIMARY KEY(cmcId),
  CONSTRAINT `FK_pnmpcmtsreport_entity_on_cmcId` FOREIGN KEY(`cmcId`) REFERENCES `entity`(`entityId`) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE PnmpCmDataLast(
  entityId BIGINT(20),
  cmcId BIGINT(20),
  cmMac VARCHAR(20),
  statusValue TINYINT(2),
  checkStatus TINYINT(2),
  tapCoefficient VARCHAR(250),
  spectrumResponse VARCHAR(10000),
  mte DOUBLE,
  preMte DOUBLE,
  postMte DOUBLE,
  tte DOUBLE,
  mtc DOUBLE,
  mtr DOUBLE,
  mtrLevel TINYINT(2),
  nmtter DOUBLE,
  premtter DOUBLE,
  postmtter DOUBLE,
  ppesr DOUBLE,
  mrLevel DOUBLE,
  Tdr DOUBLE,
  upSnr DOUBLE,
  upTxPower DOUBLE,
  downSnr DOUBLE,
  downRxPower DOUBLE,
  preEqualizationState TINYINT(2),
  orginalValue text,
  upChannelId int(11),
  upChannelFreq BIGINT(20),
  upChannelWidth  BIGINT(20),
  collectTime TIMESTAMP,
  PRIMARY KEY(entityId,cmcId,cmMac)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE PnmpCmDataHis(
  entityId BIGINT(20),
  cmcId BIGINT(20),
  cmMac VARCHAR(20),
  statusValue TINYINT(2),
  checkStatus TINYINT(2),
  mte DOUBLE,
  preMte DOUBLE,
  postMte DOUBLE,
  tte DOUBLE,
  mtc DOUBLE,
  mtr DOUBLE,
  nmtter DOUBLE,
  premtter DOUBLE,
  postmtter DOUBLE,
  ppesr DOUBLE,
  mrLevel DOUBLE,
  Tdr DOUBLE,
  upSnr DOUBLE,
  upTxPower DOUBLE,
  downSnr DOUBLE,
  DownRxPower DOUBLE,
  collectTime TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE PnmpTargetThreshold(
	TargetName VARCHAR(50),
	thresholdName VARCHAR(50),
	thresholdLevel  int(11),
	lowValue DOUBLE,
	highValue DOUBLE,
    PRIMARY KEY(targetName,thresholdName)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE CmSignalTargetThreshold(
	targetName VARCHAR(50),
	thresholdName VARCHAR(50),
	thresholdLevel  int(11),
	lowValue DOUBLE,
	highValue DOUBLE,
    PRIMARY KEY(targetName,thresholdName)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE PnmpCmGroup(
  entityId BIGINT(20),
  cmcId BIGINT(20),
  cmMac VARCHAR(20),
  CorrelationGroup int(11),
  PRIMARY KEY(entityId,cmcId,cmMac)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
/* -- version 2.10.0.0,build 2017-08-09,module cm */
-- version 2.10.0.9,build 2018-05-23,module cm
CREATE TABLE PnmpVarianceLast(
  entityId BIGINT(20),
  cmcId BIGINT(20),
  cmMac VARCHAR(20),
  mtrVariance DOUBLE,
  upSnrVariance DOUBLE,
  mtrToUpSnrSimilarity DOUBLE,
  collectTime TIMESTAMP,
  PRIMARY KEY(entityId,cmcId,cmMac)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE PnmpVarianceHis(
  entityId BIGINT(20),
  cmcId BIGINT(20),
  cmMac VARCHAR(20),
  mtrVariance DOUBLE,
  upSnrVariance DOUBLE,
  mtrToUpSnrSimilarity DOUBLE,
  collectTime TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
/* -- version 2.10.0.9,build 2018-05-23,module cm */
