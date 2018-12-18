
/* Casa C2200 */
insert into EntityType(typeId,name,displayName, module, modulePath, sysObjectId) values
    (43200,'casa-c2200','CASA-C2200','cmts','/cmts','1.3.6.1.4.1.20858.2.20');
update EntityType set icon16 = 'network/cmts/casa_16.png',
					icon32 = 'network/cmts/casa_32.png',
					icon48 = 'network/cmts/casa_48.png',
					icon64 = 'network/cmts/casa_64.png',
					discoveryBean = 'cmtsDiscoveryService',
					corpId = 20858 where typeId = 43200;
insert into EntityTypeRelation (type, typeId) values (1,43200);
insert into EntityTypeRelation (type, typeId) values (3, 43200);
insert into EntityTypeRelation (type, typeId) values (40000,43200);
insert into EntityTypeRelation (type, typeId) values (50000,43200);
insert into EntityTypeRelation (type, typeId) values (60000,43200);
insert into EntityTypeRelation (type, typeId) values (110000,43200);
insert into EntityTypeRelation (type, typeId) values (43200,43200);
insert into BatchAutoDiscoveryEntityType(typeId) values(43200);
insert into DevicePerfTarget(perfTargetName, parentType, EntityType, typeId, targetGroup, groupPriority) VALUES 
            ('cmc_onlineStatus', 60000, 40000, 43200, 'cmc_deviceStatus', 1),
            ('cmc_snr', 60000, 40000, 43200, 'cmc_signalQuality', 3),
            ('cmc_ber', 60000, 40000, 43200, 'cmc_signalQuality', 3),
            ('cmc_upLinkFlow', 60000, 40000, 43200, 'cmc_flow', 4),
            ('cmc_channelSpeed', 60000, 40000, 43200, 'cmc_flow', 4),
            ('cmc_cpuUsed', 60000, 40000, 43200, 'cmc_service', 2),
            ('cmc_memUsed', 60000, 40000, 43200, 'cmc_service', 2);

/* Casa C3000 */
insert into EntityType(typeId,name,displayName, module, modulePath, sysObjectId) values
    (43300,'casa-c3000','CASA-C3000','cmts','/cmts','1.3.6.1.4.1.20858.2.50');
update EntityType set icon16 = 'network/cmts/casa_16.png',
                    icon32 = 'network/cmts/casa_32.png',
                    icon48 = 'network/cmts/casa_48.png',
                    icon64 = 'network/cmts/casa_64.png',
                    discoveryBean = 'cmtsDiscoveryService',
                    corpId = 20858 where typeId = 43300;
insert into EntityTypeRelation (type, typeId) values (1,43300);
insert into EntityTypeRelation (type, typeId) values (3, 43300);
insert into EntityTypeRelation (type, typeId) values (40000,43300);
insert into EntityTypeRelation (type, typeId) values (50000,43300);
insert into EntityTypeRelation (type, typeId) values (60000,43300);
insert into EntityTypeRelation (type, typeId) values (110000,43300);
insert into EntityTypeRelation (type, typeId) values (43300,43300);
insert into BatchAutoDiscoveryEntityType(typeId) values(43300);
insert into DevicePerfTarget(perfTargetName, parentType, EntityType, typeId, targetGroup, groupPriority) VALUES 
            ('cmc_onlineStatus', 60000, 40000, 43300, 'cmc_deviceStatus', 1),
            ('cmc_snr', 60000, 40000, 43300, 'cmc_signalQuality', 3),
            ('cmc_ber', 60000, 40000, 43300, 'cmc_signalQuality', 3),
            ('cmc_upLinkFlow', 60000, 40000, 43300, 'cmc_flow', 4),
            ('cmc_channelSpeed', 60000, 40000, 43300, 'cmc_flow', 4),
            ('cmc_cpuUsed', 60000, 40000, 43300, 'cmc_service', 2),
            ('cmc_memUsed', 60000, 40000, 43300, 'cmc_service', 2);

