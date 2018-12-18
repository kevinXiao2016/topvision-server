/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

/* modify by Rod Authority Default View*/
-- version 1.7.12.1,build 2013-04-10,module server
DROP FUNCTION if exists topoFolderFun;

CREATE FUNCTION topoFolderFun(folderIdTmp bigint(20)) RETURNS varchar(1000) 
DETERMINISTIC
begin
declare sTemp varchar(1000);
declare sTempChd varchar(1000);
set sTemp = '';
set sTempChd = cast(folderIdTmp as char);
lable : loop 
set sTemp = concat(sTemp,',',sTempChd);
select group_concat(folderId) into sTempChd from topofolder where find_in_set(superiorId, sTempChd) > 0;
if found_rows()=0 or sTempChd is null then
return substring(sTemp,2);
end if;
end loop;
end$$
/* -- version 1.7.12.1,build 2013-04-10,module server */


-- version 1.7.16.0,build 2013-08-09,module server
create procedure autoInsertAuthotiry(in entityId bigint(20), in folderId int(10))
begin
declare _sql varchar(1000);
declare _selectSql varchar(1000);
declare _count int(10);
declare _folderIdInSearch int(10);
declare _tableName varchar(100);
declare no_more_table int default 0;   
declare rs_cursor cursor for select table_name from information_schema.tables where table_schema = 'ems' and table_name like 't_entity_%' and table_type = 'base table'; 
declare continue handler for not found set no_more_table=1;   
open rs_cursor;
repeat   
fetch rs_cursor into _tableName;
block1:begin
declare no_more_folder int default 0;  
declare continue handler for not found set no_more_folder = -1;  
set _folderIdInSearch = right(_tableName, 2);
set _count = 0;
select 1 into _count from dual where find_in_set(folderId,topoFolderFun(_folderIdInSearch));
if(_count = 1) then
set _sql = concat('insert into ', _tableName, ' values(', entityId, ')'); 
set @sql = _sql;
prepare s2 from @sql;
execute s2;
deallocate prepare s2;
end if;
end block1;
until no_more_table
end repeat;   
close rs_cursor;   
end$$
/* -- version 1.7.16.0,build 2013-08-09,module server */

-- version 2.0.0.0,build 2013-11-13,module server
CREATE FUNCTION topoEventFun(id bigint(20), tableName varchar(20)) RETURNS varchar(1000) 
DETERMINISTIC
begin
declare sTemp varchar(1000);
declare sTempChd varchar(1000);
set sTemp = '';
set sTempChd = cast(id as char);
lable : loop
set sTemp = concat(sTemp,',',sTempChd);
if(tableName = 'eventtype') then  
select group_concat(typeId) into sTempChd from eventtype where find_in_set(parentId, sTempChd) > 0;
elseif(tableName = 'alerttype') then 
select group_concat(typeId) into sTempChd from alertType where find_in_set(category, sTempChd) > 0;
end if;
if found_rows()=0 or sTempChd is null then
return substring(sTemp,2);
end if;
end loop;
end $$
/* -- version 2.0.0.0,build 2013-11-13,module server */


-- version 2.2.2.0,build 2014-8-12,module server
drop event if exists perfSummaryEvent;
create event perfSummaryEvent
on schedule every 1 day
starts date_add(date(sysdate()), interval 26 hour)
on completion preserve
enable
comment 'PerfSummaryEvent'
do 
begin
if  exists(select * from mysql.proc where name='sp_perfeponflowsummary' and db='ems' and type='PROCEDURE') then 
call sp_perfeponflowsummary();
end if;
if  exists(select * from mysql.proc where name='sp_perfcmcsnrsummary' and db='ems' and type='PROCEDURE') then 
call sp_perfcmcsnrsummary();
call sp_perfcmcflowsummary();
call sp_perfcmtssnrsummary();
call sp_perfcmtsflowsummary();
call sp_perfusernumhisccmtssummary();
call sp_perfusernumhisdevicesummary();
call sp_perfusernumhisponsummary();
call sp_perfusernumhisportsummary();
end if;
end $$
/* -- version 2.2.2.0,build 2014-8-12,module server */