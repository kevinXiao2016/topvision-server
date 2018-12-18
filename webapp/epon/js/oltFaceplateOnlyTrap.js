$(function(){
	/*
	 * 设备风扇故障 ;
	 * 刷新页面;
	 */
	var fan_fail = top.PubSub.on(top.OltTrapTypeId.FAN_FAIL, function(data){
		trapFanSendMsg(data);
	});
	/*
	 * 风扇拔出 ;
	 * 刷新页面;
	 */
	var fan_remove = top.PubSub.on(top.OltTrapTypeId.FAN_REMOVE, function(data){
		trapFanSendMsg(data);
	});
	/*
	 * 风扇插入 ;
	 * 刷新页面;
	 */
	var fan_insert = top.PubSub.on(top.OltTrapTypeId.FAN_INSERT, function(data){
		trapFanSendMsg(data);
	});
	/*
	 * PON口禁用 ;
	 * 刷新页面;
	 */
	var pon_port_disable = top.PubSub.on(top.OltTrapTypeId.PON_PORT_DISABLE, function(data){ 
		trapRefreshPage(data);
	});
	/*
	 * 光模块拔出 ;
	 * 刷新页面;
	 */
	var optical_module_remove = top.PubSub.on(top.OltTrapTypeId.OPTICAL_MODULE_REMOVE, function(data){   
		trapRefreshPage(data);
	});
	/*
	 * 光模块插入 ;
	 * 刷新页面;
	 */
	var optical_module_insert = top.PubSub.on(top.OltTrapTypeId.OPTICAL_MODULE_INSERT, function(data){   
		trapRefreshPage(data);
	});
	/*
	 * PON LOS ;
	 * 刷新页面;
	 */
	var pon_port_los = top.PubSub.on(top.OltTrapTypeId.PORT_PON_LOS, function(data){   
		trapRefreshPage(data);
	});
	
	/*
	 * SNI端口链路断开 ;
	 * 刷新页面;
	 */
	var osni_port_link_down = top.PubSub.on(top.OltTrapTypeId.OSNI_PORT_LINK_DOWN, function(data){   
		trapRefreshPage(data);
	});
	
	$(window).on('unload', function(){
		top.PubSub.off(top.OltTrapTypeId.FAN_FAIL, fan_fail);
		top.PubSub.off(top.OltTrapTypeId.FAN_REMOVE, fan_remove);
		top.PubSub.off(top.OltTrapTypeId.FAN_INSERT, fan_insert);
		top.PubSub.off(top.OltTrapTypeId.PON_PORT_DISABLE, pon_port_disable);
		top.PubSub.off(top.OltTrapTypeId.PORT_PON_LOS, pon_port_los);
		top.PubSub.off(top.OltTrapTypeId.OPTICAL_MODULE_REMOVE, optical_module_remove);
		top.PubSub.off(top.OltTrapTypeId.OPTICAL_MODULE_INSERT, optical_module_insert);
		top.PubSub.off(top.OltTrapTypeId.OSNI_PORT_LINK_DOWN, osni_port_link_down);
	})
	
});//end docuemnt.ready;
//刷新页面;
function trapRefreshPage(data){
	if(window.entityId && data.entityId && window.entityId == data.entityId){
		HeadMessage.sendMsg(data.clearMessage || data.message);
	}
}
//风扇需要延迟10秒钟，否则mib读取到的数据是错的;
function trapFanSendMsg(data){
	if(window.entityId && data.entityId && window.entityId == data.entityId){
		setTimeout(function(){
			HeadMessage.sendMsg(data.clearMessage || data.message);
		}, 20000);
	}
}


