$(function(){
	//时间输入框比较特殊，先创建时间输入框;
	createTimeInput();
	updateData();
});
//创建时间输入框;
function createTimeInput(){
	new NumberInput('hourInput','putHourInput',4, 0, 23);
	new NumberInput('minInput','putMinInput',0, 0, 59);
	new NumberInput('secondInput','putSecondInput',0, 0, 59);
}
//更新数据;
function updateData(){
	var o = ctcParam;
	$("#ctcEnable").val(o.ctcEnable);
	$("#cdrInterval").val(o.cdrInterval);
	$("#cdrNum").val(o.cdrNum);
	$("#recognitionTime").val(o.recognitionTime);
	$("#onuFwdMode").val(o.onuFwdMode);	

	var hour = parseInt((o.autoResetTime / 60 / 60) % 24);
	var mins = parseInt((o.autoResetTime / 60 ) % 60);
	var sec = parseInt((o.autoResetTime) % 60);
	$("#hourInput").val(hour);
	$("#minInput").val(mins);
	$("#secondInput").val(sec);
}
function modifyCtcParam(){
	//验证;
	var arr = [{
		id    : 'cdrInterval',
		range : [60,43200]
	},{
		id    : 'cdrNum',
		range : [100,200]
	},{
		id    : 'recognitionTime',
		range : [1,120]
	}]
	var $id = customValidateFn(arr);
	if($id !== true){//验证不通过;
		$id.focus();
		return;
	}
	var hour = parseInt( $("#hourInput").val(),10 ) * 60 * 60,
	    mins = parseInt( $("#minInput").val(),10 ) * 60, 
	    sec  = parseInt( $("#secondInput").val(),10),
	    time = hour + mins + sec;
	
	var ctcData = {
		"ctcParam.entityId"        : entityId,
		"ctcParam.ctcEnable"       : $("#ctcEnable").val(),
		"ctcParam.cdrNum"          : $("#cdrNum").val(),
		"ctcParam.cdrInterval"     : $("#cdrInterval").val(),
		"ctcParam.recognitionTime" : $("#recognitionTime").val(),
		"ctcParam.autoResetTime"   : time,
		"ctcParam.onuFwdMode"      : $("#onuFwdMode").val()
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/modifyCtcParam.tv',
		type : 'POST',
		data : ctcData,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.saveCtcParamSuc@</b>'
   			});
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.saveCtcParamF@");
		},
		cache : false
	});
}

function refreshCtcParam(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshCtcParam.tv',
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

//手动上报CDR日志操作
function reportCtcCdr(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/reportCtcCdr.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.reportCtcCdrSuc@</b>'
   			});
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.reportCtcCdrF@");
		},
		cache : false
	});
}