<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/cssStyle.inc" %>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>	
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>  
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
// for tab changed start
function tabActivate() {
	window.top.setStatusBarInfo('', '');
	onRefreshClick();
}
function tabDeactivate() {
}
function tabRemoved() {
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
}
// for tab changed end

var storeUrl = "../fault/loadActionList.tv";
var quickTip = null;
var store = null;
var grid = null;
var refreshInterval = null;
var mainMenu = null;
function onNewActionClick() {
	window.parent.createDialog("modalDlg", I18N.ALERT.newAction, 800, 500, "fault/newAction.jsp", null, true, true);
}
function onSelectAllClick() {
	selectAllRow();
}
function onDeleteClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var actionIds = [];
		for (var i = 0; i < selections.length; i++) {
			actionIds[i] = selections[i].data.actionId;
		}
		window.top.showConfirmDlg(I18N.COMMON.tip, I18N.ALERT.confirmDeleteAction, function (type) {
			if (type == "no") {
				return;
			}
			Ext.Ajax.request({url:"../fault/deleteAction.tv", success:function () {
				top.afterSaveOrDelete({
		            title: '@COMMON.tip@',
		            html: '<b class="orangeTxt">@resources/COMMON.deleteSuccess@</b>'
		        });
				onRefreshClick();
				top.afterSaveOrDelete({
	   				title: I18N.COMMON.tip,
	   				html: '<b class="orangeTxt">' + I18N.COMMON.deleteSuccess + '</b>'
	   			});
			}, failure:function () {
				window.parent.showErrorDlg();
			}, params:{actionIds:actionIds}});
		});
	} else {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.chooseAction);
	}
}
function onEnableClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var actionIds = [];
		for (var i = 0; i < selections.length; i++) {
            if(selections[i].data.status == 1){
                window.parent.showMessageDlg(I18N.COMMON.tip, selections[i].data.name + " "+I18N.SYSTEM.alreadyStart);
                return;
            }
			actionIds[actionIds.length] = selections[i].data.actionId;
		}
		window.top.showConfirmDlg(I18N.COMMON.tip, I18N.ALERT.confirmStartAction, function (type) {
			if (type == "no") {
				return;
			}
			Ext.Ajax.request({url:"../fault/enableAction.tv", success:function () {
				for (var j = 0; j < actionIds.length; j++) {
					store.getById(actionIds[j]).data.status = true;
				}
				grid.getView().refresh();
			}, failure:function () {
				window.parent.showErrorDlg();
			}, params:{actionIds:actionIds}});
		});
	} else {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.chooseAction);
	}
}
function onDisableClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var actionIds = [];
		for (var i = 0; i < selections.length; i++) {
            if(selections[i].data.status == 0){
                window.parent.showMessageDlg(I18N.COMMON.tip, selections[i].data.name + " " +I18N.SYSTEM.alreadyStop);
                return;
            }
			actionIds[actionIds.length] = selections[i].data.actionId;
		}
		window.top.showConfirmDlg(I18N.COMMON.tip, I18N.ALERT.confirmStopAction, function (type) {
			if (type == "no") {
				return;
			}
			Ext.Ajax.request({url:"../fault/disableAction.tv", success:function () {
				for (var j = 0; j < actionIds.length; j++) {
					store.getById(actionIds[j]).data.status = false;
				}
				grid.getView().refresh();
			}, failure:function () {
				window.parent.showErrorDlg();
			}, params:{actionIds:actionIds}});
		});
	} else {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.chooseAction);
	}
}
function refreshActionList() {
	onRefreshClick();
}
function onRefreshClick() {
	store.reload();
}
function onPropertyClick() {
	mainMenu.hide();
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		onShowActionClick('fault/show' + record.data.type + 'Action.tv?actionId=' + record.data.actionId);
	}
}
function selectAllRow() {
	var sm = grid.getSelectionModel();
	if (sm != null) {
		sm.selectAll();
	}
}
function renderName(value, p, record) {
	return String.format("<img src=\"../images/fault/{1}\" border=0 align=absmiddle class='pL5'>&nbsp;<a href=\"#\" onclick=\"onShowActionClick('fault/show{3}Action.tv?actionId={2}');\">{0}</a>", 
	value, record.data.typeIcon, record.data.actionId, record.data.type);
}
function renderStatus(value, p, record) {
	if (record.data.status) {
		return String.format("<img class='nm3kTip' nm3kTip=\"{0}\" src=\"../images/fault/enable.gif\" border=0 align=absmiddle>", I18N.COMMON.start);
	} else {
		return String.format("<img class='nm3kTip' nm3kTip=\"{0}\" src=\"../images/fault/disable.gif\" border=0 align=absmiddle>", I18N.COMMON.stop);
	}
}
function onShowActionClick(url) {
    window.parent.createDialog("modalDlg", I18N.FAULT.modifyAction, 800, 500, url, null, true, true);
}
//点击其它;
function showMoreOperation(id,event){
	event.getXY = function(){return [event.clientX,event.clientY];};
	var sm = grid.getSelectionModel();	
	/* if (sm != null && !sm.isSelected(rowIndex)) {
		sm.selectRow(rowIndex);
	} */
	mainMenu.showAt(event.getXY());
}
function redererOpation(value, p, record){
	return String.format("<a href=\"javascript:;\" onclick=\"onShowActionClick('fault/show{3}Action.tv?actionId={2}');\">"+ I18N.COMMON.modify +"</a> / <a href='javascript:;' onclick='onDeleteClick()'>"
	+I18N.COMMON.remove +'</a> / <a href="javascript:;" class="withSub" onclick="showMoreOperation({2},event)">'+ I18N.COMMON.others +'</a>',
	value, record.data.typeIcon, record.data.actionId, record.data.type);
}
Ext.BLANK_IMAGE_URL = "../images/s.gif";
Ext.onReady(function () {
	var columnModels = [
      {header: '<div class="txtCenter">'+I18N.COMMON.nameHeader+'</div>', width:100, sortable:false,align:"left", groupable:false, dataIndex:"name", renderer:renderName},
      {header: I18N.COMMON.type, width:100, sortable:false,dataIndex:"typeName"}, 
      {header: I18N.SYSTEM.status, width:80, sortable:false, fixed:true, align:"center", dataIndex:"status", renderer:renderStatus}, 
	  {header: '<div class="txtCenter">'+I18N.FAULT.paramHeader+'</div>', width:260, sortable:false,align:"left", groupable:false, dataIndex:"params"},
	  {header: I18N.COMMON.opration,width:80,groupable:false,dataIndex:"opration",renderer:redererOpation}];
	var myToolbar = [{text: I18N.COMMON.create, iconCls: "bmenu_new",cls:"mL10", handler: onNewActionClick}, "-",
		{text: I18N.COMMON.remove, iconCls:"bmenu_delete", handler: onDeleteClick}, "-",
		{text: I18N.COMMON.start, iconCls:"bmenu_play", handler: onEnableClick},
		{text: I18N.COMMON.stop, iconCls:"bmenu_stop", handler: onDisableClick}, "-",
		{text: I18N.COMMON.refreshTitle, iconCls:"bmenu_refresh", handler:onRefreshClick}];

	var deleteMenu = new Ext.menu.Item({text: I18N.COMMON.remove, iconCls:"bmenu_delete", handler:onDeleteClick});
	var propertyMenu = new Ext.menu.Item({text: I18N.COMMON.outline, handler:onPropertyClick});
	mainMenu = new Ext.menu.Menu({id:"mainMenu", minWidth: 130, items:[ 
	 	//deleteMenu, "-",
		{text: I18N.COMMON.start, handler: onEnableClick}, 
		{text: I18N.COMMON.stop, handler: onDisableClick}
		/* , "-",  
		{text: I18N.COMMON.refreshTitle, iconCls: "bmenu_refresh", handler: onRefreshClick} */
		]
	});
	
	var reader = new Ext.data.JsonReader(
			{root:"data",
		     idProperty: 'actionId',
			 fields:[
					 {name:"actionId"}, 
					 {name:"actionTypeId"},
					{name:"typeIcon"}, 
					{name:"type"},
					{name:"name"},
					{name:"typeName"}, 
					{name:"status"},
					{name:"params"},
					{name:"opration"}]});
	store = new Ext.data.GroupingStore({
		url:storeUrl, 
		reader:reader, 
		remoteGroup:false, 
		remoteSort:false, 
		sortInfo:{field:"name", direction:"ASC"},
		 groupField:"typeName",
		  groupOnSort:false});
	var cm = new Ext.grid.ColumnModel(columnModels);
	var groupTpl = "{text} ({[values.rs.length]})" + I18N.COMMON.items;
	grid = new Ext.grid.GridPanel({
		store:store, region:"center",bodyCssClass:"normalTable", 
		animCollapse:false, trackMouseOver:false, border:false, cm:cm, 
		view:new Ext.grid.GroupingView({forceFit:true, hideGroupedColumn:true, enableNoGroups:true, groupTextTpl:groupTpl}), 
		tbar: myToolbar, 
		loadMask : true,
		renderTo:document.body
	});
	/* grid.on("rowcontextmenu", function (grid, rowIndex, e) {
		e.preventDefault();
		var sm = grid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
		mainMenu.showAt(e.getPoint());
	}); */
	new Ext.Viewport({layout:"fit", items:[grid]});
	store.load();
	tabShown();
});
</script>
</head><body>
	<div id="tree-ct" style=""></div>
</body></html>