<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<%@include file="/include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" 
	src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ux/VerticalTabPanel.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var thresholdCm;
var thresholdData = new Array();
var thresholdStore;
var thresholdGrid;
var thresholdNameString = ["", I18N.PERF.moI.InOctets, I18N.PERF.moI.InPkts, I18N.PERF.moI.InBroadcastPkts,
                           I18N.PERF.moI.InMulticastPkts, I18N.PERF.moI.InPkts64Octets, I18N.PERF.moI.InPkts65to127Octets,
                           I18N.PERF.moI.InPkts128to255Octets, I18N.PERF.moI.InPkts256to511Octets, I18N.PERF.moI.InPkts512to1023Octets, 
                           I18N.PERF.moI.InPkts1024to1518Octets, I18N.PERF.moI.InPkts1519to1522Octets,I18N.PERF.moI.InUndersizePkts,
                           I18N.PERF.moI.InOversizePkts, I18N.PERF.moI.InFragments, I18N.PERF.moI.InMpcpFrames, I18N.PERF.moI.InMpcpOctets,
                           I18N.PERF.moI.InOAMFrames, I18N.PERF.moI.InOAMOctets, I18N.PERF.moI.InCRCErrorPkts,
                           I18N.PERF.moI.InDropEvents, I18N.PERF.moI.InJabbers, I18N.PERF.moI.InCollision,
                           I18N.PERF.moI.OutOctets, I18N.PERF.moI.OutPkts, I18N.PERF.moI.OutBroadcastPkts,
                           I18N.PERF.moI.OutMulticastPkts, I18N.PERF.moI.OutPkts64Octets, I18N.PERF.moI.OutPkts65to127Octets,
                           I18N.PERF.moI.OutPkts128to255Octets, I18N.PERF.moI.OutPkts256to511Octets,
                           I18N.PERF.moI.OutPkts512to1023Octets, I18N.PERF.moI.OutPkts1024to1518Octets,
                           I18N.PERF.moI.OutPkts1519to1522Octets, I18N.PERF.moI.OutUndersizePkts, I18N.PERF.moI.OutOversizePkts,
                           I18N.PERF.moI.OutFragments, I18N.PERF.moI.OutMpcpFrames, I18N.PERF.moI.OutMpcpOctets,
                           I18N.PERF.moI.OutOAMFrames, I18N.PERF.moI.OutOAMOctets, I18N.PERF.moI.OutCRCErrorPkts,
                           I18N.PERF.moI.OutDropEvents, I18N.PERF.moI.OutJabbers, I18N.PERF.moI.OutCollision];
var thresholdTempNameString = ["", I18N.PERF.port.mpuaTemperature, I18N.PERF.port.epuaTemperature,
                               I18N.PERF.port.geuaTemperature, I18N.PERF.port.onuTemperature];
var thresholdDlg = null;


//界面临时数据
var regionDeviceList = new Array();	//地域、设备的关系列表,用于生成地域、设备的下拉框
var ipList = new Array();	//IP列表
var ipListLength = 0;
var needToConfig = "none";	//slot、port、onu、uni中的某一项
var portTypeIsPon = false;	//当前端口是否是PON口
var devicePerfData = new Array();	//设备的性能数据,[[int,int,int,int,int,int],[SNI],[PON],[ONU],[UNI],[温度]]
var thresholdModifyData = new Array();	//[entityId, index, type]

Ext.Ajax.timeout = 1200000;

