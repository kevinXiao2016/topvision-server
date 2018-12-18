<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
<script type="text/javascript">
var cmcId = ${ cmcId };
var productType = ${ productType };
var baseStore=null;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var maxChanPowerLUT = [600, 600, 560, 540, 520, 510, 500, 490, 490, 480, 480, 470, 470, 460, 460, 450, 450];
function onRefreshClick() {
	baseStore.reload();
	$("#openChannels").attr("disabled", true);
	$("#closeChannels").attr("disabled", true);
}
function renderOpeartion(value, cellmate, record){
    var cmcPortId = record.data.cmcPortId;
    return String.format("<img src='/images/edit.gif' " + 
            "onclick='openBaseInfo(\"{0}\")'/>&nbsp;&nbsp;&nbsp;&nbsp;" , 
            cmcPortId);
}
function refreshBase(){
	window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.text.refreshDownchannelTip,'waitingMsg','ext-mb-waiting');
	$.ajax({
		url:"/cmc/channel/refreshDownChannelBaseInfo.tv?cmcId=" + cmcId+'&productType='+productType,
		type:"post",
		success:function (response){
			if(response=="true"){
				onRefreshClick();
				//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshSuccessTip);
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">' + I18N.text.refreshSuccessTip + '</b>'
	   			});
			}else{
				window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
			}
		},error: function(response) {
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
		}, cache: false
	});	
}
function renderIfName(value, p, record){
	return record.data.ifName;
}
function renderAdminStatus(value, p, record){
	if (record.data.ifAdminStatus == '1') {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/admin.gif" border=0 align=absmiddle class="nm3kTip">',
			I18N.COMMON.startTitle);		
	} else {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/noadmin.gif" border=0 align=absmiddle class="nm3kTip">',
			I18N.SYSTEM.Disable);	
	}
}
function renderOperStatus(value, p, record){
	if (record.data.ifAdminStatus == '1' && record.data.ifOperStatus == '1')  {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/admin.gif" border=0 align=absmiddle class="nm3kTip">', I18N.COMMON.on);	
	} else {
		return String.format('<img nm3kTip="{0}" src="/images/network/port/noadmin.gif" border=0 align=absmiddle  class="nm3kTip">', I18N.COMMON.off);	
	}
}

function openBaseInfo(cmcPortId){
	window.top.createDialog('downStreamConfig', I18N.text.downChannelBaseInfo, "small_16_9", 310, '/cmts/channel/showDownStreamConfigInfo.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId + '&productType='+productType, null, true, true);
}

function renderOpeartion(value, cellmate, record){
	var cmcPortId = record.data.cmcPortId;
	var channelId = record.data.docsIfDownChannelId;
	var channelIndex = record.data.channelIndex;
	return  String.format("<a href='javascript:;' onclick='openBaseInfo({0},{1},{2})'>"+I18N.CMC.label.edit+"</a> ",cmcPortId,channelId,channelIndex);
}

function openBaseInfo(cmcPortId,channelId,channelIndex){
	window.top.createDialog('cmtsDownStreamConfig', I18N.text.downChannelBaseInfo, 800, 500, 
			'/cmts/channel/showDownStreamConfigInfo.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId+'&channelId='+channelId+"&channelIndex="+channelIndex, null, true, true);
}
//Modify by Victor@20160823把ifName排序改为按cmcPortId进行排序，展示还是ifName
Ext.onReady(function () {
	var w = $(window).width() - 30;
	var h = $(window).height() - 100;
	var baseColumns = [
	    {header: I18N.CHANNEL.channelName, width: parseInt(w/14), sortable: true, align: 'center', dataIndex: 'cmcPortId',renderer: renderIfName},
		{header: I18N.CHANNEL.frequency, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'docsIfDownChannelFrequencyForunit'},
		{header: I18N.CMC.label.bandwidth, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'docsIfDownChannelWidthForunit'},	
		{header: I18N.CCMTS.channel.adminStatus, width: parseInt(w/14), sortable: false, align: 'center', dataIndex: 'ifAdminStatus',renderer: renderAdminStatus},	
		{header: I18N.CCMTS.channel.status, width: parseInt(w/14), sortable: false, align: 'center', dataIndex: 'ifOperStatus',renderer: renderOperStatus},		
       	{header: I18N.CHANNEL.modulationType, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'docsIfDownChannelModulationName'},
       	{header: I18N.CHANNEL.interleave, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'docsIfDownChannelInterleaveName'},
       	{header: I18N.CHANNEL.power, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'docsIfDownChannelPowerForunit'},	
		{header: I18N.CHANNEL.annex, width: parseInt(w/8), sortable: false, align: 'center', dataIndex: 'docsIfDownChannelAnnexName'},
		{header: I18N.CHANNEL.operation, width: 120, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'op', renderer: renderOpeartion}  
	];

	baseStore = new Ext.data.JsonStore({
	    url: ('/cmts/channel/getDownStreamBaseInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    fields: ['cmcPortId','docsIfDownChannelId', 'ifName','docsIfDownChannelFrequencyForunit', 'docsIfDownChannelWidthForunit', 'ifSpeedForunit', 'ifDescr', 'ifMtu', 'ifAdminStatus', 'ifOperStatus',
	              'docsIfDownChannelModulationName', 'docsIfDownChannelInterleaveName', 'docsIfDownChannelPowerForunit', 'docsIfDownChannelAnnexName',
	              'cmcChannelTotalCmNum', 'cmcChannelOnlineCmNum', 'channelIndex', 'docsIfDownChannelFrequency',
	              'docsIfDownChannelPower', 'docsIfDownChannelWidth']
	});
	baseStore.setDefaultSort('cmcPortId', 'ASC');
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		cls: 'normalTable',
		title: I18N.text.downChannelList,
		id: 'extbaseGridContainer', 
		margins: "0px 10px 10px 10px",
		border: true, 
		store: baseStore, 
		cm: baseCm,
		region:'center',
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
		baseStore.load();
		
		//点击从设备上获取；
		$("#btn1").click(function(){
			window.refreshBase();
		});
		
		new Ext.Viewport({
		    layout: 'border',
		    items: [{
		        region: 'north',
		        height:90,
		        border: false,
		        contentEl: 'topPart'
		    },baseGrid]
		});
		
});

function authLoad(){
    if(!refreshDevicePower){
        $("#btn1").attr("disabled",true);
    }
}
</script>
</head>
<body class="whiteToBlack" onload="authLoad()">
	<div id="topPart">
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>
		<div class="edgeAndClearFloat">
			<ol class="upChannelListOl" id="upChannelListOl">
				<li><a href="javascript:;" class="normalBtn" id="btn1" name="@CMC.ObtainFromTheEquipment@"><span><i class="miniIcoEquipment"></i>@text.refreshData@</span></a></li>
			</ol>
		</div>
	</div>	
	<dl class="legent" style="top:90px;">
		<dt class="mR5">@CMC.Legend@</dt>
		<dd><img src="../../images/correct.png" border="0" alt="" /></dd>
		<dd class="mR10">@CMC.select.open@</dd>
		<dd><img src="../../images/wrong.png" border="0" alt="" /></dd>
		<dd>@CMC.button.close@</dd>						
	</dl>
</body>
</Zeta:HTML>
