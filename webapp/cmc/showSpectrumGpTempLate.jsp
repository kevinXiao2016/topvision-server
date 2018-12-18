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
.cover {
	height: 100%;
	left: 0;
	position: absolute;
	overflow: auto;
	top: 0;
	width: 100%;
	background: #FFFFFF;
}
#frameOuter {
	width: 100%;
	height: 100%;
	overflow: hidden;
}
</style>
<script type="text/javascript">
var pageSize = <%=uc.getPageSize()%>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var grid = null;
var store = null;

Ext.onReady(function() {
	store = new Ext.data.JsonStore({
		url : '/ccmtsspectrumgp/loadSpectrumGpTempLateList.tv?r=' + Math.random(),
		root : 'data',
		totalProperty: 'rowCount',
		remoteSort : false,
		fields : [ {
			name : 'tempLateId'
		}, {
			name : 'templateName'
		}, {
			name : 'globalAdminStatus'
		}, {
			name : 'snrQueryPeriod'
		}, {
			name : 'hopHisMaxCount'
		},{
			name : 'chnlSecondaryProf'
		},{
			name : 'chnlTertiaryProf'
		},{
			name : 'gpForUpChannel1'
		},{
			name : 'gpForUpChannel2'
		},{
			name : 'gpForUpChannel3'
		},{
			name : 'gpForUpChannel4'
		}]
	});
	
	var cm = [
	  		{
	  			header : '@CMC.GP.templateName@',
	  			width : 240,
	  			sortable : false,
	  			dataIndex : 'templateName'
	  		},
	  		{
	  			header : '@CMC.GP.globalStatus@',
	  			width : 150,
	  			sortable : true,
	  			dataIndex : 'globalAdminStatus',
	  			renderer : statusRender
	  		},
	  		{
	  			header : '@CMC.GP.queryPeriod@',
	  			width : 120,
	  			sortable : true,
	  			dataIndex : 'snrQueryPeriod'
	  		},
	  		{
	  			header : '@CMC.GP.maxCount@',
	  			width : 120,
	  			sortable : true,
	  			dataIndex : 'hopHisMaxCount'
	  		},
	  		{
	  			header : '@CMC.CHANNEL.secondProf@',
	  			width : 160,
	  			sortable : true,
	  			dataIndex : 'chnlSecondaryProf',
	  			renderer : profRender
	  		},
	  		{
	  			header : '@CMC.CHANNEL.thirdProf@',
	  			width : 160,
	  			sortable : true,
	  			dataIndex : 'chnlTertiaryProf',
	  			renderer : profRender
	  		},
	  		{
	  			header : '@COMMON.manu@',
	  			width : 150,
	  			sortable : false,
	  			dataIndex : 'op',
	  			renderer : opeartionRender
	  		} ];
	
	var toolbar = [
		{
			text : '@CMC.GP.addTemplate@',
			cls:'mL10',
			iconCls : 'bmenu_new',
			handler : addGpTemplate
		},
		'-',
		{
			text : '@CMC.GP.configToDevice@',
			iconCls : 'bmenu_equipment',
			disabled : !operationDevicePower,
			handler : configToDevice
		}];
	
	grid = new Ext.grid.GridPanel(
		{
		store : store,		
		region: 'center',
		width: $(document.body).width(), 
		height: $(window).height(),
		border:false,
		bodyCssClass:'normalTable',
		animCollapse : false,
		trackMouseOver : trackMouseOver,
		viewConfig:{
			forceFit:true
		},
		columns : cm,
		tbar : toolbar,
		bbar : buildPagingToolBar(),
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		})
		//renderTo : 'templateList'
	});
	new Ext.Viewport({
	    layout: 'border',
	    items: [grid]
	});

	store.load({params : {start : 0,limit : pageSize}});
	
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

// Config Spectrum Group Template to Device
function configToDevice(){
	var selectFlag = grid.getSelectionModel().hasSelection();
	var selected = grid.getSelectionModel().getSelected();
	if(selectFlag){
		var tempLateId = selected.data.tempLateId;
		window.top.createDialog('showTemplateConfig', "@CMC.GP.templateConfig@", 712, 450,
				"/ccmtsspectrumgp/showTempLateConfig.tv?tempLateId=" + tempLateId, null, true, true);
	}else{
		window.parent.showMessageDlg("@COMMON.tip@","@CMC.GP.chooseTemplate@");
		return;
	}
}