function initFolderListData(){
	Ext.Ajax.request({
		url : '/epon/perf/getFolderDeviceList.tv?r=' + Math.random(),
		success : function(response) {
			var data;
			if(response.responseText == "false"){
				data = new Array();
			}else{
				data = Ext.util.JSON.decode(response.responseText);
			}
			if(data.length > 0){
				regionDeviceList = data.slice(0);
				for(var k=0; k<regionDeviceList.length; k++){
					var tmp = regionDeviceList[k].ips.split(",");
					var tmp2 = regionDeviceList[k].entityIds.split(",");
					var tmpLength = tmp.length;
					var isoverlap;
					for(var t=0; t<tmpLength; t++){
						isoverlap = false;
						for(var x=0; x<ipList.length;x++){
							if(ipList[x].ip == tmp[t]){
								isoverlap = true;
								break;
							}
						}
						if(!isoverlap){
							ipList.push({ip: tmp[t], entityId: tmp2[t]});
							ipListLength++;
						}
					}
				}
				for(var x=0; x<regionDeviceList.length; x++){
					$("#regionSel").append(String.format("<option value='{0}'>{1}</option>", 
							regionDeviceList[x].folderId, regionDeviceList[x].folderName));
				}
			}else{
				$("#regionSel").empty();
				$("#regionSel").append("<option value=-1>" + I18N.PERF.allFolderNoDevice + "</option>");
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.loadDataError);
		}
	});
}
Ext.onReady(function(){
	initButton('all');
	modifyThreshold(0,0,0,0,0,"");
	closeThresholdDlg();
	var h = window.document.body.offsetHeight < 524 ? 524 : window.document.body.offsetHeight;
	var w = window.document.body.offsetWidth;
	initFolderListData();
    
	//$("#thresholdFieldset").css({width: w - 350, height: h - 190});

	thresholdCm = [ {header: I18N.PERF.moI.name, id:'id1', dataIndex: 'thresholdName', align: 'left',
					  sortable: true, width: parseInt((w - 425)/3)}
				  , {header: I18N.PERF.port.thresholdUpper, id:'id2', dataIndex: 'thresholdUpper', align: 'right',
					  sortable: true, width: parseInt((w - 425)/3)}
				  , {header: I18N.PERF.port.thresholdLower, id:'id3', dataIndex: 'thresholdLower', align: 'right',
					  sortable: true, width: parseInt((w - 425)/3)}
				  , {header: I18N.PERF.mo.Index + 'ID', id:'id4', dataIndex: 'thresholdIndex', align: 'center',
					  sortable: true, width: 50, hidden: true}
				  , {header: I18N.PERF.moI.type, id:'id5', dataIndex: 'thresholdType', align: 'center',
					  sortable: true, width: 60, hidden: true}
				  , {header: I18N.COMMON.manu, id:'id6', dataIndex: 'id', align: 'center',
					  width: 50, renderer : thresholdRender}
		          ];
	thresholdStore = new Ext.data.SimpleStore({
		data : thresholdData,
		fields: ['thresholdUpper','thresholdLower','thresholdIndex', 'thresholdType', 'thresholdName', 'entityId']
	});
	thresholdGrid = new Ext.grid.GridPanel({
		id : 'thresholdGrid',
		title : I18N.PERF.moI.type + ': ' + getThresholdTypeSel(),
		renderTo : 'thresholdGridDiv',
		border : true,
		frame : false,
		autoScroll : true,
		width : w - 350,
		height : h - 225,
		store : thresholdStore,
		columns : thresholdCm,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		listeners : {
			'viewready' : function(){

			}
		}
	});
	
});
function getThresholdTypeSel(){
	var re = "";
	re += '<select id="thresholdSel" style="width:110px;" onchange="thresholdSelChange()">';
	re += '<option value=1>' + I18N.PERF.port.oltSniPort + '</option>';
	re += '<option value=2>' + I18N.PERF.port.oltPonPort + '</option>';
	re += '<option value=3>' + I18N.PERF.port.onuPonPort + '</option>';
	re += '<option value=4>' + I18N.PERF.port.onuUniPort + '</option>';
	re += '<option value=5>' + I18N.PERF.port.temperature + '</option>';
	re += '</select>';
	return re;
}
function thresholdRender(value, cellmate, record){
	if(operationDevicePower){
		return String.format("<img src='/images/edit.gif' title='{6}' " + 
				"onclick='modifyThreshold(\"{0}\",\"{1}\",\"{2}\",\"{3}\",\"{4}\",\"{5}\")' />", 
				record.data.thresholdIndex, record.data.thresholdType, record.data.entityId, record.data.thresholdUpper, 
				record.data.thresholdLower, record.data.thresholdName, I18N.PERF.port.modifyThreshold);
	}else{
		return String.format("<img src='/images/editDisable.gif' title='{0}'/>",I18N.PERF.port.modifyThreshold);
	}
}
function modifyThreshold(index, type, entityId, upper, lower, name){
	if (thresholdDlg == null) {
		thresholdDlg = new Ext.Window({
			title: I18N.PERF.port.modifyThreshold, 
			width: 310, 
			height: 200, 
			modal: true, 
			closable: false, 
			plain: false, 
			resizable: false, 
			shadow: true,
			contentEl: 'thresholdDlgDiv',
			listeners : {
				'beforeclose' : function(){
					closeThresholdDlg();
					return false;
				}
			}
		});
	}
	thresholdModifyData = [entityId, index, type];
	$("#thresholdUpperInput").val(upper);
	$("#thresholdLowerInput").val(lower);
	$("#thresholdNameSpan").text(name);
	if(index == 50){
		document.getElementById("thresholdUpperInput").title = I18N.PERF.port.plsInput085;
		document.getElementById("thresholdUpperInput").maxLength = 2;
		document.getElementById("thresholdLowerInput").title = I18N.PERF.port.plsInput085;
		document.getElementById("thresholdLowerInput").maxLength = 2;
	}else{
		document.getElementById("thresholdUpperInput").title = I18N.PERF.port.plsInput0;
		document.getElementById("thresholdUpperInput").maxLength = 20;
		document.getElementById("thresholdLowerInput").title = I18N.PERF.port.plsInput0;
		document.getElementById("thresholdLowerInput").maxLength = 20;
	}
	thresholdDlg.show();
	$("#thresholdDlgDiv").show();
}
function closeThresholdDlg(){
	if (thresholdDlg != null) {
		thresholdDlg.hide();
	}
}
//获取设备的性能参数
function loadDevicePerfData(){
	var deviceId = -1;
	if($("#deviceSel").css("display") == 'none'){
		var ip = $("#deviceInput").val();
		for(var x=0; x<ipList.length; x++){
			if(ip == ipList[x].ip){
				deviceId = ipList[x].entityId;
			}
		}
	}else{
		deviceId = $("#deviceSel").val();
	}
	Ext.Ajax.request({
		url : '/epon/perf/getDevicePerfData.tv?r=' + Math.random() + '&entityId=' + deviceId,
		success : function(response){
			if(response.responseText == "false"){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.loadDataError);
				return;
			}
			var data = Ext.util.JSON.decode(response.responseText);
			var tmpList = [5, 10, 15, 30, 60, 100, 300, 900];
			if(tmpList.indexOf(data[0][0]) > -1){
				$("#oltPerfCollectInput").val(data[0][0][0]);
			}
			if(tmpList.indexOf(data[0][1]) > -1){
				$("#onuPerfCollectInput").val(data[0][0][1]);
			}
			$("#oltTempCollectInput").val(data[0][0][2]);
			$("#onuTempCollectInput").val(data[0][0][3]);
			$("#15minRecordInput").val(data[0][0][4]);
			$("#24hourRecordInput").val(data[0][0][5]);
			//阈值
			devicePerfData = new Array();
			devicePerfData[0] = data[0].slice(0);
			if(data.length < 6){
				for(var k=0; k<6-data.length; k++){
					data.push(new Array());
				}
			}
			for(var v=1; v<6; v++){
				devicePerfData[v] = new Array();
			}
			for(var u=1; u<6; u++){
				if(typeof data[u] != 'object'){
					continue;
				}
				var tmpL1 = data[u].length;
				if(tmpL1 > 0){
					if(data[u][0][3] == 1 || data[u][0][3] == 2 || data[u][0][3] == 3 || data[u][0][3] == 4){
						var f = data[u][0][3];
						if(data[u][0][2] == 50){
							f = 5;
						}
						devicePerfData[f] = data[u].slice(0);
						for(var x=0; x<tmpL1; x++){
							if(devicePerfData[f][x].length == 4){
								if(data[u][0][2] == 50){
									devicePerfData[f][x].push(thresholdTempNameString[devicePerfData[f][x][3]]);
								}else{
									devicePerfData[f][x].push(thresholdNameString[devicePerfData[f][x][2]]);
								}
								devicePerfData[f][x].push(deviceId);
							}
						}
					}
				}
			}
			thresholdSelChange();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.loadDataError);
		}		
	});
}
//selectChange的联动
function loadSelData(s, selV){//1:getSlot, 2:getPort, 3:getOnu, 4:getUni
	if(s == 3 && !portTypeIsPon){
		$("#onuSel").empty();
		$("#onuSel").append("<option value=-1>" + I18N.PERF.noOnu + "</option>");
		changeDivInfo(s);
		return;
	}
	var params = {selType : s};
	if(s == 1){
		if($("#deviceSel").css("display") == "none"){
			var tmpIp = $("#deviceInput").val();
			for(var i=0; i<ipListLength; i++){
				if(ipList[i].ip == tmpIp){
					params.entityId = ipList[i].entityId;
				}
			}
		}else{
			params.entityId = $("#deviceSel").val();
		}
	}else if(s == 2){
		params.slotId = parseInt($("#slotSel").val());
	}else if(s == 3){
		params.portId = parseInt($("#portSel").val());
	}else if(s == 4){
		params.onuId = parseInt($("#onuSel").val());
	}
	Ext.Ajax.request({
		url : '/epon/perf/getNumListById.tv?r=' + Math.random(),
		success : function(response) {
			var data;
			if(response.responseText == "false"){
				data = new Array();
			}else{
				data = Ext.util.JSON.decode(response.responseText);
			}
			var sel;
			var noneTip = "";
			if(s == 1){
				sel = $("#slotSel");
				loadDevicePerfData();
				noneTip = I18N.PERF.noSlot;
			}else if(s == 2){
				sel = $("#portSel");
				noneTip = I18N.PERF.noPort;
			}else if(s == 3){
				sel = $("#onuSel");
				noneTip = I18N.PERF.noOnu;
			}else if(s == 4){
				sel = $("#uniSel");
				noneTip = I18N.PERF.noUni;
			}
			sel.empty();
			if(data.length > 0){
				if(s == 2){
					if(data[0].portType == 2){
						portTypeIsPon = true;
					}else{
						portTypeIsPon = false;
					}
				}
				sel.append("<option value=-1>" + I18N.COMMON.select + "</option>");
				if(data.length > 1){
					sel.append("<option value=0>" + I18N.COMMON.all + "</option>");
				}
				for(var a=0; a<data.length; a++){
					if(s == 1 && data[a].num == 0){
						data[a].num = I18N.EPON.mpua;
					}
					sel.append(String.format("<option value={0}>{1}</option>", data[a].id, data[a].num));
				}
				sel.data("data", data);
			}else{
				sel.append(String.format("<option value=-1>{0}</option>", noneTip));
			}
			if(selV != null){
				changeDivInfo(s, selV);
			}else{
				changeDivInfo(s);
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.loadDataError);
		},
		params : params
	});
}
//1:deviceChange, 2:slotChange, 3:portChange, 4:onuChange, 5:uniChange
function changeDivInfo(s, selV){
	$("#initTab").hide();
	$("#deviceTab").hide();
	$("#portTab").show();
	$("#refreshBt").show();
	if(s == 1){
		$("#portTab").hide();
		$("#deviceTab").show();
		needToConfig = "device";
	}else if(s == 2){
		$(".15min24hourClass").hide();
		$(".temperatureClass").show();
		$("#temperatureStatSpan").html(getStatTextString(s, 3));
		needToConfig = "slot";
	}else if(s == 3){
		$(".15min24hourClass").show();
		$(".temperatureClass").hide();
		$("#15minStatSpan").html(getStatTextString(s, 1));
		$("#24hourStatSpan").html(getStatTextString(s, 2));
		if(portTypeIsPon){
			needToConfig = "pon";
		}else{
			needToConfig = "sni";
		}
	}else if(s == 4){
		$(".15min24hourClass").show();
		$(".temperatureClass").show();
		$("#15minStatSpan").html(getStatTextString(s, 1));
		$("#24hourStatSpan").html(getStatTextString(s, 2));
		$("#temperatureStatSpan").html(getStatTextString(s, 3));
		needToConfig = "onu";
	}else if(s == 5){
		$(".15min24hourClass").show();
		$(".temperatureClass").hide();
		$("#15minStatSpan").html(getStatTextString(s, 1));
		$("#24hourStatSpan").html(getStatTextString(s, 2));
		needToConfig = "uni";
	}
	if(selV != null){
		if(s == 1){
			$("#slotSel").val(selV);
			slotChange();
		}else if(s == 2){
			$("#portSel").val(selV);
			portChange();
		}else if(s == 3){
			$("#onuSel").val(selV);
			onuChange();
		}else if(s == 4){
			$("#uniSel").val(selV);
			uniChange();
		}
	}
}
function regionChange(){
	var reg = $("#regionSel").val();
	$("#deviceSel").empty();
	$("#deviceSel").append("<option value=-1>" + I18N.COMMON.select + "</option>");
	if(reg == 0){
		$("#deviceSel").hide();
		$("#deviceInput").val("");
		$("#deviceInput").show();
		$("#refreshBt").hide();
		backTheSelect(1);
	}else{
		$("#deviceInput").hide();
		for(var x=0; x<regionDeviceList.length; x++){
			if(reg == regionDeviceList[x].folderId){
				var tmp = regionDeviceList[x].ips.split(",");
				var tmp2 = regionDeviceList[x].entityIds.split(",");
				for(var y=0; y<tmp.length; y++){
					$("#deviceSel").append(String.format("<option value='{0}'>{1}</option>", tmp2[y], tmp[y]));
				}
			}
		}
		$("#deviceSel").show();
		deviceChange(2);
	}
	$("#initTab").show();
	$("#deviceTab").hide();
	$("#portTab").hide();
	changeLocationSpan();
}
function deviceChange(s){//1:input, 2:select
	var ip = I18N.COMMON.select;
	var flag = true;
	if(s == 1){
		ip = $("#deviceInput").val();
	}else if(s == 2){
		if($("#deviceSel").val() == -1){
			ip = "";
		}else{
			ip = $("#deviceSel").find("option:selected").text();
		}
	}else{
		ip = "";
	}
	backTheSelect(1);
	for(var x=0; x<regionDeviceList.length; x++){
		var tmp = regionDeviceList[x].ips.split(",");
		if(ip != "" && tmp.indexOf(ip) > -1){
			loadSelData(1);
			flag = false;
		}
	}
	if(flag){
		$("#initTab").show();
		$("#deviceTab").hide();
		$("#portTab").hide();
		$("#locationSpan").text("");
		$("#refreshBt").hide();
	}else{
		changeLocationSpan();
	}
}
function slotChange(){
	var slotNo = $("#slotSel").find("option:selected").text();
	var slotId = $("#slotSel").val();
	backTheSelect(2);
	if(slotId == -1){
		if($("#deviceSel").css("display") == 'none'){
			deviceChange(1);
		}else{
			deviceChange(2);
		}
	}else if(slotId == 0){
		changeDivInfo(2);
	}else{
		loadSelData(2);
	}
	changeLocationSpan();
}
function portChange(){
	var portId = $("#portSel").val();
	backTheSelect(3);
	if(portId == -1){
		slotChange();
	}else if(portId == 0){
		changeDivInfo(3);
	}else{
		loadSelData(3);
	}
	changeLocationSpan();
}
function onuChange(){
	var onuId = $("#onuSel").val();
	backTheSelect(4);
	if(onuId == -1){
		portChange();
	}else if(onuId == 0){
		changeDivInfo(4);
	}else{
		loadSelData(4);
	}
	changeLocationSpan();
}
function uniChange(){
	var uniId = $("#uniSel").val();
	if(uniId == -1){
		onuChange();
	}else{
		changeDivInfo(5);
	}
	changeLocationSpan();
}
function backTheSelect(s){//1:device 2:slot 3:port 4:onu
	$("#uniSel").empty();
	$("#uniSel").append("<option value=-1>" + I18N.COMMON.select + "</option>");
	if(s < 4){
		$("#onuSel").empty();
		$("#onuSel").append("<option value=-1>" + I18N.COMMON.select + "</option>");
	}
	if(s < 3){
		$("#portSel").empty();
		$("#portSel").append("<option value=-1>" + I18N.COMMON.select + "</option>");
	}
	if(s < 2){
		$("#slotSel").empty();
		$("#slotSel").append("<option value=-1>" + I18N.COMMON.select + "</option>");
	}
}

