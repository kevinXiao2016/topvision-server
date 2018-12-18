<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/include/nocache.inc" %>
<%@ include file="/include/cssStyle.inc" %>
<link rel="stylesheet" type="text/css" href="/css/gui.css"/>  
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var entityId = <s:property value="entityId"/>;
var uniIndex = <s:property value = "uniIndex"/>;
var uniIgmpValue = ${uniIgmpValue} ? ${uniIgmpValue} : new Array();
var addMvlanFlag = false;//标记进入页面时是否需要add一条mvlanList的绑定
//组播模板
var mvlanList = ${mvlanList};
var mvlanGrid;
var mvlanStore;
var mvlanData = new Array();
//UNI口允许通过的组播模板
var uniMvlanList = ${uniMvlanList};
var uniMvlanGrid;
var uniMvlanStore;
var uniMvlanData = new Array();
//转换列表
var transRule = ${transList};
var transGrid;
var transStore;
var transData = new Array();
//MVID
var mvidData0 = ${uniMvidList};//保存用的数据mvidlist，操作用的data，非展示用的data
var mvidGrid1;
var mvidStore1;
var mvidData1 = new Array();
var mvidGrid2;
var mvidStore2;
var mvidData2 = new Array();


//通过mibIndex获得num
function getNum(index, s){
	var num;
	switch (s)
	{
	case 1: num = (index & 0xFF000000) >> 24;
		break;
	case 2: num = (index & 0xFF0000) >> 16;
		break;
	case 3: num = (index & 0xFF00) >> 8;
		break;
	case 4: num = index & 0xFF;
		break;
	} 
	return num;
}
//通过index获得location
function getUniLocationByIndex(index){
	var tmp = index.toString(16);
	t = tmp.substring(0, tmp.length-4) + tmp.substring(tmp.length-2, tmp.length);
	t = parseInt(t, 16);
	var uniLoc = getNum(t, 1) + "/" + getNum(t, 2) + ":" + getNum(t, 3) + "/" + getNum(t, 4);
	return uniLoc;
}
function refreshClick(){
	var params = {
		entityId : entityId,
		uniIndex : uniIndex
	};
	var url = '/epon/igmp/refreshIgmpUni.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.fetchingIgmpUni, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchIgmpUniFailed);
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchIgmpUniSuc);
			window.location.reload();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchIgmpUniFailed);
		},
		params : params
	});
}
//MVID的数据转换
function data0To1(){
	mvidData1 = new Array();
	var L = mvidData0.length;
	for(var t=0; 6*t<L; t++){
		mvidData1[t] = new Array();
		var tmpL = (L-6*t)<6 ? (L-6*t) : 6;
		for(var k=0; k<tmpL; k++){
			mvidData1[t][k] = mvidData0[6*t + k];
		}
	}
}
function data0To2(){
	mvidData2 = new Array();
	var L = mvidData0.length;
	for(var t=0; 3*t<L; t++){
		mvidData2[t] = new Array();
		var tmpL = (L-3*t)<3 ? (L-3*t) : 3;
		for(var k=0; k<tmpL; k++){
			mvidData2[t][k] = mvidData0[3*t + k];
		}
	}
}
function cancelClick(){
	window.parent.closeWindow('igmpUni');
}
function saveClick(){
	var uniLoc = getUniLocationByIndex(uniIndex);
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.IGMP.mdfIgmpUni , uniLoc), 'ext-mb-waiting');
	var tmpList = new Array();
	for(var i=0; i<uniMvlanData.length; i++){
		tmpList[i] = uniMvlanData[i][0];
	}
	var tmpStr = tmpList.join();
	if(false){//进行添加uniMvlan，成功后再修改uniIgmpInfo
		var params = {
			entityId : entityId,
			uniIndex : uniIndex,
			uniPortMvlanListStr : tmpStr
		};
		var url = '/epon/igmp/addIgmpUniPort.tv?r=' + Math.random();
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniFailed , uniLoc));
					return;
				}
				setTimeout('modifyUniIgmpInfo()',5000);
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniFailed , uniLoc));
			},
			params : params
		});
	}else if(false){//进行删除uniMvlan，之后不修改uniIgmpInfo
		if(addMvlanFlag){
			window.parent.closeWaitingDlg();
			$("#saveBt").attr("disabled",true);
			$("#saveBt").mouseout();
			return;
		}
		var params = {
			entityId : entityId,
			uniIndex : uniIndex
		};
		var url = '/epon/igmp/deleteIgmpUniPort.tv?r=' + Math.random();
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniFailed , uniLoc));
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniSuc , uniLoc));
				cancelClick();
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniFailed , uniLoc));
			},
			params : params
		});
	}else{//进行修改uniMvlan，成功后再修改uniIgmpInfo
		var params = {
			entityId : entityId,
			uniIndex : uniIndex,
			uniPortMvlanListStr : tmpStr
		};
		var url = '/epon/igmp/modifyIgmpUniPort.tv?r=' + Math.random();
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniFailed , uniLoc));
					return;
				}
				modifyUniIgmpInfo();
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniFailed , uniLoc));
			},
			params : params
		});
	}
}
function modifyUniIgmpInfo(){
	var uniLoc = getUniLocationByIndex(uniIndex);
	var maxGroupNum = $("#uniMaxNum").val();
	var mode = $("#uniMode").val();
	var transId = 0;
	if(!checkedNum(1)){
		$("#saveBt").attr("disabled",true);
		$("#saveBt").mouseout();
		return;
	}else{
		maxGroupNum = parseInt(maxGroupNum);
	}
	if(mode == 2){
		if(!checkedNum(2)){
			$("#saveBt").attr("disabled",true);
			$("#saveBt").mouseout();
			return;
		}else{
			transId = parseInt($("#uniTransId").val());
		}
	}
	var tmpStr = mvidData0.join();
	var params = {
		entityId : entityId,
		uniIndex : uniIndex,
		maxGroupNum : maxGroupNum,
		uniVlanMode : mode,
		transId : transId,
		uniPortMvidList : tmpStr
	};
	var url = '/epon/igmp/modifyIgmpMcUniConfig.tv?r=' + Math.random();
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniFailed , uniLoc));
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniSuc , uniLoc));
			cancelClick();
		},
		error : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.IGMP.mdfIgmpUniFailed , uniLoc));
		},
		params : params
	});
}
function uniModeChange(){
	$("#saveBt").attr("disabled",false);
	var uniMode = $("#uniMode").val();
	if(uniMode == 2){
		$("#transGrid").empty();
		$("#transField").show();
		$(".transSelect").show();
		loadTransGrid();
		$("#mvidField").css("width",305);
		$("#mvidDiv1").hide();
		data0To2();
		$("#mvidDiv2").show();
		$("#mvidDiv2").empty();
		loadMvidGrid2();
	}else{
		$("#transField").hide();
		$(".transSelect").hide();
		$("#mvidField").css("width",638);
		$("#mvidDiv2").hide();
		data0To1();
		$("#mvidDiv1").show();
		$("#mvidDiv1").empty();
		loadMvidGrid1();
	}
}

