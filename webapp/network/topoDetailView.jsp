<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<title><s:property value="topoFolder.name" />
</title>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module workbench
    import js.customColumnModel
    import js.tools.ipText
    import network.topoDetailView
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.cmc.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style>
.enableIconClass {background: url( /images/checked.gif ) no-repeat;}
.disableIconClass {background: url( /images/unchecked.gif ) no-repeat;}
.x-toolbar .borderLine .x-btn-tl { background-position: -6px 0;}
.x-toolbar .borderLine .x-btn-tc { background-position: 0 -9px;}
.x-toolbar .borderLine .x-btn-tr { background-position: -9px 0;}
.x-toolbar .borderLine .x-btn-ml { background-position: -6px -24px;}
.x-toolbar .borderLine .x-btn-mc { background-position: 0 -2168px;}
.x-toolbar .borderLine .x-btn-mr { background-position: -9px -24px;}
.x-toolbar .borderLine .x-btn-bl { background-position: -6px -3px;}
.x-toolbar .borderLine .x-btn-bc { background-position: 0 -18px;}
.x-toolbar .borderLine .x-btn-br { background-position: -9px -3px;}
</style>
<script type="text/javascript">
// for tab changed start
function tabActivate() {
	showEntityCount();
	onRefreshClick();
}
function tabDeactivate() {
	//doOnunload();
}
function tabRemoved() {
}
function tabShown() {
	showEntityCount();
}
// for tab changed end
var saveColumnId = 'topoDetailView';//保存表头数据的id;
//是否显示下级设备;
var DISPLAY_SUBNET = ${displaySubnet};
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var googleSupported = <%= SystemConstants.getInstance().getBooleanParam("googleSupported", false) %>;
var cmcPerfPower = <%=uc.hasPower("cmcPerfParamConfig")%>;
var editTopoPower = <%= uc.hasPower("topoEdit") %>;
var topoFolderId = '<s:property value="topoFolder.folderId"/>';
var topoFolderName = '<s:property value="topoFolder.name"/>';
var entityId = '${entityId}';
var columnModels = [
	{id:'name', header:'<div class="txtCenter">' + I18N.NETWORK.nameHeader + '</div>', width:150, sortable:true, groupable:false, dataIndex:'name', renderer: renderName, align:'left'},
    {header: I18N.NETWORK.ipHeader, width:100, sortable:true, groupable:false, dataIndex: 'ip'},
    {header: I18N.NETWORK.typeHeader, width:100, sortable:true, dataIndex:'typeName',menuDisabled:true, hideable :false},
    {header: I18N.NETWORK.onlineStatus, width:60, sortable:true, dataIndex: 'state', align:'center', renderer: renderOnlie},   
	{header: I18N.NETWORK.manageStatus, width:60, sortable:true, dataIndex: 'status', align:'center', renderer: renderManagement},    
	//{header: I18N.NETWORK.alarmLevelHeader, width:100, sortable:true, dataIndex: 'alert', align:'center', renderer: renderLevel},
	//{header: I18N.NETWORK.snmpHeader, width:100, sortable:true, dataIndex: 'snmpSupport', align:'center', renderer: renderSnmp},
    {header:I18N.COMMON.snapTimeHeader, width:120, sortable:true, groupable:false, dataIndex:'snapTime'},	
    {header:I18N.COMMON.opration,renderer:rendererMenu, width:200,fixed:true}
];
var storeUrl = 'getEntityByDetail.tv?folderId=${topoFolder.folderId}';

var dispatcherInterval = <s:property value="topoFolder.refreshInterval"/>;
var dispatcherTimer = null;
function getPropertyUrl() {
	return "network/allEntityProperty.jsp";
}

function showRealTimeInfo(cmcId,name){
    window.parent.addView('entity-realTime' + cmcId, '@COMMON.realtimeInfo@['+name+']', 'entityTabIcon', '/cmc/showCmcRealTimeData.tv?cmcId=' + cmcId);
}

function showOltRealTimeInfo(entityId, name, entityIp){
	window.parent.addView('oltRealTimeInfo' + entityId, '@COMMON.realtimeInfo@['+name+']' , 'entityTabIcon', '/epon/oltRealtime/showOltRealTime.tv?entityId='+ entityId + "&entityName=" + name + "&entityIp=" + entityIp);
}

function rendererMenu(value, p, record){
	var type = record.data.type;
	var oltType = EntityType.getOltType();
	var cmtsType = EntityType.getCmtsType();
	var ccmtsType = EntityType.getCcmtsType();
	var html = "";
	switch(type){
		case oltType:
			html = "<a href='javascript:;' onClick='showOltRealTimeInfo(" + record.data.entityId+",\""+ record.data.name+ "\""+",\""+ record.data.ip+ "\")'>@COMMON.realtimeInfo@</a> / ";
			return String.format(html + "<a class='withSub' href='javascript:;' onClick='showMoreOperation({0},event)'>" +I18N.COMMON.others + "</a>", record.id);
		case ccmtsType:
			 html = "<a href='javascript:;' onClick='showRealTimeInfo(" + record.data.entityId+",\""+ record.data.name+ "\")'>" + I18N.COMMON.realtimeInfo + "</a> / ";
			 return String.format(html + "<a class='withSub' href='javascript:;' onClick='showMoreOperation({0},event)'>" +I18N.COMMON.others + "</a>", record.id);
		case cmtsType:
			return String.format(html + "<a class='withSub' href='javascript:;' onClick='showMoreOperation({0},event)'>" +I18N.COMMON.others + "</a>", record.id);
			break;
		default:
			return String.format("<a href='javascript:;' onclick='onDeleteClick()'>"+ I18N.COMMON.removeFromTopo +"</a>");
				break;
	}
	
}

//其它操作菜单项
function showMoreOperation(rid,event){
	var record = grid.getStore().getById(rid);  // Get the Record
	grid.getSelectionModel().selectRecords([record]);
	event.getXY = function(){return [event.clientX,event.clientY];};
    var entityMenu = getEntityMenu(record);
    entityMenu.showAt(event.getXY());
}
</script>
</head>
<body class="whiteToBlack">
</body>
</Zeta:HTML>