//deviceSel
function deviceInputKeyup(){
	deviceInputFocus();
}
function deviceInputFocus(){
	var flagNum = 0;
	var ip = $("#deviceInput").val();
	$("#deviceInputSel").empty();
	var tmpIpList = new Array();
	for(var x=0; x<ipListLength; x++){
		if(ipList[x].ip.indexOf(ip) > -1 || ip == ""){
			tmpIpList.push(ipList[x].ip);
			flagNum++;
		}
	}
	if(flagNum == 0){
		$("#deviceInputSel").append("<div>" + I18N.PERF.noMatchIp + "</div>");
	}else if(flagNum > 500){
		for(var x=0; x<200; x++){
			$("#deviceInputSel").append(getDeviceInputSel(tmpIpList[x]));
		}
		$("#deviceInputSel").append(String.format("<div>" + I18N.PERF.hasMatchIp + "</div>", parseInt(flagNum - 200)));
	}else{
		for(var x=0; x<tmpIpList.length; x++){
			$("#deviceInputSel").append(getDeviceInputSel(tmpIpList[x]));
		}
	}
	if(flagNum > 20){
		$("#deviceInputSel").css({'height': 400, 'overflow': 'auto'});
	}else{
		$("#deviceInputSel").css({'height': (20*flagNum+2), 'overflow': 'auto'});
	}
	$("#deviceInputSel").show();
}
function deviceInputBlur(){
	if(document.activeElement.id != "deviceInputSel" && document.activeElement.id.substring(0,3) != 'sel'){
		$("#deviceInputSel").hide();
	}
}
function deviceInputSelBlur(){
	if(document.activeElement.id != "deviceInput" && document.activeElement.id.substring(0,3) != 'sel'){
		$("#deviceInputSel").hide();
	}
}
function inputSelBlur(){
	if(document.activeElement.id != "deviceInput" && document.activeElement.id.substring(0,3) != 'sel'
		&& document.activeElement.id != "deviceInputSel"){
		$("#deviceInputSel").hide();
	}
}
function getDeviceInputSel(v){
	var re = "";
	re = String.format("<div id='sel{0}' onmouseover='inputSelMouseOver(this.id)' onmouseout='inputSelMouseOut(this.id)' " + 
		"onclick='inputSelClick(this.id)' onblur='inputSelBlur()' style='height:20px;cursor:hander;' >{0}</div>", v);
	return re;
}
function inputSelMouseOver(id){
	document.getElementById(id).style.backgroundColor = '#dfdfdf';
}
function inputSelMouseOut(id){
	document.getElementById(id).style.backgroundColor = '';
}
function inputSelClick(id){
	$("#deviceInput").val(id.substring(3));
	deviceChange(1);
	$("#deviceInputSel").hide();
}

