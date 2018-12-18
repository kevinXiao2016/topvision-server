/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0,build 2011-3-31,module server

/*Table structure for table Entity */
CREATE TABLE Entity (
  entityId bigint(20) NOT NULL auto_increment,
  parentId bigint(20) default 0,
  name varchar(64) default '',
  ip varchar(32) default NULL,
  createTime timestamp NOT NULL default '0000-00-00 00:00:00',
  modifyTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  offManagementTime bigint(20) default 0,
  icon varchar(64) default NULL,
  snmpSupport tinyint(1) default NULL,
  sysName varchar(64) default NULL,
  sysDescr varchar(255) default NULL,
  sysObjectID varchar(255) default NULL,
  sysContact varchar(64) default NULL,
  sysLocation varchar(64) default NULL,
  sysServices varchar(64) default NULL,
  sysUpTime varchar(64) default NULL,
  panel varchar(64) default NULL,
  note varchar(128) default NULL,
  url varchar(256) default NULL,
  virtual char(1) default '0',
  extend1 int(11) default NULL,
  typeId bigint(20) default NULL,
  type bigint(20) default NULL,
  corpId bigint(20) default '1',
  ipv6 varchar(128) default NULL,
  os varchar(64) default NULL,
  mac varchar(32) default NULL,
  location varchar(128) default NULL,
  duty varchar(32) default NULL,
  agentInstalled varchar(1) default '0',
  status char(1) default '1' COMMENT '0表示停止管理, 1表示正在被管理',
  configStatus char(4) default 0 COMMENT '0表示无须同步和下发 1表示有需要同步的配置 2表示有需要下发的配置 3表示既有需要同步的也有需要下发的',
  categoryId bigint default 0,
  virtualNetworkStatus int(4) default 1 COMMENT '1表示不属于设备的快捷方式，具体的值表示为快捷方式并且位于哪个虚拟子网',
  PRIMARY KEY  (entityId)
);

/*Table structure for table FolderCategory */
CREATE TABLE FolderCategory (
  categoryId int NOT NULL,
  name varchar(64) NOT NULL COMMENT '不能为空',
  note varchar(256),
  PRIMARY KEY  (categoryId)
);

/*Table structure for table TopoFolder */
CREATE TABLE TopoFolder (
  folderId bigint(20) NOT NULL auto_increment,
  superiorId bigint(20) default NULL,
  categoryId bigint(20) default 10,
  name varchar(64) NOT NULL COMMENT '不能为空',
  type int(11) default NULL COMMENT '物理拓扑、网络拓扑、子网、云图, 其它',
  note varchar(128) default NULL,
  createTime timestamp NOT NULL default '0000-00-00 00:00:00',
  modifyTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  displayGrid char(1) default '0',
  backgroundImg varchar(64) default NULL COMMENT '背景图形路径',
  backgroundColor char(7) default NULL COMMENT '背景色格式:#FFFFFF',
  backgroundFlag char(1) default NULL,
  backgroundPosition int(1) default NULL,
  linkColor varchar(7) default NULL COMMENT '默认#000000',
  linkSelectedColor varchar(7) default NULL COMMENT '默认',
  linkWidth float default '1.2' COMMENT '默认1',
  linkStartArrow char(1) default NULL,
  linkEndArrow char(1) default NULL,
  linkShadow char(1) default '1',
  markerAlertMode char(1) default '1',
  x int(11) default NULL,
  y int(11) default NULL,
  width int(11) default 1600,
  height int(11) default 1200,
  zoom float default '1',
  refreshInterval bigint(20) default NULL COMMENT '自动后台取数频率',
  icon varchar(256) default NULL COMMENT '图标',
  url varchar(256) default NULL COMMENT 'URL',
  path varchar(256) default NULL,
  fixed char(1) default '0',
  entityLabel varchar(16) default NULL,
  linkLabel varchar(16) default NULL,
  displayLinkLabel char(1) default '1',
  displayLink char(1) default '1',
  displayName char(1) default '1',
  displayCluetip char(1) default '1',
  displayAlertIcon      char(1) default '1',
  displayNoSnmp char(1) default '1',
  displayRouter char(1) default '1',
  displaySwitch char(1) default '1',
  displayL3switch char(1) default '1',
  displayServer char(1) default '1',
  displayDesktop char(1) default '1',
  displayOthers char(1) default '1',
  entityForOrgin bigint(20) default '0',
  depthForOrgin int(11) default '1',
  extend1 varchar(64) default NULL,
  extend2 varchar(128) default NULL,
  subnetIp varchar(16) default NULL,
  subnetMask varchar(16) default NULL,
  displayEntityLabel char(1) default '1',
  PRIMARY KEY  (folderId)
);

/*Table structure for table ActionType */
CREATE TABLE ActionType (
  actionTypeId int(11) NOT NULL auto_increment,
  name varchar(32) default NULL,
  displayName varchar(64) default NULL,
  enabled tinyint(1) default NULL,
  actionClass varchar(128) default NULL,
  params blob,
  PRIMARY KEY  (actionTypeId)
);

