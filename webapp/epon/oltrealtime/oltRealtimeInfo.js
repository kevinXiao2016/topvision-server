var slotColumn,slotStore,slotGrid,sniCloumn,sniStore,sniGrid;
var grid1_height = null, grid2_height = null;

//自适应设置列表的宽度
function autoHeight(){
	var w = $(window).width();
	slotGrid.setWidth(w);
	sniGrid.setWidth(w);
};//end autoHeight;

//resize事件增加函数节流
function throttle(method, context){
	clearTimeout(method.tId);
	method.tId = setTimeout(function(){
		method.call(context);
	},100);
}

//刷新
function refreshHandler(){
	window.location.reload(true);
}

//重启
function resetHandler(){
	window.parent.showConfirmDlg("@entitySnapPage.message@", "@entitySnapPage.deviceRestart@", function(type) {
		if (type == 'no') {
			return;
		}
		window.parent.showWaitingDlg("@entitySnapPage.wait@", "@entitySnapPage.restarting@", 'ext-mb-waiting');
		Ext.Ajax.request({url:"/epon/resetOlt.tv", success: function (response) {
	        window.parent.closeWaitingDlg();
	        if(response.responseText == "success"){
	        	top.afterSaveOrDelete({
	                title: '@COMMON.tip@',
	                html: '<b class="orangeTxt">@entitySnapPage.restartSuccess@</b>'
	            });
	        }else{
	        	window.parent.showMessageDlg("@entitySnapPage.message@", "@entitySnapPage.restartFail@");
	        }
	    }, 
	    failure: function(){
	        window.parent.closeWaitingDlg();
	        window.parent.showMessageDlg("@entitySnapPage.message@", "@entitySnapPage.restartFail@");
	    }, 
	    params: {entityId : entityId}
	    });
	});
}

//ping
function pingHandler(){
	window.parent.createDialog("modalDlg", 'Ping ' + entityIp, 600, 400,
			"entity/runCmd.tv?cmd=ping&ip=" + entityIp, null, true, true);
}

//traceRoute
function traceRouteHandler(){
	window.parent.createDialog("modalDlg", 'Tracert' + " - " + entityIp, 600, 400,
			"entity/runCmd.tv?cmd=tracert&ip=" + entityIp, null, true, true);
}

function buildToolBar(){
	new Ext.Toolbar({
        renderTo: "topToolBar",
        items: [
           {text: '@COMMON.refresh@',cls:'mL10', iconCls: 'bmenu_refresh', handler: refreshHandler},
           {text: '@COMMON.restore@', iconCls: 'bmenu_Back', handler: resetHandler,disabled:!operationDevicePower},
           {text: 'PING', iconCls: 'bmenu_Cmd', handler: pingHandler},
           {text: 'TraceRoute', iconCls: 'bmenu_twoArr', handler: traceRouteHandler}
        ]
    });
}

//显示pon口详细信息
function showPonInfo(flag){
	var showOnline = false;
	if(flag == 1){
		showOnline = true;
	}
	top.addView('ponRealTimeInfo' + entityId, '@EPON.ponPort@@COMMON.realtimeInfo@['+entityName+']' , 'entityTabIcon', '/epon/oltRealtime/showOltPonInfo.tv?entityId=' + entityId + "&onlineFlag=" + showOnline, null, true);
}

//显示下级设备详细信息
function showSubInfo(flag){
	var showOnline = false;
	if(flag == 1){
		showOnline = true;
	}
	top.addView('onuRealTimeInfo' + entityId, '@SERVICE.downlink@@COMMON.realtimeInfo@['+entityName+']', 'entityTabIcon', '/epon/oltRealtime/showOltSubInfo.tv?entityId=' + entityId + "&onlineFlag=" + showOnline, null, true);
}

