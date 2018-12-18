<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<html>
<head>
<title>Entity Snap</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cm.resources" var="cm"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style>
.bmenu_manage {
	background : url("/images/fault/action.gif") no-repeat;
}
.bmenu_remanage {
	background : url("/images/add.png") no-repeat;
}
.bmenu_cancelmanage {
	background : url("/images/minus.png") no-repeat;
}
</style>
<script type="text/javascript">
/* $(function(){
	//alert("test");
	$.ajax({
        type: "GET",
        url: "/cmpoll/loadDeviceList.tv",//String.format("/{0}/js/extendMenu.js",record.data.module),
        //dataType: "script",
        async : false,
        success  : function (json) {
			alert(json+"xxx");	
        }
    });
}); */
var baseGrid = null;
function loadPage(){
	//var w = document.body.clientWidth;// - 30;
	var h = document.body.clientHeight;// - 100;
	//w = w*.5;
	var sm = new Ext.grid.CheckboxSelectionModel({
		handleMouseDown: Ext.emptyFn,
		singleSelect:true	
	}); 
	var baseColumns = [	
	           	    sm,
	           		{header: I18N.Config.oltConfigFileImported.deviceName, sortable: true, align: 'center', dataIndex: 'deviceName'},
                	{header: I18N.MAIN.templateRootNode,  sortable: false, align: 'center', dataIndex: 'deviceTypeToString'}
                  	];
	var baseStore = new Ext.data.JsonStore({
	    url: ('/cmpoll/loadDeviceList.tv'),
	    root: 'data',
	    //remoteSort: true,//是否支持后台排序 
	    fields: ['deviceObjId','deviceName','deviceType','deviceTypeToString']
	});
	//baseStore.setDefaultSort('objectId', 'ASC');
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		id: 'deviceObjectList', 
		//width: w, 
		height: h,
		//animCollapse: animCollapse, 
		//trackMouseOver: trackMouseOver, 
		//border: true, 
		store: baseStore, 
		sm:sm,
		cm: baseCm,
		renderTo: 'deviceObjectList_div'});
	baseGrid.on('rowdblclick', function(baseGrid, rowIndex, e) {
	});
	baseStore.load();
}
function okClick() {
	//var grid = Ext.getCmp("deviceObjectList");
	var sm = baseGrid.getSelectionModel();
	//var record = sm.getSelected();
	//var s = tree;
	window.top.ZetaCallback = {type:'ok', selectedItem:sm}; 
	
	window.top.closeWindow('deviceListTree');
}
function cancelClick() {
	window.top.ZetaCallback = {type:'cancel'};
	window.top.closeWindow('deviceListTree');
}
Ext.onReady(function(){
	loadPage();
});
</script>
</head>
<BODY class=POPUP_WND style="margin: 10px">
	<table cellspacing=0 cellpadding=0 width=100% height=100%>
		<tr>
			<td height=20><label><fmt:message key="cmPoll.pleaseChooseDevice" bundle="${resources}"/></label></td>
		</tr>
		<tr>
			<td height=4></td>
		</tr>
		<tr>
			<td height=100% class=TREE-CONTAINER><div id="deviceObjectList_div"
					style="height: 100%; overflow: auto;"></div>
			</td>
		</tr>
		<tr>
			<td height=4>&nbsp;</td>
		</tr>
		<tr>
			<td valign=top align=right>
				<button id=okBt class=BUTTON75
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onMouseOut="this.className='BUTTON75'" onclick="okClick()"><fmt:message key="COMMON.ok" bundle="${resources}"/></button>&nbsp;
				<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resources}"/></button>
			</td>
		</tr>
		<tr>
			<td height=4></td>
		</tr>
	</table>
</BODY>
</html>
