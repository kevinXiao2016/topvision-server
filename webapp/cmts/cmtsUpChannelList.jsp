<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    MODULE  CMC
    css css.white.disabledStyle
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/Nm3kTabBtn.js"></script>
<script type="text/javascript">
var STATUS_UP = 1;
var STATUS_DOWN = 2;
var cmcId = ${ cmcId };
var productType = ${ productType };
Ext.BLANK_IMAGE_URL = "/images/s.gif";
var baseStore=null;
var qualityStore=null;
var baseGrid = null;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var currentTab = 0;

function onRefreshClick() {
	baseStore.reload();
}
function renderOnlySCDMAParam(value,p,record){
	if(record.data.docsIfUpChannelTypeName=='SCDMA'){
		return value;
		}else{
				return '-';
			}
}
function renderAdminStatus(value, p, record){
	if (record.data.ifAdminStatus == '1') {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/admin.gif" border=0 align=absmiddle class="nm3kTip">',
			I18N.COMMON.on);		
	} else {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/noadmin.gif" border=0 align=absmiddle  class="nm3kTip">',
			I18N.COMMON.off);	
	}
}
function renderIfName(value, p, record){
	return record.data.ifName;
}
function rendererAdminUpOrDown(value, p, record){
	if (record.data.channelPreEqEnable == '1') {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/admin.gif" border=0 align=absmiddle  class="nm3kTip">',
			I18N.COMMON.on);		
	} else {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/noadmin.gif" border=0 align=absmiddle  class="nm3kTip">',
			I18N.COMMON.off);	
	}
}
function renderSigQIncludesContention(value, p, record){
	if (value == '1') {
		return I18N.COMMON.yes;
	} else {
		return I18N.COMMON.no;	
	}
}
function renderOperStatus(value, p, record){
	if (record.data.ifAdminStatus == '1' && record.data.ifOperStatus == '1')  {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/admin.gif" border=0 align=absmiddle class="nm3kTip">', I18N.COMMON.on);	
	} else {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/noadmin.gif" border=0 align=absmiddle class="nm3kTip">', I18N.COMMON.off);	
	}
}

function renderOpeartion(value, cellmate, record){
	var cmcPortId = record.data.cmcPortId;
	var channelId = record.data.channelId;
	var channelIndex = record.data.channelIndex;
	return  String.format("<a href='javascript:;' onclick='openBaseInfo({0},{1},{2})'>"+I18N.CMC.label.edit+"</a> ",cmcPortId,channelId,channelIndex);
}