//公用
function comperString(upper, lower){	// a < b return true
	var len1 = upper.length
	var len2 = lower.length
	if(len1 > len2){
		return false
	}
	if(len1 < len2){
		return true
	}
	//如果上下限都是 0 的话,则不要求上限大于下限
	if((upper == "0" && lower == "0") || upper == lower ) 
		return false
	//如果上下限不为0,则必须上限大于下限
	var cursor = 0
	while(cursor < len1){
		var m = upper.charAt(cursor)
		var n = lower.charAt(cursor)
		if(m>n)
			return false
		if(m<n)
			return true
		cursor++
	}
	return true	
}
function checkedInputNum(id){
	//$("#" + id).css("border", "1px solid red");
	var v = $("#" + id).val();
	//var reg = /^([1-9][0-9]{0,20})+$/ig;
	var reg = /^[0-9]\d*$/;
	if(reg.exec(v)){
		//$("#" + id).css("border", "1px solid #ccc");
		return true;
	}
	return false;
}
function changeLocationSpan(){
	var text = "";
	//device
	var ip = "";
	if($("#deviceSel").css("display") == "none"){
		ip = $("#deviceInput").val();
	}else if($("#deviceSel").val() != -1){
		ip = $("#deviceSel").find("option:selected").text();
	}
	if(ip == ""){
		$("#locationSpan").text("");
		return;
	}else{
		text += I18N.PERF.device + ": " + ip;
	}
	//slot
	if($("#slotSel").val() < 1){
		if($("#slotSel").val() == 0){
			text += " " + I18N.PERF.sAllSlot;
		}
		$("#locationSpan").text(text);
		return;
	}else{
		text += "   >> " + I18N.PERF.slot + ": " + $("#slotSel").find("option:selected").text();
	}
	//port
	if($("#portSel").val() < 1){
		if($("#portSel").val() == 0){
			text += " " + I18N.PERF.sAllPort;
		}
		$("#locationSpan").text(text);
		return;
	}else{
		text += "  >> " + I18N.PERF.ports + ": " + $("#portSel").find("option:selected").text();
	}
	//onu
	if($("#onuSel").val() < 1){
		if($("#onuSel").val() == 0){
			text += " " + I18N.PERF.sAllOnu;
		}
		$("#locationSpan").text(text);
		return;
	}else{
		text += "  >> ONU: " + $("#onuSel").find("option:selected").text();
	}
	//uni
	if($("#uniSel").val() < 1){
		if($("#uniSel").val() == 0){
			text += " " + I18N.PERF.sAllUni;
		}
		$("#locationSpan").text(text);
		return;
	}else{
		text += "  >> UNI: " + $("#uniSel").find("option:selected").text();
	}
	//end
	$("#locationSpan").text(text);
}
//使能状态的html: text/img
//s:2:slot, 3:port, 4:onu, 5:uni
//a:1:15min, 2:24hour, 3:temperature
function getStatTextString(s, a){
	var re = "";
	var text = I18N.PERF.port.closed;
	var color = "red";
	var sel;
	if(s == 2){
		sel = $("#slotSel");
	}else if(s == 3){
		sel = $("#portSel");
	}else if(s == 4){
		sel = $("#onuSel");
	}else if(s == 5){
		sel = $("#uniSel");
	}
	if(sel.val() == -1){
		return "";
	}
	if(sel.val() == 0){
		return getStatImgString(s, a);
	}
	var stat = 0;
	for(var tt=0; tt<sel.data("data").length; tt++){
		if(sel.val() == sel.data("data")[tt].id){
			stat = sel.data("data")[tt].stat;
		}
	}
	var tmp1 = [4, 2, 1];
	var tmp1L = tmp1.length;
	var tmp2 = new Array();
	for(var x=0; x<tmp1L; x++){
		if(stat >= tmp1[x]){
			stat -= tmp1[x];
			tmp2.push(tmp1L - x);
		}
	}
	if(tmp2.length > 0 && tmp2.indexOf(a) > -1){
		text = I18N.PERF.port.opened;
		color = "green";
	}
	re = String.format("<font style='color:{1}'>{0}</font>", text, color);
	return re;
}
//s:2:slot, 3:port, 4:onu, 5:uni
//a:1:15min, 2:24hour, 3:temperature
function getStatImgString(s, a){
	var sel;
	if(s == 2){
		sel = $("#slotSel");
	}else if(s == 3){
		sel = $("#portSel");
	}else if(s == 4){
		sel = $("#onuSel");
	}else if(s == 5){
		sel = $("#uniSel");
	}
	var re = "";
	var text;
	var img = "";
	var data = sel.data("data");
	for(var x=0; x<data.length; x++){
		var stat = parseInt(data[x].stat);
		var num = parseInt(data[x].num);
		var tmp1 = [4, 2, 1];
		var tmp1L = tmp1.length;
		var tmp2 = new Array();
		for(var y=0; y<tmp1L; y++){
			if(stat >= tmp1[y]){
				stat -= tmp1[y];
				tmp2.push(tmp1L - y);
			}
		}
		if(tmp2.length > 0 && tmp2.indexOf(a) > -1){
			text = I18N.PERF.port.opened;
			img = "/images/network/port/up.png";
		}else{
			text = I18N.PERF.port.closed;
			img = "/images/network/port/down.png";
		}
		re += String.format("<img src='{0}' title='{1}' /><span style='width:25px;'>{2}</span>", img, text, num);
		if((x + 1) % 8 == 0){
			re += "<br>";
		}
	}
	return re;
}
//阈值的方法
function thresholdSelChange(){
	thresholdStore.loadData(devicePerfData[parseInt($("#thresholdSel").val())]);
}

