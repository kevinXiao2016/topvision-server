<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="report"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript">
var pageSize = 10
var store
var grid
window.top.ZetaCallback = null
function queryClick() {
	var el = Zeta$('folderId');
	var fid = el.options[el.selectedIndex].value;
	el = Zeta$('entityType');
	var type = el.options[el.selectedIndex].value;
	var name = Zeta$('name').value.trim();
	var ip = Zeta$('ip').value.trim();
	store.setBaseParam("folderId", fid);
	store.setBaseParam("type", type);
	store.setBaseParam("name", name);
	store.setBaseParam("ip", ip);
	store.load({params: {start: 0, limit: pageSize}});
}
function resetClick() {
	Zeta$('folderId').selectedIndex = 0;
	Zeta$('entityType').selectedIndex = 0;
	Zeta$('name').value = '';
	Zeta$('ip').value = '';
}
function okClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections(); 
		var ids = [];
		var names = [];
		var ips = [];
		for (var i = 0; i < selections.length; i++) {
			ids[i] = selections[i].data.entityId;
			if (selections[i].data.name == '') 
			{
				names[i] = selections[i].data.ip; 
			} else {
				names[i] = selections[i].data.name;
			}
			ips[i] = selections[i].data.ip;
		}
		window.top.ZetaCallback = {type: 'entity', entityIds: ids, entityNames: names, entityIps: ips, size: selections.length};
	} else {
		window.top.ZetaCallback = null;
	}
	window.top.closeWindow('modalDlg');
}
function cancelClick() {
	window.top.ZetaCallback = null;
	window.top.closeWindow('modalDlg');
}
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 
Ext.onReady(function() {
	store = new Ext.data.JsonStore({
	    url: 'queryEntity.tv',
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['entityId', 'name', 'ip', 'typeName']
	});
    store.setDefaultSort('ip', 'asc');
	<%
	if ("true".equals(request.getParameter("singleSelect"))) {
	%>
	var sm = new Ext.grid.CheckboxSelectionModel({singleSelect: true});
	<%
	} else {
	%>
	var sm = new Ext.grid.CheckboxSelectionModel();
	<%
	}
	%>
    var cm = new Ext.grid.ColumnModel([
    	sm,
    	{header: '<fmt:message bundle="${report}" key="report.deviceName" />', dataIndex: 'name', sortable: false, width: 200},
		{header: '<fmt:message bundle="${report}" key="report.deviceAddr" />', dataIndex: 'ip', sortable: true, width: 150},
    	{header: '<fmt:message bundle="${report}" key="report.deviceType" />', dataIndex: 'typeName', sortable: true, width: 150}
    ]);
    cm.defaultSortable = true;
	grid = new Ext.grid.GridPanel({width: 624, height: 290, border: true,
        store: store, cm: cm, sm: sm, trackMouseOver: trackMouseOver,
        bbar: new Ext.PagingToolbar({pageSize: pageSize, store: store, displayInfo: true, items: ['-','<fmt:message bundle="${report}" key="COMMON.pageShow" />10<fmt:message bundle="${report}" key="COMMON.size" />']}),
        renderTo: 'grid-div'
    });
	store.load({params: {start: 0, limit: pageSize}});
	Zeta$('queryPanel').style.display = '';
	Zeta$('buttonPanel').style.display = '';

	$("#entityType").find("option[value=30001]").remove();
	$("#entityType").find("option[value=20000]").remove();
	$("#entityType").find("option[value=30000]").remove();
	$("#entityType").find("option[value=255]").remove();
	$("#entityType").find("option[value=69]").remove();
	$("#entityType").find("option[value=67]").remove();
	$("#entityType").find("option[value=65]").remove();
	$("#entityType").find("option[value=37]").remove();
	$("#entityType").find("option[value=36]").remove();
	$("#entityType").find("option[value=34]").remove();
	$("#entityType").find("option[value=33]").remove();
});
</script>
</HEAD><BODY class=POPUP_WND style="padding:10px;">
<div id="queryPanel" style="display:none">
<table width=100% cellspacing=0 cellpadding=0>
	<tr><td width=80px height=26px><fmt:message bundle="${report}" key="report.topo" />:</td><Td>
		<select id="folderId" style="width:172px" name="folderId">
			<option value="0"><fmt:message bundle="${report}" key="report.allTopo" /></option>
			<s:iterator value="topoFolders">
			<option value="<s:property value="folderId"/>"><s:property value="name"/></option>
			</s:iterator>
		</select></Td>
	<Td width=50px></Td>	
	<td width=80px><fmt:message bundle="${report}" key="report.deviceType" />:</td>
	<td align=right>
	<select id="entityType" name="entityType" style="width:172px;">
		<option value="0"><fmt:message bundle="${report}" key="report.allDeviceType" /></option>
		<s:iterator value="entityTypes">
		<option value="<s:property value="typeId"/>"><s:property value="displayName"/></option>
		</s:iterator>
	</select></td></tr>
	<tr><td width=80px height=26px><fmt:message bundle="${report}" key="report.deviceName" />:</td><Td><input class=iptxt id="name" type=text style="width:172px"></Td>
	<Td></Td>	
	<td width=80px><fmt:message bundle="${report}" key="report.deviceAddr" />:</td><td align=right><input class=iptxt id="ip" type=text style="width:172px"></td></tr>
	<tr><td colspan=5 align=right style="padding-top:6px;padding-bottom:8px;"><button type="button" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="queryClick()"><fmt:message bundle="${report}" key="COMMON.query" /></button>&nbsp;<button type="button" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="resetClick()"><fmt:message bundle="${report}" key="COMMON.reset" /></button></td></tr>
</table>
</div>

<div id="grid-div"></div>

<div id="buttonPanel" align=right style="padding-top:10px;display:none">
	<button type=button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="okClick()"><fmt:message bundle="${report}" key="COMMON.confirm" /></button>&nbsp;<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="cancelClick()"><fmt:message bundle="${report}" key="COMMON.cancel" /></button>
</div>
</BODY></HTML>