function openBaseInfo(cmcPortId,channelId,channelIndex){
	//+'&productType='+productType+'&cmcMac='+cmcMac
	window.top.createDialog('cmtsUpStreamConfig', I18N.text.upChannelBaseInfo, 800, 500, 
			'/cmts/channel/showUpStreamBaseInfoConfig.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId+'&channelId='+channelId+"&channelIndex="+channelIndex, null, true, true);
}

function renderCcerRate(value,p,record){
	if(record.data.ifAdminStatus == STATUS_UP){
		var ccerRate = record.data.ccerRate
		return value + "/" + ccerRate + "%";
	}else{
		return ' - ';
	}
}

function renderUcerRate(value,p,record){
	if(record.data.ifAdminStatus == STATUS_UP){
		var ucerRate = record.data.ucerRate
		return value + "/" + ucerRate + "%";
	}else{
		return ' - ';
	}
}
//Modify by Victor@20160823把ifName排序改为按cmcPortId进行排序，展示还是ifName
function createMoreInfoList(){
	var w = $(window).width() - 30;
	var h = $(window).height() - 100;
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var baseColumns = [	
	    //sm,
		{header: I18N.CHANNEL.channelName, width: 70,sortable: true, align: 'center', dataIndex: 'cmcPortId', renderer: renderIfName},
       	//{header: I18N.CHANNEL.preEqEnable, width: 80, sortable:false, align: 'center', hidden:true, dataIndex: 'channelPreEqEnable',renderer: rendererAdminUpOrDown},
     	//{header: I18N.CHANNEL.totalCmNum, width: 60, sortable: false, align: 'center', dataIndex: 'cmcChannelTotalCmNum'},	
		//{header: I18N.CHANNEL.onlineCmNum, width: 70, sortable: false, align: 'center', dataIndex: 'cmcChannelOnlineCmNum'},
		{header: I18N.CHANNEL.frequency, width: 60, sortable: false, align: 'center', dataIndex: 'docsIfUpChannelFrequencyForunit'},
		{header: I18N.CMC.label.bandwidth, width: 60, sortable:false, align : 'center', dataIndex: 'docsIfUpChannelWidthForunit'},
		{header: I18N.CHANNEL.snr, width: 60, sortable:false, align : 'center', dataIndex: 'docsIfSigQSignalNoiseForunit'},
        {header: I18N.CHANNEL.power, width: 60, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'docsIf3SignalPowerForunit'},
       	{header: I18N.CHANNEL.channelType, width: 60, sortable:false, align: 'center', dataIndex: 'docsIfUpChannelTypeName'},
       	//{header: I18N.CHANNEL.extChannelMode, width: 60, sortable:false, align: 'center', dataIndex: 'channelExtModeName'},
		{header: I18N.CHANNEL.modulationProfile, width: 130, sortable:false, align: 'center', dataIndex: 'docsIfUpChannelModulationProfileName'/* , renderer:showProfile */},
		{header: I18N.CCMTS.channel.adminStatus, width: 60, sortable:false, align: 'center', dataIndex: 'ifAdminStatus',renderer: renderAdminStatus},	
		{header: I18N.CCMTS.channel.status, width: 60, sortable:false, align: 'center', dataIndex: 'ifOperStatus',renderer: renderOperStatus},
		{header: I18N.CHANNEL.operation, width: 120, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'op', renderer: renderOpeartion}  
		//{header: I18N.CHANNEL.rangingBackoffStart, width: 60, sortable: false, align: 'center',/* hidden:true, */ dataIndex: 'channelRangingBackoffStart'},
		//{header: I18N.CHANNEL.rangingBackoffEnd, width: 60, sortable:false, align : 'center',/* hidden:true, */ dataIndex: 'channelRangingBackoffEnd'},
       	//{header: I18N.CHANNEL.txBackoffStart, width: 60, sortable:false, align: 'center',/* hidden:true, */ dataIndex: 'channelTxBackoffStart'},
       	//{header: I18N.CHANNEL.txBackoffEnd, width: 60, sortable:false, align: 'center',/* hidden:true, */ dataIndex: 'channelTxBackoffEnd'},
       	//{header: I18N.CHANNEL.activeCodes, width: 70, sortable:false, align: 'center',/* hidden:true, */ dataIndex: 'channelScdmaActiveCodes',renderer:renderOnlySCDMAParam},	
		//{header: I18N.CHANNEL.codesPerSlot, width: 120, sortable: false, align: 'center',/* hidden:true, */ dataIndex: 'channelScdmaCodesPerSlot',renderer:renderOnlySCDMAParam},
		//{header: I18N.CHANNEL.frameSize, width: 120, sortable:false, align : 'center',/* hidden:true, */ dataIndex: 'channelScdmaFrameSize',renderer:renderOnlySCDMAParam}
		//{header: I18N.CHANNEL.channelStatus, width: 80, sortable:false, align : 'center', dataIndex: 'docsIfUpChannelStatusName'},
		];

	baseStore = new Ext.data.JsonStore({
	    url: ('/cmts/channel/getUpStreamBaseInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    fields: ['cmcPortId','channelId','docsIfUpChannelFrequencyForunit','docsIfUpChannelWidthForunit','docsIfSigQSignalNoiseForunit','docsIf3SignalPowerForunit', 'docsIfUpChannelModulationProfileName','ifSpeedForunit', 
	    		'channelExtModeName',
	    		'ifMtu', 'ifAdminStatus','ifOperStatus', 'channelSlotSize', 'ifDescr', 'ifName', 'channelTxTimingOffset', 'channelRangingBackoffStart', 
		     	    'channelRangingBackoffEnd', 'channelTxBackoffStart', 'channelTxBackoffEnd', 'channelScdmaActiveCodes', 'channelScdmaCodesPerSlot', 
		     	    'channelScdmaFrameSize','channelScdmaHoppingSeed', 'docsIfUpChannelTypeName', 'channelCloneFrom', 'docsIfUpChannelStatusName', 'channelPreEqEnable',
		     	    'cmcChannelTotalCmNum', 'cmcChannelOnlineCmNum', 'channelIndex', 'channelFrequency',
		     	    'channelWidth']
	});
	baseStore.setDefaultSort('cmcPortId', 'ASC');
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	
	var h2 = $(window).height() - 110;
	baseGrid = new Ext.grid.GridPanel({
		id: 'baseGridContainer', 
		cls: 'normalTable clearBoth',
		title: I18N.text.upChannelBaseInfo,
		border: true, 
		store: baseStore, 
		cm: baseCm,
		region:'center',
		renderTo:'baseGridPanel',
		height: h2,
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
		baseStore.load();
		
}

function createQualityList(){
	var w = $(window).width() - 30;
	var h = $(window).height() - 110;
	var qualityColumns = [
		{header: I18N.CHANNEL.channel, width: parseInt(w*7/55), sortable: true, align: 'center', dataIndex: 'ifName'},
		//{header: I18N.CHANNEL.includesContention,width:parseInt(w*2/11),sortable:false, align: 'center', dataIndex: 'docsIfSigQIncludesContention' , renderer: renderSigQIncludesContention },
		//{header: I18N.CHANNEL.unerroreds, width: parseInt(w*2/11),sortable:false, align: 'center', dataIndex: 'sigQCorrecteds'},
		{header: I18N.CHANNEL.correcteds, width: parseInt(w*2/11),sortable:false, align : 'center', dataIndex: 'sigQCorrecteds',renderer:renderCcerRate},
       	{header: I18N.CHANNEL.uncorrectables, width: parseInt(w*2/11), sortable:false, align: 'center', dataIndex: 'sigQUncorrectables',renderer:renderUcerRate},
       	{header: I18N.CHANNEL.snr, width:parseInt(w*7/55), sortable:false, align: 'center', dataIndex: 'sigQSignalNoiseForunit'}
	];
	qualityStore = new Ext.data.JsonStore({
	    url: ('/cmts/channel/getUpStreamQualityInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    //remoteSort: true, //是否支持后台排序
	    fields: ['cmcChannelId', 'ifDescr', 'ifName', 'ifAdminStatus','sigQCorrecteds','sigQUncorrectables','ccerRate','ucerRate','sigQSignalNoiseForunit']
	});
	
	qualityStore.setDefaultSort('ifDescr', 'ASC');
	
	var qualityCm = new Ext.grid.ColumnModel(qualityColumns);
	qualityGrid = new Ext.grid.GridPanel({
		id: 'qualityGridContainer', 
		cls: 'normalTable clearBoth',
		title: I18N.text.upChannelSignalQuality,
		height: $(window).height()-110,
		border: true, 
		store: qualityStore, 
		cm: qualityCm,
		renderTo:'baseGridPanel2',
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	qualityStore.load();
	
}
Ext.onReady(function () {
	createMoreInfoList();
	createQualityList();
	
	//点击从设备上获取；
	$("#btn1").click(function(){
		if(currentTab > 0){
			window.refreshSignalInfo();
		}else{
			window.refreshBaseinfo();
		}
		
	});

	var tab1 = new Nm3kTabBtn({
	    renderTo:"putBtn",
	    callBack:"showOne",
	    tabs:["@text.baseInfo@","@text.signalQuality@"]
	});
	tab1.init();
	
	$(window).resize(function(){
		autoHeight();
	})
	
});

function refreshBaseinfo(){
	window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.text.refreshUpchannelTip,'waitingMsg','ext-mb-waiting');
	$.ajax({
		url: '/cmc/channel/refreshUpChannelBaseInfo.tv?cmcId=' + cmcId+'&productType='+productType,
	    type: 'post',
	    success: function(response) {
	  	    if(response=="true"){
	  	    	onRefreshClick()  	    		
	  	    	//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshSuccessTip);
	  	    	window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">' + I18N.text.refreshSuccessTip + '</b>'
	   			});
			}else{
				window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
			}
		}, error: function(response) {
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
		}, cache: false
	}); 
}
function refreshSignalInfo(){
	window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.text.refreshUpchannelTip,'waitingMsg','ext-mb-waiting');
	$.ajax({
		url: '/cmc/channel/refreshUpChannelSignalQualityInfo.tv?cmcId=' + cmcId+'&productType='+productType ,
	    type: 'post',
	    success: function(response) {	       
	    	if(response=="true"){
  	    		qualityStore.reload();
				window.parent.closeWaitingDlg();
				//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshSuccessTip);
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">' + I18N.text.refreshSuccessTip + '</b>'
	   			});
			}else{
				window.parent.closeWaitingDlg();
				window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
			}
		}, error: function(response) {
			window.parent.closeWaitingDlg();
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
		}, cache: false
	}); 
}
function autoHeight(){
	var h = $(window).height() - 120;	
	var w = $(window).width()- 20;
	if(baseGrid !=null && qualityGrid !=null){
		baseGrid.setHeight(h);
		baseGrid.setWidth(w);
		qualityGrid.setHeight(h);
		qualityGrid.setWidth(w);
	}
}