/**
 * 交互
 */
function refreshBtClick(){
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.COMMON.fetchConfirm, function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
		var tmpK = $("#deviceSel").css("display") == "none" ? 1 : 2;
		var deviceId;
		if(tmpK == 1){	//input
			for(var x=0; x<ipListLength; x++){
				if(ipList[x].ip == $("#deviceInput").val()){
					deviceId = ipList[x].entityId;
				}
			}
		}else{
			deviceId = $("#deviceSel").val();
		}
		Ext.Ajax.request({
			url : '/epon/refreshOlt.tv?r=' + Math.random() + '&entityId=' + deviceId,
			success : function(response){
				if(response.responseText != "success"){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
				deviceChange(tmpK);
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
			}
		});
	});
}
function openEnableStat(s){
	if(needToConfig == "slot" || needToConfig == "sni" || needToConfig == "onu" || needToConfig == "uni" || needToConfig == "pon"){
		setEnableStatPre(s, 1, needToConfig);
	}
}
function closeEnableStat(s){
	if(needToConfig == "slot" || needToConfig == "sni" || needToConfig == "onu" || needToConfig == "uni" || needToConfig == "pon"){
		setEnableStatPre(s, 2, needToConfig);
	}
}
//a: 1:开启,2:关闭
//s: 1:15min, 2:24hour, 3:temperature
function setEnableStatPre(s, a, type){
	var tmpK = $("#deviceSel").css("display") == "none" ? 1 : 2;
	var deviceId;
	if(tmpK == 1){	//input
		for(var x=0; x<ipListLength; x++){
			if(ipList[x].ip == $("#deviceInput").val()){
				deviceId = ipList[x].entityId;
			}
		}
	}else{
		deviceId = $("#deviceSel").val();
	}
	var idList = new Array();
	var sel;
	if(type == 'slot'){
		sel = $("#slotSel");
	}else if(type == 'pon' || type == 'sni'){
		sel = $("#portSel");
	}else if(type == 'onu'){
		sel = $("#onuSel");
	}else if(type == 'uni'){
		sel = $("#uniSel");
	}
	if(sel.val() == 0){
		var tmpD = sel.data("data");
		for(var t=0; t<tmpD.length; t++){
			idList.push(tmpD[t].id);
		}
	}else if(sel.val() != -1){
		idList.push(parseInt(sel.val()));
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.PERF.port.settingStat, 'ext-mb-waiting');
	var setableList = new Array();
	setEnableStat(s, a, type, deviceId, idList, setableList);
}
function setEnableStat(s, a, type, deviceId, idList, setableList){
	if(idList.length > 0){
		var params = {entityId: deviceId ,configEnable : s, enableStat: a, configType : type};
		if(type == 'slot'){
			params.slotId = parseInt(idList[0]);
		}else if(type == 'pon' || type == 'sni'){
			params.portId = parseInt(idList[0]);
		}else if(type == 'onu'){
			params.onuId = parseInt(idList[0]);
		}else if(type == 'uni'){
			params.uniId = parseInt(idList[0]);
		}
		Ext.Ajax.request({
			url : '/epon/perf/setEnableStat.tv?r=' + Math.random(),
			success : function(response){
				if(response.responseText != "success"){
					setEnableFailed(s, a, type, deviceId, idList, setableList);
					return;
				}
				setEnableSuccess(s, a, type, deviceId, idList, setableList);
			},
			failure : function() {
				setEnableFailed(s, a, type, idList, setableList);
			},
			params : params
		});
	}else if(setableList.length > 0){
		var str = a == 1 ? I18N.COMMON.open : I18N.COMMON.close;
		if(setableList.length > 1){
			var tmpX = {slot: ' ' + I18N.PERF.slot + ':  ', sni: I18N.PERF.sniPort + ': ',
					pon: I18N.PERF.ponPort + ': ', onu: ' ONU: ', uni: I18N.PERF.uniPort + ': '};
			var tmpN = 0;
			str += tmpX[type];
			for(var k=0; k<setableList.length; k++){
				if(setableList[k].result == I18N.COMMON.ok){
					if(tmpN > 0){
						str += ",";
					}
					str += setableList[k].num;
					tmpN++;
				}
			}
			if(tmpN > 0){
				str += I18N.PERF.de;
				str += s == 1 ? I18N.PERF.port.x15minStat : s == 2 ? I18N.PERF.port.x24hourStat : I18N.PERF.port.temperatureStat;
				str += I18N.COMMON.ok + '!' + (a == 1 ? I18N.COMMON.open : I18N.COMMON.close) + tmpX[type];
			}
			tmpN = 0;
			for(var k=0; k<setableList.length; k++){
				if(setableList[k].result == I18N.COMMON.error){
					if(tmpN > 0){
						str += ",";
					}
					str += setableList[k].num;
					tmpN++;
				}
			}
			if(tmpN > 0){
				str += I18N.PERF.de;
				str += s == 1 ? I18N.PERF.port.x15minStat : s == 2 ? I18N.PERF.port.x24hourStat : I18N.PERF.port.temperatureStat;
				str += I18N.COMMON.error + '!';
			}else{
				str = str.substring(0, str.length - 7);
			}
		}else{
			str += I18N.PERF.de;
			str += s == 1 ? I18N.PERF.port.x15minStat : s == 2 ? I18N.PERF.port.x24hourStat : I18N.PERF.port.temperatureStat;
			str += setableList[0].result + "!";
		}
		window.parent.showMessageDlg(I18N.COMMON.tip, str);
		var selV = 0;
		if(type == 'slot'){
			if(setableList.length == 1){
				selV = $("#slotSel").val();
			}
			loadSelData(1, selV);
		}else if(type == 'pon' || type == 'sni'){
			if(setableList.length == 1){
				selV = $("#portSel").val();
			}
			loadSelData(2, selV);
		}else if(type == 'onu'){
			if(setableList.length == 1){
				selV = $("#onuSel").val();
			}
			loadSelData(3, selV);
		}else if(type == 'uni'){
			if(setableList.length == 1){
				selV = $("#uniSel").val();
			}
			loadSelData(4, selV);
		}
	}else{
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.unknownError);
	}
}
function setEnableFailed(s, a, type, deviceId, idList, setableList){
	var sel;
	if(type == 'slot'){
		sel = $("#slotSel");
	}else if(type == 'pon' || type == 'sni'){
		sel = $("#portSel");
	}else if(type == 'onu'){
		sel = $("#onuSel");
	}else if(type == 'uni'){
		sel = $("#uniSel");
	}
	var tmpD = sel.data("data");
	for(var t=0; t<tmpD.length; t++){
		if(idList[0] == tmpD[t].id){
			setableList.push({id: tmpD[t].id, num: tmpD[t].num, result: I18N.COMMON.error});
			idList.splice(0, 1);
			break;
		}
	}
	setEnableStat(s, a, type, deviceId, idList, setableList);
}
function setEnableSuccess(s, a, type, deviceId, idList, setableList){
	var sel;
	if(type == 'slot'){
		sel = $("#slotSel");
	}else if(type == 'pon' || type == 'sni'){
		sel = $("#portSel");
	}else if(type == 'onu'){
		sel = $("#onuSel");
	}else if(type == 'uni'){
		sel = $("#uniSel");
	}
	var tmpD = sel.data("data");
	for(var t=0; t<tmpD.length; t++){
		if(idList[0] == tmpD[t].id){
			setableList.push({id: tmpD[t].id, num: tmpD[t].num, result: I18N.COMMON.ok});
			idList.splice(0, 1);
			break;
		}
	}
	setEnableStat(s, a, type, deviceId, idList, setableList);
}
//修改阈值
function modifyThresholdClick(){
	var upper = $("#thresholdUpperInput").val() + "";
	var lower = $("#thresholdLowerInput").val() + "";
	if(parseInt(upper.length) == 20){
		var sub_perfThresholdUpper1 = upper.substring(0,10);
		var sub_perfThresholdUpper2 = upper.substring(10,20);
		if(parseInt(sub_perfThresholdUpper1) > 1844674407){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.thresholdValUpper );
			return;
		}
		if(parseInt(sub_perfThresholdUpper1) == 1844674407 && parseInt(sub_perfThresholdUpper2) > 3709551615 ){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.thresholdValUpper );
			return;
		}
	}
	if(thresholdModifyData[1] == 50){
		if(parseInt(upper) > 85){
			$("#thresholdUpperInput").focus();
			return;
		}
		if(parseInt(lower) > 85){
			$("#thresholdLowerInput").focus();
			return;
		}
	}
	if(!checkedInputNum("thresholdUpperInput")){
		$("#thresholdUpperInput").focus();
		return;
	}
	if(!checkedInputNum("thresholdLowerInput")){
		$("#thresholdLowerInput").focus();
		return;
	}
	if(comperString(upper, lower)){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.thresholdValError);
		$("#thresholdLowerInput").focus();
		return;
	}
	var typeStr = "";
	if(thresholdModifyData[1] == 50){
		typeStr = "TEMPERATURE";
	}else{
		switch(thresholdModifyData[2]){
		case 1: typeStr = "SNI";
			break;
		case 2: typeStr = "PON";
			break;
		case 3: typeStr = "ONUPON";
			break;
		case 4: typeStr = "ONUUNI";
			break;
		}
	}
	var params = {entityId: thresholdModifyData[0], perfThresholdTypeIndex: thresholdModifyData[1], 
				perfThresholdObject: thresholdModifyData[2], perfThresholdUpper: upper,
				perfThresholdLower: lower, thresholdType: typeStr};
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.PERF.port.modifyingThreshold, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : '/epon/perf/modifyPerfThreshold.tv?r=' + Math.random(),
		success : function(response){
			if(response.responseText == "success"){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyThresholdSuccess);
				deviceChange($("#deviceSel").css("display") == "none" ? 1 : 2);
				closeThresholdDlg();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyThresholdFailed);
			}
		},
		failure : function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyThresholdFailed);
		},
		params : params
	});
}
//采集记录数设置
function recordBtClick(){
	var s = $("#deviceSel").css("display") == "none" ? 1 : 2;
	var deviceId;
	if(s == 1){	//input
		for(var x=0; x<ipListLength; x++){
			if(ipList[x].ip == $("#deviceInput").val()){
				deviceId = ipList[x].entityId;
			}
		}
	}else{
		deviceId = $("#deviceSel").val();
	}
	var record15 = parseInt($("#15minRecordInput").val());
	var record24 = parseInt($("#24hourRecordInput").val());
	if(!checkedInputNum("15minRecordInput") || record15 < 1 || record15 > 96){
		$("#15minRecordInput").focus();
		return;
	}
	if(!checkedInputNum("24hourRecordInput") || record24 < 1 || record24 > 30){
		$("#24hourRecordInput").focus();
		return;
	}
	var params = {entityId: deviceId, perfStats15MinMaxRecord: record15, perfStats24HourMaxRecord: record24};
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.PERF.port.modifyingPerfRecord, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : '/epon/perf/savePerfStatsGlobalSet.tv?r=' + Math.random(),
		success : function(response){
			if(response.responseText == "success"){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyPerfRecordSuccess);
				deviceChange(s);
				closeThresholdDlg();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyPerfRecordFailed);
			}
		},
		failure : function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyPerfRecordFailed);
		},
		params : params
	});
}
//采集周期设置
function collectionBtClick(){
	var s = $("#deviceSel").css("display") == "none" ? 1 : 2;
	var deviceId;
	if(s == 1){	//input
		for(var x=0; x<ipListLength; x++){
			if(ipList[x].ip == $("#deviceInput").val()){
				deviceId = ipList[x].entityId;
			}
		}
	}else{
		deviceId = $("#deviceSel").val();
	}
	var oltPerfCycle = $("#oltPerfCollectInput").val();
	var onuPerfCycle = $("#onuPerfCollectInput").val();
	var oltTempCycle = $("#oltTempCollectInput").val();
	var onuTempCycle = $("#onuTempCollectInput").val();
	if(!checkedInputNum("oltTempCollectInput") || oltTempCycle < 1 || oltTempCycle > 10){
		$("#oltTempCollectInput").focus();
		return;
	}
	if(!checkedInputNum("onuTempCollectInput") || onuTempCycle < 1 || onuTempCycle > 10){
		$("#onuTempCollectInput").focus();
		return;
	}
	var params = {entityId: deviceId, topPerfStatOLTCycle: oltPerfCycle, topPerfStatONUCycle: onuPerfCycle,
				topPerfOLTTemperatureCycle: oltTempCycle, topPerfONUTemperatureCycle: onuTempCycle};
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.PERF.port.modifyingPerfCycle, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : '/epon/perf/savePerfStatCycle.tv?r=' + Math.random(),
		success : function(response){
			if(response.responseText == "success"){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyPerfCycleSuccess);
				deviceChange(s);
				closeThresholdDlg();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyPerfCycleFailed);
			}
		},
		failure : function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyPerfCycleFailed);
		},
		params : params
	});
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#regionSel").attr("disabled",false);
		$("#deviceInput").attr("disabled",false);
		$("#slotSel").attr("disabled",false);
		$("#portSel").attr("disabled",false);
		$("#onuSel").attr("disabled",false);
		$("#uniSel").attr("disabled",false);
		$("#deviceSel").attr("disabled",false);
		$("#refreshBt").attr("disabled",false);
	}
}
</script>
</head>
<body class=BLANK_WND style="margin: 15px;" onload="authLoad()">
<div class=formtip id=tips style="display: none"></div>
<!-- 设备input下拉框 -->
<div id=deviceInputSel onblur='deviceInputSelBlur()'
style="display:none;position:absolute;border:1px solid #ccc;width:147;top:46;left:369;background-color:white;z-index:100;">
</div>
<!--  开始布局 -->
<table><tr><td width=100%>
	<div id=searchDiv>
		<table>
			<tr height=35><td>
				<div>
					<span style="margin-left:10px;"><fmt:message bundle="${i18n}" key="PERF.folder" /></span>&nbsp;&nbsp;
					<select id=regionSel style="width:180px;" onchange='regionChange()'>
						<option value=0><fmt:message bundle="${i18n}" key="PERF.allDevice" /></option>
					</select>&nbsp;&nbsp;
					<span style="margin-left:83px;"><fmt:message bundle="${i18n}" key="PERF.device" /></span>&nbsp;
					<input id=deviceInput style="width:147;margin-left:5px;" maxlength=15 onfocus='deviceInputFocus()' 
						onblur='deviceInputBlur()' onkeyup='deviceInputKeyup()' onchange=deviceChange(1) />
					<select id=deviceSel style="width:150px;display:none;" onchange=deviceChange(2)>
						<option value=-1><fmt:message bundle="${i18n}" key="COMMON.select" /></option>
					</select>
				</div>
			</td></tr>
			<tr height=35><td>
				<div>
					<span style="margin-left:10px;"><fmt:message bundle="${i18n}" key="PERF.slot" /></span>&nbsp;&nbsp;
					<select id=slotSel style="width:80px;" onchange='slotChange()'>
						<option value=-1><fmt:message bundle="${i18n}" key="COMMON.select" /></option>
					</select>&nbsp;&nbsp;
					<span style="margin-left:30px;"><fmt:message bundle="${i18n}" key="PERF.ports" /></span>&nbsp;&nbsp;
					<select id=portSel style="width:80px;" onchange='portChange()'>
						<option value=-1><fmt:message bundle="${i18n}" key="COMMON.select" /></option>
					</select>&nbsp;&nbsp;
					<span style="margin-left:30px;">ONU</span>&nbsp;&nbsp;
					<select id=onuSel style="width:80px;" onchange='onuChange()'>
						<option value=-1><fmt:message bundle="${i18n}" key="COMMON.select" /></option>
					</select>&nbsp;&nbsp;
					<span style="margin-left:30px;">UNI</span>&nbsp;&nbsp;
					<select id=uniSel style="width:80px;" onchange='uniChange()'>
						<option value=-1><fmt:message bundle="${i18n}" key="COMMON.select" /></option>
					</select>&nbsp;&nbsp;
				</div>
			</td></tr>
		</table>
	</div>
