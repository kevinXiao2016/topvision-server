/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - platform
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-2-22,module platform
INSERT INTO UserGroup(userGroupId,name,description,createTime) values 
		(1,'admin group','USER.hasresourcepower','2009-10-11 11:56:10');

INSERT INTO Role(roleId,name,note,superiorId) values
		(1,'super admin','super admin',0),
		(2,'system admin','system admin',0);

/* superadmin帐号可以访问admin/dd.tv，密码为topnm3000 */
INSERT INTO Users (userId, userName, passwd, status, familyName, firstName, createTime, ipLoginActive, userGroupId) values
		(1, 'superadmin', '150A25A91AE05CFEE6FBC3962FD9EF98', 1, 'super admin', '', now(), 1, 1),
    	(2, 'admin', '', 1, 'system admin', '', now(), 1, 1);

INSERT INTO UserRoleRela(userId,roleId) values
 		(1,1),
 		(2,2);

INSERT INTO SystemPreferences(name,value,module) values
 		('used.firstly','true','core'),
 		('menubar','true','core'),
 		('applet','true','applet'),
 		('Theme.office2007','office2007','theme'),
 		('Theme.office2003','office2003','theme'),
 		('Theme.Vista','vista','theme'),
 		('Theme.Apple','apple','theme'),
 		('Theme.simple','default','theme');
 		
INSERT INTO SystemPreferences(name,value,module) values
 		('allowIpBindLogon','false','logon'),
 		('allowRepeatedlyLogon','true','logon'),
 		('stopUserWhenErrorNumber','3','logon'),
 		('stopUserWhenErrors','true','logon'),
 		('checkPasswdComplex','true','logon');

INSERT INTO ImageDirectory(directoryId,superiorId,name,path,module) values
		(10,0,'topo.grap.imageChooser.default','.','default'),
 		(1000,0,'topo.grap.imageChooser.node','network','network'),
		(1100,1000,'topo.grap.imageChooser.port','network/port','port'),
		(1200,1000,'topo.grap.imageChooser.statusIcon','network/state','state'),
 		(2000,0,'topo.grap.imageChooser.bgImage','background','background'),
 		(9000,0,'topo.grap.imageChooser.other','zgraph','other');

INSERT INTO NavigationButton(naviId,name,displayName,seq,icon16,icon24,action) values
        (1000000,'workbench','MODULE.workbench',0,'images/workbench/bt.gif','images/workbench/bb.gif','/workbench/showMenuJSP.tv'),
        (2000000,'network','MODULE.network',0,'images/network/bt.gif','images/network/bb.gif','network/menu.jsp'),
        (3000000,'topographic','MODULE.topographic',0,'images/silk/world.png','images/silk/world.png','topo/menu.jsp'),
        (4000000,'config','MODULE.config',0,'images/config/bt.gif','images/config/bb.gif','config/menu.jsp'),
        (5000000,'performance','MODULE.performance',0,'images/performance/bt.gif','images/performance/bb.gif','performance/menu.jsp'),
        (6000000,'fault','MODULE.fault',0,'images/fault/bt.gif','images/fault/bb.gif','fault/menu.jsp'),
        (7000000,'report','MODULE.report',0,'images/report/bt.gif','images/report/bb.gif','report/menu.jsp'),
        (8000000,'system','MODULE.system',0,'images/system/bt.gif','images/system/bb.gif','system/menu.jsp');

--INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
--                (1000000,'workbench',0,'MODULE.workbench'),
--                (2000000,'config',0,'MODULE.config'),
--                (3000000,'network',0,'MODULE.network'),
--                (3000001,'topoDiscovery',3000000,'MODULE.topoDiscovery'),
--                (3000002,'topoEdit',3000000,'MODULE.topoEdit'),
--                (3000003,'editDevice',3000000,'MODULE.editDevice'),
--                (3000004,'deleteDevice',3000000,'MODULE.deleteDevice'),
--                (4000000,'event',0,'MODULE.event'),
--                (4000001,'confirmAlert',4000000,'MODULE.confirmAlert'),
--                (4000002,'clearAlert',4000000,'MODULE.clearAlert'),
--                (4000003,'alertConfig',4000000,'MODULE.alertConfig'),
--                (5000000,'performance',0,'MODULE.performance'),
--                (6000000,'report',0,'MODULE.report'),
--                (6000001,'reportTask',6000000,'MODULE.reportTask'),
--                (7000000,'system',0,'MODULE.system'),
--                (7000001,'roleManagement',7000000,'MODULE.roleManagement'),
--                (7000002,'userManagement',7000000,'MODULE.userManagement'),
--                (7000003,'logManagement',7000000,'MODULE.logManagement'),
--                (7000004,'departmentManagement',7000000,'MODULE.departmentManagement'),
--                (7000005,'postManagement',7000000,'MODULE.postManagement'),
--                (7000006,'mailManagement',7000000,'MODULE.mailManagement'),
--                (8000000,'topographic',0,'MODULE.topographic'),
--                (8000001,'googleMap',8000000,'MODULE.googleMap');

--INSERT INTO RoleFunctionRela(roleId,functionId) values
--                (1,7000000),
--                (1,7000001),
--                (1,7000002),
--                (1,7000003),
--                (1,7000004),
--                (1,7000005),
--                (1,7000006),
--                (2,1000000),
--                (2,2000000),
--                (2,3000000),
--                (2,3000001),
--                (2,3000002),
--                (2,3000003),
--                (2,3000004),
--                (2,4000000),
--                (2,4000001),
--                (2,4000002),
--                (2,4000003),
--                (2,5000000),
--                (2,6000000),
--                (2,6000001),
--                (2,7000000),
--                (2,7000001),
--                (2,7000002),
--                (2,7000003),
--                (2,7000004),
--                (2,7000005),
--                (2,7000006),
--                (2,8000000),
--                (2,8000001);

INSERT INTO MenuItem(itemId,parentId,name,mnemonic,action,icon,target) values
                (1,0,'file','F',null,null,null),
