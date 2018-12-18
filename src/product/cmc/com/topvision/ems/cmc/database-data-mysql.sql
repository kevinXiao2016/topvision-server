/*
SQLyog Community Edition- MySQL GUI v5.21
Host - 5.0.19-nt-log : Database - ems
*********************************************************************
Server version : 5.0.19-nt-log
*/

-- version 1.0.0, build 2011-6-22, module cmc
--modify by victor @2011.10.27CMC入口改道ONU处了
--modify by dosion huang @2012.4.19 修改8800A与8800B的onuType
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (12000,   'cmc8800b',  'CC8800B',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.2',   'network/onu/outDoorOnuIcon_16.png',  'network/onu/outDoorOnuIcon_32.png',  'network/onu/outDoorOnuIcon_48.png',  'network/onu/outDoorOnuIcon_64.png',  1);
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (11001,   'cmc8800a',  'CC8800A',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.1',   'network/onu/outDoorOnuIcon_16.png',  'network/onu/outDoorOnuIcon_32.png',  'network/onu/outDoorOnuIcon_48.png',  'network/onu/outDoorOnuIcon_64.png',  1);
/* -- version 1.0.0, build 2011-6-22, module cmc */

-- version 1.0.0, build 2012-3-16, module cmc */

/*CC告警代码*/
INSERT INTO CmcEventTypeRelation(deviceEventTypeId, emsEventTypeId) VALUES
        (11180, 111180),
        (11181, 111181);

        -- (-66030102, 11100),
        -- (-66030103, 11101),
        -- (-66030104, 11102),
        -- (-66030108, 11103),
        -- (-66030109, 11104),
        -- (-66030110, 11105),
        -- (-66030111, 11106),
        -- (-66030202, 11107),
        -- (-66030203, 11108),
        -- (-66030205, 11109),
        -- (-66030206, 11110),
        -- (-66030207, 11111),
        -- (-66030300, 11112),
        -- (-66030400, 11113),
        -- (-66030401, 11114),
        -- (-66030402, 11115),
        -- (-66050102, 11116),
        -- (-66050103, 11117),
        -- (-66050203, 11118),
        -- (-66050206, 11119),
        -- (-66060200, 11120),
        -- (-66060510, 11121),
        -- (-66060509, 11122),
        -- (-66060600, 11123),
        -- (-66060700, 11124),
        -- (-66010100, 11125),
        -- (-66010200, 11126),

        --(-67060100, 11200),
        --(-67060200, 11201),
        --(-67060300, 11202),
        --(-67060400, 11203),
        --(-67060500, 11204),
        --(-67060500, 11205),
        --(-67020200, 11206),
        --(-67020300, 11207),
        --(-67020400, 11208),
        --(-67020500, 11209),
        --(-67020600, 11210),
        --(-67020700, 11211),
        --(-67020800, 11212),
        --(-67020900, 11213),
        --(-67021000, 11214),
        --(-67021100, 11215),
        --(-67021200, 11216),
        --(-67021500, 11217),
        --(-67021600, 11218),
        --(-67021700, 11219),
        --(-67021800, 11220),
        --(-67021900, 11221),
        --(-67030100, 11222),
        --(-67030200, 11223),
        --(-67030300, 11224),
        --(-67030400, 11225),
        --(-67030500, 11226),
        --(-67030600, 11227),
        --(-67040100, 11228),
        --(-67040200, 11229),
        --(-67040300, 11230),
        --(-67040400, 11231),
        --(-67040500, 11232),
        --(-67010100, 11233),
        --(-67010200, 11234),
        --(-67010300, 11235),

        --(-73000400, 11300),
        --(-73000401, 11301),
        --(-73000402, 11302),
        --(-73000403, 11303),
        --(-73000500, 11304),
        --(-73010100, 11305),
        --(-73010200, 11306),
        --(-73010400, 11307),
        --(-73010500, 11308),
        --(-73010501, 11309),
        --(-73010502, 11310),
        --(-73010600, 11311),
        --(-73010601, 11312),
        --(-73010700, 11313),
        --(-73010800, 11314),
        --(-73010900, 11315),
        --(-73011000, 11316),
        --(-73011100, 11317),
        --(-73011200, 11318),
        --(-73011201, 11319),
        --(-73011300, 11320),
        --(-73011301, 11321),
        --(-73011400, 11322),
        --(-73011401, 11323),
        --(-73011500, 11324),
        --(-73011501, 11325),
        --(-73011502, 11326),
        --(-73011600, 11327),
        --(-73011601, 11328),
        --(-73011700, 11329),
        --(-73011800, 11330),
        --(-73000501, 11331),
        --(-73020100, 11332),
        --(-73020101, 11333),
        --(-73020110, 11334),
        --(-73020111, 11335),
        --(-73020112, 11336),
        --(-73020113, 11337),
        --(-73020114, 11338),
        --(-73020115, 11339),
        --(-73020102, 11340),
        --(-73020103, 11341),
        --(-73020104, 11342),
        --(-73020105, 11343),
        --(-73020106, 11344),
        --(-73020107, 11345),
        --(-73020108, 11346),
        --(-73020109, 11347),
        --(-73020116, 11348),
        --(-73030100, 11349),
        --(-73030200, 11350),
        --(-73030300, 11351),
        --(-73021100, 11352),
        --(-73055100, 11353),
        --(-73055200, 11354),
        --(-73055300, 11355),
        --(-73055400, 11356),
        --(-73055500, 11357),
        --(-73055600, 11358),
        --(-73055700, 11359),
        --(-73055800, 11360),

        --(-74000100, 11400),
        --(-74000200, 11401),
        --(-74000300, 11402),
        --(-74000400, 11403),
        --(-74000500, 11404),
        --(-74000600, 11405),
        --(-74000700, 11406),
        --(-74000800, 11407),
        --(-74000900, 11408),

        --(-75010100, 11500),

        --(-76000100, 11600),
        --(-76000200, 11601),

        --(-82010100, 11700),
        --(-82010200, 11701),
        --(-82010300, 11702),

        --(-82010400, 11800),
        --(-82010500, 11801),

        --(-83000100, 11900),
        --(-83000101, 11901),
        --(-83000110, 11902),
        --(-83000111, 11903),
        --(-83000113, 11904),
        --(-83000114, 11905),
        --(-83000115, 11906),
        --(-83000116, 11907),
        --(-83000117, 11908),
        --(-83000118, 11909),
        --(-83000119, 11910),
        --(-83000102, 11911),
        --(-83000120, 11912),
        --(-83000121, 11913),
        --(-83000122, 11914),
        --(-83000123, 11915),
        --(-83000124, 11916),
        --(-83000125, 11917),
        --(-83000126, 11918),
        --(-83000127, 11919),
        --(-83000103, 11920),
        --(-83000104, 11921),
        --(-83000105, 11922),
        --(-83000106, 11923),
        --(-83000107, 11924),
        --(-83000108, 11925),
        --(-83000109, 11926),
        --(-83000200, 11927),
        --(-83000201, 11928),
        --(-83000210, 11929),
        --(-83000211, 11930),
        --(-83000212, 11931),
        --(-83000213, 11932),
        --(-83000214, 11933),
        --(-83000215, 11934),
        --(-83000216, 11935),
        --(-83000217, 11936),
        --(-83000218, 11937),
        --(-83000219, 11938),
        --(-83000202, 11939),
        --(-83000220, 11940),
        --(-83000221, 11941),
        --(-83000222, 11942),
        --(-83000223, 11943),
        --(-83000224, 11944),
        --(-83000225, 11945),
        --(-83000226, 11946),
        --(-83000227, 11947),
        --(-83000203, 11948),
        --(-83000204, 11949),
        --(-83000205, 11950),
        --(-83000206, 11951),
        --(-83000207, 11952),
        --(-83000208, 11953),
        --(-83000209, 11954),
        --(-83000300, 11955),
        --(-83000301, 11956),
        --(-83000302, 11957),
        --(-83000303, 11958),
        --(-83000304, 11959),
        --(-83010100, 11960),
        --(-83010101, 11961),
        --(-83010110, 11962),
        --(-83010111, 11963),
        --(-83010112, 11964),
        --(-83010113, 11965),
        --(-83010114, 11966),
        --(-83010115, 11967),
        --(-83010116, 11968),
        --(-83010117, 11969),
        --(-83010118, 11970),
        --(-83010102, 11971),
        --(-83010103, 11972),
        --(-83010104, 11973),
        --(-83010105, 11974),
        --(-83010106, 11975),
        --(-83010107, 11976),
        --(-83010108, 11977),
        --(-83010109, 11978),
        --(-83010200, 11979),
        --(-83010201, 11980),
        --(-83010210, 11981),
        --(-83010211, 11982),
        --(-83010212, 11983),
        --(-83010213, 11984),
        --(-83010214, 11985),
        --(-83010215, 11986),
        --(-83010202, 11987),
        --(-83010203, 11988),
        --(-83010204, 11989),
        --(-83010205, 11990),
        --(-83010206, 11991),
        --(-83010207, 11992),
        --(-83010208, 11993),
        --(-83010209, 11994),
        --(-83010300, 11995),
        --(-83020100, 11996),
        --(-83020101, 11997),
        --(-83020102, 11998),
        --(-83020103, 11999),
        --(-83020200, 12000),
        --(-83020201, 12001),
        --(-83020202, 12002),
        --(-83020203, 12003),

        --(-86000100, 12100),
        --(-86000200, 12101),
        --(-86000300, 12102),

        --(-87000100, 12200),
        --(-87000200, 12201),

        --(-89010100, 12300),
        --(-89010200, 12301),
        --(-89010300, 12302);

INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
              (-8, -10000, 0, 'ALERT.ccAlert', 0, '', 0, 0, '0', '1', NULL, NULL);

INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes,  smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
        --(11180,  -8,  'CHANNEL_LINK_UP',  'ALERT.ccChannelUp',  4,  '',  0,  0,  '0',  '1',  '',  ''),
        (11181,  -8,  'CHANNEL_LINK_DOWN',  'ALERT.ccChannelDown',  5,  '',  0,  0,  '0',  '1',  '',  '');

/*CC事件代码定义，把设备上报的告警做为事件统一处理*/
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
        (-8, -10000, 'CCMTS Event', 'EVENT.ccAlert', '');

INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        (11180,  -8,  'CHANNEL_LINK_UP',  'EVENT.ccChannelUp'),
        (11181,  -8,  'CHANNEL_LINK_DOWN',  'EVENT.ccChannelDown');

--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
        --(10010, 10000, 'Authentication and Encryption', '认证加密', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(11100,  10010, 'Auth Reject - No Information',  '认证拒绝(没有信息)'),
        --(11101,  10010, 'Auth Reject - Unauthorized CM',  '认证拒绝(CM未授权)'),
        --(11102,  10010, 'Auth Reject - Unauthorized SAID',  '认证拒绝(SAID未授权)'),
        --(11103,  10010, 'Auth Reject - Permanent Authorization Failure',  '认证拒绝(永久性授权失败)'),
        --(11104,  10010, 'Auth Reject - Time of Day not acquired',  '认证拒绝(未得到时间)'),
        --(11105,  10010, 'Auth Reject - EAE disabled',  '认证拒绝(EAE不可用)'),
        --(11106,  10010, 'CM Certificate Error',  'CM证书错误'),
        --(11107,  10010, 'Auth Invalid - No Information',  '认证失效(没有信息)'),
        --(11108,  10010, 'Auth Invalid - Unauthorized CM',  '认证失效(CM未授权)'),
        --(11109,  10010, 'Auth Invalid - Unsolicited',  '认证失效(Unsolicited)'),
        --(11110,  10010, 'Auth Invalid - Invalid Key Sequence Number',  '认证失效(序列号无效)'),
        --(11111,  10010, 'Auth Invalid - Message (Key Request) Authentication Failure',  '认证失效(密钥请求认证失败)'),
        --(11112,  10010, 'Unsupported Crypto Suite(BPKM)',  '加密组件不支持(BPKM)'),
        --(11113,  10010, 'Failed to retrieve CRL from CRL Server IP',  '重新获取CRL失败'),
        --(11114,  10010, 'Failed to retrieve OCSP status',  '重新获取OCSP状态失败'),
        --(11115,  10010, 'CRL data not available when validating CM certificate chain',  '获取CRL数据不可用'),
        --(11116,  10010, 'Key Reject - No Information',  '密钥拒绝(没有信息)'),
        --(11117,  10010, 'Key Reject - Unauthorized SAID',  '密钥拒绝(SAID未授权)'),
        --(11118,  10010, 'TEK Invalid - No Information',  'TEK无效(没有信息)'),
        --(11119,  10010, 'TEK Invalid - Invalid Key Sequence Number',  'TEK无效(序列号无效)'),
        --(11120,  10010, 'Unsupported Crypto Suite(Dynamic SA)',  '加密组件不支持(Dynamic SA)'),
        --(11121,  10010, 'Map Reject - Downstream Traffic Flow Not Mapped to BPI+ SAID',  '映射拒绝(下行通信流未映射到BPI和SAID)'),
        --(11122,  10010, 'Map Reject-Not Authorized for Requested Downstream Traffic Flow',  '映射拒绝(请求的下行通信流未授权)'),
        --(11123,  10010, 'Mapped to Existing SAID',  '映射到已存在的SAID'),
        --(11124,  10010, 'Mapped to New SAID',  '映射到新的SAID'),
        --(11125,  10010, 'Missing Required TLV Type',  '缺少所需的TLV类型'),
         --(11126,  10010, 'The first Configuration TLV Type that contain invalid value',  'TLV值无效');
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
        --(10020, 10000, 'DBC,  DCC and UCC', 'DBC，DCC和UCC', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(11200,  10020, 'Unknown DBC transaction',  '未知的DBC传输'),
        --(11201,  10020, 'DBC-REQ rejected - confirmation code',  'DBC-REQ拒绝(确认码)'),
        --(11202,  10020, 'DBC-RSP not received',  '未收到DBC-RSP'),
        --(11203,  10020, 'Bad CM DBC-RSP',  '损坏的CM DBC-RSP'),
        --(11204,  10020, 'DBC-RSP Partial Service reason',  'DBC-RSP不公平服务原因'),
        --(11205,  10020, 'DCC rejected already there',  'DCC冲突'),
        --(11206,  10020, 'DCC depart old',  '原通道剥离'),
        --(11207,  10020, 'DCC arrive new',  '新通道到达'),
        --(11208,  10020, 'DCC aborted unable to acquire new downstream channel',  'DCC被取消(无法获取新下行通道)'),
        --(11209,  10020, 'DCC aborted no UCD for new upstream channel',  'DCC被取消(在新上行通道未获得UCD消息)'),
        --(11210,  10020, 'DCC aborted unable to communicate on new upstream channel',  'DCC被取消(在新上行通道上不能通信)'),
        --(11211,  10020, 'DCC rejected unspecified reason',  'DCC被拒绝(未指定原因)'),
        --(11212,  10020, 'DCC rejected permanent - DCC not supported',  '永久性DCC拒绝(DCC不支持)'),
        --(11213,  10020, 'DCC rejected service flow not found',  'DCC被拒绝(服务流未找到)'),
        --(11214,  10020, 'DCC rejected required parameter not present',  'DCC被拒绝(所需参数不存在)'),
        --(11215,  10020, 'DCC rejected authentication failure',  'DCC被拒绝(认证失败)'),
        --(11216,  10020, 'DCC rejected multiple errors',  'DCC被拒绝(多种错误)'),
        --(11217,  10020, 'DCC rejected,  duplicate SF reference-ID or index in message',  'DCC被拒绝(重复的SFID reference-ID或索引)'),
        --(11218,  10020, 'DCC rejected parameter invalid for context',  'DCC被拒绝(无效的参数)'),
        --(11219,  10020, 'DCC rejected message syntax error',  'DCC被拒绝(消息语法错误)'),
        --(11220,  10020, 'DCC rejected message too big',  'DCC被拒绝(消息数据量太大)'),
        --(11221,  10020, 'DCC rejected 2.0 mode disabled',  'DCC被拒绝(2.0模式不可用)'),
        --(11222,  10020, 'DCC-RSP not received on old channel',  '在原有通道上未收到DCC-RSP消息'),
        --(11223,  10020, 'DCC-RSP not received on new channel',  '在新通道上未收到DCC-RSP消息'),
        --(11224,  10020, 'DCC-RSP rejected unspecified reason',  'DCC-RSP被拒绝(不明原因)'),
        --(11225,  10020, 'DCC-RSP rejected unknown transaction ID',  'DCC-RSP被拒绝(未知的交互ID)'),
        --(11226,  10020, 'DCC-RSP rejected authentication failure',  'DCC-RSP被拒绝(认证失败)'),
        --(11227,  10020, 'DCC-RSP rejected message syntax error',  'DCC-RSP被拒绝(消息语法错误)'),
        --(11228,  10020, 'DCC-ACK not received',  '未收到DCC-ACK'),
        --(11229,  10020, 'DCC-ACK rejected unspecified reason',  'DCC-ACK被拒绝(不明原因)'),
        --(11230,  10020, 'DCC-ACK rejected unknown transaction ID',  'DCC-ACK被拒绝(未知的交互ID)'),
        --(11231,  10020, 'DCC-ACK rejected authentication failure',  'DCC-ACK被拒绝(认证失败)'),
        --(11232,  10020, 'DCC-ACK rejected message syntax error',  'DCC-ACK被拒绝(消息语法错误)'),
        --(11233,  10020, 'UCC-RSP not received on previous channel ID',  '原有通道未收到UCC-RSP'),
        --(11234,  10020, 'UCC-RSP received with invalid channel ID',  '收到的UCC-RSP消息的通道ID无效'),
        --(11235,  10020, 'UCC-RSP received with invalid channel ID on new channel',  '在新通道上收到的UCC-RSP消息的通道ID无效');
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
        --(10030, 10000, 'Registration and TLV-11', '注册和TLV-11', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(11300,  10030, 'Service unavailable - Other',  '无法提供服务(其他)'),
        --(11301,  10030, 'Service unavailable - Unrecognized configuration setting',  '无法提供服务(不识别的配置设置)'),
        --(11302,  10030, 'Service unavailable - Temporarily unavailable',  '无法提供服务(暂时不提供)'),
        --(11303,  10030, 'Service unavailable - Permanent',  '无法提供服务(永久性不提供)'),
        --(11304,  10030, 'Registration rejected authentication failure: CMTS MIC invalid',  '注册被拒绝(认证失败:无效的CMTS MIC)'),
        --(11305,  10030, 'REG REQ has Invalid MAC header',  'REG REQ的MAC头无效'),
        --(11306,  10030, 'REG REQ has Invalid SID or not in use',  'REG REQ的SID无效或者未使用'),
        --(11307,  10030, 'REG REQ missed Required TLVs',  'REG REQ缺少所需的TLVs'),
        --(11308,  10030, 'Bad DS FREQ - Format Invalid',  '无效的下行中心频率(无效的格式)'),
        --(11309,  10030, 'Bad DS FREQ - Not in use',  'Bad DS FREQ(未使用)'),
        --(11310,  10030, 'Bad DS FREQ - Not Multiple of 62500 Hz',  'Bad DS FREQ(不是62500Hz的倍数)'),
        --(11311,  10030, 'Bad US CH - Invalid or Unassigned',  'Bad US CH(无效或者未赋值)'),
        --(11312,  10030, 'Bad US CH - Change followed with (RE-) Registration REQ',  'Bad US CH(随着Registration REQ改变)'),
        --(11313,  10030, 'Bad US CH - Overload',  '上行通道超负荷'),
        --(11314,  10030, 'Network Access has Invalid Parameter',  '携带无效参数的网络访问'),
        --(11315,  10030, 'Bad Class of Service - Invalid Configuration',  'Bad Class of Service(无效的配置)'),
        --(11316,  10030, 'Bad Class of Service - Unsupported class',  'Bad Class of Service(不支持的类别)'),
        --(11317,  10030, 'Bad Class of Service - Invalid class ID or out of range',  'Bad Class of Service(无效的类别ID或者越界)'),
        --(11318,  10030, 'Bad Max DS Bit Rate - Invalid Format',  '无效的最大下行比特率(无效的格式)'),
        --(11319,  10030, 'Bad Max DS Bit Rate Unsupported Setting',  '无效的最大下行比特率(不支持的设置)'),
        --(11320,  10030, 'Bad Max US Bit - Invalid Format',  'Bad Max US Bit(无效的格式)'),
        --(11321,  10030, 'Bad Max US Bit Rate - Unsupported Setting',  'Bad Max US Bit Rate(不支持的设置)'),
        --(11322,  10030, 'Bad US Priority Configuration - Invalid Format',  '无效的上行优先级配置(无效的格式)'),
        --(11323,  10030, 'Bad US Priority Configuration - Setting out of Range',  '设置的上行优先级越界'),
        --(11324,  10030, 'Guaranteed MUBR Configuration setting - Invalid Format',  '设置的最小上行接收速率格式无效'),
        --(11325,  10030, 'Guaranteed MUBR Configuration setting - Exceed Max US Bit Rate',  '无效的最小上行接收速率设置(超过最大值)'),
        --(11326,  10030, 'Guaranteed MUBR Configuration setting - Out of Range',  '无效的最小上行接收速率设置(越界)'),
        --(11327,  10030, 'MUTB configuration setting - Invalid Format',  '设置的最大上行发射突发格式无效'),
        --(11328,  10030, 'MUTB configuration setting - Out of Range',  '设置的最大上行发射突发越界'),
        --(11329,  10030, 'Invalid Modem Capabilities configuration setting',  '无效的Modem使能设置'),
        --(11330,  10030, 'Parameter is outside of the range in configuration file',  '配置文件参数越界'),
        --(11331,  10030, 'Registration authentication failure',  '注册认证失败:REG REQ被拒绝(TLV参数和学习的TLV参数不匹配)'),
        --(11332,  10030, 'REG REQ rejected - Unspecified reason',  'REG REQ被拒绝(不明原因)'),
        --(11333,  10030, 'REG REQ rejected - Unrecognized configuration setting',  'REG REQ被拒绝(不识别的配置设置)'),
        --(11334,  10030, 'REG REQ rejected - Major service flow error',  'REG REQ被拒绝(主要的服务流错误)'),
        --(11335,  10030, 'REG REQ rejected - Major classifier error',  'REG REQ被拒绝(主要的分类器错误)'),
        --(11336,  10030, 'REG REQ rejected - Major PHS rule error',  'REG REQ被拒绝(主要的PHS规则错误)'),
        --(11337,  10030, 'REG REQ rejected - Multiple major errors',  'REG REQ被拒绝(多种重大错误)'),
        --(11338,  10030, 'REG REQ rejected - Message syntax error',  'REG REQ被拒绝(消息语法错误)'),
        --(11339,  10030, 'REG REQ rejected - Primary service flow error',  'REG REQ被拒绝(主服务流错误)'),
        --(11340,  10030, 'REG REQ rejected - temporary no resource',  'REG REQ被拒绝(暂时没有资源)'),
        --(11341,  10030, 'REG REQ rejected - Permanent administrative',  'REG REQ被拒绝(永久性管理)'),
        --(11342,  10030, 'REG REQ rejected - Required parameter not present',  'REG REQ被拒绝(所需参数不存在)'),
        --(11343,  10030, 'REG REQ rejected - Header suppression setting not supported',  'REG REQ被拒绝(不支持头抑制设置x)'),
        --(11344,  10030, 'REG REQ rejected - Multiple errors',  'REG REQ被拒绝(多种错误)'),
        --(11345,  10030, 'REG REQ rejected - duplicate reference-ID or index in message',  'REG REQ被拒绝(重复的reference-ID或索引)'),
        --(11346,  10030, 'REG REQ rejected - parameter invalid for context',  'REG REQ被拒绝(在上下文中参数无效)'),
        --(11347,  10030, 'REG REQ rejected - Authorization failure',  'REG REQ被拒绝(授权失败)'),
        --(11348,  10030, 'REG REQ rejected - Message too big',  'REG REQ被拒绝(消息量太大)'),
        --(11349,  10030, 'REG aborted no REG - ACK',  'REG被取消(没有REG-ACK)'),
        --(11350,  10030, 'REG ACK rejected unspecified reason',  'REG ACK被拒绝(不明原因)'),
        --(11351,  10030, 'REG ACK rejected message syntax error',  'REG ACK拒绝(消息语法错误)'),
        --(11352,  10030, 'T9 Timeout - Never received REG-REQ or all REG-REQ-MP fragments',  'T9超时(从未收到REG-REQ或者所有的REG-REQ-MP片段)'),
        --(11353,  10030, 'Missing RCP in REG-REQ or REG-REQ-MP',  '在REG-REQ或REG-REQ-MP中丢失RCP'),
        --(11354,  10030, 'Received Non-Queue-Depth Based Bandwidth Request(MTC mode)',  '收到基于带宽请求的Non-Queue-Depth，并且多传输通道模式可用'),
        --(11355,  10030, 'Received Queue-Depth Based Bandwidth Request(disabled MTC mode)',  '多传输通道模式不可用时收到基于带宽请求的Queue-Depth'),
        --(11356,  10030, 'Received REG-ACK with TCS - Partial Service',  '收到有TCS的REG-ACK(不公平的服务)'),
        --(11357,  10030, 'Received REG-ACK with RCS - Partial Service',  '收到有RCS的REG-ACK(不公平的服务)'),
        --(11358,  10030, 'T6 Timer expires and Retries Exceeded',  'T6计时器终止且重试超限'),
        --(11359,  10030, 'Initializing Channel Timeout',  '初始化通道超时'),
        --(11360,  10030, 'REG-REQ-MP received when no MDD present',  '不存在MDD时收到REG-REQ-MP');
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
        --(10040, 10000, 'QoS', 'QoS', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(11500, 10040,  'Attribute Masks for SFID do not satisfy those in the SCN',  'SFID关联的属性不符合Service CLASS name');
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
       -- (10050, 10000, 'Ranging', '测距', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(11700,  10050, 'No Ranging Requests received from POLLED CM',  '未从被轮询的CM处获得测距请求'),
        --(11701,  10050, 'Retries exhausted for polled CM (report MAC address)',  '轮询CM重试耗尽'),
        --(11702,  10050, 'Unable to Successfully Range CM Retries Exhausted',  '不能成功的测距重试耗尽'),
        --(11800,  10050, 'Failed to receive Periodic RNG-REQ from modem',  '从modem上接受周期性的RNG-REQ失败'),
        --(11801,  10050, 'CM transmitted B-INIT-RNG-REQ with MD-DS-SG ID of zero',  'CM发送MD-DS-SG ID为0的B-INIT-RNG-REQ');
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
       -- (10060, 10000, 'Dynamic Services', '动态服务', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(11900,  10060, 'Service Add rejected - Unspecified reason',  '服务增加被拒绝(不明原因)'),
        --(11901,  10060, 'Service Add rejected - Unrecognized configuration setting',  '服务增加被拒绝(不识别的配置设置)'),
        --(11902,  10060, 'Service Add rejected - Classifier not found',  '服务增加被拒绝(未找到分类器)'),
        --(11903,  10060, 'Service Add rejected - Classifier exists',  '服务增加被拒绝(分类器存在)'),
        --(11904,  10060, 'Service Add rejected - PHS rule exists',  '服务增加被拒绝(PHS规则存在)'),
        --(11905,  10060, 'Service Add rejected - Duplicated reference-ID or index',  '服务增加被拒绝(重复的reference-ID或索引)'),
        --(11906,  10060, 'Service Add rejected - Multiple upstream flows',  '服务增加被拒绝(多个上行流)'),
        --(11907,  10060, 'Service Add rejected - Multiple downstream flows',  '服务增加被拒绝(多个下行流)'),
        --(11908,  10060, 'Service Add rejected - Classifier for another flow',  '服务增加被拒绝(分类器为其它流使用)'),
        --(11909,  10060, 'Service Add rejected - PHS rule for another flow',  '服务增加被拒绝(PHS规则为其它流使用)'),
        --(11910,  10060, 'Service Add rejected - Parameter invalid for context',  '服务增加被拒绝(参数无效)'),
        --(11911,  10060, 'Service Add rejected - Temporary no resource',  '服务增加被拒绝(暂时没有资源)'),
        --(11912,  10060, 'Service Add rejected - Authorization failure',  '服务增加被拒绝(授权失败)'),
        --(11913,  10060, 'Service Add rejected - Major service flow error',  '服务增加被拒绝(主要的服务流错误)'),
        --(11914,  10060, 'Service Add rejected - Major classifier error',  '服务增加被拒绝(主要的分类器错误)'),
        --(11915,  10060, 'Service Add rejected - Major PHS rule error',  '服务增加被拒绝(主要的PHS规则错误)'),
        --(11916,  10060, 'Service Add rejected - Multiple major errors',  '服务增加被拒绝(多种重大错误)'),
        --(11917,  10060, 'Service Add rejected - Message syntax error',  '服务增加被拒绝(消息语法错误)'),
        --(11918,  10060, 'Service Add rejected - Message too big',  '服务增加被拒绝(消息量太大)'),
        --(11919,  10060, 'Service Add rejected - Temporary DCC',  '服务增加被拒绝(临时的DCC)'),
        --(11920,  10060, 'Service Add rejected - Permanent administrative',  '服务增加被拒绝(永久性管理)'),
        --(11921,  10060, 'Service Add rejected - Required parameter not present',  '服务增加被拒绝(所需参数不存在)'),
        --(11922,  10060, 'Service Add rejected - Header suppression setting not supported',  '服务增加被拒绝(不支持头抑制设置)'),
        --(11923,  10060, 'Service Add rejected - Service flow exists',  '服务增加被拒绝(服务流存在)'),
        --(11924,  10060, 'Service Add rejected - HMAC Auth failure',  '服务增加被拒绝(HMAC认证失败)'),
        --(11925,  10060, 'Service Add rejected - Add aborted',  '服务增加被拒绝(增加被取消)'),
        --(11926,  10060, 'Service Add rejected - Multiple errors',  '服务增加被拒绝(多种错误)'),
        --(11927,  10060, 'Service Change rejected - Unspecified reason',  '服务改变被拒绝(不明原因)'),
        --(11928,  10060, 'Service Change rejected - Unrecognized configuration setting',  '服务改变被拒绝(不识别的配置设置)'),
        --(11929,  10060, 'Service Change rejected - Classifier not found',  '服务改变被拒绝(分类器未找到)'),
        --(11930,  10060, 'Service Change rejected - Classifier exists',  '服务改变被拒绝(分类器存在)'),
        --(11931,  10060, 'Service Change rejected - PHS rule not found',  '服务改变被拒绝(PHS规则未找到)'),
        --(11932,  10060, 'Service Change rejected - PHS rule exists',  '服务改变被拒绝(PHS规则存在)'),
        --(11933,  10060, 'Service Change rejected - Duplicated reference-ID or index',  '服务改变被拒绝(重复的reference-ID或索引)'),
        --(11934,  10060, 'Service Change rejected - Multiple upstream flows',  '服务改变被拒绝(多个上行流)'),
        --(11935,  10060, 'Service Change rejected - Multiple downstream flows',  '服务改变被拒绝(多个下行流)'),
        --(11936,  10060, 'Service Change rejected - Classifier for another flow',  '服务改变被拒绝(分类器为其它流使用)'),
        --(11937,  10060, 'Service Change rejected - PHS rule for another flow',  '服务改变被拒绝(PHS规则为其它流使用)'),
        --(11938,  10060, 'Service Change rejected - Invalid parameter for context',  '服务改变被拒绝(无效的参数)'),
        --(11939,  10060, 'Service Change rejected - Temporary no resource',  '服务改变被拒绝(暂时没有资源)'),
        --(11940,  10060, 'Service Change rejected - Authorization failure',  '服务改变被拒绝(授权失败)'),
        --(11941,  10060, 'Service Change rejected - Major service flow error',  '服务改变被拒绝(主要的服务流错误)'),
        --(11942,  10060, 'Service Change rejected - Major classifier error',  '服务改变被拒绝(主要的分类器错误)'),
        --(11943,  10060, 'Service Change rejected - Major PHS error',  '服务改变被拒绝(主要的PHS规则错误)'),
        --(11944,  10060, 'Service Change rejected - Multiple major errors',  '服务改变被拒绝(多种重大错误)'),
        --(11945,  10060, 'Service Change rejected - Message syntax error',  '服务改变被拒绝(消息语法错误)'),
        --(11946,  10060, 'Service Change rejected - Message too big',  '服务改变被拒绝(消息量太大)'),
        --(11947,  10060, 'Service Change rejected - Temporary DCC',  '服务改变被拒绝(临时的DCC)'),
        --(11948,  10060, 'Service Change rejected - Permanent administrative',  '服务改变被拒绝(永久性管理)'),
        --(11949,  10060, 'Service Change rejected - Requester not owner of service flow',  '服务改变被拒绝(请求者不是服务流的拥有者)'),
        --(11950,  10060, 'Service Change rejected - Service flow not found',  '服务改变被拒绝(服务流未找到)'),
        --(11951,  10060, 'Service Change rejected - Required parameter not present',  '服务改变被拒绝(所需参数不存在)'),
        --(11952,  10060, 'Service Change rejected-Header suppression setting not supported',  '服务改变被拒绝(不支持头抑制设置)'),
        --(11953,  10060, 'Service Change rejected - HMAC Auth failure',  '服务改变被拒绝(HMAC认证失败)'),
        --(11954,  10060, 'Service Change rejected - Multiple errors',  '服务改变被拒绝(多种错误)'),
        --(11955,  10060, 'Service Delete rejected - Unspecified reason',  '服务删除被拒绝(不明原因)'),
        --(11956,  10060, 'Service Delete rejected - Requester not owner of service flow',  '服务删除被拒绝(请求者不是服务流的拥有者)'),
        --(11957,  10060, 'Service Delete rejected - Service flow not found',  '服务删除被拒绝(服务流未找到)'),
        --(11958,  10060, 'Service Delete rejected - HMAC Auth failure',  '服务删除被拒绝(HMAC认证失败)'),
        --(11959,  10060, 'Service Delete rejected - Message syntax error',  '服务删除被拒绝(消息语法错误)'),
        --(11960,  10060, 'Service Add Response rejected - Invalid transaction ID(DSR)',  '服务增加应答被拒绝(无效的传输ID)(动态服务响应)'),
        --(11961,  10060, 'Service Add aborted - No RSP',  '服务增加应答被拒绝(没有RSP)'),
        --(11962,  10060, 'Service Add Response rejected - PHS rule exists',  '服务增加应答被拒绝(PHS规则存在)'),
        --(11963,  10060, 'Service Add Response rejected - Duplicate reference_ID or index',  '服务增加应答被拒绝(重复的reference-ID或索引)'),
        --(11964,  10060, 'Service Add Response rejected - Classifier for another flow',  '服务增加应答被拒绝(分类器为其它流使用)'),
        --(11965,  10060, 'Service Add Response rejected - Parameter invalid for context',  '服务增加应答被拒绝(无效的参数)'),
        --(11966,  10060, 'Service Add Response rejected - Major service flow error',  '服务增加应答被拒绝(主要的服务流错误)'),
        --(11967,  10060, 'Service Add Response rejected - Major classifier error',  '服务增加应答被拒绝(主要的分类器错误)'),
        --(11968,  10060, 'Service Add Response rejected - Major PHS Rule error',  '服务增加应答被拒绝(主要的PHS规则错误)'),
        --(11969,  10060, 'Service Add Response rejected - Multiple major errors',  '服务增加应答被拒绝(多种重大错误)'),
        --(11970,  10060, 'Service Add Response rejected - Message too big',  '服务增加应答被拒绝(消息量太大)'),
        --(11971,  10060, 'Service Add Response rejected - HMAC Auth failure',  '服务增加应答被拒绝(HMAC认证失败)'),
        --(11972,  10060, 'Service Add Response rejected - Message syntax error',  '服务增加应答被拒绝(消息语法错误)'),
        --(11973,  10060, 'Service Add Response rejected - Unspecified reason',  '服务增加应答被拒绝(不明原因)'),
        --(11974,  10060, 'Service Add Response rejected-Unrecognized configuration setting',  '服务增加应答被拒绝(不识别的配置设置)'),
        --(11975,  10060, 'Service Add Response rejected - Required parameter not present',  '服务增加应答被拒绝(所需参数不存在)'),
        --(11976,  10060, 'Service Add Response rejected - Service Flow exists',  '服务增加应答被拒绝(服务流存在)'),
        --(11977,  10060, 'Service Add Response rejected - Multiple errors',  '服务增加应答被拒绝(多种错误)'),
        --(11978,  10060, 'Service Add Response rejected - Classifier exists',  '服务增加应答被拒绝(不识别的配置设置)'),
        --(11979,  10060, 'Service Change Response rejected - Invalid transaction ID',  '服务增加应答被拒绝(无效的传输ID)'),
        --(11980,  10060, 'Service Change aborted - No RSP',  '服务增加应答被取消(没有RSP)'),
        --(11981,  10060, 'Service Change Response rejected-Duplicated',  '服务改变应答被拒绝(重复的reference-ID或索引)'),
        --(11982,  10060, 'Service Change Response rejected - Invalid parameter for context',  '服务改变应答被拒绝(无效参数)'),
        --(11983,  10060, 'Service Change Response rejected - Major classifier error',  '服务改变应答被拒绝(主要的分类器错误)'),
        --(11984,  10060, 'Service Change Response rejected - Major PHS rule error',  '服务改变应答被拒绝(主要的PHS规则错误)'),
        --(11985,  10060, 'Service Change Response rejected - Multiple Major errors',  '服务改变应答被拒绝(多种重大错误)'),
        --(11986,  10060, 'Service Change Response rejected - Message too big',  '服务改变应答被拒绝(消息量太大)'),
        --(11987,  10060, 'Service Change Response rejected - HMAC Auth failure',  '服务改变应答被拒绝(HMAC认证失败)'),
        --(11988,  10060, 'Service Change Response rejected - Message syntax error',  '服务改变应答被拒绝(消息语法错误)'),
        --(11989,  10060, 'Service Change Response rejected - Unspecified reason',  '服务改变应答被拒绝(不明原因)'),
        --(11990,  10060, 'Service Change Response rejected - Unrecognized configuration',  '服务改变应答被拒绝(不识别的配置设置)'),
        --(11991,  10060, 'Service Change Response rejected - Not required parameter',  '服务改变应答被拒绝(所需参数不存在)'),
        --(11992,  10060, 'Service Change Response rejected - Multiple errors',  '服务改变应答被拒绝(多种错误)'),
        --(11993,  10060, 'Service Change Response rejected - Classifier exists',  '服务改变应答被拒绝(分类器存在)'),
        --(11994,  10060, 'Service Change Response rejected - PHS rule exists',  '服务改变应答被拒绝(PHS规则存在)'),
        --(11995,  10060, 'Service Delete Response rejected - Invalid transaction ID',  '服务删除应答被拒绝(无效传输ID)'),
        --(11996,  10060, 'Service Add Response rejected - Invalid Transaction ID(DSA)',  '服务增加应答被拒绝(无效传输ID)(动态服务确认)'),
        --(11997,  10060, 'Service Add Aborted - No ACK',  '服务增加被取消(没有确认)'),
        --(11998,  10060, 'Service Add ACK rejected - HMAC auth failure',  '服务增加确认被拒绝(HMAC认证失败)'),
        --(11999,  10060, 'Service Add ACK rejected - Message syntax error',  '服务增加确认被拒绝(消息语法错误)'),
        --(12000,  10060, 'Service Change ACK rejected - Invalid transaction ID',  '服务改变确认被拒绝(无效传输ID)'),
        --(12001,  10060, 'Service Change Aborted - No ACK',  '服务改变被取消(没有确认)'),
        --(12002,  10060, 'Service Change ACK rejected - HMAC Auth failure',  '服务改变确认被拒绝(HAMC认证失败)'),
        --(12003,  10060, 'Service Change ACK rejected - Message syntax error',  '服务改变确认被拒绝(消息语法错误)');
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
        --(10070, 10000, 'Diagnostic Log', '诊断日志', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(12100,  10070, 'Diagnostic log size reached high threshold',  '日志大小达到设定上限'),
        --(12101,  10070, 'Diagnostic log size dropped to low threshold',  '日志大小低于设定下限'),
        --(12102,  10070, 'Diagnostic log size reached full threshold',  '日志已满');
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
         --(10080, 10000, 'IPDR', '因特网数据记录', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(12200,  10080, 'IPDR Connection Terminated',  'IPDR连接中断'),
        --(12201,  10080, 'IPDR Collector Failover Error: Backup Collector IP',  'IPDR接收端故障转移出错(备份接收端IP)');
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
          --(10090, 10000, 'Multicast', '组播', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(12300, 10090,  'Aggregate Session Limit defined exceeded by join',  '会话总数超出限制'),
        --(12301, 10090,  'Multicast session S, G of the join Y not authorized',  '加入Y的组播会话S, G未授权'),
        --(12302, 10090,  'Multicast Profile Profile Name created',  '组播产生的Profile Name');
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
           --(10100, 10000, 'CM-STATUS', 'CM-STATUS', '');
--INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        --(11400, 10100,  'CM-STATUS received prior to REG-ACK',  'REG-ACK之前收到CM-STATUS消息'),
        --(11401, 10100,  'CM-STATUS received while enable bit cleared',  'enable位被清除时收到CM-STATUS消息'),
        --(11402, 10100,  'CM-STATUS received - secondary channel MDD timeout',  '收到CM-STATUS消息(次要通道MDD超时)'),
        --(11403, 10100,  'CM-STATUS received - QAM/FEC lock failure',  '收到CM-STATUS消息(QAM/FEC阻塞失败)'),
        --(11404, 10100,  'CM-STATUS received - sequence out-of-range',  '收到CM-STATUS消息(序列越界)'),
        --(11405, 10100,  'CM-STATUS received - MDD recovery',  '收到CM-STATUS消息(MDD恢复)'),
        --(11406, 10100,  'CM-STATUS received - QAM/FEC recovery',  '收到CM-STATUS消息(QAM/FEC恢复)'),
        --(11407, 10100,  'CM-STATUS received - T4 timeout',  '收到CM-STATUS消息(T4超时)'),
        --(11408, 10100,  'CM-STATUS received - T3 retries exceeded',  '收到CM-STATUS消息(T3重试超限)'),
        --(11600, 10100,  'CM-CTRL - Command: mute,  or cmReinit,  or forwarding',  'CM-CTRL(命令:mute,  cmReinit,  或者forwarding)'),
        --(11601, 10100,  'CM-CTRL - Invalid message format',  'CM-CTRL消息格式无效');
        -- */
/**对告警的修改*/
--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
        --(10010, 10000, 0, 'Authentication and Encryption', '认证加密',  '',  0,  0,  '0',  '1',  '',  '');
--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
        --(11100, 10010, 4,  'Auth Reject - No Information',  '认证拒绝(没有信息)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11101, 10010, 4,  'Auth Reject - Unauthorized CM',  '认证拒绝(CM未授权)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11102, 10010, 4,  'Auth Reject - Unauthorized SAID',  '认证拒绝(SAID未授权)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11103, 10010, 4,  'Auth Reject - Permanent Authorization Failure',  '认证拒绝(永久性授权失败)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11104, 10010, 4,  'Auth Reject - Time of Day not acquired',  '认证拒绝(未得到时间)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11105, 10010, 1,  'Auth Reject - EAE disabled',  '认证拒绝(EAE不可用)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11106, 10010, 4,  'CM Certificate Error',  'CM证书错误',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11107, 10010, 4,  'Auth Invalid - No Information',  '认证失效(没有信息)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11108, 10010, 4,  'Auth Invalid - Unauthorized CM',  '认证失效(CM未授权)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11109, 10010, 4,  'Auth Invalid - Unsolicited',  '认证失效(Unsolicited)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11110, 10010, 4,  'Auth Invalid - Invalid Key Sequence Number',  '认证失效(序列号无效)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11111, 10010, 4,  'Auth Invalid - Message (Key Request) Authentication Failure',  '认证失效(密钥请求认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11112, 10010, 4,  'Unsupported Crypto Suite(BPKM)',  '加密组件不支持(BPKM)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11113, 10010, 3,  'Failed to retrieve CRL from CRL Server IP',  '重新获取CRL失败',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11114, 10010, 3,  'Failed to retrieve OCSP status',  '重新获取OCSP状态失败',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11115, 10010, 3,  'CRL data not available when validating CM certificate chain',  '获取CRL数据不可用',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11116, 10010, 4,  'Key Reject - No Information',  '密钥拒绝(没有信息)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11117, 10010, 4,  'Key Reject - Unauthorized SAID',  '密钥拒绝(SAID未授权)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11118, 10010, 4,  'TEK Invalid - No Information',  'TEK无效(没有信息)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11119, 10010, 4,  'TEK Invalid - Invalid Key Sequence Number',  'TEK无效(序列号无效)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11120, 10010, 4,  'Unsupported Crypto Suite(Dynamic SA)',  '加密组件不支持(Dynamic SA)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11121, 10010, 1,  'Map Reject - Downstream Traffic Flow Not Mapped to BPI+ SAID',  '映射拒绝(下行通信流未映射到BPI和SAID)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11122, 10010, 4,  'Map Reject-Not Authorized for Requested Downstream Traffic Flow',  '映射拒绝(请求的下行通信流未授权)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11123, 10010, 4,  'Mapped to Existing SAID',  '映射到已存在的SAID',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11124, 10010, 4,  'Mapped to New SAID',  '映射到新的SAID',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11125, 10010, 2,  'Missing Required TLV Type',  '缺少所需的TLV类型',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11126, 10010, 2,  'The first Configuration TLV Type that contain invalid value',  'TLV值无效',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
        -- (10020 ,10000, 0, 'DBC,  DCC and UCC', 'DBC，DCC和UCC',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
        --(11200, 10020, 2,  'Unknown DBC transaction',  '未知的DBC传输',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11201, 10020, 3,  'DBC-REQ rejected - confirmation code',  'DBC-REQ拒绝(确认码)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11202, 10020, 3,  'DBC-RSP not received',  '未收到DBC-RSP',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11203, 10020, 3,  'Bad CM DBC-RSP',  '损坏的CM DBC-RSP',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11204, 10020, 3,  'DBC-RSP Partial Service reason',  'DBC-RSP不公平服务原因',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11205, 10020, 3,  'DCC rejected already there',  'DCC冲突',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11206, 10020, 2,  'DCC depart old',  '原通道剥离',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11207, 10020, 2,  'DCC arrive new',  '新通道到达',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11208, 10020, 3,  'DCC aborted unable to acquire new downstream channel',  'DCC被取消(无法获取新下行通道)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11209, 10020, 3,  'DCC aborted no UCD for new upstream channel',  'DCC被取消(在新上行通道未获得UCD消息)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11210, 10020, 3,  'DCC aborted unable to communicate on new upstream channel',  'DCC被取消(在新上行通道上不能通信)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11211, 10020, 3,  'DCC rejected unspecified reason',  'DCC被拒绝(未指定原因)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11212, 10020, 3,  'DCC rejected permanent - DCC not supported',  '永久性DCC拒绝(DCC不支持)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11213, 10020, 3,  'DCC rejected service flow not found',  'DCC被拒绝(服务流未找到)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11214, 10020, 3,  'DCC rejected required parameter not present',  'DCC被拒绝(所需参数不存在)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11215, 10020, 3,  'DCC rejected authentication failure',  'DCC被拒绝(认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11216, 10020, 3,  'DCC rejected multiple errors',  'DCC被拒绝(多种错误)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11217, 10020, 3,  'DCC rejected,  duplicate SF reference-ID or index in message',  'DCC被拒绝(重复的SFID reference-ID或索引)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11218, 10020, 3,  'DCC rejected parameter invalid for context',  'DCC被拒绝(无效的参数)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11219, 10020, 3,  'DCC rejected message syntax error',  'DCC被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11220, 10020, 3,  'DCC rejected message too big',  'DCC被拒绝(消息数据量太大)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11221, 10020, 3,  'DCC rejected 2.0 mode disabled',  'DCC被拒绝(2.0模式不可用)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11222, 10020, 3,  'DCC-RSP not received on old channel',  '在原有通道上未收到DCC-RSP消息',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11223, 10020, 3,  'DCC-RSP not received on new channel',  '在新通道上未收到DCC-RSP消息',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11224, 10020, 3,  'DCC-RSP rejected unspecified reason',  'DCC-RSP被拒绝(不明原因)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11225, 10020, 3,  'DCC-RSP rejected unknown transaction ID',  'DCC-RSP被拒绝(未知的交互ID)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11226, 10020, 3,  'DCC-RSP rejected authentication failure',  'DCC-RSP被拒绝(认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11227, 10020, 3,  'DCC-RSP rejected message syntax error',  'DCC-RSP被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11228, 10020, 3,  'DCC-ACK not received',  '未收到DCC-ACK',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11229, 10020, 3,  'DCC-ACK rejected unspecified reason',  'DCC-ACK被拒绝(不明原因)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11230, 10020, 3,  'DCC-ACK rejected unknown transaction ID',  'DCC-ACK被拒绝(未知的交互ID)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11231, 10020, 3,  'DCC-ACK rejected authentication failure',  'DCC-ACK被拒绝(认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11232, 10020, 3,  'DCC-ACK rejected message syntax error',  'DCC-ACK被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11233, 10020, 3,  'UCC-RSP not received on previous channel ID',  '原有通道未收到UCC-RSP',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11234, 10020, 3,  'UCC-RSP received with invalid channel ID',  '收到的UCC-RSP消息的通道ID无效',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11235, 10020, 3,  'UCC-RSP received with invalid channel ID on new channel',  '在新通道上收到的UCC-RSP消息的通道ID无效',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
        --(10030, 10000, 0, 'Registration and TLV-11', '注册和TLV-11',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
        --(11300, 10030, 3,  'Service unavailable - Other',  '无法提供服务(其他)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11301, 10030, 3,  'Service unavailable - Unrecognized configuration setting',  '无法提供服务(不识别的配置设置)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11302, 10030, 3,  'Service unavailable - Temporarily unavailable',  '无法提供服务(暂时不提供)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11303, 10030, 3,  'Service unavailable - Permanent',  '无法提供服务(永久性不提供)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11304, 10030, 3,  'Registration rejected authentication failure: CMTS MIC invalid',  '注册被拒绝(认证失败:无效的CMTS MIC)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11305, 10030, 3,  'REG REQ has Invalid MAC header',  'REG REQ的MAC头无效',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11306, 10030, 3,  'REG REQ has Invalid SID or not in use',  'REG REQ的SID无效或者未使用',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11307, 10030, 3,  'REG REQ missed Required TLVs',  'REG REQ缺少所需的TLVs',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11308, 10030, 3,  'Bad DS FREQ - Format Invalid',  '无效的下行中心频率(无效的格式)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11309, 10030, 3,  'Bad DS FREQ - Not in use',  'Bad DS FREQ(未使用)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11310, 10030, 3,  'Bad DS FREQ - Not Multiple of 62500 Hz',  'Bad DS FREQ(不是62500Hz的倍数)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11311, 10030, 3,  'Bad US CH - Invalid or Unassigned',  'Bad US CH(无效或者未赋值)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11312, 10030, 3,  'Bad US CH - Change followed with (RE-) Registration REQ',  'Bad US CH(随着Registration REQ改变)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11313, 10030, 3,  'Bad US CH - Overload',  '上行通道超负荷',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11314, 10030, 3,  'Network Access has Invalid Parameter',  '携带无效参数的网络访问',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11315, 10030, 3,  'Bad Class of Service - Invalid Configuration',  'Bad Class of Service(无效的配置)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11316, 10030, 3,  'Bad Class of Service - Unsupported class',  'Bad Class of Service(不支持的类别)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11317, 10030, 3,  'Bad Class of Service - Invalid class ID or out of range',  'Bad Class of Service(无效的类别ID或者越界)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11318, 10030, 3,  'Bad Max DS Bit Rate - Invalid Format',  '无效的最大下行比特率(无效的格式)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11319, 10030, 3,  'Bad Max DS Bit Rate Unsupported Setting',  '无效的最大下行比特率(不支持的设置)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11320, 10030, 3,  'Bad Max US Bit - Invalid Format',  'Bad Max US Bit(无效的格式)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11321, 10030, 3,  'Bad Max US Bit Rate - Unsupported Setting',  'Bad Max US Bit Rate(不支持的设置)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11322, 10030, 3,  'Bad US Priority Configuration - Invalid Format',  '无效的上行优先级配置(无效的格式)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11323, 10030, 3,  'Bad US Priority Configuration - Setting out of Range',  '设置的上行优先级越界',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11324, 10030, 3,  'Guaranteed MUBR Configuration setting - Invalid Format',  '设置的最小上行接收速率格式无效',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11325, 10030, 3,  'Guaranteed MUBR Configuration setting - Exceed Max US Bit Rate',  '无效的最小上行接收速率设置(超过最大值)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11326, 10030, 3,  'Guaranteed MUBR Configuration setting - Out of Range',  '无效的最小上行接收速率设置(越界)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11327, 10030, 3,  'MUTB configuration setting - Invalid Format',  '设置的最大上行发射突发格式无效',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11328, 10030, 3,  'MUTB configuration setting - Out of Range',  '设置的最大上行发射突发越界',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11329, 10030, 3,  'Invalid Modem Capabilities configuration setting',  '无效的Modem使能设置',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11330, 10030, 3,  'Parameter is outside of the range in configuration file',  '配置文件参数越界',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11331, 10030, 3,  'Registration authentication failure',  '注册认证失败:REG REQ被拒绝(TLV参数和学习的TLV参数不匹配)',  '',  0,  0,  '0',  '1',  '',  ''),
        --(11332, 10030, 3,  'REG REQ rejected - Unspecified reason',  'REG REQ被拒绝(不明原因)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11333, 10030, 3,  'REG REQ rejected - Unrecognized configuration setting',  'REG REQ被拒绝(不识别的配置设置)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11334, 10030, 3,  'REG REQ rejected - Major service flow error',  'REG REQ被拒绝(主要的服务流错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11335, 10030, 3,  'REG REQ rejected - Major classifier error',  'REG REQ被拒绝(主要的分类器错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11336, 10030, 3,  'REG REQ rejected - Major PHS rule error',  'REG REQ被拒绝(主要的PHS规则错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11337, 10030, 3,  'REG REQ rejected - Multiple major errors',  'REG REQ被拒绝(多种重大错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11338, 10030, 3,  'REG REQ rejected - Message syntax error',  'REG REQ被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11339, 10030, 3,  'REG REQ rejected - Primary service flow error',  'REG REQ被拒绝(主服务流错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11340, 10030, 3,  'REG REQ rejected - temporary no resource',  'REG REQ被拒绝(暂时没有资源)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11341, 10030, 3,  'REG REQ rejected - Permanent administrative',  'REG REQ被拒绝(永久性管理)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11342, 10030, 3,  'REG REQ rejected - Required parameter not present',  'REG REQ被拒绝(所需参数不存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11343, 10030, 3,  'REG REQ rejected - Header suppression setting not supported',  'REG REQ被拒绝(不支持头抑制设置x)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11344, 10030, 3,  'REG REQ rejected - Multiple errors',  'REG REQ被拒绝(多种错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11345, 10030, 3,  'REG REQ rejected - duplicate reference-ID or index in message',  'REG REQ被拒绝(重复的reference-ID或索引)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11346, 10030, 3,  'REG REQ rejected - parameter invalid for context',  'REG REQ被拒绝(在上下文中参数无效)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11347, 10030, 3,  'REG REQ rejected - Authorization failure',  'REG REQ被拒绝(授权失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11348, 10030, 3,  'REG REQ rejected - Message too big',  'REG REQ被拒绝(消息量太大)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11349, 10030, 3,  'REG aborted no REG - ACK',  'REG被取消(没有REG-ACK)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11350, 10030, 3,  'REG ACK rejected unspecified reason',  'REG ACK被拒绝(不明原因)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11351, 10030, 3,  'REG ACK rejected message syntax error',  'REG ACK拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11352, 10030, 3,  'T9 Timeout - Never received REG-REQ or all REG-REQ-MP fragments',  'T9超时(从未收到REG-REQ或者所有的REG-REQ-MP片段)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11353, 10030, 4,  'Missing RCP in REG-REQ or REG-REQ-MP',  '在REG-REQ或REG-REQ-MP中丢失RCP',  '',  0,  0,  '0',  '1',  '',  ''),
--(11354, 10030, 2,  'Received Non-Queue-Depth Based Bandwidth Request(MTC mode)',  '收到基于带宽请求的Non-Queue-Depth，并且多传输通道模式可用',  '',  0,  0,  '0',  '1',  '',  ''),
--(11355, 10030, 2,  'Received Queue-Depth Based Bandwidth Request(disabled MTC mode)',  '多传输通道模式不可用时收到基于带宽请求的Queue-Depth',  '',  0,  0,  '0',  '1',  '',  ''),
--(11356, 10030, 2,  'Received REG-ACK with TCS - Partial Service',  '收到有TCS的REG-ACK(不公平的服务)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11357, 10030, 2,  'Received REG-ACK with RCS - Partial Service',  '收到有RCS的REG-ACK(不公平的服务)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11358, 10030, 3,  'T6 Timer expires and Retries Exceeded',  'T6计时器终止且重试超限',  '',  0,  0,  '0',  '1',  '',  ''),
--(11359, 10030, 3,  'Initializing Channel Timeout',  '初始化通道超时',  '',  0,  0,  '0',  '1',  '',  ''),
--(11360, 10030, 3,  'REG-REQ-MP received when no MDD present',  '不存在MDD时收到REG-REQ-MP',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
     --   (10040, 10000, 0, 'QoS', 'QoS',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
--(11500, 10040, 2,  'Attribute Masks for SFID do not satisfy those in the SCN',  'SFID关联的属性不符合Service CLASS name',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
 --       (10050, 10000, 0, 'Ranging', '测距',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
--(11700, 10050, 3,  'No Ranging Requests received from POLLED CM',  '未从被轮询的CM处获得测距请求',  '',  0,  0,  '0',  '1',  '',  ''),
--(11701, 10050, 3,  'Retries exhausted for polled CM (report MAC address)',  '轮询CM重试耗尽',  '',  0,  0,  '0',  '1',  '',  ''),
--(11702, 10050, 3,  'Unable to Successfully Range CM Retries Exhausted',  '不能成功的测距重试耗尽',  '',  0,  0,  '0',  '1',  '',  ''),
--(11800, 10050, 3,  'Failed to receive Periodic RNG-REQ from modem',  '从modem上接受周期性的RNG-REQ失败',  '',  0,  0,  '0',  '1',  '',  ''),
--(11801, 10050, 1,  'CM transmitted B-INIT-RNG-REQ with MD-DS-SG ID of zero',  'CM发送MD-DS-SG ID为0的B-INIT-RNG-REQ',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
       -- (10060, 10000, 0, 'Dynamic Services', '动态服务',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
--(11900, 10060, 3,  'Service Add rejected - Unspecified reason',  '服务增加被拒绝(不明原因)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11901, 10060, 3,  'Service Add rejected - Unrecognized configuration setting',  '服务增加被拒绝(不识别的配置设置)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11902, 10060, 3,  'Service Add rejected - Classifier not found',  '服务增加被拒绝(未找到分类器)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11903, 10060, 3,  'Service Add rejected - Classifier exists',  '服务增加被拒绝(分类器存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11904, 10060, 3,  'Service Add rejected - PHS rule exists',  '服务增加被拒绝(PHS规则存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11905, 10060, 3,  'Service Add rejected - Duplicated reference-ID or index',  '服务增加被拒绝(重复的reference-ID或索引)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11906, 10060, 3,  'Service Add rejected - Multiple upstream flows',  '服务增加被拒绝(多个上行流)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11907, 10060, 3,  'Service Add rejected - Multiple downstream flows',  '服务增加被拒绝(多个下行流)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11908, 10060, 3,  'Service Add rejected - Classifier for another flow',  '服务增加被拒绝(分类器为其它流使用)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11909, 10060, 3,  'Service Add rejected - PHS rule for another flow',  '服务增加被拒绝(PHS规则为其它流使用)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11910, 10060, 3,  'Service Add rejected - Parameter invalid for context',  '服务增加被拒绝(参数无效)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11911, 10060, 3,  'Service Add rejected - Temporary no resource',  '服务增加被拒绝(暂时没有资源)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11912, 10060, 3,  'Service Add rejected - Authorization failure',  '服务增加被拒绝(授权失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11913, 10060, 3,  'Service Add rejected - Major service flow error',  '服务增加被拒绝(主要的服务流错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11914, 10060, 3,  'Service Add rejected - Major classifier error',  '服务增加被拒绝(主要的分类器错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11915, 10060, 3,  'Service Add rejected - Major PHS rule error',  '服务增加被拒绝(主要的PHS规则错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11916, 10060, 3,  'Service Add rejected - Multiple major errors',  '服务增加被拒绝(多种重大错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11917, 10060, 3,  'Service Add rejected - Message syntax error',  '服务增加被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11918, 10060, 3,  'Service Add rejected - Message too big',  '服务增加被拒绝(消息量太大)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11919, 10060, 3,  'Service Add rejected - Temporary DCC',  '服务增加被拒绝(临时的DCC)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11920, 10060, 3,  'Service Add rejected - Permanent administrative',  '服务增加被拒绝(永久性管理)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11921, 10060, 3,  'Service Add rejected - Required parameter not present',  '服务增加被拒绝(所需参数不存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11922, 10060, 3,  'Service Add rejected - Header suppression setting not supported',  '服务增加被拒绝(不支持头抑制设置)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11923, 10060, 3,  'Service Add rejected - Service flow exists',  '服务增加被拒绝(服务流存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11924, 10060, 3,  'Service Add rejected - HMAC Auth failure',  '服务增加被拒绝(HMAC认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11925, 10060, 3,  'Service Add rejected - Add aborted',  '服务增加被拒绝(增加被取消)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11926, 10060, 3,  'Service Add rejected - Multiple errors',  '服务增加被拒绝(多种错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11927, 10060, 3,  'Service Change rejected - Unspecified reason',  '服务改变被拒绝(不明原因)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11928, 10060, 3,  'Service Change rejected - Unrecognized configuration setting',  '服务改变被拒绝(不识别的配置设置)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11929, 10060, 3,  'Service Change rejected - Classifier not found',  '服务改变被拒绝(分类器未找到)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11930, 10060, 3,  'Service Change rejected - Classifier exists',  '服务改变被拒绝(分类器存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11931, 10060, 3,  'Service Change rejected - PHS rule not found',  '服务改变被拒绝(PHS规则未找到)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11932, 10060, 3,  'Service Change rejected - PHS rule exists',  '服务改变被拒绝(PHS规则存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11933, 10060, 3,  'Service Change rejected - Duplicated reference-ID or index',  '服务改变被拒绝(重复的reference-ID或索引)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11934, 10060, 3,  'Service Change rejected - Multiple upstream flows',  '服务改变被拒绝(多个上行流)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11935, 10060, 3,  'Service Change rejected - Multiple downstream flows',  '服务改变被拒绝(多个下行流)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11936, 10060, 3,  'Service Change rejected - Classifier for another flow',  '服务改变被拒绝(分类器为其它流使用)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11937, 10060, 3,  'Service Change rejected - PHS rule for another flow',  '服务改变被拒绝(PHS规则为其它流使用)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11938, 10060, 3,  'Service Change rejected - Invalid parameter for context',  '服务改变被拒绝(无效的参数)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11939, 10060, 3,  'Service Change rejected - Temporary no resource',  '服务改变被拒绝(暂时没有资源)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11940, 10060, 3,  'Service Change rejected - Authorization failure',  '服务改变被拒绝(授权失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11941, 10060, 3,  'Service Change rejected - Major service flow error',  '服务改变被拒绝(主要的服务流错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11942, 10060, 3,  'Service Change rejected - Major classifier error',  '服务改变被拒绝(主要的分类器错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11943, 10060, 3,  'Service Change rejected - Major PHS error',  '服务改变被拒绝(主要的PHS规则错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11944, 10060, 3,  'Service Change rejected - Multiple major errors',  '服务改变被拒绝(多种重大错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11945, 10060, 3,  'Service Change rejected - Message syntax error',  '服务改变被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11946, 10060, 3,  'Service Change rejected - Message too big',  '服务改变被拒绝(消息量太大)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11947, 10060, 3,  'Service Change rejected - Temporary DCC',  '服务改变被拒绝(临时的DCC)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11948, 10060, 3,  'Service Change rejected - Permanent administrative',  '服务改变被拒绝(永久性管理)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11949, 10060, 3,  'Service Change rejected - Requester not owner of service flow',  '服务改变被拒绝(请求者不是服务流的拥有者)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11950, 10060, 3,  'Service Change rejected - Service flow not found',  '服务改变被拒绝(服务流未找到)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11951, 10060, 3,  'Service Change rejected - Required parameter not present',  '服务改变被拒绝(所需参数不存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11952, 10060, 3,  'Service Change rejected-Header suppression setting not supported',  '服务改变被拒绝(不支持头抑制设置)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11953, 10060, 3,  'Service Change rejected - HMAC Auth failure',  '服务改变被拒绝(HMAC认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11954, 10060, 3,  'Service Change rejected - Multiple errors',  '服务改变被拒绝(多种错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11955, 10060, 3,  'Service Delete rejected - Unspecified reason',  '服务删除被拒绝(不明原因)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11956, 10060, 3,  'Service Delete rejected - Requester not owner of service flow',  '服务删除被拒绝(请求者不是服务流的拥有者)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11957, 10060, 3,  'Service Delete rejected - Service flow not found',  '服务删除被拒绝(服务流未找到)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11958, 10060, 3,  'Service Delete rejected - HMAC Auth failure',  '服务删除被拒绝(HMAC认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11959, 10060, 3,  'Service Delete rejected - Message syntax error',  '服务删除被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11960, 10060, 3,  'Service Add Response rejected - Invalid transaction ID(DSR)',  '服务增加应答被拒绝(无效的传输ID)(动态服务响应)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11961, 10060, 3,  'Service Add aborted - No RSP',  '服务增加应答被拒绝(没有RSP)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11962, 10060, 3,  'Service Add Response rejected - PHS rule exists',  '服务增加应答被拒绝(PHS规则存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11963, 10060, 3,  'Service Add Response rejected - Duplicate reference_ID or index',  '服务增加应答被拒绝(重复的reference-ID或索引)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11964, 10060, 3,  'Service Add Response rejected - Classifier for another flow',  '服务增加应答被拒绝(分类器为其它流使用)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11965, 10060, 3,  'Service Add Response rejected - Parameter invalid for context',  '服务增加应答被拒绝(无效的参数)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11966, 10060, 3,  'Service Add Response rejected - Major service flow error',  '服务增加应答被拒绝(主要的服务流错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11967, 10060, 3,  'Service Add Response rejected - Major classifier error',  '服务增加应答被拒绝(主要的分类器错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11968, 10060, 3,  'Service Add Response rejected - Major PHS Rule error',  '服务增加应答被拒绝(主要的PHS规则错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11969, 10060, 3,  'Service Add Response rejected - Multiple major errors',  '服务增加应答被拒绝(多种重大错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11970, 10060, 3,  'Service Add Response rejected - Message too big',  '服务增加应答被拒绝(消息量太大)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11971, 10060, 3,  'Service Add Response rejected - HMAC Auth failure',  '服务增加应答被拒绝(HMAC认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11972, 10060, 3,  'Service Add Response rejected - Message syntax error',  '服务增加应答被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11973, 10060, 3,  'Service Add Response rejected - Unspecified reason',  '服务增加应答被拒绝(不明原因)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11974, 10060, 3,  'Service Add Response rejected-Unrecognized configuration setting',  '服务增加应答被拒绝(不识别的配置设置)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11975, 10060, 3,  'Service Add Response rejected - Required parameter not present',  '服务增加应答被拒绝(所需参数不存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11976, 10060, 3,  'Service Add Response rejected - Service Flow exists',  '服务增加应答被拒绝(服务流存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11977, 10060, 3,  'Service Add Response rejected - Multiple errors',  '服务增加应答被拒绝(多种错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11978, 10060, 3,  'Service Add Response rejected - Classifier exists',  '服务增加应答被拒绝(不识别的配置设置)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11979, 10060, 3,  'Service Change Response rejected - Invalid transaction ID',  '服务增加应答被拒绝(无效的传输ID)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11980, 10060, 3,  'Service Change aborted - No RSP',  '服务增加应答被取消(没有RSP)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11981, 10060, 3,  'Service Change Response rejected-Duplicated',  '服务改变应答被拒绝(重复的reference-ID或索引)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11982, 10060, 3,  'Service Change Response rejected - Invalid parameter for context',  '服务改变应答被拒绝(无效参数)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11983, 10060, 3,  'Service Change Response rejected - Major classifier error',  '服务改变应答被拒绝(主要的分类器错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11984, 10060, 3,  'Service Change Response rejected - Major PHS rule error',  '服务改变应答被拒绝(主要的PHS规则错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11985, 10060, 3,  'Service Change Response rejected - Multiple Major errors',  '服务改变应答被拒绝(多种重大错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11986, 10060, 3,  'Service Change Response rejected - Message too big',  '服务改变应答被拒绝(消息量太大)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11987, 10060, 3,  'Service Change Response rejected - HMAC Auth failure',  '服务改变应答被拒绝(HMAC认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11988, 10060, 3,  'Service Change Response rejected - Message syntax error',  '服务改变应答被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11989, 10060, 3,  'Service Change Response rejected - Unspecified reason',  '服务改变应答被拒绝(不明原因)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11990, 10060, 3,  'Service Change Response rejected - Unrecognized configuration',  '服务改变应答被拒绝(不识别的配置设置)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11991, 10060, 3,  'Service Change Response rejected - Not required parameter',  '服务改变应答被拒绝(所需参数不存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11992, 10060, 3,  'Service Change Response rejected - Multiple errors',  '服务改变应答被拒绝(多种错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11993, 10060, 3,  'Service Change Response rejected - Classifier exists',  '服务改变应答被拒绝(分类器存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11994, 10060, 3,  'Service Change Response rejected - PHS rule exists',  '服务改变应答被拒绝(PHS规则存在)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11995, 10060, 3,  'Service Delete Response rejected - Invalid transaction ID',  '服务删除应答被拒绝(无效传输ID)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11996, 10060, 3,  'Service Add Response rejected - Invalid Transaction ID(DSA)',  '服务增加应答被拒绝(无效传输ID)(动态服务确认)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11997, 10060, 3,  'Service Add Aborted - No ACK',  '服务增加被取消(没有确认)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11998, 10060, 3,  'Service Add ACK rejected - HMAC auth failure',  '服务增加确认被拒绝(HMAC认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11999, 10060, 3,  'Service Add ACK rejected - Message syntax error',  '服务增加确认被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  ''),
--(12000, 10060, 3,  'Service Change ACK rejected - Invalid transaction ID',  '服务改变确认被拒绝(无效传输ID)',  '',  0,  0,  '0',  '1',  '',  ''),
--(12001, 10060, 3,  'Service Change Aborted - No ACK',  '服务改变被取消(没有确认)',  '',  0,  0,  '0',  '1',  '',  ''),
--(12002, 10060, 3,  'Service Change ACK rejected - HMAC Auth failure',  '服务改变确认被拒绝(HAMC认证失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(12003, 10060, 3,  'Service Change ACK rejected - Message syntax error',  '服务改变确认被拒绝(消息语法错误)',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
      --  (10070, 10000, 0, 'Diagnostic Log', '诊断日志',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
--(12100, 10070, 3, 'Diagnostic log size reached high threshold',  '日志大小达到设定上限',  '',  0,  0,  '0',  '1',  '',  ''),
--(12101, 10070, 2, 'Diagnostic log size dropped to low threshold',  '日志大小低于设定下限',  '',  0,  0,  '0',  '1',  '',  ''),
--(12102, 10070, 3, 'Diagnostic log size reached full threshold',  '日志已满',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
 --       (10080, 10000, 0, 'IPDR', '因特网数据记录',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
--(12200, 10080, 2,  'IPDR Connection Terminated',  'IPDR连接中断',  '',  0,  0,  '0',  '1',  '',  ''),
--(12201, 10080, 3,  'IPDR Collector Failover Error: Backup Collector IP',  'IPDR接收端故障转移出错(备份接收端IP)',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
  --      (10090, 10000, 0, 'Multicast', '组播',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
--(12300, 10090, 3,  'Aggregate Session Limit defined exceeded by join',  '会话总数超出限制',  '',  0,  0,  '0',  '1',  '',  ''),
--(12301, 10090, 2,  'Multicast session S, G of the join Y not authorized',  '加入Y的组播会话S, G未授权',  '',  0,  0,  '0',  '1',  '',  ''),
--(12302, 10090, 1,  'Multicast Profile Profile Name created',  '组播产生的Profile Name',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
   --     (10100, 10000, 0, 'CM-STATUS', 'CM-STATUS',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
--(11400, 10100, 2,  'CM-STATUS received prior to REG-ACK',  'REG-ACK之前收到CM-STATUS消息',  '',  0,  0,  '0',  '1',  '',  ''),
--(11401, 10100, 2,  'CM-STATUS received while enable bit cleared',  'enable位被清除时收到CM-STATUS消息',  '',  0,  0,  '0',  '1',  '',  ''),
--(11402, 10100, 2,  'CM-STATUS received - secondary channel MDD timeout',  '收到CM-STATUS消息(次要通道MDD超时)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11403, 10100, 2,  'CM-STATUS received - QAM/FEC lock failure',  '收到CM-STATUS消息(QAM/FEC阻塞失败)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11404, 10100, 2,  'CM-STATUS received - sequence out-of-range',  '收到CM-STATUS消息(序列越界)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11405, 10100, 2,  'CM-STATUS received - MDD recovery',  '收到CM-STATUS消息(MDD恢复)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11406, 10100, 2,  'CM-STATUS received - QAM/FEC recovery',  '收到CM-STATUS消息(QAM/FEC恢复)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11407, 10100, 2,  'CM-STATUS received - T4 timeout',  '收到CM-STATUS消息(T4超时)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11408, 10100, 2,  'CM-STATUS received - T3 retries exceeded',  '收到CM-STATUS消息(T3重试超限)',  '',  0,  0,  '0',  '1',  '',  '');

--INSERT INTO AlertType(typeId, category, levelId, name, displayName, alertTimes, smartUpdate, terminate, threshold, active, oid, note)VALUES
--(11600, 10100, 1,  'CM-CTRL - Command: mute,  or cmReinit,  or forwarding',  'CM-CTRL(命令:mute,  cmReinit,  或者forwarding)',  '',  0,  0,  '0',  '1',  '',  ''),
--(11601, 10100, 1,  'CM-CTRL - Invalid message format',  'CM-CTRL消息格式无效',  '',  0,  0,  '0',  '1',  '',  '');

INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
                (11180, 11181, 0),
                (11181, 11181, 1);
--INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
--(11100, 11100, 1),
--(11101, 11101, 1),
--(11102, 11102, 1),
--(11103, 11103, 1),
--(11104, 11104, 1),
--(11105, 11105, 1),
--(11106, 11106, 1),
--(11107, 11107, 1),
--(11108, 11108, 1),
--(11109, 11109, 1),
--(11110, 11110, 1),
--(11111, 11111, 1),
--(11112, 11112, 1),
--(11113, 11113, 1),
--(11114, 11114, 1),
--(11115, 11115, 1),
--(11116, 11116, 1),
--(11117, 11117, 1),
--(11118, 11118, 1),
--(11119, 11119, 1),
--(11120, 11120, 1),
--(11121, 11121, 1),
--(11122, 11122, 1),
--(11123, 11123, 1),
--(11124, 11124, 1),
--(11125, 11125, 1),
--(11126, 11126, 1),
--(11200, 11200, 1),
--(11201, 11201, 1),
--(11202, 11202, 1),
--(11203, 11203, 1),
--(11204, 11204, 1),
--(11205, 11205, 1),
--(11206, 11206, 1),
--(11207, 11207, 1),
--(11208, 11208, 1),
--(11209, 11209, 1),
--(11210, 11210, 1),
--(11211, 11211, 1),
--(11212, 11212, 1),
--(11213, 11213, 1),
--(11214, 11214, 1),
--(11215, 11215, 1),
--(11216, 11216, 1),
--(11217, 11217, 1),
--(11218, 11218, 1),
--(11219, 11219, 1),
--(11220, 11220, 1),
--(11221, 11221, 1),
--(11222, 11222, 1),
--(11223, 11223, 1),
--(11224, 11224, 1),
--(11225, 11225, 1),
--(11226, 11226, 1),
--(11227, 11227, 1),
--(11228, 11228, 1),
--(11229, 11229, 1),
--(11230, 11230, 1),
--(11231, 11231, 1),
--(11232, 11232, 1),
--(11233, 11233, 1),
--(11234, 11234, 1),
--(11235, 11235, 1),
--(11300, 11300, 1),
--(11301, 11301, 1),
--(11302, 11302, 1),
--(11303, 11303, 1),
--(11304, 11304, 1),
--(11305, 11305, 1),
--(11306, 11306, 1),
--(11307, 11307, 1),
--(11308, 11308, 1),
--(11309, 11309, 1),
--(11310, 11310, 1),
--(11311, 11311, 1),
--(11312, 11312, 1),
--(11313, 11313, 1),
--(11314, 11314, 1),
--(11315, 11315, 1),
--(11316, 11316, 1),
--(11317, 11317, 1),
--(11318, 11318, 1),
--(11319, 11319, 1),
--(11320, 11320, 1),
--(11321, 11321, 1),
--(11322, 11322, 1),
--(11323, 11323, 1),
--(11324, 11324, 1),
--(11325, 11325, 1),
--(11326, 11326, 1),
--(11327, 11327, 1),
--(11328, 11328, 1),
--(11329, 11329, 1),
--(11330, 11330, 1),
--(11331, 11331, 1),
--(11332, 11332, 1),
--(11333, 11333, 1),
--(11334, 11334, 1),
--(11335, 11335, 1),
--(11336, 11336, 1),
--(11337, 11337, 1),
--(11338, 11338, 1),
--(11339, 11339, 1),
--(11340, 11340, 1),
--(11341, 11341, 1),
--(11342, 11342, 1),
--(11343, 11343, 1),
--(11344, 11344, 1),
--(11345, 11345, 1),
--(11346, 11346, 1),
--(11347, 11347, 1),
--(11348, 11348, 1),
--(11349, 11349, 1),
--(11350, 11350, 1),
--(11351, 11351, 1),
--(11352, 11352, 1),
--(11353, 11353, 1),
--(11354, 11354, 1),
--(11355, 11355, 1),
--(11356, 11356, 1),
--(11357, 11357, 1),
--(11358, 11358, 1),
--(11359, 11359, 1),
--(11360, 11360, 1),
--(11500, 11500, 1),
--(11700, 11700, 1),
--(11701, 11701, 1),
--(11702, 11702, 1),
--(11800, 11800, 1),
--(11801, 11801, 1),
--(11900, 11900, 1),
--(11901, 11901, 1),
--(11902, 11902, 1),
--(11903, 11903, 1),
--(11904, 11904, 1),
--(11905, 11905, 1),
--(11906, 11906, 1),
--(11907, 11907, 1),
--(11908, 11908, 1),
--(11909, 11909, 1),
--(11910, 11910, 1),
--(11911, 11911, 1),
--(11912, 11912, 1),
--(11913, 11913, 1),
--(11914, 11914, 1),
--(11915, 11915, 1),
--(11916, 11916, 1),
--(11917, 11917, 1),
--(11918, 11918, 1),
--(11919, 11919, 1),
--(11920, 11920, 1),
--(11921, 11921, 1),
--(11922, 11922, 1),
--(11923, 11923, 1),
--(11924, 11924, 1),
--(11925, 11925, 1),
--(11926, 11926, 1),
--(11927, 11927, 1),
--(11928, 11928, 1),
--(11929, 11929, 1),
--(11930, 11930, 1),
--(11931, 11931, 1),
--(11932, 11932, 1),
--(11933, 11933, 1),
--(11934, 11934, 1),
--(11935, 11935, 1),
--(11936, 11936, 1),
--(11937, 11937, 1),
--(11938, 11938, 1),
--(11939, 11939, 1),
--(11940, 11940, 1),
--(11941, 11941, 1),
--(11942, 11942, 1),
--(11943, 11943, 1),
--(11944, 11944, 1),
--(11945, 11945, 1),
--(11946, 11946, 1),
--(11947, 11947, 1),
--(11948, 11948, 1),
--(11949, 11949, 1),
--(11950, 11950, 1),
--(11951, 11951, 1),
--(11952, 11952, 1),
--(11953, 11953, 1),
--(11954, 11954, 1),
--(11955, 11955, 1),
--(11956, 11956, 1),
--(11957, 11957, 1),
--(11958, 11958, 1),
--(11959, 11959, 1),
--(11960, 11960, 1),
--(11961, 11961, 1),
--(11962, 11962, 1),
--(11963, 11963, 1),
--(11964, 11964, 1),
--(11965, 11965, 1),
--(11966, 11966, 1),
--(11967, 11967, 1),
--(11968, 11968, 1),
--(11969, 11969, 1),
--(11970, 11970, 1),
--(11971, 11971, 1),
--(11972, 11972, 1),
--(11973, 11973, 1),
--(11974, 11974, 1),
--(11975, 11975, 1),
--(11976, 11976, 1),
--(11977, 11977, 1),
--(11978, 11978, 1),
--(11979, 11979, 1),
--(11980, 11980, 1),
--(11981, 11981, 1),
--(11982, 11982, 1),
--(11983, 11983, 1),
--(11984, 11984, 1),
--(11985, 11985, 1),
--(11986, 11986, 1),
--(11987, 11987, 1),
--(11988, 11988, 1),
--(11989, 11989, 1),
--(11990, 11990, 1),
--(11991, 11991, 1),
--(11992, 11992, 1),
--(11993, 11993, 1),
--(11994, 11994, 1),
--(11995, 11995, 1),
--(11996, 11996, 1),
--(11997, 11997, 1),
--(11998, 11998, 1),
--(11999, 11999, 1),
--(12000, 12000, 1),
--(12001, 12001, 1),
--(12002, 12002, 1),
--(12003, 12003, 1),
--(12100, 12100, 1),
--(12101, 12101, 1),
--(12102, 12102, 1),
--(12200, 12200, 1),
--(12201, 12201, 1),
--(12300, 12300, 1),
--(12301, 12301, 1),
--(12302, 12302, 1),
--(11400, 11400, 1),
--(11401, 11401, 1),
--(11402, 11402, 1),
--(11403, 11403, 1),
--(11404, 11404, 1),
--(11405, 11405, 1),
--(11406, 11406, 1),
--(11407, 11407, 1),
--(11408, 11408, 1),
--(11600, 11600, 1),
--(11601, 11601, 1);

--begin:增加CC掉线告警，将typeId设为11179
--@Modify by Rod
--INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
--    (111179, -8, 'CCMTS Event', 'EVENT.ccmtslose', '');
--INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
--    (111179, -8, 'ALERT.ccAlert','ALERT.ccmtslose',5,'',0,  0,  '0',  '1',  '',  '');
--INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
--    (111179,111179,1);
--INSERT INTO CmcEventTypeRelation(deviceEventTypeId, emsEventTypeId) VALUES
--    (11179, 111179);
/* -- version 1.0.0, build 2012-3-16, module cmc */

/* modify by victor@2013-4-8把CC的类型ID改为30000*/
-- version 1.7.12.0,build 2013-4-8,module cmc
DELETE FROM EntityType WHERE typeId = 11001;
DELETE FROM EntityType WHERE typeId = 12000;
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30000,   'CCMTS',  'CCMTS',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1',   'network/ccmts/ccmts_16.png',  'network/ccmts/ccmts_32.png',  'network/ccmts/ccmts_48.png',  'network/ccmts/ccmts_64.png',  32285);
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30002,   'cmc8800b',  'CC8800B',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.2',   'network/ccmts/ccmts_b_16.png',  'network/ccmts/ccmts_b_32.png',  'network/ccmts/ccmts_b_48.png',  'network/ccmts/ccmts_b_64.png',  32285);
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30003,   'cmc8800c',  'CC8800C',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.3',   'network/ccmts/ccmts_c_16.png',  'network/ccmts/ccmts_c_32.png',  'network/ccmts/ccmts_c_48.png',  'network/ccmts/ccmts_c_64.png',  32285);
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30004,   'cmc8800d',  'CC8800D',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.4',   'network/ccmts/ccmts_d_16.png',  'network/ccmts/ccmts_d_32.png',  'network/ccmts/ccmts_d_48.png',  'network/ccmts/ccmts_d_64.png',  32285);
/* -- version 1.7.12.0,build 2013-4-8,module cmc */

/* Add event type by Rod@2013-4-22 增加CMC的事件类型 */
-- version 1.7.12.0,build 2013-4-22,module cmc
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (102005, -8, 'CCMTS Event', '上下行信道上线', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (102006, -8, 'CCMTS Event', '上下行信道下线', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101002, -8, 'CCMTS Event', '发现CC连接', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101003, -8, 'CCMTS Event', '丢失CC连接', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101004, -8, 'CCMTS Event', 'CM下线', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101005, -8, 'CCMTS Event', 'CM上线(range通过)', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101006, -8, 'CCMTS Event', 'CM上线(DHCP和tftp过程通过)', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101010, -8, 'CCMTS Event', '修改下行信道参数', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101011, -8, 'CCMTS Event', '关闭下行信道', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101012, -8, 'CCMTS Event', '开启下行信道', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101013, -8, 'CCMTS Event', '关闭上行信道', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101014, -8, 'CCMTS Event', '开启上行信道', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101015, -8, 'CCMTS Event', '修改上行信道参数', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101016, -8, 'CCMTS Event', '设置上行信道电平', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101017, -8, 'CCMTS Event', 'CC配置异常(失败或告警)', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101019, -8, 'CCMTS Event', '主线DB同步失败(线卡到主控)', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101020, -8, 'CCMTS Event', '主线DB同步失败(主控到线卡)', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101022, -8, 'CCMTS Event', '主线DB同步成功(主控到线卡)', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101023, -8, 'CCMTS Event', '线卡上线', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101024, -8, 'CCMTS Event', '线卡下线', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101025, -8, 'CCMTS Event', '重启CC', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101026, -8, 'CCMTS Event', '设置下行信道为IPQAM模式', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101027, -8, 'CCMTS Event', 'CCMTS主动上报告警状态', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (101028, -8, 'CCMTS Event', 'CCMTS主动清除告警状态', '');
INSERT INTO EventType(typeId, parentId, name, displayName, note) VALUES
    (104008, -8, 'CCMTS Event', 'DHCP Relay错误', '');
INSERT INTO CmcEventTypeRelation(deviceEventTypeId, emsEventTypeId) VALUES
        (2005, 102005),
        (2006, 102006),
        (1002, 101002),
        (1003, 101003),
        (1004, 101004),
        (1005, 101005),
        (1006, 101006),
        (1010, 101010),
        (1011, 101011),
        (1012, 101012),
        (1013, 101013),
        (1014, 101014),
        (1015, 101015),
        (1016, 101016),
        (1017, 101017),
        (1019, 101019),
        (1020, 101020),
        (1022, 101022),
        (1023, 101023),
        (1024, 101024),
        (1025, 101025),
        (1026, 101026),
        (1027, 101027),
        (1028, 101028),
        (4008, 104008);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (102006, -8, 'ALERT.ccAlert','上下行信道下线',2,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (102006,102006,1),
    (102005,102006,0);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101003, -8, 'ALERT.ccAlert','丢失CC连接',2,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101003,101003,1),
    (101002,101003,0);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101004, -8, 'ALERT.ccAlert','CM下线',2,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101004,101004,1);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101011, -8, 'ALERT.ccAlert','关闭下行信道',2,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101011,101011,1),
    (101012,101011,0);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101013, -8, 'ALERT.ccAlert','关闭上行信道',2,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101013,101013,1),
    (101014,101013,0);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101017, -8, 'ALERT.ccAlert','CC配置异常(失败或告警)',5,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101017,101017,1);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101019, -8, 'ALERT.ccAlert','主线DB同步失败(线卡到主控)',5,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101019,101019,1);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101020, -8, 'ALERT.ccAlert','主线DB同步失败(主控到线卡)',5,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101020,101020,1),
    (101022,101020,0);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101024, -8, 'ALERT.ccAlert','线卡下线',2,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101024,101024,1),
    (101023,101024,0);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101025, -8, 'ALERT.ccAlert','重启CC',2,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101025,101025,1);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101027, -8, 'ALERT.ccAlert','CCMTS主动上报告警状态',6,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101027,101027,1);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (101028, -8, 'ALERT.ccAlert','CCMTS主动清除告警状态',6,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (101028,101028,1);
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (104008, -8, 'ALERT.ccAlert','DHCP Relay错误',6,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId, alertTypeId, type) VALUES
    (104008,104008,1);
/* -- version 1.7.12.0,build 2013-4-22,module cmc */

/* modify by victor@20130618合并时修改版本从1.7.13.3改为1.7.14.0，保持连续。修改module epon为cmc*/
-- version 1.7.14.0,build 2013-6-7,module cmc
DELETE from ReportTemplate where templateId like '8%';
/* -- version 1.7.14.0,build 2013-6-7,module cmc */

-- version 1.7.14.0,build 2013-6-17,module cmc
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
        (1016393,  10000, 'E_RT_ALM_CCMTS_PWR_DOWN', 'DB.eventType.e1016393');
INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
        (1016393,  10000, 'E_RT_EVE_CCMTS_PWR_DOWN', 'DB.eventType.e1016393', 5, '', 0, 0, '0', '1', '', '');
INSERT INTO CmcEventTypeRelation(deviceEventTypeId,emsEventTypeId) VALUES (1016393,1016393);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (1016393,1016393,1);
/* -- version 1.7.14.0,build 2013-6-17,module cmc */

-- version 1.7.14.0,build 2013-06-17,module cmc
INSERT INTO perfthresholdtemplate(templateId,templateName, templateType, createUser,isDefaultTemplate, createTime)
                values(2,'CCMTS_DEFAULT_TEMPLATE', 30000,'system', 1, sysdate());
INSERT INTO perfglobal(flag,isPerfOn,isRelationWithDefaultTemplate,defaultTemplateId,isPerfThreshold,defaultCollectTime) values ('CC', 1,1,2,1, 15);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_NOISE', 30000, 'Performance.ccNoise','Performance.signalQuality', 'dB');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_UTILIZATION', 30000, 'Performance.ccUsed', 'Performance.speed', '%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_SPEED', 30000, 'Performance.ccSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_ERRORCODE', 30000, 'Performance.ccErrorCode','Performance.signalQuality','');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_UNERRORCODE', 30000, 'Performance.ccUnErrorCode','Performance.signalQuality','');

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_CPUUSED', 30000, 'Performance.cpuUsed','Performance.deviceServiceQuality', '%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_FLASHUSED', 30000, 'Performance.flashUsed','Performance.deviceServiceQuality', '%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_MEMUSED', 30000, 'Performance.memUsed','Performance.deviceServiceQuality', '%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_UPLINK_INSPEED', 30000, 'Performance.upLinkInSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_UPLINK_OUTSPEED', 30000, 'Performance.upLinkOutSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_UPLINK_UESD', 30000, 'Performance.upLinkUsed', 'Performance.speed', '%');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_MAC_INSPEED', 30000, 'Performance.macInSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_MAC_OUTSPEED', 30000, 'Performance.macOutSpeed', 'Performance.speed','Mbps');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_MAC_USED', 30000, 'Performance.macUsed', 'Performance.speed', '%');

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_PON_RE_POWER', 30000, 'Performance.optRePower','Performance.optLink','dBm');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_PON_RT_POWER', 30000, 'Performance.optRtPower','Performance.optLink','dBm');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_PON_TEMP', 30000, 'Performance.optTemp','Performance.optLink','℃');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_PON_CURRENT',30000, 'Performance.optCurrent','Performance.optLink','mA');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_PON_VOLTAGE',30000, 'Performance.optVoltage','Performance.optLink','V');

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_UP_MODULE_TEMP', 30000, 'Performance.upTemp','Performance.moduleTemp','℃');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_RF_MODULE_TEMP', 30000, 'Performance.rfTemp','Performance.moduleTemp','℃');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_BCM_MODULE_TEMP', 30000, 'Performance.bcmTemp','Performance.moduleTemp','℃');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_POWER_MODULE_TEMP',30000, 'Performance.powerTemp','Performance.moduleTemp','℃');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_MEM_MODULE_TEMP',30000, 'Performance.memTemp','Performance.moduleTemp','℃');

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_UNUSUAL_CMNUMBER',30000, 'Performance.cmNumber','Performance.deviceServiceQuality','');

INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('CC_NOISE',2,'1_45_5',1,1,1,'00:00-23:59#1234567'),
	('CC_UTILIZATION',2,'1_80_4',1,1,1,'00:00-23:59#1234567'),
	('CC_SPEED', 2,'1_50_5',1,1,1,'00:00-23:59#1234567'),
	('CC_ERRORCODE', 2,'1_100_4',1,1,1,'00:00-23:59#1234567'),
	('CC_UNERRORCODE', 2,'1_100_4',1,1,1,'00:00-23:59#1234567'),
	('CC_CPUUSED',2,'1_60_4',1,1,1,'00:00-23:59#1234567'),
	('CC_FLASHUSED', 2,'1_80_3',1,1,1,'0:00-23:59#1234567'),
	('CC_MEMUSED', 2,'1_80_4',1,1,1,'00:00-23:59#1234567'),
	('CC_UPLINK_INSPEED', 2,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('CC_UPLINK_OUTSPEED', 2,'1_800_4',1,1,1,'00:00-23:59#1234567'),
	('CC_UPLINK_UESD', 2,'1_80_4',1,1,1,'00:00-23:59#1234567'),
	('CC_MAC_INSPEED', 2,'1_8000_4',1,1,1,'00:00-23:59#1234567'),
	('CC_MAC_USED', 2,'1_80_4',1,1,1,'00:00-23:59#1234567'),
	('CC_MAC_OUTSPEED', 2,'1_80_4',1,1,1,'00:00-23:59#1234567'),
	('CC_PON_RE_POWER', 2,'1_40_4',1,1,1,'00:00-23:59#1234567'),
	('CC_PON_RT_POWER', 2,'1_40_4',1,1,1,'00:00-23:59#1234567'),
	('CC_PON_TEMP', 2,'1_60_5',1,1,1,'00:00-23:59#1234567'),
	('CC_PON_CURRENT', 2,'1_100_4',1,1,1,'00:00-23:59#1234567'),
	('CC_PON_VOLTAGE', 2,'1_12_5',1,1,1,'00:00-23:59#1234567'),
	
	('CC_UP_MODULE_TEMP', 2,'1_70_3',1,1,1,'00:00-23:59#1234567'),
	('CC_RF_MODULE_TEMP', 2,'1_70_3',1,1,1,'00:00-23:59#1234567'),
	('CC_BCM_MODULE_TEMP', 2,'1_70_3',1,1,1,'00:00-23:59#1234567'),
	('CC_POWER_MODULE_TEMP', 2,'1_70_3',1,1,1,'00:00-23:59#1234567'),
	('CC_MEM_MODULE_TEMP', 2,'1_70_3',1,1,1,'00:00-23:59#1234567'),
	
	('CC_UNUSUAL_CMNUMBER', 2,'1_100_3',1,1,1,'00:00-23:59#1234567');

INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-50002,-50000,'CCMTS Event','EVENT.ccmtsThreshold','');
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-800,-50002,'CCMTSNoiseEvent','DB.eventType.e800',''),
                (-801,-50002,'CCMTSNoiseEvent','DB.eventType.e801',''),
                (-802,-50002,'CCMTSUtilizationEvent','DB.eventType.e802',''),
                (-803,-50002,'CCMTSUtilizationEvent','DB.eventType.e803',''),
                (-804,-50002,'CCMTSSpeedEvent','DB.eventType.e804',''),
                (-805,-50002,'CCMTSSpeedEvent','DB.eventType.e805',''),
                (-806,-50002,'CCMTSErrorCodeEvent','DB.eventType.e806',''),
                (-807,-50002,'CCMTSErrorCodeEvent','DB.eventType.e807',''),
                (-808,-50002,'CCMTSUnErrorCodeEvent','DB.eventType.e808',''),
                (-809,-50002,'CCMTSUnErrorCodeEvent','DB.eventType.e809',''),
                
                (-810,-50002,'CCMTS_CpuUesdEvent','DB.eventType.e810',''),
                (-811,-50002,'CCMTS_CpuUesdEvent','DB.eventType.e811',''),
                (-812,-50002,'CCMTS_MemUesdEvent','DB.eventType.e812',''),
                (-813,-50002,'CCMTS_MemUesdEvent','DB.eventType.e813',''),
                (-814,-50002,'CCMTS_FlashUesdEvent','DB.eventType.e814',''),
                (-815,-50002,'CCMTS_FlashUesdEvent','DB.eventType.e815',''),
                (-816,-50002,'CCMTS_UpLink_InSpeedEvent','DB.eventType.e816',''),
                (-817,-50002,'CCMTS_UpLink_InSpeedEvent','DB.eventType.e817',''),
                (-818,-50002,'CCMTS_UpLink_OutSpeedEvent','DB.eventType.e818',''),
                (-819,-50002,'CCMTS_UpLink_OutSpeedEvent','DB.eventType.e819',''),
                (-820,-50002,'CCMTS_Uplink_UsedEvent','DB.eventType.e820',''),
                (-821,-50002,'CCMTS_Uplink_UsedEvent','DB.eventType.e821',''),
                (-822,-50002,'CCMTS_Mac_InSpeedEvent','DB.eventType.e822',''),
                (-823,-50002,'CCMTS_Mac_InSpeedEvent','DB.eventType.e823',''),
                (-824,-50002,'CCMTS_Mac_OutSpeedEvent','DB.eventType.e824',''),
                (-825,-50002,'CCMTS_Mac_OutSpeedEvent','DB.eventType.e825',''),
                (-826,-50002,'CCMTS_Mac_UsedEvent','DB.eventType.e826',''),
                (-827,-50002,'CCMTS_Mac_UsedEvent','DB.eventType.e827',''),
                
                (-828,-50002,'CCMTS_Pon_Re_PowerEvent','DB.eventType.e828',''),
                (-829,-50002,'CCMTS_Pon_Re_PowerEvent','DB.eventType.e829',''),
                (-830,-50002,'CCMTS_Pon_Rt_PowerEvent','DB.eventType.e830',''),
                (-831,-50002,'CCMTS_Pon_Rt_PowerEvent','DB.eventType.e831',''),
                (-832,-50002,'CCMTS_Pon_TempEvent','DB.eventType.e832',''),
                (-833,-50002,'CCMTS_Pon_TempEvent','DB.eventType.e833',''),
                (-834,-50002,'CCMTS_Pon_CurrentEvent','DB.eventType.e834',''),
                (-835,-50002,'CCMTS_Pon_CurrentEvent','DB.eventType.e835',''),
                (-836,-50002,'CCMTS_Pon_VoltageEvent','DB.eventType.e836',''),
                (-837,-50002,'CCMTS_Pon_VoltageEvent','DB.eventType.e837',''),
                
                (-838,-50002,'CCMTS_Up_TempEvent','DB.eventType.e838',''),
                (-839,-50002,'CCMTS_Up_TempEvent','DB.eventType.e839',''),
                (-840,-50002,'CCMTS_Rf_TempEvent','DB.eventType.e840',''),
                (-841,-50002,'CCMTS_Rf_TempEvent','DB.eventType.e841',''),
                (-842,-50002,'CCMTS_Bcm_TempEvent','DB.eventType.e842',''),
                (-843,-50002,'CCMTS_Bcm_TempEvent','DB.eventType.e843',''),
                (-844,-50002,'CCMTS_Mem_TempEvent','DB.eventType.e844',''),
                (-845,-50002,'CCMTS_Mem_TempEvent','DB.eventType.e845',''),
                (-846,-50002,'CCMTS_Power_TempEvent','DB.eventType.e846',''),
                (-847,-50002,'CCMTS_Power_TempEvent','DB.eventType.e847',''),
                (-848,-50002,'CCMTS_Cm_NumberEvent','DB.eventType.e848',''),
                (-849,-50002,'CCMTS_Cm_NumberEvent','DB.eventType.e849','');
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
              (-50002,-50000,'Threshold Alert','ALERT.ccmtsThreshold',0,'',0,0,'0','1',NULL,NULL);              
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
                (-800,-50002,'CCMTSNoiseEvent','DB.alertType.a800',5,'',0,  0,  '0',  '1',  '',  ''),
                (-802,-50002,'CCMTSUtilizationEvent','DB.alertType.a802',5,'',0,  0,  '0',  '1',  '',  ''),
                (-804,-50002,'CCMTSSpeedEvent','DB.alertType.a804',5,'',0,  0,  '0',  '1',  '',  ''),
                (-806,-50002,'CCMTSErrorCode','DB.alertType.a806',5,'',0,  0,  '0',  '1',  '',  ''),
                (-808,-50002,'CCMTSUnErrorCode','DB.alertType.a808',5,'',0,  0,  '0',  '1',  '',  ''),
                
                (-810,-50002,'CCMTS_CpuUesd','DB.alertType.a810',5,'',0,  0,  '0',  '1',  '',  ''),
                (-812,-50002,'CCMTS_MemUesd','DB.alertType.a812',5,'',0,  0,  '0',  '1',  '',  ''),
                (-814,-50002,'CCMTS_FlashUesd','DB.alertType.a814',5,'',0,  0,  '0',  '1',  '',  ''),
                (-816,-50002,'CCMTS_UpLink_InSpeed','DB.alertType.a816',5,'',0,  0,  '0',  '1',  '',  ''),
                (-818,-50002,'CCMTS_UpLink_OutSpeed','DB.alertType.a818',5,'',0,  0,  '0',  '1',  '',  ''),
                (-820,-50002,'CCMTS_Uplink_Used','DB.alertType.a820',5,'',0,  0,  '0',  '1',  '',  ''),
                (-822,-50002,'CCMTS_Mac_InSpeed','DB.alertType.a822',5,'',0,  0,  '0',  '1',  '',  ''),
                (-824,-50002,'CCMTS_Mac_OutSpeed','DB.alertType.a824',5,'',0,  0,  '0',  '1',  '',  ''),
                (-826,-50002,'CCMTS_Mac_Used','DB.alertType.a826',5,'',0,  0,  '0',  '1',  '',  ''),
                
                (-828,-50002,'CCMTS_Pon_Re_Power','DB.alertType.a828',5,'',0,  0,  '0',  '1',  '',  ''),
                (-830,-50002,'CCMTS_Pon_Rt_Power','DB.alertType.a830',5,'',0,  0,  '0',  '1',  '',  ''),
                (-832,-50002,'CCMTS_Pon_Temp','DB.alertType.a832',5,'',0,  0,  '0',  '1',  '',  ''),
                (-834,-50002,'CCMTS_Pon_Current','DB.alertType.a834',5,'',0,  0,  '0',  '1',  '',  ''),
                (-836,-50002,'CCMTS_Pon_Voltage','DB.alertType.a836',5,'',0,  0,  '0',  '1',  '',  ''),
                
                (-838,-50002,'CCMTS_Up_Temp','DB.alertType.a838',5,'',0,  0,  '0',  '1',  '',  ''),
                (-840,-50002,'CCMTS_Rf_Temp','DB.alertType.a840',5,'',0,  0,  '0',  '1',  '',  ''),
                (-842,-50002,'CCMTS_Bcm_Temp','DB.alertType.a842',5,'',0,  0,  '0',  '1',  '',  ''),
                (-844,-50002,'CCMTS_Mem_Temp','DB.alertType.a844',5,'',0,  0,  '0',  '1',  '',  ''),
                (-846,-50002,'CCMTS_Power_Temp','DB.alertType.a846',5,'',0,  0,  '0',  '1',  '',  ''),
                (-848,-50002,'CCMTS_Cm_Number','DB.alertType.a848',5,'',0,  0,  '0',  '1',  '',  '');
                
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
                (-800,-800,1),
                (-801,-800,0),
                (-802,-802,1),
                (-803,-802,0),
                (-804,-804,1),
                (-805,-804,0),
                (-806,-806,1),
                (-807,-806,0),
                (-808,-808,1),
                
                (-809,-808,0),
                (-810,-810,1),
                (-811,-810,0),
                (-812,-812,1),
                (-813,-812,0),
                (-814,-814,1),
                (-815,-814,0),
                (-816,-816,1),
                (-817,-816,0),
                (-818,-818,1),
                (-819,-818,0),
                (-820,-820,1),
                (-821,-820,0),
                (-822,-822,1),
                (-823,-822,0),
                (-824,-824,1),
                (-825,-824,0),
                (-826,-826,1),
                (-827,-826,0),
                
                
                (-828,-828,1),
                (-829,-828,0),
                (-830,-830,1),
                (-831,-830,0),
                (-832,-832,1),
                (-833,-832,0),
                (-834,-834,1),
                (-835,-834,0),
                (-836,-836,1),
                (-837,-836,0),
                
                (-838,-838,1),
                (-839,-838,0),
                (-840,-840,1),
                (-841,-840,0),
                (-842,-842,1),
                (-843,-842,0),
                (-844,-844,1),
                (-845,-844,0),
                (-846,-846,1),
                (-847,-846,0),
                (-848,-848,1),
                (-849,-849,0);
                
/* -- version 1.7.14.0,build 2013-06-17,module cmc */
-- version 1.7.16.0,build 2013-6-17,module cmc
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('cpeCollectSatus',   '1',  'cmcTerminal'),
                ('cpeCollectPeriod',   '1800000',  'cmcTerminal'),
                ('cpeNumStatisticStatus',   '1',  'cmcTerminal'),
                ('cpeActionStatisticStatus',   '1',  'cmcTerminal'),
                ('cmCollectSatus',   '0',  'cmcTerminal'),
                ('cmCollectPeriod',   '1800000',  'cmcTerminal'),
                ('cmTypeStatisticStatus',   '1',  'cmcTerminal'),
                ('cmActionStaticStatus',   '1',  'cmcTerminal'),
                ('cmStaticSource',   '0',  'cmcTerminal'),
                ('initDataSavePolicy',   '180',  'cmcTerminal'),
                ('statisticDataSavePolicy',   '365',  'cmcTerminal'),
                ('actionDataSavePolicy',   '90',  'cmcTerminal');
/* -- version 1.7.16.0,build 2013-6-1729,module cmc */

-- version 1.7.16.0,build 2013-6-29,module cmc
delete from systempreferences where name = 'cmStaticSource';
delete from systempreferences where name = 'cmCollectSatus';
delete from systempreferences where name = 'cpeCollectSatus';
delete from systempreferences where name = 'cmActionStaticStatus';
INSERT INTO systempreferences(name,  value,  module) VALUES ('cmStatisticSource',   '0',  'cmcTerminal');
INSERT INTO systempreferences(name,  value,  module) VALUES ('cmCollectStatus',   '0',  'cmcTerminal');
INSERT INTO systempreferences(name,  value,  module) VALUES ('cpeCollectStatus',   '1',  'cmcTerminal');
INSERT INTO systempreferences(name,  value,  module) VALUES ('cmActionStatisticStatus',   '1',  'cmcTerminal');
update systempreferences set value = '15552000000' where name = 'initDataSavePolicy';
update systempreferences set value = '31536000000' where name = 'statisticDataSavePolicy';
update systempreferences set value = '7776000000' where name = 'actionDataSavePolicy';
/* -- version 1.7.16.0,build 2013-6-29,module cmc */

-- version 1.7.16.0,build 2013-6-29,module cmc
delete from systempreferences where name = 'cpeCollectPeriod';
delete from systempreferences where name = 'cmCollectPeriod';

INSERT INTO systempreferences(name,  value,  module) VALUES
                ('cpeCollectPeriod',   '1800000',  'CollectTimeConfig'),
                ('cmCollectPeriod',   '1800000',  'CollectTimeConfig');
/* -- version 1.7.16.0,build 2013-6-27,module cmc */

-- version 1.7.16.0,build 2013-6-28,module cmc
--call initSnrSummary();
--call initChannelcmSummary();
/* -- version 1.7.16.0,build 2013-6-28,module cmc */

-- version 1.7.16.0,build 2013-06-29,module cmc
UPDATE EventType SET displayName = 'DB.event.e102005' WHERE typeId = 102005;
UPDATE EventType SET displayName = 'DB.event.e102006' WHERE typeId = 102006;
UPDATE EventType SET displayName = 'DB.event.e101002' WHERE typeId = 101002;
UPDATE EventType SET displayName = 'DB.event.e101003' WHERE typeId = 101003;
UPDATE EventType SET displayName = 'DB.event.e101004' WHERE typeId = 101004;
UPDATE EventType SET displayName = 'DB.event.e101005' WHERE typeId = 101005;
UPDATE EventType SET displayName = 'DB.event.e101006' WHERE typeId = 101006;
UPDATE EventType SET displayName = 'DB.event.e101010' WHERE typeId = 101010;
UPDATE EventType SET displayName = 'DB.event.e101011' WHERE typeId = 101011;
UPDATE EventType SET displayName = 'DB.event.e101012' WHERE typeId = 101012;
UPDATE EventType SET displayName = 'DB.event.e101013' WHERE typeId = 101013;
UPDATE EventType SET displayName = 'DB.event.e101014' WHERE typeId = 101014;
UPDATE EventType SET displayName = 'DB.event.e101015' WHERE typeId = 101015;
UPDATE EventType SET displayName = 'DB.event.e101016' WHERE typeId = 101016;
UPDATE EventType SET displayName = 'DB.event.e101017' WHERE typeId = 101017;
UPDATE EventType SET displayName = 'DB.event.e101019' WHERE typeId = 101019;
UPDATE EventType SET displayName = 'DB.event.e101020' WHERE typeId = 101020;
UPDATE EventType SET displayName = 'DB.event.e101022' WHERE typeId = 101022;
UPDATE EventType SET displayName = 'DB.event.e101023' WHERE typeId = 101023;
UPDATE EventType SET displayName = 'DB.event.e101024' WHERE typeId = 101024;
UPDATE EventType SET displayName = 'DB.event.e101025' WHERE typeId = 101025;
UPDATE EventType SET displayName = 'DB.event.e101026' WHERE typeId = 101026;
UPDATE EventType SET displayName = 'DB.event.e101027' WHERE typeId = 101027;
UPDATE EventType SET displayName = 'DB.event.e101028' WHERE typeId = 101028;
UPDATE EventType SET displayName = 'DB.event.e104008' WHERE typeId = 104008;
UPDATE AlertType SET displayName = 'DB.alert.a102006' WHERE typeId = 102006;
UPDATE AlertType SET displayName = 'DB.alert.a101003' WHERE typeId = 101003;
UPDATE AlertType SET displayName = 'DB.alert.a101004' WHERE typeId = 101004;
UPDATE AlertType SET displayName = 'DB.alert.a101011' WHERE typeId = 101011;
UPDATE AlertType SET displayName = 'DB.alert.a101013' WHERE typeId = 101013;
UPDATE AlertType SET displayName = 'DB.alert.a101017' WHERE typeId = 101017;
UPDATE AlertType SET displayName = 'DB.alert.a101019' WHERE typeId = 101019;
UPDATE AlertType SET displayName = 'DB.alert.a101020' WHERE typeId = 101020;
UPDATE AlertType SET displayName = 'DB.alert.a101024' WHERE typeId = 101024;
UPDATE AlertType SET displayName = 'DB.alert.a101025' WHERE typeId = 101025;
UPDATE AlertType SET displayName = 'DB.alert.a101027' WHERE typeId = 101027;
UPDATE AlertType SET displayName = 'DB.alert.a101028' WHERE typeId = 101028;
UPDATE AlertType SET displayName = 'DB.alert.a104008' WHERE typeId = 104008;
--begin: 去掉上下行信道事件
--@Modify by Rod
DELETE FROM EventType WHERE typeId = 102005;
DELETE FROM EventType WHERE typeId = 102006;
DELETE FROM CmcEventTypeRelation WHERE emsEventTypeId = 102005;
DELETE FROM CmcEventTypeRelation WHERE emsEventTypeId = 102006;
DELETE FROM AlertType WHERE typeId = 102006;
DELETE FROM Event2Alert WHERE alertTypeId = 102006;
/* -- version 1.7.16.0,build 2013-06-29,module cmc */

-- version 1.7.16.0,build 2013-6-29,module cmc
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000002,'loadBalPolicyTpl',4000000,'cmc.loadbalancetpl');
INSERT INTO RoleFunctionRela(roleId,functionId) values
        (2,4000002);
/* -- version 1.7.16.0,build 2013-6-29,module cmc */

/* Modify by Rod CC告警类型整理*/
-- version 1.7.16.0,build 2013-7-8,module cmc
DELETE FROM EventType WHERE typeId = 1016393;
DELETE FROM AlertType WHERE typeId = 1016393;
DELETE FROM CmcEventTypeRelation WHERE deviceEventTypeId = 1016393 and emsEventTypeId = 1016393;
DELETE FROM Event2Alert WHERE eventTypeId = 1016393 and alertTypeId = 1016393;
INSERT INTO EventType(typeId,parentId,name,displayName) VALUES
        (116393,  -8, 'E_RT_ALM_CCMTS_PWR_DOWN', 'DB.eventType.e1016393');
INSERT INTO AlertType(typeId, category, name, displayName, levelId, alertTimes, smartUpdate, terminate, threshold, active, oid, note) VALUES
        (116393,  -8, 'E_RT_EVE_CCMTS_PWR_DOWN', 'DB.eventType.e1016393', 5, '', 0, 0, '0', '1', '', '');
INSERT INTO CmcEventTypeRelation(deviceEventTypeId,emsEventTypeId) VALUES (116393,116393);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES (116393,116393,1);

DELETE FROM EventType WHERE typeId = 102005;
DELETE FROM EventType WHERE typeId = 102006;
DELETE FROM AlertType WHERE typeId = 102006;
DELETE FROM Event2Alert WHERE eventTypeId = 102006 and alertTypeId = 102006 and type = 1;
DELETE FROM Event2Alert WHERE eventTypeId = 102005 and alertTypeId = 102006 and type = 0;
DELETE FROM CmcEventTypeRelation WHERE deviceEventTypeId = 2005 and emsEventTypeId = 102005;
DELETE FROM CmcEventTypeRelation WHERE deviceEventTypeId = 2006 and emsEventTypeId = 102006;
/* -- version 1.7.16.0,build 2013-7-8,module cmc */

-- version 1.7.17.1,build 2013-7-20,module cmc
INSERT INTO GlobalCollectTimeTable(perftargetName,interval_,targetGroup,type,enable) VALUES
                ('upLinkFlow',15,'flow',2,1),
                ('macFlow',15,'flow',2,1),
                ('channelSpeed',15,'flow',2,1),
                ('cpuUsed',15,'service',2,1),
                ('memUsed',15,'service',2,1),
                ('flashUsed',15,'service',2,1),
                ('optLink',15,'service',2,1),
                ('snr',15,'signalQuality',2,1),
                ('ber',15,'signalQuality',2,1),
                ('moduleTemp',15,'service',2,1);
/* -- version 1.7.17.1,build 2013-7-20,module cmc */  

-- version 1.7.18.0,build 2013-8-5,module cmc
UPDATE systempreferences set value = '0' where name = 'cpeNumStatisticStatus';
UPDATE systempreferences set value = '0' where name = 'cpeActionStatisticStatus';
UPDATE systempreferences set value = '0' where name = 'cmTypeStatisticStatus';
/* -- version 1.7.18.0,build 2013-8-5,module cmc */

-- version 1.7.18.1,build 2013-8-9,module cmc
UPDATE systempreferences set value = '0' where name = 'cmActionStatisticStatus';
UPDATE systempreferences set value = '0' where name = 'cpeCollectStatus';
/* -- version 1.7.18.1,build 2013-8-9,module cmc */

-- version 1.7.18.1,build 2013-8-22,module cmc
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000003,'globalSpectrumGpMgmt',4000000,'Config.globalGpManage');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000004,'spectrumGpTempLateMgmt',4000000,'Config.gpTempManage');
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
        (4000005,'templateConfigLog',4000000,'Config.tempConfigLog');
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,4000003);
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,4000004);
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,4000005);
/* -- version 1.7.18.1,build 2013-8-22,module cmc */

/* Modify by Rod C-A C-B整理*/
-- version 1.7.18.1,build 2013-9-9,module cmc
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30006,   'cmc8800c-b',  'CC8800C-B',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.3.2',   'network/ccmts/ccmts_c_16.png',  'network/ccmts/ccmts_c_32.png',  'network/ccmts/ccmts_c_48.png',  'network/ccmts/ccmts_c_64.png',  32285);
/* -- version 1.7.18.1,build 2013-9-9,module cmc */
                
-- version 2.0.0.1,build 2013-10-22,module cmc
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(4000006,'terminalCollectConfig',4000000,'userPower.terminalCollectConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,4000006);

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000001,'cmcUserNumHis',5000000,'userPower.cmcUserNumHis');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000001);
			
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000002,'cmcAllCcmtsUserNum',5000000,'userPower.cmcAllCcmtsUserNum');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000002);
			
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000003,'cmCpeQuery',5000000,'userPower.cmCpeQuery');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000003);
			
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000007,'cmcPerfParamConfig',5000000,'userPower.cmcPerfParamConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000007);

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000008,'ccGlobalConfig',5000000,'userPower.ccGlobalConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000008);

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000009,'ccPerfMgmt',5000000,'userPower.ccPerfMgmt');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000009);
/* -- version 2.0.0.1,build 2013-10-22,module cmc */

-- version 2.0.0.1,build 2013-10-30,module cmc
UPDATE reporttemplate SET url = '/cmc/report/showCcmtsDeviceAsset.tv' WHERE templateId = 10006;
UPDATE reporttemplate SET url = '/cmc/report/showCmDeviceAsset.tv' WHERE templateId = 10007;
UPDATE reporttemplate SET url = '/cmc/report/showCcmtsChannelAsset.tv' WHERE templateId = 10008;
UPDATE reporttemplate SET url = '/cmc/report/showCcmtsUsSnrStatistics.tv' WHERE templateId = 20006;
UPDATE reporttemplate SET url = '/cmc/report/showCcmtsUserFlowStatistics.tv' WHERE templateId = 20007;
UPDATE reporttemplate SET url = '/cmc/report/showCmRealTimeUserStatic.tv' WHERE templateId = 20008;
UPDATE reporttemplate SET url = '/cmc/report/showCmDailyNumStatic.tv' WHERE templateId = 20009;
UPDATE reporttemplate SET url = '/cmc/report/queryOltRunningStatus.tv' WHERE templateId = 20010;
/* -- version 2.0.0.1,build 2013-10-30,module cmc */

-- version 2.0.0.1,build 2013-11-5,module cmc
DELETE FROM EntityType WHERE typeId = 30003;
/* -- version 2.0.0.1,build 2013-11-5,module cmc */

-- version 2.0.0.1,build 2013-11-11,module cmc
DELETE FROM perfthresholdrule WHERE targetId = 'CC_POWER_MODULE_TEMP';
DELETE FROM perfTarget WHERE targetId = 'CC_POWER_MODULE_TEMP';
DELETE FROM EventType where typeId = -846;
DELETE FROM EventType where typeId = -847;
DELETE FROM AlertType where typeId = -846;
DELETE FROM Event2Alert where alertTypeId = -846;
/* -- version 2.0.0.1,build 2013-11-11,module cmc */

-- version 2.0.0.1,build 2013-11-13,module cmc
DELETE FROM FunctionItem WHERE functionId = 4000006;
DELETE FROM RoleFunctionRela WHERE roleId = 2 AND functionId = 4000006;

INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(5000014,'terminalCollectConfig',5000000,'userPower.terminalCollectConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,5000014);
/* -- version 2.0.0.1,build 2013-11-13,module cmc */

-- version 2.0.0.1,build 2013-11-14,module cmc
UPDATE perfthresholdrule SET thresholds = '1_80_3' WHERE targetId = 'CC_UNUSUAL_CMNUMBER' and templateId = 2;
/* -- version 2.0.0.1,build 2013-11-14,module cmc */

-- version 2.0.0.1,build 2013-11-20,module cmc
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10006, 10000, 'ccmtsDeviceListReportCreator', 'report.ccmtsDeviceListReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmc/report/showCcmtsDeviceAsset.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10007, 10000, 'cmReportCreator', 'report.cmReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmc/report/showCmDeviceAsset.tv', '1');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (10008, 10000, 'ccmtsChannelListReportCreator', 'report.ccmtsChannelListReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmc/report/showCcmtsChannelAsset.tv', '1');

INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20006, 20000, 'cmcSnrReportCreator', 'report.cmcSnrReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmc/report/showCcmtsUsSnrStatistics.tv', '1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20007, 20000, 'cmcUserFlowReportCreator', 'report.cmcUserFlowRepor', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmc/report/showCcmtsUserFlowStatistics.tv', '1');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20008, 20000, 'cmRealTimeUserStaticReportCreator', 'report.cmRealTimeUserStaticReportCreator', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmc/report/showCmRealTimeUserStatic.tv', '1');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20009, 20000, 'cmDailyNumStaticReportCreator', 'report.cmDailyNumStaticReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmc/report/showCmDailyNumStatic.tv', '0');
INSERT INTO ReportTemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20010, 20000,'oltRunningStatusReportCreator', 'report.oltRunningStatusReport', NULL, 'icoG1', 'device_48.gif', 'asset', NULL, '/cmc/report/queryOltRunningStatus.tv', '1');
/* -- version 2.0.0.1,build 2013-11-20,module cmc */

/* -- version 2.0.2.0,build 2013-12-03,module cmc */
INSERT INTO systempreferences(name,  value,  module) VALUES ('autoClearPeriod','0','autoClear');
/* -- version 2.0.2.0,build 2013-12-03,module cmc */

-- version 2.0.2.0,build 2013-12-4,module cmc
INSERT INTO GlobalCollectTimeTable(perftargetName,interval_,targetGroup,type,enable) VALUES
            ('opticalReceiver',15,'optical',2,1);
/* -- version 2.0.2.0,build 2013-12-4,module cmc */

-- version 2.0.3.0,build 2013-12-24,module cmc
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CM_FLAP_INSFAIL', 30000, 'CM FLAP', 'CM_FLAP_MONITOR','');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CM_FLAP_INSFAILGROW', 30000, 'CM FLAP', 'CM_FLAP_MONITOR','');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CM_FLAP_POWER_ADJ', 30000, 'CM FLAP', 'CM_FLAP_MONITOR','');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CM_FLAP_HITGROW_PEC', 30000, 'CM FLAP', 'CM_FLAP_MONITOR','');

insert into GlobalCollectTimeTable(perftargetName,interval_,targetGroup,type,enable) VALUES
                ('cmflap',15,'cmflapGrp',2,1);
/* -- version 2.0.3.0,build 2013-12-24,module cmc */

-- version 2.0.3.0,build 2014-1-6,module cmc
UPDATE perfthresholdrule SET thresholds = '5_20_3' WHERE targetId = 'CC_NOISE' and templateId = 2;
/* -- version 2.0.3.0,build 2014-1-6,module cmc */

-- version 2.0.3.0,build 2014-1-7,module cmc
update globalcollecttimetable set enable=0 where perfTargetName='cmflap' and type=2 and targetgroup='cmflapGrp';
insert into globalcollecttimetable (perfTargetName, type, interval_, enable, targetGroup)
	values	('cmflap', 1, 15, 0, 'cmflapGrp');
/*-- version 2.0.3.0,build 2014-1-7,module cmc*/

-- version 2.0.3.0,build 2014-1-9,module cmc
/*CM上线时间估算为1-2分钟，当前默认采集周期是15m，因此取中间值7-15--10次*/
INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('CM_FLAP_INSFAILGROW',2,'1_10_5',1,1,1,'00:00-23:59#1234567');	
	
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
                (-918,-50002,'CCMTSCMEvent','DB.eventType.e918',''),
                (-919,-50002,'CCMTSCMEvent','DB.eventType.e919','');
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
    (-918, -50002, 'CCMTSCMEvent','DB.alertType.a918',3,'',0,  0,  '0',  '1',  '',  '');

INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
                (-918,-918,1),
                (-919,-918,0);
/* -- version 2.0.3.0,build 2014-1-9,module cmc */

-- version 2.0.3.0,build 2014-1-10,module cmc
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('hisCollectCycle',   '3600000',  'spectrum'),
                ('hisCollectDuration',   '900000',  'spectrum'),
                ('hisTimeInterval',   '300000',  'spectrum'),
                ('timeInterval',   '5000',  'spectrum'),
                ('timeLimit',   '3600000',  'spectrum');
/* -- version 2.0.3.0,build 2014-1-10,module cmc */

-- version 2.0.3.0,build 2014-1-13,module cmc
update perfthresholdrule set thresholds='1_10_3' where targetId='CM_FLAP_INSFAILGROW';
/* -- version 2.0.3.0,build 2014-1-13,module cmc */

-- version 2.0.3.0,build 2014-2-8,module cmc
delete from globalcollecttimetable where perfTargetName = 'cmflap' and type = 1;
/* -- version 2.0.3.0,build 2014-2-8,module cmc */

-- version 2.0.3.0,build 2014-2-8,module cmc
UPDATE perfTarget SET unit = '%' where targetId = 'CC_ERRORCODE';
UPDATE perfTarget SET unit = '%' where targetId = 'CC_UNERRORCODE';
/* -- version 2.0.3.0,build 2014-2-8,module cmc */

-- version 2.0.3.0,build 2014-2-9,module cmc
UPDATE perfthresholdrule SET thresholds = '1_20_3' where targetId = 'CC_ERRORCODE' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_20_3' where targetId = 'CC_UNERRORCODE' and templateId = 2;
/* -- version 2.0.3.0,build 2014-2-9,module cmc */

-- version 2.0.3.0,build 2014-2-12,module cmc
update GlobalCollectTimeTable set enable = 0 where perftargetName='cpuUsed' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='memUsed' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='flashUsed' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='optLink' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='moduleTemp' and type=2;
update GlobalCollectTimeTable set interval_ = 30, enable = 0 where perftargetName='upLinkFlow' and type=2;
update GlobalCollectTimeTable set interval_ = 30, enable = 0 where perftargetName='macFlow' and type=2;
update GlobalCollectTimeTable set interval_ = 30, enable = 0 where perftargetName='channelSpeed' and type=2;
/* -- version 2.0.3.0,build 2014-2-12,module cmc */

-- version 2.0.3.0,build 2014-2-13,module cmc
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30007,   'cmc8800s',  'CC8800S',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.5',   'network/ccmts/ccmts_b_16.png',  'network/ccmts/ccmts_b_32.png',  'network/ccmts/ccmts_b_48.png',  'network/ccmts/ccmts_b_64.png',  32285);
/* -- version 2.0.3.0,build 2014-2-13,module cmc */
                
-- version 2.0.3.0,build 2014-2-21,module cmc
update EntityType set icon16 = 'network/ccmts/ccmts_s_16.png',icon32 = 'network/ccmts/ccmts_s_32.png',icon48 = 'network/ccmts/ccmts_s_48.png',icon64 = 'network/ccmts/ccmts_s_64.png' where typeId=30007;
DELETE FROM globalcollecttimetable WHERE perfTargetName = 'cmflap' and type = 1;
/* -- version 2.0.3.0,build 2014-2-21,module cmc */

-- version 2.0.3.0,build 2014-3-4,module cmc
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(60000,   'CCMTS,CMTS',  'CCMTS,CMTS',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(70000,   'CC with agent',  'CC with agent',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
(80000,   'CC without agent',  'CC without agent',  'network',  '/network',   '',   '',  '',  '',  '',  1); 
insert into entitytyperelation (type, typeId) values (1,30002);
insert into entitytyperelation (type, typeId) values (1,30004);
insert into entitytyperelation (type, typeId) values (1,30006);
insert into entitytyperelation (type, typeId) values (2,30000);
insert into entitytyperelation (type, typeId) values (2,60000);
insert into entitytyperelation (type, typeId) values (2,70000);
insert into entitytyperelation (type, typeId) values (2,80000);
insert into entitytyperelation (type, typeId) values (30000,30002);
insert into entitytyperelation (type, typeId) values (30000,30004);
insert into entitytyperelation (type, typeId) values (30000,30006);
insert into entitytyperelation (type, typeId) values (50000,30002);
insert into entitytyperelation (type, typeId) values (50000,30004);
insert into entitytyperelation (type, typeId) values (60000,30002);
insert into entitytyperelation (type, typeId) values (60000,30004);
insert into entitytyperelation (type, typeId) values (60000,30006);
insert into entitytyperelation (type, typeId) values (70000,30002);
insert into entitytyperelation (type, typeId) values (70000,30004);
insert into entitytyperelation (type, typeId) values (70000,30006);
insert into entitytyperelation (type, typeId) values (30002,30002);
insert into entitytyperelation (type, typeId) values (30004,30004);
insert into entitytyperelation (type, typeId) values (30006,30006);
/* -- version 2.0.3.0,build 2014-3-4,module cmc */
-- version 2.0.3.0,build 2014-3-8,module cmc
insert into entitytyperelation (type, typeId) values (3,30000);
insert into entitytyperelation (type, typeId) values (4,30000);
insert into entitytyperelation (type, typeId) values (1,30007);
insert into entitytyperelation (type, typeId) values (30000,30007);
insert into entitytyperelation (type, typeId) values (50000,30007);
insert into entitytyperelation (type, typeId) values (60000,30007);
insert into entitytyperelation (type, typeId) values (70000,30007);
insert into entitytyperelation (type, typeId) values (30007,30007);
/* -- version 2.0.3.0,build 2014-3-8,module cmc */

-- version 2.0.6.2,build 2014-3-15,module cmc
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values(5000016,'cmtsSpectrumConfig',5000000,'cmc.cmtsSpectrumConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,5000016);
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values(5000017,'spectrumVideoMgmt',5000000,'cmc.spectrumVideoMgmt');
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,5000017);
/* -- version 2.0.6.2,build 2014-3-15,module cmc */ 

-- version 2.0.6.4,build 2014-3-23,module cmc
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20011,20000,'ccmtsUpChlFlowReportCreator','report.ccmtsUpChannelFlowReport',NULL,'icoG1','device_48.gif','asset',NULL,'/cmc/report/showCcmtsUpChlFlowAsset.tv','1');
INSERT INTO reporttemplate(templateId,superiorId,name,displayName,note,icon16,icon48,module,path,url,taskable) VALUES (20012,20000,'ccmtsDownChlFlowReportCreator','report.ccmtsDownChannelFlowReport',NULL,'icoG1','device_48.gif','asset',NULL,'/cmc/report/showCcmtsDownChlFlowAsset.tv','1');
/* -- version 2.0.6.4,build 2014-3-23,module cmc */

-- version 2.0.6.4,build 2014-3-24,module cmc
DELETE FROM perfthresholdrule where targetId = 'CM_FLAP_INSFAILGROW';
DELETE FROM perfTarget WHERE targetId = 'CM_FLAP_INSFAIL';
DELETE FROM perfTarget WHERE targetId = 'CM_FLAP_INSFAILGROW';
DELETE FROM perfTarget WHERE targetId = 'CM_FLAP_POWER_ADJ';
DELETE FROM perfTarget WHERE targetId = 'CM_FLAP_HITGROW_PEC';
/* -- version 2.0.6.4,build 2014-3-24,module cmc */

-- version 2.1.0.0,build 2014-3-25,module cmc
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
     (120000,   'Optical receiver',  'Optical receiver',  'network',  '/network',   '',   '',  '',  '',  '',  1);
insert into entitytyperelation (type, typeId) values (2,120000);
insert into entitytyperelation (type, typeId) values (120000,30006);
/* -- version 2.1.0.0,build 2014-3-25,module cmc */

-- version 2.1.0.0,build 2014-3-27,module cmc
insert into entitytyperelation (type, typeId) values (50000,30006);
/* -- version 2.1.0.0,build 2014-3-27,module cmc */

-- version 2.1.0.0,build 2014-3-27,module cmc
delete from entityType where typeId = 30006;
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30006,   'cmc8800c-b',  'CC8800C-B',  'cmc',  '/cmc',  '1.3.6.1.4.1.32285.11.1.1.1.3.2',   'network/ccmts/ccmts_c_b_16.png',  'network/ccmts/ccmts_c_b_32.png',  'network/ccmts/ccmts_c_b_48.png',  'network/ccmts/ccmts_c_b_64.png',  32285);
/* -- version 2.1.0.0,build 2014-3-27,module cmc */

-- version 2.0.6.2,build 2014-4-1,module cmc
delete from perfTarget where targetId = 'CM_FLAP_INSFAIL';
delete from perfTarget where targetId = 'CM_FLAP_INSFAILGROW';
delete from perfTarget where targetId = 'CM_FLAP_POWER_ADJ';
delete from perfTarget where targetId = 'CM_FLAP_HITGROW_PEC';
/* -- version 2.0.6.2,build 2014-4-1,module cmc */
-- version 2.1.0.0,build 2014-4-23,module cmc
delete from perfglobal where flag = 'CC';
INSERT INTO perfglobal(flag,isPerfOn,isRelationWithDefaultTemplate,defaultTemplateId,isPerfThreshold,defaultCollectTime) values ('60000', 1,1,2,1, 15);
update perfthresholdtemplate set templateName = 'CMTS_DEFAULT_TEMPLATE',templateType = 60000 where templateId = 2; 
/* -- version 2.1.0.0,build 2014-4-23,module cmc */
-- version 2.1.0.0,build 2014-4-25,module cmc */
update entitytype set displayname = 'CMTS' where typeId = 30000;
/* -- version 2.1.0.0,build 2014-4-25,module cmc */
-- version 2.1.0.0,build 2014-4-28,module cmc */
update entitytype set displayname = 'CMTS' where typeId = 60000;
/* -- version 2.1.0.0,build 2014-4-28,module cmc */
-- version 2.1.0.0,build 2014-5-5,module cmc
update GlobalCollectTimeTable set enable = 0 where perftargetName='cpuUsed' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='memUsed' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='flashUsed' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='optLink' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='moduleTemp' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='upLinkFlow' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='macFlow' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='channelSpeed' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='opticalReceiver' and type=2;
update GlobalCollectTimeTable set enable = 0 where perftargetName='cmflap' and type=2;
/* -- version 2.1.0.0,build 2014-5-5,module cmc */

-- version 2.1.0.0,build 2014-5-12,module cmc
INSERT INTO batchautodiscoveryentitytype(typeId) values(30002);
INSERT INTO batchautodiscoveryentitytype(typeId) values(30004);
INSERT INTO batchautodiscoveryentitytype(typeId) values(30006);
INSERT INTO batchautodiscoveryentitytype(typeId) values(30007);
/* -- version 2.1.0.0,build 2014-5-12,module cmc */

-- version 2.3.1.0,build 2014-8-12,module cmc
delete from perfthresholdrule where targetId = 'CC_UP_MODULE_TEMP';
delete from perfthresholdrule where targetId = 'CC_RF_MODULE_TEMP';
delete from perfthresholdrule where targetId = 'CC_BCM_MODULE_TEMP';
delete from perfthresholdrule where targetId = 'CC_MEM_MODULE_TEMP';
delete from perfTarget where targetGroup = 'Performance.moduleTemp';

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_US_MODULE_TEMP', 30000, 'Performance.usTemp','Performance.moduleTemp','℃');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_DS_MODULE_TEMP', 30000, 'Performance.dsTemp','Performance.moduleTemp','℃');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_INSIDE_TEMP', 30000, 'Performance.insideTemp','Performance.moduleTemp','℃');
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit) values('CC_OUTSIDE_TEMP',30000, 'Performance.outsideTemp','Performance.moduleTemp','℃');

INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values 
('CC_US_MODULE_TEMP', 2,'1_70_3',1,1,1,'00:00-23:59#1234567'),
('CC_DS_MODULE_TEMP', 2,'1_70_3',1,1,1,'00:00-23:59#1234567'),
('CC_INSIDE_TEMP', 2,'1_70_3',1,1,1,'00:00-23:59#1234567'),
('CC_OUTSIDE_TEMP', 2,'1_70_3',1,1,1,'00:00-23:59#1234567');
/* -- version 2.3.1.0,build 2014-8-12,module cmc */

-- version 2.4.0.0,build 2014-8-13,module cmc
update systempreferences set value='2000' where module = 'spectrum' and name = 'timeInterval';
/* -- version 2.4.0.0,build 2014-8-13,module cmc */
-- version 2.4.0.0,build 2014-8-25,module cmc
insert into UserCustomlization(userId,functionName,functionAction,icon) values (2,'WorkBench.cmcpe','showCmCpeQuery()','icoD1');
/* -- version 2.2.6.0,build 2014-8-25,module cmc */

-- version 2.4.0.0,build 2014-8-26,module cmc
UPDATE perfthresholdrule SET thresholds = '5_11_4' WHERE targetId = 'CC_NOISE' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_45_3' WHERE targetId = 'CC_SPEED' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_75_3#1_90_4' WHERE targetId = 'CC_UTILIZATION' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_15_3#1_30_4' WHERE targetId = 'CC_UNERRORCODE' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_90_3' WHERE targetId = 'CC_CPUUSED' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_90_3' WHERE targetId = 'CC_FLASHUSED' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_90_3' WHERE targetId = 'CC_MEMUSED' and templateId = 2;

UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'CC_UPLINK_INSPEED' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_800_3' WHERE targetId = 'CC_UPLINK_OUTSPEED' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_50_3#1_70_4#1_85_5' WHERE targetId = 'CC_UPLINK_UESD' and templateId = 2;

UPDATE perfthresholdrule SET thresholds = '1_750_3' WHERE targetId = 'CC_MAC_INSPEED' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_120_3' WHERE targetId = 'CC_MAC_OUTSPEED' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_50_3#1_70_4#1_85_5' WHERE targetId = 'CC_MAC_USED' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_-7_2#5_-24_3#5_-27_4' WHERE targetId = 'CC_PON_RE_POWER' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '5_0_2#1_2_3' WHERE targetId = 'CC_PON_RT_POWER' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '5_0_2#1_90_3' WHERE targetId = 'CC_PON_TEMP' and templateId = 2;
delete from perfthresholdrule  WHERE targetId = 'CC_PON_CURRENT' and templateId = 2;
delete from perfthresholdrule  WHERE targetId = 'CC_PON_VOLTAGE' and templateId = 2;

UPDATE perfthresholdrule SET thresholds = '5_-40_2#1_80_3#1_85_4' WHERE targetId = 'CC_US_MODULE_TEMP' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '5_-40_2#1_80_3#1_85_4' WHERE targetId = 'CC_DS_MODULE_TEMP' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '5_-40_2#1_100_3#1_120_4' WHERE targetId = 'CC_INSIDE_TEMP' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '5_-40_2#1_80_3#1_85_4' WHERE targetId = 'CC_OUTSIDE_TEMP' and templateId = 2;
UPDATE perfthresholdrule SET thresholds = '1_25_3' WHERE targetId = 'CC_UNUSUAL_CMNUMBER' and templateId = 2;
/* -- version 2.4.0.0,build 2014-8-26,module cmc */

-- version 2.4.0.0,build 2014-8-27,module cmc
update GlobalCollectTimeTable set enable = 1 where perftargetName='cpuUsed' and type=2;
update GlobalCollectTimeTable set enable = 1 where perftargetName='memUsed' and type=2;
update GlobalCollectTimeTable set enable = 1 where perftargetName='flashUsed' and type=2;
update GlobalCollectTimeTable set enable = 1 where perftargetName='optLink' and type=2;
update GlobalCollectTimeTable set enable = 1 where perftargetName='channelSpeed' and type=2;
update GlobalCollectTimeTable set enable = 1 where perftargetName='upLinkFlow' and type=2;
update GlobalCollectTimeTable set enable = 1 where perftargetName='macFlow' and type=2;
update GlobalCollectTimeTable set enable = 1,interval_ = 240 where perftargetName='cmflap' and type=2;
update GlobalCollectTimeTable set enable = 1,interval_ = 5 where perftargetName='opticalReceiver' and type=2;
/* -- version 2.4.0.0,build 2014-8-27,module cmc */

-- version 2.4.0.0,build 2014-9-18,module cmc
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values
			(10000006,'spectrumManagement',2,'userPower.spectrumManagement');
INSERT INTO RoleFunctionRela(roleId,functionId) values(1,10000006);
INSERT INTO RoleFunctionRela(roleId,functionId) values(2,10000006);
/* -- version 2.4.0.0,build 2014-9-18,module cmc */

-- version 2.4.3.0,build 2014-10-17,module cmc
update EntityType set name='cmc',displayName="CMC" where name = 'CCMTS';
insert into EntityTypeRelation values(3, 30001);
insert into EntityTypeRelation values(3, 30002);
insert into EntityTypeRelation values(3, 30004);
insert into EntityTypeRelation values(3, 30005);
insert into EntityTypeRelation values(3, 30006);
insert into EntityTypeRelation values(3, 30007);
/* -- version 2.4.3.0,build 2014-10-17,module cmc */

-- version 2.4.3.0,build 2014-10-22,module cmc
UPDATE perfthresholdrule SET thresholds = '5_14_4' WHERE targetId = 'CC_NOISE' and templateId = 2;
/* -- version 2.4.3.0,build 2014-10-22,module cmc */

-- version 2.4.3.0,build 2014-10-29,module cmc
delete from perfthresholdrule  WHERE targetId = 'CC_UPLINK_INSPEED' and templateId = 2;
delete from perfthresholdrule  WHERE targetId = 'CC_UPLINK_OUTSPEED' and templateId = 2;
delete from perfthresholdrule  WHERE targetId = 'CC_MAC_INSPEED' and templateId = 2;
delete from perfthresholdrule  WHERE targetId = 'CC_MAC_OUTSPEED' and templateId = 2;
delete from perfthresholdrule  WHERE targetId = 'CC_SPEED' and templateId = 2;
/* -- version 2.4.3.0,build 2014-10-29,module cmc */

-- version 2.4.3.0,build 2014-10-29,module cmc
delete from perfthresholdrule WHERE targetId = 'CC_INSIDE_TEMP' and templateId = 2;
delete from perftarget WHERE targetId = 'CC_INSIDE_TEMP';
/* -- version 2.4.3.0,build 2014-10-29,module cmc */

-- version 2.4.3.0,build 2014-10-30,module cmc
delete from alerttype where typeId = -842;
delete from eventtype where typeId = -842;
delete from eventtype where typeId = -843;
/* -- version 2.4.3.0,build 2014-10-30,module cmc */

-- version 2.4.5.0,build 2014-11-26,module cmc
INSERT INTO PortletItem(itemId,name,note,url,type,loadingText,icon,refreshable,refreshInterval,closable,settingable,module,categoryId) values
       (420,'PortletCategory.getTopLowNoise',NULL,'/cmcperf/getTopLowNoiseLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
       (421,'PortletCategory.channelUsed',NULL,'/cmcperf/getTopChnlUtiliLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
       (422,'PortletCategory.berRate',NULL,'/cmcperf/getTopPortletErrorCodesLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
       (423,'PortletCategory.cmFlapIns',NULL,'/cmcperf/getTopPortletFlapInsGrowthLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
       (424,'PortletCategory.cmcOptical',NULL,'/cmcperf/loadCmcOpticalTop10.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
       (425,'PortletCategory.getTopCcUsers',NULL,'/cmcperf/getTopCcUsersLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
       (426,'PortletCategory.getTopUpChnUsers',NULL,'/cmcperf/getTopUpChnUsersLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4),
       (427,'PortletCategory.getTopDownChnUsers',NULL,'/cmcperf/getTopDownChnUsersLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4);
/* -- version 2.4.5.0,build 2014-11-26,module cmc */

-- version 2.4.5.0,build 2015-1-14,module cmc
UPDATE perfthresholdtemplate SET parentType = 60000 WHERE templateId = 2;
delete from  perfthresholdtemplate WHERE isDefaultTemplate != 1;
delete from perftarget where targetType = 30000;

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_CONNECTIVITY', 60000, 'Performance.delay','Performance.cmc_deviceStatus','ms',10000,1,1,0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_CPUUSED', 60000, 'Performance.cpuUsed','Performance.deviceServiceQuality','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_FLASHUSED', 60000, 'Performance.flashUsed','Performance.deviceServiceQuality','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_MEMUSED', 60000, 'Performance.memUsed','Performance.deviceServiceQuality','%',100,0,1,2);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_BPSK', 60000, 'Performance.ccNoise_BPSK','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_QPSK', 60000, 'Performance.ccNoise_QPSK','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_PSK8', 60000, 'Performance.ccNoise_PSK8','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_PSK16', 60000, 'Performance.ccNoise_PSK16','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_PSK32', 60000, 'Performance.ccNoise_PSK32','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_QAM16', 60000, 'Performance.ccNoise_QAM16','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_QAM32', 60000, 'Performance.ccNoise_QAM32','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_QAM64', 60000, 'Performance.ccNoise_QAM64','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_QAM256', 60000, 'Performance.ccNoise_QAM256','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_OTHER', 60000, 'Performance.ccNoise_other','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_ERRORCODE', 60000, 'Performance.ccErrorCode','Performance.signalQuality','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_UNERRORCODE', 60000, 'Performance.ccUnErrorCode','Performance.signalQuality','%',100,0,1,2);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_UTILIZATION', 60000, 'Performance.ccUsed', 'Performance.speed','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_UPLINK_IN_UESD', 60000, 'Performance.upLinkInUsed', 'Performance.speed','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_MAC_IN_USED', 60000, 'Performance.macInUsed', 'Performance.speed','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_UPLINK_OUT_UESD', 60000, 'Performance.upLinkOutUsed', 'Performance.speed','%',100,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_MAC_OUT_USED', 60000, 'Performance.macOutUsed', 'Performance.speed','%',100,0,1,2);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_ONU_MODULE_RE_POWER', 60000, 'Performance.optRePower','Performance.optLink','dBm',-3,-27,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_ONU_MODULE_TX_POWER', 60000, 'Performance.optRtPower','Performance.optLink','dBm',2,0,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_ONU_MODULE_TEMP', 60000, 'Performance.optTemp','Performance.optLink','℃',120,-40,1,0);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_PON_MODULE_RE_POWER', 60000, 'Performance.uplinkOptRePower','Performance.optLink','dBm',-3,-24,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_PON_MODULE_TX_POWER', 60000, 'Performance.uplinkOptRtPower','Performance.optLink','dBm',3,-9,1,2);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_PON_MODULE_TEMP', 60000, 'Performance.uplinkOptTemp','Performance.optLink','℃',120,-40,1,0);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_US_MODULE_TEMP', 60000, 'Performance.usTemp','Performance.moduleTemp','℃',120,-40,1,0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_DS_MODULE_TEMP', 60000, 'Performance.dsTemp','Performance.moduleTemp','℃',120,-40,1,0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_OUTSIDE_TEMP',60000, 'Performance.outsideTemp','Performance.moduleTemp','℃',120,-40,1,0);

INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_UNUSUAL_CMNUM',60000, 'Performance.cmNumber','Performance.deviceServiceQuality','',10000,0,1,0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CMTS_UNUSUAL_CMNUM',60000, 'Performance.cmtsCmNumber','Performance.deviceServiceQuality','',10000,0,1,0);
/* -- version 2.4.5.0,build 2015-1-14,module cmc */


-- version 2.4.5.0,build 2015-1-16,module cmc
INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('CC_CONNECTIVITY', 2,'1_1000_5_5',1,1,1,'00:00-23:59#1234567'),
	('CC_FLASHUSED', 2,'1_90_3_5',1,1,1,'0:00-23:59#1234567'),
	('CC_MEMUSED', 2,'1_90_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_CPUUSED',2,'1_90_4_5',1,1,1,'00:00-23:59#1234567'),
	
	('CC_NOISE_BPSK',2,'5_11_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_QPSK',2,'5_14_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_PSK8',2,'5_20_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_PSK16',2,'5_25_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_PSK32',2,'5_31_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_QAM16',2,'5_21_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_QAM32',2,'5_25_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_QAM64',2,'5_27_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_QAM256',2,'5_33_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_OTHER',2,'5_11_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_ERRORCODE', 2,'1_20_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_UNERRORCODE', 2,'1_15_3_5#1_30_4_4',1,1,1,'00:00-23:59#1234567'),
	
	('CC_UTILIZATION',2,'1_75_3_5#1_90_4_4',1,1,1,'00:00-23:59#1234567'),
	('CC_UPLINK_IN_UESD', 2,'1_50_2_4#1_70_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	('CC_MAC_IN_USED', 2,'1_50_2_4#1_70_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	('CC_UPLINK_OUT_UESD', 2,'1_50_2_4#1_70_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	('CC_MAC_OUT_USED', 2,'1_50_2_4#1_70_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	
	('CC_ONU_MODULE_RE_POWER', 2,'1_-7_2_4#5_-24_3_5#5_-27_4_6',1,1,1,'00:00-23:59#1234567'),
	('CC_ONU_MODULE_TX_POWER', 2,'5_0_2_4#1_2_3_5',1,1,1,'00:00-23:59#1234567'),
	('CC_ONU_MODULE_TEMP', 2,'5_0_2_4#1_90_3_5',1,1,1,'00:00-23:59#1234567'),
	('CC_PON_MODULE_RE_POWER', 2,'5_-24_3_4#1_-3_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_PON_MODULE_TX_POWER', 2,'5_-9_3_4#1_3_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_PON_MODULE_TEMP', 2,'5_0_2_4#1_90_3_5',1,1,1,'00:00-23:59#1234567'),
	
	('CC_US_MODULE_TEMP', 2,'5_-40_2_4#1_80_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	('CC_DS_MODULE_TEMP', 2,'5_-40_2_4#1_80_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	('CC_OUTSIDE_TEMP', 2,'5_-40_2_4#1_80_3_5#1_85_4_6',1,1,1,'00:00-23:59#1234567'),
	
	('CC_UNUSUAL_CMNUM', 2,'1_25_3_5',1,1,1,'00:00-23:59#1234567'),
	('CMTS_UNUSUAL_CMNUM', 2,'1_80_3_5',1,1,1,'00:00-23:59#1234567');
/* -- version 2.4.5.0,build 2015-1-16,module cmc */

-- version 2.4.5.0,build 2015-1-16,module cmc
delete from Event2Alert where eventTypeId = -804;
delete from Event2Alert where eventTypeId = -805;
delete from Event2Alert where eventTypeId = -816;
delete from Event2Alert where eventTypeId = -817;
delete from Event2Alert where eventTypeId = -818;
delete from Event2Alert where eventTypeId = -819;
delete from Event2Alert where eventTypeId = -820;
delete from Event2Alert where eventTypeId = -821;
delete from Event2Alert where eventTypeId = -822;
delete from Event2Alert where eventTypeId = -823;
delete from Event2Alert where eventTypeId = -824;
delete from Event2Alert where eventTypeId = -825;
delete from Event2Alert where eventTypeId = -834;
delete from Event2Alert where eventTypeId = -835;
delete from Event2Alert where eventTypeId = -836;
delete from Event2Alert where eventTypeId = -837;

delete from AlertType where typeId = -804;
delete from AlertType where typeId = -816;
delete from AlertType where typeId = -818;
delete from AlertType where typeId = -820;
delete from AlertType where typeId = -822;
delete from AlertType where typeId = -824;
delete from AlertType where typeId = -834;
delete from AlertType where typeId = -836;

delete from EventType where typeId = -804;
delete from EventType where typeId = -805;
delete from EventType where typeId = -816;
delete from EventType where typeId = -817;
delete from EventType where typeId = -818;
delete from EventType where typeId = -819;
delete from EventType where typeId = -820;
delete from EventType where typeId = -821;
delete from EventType where typeId = -822;
delete from EventType where typeId = -823;
delete from EventType where typeId = -824;
delete from EventType where typeId = -825;
delete from EventType where typeId = -834;
delete from EventType where typeId = -835;
delete from EventType where typeId = -836;
delete from EventType where typeId = -837;

INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
 				(-820,-50002,'CCMTS_Uplink_Out_UsedEvent','DB.eventType.e820',''),
  				(-821,-50002,'CCMTS_Uplink_Out_UsedEvent','DB.eventType.e821',''),
                (-850,-50002,'CCMTS_Uplink_In_UsedEvent','DB.eventType.e850',''),
                (-851,-50002,'CCMTS_Uplink_In_UsedEvent','DB.eventType.e851',''),
                (-852,-50002,'CCMTS_MAC_Out_UsedEvent','DB.eventType.e852',''),
                (-853,-50002,'CCMTS_MAC_Out_UsedEvent','DB.eventType.e853','');
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
                (-820,-50002,'CCMTS_Uplink_Out_Used','DB.alertType.a820',5,'',0,  0,  '0',  '1',  '',  ''),
                (-850,-50002,'CCMTS_Uplink_In_Used','DB.alertType.a850',5,'',0,  0,  '0',  '1',  '',  ''),
                (-852,-50002,'CCMTS_MAC_Out_Used','DB.alertType.a852',5,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
				(-820,-820,1),
				(-821,-820,0),
                (-850,-850,1),
                (-851,-850,0),
                (-852,-852,1),
                (-853,-852,0);
/* -- version 2.4.5.0,build 2015-1-16,module cmc */

-- version 2.4.8.0,build 2015-2-10,module cmc
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30010, 'cmc8800e', 'CC8800E',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.6',   'network/ccmts/ccmts_e_16.png',  'network/ccmts/ccmts_e_32.png',  'network/ccmts/ccmts_e_48.png',  'network/ccmts/ccmts_e_64.png',  32285);
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30011, 'cmc8800c-e', 'CC8800C-E',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.3.3',   'network/ccmts/ccmts_c_e_16.png',  'network/ccmts/ccmts_c_e_32.png',  'network/ccmts/ccmts_c_e_48.png',  'network/ccmts/ccmts_c_e_64.png',  32285);
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30012, 'cmc8800d-e', 'CC8800D-E',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.4.1',   'network/ccmts/ccmts_d_e_16.png',  'network/ccmts/ccmts_d_e_32.png',  'network/ccmts/ccmts_d_e_48.png',  'network/ccmts/ccmts_d_e_64.png',  32285);          

INSERT INTO batchautodiscoveryentitytype(typeId) values(30010);
INSERT INTO batchautodiscoveryentitytype(typeId) values(30011);
INSERT INTO batchautodiscoveryentitytype(typeId) values(30012);
-- E
insert into entitytyperelation select 1, 30010;
insert into entitytyperelation select 30000, 30010;
insert into entitytyperelation select 50000, 30010;
insert into entitytyperelation select 60000, 30010;
insert into entitytyperelation select 70000, 30010;
insert into entitytyperelation select 30010, 30010;
insert into entitytyperelation select 3, 30010;
-- C-E
insert into entitytyperelation select 1, 30011;
insert into entitytyperelation select 30000, 30011;
insert into entitytyperelation select 50000, 30011;
insert into entitytyperelation select 60000, 30011;
insert into entitytyperelation select 70000, 30011;
insert into entitytyperelation select 30011, 30011;
insert into entitytyperelation select 3, 30011;
-- D-E
insert into entitytyperelation select 1, 30012;
insert into entitytyperelation select 30000, 30012;
insert into entitytyperelation select 50000, 30012;
insert into entitytyperelation select 60000, 30012;
insert into entitytyperelation select 70000, 30012;
insert into entitytyperelation select 30012, 30012;
insert into entitytyperelation select 3, 30012;   
-- E Performance
insert into deviceperftarget VALUES 
            ('cmc_onlineStatus', 60000, 30000, 30010, 'cmc_deviceStatus', 1),
            ('cmc_cpuUsed', 60000, 30000, 30010, 'cmc_service', 2),
            ('cmc_memUsed', 60000, 30000, 30010, 'cmc_service', 2),
            ('cmc_flashUsed', 60000, 30000, 30010, 'cmc_service', 2),
            ('cmc_optLink', 60000, 30000, 30010, 'cmc_service', 2),
            ('cmc_moduleTemp', 60000, 30000, 30010, 'cmc_service', 2),
            ('cmc_snr', 60000, 30000, 30010, 'cmc_signalQuality', 3),
            ('cmc_ber', 60000, 30000, 30010, 'cmc_signalQuality', 3),
            ('cmc_upLinkFlow', 60000, 30000, 30010, 'cmc_flow', 4),
            ('cmc_macFlow', 60000, 30000, 30010, 'cmc_flow', 4),
            ('cmc_channelSpeed', 60000, 30000, 30010, 'cmc_flow', 4),
            ('cmc_cmflap', 60000, 30000, 30010, 'cmc_businessQuality', 5),
            ('cmc_opticalReceiver', 60000, 30000, 30010, 'cmc_businessQuality', 5);
-- C-E Performance
insert into deviceperftarget VALUES 
            ('cmc_onlineStatus', 60000, 30000, 30011, 'cmc_deviceStatus', 1),
            ('cmc_cpuUsed', 60000, 30000, 30011, 'cmc_service', 2),
            ('cmc_memUsed', 60000, 30000, 30011, 'cmc_service', 2),
            ('cmc_flashUsed', 60000, 30000, 30011, 'cmc_service', 2),
            ('cmc_optLink', 60000, 30000, 30011, 'cmc_service', 2),
            ('cmc_moduleTemp', 60000, 30000, 30011, 'cmc_service', 2),
            ('cmc_snr', 60000, 30000, 30011, 'cmc_signalQuality', 3),
            ('cmc_ber', 60000, 30000, 30011, 'cmc_signalQuality', 3),
            ('cmc_upLinkFlow', 60000, 30000, 30011, 'cmc_flow', 4),
            ('cmc_macFlow', 60000, 30000, 30011, 'cmc_flow', 4),
            ('cmc_channelSpeed', 60000, 30000, 30011, 'cmc_flow', 4),
            ('cmc_cmflap', 60000, 30000, 30011, 'cmc_businessQuality', 5),
            ('cmc_opticalReceiver', 60000, 30000, 30011, 'cmc_businessQuality', 5); 
-- D-E Performance      
insert into deviceperftarget VALUES 
            ('cmc_onlineStatus', 60000, 30000, 30012, 'cmc_deviceStatus', 1),
            ('cmc_cpuUsed', 60000, 30000, 30012, 'cmc_service', 2),
            ('cmc_memUsed', 60000, 30000, 30012, 'cmc_service', 2),
            ('cmc_flashUsed', 60000, 30000, 30012, 'cmc_service', 2),
            ('cmc_optLink', 60000, 30000, 30012, 'cmc_service', 2),
            ('cmc_moduleTemp', 60000, 30000, 30012, 'cmc_service', 2),
            ('cmc_snr', 60000, 30000, 30012, 'cmc_signalQuality', 3),
            ('cmc_ber', 60000, 30000, 30012, 'cmc_signalQuality', 3),
            ('cmc_upLinkFlow', 60000, 30000, 30012, 'cmc_flow', 4),
            ('cmc_macFlow', 60000, 30000, 30012, 'cmc_flow', 4),
            ('cmc_channelSpeed', 60000, 30000, 30012, 'cmc_flow', 4),
            ('cmc_cmflap', 60000, 30000, 30012, 'cmc_businessQuality', 5),
            ('cmc_opticalReceiver', 60000, 30000, 30012, 'cmc_businessQuality', 5);         
/* -- version 2.4.8.0,build 2015-2-10,module cmc */
            
-- version 2.4.8.0,build 2015-03-11,module cmc
insert into systempreferences(name,value,module) VALUES
('overThresholdTimes','4','spectrum'),
('notOverThresholdTimes','4','spectrum'),
('overThresholdPercent','20','spectrum'),
('notOverThresholdPercent','10','spectrum');
/* -- version 2.4.8.0,build 2015-03-11,module cmc */

-- version 2.4.8.0,build 2015-03-11,module cmc
insert into alerttype(typeId,category,name,displayname,levelId,updatelevel,smartupdate,TERMINATE,threshold,active)
values('200001','200000','SpectrumAlert','SpectrumAlert',4,0,0,0,0,1);
insert into eventtype(typeId,parentId,name,displayname,note)
values('200001','200000','SpectrumAlert','SpectrumAlert','');
insert into event2alert(eventtypeId,alerttypeid,type)
values('200001','200001',1);
insert into event2alert(eventtypeId,alerttypeid,type)
values('200002','200001',0);
insert into alerttype(typeId,category,name,displayname,levelId,updatelevel,smartupdate,TERMINATE,threshold,active)
values('200002','200000','SpectrumClearAlert','SpectrumClearAlert',4,0,0,0,0,1);
insert into eventtype(typeId,parentId,name,displayname,note)
values('200002','200000','SpectrumClearAlert','SpectrumClearAlert','');
/* -- version 2.4.8.0,build 2015-03-11,module cmc */

-- version 2.4.8.0,build 2015-03-11,module cmc
INSERT INTO FunctionItem(functionId,name,superiorId,displayName) values(5000020,'spectrumAlertConfig',5000000,'cmc.spectrumAlertConfig');
INSERT INTO RoleFunctionRela(roleId,functionId) values (2,5000020);
/* -- version 2.4.8.0,build 2015-03-11,module cmc */ 

-- version 2.4.8.0,build 2015-03-27,module cmc
update UserCustomlization set icon = 'icoG13' where userId = 2 and functionName = 'WorkBench.cmcpe';
/* -- version 2.4.8.0,build 2015-03-27,module cmc */

			
-- version 2.4.8.1,build 2015-3-17,module cmc

-- 独立型E
update EntityType set displayName ='CC8800E(独立)' where typeId = 30010;
update EntityType set displayName ='CC8800C-E(独立)' where typeId = 30011;
update EntityType set displayName ='CC8800D-E(独立)' where typeId = 30012;

-- 强集中型E
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30013, 'cmc8800e', 'CC8800E(强集中)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.6',   'network/ccmts/ccmts_e_16.png',  'network/ccmts/ccmts_e_32.png',  'network/ccmts/ccmts_e_48.png',  'network/ccmts/ccmts_e_64.png',  32285);
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30014, 'cmc8800c-e', 'CC8800C-E(强集中)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.3.3',   'network/ccmts/ccmts_c_e_16.png',  'network/ccmts/ccmts_c_e_32.png',  'network/ccmts/ccmts_c_e_48.png',  'network/ccmts/ccmts_c_e_64.png',  32285);
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30015, 'cmc8800d-e', 'CC8800D-E(强集中)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.4.1',   'network/ccmts/ccmts_d_e_16.png',  'network/ccmts/ccmts_d_e_32.png',  'network/ccmts/ccmts_d_e_48.png',  'network/ccmts/ccmts_d_e_64.png',  32285);

-- 弱集中型E
--INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
--                (30016, 'cmc8800e', 'CC8800E(弱集中)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.6',   'network/ccmts/ccmts_e_16.png',  'network/ccmts/ccmts_e_32.png',  'network/ccmts/ccmts_e_48.png',  'network/ccmts/ccmts_e_64.png',  32285);
--INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
--                (30017, 'cmc8800c-e', 'CC8800C-E(弱集中)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.3.3',   'network/ccmts/ccmts_c_e_16.png',  'network/ccmts/ccmts_c_e_32.png',  'network/ccmts/ccmts_c_e_48.png',  'network/ccmts/ccmts_c_e_64.png',  32285);
--INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
--                (30018, 'cmc8800d-e', 'CC8800D-E(弱集中)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.4.1',   'network/ccmts/ccmts_d_e_16.png',  'network/ccmts/ccmts_d_e_32.png',  'network/ccmts/ccmts_d_e_48.png',  'network/ccmts/ccmts_d_e_64.png',  32285);

-- E型大类
--INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
--                (130000, 'cmc8800E', 'CC8800E',  'cmc',  '/cmc',   '',   '',  '',  '',  '',  32285);
                
INSERT INTO batchautodiscoveryentitytype(typeId) values(30013);
INSERT INTO batchautodiscoveryentitytype(typeId) values(30014);
INSERT INTO batchautodiscoveryentitytype(typeId) values(30015);
--INSERT INTO batchautodiscoveryentitytype(typeId) values(30016);
--INSERT INTO batchautodiscoveryentitytype(typeId) values(30017);
--INSERT INTO batchautodiscoveryentitytype(typeId) values(30018);

insert into entitytyperelation select 1, 30013;
insert into entitytyperelation select 30000, 30013;
insert into entitytyperelation select 60000, 30013;
insert into entitytyperelation select 80000, 30013;
insert into entitytyperelation select 30013, 30013;
insert into entitytyperelation select 3, 30013;

insert into entitytyperelation select 1, 30014;
insert into entitytyperelation select 30000, 30014;
insert into entitytyperelation select 60000, 30014;
insert into entitytyperelation select 80000, 30014;
insert into entitytyperelation select 30014, 30014;
insert into entitytyperelation select 3, 30014;

insert into entitytyperelation select 1, 30015;
insert into entitytyperelation select 30000, 30015;
insert into entitytyperelation select 60000, 30015;
insert into entitytyperelation select 80000, 30015;
insert into entitytyperelation select 30015, 30015;
insert into entitytyperelation select 3, 30015;

--insert into entitytyperelation select 1, 30016;
--insert into entitytyperelation select 30000, 30016;
--insert into entitytyperelation select 60000, 30016;
--insert into entitytyperelation select 80000, 30016;
--insert into entitytyperelation select 30016, 30016;
--insert into entitytyperelation select 3, 30016;

--insert into entitytyperelation select 1, 30017;
--insert into entitytyperelation select 30000, 30017;
--insert into entitytyperelation select 60000, 30017;
--insert into entitytyperelation select 80000, 30017;
--insert into entitytyperelation select 30017, 30017;
--insert into entitytyperelation select 3, 30017;

--insert into entitytyperelation select 1, 30018;
--insert into entitytyperelation select 30000, 30018;
--insert into entitytyperelation select 60000, 30018;
--insert into entitytyperelation select 80000, 30018;
--insert into entitytyperelation select 30018, 30018;
--insert into entitytyperelation select 3, 30018;

insert into deviceperftarget VALUES 
			('cmc_onlineStatus', 60000, 30000, 30013, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30013, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30013, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30013, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30013, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30013, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30013, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30013, 'cmc_businessQuality', 5);

insert into deviceperftarget VALUES 
			('cmc_onlineStatus', 60000, 30000, 30014, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30014, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30014, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30014, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30014, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30014, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30014, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30014, 'cmc_businessQuality', 5);
			
insert into deviceperftarget VALUES 
			('cmc_onlineStatus', 60000, 30000, 30015, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30015, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30015, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30015, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30015, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30015, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30015, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30015, 'cmc_businessQuality', 5);
			
--insert into deviceperftarget VALUES 
--			('cmc_onlineStatus', 60000, 30000, 30016, 'cmc_deviceStatus', 1),
--			('cmc_cpuUsed', 60000, 30000, 30016, 'cmc_service', 2),
--			('cmc_memUsed', 60000, 30000, 30016, 'cmc_service', 2),
--			('cmc_flashUsed', 60000, 30000, 30016, 'cmc_service', 2),
--			('cmc_optLink', 60000, 30000, 30016, 'cmc_service', 2),
--			('cmc_moduleTemp', 60000, 30000, 30016, 'cmc_service', 2),
--			('cmc_snr', 60000, 30000, 30016, 'cmc_signalQuality', 3),
--			('cmc_ber', 60000, 30000, 30016, 'cmc_signalQuality', 3),
--			('cmc_upLinkFlow', 60000, 30000, 30016, 'cmc_flow', 4),
--			('cmc_macFlow', 60000, 30000, 30016, 'cmc_flow', 4),
--			('cmc_channelSpeed', 60000, 30000, 30016, 'cmc_flow', 4),
--			('cmc_cmflap', 60000, 30000, 30016, 'cmc_businessQuality', 5),
--			('cmc_opticalReceiver', 60000, 30000, 30016, 'cmc_businessQuality', 5);
			
--insert into deviceperftarget VALUES 
--			('cmc_onlineStatus', 60000, 30000, 30017, 'cmc_deviceStatus', 1),
--			('cmc_cpuUsed', 60000, 30000, 30017, 'cmc_service', 2),
--			('cmc_memUsed', 60000, 30000, 30017, 'cmc_service', 2),
--			('cmc_flashUsed', 60000, 30000, 30017, 'cmc_service', 2),
--			('cmc_optLink', 60000, 30000, 30017, 'cmc_service', 2),
--			('cmc_moduleTemp', 60000, 30000, 30017, 'cmc_service', 2),
--			('cmc_snr', 60000, 30000, 30017, 'cmc_signalQuality', 3),
--			('cmc_ber', 60000, 30000, 30017, 'cmc_signalQuality', 3),
--			('cmc_upLinkFlow', 60000, 30000, 30017, 'cmc_flow', 4),
--			('cmc_macFlow', 60000, 30000, 30017, 'cmc_flow', 4),
--			('cmc_channelSpeed', 60000, 30000, 30017, 'cmc_flow', 4),
--			('cmc_cmflap', 60000, 30000, 30017, 'cmc_businessQuality', 5),
--			('cmc_opticalReceiver', 60000, 30000, 30017, 'cmc_businessQuality', 5);
			
--insert into deviceperftarget VALUES 
--			('cmc_onlineStatus', 60000, 30000, 30018, 'cmc_deviceStatus', 1),
--			('cmc_cpuUsed', 60000, 30000, 30018, 'cmc_service', 2),
--			('cmc_memUsed', 60000, 30000, 30018, 'cmc_service', 2),
--			('cmc_flashUsed', 60000, 30000, 30018, 'cmc_service', 2),
--			('cmc_optLink', 60000, 30000, 30018, 'cmc_service', 2),
--			('cmc_moduleTemp', 60000, 30000, 30018, 'cmc_service', 2),
--			('cmc_snr', 60000, 30000, 30018, 'cmc_signalQuality', 3),
--			('cmc_ber', 60000, 30000, 30018, 'cmc_signalQuality', 3),
--			('cmc_upLinkFlow', 60000, 30000, 30018, 'cmc_flow', 4),
--			('cmc_macFlow', 60000, 30000, 30018, 'cmc_flow', 4),
--			('cmc_channelSpeed', 60000, 30000, 30018, 'cmc_flow', 4),
--			('cmc_cmflap', 60000, 30000, 30018, 'cmc_businessQuality', 5),
--			('cmc_opticalReceiver', 60000, 30000, 30018, 'cmc_businessQuality', 5);
			
/* -- version 2.4.8.1,build 2015-3-17,module cmc */
			
-- version 2.4.8.1,build 2015-3-20,module cmc
update EntityType set standard = 2 where typeId in (30013 , 30014, 30015);
--update EntityType set standard = 3 where typeId in (30016 , 30017, 30018);
/* -- version 2.4.8.1,build 2015-3-20,module cmc */	

-- version 2.4.8.1,build 2015-3-24,module cmc
UPDATE userCustomlization SET icon = 'icoG13' WHERE userId = 2 and functionName = 'WorkBench.cmcpe';
/* -- version 2.4.8.1,build 2015-3-24,module cmc */

-- version 2.4.10.0,build 2015-4-13,module cmc
delete from  deviceperftarget where perfTargetName = 'cmc_upLinkFlow' and typeId = 30013;
delete from  deviceperftarget where perfTargetName = 'cmc_upLinkFlow' and typeId = 30014;
delete from  deviceperftarget where perfTargetName = 'cmc_upLinkFlow' and typeId = 30015;
/* -- version 2.4.10.0,build 2015-4-13,module cmc */

-- version 2.5.1.0,build 2015-3-27,module cmc
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('cmReadCommunity',   'public',  'cmcTerminal'),
                ('cmWriteCommunity',   'private',  'cmcTerminal');
/* -- version 2.5.1.0,build 2015-3-27,module cmc */
                
-- version 2.5.2.0,build 2015-4-16,module cmc              
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('cmHistorySavePolicy',   '2592000000',  'cmcTerminal');
INSERT INTO systempreferences(name,  value,  module) VALUES
                ('RemoteQueryCmMode',   '1',  'RemoteQuery');       
/* -- version 2.5.2.0,build 2015-4-16,module cmc */       
                
-- version 2.5.2.0,build 2015-4-28,module cmc
insert into FunctionItem(functionId,name,superiorId,displayName) values
        (5000021,'cmDailyNumStatic',5000000,'rolePower.cmDailyNumStatic');
insert into rolefunctionrela values(2,5000021);

insert into FunctionItem(functionId,name,superiorId,displayName) values
        (5000022,'dispersion',5000000,'rolePower.dispersion');
insert into rolefunctionrela values(2,5000022);
/* -- version 2.5.2.0,build 2015-4-28,module cmc */

-- version 2.5.3.1,build 2015-05-18,module cmc
insert into entitytyperelation (type, typeId) values (130000,30010);
insert into entitytyperelation (type, typeId) values (130000,30011);
insert into entitytyperelation (type, typeId) values (130000,30012);
insert into entitytyperelation (type, typeId) values (130000,30013);
insert into entitytyperelation (type, typeId) values (130000,30014);
insert into entitytyperelation (type, typeId) values (130000,30015);
/* -- version 2.5.3.1,build 2015-05-18,module cmc */

-- version 2.4.10.0,build 2015-5-12,module cmc
delete from  EventType where typeId = 116393;
delete from  AlertType where typeId = 116393;
delete from  CmcEventTypeRelation where deviceEventTypeId = 116393;
delete from  Event2Alert where eventTypeId = 116393;
/* -- version 2.4.10.0,build 2015-5-12,module cmc */

-- version 2.4.11.0,build 2015-06-04,module cmc
delete from deviceperftarget where entityType = 30000;
insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 
			('cmc_cpuUsed', 60000, 30000, 30001, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30001, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30001, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30001, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30001, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30001, 'cmc_signalQuality', 3),
			('cmc_macFlow', 60000, 30000, 30001, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30001, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30001, 'cmc_businessQuality', 5),
			
			('cmc_onlineStatus', 60000, 30000, 30002, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30002, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30002, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30002, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30002, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30002, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30002, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30002, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30002, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30002, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30002, 'cmc_businessQuality', 5),
			
			('cmc_onlineStatus', 60000, 30000, 30004, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30004, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30004, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30004, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30004, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30004, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30004, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30004, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30004, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30004, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30004, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30004, 'cmc_businessQuality', 5),
			
			('cmc_cpuUsed', 60000, 30000, 30005, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30005, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30005, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30005, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30005, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30005, 'cmc_signalQuality', 3),
			('cmc_macFlow', 60000, 30000, 30005, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30005, 'cmc_flow', 4),
			('cmc_opticalReceiver', 60000, 30000, 30005, 'cmc_businessQuality', 5),
			('cmc_cmflap', 60000, 30000, 30005, 'cmc_businessQuality', 5),
			('cmc_moduleTemp', 60000, 30000, 30005, 'cmc_service', 2),
			
			('cmc_onlineStatus', 60000, 30000, 30006, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30006, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30006, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30006, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30006, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30006, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30006, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30006, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30006, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30006, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30006, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30006, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30006, 'cmc_businessQuality', 5),
			
			('cmc_onlineStatus', 60000, 30000, 30007, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30007, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30007, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30007, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30007, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30007, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30007, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30007, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30007, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30007, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30007, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30007, 'cmc_businessQuality', 5),
			
			('cmc_onlineStatus', 60000, 30000, 30010, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30010, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30010, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30010, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30010, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30010, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30010, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30010, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30010, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30010, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30010, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30010, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30010, 'cmc_businessQuality', 5),
			
			('cmc_onlineStatus', 60000, 30000, 30011, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30011, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30011, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30011, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30011, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30011, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30011, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30011, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30011, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30011, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30011, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30011, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30011, 'cmc_businessQuality', 5),
			
			('cmc_onlineStatus', 60000, 30000, 30012, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30012, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30012, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30012, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30012, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30012, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30012, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30012, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30012, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30012, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30012, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30012, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30012, 'cmc_businessQuality', 5),		
			
			('cmc_cpuUsed', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30013, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30013, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30013, 'cmc_signalQuality', 3),
			('cmc_macFlow', 60000, 30000, 30013, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30013, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30013, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30013, 'cmc_businessQuality', 5),

			('cmc_cpuUsed', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30014, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30014, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30014, 'cmc_signalQuality', 3),
			('cmc_macFlow', 60000, 30000, 30014, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30014, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30014, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30014, 'cmc_businessQuality', 5),
			
			('cmc_cpuUsed', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30015, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30015, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30015, 'cmc_signalQuality', 3),
			('cmc_macFlow', 60000, 30000, 30015, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30015, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30015, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30015, 'cmc_businessQuality', 5);
			
delete from globalperfcollecttime where entityType = 30000;
INSERT INTO globalperfcollecttime(perfTargetName, parentType, entityType, globalInterval, globalEnable, targetGroup, groupPriority) VALUES
			('cmc_onlineStatus', 60000, 30000, 5, 1, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 15, 1, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 15, 1, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 15, 1, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 15, 1, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 15, 1, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 15, 1, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 15, 1, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30, 1, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30, 1, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30, 1, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 240, 1, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 5, 1, 'cmc_businessQuality', 5);

UPDATE cmattribute SET displaystatus=1 WHERE statusvalue=6;
UPDATE cmattribute SET displayIp=StatusIpAddress WHERE StatusIpAddress IS NOT NULL;
UPDATE cmattribute SET displayIp=StatusInetAddress WHERE StatusIpAddress IS NULL AND StatusInetAddress IS NOT NULL;
/*-- version 2.4.11.0,build 2015-6-4,module cmc*/

-- version 2.6.0.0,build 2015-06-08,module cmc
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId) VALUES
                (30020, 'cmc8800c-10g', 'CC8800C-10G(独立)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.3.4',   'network/ccmts/ccmts_c10g_16.png',  'network/ccmts/ccmts_c10g_32.png',  'network/ccmts/ccmts_c10g_48.png',  'network/ccmts/ccmts_c10g_64.png',  32285);
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId, standard) VALUES
                (30021, 'cmc8800c-10g', 'CC8800C-10G(强集中)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.3.4',   'network/ccmts/ccmts_c10g_16.png',  'network/ccmts/ccmts_c10g_32.png',  'network/ccmts/ccmts_c10g_48.png',  'network/ccmts/ccmts_c10g_64.png',  32285, 2);

                
insert into entitytyperelation select 1, 30020;
insert into entitytyperelation select 30000, 30020;
insert into entitytyperelation select 50000, 30020;
insert into entitytyperelation select 60000, 30020;
insert into entitytyperelation select 70000, 30020;
insert into entitytyperelation select 30010, 30020;
insert into entitytyperelation select 3, 30020;

insert into entitytyperelation select 1, 30021;
insert into entitytyperelation select 30000, 30021;
insert into entitytyperelation select 60000, 30021;
insert into entitytyperelation select 80000, 30021;
insert into entitytyperelation select 30021, 30021;
insert into entitytyperelation select 3, 30021;

INSERT INTO batchautodiscoveryentitytype(typeId) values(30020);

insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 
			('cmc_onlineStatus', 60000, 30000, 30020, 'cmc_deviceStatus', 1),
			('cmc_cpuUsed', 60000, 30000, 30020, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30020, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30020, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30020, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30020, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30020, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30020, 'cmc_signalQuality', 3),
			('cmc_upLinkFlow', 60000, 30000, 30020, 'cmc_flow', 4),
			('cmc_macFlow', 60000, 30000, 30020, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30020, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30020, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30020, 'cmc_businessQuality', 5);
			
insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 	
			('cmc_cpuUsed', 60000, 30000, 30021, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30021, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30021, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30021, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30021, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30021, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30021, 'cmc_signalQuality', 3),
			('cmc_macFlow', 60000, 30000, 30021, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30021, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30021, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30021, 'cmc_businessQuality', 5);
/* -- version 2.6.0.0,build 2015-06-08,module cmc */

-- version 2.6.0.0,build 2015-6-19 14:54,module cmc
update cmceventtyperelation set deviceEventTypeId = 1041 where emsEventTypeId = 101004;
update cmceventtyperelation set deviceEventTypeId = 1043 where emsEventTypeId = 101006;
insert into event2alert(eventTypeId,alertTypeId,type) values(101006,101004,0);
/* -- version 2.6.0.0,build 2015-6-19 14:54,module cmc */


-- version 2.6.0.0,build 2015-06-23,module cmc
update entitytype set discoveryBean='cmc8800aDiscoveryService' where typeId in (30001,30005);
update entitytype set discoveryBean='cmc8800bDiscoveryService' where typeId in (30002,30004,30006,30007,30010,30011,30012,30020);
/* -- version 2.6.0.0,build 2015-06-23,module cmc */

-- version 2.6.1.1,build 2015-07-06,module cmc
update entitytype set discoveryBean='cmc8800aDiscoveryService' where typeId in (30013,30014,30015,30021);
insert into entityTypeRelation(type,typeId) values(130000,30020);
insert into entityTypeRelation(type,typeId) values(130000,30021);

/* -- version 2.6.1.1,build 2015-07-06,module cmc */

-- version 2.6.2.0,build 2015-07-25,module cmc
/** add by Victor@2015-07-25解决V2.4升级过来的问题，删除CM的阈值告警 */
delete from AlertType where typeId = -50003 OR category = -50003;
delete from EventType where typeId = -50003 OR parentId = -50003;
delete from FunctionItem where functionId = 9000000 OR superiorId = 9000000;
delete from NavigationButton where naviId = 9000000;
delete from Event2Alert where alertTypeId = -501;
delete from Event2Alert where alertTypeId = -503;
delete from Event2Alert where alertTypeId = -504;
delete from Event2Alert where alertTypeId = -505;
delete from Event2Alert where alertTypeId = -506;
delete from Event2Alert where alertTypeId = -507;
delete from Event2Alert where alertTypeId = -508;
delete from Event2Alert where alertTypeId = -510;
/* -- version 2.6.2.0,build 2015-07-25,module cmc */

-- version 2.6.3.0,build 2015-08-06,module cmc
/** add by loyal@2015-08-06 解决设备类型插入错误导致根据设备类型查询时出错问题*/
delete from entitytyperelation where type = 30010 and typeId = 30020;
insert into entitytyperelation select 30020, 30020;
/* -- version 2.6.3.0,build 2015-08-06,module cmc */

-- version 2.6.8.0,build 2016-02-22,module cmc
update alerttype set category = -8 where typeId = 200001;
update alerttype set category = -8 where typeId = 200002;
/* -- version 2.6.8.0,build 2016-02-22,module cmc */

-- version 2.6.8.0,build 2016-2-22,module cmc
update cmceventtyperelation set deviceEventTypeId = 8641 where emsEventTypeId = 101004;
update cmceventtyperelation set deviceEventTypeId = 8643 where emsEventTypeId = 101006;
/* -- version 2.6.8.0,build 2016-2-22,module cmc */

-- version 2.6.8.3,build 2016-3-24,module cmc
INSERT INTO CmcEventTypeRelation(deviceEventTypeId, emsEventTypeId) VALUES
        (16433, 1016433),
        (16435, 1016435),
        (16434, 1016434),
        (16393, 101003);
INSERT INTO EventType(typeId, parentId, name, displayName) VALUES
        (1016433,  -8,  'CMTS_OFFLINE',  'DB.eventType.e1016433'),
        (1016434,  -8,  'CMTS_ONLINE',  'DB.eventType.e1016434'),
        (1016435,  -8,  'CMTS_FIBER_BREAK',  'DB.eventType.e1016435');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES 
	(1016434,1016433,0),
	(1016433,1016433,1),
	(1016434,1016435,0),
	(1016435,1016435,1),
	(1016434,101003,0);
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
	(1016433,-8,'CMTS_OFFLINE','DB.alertType.a1016433',3,'',0,0,'0','1',NULL,NULL),
	(1016435,-8,'CMTS_FIBER_BREAK','DB.alertType.a1016435',3,'',0,0,'0','1',NULL,NULL);
/* -- version 2.6.8.3,build 2016-3-24,module cmc */

-- version 2.6.8.3,build 2016-3-25,module cmc
insert into cmceventtyperelation values(1036, 101036);
insert into cmceventtyperelation values(1035, 101035);
INSERT INTO eventType(typeId,parentId,name,displayName,note) VALUES
 				(101036,-8,'CCMTS_SPECTRUM_HOP','DB.eventType.e101036',''),
  				(101035,-8,'CCMTS_SPECTRUM_HOP_RECOVERY','DB.eventType.e101035','');
INSERT INTO AlertType(typeId,  category,  name,  displayName,  levelId,  alertTimes, smartUpdate,  terminate,  threshold,  active,  oid,  note) VALUES
                (101036,-8,'CCMTS_SPECTRUM_HOP','DB.alertType.a101036',5,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES	
				(101036,101036,1),
				(101035,101036,0);
/* -- version 2.6.8.3,build 2016-3-25,module cmc */

-- version 2.6.8.0,build 2016-3-27,module cmc
INSERT INTO nbiperfgroup VALUES (7, 'PER_CCMTS_SERVICE','CMTS服务质量','cmc');
INSERT INTO nbiperfgroup VALUES (8, 'PER_CCMTS_TEMP','CMTS温度','cmc');
INSERT INTO nbiperfgroup VALUES (9, 'PER_CCMTS_OPT','CMTS光链路信息','cmc');
INSERT INTO nbiperfgroup VALUES (10, 'PER_CCMTS_SIGNAL','CMTS信号质量','cmc');
INSERT INTO nbiperfgroup VALUES (11, 'PER_CCMTS_OPTRE','CMTS光机接收功率','cmc');
INSERT INTO nbiperfgroup VALUES (12, 'PER_CCMTS_CHANNEL','CMTS信道速率','cmc');
INSERT INTO nbiperfgroup VALUES (13, 'PER_CCMTS_DEVICEL','CMTS设备速率','cmc');
INSERT INTO nbiperfgroup VALUES (14, 'PER_CCMTS_CM','CM数量','cmc');
INSERT INTO nbiperfgroupindex VALUES (7, 20, '1.3.6.1.4.1.32285.11.1.1.2.1.1.1.11', 'topCcmtsSysCPURatio', 'CPU利用率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (7, 21, '1.3.6.1.4.1.32285.11.1.1.2.1.1.1.10', 'topCcmtsSysRAMRatio', '内存利用率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (7, 22, '1.3.6.1.4.1.32285.11.1.1.2.1.1.1.13', 'topCcmtsSysFlashRatio', 'Flash利用率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (8, 23, '1.3.6.1.4.1.32285.11.1.1.2.1.19.1.1', 'usModuleTemp', '上行放大模块温度', 5,1);
INSERT INTO nbiperfgroupindex VALUES (8, 24, '1.3.6.1.4.1.32285.11.1.1.2.1.19.1.2', 'dsModuleTemp', '下行放大模块温度', 5,1);
INSERT INTO nbiperfgroupindex VALUES (8, 25, '1.3.6.1.4.1.32285.11.1.1.2.1.19.1.3', 'outsideTemp', 'DOCSIC  MAC温度', 5,1);
INSERT INTO nbiperfgroupindex VALUES (9, 26, '1.3.6.1.4.1.32285.12.2.3.9.6.1.1', 'optTxPower', '光口发送功率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (9, 27, '1.3.6.1.4.1.32285.12.2.3.9.6.1.2', 'optRePower', '光口接收功率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (9, 28, '1.3.6.1.4.1.32285.12.2.3.9.6.1.3', 'optVoltage', '光口电压', 5,1);
INSERT INTO nbiperfgroupindex VALUES (9, 29, '1.3.6.1.4.1.32285.12.2.3.9.6.1.4', 'optTemp', '光口温度', 5,1);
INSERT INTO nbiperfgroupindex VALUES (9, 30, '1.3.6.1.4.1.32285.12.2.3.9.6.1.5', 'optCurrent', '光口偏置电流', 5,1);
INSERT INTO nbiperfgroupindex VALUES (10, 31, '1.3.6.1.4.1.32285.12.2.3.9.7.1.3', 'SingleNoise:noise', '信噪比', 5,1);
INSERT INTO nbiperfgroupindex VALUES (10, 32, '1.3.6.1.4.1.32285.12.2.3.9.7.1.1', 'UsBitErrorRate:bitErrorRate', '可纠错码率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (10, 33, '1.3.6.1.4.1.32285.12.2.3.9.7.1.2', 'UsBitErrorRate:unBitErrorRate', '不可纠错码率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (11, 34, '1.3.6.1.4.1.32285.11.1.1.2.21.3.1.2', 'CmcOpReceiverInputInfo:inputPower', '光机接收功率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (12, 35, '1.3.6.1.4.1.32285.12.2.3.9.8.1.1', 'CmcFlowQuality.ifChannelSpeed', '信道速率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (12, 36, '1.3.6.1.4.1.32285.12.2.3.9.8.1.2', 'cmcFlowQuality.ifUtilization', '信道利用率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (13, 37, '1.3.6.1.4.1.32285.12.2.3.9.9.1.1', 'CmcFlowQuality.ifInSpeed', 'MAC域入方向速率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (13, 38, '1.3.6.1.4.1.32285.12.2.3.9.9.1.2', 'cmcifOutSpeed', 'MAC域出方向速率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (13, 39, '1.3.6.1.4.1.32285.12.2.3.9.9.1.3', 'cmcMacDomainUsed', 'MAC域利用率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (13, 40, '1.3.6.1.4.1.32285.12.2.3.9.9.1.4', 'cmcIfInSpeed', '上联口入方向速率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (13, 41, '1.3.6.1.4.1.32285.12.2.3.9.9.1.5', 'cmcIfOutSpeed', '上联口出方向速率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (13, 42, '1.3.6.1.4.1.32285.12.2.3.9.9.1.6', 'cmcifInUsed', '上联口入方向利用率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (13, 43, '1.3.6.1.4.1.32285.12.2.3.9.9.1.7', 'cmcifOutUsed', '上联口出方向利用率', 5,1);
INSERT INTO nbiperfgroupindex VALUES (14, 44, '1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.2', 'cmNumOnline', '在线CM数', 5,0);
INSERT INTO nbiperfgroupindex VALUES (14, 45, '1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.5', 'cmNumOffline', '离线CM数', 5,0);
INSERT INTO nbiperfgroupindex VALUES (14, 46, '1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.1', 'cmNumTotal', 'CM总数', 5,0);
INSERT INTO nbiperfgroupindex VALUES (14, 47, '1.3.6.1.4.1.32285.12.2.3.9.11.1.1', 'cmNumOther', '其它CM数', 5,0);
/* -- version 2.6.8.0,build 2016-3-27,module cmc */

-- version 2.6.8.7,build 2016-3-31,module cmc
update perfthresholdrule set thresholds = '1_90_3_4' where targetId = 'CC_FLASHUSED' and templateId = 2 and thresholds = '1_90_3_5';
update perfthresholdrule set thresholds = '1_90_3_4#1_95_4_5' where targetId = 'CC_MEMUSED' and templateId = 2 and thresholds = '1_90_4_5';
update perfthresholdrule set thresholds = '1_80_3_4#1_85_4_5' where targetId = 'CC_CPUUSED' and templateId = 2 and thresholds = '1_90_4_5';
update perfthresholdrule set thresholds = '1_20_3_5', minuteLength = 60,number = 2 where targetId = 'CC_ERRORCODE' and templateId = 2 and thresholds = '1_20_4_5';
update perfthresholdrule set thresholds = '1_1_3_5#1_3_4_4', minuteLength = 60,number = 2 where targetId = 'CC_UNERRORCODE' and templateId = 2 and thresholds = '1_15_3_5#1_30_4_4';
update perfthresholdrule set thresholds = '1_70_3_5#1_85_4_6' where targetId = 'CC_UPLINK_IN_UESD' and templateId = 2 and thresholds = '1_50_2_4#1_70_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '1_70_3_5#1_85_4_6' where targetId = 'CC_MAC_IN_USED' and templateId = 2 and thresholds = '1_50_2_4#1_70_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '1_70_3_5#1_85_4_6' where targetId = 'CC_UPLINK_OUT_UESD' and templateId = 2 and thresholds = '1_50_2_4#1_70_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '1_70_3_5#1_85_4_6' where targetId = 'CC_MAC_OUT_USED' and templateId = 2 and thresholds = '1_50_2_4#1_70_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '5_-40_2_4#1_80_3_5#1_85_4_6' where targetId = 'CC_ONU_MODULE_TEMP' and templateId = 2 and thresholds = '5_0_2_4#1_90_3_5';
update perfthresholdrule set thresholds = '5_-40_2_4#1_80_3_5#1_85_4_6' where targetId = 'CC_PON_MODULE_TEMP' and templateId = 2 and thresholds = '5_0_2_4#1_90_3_5';
update perfthresholdrule set thresholds = '5_-40_2_4#1_80_3_5#1_85_4_6' where targetId = 'CC_US_MODULE_TEMP' and templateId = 2 and thresholds = '5_-40_2_4#1_80_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '5_-40_2_4#1_80_3_5#1_85_4_6' where targetId = 'CC_DS_MODULE_TEMP' and templateId = 2 and thresholds = '5_-40_2_4#1_80_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '5_-40_2_4#1_80_3_5#1_85_4_6' where targetId = 'CC_OUTSIDE_TEMP' and templateId = 2 and thresholds = '5_-40_2_4#1_80_3_5#1_85_4_6';
update perfthresholdrule set thresholds = '5_-9_4_4#1_3_3_5' where targetId = 'CC_PON_MODULE_TX_POWER' and templateId = 2 and thresholds = '5_-9_3_4#1_3_4_5';
delete from  perfthresholdrule  where targetId = 'CC_UTILIZATION' and templateId = 2;
delete from  perfthresholdrule  where targetId = 'CC_NOISE_BPSK' and templateId = 2;
delete from  perfthresholdrule  where targetId = 'CC_NOISE_PSK8' and templateId = 2;
delete from  perfthresholdrule  where targetId = 'CC_NOISE_PSK16' and templateId = 2;
delete from  perfthresholdrule  where targetId = 'CC_NOISE_PSK32' and templateId = 2;
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_QAM8', 60000, 'Performance.ccNoise_QAM8','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_NOISE_QAM128', 60000, 'Performance.ccNoise_QAM128','Performance.signalQuality','dB',50,0,1,1);
INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange) values
	('CC_NOISE_QAM8', 2,'5_16_4_5',1,1,1,'00:00-23:59#1234567'),
	('CC_NOISE_QAM128', 2,'5_30_4_5',1,1,1,'00:00-23:59#1234567');
/* -- version 2.6.8.7,build 2016-3-31,module cmc */

-- version 2.6.8.8,build 2016-4-6,module cmc
update perfthresholdrule set minuteLength = 1,number = 1 where targetId = 'CC_ERRORCODE' and templateId = 2 and minuteLength = 60 and number = 2;
update perfthresholdrule set thresholds = '1_3_3_5#5_-9_4_4' where targetId = 'CC_PON_MODULE_TX_POWER' and templateId = 2 and thresholds = '5_-9_4_4#1_3_3_5';
/* -- version 2.6.8.8,build 2016-4-6,module cmc */

-- version 2.7.1.0,build 2016-6-22,module cmc
delete from perfthresholdrule where targetId = 'CC_ERRORCODE' and templateId = 2;
delete from perfthresholdrule where targetId = 'CC_UNERRORCODE' and templateId = 2;
update perfthresholdrule set thresholds = '1_85_4_5#1_80_3_4' where targetId = 'CC_CPUUSED' and templateId = 2;
update perfthresholdrule set thresholds = '1_95_4_5#1_90_3_4' where targetId = 'CC_MEMUSED' and templateId = 2;
update perfthresholdrule set thresholds = '1_85_4_5#1_70_3_4' where targetId = 'CC_UPLINK_IN_UESD' and templateId = 2;
update perfthresholdrule set thresholds = '1_85_4_5#1_70_3_4' where targetId = 'CC_UPLINK_OUT_UESD' and templateId = 2;
update perfthresholdrule set thresholds = '1_85_4_5#1_70_3_4' where targetId = 'CC_MAC_IN_USED' and templateId = 2;
update perfthresholdrule set thresholds = '1_85_4_5#1_70_3_4' where targetId = 'CC_MAC_OUT_USED' and templateId = 2;
update perfthresholdrule set thresholds = '1_-5_3_5#5_-25_3_4' where targetId = 'CC_ONU_MODULE_RE_POWER' and templateId = 2;
update perfthresholdrule set thresholds = '5_0_3_5#1_4_2_4' where targetId = 'CC_ONU_MODULE_TX_POWER' and templateId = 2;
delete from perfthresholdrule where targetId = 'CC_PON_MODULE_RE_POWER' and templateId = 2;
delete from perfthresholdrule where targetId = 'CC_PON_MODULE_TX_POWER' and templateId = 2;
update perfthresholdrule set thresholds = '1_75_4_5#1_70_3_4' where targetId = 'CC_ONU_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set thresholds = '1_75_4_5#1_70_3_4' where targetId = 'CC_PON_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set thresholds = '1_75_4_5#1_70_3_4' where targetId = 'CC_US_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set thresholds = '1_75_4_5#1_70_3_4' where targetId = 'CC_DS_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set thresholds = '1_75_4_5#1_70_3_4' where targetId = 'CC_OUTSIDE_TEMP' and templateId = 2;
delete from perfthresholdrule where targetId = 'CC_UNUSUAL_CMNUM' and templateId = 2;
delete from perfthresholdrule where targetId = 'CMTS_UNUSUAL_CMNUM' and templateId = 2;

update perfthresholdrule set clearRules = '5_500' where targetId = 'CC_CONNECTIVITY' and templateId = 2;
update perfthresholdrule set clearRules = '5_80' where targetId = 'CC_FLASHUSED' and templateId = 2;
update perfthresholdrule set clearRules = '5_85#5_80' where targetId = 'CC_MEMUSED' and templateId = 2;
update perfthresholdrule set clearRules = '5_75#5_70' where targetId = 'CC_CPUUSED' and templateId = 2;
update perfthresholdrule set clearRules = '1_16' where targetId = 'CC_NOISE_QPSK' and templateId = 2;
update perfthresholdrule set clearRules = '1_18' where targetId = 'CC_NOISE_QAM8' and templateId = 2;
update perfthresholdrule set clearRules = '1_23' where targetId = 'CC_NOISE_QAM16' and templateId = 2;
update perfthresholdrule set clearRules = '1_27' where targetId = 'CC_NOISE_QAM32' and templateId = 2;
update perfthresholdrule set clearRules = '1_29' where targetId = 'CC_NOISE_QAM64' and templateId = 2;
update perfthresholdrule set clearRules = '1_32' where targetId = 'CC_NOISE_QAM128' and templateId = 2;
update perfthresholdrule set clearRules = '1_35' where targetId = 'CC_NOISE_QAM256' and templateId = 2;
update perfthresholdrule set clearRules = '1_13' where targetId = 'CC_NOISE_OTHER' and templateId = 2;

update perfthresholdrule set clearRules = '5_75#5_60' where targetId = 'CC_UPLINK_IN_UESD' and templateId = 2;
update perfthresholdrule set clearRules = '5_75#5_60' where targetId = 'CC_MAC_IN_USED' and templateId = 2;
update perfthresholdrule set clearRules = '5_75#5_60' where targetId = 'CC_UPLINK_OUT_UESD' and templateId = 2;
update perfthresholdrule set clearRules = '5_75#5_60' where targetId = 'CC_MAC_OUT_USED' and templateId = 2;
update perfthresholdrule set clearRules = '5_-6#1_-24' where targetId = 'CC_ONU_MODULE_RE_POWER' and templateId = 2;
update perfthresholdrule set clearRules = '1_0.5#5_3.5' where targetId = 'CC_ONU_MODULE_TX_POWER' and templateId = 2;
update perfthresholdrule set clearRules = '5_70#5_65' where targetId = 'CC_ONU_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set clearRules = '5_70#5_65' where targetId = 'CC_PON_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set clearRules = '5_70#5_65' where targetId = 'CC_US_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set clearRules = '5_70#5_65' where targetId = 'CC_DS_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set clearRules = '5_70#5_65' where targetId = 'CC_OUTSIDE_TEMP' and templateId = 2;

/* -- version 2.7.1.0,build 2016-6-22,module cmc */

-- version 2.7.1.0,build 2016-7-12,module cmc
delete from CmcEventTypeRelation where deviceEventTypeId = 16433;
delete from CmcEventTypeRelation where deviceEventTypeId = 16435;
delete from CmcEventTypeRelation where deviceEventTypeId = 16434;
delete from CmcEventTypeRelation where deviceEventTypeId = 16393;
INSERT INTO CmcEventTypeRelation(deviceEventTypeId, emsEventTypeId) VALUES
        (16433, 1016433),
        (16435, 1016435),
        (16434, 1016434),
        (16393, 101003);
/* -- version 2.7.1.0,build 2016-7-12,module cmc */

-- version 2.7.1.0,build 2016-6-23,module cmc
ALTER TABLE cmattribute add column preStatus int not null default -1;
ALTER TABLE cmattribute add column docsisMode int not null default -1;
/* -- version 2.7.1.0,build 2016-6-23,module cmc */

-- version 2.7.2.0,build 2016-7-22,module cmc
update perfTarget set maxNum = 4  where targetId = 'CC_ONU_MODULE_TX_POWER'; 
/* -- version 2.7.2.0,build 2016-7-22,module cmc */

-- version 2.7.1.0,build 2016-8-1,module cmc
insert into systempreferences values('Ping.cmping',1,'toolPing');
/* -- version 2.7.1.0,build 2016-8-1,module cmc */
 
-- version 2.7.4.0,build 2016-8-15,module cmc
update perftarget set targetDisplayName = 'Performance.optOnuRePower' where targetId = 'CC_ONU_MODULE_RE_POWER';
update perftarget set targetDisplayName = 'Performance.optOnuTemp' where targetId = 'CC_ONU_MODULE_TEMP';
/* -- version 2.7.4.0,build 2016-8-15,module cmc */
-- version 2.7.4.0,build 2016-11-4,module cmc
update entitytype set sysObjectID='' where typeId = 30000;
/* -- version 2.7.4.0,build 2016-11-4,module cmc */

-- version 2.7.4.0,build 2016-9-15,module cmc
update perfthresholdrule set thresholds = '1_85_4_5#1_80_3_4',clearRules = '5_80#5_75' where targetId = 'CC_ONU_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set thresholds = '1_85_4_5#1_80_3_4',clearRules = '5_80#5_75' where targetId = 'CC_PON_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set thresholds = '1_85_4_5#1_80_3_4',clearRules = '5_80#5_75' where targetId = 'CC_US_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set thresholds = '1_85_4_5#1_80_3_4',clearRules = '5_80#5_75' where targetId = 'CC_DS_MODULE_TEMP' and templateId = 2;
update perfthresholdrule set thresholds = '1_85_4_5#1_80_3_4',clearRules = '5_80#5_75' where targetId = 'CC_OUTSIDE_TEMP' and templateId = 2;

update perfthresholdrule set minuteLength = 60,number = 3 where targetId = 'CC_NOISE_QPSK' and templateId = 2;
update perfthresholdrule set minuteLength = 60,number = 3 where targetId = 'CC_NOISE_QAM8' and templateId = 2;
update perfthresholdrule set minuteLength = 60,number = 3 where targetId = 'CC_NOISE_QAM16' and templateId = 2;
update perfthresholdrule set minuteLength = 60,number = 3 where targetId = 'CC_NOISE_QAM32' and templateId = 2;
update perfthresholdrule set minuteLength = 60,number = 3 where targetId = 'CC_NOISE_QAM64' and templateId = 2;
update perfthresholdrule set minuteLength = 60,number = 3 where targetId = 'CC_NOISE_QAM128' and templateId = 2;
update perfthresholdrule set minuteLength = 60,number = 3 where targetId = 'CC_NOISE_QAM256' and templateId = 2;
update perfthresholdrule set minuteLength = 60,number = 3 where targetId = 'CC_NOISE_OTHER' and templateId = 2;
/* -- version 2.7.4.0,build 2016-9-15,module cmc */


-- version 2.7.5.0,build 2016-10-11,module cmc
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId,discoveryBean) VALUES
                (30022, 'cmc8800f', 'CC8800F(独立)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.7',   'network/ccmts/ccmts_c_e_16.png',  
                'network/ccmts/ccmts_c_e_32.png',  'network/ccmts/ccmts_c_e_48.png',  'network/ccmts/ccmts_c_e_64.png',  32285,'cmc8800bDiscoveryService');
INSERT INTO batchautodiscoveryentitytype(typeId) values(30022);

insert into entitytyperelation select 1, 30022;
insert into entitytyperelation select 30000, 30022;
insert into entitytyperelation select 50000, 30022;
insert into entitytyperelation select 60000, 30022;
insert into entitytyperelation select 70000, 30022;
insert into entitytyperelation select 30011, 30022;
insert into entitytyperelation select 3, 30022;
insert into entitytyperelation select 130000,30011;

insert into deviceperftarget VALUES 
            ('cmc_onlineStatus', 60000, 30000, 30022, 'cmc_deviceStatus', 1),
            ('cmc_cpuUsed', 60000, 30000, 30022, 'cmc_service', 2),
            ('cmc_memUsed', 60000, 30000, 30022, 'cmc_service', 2),
            ('cmc_flashUsed', 60000, 30000, 30022, 'cmc_service', 2),
            ('cmc_optLink', 60000, 30000, 30022, 'cmc_service', 2),
            ('cmc_moduleTemp', 60000, 30000, 30022, 'cmc_service', 2),
            ('cmc_snr', 60000, 30000, 30022, 'cmc_signalQuality', 3),
            ('cmc_ber', 60000, 30000, 30022, 'cmc_signalQuality', 3),
            ('cmc_upLinkFlow', 60000, 30000, 30022, 'cmc_flow', 4),
            ('cmc_macFlow', 60000, 30000, 30022, 'cmc_flow', 4),
            ('cmc_channelSpeed', 60000, 30000, 30022, 'cmc_flow', 4),
            ('cmc_cmflap', 60000, 30000, 30022, 'cmc_businessQuality', 5),
            ('cmc_opticalReceiver', 60000, 30000, 30022, 'cmc_businessQuality', 5);
            
INSERT INTO EntityType(typeId,  name,  displayName,  module,  modulePath,  sysObjectID,  icon16,  icon32,  icon48,  icon64,  corpId, standard,discoveryBean) VALUES
                (30023, 'cmc8800f', 'CC8800F(强集中)',  'cmc',  '/cmc',   '1.3.6.1.4.1.32285.11.1.1.1.7',   'network/ccmts/ccmts_c_e_16.png',  'network/ccmts/ccmts_c_e_32.png',  
                'network/ccmts/ccmts_c_e_48.png',  'network/ccmts/ccmts_c_e_64.png',  32285, 2,'cmc8800aDiscoveryService');

insert into entitytyperelation select 1, 30023;
insert into entitytyperelation select 30000, 30023;
insert into entitytyperelation select 60000, 30023;
insert into entitytyperelation select 80000, 30023;
insert into entitytyperelation select 30021, 30023;
insert into entitytyperelation select 3, 30023;
			
insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 	
			('cmc_cpuUsed', 60000, 30000, 30023, 'cmc_service', 2),
			('cmc_memUsed', 60000, 30000, 30023, 'cmc_service', 2),
			('cmc_flashUsed', 60000, 30000, 30023, 'cmc_service', 2),
			('cmc_optLink', 60000, 30000, 30023, 'cmc_service', 2),
			('cmc_moduleTemp', 60000, 30000, 30023, 'cmc_service', 2),
			('cmc_snr', 60000, 30000, 30023, 'cmc_signalQuality', 3),
			('cmc_ber', 60000, 30000, 30023, 'cmc_signalQuality', 3),
			('cmc_macFlow', 60000, 30000, 30023, 'cmc_flow', 4),
			('cmc_channelSpeed', 60000, 30000, 30023, 'cmc_flow', 4),
			('cmc_cmflap', 60000, 30000, 30023, 'cmc_businessQuality', 5),
			('cmc_opticalReceiver', 60000, 30000, 30023, 'cmc_businessQuality', 5);
/* -- version 2.7.5.0,build 2016-10-11,module cmc */

-- version 2.8.0.0,build 2016-10-13,module cmc
delete from CmcEventTypeRelation where deviceEventTypeId in(1019,1020,1022);
INSERT INTO CmcEventTypeRelation(deviceEventTypeId, emsEventTypeId) VALUES(7232, 101019);

delete from alerttype where typeId in (101020,101022);
delete from event2alert where eventTypeId in (101020,101022);
/* -- version 2.8.0.0,build 2016-10-13,module cmc */

-- version 2.8.0.0,build 2016-10-20,module cmc
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CM_FLAP_INS', 60000, 'Performance.cmFlapIns','Performance.deviceServiceQuality','', 10000, 0, 1, 0);
/* -- version 2.8.0.0,build 2016-10-20,module cmc */

-- version 2.8.0.0,build 2016-11-23,module cmc
update event2alert set alertTypeId = -848 where eventTypeId = -849;
/* -- version 2.8.0.0,build 2016-11-23,module cmc */

-- version 2.8.0.0,build 2016-12-1,module cmc
delete from AlertType where typeId = 101027;
delete from eventType where typeId = 101027;
delete from event2alert where eventTypeId = 101027;
delete from cmceventtyperelation where emsEventTypeId = 101027;

delete from AlertType where typeId = 101028;
delete from eventType where typeId = 101028;
delete from event2alert where eventTypeId = 101028;
delete from cmceventtyperelation where emsEventTypeId = 101028;

delete from AlertType where typeId = 101019;
delete from eventType where typeId = 101019;
delete from event2alert where eventTypeId = 101019;
delete from cmceventtyperelation where emsEventTypeId = 101019;

delete from AlertType where typeId = 104008;
delete from eventType where typeId = 104008;
delete from event2alert where eventTypeId = 104008;
delete from cmceventtyperelation where emsEventTypeId = 104008;

delete from AlertType where typeId = 11181;
delete from eventType where typeId = 11181;
delete from event2alert where eventTypeId = 11181;
delete from cmceventtyperelation where emsEventTypeId = 11181;
/* -- version 2.8.0.0,build 2016-12-1,module cmc */

-- version 2.8.0.1,build 2016-11-21 14:12:00,module server
INSERT INTO CmcEventTypeRelation(deviceEventTypeId, emsEventTypeId) VALUES(4263317513, 1904649);
INSERT INTO CmcEventTypeRelation(deviceEventTypeId, emsEventTypeId) VALUES(4263317514, 1904650);
/* -- version 2.8.0.1,build 2016-11-21 14:12:00,module server  */

-- version 2.8.0.1,build 2017-1-16, module cmc
INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange,clearRules) values
	('CC_UTILIZATION',2,'1_90_4_4',1,1,1,'00:00-23:59#1234567','5_75');
/* -- version 2.8.0.1,build 2017-1-16, module cmc  */

/* EMS-13745 删除线卡下线告警*/
-- version 2.9.0.0,build 2017-1-18, module cmc
delete from CmcEventTypeRelation where emsEventTypeId=101023;
delete from CmcEventTypeRelation where emsEventTypeId=101024;
delete from Event2Alert where alertTypeId=101024;
delete from EventType where typeId=101023;
delete from EventType where typeId=101024;
delete from AlertType where typeId=101024;
/* -- version 2.9.0.0,build 2017-1-18, module cmc  */

-- version 2.9.0.0,build 2017-2-6, module cmc
insert into systempreferences(name,value,module) values('cmtsFlapClearPeriod','7','autoClear');

insert into FunctionItem(functionId,name,superiorId,displayName) values
        (8000032,'cmcFlapClear',8000000,'rolePower.cmcFlapClear');
insert into rolefunctionrela values(2,8000032);
insert into rolefunctionrela values(1,8000032);
/* -- version 2.9.0.0,build 2017-2-6, module cmc  */

-- version 2.9.0.0,build 2017-2-23 11:16,module cmc
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES    
                (101002,101025,0);
/* -- version 2.9.0.0,build 2017-2-23 11:16,module cmc */

-- version 2.9.0.2,build 2017-04-19,module cmc
insert into entitytyperelation (type, typeId) values (130000,30022);
insert into entitytyperelation (type, typeId) values (130000,30023);
/* -- version 2.9.0.2,build 2017-04-19,module cmc */

-- version 2.9.0.4,build 2017-04-27 10:23,module cmc
update EntityType set icon16='network/ccmts/ccmts_f_16.png',icon32='network/ccmts/ccmts_f_32.png',icon48='network/ccmts/ccmts_f_48.png',icon64='network/ccmts/ccmts_f_64.png' where typeId=30022;
/* -- version 2.9.0.4,build 2017-04-27 10:23,module cmc */
-- version 2.8.1.4,build 2017-6-23,module cmc
INSERT INTO CmcEventTypeRelation(deviceEventTypeId, emsEventTypeId) VALUES
        (16439, 1016439);
/* -- version 2.8.1.4,build 2017-6-23,module cmc */

        
-- version 2.9.1.7,build 2017-08-10 17:30:00,module cmc
insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 
            ('cmc_ccupFlow', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30001, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30001, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30002, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30002, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30004, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30004, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30005, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30005, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30006, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30006, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30007, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30007, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30010, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30010, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30011, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30011, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30012, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30012, 'cmc_netInfo', 6),    
            
            ('cmc_ccupFlow', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30013, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30013, 'cmc_netInfo', 6),

            ('cmc_ccupFlow', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30014, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30014, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30015, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30015, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30020, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30020, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30021, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30021, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30022, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30022, 'cmc_netInfo', 6),
            
            ('cmc_ccupFlow', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_ccDownFlow', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_cmReAvg', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_cmReNotInRange', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_cmTxAvg', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_cmTxNotInRange', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_downSnrAvg', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_downSnrNotInRange', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_onlineRatio', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_upSnrAvg', 60000, 30000, 30023, 'cmc_netInfo', 6),
            ('cmc_upSnrNotInRange', 60000, 30000, 30023, 'cmc_netInfo', 6);
            
INSERT INTO cmtsinfothreshold(upSnrMin, upSnrMax,downSnrMin,downSnrMax,upPowerMin,upPowerMax,downPowerMin,downPowerMax) VALUES
        (30,null,33,null,40,50,-5,5);
/*-- version 2.9.1.7,build 2017-08-10 17:30:00,module cmc*/
        
-- version 2.9.1.8,build 2017-08-21,module cmc
delete from deviceperftarget where perfTargetName='cmc_ccDownFlow' and groupPriority=6;

insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES  
('cmc_cmDownFlow', 60000, 30000, 30001, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30002, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30004, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30005, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30006, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30007, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30010, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30011, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30012, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30013, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30014, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30015, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30020, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30021, 'cmc_netInfo', 6),
('cmc_cmDownFlow', 60000, 30000, 30022, 'cmc_netInfo', 6),  
('cmc_cmDownFlow', 60000, 30000, 30023, 'cmc_netInfo', 6);
/*-- version 2.9.1.8,build 2017-08-21,module cmc*/

-- version 2.9.1.8,build 2017-09-20,module cmc
INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
    (100303,-8,'Device Replace','EVENT.deviceReplace','');
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
    (100303,-8,'Device Replace','ALERT.deviceReplace',3,'',0,0,'1','1',NULL,NULL);
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
    (100303,100303,1);
/*-- version 2.9.1.8,build 2017-09-20,module cmc*/
    
-- version 2.9.1.14,build 2017-10-20,module cmc
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_DOROPT_TEMP', 60000, 'Performance.dorTemp','Performance.cmc_businessQuality','℃',120,-40,1,0);
INSERT INTO perfTarget(targetId, targetType, targetDisplayName, targetGroup, unit, maxNum, minNum, enableStatus, regexpValue) values('CC_DOR_VOLTAGE', 60000, 'Performance.dorVoltage','Performance.cmc_businessQuality','V',300,0,1,0);
insert into deviceperftarget(perfTargetName, parentType, entityType, typeId, targetGroup, groupPriority) VALUES 
('cmc_dorLinePower', 60000, 30000, 30005, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30006, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30010, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30011, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30012, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30013, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30014, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30015, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30020, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30021, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30022, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 30023, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30005, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30006, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30010, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30011, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30012, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30013, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30014, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30015, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30020, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30021, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30022, 'cmc_businessQuality', 5),
('cmc_dorOptTemp', 60000, 30000, 30023, 'cmc_businessQuality', 5);

INSERT INTO globalperfcollecttime(perfTargetName, parentType, entityType, globalInterval, globalEnable, targetGroup, groupPriority) VALUES
('cmc_dorOptTemp', 60000, 30000, 15, 1, 'cmc_businessQuality', 5),
('cmc_dorLinePower', 60000, 30000, 15, 1, 'cmc_businessQuality', 5);

INSERT INTO perfthresholdrule (targetId,templateId,thresholds,minuteLength,number,isTimeLimit,timeRange, clearRules) values
('CC_DOROPT_TEMP',2,'1_85_4_5#1_80_3_4',1,1,1,'00:00-23:59#1234567', '5_80#5_75');

INSERT INTO EventType(typeId,parentId,name,displayName,note) VALUES
(-854,-50002,'CCMTS_DorOpt_TempEvent','DB.eventType.e854',''),
(-855,-50002,'CCMTS_DorOpt_TempEvent','DB.eventType.e855','');
INSERT INTO AlertType(typeId,category,name,displayName,levelId,alertTimes,smartUpdate,terminate,threshold,active,oid,note) VALUES
(-854,-50002,'CCMTS_DorOpt_TempEvent','DB.alertType.a854',5,'',0,  0,  '0',  '1',  '',  '');
INSERT INTO Event2Alert(eventTypeId,alertTypeId,type) VALUES
(-854,-854,1),
(-855,-854,0);
/*-- version 2.9.1.14,build 2017-10-24,module cmc*/

-- version 2.9.1.15,build 2017-11-03,module cmc
INSERT INTO PortletItem(itemId,name,note,url,type,loadingText,icon,refreshable,refreshInterval,closable,settingable,module,categoryId) values
(428,'PortletCategory.cmcOpticalTemp',NULL,'/cmcperf/getTopCmcOpticalTempLoading.tv',1,NULL,NULL,'1',NULL,NULL,NULL,'network',4);
 /*-- version 2.9.1.15,build 2017-11-03,module cmc*/

-- version 2.10.0.5,build 2018-01-26,module cmc
update entitytype set displayname = 'CMC' where typeId = 30000;
 /*-- version 2.10.0.5,build 2018-01-26,module cmc*/