</td></tr>
<tr><td><hr color=#1973b4 size=1>
</td></tr>
<tr height=30><td>
	<div style="height:20px;padding-left:10px;"><span id="locationSpan"></span>
		<button id=refreshBt onclick='refreshBtClick()' style="margin-left:30px;display:none;" class=BUTTON120>
			<fmt:message bundle="${i18n}" key="COMMON.fetch" /></button>
	</div>
</td></tr>
<tr><td style="padding-left:5px;">
<!-- 端口/板卡 -->
	<div id=initTab style="">
		<fmt:message bundle="${i18n}" key="PERF.fastCfgHelp" />
	</div>
	<div id=portTab style="display:none;">
		<table style="margin:10px;">
			<tr><td colspan=9>
				<table>
					<tr><td>
						<table>
							<!-- 15min -->
							<tr class=15min24hourClass height=50><td width=170>
								<fmt:message bundle="${i18n}" key="PERF.port.x15minStatNow" />
							</td><td colspan=2><span id=15minStatSpan></span>
							</td></tr>
							<tr class=15min24hourClass height=50><td>
								<fmt:message bundle="${i18n}" key="PERF.port.x15minStat" />
							</td><td width=80>
								<button onclick="openEnableStat(1)" class=BUTTON75>
									<fmt:message bundle="${i18n}" key="COMMON.open" /></button>
							</td><td width=300>
								<button onclick="closeEnableStat(1)" class=BUTTON75>
									<fmt:message bundle="${i18n}" key="COMMON.close" /></button>
							</td></tr>
							<!-- 24hour -->
							<tr class=15min24hourClass height=50><td width=170>
								<fmt:message bundle="${i18n}" key="PERF.port.x24hourStatNow" />
							</td><td colspan=2><span id=24hourStatSpan></span>
							</td></tr>
							<tr class=15min24hourClass height=50><td>
								<fmt:message bundle="${i18n}" key="PERF.port.x24hourStat" />
							</td><td width=80>
								<button onclick="openEnableStat(2)" class=BUTTON75>
									<fmt:message bundle="${i18n}" key="COMMON.open" /></button>
							</td><td width=300>
								<button onclick="closeEnableStat(2)" class=BUTTON75>
									<fmt:message bundle="${i18n}" key="COMMON.close" /></button>
							</td></tr>
							<!-- 温度 -->
							<tr class=temperatureClass height=50><td width=170>
								<fmt:message bundle="${i18n}" key="PERF.port.temperatureStatNow" />
							</td><td colspan=2><span id=temperatureStatSpan></span>
							</td></tr>
							<tr class=temperatureClass height=50><td>
								<fmt:message bundle="${i18n}" key="PERF.port.temperatureStat" />
							</td><td width=80>
								<button onclick="openEnableStat(3)" class=BUTTON75>
									<fmt:message bundle="${i18n}" key="COMMON.open" /></button>
							</td><td width=300>
								<button onclick="closeEnableStat(3)" class=BUTTON75>
									<fmt:message bundle="${i18n}" key="COMMON.close" /></button>
							</td></tr>
						</table>
					</td></tr>
				</table>
			</td></tr>
		</table>
	</div>
