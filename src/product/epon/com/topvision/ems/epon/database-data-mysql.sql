/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-9-8,module epon
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (10000,  'olt', 'OLT', 'epon', '/epon',  '1.3.6.1.4.1.32285.11.2.1', 'network/olt_16.gif', 'network/olt_32.gif', 'network/olt_48.gif', 'network/olt_64.gif', 1);
--modify by victor @2011.10.26暂时没有这种类型，需要时添加
--INSERT INTO EntityType(typeId, name, displayName, module, modulePath, type, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
--                (11000,  'mdu', 'MDU', 'epon', '/epon/mdu', 10000,  '1.3.6.1.4.1.32285.11.2.4', 'network/mdu_16.gif', 'network/mdu_32.gif', 'network/mdu_48.gif', 'network/mdu_64.gif', 1);

INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (10001,  'pn8601', 'PN8601', 'epon', '/epon',  '1.3.6.1.4.1.32285.11.2.1.1', 'network/olt_16.gif', 'network/olt_32.gif', 'network/olt_48.gif', 'network/olt_64.gif', 1);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (10002,  'pn8602', 'PN8602', 'epon', '/epon/8602',  '1.3.6.1.4.1.32285.11.2.1.2', 'network/8602_16.gif', 'network/8602_32.gif', 'network/8602_48.gif', 'network/8602_64.gif', 1);
--modify by loyal  ONU类型，添加到拓扑图功能需要
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (33,  'pn8621', 'PN8621', 'epon', '/epon',  '', 'network/onu/8624Icon_16.png', 'network/onu/8624Icon_32.png', 'network/onu/8624Icon_48.png', 'network/onu/8624Icon_64.png', 1);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (34,  'pn8622', 'PN8622', 'epon', '/epon',  '', 'network/onu/outDoorOnuIcon_16.png', 'network/onu/outDoorOnuIcon_32.png', 'network/onu/outDoorOnuIcon_48.png', 'network/onu/outDoorOnuIcon_64.png', 1);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (36,  'pn8624', 'PN8624', 'epon', '/epon',  '', 'network/onu/8624Icon_16.png', 'network/onu/8624Icon_32.png', 'network/onu/8624Icon_48.png', 'network/onu/8624Icon_64.png', 1);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (37,  'pn8625', 'PN8625', 'epon', '/epon',  '', 'image/8622Icon.png', 'image/8622Icon.png', 'image/8622Icon.png', 'image/8622Icon.png', 1);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (65,  'pn8641', 'PN8641', 'epon', '/epon',  '', 'network/onu/8624Icon_16.png', 'network/onu/8624Icon_32.png', 'network/onu/8624Icon_48.png', 'network/onu/8624Icon_64.png', 1);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (67,  'pn8643', 'PN8643', 'epon', '/epon',  '', 'network/onu/8624Icon_16.png', 'network/onu/8624Icon_32.png', 'network/onu/8624Icon_48.png', 'network/onu/8624Icon_64.png', 1);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (69,  'pn8645', 'PN8645', 'epon', '/epon',  '', 'network/onu/8624Icon_16.png', 'network/onu/8624Icon_32.png', 'network/onu/8624Icon_48.png', 'network/onu/8624Icon_64.png', 1);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (255,  'UNKNOWN_ONU', 'UNKNOWN_ONU', 'epon', '/epon',  '', 'network/onu/8624Icon_16.png', 'network/onu/8624Icon_32.png', 'network/onu/8624Icon_48.png', 'network/onu/8624Icon_64.png', 1);

--modify by victor @2011.10.26原计划虚拟一个来表示不可管OLT，去掉，使用8602来替代，通过后台采集屏蔽这种差异
--INSERT INTO EntityType(typeId, name, displayName, module, modulePath, type, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
--                (10003,  'pn8603', 'PN8603', 'epon', '/epon/8603', 10000,  '1.3.6.1.4.1.32285.11.2.3.3', 'network/olt_16.gif', 'network/olt_32.gif', 'network/olt_48.gif', 'network/olt_64.gif', 1);

/* -- version 1.0.0,build 2011-9-8,module epon */

-- version 1.0.0,build 2011-12-21,module epon
insert into OltOnuTypeInfo values('PN8621',33,1,0,1,'DB.oltOnuTypeInfo.onu33', 'image/onuIcon.png');
insert into OltOnuTypeInfo values('PN8622',34,1,0,1,'DB.oltOnuTypeInfo.onu34', 'image/8622Icon.png');
insert into OltOnuTypeInfo values('PN8624',36,0,4,1,'DB.oltOnuTypeInfo.onu36', 'image/onuIcon.png');
insert into OltOnuTypeInfo values('PN8625',37,0,4,1,'DB.oltOnuTypeInfo.onu37', 'image/8625Icon.png');

insert into OltOnuTypeInfo values('PN8641',65,0,8,1,'DB.oltOnuTypeInfo.onu65', 'image/onuIcon.png');
insert into OltOnuTypeInfo values('PN8643',67,0,16,1,'DB.oltOnuTypeInfo.onu67', 'image/onuIcon.png');
insert into OltOnuTypeInfo values('PN8645',69,0,24,1,'DB.oltOnuTypeInfo.onu69', 'image/onuIcon.png');
insert into OltOnuTypeInfo values('UNKNOWN',255,0,0,0,'DB.oltOnuTypeInfo.onu255', 'image/blankOnu.png');

/* -- version 1.0.0,build 2011-12-21,module epon */

-- version 1.0.0,build 2011-12-22,module epon
/*EPON告警代码*/

INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-7,-10000,'Epon Alert','DB.alertType.eponAlert',0,'',0,0,'0','1',NULL,NULL);
INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES

        (1,  -7, 'E_ALM_SYSTEM_RESET', 'DB.alertType.a1', 4, '', 0, 0, '0', '1', '', ''),

        (4101,  -7, 'E_RT_ALM_BD_REMOVE', 'DB.alertType.a4101', 4, '', 0, 0, '0', '1', '', ''),

        (4109,  -7, 'E_RT_ALM_BD_FAN_REMOVE', 'DB.alertType.a4109', 5, '', 0, 0, '0', '1', '', ''),

        (4113,  -7, 'E_RT_ALM_BD_PWR_REMOVE', 'DB.alertType.a4113', 5, '', 0, 0, '0', '1', '', ''),

        (16433,  -7, 'E_ALM_ONU_OFFLINE', 'DB.alertType.a16433', 4, '', 0, 0, '0', '1', '', ''),

      --(8193,  -7, 'E_ALM_PORT_LINK_DOWN', 'DB.alertType.a8193', 5, '', 0, 0, '0', '1', '', ''),

        (4098,  -7, 'E_ALM_BD_PROV_FAIL', 'DB.alertType.a4098', 4, '', 0, 0, '0', '1', '', ''),

      --(4103,  -7, 'E_ALM_BD_TEMP_HIGH', 'DB.alertType.a4103', 2, '', 0, 0, '0', '1', '', ''),

      --(4105,  -7, 'E_ALM_BD_TEMP_LOW', 'DB.alertType.a4105', 2, '', 0, 0, '0', '1', '', ''),

      --(4107,  -7, 'E_ALM_BD_FAN_FAIL', 'DB.alertType.a4107', 4, '', 0, 0, '0', '1', '', ''),

      --(4111,  -7, 'E_ALM_BD_PWR_FAIL', 'DB.alertType.a4111', 5, '', 0, 0, '0', '1', '', ''),

        (4115,  -7, 'E_ALM_BD_PONCHIP_ERROR', 'DB.alertType.a4115', 4, '', 0, 0, '0', '1', '', ''),

        (4117,  -7, 'E_ALM_BD_TYPE_MISMATCH', 'DB.alertType.a4117', 5, '', 0, 0, '0', '1', '', ''),

        (4119,  -7, 'E_ALM_BD_SW_MISMATCH', 'DB.alertType.a4119', 5, '', 0, 0, '0', '1', '', ''),

        (4121,  -7, 'E_ALM_BD_SLOT_MISMATCH', 'DB.alertType.a4121', 5, '', 0, 0, '0', '1', '', ''),

      --(4123,  -7, 'E_ALM_BD_PERF_EXCEED_HIGH', 'DB.alertType.a4123', 3, '', 0, 0, '0', '1', '', ''),

      --(4124,  -7, 'E_ALM_BD_PERF_EXCEED_LOW', 'DB.alertType.a4124', 3, '', 0, 0, '0', '1', '', ''),

        (16388,  -7, 'E_ALM_ONU_MAC_AUTH_ERROR', 'DB.alertType.a16388', 2, '', 0, 0, '0', '1', '', ''),

      --(16398,  -7, 'E_ALM_ONU_PERF_EXCEED_HIGH', 'DB.alertType.a16398', 4, '', 0, 0, '0', '1', '', ''),

      --(16399,  -7, 'E_ALM_ONU_PERF_EXCEED_LOW', 'DB.alertType.a16399', 4, '', 0, 0, '0', '1', '', ''),

        (16393,  -7, 'E_RT_ALM_ONU_PWR_DOWN', 'DB.alertType.a16393', 2, '', 0, 0, '0', '1', '', ''),

        (20483,  -7, 'E_ALM_ONU_UNI_LOOP', 'DB.alertType.a20483', 3, '', 0, 0, '0', '1', '', ''),

        (20485,  -7, 'E_ALM_ONU_UNI_AUTONEG_FAIL', 'DB.alertType.a20485', 2, '', 0, 0, '0', '1', '', ''),

        (4099,  -7, 'E_ALM_BD_RESET', 'DB.alertType.a4099', 4, '', 0, 0, '0', '1', '', ''),

        (4125,  -7, 'E_ALM_BD_CPU_OVERLOAD', 'DB.alertType.a4125', 3, '', 0, 0, '0', '1', '', ''),

        (4127,  -7, 'E_ALM_BD_HEARTBEAT_LOST', 'DB.alertType.a4127', 4, '', 0, 0, '0', '1', '', ''),

        (12289,  -7, 'E_ALM_PORT_PON_LOOPBACK', 'DB.alertType.a12289', 4, '', 0, 0, '0', '1', '', ''),

        (12290,  -7, 'E_ALM_PORT_SFP_FAIL', 'DB.alertType.a12290', 4, '', 0, 0, '0', '1', '', ''),

        (12293,  -7, 'E_ALM_PORT_PON_DISABLE', 'DB.alertType.a12293', 5, '', 0, 0, '0', '1', '', ''),

        (12295,  -7, 'E_ALM_PORT_PON_LLID_EXCD', 'DB.alertType.a12295', 2, '', 0, 0, '0', '1', '', ''),

        (12305,  -7, 'E_ALM_PORT_SFP_RXPWR_HIGH', 'DB.alertType.a12305', 4, '', 0, 0, '0', '1', '', ''),

        (12306,  -7, 'E_ALM_PORT_SFP_RXPWR_LOW', 'DB.alertType.a12306', 4, '', 0, 0, '0', '1', '', ''),

        (12307,  -7, 'E_ALM_PORT_SFP_TXPWR_HIGH', 'DB.alertType.a12307', 4, '', 0, 0, '0', '1', '', ''),

        (12308,  -7, 'E_ALM_PORT_SFP_TXPWR_LOW', 'DB.alertType.a12308', 4, '', 0, 0, '0', '1', '', ''),

        (12309,  -7, 'E_ALM_PORT_SFP_TXBIAS_HIGH', 'DB.alertType.a12309', 4, '', 0, 0, '0', '1', '', ''),

        (12310,  -7, 'E_ALM_PORT_SFP_TXBIAS_LOW', 'DB.alertType.a12310', 4, '', 0, 0, '0', '1', '', ''),

        (12311,  -7, 'E_ALM_PORT_SFP_VCC_HIGH', 'DB.alertType.a12311', 4, '', 0, 0, '0', '1', '', ''),

        (12312,  -7, 'E_ALM_PORT_SFP_VCC_LOW', 'DB.alertType.a12312', 4, '', 0, 0, '0', '1', '', ''),

        (12313,  -7, 'E_ALM_PORT_SFP_TEMP_HIGH', 'DB.alertType.a12313', 4, '', 0, 0, '0', '1', '', ''),

        (12314,  -7, 'E_ALM_PORT_SFP_TEMP_LOW', 'DB.alertType.a12314', 4, '', 0, 0, '0', '1', '', ''),

        (12315,  -7, 'E_WARN_PORT_SFP_RXPWR_HIGH', 'DB.alertType.a12315', 4, '', 0, 0, '0', '1', '', ''),

        (12316,  -7, 'E_WARN_PORT_SFP_RXPWR_LOW', 'DB.alertType.a12316', 4, '', 0, 0, '0', '1', '', ''),

        (12317,  -7, 'E_WARN_PORT_SFP_TXPWR_HIGH', 'DB.alertType.a12317', 4, '', 0, 0, '0', '1', '', ''),

        (12318,  -7, 'E_WARN_PORT_SFP_TXPWR_LOW', 'DB.alertType.a12318', 4, '', 0, 0, '0', '1', '', ''),

        (12319,  -7, 'E_WARN_PORT_SFP_TXBIAS_HIGH', 'DB.alertType.a12319', 4, '', 0, 0, '0', '1', '', ''),

        (12320,  -7, 'E_WARN_PORT_SFP_TXBIAS_LOW', 'DB.alertType.a12320', 4, '', 0, 0, '0', '1', '', ''),

        (12321,  -7, 'E_WARN_PORT_SFP_VCC_HIGH', 'DB.alertType.a12321', 4, '', 0, 0, '0', '1', '', ''),

        (12322,  -7, 'E_WARN_PORT_SFP_VCC_LOW', 'DB.alertType.a12322', 4, '', 0, 0, '0', '1', '', ''),

      --(12323,  -7, 'E_WARN_PORT_SFP_TEMP_HIGH', 'DB.alertType.a12323', 4, '', 0, 0, '0', '1', '', ''),

      --(12324,  -7, 'E_WARN_PORT_SFP_TEMP_LOW', 'DB.alertType.a12324', 4, '', 0, 0, '0', '1', '', ''),

        (12325,  -7, 'E_ALM_PORT_PON_LOS', 'DB.alertType.a12325', 5, '', 0, 0, '0', '1', '', ''),

        (12326,  -7, 'E_ALM_PORT_PON_LOS_RECOVERY', 'DB.alertType.a12326', 5, '', 0, 0, '0', '1', '', ''),

        (16385,  -7, 'E_ALM_ONU_FATAL_ERROR', 'DB.alertType.a16385', 4, '', 0, 0, '0', '1', '', ''),

        (16386,  -7, 'E_ALM_ONU_KEY_EXCHANGE_ERROR', 'DB.alertType.a16386', 2, '', 0, 0, '0', '1', '', ''),

        (16387,  -7, 'E_ALM_ONU_OAM_TIMEOUT', 'DB.alertType.a16387', 2, '', 0, 0, '0', '1', '', ''),

        (16389,  -7, 'E_ALM_ONU_US_RXPWR_LOW', 'DB.alertType.a16389', 3, '', 0, 0, '0', '1', '', ''),

        (16390,  -7, 'E_ALM_ONU_US_RXPWR_HIGH', 'DB.alertType.a16390', 3, '', 0, 0, '0', '1', '', ''),

        (16391,  -7, 'E_ALM_ONU_DS_RXPWR_LOW', 'DB.alertType.a16391', 3, '', 0, 0, '0', '1', '', ''),

        (16392,  -7, 'E_ALM_ONU_DS_RXPWR_HIGH', 'DB.alertType.a16392', 3, '', 0, 0, '0', '1', '', ''),

        (16394,  -7, 'E_ALM_ONU_DS_BER_ERROR', 'DB.alertType.a16394', 3, '', 0, 0, '0', '1', '', ''),

        (16395,  -7, 'E_ALM_ONU_DS_FER_ERROR', 'DB.alertType.a16395', 3, '', 0, 0, '0', '1', '', ''),

        (16396,  -7, 'E_ALM_ONU_US_BER_ERROR', 'DB.alertType.a16396', 3, '', 0, 0, '0', '1', '', ''),

        (16397,  -7, 'E_ALM_ONU_US_FER_ERROR', 'DB.alertType.a16397', 3, '', 0, 0, '0', '1', '', ''),

        (16417,  -7, 'E_ALM_ONU_EQUMT_ERROR', 'DB.alertType.a16417', 4, '', 0, 0, '0', '1', '', ''),

      --(16418,  -7, 'E_ALM_ONU_POWER_ERROR', 'DB.alertType.a16418', 3, '', 0, 0, '0', '1', '', ''),

      --(16419,  -7, 'E_ALM_ONU_BTRY_MISSING', 'DB.alertType.a16419', 3, '', 0, 0, '0', '1', '', ''),

      --(16420,  -7, 'E_ALM_ONU_BTRY_VCC_LOW', 'DB.alertType.a16420', 3, '', 0, 0, '0', '1', '', ''),

        (16421,  -7, 'E_ALM_ONU_PANNEL_ALARM', 'DB.alertType.a16421', 3, '', 0, 0, '0', '1', '', ''),

        (16422,  -7, 'E_ALM_ONU_SELFTEST_FAIL', 'DB.alertType.a16422', 4, '', 0, 0, '0', '1', '', ''),

        (16423,  -7, 'E_ALM_ONU_TEMP_HIGH', 'DB.alertType.a16423', 3, '', 0, 0, '0', '1', '', ''),

        (16424,  -7, 'E_ALM_ONU_TEMP_LOW', 'DB.alertType.a16424', 3, '', 0, 0, '0', '1', '', ''),

      --(16425,  -7, 'E_ALM_ONU_TEMP_OK', 'DB.alertType.a16425', 3, '', 0, 0, '0', '1', '', ''),

        (16427,  -7, 'E_ALM_ONU_IAD_CONT_FAIL', 'DB.alertType.a16427', 4, '', 0, 0, '0', '1', '', ''),

        (16432,  -7, 'E_ALM_ONU_PROTECT_SWITCH', 'DB.alertType.a16432', 4, '', 0, 0, '0', '1', '', ''),

        (16435,  -7, 'E_ALM_ONU_LOS', 'DB.alertType.a16435', 4, '', 0, 0, '0', '1', '', ''),

        (16437,  -7, 'E_ALM_ONU_ROGUE', 'DB.alertType.a16437', 4, '', 0, 0, '0', '1', '', ''),

        (20487,  -7, 'E_ONU_ETH_LOS', 'DB.alertType.a20487', 3, '', 0, 0, '0', '1', '', ''),

        (20489,  -7, 'E_ONU_ETH_FAIL', 'DB.alertType.a20489', 3, '', 0, 0, '0', '1', '', ''),

        (20491,  -7, 'E_ONU_ETH_CONGEST', 'DB.alertType.a20491', 3, '', 0, 0, '0', '1', '', '');


/*EPON事件代码定义，把设备上报的告警做为事件统一处理*/

INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
        (1,1,'EPON',1),
        (10001,1,'EPON',2),
        (61441,1,'EPON',3),

        (3,3,'EPON',1),
        (100003,3,'EPON',2),
        (61443,3,'EPON',3),

        (5,5,'EPON',1),
        (100005,5,'EPON',2),
        (61445,5,'EPON',3),

        (7,7,'EPON',1),
        (100007,7,'EPON',2),
        (61447,7,'EPON',3),

        (9,9,'EPON',1),
        (100009,9,'EPON',2),
        (61449,9,'EPON',3),

        (11,11,'EPON',1),
        (100011,11,'EPON',2),
        (61451,11,'EPON',3),

        (13,13,'EPON',1),
        (100013,13,'EPON',2),
        (61453,13,'EPON',3),

        (15,15,'EPON',1),
        (100015,15,'EPON',2),
        (61455,15,'EPON',3),

        (260,260,'EPON',1),
        (100260,260,'EPON',2),
        (61700,260,'EPON',3),

        (259,259,'EPON',1),
        (100259,259,'EPON',2),
        (61699,259,'EPON',3),

        (4098,4098,'EPON',1),
        (101,4098,'EPON',2),
        (4098,4098,'EPON',3),

        (4101,4101,'EPON',1),
        (103,4101,'EPON',2),
        (4101,4101,'EPON',3),

        (4102,4102,'EPON',1),
        (104102,4102,'EPON',2),
        (4102,4102,'EPON',3),

        (4103,4103,'EPON',1),
        (104,4103,'EPON',2),
        (4103,4103,'EPON',3),

        (4104,4104,'EPON',1),
        (104104,4104,'EPON',2),
        (4104,4104,'EPON',3),

        (4105,4105,'EPON',1),
        (104105,4105,'EPON',2),
        (4105,4105,'EPON',3),

        (4107,4107,'EPON',1),
        (105,4107,'EPON',2),
        (4107,4107,'EPON',3),

        (4108,4108,'EPON',1),
        (104108,4108,'EPON',2),
        (4108,4108,'EPON',3),

        (4109,4109,'EPON',1),
        (106,4109,'EPON',2),
        (4109,4109,'EPON',3),

        (4110,4110,'EPON',1),
        (104110,4110,'EPON',2),
        (4110,4110,'EPON',3),

        (4111,4111,'EPON',1),
        (107,4111,'EPON',2),
        (4111,4111,'EPON',3),

        (4112,4112,'EPON',1),
        (104112,4112,'EPON',2),
        (4112,4112,'EPON',3),

        (4113,4113,'EPON',1),
        (108,4113,'EPON',2),
        (4113,4113,'EPON',3),

        (4114,4114,'EPON',1),
        (104114,4114,'EPON',2),
        (4114,4114,'EPON',3),

        (4115,4115,'EPON',1),
        (109,4115,'EPON',2),
        (4115,4115,'EPON',3),

        (4117,4117,'EPON',1),
        (104117,4117,'EPON',2),
        (4117,4117,'EPON',3),

        (4119,4119,'EPON',1),
        (104119,4119,'EPON',2),
        (4119,4119,'EPON',3),

        (4121,4121,'EPON',1),
        (104121,4121,'EPON',2),
        (4121,4121,'EPON',3),

        (4123,4123,'EPON',1),
        (104123,4123,'EPON',2),
        (4123,4123,'EPON',3),

        (4124,4124,'EPON',1),
        (104124,4124,'EPON',2),
        (4124,4124,'EPON',3),

        (4125,4125,'EPON',1),
        (104125,4125,'EPON',2),
        (4125,4125,'EPON',3),

        (4127,4127,'EPON',1),
        (104127,4127,'EPON',2),
        (4127,4127,'EPON',3),

        (12289,12289,'EPON',1),
        (201,12289,'EPON',2),
        (12289,12289,'EPON',3),

        (12290,12290,'EPON',1),
        (202,12290,'EPON',2),
        (12290,12290,'EPON',3),

        (12293,12293,'EPON',1),
        (204,12293,'EPON',2),
        (12293,12293,'EPON',3),

        (12295,12295,'EPON',1),
        (205,12295,'EPON',2),
        (12295,12295,'EPON',3),

        (12305,12305,'EPON',1),
        (112305,12305,'EPON',2),
        (257,12305,'EPON',3),

        (12306,12306,'EPON',1),
        (112306,12306,'EPON',2),
        (258,12306,'EPON',3),

        (12307,12307,'EPON',1),
        (112307,12307,'EPON',2),
        (259,12307,'EPON',3),

        (12308,12308,'EPON',1),
        (112308,12308,'EPON',2),
        (260,12308,'EPON',3),

        (12309,12309,'EPON',1),
        (112309,12309,'EPON',2),
        (261,12309,'EPON',3),

        (12310,12310,'EPON',1),
        (112310,12310,'EPON',2),
        (262,12310,'EPON',3),

        (12311,12311,'EPON',1),
        (112311,12311,'EPON',2),
        (263,12311,'EPON',3),

        (12312,12312,'EPON',1),
        (112312,12312,'EPON',2),
        (264,12312,'EPON',3),

        (12313,12313,'EPON',1),
        (112313,12313,'EPON',2),
        (265,12313,'EPON',3),

        (12314,12314,'EPON',1),
        (112314,12314,'EPON',2),
        (266,12314,'EPON',3),

        (12315,12315,'EPON',1),
        (112315,12315,'EPON',2),
        (267,12315,'EPON',3),

        (12316,12316,'EPON',1),
        (112316,12316,'EPON',2),
        (268,12316,'EPON',3),


        (12317,12317,'EPON',1),
        (112317,12317,'EPON',2),
        (269,12317,'EPON',3),

        (12318,12318,'EPON',1),
        (112318,12318,'EPON',2),
        (270,12318,'EPON',3),

        (12319,12319,'EPON',1),
        (112319,12319,'EPON',2),
        (271,12319,'EPON',3),


        (12320,12320,'EPON',1),
        (112320,12320,'EPON',2),
        (272,12320,'EPON',3),


        (12321,12321,'EPON',1),
        (112321,12321,'EPON',2),
        (273,12321,'EPON',3),


        (12322,12322,'EPON',1),
        (112322,12322,'EPON',2),
        (274,12322,'EPON',3),


        (12323,12323,'EPON',1),
        (112323,12323,'EPON',2),
        (275,12323,'EPON',3),

        (12324,12324,'EPON',1),
        (112324,12324,'EPON',2),
        (276,12324,'EPON',3),

        (8193,8193,'EPON',1),
        (2031,8193,'EPON',2),
        (8193,8193,'EPON',3),

        (8194,8194,'EPON',1),
        (2032,8194,'EPON',2),
        (8194,8194,'EPON',3),

        (16385,16385,'EPON',1),
        (301,16385,'EPON',2),
        (16385,16385,'EPON',3),

        (16386,16386,'EPON',1),
        (302,16386,'EPON',2),
        (16386,16386,'EPON',3),

        (16387,16387,'EPON',1),
        (303,16387,'EPON',2),
        (16387,16387,'EPON',3),

        (16388,16388,'EPON',1),
        (304,16388,'EPON',2),
        (16388,16388,'EPON',3),

        (16389,16389,'EPON',1),
        (305,16389,'EPON',2),
        (16389,16389,'EPON',3),

        (16390,16390,'EPON',1),
        (306,16390,'EPON',2),
        (16390,16390,'EPON',3),

        (16391,16391,'EPON',1),
        (307,16391,'EPON',2),
        (16391,16391,'EPON',3),

        (16392,16392,'EPON',1),
        (308,16392,'EPON',2),
        (16392,16392,'EPON',3),

        (16393,16393,'EPON',1),
        (310,16393,'EPON',2),
        (16393,16393,'EPON',3),

        (16394,16394,'EPON',1),
        (311,16394,'EPON',2),
        (16394,16394,'EPON',3),

        (16395,16395,'EPON',1),
        (312,16395,'EPON',2),
        (16395,16395,'EPON',3),

        (16396,16396,'EPON',1),
        (313,16396,'EPON',2),
        (16396,16396,'EPON',3),

        (16397,16397,'EPON',1),
        (314,16397,'EPON',2),
        (16397,16397,'EPON',3),

        (16398,16398,'EPON',1),
        (315,16398,'EPON',2),
        (16398,16398,'EPON',3),

        (16399,16399,'EPON',1),
        (316,16399,'EPON',2),
        (16399,16399,'EPON',3),

        (16417,16417,'EPON',1),
        (116417,16417,'EPON',2),
        (1,16417,'EPON',3),

        (16418,16418,'EPON',1),
        (116418,16418,'EPON',2),
        (3,16418,'EPON',3),

        (16419,16419,'EPON',1),
        (116419,16419,'EPON',2),
        (4,16419,'EPON',3),

        (16420,16420,'EPON',1),
        (116420,16420,'EPON',2),
        (5,16420,'EPON',3),

        (16421,16421,'EPON',1),
        (116421,16421,'EPON',2),
        (6,16421,'EPON',3),

        (16422,16422,'EPON',1),
        (116422,16422,'EPON',2),
        (7,16422,'EPON',3),

        (16423,16423,'EPON',1),
        (116423,16423,'EPON',2),
        (9,16423,'EPON',3),

        (16424,16424,'EPON',1),
        (116424,16424,'EPON',2),
        (10,16424,'EPON',3),

        (16425,16425,'EPON',1),
        (116425,16425,'EPON',2),
        (16425,16425,'EPON',3),

        (16427,16427,'EPON',1),
        (116427,16427,'EPON',2),
        (11,16427,'EPON',3),

        (16432,16432,'EPON',1),
        (116432,16432,'EPON',2),
        (12,16432,'EPON',3),

        (16433,16433,'EPON',1),
        (116433,16433,'EPON',2),
        (16433,16433,'EPON',3),

        (16434,16434,'EPON',1),
        (116434,16434,'EPON',2),
        (16434,16434,'EPON',3),

        (257,257,'EPON',1),
        (13001,257,'EPON',2),
        (61697,257,'EPON',3),

        (258, 258,'EPON',1),
        (13002,258,'EPON',2),
        (61698,258,'EPON',3),

        (261,261,'EPON',1),
        (100261,261,'EPON',2),
        (61701,261,'EPON',3),

        (262,262,'EPON',1),
        (100262,262,'EPON',2),
        (61702,262,'EPON',3),

        (263,263,'EPON',1),
        (100263,263,'EPON',2),
        (61703,263,'EPON',3),

        (20481,20481,'EPON',1),
        (401,20481,'EPON',2),
        (20481,20481,'EPON',3),

        (20483,20483,'EPON',1),
        (402,20483,'EPON',2),
        (772,20483,'EPON',3),

        (20485,20485,'EPON',1),
        (403,20485,'EPON',2),
        (769,20485,'EPON',3),

        (20487,20487,'EPON',1),
        (120487,20487,'EPON',2),
        (770,20487,'EPON',3),

        (20489,20489,'EPON',1),
        (120489,20489,'EPON',2),
        (771,20489,'EPON',3),

        (20491,20491,'EPON',1),
        (120491,20491,'EPON',2),
        (773,20491,'EPON',3),

        (513,513,'EPON',1),
        (14001,513,'EPON',2),
        (513,513,'EPON',3),

        (514,514,'EPON',1),
        (14002,514,'EPON',2),
        (514,514,'EPON',3),

        (1537,1537,'EPON',1),
        (101537,1537,'EPON',2),
        (62977,1537,'EPON',3),

        (515,515,'EPON',1),
        (14003,515,'EPON',2),
        (515,515,'EPON',3),

        (12325,12325,'EPON',1),
        (112325,12325,'EPON',2),
        (12325,12325,'EPON',3),

        (12326,12326,'EPON',1),
        (112326,12326,'EPON',2),
        (12326,12326,'EPON',3),

        (16435,16435,'EPON',1),
        (116435,16435,'EPON',2),
        (16435,16435,'EPON',3),

        (16437,16437,'EPON',1),
        (116437,16437,'EPON',2),
        (16437,16437,'EPON',3);

INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-7,-10000,'EPON Event','DB.eventType.eponEvent','');
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-5,-7,'ONU Event','DB.eventType.onuEvent','');
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
        (1,  -7, 'E_ALM_SYSTEM_RESET', 'DB.eventType.e1'),

        (3,  -7, 'E_ALM_SYSTEM_HA_SWITCH', 'DB.eventType.e3'),

        (5,  -7, 'E_ALM_SYSTEM_BD_UPGRADE', 'DB.eventType.e5'),

        (7,  -7, 'E_ALM_ONU_UPGRADE_OK', 'DB.eventType.e7'),

        (9,  -7, 'E_ALM_FILE_UPLOAD_OK', 'DB.eventType.e9'),

        (11,  -7, 'E_ALM_FILE_DOWNLOAD_OK', 'DB.eventType.e11'),

        (13,  -7, 'E_ALM_SW_SYNC_OK', 'DB.eventType.e13'),

        (15,  -7, 'E_ALM_CONFIG_SYNC_OK', 'DB.eventType.e15'),

        (260,  -7, 'E_ALM_BD_OFFLINE', 'DB.eventType.e260'),

        (259,  -7, 'E_ALM_BD_ONLINE', 'DB.eventType.e259'),

        (4098,  -7, 'E_ALM_BD_PROV_FAIL', 'DB.eventType.e4098'),

        (4099,  -7, 'E_ALM_BD_RESET', 'DB.eventType.e4099'),

        (4101,  -7, 'E_ALM_BD_REMOVE', 'DB.eventType.e4101'),

        (4102,  -7, 'E_ALM_BD_INSERT', 'DB.eventType.e4102'),

        (4103,  -7, 'E_ALM_BD_TEMP_HIGH', 'DB.eventType.e4103'),

        (4104,  -7, 'E_ALM_BD_TEMP_OK', 'DB.eventType.e4104'),

        (4105, -7, 'E_ALM_BD_TEMP_LOW', 'DB.eventType.e4105'),

        (4107,  -7, 'E_ALM_BD_FAN_FAIL', 'DB.eventType.e4107'),

        (4108,  -7, 'E_ALM_BD_FAN_NORMAL', 'DB.eventType.e4108'),

        (4109,  -7, 'E_ALM_BD_FAN_REMOVE', 'DB.eventType.e4109'),

        (4110,  -7, 'E_ALM_BD_FAN_INSERT', 'DB.eventType.e4110'),

        (4111, -7, 'E_ALM_BD_PWR_FAIL', 'DB.eventType.e4111'),

        (4112,  -7, 'E_ALM_BD_PWR_OK', 'DB.eventType.e4112'),

        (4113,  -7, 'E_ALM_BD_PWR_REMOVE', 'DB.eventType.e4113'),

        (4114,  -7, 'E_ALM_BD_PWR_INSERT', 'DB.eventType.e4114'),

        (4115,  -7, 'E_ALM_BD_PONCHIP_ERROR', 'DB.eventType.e4115'),

        (4117,  -7, 'E_ALM_BD_TYPE_MISMATCH', 'DB.eventType.e4117'),

        (4119,  -7, 'E_ALM_BD_SW_MISMATCH', 'DB.eventType.e4119'),

        (4121,  -7, 'E_ALM_BD_SLOT_MISMATCH', 'DB.eventType.e4121'),

        (4123,  -7, 'E_ALM_BD_PERF_EXCEED_HIGH', 'DB.eventType.e4123'),

        (4124,  -7, 'E_ALM_BD_PERF_EXCEED_LOW', 'DB.eventType.e4124'),

        (4125,  -7, 'E_ALM_BD_CPU_OVERLOAD', 'DB.eventType.e4125'),

        (4127,  -7, 'E_ALM_BD_HEARTBEAT_LOST', 'DB.eventType.e4127'),

        (12289,  -7, 'E_ALM_PORT_PON_LOOPBACK', 'DB.eventType.e12289'),

        (12290,  -7, 'E_ALM_PORT_SFP_FAIL', 'DB.eventType.e12290'),

        (12293,  -7, 'E_ALM_PORT_PON_DISABLE', 'DB.eventType.e12293'),

        (12295,  -7, 'E_ALM_PORT_PON_LLID_EXCD', 'DB.eventType.e12295'),

        (12305,  -7, 'E_ALM_PORT_SFP_RXPWR_HIGH', 'DB.eventType.e12305'),

        (12306,  -7, 'E_ALM_PORT_SFP_RXPWR_LOW', 'DB.eventType.e12306'),

        (12307,  -7, 'E_ALM_PORT_SFP_TXPWR_HIGH', 'DB.eventType.e12307'),

        (12308,  -7, 'E_ALM_PORT_SFP_TXPWR_LOW', 'DB.eventType.e12308'),

        (12309,  -7, 'E_ALM_PORT_SFP_TXBIAS_HIGH', 'DB.eventType.e12309'),

        (12310,  -7, 'E_ALM_PORT_SFP_TXBIAS_LOW', 'DB.eventType.e12310'),

        (12311,  -7, 'E_ALM_PORT_SFP_VCC_HIGH', 'DB.eventType.e12311'),

        (12312,  -7, 'E_ALM_PORT_SFP_VCC_LOW', 'DB.eventType.e12312'),

        (12313,  -7, 'E_ALM_PORT_SFP_TEMP_HIGH', 'DB.eventType.e12313'),

        (12314,  -7, 'E_ALM_PORT_SFP_TEMP_LOW', 'DB.eventType.e12314'),

        (12315,  -7, 'E_WARN_PORT_SFP_RXPWR_HIGH', 'DB.eventType.e12315'),

        (12316,  -7, 'E_WARN_PORT_SFP_RXPWR_LOW', 'DB.eventType.e12316'),

        (12317,  -7, 'E_WARN_PORT_SFP_TXPWR_HIGH', 'DB.eventType.e12317'),

        (12318,  -7, 'E_WARN_PORT_SFP_TXPWR_LOW', 'DB.eventType.e12318'),

        (12319,  -7, 'E_WARN_PORT_SFP_TXBIAS_HIGH', 'DB.eventType.e12319'),

        (12320,  -7, 'E_WARN_PORT_SFP_TXBIAS_LOW', 'DB.eventType.e12320'),

        (12321,  -7, 'E_WARN_PORT_SFP_VCC_HIGH', 'DB.eventType.e12321'),

        (12322,  -7, 'E_WARN_PORT_SFP_VCC_LOW', 'DB.eventType.e12322'),

        (12323,  -7, 'E_WARN_PORT_SFP_TEMP_HIGH', 'DB.eventType.e12323'),

        (12324,  -7, 'E_WARN_PORT_SFP_TEMP_LOW', 'DB.eventType.e12324'),

        (12325,  -7, 'E_ALM_PORT_PON_LOS', 'DB.eventType.e12325'),

        (12326,  -7, 'E_ALM_PORT_PON_LOS_RECOVERY', 'DB.eventType.e12326'),

        (8193,  -7, 'E_ALM_PORT_LINK_DOWN', 'DB.eventType.e8193'),

        (8194, -7, 'E_ALM_PORT_LINK_UP', 'DB.eventType.e8194'),

        (16385,  -5, 'E_ALM_ONU_FATAL_ERROR', 'DB.eventType.e16385'),

        (16386,  -5, 'E_ALM_ONU_KEY_EXCHANGE_ERROR', 'DB.eventType.e16386'),

        (16387,  -5, 'E_ALM_ONU_OAM_TIMEOUT', 'DB.eventType.e16387'),

        (16388,  -5, 'E_ALM_ONU_MAC_AUTH_ERROR', 'DB.eventType.e16388'),

        (16389,  -5, 'E_ALM_ONU_US_RXPWR_LOW', 'DB.eventType.e16389'),

        (16390,  -5, 'E_ALM_ONU_US_RXPWR_HIGH', 'DB.eventType.e16390'),

        (16391,  -5, 'E_ALM_ONU_DS_RXPWR_LOW', 'DB.eventType.e16391'),

        (16392,  -5, 'E_ALM_ONU_DS_RXPWR_HIGH', 'DB.eventType.e16392'),

        (16393,  -5, 'E_ALM_ONU_PWR_DOWN', 'DB.eventType.e16393'),

        (16394,  -5, 'E_ALM_ONU_DS_BER_ERROR', 'DB.eventType.e16394'),

        (16395,  -5, 'E_ALM_ONU_DS_FER_ERROR', 'DB.eventType.e16395'),

        (16396,  -5, 'E_ALM_ONU_US_BER_ERROR', 'DB.eventType.e16396'),

        (16397,  -5, 'E_ALM_ONU_US_FER_ERROR', 'DB.eventType.e16397'),

        (16398,  -5, 'E_ALM_ONU_PERF_EXCEED_HIGH', 'DB.eventType.e16398'),

        (16399,  -5, 'E_ALM_ONU_PERF_EXCEED_LOW', 'DB.eventType.e16399'),

        (16417,  -5, 'E_ALM_ONU_EQUMT_ERROR', 'DB.eventType.e16417'),

        (16418,  -5, 'E_ALM_ONU_POWER_ERROR', 'DB.eventType.e16418'),

        (16419,  -5, 'E_ALM_ONU_BTRY_MISSING', 'DB.eventType.e16419'),

        (16420,  -5, 'E_ALM_ONU_BTRY_VCC_LOW', 'DB.eventType.e16420'),

        (16421,  -5, 'E_ALM_ONU_PANNEL_ALARM', 'DB.eventType.e16421'),

        (16422,  -5, 'E_ALM_ONU_SELFTEST_FAIL', 'DB.eventType.e16422'),

        (16423,  -5, 'E_ALM_ONU_TEMP_HIGH', 'DB.eventType.e16423'),

        (16424,  -5, 'E_ALM_ONU_TEMP_LOW', 'DB.eventType.e16424'),

        (16425,  -5, 'E_ALM_ONU_TEMP_OK', 'DB.eventType.e16425'),

        (16427,  -5, 'E_ALM_ONU_IAD_CONT_FAIL', 'DB.eventType.e16427'),

        (16432,  -5, 'E_ALM_ONU_PROTECT_SWITCH', 'DB.eventType.e16432'),

        (16433,  -5, 'E_ALM_ONU_OFFLINE', 'DB.eventType.e16433'),

        (16434,  -5, 'E_ALM_ONU_ONLINE', 'DB.eventType.e16434'),

        (16435,  -5, 'E_ALM_ONU_LOS', 'DB.eventType.e16435'),

        (16437,  -5, 'E_ALM_ONU_ROGUE', 'DB.eventType.e16437'),

        (257,  -5, 'E_ALM_ONU_REGISTER', 'DB.eventType.e257'),

        (258,  -5, 'E_ALM_ONU_DEREGISTER', 'DB.eventType.e258'),

        (261,  -5, 'E_ALM_ONU_AUTO_UPGRADE', 'DB.eventType.e261'),

        (262,  -5, 'E_ALM_ONU_ILLEGALREGISTER', 'DB.eventType.e262'),

        (263,  -5, 'E_ALM_ONU_LOIDCONFLICT', 'DB.eventType.e263'),

        (20481,  -5, 'E_ALM_ONU_UNI_LINKDOWN', 'UNI LINK DOWN'),

        (20483,  -5, 'E_ALM_ONU_UNI_LOOP', 'DB.eventType.e20483'),

        (20485,  -5, 'E_ALM_ONU_UNI_AUTONEG_FAIL', 'DB.eventType.e20485'),

        (20487,  -5, 'E_ONU_ETH_LOS', 'DB.eventType.e20487'),

        (20489,  -5, 'E_ONU_ETH_FAIL', 'DB.eventType.e20489'),

        (20491,  -5, 'E_ONU_ETH_CONGEST', 'DB.eventType.e20491'),

        (513,  -5, 'E_ALM_ONU_UNI_NEW_CBAT_RECOGNIZED', 'DB.eventType.e513'),

        (514,  -5, 'E_ALM_ONU_UNI_CBAT_UPDATE', 'DB.eventType.e514'),

        (1537,  -7, 'E_ALM_IGMP_CDR_REPORT', 'DB.eventType.e1537'),

        (515,  -5, 'E_ALM_ONU_UNI_CBAT_REMOVED', 'DB.eventType.e515');

INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES

                (1,1,1),

                (4101,4101,1),
                (4102,4101,0),

                (4109,4109,1),
                (4110,4109,0),

                (4113,4113,1),
                (4114,4113,0),

                (16433, 16433,1),
                (16434, 16433,0),

                (12325,12325,1),
                (12326,12325,0),

                (8193,-603,1),
                (8194,-603,0),

                (16388,16388,1),

                (4098,4098,1),

                (16435,16435,1),

                (16437,16437,1),

                (4103,-604,1),
                (4104,-604,0),
                (4105,-604,1),

                (4107,-602,1),
                (4108,-602,0),

                (4111,-601,1),
                (4112,-601,0),

                (4115,4115,1),

                (4117,4117,1),

                (4119,4119,1),

                (4121,4121,1),

                (4123,-205,1),

                (4124,-205,1),

                (16398,-205,1),

                (16399,-205,1),

                (20483,20483,1),

                (20485,20485,1),

                (16393,16393,1),
                (16434,16393,0),

                (4099, 4099,1),

                (4125, 4125,1),

                (4127, 4127,1),

                (12289, 12289,1),

                (12290, 12290,1),

                (12293, 12293,1),

                (12295, 12295,1),

                (12305, 12305,1),

                (12306, 12306,1),

                (12307, 12307,1),

                (12308, 12308,1),

                (12309, 12309,1),

                (12310, 12310,1),

                (12311, 12311,1),

                (12312, 12312,1),

                (12313, 12313,1),

                (12314, 12314,1),

                (12315, 12315,1),

                (12316, 12316,1),

                (12317, 12317,1),

                (12318, 12318,1),

                (12319, 12319,1),

                (12320, 12320,1),

                (12321, 12321,1),

                (12322, 12322,1),

                (12323, -604,1),

                (12324, -604,1),

                (16385, 16385,1),

                (16386, 16386,1),

                (16387, 16387,1),

                (16389, 16389,1),

                (16390, 16390,1),

                (16391, 16391,1),

                (16392, 16392,1),

                (16394, 16394,1),

                (16395, 16395,1),

                (16396, 16396,1),

                (16397, 16397,1),

                (16417, 16417,1),

                (16418, -601,1),

                (16419, -601,1),

                (16420, -601,1),

                (16421, 16421,1),

                (16422, 16422,1),

                (16423, -604,1),

                (16424, -604,1),

                (16425, -604,0),

                (16427, 16427,1),

                (16432, 16432,1),

                (20487, 20487,1),

                (20489, 20489,1),

                (20491, 20491,1);

