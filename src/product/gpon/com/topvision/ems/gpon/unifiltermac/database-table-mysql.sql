-- version 2.9.0.0,build 2016-12-24,module gpon
CREATE TABLE  GponUniFilterMac(
	entityId  bigint(20) not null,
	uniId  bigint(20) not null,
	macAddressString  varchar(20),
	PRIMARY KEY  (uniId,macAddressString),
	CONSTRAINT FK_GponUniFilterMac FOREIGN KEY (uniId) REFERENCES oltunirelation (uniId) ON DELETE CASCADE ON UPDATE CASCADE
);
/**-- version 2.9.0.0,build 2016-12-24,module gpon*/