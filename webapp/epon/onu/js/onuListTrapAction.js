$(function(){
	//ONU升级
	var onu_upgrade = top.PubSub.on(top.OltTrapTypeId.ONU_UPGRADE, function(data){
		trapOnuUpgrade(data);
	});
	//ONU自动升级 
	var onu_auto_upgrade = top.PubSub.on(top.OltTrapTypeId.ONU_AUTO_UPGRADE, function(data){
		trapOnuUpgrade(data);
	});
	
	//板卡离线，store重载;
	var board_offline = top.PubSub.on(top.OltTrapTypeId.BOARD_OFFLINE, function(data){
		//var source: "SLOT:3"
		trapReloadStore(data);
	});
	//板卡拔出，store重载;
	var board_remove = top.PubSub.on(top.OltTrapTypeId.BOARD_REMOVE, function(data){
		trapReloadStore(data);
	});
	//设备重启,store重载;
	var olt_reboot = top.PubSub.on(top.OltTrapTypeId.OLT_REBOOT, function(data){
		setOnuStateByOlt(data, false);
	});
	//板卡重启，store重载;
	var board_reset = top.PubSub.on(top.OltTrapTypeId.BOARD_RESET, function(data){
		trapReloadStore(data);
	});
	//光模块拔出 ，store重载;
	var optical_module_remove = top.PubSub.on(top.OltTrapTypeId.OPTICAL_MODULE_REMOVE, function(data){   
		trapReloadStore(data);
	});
	//主干光纤断纤，store重载;
	var port_pon_los = top.PubSub.on(top.OltTrapTypeId.PORT_PON_LOS, function(data){   
		trapReloadStore(data);
	});
	//ONU掉电，store重载;
	var onu_pwr_off = top.PubSub.on(top.OltTrapTypeId.ONU_PWR_OFF, function(data){   
		setOnuState(data, false);
		//trapReloadStore(data);
	});
	//onu下线，store重载;
	var onu_offline = top.PubSub.on(top.OltTrapTypeId.ONU_OFFLINE, function(data){  
		setOnuState(data, false);
		//trapReloadStore(data);
	});
	//onu上线，store重载;
	var onu_online = top.PubSub.on(top.OltTrapTypeId.ONU_ONLINE, function(data){
		setOnuState(data, true);
		//更新光接收功率、光发送功率、PON接收功率的值;
		trapSetOnuPower(data);
		//trapReloadStore(data);
	});
	//onu断纤，store重载;
	var onu_fiber_break = top.PubSub.on(top.OltTrapTypeId.ONU_FIBER_BREAK, function(data){
		setOnuState(data, false);
		//trapReloadStore(data);
	});
	//onu长发光，store重载;
	var onu_rogue = top.PubSub.on(top.OltTrapTypeId.ONU_ROGUE, function(data){
		setOnuState(data, false);
		//trapReloadStore(data);
	});
	//onu删除，store重载;
	var onu_delete = top.PubSub.on(top.OltTrapTypeId.ONU_DELETE, function(data){
		trapReloadStore(data);
	});
	$(window).on('unload', function(){
		top.PubSub.off(top.OltTrapTypeId.ONU_UPGRADE, onu_upgrade);
		top.PubSub.off(top.OltTrapTypeId.ONU_AUTO_UPGRADE, onu_auto_upgrade);
		top.PubSub.off(top.OltTrapTypeId.BOARD_OFFLINE, board_offline); 
		top.PubSub.off(top.OltTrapTypeId.BOARD_REMOVE, board_remove);
		top.PubSub.off(top.OltTrapTypeId.OLT_REBOOT, olt_reboot);
		top.PubSub.off(top.OltTrapTypeId.BOARD_RESET, board_reset);
		top.PubSub.off(top.OltTrapTypeId.OPTICAL_MODULE_REMOVE, optical_module_remove);
		top.PubSub.off(top.OltTrapTypeId.PORT_PON_LOS, port_pon_los);
		top.PubSub.off(top.OltTrapTypeId.ONU_PWR_OFF, onu_pwr_off);
		top.PubSub.off(top.OltTrapTypeId.ONU_OFFLINE, onu_offline);
		top.PubSub.off(top.OltTrapTypeId.ONU_ONLINE, onu_online);
		top.PubSub.off(top.OltTrapTypeId.ONU_FIBER_BREAK, onu_fiber_break);
		top.PubSub.off(top.OltTrapTypeId.ONU_ROGUE, onu_rogue);
		top.PubSub.off(top.OltTrapTypeId.ONU_DELETE, onu_delete);
	})
});


function setOnuState(data, state) {
    if(store) {
        var ip = data.ip || data.host;
        // 根据ip找到对应的行，置为下线
        for (var i = 0, len=store.getCount(); i < len; i++) {
            if (store.getAt(i).get('onuId') === data.entityId) {
                store.getAt(i).set('onuOperationStatus', state);
                store.getAt(i).commit();
                break;
            }
        }
    }
}

//除了更新状态，还需要将 光接收功率、光发送功率、PON接收功率 置为--;
function setOnuStateByOlt(data, state) {
    if(store) {
        var ip = data.ip || data.host;
        // 根据ip找到对应的行，置为下线
        for (var i = 0, len=store.getCount(); i < len; i++) {
            if (store.getAt(i).get('entityIp') === ip) {
                store.getAt(i).set('onuOperationStatus', state);
                store.getAt(i).set('onuPonRevPower','--');
                store.getAt(i).set('onuPonTransPower','--');
                store.getAt(i).set('oltponrevpower','--');
                store.getAt(i).commit();
            }
        }
    }
}
//更新光接收功率、光发送功率、PON接收功率的值;
function trapSetOnuPower(data){
	if(store) {
        var ip = data.ip || data.host;
        // 根据ip找到对应的行;
        for (var i = 0, len=store.getCount(); i < len; i++) {
            if (store.getAt(i).get('entityIp') === ip) {
            	store.getAt(i).set('onuPonRevPower', store.getAt(i).get('onuPonRevPower'));
                store.getAt(i).set('onuPonTransPower', store.getAt(i).get('onuPonTransPower'));
                store.getAt(i).set('oltponrevpower', store.getAt(i).get('oltponrevpower'));
                store.getAt(i).commit();
            }
        }
	}
}

function trapReloadStore(data){
	if(store){
		store.reload();
	}
}
//onu升级需要延迟70秒后再去读取mib;
function trapOnuUpgrade(data){
	setTimeout(function(){
		trapReloadStore(data);
	}, 70000)
}
