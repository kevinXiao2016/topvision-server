/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 2.0.6.6,build 2014-06-23,module admin
create table SystemMonitor
(
   collectTime      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   cpu              DECIMAL(5,4),
   heapMemory       bigint(20),
   nonHeapMemory    bigint(20),
   disk             bigint(20),
   primary key (collectTime)
);
/* -- version 2.0.6.6,build 2014-06-23,module admin */

-- version 2.2.5.0,build 2014-07-15,module admin
ALTER table SystemMonitor add COLUMN (threadCount bigint(20));
/* -- version 2.2.5.0,build 2014-07-15,module admin */

-- version 2.8.0.6,build 2017-01-04,module admin
ALTER table SystemMonitor add COLUMN (diskReads bigint(20) default 0);
ALTER table SystemMonitor add COLUMN (diskWrites bigint(20) default 0);
/* -- version 2.8.0.6,build 2017-01-04,module admin */
