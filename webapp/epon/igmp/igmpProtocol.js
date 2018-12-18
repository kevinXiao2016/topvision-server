$(function(){
	//先创建ip输入框，只有有输入框了，才能update后台传过来的值;
	createIpInput();
	//更新后台读过来的值;
	updateData(globalParam);
	//根据igmpVersion，界面上做出相应的显示和隐藏;需求变更，界面上将不再做出显示和隐藏;
	//versionDisplay();
	//根据需要调整其他参数中td的顺序;在v3模式、非snooping 版本下v2版本老化时间这2个td应该调整到前面来;
	changeTdPosion();
	//设置版本是否可以切换;
	enableChangeVersion();
});//end document.ready;
//创建ip输入框,和他后面的help图片;
function createIpInput(){
	//创建ip输入框,id为QuerySrcIp_ems,parent的id为putQuerySrcIp;
	window.QuerySrcIp_ems = new ipV4Input('QuerySrcIp_div','putQuerySrcIp',null);
	window.QuerySrcIp_ems.width(120);
	//在输入框后面加入帮助问号,先调整一下ip输入框的样式;
	$("#QuerySrcIp_div").css({float:'left'});
}
//更新后台读过来的值;
function updateData(o){
	$("#versionSel").val(igmpVersion); //根据igmpVersion，判断<select>选中哪个;
	$("#QueryInterval_ems").val(o.commonInterval);    //通用查询间隔;
	$("#SqueryInterval_ems").val(o.specialInterval);  //特定组查询间隔;
	$("#RobustVariable_ems").val(o.robustVariable);   //健壮系数;
	$("#SqueryFreq_ems").val(o.squeryNum);            //特定组查询次数;
	window.QuerySrcIp_ems.setValue(o.querySrcIp);     //查询报文源IP;
	$("#IgmpMaxBW_ems").val(o.globalBW);
	$("#V2QueryRespTime_ems").val(o.v2RespTime);      //V2通用查询响应时间;
	$("#SqueryRespV2_ems").val(o.squeryRespV2);       //V2特定查询响应时间;
	$("#V3QueryRespTime_ems").val(o.v3RespTime);      //V3通用查询响应时间;
	$("#SqueryRespV3_ems").val(o.squeryRespV3);       //V3特定查询响应时间;
	$("#SnoopingAgingTime_ems").val(o.snpAgingTime);  //snooping老化时间;
	$("#V3CompatV2Timeout_ems").val(o.v2Timeout);     //V2版本老化时间;
}	
//只有disable才能切换模式;
function enableChangeVersion(){
	var $versionSel = $("#versionSel");
	if(mode == IGMPMODE.V3){
		$versionSel.removeAttr("disabled");
	}else{
		$versionSel.attr({disabled : 'disabled'});
	}
};
//根据igmpVersion，判断<select>选中哪个,并且做出相应的显示和隐藏;
function versionDisplay(){
	resetVersion();
	var $v2Container = $("#v2Container"),
		$v3Container = $("#v3Container"),
		$otherContainer = $("#otherContainer"),
		$snoopingTime = $(".snoopingTime"),
		$v2Time = $(".v2Time");
	
	//snooping 模式下  显示 snooping老化时间、和版本无关联，只和模式关联;
	//v2                     不显示: V3版本参数 、V2版本老化时间;
	//v3                     全部显示;
	//v3-only  不显示: V2版本参数、V2版本老化时间;
	if(mode === IGMPMODE.V3ONLY){
		$snoopingTime.css({display:'block'});
	}else{
		$snoopingTime.css({display:'none'});
	}
	switch(igmpVersion){
	case IGMPMODE.V2: //v2;
		$v3Container.css({display:'none'});
		$v2Time.css({display:'none'});
		displayOtherContainer();
		break;
	case IGMPMODE.V3ONLY: //v3-only;
		$v2Container.css({display:'none'});
		$v2Time.css({display:'none'});
		displayOtherContainer();
		break
	}
}
function resetVersion(){
	var $v2Container = $("#v2Container"),
	    $v3Container = $("#v3Container"),
	    $otherContainer = $("#otherContainer"),
	    $snoopingTime = $(".snoopingTime"),
	    $v2Time = $(".v2Time");
	
	$v2Container.css({display:''});
	$v3Container.css({display:''});
	$otherContainer.css({display:''});
	$snoopingTime.css({display:''});
	$v2Time.css({display:''});
}
//如果其他参数中，2个板块都是隐藏的，那么其他这个板块也一起隐藏;
function displayOtherContainer(){
	var $otherContainer = $("#otherContainer"),
	    $snoopingTime = $(".snoopingTime"),
	    $v2Time = $(".v2Time");
	
	if($v2Time.eq(0).is(":hidden") && $snoopingTime.eq(0).is(":hidden")){
		$otherContainer.css({display:'none'});
	}
}
function changeVersion(){
	igmpVersion = parseInt($("#versionSel").val(),10);
	//versionDisplay();
	//changeTdPosion();
}
//改变td的位置信息;
//在v3模式、非snooping 版本下v2版本老化时间这2个td应该调整到前面来;
function changeTdPosion(){
	var $tr = $("#otherTable tbody tr:eq(0)");
	if(igmpVersion === IGMPMODE.V3 && mode !== 4){//v3模式，非snooping
		if( $tr.find("td:eq(0)").hasClass("snoopingTimeTd")){ //第一个td是snooping老化时间;
			var aV2TimeTd = $tr.find("td.v2TimeTd");
			$tr.prepend(aV2TimeTd);	
		}
	}
}
//应用;
function applyFn(){
	//snooping 模式下  显示 snooping老化时间、和版本无关联，只和模式关联;
	//v2                     不显示: V3版本参数 、V2版本老化时间;
	//v3                     全部显示;
	//v3-only  不显示: V2版本参数、V2版本老化时间;
		
	//无论是什么模式、什么版本，基本参数板块都需要验证,其中IP输入框要单独验证;
	var vBase = validateBase();
	if(vBase !== true){//验证不通过;
		vBase.focus();
		return;
	}
	//单独验证ip输入框;
	var ipInput = getIpValue('QuerySrcIp_div');
	if ( !checkedIpValue(ipInput) || !checkIsNomalIp(ipInput)) {
		$("#QuerySrcIp_div :text").eq(0).focus();
		return;
	}
	//验证模式，通过返回true,不通过$id;
	var version,
		$SqueryInterval_ems = $("#SqueryInterval_ems"),
		SqueryInterval_val  = parseInt($SqueryInterval_ems.val(), 10),
		$SqueryRespV2_ems   = $("#SqueryRespV2_ems"),
		SqueryRespV2_val    = parseInt($SqueryRespV2_ems.val(), 10),
		$SqueryRespV3_ems   = $("#SqueryRespV3_ems"),
		SqueryRespV3_val    = $SqueryRespV3_ems.val(),
		$V3QueryRespTime_ems = $("#V3QueryRespTime_ems"),
		V3QueryRespTime_val = parseInt($V3QueryRespTime_ems.val(), 10),
		$QueryInterval_ems = $("#QueryInterval_ems"),
		QueryInterval_val = parseInt($QueryInterval_ems.val(), 10);
	//过去每种模式，只要验证自己的值，现在所有的都需要验证;
	version = validateV3();
	if(version !== true){//验证不通过;
		version.focus();
		return;
	}
	var errTip,maxText;
	//通用查询间隔必须大于V3通用查询响应时间
	if(QueryInterval_val*10 <= V3QueryRespTime_val){
		errTip = "@igmp.tip.tip44@";
		top.showMessageDlg("@COMMON.tip@", errTip);
		return;
	}
	
	//特定组查询间隔，必须大于修改前的v2特定查询响应时间和修改前的v3特定查询响应时间;
	if(SqueryInterval_val <= globalParam.squeryRespV2 || SqueryInterval_val <= globalParam.squeryRespV3){
		var num = Math.max(globalParam.squeryRespV2, globalParam.squeryRespV3);
		if(globalParam.squeryRespV2 >= globalParam.squeryRespV3){
			maxText = "V2";
		}else{
			maxText = "V3";
		}
		errTip = String.format("@igmp.tip.tip39@", maxText, num);
		top.showMessageDlg("@COMMON.tip@", errTip);
		return;
	}
	//在v3模式下，特定组查询间隔必须大于v2响应时间和v3响应时间;
	if( SqueryInterval_val <= SqueryRespV2_val || SqueryInterval_val <= SqueryRespV3_val){
		errTip = "@igmp.tip.tip36@";
		top.showMessageDlg("@COMMON.tip@", errTip);
		return;
	}
	//如果是snooping模式，无论是什么版本，都要验证snooping老化时间;需求变更后，任何模式都需要验证snooping;
	var snoopingVal = $("SnoopingAgingTime_ems").val();
	var $snoopping = customValidateFn([{
		id    : 'SnoopingAgingTime_ems',
		range : [1,30]
	}]);
	if($snoopping !== true){//验证不通过;
		$snoopping.focus();
		return;
	}
	var versionChange = false;
	if(globalParam.igmpVersion != $("#versionSel").val()){
		versionChange = true;
	}
	var data  = {
		"globalParam.entityId"        : globalParam.entityId,
		"globalParam.commonInterval"  : $("#QueryInterval_ems").val(),      //通用查询间隔;
		"globalParam.globalBW"        : $("#IgmpMaxBW_ems").val(),          //IGMP总带宽;
		"globalParam.igmpMode"        : window.mode,                        //当前模式;
		"globalParam.igmpVersion"     : $("#versionSel").val(),             //IGMP版本;
		"globalParam.querySrcIp"      : window.QuerySrcIp_ems.getValue(),   //查询报文源IP;
		"globalParam.robustVariable"  : $("#RobustVariable_ems").val(),     //健壮系数;
		"globalParam.snpAgingTime"    : $("#SnoopingAgingTime_ems").val(),  //Snooping老化时间;
		"globalParam.specialInterval" : $("#SqueryInterval_ems").val(),     //特定组间隔查询;
		"globalParam.squeryNum"       : $("#SqueryFreq_ems").val(),         //特定查询次数;
		"globalParam.squeryRespV2"    : $("#SqueryRespV2_ems").val(),       //V2特定查询响应时间;
		"globalParam.squeryRespV3"    : $("#SqueryRespV3_ems").val(),       //V3特定查询响应时间;
		"globalParam.v2RespTime"      : $("#V2QueryRespTime_ems").val(),    //V2通用查询响应时间;
		"globalParam.v2Timeout"       : $("#V3CompatV2Timeout_ems").val(),  //V2版本老化时间;
		"globalParam.v3RespTime"      : $("#V3QueryRespTime_ems").val(),     //V3通用查询响应时间;
		"versionChange"               : versionChange
	}	
	modifyIgmpProtocol(data);
}