function showOne(num){
	currentTab = num
	var $jsShow = $(".jsShow")
	$jsShow.css("display","none");
	$jsShow.eq(num).fadeIn(function(){
		autoHeight();	
		$(window).resize();
	});
	
}
function correctGrid(){
	var w = $(window).width()- 20;
	var h = $(window).height() - 120;
	if(w>20 && h>20 && baseGrid !=null && qualityGrid !=null){
		qualityGrid.setWidth(w);
		qualityGrid.setHeight(h);
		baseGrid.setWidth(w);
		baseGrid.setHeight(h);
		$(window).resize();
	};//end if;
}

function authLoad(){
    if(!refreshDevicePower){
        $("#btn1").attr("disabled",true);
    }
}
</script>
</head>
	<body class="whiteToBlack"
		style="width: 100%; height: 100%; overflow: hidden;" onload="authLoad()">
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>
		<div class="edgeAndClearFloat">
			<div id="putBtn"></div>
			<ol class="upChannelListOl pL20" id="upChannelListOl">
				<li><a href="javascript:;" class="normalBtn" id="btn1"
					name=""><span><i class="miniIcoEquipment"></i>@text.refreshData@</span></a></li>
			</ol>
		</div>
		<div id="baseGridPanel" class="jsShow clearBoth edge10"></div>
		<div id="baseGridPanel2" class="jsShow clearBoth edge10"
			></div>

		<dl class="legent" style="top: 90px;">
			<dt class="mR5">@CMC.Legend@</dt>
			<dd>
				<img src="../../images/correct.png" border="0" alt="" />
			</dd>
			<dd class="mR10">@CMC.select.open@</dd>
			<dd>
				<img src="../../images/wrong.png" border="0" alt="" />
			</dd>
			<dd>@CMC.button.close@</dd>
		</dl>

	</body>
</Zeta:HTML>