--                (4,0,'edit','E',null,null,null),
                (7,0,'view','V',null,null,null),
                (10,0,'go','G',null,null,null),
                (15,0,'tool','T',null,null,null),
                (18,0,'window','W',null,null,null),
                (20,0,'help','H',null,null,null);
                
INSERT INTO RoleMenuRela(roleId,itemId) values
                (1,1),
 --               (1,4),
                (1,7),
                (1,10),
                (1,15),
                (1,18),
                (1,20);

INSERT INTO MenuItem(itemId,parentId,name,type,action,icon,target) values
--                (103,1,'toplogyguide',1,'javascript: startTopoDiscovery()',null,null),
--                (105,1,'',0,null,null,null),
                (115,1,'MenuItem.editpersoninfo',1,'javascript: modifyPersonalInfoClick()',null,null),
                (116,1,'MenuItem.editpassword',1,'javascript: setPasswdClick()',null,null),                
                (117,1,'',0,null,null,null),
                (118,1,'MenuItem.individualization',1,'javascript: showPersonalize()',null,null),
                (119,1,'MenuItem.customDesktopClick',1,'javascript: customDesktopClick()',null,null),     				              
                (120,1,'MenuItem.openSoundTip',1,'javascript: openSoundTip()','<div id="soundTipMenuItem" class=uncheckedItem><img src="images/s.gif" width=16px height=16px border=0><div>',null),
                (125,1,'',0,null,null,null),
                (130,1,'MenuItem.exit',1,'javascript: onLogoffClick()',null,null);
                
INSERT INTO RoleMenuRela(roleId,itemId) values
--                (1,103),
--                (1,105),
                (1,115),
                (1,116),
                (1,117),
                (1,119),
                (1,120),
                (1,125),
                (1,130);

INSERT INTO MenuItem(itemId,parentId,name,type,action,icon,target) values
                (703,7,'MenuItem.hideMenubarClick',1,'javascript: showMenubarClick()',null,null),
                (704,7,'MenuItem.onShowStatusbarClick',1,'javascript: onShowStatusbarClick()','<div id="statusbarMenuItem" class=checkedItem><img src="images/s.gif" width=16px height=16px border=0><div>',null),
                (705,7,'',0,null,null,null),
                (708,7,'MenuItem.windowstyle',1,null,null,null),
                (709,7,'MenuItem.onMaxViewClick',1,'javascript: onMaxViewClick()','<img src="images/maxview.gif" border=0>',null),
                (710,7,'',0,null,null,null),
                (711,7,'MenuItem.navwindow',1,null,null,null),
                (712,7,'MenuItem.propwindow',1,null,null,null),
                (714,7,'',0,null,null,null),
                (718,7,'MenuItem.onShowMessangerClick',1,'javascript: onShowMessangerClick()',null,null),
                (719,7,'',0,null,null,null),
                (720,7,'MenuItem.onRefreshClick',1,'javascript: onRefreshClick()','<img src="images/f5.gif" border=0>',null);
                
INSERT INTO MenuItem(itemId,parentId,name,type,action,icon,target) values
				(731,711,'MenuItem.normal',1,'javascript: normalNaviPanel()','<div id="naviMenuItem2" class=uncheckedItem><img src="images/s.gif" width=16px height=16px border=0><div>',null),
				(732,711,'MenuItem.min',1,'javascript: minNaviPanel()','<div id="naviMenuItem1" class=uncheckedItem><img src="images/s.gif" width=16px height=16px border=0><div>',null),
				(733,711,'MenuItem.close',1,'javascript: closeNaviPanel()','<div id="naviMenuItem0" class=uncheckedItem><img src="images/s.gif" width=16px height=16px border=0><div>',null),
				(734,712,'MenuItem.normal',1,'javascript: normalPropertyPanel()','<div id="propertyMenuItem2" class=uncheckedItem><img src="images/s.gif" width=16px height=16px border=0><div>',null),
				(735,712,'MenuItem.min',1,'javascript: minPropertyPanel()','<div id="propertyMenuItem1" class=uncheckedItem><img src="images/s.gif" width=16px height=16px border=0><div>',null),
				(736,712,'MenuItem.close',1,'javascript: closePropertyPanel()','<div id="propertyMenuItem0" class=uncheckedItem><img src="images/s.gif" width=16px height=16px border=0><div>',null),
                (752,708,'MenuItem.Office2007',1,'javascript: setThemeStyle("office2007")',null,null),
                (753,708,'MenuItem.Office2003',1,'javascript: setThemeStyle("office2003")',null,null),
                (754,708,'MenuItem.Vista',1,'javascript: setThemeStyle("vista")',null,null),
                (755,708,'MenuItem.Apple',1,'javascript: setThemeStyle("apple")',null,null),
                (757,708,'MenuItem.simple',1,'javascript: setThemeStyle("default")',null,null);
                
INSERT INTO MenuItem(itemId,parentId,name,type,action,icon,target) values
				(1015,10,'MenuItem.goToStartPage',1,'javascript: goToStartPage()',null,null),
                (1025,10,'MenuItem.showMyDesktop',1,'javascript: showMyDesktop()',null,null),
                (1029,10,'',0,null,null,null),
                (1030,10,'MenuItem.showGmView',1,'javascript: showGmView()',null,null);

INSERT INTO MenuItem(itemId,parentId,name,type,action,icon,target) values
                (1503,15,'MenuItem.alertSettingClick',1,'javascript: alertSettingClick()',null,null),
                (1504,15,'MenuItem.alertFilterClick',1,'javascript: alertFilterClick()',null,null),
                (1506,15,'',0,null,null,null),
                (1530,15,'MenuItem.showNetworkPreferences',1,'javascript: showNetworkPreferences()',null,null);