/* -- version 1.0.0,build 2011-12-22,module epon */

-- version 1.7.3,build 2012-8-8,module epon
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (12001,  'onu', 'ONU', 'epon', '/epon',  '1.3.6.1.4.1.32285.11.2.4', 'network/onu_16.gif', 'network/onu_32.gif', 'network/onu_48.gif', 'network/onu_64.gif', 1);

/* -- version 1.7.3,build 2012-8-8,module epon */

-- version 1.7.7.3,build 2012-11-14,module epon
UPDATE EntityType SET module = 'onu' WHERE typeId = 12001;
UPDATE EntityType SET module = 'onu' WHERE typeId = 33;
UPDATE EntityType SET module = 'onu' WHERE typeId = 34;
UPDATE EntityType SET module = 'onu' WHERE typeId = 36;
UPDATE EntityType SET module = 'onu' WHERE typeId = 37;
UPDATE EntityType SET module = 'onu' WHERE typeId = 65;
UPDATE EntityType SET module = 'onu' WHERE typeId = 68;
UPDATE EntityType SET module = 'onu' WHERE typeId = 71;
UPDATE EntityType SET module = 'onu' WHERE typeId = 255;
/* -- version 1.7.7.3,build 2012-11-14,module epon */

-- version 1.7.7.4,build 2012-11-21,module epon
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (10003,  'pn8603', 'PN8603', 'epon', '/epon/8603',  '1.3.6.1.4.1.32285.11.2.1.3', 'network/8603_16.gif', 'network/8603_32.gif', 'network/8603_48.gif', 'network/network/8603_64.gif', 1);

INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
        (12327,  -7, 'E_ALM_PORT_ATTACK_BLACKLIST', 'DB.eventType.e12327');
INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
        (12327,  -7, 'E_ALM_PORT_ATTACK_BLACKLIST', 'DB.alertType.a12327', 5, '', 0, 0, '0', '1', '', '');
INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
        (12327,12327,'EPON',1),
        (112327,12327,'EPON',2),
        (12327,12327,'EPON',3);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (12327,12327,1);

-- Add Board OnLine Alert
--INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
--      (259,  -7, 'E_ALM_BD_ONLINE', 'DB.alertType.a259', 5, '', 0, 0, '0', '1', '', '');
--INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (259,259,1);
/* -- version 1.7.7.4,build 2012-11-21,module epon */

-- version 1.7.8.0,build 2012-12-27,module epon
update entitytype set typeId = 68 , module = 'onu',name='pn8644',displayName='pn8644' where typeId = 67;
update entitytype set typeId = 71 , module = 'onu',name='pn8647',displayName='pn8647' where typeId = 69;
/* -- version 1.7.8.0,build 2012-12-27,module epon */

-- version 1.7.8.0,build 2013-1-4,module epon
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
        (12328,  -7, 'E_ALM_PORT_ATTACK_BLACKLIST_CLEAR', 'DB.eventType.e12328');
INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
        (12328,12328,'EPON',1),
        (112328,12328,'EPON',2),
        (12328,12328,'EPON',3);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (12328,12327,0);
/* -- version 1.7.8.0,build 2013-1-4,module epon */

-- version 1.7.8.0,build 2013-3-14,module epon
DELETE FROM EventType WHERE typeId=16397;
DELETE FROM EventType WHERE typeId=16398;
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES  (16397,  -5, 'E_ALM_ONU_US_FER_ERROR', 'DB.eventType.e16397'),
                                                                (16398,  -5, 'E_ALM_ONU_PERF_EXCEED_HIGH', 'DB.eventType.e16398');
/* -- version 1.7.8.0,build 2013-3-14,module epon */

/* modify by victor@2013-4-8把ONU的类型ID改为13000*/
-- version 1.7.12.0,build 2013-4-8,module epon
update EntityType set typeId = 13000 where typeId = 12001;
--update EntityType set corpId = 32285 where type = 10000 or type = 13000; 设备类型重构后去掉type字段
/* -- version 1.7.12.0,build 2013-4-8,module epon */

-- version 1.7.13.3,build 2013-5-31,module epon */
update ReportTemplate set url = '/epon/report/querySniFlowStastic.tv' where templateId = 700043;
update ReportTemplate set url = '/epon/report/queryPonFlowStastic.tv' where templateId = 700044;
/* -- version 1.7.13.3,build 2013-5-31,module epon */

-- version 1.7.13.3,build 2013-6-7,module epon */
DELETE from ReportTemplate where templateId like '7%';
/* -- version 1.7.13.3,build 2013-6-7,module epon */

-- version 1.7.14.0,build 2013-06-17,module epon
INSERT INTO perfthresholdtemplate(templateId,templateName, templateType, createUser,isDefaultTemplate, createTime)
                values(1,'OLT_DEFAULT_TEMPLATE', 10000,'system', 1, sysdate());
INSERT INTO perfglobal(flag,isPerfOn,isRelationWithDefaultTemplate,defaultTemplateId,isPerfThreshold,defaultCollectTime) values ('OLT', 1,1,1,1, 15);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_SNI_IN_SPEED', 10000, 'Performance.sniInSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_SNI_OUT_SPEED', 10000, 'Performance.sniOutSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PON_IN_SPEED', 10000, 'Performance.ponInSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PON_OUT_SPEED', 10000, 'Performance.ponOutSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PON_USED', 10000, 'Performance.ponUsed', 'Performance.speed','%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_SNI_USED', 10000, 'Performance.sniUsed', 'Performance.speed','%');

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_ONUPON_OUT_SPEED', 10000, 'Performance.onuPonOutSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_ONUPON_IN_SPEED', 10000, 'Performance.onuPonInSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_UNI_OUT_SPEED', 10000, 'Performance.uniOutSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_UNI_IN_SPEED', 10000, 'Performance.uniInSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_ONUPON_USED', 10000, 'Performance.onuPonUsed', 'Performance.speed','%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_UNI_USED', 10000, 'Performance.uniUsed', 'Performance.speed','%');

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_CPU_USED', 10000, 'Performance.cpuUsed', 'Performance.serviceQuality', '%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_FLASH_USED', 10000, 'Performance.flashUsed', 'Performance.serviceQuality', '%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_MEM_USED', 10000, 'Performance.memUsed', 'Performance.serviceQuality', '%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_FAN_SPEED', 10000, 'Performance.fanSpeed', 'Performance.serviceQuality', 'rps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_BOARD_TEMP', 10000, 'Performance.boardTemp', 'Performance.serviceQuality', '℃');

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PORT_RE_POWER', 10000, 'Performance.optRePower','Performance.optLink','dBm');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PORT_TX_POWER', 10000, 'Performance.optRtPower','Performance.optLink','dBm');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PORT_TEMP', 10000, 'Performance.optTemp','Performance.optLink','℃');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PORT_CURRENT', 10000, 'Performance.optCurrent','Performance.optLink','mA');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PORT_VOLTAGE', 10000, 'Performance.optVoltage','Performance.optLink','V');

INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('OLT_SNI_IN_SPEED', 1,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_SNI_OUT_SPEED', 1,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_PON_IN_SPEED', 1,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_PON_OUT_SPEED', 1,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_SNI_USED', 1,'1_80_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_PON_USED', 1,'1_80_5',1,1,1,'00:00-23:59#1234567'),

	('OLT_ONUPON_OUT_SPEED', 1,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_ONUPON_IN_SPEED', 1,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_UNI_OUT_SPEED', 1,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_UNI_IN_SPEED', 1,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_ONUPON_USED', 1,'1_80_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_UNI_USED', 1,'1_80_5',1,1,1,'00:00-23:59#1234567'),

	('OLT_CPU_USED', 1,'1_60_3',1,1,1,'00:00-23:59#1234567'),
	('OLT_FLASH_USED', 1,'1_85_3',1,1,1,'0:00-23:59#1234567'),
	('OLT_MEM_USED', 1,'1_80_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_FAN_SPEED', 1,'1_150_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_BOARD_TEMP', 1,'1_60_4',1,1,1,'00:00-23:59#1234567'),

	('OLT_PORT_RE_POWER', 1,'1_40_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_PORT_TX_POWER', 1,'1_40_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_PORT_TEMP', 1,'1_60_4',1,1,1,'0:00-23:59#1234567'),
	('OLT_PORT_CURRENT', 1,'1_100_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_PORT_VOLTAGE', 1,'1_12_4',1,1,1,'00:00-23:59#1234567');

INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-50001,-50000,'EPON Event','EVENT.eponThreshold','');
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-710,-50001,'OLTSniInSpeedEvent','DB.eventType.e710',''),
                (-711,-50001,'OLTSniInSpeedEvent','DB.eventType.e711',''),
                (-712,-50001,'OLTSniOutSpeedEvent','DB.eventType.e712',''),
                (-713,-50001,'OLTSniOutSpeedEvent','DB.eventType.e713',''),
                (-714,-50001,'OLTPonInSpeedEvent','DB.eventType.e714',''),
                (-715,-50001,'OLTPonInSpeedEvent','DB.eventType.e715',''),
                (-716,-50001,'OLTPonOutSpeedEvent','DB.eventType.e716',''),
                (-717,-50001,'OLTPonOutSpeedEvent','DB.eventType.e717',''),
                (-718,-50001,'OLTOnuPonInSpeedEvent','DB.eventType.e718',''),
                (-719,-50001,'OLTOnuPonInSpeedEvent','DB.eventType.e719',''),
                (-720,-50001,'OLTOnuPonOutSpeedEvent','DB.eventType.e720',''),
                (-721,-50001,'OLTOnuPonOutSpeedEvent','DB.eventType.e721',''), 
                (-722,-50001,'OLTUniInSpeedEvent','DB.eventType.e722',''),
                (-723,-50001,'OLTUniInSpeedEvent','DB.eventType.e723',''),
                (-724,-50001,'OLTUniOutSpeedEvent','DB.eventType.e724',''),
                (-725,-50001,'OLTUniOutSpeedEvent','DB.eventType.e725',''),
                
                (-730,-50001,'OLTOptCurrentEvent','DB.eventType.e730',''),
                (-731,-50001,'OLTOptCurrentEvent','DB.eventType.e731',''),
                (-732,-50001,'OLTOptRePowerEvent','DB.eventType.e732',''),
                (-733,-50001,'OLTOptRePowerEvent','DB.eventType.e733',''),
                (-734,-50001,'OLTOptTempEvent','DB.eventType.e734',''),
                (-735,-50001,'OLTOptTempEvent','DB.eventType.e735',''),
                (-736,-50001,'OLTOptTxPowerEvent','DB.eventType.e736',''),
                (-737,-50001,'OLTOptTxPowerEvent','DB.eventType.e737',''),
                (-738,-50001,'OLTOptVolEvent','DB.eventType.e738',''),
                (-739,-50001,'OLTOptVolEvent','DB.eventType.e739',''),
                
                (-751,-50001,'OLTSlotFlashEvent','DB.eventType.e751',''),
                (-752,-50001,'OLTSlotFlashEvent','DB.eventType.e752',''),    
                (-753,-50001,'OLTSlotMemEvent','DB.eventType.e753',''),
                (-754,-50001,'OLTSlotMemEvent','DB.eventType.e754',''),
                (-755,-50001,'OLTSlotTempEvent','DB.eventType.e755',''),
                (-756,-50001,'OLTSlotTempEvent','DB.eventType.e756',''),
                (-757,-50001,'OLTSlotCpuEvent','DB.eventType.e757',''),
                (-758,-50001,'OLTSlotCpuEvent','DB.eventType.e758',''),
                (-761,-50001,'OLTFanEvent','DB.eventType.e761',''),
                (-762,-50001,'OLTFanEvent','DB.eventType.e762','');
                
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-50001,-50000,'Threshold Alert','ALERT.eponThreshold',0,'',0,0,'0','1',NULL,NULL);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
                (-710,-50001,'OLTSniReceiveSpeed','DB.alertType.a710',5,'',0,  0,  '0',  '1',  '',  ''),
                (-712,-50001,'OLTSniSendSpeed','DB.alertType.a712',5,'',0,  0,  '0',  '1',  '',  ''),
                (-714,-50001,'OLTPonReceiveSpeed','DB.alertType.a714',5,'',0,  0,  '0',  '1',  '',  ''),
                (-716,-50001,'OLTPonSendSpeed','DB.alertType.a716',5,'',0,  0,  '0',  '1',  '',  ''),
                (-718,-50001,'OLTOnuPonInSpeedEvent','DB.alertType.a718',5,'',0,  0,  '0',  '1',  '',  ''),
                (-720,-50001,'OLTOnuPonOutSpeedEvent','DB.alertType.a720',5,'',0,  0,  '0',  '1',  '',  ''),
                (-722,-50001,'OLTUniInSpeedEvent','DB.alertType.a722',5,'',0,  0,  '0',  '1',  '',  ''),
                (-724,-50001,'OLTUniOutSpeedEvent','DB.alertType.a724',5,'',0,  0,  '0',  '1',  '',  ''),
               
                (-730,-50001,'OLTOptCurrentEvent','DB.alertType.a730',5,'',0,  0,  '0',  '1',  '',  ''),
                (-732,-50001,'OLTOptRePowerEvent','DB.alertType.a732',5,'',0,  0,  '0',  '1',  '',  ''),
                (-734,-50001,'OLTOptTempEvent','DB.alertType.a734',5,'',0,  0,  '0',  '1',  '',  ''),
                (-736,-50001,'OLTOptTxPowerEvent','DB.alertType.a736',5,'',0,  0,  '0',  '1',  '',  ''),
                (-738,-50001,'OLTOptVolEvent','DB.alertType.a738',5,'',0,  0,  '0',  '1',  '',  ''),
               
                (-751,-50001,'OLTSlotFlashEvent','DB.alertType.a751',5,'',0,  0,  '0',  '1',  '',  ''),
                (-753,-50001,'OLTSlotMemEvent','DB.alertType.a753',5,'',0,  0,  '0',  '1',  '',  ''),
                (-755,-50001,'OLTSlotTempEvent','DB.alertType.a755',5,'',0,  0,  '0',  '1',  '',  ''), 
                (-757,-50001,'OLTSlotCpuEvent','DB.alertType.a757',5,'',0,  0,  '0',  '1',  '',  ''),  
                (-761,-50001,'OLTFanEvent','DB.alertType.a761',5,'',0,  0,  '0',  '1',  '',  '');
                 
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
                (-710,-710,1),
                (-711,-710,0),
                (-712,-712,1),
                (-713,-712,0),
                (-714,-714,1),
                (-715,-714,0),
                (-716,-716,1),
                (-717,-716,0),
                (-718,-718,1),
                (-719,-718,0),
                (-720,-720,1),
                (-721,-720,0),
                (-722,-722,1),
                (-723,-722,0),
                (-724,-724,1),
                (-725,-724,0),
                
                (-730,-730,1),
                (-731,-730,0),
                (-732,-732,1),
                (-733,-732,0),
                (-734,-734,1),
                (-735,-734,0),
                (-736,-736,1),
                (-737,-736,0),
                (-738,-738,1),
                (-739,-738,0),
                
                (-751,-751,1),
                (-752,-751,0),
                (-753,-753,1),
                (-754,-753,0),
                (-755,-755,1),
                (-756,-755,0),
                (-757,-757,1),
                (-758,-757,0),
                (-761,-761,1),
                (-762,-761,0);
                
                