<!-- 基本配置 -->
	<div id=deviceTab style="display:none;">
		<table style="margin:10px;">
			<tr><td valign=top>
				<table>
					<!-- 采集周期 -->
					<tr height=30px><td><hr size=1 style="width:60px;color:#1973b4;">
					</td><td align=center><fmt:message bundle="${i18n}" key="PERF.port.collectCycle" />
					</td><td><hr size=1 style="width:125px;color:#1973b4;">
					</td></tr>
					<tr height=30px><td colspan=2><fmt:message bundle="${i18n}" key="PERF.port.collectCycleOltPerf" />
					</td><td>
						<select id=oltPerfCollectInput style="width:100px;" disabled>
							<option value=5>5</option>
							<option value=10>10</option>
							<option value=15>15</option>
							<option value=30>30</option>
							<option value=60>60</option>
							<option value=100>100</option>
							<option value=300>300</option>
							<option value=900>900</option>
						</select> <fmt:message bundle="${i18n}" key="COMMON.S" />
					</td></tr>
					<tr height=30px><td colspan=2><fmt:message bundle="${i18n}" key="PERF.port.collectCycleOnuPerf" />
					</td><td>
						<select id=onuPerfCollectInput style="width:100px;" disabled>
							<option value=5>5</option>
							<option value=10>10</option>
							<option value=15>15</option>
							<option value=30>30</option>
							<option value=60>60</option>
							<option value=100>100</option>
							<option value=300>300</option>
							<option value=900>900</option>
						</select> <fmt:message bundle="${i18n}" key="COMMON.S" />
					</td></tr>
					<tr height=30px><td colspan=2><fmt:message bundle="${i18n}" key="PERF.port.collectCycleOltTemp" />
					</td><td><input id=oltTempCollectInput style="width:100px;" maxlength=2 
						title='<fmt:message bundle="${i18n}" key="PERF.port.plsInput1to10" />' disabled/> 
						<fmt:message bundle="${i18n}" key="COMMON.S" />
					</td></tr>
					<tr height=30px><td colspan=2><fmt:message bundle="${i18n}" key="PERF.port.collectCycleOnuTemp" />
					</td><td><input id=onuTempCollectInput style="width:100px;" maxlength=2 
						title='<fmt:message bundle="${i18n}" key="PERF.port.plsInput1to10" />' disabled/> 
						<fmt:message bundle="${i18n}" key="COMMON.S" />
					</td></tr>
					<tr height=35px><td colspan=2>
					</td><td>
						<button id=collectionBt onclick='collectionBtClick()' class=BUTTON95 style='display:none;'>
							<fmt:message bundle="${i18n}" key="COMMON.saveCfg" /></button>
					</td></tr>
					<!-- 采集记录数 -->
					<tr height=30px><td><hr size=1 style="width:60px;color:#1973b4;">
					</td><td><fmt:message bundle="${i18n}" key="PERF.port.collectRecord" />
					</td><td><hr size=1 style="width:125px;color:#1973b4;">
					</td></tr>
					<tr height=30px><td colspan=2><fmt:message bundle="${i18n}" key="PERF.port.x15minRecord" />
					</td><td><input id=15minRecordInput style="width:100px;" maxlength=2 
						onfocus="inputFocused('15minRecordInput', '<fmt:message bundle="${i18n}" key="PERF.port.plsInput1to96" />', 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');"
						onclick="clearOrSetTips(this);" />
					</td></tr>
					<tr height=30px><td colspan=2><fmt:message bundle="${i18n}" key="PERF.port.x24hourRecord" />
					</td><td><input id=24hourRecordInput style="width:100px;" maxlength=2 
						onfocus="inputFocused('24hourRecordInput', '<fmt:message bundle="${i18n}" key="PERF.port.plsInput1to30" />', 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');"
						onclick="clearOrSetTips(this);" />
					</td></tr>
					<tr height=35px><td colspan=2>
					</td><td>
						<button id=recordBt onclick='recordBtClick()' class=BUTTON95>
							<fmt:message bundle="${i18n}" key="COMMON.saveCfg" /></button>
					</td></tr>
				</table>
			</td><td style="padding-left:10px;">
				<div id=thresholdGridDiv style="margin-left:5px;"></div>
				<%-- <fieldset id="thresholdFieldset">
					<legend>throesholdType: 
						<select id="thresholdSel" style="width:110px;" onchange="thresholdSelChange()">
							<option value=1>OLT SNI</option>
							<option value=2>OLT PON</option>
							<option value=3>ONU PON</option>
							<option value=4>ONU UNI</option>
							<option value=5>temprature</option>
						</select>
					</legend>
				</fieldset> --%>
			</td></tr>
		</table>
	</div>
