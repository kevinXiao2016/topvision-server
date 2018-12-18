<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/cmc/rendererFunction.js"></script>
<script type="text/javascript">
var cmMac = '<s:property value="cmMac"/>';
function onRefreshClick() {
	store.reload();
}
var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "/images/s.gif";
Ext.onReady(function () {
	var w = document.body.clientWidth - 40 ;
	var h = document.body.clientHeight - 60;
	
	var columns = [
		{header: I18N.QoS.serviceFlowId, width: parseInt(w*1/4)-1, sortable: true, align: 'center', dataIndex: 'docsQosServiceFlowId'},	
		{header: 'SID', width: parseInt(w*1/4)-1, sortable: false, align: 'center', dataIndex: 'docsQosServiceFlowSID'},
		//{header: 'CM MAC', width: 150, sortable: true, align: 'center', dataIndex: 'statusMacAddress'},
		{header: I18N.QoS.direction, width: parseInt(w*1/4)-1, sortable: false, align: 'center', dataIndex: 'docsQosServiceFlowDirectionString'},
		{header: I18N.QoS.serviceFlowPrimary, width: parseInt(w*1/4)-1, sortable:false, align : 'center', dataIndex: 'docsQosServiceFlowPrimary',renderer:renderYesOrNo}       
		];
	store = new Ext.data.JsonStore({
	    url: ('getCmServiceFlowList.tv?cmMac='+cmMac),
	    root: 'data',
	    //remoteSort: true, 
	    fields: ['docsQosServiceFlowId', 'docsQosServiceFlowSID', 'statusMacAddress', 
	     	    'docsQosServiceFlowDirectionString', 'docsQosServiceFlowPrimary']
	});
	store.setDefaultSort('docsQosServiceFlowId', 'ASC');
	
	var cm = new Ext.grid.ColumnModel(columns);
	grid = new Ext.grid.GridPanel({id: 'extGridContainer', width: w, height: h,
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true, region:'center',
		store: store, cm: cm,
		//viewConfig: {forceFit:true},
		renderTo: 'serviceFlowContactedToCm'});
    grid.on('rowdblclick', function(grid, rowIndex, e) {
    });  		
	store.load();
});
function cancelClick() {
	window.top.closeWindow('cmServiceStream');
}
</script>
<title><fmt:message bundle='${cmc}' key='CM.cmServiceFlow'/></title>
</head>
<body class=POPUP_WND style="padding-top: 15px; padding-left: 15px; padding-right: 15px; padding-bottom: 5px">
	<div id="serviceFlowContactedToCm" style="overflow:auto;"></div>
	<div align=right style="margin-top: 10px; margin-right:10px;margin-bottom: 5px;">
		<button id=saveBt class=BUTTON75 type="button"
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onMouseDown="this.className='BUTTON_PRESSED75'"
			onclick="cancelClick()"><fmt:message bundle='${resources}' key='COMMON.close'/></button>
	</div>
</body>