<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var igmpEntity = ${igmpEntityObject};
var sniConfig = ${sniConfigObject};
var igmpMcParamMgmt = ${igmpMcParamMgmtObject};
var olt = null;
var TrunkList = null;

var mvidList = ${mvidList};
if(mvidList.join("") == "false"){
	mvidList = [[]];
}

var mvlanData = igmpMcParamMgmt[0].topMcMVlanList ? igmpMcParamMgmt[0].topMcMVlanList : new Array();
var vlanListTmp = ${vlanListObject};
var vlanList = vlanListTmp[0];
var vlanVif = vlanListTmp[1];
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
function initData() {
	if(igmpEntity[0]!=null){
		$("#igmpWorkMode").val(igmpEntity[0].igmpMode);
		$("#igmpVersion").val(igmpEntity[0].igmpVersion);
		$("#igmpRobustVariable").val(igmpEntity[0].robustVariable);
		$("#maxQueryResponseTime").val(igmpEntity[0].maxQueryResponseTime);
		$("#queryInterval").val(igmpEntity[0].queryInterval);
		$("#lastMemberQueryCount").val(igmpEntity[0].lastMemberQueryCount);
		$("#lastMemberQueryInterval").val(igmpEntity[0].lastMemberQueryInterval/10);
	}
    if(sniConfig[0]!=null){
    	$("#sniType").val(sniConfig[0].topMcSniPortType);
    }
	if(igmpMcParamMgmt[0]!=null){
		$("#igmpMaxGroupNum").val(igmpMcParamMgmt[0].topMcMaxGroupNum);
		$("#topMcMaxBw").val(igmpMcParamMgmt[0].topMcMaxBw);
		$("#snoopAgeTime").val(igmpMcParamMgmt[0].topMcSnoopingAgingTime);
	}
	loadOltJson();
	loadTrunkJson();
	igmpWorkModeChange();
	sniTypeChange();
	if(sniConfig[0]!=null){
		if(sniConfig[0].topMcSniPortType == 1){
			var tempArray = sniConfig[0].topMcSniPort.split(":");
			$("#sniPortList").val(tempArray[0]+"/"+tempArray[1]);
		}else if(sniConfig[0].topMcSniPortType == 2){
			$("#sniPortList").val(sniConfig[0].topMcSniAggPort);
		}
	}

	initMvlan();
}
function loadOltJson() {
	$.ajax({
        url: "/epon/oltObjectCreate.tv",
        type: 'POST',
        async: false,
        data: "entityId=" + entityId +"&num=" + Math.random(),
        dataType:"json",
        success: function(json) {
        	olt = json;
        }, error: function(json) {
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.EPON.loadOltError);
    }, cache: false
    });
}
function loadTrunkJson() {
	$.ajax({
        url: '/epon/trunk/loadTrunkConfigList.tv',
        type: 'POST',
        async: false,
        data: "entityId=" + entityId +"&num=" + Math.random(),
        dataType:"json",
        success: function(jsons) {             
        	TrunkList = jsons;
        }, 
        error: function(TrunkJson) {
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.TRUNK.loadTrunkError);
    }, cache: false});
}
function saveClick() {
	var igmpWorkMode = $("#igmpWorkMode").val();
	var params = {};
	// IGMP模式不为 关闭 时，验证其他参数。
	if (igmpWorkMode != 3) {
		var igmpVersion = $("#igmpVersion").val();
		var igmpRobustVariable = $("#igmpRobustVariable").val();
		var maxQueryResponseTime = $("#maxQueryResponseTime").val();
		var lastMemberQueryCount = $("#lastMemberQueryCount").val();
		var queryInterval = $("#queryInterval").val();
		var lastMemberQueryInterval = $("#lastMemberQueryInterval").val();
		var sniType = $("#sniType").val();
		var igmpMaxGroupNum = $("#igmpMaxGroupNum").val();
		var topMcMaxBw = $("#topMcMaxBw").val();
		var topMcSnoopingAgingTime = $("#snoopAgeTime").val();
		var topMcMVlan = mvlanData.join(",");
		var sniPort = "";
		var sniAggPort = "";
	 	if(sniType == 1){
			sniPort = $("#sniPortList").val();
			if(sniPort == null||sniPort == ""){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.sniNotNull);
				return;
		    }
			var tempArray = sniPort.split("/");
			sniPort = tempArray[0]+":"+tempArray[1];
			sniAggPort = "";
		}else if(sniType == 2){
	        sniAggPort = $("#sniPortList").val();
	        if(sniAggPort == null || sniAggPort == "" || sniAggPort == -1){
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.trunkNotNull)
	        	return;
	        }
	        sniPort = "";
		}else if(sniType == 0){
	        sniAggPort = "";
	        sniPort = "";
		}
		if(!checkIgmpMaxGroupNum()){
			Zeta$("igmpMaxGroupNum").focus();
			return ;
		}
		if(!checkIgmpMaxBw()){
			Zeta$("topMcMaxBw").focus();
			return ;
		}
		if(!checkIgmpAgeTime()){
			Zeta$("snoopAgeTime").focus();
			return ;
		}
		if(!checkQueryInterval()){
			Zeta$("queryInterval").focus();
			return ;
		}
		if(!checkMaxQueryResponseTime()){
			Zeta$("maxQueryResponseTime").focus();
			return ;
		}
		if(!checkLastMemberQueryInterval()){
			Zeta$("lastMemberQueryInterval").focus();
			return ;
		}
		
		params = {
			entityId: entityId,
  			igmpWorkMode:igmpWorkMode,
  			igmpVersion:igmpVersion,
  			robustVariable:igmpRobustVariable,
  			maxQueryResponseTime:maxQueryResponseTime,
  			queryInterval:queryInterval,
  			lastMemberQueryCount:lastMemberQueryCount,
  			lastMemberQueryInterval:lastMemberQueryInterval*10,
  			igmpMaxGroupNum:igmpMaxGroupNum,
  			topMcMaxBw:topMcMaxBw,
  			topMcSnoopingAgingTime:topMcSnoopingAgingTime,
  			topMcMVlan:topMcMVlan,
  			sniType:sniType,
  			sniPort:sniPort,
  			sniAggPort:sniAggPort
		};
	} else {
		params = {
			entityId: entityId,
	  		igmpWorkMode:igmpWorkMode
		};
	}
	
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving , 'ext-mb-waiting');
	Ext.Ajax.request({
		url:"modifyIgmpGlobalInfo.tv",
		cache : false,
		success:function(response){
			if(response.responseText == "success"){
				window.parent.closeWaitingDlg();
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.udtGlobalOk)
				if (window.parent.getFrame("entity-" + entityId)) {
					window.parent.getFrame("entity-" + entityId).deviceIgmpMode = parseInt(igmpWorkMode);
					window.parent.getFrame("entity-" + entityId).refreshDeviceMenu(parseInt(igmpWorkMode));
				}
				window.parent.closeWindow('igmpProtocol');
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.udtGlobalError)
			}
		},failure:function (response) {
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.udtGlobalError)
        },params: params
  	});
}
function cancelClick() {
    window.parent.closeWindow('igmpProtocol');
}
function sniTypeChange(){
	var sniTypeTemp = $("#sniType").val();
	var position = Zeta$("sniPortList");
	if(sniTypeTemp == 0){
		$("#sniPortListText").html(I18N.IGMP.aviSni4Igmp)
		$("#sniPortListText").css("width","170");
		$("#sniPortList").hide();
	}else if(sniTypeTemp == 1){//sni口list
		$("#sniPortListText").css("width","100");
		$("#sniPortListText").show();
		$("#sniPortList").show();
		$("#sniPortListText").html(I18N.IGMP.sniLoc);
	    position.options.length = 0;
	    for (var i = 1; i < olt.slotList.length+1; i++) {
		    for(var j = 1; j< olt.slotList[i-1].portList.length+1;j++){
			    var portSubType = olt.slotList[i-1].portList[j-1].portSubType;
			    if("geCopper"==portSubType||"geFiber"==portSubType||"xeFiber"==portSubType){
			        var option = document.createElement('option');
			        if(olt.slotList[i-1].slotLogicNum>9){
			        	option.value = olt.slotList[i-1].slotLogicNum+"/0"+j;
				    }else{
				    	 option.value = "0"+olt.slotList[i-1].slotLogicNum+"/0"+j;
					}
			        option.text = olt.slotList[i-1].slotLogicNum+"/"+j;
			        try {
			            position.add(option, null);
			        } catch(ex) {
			            position.add(option);
			        }
				    }
			    }
	    }
	}else if(sniTypeTemp == 2){//trunk组list
		$("#sniPortListText").html(I18N.IGMP.trunkId)
		$("#sniPortListText").css("width","100");
		$("#sniPortListText").show();
		$("#sniPortList").show();
	    position.options.length = 0;
	    if(TrunkList.data.length == 0){
	    	 var option = document.createElement('option');
		        option.value = -1;
		        option.text = I18N.IGMP.noTrunk
		        try {
		            position.add(option, null);
		        } catch(ex) {
		            position.add(option);
		        }
		}else{
			for (var i = 1; i < TrunkList.data.length+1; i++) {
		        var option = document.createElement('option');
		        option.value = TrunkList.data[i-1].sniTrunkGroupConfigIndex;
		        option.text = TrunkList.data[i-1].sniTrunkGroupConfigIndex;
		        try {
		            position.add(option, null);
		        } catch(ex) {
		            position.add(option);
		        }
		    }
		}
	}
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function igmpWorkModeChange(){
	var modeTemp = $("#igmpWorkMode").val();
	if(modeTemp == 3 ){//关闭
		$(".globalClass").attr("disabled",true);
		$(".globalClass:text").addClass("normalInputDisabled");
		$("select.globalClass").addClass("normalSelDisabled");
	}else{//disabled
		$(".globalClass").attr("disabled",false);
		$(".globalClass:text").removeClass("normalInputDisabled");
		$("select.globalClass").removeClass("normalSelDisabled");
	}
}
function checkIgmpMaxGroupNum(){
	var reg0 = /^([0-9])+$/;
	var maxGroupNum = $("#igmpMaxGroupNum").val();
	if(maxGroupNum == "" || maxGroupNum == null||maxGroupNum>2000||maxGroupNum==0){
		return false;
	}else{
		if(reg0.exec(maxGroupNum)){
			return true;
		}else{
			return false;
		}
	}
}
function checkIgmpMaxBw(){
	var reg0 = /^([0-9])+$/;
	var maxBw = $("#topMcMaxBw").val();
	if(maxBw == "" || maxBw == null||maxBw>1000000||maxBw==0){
		return false;
	}else{
		if(reg0.exec(maxBw)){
			return true;
		}else{
			return false;
		}
	}
}
function checkIgmpAgeTime(){
	var reg0 = /^([0-9])+$/;
	var ageTime = $("#snoopAgeTime").val();
	if(ageTime == "" || ageTime == null||ageTime>30||ageTime==0){
		return false;
	}else{
		if(reg0.exec(ageTime)){
			return true;
		}else{
			return false;
		}
	}
}
function checkQueryInterval(){
	var reg0 = /^([0-9])+$/;
	var queryInterval = $("#queryInterval").val();
	if(queryInterval == "" || queryInterval == null||queryInterval>5000||queryInterval<30){
		return false;
	}else{
		if(reg0.exec(queryInterval)){
			return true;
		}else{
			return false;
		}
	}
}
function checkMaxQueryResponseTime(){
	var reg0 = /^([0-9])+$/;
	var maxQueryResponseTime = $("#maxQueryResponseTime").val();
	if(maxQueryResponseTime == "" || maxQueryResponseTime == null||maxQueryResponseTime>255||maxQueryResponseTime==0){
		return false;
	}else{
		if(reg0.exec(maxQueryResponseTime)){
			return true;
		}else{
			return false;
		}
	}
}
function checkLastMemberQueryInterval(){
	var reg0 = /^([0-9])+$/;
	var lastMemberQueryInterval = $("#lastMemberQueryInterval").val();
	if(lastMemberQueryInterval == "" || lastMemberQueryInterval == null||lastMemberQueryInterval>3||lastMemberQueryInterval==0){
		return false;
	}else{
		if(reg0.exec(lastMemberQueryInterval)){
			return true;
		}else{
			return false;
		}
	}
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.fetching)
	 $.ajax({
	        url: '/epon/igmp/refreshIgmpProtocol.tv',
	        type: 'POST',
	        data: "&entityId=" + entityId + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchingOk)
	            	window.location.reload();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchingError)
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchingError)
	    }, cache: false
	    });
}
//mvlan
var mvlanNeedToHide = true;
function initMvlan(){
	$("#mvlanSel").focus(function(){
		mvlanNeedToHide = false;
	});
	var p = $("#topMcMVlanSpan");
	for(var a=0; a<mvlanData.length; a++){
		if(operationDevicePower){
			p.append(String.format("<font id='mvlanFont{0}'>{0}</font>" +
					"<img id='mvlanImg{0}' src='/images/delete.gif' onclick='delMvlan({0})'>", mvlanData[a]));
		}else{
			p.append(String.format("<font id='mvlanFont{0}'>{0}</font>" +
					"<img id='mvlanImg{0}' src='/images/deleteDisable.gif'>", mvlanData[a]));
		}
	}
	mvlanInputChange();
}
function mvlanFocus(){
	mvlanNeedToHide = false;
	var pos = getElPositionById("topMcMVlan");
	$("#mvlanSel").css({"left": pos[0] + 2, "top": pos[1] + 19, "width": $("#topMcMVlan").width() + 2, "height": 30}).show();
	mvlanKeyup();
}
function mvlanBlur(){
	mvlanNeedToHide = true;
	var v = $("#topMcMVlan").val();
	if(v && checkedMvlan(v) && mvlanData.indexOf(parseFloat(v)) == -1 && mvlanData.length < 4 && vlanList.indexOf(parseFloat(v)) > -1){
		$("#topMcMVlanSpan").append(String.format("<font id='mvlanFont{0}'>{0}</font>" +
				"<img id='mvlanImg{0}' src='/images/delete.gif' onclick='delMvlan({0})'>", v));
		mvlanData.push(parseFloat(v));
	}
	$("#topMcMVlan").val("");
	mvlanInputChange();
	/* setTimeout(function(){
		if(mvlanNeedToHide){
			$("#mvlanSel").hide();
		}
	}, 200); */
}
function mvlanInputChange(){
	$("#topMcMVlan").width(288 - $("#topMcMVlanSpan").width());
}
function delMvlan(v){
	var msg = checkedMvlanUsed(v);
	if(msg){
		//I18N.IGMP.mvlanIsUsing : MVLAN {0} 被组播组: {1} 使用,要删除该MVLAN需先删除这些组播组
		return window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mvlanIsUsing, v, msg));
	}
	$("#mvlanFont" + v).remove();
	$("#mvlanImg" + v).remove();
	mvlanData.splice(mvlanData.indexOf(parseFloat(v)), 1);
	mvlanBlur();
	$("#mvlanSel").css("display","none");
}
function checkedMvlanUsed(v){
	var msg = "";
	var al = mvidList.length;
	for(var a=0; a<al; a++){
		var bl = mvidList[a].length;
		if(bl > 1 && mvidList[a][0] == parseFloat(v)){
			var tmp = mvidList[a].slice(1);
			msg = changeToString(tmp).join(",");
		}
	}
	return msg;
}
function mvlanKeyup(){
	var p = $("#mvlanSel");
	p.empty();
	var v = $("#topMcMVlan").val();
	if(v.length > 2 && (v.indexOf(";") == v.length - 1 || v.indexOf(",") == v.length - 1)){
		v = v.substring(0, v.length - 1);
		$("#topMcMVlan").val(v);
		mvlanBlur();
		$("#topMcMVlan").focus();
		mvlanFocus();
		return;
	}
	if(v != "" && !checkedMvlan(v)){
		p.append(String.format("<span style='width:100%;height:20;'>" + I18N.IGMP.range4094 + "</span>"));
		p.height(22);
		return false;
	}
	var addA = new Array();
	for(var a=0; a<vlanList.length; a++){
		if((vlanList[a] + "").indexOf(v) == 0){
			addA.push(vlanList[a]);
		}
	}
	var addAL = addA.length;
	if(addAL){
		var flag = 0;
		if(addAL > 100){
			flag = addAL - 80;
			addA.length = 80;
		}
		for(var a=0; a<addA.length; a++){
			p.append(String.format("<div id='vlanSpan_{0}' style='width:100%;height:20;cursor:hand;' onclick='vlanSpanClick(this)' " +
					"onfocus='mvlanNeedToHide=false;' " +
					"onmouseover='vlanSpanOver(this)' onmouseout='vlanSpanOut(this)'>VLAN:{0}</div>", parseFloat(addA[a])));
		}
		if(flag){
			p.append(String.format(I18N.IGMP.restVlanTip, flag));
		}
		if(addA.length > 10){
			p.height(180);
		}else{
			p.height(20*addA.length + 2);
		}
		return;
	}else if(vlanVif.indexOf(v) > -1){
		p.append(String.format("<span style='width:100%;height:20;'>" + I18N.IGMP.viUnuse + "</span>"));
	}else{
		p.append(String.format("<span style='width:100%;height:20;'>" + I18N.IGMP.vlanNotExist + "</span>"));
	}
	p.height(22);
}
function vlanSpanOver(el){
	$(el).css("background-color", "#cccccc");
}
function vlanSpanOut(el){
	$(el).css("background", "transparent");
}
function vlanSpanClick(el){
	mvlanNeedToHide = true;
	$("#topMcMVlan").val(parseFloat(el.id.split("_")[1]));
	mvlanBlur();
}
function mvlanKeydown(){
	if(window.event){
		var k = event.keyCode;
		if(k == 13){
			mvlanBlur();
			$("#topMcMVlan").focus();
			mvlanFocus();
		}
	}
}
function checkedMvlan(v){
	if(isNaN(v) || parseFloat(v) < 2 || parseFloat(v) > 4094 || v.indexOf(".") > -1){
		return false;
	}
	return true;
}
//通过id获取元素的位置
function getElPositionById(id){
	var el = document.getElementById(id);
	var x=0;
	while(el) {
		x += el.offsetLeft;
		el = el.offsetParent;
	}
	el = document.getElementById(id);
	var y=0;
	for(var e = el; e; e = e.offsetParent){
		y += e.offsetTop;
	}
	for(e = el.parentNode; e && e != document.body; e=e.parentNode){
		y -= e.scrollTop || 0;
	}
	return [x, y];
}
function changeToString(list){
	var re = new Array();
	if(list.length > 1){
		list.sort(function(a, b){
			return a - b;
		});
		var f = 0;
		var n = 0;
		var ll = list.length;
		for(var i=1; i<ll; i++){
			if(list[i] == list[i - 1] + 1){
				n++;
			}else{
				re = _changeToString(re, f, n, list);
				f = i;
				n = 0;
			}
			if(i == ll - 1){
				re = _changeToString(re, f, n, list);
			}
		}
	}else if(list.length == 1){
		re.push(list[0]);
	}
	return re;
}
function _changeToString(re, f, n, list){
	if(n == 0){
		re.push(list[f]);	
	}else if(n == 1){
		re.push(list[f]);
		re.push(list[f + 1]);
	}else{
		re.push(list[f] + "-" + list[f + n]);
	}
	return re;
}