/* -- version 1.7.14.0,build 2013-06-17,module epon */

-- version 1.7.14.0,build 2013-07-20,module epon
INSERT INTO GlobalCollectTimeTable (perftargetName,type,interval_,enable,targetGroup) VALUES
				('sniFlow',1,15,1,'flow'),
				('ponFlow',1,15,1,'flow'),
				('onuPonFlow',1,15,1,'flow'),
				('uniFlow',1,15,1,'flow'),
				
				('cpuUsed',1,15,1,'service'),
				('memUsed',1,15,1,'service'),
				('flashUsed',1,15,1,'service'),
				('boardTemp',1,15,1,'service'),
				('fanSpeed',1,15,1,'service'),
				
				('optLink',1,15,1,'service');
/* -- version 1.7.14.0,build 2013-07-20,module epon */

-- version 1.7.14.0,build 2013-07-31,module epon
--call initFlowSummary();
/*-- version 1.7.14.0,build 2013-07-31,module epon*/

-- version 1.7.14.0,build 2013-08-24,module epon
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES (17000,  -7, 'E_ALM_ONU_MAC_SUCCESS', 'DB.eventType.e17000');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (17000,16388,0);
/* -- version 1.7.14.0,build 2013-08-24,module epon */     

-- version 1.7.14.0,build 2013-08-27,module epon
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (259,4117,0);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (259,4119,0);
/* -- version 1.7.14.0,build 2013-08-27,module epon */

-- version 2.0.0.1,build 2013-10-22,module epon
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000004,'oltPerformanConfig',5000000,'userPower.oltPerformanConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000004);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000005,'oltGlobalConfig',5000000,'userPower.oltGlobalConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000005);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000006,'oltPerfMgmt',5000000,'userPower.oltPerfMgmt');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000006);
/* -- version 2.0.0.1,build 2013-10-22,module epon */

-- version 2.0.0.1,build 2013-10-30,module epon
UPDATE reporttemplate SET url = '/epon/report/showEponDeviceAsset.tv' WHERE templateId = 10001;
UPDATE reporttemplate SET url = '/epon/report/showEponBoardReport.tv' WHERE templateId = 10002;
UPDATE reporttemplate SET url = '/epon/report/showEponSniPortReport.tv' WHERE templateId = 10003;
UPDATE reporttemplate SET url = '/epon/report/showEponPonPortReport.tv' WHERE templateId = 10004;
UPDATE reporttemplate SET url = '/epon/report/showOnuDeviceAsset.tv' WHERE templateId = 10005;
UPDATE reporttemplate SET url = '/epon/report/showEponCpuRankReport.tv' WHERE templateId = 20001;
UPDATE reporttemplate SET url = '/epon/report/showEponMemRankReport.tv' WHERE templateId = 20002;
UPDATE reporttemplate SET url = '/epon/report/querySniFlowStastic.tv' WHERE templateId = 20003;
UPDATE reporttemplate SET url = '/epon/report/queryPonFlowStastic.tv' WHERE templateId = 20004;
UPDATE reporttemplate SET url = '/epon/report/showEponDelayRankReport.tv' WHERE templateId = 20005;
UPDATE reporttemplate SET url = '/epon/report/showCurAlertReport.tv' WHERE templateId = 30001;
UPDATE reporttemplate SET url = '/epon/report/showHistoryAlarmReport.tv' WHERE templateId = 30002;
/* -- version 2.0.0.1,build 2013-10-30,module epon */

-- version 2.0.0.1,build 2013-10-31,module epon
UPDATE portletitem SET url = '/epon/perf/getTopSniLoading.tv' WHERE itemId = 416;
UPDATE portletitem SET url = '/epon/perf/getTopPonLoading.tv' WHERE itemId = 417;
/*-- version 2.0.0.1,build 2013-10-31,module epon*/

-- version 2.0.0.1,build 2013-10-31,module epon
update entitytype set displayName='PN8644' where typeId = 68;
update entitytype set displayName='PN8647' where typeId = 71;
/* -- version 2.0.0.1,build 2013-10-31,module epon */

-- version 2.0.0.1,build 2013-11-13,module epon
update eventtype set parentId = -7 where parentId = -5;
delete from eventtype where typeId = -5 ;
/* -- version 2.0.0.1,build 2013-11-13,module epon */

-- version 2.0.0.1,build 2013-11-20,module epon
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10001, 10000, 'oltDeviceListReportCreator', 'report.oltDeviceList', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/epon/report/showEponDeviceAsset.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10002, 10000, 'oltBoardReportCreator', 'report.oltBoardReport', NULL, 'icoG9', 'device_48.gif', 'board', NULL, '/epon/report/showEponBoardReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10003, 10000, 'oltSniPortReportCreator', 'report.oltSniPortReport', NULL, 'icoG7', 'device_48.gif', 'port', NULL, '/epon/report/showEponSniPortReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10004, 10000, 'oltPonPortReportCreator', 'report.oltPonPortReport', NULL, 'icoG7', 'device_48.gif', 'port', NULL, '/epon/report/showEponPonPortReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10005, 10000, 'onuDeviceListReportCreator', 'report.onuDeviceListReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/epon/report/showOnuDeviceAsset.tv', '1');

INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20001, 20000, 'oltCpuReportCreator', 'report.oltCpuReport', NULL, 'icoG5', 'device_48.gif', 'perf', NULL, '/epon/report/showEponCpuRankReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20002, 20000, 'oltMemReportCreator', 'report.oltMemReport', NULL, 'icoG6', 'device_48.gif', 'perf', NULL, '/epon/report/showEponMemRankReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20003, 20000, 'oltSniPortFlowReportCreator', 'report.oltSniPortFlowReport', NULL, 'icoG1', 'device_48.gif', 'perf', NULL, '/epon/report/querySniFlowStastic.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20004, 20000, 'oltPonPortFlowReportCreator', 'report.oltPonPortFlowReport', NULL, 'icoG1', 'device_48.gif', 'perf', NULL, '/epon/report/queryPonFlowStastic.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20005, 20000, 'oltResponseReportCreator', 'report.oltResponseReport', NULL, 'icoG8', 'device_48.gif', 'perf', NULL, '/epon/report/showEponDelayRankReport.tv', '1');

INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (30001, 30000, 'currentAlarmReportCreator', 'report.currentAlarmReport', NULL, 'icoG3', 'device_48.gif', 'alert', NULL, '/epon/report/showCurAlertReport.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (30002, 30000, 'historyAlarmReportCreator', 'report.historyAlarmReport', NULL, 'icoG4', 'device_48.gif', 'alert', NULL, '/epon/report/showHistoryAlarmReport.tv', '1');
/* -- version 2.0.0.1,build 2013-11-20,module epon */

-- version 2.0.0.1,build 2013-11-26,module epon
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
        (4130,  -7, 'E_ALM_BD_MEM_USAGE_HIGH', 'DB.eventType.e4130'),
        (4131,  -7, 'E_ALM_BD_MEM_USAGE_HIGH_OK', 'DB.eventType.e4131'),
        (12297,  -7, 'E_ALM_OPTICAL_MODULE_REMOVE', 'DB.eventType.e12297'),
        (12298,  -7, 'E_ALM_OPTICAL_MODULE_INSERT', 'DB.eventType.e12298'),
        (266,  -7, 'E_ALM_ONU_MAC_CONFLICT', 'DB.eventType.e266'),
        (16431,  -7, 'E_ALM_ONU_PROTECT_SWITCH_OK', 'DB.eventType.e16431'),
        (4118,  -7, 'E_ALM_BD_TYPE_MISMATCH_OK', 'DB.eventType.e4118'),
        (4120,  -7, 'E_ALM_BD_SW_MISMATCH_OK', 'DB.eventType.e4120'),
        (4122,  -7, 'E_ALM_BD_SLOT_MISMATCH_OK', 'DB.eventType.e4122'),
        (4126,  -7, 'E_ALM_BD_CPU_OVERLOAD_OK', 'DB.eventType.e4126'),
        (12294,  -7, 'E_ALM_PORT_PON_DISABLE_OK', 'DB.eventType.e12294'),
        (12304,  -7, 'E_ALM_PORT_SFP_RXPWR_OK', 'DB.eventType.e12304'),
        (12303,  -7, 'E_ALM_PORT_SFP_TXPWR_OK', 'DB.eventType.e12303'),
        (16300,  -7, 'E_ALM_ONU_PWR_DOWN_OK', 'DB.eventType.e16300'),
        (20484,  -7, 'E_ALM_ONU_UNI_LOOP_OK', 'DB.eventType.e20484'),
        (20486,  -7, 'E_ALM_ONU_UNI_AUTONEG_FAIL_OK', 'DB.eventType.e20486'),
        (20482,  -7, 'E_ALM_ONU_UNI_LINKDOWN_OK', 'DB.eventType.e20482');
INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
        (4130,4130,'EPON',1),
        (104130,4130,'EPON',2),
        (4130,4130,'EPON',3),
        (12297,12297,'EPON',1),
        (207,12297,'EPON',2),
        (12297,12297,'EPON',3),
        (12298,12298,'EPON',1),
        (209,12298,'EPON',2),
        (12298,12298,'EPON',3),
        (266,266,'EPON',1),
        (100266,266,'EPON',2),
        (61706,266,'EPON',3);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
        (4130,4130,1),
        (4131,4130,0),
        (12297,12297,1),
        (12298,12297,0),
        (266,266,1),
        (16431,16432,0),
        (4118,4117,0),
        (4120,4119,0),
        (4122,4121,0),
        (4126,4125,0),
        (12294,12293,0),
        (12304,12305,0),
        (12304,12306,0),
        (12303,12307,0),
        (12303,12308,0),
        (16300,16393,0),
        (20484,20483,0),
        (20486,20485,0),
        (20482,20481,0);
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
        (4130,  -7, 'E_ALM_BD_MEM_USAGE_HIGH', 'DB.alertType.a4130', 5, '', 0, 0, '0', '1', '', ''),
        (12297,  -7, 'E_ALM_OPTICAL_MODULE_REMOVE', 'DB.alertType.a12297', 5, '', 0, 0, '0', '1', '', ''),
        (266,  -7, 'E_ALM_ONU_MAC_CONFLICT', 'DB.alertType.a266', 5, '', 0, 0, '0', '1', '', '');
/* -- version 2.0.0.1,build 2013-11-26,module epon */

-- version 2.0.3.0,build 2013-12-21,module epon
update EponEventTypeRelation set deviceEventTypeId = 112297 where deviceEventTypeId = 207 and emsEventTypeId = 12297;
update EponEventTypeRelation set deviceEventTypeId = 112298 where deviceEventTypeId = 209 and emsEventTypeId = 12298;
/* -- version 2.0.3.0,build 2013-12-21,module epon */

-- version 2.0.3.0,build 2014-1-6,module epon
UPDATE perfthresholdrule SET thresholds = '1_75_3' WHERE targetId = 'OLT_CPU_USED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '5_25_4' WHERE targetId = 'OLT_FAN_SPEED' and templateId = 1;
/* -- version 2.0.3.0,build 2014-1-6,module epon */

-- version 2.0.3.0,build 2014-1-15,module epon
UPDATE eponeventtyperelation SET deviceEventTypeId = 10001 WHERE emsEventTypeId = 1 and type = 2;

INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
        (20481,  -7, 'E_ALM_ONU_UNI_LINKDOWN', 'DB.alertType.a20481', 2, '', 0, 0, '0', '1', '', '');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (20481,20481,1);

INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
        (4099,4099,'EPON',1),
        (102,4099,'EPON',2),
        (4099,4099,'EPON',3),
        
        (2,2,'EPON',1),
        (100002,2,'EPON',2),
        (61442,2,'EPON',3);
        
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
        (2,  -7, 'E_ALM_SYSTEM_START', 'DB.eventType.e2');        
/* -- version 2.0.3.0,build 2014-1-15,module epon */

-- version 2.0.3.0,build 2014-1-17,module epon
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
	(53249,  -7, 'E_ALM_ONU_US_RXPWR_OK', 'DB.eventType.e53249'), 
	(53250,  -7, 'E_ALM_ONU_DS_RXPWR_OK', 'DB.eventType.e53250'); 

INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
        (53249,16389,0),
        (53249,16390,0),
        (53250,16391,0),
        (53250,16392,0);
/* -- version 2.0.3.0,build 2014-1-17,module epon */

-- version 2.0.3.0,build 2014-2-8,module epon
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (40,  'pn8628', 'PN8628', 'onu', '/epon',  '', 'network/onu/8628Icon_16.png', 'network/onu/8628Icon_32.png', 'network/onu/8628Icon_48.png', 'network/onu/8628Icon_64.png', 1);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (48,  'pn8630', 'PN8630', 'onu', '/epon',  '', 'network/onu/8630Icon_16.png', 'network/onu/8630Icon_32.png', 'network/onu/8630Icon_48.png', 'network/onu/8630Icon_64.png', 1);
/* -- version 2.0.3.0,build 2014-2-8,module epon */

-- version 2.0.3.0,build 2014-2-21,module epon
delete from entityType where typeId = 40;
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (40,  'pn8628', 'PN8628', 'onu', '/epon',  '', 'network/onu/8628Icon_16.png', 'network/onu/8628Icon_32.png', 'network/onu/8628Icon_48.png', 'network/onu/8628Icon_64.png', 1);
/* -- version 2.0.3.0,build 2014-2-21,module epon */

-- version 2.0.3.0,build 2014-3-4,module epon
insert into entitytyperelation (type, typeId) values (1,33);
insert into entitytyperelation (type, typeId) values (1,34);
insert into entitytyperelation (type, typeId) values (1,36);
insert into entitytyperelation (type, typeId) values (1,37);
insert into entitytyperelation (type, typeId) values (1,65);
insert into entitytyperelation (type, typeId) values (1,68);
insert into entitytyperelation (type, typeId) values (1,71);
insert into entitytyperelation (type, typeId) values (1,255);
insert into entitytyperelation (type, typeId) values (1,10001);
insert into entitytyperelation (type, typeId) values (1,10002);
insert into entitytyperelation (type, typeId) values (1,10003);
insert into entitytyperelation (type, typeId) values (2,10000);
insert into entitytyperelation (type, typeId) values (2,13000);
insert into entitytyperelation (type, typeId) values (10000,10001);
insert into entitytyperelation (type, typeId) values (10000,10002);
insert into entitytyperelation (type, typeId) values (10000,10003);
insert into entitytyperelation (type, typeId) values (13000,33);
insert into entitytyperelation (type, typeId) values (13000,34);
insert into entitytyperelation (type, typeId) values (13000,36);
insert into entitytyperelation (type, typeId) values (13000,37);
insert into entitytyperelation (type, typeId) values (13000,65);
insert into entitytyperelation (type, typeId) values (13000,68);
insert into entitytyperelation (type, typeId) values (13000,71);
insert into entitytyperelation (type, typeId) values (13000,255);
insert into entitytyperelation (type, typeId) values (50000,10001);
insert into entitytyperelation (type, typeId) values (50000,10002);
insert into entitytyperelation (type, typeId) values (50000,10003);
insert into entitytyperelation (type, typeId) values (33,33);
insert into entitytyperelation (type, typeId) values (34,34);
insert into entitytyperelation (type, typeId) values (36,36);
insert into entitytyperelation (type, typeId) values (37,37);
insert into entitytyperelation (type, typeId) values (65,65);
insert into entitytyperelation (type, typeId) values (68,68);
insert into entitytyperelation (type, typeId) values (71,71);
insert into entitytyperelation (type, typeId) values (255,255);
insert into entitytyperelation (type, typeId) values (10001,10001);
insert into entitytyperelation (type, typeId) values (10002,10002);
insert into entitytyperelation (type, typeId) values (10003,10003);
update EntityType set corpId = 32285 where typeId in (select typeId from entitytyperelation where type = 10000 or type = 13000);
/* -- version 2.0.3.0,build 2014-3-4,module epon */
-- version 2.0.3.0,build 2014-3-8,module epon
insert into entitytyperelation (type, typeId) values (3,10000);
insert into entitytyperelation (type, typeId) values (4,10000);
insert into entitytyperelation (type, typeId) values (4,13000);
/* -- version 2.0.3.0,build 2014-3-8,module epon */

-- version 2.0.3.0,build 2014-3-22,module epon
delete from perfTarget where targetId = 'OLT_PON_USED';
delete from perfTarget where targetId = 'OLT_SNI_USED';
delete from perfTarget where targetId = 'OLT_ONUPON_OUT_SPEED';
delete from perfTarget where targetId = 'OLT_ONUPON_IN_SPEED';
delete from perfTarget where targetId = 'OLT_UNI_OUT_SPEED';
delete from perfTarget where targetId = 'OLT_UNI_IN_SPEED';
delete from perfTarget where targetId = 'OLT_ONUPON_USED';
delete from perfTarget where targetId = 'OLT_UNI_USED';
update perfTarget set targetDisplayName = 'Performance.olt_memUsed' where targetId = 'OLT_MEM_USED';
update perfTarget set targetDisplayName = 'Performance.olt_cpuUsed' where targetId = 'OLT_CPU_USED';
update perfTarget set targetDisplayName = 'Performance.olt_flashUsed' where targetId = 'OLT_FLASH_USED';
update perfTarget set targetDisplayName = 'Performance.olt_fanSpeed' where targetId = 'OLT_FAN_SPEED';
update perfTarget set targetDisplayName = 'Performance.olt_boardTemp' where targetId = 'OLT_BOARD_TEMP';
/* -- version 2.0.3.0,build 2014-3-22,module epon */ 

-- version 2.0.3.0,build 2014-4-23,module epon
delete from perfglobal where flag = 'OLT';
INSERT INTO perfglobal(flag,isPerfOn,isRelationWithDefaultTemplate,defaultTemplateId,isPerfThreshold,defaultCollectTime) values ('10000', 1,1,1,1, 15);
/* -- version 2.0.3.0,build 2014-4-23,module epon */ 
-- version 2.0.3.0,build 2014-5-12,module epon
INSERT INTO batchautodiscoveryentitytype(typeId) values(10001);
INSERT INTO batchautodiscoveryentitytype(typeId) values(10002);
INSERT INTO batchautodiscoveryentitytype(typeId) values(10003);
/* -- version 2.0.3.0,build 2014-5-12,module epon */
                
-- version 2.2.3.0,build 2014-5-20,module epon
INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
        (19,19,'EPON',1),
        (100019,19,'EPON',2),
        (61459,19,'EPON',3),
        (21,21,'EPON',1),
        (100021,21,'EPON',2),
        (61461,21,'EPON',3),
        (5126,5126,'EPON',1),
        (105126,5126,'EPON',2),
        (5126,5126,'EPON',3),
        (5128,5128,'EPON',1),
        (105128,5128,'EPON',2),
        (5128,5128,'EPON',3),
        (5130,5130,'EPON',1),
        (105130,5130,'EPON',2),
        (5130,5130,'EPON',3),
        (5382,5382,'EPON',1),
        (105382,5382,'EPON',2),
        (5382,5382,'EPON',3),
        (5384,5384,'EPON',1),
        (105384,5384,'EPON',2),
        (5384,5384,'EPON',3),
        (5386,5386,'EPON',1),
        (105386,5386,'EPON',2),
        (5386,5386,'EPON',3),
        (5638,5638,'EPON',1),
        (105638,5638,'EPON',2),
        (5638,5638,'EPON',3),
        (5640,5640,'EPON',1),
        (105640,5640,'EPON',2),
        (5640,5640,'EPON',3),
        (5642,5642,'EPON',1),
        (105642,5642,'EPON',2),
        (5642,5642,'EPON',3),
        (5894,5894,'EPON',1),
        (105894,5894,'EPON',2),
        (5894,5894,'EPON',3),
        (5896,5896,'EPON',1),
        (105896,5896,'EPON',2),
        (5896,5896,'EPON',3),
        (5898,5898,'EPON',1),
        (105898,5898,'EPON',2),
        (5898,5898,'EPON',3),
        (28673,28673,'EPON',1),
        (128673,28673,'EPON',2),
        (28673,28673,'EPON',3);
        
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
    (19,  -7, 'ARP_SRC_IP_ATTACK', 'DB.eventType.e19'), 
    (21,  -7, 'ARP_SRC_MAC_ATTACK', 'DB.eventType.e21'),
    (5126,  -7, 'CPU_IN_ARP_PKTS_LOW', 'DB.eventType.e5126'),
    (5128,  -7, 'CPU_IN_ARP_OCTETS_LOW', 'DB.eventType.e5128'),
    (5130,  -7, 'CPU_IN_ARP_RATE_LOW', 'DB.eventType.e5130'),
    (5382,  -7, 'CPU_IN_ARP_PKTS_HIGH', 'DB.eventType.e5382'),
    (5384,  -7, 'CPU_IN_ARP_OCTETS_HIGH', 'DB.eventType.e5384'),
    (5386,  -7, 'CPU_IN_ARP_RATE_HIGH', 'DB.eventType.e5386'),
    (5638,  -7, 'CPU_OUT_ARP_PKTS_LOW', 'DB.eventType.e5638'),
    (5640,  -7, 'CPU_OUT_ARP_OCTETS_LOW', 'DB.eventType.e5640'),
    (5642,  -7, 'CPU_OUT_ARP_RATE_LOW', 'DB.eventType.e5642'),
    (5894,  -7, 'CPU_OUT_ARP_PKTS_HIGH', 'DB.eventType.e5894'),
    (5896,  -7, 'CPU_OUT_ARP_OCTETS_HIGH', 'DB.eventType.e5896'),
    (5898,  -7, 'CPU_OUT_ARP_RATE_HIGH', 'DB.eventType.e5898'),
    (28673,  -7, 'ARP_LEARN_NUM_REACH_LIMIT', 'DB.eventType.e28673'); 
    
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
        (19,19,1),
        (21,21,1),
        (5126,5126,1),
        (5128,5128,1),
        (5130,5130,1),
        (5382,5382,1),
        (5384,5384,1),
        (5386,5386,1),
        (5638,5638,1),
        (5640,5640,1),
        (5642,5642,1),
        (5894,5894,1),
        (5896,5896,1),
        (5898,5898,1),
        (28673,28673,1);    
        
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
        (19,  -7, 'ARP_SRC_IP_ATTACK', 'DB.alertType.a19', 4, '', 0, 0, '0', '1', '', ''),
        (21,  -7, 'ARP_SRC_MAC_ATTACK', 'DB.alertType.a21', 4, '', 0, 0, '0', '1', '', ''),
        (5126,  -7, 'CPU_IN_ARP_PKTS_LOW', 'DB.alertType.a5126', 3, '', 0, 0, '0', '1', '', ''),
        (5128,  -7, 'CPU_IN_ARP_OCTETS_LOW', 'DB.alertType.a5128', 3, '', 0, 0, '0', '1', '', ''),
        (5130,  -7, 'CPU_IN_ARP_RATE_LOW', 'DB.alertType.a5130', 3, '', 0, 0, '0', '1', '', ''),
        (5382,  -7, 'CPU_IN_ARP_PKTS_HIGH', 'DB.alertType.a5382', 3, '', 0, 0, '0', '1', '', ''),
        (5384,  -7, 'CPU_IN_ARP_OCTETS_HIGH', 'DB.alertType.a5384', 3, '', 0, 0, '0', '1', '', ''),
        (5386,  -7, 'CPU_IN_ARP_RATE_HIGH', 'DB.alertType.a5386', 3, '', 0, 0, '0', '1', '', ''),
        (5638,  -7, 'CPU_OUT_ARP_PKTS_LOW', 'DB.alertType.a5638', 3, '', 0, 0, '0', '1', '', ''),
        (5640,  -7, 'CPU_OUT_ARP_OCTETS_LOW', 'DB.alertType.a5640', 3, '', 0, 0, '0', '1', '', ''),
        (5642,  -7, 'CPU_OUT_ARP_RATE_LOW', 'DB.alertType.a5642', 3, '', 0, 0, '0', '1', '', ''),
        (5894,  -7, 'CPU_OUT_ARP_PKTS_HIGH', 'DB.alertType.a5894', 3, '', 0, 0, '0', '1', '', ''),
        (5896,  -7, 'CPU_OUT_ARP_OCTETS_HIGH', 'DB.alertType.a5896', 3, '', 0, 0, '0', '1', '', ''),
        (5898,  -7, 'CPU_OUT_ARP_RATE_HIGH', 'DB.alertType.a5898', 3, '', 0, 0, '0', '1', '', ''),
        (28673,  -7, 'ARP_LEARN_NUM_REACH_LIMIT', 'DB.alertType.a28673', 4, '', 0, 0, '0', '1', '', '');
/* -- version 2.2.3.0,build 2014-5-20,module epon */

-- version 2.2.3.0,build 2014-5-21,module epon
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values(2000003,'oltList',2000000,'rolePower.oltList');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values(2000004,'onuList',2000000,'rolePower.onuList');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values(4000001,'eponConfigFileMgmt',4000000,'Config.eponConfigFileMgt');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values(5000015,'oltSpectrumConfig',5000000,'cmc.oltSpectrumConfig');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values(8000023,'autoClear8800A',8000000,'uesrPower.autoClear8800A');

INSERT INTO RoleFunctionRela(roleId,functionId) values(2,2000003);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,2000004);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,4000001);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000015);
INSERT INTO RoleFunctionRela(roleId,functionId) values(1,8000023);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000023);          
/* -- version 2.2.3.0,build 2014-5-21,module epon */