//组播模板列表
function loadMvlanData(){
	var k=0;
	for(var t=0; 5*t<mvlanList.length; t++){
		var tempFlag = true;
		for(var j=0; j<uniMvlanList.length; j++){
			if(mvlanList[5*t] == uniMvlanList[j]){
				tempFlag = false;
			}
		}
		if(tempFlag){
			mvlanData[k] = [mvlanList[5*t],mvlanList[5*t+1],mvlanList[5*t+2],mvlanList[5*t+3],mvlanList[5*t+4]];
			k++;
		}
	}
}
function loadMvlanGrid(){
	if(mvlanGrid==null || mvlanGrid==undefined){
		loadMvlanData();
	}
	mvlanStore = new Ext.data.SimpleStore({
		data : mvlanData,
		fields : ['mvlanId','mvlanOtherName','mvlanName','authority','mvlanProxyList']
	});
	mvlanGrid = new Ext.grid.GridPanel({
		id : 'mvlanGrid',
		renderTo : 'mvlanListGrid',
		store : mvlanStore,
		width : 310,
		height : 200,
		frame : false,
		autoScroll : true,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false,
			listeners: {
				'selectionchange' : function(){
					clickMvlanRow();
				}
			}
		}),
		border : false,
		columns: [{
				header: I18N.IGMP.templId,
				dataIndex: 'mvlanId',
				align: 'center',
				width: 75
			},{
				header: I18N.IGMP.templName,
				dataIndex: 'mvlanOtherName',
				align: 'center',
				width: 90,
				hidden : true
			},{
				header: I18N.IGMP.templAlias,
				dataIndex: 'mvlanName',
				align: 'center',
				width: 80
			},{
				header: I18N.COMMON.authority,
				dataIndex: 'authority',
				align: 'center',
				width: 50
			},{
				header: I18N.IGMP.mvlanList,
				dataIndex: 'mvlanProxyList',
				align: 'center',
				width: 80
			}]
	});
}
function clickMvlanRow(){
	$("#joinBt").attr("disabled",false);
}
//UNI允许通过的组播模板列表
function loadUniMvlanData(){
	var j=0;
	for(var t=0; 5*t<mvlanList.length; t++){
		var tempFlag = false;
		for(var k=0; k<uniMvlanList.length; k++){
			if(uniMvlanList[k] == mvlanList[5*t]){
				tempFlag = true;
			}
		}
		if(tempFlag){
			uniMvlanData[j] = [mvlanList[5*t],mvlanList[5*t+1],mvlanList[5*t+2],mvlanList[5*t+3],mvlanList[5*t+4]];
			j++;
		}
	}
}
function loadUniMvlanGrid(){
	if(uniMvlanGrid==null || uniMvlanGrid==undefined){
		loadUniMvlanData();
	}
	uniMvlanStore = new Ext.data.SimpleStore({
		data : uniMvlanData,
		fields : ['mvlanId','mvlanOtherName','mvlanName','authority','mvlanProxyList']
	});
	uniMvlanGrid = new Ext.grid.GridPanel({
		id : 'uniMvlanGrid',
		renderTo : 'uniMvlanListGrid',
		store : uniMvlanStore,
		width : 225,
		height : 200,
		frame : false,
		autoScroll : true,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false,
			listeners: {
				'selectionchange' : function(){
					clickUniRow();
				}
			}
		}),
		border : false,
		columns: [{
				header: I18N.IGMP.templId,
				dataIndex: 'mvlanId',
				align: 'center',
				width: 75
			},{
				header: I18N.IGMP.templAlias,
				dataIndex: 'mvlanOtherName',
				align: 'center',
				width: 90,
				hidden : true
			},{
				header: I18N.IGMP.templName,
				dataIndex: 'mvlanName',
				align: 'center',
				width: 125
			},{
				header: I18N.COMMON.authority,
				dataIndex: 'authority',
				align: 'center',
				width: 50,
				hidden : true
			},{
				header: I18N.IGMP.mvlanList,
				dataIndex: 'mvlanProxyList',
				align: 'center',
				width: 100,
				hidden : true
			}]
	});
}
function clickUniRow(){
	$("#outBt").attr("disabled",false);
}
//Trans列表
function transContext(grid,rowIndex,e){
	grid.selModel.selectRow(rowIndex,true);
	transMenu.showAt(e.getXY());
}
var transMenu = new Ext.menu.Menu({
    id:'transMenu',
    enableScrolling: false,
    items:[{
        id:'id1',
        text: I18N.UNI.mdfUniTransRule,
        handler : showIgmpTransJsp
    }]
});
function showIgmpTransJsp(){
	window.parent.createDialog("igmpTranslation", I18N.IGMP.translateMgmt, 450, 380, "epon/igmp/showIgmpTranslation.tv?entityId="+entityId,
			 null, true, true);
}
function ruleToData(){
	var j = 0;
	for(var k=0; k<transRule.length; k++){
		for(var t=8; 17-2*t<transRule[k].length; t--){
			transData[j] = new Array();
			transData[j][0] = transRule[k][0];
			transData[j][1] = transRule[k][17-2*t];
			transData[j][2] = transRule[k][18-2*t];
			transData[j][3] = (17 - transRule[k].length)/2;
			j++;
		}
	}
}
function loadTransGrid(){
	if(transGrid==null || transGrid==undefined){
		ruleToData();
	}
	transStore = new Ext.data.SimpleStore({
		data : transData,
		fields : ['transId','transOldId','transNewId','leaveNum']
	});
	transGrid = new Ext.grid.GridPanel({
		id : 'TransGrid',
		renderTo : 'transGrid',
		store : transStore,
		width : 305,
		height : 130,
		frame : false,
		autoScroll : true,
		border : false,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns: [{
				header: I18N.IGMP.translateId,
				dataIndex: 'transId',
				align: 'center',
				width: 100
			},{
				header: I18N.IGMP.originId,
				dataIndex: 'transOldId',
				align: 'center',
				width: 80
			},{
				header: I18N.IGMP.newMvlanId,
				dataIndex: 'transNewId',
				align: 'center',
				width: 90
			},{
				header: I18N.IGMP.restRules,
				dataIndex: 'leaveNum',
				align: 'center',
				width: 30,
				hidden: true
			}],
		listeners: {
			'rowcontextmenu': function(grid,rowIndex,e){
				transContext(grid,rowIndex,e);
	        },
	        'dblclick':{
	           	fn : doubleClick,
				scope : this
	        }
		}
	});
}
function doubleClick(){
	var selectedRow = transGrid.getSelectionModel().getSelected();
	var transIdTemp = selectedRow.get('transId');
	$("#uniTransId").val(transIdTemp);
	$("#saveBt").attr("disabled",false);
}

