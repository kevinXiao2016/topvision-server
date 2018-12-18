var oUplinkPort = {
	GE     : null, //如果没有去后台拿数据，则为null,拿了数据后，则存储在该属性中;
	XE     : null,
	ETHAGG : null
}
$(function(){
	$("#typeSel").val(uplinkPort.portType);
	init();
});

// 修改切换端口类型时,必须先切换到INVALID类型
// 在选择INVALID的时候,不能选择任何端口和上行聚合组ID 考虑给-1标识
//　在选择XE或者GE时,只能选择端口; 在选择ETHAGG时,只能选择上行端口聚合组  考虑给-1标识
function modifyUplinkPort(){
	var portType = parseInt($("#typeSel").val(), 10),
	    portIndex, 
	    aggId;
	if(uplinkPort.portType != 1 && portType !== 1){ //原来的端口类型不是INVALID,修改后的类型也不是INVALID;
		top.showMessageDlg("@COMMON.tip@", "@imgp.tip.tip32@");
		return;
	}
	
	switch(portType){
	case 1:
		portIndex = -1;
		aggId = -1;
		break;
	case 3:
		portIndex = $("#portNumSel").val();
		aggId = -1;
		if(portIndex == null){
			top.showMessageDlg("@COMMON.tip@", "@igmp.tip.tip31@");
			return;
		}
		break;
	case 4:
		portIndex = $("#portNumSel").val();
		if(portIndex == null){
			top.showMessageDlg("@COMMON.tip@", "@igmp.tip.tip31@");
			return;
		}
		aggId = -1;
		break;
	case 9:
		portIndex = -1;
		aggId = $("#aggIdSel").val();
		if(aggId == null){
			top.showMessageDlg("@COMMON.tip@", "@igmp.tip.tip31@");
			return;
		}
		break;
	}
	
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/modifyUplinkPort.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			portType : portType,
			portIndex : portIndex,
			uplinkAggId :　aggId
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.editUplinkS@</b>'
   			});
			window.location.href = window.location.href;
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.editUplinkF@");
			window.location.href = window.location.href;
		},
		cache : false
	});
}

function refreshUplinkPort(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshUplinkPort.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			window.location.reload();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

function init(){
	var type = $("#typeSel").val(),
	    $jsPortNumTd    = $(".jsPortNumTd"),
	    $jsAggIdTd      = $(".jsAggIdTd");
	
	if(type == 1){
		displayPort('hidden');
		displayAggId('hidden');
	}else if(type == 9){
		displayPort('hidden');
		displayAggId('visible');
		//如果聚合组ID在tr的后面，则要更换位置到前面来;
		if( $("#upLinkTable tbody tr:eq(0) td:last").hasClass("jsAggIdTd") ){
			$jsAggIdTd.each(function(){
				$(this).insertBefore($(".jsPortNumTd:eq(0)"));
			});
		}
		if(oUplinkPort.ETHAGG === null){
			loadSniAggList({
				portType        : type,
				entityId        : entityId,
				successCallBack : function(json){
					oUplinkPort.ETHAGG = json;
					createAggIdSel(json);
				}
			});
		}else{
			createAggIdSel(oUplinkPort.ETHAGG);
		}
	}else{ //3或者4;
		displayPort('visible');
		displayAggId('hidden');
		//如果端口号在tr的后面，则要更换位置到前面来;
		if( $("#upLinkTable tbody tr:eq(0) td:last").hasClass("jsPortNumTd") ){
			$jsPortNumTd.each(function(){
				$(this).insertBefore($(".jsAggIdTd:eq(0)"));
			});
		}
		//如果没有去过后台，则获取数据后存起来。今后不再访问后台了;
		switch(parseInt(type)){
		case 3:
			if(oUplinkPort.GE === null){
				loadSniListByType({
					portType        : type,
					entityId        : entityId,
					successCallBack : function(json){
						oUplinkPort.GE = json;
						createPortSel(json);
					}
				});
			}else{
				createPortSel(oUplinkPort.GE);
			}
			break;
		case 4:
			if(oUplinkPort.XE === null){
				loadSniListByType({
					portType        : type,
					entityId        : entityId,
					successCallBack : function(json){
						oUplinkPort.XE = json;
						createPortSel(json);
					}
				});
			}else{
				createPortSel(oUplinkPort.XE);
			}
			break;
		};//end switch;
	};//end if else;
}
function displayPort(visibility){
	$(".jsPortNumTd label, .jsPortNumTd div").css({visibility:visibility});
}
function displayAggId(visibility){
	$(".jsAggIdTd label, .jsAggIdTd div").css({visibility:visibility});
}
function createPortSel(list){
	var opt = '';
	for(var i=0,len=list.length; i<len; i++){
		opt += String.format('<option value="{0}">{1}</option>', list[i].portIndex, list[i].portName);
	}
	$("#portNumSel").html(opt);
	$("#portNumSel").val(uplinkPort.portIndex);
}
function createAggIdSel(list){
	/*var tpl = new Ext.XTemplate(
		'<tpl for=".">',
			'<option value="{portIndex}">{portIndex}</option>',
		'</tpl>'
	);
	tpl.overwrite('aggIdSel', list);*/
	var opt = '';
	for(var i=0,len=list.length; i<len; i++){
		opt += String.format('<option value="{0}">{0}</option>', list[i].portIndex);
	}
	$("#aggIdSel").html(opt);
	$("#aggIdSel").val(uplinkPort.uplinkAggId);
}





















