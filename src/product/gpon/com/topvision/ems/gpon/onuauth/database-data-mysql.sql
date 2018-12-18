-- version 2.9.0.0,build 2016-12-29,module gpon
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000018,'gponOnuAutoFindList',4000000,'userPower.gponOnuAutoFindList');
  
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_GPON_TX_POWER', 10000, 'Performance.gponOptTxPower','Performance.optLink','dBm',6,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_GPON_RE_POWER', 13000, 'Performance.onuGponRePower','Performance.optLink','dBm',-7, -27, 1, 2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_GPON_TX_POWER', 13000, 'Performance.onuGponTxPower','Performance.optLink','dBm', 6, 0, 1, 2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_GPONLLID_RE_POWER', 13000, 'Performance.oltGponRePower','Performance.optLink','dBm', -7, -28, 1, 2);

/****************************************************************
INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange,clearRules) values
('OLT_GPON_TX_POWER', 1,'1_5.5_3_4#5_1.5_3_5',1,1,1,'00:00-23:59#1234567','5_5#1_2'),	
('OLT_GPON_TX_POWER', 1000,'1_5.5_3_4#5_1.5_3_5',1,1,1,'00:00-23:59#1234567','5_5#1_2'),
('ONU_GPON_RE_POWER', 3,'1_-7_2_4#5_-27_2_5',1,1,1,'00:00-23:59#1234567','5_-8#1_-25'),
('ONU_GPON_TX_POWER', 3,'1_5.5_2_4#5_0.5_2_5',1,1,1,'00:00-23:59#1234567','5_5#1_1'),
('OLT_GPONLLID_RE_POWER', 3,'1_-7_2_4#5_-28_2_5',1,1,1,'00:00-23:59#1234567','5_-8#1_-26');
*****************************************************************/

/* -- version 2.9.0.0,build 2016-12-29,module gpon  */

-- version 2.9.0.0,build 2017-4-13,module gpon
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000018);
/* -- version 2.9.0.0,build 2017-4-13,module gpon  */
        