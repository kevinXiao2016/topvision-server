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
<style type="text/css">
body,html{height:100%; overflow:hidden;}
.openFrame{ position: absolute; top:0px; left:0px; z-index:2; display:none;}
</style>
<script type="text/javascript">
var pageSize = <%=uc.getPageSize()%>;
var grid = null;
var store = null;

function cancelClick() {
	window.parent.closeWindow('showGlobalSpectrumGp');	
}
function onRefreshClick() {
	store.reload();
}

Ext.onReady(function() {
	SelectionModel = new Ext.grid.CheckboxSelectionModel({
		header : '<div class="x-grid3-hd-checker"></div>',
		singleSelect : false
	});
	var cm = [
		SelectionModel,
		{
			header : '@CMC.GP.groupName@',
			width : 300,
			sortable : true,
			dataIndex : 'emsGroupName'
		},
		{
			header : '@CMC.GP.adminStatus@',
			width : 110,
			sortable : true,
			dataIndex : 'adminStatus',
			renderer : statusRender
		},
		{
			header : '@CMC.GP.hopPeriod@',
			width : 150,
			sortable : true,
			dataIndex : 'hopPeriod'
		},
		{
			header : '@CMC.GP.maxHopLimit@',
			width : 150,
			sortable : true,
			dataIndex : 'maxHopLimit'
		},
		{
			header : '@CMC.GP.hopPolicy@',
			width : 180,
			sortable : true,
			dataIndex : 'groupPolicy',
			renderer : policyRender
		},
		{
			header : '@CHANNEL.operation@',
			width : 180,
			sortable : true,
			dataIndex : 'op',
			renderer : opeartionRender
		} ];

	store = new Ext.data.JsonStore({
		url : '/ccmtsspectrumgp/loadGlobalSpectrumGpList.tv',
		root : 'data',
		totalProperty: 'rowCount',
		remoteSort : false,
		fields : [ {
			name : 'emsGroupName'
		}, {
			name : 'adminStatus'
		}, {
			name : 'hopPeriod'
		}, {
			name : 'maxHopLimit'
		}, {
			name : 'groupPolicy'
		},{
			name : 'emsGroupId'
		}]
	});

	grid = new Ext.grid.GridPanel(
		{
		store : store,
		border:true,
		animCollapse : false,
		trackMouseOver : trackMouseOver,
		columns : cm,
		bodyCssClass:'normalTable',
		viewConfig:{forceFit:true},
		sm : SelectionModel,
		tbar : [
				{
					text : '@CMC.GP.addGroup@',
					cls: 'mL10',
					iconCls : 'bmenu_new',
					handler : addGlobalGroup
				},
				'-',
				{
					text : '@CMC.GP.deleteGlobalGp@',
					iconCls : 'bmenu_delete',
					handler : deleteGlobalGroup
				}],
		bbar : buildPagingToolBar(),
		renderTo : document.body
	});
	new Ext.Viewport({layout: 'fit', items: [grid]});
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

function closeFrame(){
	var $iframe = $("#openFrame");
	$iframe.fadeOut().attr("src","");
}
function addGlobalGroup(){
	var $iframe = $("#openFrame");
	frames["openFrame"].location.href = "/ccmtsspectrumgp/showNewGlobalSpectrumGp.tv?groupModifyFlag=0";
	$iframe.fadeIn();
	/* window.top.createDialog('showNewGlobalSpectrumGp', '@CMC.GP.addGlobalGp@', 600, 570,
			'/ccmtsspectrumgp/showNewGlobalSpectrumGp.tv?groupModifyFlag=0', null, true, true); */
}

function deleteGlobalGroup(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var emsGroupId = [];
		for (var i = 0; i < selections.length; i++) {
			emsGroupId[i] = selections[i].data.emsGroupId;
		}
		window.top.showConfirmDlg("@CMC.tip.tipMsg@", "@CMC.GP.confirmDelGlobalGp@", function(type) {
			if (type == 'no') {return;}
			$.ajax({
				url : '/ccmtsspectrumgp/deleteGlobalSpectrumGp.tv',
				type : 'POST',
				data : "groupIds=" + emsGroupId + "&num=" + Math.random(),
				dataType : "text",
				success : function(text) {
					if (text == 'success') {
						window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.delGlobalGpSuccess@");
						store.reload();
					}else{
						window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.delGlobalGpFail@");
					}
				},
				error : function(text) {
					window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.delGlobalGpFail@");
				},
				cache : false
			});
		});
	} else {
		window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.GP.selGlobalGp@");
	}
}

function opeartionRender(value, cellmate, record){
	var emsGroupId = record.data.emsGroupId;
	return String.format(
			"<a href='javascript:;'" + 
			"onclick='modifyGlobalGroup(\"{0}\")' >@CMC.text.modify@</a> / " + 
			"<a href='javascript:;'" + 
			"onclick='deleteGlobalGroup()'>@route.button.delete@</a>",emsGroupId);
}

function modifyGlobalGroup(emsGroupId){
	var $iframe = $("#openFrame");
	frames["openFrame"].location.href = '/ccmtsspectrumgp/showNewGlobalSpectrumGp.tv?emsGroupId='+emsGroupId +'&groupModifyFlag=1';
	$iframe.fadeIn();
	/* window.top.createDialog('showNewGlobalSpectrumGp', '@CMC.GP.modifyGlobalGp@', 600, 570,
			'/ccmtsspectrumgp/showNewGlobalSpectrumGp.tv?emsGroupId='+emsGroupId +'&groupModifyFlag=1', null, true, true); */
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
<body class="whiteToBlack">
	<iframe width="100%" height="100%" src="" frameborder="0" id="openFrame" name="openFrame" class="openFrame"></iframe>
</body>
</Zeta:HTML>