/*Table structure for table Action */
CREATE TABLE Action (
  actionId int(11) NOT NULL auto_increment,
  actionTypeId int(11) default NULL,
  name varchar(64) default NULL,
  params blob,
  enabled tinyint(1) default '1',
  PRIMARY KEY  (actionId),
  KEY FK_ACTION_TYPE (actionTypeId),
  CONSTRAINT FK_ACTION_TYPE FOREIGN KEY (actionTypeId) REFERENCES ActionType (actionTypeId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table AlertType */
CREATE TABLE AlertType (
  typeId int(11) NOT NULL,
  category int(11) default '-1',
  name varchar(64) default NULL,
  displayName varchar(64) default NULL,
  levelId int(1) default '3' COMMENT '告警等级',
  updateLevel tinyint(1) default '0',
  alertTimes varchar(10) default NULL,
  smartUpdate tinyint(1) default '0',
  terminate tinyint(1) default '0',
  threshold char(1) default '0' COMMENT '是否阈值类型',
  active char(1) default NULL COMMENT '是否激活',
  oid varchar(32) default NULL,
  note varchar(256) default NULL,
  PRIMARY KEY  (typeId)
);

/*Table structure for table Levels */
CREATE TABLE Levels (
  levelId int(11) NOT NULL auto_increment,
  name varchar(64) NOT NULL,
  note varchar(256) default NULL,
  active char(1) default NULL,
  icon varchar(64) default NULL,
  color varchar(16) default NULL,
  sound varchar(64) default NULL,
  PRIMARY KEY  (levelId)
);

/*Table structure for table Alert */
CREATE TABLE Alert (
  alertId bigint(20) NOT NULL auto_increment,
  typeId int(11) default NULL,
  name varchar(255) default NULL,
  levelId int(2) default NULL,
  monitorId bigint(20) default NULL COMMENT 'Entity，Port，Monitor',
  entityId bigint(20) default NULL,
  host varchar(64) default NULL COMMENT 'IP或者主机名',
  source varchar(32) default NULL COMMENT '比如端口、软件',
  message varchar(512) default NULL,
  firstTime timestamp NOT NULL default '0000-00-00 00:00:00',
  lastTime timestamp NOT NULL default '0000-00-00 00:00:00',
  happenTimes int(11) default NULL,
  confirmUser varchar(32) default NULL,
  confirmTime timestamp NOT NULL default '0000-00-00 00:00:00',
  confirmMessage varchar(255) default NULL,
  status int(1) default NULL COMMENT ' 0-alert,1-confirm,2-auto clear,3-manual clear',
  orginalCode bigint(20) default NULL,
  extend1 varchar(32) default NULL,
  userObjectString text,
  PRIMARY KEY  (alertId),
  KEY FK_ALERT_TYPE (typeId),
  KEY FK_ALERT_LEVEL (levelId),
  CONSTRAINT FK_ALERT_LEVEL FOREIGN KEY (levelId) REFERENCES Levels (levelId),
  CONSTRAINT FK_ALERT_ENTITY FOREIGN KEY(entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_ALERT_TYPE FOREIGN KEY (typeId) REFERENCES AlertType (typeId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table AlertComment */
CREATE TABLE AlertComment (
  commentId bigint(20) NOT NULL auto_increment,
  name varchar(128) NOT NULL,
  note varchar(512) default NULL,
  PRIMARY KEY  (commentId)
);

/*Table structure for table AlertFilter */
CREATE TABLE AlertFilter (
  filterId bigint(20) NOT NULL auto_increment,
  name varchar(64) default NULL,
  type int(1) default NULL,
  combined char(1) default NULL,
  ip varchar(32) default NULL,
  typeId bigint(20) default NULL,
  params varchar(32) default NULL,
  note varchar(128) default NULL,
  PRIMARY KEY  (filterId)
);

/*Table structure for table AlertTypeActionRela */
CREATE TABLE AlertTypeActionRela (
  alertTypeId int(11) default NULL,
  actionId int(11) default NULL
);

/*Table structure for table EventType */
CREATE TABLE EventType (
  typeId int(11) NOT NULL,
  parentId int(11) default '-1',
  name varchar(64) default NULL,
  displayName varchar(64) default NULL,
  note varchar(256) default NULL,
  PRIMARY KEY  (typeId)
);

/*Table structure for table Event */
CREATE TABLE Event (
  eventId bigint(20) NOT NULL auto_increment,
  alertId bigint(20) default NULL,
  typeId int(11) default NULL,
  name varchar(64) default NULL,
  monitorId bigint(20) default NULL,
  entityId bigint(20) default NULL,
  host varchar(64) default NULL COMMENT 'IP或者主机名',
  source varchar(64) default NULL COMMENT '比如端口、软件',
  value varchar(32) default NULL,
  message varchar(512) default NULL,
  orginalCode bigint(20) default NULL,
  createTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  userObjectString text,
  PRIMARY KEY  (eventId),
  KEY FK_Event_TYPE (typeId),
  CONSTRAINT FK_Event_ENTITY FOREIGN KEY(entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_Event_TYPE FOREIGN KEY (typeId) REFERENCES EventType (typeId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table Syslog */
CREATE TABLE Syslog(
   syslogId bigint(20) NOT NULL auto_increment,
   host varchar(20) NOT NULL,
   entityId bigint(20) NOT NULL,
   facility tinyint NOT NULL,
   level tinyint NOT NULL,
   time timestamp NOT NULL default CURRENT_TIMESTAMP,
   text varchar(255) NOT NULL,
   PRIMARY KEY  (syslogId),
   CONSTRAINT FK_Syslog_ENTITY FOREIGN KEY(entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table Event2Alert */
CREATE TABLE Event2Alert (
  eventTypeId int(11) NOT NULL default '0',
  alertTypeId int(11) NOT NULL,
  type tinyint NOT NULL,
  PRIMARY KEY  (eventTypeId,alertTypeId,type)
);

/*Table structure for table Connectivity */
CREATE TABLE Connectivity (
  id bigint(20) NOT NULL auto_increment,
  entityId bigint(20) default NULL,
  itemValue decimal(12,4) default NULL,
  collectTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (id),
  CONSTRAINT FK_Conn_ENTITY FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table Monitor */
CREATE TABLE Monitor (
  monitorId bigint(20) NOT NULL auto_increment,
  entityId bigint(20) default NULL,
  category varchar(64) NOT NULL,
  groupId bigint(20) default NULL,
  name varchar(64) default NULL,
  content blob COMMENT '序列化java对象',
  intervalOfNormal decimal(20,0) default NULL,
  intervalAfterError decimal(20,0) default NULL,
  note varchar(256) default NULL,
  createTime timestamp NOT NULL default '0000-00-00 00:00:00',
  modifyTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  status char(1) default NULL COMMENT '0停用, 1启用',
  available char(1) default NULL COMMENT '0不可用, 1可用',
  healthy char(1) default NULL COMMENT '0健康, 1健康',
  reason varchar(1024) default NULL,
  lastCollectTime timestamp NOT NULL default '0000-00-00 00:00:00',
  jobClass varchar(256) default NULL,
  PRIMARY KEY  (monitorId),
  CONSTRAINT FK_MONITOR_ENTITY FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table MonitorValue */
CREATE TABLE MonitorValue (
  id bigint(20) NOT NULL auto_increment,
  entityId bigint(20) default NULL,
  itemName varchar(32) default NULL,
  itemIndex int(11) default NULL COMMENT '编号为0的为平均值',
  itemValue decimal(16,4) default NULL,
  extValue decimal(16,4) default NULL,
  note varchar(64) default NULL,
  collectTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  summarized tinyint(1) default '0',
  PRIMARY KEY  (id)
);

/*Table structure for table MonitorValueDaily */
CREATE TABLE MonitorValueDaily (
  id bigint(20) NOT NULL auto_increment,
  entityId bigint(20) default NULL,
  itemName varchar(32) default NULL,
  itemIndex int(11) default NULL COMMENT '编号为0的为平均值',
  itemValue decimal(16,4) default NULL,
  itemValueMax decimal(16,4) default NULL,
  itemValueMin decimal(16,4) default NULL,
  summaryCount int(11) default NULL,
  collectTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (id)
);

/*Table structure for table MonitorValueHourly */
CREATE TABLE MonitorValueHourly (
  id bigint(20) NOT NULL auto_increment,
  entityId bigint(20) default NULL,
  itemName varchar(32) default NULL,
  itemIndex int(11) default NULL COMMENT '编号为0的为平均值',
  itemValue decimal(16,4) default NULL,
  itemValueMax decimal(16,4) default NULL,
  itemValueMin decimal(16,4) default NULL,
  summaryCount int(11) default NULL,
  collectTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  summarized tinyint(1) default '0',
  PRIMARY KEY  (id)
);


/*Table structure for table EntityAddress */
CREATE TABLE EntityAddress (
  entityId bigint(20) NOT NULL default '0',
  ip varchar(32) NOT NULL,
  PRIMARY KEY  (entityId,ip),
  CONSTRAINT FK_ENTITY_ADDRESS FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table EntityAttribute */
CREATE TABLE EntityAttribute (
  entityId bigint(20) NOT NULL,
  attributeGroup varchar(128) default 'default',
  attributeName varchar(128) default NULL,
  attributeValue varchar(256) default NULL,
  attributeNote varchar(256) default NULL,
  extValue varchar(256) default NULL,
  PRIMARY KEY  (entityId,attributeGroup,attributeName)
);

/*Table structure for table EntityCorp */
CREATE TABLE EntityCorp (
  corpId bigint(20) NOT NULL auto_increment,
  sysObjectID varchar(128) default NULL,
  name varchar(64) default NULL,
  displayName varchar(64) default NULL,
  properties blob,
  remark varchar(256) default NULL,
  PRIMARY KEY  (corpId)
);

/*Table structure for table EntityFolderRela */
CREATE TABLE EntityFolderRela (
  entityId bigint(20) NOT NULL,
  folderId bigint(20) NOT NULL,
  x double default NULL,
  y double default NULL,
  nameInFolder varchar(64) default NULL,
  iconInFolder varchar(64) default NULL,
  fixed char(1) default '0',
  visible char(1) default '1',
  groupId bigint(20) default 0,
  PRIMARY KEY  (entityId,folderId),
  CONSTRAINT FK_ENTITY_FOLDER FOREIGN KEY (folderId) REFERENCES TopoFolder (folderId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_FOLDER_ENTITY FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table EntityMonitorItem */
CREATE TABLE EntityMonitorItem (
  itemId bigint(20) NOT NULL auto_increment,
  typeId bigint(20) default NULL COMMENT '设备类型',
  itemName varchar(64) default NULL,
  name varchar(128) default NULL,
  PRIMARY KEY  (itemId)
);

/*Table structure for table EntitySnap */
CREATE TABLE EntitySnap (
  entityId bigint(20) NOT NULL default '0',
  cpu decimal(12,4) default '-1.0000' COMMENT '-1表示无数据',
  mem decimal(12,4) default '-1.0000',
  vmem decimal(12,4) default '-1.0000',
  usedMem decimal(16,4) default '-1.0000',
  disk decimal(12,4) default '-1.0000',
  usedDisk decimal(16,4) default '-1.0000',
  diskio decimal(16,4) default '-1.0000',
  processCount int(11) default '-1',
  snapTime timestamp default '0000-00-00 00:00:00',
  state tinyint(1) default NULL COMMENT '是否上线, 1为up, 0为down',
  delay int(11) default NULL,
  delayTime timestamp default '0000-00-00 00:00:00',
  sysUpTime varchar(64) default NULL,
  ext1 varchar(32) default NULL,
  ext2 varchar(32) default NULL,
  ext3 varchar(64) default NULL,
  PRIMARY KEY  (entityId),
  CONSTRAINT FK_ENTITY_SNAP FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table ResourceCategory */
CREATE TABLE ResourceCategory (
  categoryId bigint(20) NOT NULL auto_increment,
  parentId bigint(20) default 0,
  path varchar(64) default NULL,
  categoryCode varchar(32) default NULL,
  name varchar(64) default NULL,
  displayName varchar(64) default NULL,
  note varchar(256) default NULL,
  seq int(3) default 0,
  PRIMARY KEY  (categoryId)
);

/*Table structure for table EntityType */
CREATE TABLE EntityType (
  typeId bigint(20) NOT NULL auto_increment,
  name varchar(64) default NULL,
  displayName varchar(64) default NULL,
  type int(11) default NULL,
  module varchar(32) default NULL,
  modulePath varchar(64) default NULL,
  sysObjectID varchar(128) default NULL,
  icon16 varchar(64) default NULL,
  icon32 varchar(64) default NULL,
  icon48 varchar(64) default NULL,
  icon64 varchar(64) default NULL,
  properties blob,
  remark varchar(256) default NULL,
  corpId bigint(20) default '1',
  logicPane varchar(64) default NULL,
  physicalPane varchar(32) default NULL,
  categoryId bigint default 0,
  PRIMARY KEY  (typeId)
);

/*Table structure for table FolderUserGroupRela */
CREATE TABLE FolderUserGroupRela (
  folderId bigint(20) NOT NULL,
  userGroupId bigint(20) NOT NULL,
  power int(11) default NULL,
  KEY FK_GROUPS_FOLDER (folderId),
  CONSTRAINT FK_GROUPS_FOLDER FOREIGN KEY (folderId) REFERENCES TopoFolder (folderId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table GoogleEntity */
CREATE TABLE GoogleEntity (
  entityId bigint(20) NOT NULL,
  longitude decimal(17,14) default NULL,
  latitude decimal(17,14) default NULL,
  zoom int(2) default NULL,
  minZoom int(2) default NULL,
  maxZoom int(2) default NULL,
  location text(20) default NULL,
  fixed tinyint(1) default 0,
  PRIMARY KEY  (entityId),
  CONSTRAINT FK_GOOGLE_ENTITY FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table Graph */
CREATE TABLE Graph (
  graphId bigint(20) NOT NULL auto_increment,
  superiorId bigint(20) NOT NULL,
  name varchar(64) NOT NULL COMMENT '不能为空',
  type int(11) default NULL,
  editable char(1) default NULL,
  orderSeq int(11) default NULL,
  descr varchar(256) default NULL,
  systemCode varchar(32) default NULL,
  PRIMARY KEY  (graphId)
);

/*Table structure for table GraphEdge */
CREATE TABLE GraphEdge (
  edgeId bigint(20) NOT NULL auto_increment,
  name varchar(64) NOT NULL COMMENT '不能为空',
  type int(11) default NULL,
  srcVertexId bigint(20) default NULL,
  destVertexId bigint(20) default NULL,
  text varchar(32) default NULL,
  width int(11) default NULL,
  dashed char(1) default NULL,
  startArrow char(1) default NULL,
  endArrow char(1) default NULL,
  note varchar(128) default NULL,
  createTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (edgeId)
);

/*Table structure for table GraphVertex */
CREATE TABLE GraphVertex (
  vertexId bigint(20) NOT NULL auto_increment,
  graphId bigint(20) NOT NULL,
  name varchar(64) default NULL,
  icon varchar(32) NOT NULL,
  userObjId bigint(20) NOT NULL,
  userObjType int(11) default NULL,
  PRIMARY KEY  (vertexId)
);

/*Table structure for table HistoryAlert */
CREATE TABLE HistoryAlert (
  alertId bigint(20) NOT NULL auto_increment,
  typeId int(11) default NULL,
  name varchar(255) default NULL,
  levelId int(2) default NULL,
  monitorId bigint(20) default NULL COMMENT 'Entity，Port，Monitor',
  entityId bigint(20) default NULL,
  host varchar(64) default NULL COMMENT 'IP或者主机名',
  source varchar(32) default NULL COMMENT '比如端口、软件',
  message varchar(512) default NULL,
  firstTime timestamp NOT NULL default '0000-00-00 00:00:00',
  lastTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  happenTimes int(11) default NULL,
  confirmUser varchar(32) default NULL,
  confirmTime timestamp NOT NULL default '0000-00-00 00:00:00',
  confirmMessage varchar(255) default NULL,
  status int(1) default NULL COMMENT ' 0-alert,1-confirm,2-auto clear,3-manual clear',
  clearUser varchar(32) default NULL,
  clearTime timestamp NOT NULL default '0000-00-00 00:00:00',
  clearMessage varchar(255) default NULL,
  extend1 varchar(32) default NULL,
  userObject blob,
  PRIMARY KEY  (alertId),
  CONSTRAINT FK_HistoryAlert_ENTITY FOREIGN KEY(entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table HostService */
CREATE TABLE HostService (
  serviceId bigint(20) NOT NULL auto_increment,
  entityId bigint(20) default NULL,
  port int(11) default NULL,
  protocol varchar(16) default NULL,
  state int(11) default NULL,
  name varchar(64) default NULL,
  version varchar(64) default NULL,
  PRIMARY KEY  (serviceId)
);

/*Table structure for table InterceptorLog */
CREATE TABLE InterceptorLog (
  logId bigint(20) NOT NULL auto_increment,
  entityId bigint(20) default NULL,
  ifIndex int(11) default NULL,
  auto char(1) default NULL,
  result char(1) default NULL,
  reason varchar(64) default NULL,
  description varchar(255) default NULL,
  interceptorTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (logId)
);

/*Table structure for table Link */
CREATE TABLE Link (
  linkId bigint(20) NOT NULL auto_increment,
  name varchar(64) NOT NULL COMMENT '不能为空',
  type varchar(64) default NULL,
  connectType int(2) default 5,
  ifSpeed decimal(16,0) default 0,
  srcEntityId bigint(20) default NULL,
  srcIfIndex bigint(20) default NULL,
  destEntityId bigint(20) default NULL,
  destIfIndex bigint(20) default NULL,
  width int(11) default NULL,
  dashed char(1) default NULL,
  startArrow char(1) default NULL,
  endArrow char(1) default NULL,
  srcIfSpeed decimal(16,0) default NULL,
  destIfSpeed decimal(16,0) default NULL,
  srcIfOctets decimal(16,4) default NULL,
  destIfOctets decimal(16,4) default NULL,
  srcIfOctetsRate decimal(16,4) default NULL,
  destIfOctetsRate decimal(16,4) default NULL,
  linkIfOctets decimal(16,4) default NULL,
  linkIfOctetsRate decimal(16,4) default NULL,
  note varchar(64) default NULL,
  createTime timestamp NOT NULL default '0000-00-00 00:00:00',
  modifyTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  startArrowStyle varchar(16) default 'none',
  endArrowStyle varchar(16) default 'none',
  strokeColor varchar(7) default '#ffffff',
  strokeWeight varchar(3) default '1',
  PRIMARY KEY  (linkId)
);

/*Table structure for table LogicPaneIfIndex */
CREATE TABLE LogicPaneIfIndex (
  typeId bigint(20) NOT NULL default '0',
  ifIndex int(11) NOT NULL default '0',
  type int(11) default NULL,
  x varchar(16) default NULL,
  y varchar(16) default NULL,
  PRIMARY KEY  (typeId,ifIndex)
);

/*Table structure for table MapNode */
CREATE TABLE MapNode (
  nodeId bigint(20) NOT NULL auto_increment,
  name varchar(64) default NULL,
  folderId bigint(20) default NULL,
  type int(11) default NULL,
  url varchar(256) default NULL,
  text varchar(256) default NULL,
  href varchar(64) default NULL,
  x int(11) default NULL,
  y int(11) default NULL,
  width int(11) default NULL,
  height int(11) default NULL,
  icon varchar(256) default NULL,
  fixed char(1) default '0',
  dashed char(1) default '0',
  startArrow char(1) default '0',
  endArrow char(1) default '0',
  strokeColor varchar(7) default NULL,
  strokeWeight int(11) default NULL,
  fillColor varchar(7) default NULL,
  shadow char(1) default NULL,
  shadowColor varchar(7) default NULL,
  shadowOffset int(11) default NULL,
  gradient char(1) default '0',
  textColor varchar(7) default NULL,
  fontFamily varchar(16) default NULL,
  fontColor varchar(7) default NULL,
  fontSize int(11) default NULL,
  fontStyle varchar(16) default NULL,
  fontWeight varchar(16) default NULL,
  points varchar(256) default NULL,
  expanded char(1) default '1',
  groupId bigint default 0,
  userObjType   int default 0,
  userObjId     bigint default 0,
  gradientColor varchar(7) default '#000000',
  PRIMARY KEY  (nodeId),
  KEY FK_NODE_FOLDER (folderId,name),
  CONSTRAINT FK_NODE_FOLDER FOREIGN KEY (folderId) REFERENCES TopoFolder (folderId) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT='拓扑图结点表，用于存储画图板中的结点';

/*Table structure for table Port */
CREATE TABLE Port (
  portId bigint(20) NOT NULL auto_increment,
  entityId bigint(20) default NULL,
  name varchar(64) default NULL,
  note varchar(255) default NULL,
  card bigint(20) default '-1',
  ifIndex bigint(20) NOT NULL default '0',
  ifDescr varchar(255) default NULL,
  ifType decimal(12,0) default NULL,
  ifMtu decimal(12,0) default NULL,
  ifSpeed decimal(12,0) default NULL,
  ifPhysAddress varchar(32) default NULL,
  ifAdminStatus tinyint(1) default NULL,
  ifOperStatus tinyint(1) default NULL,
  ifLastChange varchar(32) default NULL,
  ifName varchar(64) NOT NULL COMMENT '不能为空',
  ifAlias varchar(64) default NULL,
  x varchar(16) default NULL,
  y varchar(16) default NULL,
  PRIMARY KEY  (portId),
  KEY FK_ENTITY_PORT (entityId),
  CONSTRAINT FK_ENTITY_PORT FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table PortPerf */
CREATE TABLE PortPerf (
  id bigint(20) NOT NULL auto_increment,
  entityId bigint(20) NOT NULL,
  ifIndex int(11) default NULL,
  ifOctets decimal(16,4) default NULL,
  ifOctetsRate decimal(16,4) default NULL,
  ifInOctets decimal(16,4) default NULL,
  ifInOctetsRate decimal(16,4) default NULL,
  ifOutOctets decimal(16,4) default NULL,
  ifOutOctetsRate decimal(16,4) default NULL,
  ifUcastPkts decimal(16,4) default NULL,
  ifInUcastPkts decimal(16,4) default NULL,
  ifOutUcastPkts decimal(16,4) default NULL,
  ifNUcastPkts decimal(16,4) default NULL,
  ifInNUcastPkts decimal(16,4) default NULL,
  ifOutNUcastPkts decimal(16,4) default NULL,
  ifErrors decimal(16,4) default NULL,
  ifErrorsRate decimal(16,4) default NULL,
  ifInErrors decimal(16,4) default NULL,
  ifInErrorsRate decimal(16,4) default NULL,
  ifOutErrors decimal(16,4) default NULL,
  ifOutErrorsRate decimal(16,4) default NULL,
  ifDiscards decimal(16,4) default NULL,
  ifDiscardsRate decimal(16,4) default NULL,
  ifInDiscards decimal(16,4) default NULL,
  ifInDiscardsRate decimal(16,4) default NULL,
  ifOutDiscards decimal(16,4) default NULL,
  ifOutDiscardsRate decimal(16,4) default NULL,
  intervalSeconds int(11) default NULL,
  collectTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  summarized tinyint(1) default '0',
  PRIMARY KEY  (id)
);

/*Table structure for table PortPerfDaily */
CREATE TABLE PortPerfDaily (
  id bigint(20) NOT NULL auto_increment,
  entityId bigint(20) NOT NULL,
  ifIndex int(11) default NULL,
  ifDiscards decimal(16,4) default NULL,
  ifDiscardsMax decimal(16,4) default NULL,
  ifDiscardsMin decimal(16,4) default NULL,
  ifDiscardsRate decimal(16,4) default NULL,
  ifDiscardsRateMax decimal(16,4) default NULL,
  ifDiscardsRateMin decimal(16,4) default NULL,
  ifErrors decimal(16,4) default NULL,
  ifErrorsMax decimal(16,4) default NULL,
  ifErrorsMin decimal(16,4) default NULL,
  ifErrorsRate decimal(16,4) default NULL,
  ifErrorsRateMax decimal(16,4) default NULL,
  ifErrorsRateMin decimal(16,4) default NULL,
  ifInDiscards decimal(16,4) default NULL,
  ifInDiscardsMax decimal(16,4) default NULL,
  ifInDiscardsMin decimal(16,4) default NULL,
  ifInDiscardsRate decimal(16,4) default NULL,
  ifInDiscardsRateMax decimal(16,4) default NULL,
  ifInDiscardsRateMin decimal(16,4) default NULL,
  ifInErrors decimal(16,4) default NULL,
  ifInErrorsMax decimal(16,4) default NULL,
  ifInErrorsMin decimal(16,4) default NULL,
  ifInErrorsRate decimal(16,4) default NULL,
  ifInErrorsRateMax decimal(16,4) default NULL,
  ifInErrorsRateMin decimal(16,4) default NULL,
  ifInNUcastPkts decimal(16,4) default NULL,
  ifInNUcastPktsMax decimal(16,4) default NULL,
  ifInNUcastPktsMin decimal(16,4) default NULL,
  ifInOctets decimal(16,4) default NULL,
  ifInOctetsMax decimal(16,4) default NULL,
  ifInOctetsMin decimal(16,4) default NULL,
  ifInOctetsRate decimal(16,4) default NULL,
  ifInOctetsRateMax decimal(16,4) default NULL,
  ifInOctetsRateMin decimal(16,4) default NULL,
  ifInUcastPkts decimal(16,4) default NULL,
  ifInUcastPktsMax decimal(16,4) default NULL,
  ifInUcastPktsMin decimal(16,4) default NULL,
  ifNUcastPkts decimal(16,4) default NULL,
  ifNUcastPktsMax decimal(16,4) default NULL,
  ifNUcastPktsMin decimal(16,4) default NULL,
  ifOctets decimal(16,4) default NULL,
  ifOctetsMax decimal(16,4) default NULL,
  ifOctetsMin decimal(16,4) default NULL,
  ifOctetsRate decimal(16,4) default NULL,
  ifOctetsRateMax decimal(16,4) default NULL,
  ifOctetsRateMin decimal(16,4) default NULL,
  ifOutDiscards decimal(16,4) default NULL,
  ifOutDiscardsMax decimal(16,4) default NULL,
  ifOutDiscardsMin decimal(16,4) default NULL,
  ifOutDiscardsRate decimal(16,4) default NULL,
  ifOutDiscardsRateMax decimal(16,4) default NULL,
  ifOutDiscardsRateMin decimal(16,4) default NULL,
  ifOutErrors decimal(16,4) default NULL,
  ifOutErrorsMax decimal(16,4) default NULL,
  ifOutErrorsMin decimal(16,4) default NULL,
  ifOutErrorsRate decimal(16,4) default NULL,
  ifOutErrorsRateMax decimal(16,4) default NULL,
  ifOutErrorsRateMin decimal(16,4) default NULL,
  ifOutNUcastPkts decimal(16,4) default NULL,
  ifOutNUcastPktsMax decimal(16,4) default NULL,
  ifOutNUcastPktsMin decimal(16,4) default NULL,
  ifOutOctets decimal(16,4) default NULL,
  ifOutOctetsMax decimal(16,4) default NULL,
  ifOutOctetsMin decimal(16,4) default NULL,
  ifOutOctetsRate decimal(16,4) default NULL,
  ifOutOctetsRateMax decimal(16,4) default NULL,
  ifOutOctetsRateMin decimal(16,4) default NULL,
  ifOutUcastPkts decimal(16,4) default NULL,
  ifOutUcastPktsMax decimal(16,4) default NULL,
  ifOutUcastPktsMin decimal(16,4) default NULL,
  ifUcastPkts decimal(16,4) default NULL,
  ifUcastPktsMax decimal(16,4) default NULL,
  ifUcastPktsMin decimal(16,4) default NULL,
  collectTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  summaryCount int(11) default NULL,
  PRIMARY KEY  (id)
);

/*Table structure for table PortPerfHourly */
CREATE TABLE PortPerfHourly (
  id bigint(20) NOT NULL auto_increment,
  entityId bigint(20) NOT NULL,
  ifIndex int(11) default NULL,
  ifDiscards decimal(16,4) default NULL,
  ifDiscardsMax decimal(16,4) default NULL,
  ifDiscardsMin decimal(16,4) default NULL,
  ifDiscardsRate decimal(16,4) default NULL,
  ifDiscardsRateMax decimal(16,4) default NULL,
  ifDiscardsRateMin decimal(16,4) default NULL,
  ifErrors decimal(16,4) default NULL,
  ifErrorsMax decimal(16,4) default NULL,
  ifErrorsMin decimal(16,4) default NULL,
  ifErrorsRate decimal(16,4) default NULL,
  ifErrorsRateMax decimal(16,4) default NULL,
  ifErrorsRateMin decimal(16,4) default NULL,
  ifInDiscards decimal(16,4) default NULL,
  ifInDiscardsMax decimal(16,4) default NULL,
  ifInDiscardsMin decimal(16,4) default NULL,
  ifInDiscardsRate decimal(16,4) default NULL,
  ifInDiscardsRateMax decimal(16,4) default NULL,
  ifInDiscardsRateMin decimal(16,4) default NULL,
  ifInErrors decimal(16,4) default NULL,
  ifInErrorsMax decimal(16,4) default NULL,
  ifInErrorsMin decimal(16,4) default NULL,
  ifInErrorsRate decimal(16,4) default NULL,
  ifInErrorsRateMax decimal(16,4) default NULL,
  ifInErrorsRateMin decimal(16,4) default NULL,
  ifInNUcastPkts decimal(16,4) default NULL,
  ifInNUcastPktsMax decimal(16,4) default NULL,
  ifInNUcastPktsMin decimal(16,4) default NULL,
  ifInOctets decimal(16,4) default NULL,
  ifInOctetsMax decimal(16,4) default NULL,
  ifInOctetsMin decimal(16,4) default NULL,
  ifInOctetsRate decimal(16,4) default NULL,
  ifInOctetsRateMax decimal(16,4) default NULL,
  ifInOctetsRateMin decimal(16,4) default NULL,
  ifInUcastPkts decimal(16,4) default NULL,
  ifInUcastPktsMax decimal(16,4) default NULL,
  ifInUcastPktsMin decimal(16,4) default NULL,
  ifNUcastPkts decimal(16,4) default NULL,
  ifNUcastPktsMax decimal(16,4) default NULL,
  ifNUcastPktsMin decimal(16,4) default NULL,
  ifOctets decimal(16,4) default NULL,
  ifOctetsMax decimal(16,4) default NULL,
  ifOctetsMin decimal(16,4) default NULL,
  ifOctetsRate decimal(16,4) default NULL,
  ifOctetsRateMax decimal(16,4) default NULL,
  ifOctetsRateMin decimal(16,4) default NULL,
  ifOutDiscards decimal(16,4) default NULL,
  ifOutDiscardsMax decimal(16,4) default NULL,
  ifOutDiscardsMin decimal(16,4) default NULL,
  ifOutDiscardsRate decimal(16,4) default NULL,
  ifOutDiscardsRateMax decimal(16,4) default NULL,
  ifOutDiscardsRateMin decimal(16,4) default NULL,
  ifOutErrors decimal(16,4) default NULL,
  ifOutErrorsMax decimal(16,4) default NULL,
  ifOutErrorsMin decimal(16,4) default NULL,
  ifOutErrorsRate decimal(16,4) default NULL,
  ifOutErrorsRateMax decimal(16,4) default NULL,
  ifOutErrorsRateMin decimal(16,4) default NULL,
  ifOutNUcastPkts decimal(16,4) default NULL,
  ifOutNUcastPktsMax decimal(16,4) default NULL,
  ifOutNUcastPktsMin decimal(16,4) default NULL,
  ifOutOctets decimal(16,4) default NULL,
  ifOutOctetsMax decimal(16,4) default NULL,
  ifOutOctetsMin decimal(16,4) default NULL,
  ifOutOctetsRate decimal(16,4) default NULL,
  ifOutOctetsRateMax decimal(16,4) default NULL,
  ifOutOctetsRateMin decimal(16,4) default NULL,
  ifOutUcastPkts decimal(16,4) default NULL,
  ifOutUcastPktsMax decimal(16,4) default NULL,
  ifOutUcastPktsMin decimal(16,4) default NULL,
  ifUcastPkts decimal(16,4) default NULL,
  ifUcastPktsMax decimal(16,4) default NULL,
  ifUcastPktsMin decimal(16,4) default NULL,
  collectTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  summaryCount int(11) default NULL,
  summarized tinyint(1) default '0',
  PRIMARY KEY  (id)
);

/*Table structure for table PortSnap */
CREATE TABLE PortSnap (
  entityId bigint(20) NOT NULL,
  ifIndex int(11) NOT NULL default '0',
  ifOctets decimal(16,4) default NULL,
  ifOctetsRate decimal(16,4) default NULL,
  ifInOctets decimal(16,4) default NULL,
  ifInOctetsRate decimal(16,4) default NULL,
  ifOutOctets decimal(16,4) default NULL,
  ifOutOctetsRate decimal(16,4) default NULL,
  ifUcastPkts decimal(16,4) default NULL,
  ifInUcastPkts decimal(16,4) default NULL,
  ifOutUcastPkts decimal(16,4) default NULL,
  ifNUcastPkts decimal(16,4) default NULL,
  ifInNUcastPkts decimal(16,4) default NULL,
  ifOutNUcastPkts decimal(16,4) default NULL,
  ifErrors decimal(16,4) default NULL,
  ifErrorsRate decimal(16,4) default NULL,
  ifInErrors decimal(16,4) default NULL,
  ifInErrorsRate decimal(16,4) default NULL,
  ifOutErrors decimal(16,4) default NULL,
  ifOutErrorsRate decimal(16,4) default NULL,
  ifDiscards decimal(16,4) default NULL,
  ifDiscardsRate decimal(16,4) default NULL,
  ifInDiscards decimal(16,4) default NULL,
  ifInDiscardsRate decimal(16,4) default NULL,
  ifOutDiscards decimal(16,4) default NULL,
  ifOutDiscardsRate decimal(16,4) default NULL,
  collectTime timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (entityId,ifIndex),
  CONSTRAINT FK_PORT_SNAP FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table Services */
CREATE TABLE Services (
  name varchar(64) NOT NULL,
  port int(11) default NULL,
  timeout int(11) default NULL COMMENT '秒为单位',
  protocol varchar(16) default NULL COMMENT 'tcp/udp',
  note varchar(128) default NULL,
  scaned char(1) default NULL COMMENT '在发现设备的时候是否扫描该服务',
  type int(11) default NULL,
  PRIMARY KEY  (name)
);

/*Table structure for table SnmpParam */
CREATE TABLE SnmpParam (
  entityId bigint(20) NOT NULL,
  version int(11) default NULL,
  timeout int(10) default NULL,
  retry int(10) default NULL,
  port int(10) default NULL,
  mibs text(20) default NULL,
  community varchar(128) default NULL,
  writeCommunity varchar(128) default NULL,
  username varchar(32) default NULL,
  securityLevel int(11) default NULL,
  authProtocol varchar(32) default NULL,
  privProtocol varchar(32) default NULL,
  authPassword varchar(128) default NULL,
  privPassword varchar(128) default NULL,
  authoritativeEngineID varchar(128) default NULL,
  contextName varchar(128) default NULL,
  contextId varchar(128) default NULL,
  PRIMARY KEY  (entityId),
  CONSTRAINT FK_ENTITY_SNMPPARAM FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table Threshold */
CREATE TABLE Threshold (
  thresholdId bigint(20) NOT NULL auto_increment,
  name varchar(128) NOT NULL,
  type int(1) default NULL,
  defaultThreshold char(1) default NULL,
  description varchar(512) default NULL,
  operator1 int(2) default NULL,
  threshold1 varchar(64) default NULL,
  alertLevel1 int(1) default NULL,
  count1 int(11) default NULL,
  alertDesc1 varchar(512) default NULL,
  operator2 int(2) default NULL,
  threshold2 varchar(64) default NULL,
  alertLevel2 int(1) default NULL,
  count2 int(11) default NULL,
  alertDesc2 varchar(512) default NULL,
  PRIMARY KEY  (thresholdId)
);

/*Table structure for table ThresholdEntityRela */
CREATE TABLE ThresholdEntityRela (
  entityId bigint(20) default NULL,
  objectId bigint(20) default NULL,
  itemName varchar(64) default NULL,
  thresholdId bigint(20) default NULL,
  KEY FK_ENTITY_THR (thresholdId),
  KEY FK_THR_ENTITY (entityId),
  CONSTRAINT FK_THR_ENTITY FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_ENTITY_THR FOREIGN KEY (thresholdId) REFERENCES Threshold (thresholdId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table ThresholdMonitorRela */
CREATE TABLE ThresholdMonitorRela (
  monitorId bigint(20) default NULL,
  thresholdId bigint(20) default NULL,
  itemName varchar(64) default NULL,
  objectId bigint(20) default NULL,
  KEY FK_RELA_MONITOR (monitorId),
  KEY FK_RELA_THRESHOLD (thresholdId),
  CONSTRAINT FK_RELA_THRESHOLD FOREIGN KEY (thresholdId) REFERENCES Threshold (thresholdId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_RELA_MONITOR FOREIGN KEY (monitorId) REFERENCES Monitor (monitorId) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table TopoLabel */
CREATE TABLE TopoLabel (
  labelId varchar(32) NOT NULL,
  displayName varchar(32) NOT NULL,
  value bigint(20) NOT NULL,
  color varchar(16) NOT NULL,
  module varchar(32) NOT NULL,
  folderId bigint(20) default NULL,
  PRIMARY KEY  (labelId)
);

/*Table structure for table TopologyParam */
CREATE TABLE TopologyParam (
  category varchar(32) NOT NULL default 'topology',
  name varchar(32) NOT NULL,
  value varchar(128) default NULL,
  PRIMARY KEY  (category,name)
);

/*Table structure for table SoftwareInstalled */
CREATE TABLE SoftwareInstalled (
  softwareId bigint(20) default NULL,
  softwareIndex int(11) NOT NULL,
  name varchar(128) default NULL,
  productId varchar(32) default NULL,
  type int default 0,
  entityId bigint(20) default NULL,
  note varchar(128) default NULL,
  installTime varchar(32) default NULL,
  PRIMARY KEY  (softwareId)
);


/*Table structure for table RunProcess */
CREATE TABLE RunProcess (
  processId bigint(20) default NULL,
  entityId bigint(20) default NULL,
  name varchar(128) default NULL,
  path varchar(256) default NULL,
  productId varchar(32) default NULL,
  type int default 0,
  status int default 0,
  cpuUsage varchar(32) default NULL,
  memUsage varchar(32) default NULL,
  createTime varchar(32) default NULL,
  PRIMARY KEY  (processId)
);

/*Table structure for table MacPrefixes */
CREATE TABLE MacPrefixes (
  prefix varchar(16) NOT NULL,
  corp varchar(128) default NULL,
  corp_zh varchar(128) default NULL,
  type varchar(16) default NULL,
  note varchar(128) default NULL,
  PRIMARY KEY  (prefix)
);


/* -- version 1.0.0,build 2011-3-31,module server */

-- version 1.0.0,build 2012-2-24,module server
create table virtualNetwork(
  virtualNetId bigint(20) not null AUTO_INCREMENT,
  folderId  bigint(20) not null,
  virtualName varchar(100) default null,
  virtualType int(4) default 1,
  createTime  timestamp DEFAULT '2000-01-01 00:00:00',
  modifyTime  timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  x int(4) default null,
  y int(4) default null,
  width int(4) default null,
  height int(4) default null,
  zoom int(4) default null,
  icon varchar(255) default null,
  fixed char(1) default '0',
  visiable tinyint default 1,
  primary key(virtualNetId),
  constraint FK_VIRTUAL_FOLDER foreign key(folderId) references topofolder(folderId) on delete cascade on update cascade
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

create table virtualProductRelation(
   virtualNetId bigint(20) not null,
   productId bigint(20) not null,
   productType  varchar(10) not null,
   productName varchar(30) not null,
   productIp  varchar(20) not null,
   createTime timestamp not null,
   primary key(productId,virtualNetId,productType),
   constraint FK_VIRTUAL_RELATION foreign key(virtualNetId) references virtualNetwork(virtualNetId) on delete cascade on update cascade
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

create table productSnmpParam(
   productId bigint(20) not null,
   productType varchar(20) not null,
   productIp varchar(30) not null,
   version int(11) default null,
   timeout int(10) default null,
   retry  int(10) default null,
   port int(10) default null,
   mibs varchar(64) default null,
   readCommunity varchar(128) default null,
   writeCommunity varchar(128) default null,
   username varchar(32) default null,
   securityLevel int(11) default null,
   authProtocol varchar(32) default null,
   privProtocol varchar(32) default null,
   authoritativeEngineId varchar(128) default null,
   contextName varchar(128) default null,
   contextId varchar(128) default null,
   primary key(productId),
   constraint FK_PRODUCT_SNMPPARAM foreign key(productId) references virtualProductRelation(productId) on delete cascade on update cascade
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

create table EntityProductRelation(
   entityId  bigint(20) not null,
   productId bigint(20) not null,
   productType varchar(100),
   primary key(entityId,productId,productType),
   constraint FK_EntityProductRelation_ENTITY foreign key(entityId) references entity(entityId) on delete cascade on update cascade
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

/* -- version 1.0.0,build 2012-2-24,module server */

-- version 1.6.6,build 2012-5-4,module server
CREATE TABLE PerfMonitor (
  monitorId bigint(20) NOT NULL auto_increment,
  identifyKey bigint(20)  NOT NULL ,
  category varchar(64) NOT NULL,
  domain blob COMMENT '序列化java对象',
  snmpParam blob COMMENT '序列化SnmpParam对象',
  initialDelay decimal(20,0) default NULL,
  period decimal(20,0) default NULL,
  scheduleType tinyint,
  isStartUpWithServer  tinyint,
  createTime timestamp NOT NULL default '0000-00-00 00:00:00',
  lastCollectTime timestamp NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (monitorId)
);
/* -- version 1.6.6,build 2012-5-4,module server */

-- version 1.7.1,build 2012-6-25,module server

CREATE TABLE UserAttention (
  userId bigint(20) NOT NULL,
  entityId bigint(20) NOT NULL,
  PRIMARY KEY  (userId,entityId),
  KEY FK_USER_ID (userId),
  KEY FK_ENTITY_ID (entityId),
  CONSTRAINT FK_USER_ID FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_ENTITY_ID FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Usercustomlization (
   userId bigint(20) NOT NULL DEFAULT '0',
   functionName varchar(255) DEFAULT NULL,
   functionAction varchar(255) DEFAULT NULL,
   icon varchar(255) DEFAULT NULL
);

/* -- version 1.7.1,build 2012-6-25,module server */


-- version 1.7.7.4,build 2012-11-28,module server
CREATE TABLE usermibbles (
  userId bigint(20) DEFAULT NULL,
  mib varchar(255) NOT NULL DEFAULT ''
);
/* -- version 1.7.7.4,build 2012-11-28,module server */

-- version 1.7.10.0,build 2013-01-09,module server
/* snmp v3 user for agent **/
CREATE TABLE snmpv3userTable (
  entityId bigint(20) NOT NULL ,
  snmpSecurityName varchar(255) NOT NULL,
  snmpUserEngineId varchar(255) DEFAULT NULL,
  snmpUserName varchar(255) DEFAULT NULL,
  snmpAuthProtocol varchar(255) DEFAULT NULL,
  snmpAuthKeyChange varchar(255) DEFAULT NULL COMMENT '刷新时不更新',
  snmpPrivProtocol varchar(255) DEFAULT NULL,
  snmpPrivKeyChange varchar(255) DEFAULT NULL COMMENT '刷新时不更新',
  snmpUserPublic varchar(255) DEFAULT NULL,
  snmpGroupName varchar(255) DEFAULT NULL,
  snmpSecurityMode int(11) DEFAULT NULL,
  PRIMARY KEY (entityId,snmpUserName,snmpUserEngineId),
  constraint FK_snmpv3userTable_ENTITY foreign key(entityId) references entity(entityId) on delete cascade on update cascade
);

/** Snmp v3 Group  table **/
CREATE TABLE snmpv3AccessTable (
  entityId bigint(20) NOT NULL ,
  snmpSecurityMode int(11) DEFAULT NULL,
  snmpGroupName varchar(255) DEFAULT NULL,
  snmpContextPrefix varchar(255) DEFAULT NULL,
  snmpSecurityLevel int(11) DEFAULT NULL,
  snmpContextMatch int(11) DEFAULT NULL,
  snmpReadView varchar(255) DEFAULT NULL,
  snmpWriteView varchar(255) DEFAULT NULL,
  snmpNotifyView varchar(255) DEFAULT NULL,
  PRIMARY KEY (entityId,snmpSecurityMode,snmpGroupName,snmpSecurityLevel),
  constraint FK_snmpv3AccessTable_ENTITY foreign key(entityId) references entity(entityId) on delete cascade on update cascade
);

/* snmp v3 view table **/
CREATE TABLE snmpv3ViewTable (
  entityId bigint(20) NOT NULL ,
  snmpViewName varchar(255) NOT NULL ,
  snmpViewSubtree varchar(255) NOT NULL,
  snmpViewMask varchar(255) DEFAULT NULL,
  snmpViewMode int(11) DEFAULT NULL,
  PRIMARY KEY (entityId,snmpViewName,snmpViewSubtree),
  constraint FK_snmpv3ViewTable_ENTITY foreign key(entityId) references entity(entityId) on delete cascade on update cascade
);

/* snmp v3 SnmpTarget table **/
CREATE TABLE SnmpTargetTable (
        entityId bigint(20) NOT NULL,
        targetName varchar(255) NOT NULL,
        targetDomain varchar(255),
        targetAddress varchar(255),
        targetTimeout bigint(20),
        targetRetryCount int(11),
        targetTagList varchar(255),
        targetParams varchar(255),
        targetStorageType int(11),
        PRIMARY KEY (entityId, targetName)
);

/* snmp v3 SnmpTargetParams table **/
CREATE TABLE SnmpTargetParams (
        entityId bigint(20) NOT NULL,
        targetParamsName varchar(255) NOT NULL,
        targetParamsMPModel bigint(20),
        targetParamsSecurityModel bigint(20),
        targetParamsSecurityName varchar(255),
        targetParamsSecurityLevel int(11),
        targetParamsStorageType int(11),
        PRIMARY KEY (entityId, targetParamsName)
);

/* snmp v3 SnmpNotify table **/
CREATE TABLE SnmpNotifyTable (
        entityId bigint(20) NOT NULL,
        notifyName varchar(255) NOT NULL,
        notifyTag varchar(255),
        notifyType int(11),
        notifyStorageType int(11),
        PRIMARY KEY (entityId, notifyName)
);

/* snmp v3 SnmpNotifyFilterProfile table **/
CREATE TABLE SnmpNotifyFilterProfile (
        entityId bigint(20) NOT NULL,
        targetParamsName varchar(255) NOT NULL,
        notifyFilterProfileName varchar(255),
        notifyFilterProfileStorType int(11),
        PRIMARY KEY (entityId, targetParamsName)
);

/* snmp v3 SnmpNotifyFilter table **/
CREATE TABLE SnmpNotifyFilterTable (
        entityId bigint(20) NOT NULL,
        notifyFilterProfileName varchar(255) NOT NULL,
        notifyFilterSubtree varchar(255) NOT NULL,
        notifyFilterMask varchar(255),
        notifyFilterType int(11),
        notifyFilterStorType int(11),
        PRIMARY KEY (entityId, notifyFilterProfileName, notifyFilterSubtree)
);
/* -- version 1.7.10.0,build 2013-01-09,module server */

/* modify by victor@20130224 修改登录名区分大小写*/
-- version 1.7.10.0,build 2013-02-26,module server
ALTER TABLE users MODIFY COLUMN userName VARCHAR(32) BINARY CHARACTER SET utf8 COLLATE utf8_bin NOT NULL;

/* -- version 1.7.10.0,build 2013-02-26,module server */

-- version 1.7.12.1,build 2013-04-10,module server
ALTER TABLE Entity ADD COLUMN lastRefreshTime timestamp NOT NULL;
ALTER TABLE Entity ADD COLUMN authorityUserId int;
/* -- version 1.7.12.1,build 2013-04-10,module server */

-- version 1.7.13.2,build 2013-04-24,module server
alter table perfmonitor modify lastCollectTime timestamp on UPDATE CURRENT_TIMESTAMP;
alter table monitor modify lastCollectTime timestamp null;
/* -- version 1.7.13.2,build 2013-04-29,module server */

-- version 1.7.15.0,build 2013-06-07,module server
ALTER TABLE entity MODIFY COLUMN sysName VARCHAR(255);
ALTER TABLE entity MODIFY COLUMN sysContact VARCHAR(255);
ALTER TABLE entity MODIFY COLUMN sysLocation VARCHAR(255);
/* -- version 1.7.15.0,build 2013-06-07,module server */

-- version 1.7.15.0,build 2013-06-08,module server
create table perfThresholdTemplate(
	templateId int(4) not null auto_increment,
	templateName varchar(32) not null unique,
	templateType int(11) not null,
	createUser varchar(32) not null,
	isDefaultTemplate tinyint not null,
	createTime timestamp not null default '0000-00-00 00:00:00',
	modifyTime timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	primary key(templateId)
);

create table perfTarget(
	targetId varchar(20) not null,
	targetType int not null,
	targetDisplayName varchar(100),
	targetGroup varchar(100),
	unit varchar(50),
	primary key(targetId)
);

create table perfThresholdRule(
	ruleId int(4) not null auto_increment,
	targetId varchar(20) not null,
	templateId int(4) not null,
	thresholds varchar(200) not null,
	minuteLength int not null,
	number int not null,
	isTimeLimit tinyint not null,
	timeRange varchar(100),
	primary key(ruleId),
	CONSTRAINT FK_TARGET_RULE FOREIGN KEY (targetId) REFERENCES perfTarget(targetId) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_TEMPLATE_RULE FOREIGN KEY (templateId) REFERENCES perfThresholdTemplate(templateId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfTemplateEntityRelation(
	entityId bigint(20) not null,
	templateId int not null,
	isPerfThreshold tinyint default 0,
	primary key(entityId),
	CONSTRAINT FK_PERF_TEMPLATE FOREIGN KEY (templateId) REFERENCES perfThresholdTemplate(templateId) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_PERF_ENTITY FOREIGN KEY (entityId) REFERENCES Entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table perfGlobal(
	flag varchar(10) not null,
	isPerfOn tinyint not null,
	isRelationWithDefaultTemplate tinyint not null,
    defaultTemplateId int(10) not null,
	isPerfThreshold tinyint not null,
	defaultCollectTime int not null,
	primary key(flag)
);

/* -- version 1.7.15.0,build 2013-06-08,module server */

-- version 1.7.15.0,build 2013-6-16,module server
ALTER table ReportTemplate add COLUMN (taskUrl varchar(64));
/* -- version 1.7.15.0,build 2013-6-16,module server */

-- version 1.7.15.0,build 2013-6-18,module server
ALTER table ReportTemplate drop COLUMN taskUrl;

/*==============================================================*/
/* Table: reporttask                                            */
/*==============================================================*/
create table reporttask
(
   taskId               bigint(20) not null auto_increment,
   note                 varchar(256),
   templateId           bigint(20),
   taskName             varchar(128),
   email                varchar(256),
   state                char(1),
   cycleType            int,
   cronExpression       varchar(256),
   conditions           blob,
   pdfEnabled           char(1),
   excelEnabled         char(1),
   htmlEnabled          char(1),
   primary key (taskId)
);

/*==============================================================*/
/* Table: reportInstances                                                */
/*==============================================================*/
create table reportInstances
(
   reportId             bigint(20) not null auto_increment,
   reportName           varchar(128),
   note                 varchar(256),
   createTime           timestamp,
   fileType             int,
   filePath             varchar(256),
   taskId               bigint(20),
   primary key (reportId)
);
/* -- version 1.7.15.0,build 2013-6-18,module server */

-- version 1.7.15.0,build 2013-6-27,module server
ALTER table reporttask add COLUMN (userId bigint(20));
ALTER table reportInstances add COLUMN (userId bigint(20));
/* -- version 1.7.15.0,build 2013-6-27,module server */

-- version 1.7.15.0,build 2013-07-20,module server
create table GlobalCollectTimeTable(
	perfTargetName varchar(20) not null,
	type tinyint not null,
	interval_ int not null,
	enable tinyint not null,
	targetGroup varchar(20) not null,
	primary key(perfTargetName,type)
);

create table DeviceCollectTimeTable(
	entityId bigint(20) not null,
	perfTargetName varchar(20) not null,
	interval_ int not null,
	enable tinyint not null,
    type tinyint not null,
	primary key(entityId,perfTargetName),
	CONSTRAINT FK_DeviceCollectTime_entityId FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 1.7.15.0,build 2013-07-20,module server */

-- version 2.0.0.0,build 2013-09-30,module server
/*-- plase don't delete this DROP command*/
DROP TABLE IF EXISTS billboard; 
CREATE TABLE billboard (
	noticeId  int(11) NOT NULL AUTO_INCREMENT,
	userId  bigint(20) NOT NULL ,
	createTime  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	startTime  timestamp NOT NULL ,
	deadline  timestamp NOT NULL ,
	status  tinyint(4) NOT NULL DEFAULT 1,
	content  varchar(255)  NOT NULL ,
	PRIMARY KEY (noticeId),
	INDEX userId_index (userId, status) USING BTREE
);
/*-- version 2.0.0.0,build 2013-09-30,module server */


-- version 2.0.0.0,build 2014-01-05,module server
alter table link add constraint FK_LINK_ENTITY_SRC foreign key(srcEntityId) references entity(entityId) on delete cascade on update cascade;

alter table link add constraint FK_LINK_ENTITY_DEST foreign key(destEntityId) references entity(entityId) on delete cascade on update cascade;
/* -- version 2.0.0.0,build 2014-01-05,module server */

-- version 2.0.0.0,build 2014-3-4,module server
ALTER TABLE entitytype DROP COLUMN type;
ALTER TABLE entity DROP COLUMN type;
DROP TABLE IF EXISTS EntityTypeRelation;
CREATE TABLE EntityTypeRelation (
  type bigint(20),
  typeId bigint(20)
);
/*-- version 2.0.0.0,build 2014-3-4,module server */
-- version 2.0.0.0,build 2014-03-12,module server
CREATE TABLE deviceperfcollecttime (
  entityId  bigint(20) NOT NULL,
  perfTargetName  varchar(32) NOT NULL,
  collectInterval  int(11) NOT NULL,
  targetEnable  tinyint(4) NOT NULL,
  parentType bigint(20) NOT NULL,
  entityType  bigint(20) NOT NULL,
  typeId  bigint(20) NOT NULL,
  PRIMARY KEY (entityId, perfTargetName),
  CONSTRAINT FK_DevicePerfCollectTime_entityId FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE globalperfcollecttime (
  perfTargetName varchar(32) NOT NULL,
  parentType bigint(20) NOT NULL,
  entityType bigint(20) NOT NULL,
  globalInterval int(11) NOT NULL,
  globalEnable tinyint(4) NOT NULL,
  targetGroup varchar(32) NOT NULL, 
  groupPriority tinyint(4) NOT NULL,
  PRIMARY KEY (perfTargetName, entityType)
);

CREATE TABLE deviceperftarget (
  perfTargetName varchar(32) NOT NULL,
  parentType bigint(20) NOT NULL,
  entityType bigint(20) NOT NULL,
  typeId bigint(20) NOT NULL,
  targetGroup varchar(32) NOT NULL,
  groupPriority tinyint(4) NOT NULL,
  PRIMARY KEY (perfTargetName, typeId)
);

create table perfConnectivity (
  entityId bigint(20) not null,
  collectValue int(11) not null,
  collectTime timestamp not null,
  primary key(entityId,collectTime)
);

/* -- version 2.0.0.0,build 2014-03-12,module server */

 
-- version 2.0.0.0,build 2014-03-24,module server
ALTER table ReportTemplate add COLUMN (display tinyint(4) NOT NULL DEFAULT 1);
/* -- version 2.0.0.0,build 2014-03-24,module server */

-- version 2.0.0.0,build 2014-05-06,module server
alter table reportinstances change createTime createTime timestamp DEFAULT CURRENT_TIMESTAMP;
/* -- version 2.0.0.0,build 2014-05-06,module server */

-- version 2.0.0.0,build 2014-05-09,module server
alter table entity add unique key(ip);
/* -- version 2.0.0.0,build 2014-05-09,module server */

-- version 2.0.0.0,build 2014-05-11,module server
create table batchAutoDiscoveryIps
(
   id                   bigint(20) auto_increment not null,
   ipInfo               varchar(50),
   name                 varchar(63),
   folderId             bigint(20),
   autoDiscovery        tinyint,
   createtime           timestamp DEFAULT CURRENT_TIMESTAMP,
   lastDiscoveryTime    timestamp NULL,
   primary key (id)
);
alter table batchAutoDiscoveryIps add constraint FK_batchAutoDiscovery_folderId foreign key (folderId)
      references topofolder(folderId) on delete set null;

create table batchAutoDiscoverySnmpConfig
(
   id                   bigint(20) auto_increment not null,
   name                 varbinary(20),
   readCommunity        varchar(20),
   writeCommunity       varchar(20),
   version              tinyint,
   username             varchar(32),
   authProtocol         varchar(32),
   authPassword         varchar(128),
   privProtocol         varchar(32),
   privPassword         varchar(128),
   primary key (id)
);

create table batchAutoDiscoveryPeriod
(
   strategyType         tinyint,
   periodStart          bigint,
   period               bigint,
   active               tinyint
);

create table batchAutoDiscoveryEntityType
(
   typeId               bigint,
   primary key (typeId)
);
/* -- version 2.0.0.0,build 2014-05-11,module server */

-- version 2.2.2.0,build 2014-05-19,module server
create table entitypassword
(
   ip         bigint(20),
   userName         varchar(32),
   password         varchar(32),
   enablePassword   varchar(32),
   primary key (ip)
);
/* -- version 2.2.2.0,build 2014-05-19,module server */


-- version 2.2.2.0,build 2014-06-23,module server
alter table entitypassword add ipString varchar(32);
/* -- version 2.2.2.0,build 2014-06-23,module server */

-- version 2.2.2.0,build 2014-07-11,module server
alter table batchautodiscoverysnmpconfig change readCommunity readCommunity  varchar(32);
alter table batchautodiscoverysnmpconfig change writeCommunity writeCommunity  varchar(32);
/* -- version 2.2.2.0,build 2014-07-11,module server */

-- version 2.2.2.0,build 2014-07-16,module server
rename table entitypassword to telnetLoginConfig;
/* -- version 2.2.2.0,build 2014-07-16,module server */

-- version 2.2.2.0,build 2014-7-17,module server
ALTER table reporttask add COLUMN (reportId varchar(64));
ALTER table reporttask add COLUMN (reportName varchar(64));
alter table reportinstances change COLUMN reportId instanceId bigint(20) not null auto_increment;
alter table reportinstances change COLUMN reportName instanceTitle varchar(128);
/* -- version 2.2.2.0,build 2014-7-17,module server */

-- version 2.2.2.0,build 2014-07-21,module server
create table sendConfigEntity
(
   entityId   bigint(20),
   ip         bigint(20),
   state         tinyint,
   resultId         bigint(20),
   dt   timestamp NOT NULL default '0000-00-00 00:00:00',
   primary key (entityId),
   CONSTRAINT FK_SendConfigEntity_entityId FOREIGN KEY (entityId) REFERENCES entity(entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.2.2.0,build 2014-07-21,module server */ 
-- version 2.2.2.0,build 2014-7-22,module server
create table entityCommonConfig
(
	config     varchar(1024) comment '一行一条命令', 
	type bigint(20)
);
/* -- version 2.2.2.0,build 2014-7-22,module server */

-- version 2.2.2.0,build 2014-07-28,module server
alter table Alert modify column host varchar(128) default NULL;
alter table HistoryAlert modify column host varchar(128) default NULL;
/* -- version 2.2.2.0,build 2014-07-28,module server */

-- version 2.2.2.0,build 2014-08-07,module server
alter table Event modify column host varchar(128) default NULL;
/* -- version 2.2.2.0,build 2014-08-07,module server */

-- version 2.4.3.0,build 2014-10-17,module server
alter table EntityType add COLUMN (disabled tinyint(1) NOT NULL DEFAULT 0);
/* -- version 2.4.3.0,build 2014-10-17,module server */

-- version 2.4.3.0,build 2014-10-30,module server
create table AutoRefreshConfig(autoRefreshSwitch int, autoRefreshInterval int);
/* -- version 2.4.3.0,build 2014-10-30,module server */

-- version 2.4.3.0,build 2014-11-06,module server
create table userauthfolder
(
   userId bigint(20) not null,
   folderId bigint(20) not null,
   primary key (userId,folderId),
   CONSTRAINT FK_authuserfolder_userId FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_authuserfolder_folderId FOREIGN KEY (folderId) REFERENCES topofolder(folderId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.4.3.0,build 2014-11-06,module server */

-- version 2.4.6.0,build 2014-12-06,module server
create table UpgradeRecord
(
   recordId             bigint not null auto_increment,
   entityId             bigint,
   entityName           varchar(64),
   manageIp             varchar(64),
   mac                  varchar(64),
   originVersion        varchar(64),
   retryTimes           int,
   upgradeVersion       varchar(64),
   status               int,
   startTime            timestamp,
   endTime              timestamp,
   jobName              varchar(64),
   upLinkEntityName     varchar(64),
   typeId               bigint,
   typeName             varchar(64),
   primary key (recordId)
);
create table UpgradeJob
(
   jobId                bigint not null auto_increment,
   name                 varchar(64),
   createTime           timestamp,
   updateTime           timestamp,
   workerClass             varchar(256),
   jobClass             varchar(256),
   startTime            timestamp,
   imageFile            varchar(64),
   versionName          varchar(64),
   transferType         varchar(4),
   subType varchar(64),
   primary key (jobId)
);
create table UpgradeEntity
(
   jobId                bigint not null,
   entityId             bigint not null,
   upgradeStatus        int,
   upgradeNote        text,
   retryTimes           int,
   retry                tinyint,
   endTime              timestamp,
   startTime            timestamp,
   primary key (jobId, entityId)
);
alter table UpgradeEntity add constraint FK_Reference_2 foreign key (jobId)
      references UpgradeJob (jobId) on delete cascade on update restrict;
alter table UpgradeEntity add constraint FK_Reference_3 foreign key (entityId)
      references Entity (entityId) on delete cascade on update restrict;
/* -- version 2.4.6.0,build 2014-09-25,module server */

-- version 2.4.6.0,build 2014-12-11,module server
ALTER TABLE sendConfigEntity ADD COLUMN result text;
/* -- version 2.4.6.0,build 2014-12-11,module server */

-- version 2.4.6.0,build 2015-1-14,module server
alter table perftarget modify column targetId varchar(64);
alter table perfthresholdrule modify column targetId varchar(64);
ALTER TABLE perftarget ADD COLUMN maxNum float;
ALTER TABLE perftarget ADD COLUMN minNum float;
ALTER TABLE perftarget ADD COLUMN enableStatus int;
ALTER TABLE perftarget ADD COLUMN regexpValue int;

ALTER TABLE perfthresholdtemplate ADD COLUMN parentType int;

create table targetTypeRela
(
   targetId    varchar(20) not null,
   typeCode    varchar(20) not null unique,
   description varchar(64),
   primary key (targetId)
);
/* -- version 2.4.6.0,build 2014-1-14,module server */
-- version 2.6.0.0,build 2015-05-21,module server
create table alertsound
(
   id               int not null auto_increment,
   name             varchar(256) not null,
   description      varchar(512),
   deletable        int not null,
   primary key (id)
);

alter table levels add column soundId int;
/*-- version 2.6.0.0,build 2015-05-21,module server */


-- version 2.4.7.0,build 2015-3-20,module server
alter table EntityType add column (standard tinyint(1) NOT NULL DEFAULT 0);
/* -- version 2.4.7.0,build 2014-3-20,module server */

-- version 2.4.9.0,build 2015-4-15,module server
ALTER TABLE perfTemplateEntityRelation DROP FOREIGN KEY FK_PERF_TEMPLATE;
/* -- version 2.4.9.0,build 2014-4-15,module server */

-- version 2.6.0.0,build 2015-05-21,module server
create table AlertConfirmConfig(alertConfirmConfig int);
alter table Alert add COLUMN (clearUser varchar(32) DEFAULT NULL);
alter table Alert add COLUMN (clearTime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00');
alter table Alert add COLUMN (clearMessage varchar(255) DEFAULT NULL);
/* -- version 2.6.0.0,build 2015-05-21,module server */

-- version 2.6.0.0,build 2015-06-16,module server
-- drop table globalcollecttimetable;
-- drop table devicecollecttimetable;
/* -- version 2.6.0.0,build 2015-06-16,module server */


-- version 2.6.0.0,build 2015-06-23,module server
alter table EntityType add column discoveryBean varchar(64) DEFAULT NULL;
/* -- version 2.6.0.0,build 2015-06-23,module server */
 
-- version 2.6.0.0,build 2015-7-25,module server
CREATE TABLE perftargetCategory (
	typeId  int(11) NOT NULL ,
	perfIndex  varchar(255) NOT NULL ,
	category  varchar(255) ,
	PRIMARY KEY (typeId, perfIndex)
);
/*-- version 2.6.0.0,build 2015-7-25,module server*/

-- version 2.6.0.0,build 2015-08-01,module server
alter table entity add constraint FK_ENTITY_SUBENTITY foreign key(parentId) references entity(entityId) on delete cascade on update cascade;
/* -- version 2.6.0.0,build 2015-08-01,module server */-- version 2.6.2.0,build 2015-07-16,module server
ALTER TABLE perftarget MODIFY maxNum DECIMAL(15,2);
ALTER TABLE perftarget MODIFY minNum DECIMAL(15,2);
/* -- version 2.6.2.0,build 2015-07-16,module server */

-- version 2.6.2.0,build 2015-08-11,module server
ALTER TABLE perfmonitor ADD INDEX identifyKey_Index (identifyKey);
/* -- version 2.6.2.0,build 2015-08-11,module server */

-- version 2.6.2.0,build 2015-09-22,module server
CREATE TABLE BaiduEntity (
  entityId bigint(20) NOT NULL,
  typeId bigint(20) default NULL,
  longitude decimal(17,14) default NULL,
  latitude decimal(17,14) default NULL,
  zoom int(2) default NULL,
  minZoom int(2) default NULL,
  maxZoom int(2) default NULL,
  location text(20) default NULL,
  PRIMARY KEY  (entityId),
  CONSTRAINT FK_BAIDU_ENTITY FOREIGN KEY (entityId) REFERENCES Entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE
);
/* -- version 2.6.2.0,build 2015-09-22,module server */


-- version 2.4.9.0,build 2015-7-23,module server
create table entityreplacerecord(
    entityId bigint(20),
    macAddress varchar(20),
    preIpAddress varchar(20),
    replaceIpAddress varchar(20),
    replaceTime timestamp,
    primary key(entityId,replaceTime)
);
/* -- version 2.4.9.0,build 2014-7-23,module server */ 

-- version 2.6.4.0,build 2015-9-21,module server
CREATE TABLE AliasLog (
	logTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  	entityId bigint(20) NOT NULL,
 	 name varchar(64) default '',
  	ip varchar(32) default NULL,
 	mac varchar(32) default NULL,
  	PRIMARY KEY (logTime,entityId)
);
/* -- version 2.6.4.0,build 2015-9-21,module server  */

-- version 2.6.5.0,build 2015-11-12,module server
create table ConfigBackupRecords(
  operationType int(5),
  entityId  bigint(20),
  userId bigint(20),
  clientIp varchar(20),
  result varchar(64),
  fileName varchar(64),
  operationTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
/*-- version 2.6.5.0,build 2015-11-12,module server*/

-- version 2.6.8.0,build 2016-3-26,module server
CREATE TABLE nbiperfgroup (             
    groupId int(11) NOT NULL,             
    groupName varchar(255),
    displayName varchar(64),
    groupModule varchar(16),
    PRIMARY KEY (groupId)
);

CREATE TABLE nbiperfgroupindex (                   
    groupId int(11) NOT NULL,
    perfIndex int(11) NOT NULL,
    oid varchar(255),
    perfIndexName varchar(255),
    perfIndexDisplayName varchar(255),
    period int(11),
    selected tinyint(1),
    PRIMARY KEY (groupId,perfIndex)
);
              
CREATE TABLE nbibaseconfig (
    mode int(2) NOT NULL DEFAULT 1,  
    ftpAddr varchar(32),      
    ftpPort int(11),
    ftpUser varchar(32),
    ftpPwd varchar(32),
    filePath varchar(32),
    recordMax int(11),
    fileSaveTime int(11),
    nbiAddr varchar(32),
    nbiPort int(11),
    encoding varchar(32) DEFAULT "GBK",
    nbiSwitch int(1) DEFAULT 0
);

CREATE TABLE nbiexportconfig (
   five_enable int(1)DEFAULT 1,
   ten_enable int(1)DEFAULT 0,
   fifteen_enable int(1)DEFAULT 0,
   thirty_enable  int(1)DEFAULT 0,
   sixty_enable int(1)DEFAULT 0
);
/*-- version 2.6.8.0,build 2016-3-26,module server*/

-- version 2.7.1.0,build 2016-6-22,module server
ALTER TABLE perfthresholdrule ADD COLUMN clearRules  varchar(200);
/*-- version 2.7.1.0,build 2016-6-22,module server*/

-- version 2.7.1.0,build 2016-7-12,module server
ALTER TABLE entitysnap modify vmem decimal(20,4);
/*-- version 2.7.1.0,build 2016-7-12,module server*/

-- version 2.7.1.0,build 2016-8-10,module server
CREATE TABLE thresholdAlertLastValue (
entityId  bigint(20) NOT NULL ,
alertEventId  int(255) NOT NULL ,
source  varchar(32)  NOT NULL ,
levelId int(11) NOT NULL,
perfValue  varchar(50) DEFAULT NULL,
PRIMARY KEY (entityId, alertEventId, source )
);
/** -- version 2.7.1.0,build 2016-8-09,module server */


-- version 2.7.3.0,build 2016-8-18,module server
alter table telnetLoginConfig add column isAAA int(1) default 0;
/* -- version 2.7.3.0,build 2016-8-18,module server  */

-- version 2.7.3.0,build 2016-9-8,module server
drop table aliaslog;
create table aliaslog(entityId bigint(20), parentId bigint(20), oldname varchar(100), newname varchar(100), ip varchar(100), mac varchar(30), `index` bigint(20), typeId int, createTime timestamp, actionType tinyint, actionTime timestamp);
/* -- version 2.7.3.0,build 2016-9-8,module server  */

-- version 2.7.3.0,build 2016-11-12,module server
create table useralert(userId bigint, alertTypeId int, primary key(userId, alertTypeId));
/* -- version 2.7.3.0,build 2016-11-12,module server  */

-- version 2.7.3.0,build 2016-11-17,module server
CREATE TABLE autoclearrecord (                                                        
      oltName varchar(64) DEFAULT NULL,                                                   
      oltIp varchar(20) DEFAULT NULL,                                                     
      onuName varchar(64) DEFAULT NULL,                                                   
      onuMac varchar(20) DEFAULT NULL,                                                    
      onuType varchar(20) DEFAULT NULL,                                                   
      onuIndex varchar(20) DEFAULT NULL,                                                   
      offlineTime timestamp NULL DEFAULT NULL,                         
      clearTime timestamp  NULL DEFAULT NULL)
/*-- version 2.7.3.0,build 2016-11-17,module server */

-- version 2.8.0.1,build 2016-11-21 14:12:00,module server
ALTER TABLE alert MODIFY source VARCHAR(100);
ALTER TABLE historyalert MODIFY source VARCHAR(100);
/* -- version 2.8.0.1,build 2016-11-21 14:12:00,module server  */

-- version 2.9.0.0,build 2017-01-12 20:43:00,module server
CREATE TABLE alerttypeaboutusers (                                                                                              
userId bigint(20) NOT NULL,                                                                                                   
alertTypeId int(11) NOT NULL,                                                                                                 
actionTypeId int(5) NOT NULL,                                                                                                     
KEY FK_ALERT_ALERT (alertTypeId),  
KEY FK_ALERT_USER (userId),
CONSTRAINT FK_ALERT_ALERT FOREIGN KEY (alertTypeId) REFERENCES alerttype (typeId) ON DELETE CASCADE ON UPDATE CASCADE,  
CONSTRAINT FK_ALERT_USER FOREIGN KEY (userId) REFERENCES users (userId) ON DELETE CASCADE ON UPDATE CASCADE             
);
/* -- version 2.9.0.0,build 2017-01-12 20:43:00,module server  */

-- version 2.9.0.0,build 2017-01-18 17:07:00,module server
ALTER TABLE entity ADD COLUMN contact varchar(64) DEFAULT NULL;
/* -- version 2.9.0.0,build 2017-01-18 17:07:00,module server  */

-- version 2.9.0.0,build 2017-3-8 13:56:00,module server
ALTER TABLE AliasLog ADD COLUMN sn varchar(500) DEFAULT NULL;
/* -- version 2.9.0.0,build 2017-3-8 13:56:00,module server  */
-- version 2.9.0.2,build 2017-4-20 13:56:00,module server
ALTER TABLE upgradejob ADD COLUMN type int(2) DEFAULT NULL;
/* -- version 2.9.0.2,build 2017-4-20 13:56:00,module server  */
-- version 2.9.0.4,build 2017-4-28 13:56:00,module server
ALTER TABLE telnetloginconfig modify COLUMN userName varchar(64) DEFAULT NULL;
ALTER TABLE telnetloginconfig modify COLUMN password varchar(64) DEFAULT NULL;
ALTER TABLE telnetloginconfig modify COLUMN enablePassword varchar(64) DEFAULT NULL;
/* -- version 2.9.0.2,build 2017-4-28 13:56:00,module server  */

-- version 2.9.0.5,build 2017-5-10,module server
ALTER TABLE alert ADD COLUMN rowCreateTime timestamp default CURRENT_TIMESTAMP;
ALTER TABLE historyalert ADD COLUMN rowCreateTime timestamp default CURRENT_TIMESTAMP;
/* -- version 2.9.0.5,build 2017-5-10,module server  */
-- version 2.8.0.6,build 2017-6-15,module server
CREATE TABLE executorThreadSnap (
engineId  bigint(20) NOT NULL ,
activeCount int(11),
poolSize int(11),
completedTaskCount bigint(20),
collectTime timestamp NOT NULL default '0000-00-00 00:00:00',
PRIMARY KEY (engineId, collectTime)
);
/** -- version 2.8.0.6,build 2017-6-15,module server */

-- version 2.9.1.5,build 2017-7-14,module server
ALTER TABLE entitycommonconfig ADD folderId bigint(20) DEFAULT 10 NOT NULL;
ALTER TABLE entitycommonconfig ADD CONSTRAINT FK_config_topofolder FOREIGN KEY(folderId) REFERENCES topofolder(folderId) ON DELETE CASCADE ON UPDATE CASCADE;
/* -- version 2.9.1.5,build 2017-7-14,module server  */

-- version 2.9.1.6,build 2017-8-9,module server
CREATE TABLE `autoclearcmcirecord` (
  `cmcId` bigint(20) NOT NULL,
  `cmcIndex` bigint(20) DEFAULT NULL,
  `cmcType` int(11) DEFAULT NULL,
  `cmcEntityId` bigint(20) DEFAULT NULL,
  `alias` varchar(64) DEFAULT '',
  `typeName` varchar(64) DEFAULT '',
  `ipAddress` varchar(32) DEFAULT NULL,
  `macAddr` varchar(20) DEFAULT NULL,
  `offlineTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `clearTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cmcId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/* -- version 2.9.1.6,build 2017-8-9,module server  */

-- version 2.9.2.0,build 2017-4-14 19:26:00,module server
ALTER TABLE AlertFilter ADD COLUMN typeIds text NOT NULL;
ALTER TABLE AlertFilter ADD COLUMN onuLevel int(11) DEFAULT NULL;
/* -- version 2.9.2.0,build 2017-4-14 19:26:00,module server  */

-- version 2.9.1.11,build 2018-2-1 19:26:00,module server
ALTER TABLE autoclearrecord  MODIFY COLUMN onuMac varchar(50);
/* -- version 2.9.1.11,build 2018-2-1 19:26:00,module server  */

-- version 2.9.1.21,build 2018-4-17,module server
create table perfthresholdrule_bak as select * from perfthresholdrule;

DELETE
FROM
    perfthresholdrule
WHERE
    (targetId,templateId) IN (
        SELECT
            targetId,templateId
        FROM
            perfthresholdrule_bak
        GROUP BY
            targetId,templateId
        HAVING
            count(*) > 1
    )
AND ruleId NOT IN (
    SELECT
        max(ruleId)
    FROM
        perfthresholdrule_bak
    GROUP BY
        targetId,templateId
    HAVING
        count(*) > 1
);

drop table perfthresholdrule_bak
/* -- version 2.9.1.21,build 2018-4-17,module server  */

-- version 2.10.0.1,build 2017-12-12 13:56:00,module server
ALTER TABLE AlertType ADD COLUMN localeName varchar(100) DEFAULT NULL;
/* -- version 2.10.0.1,build 2017-12-12 13:56:00,module server  */

-- version 2.9.1.14,build 2017-12-26 15:24:00,module server
ALTER TABLE entitytyperelation ADD INDEX(typeId);
/* -- version 2.9.1.14,build 2017-12-26 15:24:00,module server  */