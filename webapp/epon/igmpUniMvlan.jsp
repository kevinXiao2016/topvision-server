<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library ext
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
Ext.Ajax.timeout = 100000;
var entityId = ${entityId};
var uniIndex = ${uniIndex};

//组播模板
var mvlanList = ${mvlanList};
mvlanList = mvlanList.join("") == "false" ? new Array() : mvlanList;
var mvlanGrid;
var mvlanStore;
var mvlanData = new Array();
//UNI口允许通过的组播模板
var uniMvlanList = ${uniMvlanList};
if(uniMvlanList.join("") == "false"){
	uniMvlanList = new Array();
}
var uniMvlanGrid;
var uniMvlanStore;
var uniMvlanData = new Array();


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
	    cls:"normalTable edge10",stripeRows:true,region: "center",bodyCssClass: 'normalTable',
	    viewConfig: {forceFit: true},
		title : I18N.UNI.mvlanGridTitle ,
		id : 'mvlanGrid',
		renderTo : 'mvlanListGrid',
		store : mvlanStore,
		width : 310,
		height : 360,
		autoScroll : true,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false,
			listeners: {
				'selectionchange' : function(){
					clickMvlanRow();
				}
			}
		}),
		border : true,
		columns: [{
				header: I18N.IGMP.templId ,dataIndex: 'mvlanId'
			},{
				header: I18N.IGMP.templName,dataIndex: 'mvlanName'
			},{
				header: I18N.COMMON.authority,dataIndex: 'authority'
			},{
				header: I18N.IGMP.mvlanList,dataIndex: 'mvlanProxyList'
			}]
	});
}
function clickMvlanRow(){
	if(operationDevicePower){
		$("#joinBt").attr("disabled",false);
	}
}
//UNI口允许通过的组播组播模板列表
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
	    cls:"normalTable edge10",stripeRows:true,region: "center",bodyCssClass: 'normalTable',
	    viewConfig: {forceFit: true},
		title : I18N.UNI.uniMvlanGridTitle ,
		id : 'uniMvlanGrid',
		renderTo : 'uniMvlanListGrid',
		store : uniMvlanStore,
		width : 270,
		height : 360,
		autoScroll : true,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false,
			listeners: {
				'selectionchange' : function(){
					clickUniRow();
				}
			}
		}),
		border : true,
		columns: [{
				header: I18N.IGMP.templId,dataIndex: 'mvlanId'
			},{
				header: I18N.IGMP.templName,dataIndex: 'mvlanName'
			}]
	});
}
function clickUniRow(){
	if(operationDevicePower){
		$("#outBt").attr("disabled",false);
	}
}

Ext.onReady(function(){
	loadMvlanGrid();
	loadUniMvlanGrid();
});

function joinClick(){
	if(mvlanGrid.getSelectionModel().getSelected()==null || mvlanGrid.getSelectionModel().getSelected()==undefined){
		return;
	}
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
	//$("#saveBt").attr("disabled",false);
}
function outClick(){
	if(uniMvlanGrid.getSelectionModel().getSelected()==null || uniMvlanGrid.getSelectionModel().getSelected()==undefined){
		return;
	}
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
	//$("#saveBt").attr("disabled",false);
}
function saveBtClick(){
	var uniLoc = getUniLocationByIndex(uniIndex);
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.UNI.mdfingUniMvlanTemplList , 'ext-mb-waiting');
	var tmpList = new Array();
	for(var i=0; i<uniMvlanData.length; i++){
		tmpList[i] = uniMvlanData[i][0];
	}
	var tmpStr = tmpList.join();
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
					window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.UNI.mdfUniMvlanTemplListEr , uniLoc))
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.UNI.mdfUniMvlanTemplListOk , uniLoc))
				cancelClick();
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.UNI.mdfUniMvlanTemplListEr , uniLoc))
			},
			params : params
		});
}
function refreshClick(){
	var params = {
		entityId : entityId,
		uniIndex : uniIndex
	};
	var url = '/epon/igmp/refreshIgmpUni.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.UNI.fetchingUniIgmpInfo , 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.fetchUniIgmpInfoEr )
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.fetchUniIgmpInfoOk )
			window.location.reload();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.fetchUniIgmpInfoEr )
		},
		params : params
	});
}
function cancelClick(){
	window.parent.closeWindow('igmpUniMvlan')
}
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
	var t = parseInt(index / 256) + (index % 256);
	var uniLoc = "";
	var i = 4;
	while (i > 0) {
		uniLoc = getNum(t, i) + uniLoc;
		if(i & 1){
			if(i & 2){
				uniLoc = ":" + uniLoc;
			}
		}else{
			uniLoc = "/" + uniLoc;
		}
		i--;
	}
	return uniLoc;
}
function authLoad(){
	if(!operationDevicePower){
	    $("#joinBt").attr("disabled",true);
	    $("#outBt").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
<body class=openWinBody onload="authLoad()">
	<table>
		<tr><td>
			<div id=uniMvlanListGrid></div>
		</td><td>
			<button id=joinBt class=BUTTON75 onclick=joinClick()>@COMMON.moveIn@ <<</button><br><br>
			<button id=outBt class=BUTTON75 onclick=outClick()>@COMMON.moveOut@ >></button><br><br>
			<font color=gray>@COMMON.moveManuTip@</font>
			</td><td>
			<div id=mvlanListGrid></div>
		</td></tr>
	</table>
	
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="saveBt" onClick="saveBtClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>