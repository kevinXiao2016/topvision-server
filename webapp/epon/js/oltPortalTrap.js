$(function(){
	//设备启动，连续运行板卡等时间控件置灰;
	var olt_start = top.PubSub.on(top.OltTrapTypeId.OLT_START, function(data){
		disabledRuntime(data);
	});
	
	// 设备重启,连续运行板块时间控件置灰，显示获取时间失败;
	// cpu、内存、flash 显示没有获取到数据;
	var olt_reboot = top.PubSub.on(top.OltTrapTypeId.OLT_REBOOT, function(data){
		disabledRuntime(data);
	});
	$(window).on('unload', function(){
		top.PubSub.off(top.OltTrapTypeId.OLT_START, olt_start);
		top.PubSub.off(top.OltTrapTypeId.OLT_REBOOT, olt_reboot);
	});
});
function disabledRuntime(data){
	if(data && data.entityId){
		if(window.entityId && window.entityId == data.entityId){ //判断告警的ip和设备快照中设备的ip相等;
			if(oRunTime && oRunTime.clock && oRunTime.clock !== null){ //连续运行;
				oRunTime.clock.update({startTime: -1});
			}
			if(oProgressCir && oProgressCir.cpuCir && oProgressCir.cpuCir !== null){
				oProgressCir.cpuCir.update(-1);
			}
			if(oProgressCir && oProgressCir.memCir && oProgressCir.memCir !== null){
				oProgressCir.memCir.update(-1);
			}
			if(oProgressCir && oProgressCir.flashCir && oProgressCir.flashCir !== null){
				oProgressCir.flashCir.update(-1);
			}
		}
	}
}
