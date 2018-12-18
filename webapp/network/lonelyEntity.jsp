<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
//for tab changed start
var topoEdit =<%=uc.hasPower("topoEdit")%>;
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

var pageSize = <%= uc.getPageSize() %>;
var entityFocusedId = null;
var grid = null;
var store = null;
var pagingToolbar = null;

function buildPageBox(page) {
	return String.format(I18N.COMMON.displayPerPage, page);
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value; 
	if(p == pageSize) return;
	pageSize = parseInt(p);
	Ext.Ajax.request({url: '../system/setPageSize.tv',
		success: function() {
			pagingToolbar.pageSize = pageSize;
			pagingToolbar.doLoad(0);
		},
		failure: function(){},
		params: {pageSize: pageSize}
	});
}
function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({pageSize:pageSize, store:store, displayInfo:true,
		items:['-', buildPageBox(pageSize)]});
	return pagingToolbar;
}
function onEmptyClick() {
	window.top.showConfirmDlg(I18N.RECYLE.tip, I18N.RECYLE.confirmEmpty, function(type) {
		if (type == 'no') {return;}
		Ext.Ajax.request({url: '../entity/emptyRecyle.tv',
		   success: function() {onRefreshClick();},
		   failure: function(){window.top.showMessageDlg(I18N.RECYLE.error, I18N.RECYLE.emptyFailure, 'error')}
		});			
	});
}
function onSelectAllClick() {
  		var sm = grid.getSelectionModel();
	if (sm != null) {
		sm.selectAll();
	}	
}
function onTracertClick() {
}
function onPingClick() {
}
function onMoveToClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		window.top.createDialog('topoFolderTree', I18N.RECYLE.moveToTitle, 300, 330, 'network/popFolderTree.jsp', null, true, true,
			function(){
				var callbackObj = window.top.ZetaCallback;
				if (callbackObj == null || callbackObj.type != 'ok') {return;}
				var selectedItemId = callbackObj.selectedItemId;
				if (selectedItemId == null) {return;}
				var selections = sm.getSelections();
				var entityIds = [];
				for (var i = 0; i < selections.length; i++) {
					entityIds[i] = selections[i].data.entityId;
				}
				Ext.Ajax.request({url: '../entity/moveEntityFromRecyle.tv',
				   success: function() {onRefreshClick();},
				   failure: function(){window.top.showMessageDlg(I18N.RECYLE.error, I18N.RECYLE.moveEntityFailure, 'error')},
				   params: {destFolderId:selectedItemId, entityIds:entityIds}
				})
				window.top.ZetaCallback = null;
		});
	}	
}
function onDeleteClick() {
	var sm = grid.getSelectionModel();
	if(sm.getSelections().length==0){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.lonelyEntity.note1);
		return;
	}
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var entityIds = [];
		for (var i = 0; i < selections.length; i++) {
			entityIds[i] = selections[i].data.entityId;
		}
		window.top.showConfirmDlg(I18N.RECYLE.tip, I18N.RECYLE.confirmDeleteRow, function(type) {
			if (type == 'no') {return;}
			Ext.Ajax.request({url: '../entity/deleteEntityFromRecyle.tv',
			   success: function() {onRefreshClick();},
			   failure: function(){window.top.showMessageDlg(I18N.RECYLE.error, I18N.RECYLE.deleteFailure, 'error')},
			   params: {entityIds:entityIds}
			});			
		});
	}
}
function doRefresh() {
	onRefreshClick();
}
function onRefreshClick() {
	store.reload();
}
function onPropertyClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var r = sm.getSelected();
		window.parent.showProperty('entity/showEntityPropertyJsp.tv?entityId=' + r.data.entityId + '&sourceType=lonelyEntity');
	}	
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
    var xg = Ext.grid;
	store = new Ext.data.JsonStore({
	    url: '../entity/loadEntityInLonely.tv',
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['entityId', 'name', 'ip', 'typeName', 'modifyTime']
	});
    store.setDefaultSort('name', 'asc');

    var cm = new Ext.grid.ColumnModel([
    	{header: I18N.RECYLE.name, dataIndex: 'name', width: 150, sortable: true},
    	{header: I18N.RECYLE.ip, dataIndex: 'ip', width: 150, sortable: true},
    	{header: I18N.RECYLE.type, dataIndex: 'typeName', width: 150, sortable: true}
    ]);
    cm.defaultSortable = false;
    var tbar=[
      	{text:I18N.RECYLE.deleteAction, iconCls:'bmenu_delete', handler:onDeleteClick},
      	{text:I18N.RECYLE.empty, iconCls:'bmenu_recyle', handler:onEmptyClick}, '-',
    	{text:I18N.RECYLE.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}]
    else{
      var tbar=[
		    {text:I18N.RECYLE.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}]
    }
	grid = new xg.GridPanel({border:false, region:'center',
        store:store, cm:cm, trackMouseOver:false,
        viewConfig: {forceFit:true, enableRowBody:true, showPreview:false},
        tbar:tbar,        
        bbar:buildPagingToolBar(),
		renderTo: document.body
    });    

	var deleteMenu = new Ext.menu.Item({text:I18N.RECYLE.deleteAction, iconCls:'bmenu_delete', handler:onDeleteClick});
	var moveToMenu = new Ext.menu.Item({text:I18N.RECYLE.moveTo, handler: onMoveToClick});
	var propertyMenu = new Ext.menu.Item({text:I18N.RECYLE.property, handler:onPropertyClick});
    var mainMenu = new Ext.menu.Menu({id: 'mainMenu', minWidth: 140, enableScrolling: false, items:[
		{text:I18N.RECYLE.refresh, iconCls:'bmenu_refresh', handler:onRefreshClick}, '-'
	]});
	if(topoEdit){
       mainMenu.addItem(moveToMenu); 
       mainMenu.addItem('-'); 
    }
    mainMenu.addItem(deleteMenu);
    mainMenu.addItem({text:I18N.RECYLE.emptyRecyle, iconCls:'bmenu_recyle', handler:onEmptyClick});
    mainMenu.addItem('-'); 
    mainMenu.addItem(propertyMenu);
    
    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
   		var sm = grid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
		deleteMenu.enable();
		moveToMenu.enable();
		propertyMenu.enable();
    	mainMenu.showAt(e.getPoint());
    });  
    
    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var r = grid.getStore().getAt(rowIndex);
        window.top.showProperty('entity/showEntityPropertyJsp.tv?entityId=' + r.data.entityId);	
    });    
    
    new Ext.Viewport({layout: 'fit', items:[grid]});
    store.load({params:{start:0, limit:pageSize}});
    tabShown();
});	
</script>
</head>
<body class=CONTENT_WND>
</body>
</html>
