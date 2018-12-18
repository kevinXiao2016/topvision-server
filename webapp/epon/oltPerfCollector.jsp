<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<%@include file="/include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" 
	src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<title><fmt:message bundle="${i18n}" key="PERF.eponCollectMgmt" /></title>
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
<script type="text/javascript">
Ext.Ajax.timeout = 800000;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
//[{slotNo, slotId, portObj:[{portId, portNo, stat, collect_oldCollect, type, onuObj:[{onuNo, 
//	onuId, onuPon: stat_collect_oldCollect, uni: uniNo_stat_collect_oldCollect_uniNo_stat_collect_oldCollect}, {}...]}, {}...]}, {}...]
var deviceData = new Array();	
var oltData = new Array();	//[{slotNo, slotId, type, portId, portNo, stat, collect, tmpIndex},{}...]
var oltStore;
var oltGrid;
var onuData = new Array();	//[[onuNo, onuId, stat_collect, uniNo_collect, uniNo_collect, ...], [], []...]
var onuStore;
var onuGrid;

//界面临时数据
var showAllPort = false;	//是否显示所有的端口
var regionDeviceList = new Array();	//地域、设备的关系列表，用于生成地域、设备的下拉框
var ipList = new Array();	//IP列表
var ipListLength = 0;
var onuGirdLoadNullFlag = true;	//onuGrid是否是load的空数组
var selectedEntityId;
var w = 0;
var h = 0;
var mpuaNum = "";
var settingFlag = false;
var checkedSrc = "/images/monitor/checked.gif";
var uncheckedSrc = "/images/monitor/unchecked.gif";
var uniColNum = 1;

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
					for(var t=0; t<tmpLength; t++){
						//判断IP是否重叠了，如果有重叠的，只使用第一个设备
						var isOverlap;
						for(var x=0; x < ipList.length;x++){
							if(ipList[x].ip == tmp[t]){
								isOverlap = true;
								break;
							}
						}
						if(!isOverlap){
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
				$("#regionSel").append(String.format("<option value=-1>{0}</option>", I18N.PERF.allFolderNoDevice));
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.sqlContentError);
		}
	});
}
function loadDeviceData(deviceId){
	Ext.Ajax.request({
		url : '/epon/perf/getDeviceCollectData.tv?r=' + Math.random() + '&entityId=' + deviceId,
		success : function(response) {
			if(response.responseText == "false"){
				deviceData = new Array();
			}else{
				deviceData = Ext.util.JSON.decode(response.responseText);
				loadOltData();
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.sqlContentError);
		}
	});	
}
function loadOltData(){
	mpuaNum = "";
	var initFlag = new Array();
	oltData = new Array();
	for(var a=0; a<deviceData.length; a++){
		var tmp = deviceData[a].portObj;
		for(var b=0; b<tmp.length; b++){
			var slotTmp = {slotNo : deviceData[a].slotNo, slotId : deviceData[a].slotId, portNo : tmp[b].portNo,
					portId: tmp[b].portId, stat: tmp[b].stat, collect: tmp[b].collect.split("_")[0], type: tmp[b].type};
			oltData.push(slotTmp);
			if(slotTmp.slotNo == 0){
				mpuaNum = "_" + deviceData[a].mpuaNo;
			}
			if(initFlag.indexOf(deviceData[a].slotId) == -1){
				initFlag.push(deviceData[a].slotId);
			}
		}
	}
	oltStore.loadData({data: oltData});
	if(initFlag.length > 3){
		oltGrid.getView().collapseAllGroups();
	}
}
function loadOltGrid(){
	var reader = new Ext.data.JsonReader({
		root: "data",
		fields: [
			{name: 'slotNo'},
	   		{name: 'slotId'},
	    	{name: 'portNo'},
	    	{name: 'portId'},
	    	{name: 'stat'},
	    	{name: 'collect'},
	    	//{name: 'slotType'},
	    	{name: 'type'}
		]	    
	});
	var cm = new Ext.grid.ColumnModel([
	    {header: I18N.OLT.slot,dataIndex:'slotNo',width:120, align: 'center',sortable: false,resizable: true, id:'id1',
		    menuDisabled :true, renderer: slotRender},
		{header: I18N.ONU.portNum,dataIndex:'portNo',width:120, align: 'center',sortable: false,resizable: true, id: 'id2',
			menuDisabled :true, renderer: portRender}
	]);
	oltStore = new Ext.data.GroupingStore({
	 	data: {data: oltData},
		remoteGroup: false,
		remoteSort: false,
		reader: reader,
		sortInfo: {field: 'slotNo', direction: "ASC"},
		groupField: 'slotNo',
		groupOnSort: false
	});
	oltGrid = new Ext.grid.GridPanel({
        border:true, 
        region:'center',
        width: 250,
        height: h - 174,
  	    store : oltStore,
	    title : I18N.PERF.oltPortList, 
        cm : cm,
        hideHeaders: true,
        selModel : new Ext.grid.RowSelectionModel({
            singleSelect: true,
            listeners : {
            	'selectionchange' : function (){
    				var sel = oltGrid.getSelectionModel().getSelected();
    				if(sel){
        				Ext.getCmp("OnuGrid").setTitle(String.format(I18N.PERF.onuInThePon, sel.data.slotNo, sel.data.portNo));
    					loadOnuData(sel.data.slotNo, sel.data.portNo);
    					onuGirdLoadNullFlag = false;
    				}else{
		                changeOnuObj();
    					onuGirdLoadNullFlag = true;
        				setTimeout(function(){
            				if(onuGirdLoadNullFlag){
                				Ext.getCmp("OnuGrid").setTitle(I18N.PERF.onuPortList);
		    					onuStore.loadData(new Array());
                			}
            			}, 300);
        			}
    			}
            }
        }),
      	view: new Ext.grid.GroupingView({
            forceFit: true,
	        hideGroupedColumn: true,
	        enableNoGroups: true
        }),
  		renderTo: "oltGridDiv",
  		listeners : {
			'viewready' : function(){
				
			},
			'rowcontextmenu': function(grid,rowIndex,e){
				grid.selModel.selectRow(rowIndex,true);
				oltMenu.showAt(e.getXY());
			}
  	  	}
    });
}
function slotRender(value, cellmate, record){
	var re = "";
	if(record.data.slotNo == 0){
		re = I18N.OLT.mpua + mpuaNum;
	}else{
		if(record.data.type == 1){
			re = I18N.OLT.geua + "_" + record.data.slotNo;
		}else if(record.data.type == 2){
			re = I18N.OLT.epua + "_" + record.data.slotNo;
		}
	}
	return re;
}
function portRender(value, cellmate, record){
	var re = "";
	if(record.data.type == 1){
		re = I18N.PERF.sniPort + ":";
	}else if(record.data.type == 2){
		re = I18N.PERF.ponPort + ":";
	}
	re += record.data.slotNo + " / " + record.data.portNo;
	re += createBt(record.data.stat, record.data.collect.split("_")[0], record.data.slotNo + "_" + record.data.portNo);
	return re;
}
var oltMenu = new Ext.menu.Menu({
	id:'oltMenu',
	enableScrolling: false,
	items:[{
	    id:'oltMenu1',
	    text: I18N.PERF.collectSlotPerfData,
	    handler : function(){
		    fastConfig(1);
	    }
	},{
	    id:'oltMenu2',
	    text: I18N.PERF.notCollectSlotPerfData,
	    handler : function(){
	    	fastConfig(2);
	    }
	}]
});
var onuMenu = new Ext.menu.Menu({
	id:'onuMenu',
	enableScrolling: false,
	items:[{
	    id:'onuMenu1',
	    text: I18N.PERF.collectOnuPerfData,
	    handler : function(){
	    	fastConfig(3);
	    }
	},{
	    id:'onuMenu2',
	    text: I18N.PERF.notCollectOnuPerfData,
	    handler : function(){
	    	fastConfig(4);
	    }
	}]
});
function loadOnuData(slotNo, portNo){
	onuData = new Array();
	for(var a=0; a<deviceData.length; a++){
		if(deviceData[a].type == 2 && deviceData[a].slotNo == slotNo){
			var tmpP = deviceData[a].portObj;
			for(var b=0; b<tmpP.length; b++){
				if(tmpP[b].portNo == portNo && tmpP[b].type == 2){
					var tmpO = tmpP[b].onuObj;
					for(var c=0; c<tmpO.length; c++){
						var onuTmp = [slotNo, portNo, tmpO[c].onuNo, tmpO[c].onuId, tmpO[c].onuPon, tmpO[c].uni];
						onuData.push(onuTmp);
					}
					break;
				}
			}
		}
	}
	onuStore.loadData(onuData);
}
function loadOnuGrid(){
	var cm = [ {header: 'ONU' + I18N.COMMON.location, id:'id1', dataIndex: 'onuNo', align: 'left', sortable: true, width:70, renderer: onuLocRender}
		  , {header: I18N.EPON.onuPonPort, id:'id2', dataIndex: 'onuPon', align: 'left', sortable: true, width: 150, renderer: onuRender}
		  , {header: I18N.EPON.uniPort, id:'id3', dataIndex: 'uni', align: 'left', sortable: true, width: w - 545 < 395 ? 395 : w - 545,
				  renderer: uniRender}];
	onuStore = new Ext.data.SimpleStore({
		data : onuData,
		fields: ['slotNo', 'portNo', 'onuNo','onuId','onuPon', 'uni']
	});
	onuGrid = new Ext.grid.GridPanel({
		id : 'OnuGrid',
		title : I18N.PERF.onuPortList,
		renderTo : 'onuGridDiv',
		border : true,
		frame : false,
		autoScroll : true,
		width : w - 300,
		height : h - 174,
		store : onuStore,
		columns : cm,
		selModel : new Ext.grid.RowSelectionModel({singleSelect : true, handleMouseDown: Ext.emptyFn}),
		listeners : {
			'viewready' : function(){
			
			},
			'rowcontextmenu': function(grid,rowIndex,e){
				grid.selModel.selectRow(rowIndex,true);
				onuMenu.showAt(e.getXY());
			}
		}
	});
}
function onuLocRender(value, cellmate, record){
	return record.data.slotNo + " / " + record.data.portNo + " : " + value;
}
function onuRender(value, cellmate, record){
	var re = "";
	var stat = value.split("_")[0];
	var collect = value.split("_")[1];
	var tmpLoc = record.data.slotNo + " / " + record.data.portNo + " : " + record.data.onuNo;
	re += String.format("<img src='/images/monitor/port_2.png' title='{1}:{0}'>", tmpLoc, I18N.EPON.onuPonPort);
	re += createBt(stat, collect, record.data.slotNo + "_" + record.data.portNo + "_" + record.data.onuNo);
	return re;
}
function uniRender(value, cellmate, record){
	var re = "";
	if(value != ""){
		var tmpList = value.split("_");
		for(var k=0; 4*k<tmpList.length; k++){
			var uniNoTmp = tmpList[4*k];
			uniNoTmp = uniNoTmp > 9 ? uniNoTmp : ("&nbsp; " + uniNoTmp);
			var tmpUniId = record.data.slotNo + "_" + record.data.portNo + "_" + record.data.onuNo + "_" + tmpList[4*k];
			var tmpUniLoc = I18N.EPON.uniPort + ":"+record.data.slotNo+" / "+record.data.portNo+" : "+record.data.onuNo+" / "+tmpList[4*k];
			re += String.format("<span style='width:40px;text-align:right;'><img src='/images/monitor/port_1.png' title='{1}'>{0}</span>",
				uniNoTmp, tmpUniLoc);
			re += createBt(tmpList[4*k+1], tmpList[4*k+2], tmpUniId);
			if((k + 1) % uniColNum == 0){
				re += "<br>";
			}
		}
	}
	return re;
}
function changeOnuObj(){
	if(onuStore.getAt(0)){
		var slotNo = onuStore.getAt(0).data.slotNo;
		var portNo = onuStore.getAt(0).data.portNo;
		for(var a=0; a<deviceData.length; a++){
			if(slotNo == deviceData[a].slotNo){
				var tmp = deviceData[a].portObj;
				for(var b=0; b<tmp.length; b++){
					if(tmp[b].portNo == portNo){
						for(var c=0; c<tmp[b].onuObj.length; c++){
							var tmpO = tmp[b].onuObj[c];
							var tmpOnuPon = tmpO.onuPon;
							var onuPonCol = 0;
							var onuPonStat = tmpOnuPon.split("_")[0];
							if(onuPonStat & 1){
								if($($("#x1_" + slotNo + "_" + portNo + "_" + tmpO.onuNo)
										.children()[0]).attr("src").indexOf(checkedSrc) > -1){
									onuPonCol++;
								}
							}
							if(onuPonStat & 2){
								if($($("#x2_" + slotNo + "_" + portNo + "_" + tmpO.onuNo)
										.children()[0]).attr("src").indexOf(checkedSrc) > -1){
									onuPonCol += 2;
								}
							}
							deviceData[a].portObj[b].onuObj[c].onuPon = tmpOnuPon.split("_")[0]+"_"+onuPonCol+"_"+tmpOnuPon.split("_")[2];
							if(tmpO.uni != ""){
								var tmpU = tmpO.uni.split("_");
								for(var d=0; 4*d<tmpU.length; d++){
									var uniCol = 0;
									var uniStat = tmpU[4*d + 1];
									if(uniStat & 1){
										if($($("#x1_" + slotNo + "_" + portNo + "_" + tmpO.onuNo + "_" + tmpU[4*d])
												.children()[0]).attr("src").indexOf(checkedSrc) > -1){
											uniCol++;
										}
									}
									if(uniStat & 2){
										if($($("#x2_" + slotNo + "_" + portNo + "_" + tmpO.onuNo + "_" + tmpU[4*d])
												.children()[0]).attr("src").indexOf(checkedSrc) > -1){
											uniCol += 2;
										}
									}
									tmpU[4*d + 2] = uniCol;
								}
								deviceData[a].portObj[b].onuObj[c].uni = tmpU.join("_");
							}
						}
						return;
					}
				}
			}
		}
	}
}