/* Cisco UBR7246 */
insert into EntityType(typeId,name,displayName, module, modulePath, sysObjectId) values
    (42200,'ubr7246','UBR7246','cmts','/cmts','1.3.6.1.4.1.9.1.271');
update EntityType set icon16 = 'network/cmts/cmts_16.png',
                    icon32 = 'network/cmts/cmts_32.png',
                    icon48 = 'network/cmts/cmts_48.png',
                    icon64 = 'network/cmts/cmts_64.png',
                    discoveryBean='cmtsDiscoveryService',
                    corpId = 9 where typeId = 42200;
insert into EntityTypeRelation (type, typeId) values (1,42200);
insert into EntityTypeRelation (type, typeId) values (3, 42200);
insert into EntityTypeRelation (type, typeId) values (40000,42200);
insert into EntityTypeRelation (type, typeId) values (50000,42200);
insert into EntityTypeRelation (type, typeId) values (60000,42200);
insert into EntityTypeRelation (type, typeId) values (100000,42200);
insert into EntityTypeRelation (type, typeId) values (42200,42200);
insert into BatchAutoDiscoveryEntityType(typeId) values(42200);
insert into DevicePerfTarget(perfTargetName, parentType, EntityType, typeId, targetGroup, groupPriority) VALUES 
            ('cmc_onlineStatus', 60000, 40000, 42200, 'cmc_deviceStatus', 1),
            ('cmc_snr', 60000, 40000, 42200, 'cmc_signalQuality', 3),
            ('cmc_ber', 60000, 40000, 42200, 'cmc_signalQuality', 3),
            ('cmc_upLinkFlow', 60000, 40000, 42200, 'cmc_flow', 4),
            ('cmc_channelSpeed', 60000, 40000, 42200, 'cmc_flow', 4),
            ('cmc_cpuUsed', 60000, 40000, 42200, 'cmc_service', 2),
            ('cmc_memUsed', 60000, 40000, 42200, 'cmc_service', 2);

/* Cisco UBR10012 */
insert into EntityType(typeId,name,displayName, module, modulePath, sysObjectId) values
    (42300,'ubr10012','UBR10012','cmts','/cmts','1.3.6.1.4.1.9.1.317');
update EntityType set icon16 = 'network/cmts/cmts_16.png',
                    icon32 = 'network/cmts/cmts_32.png',
                    icon48 = 'network/cmts/cmts_48.png',
                    icon64 = 'network/cmts/cmts_64.png',
                    discoveryBean='cmtsDiscoveryService',
                    corpId = 9 where typeId = 42300;
insert into EntityTypeRelation (type, typeId) values (1,42300),
													(3, 42300),
													(40000,42300),
													(50000,42300),
													(60000,42300),
													(100000,42300),
													(42300,42300);
insert into BatchAutoDiscoveryEntityType(typeId) values(42300);
insert into DevicePerfTarget(perfTargetName, parentType, EntityType, typeId, targetGroup, groupPriority) VALUES 
				('cmc_onlineStatus', 60000, 40000, 42300, 'cmc_deviceStatus', 1),
				('cmc_snr', 60000, 40000, 42300, 'cmc_signalQuality', 3),
				('cmc_ber', 60000, 40000, 42300, 'cmc_signalQuality', 3),
				('cmc_upLinkFlow', 60000, 40000, 42300, 'cmc_flow', 4),
				('cmc_channelSpeed', 60000, 40000, 42300, 'cmc_flow', 4),
				('cmc_cpuUsed', 60000, 40000, 42300, 'cmc_service', 2),
				('cmc_memUsed', 60000, 40000, 42300, 'cmc_service', 2);

/* Arris C4 */
insert into EntityType(typeId,name,displayName, module, modulePath, sysObjectId) values
    (44100,'C4','C4','cmts','/cmts','1.3.6.1.4.1.4998.2.1');
