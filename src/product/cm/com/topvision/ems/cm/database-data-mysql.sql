/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 2.5.1.0,build 2015-3-7,module cm
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('cmPollStatus',   '1',  'cmPoll'),
                ('cmPollInterval',   '14400',  'cmPoll');
/* -- version 2.5.1.0,build 2015-3-7,module cm */
                
-- version 2.5.1.0,build 2015-3-12,module cm
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('cmCount',   '100',  'cmPoll'),
                ('maxCollectThread',   '10',  'cmPoll');
/* -- version 2.5.1.0,build 2015-3-12,module cm */
                
-- version 2.5.1.0,build 2015-03-25,module cm
DROP VIEW IF EXISTS opticalNode;
CREATE VIEW opticalNode AS
SELECT entityId id, name
FROM entity
WHERE typeId>=30000 AND typeId<40000;

DROP VIEW IF EXISTS opticalNodeRelation;
CREATE VIEW opticalNodeRelation AS
SELECT opticalnode.id opticalNodeId, ca.cmcId cmtsId, cub.channelIndex
FROM cmcattribute ca, cmcupchannelbaseinfo cub, opticalnode
WHERE ca.cmcId=cub.cmcId AND ca.cmcId=opticalnode.id;
/* -- version 2.5.1.0,build 2015-03-25,module cm */

-- version 2.5.1.0,build 2015-3-25,module cm
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (5000018,'cmPollConfig',5000000,'userPower.cmPollConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,5000018);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (5000019,'cmCollectList',5000000,'userPower.cmPollCollectList');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,5000019);
/* -- version 2.5.1.0,build 2015-3-25,module cm */
        
        
-- version 2.6.0.0,build 2016-12-08,module cm
insert into systempreferences values('cmUpgrade.interval', 86400, 'cmUpgrade');
/* -- version 2.6.0.0,build 2015-12-08,module cm */


-- version 2.10.0.0,build 2017-08-09,module cm
insert into PnmpTargetThreshold(targetName,thresholdName,thresholdLevel,lowValue,highValue) values
('mtc','health', 0, null,2),
('mtc','bad', 2, 2,null),

('mtr','health', 0, 25,null),
('mtr','marginal', 1, 18,25),
('mtr','bad', 2, null,18),

('nmtter','health', 0, null,-17),
('nmtter','marginal', 1, -17,-11),
('nmtter','bad', 2, -11,null),

('premtter','health', 0, null,-17),
('premtter','marginal', 1, -17,-11),
('premtter','bad', 2, -11,null),

('postmtter','health', 0, null,-17),
('postmtter','marginal', 1, -17,-11),
('postmtter','bad', 2, -11,null),

('ppesr','health', 0, -3,3),
('ppesr','mrlevelMarginal', 1, -9,-3),
('ppesr','mrlevelBad', 2, null,-9),
('ppesr','delayMarginal', 1, 3,9),
('ppesr','delayBad', 2, 9,null),

('mrlevel','health', 0, null,-25),
('mrlevel','marginal', 1, -25,-18),
('mrlevel','bad', 2, -18,null);

insert into CmSignalTargetThreshold(targetName,thresholdName,thresholdLevel,lowValue,highValue) values
('upSendPower','health', 0, 8,50),
('upSendPower','tooLow', 2, null,8),
('upSendPower','tooHigh', 2, 50,null),

('downRePower','health', 0, -15,15),
('downRePower','tooLow', 2, null,-15),
('downRePower','tooHigh', 2, 15,null),

('upSnr','health', 0, 15,null),
('upSnr','bad', 2, null,15),

('downSnr','health', 0, 27,null),
('downSnr','bad', 2, null,27);


INSERT INTO systempreferences(name,  value,  module) VALUES
                ('PNMP.correlationGroupMtrLevel',   '25',  'PNMP');
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('PNMP.isDebug',   'false',  'PNMP');
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('PNMP.intervalLow',   '115200',  'PNMP');
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('PNMP.intervalMiddle',   '14400',  'PNMP');
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('PNMP.intervalHigh',   '600',  'PNMP');
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('PNMP.lowSpeedStartTime',   '8,16,24',  'PNMP');
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('PNMP.middleSpeedStartTime',   '2,6,10,14,18,22',  'PNMP');
/* -- version 2.10.0.0,build 2017-08-09,module cm */
-- version 2.10.0.9,build 2018-05-23,module cm
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('PNMP.varianceInterval',   '604800000',  'PNMP');

update PnmpTargetThreshold set lowValue=20 where targetName='mtr' and thresholdName='health';
update PnmpTargetThreshold set lowValue=10,highValue=20 where targetName='mtr' and thresholdName='marginal';
update PnmpTargetThreshold set highValue=10 where targetName='mtr' and thresholdName='bad';

delete from PnmpTargetThreshold where targetName='mrlevel' and thresholdName='health';
delete from PnmpTargetThreshold where targetName='mrlevel' and thresholdName='marginal';
delete from PnmpTargetThreshold where targetName='mrlevel' and thresholdName='bad';

/* -- version 2.10.0.9,build 2018-05-23,module cm */
-- version 2.10.0.13,build 2018-09-11,module cm
delete from systempreferences where name ='PNMP.varianceInterval';
/* -- version 2.10.0.13,build 2018-09-11,module cm */