INSERT INTO MenuItem(itemId,parentId,name,type,action,icon,target) values
                (1801,18,'MenuItem.setHomeClick',1,'javascript: setHomeClick()',null,null),
                (1804,18,'',0,null,null,null),
                (1806,18,'MenuItem.previousViewClick',1,'javascript: previousViewClick()',null,null),
                (1807,18,'MenuItem.nextViewClick',1,'javascript: nextViewClick()',null,null),
                (1808,18,'',0,null,null,null),
                (1810,18,'MenuItem.closeTabClick',1,'javascript: closeTabClick()',null,null),
                (1811,18,'MenuItem.closeOtherTabClick',1,'javascript: closeOtherTabClick()',null,null),
                (1812,18,'MenuItem.closeAllTabClick',1,'javascript: closeAllTabClick()',null,null);


INSERT INTO MenuItem(itemId,parentId,name,type,action,icon,target) values
--              (2001,20,'onWelcomeClick',1,'javascript: onWelcomeClick()',null,null),
--              (2003,20,'',0,null,null,null),
--                (2008,20,'onHelpClick',1,'javascript: onHelpClick()','<img src="images/help.gif" border=0>',null),
--				(2009,20,'',0,null,null,null),
                (2021,20,'MenuItem.onShowRuntimeClick',1,'javascript: onShowRuntimeClick()',null,null),
                (2022,20,'',0,null,null,null),
                (2023,20,'MenuItem.onContactClick',1,'javascript: onContactClick()',null,null),
                (2025,20,'MenuItem.onLicenseClick',1,'javascript: onLicenseClick()',null,null),
                (2026,20,'',0,null,null,null),
                (2030,20,'MenuItem.onAboutClick',1,'javascript: onAboutClick()',null,null);

INSERT INTO ToolbarButton(buttonId,text,tooltip,icon,action,type) values
				(1050,'','showCurrentAlert','images/fault/bt.gif','showCurrentAlert',1),
                (1051,'','','','',0),
                (1080,'','showGmView','images/preferences.gif','showGmView',1),
                (1100,'','','','',0),
                (1101,'','onHelpClick','images/help.gif','onHelpClick',1),
                (1102,'','','','',0),
                (1103,'','exit','images/logoff.gif','onLogoffClick',1);

INSERT INTO PortletCategory(categoryId,name) values
                (2,'PortletCategory.Routine'),
                (4,'PortletCategory.NE'),
                (9,'PortletCategory.alarm');

