<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
</style>
<script>
var entityId = ${entityId};
var entity = ${entity};
var pageSize = <%= uc.getPageSize() %>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var baseStore=null;
var baseGrid = null;
var queryData = {
	start: 0,
	limit: pageSize,
	queryContent: ''
};

Ext.onReady(function(){
	var baseColumns = [	
		{header: 'CMTS@COMMON.alias@', width: 100,sortable: false, align: 'center',menuDisabled:true, dataIndex: 'cmtsName'},
   		{header: '@CHANNEL.channel@', width: 100,sortable: false, align: 'center',menuDisabled:true, dataIndex: 'channelId',renderer: channelIdRender},
   		{header: '@sessionId@', width: 100, sortable: true, align: 'center',menuDisabled:true, dataIndex: 'sessionId'},
   		{header: '@inputSrcAddress@', width: 100, sortable: true, align : 'center',menuDisabled:true,  dataIndex: 'mpegInputUdpOriginationSrcInetAddr', renderer: renderMpegInputUdpOriginationPacketsDetected},
   		{header: '@inputDestAddress@', width: 100, sortable: true, align : 'center',menuDisabled:true,  dataIndex: 'mpegInputUdpOriginationDestInetAddr'},
   		{header: '@inputDestPort@', width: 100, sortable: true, align : 'center',menuDisabled:true,  dataIndex: 'mpegInputUdpOriginationDestPort'},		
      	{header: '@detect@', width: 100, sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegInputUdpOriginationPacketsDetected', renderer: renderYN},
   		{header: '@originationActive@', width: 100, sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegInputUdpOriginationActive', renderer: renderYN},
   		{header: '@outputProgramNo@', width: 100,sortable: true, align : 'center',menuDisabled:true,  dataIndex: 'mpegOutputProgNo'},
       	{header: '@outputPmtVersion@', width: 100, sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegOutputProgPmtVersion', renderer: renderMpegInputUdpOriginationActive},
       	{header: '@outputPmtPid@',width: 100, sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegOutputProgPmtPid'},
       	{header: '@outputPcrPid@', width: 100, sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegOutputProgPcrPid', renderer: renderMpegInputUdpOriginationActive},
       	{header: '@outputTsNnmber@', width: 100, sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegOutputProgNumElems', renderer: renderMpegInputUdpOriginationActive},
       	{header: '@outputRate@', width: 100, sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegVideoSessionBitRate', renderer: renderMpegVideoSessionBitRate}
	];
	               
   	baseStore = new Ext.data.JsonStore({
   	    url: ('/cmc/ipqam/getOltProgramInfo.tv?entityId=' + entityId),
   	    root: 'data',
   	    totalProperty: 'rowCount',
   	    remoteSort: true,
   	    fields: ['cmtsName', 'entityId','cmcId','portId','sessionId', 'channelId','mpegVideoSessionIndex','mpegVideoSessionID', 'mpegInputUdpOriginationSrcInetAddr',
   	             'mpegInputUdpOriginationDestInetAddr', 'mpegInputUdpOriginationDestPort', 'mpegInputUdpOriginationPacketsDetected',
   	    		'mpegInputUdpOriginationActive', 'mpegOutputProgNo', 'mpegOutputProgPmtVersion', 'mpegOutputProgPmtPid', 
   		     	    'mpegOutputProgPcrPid', 'mpegOutputProgNumElems', 'mpegVideoSessionBitRate']
   	});
   	
   	var tbar = new Ext.Toolbar({
   	    items: [
   	      {text: '@ccm/text.refreshData@', id: 'clearSingleCm', disabled: !refreshDevicePower, iconCls: 'bmenu_equipment', handler: refreshDeviceData}, 
   	    ]
   	  });
	           	
   	var baseCm = new Ext.grid.ColumnModel(baseColumns);
   	baseGrid = new Ext.grid.GridPanel({
   		cls: 'normalTable',
   		bodyCssClass : 'normalTable',	
   		id: 'extmoreInfoGridContainer', 
   		animCollapse: animCollapse, 
   		trackMouseOver: trackMouseOver, 
   		viewConfig: { forceFit:true},
   		region: 'center',
   		border: true, 
   		store: baseStore, 
   		margins: '10px',
   		tbar: tbar,
   		bbar: buildPagingToolBar(),
   		cm: baseCm,
   		title: '@programInfo@'
   	});
   	
   	baseStore.on('beforeload', function() {
   		baseStore.baseParams = queryData;
  	});
	
   	baseStore.load();
   		
	new Ext.Viewport({
	    layout: 'border',
	    items: [{
	        region: 'north',
	        border: false,
	        contentEl: 'query-container',
	        height: 80
	      },
	      baseGrid
	    ]
	  });
})

function onRefreshClick() {
	baseStore.reload();
}

function refreshDeviceData() {
	window.parent.showWaitingDlg(I18N.COMMON.waiting, "@refreshProgramInfo@",'waitingMsg','ext-mb-waiting');
	$.ajax({
		url: '/cmc/ipqam/refreshOltProgramInfo.tv?r=' + Math.random(),
	    type: 'post',
	    data : {
	    	entityId: entityId
		},
	    success: function(response) {
  	    	onRefreshClick()  	    		
  	    	window.parent.closeWaitingDlg();
  	    	top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@text.refreshSuccessTip@</b>'
   			});
		}, error: function(response) {
			window.parent.closeWaitingDlg();
			window.parent.showMessageDlg('@COMMON.tip@', '@text.refreshFailureTip@');
		}, cache: false
	}); 
}

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:baseStore,
        displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize), '-'
    ]});
    return pagingToolbar;
}

function renderMpegVideoSessionBitRate(value,p,record){
	return value + " bps";
}

function renderMpegInputUdpOriginationActive(value,p,record){
	if(record.data.mpegInputUdpOriginationActive != 1){
		return "-"
	}else{
		return value;
	}
}

function renderMpegInputUdpOriginationPacketsDetected(value,p,record){
	if(record.data.mpegInputUdpOriginationPacketsDetected != 1){
		return "-"
	}else{
		return value;
	}
}

function renderYN(value,p,record){
	if(value == 1){
		return 'Y'
	}else{
		return 'N'
	}
}

function channelIdRender(value, cellmate, record){
	var docsIfDownChannelId = record.get('channelId');
    return '@CHANNEL.channel@'+ docsIfDownChannelId;
}

function query() {
	var _queryContent = $('#queryContent').val();
	
	queryData.queryContent = _queryContent;
	baseStore.load();
}
</script>
</head>
<body class="newBody clear-x-panel-body">
	<div id="query-container">
		<div id="header">
			<%@ include file="/epon/inc/navigator.inc"%>
		</div>
		
		<div style="margin: 10px;">
			<table>
				<tr>
					<td><input type="text" class="normalInput" style="width:600px;" id="queryContent" placeHolder="@EPON.EQAM.queryTip@" maxlength="63" /></td>
					<td><a href="javascript:;" class="normalBtn" onclick="query()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
</body>
</Zeta:HTML>  
