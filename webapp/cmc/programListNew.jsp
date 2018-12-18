<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
	import js.jquery.Nm3kTabBtn
	import js/jquery/jquery.wresize
</Zeta:Loader>
<style type="text/css">
body,html{ height:100%;}
#openLayer{ width:100%; height:100%; position:absolute; z-index:999; top:0; left:0; display:none;}
</style>
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css" /> 
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
vcEntityKey = 'cmcId';
var pageSize = <%= uc.getPageSize() %>;
var cmcId = <s:property value="cmcId"/>;
var entityId = '<s:property value="entityId"/>';
var productType ='<s:property value="productType"/>';
var baseStore=null;
var baseGrid = null;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;

function onRefreshClick() {
	baseStore.reload();
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

//基本信息;
function createMoreInfoList(){
	var w = document.body.clientWidth - 24;//多一个滚动条的距离;	
	var h = document.body.clientHeight - 100;
	h = $(window).height() - 100
	if(h < 200){h = 200;}
	
	var baseColumns = [	
		{header: '@CHANNEL.channel@', width: parseInt(w*1/15),sortable: false, align: 'center',menuDisabled:true, dataIndex: 'channelId',renderer: channelIdRender},
		{header: '@sessionId@', width: parseInt(w*1/15), sortable: true, align: 'center',menuDisabled:true, dataIndex: 'sessionId'},
		{header: '@inputSrcAddress@', width: parseInt(w*1/8), sortable: true, align : 'center',menuDisabled:true,  dataIndex: 'mpegInputUdpOriginationSrcInetAddr', renderer: renderMpegInputUdpOriginationPacketsDetected},
		{header: '@inputDestAddress@', width: parseInt(w*1/8), sortable: true, align : 'center',menuDisabled:true,  dataIndex: 'mpegInputUdpOriginationDestInetAddr'},
		{header: '@inputDestPort@', width: parseInt(w*1/8), sortable: true, align : 'center',menuDisabled:true,  dataIndex: 'mpegInputUdpOriginationDestPort'},		
       	{header: '@detect@', width: parseInt(w*1/16), sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegInputUdpOriginationPacketsDetected', renderer: renderYN},
		{header: '@originationActive@', width: parseInt(w*1/16), sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegInputUdpOriginationActive', renderer: renderYN},
		{header: '@outputProgramNo@', width: parseInt(w*1/11),sortable: true, align : 'center',menuDisabled:true,  dataIndex: 'mpegOutputProgNo'},
       	{header: '@outputPmtVersion@', width: parseInt(w*1/9), sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegOutputProgPmtVersion', renderer: renderMpegInputUdpOriginationActive},
       	{header: '@outputPmtPid@',width: parseInt(w*1/9), sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegOutputProgPmtPid'},
       	{header: '@outputPcrPid@', width: parseInt(w*1/9), sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegOutputProgPcrPid', renderer: renderMpegInputUdpOriginationActive},
       	{header: '@outputTsNnmber@', width: parseInt(w*1/8), sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegOutputProgNumElems', renderer: renderMpegInputUdpOriginationActive},
       	{header: '@outputRate@', width: parseInt(w*1/11), sortable: true, align: 'center',menuDisabled:true, dataIndex: 'mpegVideoSessionBitRate', renderer: renderMpegVideoSessionBitRate}
		];
    
	baseStore = new Ext.data.JsonStore({
	    url: ('/cmc/ipqam/getProgramInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['entityId','cmcId','portId','sessionId', 'channelId','mpegVideoSessionIndex','mpegVideoSessionID', 'mpegInputUdpOriginationSrcInetAddr',
	             'mpegInputUdpOriginationDestInetAddr', 'mpegInputUdpOriginationDestPort', 'mpegInputUdpOriginationPacketsDetected',
	    		'mpegInputUdpOriginationActive', 'mpegOutputProgNo', 'mpegOutputProgPmtVersion', 'mpegOutputProgPmtPid', 
		     	    'mpegOutputProgPcrPid', 'mpegOutputProgNumElems', 'mpegVideoSessionBitRate']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		id: 'extmoreInfoGridContainer', 
		height: h,
		animCollapse: animCollapse, 
		trackMouseOver: trackMouseOver, 
		viewConfig: { forceFit:true},
		border: true, 
		store: baseStore, 
		bbar: buildPagingToolBar(),
		cm: baseCm,title: '@programInfo@',
		renderTo: 'moreInfodetail-div'});
		baseStore.load({params:{start:0, limit: pageSize}});
}

function refreshBaseinfo(){
	window.parent.showWaitingDlg(I18N.COMMON.waiting, "@refreshProgramInfo@",'waitingMsg','ext-mb-waiting');
	$.ajax({
		url: '/cmc/ipqam/refreshProgramInfo.tv?r=' + Math.random(),
	    type: 'post',
	    data : {
	    	cmcId: cmcId,
	    	productType: productType,
			entityId: entityId
		},
	    success: function(response) {
  	    	onRefreshClick()  	    		
  	    	window.parent.closeWaitingDlg();
  	    	top.afterSaveOrDelete({
   				title: I18N.RECYLE.tip,
   				html: '<b class="orangeTxt">' + I18N.text.refreshSuccessTip +'</b>'
   			});
		}, error: function(response) {
			window.parent.closeWaitingDlg();
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
		}, cache: false
	}); 
}

function channelIdRender(value, cellmate, record){
	var docsIfDownChannelId = record.get('channelId');
    return '@CHANNEL.channel@'+ docsIfDownChannelId;
}

$(function(){
	window.createMoreInfoList();
	//点击从设备上获取；
	$("#btn1").click(function(){
		window.refreshBaseinfo();
	});
	if(!refreshDevicePower){
        $("#btn1").attr("disabled",true);
    }
});//end document.ready;

</script>
<title><fmt:message bundle="${cmc}" key="text.upChannelList"/> </title>
</head>
<body class="BLANK_WND newBody" onload="" >	
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>
		<div class="edgeAndClearFloat">
			<ol class="upChannelListOl pL0 pB0" id="upChannelListOl">
				<li><a href="javascript:;" class="normalBtn" id="btn1" name="<fmt:message bundle='${cmc}' key='text.refreshData'/>"><span><i class="miniIcoEquipment"></i>@text.refreshData@</span></a></li>
			</ol>
		</div>
		<div class="paddingLR10 clearBoth pT10">
			<div class="whiteTabContent">
				<div class="containerLegent">
					<div id="moreInfodetail-div" class="normalTable extZebra"></div>
				</div>
			</div>
		</div>
		
</body>
</Zeta:HTML>