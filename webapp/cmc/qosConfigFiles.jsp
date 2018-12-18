<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=c.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
function onRefreshClick() {
	store.reload();
}
function doOnResize() {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 70;
	grid.setWidth(w);
	grid.setHeight(h);
}

var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "../images/s.gif";
Ext.onReady(function () {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 80;
	
	var columns = [
		{header: I18N.CMC.title.index, width: 100, sortable: true, align: 'center', dataIndex: 'index'},	
		{header: I18N.QoS.priority, width: 100, sortable: true, align: 'center', dataIndex: 'priority'},
		{header: I18N.CMC.title.maxUpChannelWidth, width: 100, sortable:true, align : 'center', dataIndex: 'maxUpBandwidth'},
       	{header: I18N.CMC.title.guarUpBandwidth, width: 100, sortable:true, align: 'center', dataIndex: 'guarUpBandwidth'},
       	{header: I18N.CMC.title.maxDownWidth, width: 100, sortable:true, align: 'center', dataIndex: 'maxDownBandwidth'},
       	{header: I18N.CMC.title.maxMiniSlots, width: 100, sortable: true, align: 'center', dataIndex: 'maxTxBurst'},	
		{header: I18N.CHANNEL.equalizationData, width: 100, sortable: true, align: 'center', dataIndex: 'maxTransmitBurst'},
		{header: I18N.CMC.label.storageType, width: 100, sortable:true, align : 'center', dataIndex: 'storageType'},
       	{header: I18N.CMC.title.baselinePrivacy,width: 50, sortable:true, align: 'center', dataIndex: 'baselinePrivacy'},
       	{header: I18N.CMC.title.status, width: 50, sortable:true, align: 'center', dataIndex: 'status'}
	];
	store = new Ext.data.JsonStore({
	    url: ('getQosConfigFiles.tv?cmcId=' + cmcId),
	    root: 'data',
	    remoteSort: true, 
	    fields: ['index', 'priority', 'maxUpBandwidth', 'guarUpBandwidth', 'maxDownBandwidth', 'maxTxBurst', 'maxTransmitBurst', 'storageType', 'baselinePrivacy', 'status']
	});
	store.setDefaultSort('index', 'ASC');
	
	var cm = new Ext.grid.ColumnModel(columns);
	grid = new Ext.grid.GridPanel({id: 'extGridContainer', width: w, height: h,
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true, 
		store: store, cm: cm,
		renderTo: 'qosConfigFiles'});
    grid.on('rowdblclick', function(grid, rowIndex, e) {
    	window.top.createDialog('qosServiceStream', I18N.CMC.tip.qosServiceFlowInfo, 700, 400, 'cmcQos/showQosServiceStreamInfo.tv?cmcId=' + cmcId, null, true, true);
    });  		
	store.load();
});
</script>
</head>
<body class=BLANK_WND scroll=no style="padding: 15px;"
	onresize="doOnResize();">
	<table width=100% height=100% cellspacing=0 cellpadding=0>
		<tr>
			<td valign=top><%@ include file="../entity.inc"%></td>
		</tr>
		<tr>
			<td style="padding-top: 15px"><div id="qosConfigFiles"></div>
			</td>
		</tr>
	</table>
</body>