update EntityType set icon16 = 'network/cmts/cmts_16.png',
                      icon32 = 'network/cmts/cmts_32.png',
                      icon48 = 'network/cmts/cmts_48.png',
                      icon64 = 'network/cmts/cmts_64.png',
                      discoveryBean='cmtsDiscoveryService',
                      corpId = 4998 where typeId = 44100;
insert into EntityTypeRelation (type, typeId) values (1,44100),
													(3, 44100),
													(40000,44100),
													(50000,44100),
													(60000,44100),
													(100000,44100),
													(44100,44100);
insert into BatchAutoDiscoveryEntityType(typeId) values(44100);
insert into DevicePerfTarget(perfTargetName, parentType, EntityType, typeId, targetGroup, groupPriority) VALUES 
			('cmc_onlineStatus', 60000, 40000, 44100, 'cmc_deviceStatus', 1),
			('cmc_snr', 60000, 40000, 44100, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 40000, 44100, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 40000, 44100, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 40000, 44100, 'cmc_flow', 4),
			('cmc_cpuUsed', 60000, 40000, 44100, 'cmc_service', 2),
			('cmc_memUsed', 60000, 40000, 44100, 'cmc_service', 2);

/* Arris C4c */
insert into EntityType(typeId,name,displayName, module, modulePath, sysObjectId) values
    (44200,'C4c','C4c','cmts','/cmts','1.3.6.1.4.1.4998.2.2');
update EntityType set icon16 = 'network/cmts/cmts_16.png',
                      icon32 = 'network/cmts/cmts_32.png',
                      icon48 = 'network/cmts/cmts_48.png',
                      icon64 = 'network/cmts/cmts_64.png',
                      discoveryBean='cmtsDiscoveryService',
                      corpId = 4998 where typeId = 44200;
insert into EntityTypeRelation (type, typeId) values (1,44200),
                                                    (3, 44200),
                                                    (40000,44200),
                                                    (50000,44200),
                                                    (60000,44200),
                                                    (100000,44200),
                                                    (44200,44200);
insert into BatchAutoDiscoveryEntityType(typeId) values(44200);
insert into DevicePerfTarget(perfTargetName, parentType, EntityType, typeId, targetGroup, groupPriority) VALUES 
            ('cmc_onlineStatus', 60000, 40000, 44200, 'cmc_deviceStatus', 1),
            ('cmc_snr', 60000, 40000, 44200, 'cmc_signalQuality', 3),
            ('cmc_ber', 60000, 40000, 44200, 'cmc_signalQuality', 3),
            ('cmc_upLinkFlow', 60000, 40000, 44200, 'cmc_flow', 4),
            ('cmc_channelSpeed', 60000, 40000, 44200, 'cmc_flow', 4),
            ('cmc_cpuUsed', 60000, 40000, 44200, 'cmc_service', 2),
            ('cmc_memUsed', 60000, 40000, 44200, 'cmc_service', 2);

/* BlueLink 注意篮联使用了Net-SNMP注册的sysObjectID节点*/
insert into EntityType(typeId,name,displayName, module, modulePath, sysObjectId) values
    (45100,'bluelink','BlueLink','cmts','/cmts','1.3.6.1.4.1.8072.3.2.10');
update EntityType set icon16 = 'network/cmts/cmts_16.png',
                      icon32 = 'network/cmts/cmts_32.png',
                      icon48 = 'network/cmts/cmts_48.png',
                      icon64 = 'network/cmts/cmts_64.png',
                      discoveryBean='cmtsDiscoveryService',
                      corpId = 8072 where typeId = 45100;
insert into EntityTypeRelation (type, typeId) values (1,45100),
													(3, 45100),
													(40000,45100),
													(50000,45100),
													(60000,45100),
													(100000,45100),
													(45100,45100);
