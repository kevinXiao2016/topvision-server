<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/ext/ext-base-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-all-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>-2.1.js"></script>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script>
var entityId = <s:property value="entityId"/>;
var store = null;
function refreshBoxChanged(box) {
	var t = parseInt(box.options[box.selectedIndex].value) * 1000;
	if (t == refreshInterval) {
		return;
	}
	refreshInterval = t;
	doOnunload();
	if (t > 0) {
		doOnload();
	}
}

Ext.onReady(function(){
	Ext.BLANK_IMAGE_URL = '../images/s.gif';
    var cm = new Ext.grid.ColumnModel([
    	{header: '&nbsp;'+I18N.entityLinkInfo.portNo+'&nbsp;', dataIndex: 'ifIndex', width: 80},
    	{header: '&nbsp;'+I18N.portFlowRealData.Description+'&nbsp;', dataIndex: 'ifDescr', width: 140},
    	{header: I18N.NETWORK.typeHeader, dataIndex: 'ifType', width: 80},
    	{header: '&nbsp;'+I18N.NETWORK.manageStatus+'&nbsp;', dataIndex: 'ifAdminStatus', width: 80},
    	{header: '&nbsp;'+I18N.entityPorts.operationStatus+'&nbsp;', dataIndex: 'ifOperStatus', width: 80},
    	{header: '&nbsp;'+I18N.portFlowRealData.Totalflow+'&nbsp;', dataIndex: 'ifFlow', width: 80, align : 'right'},
    	{header: '&nbsp;'+I18N.portFlowRealData.inflow+'&nbsp;', dataIndex: 'ifFlow', width: 80, align : 'right'},
    	{header: '&nbsp;'+I18N.portFlowRealData.outflow+'&nbsp;', dataIndex: 'ifFlow', width: 80, align : 'right'}]);
    cm.defaultSortable = true;
	store = new Ext.data.JsonStore({
	    url: ('getPortRealDataByEntity.tv?entityId=' + entityId),
	    root: 'data',
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['ifIndex', 'ifDescr', 'ifType', 'ifMtu', 'ifSpeed', 'ifPhysAddress', 'ifAdminStatus', 'ifOperStatus']
	});
    var grid = new Ext.grid.GridPanel({
		store: store,
        animCollapse: false,
        trackMouseOver: trackMouseOver,
        border: true,
        cm: cm,
        width: (document.body.clientWidth - 40),
        height: (document.body.clientHeight - 100),
        viewConfig: {forceFit: false, enableRowBody: true, showPreview: false}
    });
    grid.render("detail-div");
	store.load();
	
	doOnload();
});

function selectIpClick() {
}

function refreshClick() {
	store.reload();
}

var refreshInterval = 20 * 1000;
var timer = null;
function doOnload() {
	timer = setInterval("refreshClick()", refreshInterval);
}
function doOnunload() {
	if (timer != null) {
		clearInterval(timer);
		timer = null;
	}
}
</script>
</head>
<body class=BLANK_WND style="margin: 15px;" onunload="doOnunload();">
	<table width=100% align = "center">
		<tr>
			<td>
				<table cellspacing=0 cellpadding=0>
					<tr>
						<td><a><b><fmt:message key="portFlowRealCompareInfo.portData" bundle="${resources}" /></b>
						</a> <a style="margin-left: 10px"
							href="showPortRealSingleInfo.tv?entityId=<s:property value="entityId"/>&ip=<s:property value="ip"/>"><fmt:message key="portFlowRealCompareInfo.Portanalysis" bundle="${resources}" /></a>
							<a style="margin-left: 10px"
							href="showPortRealCompareInfo.tv?entityId=<s:property value="entityId"/>&ip=<s:property value="ip"/>"><fmt:message key="portFlowRealCompareInfo.ComparativeAnalysisofmulti-port" bundle="${resources}" /></a>
						</td>
					</tr>
					<tr>
						<td>
							<table cellspacing=0 cellpadding=0>
								<tr>
									<td width=70 height=40><fmt:message key="offManagement.Deviceaddress" bundle="${resources}" />:</td>
									<td width=100><input class=iptxt id="ip" type=text
										value="<s:property value="ip"/>" readonly>
									</td>
									<td width=120>&nbsp;
										<button class=BUTTON75
											onMouseOver="this.className='BUTTON_OVER75'"
											onMouseDown="this.className='BUTTON_PRESSED75'"
											onMouseOut="this.className='BUTTON75'"
											onclick="selectIpClick()"><fmt:message key="herfFolderProperty.select" bundle="${resources}" /></button>
									</td>
									<td width=75><fmt:message key="topoLabel.refreshRate" bundle="${resources}" />:</td>
									<td width=110><select id="refreshBox" style="width: 80px"
										onchange="refreshBoxChanged(this)">
											<option value="10">10</option>
											<option value="15" selected>15</option>
											<option value="20">20</option>
											<option value="30">30</option>
											<option value="60">60</option>
											<option value="90">90</option>
											<option value="120">120</option>
											<option value="180">180</option>
									</select>&nbsp;<fmt:message key="label.seconds" bundle="${resources}" /></td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td><div id="detail-div"></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
