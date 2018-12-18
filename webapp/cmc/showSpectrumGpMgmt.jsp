<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	module cmc
</Zeta:Loader>
<head>
<script type="text/javascript">
var pageSize = <%=uc.getPageSize()%>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = '<s:property value="entityId"/>';
var grid = null;
var store = null;
function saveClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var groupIds = [];
		for (var i = 0; i < selections.length; i++) {
			groupIds[i] = selections[i].data.groupId;
		}
		window.top.showConfirmDlg("@CMC.tip.tipMsg@", "@CMC.GP.saveToglobalgp@", function(type) {
			if (type == 'no') {return;}
			$.ajax({
				url : '/ccmtsspectrumgp/transGroupFromDeviceToGlobal.tv',
				type : 'POST',
				data : "entityId="+ entityId +  "&groupIds=" + groupIds + "&num=" + Math.random(),
				dataType : "text",
				success : function(text) {
					if (text == 'success') {
						window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.saveToGlobalgpsuc@");
						store.reload();
					}else{
						window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.saveToGlobalgpfail@");
					}
				},
				error : function(text) {
					window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.saveToGlobalgpfail@");
				},
				cache : false
			});
		});
	} else {
		window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.GP.selectGp@");
	}
}

function cancelClick() {
	window.parent.closeWindow('ccmtsSpectrumGroupMgmt');	
}

function refreshData() {
	window.top.showWaitingDlg("@CMC.tip.waiting@", "@CMC.GP.refreshGp@");
	$.ajax({
		url : '/ccmtsspectrumgp/refreshGroupFromDevice.tv',
		type : 'POST',
		data : "entityId=" + entityId + "&num="	+ Math.random(),
		dataType : "text",
		success : function(text) {
			if (text == 'success') {
				window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.refreshGpsuccess@");
				store.reload();
			} else {
				window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.refreshGpFail@");
			}
		},
		error : function(text) {
			window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.refreshGpFail@");
		},
		cache : false
	});
}

Ext.onReady(function() {
	SelectionModel = new Ext.grid.CheckboxSelectionModel({
		header : '<div class="x-grid3-hd-checker"></div>',
		singleSelect : false
	});
	var cm = [
		SelectionModel,
		{
			header : '@CMC.GP.groupId@',
			width : 120,
			sortable : true,
			dataIndex : 'groupId'
		},
		{
			header : '@CMC.GP.adminStatus@',
			width : 120,
			sortable : true,
			dataIndex : 'adminStatus',
			renderer : statusRender
		},
		{
			header : '@CMC.GP.hopPeriod@',
			width : 120,
			sortable : true,
			dataIndex : 'hopPeriod'
		},
		{
			header : '@CMC.GP.maxHopLimit@',
			width : 120,
			sortable : true,
			dataIndex : 'maxHopLimit'
		},
		{
			header : '@CMC.GP.hopPolicy@',
			width : 120,
			sortable : true,
			dataIndex : 'groupPolicy',
			renderer : policyRender
		},
		{
			header : '@CHANNEL.operation@',
			width : 120,
			sortable : true,
			dataIndex : 'op',
			renderer : opeartionRender
		} ];

	store = new Ext.data.JsonStore({
		url : '/ccmtsspectrumgp/loadDeviceGroupList.tv?entityId='+entityId,
		root : 'data',
		totalProperty: 'rowCount',
		remoteSort : false,
		fields : [ {
			name : 'groupId'
		}, {
			name : 'adminStatus'
		}, {
			name : 'hopPeriod'
		}, {
			name : 'maxHopLimit'
		}, {
			name : 'groupPolicy'
		},{
			name : 'entityId'
		}]
	});

	grid = new Ext.grid.GridPanel(
		{
		store : store,
		width: 770, 
		height: 380,
		border:true,
		animCollapse : false,
		trackMouseOver : trackMouseOver,
		columns : cm,
		sm : SelectionModel,
		bodyCssClass:'normalTable',
		tbar : [
				{
					text : '@CMC.GP.addGroup@',
					iconCls : 'bmenu_new',
					disabled : !operationDevicePower,
					handler : addDeviceGroup
				},
				'-',
				{
					text : '@CMC.GP.importGlobalGp@',
					iconCls : 'bmenu_new',
					disabled : !operationDevicePower,
					handler : addGlobalGroup
				}],
		bbar : buildPagingToolBar(),
		viewConfig: {
	        forceFit: true
		},
		renderTo : "groupGrid"
	});

	store.load({
		params : {
			start : 0,
			limit : pageSize
		}
	});
});

function buildPagingToolBar() {
	var pagingToolbar = new Ext.PagingToolbar({
		id : 'extPagingBar',
		pageSize : pageSize,
		store : store,
		displayInfo : true,
		items : [ "-", String.format('@COMMON.pagingTip@', pageSize), '-' ]
	});
	return pagingToolbar;
}