/********************************************************************
将以毫秒为单位的时间转换为以天/小时/分/秒的形式显示的方法
*******************************************************************/
function timeFormat(value) {
	var s = value * 10;
	var t;
	if(s > -1){
		hour = Math.floor(s/3600000);
	    min = Math.floor(s/60000) % 60;
	    sec = Math.floor(s/1000) % 60;
	    day = parseInt(hour / 24);
	    if (day > 0) {
	    	hour = hour - 24 * day;
	        t = day + "@COMMON.D@" + hour + "@COMMON.H@";
	    } else {
	  		t = hour + "@COMMON.H@";
	 	}
		if(min < 10){
			t += "0";
		}
	    t += min + "@COMMON.M@";
	    if(sec < 10){
			t += "0";
		}
	      t += sec + "@COMMON.S@";
	}
	return t;
}

function renderStatus(value, p, record){
	if (value == 1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_on.png" border=0 align=absmiddle>',
			"@COMMON.online@");	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_off.png" border=0 align=absmiddle>',
			"@resources/COMMON.offline@");
	}
}

function renderPortNo(value, p, record){
	console.log(record.data.sniPortIndex)
	return convertPortIndexToStrWithType(record.data.sniPortIndex);
}


function renderMode(value, p, record){
	switch(value){
		case 1: return "Auto-Negotiate";
		case 2: return "Half-10M";
		case 3: return "Full-10M";
		case 4: return "Half-100M";
		case 5: return "Full-100M";
		case 6: return "Full-1000M";
		case 7: return "Full-10000M";
	}
}

function renderPreType(value, p, record){
	switch(value){
		case 0: return "non-Board";
		case 1: return "MPUA";
		case 2: return "MPUB";
		case 3: return "EPUA";
		case 4: return "EPUB";
		case 5: return "GEUA";
		case 6: return "GEUB";
		case 7: return "XGUA";
		case 8: return "XGUB";
		case 9: return "XGUC";
		case 10: return "XPUA";
		case 11: return "MPU-GEUA";
		case 12: return "MPU-GEUB";
		case 13: return "MPU-XGUC";
		case 14: return "GPUA";
		case 15: return "EPUC";
		case 16: return "EPUD";
		case 17: return "MEUA";
		case 18: return "MEUB";
		case 19: return "MEFA";
		case 20: return "MEFB";
		case 21: return "MEFC";
		case 22: return "MEFD";
		case 23: return "MGUA";
		case 24: return "MGUB";
		case 25: return "MGFA";
		case 26: return "MGFB";
		case 255: return "Unknown";
	}
}

function renderActualType(value, p, record){
	switch(value){
		case 0: return "non-Board";
		case 1: return "MPUA";
		case 2: return "MPUB";
		case 3: return "EPUA";
		case 4: return "EPUB";
		case 5: return "GEUA";
		case 6: return "GEUB";
		case 7: return "XGUA";
		case 8: return "XGUB";
		case 9: return "XGUC";
		case 10: return "XPUA";
		case 11: return "GPUA";
		case 12: return "MEUA";
		case 13: return "MEUB";
		case 14: return "MEFA";
		case 15: return "MEFB";
		case 16: return "EPUC";
		case 17: return "EPUD";
		case 21: return "MEFC";
		case 22: return "MEFD";
		case 23: return "MGUA";
		case 24: return "MGUB";
		case 25: return "MGFA";
		case 26: return "MGFB";
		case 255: return "Unknown";
	}
}

function tempRender(value, p, record){
	var onlineStatus = record.data.operationStatus;
	if(onlineStatus == 1){
		return value + "@{unitConfigConstant.tempUnit}@";
	}else{
		return "--";
	}
}

function cmNumRender(value, p, record){
	var slotType = record.data.actualType;
	//只有 pon(epua,epub,xpua,gpua,epuc,epud)板才显示CM数量
	if(slotType == 3 || slotType == 4 || slotType == 10 || slotType == 11  || slotType == 16 || slotType == 17){
		return value;
	}else{
		return "--";
	}
}

function versionRender(value, p, record){
	if(value == null || value == ''){
		return "--"
	}
	return value;
}

function usedRatioRender(value, p, record){
	var onlineStatus = record.data.operationStatus;
	if(onlineStatus == 1){
		return value + "%";
	}else{
		return "--";
	}
}

function speedRender(value, p, record){
	if(parseInt(value) == -1){
		return "--";
	}else{
		return value;
	}
}

