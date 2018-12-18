<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head><title>History Alert Viewer</title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<!--[if !IE]><!-->
<link rel="stylesheet" type="text/css" href="../css/ext-patch.css"/>
<!--<![endif]-->
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>

<script type="text/javascript" src="../js/ext/ext-base-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-all-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>-2.1.js"></script>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var store = null;
var grid = null;
function onPrintClick() {
	window.print();
}
function onRefreshClick() {
	store.reload();
}
function onMaxViewClick() {
	window.parent.onMaxViewClick();
}
function renderNote(value, p, record) {
	return String.format("<img hspace=5 src=\"../images/fault/alertLeaf.gif\" border=0 align=absmiddle>{0}", value);
}
Ext.onReady(function () {
	Ext.BLANK_IMAGE_URL = "../images/s.gif";
	Ext.QuickTips.init();
	
	var myToolbar = [
		{text: I18N.COMMON.print, iconCls:'bmenu_print', handler: onPrintClick}, '-',
		{text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}
	];

	store = new Ext.data.JsonStore({
	    url: '../fault/statHistoryAlertByCategory.tv',
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['typeId', 'name', 'note', 'count']
	});

	var columnModels = [
		{header: I18N.COMMON.name, width: 200, sortable: false, dataIndex: 'name', renderer: renderNote},
		{header: I18N.ALERT.count, width: 100, sortable: false, align: 'right', dataIndex: 'count'},
		{header: I18N.COMMON.description, width: 250, sortable: false, dataIndex: 'note'}
	];
	var cm = new Ext.grid.ColumnModel(columnModels);

	grid = new Ext.grid.GridPanel({store: store, animCollapse: animCollapse,
		trackMouseOver: trackMouseOver, border: false, cm: cm,
		sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	 	tbar: myToolbar, renderTo: document.body});
	 	
	grid.on('rowdblclick', function(grid, rowIndex, e) {
		var r = grid.getStore().getAt(rowIndex);  // Get the Record
		window.parent.setActiveTitle('alertview', r.data.name);
	    var f = window.parent.getFrame('alertview');
	    f.location.href = "alertHistoryList.jsp?typeId=" + r.data.typeId;
	});

	new Ext.Viewport({layout:"fit", items:[grid]});
	store.load();
});
</script>
</head><body class=CONTENT_WND>
</body></html>