function addDeviceGroup(){
	window.top.createDialog('showNewDeviceGroup', "@CMC.GP.addGroup@", 'mSize', 600,
			'/ccmtsspectrumgp/showNewDeviceGroup.tv?entityId=' + entityId + "&groupModifyFlag=0", null, true, true);
}

function addGlobalGroup(){
	window.top.createDialog('showGlobalGroupList', "@CMC.GP.selectGlobalGp@", 800, 500,
			'/ccmtsspectrumgp/showGlobalGroupList.tv?entityId='+entityId, null, true, true);
}

function opeartionRender(value, cellmate, record){
	var entityId = record.data.entityId;
	var groupId = record.data.groupId;
	var adminStatus = record.data.adminStatus;
	if(operationDevicePower){
		return String.format(
				"<a href='javascript:;' onclick='modifyGroup(\"{0}\",\"{1}\")'>@CMC.text.modify@</a> / " + 
				"<a href='javascript:;' onclick='deleteGroup(\"{0}\",\"{1}\",\"{2}\")'>@route.button.delete@</a>", entityId,groupId,adminStatus);
	}else{
		return String.format(
				"<img src='/images/edit.gif' title='@CMC.text.modify@'" + 
				"onclick='modifyGroup(\"{0}\",\"{1}\")' style='cursor:pointer;'/>&nbsp;&nbsp;&nbsp;&nbsp;" + 
				"<img src='/images/deleteDisable.gif' title='@route.button.delete@'" + 
				"style='cursor:pointer;'/>", entityId,groupId,adminStatus);
	}
}

function modifyGroup(entityId,groupId){
	window.top.createDialog('showNewDeviceGroup', "@CMC.GP.modifyGroup@", 'mSize', 600,
			'/ccmtsspectrumgp/showNewDeviceGroup.tv?entityId=' + entityId + "&groupId="+groupId +"&groupModifyFlag=1", null, true, true);
}

function getGroupChlNum(entityId,groupId){
	var chlNum = 0;
	$.ajax({
		url : '/ccmtsspectrumgp/loadGroupChlNum.tv',
		type : 'POST',
		async: false,
		data : "entityId=" + entityId +"&groupId=" + groupId+ "&num=" + Math.random(),
		dataType : "text",
		success : function(text) {
			chlNum = text;
		},
		error : function(text) {
		},
		cache : false
	});
	return chlNum;
}

function deleteGroup(entityId,groupId,adminStatus){
	if(adminStatus == 1){
		//获取有多少个信道信用了此跳频组
		var chlNum = getGroupChlNum(entityId,groupId);
		if(chlNum > 0){
			window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.deleteGroupnote@");
			return;
		}
	}
	window.top.showConfirmDlg("@CMC.tip.tipMsg@", "@CMC.GP.deleteGpconfirm@@COMMON.wenhao@", function(type) {
		if (type == 'no') {return;}
		window.top.showWaitingDlg("@CMC.tip.waiting@", "@CMC.GP.deletingGp@");
		$.ajax({
			url : '/ccmtsspectrumgp/deleteDeviceGroup.tv',
			type : 'POST',
			data : "entityId=" + entityId +"&groupId=" + groupId+ "&num=" + Math.random(),
			dataType : "text",
			success : function(text) {
				if (text == 'success') {
					window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.deleteGpSuccess@");
					store.reload();
				} else {
					window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.deleteGpFail@");
				}
			},
			error : function(text) {
				window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.deleteGpFail@");
			},
			cache : false
		});
	});
}

function statusRender(value, cellmate, record){
	var adminStatus = record.data.adminStatus;
	var imgStr;
	if(adminStatus ==1){
		imgStr = "<img src = '/images/speOn.png'/>";
	}else{
		imgStr = "<img src = '/images/speOff.png'/>";
	}
	return imgStr;
}

function policyRender(value, cellmate, record){
	var groupPolicy = record.data.groupPolicy;
	var policyStr;
	if(groupPolicy == 1){
		policyStr = "@CMC.GP.freqOnly@";
	}else if(groupPolicy == 2){
		policyStr = "@CMC.GP.widthOnly@";
	}else if(groupPolicy == 3){
		policyStr = "@CMC.GP.modulationOnly@";
	}else if(groupPolicy == 4){
		policyStr = "@CMC.GP.freqWidthOnly@";
	}else if(groupPolicy == 5){
		policyStr = "@CMC.GP.priority@";
	}
	return policyStr;
}
</script>
</head>
<body class="openWinBody">
	<div id = "groupGrid" style="margin:10px 15px"></div>
	
	<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
	    <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="refreshData()"  id="refreshBt">
	                <span><i class="miniIcoEquipment"></i>@CMC.title.refreshDataFromEntity@</span>
	            </a>
	        </li>
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="saveClick()"  id="saveBt">
	                <span><i class="miniIcoSaveOK"></i>@CMC.GP.saveGlobalGp@</span>
	            </a>
	        </li>
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()" id="cancelBt">
	                <span>@CMC.button.cancel@</span>
	            </a>
	        </li>
	    </ol>
	</div>
</body>
</Zeta:HTML>