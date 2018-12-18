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
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>

<script type="text/javascript">
// for tab changed start
function tabActivate() {
	window.top.setStatusBarInfo('', '');
	doRefresh();
	startTimer();
}
function tabDeactivate() {
	stopTimer();
}
function tabRemoved() {
	stopTimer();
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
	startTimer();
}
// for tab changed end

var entityId = <s:property value="entityId"/>;
var store = null;
var grid;
function tableChanged(obj) {
	location.href = 'showEntityMibJsp.tv?module=4&entityId=<s:property value="entity.entityId"/>&tableType=' + obj.options[obj.selectedIndex].value;
}
function doRefresh() {
	store.reload();
}
function refreshClick() {
	window.top.showWaitingDlg(I18N.MENU.wait, I18N.entityMib.loadingData);
	store.reload({
		callback: function() {
			window.top.closeWaitingDlg();
		}
	});
}
var refreshInterval = 0;
var timer = null;
function startTimer() {
	if (refreshInterval > 0) {
		if (timer == null) {
			timer = setInterval("doRefresh()", refreshInterval);
		}	
	}
}
function stopTimer() {
	if (timer != null) {
		clearInterval(timer);
		timer = null;
	}
}
function doOnunload() {
	stopTimer();
}
function doOnResize() {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 100;
	grid.setWidth(w);
	grid.setHeight(h);
}

function refreshBoxChanged(obj) {
	var t = parseInt(obj.options[obj.selectedIndex].value) * 1000;
	if (t == refreshInterval) {
		return;
	}
	refreshInterval = t;
	stopTimer(refreshInterval);
	startTimer();
}
function printClick() {
}
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
	var fields = [];
	var headers = [];
	<% int count = 0; %>
     <s:iterator value="headers">
     	headers[headers.length] = {header: '&nbsp;<s:property/>&nbsp;', dataIndex: 'header<%= count %>', width: 100};
     	fields[fields.length] = 'header<%= count++ %>';   
     </s:iterator>

	store = new Ext.data.JsonStore({
	    url: ('loadMibInfo.tv?entityId=' + entityId + '&tableType=<s:property value="tableType"/>'),
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: fields
	});

    var cm = new Ext.grid.ColumnModel(headers);
    cm.defaultSortable = true;
	
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 100;	
    grid = new Ext.grid.GridPanel({
		store : store,
        animCollapse:false,
        trackMouseOver:trackMouseOver,
        border: true,
        cm : cm,
        width : w,
        height: h,
        viewConfig: {forceFit:false, enableRowBody: true, showPreview: false}
    });
    grid.render("detail-div");
    
	var el = Zeta$('tableName');
	var options = el.options;
	for (var i = 0; i < options.length; i++) {
		if (options[i].value == '<s:property value="tableType"/>') {
			el.selectedIndex = i;
			break;
		}
	}    
    window.top.showWaitingDlg(I18N.MENU.wait, I18N.entityMib.loadingData);
	store.load({callback: function() {
		window.top.closeWaitingDlg();
	}});
	startTimer();
});
</script>
</head>
<body class=BLANK_WND scroll=no style="padding: 15px;"
	onresize="doOnResize()" onunload="doOnunload();">
	<table width=100% cellspacing=0 cellpadding=0>
		<tr>
			<td><%@ include file="entity.inc"%></td>
		</tr>

		<tr>
			<td height=50px valign=middle style="pading-top: 20px;">
				<div style="white-space: nowrap;">
					<table width=100% cellspacing=0 cellpadding=0>
						<tr>
							<td width=60px><fmt:message key="label.type" bundle="${resources}" /></td>
							<td width=150><select id="tableName" style="width: 150px;"
								onchange="tableChanged(this)">
									<s:iterator value="tableTypes">
										<option value="<s:property value="value"/>">
											<s:property value="name" />
										</option>
									</s:iterator>
							</select></td>
							<td width=50px>&nbsp;</td>
							<td width=75><fmt:message key="entityMib.Refreshinterval" bundle="${resources}" /></td>
							<td width=130><select id="refreshBox" style="width: 100px"
								onchange="refreshBoxChanged(this)">
									<option value="0" selected><fmt:message key="entityMib.notautomaticallyrefresh" bundle="${resources}" /></option>
									<option value="15">15<fmt:message key="label.seconds" bundle="${resources}" /></option>
									<option value="20">20<fmt:message key="label.seconds" bundle="${resources}" /></option>
									<option value="30">30<fmt:message key="label.seconds" bundle="${resources}" /></option>
									<option value="60">60<fmt:message key="label.seconds" bundle="${resources}" /></option>
									<option value="120">120<fmt:message key="label.seconds" bundle="${resources}" /></option>
									<option value="180">180<fmt:message key="label.seconds" bundle="${resources}" /></option>
							</select></td>
							<td align=right>
								<button class=BUTTON75
									onMouseOver="this.className='BUTTON_OVER75'"
									onMouseDown="this.className='BUTTON_PRESSED75'"
									onMouseOut="this.className='BUTTON75'" onclick="refreshClick()"><fmt:message key="MENU.refresh" bundle="${resources}" /></button>
							</td>
						</tr>
					</table>
				</div></td>
		</tr>
		<tr>
			<td><div id="detail-div"></div>
			</td>
		</tr>
	</table>
</body>
</html>