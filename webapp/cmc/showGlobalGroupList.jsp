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
var entityId = '<s:property value="entityId"/>';
var ccmtsSpectrumGpListJson = ${ccmtsSpectrumGpListJson}? ${ccmtsSpectrumGpListJson} : new Array();
var grid = null;
var store = null;
var groupId = null;

function getGroupId(){
	for(var j=1; j<33; j++){
		var groupAddFlag = true
		for(var k=0; k<ccmtsSpectrumGpListJson.length; k++){
			if(ccmtsSpectrumGpListJson[k].groupId == j){
				groupAddFlag = false
			}
		}
		if(groupAddFlag){
			groupId = j;
			return j;
		}
	}
	return null;
}

function saveClick() {
	groupId = getGroupId();
	//已经超过32个跳频组
	if(groupId == null){
		window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.groupLimit@");
		return;
	}
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var emsGroupId = selections[0].data.emsGroupId;
		window.top.showConfirmDlg("@CMC.tip.tipMsg@", "@CMC.GP.confglobalgroup@", function(type) {
			if (type == 'no') {return;}
			window.top.showWaitingDlg("@CMC.tip.waiting@", "@CMC.GP.refreshGp@");
			$.ajax({
				url : '/ccmtsspectrumgp/addDeviceGroupFromGlobal.tv',
				type : 'POST',
				data : "entityId ="+ entityId +  "&emsGroupId=" + emsGroupId + "&groupId =" + groupId + "&num=" + Math.random(),
				dataType : "text",
				success : function(text) {
					if (text == 'success') {
						window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.issueGroupSuccess@");
						var a = window.parent.getWindow("ccmtsSpectrumGroupMgmt").body.dom.firstChild.contentWindow.store;
						a.reload();
						cancelClick();
					}else{
						window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.issueGroupFail@");
					}
				},
				error : function(text) {
					window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.issueGroupFail@");
				},
				cache : false
			});
		});
	} else {
		window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.GP.selGlobalGp@");
	}
}

function cancelClick() {
	window.parent.closeWindow('showGlobalGroupList');	
}

Ext.onReady(function() {
	SelectionModel = new Ext.grid.CheckboxSelectionModel({
		header : '<div class="x-grid3-hd-checker"></div>',
		singleSelect : true
	});
	var cm = [
		SelectionModel,
		{
			header : '@CMC.GP.groupName@',
			width : 200,
			sortable : true,
			dataIndex : 'emsGroupName'
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
			width : 150,
			sortable : true,
			dataIndex : 'groupPolicy',
			renderer : policyRender
		}];

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
		width: 770, 
		height: 380,
		border:true,
		animCollapse : false,
		trackMouseOver : trackMouseOver,
		columns : cm,
		sm : SelectionModel,
		bodyCssClass:'normalTable',
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
	            <a href="javascript:;" class="normalBtnBig" onclick="saveClick()"  id="saveBt">
	                <span><i class="miniIcoSaveOK"></i>@VLAN.determine@</span>
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