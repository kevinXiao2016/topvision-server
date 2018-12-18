-- version 2.9.0.0,build 2016-12-29,module gpon
CREATE TABLE  GponAutoAuthConfig(
	entityId bigint(20),
	authIndex bigint(20),
	onuAutoAuthenPortlist varchar(255),
	onuType varchar(255),
	catvNum int(10),
	ethNum int(10),
	veipNum  int(10),
	wlanNum  int(10),
	lineProfileId int(10),
	srvProfileId int(10),
	CONSTRAINT FK_GponAutoAuthOnu FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY  (entityId,authIndex,onuType)
);

CREATE TABLE  GponOnuAuthConfig(
	entityId bigint(20),
	authenOnuId int(10),
	ponIndex bigint(20),
	ponId bigint(20),
	loid varchar(255),
	loidPassword varchar(255),
	password varchar(255),
	sn  varchar(255),
	srvProfileId int(10),
	lineProfileId int(10),
	CONSTRAINT FK_GponOnuAuthConfig FOREIGN KEY (ponId) REFERENCES oltponrelation (ponId)  ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY  (entityId,authenOnuId)
);

CREATE TABLE  GponOnuAutoFind(
	entityId bigint(20),
	ponIndex bigint(20),
	onuIndex bigint(20),
	onuType varchar(255),
	autoFindTime bigint(255),
	loid varchar(255),
	loidPassword varchar(255),
	password varchar(255),
	serialNumber varchar(255),
	softwareVersion varchar(255),
	hardwareVersion varchar(255),
	CONSTRAINT FK_GponOnuAutoFind FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY  (entityId,onuIndex)
);
CREATE TABLE GponOnuAuthMode(
	entityId bigint(20),
	ponIndex bigint(20),
	ponId  bigint(20),
	ponOnuAuthenMode int(10),
	ponAutoFindEnable int(2),
	CONSTRAINT FK_GponOnuAuthMode FOREIGN KEY (ponId) REFERENCES oltponrelation (ponId) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY  (entityId,ponIndex)
);
/**-- version 2.9.0.0,build 2016-12-29,module gpon*/

-- version 2.9.0.0,build 2017-3-9,module gpon
alter table GponAutoAuthConfig modify onuAutoAuthenPortlist text;
/**-- version 2.9.0.0,build 2017-3-9,module gpon*/

-- version 2.9.1.6,build 2017-7-26,module gpon
ALTER TABLE Gponautoauthconfig DROP FOREIGN KEY FK_GponAutoAuthOnu;
ALTER TABLE GponAutoAuthConfig DROP PRIMARY KEY;
ALTER TABLE GponAutoAuthConfig ADD PRIMARY KEY(entityId,authIndex);
ALTER TABLE Gponautoauthconfig MODIFY onuType CHAR(40);
ALTER TABLE Gponautoauthconfig ADD CONSTRAINT FK_GponAutoAuthOnu FOREIGN KEY (entityId) REFERENCES entity (entityId) ON DELETE CASCADE ON UPDATE CASCADE;
/**-- version 2.9.1.6,build 2017-7-26,module gpon*/