-- version 2.2.3.0,build 2014-6-3,module epon
delete from perfTarget where targetId = 'OLT_PON_USED';
delete from perfTarget where targetId = 'OLT_SNI_USED';
delete from perfTarget where targetId = 'OLT_ONUPON_USED';
delete from perfTarget where targetId = 'OLT_UNI_USED';
delete from perfthresholdrule where targetId = 'OLT_SNI_USED'; 
delete from perfthresholdrule where targetId = 'OLT_PON_USED'; 
delete from perfthresholdrule where targetId = 'OLT_ONUPON_USED'; 
delete from perfthresholdrule where targetId = 'OLT_UNI_USED'; 
/* -- version 2.2.3.0,build 2014-6-3,module epon */

-- version 2.2.3.0,build 2014-6-4,module epon
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
        (53251,19,0),
        (53252,21,0),
        (53253,5126,0),
        (53253,5382,0),
        (53254,5128,0),
        (53254,5384,0),
        (53255,5130,0),
        (53255,5386,0),
        (53256,5638,0),
        (53256,5894,0),
        (53257,5640,0),
        (53257,5896,0),
        (53258,5642,0),
        (53258,5898,0),
        (53259,28673,0);
        
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
    (53251,  -7, 'ARP_SRC_IP_ATTACK_RECOVERY', 'DB.eventType.e53251'), 
    (53252,  -7, 'ARP_SRC_MAC_ATTACK_RECOVERY', 'DB.eventType.e53252'),
    (53253,  -7, 'CPU_IN_ARP_PKTS_NORMAL', 'DB.eventType.e53253'),
    (53254,  -7, 'CPU_IN_ARP_OCTETS_NORMAL', 'DB.eventType.e53254'),
    (53255,  -7, 'CPU_IN_ARP_RATE_NORMAL', 'DB.eventType.e53255'),
    (53256,  -7, 'CPU_IN_ARP_PKTS_NORMAL', 'DB.eventType.e53256'),
    (53257,  -7, 'CPU_IN_ARP_OCTETS_NORMAL', 'DB.eventType.e53257'),
    (53258,  -7, 'CPU_IN_ARP_RATE_NORMAL', 'DB.eventType.e53258'),
    (53259,  -7, 'CPU_OUT_ARP_PKTS_NORMAL', 'DB.eventType.e53259'); 
/* -- version 2.2.3.0,build 2014-6-4,module epon */

-- version 2.2.3.0,build 2014-7-8,module epon
delete from Event2Alert where eventTypeId = -718;
delete from Event2Alert where eventTypeId = -719;
delete from Event2Alert where eventTypeId = -720;
delete from Event2Alert where eventTypeId = -721;
delete from Event2Alert where eventTypeId = -722;
delete from Event2Alert where eventTypeId = -723;
delete from Event2Alert where eventTypeId = -724;
delete from Event2Alert where eventTypeId = -725;

delete from EventType where typeId = -718;
delete from EventType where typeId = -719;
delete from EventType where typeId = -720;
delete from EventType where typeId = -721;
delete from EventType where typeId = -722;
delete from EventType where typeId = -723; 
delete from EventType where typeId = -724; 
delete from EventType where typeId = -725;

delete from AlertType where typeId = -718;
delete from AlertType where typeId = -720;
delete from AlertType where typeId = -722;
delete from AlertType where typeId = -724;

/* -- version 2.2.3.0,build 2014-7-8,module epon */


-- version 2.2.3.0,build 2014-7-18,module epon
insert into rolefunctionrela values (3,2000003);
insert into rolefunctionrela values (3,2000004);
/* -- version 2.2.3.0,build 2014-7-18,module epon */

-- version 2.3.2.0,build 2014-7-24,module epon
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30001,   'cmc8800a',  'CC8800A',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.1',   'network/ccmts/ccmts_a_16.png',  'network/ccmts/ccmts_a_32.png',  'network/ccmts/ccmts_a_48.png',  'network/ccmts/ccmts_a_64.png',  32285);
insert into entitytyperelation (type, typeId) values (1,30001);
insert into entitytyperelation (type, typeId) values (30000,30001);
insert into entitytyperelation (type, typeId) values (60000,30001);
insert into entitytyperelation (type, typeId) values (80000,30001);
insert into entitytyperelation (type, typeId) values (30001,30001);

INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30005,   'cmc8800c-a',  'CC8800C-A',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.3.1',   'network/ccmts/ccmts_c_a_16.png',  'network/ccmts/ccmts_c_a_32.png',  'network/ccmts/ccmts_c_a_48.png',  'network/ccmts/ccmts_c_a_64.png',  32285);

insert into entitytyperelation (type, typeId) values (1,30005);
insert into entitytyperelation (type, typeId) values (30000,30005);
insert into entitytyperelation (type, typeId) values (60000,30005);
insert into entitytyperelation (type, typeId) values (80000,30005);
insert into entitytyperelation (type, typeId) values (30005,30005);
insert into entitytyperelation (type, typeId) values (120000,30005);
/* -- version 2.3.2.0,build 2014-7-24,module epon */


-- version 2.3.2.0,build 2014-8-26,module epon
UPDATE perfthresholdrule SET thresholds = '1_80_3#1_85_4' WHERE targetId = 'OLT_CPU_USED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_90_3#1_95_4' WHERE targetId = 'OLT_MEM_USED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_90_3' WHERE targetId = 'OLT_FLASH_USED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_85_4' WHERE targetId = 'OLT_BOARD_TEMP' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_8_3#5_2_4' WHERE targetId = 'OLT_PORT_TX_POWER' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_-7_2#5_-24_3#5_-27_4' WHERE targetId = 'OLT_PORT_RE_POWER' and templateId = 1;
delete from perfthresholdrule  WHERE targetId = 'OLT_PORT_CURRENT' and templateId = 1;
delete from perfthresholdrule  WHERE targetId = 'OLT_PORT_VOLTAGE' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '5_0_2#1_90_3' WHERE targetId = 'OLT_PORT_TEMP' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'OLT_SNI_IN_SPEED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'OLT_SNI_OUT_SPEED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'OLT_PON_IN_SPEED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'OLT_PON_OUT_SPEED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'OLT_ONUPON_OUT_SPEED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'OLT_ONUPON_IN_SPEED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'OLT_UNI_OUT_SPEED' and templateId = 1;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'OLT_UNI_IN_SPEED' and templateId = 1;
/* -- version 2.3.2.0,build 2014-8-26,module epon */

-- version 2.3.2.0,build 2014-9-29,module epon
insert into entitytyperelation (type, typeId) values (13000,40);
insert into entitytyperelation (type, typeId) values (13000,48);
/* -- version 2.3.2.0,build 2014-9-29,module epon */

-- version 2.4.3.0,build 2014-10-17,module epon
update EntityType set module = 'olt' where module = 'epon';
insert into EntityTypeRelation values(3, 33);
insert into EntityTypeRelation values(3, 34);
insert into EntityTypeRelation values(3, 36);
insert into EntityTypeRelation values(3, 37);
insert into EntityTypeRelation values(3, 40);
insert into EntityTypeRelation values(3, 48);
insert into EntityTypeRelation values(3, 65);
insert into EntityTypeRelation values(3, 68);
insert into EntityTypeRelation values(3, 71);
insert into EntityTypeRelation values(3, 255);
insert into EntityTypeRelation values(3, 10001);
insert into EntityTypeRelation values(3, 10002);
insert into EntityTypeRelation values(3, 10003);
insert into EntityTypeRelation values(3, 13000);
/* -- version 2.4.3.0,build 2014-10-17,module epon */

-- version 2.4.3.0,build 2014-10-22,module epon
delete from perfthresholdrule  WHERE targetId = 'OLT_PORT_RE_POWER' and templateId = 1;
/* -- version 2.4.3.0,build 2014-10-22,module epon */

-- version 2.4.3.0,build 2014-10-25,module epon
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
    (-763,-50001,'OLTSniInUsedEvent','DB.eventType.e763',''),
    (-764,-50001,'OLTSniInUsedEvent','DB.eventType.e764',''),
    (-765,-50001,'OLTSniOutUsedEvent','DB.eventType.e765',''),
    (-766,-50001,'OLTSniOutUsedEvent','DB.eventType.e766',''),
    (-767,-50001,'OLTPonInUsedEvent','DB.eventType.e767',''),
    (-768,-50001,'OLTPonInUsedEvent','DB.eventType.e768',''),
    (-769,-50001,'OLTPonOutUsedEvent','DB.eventType.e769',''),
    (-770,-50001,'OLTPonOutUsedEvent','DB.eventType.e770','');
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (-764,-50001,'OLTSniInUsed','DB.alertType.a764',3,'',0,0,'0','1','',''),
    (-766,-50001,'OLTSniOutUsed','DB.alertType.a766',3,'',0,0,'0','1','',''),
    (-768,-50001,'OLTPonInUsed','DB.alertType.a768',3,'',0,0,'0','1','',''),
    (-770,-50001,'OLTPonOutUsed','DB.alertType.a770',3,'',0,0,'0','1','','');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
    (-763,-764,0),
    (-764,-764,1),
    (-765,-766,0),
    (-766,-766,1),
    (-767,-768,0),
    (-768,-768,1),
    (-769,-770,0),
    (-770,-770,1);
    
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_SNI_IN_USED', 10000, 'Performance.sniInUsed','Performance.speed','%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_SNI_OUT_USED', 10000, 'Performance.sniOutUsed','Performance.speed','%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PON_IN_USED', 10000, 'Performance.ponInUsed','Performance.speed','%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('OLT_PON_OUT_USED', 10000, 'Performance.ponOutUsed','Performance.speed','%');

INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('OLT_SNI_IN_USED', 1,'1_50_3#1_70_4#1_85_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_SNI_OUT_USED', 1,'1_50_3#1_70_4#1_85_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_PON_IN_USED', 1,'1_50_3#1_70_4#1_85_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_PON_OUT_USED', 1,'1_50_3#1_70_4#1_85_5',1,1,1,'00:00-23:59#1234567');
/* -- version 2.4.3.0,build 2014-10-25,module epon */

-- version 2.4.3.0,build 2014-10-29,module epon
delete from perfthresholdrule  WHERE targetId = 'OLT_SNI_IN_SPEED' and templateId = 1;
delete from perfthresholdrule  WHERE targetId = 'OLT_SNI_OUT_SPEED' and templateId = 1;
delete from perfthresholdrule  WHERE targetId = 'OLT_PON_IN_SPEED' and templateId = 1;
delete from perfthresholdrule  WHERE targetId = 'OLT_PON_OUT_SPEED' and templateId = 1;
/* -- version 2.4.3.0,build 2014-10-29,module epon */

-- version 2.4.5.0,build 2015-1-14,module epon
UPDATE perfthresholdtemplate SET parentType = 10000 WHERE templateId = 1;
delete from  perfthresholdtemplate WHERE isDefaultTemplate != 1;
delete from perftarget where targetType = 10000;
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_CONNECTIVITY', 10000, 'Performance.delay','Performance.olt_deviceStatus','ms',10000,1,1,0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_SNI_IN_USED', 10000, 'Performance.sniInUsed','Performance.speed','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_SNI_OUT_USED', 10000, 'Performance.sniOutUsed','Performance.speed','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_PON_IN_USED', 10000, 'Performance.ponInUsed','Performance.speed','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_PON_OUT_USED', 10000, 'Performance.ponOutUsed','Performance.speed','%',100,0,1,2);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_CPU_USED', 10000, 'Performance.cpuUsed', 'Performance.serviceQuality','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_FLASH_USED', 10000, 'Performance.flashUsed', 'Performance.serviceQuality','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_MEM_USED', 10000, 'Performance.memUsed', 'Performance.serviceQuality','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_FAN_SPEED', 10000, 'Performance.fanSpeed', 'Performance.serviceQuality', 'rps',10000,0,1,0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_BOARD_TEMP', 10000, 'Performance.boardTemp', 'Performance.serviceQuality','℃',120,-40,1,0);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_PON_RE_POWER', 10000, 'Performance.ponOptRePower','Performance.optLink','dBm',-7,-29,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_PON_TX_POWER', 10000, 'Performance.ponOptTxPower','Performance.optLink','dBm',8,2,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_PON_OPT_TEMP', 10000, 'Performance.ponOptTemp','Performance.optLink','℃',120,-40,1,0);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_SNI_RE_POWER', 10000, 'Performance.uplinkOptRePower','Performance.optLink','dBm',-3,-24,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_SNI_TX_POWER', 10000, 'Performance.uplinkOptRtPower','Performance.optLink','dBm',3,-9,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_SNI_OPT_TEMP', 10000, 'Performance.uplinkOptTemp','Performance.optLink','℃',120,-40,1,0);

/* -- version 2.4.5.0,build 2015-1-14,module epon */