insert into BatchAutoDiscoveryEntityType(typeId) values(45100);
insert into DevicePerfTarget(perfTargetName, parentType, EntityType, typeId, targetGroup, groupPriority) VALUES 
		('cmc_onlineStatus', 60000, 40000, 45100, 'cmc_deviceStatus', 1),
		('cmc_snr', 60000, 40000, 45100, 'cmc_signalQuality', 3),
		('cmc_ber', 60000, 40000, 45100, 'cmc_signalQuality', 3),
		('cmc_upLinkFlow', 60000, 40000, 45100, 'cmc_flow', 4),
		('cmc_channelSpeed', 60000, 40000, 45100, 'cmc_flow', 4),
		('cmc_cpuUsed', 60000, 40000, 45100, 'cmc_service', 2),
		('cmc_memUsed', 60000, 40000, 45100, 'cmc_service', 2);

/* DongYan VISTA-C01 */
insert into EntityType(typeId,name,displayName, module, modulePath, sysObjectId) values
    (46100,'C01','VISTA','cmts','/cmts','1.3.6.1.4.1.37737');
update EntityType set icon16 = 'network/cmts/cmts_16.png',
                      icon32 = 'network/cmts/cmts_32.png',
                      icon48 = 'network/cmts/cmts_48.png',
                      icon64 = 'network/cmts/cmts_64.png',
                      discoveryBean='cmtsDiscoveryService',
                      corpId = 37737 where typeId = 46100;
insert into EntityTypeRelation (type, typeId) values (1,46100),
													(3, 46100),
													(40000,46100),
													(50000,46100),
													(60000,46100),
													(100000,46100),
													(46100,46100);
insert into BatchAutoDiscoveryEntityType(typeId) values(46100);
insert into DevicePerfTarget(perfTargetName, parentType, EntityType, typeId, targetGroup, groupPriority) VALUES 
				('cmc_onlineStatus', 60000, 40000, 46100, 'cmc_deviceStatus', 1),
				('cmc_snr', 60000, 40000, 46100, 'cmc_signalQuality', 3),
				('cmc_ber', 60000, 40000, 46100, 'cmc_signalQuality', 3),
				('cmc_upLinkFlow', 60000, 40000, 46100, 'cmc_flow', 4),
				('cmc_channelSpeed', 60000, 40000, 46100, 'cmc_flow', 4),
				('cmc_cpuUsed', 60000, 40000, 46100, 'cmc_service', 2),
				('cmc_memUsed', 60000, 40000, 46100, 'cmc_service', 2);

/* Teleste DAH100 */
insert into EntityType(typeId,name,displayName, module, modulePath, sysObjectId) values
    (47100,'DAH100','DAH100','cmts','/cmts','1.3.6.1.4.1.3715.12.100.100');
update EntityType set icon16 = 'network/cmts/cmts_16.png',
                      icon32 = 'network/cmts/cmts_32.png',
                      icon48 = 'network/cmts/cmts_48.png',
                      icon64 = 'network/cmts/cmts_64.png',
                      discoveryBean='cmtsDiscoveryService',
                      corpId = 3715 where typeId = 47100;
insert into EntityTypeRelation (type, typeId) values (1,47100),
                                                    (3, 47100),
                                                    (40000,47100),
                                                    (50000,47100),
                                                    (60000,47100),
                                                    (100000,47100),
                                                    (47100,47100);
insert into BatchAutoDiscoveryEntityType(typeId) values(47100);
insert into DevicePerfTarget(perfTargetName, parentType, EntityType, typeId, targetGroup, groupPriority) VALUES 
                ('cmc_onlineStatus', 60000, 40000, 47100, 'cmc_deviceStatus', 1),
                ('cmc_snr', 60000, 40000, 47100, 'cmc_signalQuality', 3),
                ('cmc_ber', 60000, 40000, 47100, 'cmc_signalQuality', 3),
                ('cmc_upLinkFlow', 60000, 40000, 47100, 'cmc_flow', 4),
                ('cmc_channelSpeed', 60000, 40000, 47100, 'cmc_flow', 4),
                ('cmc_cpuUsed', 60000, 40000, 47100, 'cmc_service', 2),
                ('cmc_memUsed', 60000, 40000, 47100, 'cmc_service', 2);
