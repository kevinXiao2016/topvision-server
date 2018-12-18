/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - platform
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-2-22,module platform

/*Table structure for table VersionControl */
CREATE TABLE VersionControl (
  moduleName varchar(16) default NULL,
  versionNum varchar(16) default NULL,
  versionDate date default NULL,
  lastTime timestamp default CURRENT_TIMESTAMP,
  note varchar(512) default NULL,
  PRIMARY KEY  (moduleName)
);


/*Table structure for table Users */
CREATE TABLE Users (
  userId bigint(20) NOT NULL auto_increment,
  userName varchar(32) NOT NULL,
  familyName varchar(32) default '',
  firstName varchar(32) default NULL,
  note varchar(256) default NULL,
  passwd varchar(128) NOT NULL,
  age int(11) default NULL,
  sex int(11) default NULL,
  homeTelphone varchar(32) default NULL,
  workTelphone varchar(32) default '',
  mobile varchar(32) default NULL,
  email varchar(64) default NULL,
  homePlace varchar(128) default NULL,
  lastLoginTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  lastLoginIp varchar(32) default NULL,
  ipLoginActive int(1) default NULL,
  bindIp varchar(32) default NULL,
  limitIp varchar(256) default NULL,
  status char(1) default NULL,
  createTime timestamp NOT NULL default '0000-00-00 00:00:00',
  userGroupId bigint(20) default '0',
  departmentId bigint(20) default '0',
  placeId bigint(20) default '0',
  PRIMARY KEY  (userId)
);

/*Table structure for table DataDictionary */
CREATE TABLE DataDictionary (
  dictionaryId bigint(20) NOT NULL auto_increment,
  name varchar(64) default NULL,
  value varchar(128) default NULL,
  type varchar(128) default NULL,
  extend1 varchar(256) default NULL,
  extend2 varchar(256) default NULL,
  extend3 varchar(512) default NULL,
  extend4 varchar(1024) default NULL,
  PRIMARY KEY  (dictionaryId)
);

/*Table structure for table Department */
CREATE TABLE Department (
  departmentId bigint(20) NOT NULL auto_increment,
  superiorId bigint(20) default NULL,
  name varchar(64) NOT NULL,
  createTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  modifyTime timestamp NOT NULL default '0000-00-00 00:00:00',
  note varchar(256) default NULL,
  PRIMARY KEY  (departmentId)
);