//Operation Modify&Delete
function opeartionRender(value, cellmate, record){
	var tempLateId = record.data.tempLateId;
	return String.format(
			"<a src='javascript:;' " + 
			"onclick='modifyGpTemplate({0})'>@COMMON.modify@</a> / " + 
			"<a href='javascript:;'" + 
			"onclick='deleteGpTemplate({0})'>@COMMON.del@</a>", tempLateId);
}

//Add Spectrum Group Template
function addGpTemplate(){
	/* window.top.createDialog('showNewGpTemplate', "@CMC.GP.addTemplate@", 700, 520,
			"/ccmtsspectrumgp/showNewGpTempLate.tv?groupModifyFlag=0", null, true, true); */
	$("#templateList").hide();
	$("#frameOuter").show();
	//var m = Math.random();
	$("#frameInner").attr("src","/ccmtsspectrumgp/showNewGpTempLate.tv?groupModifyFlag=0");
	
}

//Modidy Spectrum Group Template
function modifyGpTemplate(tempLateId){
	/* window.top.createDialog('showNewGpTemplate', "@CMC.GP.updateTemplate@", 700, 520,
			'/ccmtsspectrumgp/showNewGpTempLate.tv?groupModifyFlag=1' + "&tempLateId=" + tempLateId, null, true, true); */
	$("#templateList").hide();
	$("#frameOuter").show();
	//var m = Math.random();
	$("#frameInner").attr("src","/ccmtsspectrumgp/showNewGpTempLate.tv?groupModifyFlag=1" + "&tempLateId=" + tempLateId);
}

/**
 * 返回frame的父页面
 */
function closeFrame(){
	//刷新grid
	//store.reload();
	$("#templateList").css("display","block");
	$("#frameOuter").fadeOut('slow');
	$("#frameInner").attr("src","");
}

//Delete Spectrum Group Template
function deleteGpTemplate(tempLateId){
	window.top.showConfirmDlg('@COMMON.tip@', "@CMC.GP.delTempConfirm@", function(type) {
		if (type == 'no') {
			return;
		}else{
			window.top.showWaitingDlg("@COMMON.wait@", "@CMC.GP.onDeleting@");
			$.ajax({
				url : '/ccmtsspectrumgp/deleteSpectrumGpTempLate.tv',
				type : 'POST',
				data : "tempLateId=" + tempLateId + "&num=" + Math.random(),
				success : function() {
						window.parent.showMessageDlg("@COMMON.tip@","@CMC.GP.deleteSuccess@");
						store.reload();
				},
				error : function() {
					window.parent.showMessageDlg("@COMMON.tip@","@CMC.GP.deleteFaild@");
				},
				cache : false
			});
		}
	});
}

//globalAdminStatus
function statusRender(value, cellmate, record){
	var imgStr;
	if(value ==1){
		imgStr = "<img src = '/images/speOn.png'/>";
	}else{
		imgStr = "<img src = '/images/speOff.png'/>";
	}
	return imgStr;
}

//chnlTertiaryProf&chnlSecondaryProf
function profRender(value, cellmate, record){
	switch(value){
		case 1: return "QPSK-Fair-Scdma";
		case 2: return "QAM16-Fair-Scdma";
		case 3: return "QAM64-Fair-Scdma";
		case 4: return "QAM256-Fair-Scdma";
		case 5: return "QPSK-Good-Scdma";
		case 6: return "QAM16-Good-Scdma";
		case 7: return "QAM64-Good-Scdma";
		case 8: return "QAM256-Good-Scdma";
		case 9: return "QAM64-Best-Scdma";
		case 10: return "QAM256-Best-Scdma";
		case 11: return "QPSK-Good-Atdma";
		case 12: return "QAM16-Good-Atdma";
		case 13: return "QAM64-Good-Atdma";
		case 14: return "QAM256-Good-Atdma";
		case 65535: return "@CMC.CHANNEL.delChnlProf@";
		default: return "-";
	}
}
</script>
</head>
<body class="whiteToBlack">
	<div id="templateList"></div>
	<div id="frameOuter" class="cover" style="display:none; z-index:3;">
		<iframe id="frameInner" name="frameInner" src="" style="width:100%; height:100%;" frameborder="0"></iframe>
	</div>
</body>
</Zeta:HTML>