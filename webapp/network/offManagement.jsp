<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css" />
<style type="text/css">
.bmenu_management {
	background-image: url(../images/network/management.png) !important;
}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
//for tab changed start
function tabActivate() {
	window.top.setStatusBarInfo('', '');
	doRefresh();
}
function tabDeactivate() {
}
function tabRemoved() {
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
}
// for tab changed end

var entityFocusedId = null;
var grid = null;
var store = null;

function onManagementClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var entityIds = [];
		for (var i = 0; i < selections.length; i++) {
			entityIds[i] = selections[i].data.entityId;
		}
		window.top.showConfirmDlg(I18N.MENU.tip, I18N.offManagement.note1, function(type) {
			if (type == 'no') {return;}
			Ext.Ajax.request({url: '../entity/enableManagement.tv',
			   params: {entityIds: entityIds},
			   success: function() {onRefreshClick();},
			   failure: function(){window.parent.showErrorDlg();}
			});			
		});
	} else {
		window.parent.showMessageDlg(I18N.MENU.tip, I18N.offManagement.selectDevice);
	}
}
function onSelectAllClick() {
  		var sm = grid.getSelectionModel();
	if (sm != null) {
		sm.selectAll();
	}	
}
function doRefresh() {
	store.reload();
}
function onRefreshClick() {
	store.reload();
}
function onPropertyClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var r = sm.getSelected();
		window.top.showProperty('entity/showEntityPropertyJsp.tv?entityId=' + r.data.entityId + '&sourceType=offManagement');
	}	
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
    var xg = Ext.grid;
	store = new Ext.data.JsonStore({
	    url: '../entity/loadEntityCanceled.tv',
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['entityId', 'name', 'ip', 'typeName', 'modifyTime']
	});
    store.setDefaultSort('modifyTime', 'desc');

    var cm = new Ext.grid.ColumnModel([
    	{header: I18N.NETWORK.host, dataIndex: 'name', width: 150,sortable: true},
    	{header: I18N.offManagement.Deviceaddress, dataIndex: 'ip', width: 150,sortable: true},
    	{header: I18N.NETWORK.typeHeader, dataIndex: 'typeName', width: 150,sortable: true},
    	{header: I18N.offManagement.DetachedDate, dataIndex:'modifyTime', width: 150,sortable: true}]);
    cm.defaultSortable = false;
    
    var tbar=[
   		{text: I18N.offManagement.Managementdevice, iconCls: 'bmenu_management', handler: onManagementClick}, '-',
   		{text: I18N.MENU.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}]

	grid = new xg.GridPanel({border:false, region:'center',
        store:store, cm:cm, trackMouseOver:false,
        viewConfig: {forceFit:true, enableRowBody:true, showPreview:false},
        tbar:tbar,
		renderTo: document.body
    });    

	var propertyMenu = new Ext.menu.Item({text: I18N.MENU.property, handler:onPropertyClick});
	var mainMenu = new Ext.menu.Menu({id: 'mainMenu', minWidth: 130, items:[
		{text: I18N.MENU.refresh, iconCls:'bmenu_refresh', handler:onRefreshClick}
	]});
	mainMenu.addItem({text: I18N.offManagement.Managementdevice, handler: onManagementClick});
	mainMenu.addItem('-');
	mainMenu.addItem(propertyMenu);

    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
   		var sm = grid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
		propertyMenu.enable();
    	mainMenu.showAt(e.getPoint());
    });
    
    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var r = grid.getStore().getAt(rowIndex);
        window.top.showProperty('entity/showEntityPropertyJsp.tv?entityId=' + r.data.entityId);	
    });    
    
    new Ext.Viewport({layout: 'fit', items:[grid]});
    store.load();
});
</script>
</head>
<body class=CONTENT_WND>
</body>
</html>