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
var cmcId = <s:property value="cmcId"/>;
var entityId = '<s:property value="entityId"/>';
var productType ='<s:property value="productType"/>';
var baseStore=null;
var baseGrid = null;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;

function onRefreshClick() {
	baseStore.reload();
}

//基本信息;
function createMoreInfoList(){
	var w = document.body.clientWidth - 24;//多一个滚动条的距离;	
	var h = document.body.clientHeight - 120;
	h = $(window).height() - 120
	if(h < 200){h = 200;}
	
	var baseColumns = [	
		{header: '@CHANNEL.channel@', width: parseInt(w*2/11), sortable: false, align: 'center',menuDisabled:true, dataIndex: 'channelId',renderer: channelIdRender},
		{header: '@CHANNEL.frequency@', width: parseInt(w*2/11), sortable: false, align: 'center',menuDisabled:true, dataIndex: 'qamChannelFrequencyForunit'},
		{header: '@CHANNEL.desc@', width: parseInt(w*2/11), sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'ifDescr'},
		{header: '@CHANNEL.usedBw@', width: parseInt(w*2/11), sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'qamChannelCommonOutputBwUsedString'},		
       	{header: '@CHANNEL.totalBw@', width: parseInt(w*2/11), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'qamChannelCommonOutputBwString'},
		{header: '@CHANNEL.Utilization@', width: parseInt(w*2/11), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'qamChannelCommonUtilizationString'},
		{header: '@CHANNEL.SymbolRate@', width: parseInt(w*2/11), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'qamChannelSymbolRateString'},	
		{header: '@CHANNEL.modulationType@', width: parseInt(w*2/11), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'qamChannelModulationFormatName'},		
		{header: '@CHANNEL.carrierpower@', width: parseInt(w*2/11), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'qamChannelPowerForunit'},
		{header: '@CHANNEL.interleave@', width: parseInt(w*2/11), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'qamChannelInterleaverModeName'},
		{header: 'Annex', width: parseInt(w*2/11), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'qamChannelAnnexModeName'}  
		];
    
	baseStore = new Ext.data.JsonStore({
	    url: ('/cmc/ipqam/getEqamInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    fields: ['entityId','cmcId','portId','channelId','qamChannelFrequencyForunit','ifDescr', 'qamChannelModulationFormatName','qamChannelInterleaverModeName', 
	    		'qamChannelPowerForunit', 'qamChannelAnnexModeName','qamChannelCommonOutputBwString', 'qamChannelCommonUtilizationString', 'qamChannelSymbolRateString', 
	    		'qamChannelCommonOutputBwUsedString']
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
		cm: baseCm,title: '@CHANNEL.channelstatus@',
		renderTo: 'moreInfodetail-div'});
		baseStore.load();
}

function refreshBaseinfo(){
	window.parent.showWaitingDlg(I18N.COMMON.waiting, '@refreshEqamInfo@','waitingMsg','ext-mb-waiting');
	$.ajax({
		url: '/cmc/ipqam/refreshEqamInfo.tv?r=' + Math.random(),
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
			<div class="whiteTabContent">
				<div id="qualityInfo-div" class="normalTable extZebra"></div>
			</div>
			<div class="whiteTabContent">
				<div id="statisticInfo-div" class="normalTable extZebra"></div>
			</div>
		</div>
</body>
</Zeta:HTML>