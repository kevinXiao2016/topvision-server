<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head><title>Entity Alert</title>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var entityId = <s:property value="entityId"/>;
var type = <s:property value="type"/>;
var pageSize = <%= uc.getPageSize() %>;
var userId = <%= uc.getUser().getUserId() %>;

function getLastAlertId() {
	var model =  grid.getSelectionModel();
	if (model != null) {
		if (model.hasPrevious()) {
			model.selectPrevious(false);
		} else {
			model.selectLastRow(false);
		}
		var record = model.getSelected();
		return record.data.alertId;
	}
	return 0;
}
function printClick() {
	var wnd = window.open();
	showPrintWnd(Zeta$('alert-div'), wnd.document);
	var print = function() {
		wnd.print();
		wnd.close();
	};
	if(isFirefox) {
		wnd.setTimeout(print, 500)
	} else {
		print();
	}	
}
function showPrintWnd(obj, doc) {
	if(doc == null) {
		var wnd = window.open();
		doc = wnd.document;
	} else {
		doc.open();
	}
	doc.write('<html>');
	doc.write('<head>');
	doc.write('<title>' + I18N.ALERT.deviceAlert + '[<s:property value="entity.ip"/>]</title>');
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
	doc.write('<link rel="stylesheet" type="text/css" href="../css/print.css"/>');
	Zeta$NoHeaderAndFooter(doc);
	doc.write('</head>');
	doc.write('<body style="margin:50px;"><center>');
	doc.write(obj.innerHTML);
	doc.write('</center></body>');
	doc.write('</html>');
	doc.close();
}
function onRefreshClick() {
	store.reload();
}
function doOnResize() {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 100;
	grid.setWidth(w);
	grid.setHeight(h);
}
function alertTypeChanged(obj) {
	if (obj.options[obj.selectedIndex].value == 1) {
		location.href = 'showEntityAlertJsp.tv?module=6&type=1&entityId=' + entityId;
	} else if (obj.options[obj.selectedIndex].value == 2) {
		location.href = 'showEntityAlertJsp.tv?module=6&type=2&entityId=' + entityId;
	} else if (obj.options[obj.selectedIndex].value == 3) {
		location.href = 'showEntityHistoryAlertJsp.tv?module=6&type=3&entityId=' + entityId;
	} else if (obj.options[obj.selectedIndex].value == 4) {
		location.href = 'showEntityHistoryAlertJsp.tv?module=6&type=4&entityId=' + entityId;
	}
}
function buildPageBox(page) {
	return String.format(I18N.COMMON.displayPerPage, page);
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value; 
	if(p == pageSize) return;
	pageSize = parseInt(p);
	pagingToolbar.pageSize = pageSize;
	pagingToolbar.doLoad(0);
}

function onClearAlarmClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		window.parent.showTextAreaDlg(I18N.MODULE.clearAlert, I18N.EVENT.noteHeader + ':', function (type, text) {
			if (type == "cancel") {
				return;
			}
			var alertIds = [];
			var selections = sm.getSelections();
			for (var i = 0; i < selections.length; i++) {
				alertIds[i] = selections[i].data.alertId;
			}
			Ext.Ajax.request({url:"../fault/clearAlert.tv", method:"post", success:function (response) {
				onRefreshClick();
			}, failure:function () {
			}, params:{alertIds: alertIds, message:text}});
		});
	} else {
		window.parent.showMessageDlg(I18N.EVENT.tip, I18N.FAULT.pleaseSelectAlert);
	}
}
function onConfirmAlarmClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		window.parent.showTextAreaDlg(I18N.MODULE.confirmAlert, I18N.EVENT.noteHeader + ':', function (type, text) {
		if (type == "cancel") {
			return;
		}
		var alertIds = [];
		var selections = sm.getSelections();
		for (var i = 0; i < selections.length; i++) {
			alertIds[i] = selections[i].data.alertId;
		}
		Ext.Ajax.request({url:"../fault/confirmAlert.tv", method:"post",
			success:function (response) {
				onRefreshClick();
			}, failure:function () {
			}, params:{alertIds: alertIds, message: text}});
	});
	} else {
		window.parent.showMessageDlg(I18N.EVENT.tip, I18N.FAULT.pleaseSelectAlert);
	}
}
function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize, store:store, 
		displayInfo:true, items:['-', buildPageBox(pageSize), '-', 
		{text: I18N.COMMON.confirm, iconCls:'bmenu_confirm', handler: onConfirmAlarmClick},
		{text: I18N.EVENT.clear, iconCls:'bmenu_clear', handler: onClearAlarmClick}
	]});
	return pagingToolbar;
}

