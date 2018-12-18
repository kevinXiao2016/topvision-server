/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-3-31,module server

CREATE TRIGGER tri_connectivity AFTER INSERT ON connectivity
FOR EACH ROW BEGIN
  IF NEW.itemValue<0 THEN
	INSERT INTO entitysnap(entityId, state, delay, delayTime, snapTime) VALUES(NEW.entityId, '0', -1, NEW.collectTime, NEW.collectTime)
	ON DUPLICATE KEY update state = '0', delay = -1, delayTime = NEW.collectTime;
  ELSE
	INSERT INTO entitysnap(entityId, state, delay, delayTime, snapTime) VALUES(NEW.entityId, '1', NEW.itemValue, NEW.collectTime, NEW.collectTime)
	ON DUPLICATE KEY update state = '1', delay = NEW.itemValue, delayTime = NEW.collectTime;
  END IF;   
END$$

CREATE TRIGGER tri_monitorvalue AFTER INSERT ON monitorvalue
FOR EACH ROW BEGIN
  IF NEW.itemName='cpu' THEN
	UPDATE entitysnap SET cpu = NEW.itemValue, snapTime = NEW.collectTime WHERE entityId = NEW.entityId;
  ELSEIF NEW.itemName='mem' THEN
	UPDATE entitysnap SET mem = NEW.itemValue, usedMem = NEW.extValue, snapTime = NEW.collectTime WHERE entityId = NEW.entityId;
  ELSEIF NEW.itemName='disk' THEN
	UPDATE entitysnap SET disk = NEW.itemValue, usedDisk = NEW.extValue, snapTime = NEW.collectTime WHERE entityId = NEW.entityId;
  END IF;   
END$$

CREATE TRIGGER tri_portperf AFTER INSERT ON portperf
FOR EACH ROW BEGIN
    INSERT INTO PortSnap(entityId, ifIndex, ifOctets, ifOctetsRate,	ifInOctets, ifInOctetsRate,
    	ifOutOctets, ifOutOctetsRate, ifUcastPkts, 
		ifInUcastPkts, ifOutUcastPkts, ifNUcastPkts, ifInNUcastPkts, ifOutNUcastPkts,
		ifErrors, ifErrorsRate, ifInErrors, ifInErrorsRate, ifOutErrors, ifOutErrorsRate,
		ifDiscards, ifDiscardsRate, ifInDiscards, ifInDiscardsRate, ifOutDiscards,
        ifOutDiscardsRate, collectTime)
    VALUES
    	(NEW.entityId, NEW.ifIndex, NEW.ifOctets, NEW.ifOctetsRate,
		NEW.ifInOctets, NEW.ifInOctetsRate, NEW.ifOutOctets, NEW.ifOutOctetsRate, NEW.ifUcastPkts, 
		NEW.ifInUcastPkts, NEW.ifOutUcastPkts, NEW.ifNUcastPkts, NEW.ifInNUcastPkts, NEW.ifOutNUcastPkts,
		NEW.ifErrors, NEW.ifErrorsRate, NEW.ifInErrors, NEW.ifInErrorsRate, NEW.ifOutErrors, NEW.ifOutErrorsRate,
		NEW.ifDiscards, NEW.ifDiscardsRate, NEW.ifInDiscards, NEW.ifInDiscardsRate, NEW.ifOutDiscards,
        NEW.ifOutDiscardsRate, NEW.collectTime)
    ON DUPLICATE KEY UPDATE
    	ifOctets = NEW.ifOctets, ifOctetsRate = NEW.ifOctetsRate,
		ifInOctets = NEW.ifInOctets, ifInOctetsRate = NEW.ifInOctetsRate, ifOutOctets = NEW.ifOutOctets,
		ifOutOctetsRate = NEW.ifOutOctetsRate, ifUcastPkts = NEW.ifUcastPkts, 
		ifInUcastPkts = NEW.ifInUcastPkts, ifOutUcastPkts = NEW.ifOutUcastPkts, 
		ifNUcastPkts = NEW.ifNUcastPkts, ifInNUcastPkts = NEW.ifInNUcastPkts, 
		ifOutNUcastPkts = NEW.ifOutNUcastPkts, ifErrors = NEW.ifErrors, ifErrorsRate = NEW.ifErrorsRate, 
		ifInErrors = NEW.ifInErrors, ifInErrorsRate = NEW.ifInErrorsRate, ifOutErrors = NEW.ifOutErrors, 
		ifOutErrorsRate = NEW.ifOutErrorsRate, ifDiscards = NEW.ifDiscards, ifDiscardsRate = NEW.ifDiscardsRate, 
		ifInDiscards = NEW.ifInDiscards, ifInDiscardsRate = NEW.ifInDiscardsRate, 
		ifOutDiscards = NEW.ifOutDiscards, ifOutDiscardsRate = NEW.ifOutDiscardsRate, collectTime = NEW.collectTime;
	UPDATE Link SET srcIfOctets = NEW.ifOctets, srcIfOctetsRate = NEW.ifOctetsRate
		WHERE srcEntityId = NEW.entityId AND srcIfIndex = NEW.ifIndex;
	UPDATE Link SET destIfOctets = NEW.ifOctets, destIfOctetsRate = NEW.ifOctetsRate
		WHERE destEntityId = NEW.entityId AND destIfIndex = NEW.ifIndex;