-- version 2.4.5.0,build 2015-1-16,module epon
INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('OLT_CONNECTIVITY', 1,'1_1000_5_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_SNI_IN_USED', 1,'1_50_2_4#1_70_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	('OLT_PON_IN_USED', 1,'1_50_2_4#1_70_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	('OLT_SNI_OUT_USED', 1,'1_50_2_4#1_70_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	('OLT_PON_OUT_USED', 1,'1_50_2_4#1_70_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),

	('OLT_CPU_USED', 1,'1_80_3_4#1_85_4_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_FLASH_USED', 1,'1_90_3_4',1,1,1,'0:00-23:59#1234567'),
	('OLT_MEM_USED', 1,'1_90_3_4#1_95_4_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_FAN_SPEED', 1,'5_25_4_4',1,1,1,'00:00-23:59#1234567'),
	('OLT_BOARD_TEMP', 1,'1_85_3_4',1,1,1,'00:00-23:59#1234567'),

	('OLT_PON_RE_POWER', 1,'1_-7_2_4#5_-24_3_5#5_-27_4_6',1,1,1,'00:00-23:59#1234567'),
	('OLT_PON_TX_POWER', 1,'1_8_3_4#5_2_4_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_PON_OPT_TEMP', 1,'5_0_2_4#1_90_3_5',1,1,1,'0:00-23:59#1234567'),
	
	('OLT_SNI_RE_POWER', 1,'5_-24_3_4#1_-3_4_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_SNI_TX_POWER', 1,'5_-9_3_4#1_3_4_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_SNI_OPT_TEMP', 1,'5_0_2_4#1_90_3_5',1,1,1,'0:00-23:59#1234567');
	
delete from Event2Alert where eventTypeId = -710;
delete from Event2Alert where eventTypeId = -711;
delete from Event2Alert where eventTypeId = -712;
delete from Event2Alert where eventTypeId = -713;
delete from Event2Alert where eventTypeId = -714;
delete from Event2Alert where eventTypeId = -715;
delete from Event2Alert where eventTypeId = -716;
delete from Event2Alert where eventTypeId = -717;
delete from Event2Alert where eventTypeId = -730;
delete from Event2Alert where eventTypeId = -731;
delete from Event2Alert where eventTypeId = -738;
delete from Event2Alert where eventTypeId = -739;

delete from EventType where typeId = -710;
delete from EventType where typeId = -711;
delete from EventType where typeId = -712;
delete from EventType where typeId = -713;
delete from EventType where typeId = -714;
delete from EventType where typeId = -715; 
delete from EventType where typeId = -716; 
delete from EventType where typeId = -717;
delete from EventType where typeId = -730;
delete from EventType where typeId = -731;
delete from EventType where typeId = -738;
delete from EventType where typeId = -739;

delete from AlertType where typeId = -710;
delete from AlertType where typeId = -712;
delete from AlertType where typeId = -714;
delete from AlertType where typeId = -716;
delete from AlertType where typeId = -730;
delete from AlertType where typeId = -738;
/* -- version 2.4.5.0,build 2015-1-16,module epon */
-- version 2.5.1.0,build 2015-01-28,module epon
update alerttype set name = 'E_ALM_ONU_FIBER_BREAK' where typeId = 16435;
update eventtype set name = 'E_ALM_ONU_FIBER_BREAK' where typeId = 16435;
/* -- version 2.5.1.0,build 2015-01-28,module epon */

-- version 2.5.1.0,build 2015-01-29,module epon
insert into event2alert values(16434, 16435, 0);
/* -- version 2.5.1.0,build 2015-01-29,module epon */

-- version 2.5.2.0,build 2015-04-23,module epon
INSERT INTO perfthresholdtemplate(templateId,templateName, templateType, createUser,isDefaultTemplate, createTime, parentType)
                values(3,'ONU_DEFAULT_TEMPLATE', 13000,'system', 1, sysdate(), 13000);
insert into perfGlobal values(13000, 1, 1,3,1,15);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_PON_RE_POWER', 13000, 'Performance.onuPonRePower','Performance.optLink','dBm',-7, -27, 1, 2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_PON_TX_POWER', 13000, 'Performance.onuPonTxPower','Performance.optLink','dBm', 8, 2, 1, 2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('OLT_PONLLID_RE_POWER', 13000, 'Performance.oltPonRePower','Performance.optLink','dBm', -7, -27, 1, 2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_PON_IN_SPEED', 13000, 'Performance.onuPonInSpeed','Performance.flow','Mbps', 800, 0, 1, 0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_PON_OUT_SPEED', 13000, 'Performance.onuPonOutSpeed','Performance.flow','Mbps', 800, 0, 1, 0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_UNI_IN_SPEED', 13000, 'Performance.onuUniInSpeed','Performance.flow','Mbps', 800, 0, 1, 0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_UNI_OUT_SPEED', 13000, 'Performance.onuUniOutSpeed','Performance.flow','Mbps', 800, 0, 1, 0);

INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('ONU_PON_RE_POWER', 3,'1_-7_2_4#5_-24_3_5#5_-27_4_6',1,1,1,'00:00-23:59#1234567'),
	('ONU_PON_TX_POWER', 3,'1_8_3_4#5_2_4_5',1,1,1,'00:00-23:59#1234567'),
	('OLT_PONLLID_RE_POWER', 3,'1_-7_2_4#5_-24_3_5#5_-27_4_6',1,1,1,'0:00-23:59#1234567'),
	('ONU_PON_IN_SPEED', 3,'1_800_3_4',1,1,1,'0:00-23:59#1234567'),
	('ONU_PON_OUT_SPEED', 3,'1_800_3_4',1,1,1,'0:00-23:59#1234567'),
	('ONU_UNI_IN_SPEED', 3,'1_800_3_4',1,1,1,'0:00-23:59#1234567'),
	('ONU_UNI_OUT_SPEED', 3,'1_800_3_4',1,1,1,'0:00-23:59#1234567');
insert into deviceperftarget VALUES 
		('onu_onlineStatus', 13000, 13000, 13000, 'onu_deviceStatus', 1),
		('onu_optLink', 13000, 13000, 13000, 'onu_service', 2),
		('onu_portFlow', 13000, 13000, 13000, 'onu_flow', 4);
		
INSERT INTO globalperfcollecttime VALUES
		('onu_onlineStatus', 13000, 13000, 5, 1, 'onu_deviceStatus', 1),
		('onu_optLink', 13000, 13000, 15, 1, 'onu_service', 2),
		('onu_portFlow', 13000, 13000, 15, 1, 'onu_flow', 4);

INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
    (-729,-50001,'OnuPonTransPowerEvent','DB.eventType.e729',''),
    (-728,-50001,'OnuPonTransPowerEvent','DB.eventType.e728',''),
    (-727,-50001,'OnuPonRevPowerEvent','DB.eventType.e727',''),
    (-726,-50001,'OnuPonRevPowerEvent','DB.eventType.e726',''),
    (-725,-50001,'OltPonLlidRevPowerEvent','DB.eventType.e725',''),
    (-724,-50001,'OltPonLlidRevPowerEvent','DB.eventType.e724',''),
    (-723,-50001,'OnuPonInSpeedEvent','DB.eventType.e723',''),
    (-722,-50001,'OnuPonInSpeedEvent','DB.eventType.e722',''),
    (-721,-50001,'OnuPonOutSpeedEvent','DB.eventType.e721',''),
    (-720,-50001,'OnuPonOutSpeedEvent','DB.eventType.e720',''),
    (-719,-50001,'OnuUniInSpeedEvent','DB.eventType.e719',''),
    (-718,-50001,'OnuUniInSpeedEvent','DB.eventType.e718',''),
    (-717,-50001,'OnuUniOutSpeedEvent','DB.eventType.e717',''),
    (-716,-50001,'OnuUniOutSpeedEvent','DB.eventType.e716','');
    
    
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (-729,-50001,'OnuPonTransPower','DB.alertType.a729',3,'',0,0,'0','1','',''),
    (-727,-50001,'OnuPonRevPower','DB.alertType.a727',3,'',0,0,'0','1','',''),
    (-725,-50001,'OltPonLlidRev','DB.alertType.a725',3,'',0,0,'0','1','',''),
    (-723,-50001,'OnuPonInSpeed','DB.alertType.a723',3,'',0,0,'0','1','',''),
    (-721,-50001,'OltPonOutSpeed','DB.alertType.a721',3,'',0,0,'0','1','',''),
    (-719,-50001,'OltUniInSpeed','DB.alertType.a719',3,'',0,0,'0','1','',''),
    (-717,-50001,'OltUniOutSpeed','DB.alertType.a717',3,'',0,0,'0','1','','');
    
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
    (-728,-729,0),
    (-729,-729,1),
    (-726,-727,0),
    (-727,-727,1),
    (-724,-725,0),
    (-725,-725,1),	
    (-722,-723,0),
    (-723,-723,1),
    (-720,-721,0),
    (-720,-721,1),
    (-718,-719,0),
    (-719,-719,1),
    (-716,-717,0),
    (-717,-717,1);
    
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000023,'onuPerfParamsConfig',5000000,'userPower.onuPerfParamsConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000023);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000024,'onuGlobalConfig',5000000,'userPower.onuGlobalConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000024);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000025,'onuPerfTempMgmt',5000000,'userPower.onuPerfTempMgmt');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000025);

insert into entitytyperelation (type, typeId) values (1,40);
insert into entitytyperelation (type, typeId) values (1,48);
insert into entitytyperelation (type, typeId) values (13000,40);
insert into entitytyperelation (type, typeId) values (13000,48);
insert into entitytyperelation (type, typeId) values (40,40);
insert into entitytyperelation (type, typeId) values (48,48);
/* -- version 2.5.2.0,build 2015-04-23,module epon */

-- version 2.5.2.0,build 2015-04-27,module epon
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000016,'onuAuthManage',4000000,'userPower.onuAuthManage');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000016);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000017,'onuAuthFailList',4000000,'userPower.onuAuthFailList');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000017);
/* -- version 2.5.2.0,build 2015-04-27,module epon */

-- version 2.4.9.0,build 2015-06-04,module epon
delete from deviceperftarget where entityType = 10000;
insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 
			('olt_onlineStatus', 10000, 10000, 10001, 'olt_deviceStatus', 1),
			('olt_cpuUsed', 10000, 10000, 10001, 'olt_service', 2),
			('olt_memUsed', 10000, 10000, 10001, 'olt_service', 2),
			('olt_flashUsed', 10000, 10000, 10001, 'olt_service', 2),
			('olt_boardTemp', 10000, 10000, 10001, 'olt_service', 2),
			('olt_fanSpeed', 10000, 10000, 10001, 'olt_service', 2),
			('olt_optLink', 10000, 10000, 10001, 'olt_service', 2),
			('olt_sniFlow', 10000, 10000, 10001, 'olt_flow', 4),
			('olt_ponFlow', 10000, 10000, 10001, 'olt_flow', 4),
			
			('olt_onlineStatus', 10000, 10000, 10002, 'olt_deviceStatus', 1),	
			('olt_cpuUsed', 10000, 10000, 10002, 'olt_service', 2),
			('olt_flashUsed', 10000, 10000, 10002, 'olt_service', 2),
			('olt_memUsed', 10000, 10000, 10002, 'olt_service', 2),
			('olt_boardTemp', 10000, 10000, 10002, 'olt_service', 2),
			('olt_fanSpeed', 10000, 10000, 10002, 'olt_service', 2),
			('olt_optLink', 10000, 10000, 10002, 'olt_service', 2),
			('olt_sniFlow', 10000, 10000, 10002, 'olt_flow', 4),
			('olt_ponFlow', 10000, 10000, 10002, 'olt_flow', 4),
			
			('olt_onlineStatus', 10000, 10000, 10003, 'olt_deviceStatus', 1),
			('olt_cpuUsed', 10000, 10000, 10003, 'olt_service', 2),
			('olt_memUsed', 10000, 10000, 10003, 'olt_service', 2),
			('olt_flashUsed', 10000, 10000, 10003, 'olt_service', 2),
			('olt_boardTemp', 10000, 10000, 10003, 'olt_service', 2),
			('olt_fanSpeed', 10000, 10000, 10003, 'olt_service', 2),
			('olt_optLink', 10000, 10000, 10003, 'olt_service', 2),
			('olt_sniFlow', 10000, 10000, 10003, 'olt_flow', 4),
			('olt_ponFlow', 10000, 10000, 10003, 'olt_flow', 4);

delete from globalperfcollecttime where entityType = 10000;
INSERT INTO globalperfcollecttime(perfTargetName, parentType, entityType, globalInterval, globalEnable, targetGroup, groupPriority) VALUES
			('olt_onlineStatus', 10000, 10000, 5, 1, 'olt_deviceStatus', 1),
			('olt_cpuUsed', 10000, 10000, 15, 1, 'olt_service', 2),
			('olt_memUsed', 10000, 10000, 15, 1, 'olt_service', 2),
			('olt_flashUsed', 10000, 10000, 15, 1, 'olt_service', 2),
			('olt_boardTemp', 10000, 10000, 15, 1, 'olt_service', 2),
			('olt_fanSpeed', 10000, 10000, 15, 1, 'olt_service', 2),
			('olt_optLink', 10000, 10000, 15, 1, 'olt_service', 2),
			('olt_sniFlow', 10000, 10000, 15, 1, 'olt_flow', 4),
			('olt_ponFlow', 10000, 10000, 15, 1, 'olt_flow', 4);
/* -- version 2.4.9.0,build 2015-06-04,module epon */

-- version 2.6.0.0,build 2015-06-09,module epon
delete from perfthresholdrule where targetId = 'OLT_PON_RE_POWER';  
/* -- version 2.6.0.0,build 2015-06-09,module epon */ 

-- version 2.6.0.0,build 2015-06-11,module epon
update entitytype set icon64 = 'network/8603_64.gif' where typeId = 10003
/* -- version 2.6.0.0,build 2015-06-11,module epon */ 

-- version 2.6.0.0,build 2015-06-23,module epon
update entitytype set discoveryBean='topvisionOltDiscoveryService' where typeId in (10001,10002,10003);
/* -- version 2.6.0.0,build 2015-06-23,module epon */ 

-- version 2.6.0.0,build 2015-07-10,module epon
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId, discoveryBean) VALUES
                (13100,  'other_onu', 'OTHER_ONU', 'onu', '/epon',  '', 'network/onu_16.gif', 'network/onu_32.gif', 'network/onu_48.gif', 'network/onu_64.gif', 1, null);
insert into entitytyperelation (type, typeId) values (1,13100);
insert into entitytyperelation (type, typeId) values (3,13100);
insert into entitytyperelation (type, typeId) values (13000,13100);
insert into entitytyperelation (type, typeId) values (13100,13100);
insert into entitytyperelation (type, typeId) values (130000,13100);
/* -- version 2.6.0.0,build 2015-07-10,module epon */ 

-- version 2.6.5.2,build 2015-11-23,module epon
delete from entitytyperelation where type = 13000 and typeId = 48;
insert into entitytyperelation (type, typeId) values (13000,48);

delete from entitytyperelation where type = 13000 and typeId = 40;
insert into entitytyperelation (type, typeId) values (13000,40);
/* -- version 2.6.5.2,build 2015-11-23,module epon */	
	
-- version 2.6.5.2,build 2015-11-24,module epon
update globalperfcollecttime set globalInterval = 240, globalEnable = 0 where perfTargetName = 'onu_onlineStatus' and entityType = 13000;
update globalperfcollecttime set globalInterval = 240, globalEnable = 0  where perfTargetName = 'onu_optLink' and entityType = 13000;
update globalperfcollecttime set globalInterval = 240, globalEnable = 0  where perfTargetName = 'onu_portFlow' and entityType = 13000;
/* -- version 2.6.5.2,build 2015-11-24,module epon */

-- version 2.6.8.0,build 2016-03-11,module epon
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES(259, 4099, 0);
/* -- version 2.6.8.0,build 2016-03-11,module epon */

-- version 2.6.8.0,build 2016-03-12,module epon
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-49999,-50001,'Olt Threshold Alert','ALERT.oltThreshold',0,'',0,0,'0','1',NULL,NULL);
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-49998,-50001,'Onu Threshold Alert','ALERT.onuThreshold',0,'',0,0,'0','1',NULL,NULL);
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-6,-7,'Olt Alert','DB.alertType.oltAlert',0,'',0,0,'0','1',NULL,NULL);
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-5,-7,'Onu Alert','DB.alertType.onuAlert',0,'',0,0,'0','1',NULL,NULL);
UPDATE alerttype SET category = -49999 WHERE typeId = -770;
UPDATE alerttype SET category = -49999 WHERE typeId = -768;
UPDATE alerttype SET category = -49999 WHERE typeId = -766;
UPDATE alerttype SET category = -49999 WHERE typeId = -764;
UPDATE alerttype SET category = -49999 WHERE typeId = -761;
UPDATE alerttype SET category = -49999 WHERE typeId = -757;
UPDATE alerttype SET category = -49999 WHERE typeId = -755;
UPDATE alerttype SET category = -49999 WHERE typeId = -753;
UPDATE alerttype SET category = -49999 WHERE typeId = -751;
UPDATE alerttype SET category = -49999 WHERE typeId = -736;
UPDATE alerttype SET category = -49999 WHERE typeId = -734;
UPDATE alerttype SET category = -49999 WHERE typeId = -732;

UPDATE alerttype SET category = -49998 WHERE typeId = -729;
UPDATE alerttype SET category = -49998 WHERE typeId = -727;
UPDATE alerttype SET category = -49998 WHERE typeId = -725;
UPDATE alerttype SET category = -49998 WHERE typeId = -723;
UPDATE alerttype SET category = -49998, name = 'OnuPonOutSpeed' WHERE typeId = -721;
UPDATE alerttype SET category = -49998, name = 'OnuUniInSpeed' WHERE typeId = -719;
UPDATE alerttype SET category = -49998, name = 'OnuUniOutSpeed' WHERE typeId = -717;

UPDATE alerttype SET category = -6 WHERE typeId = 1;
UPDATE alerttype SET category = -6 WHERE typeId = 19;
UPDATE alerttype SET category = -6 WHERE typeId = 21;
UPDATE alerttype SET category = -6 WHERE typeId = 266;
UPDATE alerttype SET category = -6 WHERE typeId = 4098;
UPDATE alerttype SET category = -6 WHERE typeId = 4099;
UPDATE alerttype SET category = -6 WHERE typeId = 4101;
UPDATE alerttype SET category = -6 WHERE typeId = 4109;
UPDATE alerttype SET category = -6 WHERE typeId = 4113;
UPDATE alerttype SET category = -6 WHERE typeId = 4115;
UPDATE alerttype SET category = -6 WHERE typeId = 4117;
UPDATE alerttype SET category = -6 WHERE typeId = 4119;
UPDATE alerttype SET category = -6 WHERE typeId = 4121;
UPDATE alerttype SET category = -6 WHERE typeId = 4125;
UPDATE alerttype SET category = -6 WHERE typeId = 4127;
UPDATE alerttype SET category = -6 WHERE typeId = 4130;
UPDATE alerttype SET category = -6 WHERE typeId = 5126;
UPDATE alerttype SET category = -6 WHERE typeId = 5128;
UPDATE alerttype SET category = -6 WHERE typeId = 5130;
UPDATE alerttype SET category = -6 WHERE typeId = 5382;
UPDATE alerttype SET category = -6 WHERE typeId = 5384;
UPDATE alerttype SET category = -6 WHERE typeId = 5386;
UPDATE alerttype SET category = -6 WHERE typeId = 5638;
UPDATE alerttype SET category = -6 WHERE typeId = 5640;
UPDATE alerttype SET category = -6 WHERE typeId = 5642;
UPDATE alerttype SET category = -6 WHERE typeId = 5894;
UPDATE alerttype SET category = -6 WHERE typeId = 5896;
UPDATE alerttype SET category = -6 WHERE typeId = 5898;
UPDATE alerttype SET category = -6 WHERE typeId = 12289;
UPDATE alerttype SET category = -6 WHERE typeId = 12290;
UPDATE alerttype SET category = -6 WHERE typeId = 12293;
UPDATE alerttype SET category = -6 WHERE typeId = 12295;
UPDATE alerttype SET category = -6 WHERE typeId = 12297;
UPDATE alerttype SET category = -6 WHERE typeId = 12305;
UPDATE alerttype SET category = -6 WHERE typeId = 12306;
UPDATE alerttype SET category = -6 WHERE typeId = 12307;
UPDATE alerttype SET category = -6 WHERE typeId = 12308;
UPDATE alerttype SET category = -6 WHERE typeId = 12309;
UPDATE alerttype SET category = -6 WHERE typeId = 12310;
UPDATE alerttype SET category = -6 WHERE typeId = 12311;
UPDATE alerttype SET category = -6 WHERE typeId = 12312;
UPDATE alerttype SET category = -6 WHERE typeId = 12313;
UPDATE alerttype SET category = -6 WHERE typeId = 12314;
UPDATE alerttype SET category = -6 WHERE typeId = 12315;
UPDATE alerttype SET category = -6 WHERE typeId = 12316;
UPDATE alerttype SET category = -6 WHERE typeId = 12317;
UPDATE alerttype SET category = -6 WHERE typeId = 12318;
UPDATE alerttype SET category = -6 WHERE typeId = 12319;
UPDATE alerttype SET category = -6 WHERE typeId = 12320;
UPDATE alerttype SET category = -6 WHERE typeId = 12321;
UPDATE alerttype SET category = -6 WHERE typeId = 12322;
UPDATE alerttype SET category = -6 WHERE typeId = 12325;
UPDATE alerttype SET category = -6 WHERE typeId = 12326;
UPDATE alerttype SET category = -6 WHERE typeId = 12327;
UPDATE alerttype SET category = -6 WHERE typeId = 28673;