//加载概要信息
function loadSummaryInfo(){
	$.ajax({
		url : '/epon/oltRealtime/loadBaseInfo.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		success : function(json) {
			//基本信息
			var baseInfo = json.baseInfo;
			$('#deviceName').text(baseInfo.deviceName);
			$('#upTime').text(timeFormat(baseInfo.sysUpTime));
			$('#softVersion').text(baseInfo.softwareVersion);
			//上联口统计信息
			var sniTotal = json.sniTotal;
			$("#sniTotal").text(sniTotal.onlineNum + " / " + sniTotal.totalNum);
			//cm统计信息
			var cmTotal = json.cmTotal;
			if(cmTotal) {
				$('#cmTotal').text(cmTotal.onlineNum + " / " + cmTotal.totalNum);
			}
			//pon口统计信息
			var pon = json.ponTotal;
			var ponTotal = pon.totalNum;
			var ponOnline = pon.onlineNum;
			var ponStr = '';
			ponStr += (ponOnline == 0) ? '0' : '<a href="#" class="yellowLink" onclick="showPonInfo(1)">'+ ponOnline +'</a>';
			ponStr += ' / ';	
			ponStr += (ponTotal == 0) ? '0' : '<a href="#" class="yellowLink" onclick="showPonInfo(2)">'+ ponTotal +'</a>';
			$("#ponTd").html(ponStr);
			//下级设备统计信息
			var subTotal = json.subTotal;
			var subStr = '';
			subStr += (subTotal.onlineNum == 0) ? '0' : '<a href="#" class="yellowLink" onclick="showSubInfo(1)">'+ subTotal.onlineNum +'</a>';
			subStr += ' / ';	
			subStr += (subTotal.totalNum == 0) ? '0' : '<a href="#" class="yellowLink" onclick="showSubInfo(2)">'+ subTotal.totalNum +'</a>';
			$("#subTd").html(subStr);
		},
		error : function(json) {
		},
		cache : false
	});
}