END$$

CREATE TRIGGER tri_insert_link BEFORE INSERT ON Link
FOR EACH ROW BEGIN
    DECLARE srcSpeed decimal(16,4);
    DECLARE destSpeed decimal(16,4);
    SELECT ifSpeed INTO srcSpeed FROM Port WHERE entityId = NEW.srcEntityId AND ifIndex = NEW.srcIfIndex;
    SELECT ifSpeed INTO destSpeed FROM Port WHERE entityId = NEW.destEntityId AND ifIndex = NEW.destIfIndex;
    SET NEW.srcIfSpeed = IFNULL(srcSpeed,0);
    SET NEW.destIfSpeed = IFNULL(destSpeed,0);
END$$

CREATE TRIGGER tri_update_link BEFORE UPDATE ON Link
FOR EACH ROW BEGIN
    DECLARE srcSpeed decimal(16,4);
    DECLARE destSpeed decimal(16,4);
    SELECT ifSpeed INTO srcSpeed FROM Port WHERE entityId = NEW.srcEntityId AND ifIndex = NEW.srcIfIndex;
    SELECT ifSpeed INTO destSpeed FROM Port WHERE entityId = NEW.destEntityId AND ifIndex = NEW.destIfIndex;
    SET NEW.srcIfSpeed = IFNULL(srcSpeed,0);
    SET NEW.destIfSpeed = IFNULL(destSpeed,0);
END$$
/* -- version 1.0.0,build 2011-3-31,module server */

-- version 1.7.16.0,build 2013-7-26,module server
drop trigger if exists tri_create_new_entity;

create trigger tri_create_new_entity after insert on entity
for each row begin 
    declare v_authority_userId int(11);
    declare v_entityId bigint(20);
    declare v_parentId bigint(20);
    declare v_topoId int(11);
    declare v_x int(11);
    declare v_y int(11);
    declare no_more_rows int default 0;  
    declare rs_cursor cursor for select folderId from entityfolderrela where entityId = NEW.parentId;
    declare continue handler for not found set no_more_rows=1;   
    set v_authority_userId = NEW.authorityUserId;
    set v_parentId = NEW.parentId;
    set v_entityId = NEW.entityId;
    if(v_parentId is not null) then 
    open rs_cursor;
    repeat   
    fetch rs_cursor into v_topoId; 
    if no_more_rows = 0 then 
    set v_x = RAND()*600;
    set v_y = RAND()*400;
    insert into entityfolderrela(entityId, folderId, x, y, visible) values (v_entityId, v_topoId, v_x, v_y, 1);
    end if;
    until no_more_rows   
    end repeat;   
    close rs_cursor; 
    elseif (v_authority_userId is not null) then 
    set v_topoId = (select userGroupId from users where userId = v_authority_userId);
    set v_x = RAND()*600;
    set v_y = RAND()*400;
    insert into entityfolderrela(entityId, folderId, x, y, visible) values (v_entityId, v_topoId, v_x, v_y, 1);
    end if;