</td></tr></table>
<!-- 阈值弹出框 -->
<div id=thresholdDlgDiv style="display:none;">
	<table>
		<tr><td>
			<fieldset style="background-color: white;margin: 10px;">
				<table>
					<tr height=30px><td width=100px>&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="PERF.moI.name" />: 
					</td><td width=163px><span id=thresholdNameSpan></span>
					</td></tr>
					<tr height=35px><td>&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="PERF.port.thresholdUpper" />: 
					</td><td><input style="width:150px;" id=thresholdUpperInput maxlength=20 
						onfocus="inputFocused('thresholdUpperInput', '<fmt:message bundle="${i18n}" key="PERF.port.plsInput0" />', 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');" 
						onclick="clearOrSetTips(this);" />
					</td></tr>
					<tr height=35px><td>&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="PERF.port.thresholdLower" />: 
					</td><td><input style="width:150px;" id=thresholdLowerInput maxlength=20 
						onfocus="inputFocused('thresholdLowerInput', '<fmt:message bundle="${i18n}" key="PERF.port.plsInput0" />', 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');" 
						onclick="clearOrSetTips(this);" />
					</td></tr>
				</table>
			</fieldset>
		</td></tr>
		<tr><td align=right>
			<button class=BUTTON75 onclick='modifyThresholdClick()'>
				<fmt:message bundle="${i18n}" key="COMMON.confirm" /></button>&nbsp;&nbsp;
			<button class=BUTTON75 onclick='closeThresholdDlg()'>
				<fmt:message bundle="${i18n}" key="COMMON.close" /></button>&nbsp;&nbsp;&nbsp;
		</td></tr>
	</table>
</div>
</body>
<style>

</style>
</html>