INSERT INTO PortletItem(itemId,name,note,url,type,loadingText,icon,refreshable,refreshInterval,closable,settingable,module,categoryId) values
                (202,'PortletCategory.getNetworkInfo',NULL,'../portal/getNetworkInfo.tv',3,NULL,NULL,'1',NULL,NULL,NULL,'network',2),
                (201,'PortletCategory.viewPersonalInfoForPortal',NULL,'../portal/viewPersonalInfoForPortal.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'mydesktop',2),
                (406,'PortletCategory.getDeviceDelayingTop',NULL,'../portal/getDeviceDelayingTop.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
                (407,'PortletCategory.getTopCpuLoading',NULL,'../portal/getTopCpuLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
                (408,'PortletCategory.getTopMemLoading',NULL,'../portal/getTopMemLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
				(416,'PortletCategory.getTopSniLoading',NULL,'../epon/getTopSniLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
				(417,'PortletCategory.getTopPonLoading',NULL,'../epon/getTopPonLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
                (901,'PortletCategory.getAlertDistGraph',NULL,'../portal/getAlertDistGraph.tv',2,NULL,NULL,'1',NULL,NULL,NULL,'alert',9),
                (902,'PortletCategory.getDeviceAlertTop',NULL,'../portal/getDeviceAlertTop.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'alert',9),
                (904,'PortletCategory.getAttentionEntityList',NULL,'../entity/getAttentionEntityList.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',2);
--              (903,'PortletCategory.getServerAlertTop',NULL,'../portal/getServerAlertTop.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'alert',9);

INSERT INTO SystemPreferences(name,value,module) values
                ('alertFilter.actived','true','AlertFilter'),
                ('min.level','2','AlertFilter'),
                ('syslog.listenPorts','514','syslog'),
                ('trap.listenAddress','0.0.0.0','trap'),
                ('trap.listenPorts','162','trap');

INSERT INTO UserPreferences() values
				('maxNaviNum','8','core',2),
				('naviBarOrder','[workbench, network, topographic,config, performance, fault, report, system]','core',2),
				('naviBarVisible','[workbench, network, topographic,fault, system]','core',2);

INSERT INTO EngineServer(id,name,ip,port,note,linkStatus,adminStatus) values
                (1,'localhost','127.0.0.1',3004,'default',1,1);

INSERT INTO UserPortletRela(userId,itemId,gridX,gridY) values
				(2,'201',100,100);
/* -- version 1.0.0,build 2011-2-22,module platform */

-- version 1.0.0,build 2011-8-28,module platform
--INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
--                (1000001,'newLink',1000000,'MODULE.newLink'),
--                (1000002,'newFolder',1000000,'MODULE.newFolder'),
--                (3000005,'refreshEquipment',3000000,'MODULE.refreshEquipment'),
--                (3000006,'relaGoogle',3000000,'MODULE.relaGoogle'),
--                (3000009,'manageEquipment',3000000,'MODULE.manageEquipment'),
--                (3000010,'newRegion',3000000,'MODULE.newRegion'),
--                (3000011,'newEquipment',3000000,'MODULE.newEquipment'),
--                (3000012,'pollConfig',3000000,'MODULE.pollConfig'),
--                (3000013,'configManagement',3000000,'MODULE.configManagement'),
--                (4000004,'alertPolicy',4000000,'MODULE.alertPolicy'),
--                (4000005,'filterAlert',4000000,'MODULE.filterAlert'),
--				(7000007,'smsManagement',7000000, 'MODULE.smsManagement'),
--				(7000008,'databaseManagement',7000000, 'MODULE.databaseManagement'),
--				(7000009,'sysInfoManagement',7000000,'MODULE.sysInfoManagement'),
--				(7000010,'licenseManagement',7000000, 'MODULE.licenseManagement'),
--				(7000011,'alertActionManagement',7000000, 'MODULE.alertActionManagement');

--INSERT INTO RoleFunctionRela(roleId,functionId) values
--                (2,1000001),
--                (2,1000002),
--                (2,3000005),
--                (2,3000006),
--                (2,3000009),
--                (2,3000010),
--                (2,3000011),
--                (2,3000012),
--                (2,3000013),
--                (2,4000004),
--		        (2,4000005),
--                (1,7000007),
--				(1,7000008),
--				(1,7000009),
--				(1,7000010),
--				(1,7000011),
--				(2,7000007),
--				(2,7000008),
--				(2,7000009),
--				(2,7000010),
--				(2,7000011);	
/* -- version 1.0.0,build 2011-8-28,module platform */
-- version 1.3.0,build 2011-12-20,module platform
UPDATE users set userGroupId=10 WHERE userGroupId=1;
/* -- version 1.3.0,build 2011-12-20,module platform */

-- version 1.6.6,build 2012-5-10,module platform
--INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
--                (1000003,'editPortal',1000000,'MODULE.editPortal');
--INSERT INTO RoleFunctionRela(roleId,functionId) values
--                (2,1000003);
/* -- version 1.6.6,build 2012-5-10,module platform */
         
-- version 1.7.8.0,build 2013-1-24,module platform
DELETE FROM SystemPreferences where module = 'ftpConnect';
DELETE FROM SystemPreferences where module = 'ftpServer';
DELETE FROM SystemPreferences where module = 'tftpServer';
DELETE FROM SystemPreferences where module = 'tftpClient';
                
INSERT INTO SystemPreferences(name,value,module) values
 		('ip','127.0.0.1','ftpConnect'),
 		('port','21','ftpConnect'),
 		('userName','ems','ftpConnect'),
 		('pwd','topvision','ftpConnect'),
 		('remotePath','/','ftpConnect');
 		
INSERT INTO SystemPreferences(name,value,module) values
 		('ip','127.0.0.1','ftpServer'),
 		('port','21','ftpServer'),
 		('userName','ems','ftpServer'),
 		('pwd','topvision','ftpServer'),
 		('rootPath','/','ftpServer'),
 		('writeable','true','ftpServer');
/* -- version 1.7.8.0,build 2013-1-24,module platform */

-- version 1.7.8.0,build 2013-1-30,module platform
INSERT INTO SystemPreferences(name,value,module) values
 		('ip','127.0.0.1','tftpServer'),
 		('port','1169','tftpServer'),
 		('rootPath','/','tftpServer');
 		
--INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
--    (7000012,'ftpManagement',7000000,'ftp.ftpServer'),
--    (7000013,'tftpManagement',7000000,'ftp.tftpServer');
    
--INSERT INTO RoleFunctionRela(roleId,functionId) values
--	(1,7000012),
--    (1,7000013),
--    (2,7000012),
--    (2,7000013);
/* -- version 1.7.8.0,build 2013-1-30,module platform */

-- version 1.7.11.0,build 2013-3-22,module platform
--修改TFTP的默认端口号为69
--added by hds
UPDATE SystemPreferences SET value=69 WHERE name='port' AND module='tftpServer';
/* -- version 1.7.11.0,build 2013-3-22,module platform */

-- version 1.7.12.0,build 2013-03-28,module platform
DELETE from FunctionItem;
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
		(1,'navigation',0,'rolePower.navigationControl'),		
		(1000000,'workbench',1,'MODULE.workbench'),
		(1000001,'myDeskTop',1000000,'MenuItem.showMyDesktop'),
        (1000002,'myFolder',1000000,'rolePower.favorites'),
        (2000000,'network',1,'MODULE.network'),
		(2000001,'deviceView',2000000,'WorkBench.deviceView'),
		(2000002,'resourceList',2000000,'WorkBench.entityList'),
		(2000003,'oltList',2000000,'rolePower.oltList'),
		(2000004,'onuList',2000000,'rolePower.onuList'),
		(2000005,'ccmtsList',2000000,'rolePower.ccList'),
		(2000006,'cmList',2000000,'rolePower.cmList'),
		(2000007,'cmImportInfo',2000000,'rolePower.cmImport'),
		(2000008,'newDevice',2000000,'COMMON.newEntity'),
	    (3000000,'topographic',1,'MODULE.topographic'),
        (3000001,'googleMap',3000000,'MODULE.googleMap'),
        (3000002,'topoGraph',3000000,'MODULE.topographic'),
        (4000000,'config',1,'MODULE.config'),
        (4000001,'eponConfigFileMgmt',4000000,'Config.eponConfigFileMgt'),
        (4000002,'loadBalPolicyTpl',4000000,'负载均衡策略模板'),
        (5000000,'performance',1,'MODULE.performance'),
		(5000001,'eponFastConfig',5000000,'rolePower.fastconfig'),
		(5000002,'eponFacadeMgmt',5000000,'rolePower.facadeConfig'),
		(5000003,'eponPortMonitor',5000000,'WorkBench.eponPortMonitor'),
        (6000000,'event',1,'MODULE.fault'),
        (6000001,'eventViewer',6000000,'EVENT.eventViewer'),
		(6000002,'alertView',6000000,'WorkBench.alarmView'),
		(6000003,'alertViewer',6000000,'EVENT.alarmViewer'),
		(6000004,'alertAction',6000000,'SYSTEM.alertAction'),
		(6000005,'alertStrategy',6000000,'MODULE.alertPolicy'),
		(6000006,'alertFilter',6000000,'MODULE.filterAlert'),
        (7000000,'report',1,'MODULE.report'),
        (7000001,'reportView',7000000,'MODULE.reportTask'),
        (8000000,'system',1,'MODULE.system'),
        (8000001,'roleManagement',8000000,'MODULE.roleManagement'),
        (8000002,'userManagement',8000000,'MODULE.userManagement'),
        (8000003,'logManagement',8000000,'MODULE.logManagement'),
		(8000004,'configLog',8000000,'SYSTEM.configLog'),
        (8000005,'departmentManagement',8000000,'SYSTEM.department'),
        (8000006,'postManagement',8000000,'SYSTEM.place'),
        (8000007,'visitStrategyManagement',8000000,'SYSTEM.systemAccessStrategy'),
        (8000008,'mailManagement',8000000,'SYSTEM.mailServer'),
        (8000009,'smsManagement',8000000,'SYSTEM.smsServer'),
        (8000010,'databaseManagement',8000000,'SYSTEM.databaseMaintenance'),
        (8000011,'sysInfoManagement',8000000,'MAIN.systemRuntime'),
        (8000012,'licenseManagement',8000000,'MenuItem.onLicenseClick'),
        (8000013,'ftpManagement',8000000,'ftp.ftpServer'),
        (8000014,'ftpClient',8000000,'ftp.ftpConnect'),
        (8000015,'tftpManagement',8000000,'ftp.tftpServer'),
        (8000016,'pingConfig',8000000,'rolePower.pingOption'),
        (8000017,'snmpConfig',8000000,'rolePower.snmpOption'),
        (8000018,'trapListenerConfig',8000000,'rolePower.trapListenerPortConfig'),
        (8000019,'syslogListenerConfig',8000000,'rolePower.listeningPortConfigofsyslog'),
        
		(2,'operation',0,'rolePower.operationControl'),
		(10000001,'operationDevice',2,'rolePower.operationDevice'),
        (10000002,'topoEdit',2,'rolePower.topooperation'),  
		(10000003,'confirmAlert',2,'MODULE.confirmAlert'),
		(10000004,'clearAlert',2,'MODULE.clearAlert');

DELETE FROM RoleFunctionRela;
INSERT INTO RoleFunctionRela(roleId,functionId) values
		(1,1),		
		(1,8000000),
		(1,8000001),
		(1,8000002),
		(1,8000003),
		(1,8000004),
		(1,8000005),
		(1,8000006),
		(1,8000007),
		(1,8000008),
		(1,8000009),
		(1,8000010),
		(1,8000011),
		(1,8000012),
		(1,8000013),
		(1,8000014),
		(1,8000015),
		(1,8000016),
		(1,8000017),
		(1,8000018),
		(1,8000019),
		(2,1),
		(2,1000000),
		(2,1000001),
        (2,1000002),
        (2,2000000),
		(2,2000001),
		(2,2000002),
		(2,2000003),
		(2,2000004),
		(2,2000005),
		(2,2000006),
		(2,2000007),
		(2,2000008),
        (2,3000000),
        (2,3000001),
        (2,3000002),
        (2,4000000),
        (2,4000001),
        (2,4000002),
        (2,5000000),
 		(2,5000001),
 		(2,5000002),
 		(2,5000003),
        (2,6000000),
        (2,6000001),
		(2,6000002),
		(2,6000003),
		(2,6000004),
		(2,6000005),
		(2,6000006),
        (2,7000000),
        (2,7000001),      
		(2,8000000),
		(2,8000001),
		(2,8000002),
		(2,8000003),
		(2,8000004),
		(2,8000005),
		(2,8000006),
		(2,8000007),
		(2,8000008),
		(2,8000009),
		(2,8000010),
		(2,8000011),
		(2,8000012),
		(2,8000013),
		(2,8000014),
		(2,8000015),
		(2,8000016),
		(2,8000017),
		(2,8000018),
		(2,8000019),
		(2,2),
		(2,10000001),
		(2,10000002),
		(2,10000003),
		(2,10000004);
/* -- version 1.7.12.0,build 2013-03-28,module platform */

-- version 1.7.12.0,build 2013-4-15,module platform
--添加Ping.retries,Ping.timeout,Snmp.port,Snmp.retries,Snmp.timeout,lockTime
--added by yzl
INSERT INTO SystemPreferences(name,value,module) values
 		('Ping.retries', '1', 'Ping'),
 		('Ping.timeout', '800', 'Ping'),
 		('Snmp.port', '161', 'Snmp'),
 		('Snmp.retries', '3', 'Snmp'),
 		('Snmp.timeout', '2000', 'Snmp'),
 		('lockTime', '60', 'logon');
/* -- version 1.7.12.0,build 2013-4-15,module platform */

/* Added by victor@20130508 把所有导航栏默认改为显示*/
-- version 1.7.12.0,build 2013-5-8,module platform
UPDATE UserPreferences SET value = '[workbench, network, topographic,config, performance, fault, report, system]' WHERE name = 'naviBarVisible' AND module = 'core' AND userId = 2;
/* -- version 1.7.12.0,build 2013-5-8,module platform */

-- version 1.7.12.0,build 2013-6-29,module platform
delete from FunctionItem where functionId = 4000002;
/* -- version 1.7.12.0,build 2013-6-29,module platform */

/* Added by lzt@20130810 增加文件自动定时write开关*/
-- version 1.7.12.0,build 2013-08-10,module platform
INSERT INTO SystemPreferences(name,value,module) values
 		('file.autoWrite', 'OFF', 'platform');
/* -- version 1.7.12.0,build 2013-08-10,module platform */

/* Added by fanzidong@20130910 增加FTP/TFTP启动开关*/
-- version 1.7.12.0,build 2013-09-10,module platform
 INSERT INTO SystemPreferences(name,value,module) values
 		('enable','1','ftpServer'),
 		('enable','1','tftpServer');
/* -- version 1.7.12.0,build 2013-09-10,module platform */

-- version 2.0.0.0,build 2013-09-24,module platform
INSERT INTO SystemPreferences(name,value,module) values
 		('file.saveBeforeWrite', 'OFF', 'platform'),
 		('file.autoWriteTime', '00:00_1,2,3,4,5,6,7', 'platform');
/* -- version 2.0.0.0,build 2013-09-24,module platform */
 		
-- version 2.0.0.0,build 2013-10-22,module platform
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(2000009,'cmtsList',2000000,'userPower.cmtsList');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,2000009);
			
DELETE FROM FunctionItem WHERE superiorId = 5000000;

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000013,'perfTemplateMgmt',5000000,'userPower.perfTemplateMgmt');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000013);

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(8000020,'engineServerConfig',8000000,'userPower.engineServerConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(1,8000020);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000020);

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(8000021,'fileAutoWrite',8000000,'userPower.fileAutoWrite');
INSERT INTO RoleFunctionRela(roleId,functionId) values(1,8000021);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000021);

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(8000022,'announcement',8000000,'userPower.announcement');
INSERT INTO RoleFunctionRela(roleId,functionId) values(1,8000022);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000022);
/* -- version 2.0.0.0,build 2013-10-22,module platform */

-- version 2.0.1.0,build 2013-10-30,module platform
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(10000005,'clearevent',2,'userPower.clearevent');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,10000005);
/* -- version 2.0.1.0,build 2013-10-30,module platform */

-- version 2.0.1.0,build 2013-11-7,module platform
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(2000010,'importName',2000000,'userPower.importName');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,2000010);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(2000011,'exportEntity',2000000,'userPower.exportEntity');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,2000011);
/* -- version 2.0.1.0,build 2013-11-7,module platform */

-- version 2.0.1.0,build 2013-11-13,module platform
UPDATE SystemPreferences SET value=0 WHERE name='Snmp.retries' AND module='Snmp';
UPDATE SystemPreferences SET value=5000 WHERE name='Snmp.timeout' AND module='Snmp';
UPDATE SystemPreferences SET value=3000 WHERE name='Ping.timeout' AND module='Ping';
UPDATE SystemPreferences SET value=3 WHERE name='Ping.retries' AND module='Ping';
/* -- version 2.0.1.0,build 2013-11-13,module platform */

/* Added by fanzidong@20131119 设置默认MAC地址展示格式*/
-- version 2.0.1.0,build 2013-11-20,module platform
INSERT INTO UserPreferences(name, value, module, userId) values ('macDisplayStyle', '6#M#U', 'core', 2);
/* -- version 2.0.1.0,build 2013-11-20,module platform */

/* -- version 2.0.2.0,build 2013-12-03,module platform */
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(8000023,'autoClear8800A',8000000,'uesrPower.autoClear8800A');
INSERT INTO RoleFunctionRela(roleId,functionId) values(1,8000023);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000023);
/* -- version 2.0.2.0,build 2013-12-03,module platform */