END$$
/* -- version 1.7.16.0,build 2013-7-26,module server */

-- version 2.6.4.0,build 2015-9-21,module server
drop trigger if exists tri_AliasLog;
create trigger tri_AliasLog AFTER update on entity for each row 
	begin 
	IF NEW.name!=OLD.name then
	insert into AliasLog(entityId,mac,ip,name) values (NEW.entityId,NEW.mac,NEW.ip,NEW.name);
	end if;
end$$
/* -- version 2.6.4.0,build 2015-9-21,module server */

-- version 2.6.6.0,build 2015-12-01,module server
drop trigger if exists tri_AliasLog;
drop trigger if exists tri_AliasUpdateLog;
create trigger tri_AliasUpdateLog AFTER update on entity for each row 
	begin 
	IF NEW.name!=OLD.name then
	insert into AliasLog(entityId,mac,ip,name) values (NEW.entityId,NEW.mac,NEW.ip,NEW.name);
	end if;
end$$

drop trigger if exists tri_AliasDeleteLog;
create trigger tri_AliasDeleteLog AFTER delete on entity for each row 
	begin 
	insert into AliasLog(entityId,mac,ip,name) values (OLD.entityId,OLD.mac,OLD.ip,OLD.name);
end$$
/* -- version 2.6.6.0,build 2015-12-01,module server */


-- version 2.6.8.0,build 2016-9-8,module server
drop trigger tri_AliasDeleteLog;
create trigger tri_AliasDeleteLog AFTER DELETE on entity
for each row 
begin 
declare `index` bigint(20);
select onuIndex into `index` from oltonurelation where onuId=old.entityId;
insert into AliasLog(entityId, parentId, oldname, newname, ip, mac, `index`, typeId, createTime, actionType, actionTime) values 
(old.entityId, old.parentId, old.name, null, old.ip, old.mac, `index`, old.typeId, old.createTime, 1, sysdate()); 
end$$

drop trigger tri_AliasUpdateLog;
create trigger tri_AliasUpdateLog AFTER UPDATE on entity 
for each row begin 
declare `index` bigint(20);
IF new.name!=old.name then 
select onuIndex into `index` from oltonurelation where onuId=old.entityId;
insert into AliasLog(entityId, parentId, oldname, newname, ip, mac, `index`, typeId,createTime, actionType, actionTime) values 
(old.entityId, old.parentId, old.name, new.name, old.ip, old.mac, `index`, old.typeId, old.createTime, 2, sysdate());
end if; 
end$$
/* -- version 2.6.8.0,build 2016-9-8,module server */

-- version 2.9.0.0,build 2017-3-8 13:56:00,module server
drop trigger tri_AliasDeleteLog;
create trigger tri_AliasDeleteLog AFTER DELETE on entity
for each row 
begin 
declare `index` bigint(20);
declare `sn` varchar(500);
select onuIndex into `index` from oltonurelation where onuId=old.entityId;
select onuUniqueIdentification into `sn` from oltonuattribute where onuId=old.entityId;
insert into AliasLog(entityId, parentId, oldname, newname, ip, mac, `index`,`sn`, typeId, createTime, actionType, actionTime) values 
(old.entityId, old.parentId, old.name, null, old.ip, old.mac, `index`,`sn`, old.typeId, old.createTime, 1, sysdate()); 
end$$

drop trigger tri_AliasUpdateLog;
create trigger tri_AliasUpdateLog AFTER UPDATE on entity 
for each row begin 
declare `index` bigint(20);
declare `sn` varchar(500);
IF new.name!=old.name then 
select onuIndex into `index` from oltonurelation where onuId=old.entityId;
select onuUniqueIdentification into `sn` from oltonuattribute where onuId=old.entityId;
insert into AliasLog(entityId, parentId, oldname, newname, ip, mac, `index`,`sn`, typeId,createTime, actionType, actionTime) values 
(old.entityId, old.parentId, old.name, new.name, old.ip, old.mac, `index`,`sn`, old.typeId, old.createTime, 2, sysdate());
end if; 
end$$
/* -- version 2.9.0.0,build 2017-3-8 13:56:00,module server */