//MVID列表
function loadMvidGrid1(){
	mvidStore1 = new Ext.data.SimpleStore({
		data : mvidData1,
		fields : ['mvid0','mvid1','mvid2','mvid3','mvid4','mvid5']
	});
	mvidGrid1 = new Ext.grid.GridPanel({
		id : 'MvidGrid1',
		renderTo : 'mvidDiv1',
		store : mvidStore1,
		width : 630,
		height : 100,
		frame : false,
		autoScroll : true,
		hideHeaders : true,
		selModel : new Ext.grid.CellSelectionModel({
			singleSelect : true
		}),
		viewConfig: {
			forceFit: false
		},
		border : false,
		columns: [{
				header: "MVID",
				dataIndex: 'mvid0',
				align: 'center',
				width: 100,
				renderer : function(value, cellmeta, record, rowIndex, columnIndex) {
					if(value=="" || value==null || value==undefined){
						return;
					}
					return "<font>"+ value +"</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<img src='/images/close.gif' onclick='deleteMvid1(\"" 
						+ record.data.mvid0+ "\",\""+rowIndex+ "\",\"" + columnIndex + "\")'/>";
				}
			},{
				header: "MVID",
				dataIndex: 'mvid1',
				align: 'center',
				width: 100,
				renderer : function(value, cellmeta, record, rowIndex, columnIndex) {
					if(value=="" || value==null || value==undefined){
						return;
					}
					return "<font>"+ value +"</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<img src='/images/close.gif' onclick='deleteMvid1(\"" 
						+ record.data.mvid1+ "\",\""+rowIndex+ "\",\"" + columnIndex + "\")'/>";
				}
			},{
				header: "MVID",
				dataIndex: 'mvid2',
				align: 'center',
				width: 100,
				renderer : function(value, cellmeta, record, rowIndex, columnIndex) {
					if(value=="" || value==null || value==undefined){
						return;
					}
					return "<font>"+ value +"</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<img src='/images/close.gif' onclick='deleteMvid1(\"" 
						+ record.data.mvid2+ "\",\""+rowIndex+ "\",\"" + columnIndex + "\")'/>";
				}
			},{
				header: "MVID",
				dataIndex: 'mvid3',
				align: 'center',
				width: 100,
				renderer : function(value, cellmeta, record, rowIndex, columnIndex) {
					if(value=="" || value==null || value==undefined){
						return;
					}
					return "<font>"+ value +"</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<img src='/images/close.gif' onclick='deleteMvid1(\"" 
						+ record.data.mvid3+ "\",\""+rowIndex+ "\",\"" + columnIndex + "\")'/>";
				}
			},{
				header: "MVID",
				dataIndex: 'mvid4',
				align: 'center',
				width: 100,
				renderer : function(value, cellmeta, record, rowIndex, columnIndex) {
					if(value=="" || value==null || value==undefined){
						return;
					}
					return "<font>"+ value +"</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<img src='/images/close.gif' onclick='deleteMvid1(\"" 
						+ record.data.mvid4+ "\",\""+rowIndex+ "\",\"" + columnIndex + "\")'/>";
				}
			},{
				header: "MVID",
				dataIndex: 'mvid5',
				align: 'center',
				width: 100,
				renderer : function(value, cellmeta, record, rowIndex, columnIndex) {
					if(value=="" || value==null || value==undefined){
						return;
					}
					return "<font>"+ value +"</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<img src='/images/close.gif' onclick='deleteMvid1(\"" 
						+ record.data.mvid5+ "\",\""+rowIndex+ "\",\"" + columnIndex + "\")'/>";
				}
			}]
	});
}
function deleteMvid1(mvid, row, col){
	$("#mvidInput").val("");
	var tmpL = mvidData0.length;
	if(6 * mvidData1.length != tmpL){
		var tmpNum = -1;
		for(var k=0; k<tmpL; k++){
			if(mvid == mvidData0[k]){
				tmpNum = k;
			}
		}
		if(tmpNum != -1){
			mvidData0.splice(tmpNum, 1);
			data0To1();
			mvidStore1.loadData(mvidData1);
			if(col == 0){
				mvidGrid1.getSelectionModel().select(row-1, 5, false, false);
			}else{
				mvidGrid1.getSelectionModel().select(row, col-1, false, false);
			}
		}
	}else{
		var tmpRowNo = parseInt(row) * 6 + parseInt(col);
		mvidData0.splice(tmpRowNo, 1);
		data0To1();
		mvidStore1.loadData(mvidData1);
		if(col == 0){
			mvidGrid1.getSelectionModel().select(row-1, 5, false, false);
		}else{
			mvidGrid1.getSelectionModel().select(row, col-1, false, false);
		}
	}
}
function loadMvidGrid2(){
	mvidStore2 = new Ext.data.SimpleStore({
		data : mvidData2,
		fields : ['mvid0','mvid1','mvid2']
	});
	mvidGrid2 = new Ext.grid.GridPanel({
		id : 'MvidGrid2',
		renderTo : 'mvidDiv2',
		store : mvidStore2,
		width : 245,
		height : 100,
		frame : false,
		autoScroll : true,
		hideHeaders : true,
		selModel : new Ext.grid.CellSelectionModel({
			singleSelect : true
		}),
		viewConfig: {
			forceFit: false
		},
		border : false,
		columns: [{
				header: "MVID",
				dataIndex: 'mvid0',
				align: 'center',
				width: 75,
				renderer : function(value, cellmeta, record, rowIndex, columnIndex) {
					if(value=="" || value==null || value==undefined){
						return;
					}
					return "<font>"+ value +"</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<img src='/images/close.gif' onclick='deleteMvid2(\"" 
						+ record.data.mvid0+ "\",\""+rowIndex+ "\",\"" + columnIndex + "\")'/>";
				}
			},{
				header: "MVID",
				dataIndex: 'mvid1',
				align: 'center',
				width: 75,
				renderer : function(value, cellmeta, record, rowIndex, columnIndex) {
					if(value=="" || value==null || value==undefined){
						return;
					}
					return "<font>"+ value +"</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<img src='/images/close.gif' onclick='deleteMvid2(\"" 
						+ record.data.mvid1+ "\",\""+rowIndex+ "\",\"" + columnIndex + "\")'/>";
				}
			},{
				header: "MVID",
				dataIndex: 'mvid2',
				align: 'center',
				width: 75,
				renderer : function(value, cellmeta, record, rowIndex, columnIndex) {
					if(value=="" || value==null || value==undefined){
						return;
					}
					return "<font>"+ value +"</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<img src='/images/close.gif' onclick='deleteMvid2(\"" 
						+ record.data.mvid2+ "\",\""+rowIndex+ "\",\"" + columnIndex + "\")'/>";
				}
			}]
	});
}
function deleteMvid2(mvid, row, col){
	$("#mvidInput").val("");
	var tmpL = mvidData0.length;
	if(3 * mvidData2.length != tmpL){
		var tmpNum = -1;
		for(var k=0; k<tmpL; k++){
			if(mvid == mvidData0[k]){
				tmpNum = k;
			}
		}
		if(tmpNum != -1){
			mvidData0.splice(tmpNum, 1);
			data0To2();
			mvidStore2.loadData(mvidData2);
			if(col == 0){
				mvidGrid2.getSelectionModel().select(row-1, 2, false, false);
			}else{
				mvidGrid2.getSelectionModel().select(row, col-1, false, false);
			}
		}
	}else{
		var tmpRowNo = parseInt(row) * 3 + parseInt(col);
		mvidData0.splice(tmpRowNo, 1);
		data0To2();
		mvidStore2.loadData(mvidData2);
		if(col == 0){
			mvidGrid2.getSelectionModel().select(row-1, 2, false, false);
		}else{
			mvidGrid2.getSelectionModel().select(row, col-1, false, false);
		}
	}
}

