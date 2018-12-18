/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-3-31,module platform

create procedure deleteAllTable()
begin
declare _tableName varchar(200);
declare _sql varchar(1000);
declare no_more_departments int default 0;   
declare rs_cursor CURSOR for SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='ems' and table_rows > 0;
declare continue handler for not found set no_more_departments=1;   
open rs_cursor;
repeat   
fetch rs_cursor into _tableName;   
set _sql = concat('delete from ems.' , _tableName);
set @sql = _sql;
prepare sl from @sql;
execute sl;
deallocate prepare sl;
until no_more_departments   
end repeat;   
close rs_cursor;   
end$$   

/* -- version 1.0.0,build 2011-3-31,module server */


-- version 2.5.2.0,build 2015-5-29,module platform

create procedure autoClearHistoryData(keepMonth int)
begin
declare dropStartTime timestamp;
declare _tableName varchar(100);
declare done int default 0;   
declare rs_cursor cursor for select tablename from historyTableName;
declare continue handler for not found set done=1;   
set dropStartTime = concat(date_format(date_sub(sysdate(), interval (keepMonth-1) month), '%Y%m') ,'01');
open rs_cursor;
read_loop: LOOP
fetch rs_cursor into _tableName;
if done THEN LEAVE read_loop;
end if;
call autoClearHistoryDataForTable(_tableName, dropStartTime);
end loop;
close rs_cursor;  
end$$
 
 
create procedure autoClearHistoryDataForTable(tableName varchar(100), dropStartTime timestamp)
begin
declare _sql varchar(1000);
declare _dropConcat varchar(100);
declare _addPartition varchar(20);
declare _insertCount int;
declare _error int default 0;
declare continue handler for sqlexception set _error=1;
start transaction;
set @pName = concat('p', date_format(sysdate(), '%Y%m'));
select partition_name into _addPartition from information_schema.partitions where table_name = tableName and partition_name = @pName;
if _addPartition is null then
set @pTime = date_add(concat(substring(@pName,2),'01'), interval 1 month);
set _sql = concat('alter table ', tableName, ' add partition (partition ', @pName, ' values less than (UNIX_TIMESTAMP(''',@pTime, ''')))');
select _sql;
set @sql = _sql;
prepare st from @sql;
execute st;
deallocate prepare st;
end if;
select group_concat(partition_name) into _dropConcat from information_schema.partitions where table_name = tableName
and partition_description <= unix_timestamp(dropStartTime);
if  _dropConcat is not null then
set _sql = concat('alter table ', tableName, ' drop partition ', _dropConcat);
select _sql;
set @sql = _sql;
prepare st from @sql;
execute st;
deallocate prepare st;
end if;
if _error = 0 then
commit;
else
rollback;
end if;
end$$ 

/* -- version 2.5.2.0,build 2015-5-29,module platform */

-- version 2.6.8.3,build 2017-1-6,module platform

drop procedure if exists autoClearHistoryDataForTable;

create procedure autoClearHistoryDataForTable(tableName varchar(100), dropStartTime timestamp)
begin
declare _sql varchar(1000);
declare _dropConcat varchar(100);
declare _addPartition varchar(20);
declare _insertCount int;
declare _error int default 0;
declare continue handler for sqlexception set _error=1;
start transaction;
-- add current month
set @pName = concat('p', date_format(sysdate(), '%Y%m'));
select partition_name into _addPartition from information_schema.partitions where table_name = tableName and partition_name = @pName;
if _addPartition is null then
set @pTime = date_add(concat(substring(@pName,2),'01'), interval 1 month);
set _sql = concat('alter table ', tableName, ' add partition (partition ', @pName, ' values less than (UNIX_TIMESTAMP(''',@pTime, ''')))');
select _sql;
set @sql = _sql;
prepare st from @sql;
execute st;
deallocate prepare st;
end if;
-- add next month
set @pName = concat('p', date_format(date_add(sysdate(), interval 1 month), '%Y%m'));
select partition_name into _addPartition from information_schema.partitions where table_name = tableName and partition_name = @pName;
if _addPartition is null then
set @pTime = date_add(concat(substring(@pName,2),'01'), interval 1 month);
set _sql = concat('alter table ', tableName, ' add partition (partition ', @pName, ' values less than (UNIX_TIMESTAMP(''',@pTime, ''')))');
select _sql;
set @sql = _sql;
prepare st from @sql;
execute st;
deallocate prepare st;
end if;
-- drop history month
select group_concat(partition_name) into _dropConcat from information_schema.partitions where table_name = tableName
and partition_description <= unix_timestamp(dropStartTime);
if  _dropConcat is not null then
set _sql = concat('alter table ', tableName, ' drop partition ', _dropConcat);
select _sql;
set @sql = _sql;
prepare st from @sql;
execute st;
deallocate prepare st;
end if;
if _error = 0 then
commit;
else
rollback;
end if;
end$$ 

/* -- version 2.6.8.3,build 2017-1-6,module platform */

-- version 2.8.1.4,build 2017-7-1,module platform

drop procedure if exists autoClearHistoryDataForTable;

create procedure autoClearHistoryDataForTable(tableName varchar(100), dropStartTime timestamp)
begin
declare _sql varchar(1000);
declare _dropConcat varchar(100);
declare _addPartition varchar(20);
declare _addNextPartition varchar(20);
declare _insertCount int;
declare _error int default 0;
declare continue handler for sqlexception set _error=1;
start transaction;
-- add current month
set @pName = concat('p', date_format(sysdate(), '%Y%m'));
select partition_name into _addPartition from information_schema.partitions where table_name = tableName and partition_name = @pName;
if _addPartition is null then
set @pTime = date_add(concat(substring(@pName,2),'01'), interval 1 month);
set _sql = concat('alter table ', tableName, ' add partition (partition ', @pName, ' values less than (UNIX_TIMESTAMP(''',@pTime, ''')))');
select _sql;
set @sql = _sql;
prepare st from @sql;
execute st;
deallocate prepare st;
end if;
-- add next month
set @pName = concat('p', date_format(date_add(sysdate(), interval 1 month), '%Y%m'));
select partition_name into _addNextPartition from information_schema.partitions where table_name = tableName and partition_name = @pName;
if _addNextPartition is null then
set @pTime = date_add(concat(substring(@pName,2),'01'), interval 1 month);
set _sql = concat('alter table ', tableName, ' add partition (partition ', @pName, ' values less than (UNIX_TIMESTAMP(''',@pTime, ''')))');
select _sql;
set @sql = _sql;
prepare st from @sql;
execute st;
deallocate prepare st;
end if;
-- drop history month
select group_concat(partition_name) into _dropConcat from information_schema.partitions where table_name = tableName
and partition_description <= unix_timestamp(dropStartTime);
if  _dropConcat is not null then
set _sql = concat('alter table ', tableName, ' drop partition ', _dropConcat);
select _sql;
set @sql = _sql;
prepare st from @sql;
execute st;
deallocate prepare st;
end if;
if _error = 0 then
commit;
else
rollback;
end if;
end$$ 

/* -- version 2.8.1.4,build 2017-7-1,module platform */
