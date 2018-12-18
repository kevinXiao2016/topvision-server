$(function(){
	/*
	 *  主备倒换
	 *  刷新页面
	 */
	var olt_switch = top.PubSub.on(top.OltTrapTypeId.OLT_SWITCH, function(data){
		setTimeout(function(){
			trapReloadPage(data);
		},30000);
	});
	/*
	 * 板卡上线
	 * 刷新页面 TODO 没有收到告警;
	 */
	var board_online = top.PubSub.on(top.OltTrapTypeId.BOARD_ONLINE, function(data){
		setTimeout(function(){
			trapReloadPage(data);
		},30000);
	});
	/*
	 * 板卡离线
	 * 刷新页面
	 */
	var board_offline = top.PubSub.on(top.OltTrapTypeId.BOARD_OFFLINE, function(data){
		setTimeout(function(){
			trapReloadPage(data);
		},30000);
	});
	/*
	 * 板卡温度过高或过低;
	 * 对应板卡上第三个灯亮起来; 
	 */
	var board_temperature = top.PubSub.on(top.OltTrapTypeId.BOARD_TEMPERATURE, function(data){ 
		var ledColor = ['gray','#E3FD07','gray'];
		var alertColor = ledColor[1];
		if(!data || !data.sourceObject || !data.sourceObject.slotNo || !window.entityId ||  window.entityId != data.entityId){
			return;
		}
		var	board = data.sourceObject.slotNo;
		$board = $("#device_container").find('.boardClass').eq(board-1);
		$("#device_container").find('.boardClass').eq(board-1).find('.boardTag').css({color: 'green'})
		if(DOC.createElementNS){ //svg
			$board.find("circle[title='ALARM']").attr({fill: alertColor});
		}else{ //ie8 先获取vml绘制元素的id和title,移除后重新生成标签;
			var $div = $board.find("div[alt='ALARM']"),
			    $cir = $div.find(':first-child'),
			    id = $cir.attr('id'),
			    title = $cir.attr('title'),
			    inner = String.format('<v:oval id="{0}" style="width:5px;height:5px" fillcolor={1} title="{2}" />', id, alertColor , title);
			    
			$cir.remove();
			$div.html(inner);
		}
	});
	/*
	 * 板卡被拔出;
	 * 刷新页面;
	 */
	var board_remove = top.PubSub.on(top.OltTrapTypeId.BOARD_REMOVE, function(data){
		trapReloadPage(data);
	});
	/*
	 * 板卡插入;
	 * 刷新页面;
	 */
	var board_insert = top.PubSub.on(top.OltTrapTypeId.BOARD_INSERT, function(data){
		trapReloadPage(data);
	});
	/*
	 * 板卡重启 ;
	 * 刷新页面;
	 */
	var board_reset = top.PubSub.on(top.OltTrapTypeId.BOARD_RESET, function(data){
		trapReloadPage(data);
	});
	/*
	 * 板卡类型不匹配 ;
	 * 刷新页面;
	 */
	var olt_bd_mismatch = top.PubSub.on(top.OltTrapTypeId.OLT_BD_MISMATCH, function(data){
		trapReloadPage(data);
	});
	/*
	 * 板卡软件版本不匹配 ;
	 * 刷新页面;
	 */
	var bd_sw_mismatch = top.PubSub.on(top.OltTrapTypeId.BD_SW_MISMATCH, function(data){
		trapReloadPage(data);
	});
	/*
	 * 槽位不支持板卡类型 ;
	 * 刷新页面;
	 */
	var bd_not_match = top.PubSub.on(top.OltTrapTypeId.BD_NOT_MATCH, function(data){
		trapReloadPage(data);
	});
	
	$(window).on('unload', function(){
		top.PubSub.off(top.OltTrapTypeId.OLT_SWITCH, olt_switch);
		top.PubSub.off(top.OltTrapTypeId.BOARD_ONLINE, board_online);
		top.PubSub.off(top.OltTrapTypeId.BOARD_OFFLINE, board_offline);
		top.PubSub.off(top.OltTrapTypeId.BOARD_TEMPERATURE, board_temperature);
		top.PubSub.off(top.OltTrapTypeId.BOARD_REMOVE, board_remove);
		top.PubSub.off(top.OltTrapTypeId.BOARD_INSERT, board_insert);
		top.PubSub.off(top.OltTrapTypeId.BOARD_RESET, board_reset);
		top.PubSub.off(top.OltTrapTypeId.OLT_BD_MISMATCH, olt_bd_mismatch);
		top.PubSub.off(top.OltTrapTypeId.BD_SW_MISMATCH, bd_sw_mismatch);
		top.PubSub.off(top.OltTrapTypeId.BD_NOT_MATCH, bd_not_match);
		
	});
	
	function trapReloadPage(data){
		if(window.entityId && data.entityId && window.entityId == data.entityId){
			HeadMessage.sendMsg(data.clearMessage || data.message);
		}
	}
});//end docuemnt.ready;