function modifyIgmpProtocol(data){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/modifyIgmpProtocol.tv',
		type : 'POST',
		data : data,
		dataType:"json",
		success : function() {
			top.afterSaveOrDelete({
		      title: '@COMMON.tip@',
		      html: '<b class="orangeTxt">@tip.saveS@</b>'
		    });
			window.location.href = window.location.href;
		},
		error : function() {
			top.showMessageDlg("@COMMON.tip@", "@tip.saveIgmpConfigF@");
		},
		cache : false
	});
}

//通过返回true,不通过返回$id对象;
function validateBase(){
	var baseObj = [{
		id    : 'QueryInterval_ems',
		range : [30,5000]
	},{
		id    : 'SqueryInterval_ems',
		range : [2,50000] 
	},{
		id    : 'RobustVariable_ems',
		range : [1,10]
	},{
		id    : 'SqueryFreq_ems',
		range : [1,10]
	},{
		id    : 'IgmpMaxBW_ems',
		range : [0,1000000]
	}];
	return customValidateFn(baseObj);
}
function validateV2(){
	var v2 = [{
		id    : 'V2QueryRespTime_ems',
		range : [1,255]
	},{
		id    : 'SqueryRespV2_ems',
		range : [1,255]
	}]
	return customValidateFn(v2);
}
function validateV3(){
	var v3 = [{
		id    : 'V2QueryRespTime_ems',
		range : [1,255]
	},{
		id    : 'SqueryRespV2_ems',
		range : [1,255]
	},{
		id    : 'V3QueryRespTime_ems',
		range : [1,31744]
	},{
		id    : 'SqueryRespV3_ems',
		range : [1,31744]
	},{
		id    : 'V3CompatV2Timeout_ems',
		range : [1,5000]
	}];
	return customValidateFn(v3);
}
function validateV3Only(){
	var v3Only = [{
		id    : 'V3QueryRespTime_ems',
		range : [1,31744]
	},{
		id    : 'SqueryRespV3_ems',
		range : [1,31744]
	}]
	return customValidateFn(v3Only);
}
function refreshFromDevice(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshIgmpProtocol.tv',
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
			window.location.href = window.location.href;
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}