//设备change
function regionChange(){
	var reg = $("#regionSel").val();
	$("#deviceSel").empty();
	$("#deviceSel").append(String.format("<option value=-1>{0}</option>", I18N.COMMON.select));
	if(reg == 0){
		$("#deviceSel").hide();
		$("#deviceInput").val("");
		$("#deviceInput").show();
		oltData = new Array();
		oltStore.loadData({data: oltData});
		onuData = new Array();
		onuStore.loadData(onuData);
		$("#refreshBt").hide();
		$("#locationSpan").text("");
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
	for(var x=0; x<regionDeviceList.length; x++){
		var tmp = regionDeviceList[x].ips.split(",");
		if(ip != "" && tmp.indexOf(ip) > -1){
			selectedEntityId = regionDeviceList[x].entityIds.split(",")[tmp.indexOf(ip)];
			loadDeviceData(selectedEntityId);
			flag = false;
		}
	}
	if(flag){
		$("#refreshBt").hide();
		$("#locationSpan").text("");
		onuData = new Array();
		onuStore.loadData(onuData);
		oltData = new Array();
		oltStore.loadData({data: oltData});
	}else{
		$("#refreshBt").show();
		$("#locationSpan").text(ip);
	}
	settingFlag = false;
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
		$("#deviceInputSel").append(String.format("<div style='height:20px;'>{0}</div>",  I18N.PERF.noMatchIp));
		flagNum = 1;
	}else if(flagNum > 500){
		for(var x=0; x<200; x++){
			$("#deviceInputSel").append(getDeviceInputSel(tmpIpList[x]));
		}
		$("#deviceInputSel").append(String.format("<div style='height:20px;'>"+ I18N.PERF.hasMatchIp+"</div>", parseInt(flagNum - 200)));
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
	var tmpPosition = getElPositionById('deviceInput');
	$("#deviceInputSel").css({'left': tmpPosition[0], 'top': tmpPosition[1] + 20}).show();
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
		"onclick='inputSelClick(this.id)' onblur='inputSelBlur()' style='height:20px;cursor:hand;' >{0}</div>", v);
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

//onready
Ext.onReady(function(){
	initButton('all');
	initFolderListData();
	w = window.document.body.offsetWidth;
	w = w < 980 ? 980 : w;
	uniColNum = w - 545 < 525 ? 2 : w - 545 < 700 ? 3 : w - 545 < 875 ? 4 : w - 545 < 1050 ? 5 : 6;
	h = window.document.body.offsetHeight;
	h = h < 474 ? 474 : h;
	loadOltGrid();
	loadOnuGrid();
});

//交互
function refreshBtClick(){
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.PERF.refreshConfirmTip, function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.PERF.refreshing, 'ext-mb-waiting');
		var tmpK = $("#deviceSel").css("display") == "none" ? 1 : 2;
		var deviceId = getEntityId();
		if(deviceId < 1){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.plsSelectDevice);
			return;
		}
		Ext.Ajax.request({
			url : '/epon/refreshOlt.tv?r=' + Math.random() + '&entityId=' + deviceId,
			success : function(response){
				if(response.responseText != "success"){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.refreshFailed);
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.refreshSuccess);
				deviceChange(tmpK);
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.refreshFailed);
			}
		});
	});
}
function saveBtClick(){
	if(settingFlag){
		return;
	}
	settingFlag = true;
	var tmpK = $("#deviceSel").css("display") == "none" ? 1 : 2;
	var deviceId = getEntityId();
	if(deviceId < 1){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.plsSelectDevice);
		settingFlag = false;
		return;
	}
	//ONU的数据
	changeOnuObj();
	//所有数据的处理
	var needToAdd = new Array();
	var needToUpdate = new Array();
	var needToDel = new Array();
	for(var a=0; a<deviceData.length; a++){
		var tmpS = deviceData[a];
		for(var b=0; b<tmpS.portObj.length; b++){
			var tmpP = tmpS.portObj[b];
			var portTmpId = tmpS.slotNo + "_" + tmpP.portNo;
			//var slotType = tmpS.slotType;
			var nowPortCol = 0;
			if(tmpP.stat & 1){
				if($($("#x1_" + portTmpId).children()[0]).attr("src").indexOf(checkedSrc) > -1){
					nowPortCol++;
				}
			}
			if(tmpP.stat & 2){
				if($($("#x2_" + portTmpId).children()[0]).attr("src").indexOf(checkedSrc) > -1){
					nowPortCol += 2;
				}
			}
			var oldPortCol = parseInt(tmpP.collect.split("_")[1]);
			if(oldPortCol == 0){
				if(nowPortCol > 0){
					needToAdd.push(nowPortCol + "_" + portTmpId);
				}
			}else if(oldPortCol > 0){
				if(nowPortCol == 0){
					needToDel.push(nowPortCol + "_" + portTmpId);
				}else if(nowPortCol != oldPortCol){
					needToUpdate.push(nowPortCol + "_" + portTmpId);
				}
			}
			if(tmpP.type != 2){
				continue;
			}
			for(var c=0; c<tmpP.onuObj.length; c++){
				var tmpO = tmpP.onuObj[c];
				var onuTmpId = portTmpId + "_" + tmpO.onuNo;
				var nowOnuCol = parseInt(tmpO.onuPon.split("_")[1]);
				var oldOnuCol = parseInt(tmpO.onuPon.split("_")[2]);
				if(oldOnuCol == 0){
					if(nowOnuCol > 0){
						needToAdd.push(nowOnuCol + "_" + onuTmpId);
					}
				}else{
					if(nowOnuCol == 0){
						needToDel.push(nowOnuCol + "_" + onuTmpId);
					}else if(nowOnuCol != oldOnuCol){
						needToUpdate.push(nowOnuCol + "_" + onuTmpId);
					}
				}
				var tmpU = tmpO.uni.split("_");
				for(var d=0; 4*d<tmpU.length; d++){
					var uniTmpId = onuTmpId + "_" + tmpU[4*d];
					var nowUniCol = parseInt(tmpU[4*d + 2]);
					var oldUniCol = parseInt(tmpU[4*d + 3]);
					if(oldUniCol == 0){
						if(nowUniCol > 0){
							needToAdd.push(nowUniCol + "_" + uniTmpId);
						}
					}else{
						if(nowUniCol == 0){
							needToDel.push(nowUniCol + "_" + uniTmpId);
						}else if(nowUniCol != oldUniCol){
							needToUpdate.push(nowUniCol + "_" + uniTmpId);
						}
					} 
				}
			}
		}
	}
	//开始
	var params = {entityId : deviceId};
	var typeTmp = 0;
	if(needToAdd.length > 0){
		params.addListStr = needToAdd.join(",");
		typeTmp += 1;
	}
	if(needToUpdate.length > 0){
		params.updateListStr = needToUpdate.join(",");
		typeTmp += 2;
	}
	if(needToDel.length > 0){
		params.delListStr = needToDel.join(",");
		typeTmp += 4;
	}
	if(typeTmp == 0){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.noChangeNeedToSave);
		settingFlag = false;
		return;
	}
	params.modifyType = typeTmp;
	Ext.Ajax.request({
		url : '/epon/perf/modifyOltCollector.tv?r=' + Math.random(),
		success : function(response){
			if(response.responseText != "success"){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.saveFailed);
				resetBtClick();
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.saveSuccess);
			resetBtClick();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.saveFailed);
			settingFlag = false;
		},
		params : params
	});
}
function openAllBtClick(){
	if(settingFlag){
		return;
	}
	settingFlag = true;
	var deviceId = getEntityId();
	if(deviceId < 1){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.plsSelectDevice);
		settingFlag = false;
		return;
	}
	var addList = getAllIndexList(0);
	var updateList = getAllIndexList(2);
	var modifyTmp = 0;
	var params = {entityId : deviceId};
	if(addList.length > 0){
		modifyTmp++;
		params.addListStr = addList.join(",");
	}
	if(updateList.length > 0){
		modifyTmp += 2;
		params.updateListStr = updateList.join(",");
	}
	if(modifyTmp == 0){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.noChangeNeedToSave);
		settingFlag = false;
		return;
	}
	params.modifyType = modifyTmp;
	Ext.Ajax.request({
		url : '/epon/perf/modifyOltCollector.tv?r=' + Math.random(),
		success : function(response){
			if(response.responseText != "success"){
				if(response.responseText){
					window.parent.showMessageDlg(I18N.COMMON.tip, response.responseText);
				}else{
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.modifyFailed);
				}
				resetBtClick();
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.PERF.openAllPortCollectSuccess, $("#locationSpan").text()));
			resetBtClick();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.modifyFailed);
			settingFlag = false;
		},
		params : params
	});
}
function closeAllBtClick(){
	if(settingFlag){
		return;
	}
	settingFlag = true;
	var deviceId = getEntityId();
	if(deviceId < 1){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.plsSelectDevice);
		settingFlag = false;
		return;
	}
	var indexList = getAllIndexList(1);
	if(indexList.length == 0){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.noChangeNeedToSave);
		settingFlag = false;
		return;
	}
	var params = {entityId : deviceId, modifyType: 4, delListStr: indexList.join(",")};
	Ext.Ajax.request({
		url : '/epon/perf/modifyOltCollector.tv?r=' + Math.random(),
		success : function(response){
			if(response.responseText != "success"){
				if(response.responseText){
					window.parent.showMessageDlg(I18N.COMMON.tip, response.responseText);
				}else{
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.modifyFailed);
				}
				resetBtClick();
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.PERF.closeAllPortCollectSuccess, $("#locationSpan").text()));
			resetBtClick();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.modifyFailed);
			settingFlag = false;
		},
		params : params
	});
}
function getAllIndexList(s){//0:add  1:del  2:update
	var indexL = new Array();
	for(var a=0; a<deviceData.length; a++){
		var tmpS = deviceData[a];
		for(var b=0; b<tmpS.portObj.length; b++){
			var tmpP = tmpS.portObj[b];
			var tmpPStat = tmpP.stat;
			if(s == 1){
				indexL.push("0_" + tmpS.slotNo + "_" + tmpP.portNo);
			}else if((tmpPStat & 1) || (tmpPStat & 2)){
				if(tmpP.collect.split("_")[1] == 0){
					if(s == 0){
						indexL.push(tmpPStat + "_" + tmpS.slotNo + "_" + tmpP.portNo);
					}
				}else if(s == 2){
					indexL.push(tmpPStat + "_" + tmpS.slotNo + "_" + tmpP.portNo);
				}
			}
			for(var c=0; c<tmpP.onuObj.length; c++){
				var tmpO = tmpP.onuObj[c];
				var tmpOnuStat = tmpO.onuPon.split("_")[0];
				if(s == 1){
					indexL.push("0_" + tmpS.slotNo + "_" + tmpP.portNo + "_" + tmpO.onuNo);
				}else if((tmpOnuStat & 1) || (tmpOnuStat & 2)){
					if(tmpO.onuPon.split("_")[1] == 0){
						if(s == 0){
							indexL.push(tmpOnuStat + "_" + tmpS.slotNo + "_" + tmpP.portNo + "_" + tmpO.onuNo);
						}
					}else if(s == 2){
						indexL.push(tmpOnuStat + "_" + tmpS.slotNo + "_" + tmpP.portNo + "_" + tmpO.onuNo);
					}
				}
				if(tmpO.uni != ""){
					var tmpU = tmpO.uni.split("_");
					for(var d=0; 4*d<tmpU.length; d++){
						var tmpUniStat = tmpU[4*d + 1];
						if(s == 1){
							indexL.push("0_" + tmpS.slotNo + "_" + tmpP.portNo + "_" + tmpO.onuNo + "_" + tmpU[4*d]);
						}else if((tmpUniStat & 1) || (tmpUniStat & 2)){
							if(tmpU[4*d + 3] == 0){
								if(s == 0){
									indexL.push(tmpUniStat + "_" + tmpS.slotNo + "_" + tmpP.portNo + "_" + tmpO.onuNo + "_" + tmpU[4*d]);
								}
							}else if(s == 2){
								indexL.push(tmpUniStat + "_" + tmpS.slotNo + "_" + tmpP.portNo + "_" + tmpO.onuNo + "_" + tmpU[4*d]);
							}
						}
					}
				}
			}
		}
	}
	return indexL;
}
function getEntityId(){
	var tmpK = $("#deviceSel").css("display") == "none" ? 1 : 2;
	var deviceId = -1;
	if(tmpK == 1){	//input
		for(var x=0; x<ipListLength; x++){
			if(ipList[x].ip == $("#deviceInput").val()){
				deviceId = ipList[x].entityId;
			}
		}
	}else{
		deviceId = $("#deviceSel").val();
	}
	return deviceId;
}
function resetBtClick(){
	onuData = new Array();
	onuStore.loadData(onuData);
	oltData = new Array();
	oltStore.loadData({data: oltData});
	if($("#deviceInput").css("display") == 'none'){
		deviceChange(2);
	}else{
		deviceChange(1);
	}
}
//关于grid里的button的
function createBt(stat, collect, id){
	var re = "<span style='width:125px;height:15px;margin:2 0 2 5;'>";
	var img = "";
	var divClass = "";
	var tiStr1 = "";
	var tiStr2 = "";
	var color = "";
	var cursor = "";
	var clickHandler = "";
	if(stat & 1){
		divClass = "checkedDiv15Class";
		tiStr1 = I18N.PERF.collect15min;
		if(collect & 1){
			img = String.format("<img src='{0}' title='{1}'>", checkedSrc, tiStr1);
		}else{
			img = String.format("<img src='{0}' title='{1}'>", uncheckedSrc, tiStr1);
		}
		color = "green";
		tiStr2 = I18N.PERF.collect15minOpened;
		cursor = "style='cursor:hand;'";
		clickHandler = "onclick='checkBt15Click(this)'";
	}else{
		color = "gray";
		divClass = "uncheckedDiv15Class";
		img = "";
		tiStr1 = "";
		tiStr2 = I18N.PERF.collect15minClosed;
		cursor = "style='cursor:default;'";
		clickHandler = "";
	}
	re += String.format("<span class='{0}'><button class=btClass id='{1}' {5} " + 
			"{6} onmouseover='checkBtOver(this)' onmouseout='checkBtOut(this)'>" +
			"{2}<span style='height:15px;padding-bottom:4px;color:{3};' title='{4}'>15min</span></button></span>",
			divClass, "x1_" + id, img, color, tiStr2, cursor, clickHandler);
	if(stat & 2){
		divClass = "checkedDiv24Class";
		tiStr1 = I18N.PERF.collect24h;
		if(collect & 2){
			img = String.format("<img src='{0}' title='{1}'>", checkedSrc, tiStr1);
		}else{
			img = String.format("<img src='{0}' title='{1}'>", uncheckedSrc, tiStr1);
		}
		color = "green";
		tiStr2 = I18N.PERF.collect24hOpened;
		cursor = "style='cursor:hand;'";
		clickHandler = "onclick='checkBt24Click(this)'";
	}else{
		color = "gray";
		divClass = "uncheckedDiv24Class";
		tiStr1 = "";
		img = "";
		tiStr2 = I18N.PERF.collect24hClosed;
		cursor = "style='cursor:default;'";
		clickHandler = "";
	}
	re += String.format("<span class='{0}'><button class=btClass id='{1}' {5} " + 
			"{6} onmouseover='checkBtOver(this)' onmouseout='checkBtOut(this)'>" +
			"{2}<span style='height:15px;padding-bottom:4px;color:{3};' title='{4}'>24h</span></button></span>",
			divClass, "x2_" + id, img, color, tiStr2, cursor, clickHandler);
	re += "</span>";
	return re;
}
function checkBt15Click(el){
	var im = $($(el).children()[0]);
	if(im.attr("src").indexOf(checkedSrc) > -1){
		im.attr("src", uncheckedSrc);
	}else{
		im.attr("src", checkedSrc);
	}
}
function checkBt24Click(el){
	var im = $($(el).children()[0]);
	if(im.attr("src").indexOf(checkedSrc) > -1){
		im.attr("src", uncheckedSrc);
	}else{
		im.attr("src", checkedSrc);
	}
}
function checkBtOver(el){
	$(el).css("background-color", "#cccccc");
}
function checkBtOut(el){
	$(el).css("background-color", "transparent");
}
//反键快捷操作
function fastConfig(s){//1:openOlt, 2:closeOlt, 3:openOnu, 4:closeOnu
	var configFlag;
	if(s % 2 == 1){//open
		configFlag = checkedSrc;
	}else{//close
		configFlag = uncheckedSrc;
	}
	var sel;
	if(s < 3){//olt
		sel = oltGrid.selModel.getSelected();
		if(sel){
			var slotNoTmp = sel.data.slotNo;
			for(var x=0; x<oltData.length; x++){
				if(oltData[x].slotNo == slotNoTmp){
					var statTmp = oltData[x].stat;
					var tmpId = slotNoTmp + "_" + oltData[x].portNo;
					if(statTmp & 1){
						$($("#x1_" + tmpId).children()[0]).attr("src", configFlag);
					}
					if(statTmp & 2){
						$($("#x2_" + tmpId).children()[0]).attr("src", configFlag);
					}
				}
			}
		}
	}else{//onu
		sel = onuGrid.selModel.getSelected();
		if(sel){
			var slotNoTmp = sel.data.slotNo;
			var ponNoTmp = sel.data.portNo;
			var onuNoTmp = sel.data.onuNo;
			var onuPonTmp = sel.data.onuPon.split("_");
			var uniTmp = sel.data.uni.split("_");
			var tmpId = slotNoTmp + "_" + ponNoTmp + "_" + onuNoTmp;
			if(onuPonTmp.length == 3){
				if(onuPonTmp[0] & 1){
					$($("#x1_" + tmpId).children()[0]).attr("src", configFlag);
				}
				if(onuPonTmp[0] & 2){
					$($("#x2_" + tmpId).children()[0]).attr("src", configFlag);
				}
			}
			var uniTmpLength = uniTmp.length;
			if(uniTmpLength > 3){
				for(var x=0; 4*x<uniTmpLength; x++){
					var statTmp = uniTmp[4*x+1];
					if(statTmp & 1){
						$($("#x1_" + tmpId + "_" + uniTmp[4*x]).children()[0]).attr("src", configFlag);
					}
					if(statTmp & 2){
						$($("#x2_" + tmpId + "_" + uniTmp[4*x]).children()[0]).attr("src", configFlag);
					}
				}
			}
		}
	}
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#deviceInputSel").attr("disabled",false);
		$("#regionSel").attr("disabled",false);
		$("#deviceSel").attr("disabled",false);
		$("#refreshBt").attr("disabled",false);
	}
}
</script>
</head>
<body class=BLANK_WND style="margin-top: 15px;margin-left: 15px;" onload="authLoad()">
<!-- 设备input下拉框 -->
<div id=deviceInputSel onblur='deviceInputSelBlur()'
style="display:none;position:absolute;border:1px solid #ccc;width:147;background-color:white;z-index:100;">
</div>
<table>
	<tr height=35><td width=260>
		<span style="margin-left:10px;"><fmt:message bundle="${i18n}" key="PERF.folder" /></span>&nbsp;&nbsp;
		<select id=regionSel style="width:180px;" onchange='regionChange()'>
			<option value=0><fmt:message bundle="${i18n}" key="PERF.allDevice" /></option>
		</select>
	</td><td width=600>
		<span><fmt:message bundle="${i18n}" key="PERF.device" /></span>&nbsp;
		<input id=deviceInput style="width:147;margin-left:5px;" maxlength=15 onfocus='deviceInputFocus()' 
			onblur='deviceInputBlur()' onkeyup='deviceInputKeyup()' onchange=deviceChange(1) />
		<select id=deviceSel style="width:150px;display:none;" onchange=deviceChange(2)>
			<option value=-1><fmt:message bundle="${i18n}" key="COMMON.select" /></option>
		</select>
	</td></tr>
	<tr><td colspan=2><hr size=1 color=#1973b4>
	</td></tr>
	<tr height=25><td valign=bottom>
		<fmt:message bundle="${i18n}" key="PERF.device" />:<span id=locationSpan></span>
		<button id=refreshBt onclick='refreshBtClick()' style="margin-left:20px;display:none;" class='BUTTON95' >
			<fmt:message bundle="${i18n}" key="COMMON.fetch" /></button>
	</td><td>
	</td></tr>
	<tr height=360><td><div id=oltGridDiv></div>
	</td><td><div id=onuGridDiv></div>
	</td></tr>
	<tr><td colspan=2><hr size=1 color=#1973b4>
	</td></tr>
	<tr><td>
	</td><td align=right>
		<button id=saveBt onclick='saveBtClick()' class=BUTTON75 >
			<fmt:message bundle="${i18n}" key="COMMON.saveCfg" /></button>&nbsp;&nbsp;
		<button id=resetBt onclick='resetBtClick()' class=BUTTON75 >
			<fmt:message bundle="${i18n}" key="COMMON.reset" /></button>&nbsp;&nbsp;
		<button id=openAllBt onclick='openAllBtClick()' class=BUTTON120>
			<fmt:message bundle="${i18n}" key="PERF.openAllPortCollect" /></button>&nbsp;&nbsp;
		<button id=closeAllBt onclick='closeAllBtClick()' class=BUTTON120>
			<fmt:message bundle="${i18n}" key="PERF.closeAllPortCollect" /></button>
	</td></tr>
</table>
</body>
<style>
.btClass {
	border: 0px;
	background-color: transparent;
	width: 60px;
	height: 15px;
}
.checkedDiv15Class {
	width: 60px;
	height: 16px;
	border: 1px solid green;
	border-right: 0px;
}
.uncheckedDiv15Class {
	width: 60px;
	height: 16px;
	border: 1px solid gray;
	border-right: 0px;
}
.checkedDiv24Class {
	width: 60px;
	height: 16px;
	border: 1px solid green;
	border-left: 0px;
}
.uncheckedDiv24Class {
	width: 60px;
	height: 16px;
	border: 1px solid gray;
	border-left: 0px;
}
</style>
</html>