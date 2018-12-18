<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html><head>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/data-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/data-json.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-buttons.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-toolbars.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-forms.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-menu.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-grid-foundation.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
function tabActivate() {
	window.top.setStatusBarInfo('', '');
}

function description(value, cellmate, record){
	return getI18NSystemLogString(value);
}

var cm = new Ext.grid.ColumnModel([
   	{header:'<div style="text-align:center">'+ I18N.COMMON.description + '</div>',align:'left', dataIndex: 'description', width: 250, sortable: true,renderer:description},
   	{header:'<div style="text-align:center">'+ I18N.COMMON.operator + '</div>', dataIndex: 'userName', width: 100, sortable: true},
   	{header:'<div style="text-align:center">'+ I18N.COMMON.ip + '</div>', dataIndex: 'ip', width: 120, sortable: true},
	{header:'<div style="text-align:center">'+ I18N.COMMON.operationTime + '</div>', dataIndex: 'createTimeString', width: 150, sortable: true}
]);

var store = null;
var grid = null;
function onSelectAllClick() {
	var sm = grid.getSelectionModel();
	if (sm != null) {
		sm.selectAll();
	}	
}
function onRefreshClick() {
	store.reload();
}

function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize, store:store, 
		displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize)]});
	return pagingToolbar;
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
	store = new Ext.data.JsonStore({
	    url: 'loadLogList.tv',
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    sortInfo: {field: 'createTimeString', direction: "desc"},
	    fields: ['logId', 'description', 'userName', 'ip', 'createTimeString']
	});
   	cm.defaultSortable = true;	
	grid = new Ext.grid.GridPanel({border:false, region:'center',bodyCssClass:"normalTable",
        store: store, cm: cm, trackMouseOver: trackMouseOver, stripeRows:true,
        viewConfig: {forceFit:true, showPreview: false},
        tbar: [{text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh',cls:'mL10', handler: onRefreshClick}],
		bbar: buildPagingToolBar(),
		loadMask : true, 
		renderTo: document.body
    });
    
    new Ext.Viewport({layout:'fit', items:[grid]});
    store.load({params: {start: 0, limit: pageSize}});
});
</script>
</head><body class=CONTENT_WND>
</body></html>