$(document).ready(function() {
	$("#tb1 tbody tr").each(function(){
		var $td = $(this).find("td"); 
		$td.eq(0).css({"borderRight" : "none"});
		$td.eq(1).css({"borderLeft" : "none"});
		$td.eq(2).css({"borderRight" : "none"});
		$td.eq(3).css({"borderLeft" : "none"});
	})
	$("#tb1_tit").click(function(){
		var $me = $(this),
		    $table = $("#tb1");
		if($me.hasClass("flagOpen")){
			$me.attr({"class":"flagClose"});
			$table.stop().fadeOut();
		}else if($me.hasClass("flagClose")){
			$me.attr({"class":"flagOpen"});
			$table.stop().fadeIn(function(){
				$table.css("opacity",1);
			});
		}
	});//end click;
	
	
	/********************************************************************
		加载板卡信息
	*******************************************************************/
	slotColumn = new Ext.grid.ColumnModel([
  		{header:"@ELEC.slotNo@", dataIndex:"slotIndex", width:55},
  		{header:"<div class='txtCenter'>@REALTIME.slotType@</div>", dataIndex:"actualType",width:80, renderer : renderActualType, align:"left" },
  		{header:"<div class='txtCenter'>@onuAuth.preType@</div>", dataIndex:"preConfigType",width:60, renderer : renderPreType , align:"left" },
  		{header:"@REALTIME.slotStatus@", dataIndex:"operationStatus",width:60, renderer : renderStatus},
  		{header:"@Business.cpuUtility@", dataIndex:"cpuUseRatio", width:80, renderer : usedRatioRender},
  		{header:"@Business.memUtility@", dataIndex:"memUseRatio", width:80, renderer : usedRatioRender},
  		{header:"@Business.flushUtility@", dataIndex:"flashUseRatio",width:80, renderer : usedRatioRender},
  		{header:"@COMMON.TMP@", dataIndex:"tempValue", width:60, renderer : tempRender},
  		{header:"@EPON.softwareVersion@", dataIndex:"slotVersion", renderer : versionRender},
  		{header:"@REALTIME.cmNumber@", dataIndex:"cmNum", renderer : cmNumRender}
  		
  	]);//end cm;
	slotStore = new Ext.data.JsonStore({
		url : '/epon/oltRealtime/loadSlotInfo.tv',
		fields : ["slotIndex","cmNum", "preConfigType", "actualType","cpuUseRatio","memUseRatio","flashUseRatio","currentTemp","slotVersion","operationStatus","tempValue"],
		baseParams : {
			entityId : entityId
		},
		listeners:{
			load:function(){
				grid1_height = slotStore.data.items.length * 32 + 65; 
				if(slotGrid){
					slotGrid.setHeight(grid1_height);
				}
			}
		}
	});
 	
  	slotGrid = new Ext.grid.GridPanel({
  		renderTo: 'putBoardGrid',
  		title: "<label class='extGridTit flagOpen'>@REALTIME.slotRealInfo@</label>",
  		height: 200,
  		stripeRows:true,
  		enableColumnMove: false,
  		enableColumnResize: true,
  		cls: 'normalTable pL10 pR10',
  		store: slotStore,
  		cm : slotColumn,
  		loadMask : {
             msg :'@entitySnapPage.loading@'
        },
  		viewConfig:{
  			forceFit: true
  		}
  	});
  	slotStore.load();
  	
  	/********************************************************************
		加载SNI口信息
	*******************************************************************/
  	sniCloumn = new Ext.grid.ColumnModel([
  		{header:"@ELEC.portNo@", dataIndex:"sniLocation", width:55,renderer : renderPortNo},
  		{header:"<div class='txtCenter'>@REALTIME.portName@</div>", dataIndex:"portName", align:"left"},
  		{header:"@RSTP.portStatus@", dataIndex:"operationStatus", width:60, renderer : renderStatus},
  		{header:"<div class='txtCenter'>@REALTIME.negoMode@</div>", dataIndex:"autoNegoMode", width:100, renderer : renderMode, align:"left"},
  		{header:"@report.inFlow@(Mbps)", dataIndex:"portInSpeed",width:80, renderer : speedRender},
  		{header:"@report.outFlow@(Mbps)", dataIndex:"portOutSpeed",width:80, renderer : speedRender}
  	]);//end cm;
  	sniStore = new Ext.data.JsonStore({
		url : '/epon/oltRealtime/loadSniInfo.tv',
		fields : ["sniLocation", "sniPortIndex", "portName", "sniPortType", "operationStatus","autoNegoMode", "portInSpeed", "portOutSpeed"],
		baseParams : {
			entityId : entityId
		},
		listeners:{
			load:function(){
				grid2_height = sniStore.data.items.length * 32 + 85;
				if(sniGrid){
					sniGrid.setHeight(grid2_height);
				}
			}
		}
	});
	
  	sniGrid = new Ext.grid.GridPanel({
  		renderTo: 'putPortGrid',
  		title: "<label class='extGridTit flagOpen'>@REALTIME.sniInfo@</label>",
  		height:100,
  		stripeRows:true,
  		enableColumnMove: false,
  		enableColumnResize: true,
  		cls: 'normalTable edge10',
  		store: sniStore,
  		cm : sniCloumn,
  		loadMask : {
            msg :'@entitySnapPage.loading@'
        },
  		viewConfig:{
  			forceFit: true
  		}
  	});
  	sniStore.load();
  	
  	buildToolBar();
  	
  	$(window).wresize(function(){
		throttle(autoHeight,window)
	});//end resize;
  	
  	//加载概要信息
  	loadSummaryInfo();
  	
  	$(".extGridTit").click(function(){
		var $me = $(this),
		    $table = $me.parent().parent().next();
		if($me.hasClass("flagOpen")){
			$me.attr({"class" : "flagClose extGridTit"});
			$table.stop().fadeOut();
		}else if($me.hasClass("flagClose")){
			$me.attr({"class" : "flagOpen extGridTit"});
			$table.stop().fadeIn(function(){
				$table.css("opacity",1);
			});
		};//end if;
	});//end click;
});