Ext.onReady(function(){
	loadMvlanGrid();
	loadUniMvlanGrid();
	loadMvidGrid1();
	loadMvidGrid2();
	$("#uniMaxNum").val(uniIgmpValue[0]);
	$("#uniMode").val(uniIgmpValue[1]);
	if(uniIgmpValue[1]==2){
		$("#uniTransId").val(uniIgmpValue[2]);
	}
	if(uniIgmpValue[1]==null || uniIgmpValue[1]=="" || uniIgmpValue[1]==undefined){
		uniIgmpValue[1] = 1;
	}
	uniModeChange();
	$("#uniMaxNum").focus();
});	//end of Ext.onReady

function checkedNum(s){
	$("#saveBt").attr("disabled",false);
	var reg = /^([0-9])+$/ig;
	if(s == 1){
		$("#uniMaxNum").css("border","1px solid #8bb8f3");
		var tempNum = $("#uniMaxNum").val();
		if(parseInt(tempNum)>64 || isNaN(tempNum) || !reg.exec(tempNum)){
			$("#uniMaxNum").css("border","1px solid #FF0000");
			return false;
		}
		return true;
	}else if(s == 2){
		$("#uniTransId").css("border","1px solid #8bb8f3");
		var tempNum = $("#uniTransId").val();
		var tempNumber = 0;
		transStore.filterBy(function(record){
			if(record.get('transId') == tempNum){
				tempNumber++;
			}
			return record.get('transId') == tempNum;
		});
		if(tempNumber == 0){
			transStore.filterBy(function(record){
				return true;
			});
		}
		if(parseInt(tempNum)>64 || isNaN(tempNum) || !reg.exec(tempNum)){
			$("#uniTransId").css("border","1px solid #FF0000");
			return false;
		}
		return true;
	}
}
function joinClick(){
	if(mvlanGrid.getSelectionModel().getSelected()==null || mvlanGrid.getSelectionModel().getSelected()==undefined){
		return;
	}
	//单行选择
	/* var mvlanTempId = mvlanGrid.getSelectionModel().getSelected().get('mvlanId');
	var tempIndex;
	for(var i=0; i<mvlanData.length; i++){
		if(mvlanData[i][0] == mvlanTempId){
			tempIndex = i;
		}
	}
	uniMvlanData[uniMvlanData.length] = mvlanData[tempIndex];
	for(var j=tempIndex; j<mvlanData.length-1; j++){
		mvlanData[j] = mvlanData[j+1];
	}
	mvlanData.length = mvlanData.length-1; */
	//end 单行选择
	//多行选择
	var tempSel = mvlanGrid.getSelectionModel().selections;
	var tempRowIndex = new Array();
	for(var x=0; tempSel.items[x]; x++){
		tempRowIndex[x] = mvlanGrid.getStore().indexOf(tempSel.items[x]);
	}
	tempRowIndex.sort(function(a,b){
		return a - b;
	});
	var tempLength = tempRowIndex.length;
	for(var k=0; k<tempLength; k++){
		uniMvlanData.unshift(mvlanData[tempRowIndex[k] - k]);
		mvlanData.splice(tempRowIndex[k] - k, 1);
	}
	//end 多行选择
	mvlanStore.loadData(mvlanData);
	uniMvlanStore.loadData(uniMvlanData);
	$("#joinBt").attr("disabled",true);
	$("#joinBt").mouseout();
	$("#outBt").attr("disabled",true);
	$("#saveBt").attr("disabled",false);
}
function outClick(){
	if(uniMvlanGrid.getSelectionModel().getSelected()==null || uniMvlanGrid.getSelectionModel().getSelected()==undefined){
		return;
	}
	//单行选择
	/* var uniMvlanTempId = uniMvlanGrid.getSelectionModel().getSelected().get('mvlanId');
	var tempIndex;
	for(var i=0; i<uniMvlanData.length; i++){
		if(uniMvlanData[i][0] == uniMvlanTempId){
			tempIndex = i;
		}
	}
	mvlanData[mvlanData.length] = uniMvlanData[tempIndex];
	for(var j=tempIndex; j<uniMvlanData.length-1; j++){
		uniMvlanData[j] = uniMvlanData[j+1];
	}
	uniMvlanData.length = uniMvlanData.length-1; */
	//end 单行选择
	//多行选择
	var tempSel = uniMvlanGrid.getSelectionModel().selections;
	var tempRowIndex = new Array();
	for(var x=0; tempSel.items[x]; x++){
		tempRowIndex[x] = uniMvlanGrid.getStore().indexOf(tempSel.items[x]);
	}
	tempRowIndex.sort(function(a,b){
		return a - b;
	});
	var tempLength = tempRowIndex.length;
	for(var k=0; k<tempLength; k++){
		mvlanData.unshift(uniMvlanData[tempRowIndex[k] - k]);
		uniMvlanData.splice(tempRowIndex[k] - k, 1);
	}
	//end多行选择
	mvlanStore.loadData(mvlanData);
	uniMvlanStore.loadData(uniMvlanData);
	$("#outBt").attr("disabled",true);
	$("#outBt").mouseout();
	$("#joinBt").attr("disabled",true);
	$("#saveBt").attr("disabled",false);
}
function mvidKeyup(){
	var tmp = $("#mvidInput").val();
	$("#searchBt").attr("disabled",true);
	$("#addBt").attr("disabled",true);
	if(tmp=="" || tmp==null || tmp==undefined){
		$("#searchBt").attr("disabled",false);
		return;
	}
	if(!checkMvid()){
		return;
	}else{
		$("#searchBt").attr("disabled",false);
		var tempFlag = true;
		var tmpL = mvidData0.length;
		for(var x=0; x<tmpL; x++){
			if(mvidData0[x] == parseInt(tmp)){
				tempFlag = false;
			}
		}
		if(tempFlag){
			$("#addBt").attr("disabled",false);
		}
	}	
}
function checkMvid(){
	var tmp = $("#mvidInput").val();
	var reg = /^([1-9][0-9]{0,3})+$/i;
	$("#mvidInput").css("border","1px solid #8bb8f3");
	if(reg.exec(tmp) && parseInt(tmp)<4095){
		return true;
	}
	$("#mvidInput").css("border","1px solid #ff0000");
	return false;
}
function addClick(){
	var tmp = $("#mvidInput").val();
	if(!checkMvid()){
		return;
	}else if(mvidData0.length>63){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.maxUniMvid);
		return;
	}else{
		mvidData0.unshift(parseInt(tmp));
		if($("#uniMode").val() == 2){
			data0To2();
			mvidStore2.loadData(mvidData2);
		}else{
			data0To1();
			mvidStore1.loadData(mvidData1);
		}
		$("#mvidInput").val("");
		$("#addBt").attr("disabled",true);
	}
}
function searchClick(){
	var tmp = $("#mvidInput").val();
	if(tmp=="" || tmp==null || tmp==undefined){
		mvidData0.sort(function(a,b){
			return parseInt(a) - parseInt(b);
		});
		if($("#uniMode").val() == 2){
			data0To2();
			mvidStore2.loadData(mvidData2);
			mvidGrid2.getSelectionModel().select(0, 0, false, false);
		}else{
			data0To1();
			mvidStore1.loadData(mvidData1);
			mvidGrid1.getSelectionModel().select(0, 0, false, false);
		}
	}else if(!checkMvid()){
		return;
	}else{
		var tmpF = true;
		var tmpL = mvidData0.length;
		var tmpData = new Array();
		for(var i=0; i<tmpL; i++){
			if(mvidData0[i] == parseInt(tmp)){
				tmpF = false;
			}
			if(mvidData0[i].toString().indexOf(tmp.toString()) != -1){
				tmpData.push(mvidData0[i]);
			}
		}
		if(tmpF){	//无
			var tempData = new Array();
			var tempL = tmpData.length;
			if($("#uniMode").val() == 2){
				for(var t=0; 3*t<tempL; t++){
					tempData[t] = new Array();
					var tmpL = (tempL-3*t)<3 ? (tempL-3*t) : 3;
					for(var k=0; k<tmpL; k++){
						tempData[t][k] = tmpData[3*t + k];
					}
				}
				mvidStore2.loadData(tempData);
			}else{
				for(var t=0; 6*t<tempL; t++){
					tempData[t] = new Array();
					var tmpL = (tempL-6*t)<6 ? (tempL-6*t) : 6;
					for(var k=0; k<tmpL; k++){
						tempData[t][k] = tmpData[6*t + k];
					}
				}
				mvidStore1.loadData(tempData);
			}
		}else{	//有
			if($("#uniMode").val() == 2){
				data0To2();
				mvidStore2.loadData(mvidData2);
				var tmpII = mvidData0.indexOf(tmp);
				var index1 = parseInt(tmpII / 3);
				var index2 = tmpII % 3;
				mvidGrid2.getSelectionModel().select(index1, index2, false, false);
			}else{
				data0To1();
				mvidStore1.loadData(mvidData1);
				var tmpII = mvidData0.indexOf(tmp);
				var index1 = parseInt(tmpII / 6);
				var index2 = tmpII % 6;
				mvidGrid1.getSelectionModel().select(index1, index2, false, false);
			}
		}
	}
}
</script>
</head>
<body class=BLANK_WND style="margin: 15px;overflow:hidden;">
	<table>
		<tr><td>
			<table>
				<tr><td>
					<div id=uniMvlanField>
						<fieldset style="width:230px;height:220px;">
							<legend><fmt:message bundle="${i18n}" key="UNI.uniMvlanGridTitle" /></legend>
							<div id=uniMvlanListGrid></div>
						</fieldset>
					</div>
				</td><td width=90><table>
					<tr style="vlign:bottom;"><td>&nbsp;&nbsp;
						<button id=joinBt class=BUTTON75 type="button" disabled
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'"
							onclick="joinClick()"><< <fmt:message bundle="${i18n}" key="COMMON.moveIn" /></button>
					</td></tr>
					<tr style="vlign:top;"><td>&nbsp;&nbsp;
						<button id=outBt class=BUTTON75 type="button" disabled
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'"
							onclick="outClick()"><fmt:message bundle="${i18n}" key="COMMON.moveOut" /> >></button>
					</td></tr></table>
				</td><td>
					<div id=mvlanField>
						<fieldset style="width:315px;height:220px;">
							<legend><fmt:message bundle="${i18n}" key="IGMP.templList" /></legend>
							<div id=mvlanListGrid></div>
						</fieldset>
					</div>
				</td></tr>
			</table>
		</td></tr>
		<tr><td>
			<table>
				<tr style="height:35px;"><td style="width:90px;" align=left><fmt:message bundle="${i18n}" key="COMMOIGMP.maxMvlanN.query" />
				</td><td>
					<input id="uniMaxNum" value="4" maxlength=2 style="width:40px;" onkeyup=checkedNum(1) />
				</td><td id="maxNumText" align=center width=190><fmt:message bundle="${i18n}" key="IGMP.range64" />
				</td><td style="width:90px;" align=left>&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="UNI.mvidHandleMode" />
				</td><td>
					<select id="uniMode" style="width:60px;" onchange=uniModeChange()>
						<option value=1><fmt:message bundle="${i18n}" key="UNI.strip" /></option>
						<option value=2><fmt:message bundle="${i18n}" key="UNI.trans" /></option>
						<option value=3><fmt:message bundle="${i18n}" key="UNI.substance" /></option>
					</select>
				</td><td>
				</td><td style="width:90px;display:none;" align=left class=transSelect>
					&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="UNI.mvlanTransRuleId" />
				</td><td style="display:none;" class=transSelect>
					<input id="uniTransId" maxlength=2 style="width:40px;" onkeyup=checkedNum(2) 
						title='<fmt:message bundle="${i18n}" key="IGMP.range64" />' />
				</td></tr>
			</table>
		</td></tr>
		<tr><td>
			<table>
				<tr><td>
					<fieldset id=mvidField style="width:638;height:148;">
						<legend><fmt:message bundle="${i18n}" key="UNI.mvlanNeedHandleList" /></legend>
						<div>&nbsp;&nbsp;&nbsp;&nbsp;MVID:<input id="mvidInput" style="width:70px;border:1px solid #8bb8f3"
							 	maxlength=4 title="<fmt:message bundle="${i18n}" key="IGMP.range4094" />" 
							 	onkeyup=mvidKeyup() />&nbsp;&nbsp;&nbsp;&nbsp;
							<button id=addBt type="button"
								style="color:blue;height:20px;border:0px;background-color:white;text-valign:button;"
								onclick="addClick()"><fmt:message bundle="${i18n}" key="COMMON.add" /></button>&nbsp;&nbsp;
							<button id=searchBt type="button" 
								style="color:blue;height:20px;border:0px;background-color:white;text-valign:button;"
								onclick="searchClick()"><fmt:message bundle="${i18n}" key="COMMON.query" /></button>
						</div>
						<div id=mvidDiv1></div>
						<div id=mvidDiv2 style="display:none;"></div>
					</fieldset>
				</td><td width=20>
				</td><td>
					<div id=transField style="display:none;">
						<fieldset><legend><fmt:message bundle="${i18n}" key="UNI.mvlanTransRuleList" /></legend>
							<div id=transGrid></div>
						</fieldset>
					</div>
				</td></tr>
			</table>
		</td></tr>
	</table>
	<div align="right" style="padding-right:10px;padding-top:2px;">
		<button id=refreshBt class=BUTTON95 type="button"
			onMouseOver="this.className='BUTTON_OVER95'"
			onMouseOut="this.className='BUTTON95'"
			onMouseDown="this.className='BUTTON_PRESSED95'"
			onclick="refreshClick()"><fmt:message bundle="${i18n}" key="COMMON.fetch" /></button>&nbsp;&nbsp;
		<button id=saveBt class=BUTTON95 type="button" disabled
			onMouseOver="this.className='BUTTON_OVER95'"
			onMouseOut="this.className='BUTTON95'"
			onMouseDown="this.className='BUTTON_PRESSED95'"
			onclick="saveClick()"><fmt:message bundle="${i18n}" key="COMMON.saveCfg" /></button>&nbsp;&nbsp;
		<button id=cancelBt class=BUTTON95 type="button"
			onMouseOver="this.className='BUTTON_OVER95'"
			onMouseOut="this.className='BUTTON95'"
			onMouseDown="this.className='BUTTON_PRESSED95'"
			onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
	</div>
</body>
</html>