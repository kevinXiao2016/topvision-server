var OltTrapTypeId = {
	// 设备重启
	OLT_REBOOT: "1",
	// 设备启动
	OLT_START: "2",
	// 主备倒换
	OLT_SWITCH: "3",
	// ONU升级
	ONU_UPGRADE: "7",
	// 板卡上线
	BOARD_ONLINE: "259",
	// 板卡离线
	BOARD_OFFLINE: "260",
	// ONU升级
	ONU_AUTO_UPGRADE: "261",
	// 非法ONU注册
	ONU_ILLEGAL_REG: "262",
	//TODO 此需求需要申请变更不实现 CDR主动上报
	//OLT_ILLEGAL_ONU_REGISTRATION: "0",
	// 板卡启动自动配置失败
	BOARD_START_FAILURE: "4098",
	// 板卡重启
	BOARD_RESET: "4099",
	// 板卡拔出
	BOARD_REMOVE: "4101",
	// 板卡插入
	BOARD_INSERT: "4102",
	// 板卡温度过高/板卡温度过低
	BOARD_TEMPERATURE: "-604",
	//设备风扇故障
	FAN_FAIL: "-602",
	//风扇拔出
	FAN_REMOVE: "4109",
	//风扇插入
	FAN_INSERT: "4110",
	//板卡类型不匹配
	OLT_BD_MISMATCH: "4117",
	//板卡软件版本不匹配
	BD_SW_MISMATCH: "4119",
	//槽位不支持板卡类型
	BD_NOT_MATCH: "4121",
	//SNI端口链路断开
	OSNI_PORT_LINK_DOWN: "-603",
	//PON口禁用
	PON_PORT_DISABLE: "12293",
	//光模块拔出
	OPTICAL_MODULE_REMOVE: "12297",
	//光模块插入
	OPTICAL_MODULE_INSERT: "12298",
	//主干光纤断纤
	PORT_PON_LOS: "12325",
	//ONU MAC认证失败
	ONU_MAC_AUTH_FAIL: "16388",
	//ONU掉电
	ONU_PWR_OFF: "16393",
	//ONU下线
	ONU_OFFLINE: "16433",
	//ONU上线
	ONU_ONLINE: "16434",
	//ONU断纤
	ONU_FIBER_BREAK: "16435",
	//ONU长发光
	ONU_ROGUE: "16437",
	//ONU删除
	ONU_DELETE: "16439",
	//ONU UNI口不可用/信号丢失/linkdown
	ONU_UNI_LINKDOWN: "20481"
};