/* Added by YangYi@20140113 设置提示框持续时间*/
-- version 2.0.4.0,build 2014-01-13,module platform
INSERT INTO UserPreferences(name, value, module, userId) values ('tipShowTime', '10', 'core', 2);
/* -- version 2.0.4.0,build 2013-01-13,module platform */

-- version 2.0.4.0,build 2014-01-15,module platform
UPDATE SystemPreferences SET value="ON" WHERE name='file.autoWrite' AND module='platform';
/* -- version 2.0.4.0,build 2013-01-15,module platform */

/* Added by fanzidong@20140121 添加TFTP客户端配置*/
-- version 2.0.4.0,build 2014-01-21,module platform
INSERT INTO SystemPreferences(name,value,module) values
 		('ip','127.0.0.1','tftpClient'),
 		('port','69','tftpClient');
/* -- version 2.0.4.0,build 2014-01-21,module platform */
 		
/* Added by fanzidong@20140402 设置每周开始日*/
-- version 2.0.4.0,build 2014-04-02,module platform
INSERT INTO UserPreferences(name, value, module, userId) values ('weekStartDay', '1', 'core', 2);
/* -- version 2.0.4.0,build 2014-04-02,module platform */
/* Add by YangYi@20140213 RemoteQuery配置权限*/

-- version 2.0.6.0,build 2014-04-12,module platform
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(8000024,'remoteQueryConfig',8000000,'rolePower.remoteQueryConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(1,8000024);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000024);
/*-- version 2.0.6.0,build 2014-04-12,module platform--*/

-- version 2.0.6.0,build 2014-04-17,module platform
delete from FunctionItem where functionId = 2000009;
delete from RoleFunctionRela where functionId = 2000009;
/*-- version 2.0.6.0,build 2014-04-17,module platform--*/

-- version 2.2.2.0,build 2014-04-21,module platform
UPDATE SystemPreferences SET value=0 WHERE name='Snmp.retries' AND module='Snmp';
UPDATE SystemPreferences SET value=10000 WHERE name='Snmp.timeout' AND module='Snmp';
UPDATE SystemPreferences SET value=4000 WHERE name='Ping.timeout' AND module='Ping';
UPDATE SystemPreferences SET value=1,name='Ping.count' WHERE name='Ping.retries' AND module='Ping';
/*-- version 2.2.2.0,build 2014-04-21,module platform--*/

-- version 2.2.2.0,build 2014-04-30,module platform
INSERT INTO SystemPreferences(name,value,module) values ('camera.switch','off','camera');
/*-- version 2.2.2.0,build 2014-04-30,module platform */

-- version 2.2.2.0,build 2014-05-04,module platform
INSERT INTO ImageDirectory(directoryId,superiorId,name,path,module) values
		(1300,1000,'topo.grap.imageChooser.ccmts','network/ccmts','ccmts'),
		(1400,1000,'topo.grap.imageChooser.cmts','network/cmts','cmts'),
		(1500,1000,'topo.grap.imageChooser.onu','network/onu','onu');
/*-- version 2.2.2.0,build 2014-05-04,module platform--*/
		
-- version 2.2.2.0,build 2014-05-21,module platform
delete from functionItem where functionId = 2000003;
delete from functionItem where functionId = 2000004;
delete from functionItem where functionId = 4000001;
delete from functionItem where functionId = 5000015;
delete from functionItem where functionId = 8000023;
/*-- version 2.2.2.0,build 2014-05-21,module platform--*/

-- version 2.2.2.0,build 2014-07-17,module platform
INSERT INTO Users (userId, userName, passwd, status, familyName, firstName, createTime, ipLoginActive, userGroupId) values
    	(3, 'engineer', 'E10ADC3949BA59ABBE56E057F20F883E', 1, 'engineer', '', now(), 1, 10);
insert into role values(3,'engineer','engineer',0);
insert into userrolerela values(3,3);
insert into userportletrela values (3,201,100,100,null);
insert into functionitem values (3000003,3000000,'networkView','MODULE.networkView');
insert into rolefunctionrela values (3,1);
insert into rolefunctionrela values (3,2);
insert into rolefunctionrela values (3,1000000);
insert into rolefunctionrela values (3,1000001);
insert into rolefunctionrela values (3,1000002);
insert into rolefunctionrela values (3,2000000);
insert into rolefunctionrela values (3,2000001);
insert into rolefunctionrela values (3,2000002);
insert into rolefunctionrela values (3,2000005);
insert into rolefunctionrela values (3,2000006);
insert into rolefunctionrela values (3,2000008);
insert into rolefunctionrela values (3,2000010);
insert into rolefunctionrela values (3,2000011);
insert into rolefunctionrela values (3,3000000);
insert into rolefunctionrela values (3,3000002);
insert into rolefunctionrela values (3,3000003);
insert into rolefunctionrela values (3,10000001);
insert into rolefunctionrela values (3,10000002);
insert into userCustomlization values (3,'WorkBench.entityList','showResource()','icoB2');
insert into userCustomlization values (3,'WorkBench.topo','showTopology()','icoB13');
insert into userCustomlization values (3,'WorkBench.deviceView','showEntityView()','icoB1');
insert into userCustomlization values (3,'WorkBench.ipSegmentView','showIpSegmentView()','icoB4');
/*-- version 2.2.2.0,build 2014-07-17,module platform--*/

-- version 2.2.2.0,build 2014-08-26,module platform
UPDATE SystemPreferences SET value=15000 WHERE name='Snmp.timeout' AND module='Snmp';
/*-- version 2.2.2.0,build 2014-08-26,module platform--*/

-- version 2.2.2.0,build 2014-08-27,module platform
UPDATE userpreferences SET value = 5 where name = 'tipShowTime';
insert into rolefunctionrela values (2,3000003);
/*-- version 2.2.2.0,build 2014-08-27,module platform--*/

-- version 2.4.3.0,build 2014-10-26,module platform
INSERT INTO SystemPreferences(name,value,module) values
 		('tempUnit','℃','unit'),
 		('elecLevelUnit','dBmV','unit');
/*-- version 2.4.3.0,build 2014-10-26,module platform--*/
 		
-- version 2.4.3.0,build 2014-10-31,module platform
INSERT INTO UserPreferences(name, value, module, userId) values ('autoRefreshTime', '10', 'core', 2);
/*-- version 2.4.3.0,build 2014-10-31,module platform */

-- version 2.4.5.0,build 2014-11-26,module platform
INSERT INTO UserPreferences(name, value, module, userId) values ('topNumber', '10', 'core', 2);
INSERT INTO PortletItem(itemId,name,note,url,type,loadingText,icon,refreshable,refreshInterval,closable,settingable,module,categoryId) values
                 (409,'PortletCategory.getDeviceDelayOutTop',NULL,'/portal/getDeviceDelayingOut.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4);
/*-- version 2.4.5.0,build 2014-11-26,module platform */

-- version 2.4.5.0,build 2015-3-12,module platform
UPDATE userCustomlization SET icon = 'icoG13' WHERE userId = 2 and functionName = 'WorkBench.cmcpe';
/* -- version 2.4.5.0,build 2015-3-12,module platform */

-- version 2.4.3.0,build 2015-01-15,module platform
UPDATE SystemPreferences SET value=2 WHERE name='Snmp.retries' AND module='Snmp';
UPDATE SystemPreferences SET value=5000 WHERE name='Snmp.timeout' AND module='Snmp';
UPDATE snmpparam SET timeout = 5000 ,retry=2;
/*-- version 2.4.3.0,build 2015-01-15,module platform*/

-- version 2.5.2.0,build 2015-04-01,module platform
update EngineServer SET linkStatus=2;
/*-- version 2.5.2.0,build 2015-04-01,module platform*/

-- version 2.5.2.0,build 2015-04-28,module platform
UPDATE FunctionItem set name = 'entityList',displayName = 'menunew.entitylist' where functionId = 3000001;
insert into FunctionItem(functionId,name,superiorId,displayName) values
        (8000025,'autoRefreshConfig',8000000,'rolePower.autoRefreshConfig');
insert into rolefunctionrela values(2,8000025);
insert into rolefunctionrela values(1,8000025);

insert into FunctionItem(functionId,name,superiorId,displayName) values
        (8000027,'unitConfig',8000000,'rolePower.unitConfig');
insert into rolefunctionrela values(2,8000027);
insert into rolefunctionrela values(1,8000027);

insert into FunctionItem(functionId,name,superiorId,displayName) values
        (8000028,'alertConfig',8000000,'rolePower.alertConfig');
insert into rolefunctionrela values(2,8000028);
insert into rolefunctionrela values(1,8000028);

delete from FunctionItem where functionId = 6000001;
insert into FunctionItem(functionId,name,superiorId,displayName) values
        (6000007,'alertSound',6000000,'rolePower.alertSound');
insert into rolefunctionrela values(2,6000007);

update EngineServer SET type='Default,CmPoll,Performance';
/*-- version 2.5.2.0,build 2015-04-28,module platform*/

-- version 2.5.2.0,build 2015-05-29,module platform
insert into systempreferences values('historyDataKeepMonth', 3 , 'DataBase');
insert into historyTableName select 'perfcmccpuquality'; 
insert into historyTableName select 'perfcmcerrorcodequality'; 
insert into historyTableName select 'perfcmcflashquality'; 
insert into historyTableName select 'perfcmcflowquality'; 
insert into historyTableName select 'perfcmclinkquality'; 
insert into historyTableName select 'perfcmcmemquality'; 
insert into historyTableName select 'perfcmcopreceiverinputpowerhis'; 
insert into historyTableName select 'perfcmcsnrquality';  
insert into historyTableName select 'perfcmctempquality'; 
insert into historyTableName select 'perfcmtserrorcodequality'; 
insert into historyTableName select 'perfcmtsflowquality'; 
insert into historyTableName select 'perfcmtssnrquality'; 
insert into historyTableName select 'perfconnectivity'; 
insert into historyTableName select 'perfeponboardtempquality'; 
insert into historyTableName select 'perfeponcpuquality'; 
insert into historyTableName select 'perfeponfanspeedquality'; 
insert into historyTableName select 'perfeponflashquality'; 
insert into historyTableName select 'perfeponflowquality'; 
insert into historyTableName select 'perfeponlinkquality'; 
insert into historyTableName select 'perfeponmemquality'; 
insert into historyTableName select 'perfonuflowquality';
insert into historyTableName select 'perfonulinkquality'; 
insert into historyTableName select 'dispersionhistory'; 
/*-- version 2.5.2.0,build 2015-05-29,module platform*/

-- version 2.6.3.0,build 2015-08-18,module platform
INSERT INTO SystemPreferences(name,value,module) values
 		('Ping.timeout','3000','toolPing'),
 		('Ping.count','4','toolPing');
/*-- version 2.6.3.0,build 2015-08-18,module platform*/

-- version 2.6.5.0,build 2015-11-7,module platform
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(8000029,'northBoundConfig',8000000,'userPower.northBoundConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000029);
INSERT INTO RoleFunctionRela(roleId,functionId) values(1,8000029);
/*-- version 2.6.5.0,build 2015-11-7,module platform*/ 

-- version 2.6.6.0,build 2015-12-5,module platform
update EngineServer SET type='Default,Trap,CmPoll,Performance';
/*-- version 2.6.6.0,build 2015-12-5,module platform*/

-- version 2.6.8.3,build 2016-3-20,module platform
delete from PortletItem where itemId = 904;
/*-- version 2.6.8.3,build 2016-3-20,module platform*/

-- version 2.6.8.3,build 2016-6-17,module platform
update engineserver set xmx=0,xms=0,manageStatus=1,linkStatus=1,adminStatus=1 where id = 1;
/* -- version 2.6.8.3,build 2016-6-17,module platform */

-- version 2.6.8.3,build 2017-1-6,module platform
update systempreferences set value = 12 where name = 'historyDataKeepMonth' and value = 3;
/* -- version 2.6.8.3,build 2017-1-6,module platform */


-- version 2.9.1.8,build 2017-8-12,module platform
update EngineServer SET type='Default,Trap,CmPoll,Performance,PNMP';
/*-- version 2.9.1.8,build 2017-8-12,module platform*/
-- version 2.9.1.11,build 2017-09-07,module server
INSERT INTO SystemPreferences(name,value,module) values
 		('icmp',1,'connectStrategy'),
 		('snmp',0,'connectStrategy'),
 		('tcp',0,'connectStrategy'),
 		('Snmp.connectivityOid','1.3.6.1.2.1.1.2.0','Snmp'),
 		('port',23,'tcp'),
 		('timeout',5000,'tcp');
 /* -- version 2.9.1.11,build 2017-09-07,module server */ 
 		
-- version 2.9.1.11,build 2017-09-12,module server
INSERT INTO SystemPreferences(name,value,module) values
 		('Ping.retry',0,'Ping');
/* -- version 2.9.1.11,build 2017-09-12,module server */
 
-- version 2.9.1.13,build 2017-09-28,module server
INSERT INTO UserPreferences(name, value, module, userId) values ('topoTreeDisplayDevice', '1', 'core', 2);
INSERT INTO UserPreferences(name, value, module, userId) values ('topoTreeClickToOpen', '1', 'core', 2);
/* -- version 2.9.1.13,build 2017-09-28,module server */

-- version 2.9.1.18,build 2017-12-18,module platform
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (2000013,'changeEmsName',2000000,'userPower.changeEmsName');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,2000013);
/* -- version 2.9.1.18,build 2017-12-18,module platform */

-- version 2.10.0.8,build 2018-03-01,module platform
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (8000033,'personalInfo',8000000,'rolePower.personalInfo');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (8000034,'pwdEdit',8000000,'rolePower.pwdEdit');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (8000035,'personalize',8000000,'rolePower.personalize');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (8000036,'tftpClient',8000000,'rolePower.tftpClient');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (8000037,'connectTestConfig',8000000,'rolePower.connectTestConfig');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (8000038,'tcpPortConfig',8000000,'rolePower.tcpPortConfig');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (8000039,'northPerfConfig',8000000,'rolePower.northPerfConfig');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (8000040,'cmJumpConfig',8000000,'rolePower.cmJumpConfig');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (8000041,'onuOnOffRecord',8000000,'rolePower.onuOnOffRecord');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000033);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000034);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000035);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000036);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000037);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000038);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000039);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000040);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,8000041);
/* -- version 2.10.0.8,build 2018-03-01,module platform */
-- version 2.10.0.1,build 2017-12-12,module server
INSERT INTO SystemPreferences(name,value,module) values ('AlertType.localeNameInit', 0, 'Alert');
/* -- version 2.10.0.1,build 2017-12-12,module server */

-- version 2.10.2.0,build 2018-04-17,module platform
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
            (2000014,'onuLinkList',2000000,'rolePower.onuLinkList'),
            (2000015,'eponBusiness',2000000,'rolePower.eponBusiness'),
            (2000016,'gponBusiness',2000000,'rolePower.gponBusiness');

INSERT INTO RoleFunctionRela(roleId,functionId) values
			(2,2000014),
			(2,2000015),
			(2,2000016);
/* -- version 2.10.2.0,build 2018-04-17,module platform */