/*Table structure for table FavouriteFolder */
CREATE TABLE FavouriteFolder (
  folderId bigint(20) NOT NULL auto_increment,
  superiorId bigint(20) default NULL,
  path varchar(256) default NULL,
  userId bigint(20) default NULL,
  name varchar(128) NOT NULL,
  type int(11) default NULL COMMENT '0表示文件夹, 1表示链接',
  url varchar(128) default NULL,
  shared char(1) default NULL COMMENT '0表示不共享, 1表示能被所有用户看',
  PRIMARY KEY  (folderId),
  KEY FK_WLINK_USER (userId),
  CONSTRAINT FK_WLINK_USER FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table NavigationButton */
CREATE TABLE NavigationButton (
  naviId int NOT NULL,
  name varchar(32) NOT NULL,
  displayName varchar(64) NOT NULL,
  seq int(11) default '0',
  icon16 varchar(64) default NULL,
  icon24 varchar(64) default NULL,
  action varchar(64) default NULL,
  PRIMARY KEY  (naviId)
);


/*Table structure for table FunctionItem */
CREATE TABLE FunctionItem (
  functionId bigint(20) NOT NULL,
  superiorId bigint(20) default NULL,
  name varchar(64) NOT NULL,
  displayName varchar(64) NOT NULL,
  PRIMARY KEY  (functionId)
);


/*Table structure for table MenuItem */
CREATE TABLE MenuItem (
  itemId	int(11) NOT NULL,
  parentId	int(11) NOT NULL default '0',
  name	varchar(64) NOT NULL,
  type	int(2) default 1,
  mnemonic	char(1) default NULL,
  action	varchar(128) default NULL,
  icon	varchar(128) default NULL,
  target	varchar(32) default NULL,
  note	varchar(128) default NULL,
  pluginId	int(11) default '0',
  functionId	int(11) default '0',
  PRIMARY KEY  (itemId)
);


/*Table structure for table ToolbarButton */
CREATE TABLE ToolbarButton (
  buttonId	int(11) NOT NULL,
  text	 varchar(16) default NULL,
  tooltip	 varchar(128) default NULL,
  note	 varchar(128) default NULL,
  icon	 varchar(64) default NULL,
  action	 varchar(128) default NULL,
  buttonGroup		varchar(32) default NULL,
  type	int(11) default '1' COMMENT '0表示分割线, 1表示按钮, 2表示下拉菜单',
  seq int(11) default '0',
  pluginId	 int(11) default '0',
  functionId	 int(11) default '0',
  PRIMARY KEY  (buttonId)
);

/*Table structure for table ButtonMenu */
CREATE TABLE ButtonMenu (
  itemId	int(11) NOT NULL auto_increment,
  parentId	int(11) default NULL,
  buttonId	int(11) default NULL,
  text	 varchar(64) default NULL,
  action	varchar(128) default NULL,
  type	 int(11) default '1' COMMENT '0表示分割线',
  PRIMARY KEY  (itemId)
);


/*Table structure for table GoogleLocation */
CREATE TABLE GoogleLocation (
  locationId decimal(20,0) NOT NULL,
  name varchar(64) default NULL,
  displayName varchar(64) default NULL,
  longitude float default NULL,
  latitude float default NULL,
  center float default NULL,
  extend varchar(32) default NULL,
  PRIMARY KEY  (locationId)
);

/*Table structure for table Place */
CREATE TABLE Place (
  placeId bigint(20) NOT NULL auto_increment,
  superiorId bigint(20) default NULL,
  name varchar(128) NOT NULL,
  note varchar(256) default NULL,
  PRIMARY KEY  (placeId)
);

/*Table structure for table PortletCategory */
CREATE TABLE PortletCategory (
  categoryId int(11) NOT NULL,
  name varchar(128) NOT NULL,
  PRIMARY KEY  (categoryId)
);

/*Table structure for table PortletItem */
CREATE TABLE PortletItem (
  itemId bigint(20) NOT NULL auto_increment,
  name varchar(64) default NULL,
  note varchar(256) default NULL,
  url varchar(128) default NULL,
  type int(11) default NULL,
  loadingText varchar(64) default NULL,
  icon varchar(64) default NULL,
  refreshable char(1) default NULL,
  refreshInterval int(11) default NULL,
  closable char(1) default NULL,
  settingable char(1) default NULL,
  module varchar(64) default NULL,
  categoryId int(11) default NULL,
  PRIMARY KEY  (itemId),
  KEY FK_PORTLET_CATE (categoryId),
  CONSTRAINT FK_PORTLET_CATE FOREIGN KEY (categoryId) REFERENCES PortletCategory (categoryId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table PortletView */
CREATE TABLE PortletView (
  viewId bigint(20) NOT NULL auto_increment,
  userId bigint(20) default NULL,
  name varchar(64) default NULL,
  note varchar(256) default NULL,
  type int(11) default NULL,
  PRIMARY KEY  (viewId)
);

/*Table structure for table PortletViewRela */
CREATE TABLE PortletViewRela (
  viewId bigint(20) NOT NULL default '0',
  itemId bigint(20) NOT NULL default '0',
  PRIMARY KEY  (viewId,itemId)
);

/*Table structure for table Report */
CREATE TABLE Report (
  reportId bigint(20) NOT NULL auto_increment,
  name varchar(128) default NULL,
  note varchar(256) default NULL,
  createTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  format int(11) default NULL,
  path varchar(128) default NULL,
  pdf char(1) default NULL,
  pdfPath varchar(128) default NULL,
  excelPath varchar(128) default NULL,
  excel char(1) default NULL,
  word char(1) default NULL,
  wordPath varchar(128) default NULL,
  txt char(1) default NULL,
  txtPath varchar(128) default NULL,
  taskId bigint(20) default NULL,
  PRIMARY KEY  (reportId)
);

/*Table structure for table ReportTemplate */
CREATE TABLE ReportTemplate (
  templateId bigint(20) NOT NULL auto_increment,
  superiorId bigint(20) default NULL,
  name varchar(64) default NULL,
  displayName varchar(128) default NULL,
  note varchar(254) default NULL,
  icon16 varchar(64) default NULL,
  icon48 varchar(64) default NULL,
  module varchar(64) default NULL,
  path varchar(128) default NULL,
  url varchar(64) default NULL,
  taskable char(1) default '0',
  PRIMARY KEY  (templateId)
);

/*Table structure for table ReportTask */
--CREATE TABLE ReportTask (
--  taskId bigint(20) NOT NULL auto_increment,
--  startTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
--  startInterval int(11) default NULL COMMENT '以小时为单位',
--  hour int(11) default NULL,
--  minute int(11) default NULL,
--  note varchar(256) default NULL,
--  templateId bigint(20) default NULL,
--  name varchar(128) default NULL,
--  content blob,
--  jobClass varchar(128) default NULL,
--  email varchar(256) default NULL,
--  state char(1) default NULL COMMENT '0表示停用, 1表示被启用',
--  running char(1) default NULL COMMENT '0表示活动的, 1表示正在产生报表',
--  format int(11) default NULL COMMENT '1表示pdf, 2表示Excel, 4表示Word按位与进行判断',
--  cycleType int(11) default NULL COMMENT '0表示延迟执行（不需要周期执行），1表示分钟，2表示小时，3表示天，4表示工作日周，5表示休息日周，6表示月，7表示年，8表示自定义',
--  PRIMARY KEY  (taskId),
--  KEY FK_REPTASK_CAT (templateId),
--  CONSTRAINT FK_REPTASK_CAT FOREIGN KEY (templateId) REFERENCES ReportTemplate (templateId) ON DELETE CASCADE ON UPDATE CASCADE
--);

/*Table structure for table Repository */
CREATE TABLE Repository (
  id bigint(20) NOT NULL auto_increment,
  owner varchar(32) default NULL,
  createTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  key1 varchar(32) default NULL,
  key2 varchar(32) default NULL,
  key3 varchar(32) default NULL,
  key4 varchar(32) default NULL,
  key5 varchar(32) default NULL,
  category varchar(32) default NULL,
  content varchar(1024) default NULL,
  PRIMARY KEY  (id)
);

/*Table structure for table Role */
CREATE TABLE Role (
  roleId bigint(20) NOT NULL auto_increment,
  name varchar(64) NOT NULL,
  note varchar(256) default NULL,
  superiorId bigint(20) default NULL,
  PRIMARY KEY  (roleId)
);


/*Table structure for table RoleNaviRela */
CREATE TABLE RoleNaviRela (
  roleId bigint(20) NOT NULL default '0',
  naviId int NOT NULL default '0',
  PRIMARY KEY  (roleId,naviId),
  KEY FK_ROLE_NAVI (naviId),
  CONSTRAINT FK_NAVI_ROLE FOREIGN KEY (roleId) REFERENCES Role (roleId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_ROLE_NAVI FOREIGN KEY (naviId) REFERENCES NavigationButton (naviId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table RoleFunctionRela */
CREATE TABLE RoleFunctionRela (
  roleId bigint(20) NOT NULL default '0',
  functionId bigint(20) NOT NULL default '0',
  PRIMARY KEY  (roleId,functionId),
  KEY FK_ROLE_FUNC (functionId),
  CONSTRAINT FK_FUNC_ROLE FOREIGN KEY (roleId) REFERENCES Role (roleId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_ROLE_FUNC FOREIGN KEY (functionId) REFERENCES FunctionItem (functionId) ON DELETE CASCADE ON UPDATE CASCADE
);


/*Table structure for table RoleMenuRela */
CREATE TABLE RoleMenuRela (
  roleId bigint(20) NOT NULL default '0',
  itemId int(11) NOT NULL default '0',
  PRIMARY KEY  (roleId,itemId),
  KEY FK_ROLE_MENU (itemId),
  CONSTRAINT FK_MENU_ROLE FOREIGN KEY (roleId) REFERENCES Role (roleId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_ROLE_MENU FOREIGN KEY (itemId) REFERENCES MenuItem (itemId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table RoleButtonRela */
CREATE TABLE RoleButtonRela (
  roleId bigint(20) NOT NULL default '0',
  buttonId int(11) NOT NULL default '0',
  PRIMARY KEY  (roleId,buttonId),
  KEY FK_ROLE_BUTTON (buttonId),
  CONSTRAINT FK_BUTTON_ROLE FOREIGN KEY (roleId) REFERENCES Role (roleId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_ROLE_BUTTON FOREIGN KEY (buttonId) REFERENCES ToolbarButton (buttonId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table Staff */
CREATE TABLE Staff (
  staffId bigint(20) NOT NULL auto_increment,
  userName varchar(32) NOT NULL,
  familyName varchar(32) default NULL,
  firstName varchar(32) default NULL,
  note varchar(256) default NULL,
  passwd varchar(128) NOT NULL,
  age int(11) default NULL,
  sex int(11) default NULL,
  homeTelphone varchar(32) default NULL,
  workTelphone varchar(32) default NULL,
  mobile varchar(32) default NULL,
  email varchar(64) default NULL,
  homePlace varchar(128) default NULL,
  createTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  departmentId bigint(20) default '0',
  placeId bigint(20) default '0',
  PRIMARY KEY  (staffId)
);

/*Table structure for table SystemLog */
CREATE TABLE SystemLog (
  logId bigint(20) NOT NULL auto_increment,
  userName varchar(32) default NULL,
  ip varchar(32) default NULL,
  description varchar(256) default NULL,
  appName varchar(64) default NULL,
  operationType varchar(64) default NULL,
  createTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (logId)
);

/*Table structure for table SystemPreferences */
CREATE TABLE SystemPreferences (
  name varchar(64) NOT NULL,
  value varchar(256) default NULL,
  module varchar(32) NOT NULL default '',
  PRIMARY KEY  (module,name)
);

/*Table structure for table UserDepartmentRela */
CREATE TABLE UserDepartmentRela (
  userId bigint(20) default NULL,
  departmentId bigint(20) default NULL,
  KEY FK_DEPART_USER (userId),
  KEY FK_USER_DEPART (departmentId),
  CONSTRAINT FK_DEPART_USER FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_USER_DEPART FOREIGN KEY (departmentId) REFERENCES department (departmentId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table UserGroup */
CREATE TABLE UserGroup (
  userGroupId bigint(20) NOT NULL auto_increment,
  name varchar(64) NOT NULL,
  description varchar(128) NOT NULL,
  createTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (userGroupId)
);

/*Table structure for table UserGroupRela */
CREATE TABLE UserGroupRela (
  userId bigint(20) NOT NULL,
  userGroupId bigint(20) NOT NULL,
  PRIMARY KEY  (userId,userGroupId),
  KEY FK_USER_GROUPS (userGroupId),
  CONSTRAINT FK_GROUPS_USER FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_USER_GROUPS FOREIGN KEY (userGroupId) REFERENCES UserGroup (userGroupId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table UserPlaceRela */
CREATE TABLE UserPlaceRela (
  userId bigint(20) default NULL,
  placeId bigint(20) default NULL,
  KEY FK_PLACE_USER (userId),
  KEY FK_USER_PLACE (placeId),
  CONSTRAINT FK_PLACE_USER FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_USER_PLACE FOREIGN KEY (placeId) REFERENCES Place (placeId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table UserPortletRela */
CREATE TABLE UserPortletRela (
  userId bigint(20) NOT NULL default '0',
  itemId bigint(20) NOT NULL default '0',
  gridX int(11) default NULL,
  gridY int(11) default NULL,
  param varchar(128) default NULL,
  PRIMARY KEY  (userId,itemId),
  KEY FK_USER_PORTLET (itemId),
  CONSTRAINT FK_PORTLET_USER FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_USER_PORTLET FOREIGN KEY (itemId) REFERENCES PortletItem (itemId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table UserPreferences */
CREATE TABLE UserPreferences (
  name varchar(64) NOT NULL,
  value varchar(256) default NULL,
  module varchar(64) NOT NULL,
  userId bigint(20) NOT NULL,
  PRIMARY KEY  (name,module,userId),
  KEY FK_PREF_USER (userId),
  CONSTRAINT FK_PREF_USER FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table UserRoleRela */
CREATE TABLE UserRoleRela (
  userId bigint(20) NOT NULL,
  roleId bigint(20) NOT NULL,
  PRIMARY KEY  (userId,roleId),
  KEY FK_USER_ROLE (roleId),
  CONSTRAINT FK_ROLE_USER FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_USER_ROLE FOREIGN KEY (roleId) REFERENCES Role (roleId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table ImageDirectory */
CREATE TABLE ImageDirectory (
  directoryId bigint(20) NOT NULL auto_increment,
  superiorId bigint(20) NOT NULL,
  name varchar(64) NOT NULL,
  module varchar(32),
  path varchar(512),
  PRIMARY KEY  (directoryId)
);

/*Table structure for table ImageDirectory */
CREATE TABLE ImageFile (
  fileId bigint(20) NOT NULL auto_increment,
  directoryId bigint(20) NOT NULL,
  name varchar(64) NOT NULL,
  format varchar(16) NOT NULL,
  path varchar(512),
  PRIMARY KEY  (fileId),
  KEY FK_IMG_DIR (directoryId),
  CONSTRAINT FK_IMG_DIR FOREIGN KEY (directoryId) REFERENCES ImageDirectory (directoryId) ON DELETE CASCADE ON UPDATE CASCADE  
);

/*Table structure for table EngineServer */
CREATE TABLE EngineServer (
  id int(11) NOT NULL auto_increment,
  name varchar(64) default NULL,
  ip varchar(64) default NULL,
  port int(11) default NULL,
  note varchar(255) default NULL,
  manageRange varchar(255) default NULL,
  linkStatus tinyint(1) default 0,
  adminStatus tinyint(1) default 0,
  PRIMARY KEY  (id)
);

/*Table structure for table OperatorHistory */
create table OperatorHistory
(
   operName  varchar(32) NOT NULL,
   status    int(1)  NOT NULL,
   operTime  timestamp  NOT NULL,
   entityId  bigint(20)  NOT NULL,
   operAction  varchar(255)  NOT NULL,
   clientIpAddress      varchar(20),   
   operId    bigint(20) NOT NULL auto_increment,
   PRIMARY KEY (operId)
);
/* -- version 1.0.0,build 2011-2-22,module platform */

-- version 1.7.5.0,build 2012-8-3,module platform
ALTER table users add COLUMN (language varchar(5)); 
/* -- version 1.7.5.0,build 2012-8-3,module platform */

/* Add by Victor@20131014收藏夹中链接大于128会出错，改为1024*/
-- version 1.7.18.2,build 2013-10-14,module platform
alter table favouriteFolder modify url varchar(1024); 
/* -- version 1.7.18.2,build 2013-10-14,module platform */

-- version 2.2.2.0,build 2014-09-28,module platform
alter table users modify lastLoginTime timestamp NOT NULL default CURRENT_TIMESTAMP;
/* -- version 2.2.2.0,build 2013-09-28,module platform */

/*ADD by bravin@20141116:添加1.允许多点登录使能 2.用户超时时间*/
-- version 2.4.3.0,build 2014-12-05,module platform
alter table Users add COLUMN(allowMutliIpLogin tinyint(1) DEFAULT 1);
alter table Users add COLUMN(timeout int(10) DEFAULT 30);
/* -- version 2.4.3.0,build 2014-12-05,module platform */

-- version 2.5.2.0,build 2015-4-21,module platform
alter table UserPreferences modify value TEXT; 
/* -- version 2.5.2.0,build 2015-4-21,module platform */

-- version 2.5.2.0,build 2015-4-28,module platform
alter table EngineServer change manageRange type varchar(255) default NULL; 
/* -- version 2.5.2.0,build 2015-4-28,module platform */

-- version 2.5.2.0,build 2015-5-29,module platform
create table historyTableName(tableName varchar(100));
/* -- version 2.5.2.0,build 2015-5-29,module platform */

-- version 2.6.8.3,build 2016-6-17,module platform
alter table engineserver add column version varchar(50) default null after port;
alter table engineserver add column xmx int after version;
alter table engineserver add column xms int after xmx;
alter table engineserver add column manageStatus tinyint default 0 after type;
/* -- version 2.6.8.3,build 2016-6-17,module platform */

-- version 2.9.0.0,build 2017-01-08 16:00,module platform
create table TelnetRecords
(
  id bigint(20) NOT NULL auto_increment,
  ip varchar(50) NOT NULL,
  command varchar(256) NOT NULL,
  userId varchar(16) NOT NULL,
  createTime timestamp default CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);
/* -- version 2.9.0.0,build 2017-01-08 16:00,module platform */