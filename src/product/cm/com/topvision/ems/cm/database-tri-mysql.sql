-- version 2.5.1.0,build 2015-03-25,module cm
DROP TRIGGER IF EXISTS tri_despersion;
CREATE TRIGGER tri_despersion BEFORE INSERT ON dispersionhistory
FOR EACH ROW 
BEGIN
INSERT INTO 
	dispersionlast(opticalNodeId, collectTime, cmNum, upSnrAvg, upSnrStd, upSnrDist, upPowerAvg, upPowerStd, upPowerDist) 
VALUES
	(NEW.opticalNodeId, NEW.collectTime, NEW.cmNum, NEW.upSnrAvg, NEW.upSnrStd, NEW.upSnrDist, NEW.upPowerAvg, NEW.upPowerStd, NEW.upPowerDist)
ON DUPLICATE KEY update collectTime = NEW.collectTime, cmNum = NEW.cmNum, upSnrAvg = NEW.upSnrAvg, upSnrStd = NEW.upSnrStd, upSnrDist = NEW.upSnrDist, upPowerAvg = NEW.upPowerAvg, upPowerStd = NEW.upPowerStd, upPowerDist = NEW.upPowerDist;
END $$
/* -- version 2.5.1.0,build 2015-03-25,module cm */


-- version 2.10.0.0,build 2017-08-9,module cm
alter table PnmpCmDataLast add constraint FK_PnmpCmDataLast foreign key (cmcId) references cmcEntityRelation (cmcId) on delete cascade on update cascade;
alter table PnmpCmDataHis add constraint FK_PnmpCmDataHis foreign key (cmcId) references cmcEntityRelation (cmcId) on delete cascade on update cascade;

DROP TRIGGER IF EXISTS tri_pnmp_cmdata_insert;
CREATE TRIGGER tri_pnmp_cmdata_insert AFTER INSERT ON PnmpCmDataLast
FOR EACH ROW BEGIN
insert into PnmpCmDataHis
	(cmmac,statusValue,checkStatus,entityId,cmcId,mte,preMte,postMte,tte,mtc,mtr,nmtter,premtter,postmtter,
	ppesr,mrLevel,Tdr,upSnr,upTxPower,downSnr,DownRxPower,collectTime
	)
	values
	(NEW.cmmac,NEW.statusValue,NEW.checkStatus,NEW.entityId,NEW.cmcId,NEW.mte,NEW.preMte,NEW.postMte,NEW.tte,NEW.mtc,NEW.mtr,NEW.nmtter,NEW.premtter,NEW.postmtter,
	NEW.ppesr,NEW.mrLevel,NEW.Tdr,NEW.upSnr,NEW.upTxPower,NEW.downSnr,NEW.DownRxPower,NEW.collectTime
	);
END $$ 

DROP TRIGGER IF EXISTS tri_pnmp_cmdata_update;
CREATE TRIGGER tri_pnmp_cmdata_update AFTER UPDATE ON PnmpCmDataLast
FOR EACH ROW BEGIN
insert into PnmpCmDataHis
	(cmmac,statusValue,checkStatus,entityId,cmcId,mte,preMte,postMte,tte,mtc,mtr,nmtter,premtter,postmtter,
	ppesr,mrLevel,Tdr,upSnr,upTxPower,downSnr,DownRxPower,collectTime
	)
	values
	(NEW.cmmac,NEW.statusValue,NEW.checkStatus,NEW.entityId,NEW.cmcId,NEW.mte,NEW.preMte,NEW.postMte,NEW.tte,NEW.mtc,NEW.mtr,NEW.nmtter,NEW.premtter,NEW.postmtter,
	NEW.ppesr,NEW.mrLevel,NEW.Tdr,NEW.upSnr,NEW.upTxPower,NEW.downSnr,NEW.DownRxPower,NEW.collectTime
	);
END $$ 
/* -- version 2.10.0.0,build 2017-08-9,module cm */