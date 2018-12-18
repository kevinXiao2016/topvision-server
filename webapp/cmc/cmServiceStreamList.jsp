<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<title><fmt:message bundle='${cmc}' key='QoS.serviceFlowList'/></title>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
function onRefreshClick() {
	store.reload();
}
var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "../images/s.gif";
Ext.onReady(function () {
	var w = document.body.clientWidth - 30 ;
	var h = document.body.clientHeight - 70;
	
	var columns = [
		{header: 'ServiceId', width: 50, sortable: true, align: 'center', dataIndex: 'serviceId'},	
		{header: 'CM ID', width: 50, sortable: true, align: 'center', dataIndex: 'serviceCmStatusIndex'},
		{header: I18N.CCMTS.channel.adminStatus, width: 100, sortable:true, align : 'center', dataIndex: 'serviceAdminStatus'},
       	{header: I18N.CMC.text.property, width: 100, sortable:true, align: 'center', dataIndex: 'serviceQosProfile'},
       	{header: I18N.CMC.text.CreateTime, width: 100, sortable:true, align: 'center', dataIndex: 'serviceCreateTime'},
       	{header: I18N.CMC.text.InOctets, width: 100, sortable: true, align: 'center', dataIndex: 'serviceInOctets'},	
		{header: I18N.CMC.text.InPackets, width: 100, sortable: true, align: 'center', dataIndex: 'serviceInPackets'},
		{header: 'CmStatusIndex', width: 100, sortable:true, align : 'center', dataIndex: 'serviceNewCmStatusIndex'}
	];
	store = new Ext.data.JsonStore({
	    url: ('getStreamInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    remoteSort: true, 
	    fields: ['serviceId', 'serviceCmStatusIndex', 'serviceAdminStatus', 'serviceQosProfile', 'serviceCreateTime', 'serviceInOctets', 'serviceInPackets', 'serviceNewCmStatusIndex']
	});
	store.setDefaultSort('index', 'ASC');
	
	var cm = new Ext.grid.ColumnModel(columns);
	grid = new Ext.grid.GridPanel({id: 'extGridContainer', width: w, height: h,
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true, region:'center',
		store: store, cm: cm,
		viewConfig: {forceFit:true, enableRowBody: true, showPreview: false},
		renderTo: 'qosServiceStream'});
    grid.on('rowdblclick', function(grid, rowIndex, e) {
    });  		
	store.load();
});
function cancelClick() {
	window.top.closeWindow('cmServiceStream');
}
</script>
</head>
<body class=BLANK_WND style="margin: 10pt 10pt 0pt 10pt; " class=POPUP_WND">
	<div id="qosServiceStream" style="overflow:auto; height:310px;"></div>
	<div style="height:20px;" >
		<table width=100%>
			<tr>
				<td valign=top align=right>
					<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.label.close'/></button>
				</td>
			</tr>
		</table>
  </div>
</body>