UPDATE alerttype SET category = -5 WHERE typeId = 16385;
UPDATE alerttype SET category = -5 WHERE typeId = 16386;
UPDATE alerttype SET category = -5 WHERE typeId = 16387;
UPDATE alerttype SET category = -5 WHERE typeId = 16388;
UPDATE alerttype SET category = -5 WHERE typeId = 16389;
UPDATE alerttype SET category = -5 WHERE typeId = 16390;
UPDATE alerttype SET category = -5 WHERE typeId = 16391;
UPDATE alerttype SET category = -5 WHERE typeId = 16392;
UPDATE alerttype SET category = -5 WHERE typeId = 16393;
UPDATE alerttype SET category = -5 WHERE typeId = 16394;
UPDATE alerttype SET category = -5 WHERE typeId = 16395;
UPDATE alerttype SET category = -5 WHERE typeId = 16396;
UPDATE alerttype SET category = -5 WHERE typeId = 16397;
UPDATE alerttype SET category = -5 WHERE typeId = 16417;
UPDATE alerttype SET category = -5 WHERE typeId = 16421;
UPDATE alerttype SET category = -5 WHERE typeId = 16422;
UPDATE alerttype SET category = -5 WHERE typeId = 16423;
UPDATE alerttype SET category = -5 WHERE typeId = 16424;
UPDATE alerttype SET category = -5 WHERE typeId = 16427;
UPDATE alerttype SET category = -5 WHERE typeId = 16432;
UPDATE alerttype SET category = -5 WHERE typeId = 16433;
UPDATE alerttype SET category = -5 WHERE typeId = 16435;
UPDATE alerttype SET category = -5 WHERE typeId = 16437;
UPDATE alerttype SET category = -5 WHERE typeId = 20481;
UPDATE alerttype SET category = -5 WHERE typeId = 20483;
UPDATE alerttype SET category = -5 WHERE typeId = 20485;
UPDATE alerttype SET category = -5 WHERE typeId = 20487;
UPDATE alerttype SET category = -5 WHERE typeId = 20489;
UPDATE alerttype SET category = -5 WHERE typeId = 20491;
/* -- version 2.6.8.0,build 2016-03-12,module epon */

-- version 2.6.8.0,build 2016-03-18,module epon
INSERT INTO eponeventtyperelation(deviceEventTypeId, emsEventTypeId, module, type) VALUES (321, 16435, 'EPON', 2);
/* -- version 2.6.8.0,build 2016-03-18,module epon  */

-- version 2.6.8.3,build 2016-03-24,module epon
INSERT INTO eponeventtyperelation(deviceEventTypeId, emsEventTypeId, module, type) VALUES
	(16439, 16439, 'EPON', 1),
	(116439, 16439, 'EPON', 2),
	(16439, 16439, 'EPON', 3),
	(2, 16393, 'EPON', 3);
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES (16439,  -5, 'ONU_DELETE', 'DB.eventType.e16439');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES 
	(16434,16439,0),
	(16439,16439,1),
	(16434,258,0),
	(258,258,1);
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
	(16439,-5,'ONU_DELETE','DB.alertType.a16439',2,'',0,0,'0','1',NULL,NULL),
	(258,-5,'ONU_REGISTER','DB.alertType.a258',3,'',0,0,'0','1',NULL,NULL);
/* -- version 2.6.8.3,build 2016-03-24,module epon  */

-- version 2.6.8.0,build 2016-03-27,module epon
INSERT INTO nbiperfgroup VALUES (1, 'PER_OLT_SERVICE','OLT服务质量','olt');
INSERT INTO nbiperfgroup VALUES (2, 'PER_OLT_OPT','光链路信息','olt');
INSERT INTO nbiperfgroup VALUES (3, 'PER_OLT_RATE','速率','olt');
INSERT INTO nbiperfgroup VALUES (4, 'PER_OLT_DELAY','响应延时','olt');
INSERT INTO nbiperfgroup VALUES (5, 'PER_ONU_RATE','ONU速率','onu');
INSERT INTO nbiperfgroup VALUES (6, 'PER_ONU_OPT','ONU光链路信息','onu');
INSERT INTO nbiperfgroupindex VALUES (1, 1, '1.3.6.1.4.1.32285.11.2.3.1.3.1.1.9', 'topSysBdCurrentTemperature', '板卡温度',5,1);
INSERT INTO nbiperfgroupindex VALUES (1, 2, '1.3.6.1.4.1.32285.11.2.3.1.3.1.1.4', 'topSysBdCPUUseRatio', 'CPU利用率',5,1);
INSERT INTO nbiperfgroupindex VALUES (1, 3, '1.3.6.1.4.1.32285.12.2.3.9.2.1.1', 'flashUsed', 'Flash利用率',5,1);
INSERT INTO nbiperfgroupindex VALUES (1, 4, '1.3.6.1.4.1.32285.12.2.3.9.2.1.2', 'memUsed', '内存利用率',5,1);
INSERT INTO nbiperfgroupindex VALUES (1, 5, '1.3.6.1.4.1.32285.11.2.3.1.5.1.1.4', 'fanSpeed', '风扇转速',5,1);
INSERT INTO nbiperfgroupindex VALUES (2, 6, '1.3.6.1.4.1.32285.12.2.3.9.3.1.1', 'optTxPower', '光口发送功率',5,1);
INSERT INTO nbiperfgroupindex VALUES (2, 7, '1.3.6.1.4.1.32285.12.2.3.9.3.1.2', 'optCurrent', '光口偏置电流',5,1);
INSERT INTO nbiperfgroupindex VALUES (2, 8, '1.3.6.1.4.1.32285.12.2.3.9.3.1.3', 'optVoltage', '光口电压',5,1);
INSERT INTO nbiperfgroupindex VALUES (2, 9, '1.3.6.1.4.1.32285.12.2.3.9.3.1.4', 'optTemp', '光口温度',5,1);
INSERT INTO nbiperfgroupindex VALUES (3, 10, '1.3.6.1.4.1.32285.12.2.3.9.4.1.1', 'ifInSpeed', '入方向速率',5,1);
INSERT INTO nbiperfgroupindex VALUES (3, 11, '1.3.6.1.4.1.32285.12.2.3.9.4.1.2', 'portInUsed', '入方向利用率',5,1);
INSERT INTO nbiperfgroupindex VALUES (3, 12, '1.3.6.1.4.1.32285.12.2.3.9.4.1.3', 'ifOutSpeed', '出方向速率',5,1);
INSERT INTO nbiperfgroupindex VALUES (3, 13, '1.3.6.1.4.1.32285.12.2.3.9.4.1.4', 'portOutUsed', '出方向利用率',5,1);
INSERT INTO nbiperfgroupindex VALUES (4, 14, '1.3.6.1.4.1.32285.12.2.3.9.10.1.1', 'delay', '响应延时',5,1);
INSERT INTO nbiperfgroupindex VALUES (5, 15, '1.3.6.1.4.1.32285.12.2.3.9.5.1.1', 'portInSpeed', '入方向速率',5,1);
INSERT INTO nbiperfgroupindex VALUES (5, 16, '1.3.6.1.4.1.32285.12.2.3.9.5.1.2', 'portOutSpeed', '出方向速率',5,1);
INSERT INTO nbiperfgroupindex VALUES (6, 17, '1.3.6.1.4.1.32285.12.2.3.9.1.1.1', 'onuPonTransPower', '光口发送功率',5,1);
INSERT INTO nbiperfgroupindex VALUES (6, 18, '1.3.6.1.4.1.32285.12.2.3.9.1.1.2', 'onuPonRevPower', '光口接收功率',5,1);
INSERT INTO nbiperfgroupindex VALUES (6, 19, '1.3.6.1.4.1.32285.12.2.3.9.1.1.3', 'oltPonRevPower', '基于LLID接收功率',5,1);             
/**-- version 2.6.8.0,build 2016-03-27,module epon*/

-- version 2.6.8.7,build 2016-03-31,module epon
update perfthresholdrule set thresholds = '1_65_4_4' where targetId = 'OLT_BOARD_TEMP' and templateId = 1 and thresholds = '1_85_3_4';
update perfthresholdrule set thresholds = '5_40_4_4' where targetId = 'OLT_FAN_SPEED' and templateId = 1 and thresholds = '5_25_4_4';
update perfthresholdrule set thresholds = '5_-9_4_4#1_3_3_5' where targetId = 'OLT_SNI_TX_POWER' and templateId = 1 and thresholds = '5_-9_3_4#1_3_4_5';
update perfthresholdrule set thresholds = '1_70_4_5#1_85_5_6' where targetId = 'OLT_SNI_IN_USED' and templateId = 1 and thresholds = '1_50_2_4#1_70_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '1_70_4_5#1_85_5_6' where targetId = 'OLT_PON_IN_USED' and templateId = 1 and thresholds = '1_50_2_4#1_70_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '1_70_4_5#1_85_5_6' where targetId = 'OLT_SNI_OUT_USED' and templateId = 1 and thresholds = '1_50_2_4#1_70_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '1_70_4_5#1_85_5_6' where targetId = 'OLT_PON_OUT_USED' and templateId = 1 and thresholds = '1_50_2_4#1_70_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '5_0_2_4#1_65_3_5' where targetId = 'OLT_PON_OPT_TEMP' and templateId = 1 and thresholds = '5_0_2_4#1_90_3_5';
update perfthresholdrule set thresholds = '5_0_2_4#1_65_3_5' where targetId = 'OLT_SNI_OPT_TEMP' and templateId = 1 and thresholds = '5_0_2_4#1_90_3_5';
INSERT INTO perfthresholdtemplate(templateId,templateName, templateType, createUser,isDefaultTemplate, createTime, parentType) VALUES
	(1000, 'OLT_PN8601_TEMPLATE', 10001, 'system', 1, sysdate(), 10000);
INSERT INTO perfthresholdrule (targetId, templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('OLT_CONNECTIVITY', 1000, '1_1000_5_5', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_SNI_IN_USED', 1000, '1_70_4_5#1_85_5_6', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_PON_IN_USED', 1000, '1_70_4_5#1_85_5_6', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_SNI_OUT_USED', 1000, '1_70_4_5#1_85_5_6', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_PON_OUT_USED', 1000, '1_70_4_5#1_85_5_6', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_CPU_USED', 1000, '1_80_3_4#1_85_4_5', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_FLASH_USED', 1000, '1_90_3_4', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_MEM_USED', 1000, '1_90_3_4#1_95_4_5', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_FAN_SPEED', 1000, '5_15_4_1', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_BOARD_TEMP', 1000, '1_65_4_4', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_PON_TX_POWER', 1000, '1_8_3_4#5_2_4_5', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_PON_OPT_TEMP', 1000, '5_0_2_4#1_65_3_5', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_SNI_RE_POWER', 1000, '5_-24_3_4#1_-3_4_5', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_SNI_TX_POWER', 1000, '5_-9_4_4#1_3_3_5', 1, 1, 1, '00:00-23:59#1234567'),
	('OLT_SNI_OPT_TEMP', 1000, '5_0_2_4#1_65_3_5', 1, 1, 1, '00:00-23:59#1234567');
/* -- version 2.6.8.7,build 2016-03-31,module epon  */

-- version 2.6.9.4,build 2016-07-15,module epon
INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
     (6145,  -6, 'TOTAL_ARP_LEARN_NUM_HIGH', 'DB.alertType.a6145', 3, '', 0, 0, '0', '1', '', '');
INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
     (6145,6145,'EPON',1),
     (106145,6145,'EPON',2),
     (6145,6145,'EPON',3);
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
  (6145,  -7, 'TOTAL_IPV6_NBR_NUM_HIGH', 'DB.eventType.e6145'),
  (53260, -7, 'TOTAL_IPV6_NBR_NUM_NORMAL', 'DB.eventType.e53260'); 
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES 
(6145,6145,1),
(53260,6145,0);
/* -- version 2.6.9.4,build 2016-07-15,module epon  */
	
-- version 2.7.1.0,build 2016-05-30,module epon	
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_CATV_RX_POWER', 13000, 'Performance.onuCatvRxPower','Performance.onuCatv','dBm',100, 0, 1, 0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_CATV_RF', 13000, 'Performance.onuCatvRf','Performance.onuCatv','dBuV', 100, 0, 1, 0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_CATV_VOLTAGE', 13000, 'Performance.onuCatvVoltage','Performance.onuCatv','V', 100, 0, 1, 0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('ONU_CATV_TEMP', 13000, 'Performance.onuCatvTemp','Performance.onuCatv','℃', 100, 0, 1, 0);
insert into perftargetCategory select 13000,'onuCatvInfo','onuCatvPerf';

INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('ONU_CATV_RX_POWER', 3,'1_0_3_4#5_-15_3_5',1,1,1,'00:00-23:59#1234567'),
	('ONU_CATV_RF', 3,'1_80_3_4#5_60_3_5',1,1,1,'00:00-23:59#1234567'),
	('ONU_CATV_VOLTAGE', 3,'1_13_3_4#5_11_3_5',1,1,1,'0:00-23:59#1234567'),
	('ONU_CATV_TEMP', 3,'1_70_3_4#5_-10_3_5',1,1,1,'0:00-23:59#1234567');
insert into deviceperftarget VALUES 
		('onuCatvInfo', 13000, 13000, 13000, 'onuCatv', 5);
		
INSERT INTO globalperfcollecttime VALUES
		('onuCatvInfo', 13000, 13000, 240, 0, 'onuCatv', 5);
	       
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
    (-780,-50001,'OnuCatvRxPowerEvent','DB.eventType.e780',''),
    (-781,-50001,'OnuCatvRxPowerEvent','DB.eventType.e781',''),
    (-782,-50001,'OnuCatvRfEvent','DB.eventType.e782',''),
    (-783,-50001,'OnuCatvRfEvent','DB.eventType.e783',''),
    (-784,-50001,'OnuCatvTempEvent','DB.eventType.e784',''),
    (-785,-50001,'OnuCatvTempEvent','DB.eventType.e785',''),
    (-786,-50001,'OnuCatvVoltageEvent','DB.eventType.e786',''),
    (-787,-50001,'OnuCatvVoltageEvent','DB.eventType.e787','');
    
    
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (-781,-49998,'OnuCatvRxPowerAlert','DB.alertType.a781',2,'',0,0,'0','1','',''),
    (-783,-49998,'OnuCatvRfAlert','DB.alertType.a783',2,'',0,0,'0','1','',''),
    (-785,-49998,'OnuCatvTempAlert','DB.alertType.a785',2,'',0,0,'0','1','',''),
    (-787,-49998,'OnuCatvVoltageAlert','DB.alertType.a787',2,'',0,0,'0','1','','');
    
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
    (-780,-781,0),
    (-781,-781,1),
    (-782,-783,0),
    (-783,-783,1),
    (-784,-785,0),
    (-785,-785,1),	
    (-786,-787,0),
    (-787,-787,1);		
    
insert into historytablename values('perfonucatvquality');

INSERT INTO eponeventtyperelation(deviceEventTypeId, emsEventTypeId, module, type) VALUES
	(993, 993, 'EPON', 2),
	(22273, 993, 'EPON', 1),
	(22273, 993, 'EPON', 3),
	(994, 994, 'EPON', 2),
	(22275, 994, 'EPON', 1),
	(22275, 994, 'EPON', 3),
	(995, 995, 'EPON', 2),
	(22277, 995, 'EPON', 1),
	(22277, 995, 'EPON', 3),
	(996, 996, 'EPON', 2),
	(22279, 996, 'EPON', 1),
	(22279, 996, 'EPON', 3),
	(997, 997, 'EPON', 2),
	(22281, 997, 'EPON', 1),
	(22281, 997, 'EPON', 3),
	(998, 998, 'EPON', 2),
	(22283, 998, 'EPON', 1),
	(22283, 998, 'EPON', 3),
	(999, 999, 'EPON', 2),
	(22285, 999, 'EPON', 1),
	(22285, 999, 'EPON', 3),
	(1000, 1000, 'EPON', 2),
	(22287, 1000, 'EPON', 1),
	(22287, 1000, 'EPON', 3);
	
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
    (993,-5,'E_ALM_ONU_CATV_TEMP_HIGH','DB.eventType.e993',''),
    (994,-5,'E_ALM_ONU_CATV_TEMP_LOW','DB.eventType.e994',''),
    (995,-5,'E_ALM_ONU_CATV_RXPWR_HIGH','DB.eventType.e995',''),
    (996,-5,'E_ALM_ONU_CATV_RXPWR_LOW','DB.eventType.e996',''),
    (997,-5,'E_ALM_ONU_CATV_OUT_LEVEL_HIGH','DB.eventType.e997',''),
    (998,-5,'E_ALM_ONU_CATV_OUT_LEVEL_LOW','DB.eventType.e998',''),
    (999,-5,'E_ALM_ONU_CATV_VCC_HIGH','DB.eventType.e999',''),
    (1000,-5,'E_ALM_ONU_CATV_VCC_LOW','DB.eventType.e1000',''),  
    (53400,-5,'E_ALM_ONU_CATV_TEMP_NORMAL','DB.eventType.e53400',''),
    (53401,-5,'E_ALM_ONU_CATV_RXPWR_NORMAL','DB.eventType.e53401',''),
    (53402,-5,'E_ALM_ONU_CATV_OUT_LEVEL_NORMAL','DB.eventType.e53402',''),
    (53403,-5,'E_ALM_ONU_CATV_VCC_NORMAL','DB.eventType.e53403','');

INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (993,-5,'E_ALM_ONU_CATV_TEMP_HIGH','DB.alertType.a993',3,'',0,0,'0','1','',''),
    (994,-5,'E_ALM_ONU_CATV_TEMP_LOW','DB.alertType.a994',3,'',0,0,'0','1','',''),
    (995,-5,'E_ALM_ONU_CATV_RXPWR_HIGH','DB.alertType.a995',3,'',0,0,'0','1','',''),
    (996,-5,'E_ALM_ONU_CATV_RXPWR_LOW','DB.alertType.a996',3,'',0,0,'0','1','',''),
    (997,-5,'E_ALM_ONU_CATV_OUT_LEVEL_HIGH','DB.alertType.a997',3,'',0,0,'0','1','',''),
    (998,-5,'E_ALM_ONU_CATV_OUT_LEVEL_LOW','DB.alertType.a998',3,'',0,0,'0','1','',''),
    (999,-5,'E_ALM_ONU_CATV_VCC_HIGH','DB.alertType.a999',3,'',0,0,'0','1','',''),
    (1000,-5,'E_ALM_ONU_CATV_VCC_LOW','DB.alertType.a1000',3,'',0,0,'0','1','','');

INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
    (993,993,1),
    (994,994,1),
    (53400,993,0),
    (53400,994,0),
    (995,995,1),
    (996,996,1),
    (53401,995,0),
    (53401,996,0),
    (997,997,1),
    (998,998,1),
    (53402,997,0),
    (53402,998,0),
    (999,999,1),
    (1000,1000,1),
    (53403,999,0),
    (53403,1000,0);
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId) VALUES
                (38,  'pn8626', 'PN8626', 'onu', '/epon',  '', 'network/onu/8626Icon_16.png', 'network/onu/8626Icon_32.png', 'network/onu/8626Icon_48.png', 'network/onu/8626Icon_64.png', 1);
insert into entitytyperelation (type, typeId) values 
	(1,38),
	(3,38),
	(13000,38),
	(130000,38);    

update EntityType set icon16 = 'network/onu/8625Icon_16.png',icon32='network/onu/8625Icon_32.png',icon48='network/onu/8625Icon_48.png',icon64='network/onu/8625Icon_64.png' where typeId = 37;
/* -- version 2.7.1.0,build 2016-05-30,module epon  */
	
-- version 2.7.1.0,build 2016-06-22,module epon
update perfthresholdrule set thresholds = '1_85_4_5#1_80_3_4' where targetId = 'OLT_CPU_USED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set thresholds = '1_95_4_5' where targetId = 'OLT_MEM_USED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set thresholds = '1_70_4_5#1_65_3_4#5_0_2_3' where targetId = 'OLT_BOARD_TEMP' and (templateId = 1 or templateId = 1000);
delete from perfthresholdrule where targetId = 'OLT_FAN_SPEED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set thresholds = '1_7_4_5#5_2.5_4_4' where targetId = 'OLT_PON_TX_POWER' and (templateId = 1 or templateId = 1000) ;
delete from perfthresholdrule where targetId = 'OLT_SNI_RE_POWER' and (templateId = 1 or templateId = 1000);
delete from perfthresholdrule where targetId = 'OLT_SNI_TX_POWER' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set thresholds = '1_70_4_5#1_65_3_4#5_0_2_3' where targetId = 'OLT_PON_OPT_TEMP' and (templateId = 1 or templateId = 1000) ;
update perfthresholdrule set thresholds = '1_70_4_5#1_65_3_4#5_0_2_3' where targetId = 'OLT_SNI_OPT_TEMP' and (templateId = 1 or templateId = 1000) ;
update perfthresholdrule set thresholds = '1_85_3_5#1_70_2_4' where targetId = 'OLT_SNI_IN_USED' and (templateId = 1 or templateId = 1000) ;
update perfthresholdrule set thresholds = '1_85_3_5#1_70_2_4' where targetId = 'OLT_PON_IN_USED' and (templateId = 1 or templateId = 1000) ;
update perfthresholdrule set thresholds = '1_85_3_5#1_70_2_4' where targetId = 'OLT_SNI_OUT_USED' and (templateId = 1 or templateId = 1000) ;
update perfthresholdrule set thresholds = '1_85_3_5#1_70_2_4' where targetId = 'OLT_PON_OUT_USED' and (templateId = 1 or templateId = 1000) ;

delete from perfthresholdrule where targetId = 'ONU_PON_IN_SPEED' and templateId = 3;
delete from perfthresholdrule where targetId = 'ONU_PON_OUT_SPEED' and templateId = 3;
delete from perfthresholdrule where targetId = 'ONU_UNI_IN_SPEED' and templateId = 3;
delete from perfthresholdrule where targetId = 'ONU_UNI_OUT_SPEED' and templateId = 3;
update perfthresholdrule set thresholds = '1_-5_3_5#5_-25_3_4' where targetId = 'ONU_PON_RE_POWER' and templateId = 3 ;
update perfthresholdrule set thresholds = '1_4_3_5#5_0_3_4' where targetId = 'ONU_PON_TX_POWER' and templateId = 3 ;
update perfthresholdrule set thresholds = '1_-7_3_5#5_-27_3_4' where targetId = 'OLT_PONLLID_RE_POWER' and templateId = 3;

update perfthresholdrule set clearRules = '5_500' where targetId = 'OLT_CONNECTIVITY' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_75#5_60' where targetId = 'OLT_SNI_IN_USED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_75#5_60' where targetId = 'OLT_PON_IN_USED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_75#5_60' where targetId = 'OLT_SNI_OUT_USED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_75#5_60' where targetId = 'OLT_PON_OUT_USED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_75#5_70' where targetId = 'OLT_CPU_USED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_80' where targetId = 'OLT_FLASH_USED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_90' where targetId = 'OLT_MEM_USED' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_65#5_60#1_5' where targetId = 'OLT_BOARD_TEMP' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_6.5#1_3' where targetId = 'OLT_PON_TX_POWER' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_65#5_60#1_10' where targetId = 'OLT_PON_OPT_TEMP' and (templateId = 1 or templateId = 1000);
update perfthresholdrule set clearRules = '5_65#5_55#1_10' where targetId = 'OLT_SNI_OPT_TEMP' and (templateId = 1 or templateId = 1000);

update perfthresholdrule set clearRules = '5_-6#1_-24' where targetId = 'ONU_PON_RE_POWER' and templateId = 3;
update perfthresholdrule set clearRules = '5_3.5#1_0.5' where targetId = 'ONU_PON_TX_POWER' and templateId = 3;
update perfthresholdrule set clearRules = '5_-9#1_-25' where targetId = 'OLT_PONLLID_RE_POWER' and templateId = 3;

/* -- version 2.7.1.0,build 2016-06-22,module epon  */

-- version 2.7.2.0,build 2016-07-22,module epon
update perfTarget set maxNum = -5   where targetId = 'ONU_PON_RE_POWER'; 
update perfTarget set minNum = 0  where targetId = 'ONU_PON_TX_POWER';
/* -- version 2.7.2.0,build 2016-07-22,module epon  */

-- version 2.7.2.0,build 2016-08-06,module epon
update perfthresholdrule set clearRules = '5_-1#1_-14' where targetId = 'ONU_CATV_RX_POWER'; 
update perfthresholdrule set clearRules = '5_17#1_3'  where targetId = 'ONU_CATV_RF';
update perfthresholdrule set clearRules = '5_12.5#1_11.5'  where targetId = 'ONU_CATV_VOLTAGE';
update perfthresholdrule set clearRules = '5_70#1_-10'  where targetId = 'ONU_CATV_TEMP';
update perfTarget set maxNum = 10,minNum = -20   where targetId = 'ONU_CATV_RX_POWER'; 
update perfTarget set maxNum = 120,minNum = -40 where targetId = 'ONU_CATV_TEMP';
update perfTarget set regexpValue = 1 where targetId = 'ONU_CATV_RF'; 
update perfTarget set regexpValue = 1 where targetId = 'ONU_CATV_VOLTAGE';
/* -- version 2.7.2.0,build 2016-08-06,module epon  */

-- version 2.7.5.0,build 2016-09-03,module epon
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId,disabled) VALUES
      (10005,  'pn8602-E', 'PN8602-E', 'olt', '/epon/8602-E',  '1.3.6.1.4.1.32285.11.2.1.2.1', 'network/pn8602-E_16.png', 'network/pn8602-E_32.png', 'network/pn8602-E_48.png', 'network/pn8602-E_64.png', 32285,0);