function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#cancelBt").attr("disabled",false);
		R.okBt.setDisabled(true);
	}
}
$(function(){
	var $sel = $("#mvlanSel");
	$('body').click(function(e){
		if(e.target.id != "topMcMVlan"){
			$sel.css({"display":"none"});
		}
	});
	$("#maxQueryResponseTime").focus(function(){
		$sel.css({"display":"none"});
	})
});
</script>
</head>
<body class="openWinBody" onload="initData();authLoad();">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@IGMP.igmpProtocoSet@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div id=mvlanSel onblur='mvlanBlur()'
		style='display:none;position:absolute;widht:290;border:1px solid #cccccc;background-color:white;overflow-y:scroll;'></div>

	<div class="edge10 pB0 clearBoth tabBody">
		<form onsubmit="return false;">
			<table class="dataTableRows zebra" width="100%" border="1"
				cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="4" class="txtLeftTh">@IGMP.igmpProtocoSet@</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="rightBlueTxt w150">@IGMP.workMode@</td>
						<td><select id="igmpWorkMode" class="normalSel w130" onchange=igmpWorkModeChange()>
								<option value="3">@COMMON.close@</option>
								<option value="2">@IGMP.control@</option>
								<option value="1">@IGMP.agent@</option>
								<option value="4">@IGMP.listen@</option>
						</select></td>
						<td class="rightBlueTxt">@IGMP.igmpVersion@</td>
						<td><select id="igmpVersion" class="globalClass normalSel w130">
								<option value="2">V2</option>
						</select></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@IGMP.maxMvlan@</td>
						<td>
							<input id="igmpMaxGroupNum" type="text" maxlength=4 class="globalClass" tooltip="@IGMP.maxMvlanTip@" /></td>
						<td class="rightBlueTxt">@IGMP.RobustVar@</td>
						<td><select id="igmpRobustVariable" class="globalClass normalSel w130">
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
						</select></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@IGMP.mvlanVlan@</td>
						<td colspan=3><div>
								<nobr>
									<span id='topMcMVlanSpan' style='border: 0px;'></span> 
									<input id="topMcMVlan" maxlength=5 type="text"
										title='@IGMP.maxSupportMvlan@' class="globalClass normalInput w400" onFocus='mvlanFocus()'
										onblur='mvlanBlur()' onkeyup='mvlanKeyup()' onkeydown='mvlanKeydown()' />
								</nobr>
							</div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@IGMP.maxQueryResponse@</td>
						<td><input id="maxQueryResponseTime" class="globalClass normalInput" type="text" maxlength="3"
							tooltip="@IGMP.maxQueryResponseTip@" />(0.1)@COMMON.S@</td>
						<td class="rightBlueTxt">@IGMP.queryTick@</td>
						<td><input id="queryInterval" class="globalClass normalInput" type="text" maxlength=4 tooltip="@IGMP.queryTickTip@" />@COMMON.S@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@IGMP.lastMemberQueryCount@</td>
						<td><select id="lastMemberQueryCount" class="globalClass normalSel w130">
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
						</select></td>
						<td class="rightBlueTxt">@IGMP.leaveTick@</td>
						<td><input id="lastMemberQueryInterval" class="globalClass normalInput" type="text" maxlength=4
							tooltip="@IGMP.leaveTickTip@" />@COMMON.S@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@IGMP.maxMvlanBD@</td>
						<td><input class="globalClass" type="text" id="topMcMaxBw" maxlength=7
							value="" tooltip="@IGMP.range1000000@">Kbps</td>
						<td class="rightBlueTxt">Snooping@IGMP.ageTime@</td>
						<td><input id="snoopAgeTime"
							class="globalClass" type="text" maxlength=2 tooltip="@IGMP.range30@" />@COMMON.S@</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<div class="edge10 pB0 clearBoth tabBody">
		<form onsubmit="return false;">
			<table class="dataTableRows zebra" width="100%" border="1"
				cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="4" class="txtLeftTh">@IGMP.sniCfg@</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="rightBlueTxt w150">@IGMP.sniCfgMtd@</td>
						<td><select id="sniType" class="globalClass normalSel w130" onchange=sniTypeChange()>
								<option value="1">@IGMP.assignSni@</option>
								<option value="2">@IGMP.trunk@</option>
						</select></td>
						<td id="sniPortListText" class="rightBlueTxt"></td>
						<td><select id="sniPortList" class="globalClass normalSel w130"></select></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="okBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>