function renderNote(value, p, record) {
	return String.format('<img src="../images/fault/level{0}.gif" alt="{1}" border=0 hspace=0 align=absmiddle>&nbsp;{2}',
		record.data.level, record.data.levelName, record.data.message);
}
function renderStatus(value, p, record) {
	if (record.data.status == '1') {
		return String.format('<img hspace=5 src=\"../images/fault/confirm.png\" border=0 align=absmiddle title="{0}">', I18N.FAULT.confirmed);
	} else {
		return String.format('<img hspace=5 src=\"../images/fault/unconfirm.png\" border=0 align=absmiddle title="{0}">', I18N.FAULT.unconfirmed);	
	}
}

function getLastAlertId() {
	var model =  grid.getSelectionModel();
	if (model != null) {
		if (model.hasPrevious()) {
			model.selectPrevious(false);
		} else {
			model.selectLastRow(false);
		}
		var record = model.getSelected();
		return record.data.alertId;
	}
	return 0;
}
function getNextAlertId() {
	var model =  grid.getSelectionModel();
	if (model != null) {
		if (model.hasNext()) {
			model.selectNext(false);
		} else {
			model.selectFirstRow(false);
		}
		var record = model.getSelected();
		return record.data.alertId;
	}
	return 0;
}

var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "../images/s.gif";
Ext.onReady(function () {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 100;

	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var columnModels = [sm,
		{header: I18N.COMMON.description, width: 300, sortable: true, dataIndex: 'message', renderer: renderNote},
		{header: I18N.COMMON.type, width: 150, sortable: true, dataIndex: 'typeName'},	
		{header: I18N.EVENT.timeHeader, width: 140, sortable: true, dataIndex: 'firstTime'},
		{header: I18N.FAULT.checkStatus, width: 80, sortable: true, align:'center', dataIndex: 'status', renderer: renderStatus},	
		{header: I18N.FAULT.checkCustomer, width: 80, sortable: true, dataIndex: 'confirmUser'},
		{header: I18N.FAULT.checkTime, width: 140, sortable: true, dataIndex: 'confirmTime'}
	];

	store = new Ext.data.JsonStore({
	    url: '../alert/loadEntityAlert.tv?type=' + type + '&entityId=' + entityId,
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['alertId', 'typeName', 'host', 'level', 'levelName', 
	    	'name', 'message', 'firstTime', 'status', 'confirmUser', 'confirmTime']
	});
    store.setDefaultSort('firstTime', 'desc');    

	var cm = new Ext.grid.ColumnModel(columnModels);
	grid = new Ext.grid.GridPanel({id: 'extGridContainer', width: w, height: h,
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true, 
		store: store, cm: cm, sm: sm, bbar: buildPagingToolBar(),
		renderTo: 'alert-div'});
		
    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
   		var sm = grid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
    });  		

	grid.on("rowdblclick", function (grid, rowIndex, e) {		
   		var record = grid.getStore().getAt(rowIndex);  // Get the Record
		window.parent.createDialog("modalDlg", I18N.FAULT.alertProperty, 420, 420,
			"alert/showAlertDetail.tv?alertId=" + record.data.alertId, null, true, true);		
	});
	store.load({params:{start:0, limit: pageSize}});
});
</script>
<%
boolean confirmAlertPower = uc.hasPower("confirmAlertPower");
boolean clearAlertPower = uc.hasPower("clearAlertPower");
%>
</head><body class=BLANK_WND scroll=no style="padding:15px;" onresize="doOnResize();">
<table width=100% cellspacing=0 cellpadding=0>
<tr><td>
<%@ include file="../network/entity.inc" %>
</td></tr>

<tr><td height=50px valign=middle style="pading-top:20px;">
	<table width=100% cellspacing=0 cellpadding=0>
		<tr><td>
		<select style="width:150px;" onchange="alertTypeChanged(this);">
		<option value="1" <s:if test="type==1">selected</s:if>><fmt:message key="currentAlert" bundle="${fault}"/></option>
		<option value="3" <s:if test="type==3">selected</s:if>><fmt:message key="historyAlert" bundle="${fault}"/></option>
		</select></td>
		<td align=right>
		<%
		if (confirmAlertPower){
		%>
			<button type="button" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'" 
			onclick="onConfirmAlarmClick()"><fmt:message key="EVENT.confirm" bundle="${resource}"/></button>&nbsp;
		<%
		}
		if(clearAlertPower){
		%>
			<button type="button"
			class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseDown="this.className='BUTTON_PRESSED75'"
			onMouseOut="this.className='BUTTON75'" onclick="onClearAlarmClick()"><fmt:message key="COMMON.clear" bundle="${resource}"/></button>
		<%
		}
		%>
		</td></tr>
	</table>
</td></tr>

<tr><td><div id="alert-div"></div></td></tr>
</table>
</body></html>