insert into entitytyperelation (type, typeId) values (1,10005);
insert into entitytyperelation (type, typeId) values (10000,10005);
insert into entitytyperelation (type, typeId) values (50000,10005);
insert into entitytyperelation (type, typeId) values (10005,10005);
insert into entitytyperelation (type, typeId) values (130000,10005);
insert into entitytyperelation (type, typeId) values (3,10005);
INSERT INTO batchautodiscoveryentitytype(typeId) values(10005);
insert into EntityTypeRelation values(3, 10005);

insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 
			('olt_onlineStatus', 10000, 10000, 10005, 'olt_deviceStatus', 1),
			('olt_cpuUsed', 10000, 10000, 10005, 'olt_service', 2),
			('olt_memUsed', 10000, 10000, 10005, 'olt_service', 2),
			('olt_flashUsed', 10000, 10000, 10005, 'olt_service', 2),
			('olt_boardTemp', 10000, 10000, 10005, 'olt_service', 2),
			('olt_fanSpeed', 10000, 10000, 10005, 'olt_service', 2),
			('olt_optLink', 10000, 10000, 10005, 'olt_service', 2),
			('olt_sniFlow', 10000, 10000, 10005, 'olt_flow', 4),
			('olt_ponFlow', 10000, 10000, 10005, 'olt_flow', 4);
			
update entitytype set discoveryBean='topvisionOltDiscoveryService' where typeId = 10005;

update entitytype set sysObjectID='' where typeId = 10000;
/* -- version 2.7.5.0,build 2016-09-03,module epon  */

-- version 2.8.0.0,build 2016-09-17,module epon
delete from Event2Alert where alertTypeId = -721;
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
    (-720,-721,0),
    (-721,-721,1);
/* -- version 2.8.0.0,build 2016-09-17,module epon  */
    
-- version 2.8.0.0,build 2016-12-1,module epon
delete from AlertType where typeId = 16385;
delete from eventType where typeId = 16385;
delete from event2alert where eventTypeId = 16385;
/* -- version 2.8.0.0,build 2016-12-1,module epon  */

-- version 2.8.0.0,build 2017-1-15,module epon
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
    (262,262,1);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (262,-6,'ONU_ILLEGALREGISTER','DB.alertType.a262',2,'',0,0,'0','1','','');
/* -- version 2.8.0.0,build 2017-1-15,module epon  */
    
-- version 2.8.0.0,build 2017-02-27,module epon
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId,disabled) VALUES
      (10006,  'pn8602-EF', 'PN8602-EF', 'olt', '/epon/8602-EF',  '1.3.6.1.4.1.32285.11.2.1.2.2', 'network/pn8602EF_16.png', 'network/pn8602EF_32.png', 'network/pn8602EF_48.png', 'network/pn8602EF_64.png', 32285,0);
insert into entitytyperelation (type, typeId) values (1,10006);
insert into entitytyperelation (type, typeId) values (10000,10006);
insert into entitytyperelation (type, typeId) values (50000,10006);
insert into entitytyperelation (type, typeId) values (10006,10006);
insert into entitytyperelation (type, typeId) values (130000,10006);
insert into entitytyperelation (type, typeId) values (3,10006);
INSERT INTO batchautodiscoveryentitytype(typeId) values(10006);
insert into EntityTypeRelation values(3, 10006);

insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 
			('olt_onlineStatus', 10000, 10000, 10006, 'olt_deviceStatus', 1),
			('olt_cpuUsed', 10000, 10000, 10006, 'olt_service', 2),
			('olt_memUsed', 10000, 10000, 10006, 'olt_service', 2),
			('olt_flashUsed', 10000, 10000, 10006, 'olt_service', 2),
			('olt_boardTemp', 10000, 10000, 10006, 'olt_service', 2),
			('olt_fanSpeed', 10000, 10000, 10006, 'olt_service', 2),
			('olt_optLink', 10000, 10000, 10006, 'olt_service', 2),
			('olt_sniFlow', 10000, 10000, 10006, 'olt_flow', 4),
			('olt_ponFlow', 10000, 10000, 10006, 'olt_flow', 4);
			
update entitytype set discoveryBean='topvisionOltDiscoveryService' where typeId = 10006;
/* -- version 2.8.0.0,build 2017-02-27,module epon  */

-- version 2.9.0.5,build 2017-5-6,module epon
delete from AlertType where typeId = 16423;
delete from AlertType where typeId = 16424;
update alerttype set category = -6 where typeId = 16388;
/* -- version 2.9.0.5,build 2017-5-6,module epon  */

-- version 2.9.0.5,build 2017-5-15,module epon
update EponEventTypeRelation set deviceEventTypeId = 322 where deviceEventTypeId = 116437 and emsEventTypeId = 16437;
/* -- version 2.9.0.5,build 2017-5-15,module epon  */

-- version 2.9.0.5,build 2017-06-3,module epon
INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
     (4132,  -6, 'OLT_SHELL_OPEN', 'DB.alertType.a4132', 4, '', 0, 0, '0', '1', '', '');
INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
     (4132,4132,'EPON',1),
     (104132,4132,'EPON',2),
     (4132,4132,'EPON',3);
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
  (4132,  -7, 'OLT_SHELL_OPEN', 'DB.eventType.e4132'),
  (53261, -7, 'OLT_SHELL_CLOSE', 'DB.eventType.e53261'); 
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES 
(4132,4132,1),
(53261,4132,0);
/* -- version 2.9.0.5,build 2017-06-3,module epon  */

-- version 2.8.0.0,build 2017-7-6,module epon
INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
     (260,  -6, 'E_ALM_BD_OFFLINE', 'DB.alertType.a260', 3, '', 0, 0, '0', '1', '', '');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
    (260,260,1),
    (259,260,0);
/* -- version 2.8.0.0,build 2017-7-6,module epon  */

-- version 2.9.0.5,build 2017-6-19,module epon
delete from eventType where typeId = 16437;
delete from alertType where typeId = 16437;
delete from EponEventTypeRelation where emsEventTypeId = 16437;
delete from event2alert where alerttypeId = 16437;

delete from eventType where typeId = 12299;
delete from alertType where typeId = 12299;
delete from EponEventTypeRelation where emsEventTypeId = 12299;
delete from event2alert where alerttypeId = 12299;

delete from eventType where typeId = 53262;
delete from eventType where typeId = 53263;

INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
     (12299,  -6, 'PORT_PON_ROGUEONU', 'DB.alertType.a12299', 5, '', 0, 0, '0', '1', '', '');
INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
     (16437,  -5, 'ONU_PON_ROGUE', 'DB.alertType.a16437', 4, '', 0, 0, '0', '1', '', '');
INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
     (12299,12299,'EPON',1),
     (206,12299,'EPON',2),
     (12299,12299,'EPON',3),
     (16437,16437,'EPON',1),
     (322,16437,'EPON',2),
     (16437,16437,'EPON',3);
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
  (12299,  -7, 'PORT_PON_ROGUEONU', 'DB.eventType.e12299'),
  (53262, -7, 'PORT_PON_ROGUEONU_CLEAR', 'DB.eventType.e53262'),
  (16437,  -7, 'ONU_PON_ROGUE', 'DB.eventType.e16437'),
  (53263, -7, 'ONU_PON_ROGUE_CLEAR', 'DB.eventType.e53263'); 
  
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES 
(12299,12299,1),
(53262,12299,0),
(16437,16437,1),
(53263,16437,0);
/* -- version 2.9.0.5,build 2017-6-19,module epon  */
    
-- version 2.9.1.7,build 2017-8-7,module epon
update oltonuattribute set onuUniqueIdentification = onuMac;

UPDATE entitytype SET displayName = SUBSTRING(displayName, 3) WHERE module = 'onu' AND LEFT(displayName, 2) = 'PN';
/* -- version 2.9.1.7,build 2017-8-7,module epon  */

-- version 2.9.1.8,build 2017-10-16,module epon
INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
        (4133,4133,'EPON',1),
        (104133,4133,'EPON',2),
        (4133,4133,'EPON',3),
        (4134,4134,'EPON',1),
        (104134,4134,'EPON',2),
        (4134,4134,'EPON',3),
        (4135,4135,'EPON',1),
        (104135,4135,'EPON',2),
        (4135,4135,'EPON',3),
        (4136,4136,'EPON',1),
        (104136,4136,'EPON',2),
        (4136,4136,'EPON',3),
        (4137,4137,'EPON',1),
        (104137,4137,'EPON',2),
        (4137,4137,'EPON',3),
        (4138,4138,'EPON',1),
        (104138,4138,'EPON',2),
        (4138,4138,'EPON',3),
        (4139,4139,'EPON',1),
        (104139,4139,'EPON',2),
        (4139,4139,'EPON',3);
        
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
        (4133,  -7, 'E_ALM_OFA_INPUT', 'DB.eventType.e4133'),
        (54400,  -7, 'E_ALM_OFA_INPUT_CLEAR', 'DB.eventType.e54400'),
        (4134,  -7, 'E_ALM_OFA_OUTPUT', 'DB.eventType.e4134'),
        (54401,  -7, 'E_ALM_OFA_OUTPUT_CLEAR', 'DB.eventType.e54401'),
        (4135,  -7, 'E_ALM_OFA_PUMP_CUR', 'DB.eventType.e4135'),
        (54402,  -7, 'E_ALM_OFA_PUMP_CUR_CLEAR', 'DB.eventType.e54402'),
        (4136,  -7, 'E_ALM_OFA_PUMP_TEMP', 'DB.eventType.e4136'),
        (54403,  -7, 'E_ALM_OFA_PUMP_TEMP_CLEAR', 'DB.eventType.e54403'),
        (4137,  -7, 'E_ALM_OFA_PUMP_TEC', 'DB.eventType.e4137'),
        (54404,  -7, 'E_ALM_OFA_PUMP_TEC_CLEAR', 'DB.eventType.e54404'),
        (4138,  -7, 'E_ALM_OFA_VOLT5V', 'DB.eventType.e4138'),
        (54405,  -7, 'E_ALM_OFA_VOLT5V_CLEAR', 'DB.eventType.e54405'),
        (4139,  -7, 'E_ALM_OFA_VOLT12V', 'DB.eventType.e4139'),
        (54406,  -7, 'E_ALM_OFA_VOLT12V_CLEAR', 'DB.eventType.e54406');

INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
        (4133, 4133, 1),
        (54400, 4133, 0),
        (4134, 4134, 1),
        (54401, 4134, 0),
        (4135, 4135, 1),
        (54402, 4135, 0),
        (4136, 4136, 1),
        (54403, 4136, 0),
        (4137, 4137, 1),
        (54404, 4137, 0),
        (4138, 4138, 1),
        (54405, 4138, 0),
        (4139, 4139, 1),
        (54406, 4139, 0);
        
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
        (4133,  -6, 'E_ALM_OFA_INPUT', 'DB.alertType.a4133', 5, '', 0, 0, '0', '1', '', ''),
        (4134,  -6, 'E_ALM_OFA_OUTPUT', 'DB.alertType.a4134', 5, '', 0, 0, '0', '1', '', ''),
        (4135,  -6, 'E_ALM_OFA_PUMP_CUR', 'DB.alertType.a4135', 5, '', 0, 0, '0', '1', '', ''),
        (4136,  -6, 'E_ALM_OFA_PUMP_TEMP', 'DB.alertType.a4136', 5, '', 0, 0, '0', '1', '', ''),
        (4137,  -6, 'E_ALM_OFA_PUMP_TEC', 'DB.alertType.a4137', 5, '', 0, 0, '0', '1', '', ''),
        (4138,  -6, 'E_ALM_OFA_VOLT5V', 'DB.alertType.a4138', 5, '', 0, 0, '0', '1', '', ''),
        (4139,  -6, 'E_ALM_OFA_VOLT12V', 'DB.alertType.a4139', 5, '', 0, 0, '0', '1', '', '');
/* -- version 2.9.1.8,build 2017-10-16,module epon */
        

-- version 2.10.0.4,build 2018-1-25,module epon
INSERT INTO EponEventTypeRelation(deviceEventTypeId,emsEventTypeId,module,type) VALUES
        (1025,16434,'EPON',1),
        (101025,16434,'EPON',2),
        (1025,16434,'EPON',3),
        (1027,16433,'EPON',1),
        (101027,16433,'EPON',2),
        (1027,16433,'EPON',3);
/* -- version 2.10.0.4,build 2018-1-25,module epon  */
        
        
-- version 2.10.0.4,build 2018-03-12,module epon
INSERT INTO EntityType(typeId, name, displayName, module, modulePath, sysObjectID, icon16, icon32, icon48, icon64, corpId,disabled) VALUES
      (10007,  'pn8602-G', 'PN8602-G', 'olt', '/epon/8602-G',  '1.3.6.1.4.1.32285.11.2.1.2.3', 'network/pn8602-G_16.png', 'network/pn8602-G_32.png', 'network/pn8602-G_48.png', 'network/pn8602-G_64.png', 32285,0);
insert into entitytyperelation (type, typeId) values (1,10007);
insert into entitytyperelation (type, typeId) values (10000,10007);
insert into entitytyperelation (type, typeId) values (50000,10007);
insert into entitytyperelation (type, typeId) values (10007,10007);
insert into entitytyperelation (type, typeId) values (130000,10007);
insert into entitytyperelation (type, typeId) values (3,10007);
INSERT INTO batchautodiscoveryentitytype(typeId) values(10007);
insert into EntityTypeRelation values(3, 10007);

insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 
			('olt_onlineStatus', 10000, 10000, 10007, 'olt_deviceStatus', 1),
			('olt_cpuUsed', 10000, 10000, 10007, 'olt_service', 2),
			('olt_memUsed', 10000, 10000, 10007, 'olt_service', 2),
			('olt_flashUsed', 10000, 10000, 10007, 'olt_service', 2),
			('olt_boardTemp', 10000, 10000, 10007, 'olt_service', 2),
			('olt_fanSpeed', 10000, 10000, 10007, 'olt_service', 2),
			('olt_optLink', 10000, 10000, 10007, 'olt_service', 2),
			('olt_sniFlow', 10000, 10000, 10007, 'olt_flow', 4),
			('olt_ponFlow', 10000, 10000, 10007, 'olt_flow', 4);
			
update entitytype set discoveryBean='topvisionOltDiscoveryService' where typeId = 10007;
/* -- version 2.10.0.4,build 2018-03-12,module epon  */

