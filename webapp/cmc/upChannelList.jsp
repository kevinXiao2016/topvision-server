<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<link rel="stylesheet" type="text/css" href="/css/ext-formhead-linefeed.css" />

<style type="text/css">
body,html{ height:100%;}
#openLayer{ width:100%; height:100%; position:absolute; z-index:999; top:0; left:0; display:none;}
</style>
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css" /> 
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-versioncontrol.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/cmc/rendererFunction.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript">
vcEntityKey = 'cmcId';
var STATUS_UP = 1;
var STATUS_DOWN = 2;
var qualityGrid;
var cmcId = <s:property value="cmcId"/>;
var cmcMac = '<s:property value="cmcMac"/>';
var entityId = '<s:property value="entityId"/>';
var productType ='<s:property value="productType"/>';
Ext.BLANK_IMAGE_URL = "/images/s.gif";
var baseStore=null;
var qualityStore=null;
var baseGrid = null;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var channel1 = null;
var channel2 = null;
var channel3 = null;
var channel4 = null;
// used by seemore(),for channel detail
function channelDetailInfoLoad(){
	var data = baseStore.data.items;
	$.each(data,function(i,v){
		if(v.data.channelId==1){
			channel1 = v;
		}else if(v.data.channelId==2){
			channel2 = v;
		}else if(v.data.channelId==3){
			channel3 = v;
		}else if(v.data.channelId==4){
			channel4 = v;
		}
	});
}
function renderSignalNoiseForunit(value,p,record){
	if(record.data.ifAdminStatus == STATUS_UP){
		return value;
	}else{
		return ' - ';
	}
}
function onRefreshClick() {
	baseStore.reload();
	qualityStore.reload();
	//每次刷新时需要更新表格头部的选中状态
	var gridEl = baseGrid.getEl();//得到表格的EL对象
	var hd = gridEl.select('div.x-grid3-hd-checker');
	hd.removeClass('x-grid3-hd-checker-on');
	
	$("#openChannels").attr("disabled", true);
	$("#closeChannels").attr("disabled", true);
}

//updated by flackyang 2013-09-09
function renderOpeartion(value, cellmate, record){
	var cmcPortId = record.data.cmcPortId;
	var channelId = record.data.channelId;
	var channelIndex = record.data.channelIndex;
   /*  在此版本中屏蔽自动跳频功能
   return  String.format("<a href='javascript:;' onclick='seeMore({1})'></a>" +详细
    " / <a href='javascript:;' onclick='openBaseInfo({0},{1},{2})'></a> " + 修改
    "/ <a href='javascript:;' onclick='openHopHis({1},{2})'></a>",cmcPortId,channelId,channelIndex); 查看跳频历史
   */
	 return  String.format("<a href='javascript:;' onclick='seeMore({1})'>"+I18N.CCMTS.ccmtsMessage+"</a>" +
		    " / <a href='javascript:;' onclick='openBaseInfo({0},{1},{2})'>"+I18N.CMC.label.edit+"</a> ",cmcPortId,channelId,channelIndex);
}

//查看信道跳频历史记录  add by flackyang 2013-09-09  在此版本中屏蔽自动跳频功能
/* 
function openHopHis(channelId,channelIndex){
	window.top.createDialog('chnlHopHis', I18N.CMC.GP.chnlHosHis, 800, 500, 
			'/ccmtsspectrumgp/showGroupHopHisPage.tv?entityId=' + entityId+'&channelId='+channelId+'&cmcMac='+cmcMac+"&channelIndex="+channelIndex, null, true, true);
} 
*/

// activeCode,minislotcode,scdmaframeSize，只能是SCDMA模式下起作用，其他模式则显示'-
function renderOnlySCDMAParam(value,p,record){
	if(record.data.docsIfUpChannelTypeName=='SCDMA'){
		return value;
	}else{
		return '-';
	}
}

//管理状态下的 关闭 和  开启  图片;
function renderAdminStatus(value, p, record){
	if (record.data.ifAdminStatus == '1') {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle>',
			I18N.COMMON.on);		
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle>',
			I18N.COMMON.off);	
	}
}

function rendererAdminUpOrDown(value, p, record){
	if (record.data.channelPreEqEnable == '1') {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
			I18N.COMMON.on);		
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
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

//工作状态下 的  开启  和  关闭  图片;
function renderOperStatus(value, p, record){
	if (record.data.ifAdminStatus == '1' && record.data.ifOperStatus == '1')  {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle>', I18N.COMMON.on);	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle>', I18N.COMMON.off);	
	}
}
function openProfile(profileId){
	window.top.createDialog('showModulationConfigDlg', I18N.CMC.label.relatedModFile, 600, 320, 'cmc/channel/showModulationConfigByProfileId.tv?cmcId=' + cmcId+"&profileId="+profileId, null, true, true);
}
function showProfile(value, cellmeta, record, rowIndex, columnIndex, store){
	var fieldName = baseGrid.getColumnModel().getDataIndex(columnIndex);
	var profileId = record.get(fieldName);
 
   	if(profileId == "0"){
   		str = "QPSK_Fair" ;  	
   	}else if(profileId == "1"){
   		str = "QAM16_Good";
   	}else if(profileId == "2"){
   		str = "QAM64_Best";
   	}else if(profileId == "3"){
   		str = "QAM256_Temp";
   	}else
   		str = profileId;
   	return str;
}

//updated by flackyang 2013-09-09
function openBaseInfo(cmcPortId,channelId,channelIndex){
	window.top.createDialog('upStreamConfig', I18N.text.upChannelBaseInfo, 800, 500, 
			'cmc/channel/showUpStreamBaseInfoConfig.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId+'&channelId='+channelId+'&productType='+productType+'&cmcMac='+cmcMac+"&channelIndex="+channelIndex, null, true, true);
}

//updated by flackyang 2013-09-09
function opeartionRender(value, cellmate, record){
	var cmcPortId = record.get('cmcPortId');
	var channelId = record.get('channelId');
	var channelIndex = record.get('channelIndex');
	return String.format("<a href='#' onclick='openBaseInfo({0},{1},{2})' class='jsChannelName'>"+I18N.CHANNEL.channel + channelId + 
			"</a>", cmcPortId,channelId,channelIndex);
}

function channelRender(value, cellmate, record){
	return I18N.CHANNEL.channel + value;
}

function batchOpenChannels(){
	var selections = baseGrid.getSelectionModel().getSelections();
	var channelIndexs = new Array();
	for(var i = 0; i < selections.length; i++){
		channelIndexs[i] = selections[i].data.channelIndex;
	}
	if(checkChannelFrequency(baseGrid.store.data.items,selections) != true){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.batchOpenUpFrequencyInvalid	);
		return;
	}
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.tip.confirmOpenChannels, function (type) {
		if(type=="ok"){
			setChannelsStatus("/cmc/channel/batchOpenChannels.tv?cmcId="+cmcId,channelIndexs);
		}
	});
}

function batchCloseChannels(){
	var selections = baseGrid.getSelectionModel().getSelections();
	var channelIndexs = new Array();
	for(var i = 0; i < selections.length; i++){
		channelIndexs[i] = selections[i].data.channelIndex;
	}
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.tip.confirmCloseChannels, function (type) {
		if(type=="ok"){
			setChannelsStatus("/cmc/channel/batchCloseChannels.tv?cmcId="+cmcId,channelIndexs);
		}
	});
}
function setChannelsStatus(url,channelIndexs){	
	window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
	$.ajax({
		url:url,
		type:"post",
		data:{channelIndexs: channelIndexs},
		dataType:"json",
		success:function (response){
			if(response.message == "success"){
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
	   				title: I18N.RECYLE.tip,
	   				html: '<b class="orangeTxt">' + I18N.CMC.tip.setSuccess + '</b>'
	   			});
			}else{
				window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.setfailedChannels + response.failureList);
			}
			onRefreshClick();
		},error: function(response) {
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.setfailedChannels + response.failureList);
		}, cache: false		
	});
}
//修改开启按钮的可操作状态
function changeOpenButtonStatus(selections){
	var status = true;
	if(selections.length != 0){
		for(var i = 0; i < selections.length; i++){
			if(selections[i].data.ifAdminStatus != 1){
				status = false;
			}
		}
	}else{
		status = true;
	}
	
	//操作权限
	if(operationDevicePower){
		$("#openChannels").attr("disabled", status);
	}
}
//修改关闭按钮的可操作状态
function changeCloseButtonStatus(selections){
	var status = true;
	if(selections.length != 0){
		for(var i = 0; i < selections.length; i++){
			if(selections[i].data.ifAdminStatus == 1){
				status = false;
			}
		}
	}else{
		status = true;
	}
	//操作权限
	if(operationDevicePower){
		$("#closeChannels").attr("disabled", status);
	}
}
//Check Frequency
function checkChannelFrequency(data,selections){
	var max = 0;
	var min = 1000000000;
	for(var i = 0; i < selections.length; i++){
	    if(selections[i].data.ifAdminStatus == 1){
	        continue;
	    }
		//验证与已经开启的通道的频点不重合
		for(var j = 0; j < data.length; j++){
			if(data[j].data.ifAdminStatus == 1){
				var cutValue = Math.abs(selections[i].data.channelFrequency - 
						data[j].data.channelFrequency);
				var widthContainer = data[j].data.channelWidth/2 + selections[i].data.channelWidth/2;
				if(cutValue < widthContainer){
					return false;
				}
			}
		}
		//验证一套开启通道的频点之间不重合
		for(var j = 0; j < selections.length; j++){
			if( j != i && selections[j].data.ifAdminStatus != 1){
				var cutValue = Math.abs(selections[i].data.channelFrequency - 
						selections[j].data.channelFrequency);
				var widthContainer = selections[j].data.channelWidth/2 + selections[i].data.channelWidth/2;
				if(cutValue < widthContainer){
					return false;
				}
			}
		}
	}
	return true;
}
/*********************************************************************
 *onMonuseDown onHdMouseDown
 *Copy From oltAlert.jsp
 *modify by huangdongsheng
 *********************************************************************/
function onMonuseDown(e, t){
	if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		// mouseHandled flag check for a duplicate selection (handleMouseDown) call
		if(!this.mouseHandled && row){
			var gridEl = this.grid.getEl();//得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if(this.isSelected(index)){
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				//判断头部的全选CheckBox框是否选中，如果是，则删除
				if(isChecked){
					hd.removeClass('x-grid3-hd-checker-on');
				}
			}else{
				this.selectRow(index, true);
				//判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
				if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
					hd.addClass('x-grid3-hd-checker-on');
				};
			}
		}
	}
	changeOpenButtonStatus(this.grid.getSelectionModel().getSelections());
	changeCloseButtonStatus(this.grid.getSelectionModel().getSelections());
	this.mouseHandled = false;
}

function onHdMouseDown(e, t){
	/**
	*大家觉得上面重写的代码应该已经实现了这个功能了，可是又发现下面这行也重写了
	*由原来的t.className修改为t.className.split(' ')[0]
	*为什么呢？这个是我在快速点击头部全选CheckBox时，
	*操作过程发现，有的时候x-grid3-hd-checker-on这个样式还没有删除或者多一个空格，结果导致下面这个判断不成立
	*去全选或者全选不能实现
	*/
	if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
	    e.stopEvent();
	    var hd = Ext.fly(t.parentNode);
	    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
	    if(isChecked){
	        hd.removeClass('x-grid3-hd-checker-on');
	        this.clearSelections();
	    }else{
	        hd.addClass('x-grid3-hd-checker-on');
	        this.selectAll();
	    }
	}
	changeOpenButtonStatus(this.grid.getSelectionModel().getSelections());
	changeCloseButtonStatus(this.grid.getSelectionModel().getSelections());
}

Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});

/**
 * 覆写，解决点击行后全选没取消的情况
 */
Ext.override(Ext.grid.GridView,{  
    
    onRowSelect : function(row){  
        this.addRowClass(row, "x-grid3-row-selected"); 
        this.removeRowClass(row, "yellow-row");
        this.removeRowClass(row, "red-row");
        this.removeRowClass(row, "white-row");
        var selected = 0;  
        var len = this.grid.store.getCount();  
        for(var i = 0; i < len; i++){  
            var r = this.getRow(i);  
            if(r){  
               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
            }  
        }  
          
        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
          
        if (selected == len && !hd.hasClass('x-grid3-hd-checker-on')) {  
             hd.addClass('x-grid3-hd-checker-on');   
        }  
    },  
  
    onRowDeselect : function(row){  
        this.removeRowClass(row, "x-grid3-row-selected");
        this.removeRowClass(row, "yellow-row");
        this.removeRowClass(row, "red-row");
        this.removeRowClass(row, "white-row");
            var selected = 0;  
            var len = this.grid.store.getCount();  
            for(var i = 0; i < len; i++){  
                var r = this.getRow(i);  
                if(r){  
                   if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
                }  
            }  
            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
              
            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
                 hd.removeClass('x-grid3-hd-checker-on');   
            }  
    }  
});
//基本信息;
function createMoreInfoList(){
	var w = document.body.clientWidth - 24;//多一个滚动条的距离;	
	var h = document.body.clientHeight - 120;
	h = $(window).height() - 120
	if(h < 200){h = 200;}
	
	var sm = new Ext.grid.CheckboxSelectionModel({listeners:{
	    'rowselect':function(sm,rowIndex,record){
	        changeOpenButtonStatus(baseGrid.getSelectionModel().getSelections());
	    	changeCloseButtonStatus(baseGrid.getSelectionModel().getSelections());
	    },
		'rowdeselect':function(sm,rowIndex,record){
		    changeOpenButtonStatus(baseGrid.getSelectionModel().getSelections());
	    	changeCloseButtonStatus(baseGrid.getSelectionModel().getSelections());
	    }
	}}); 
	
	var baseColumns = [	
	    sm,
	    //{header: 'cmcPortId', width: 100, sortable: false, align: 'center',menuDisabled:true, hidden:true, dataIndex: 'cmcPortId'},	
		{header: I18N.CHANNEL.channel, width: 60,sortable: false, align: 'center',menuDisabled:true, dataIndex: 'channelId', renderer: opeartionRender},
       	//{header: I18N.CHANNEL.preEqEnable, width: 80, sortable:false, align: 'center',menuDisabled:true, hidden:true, dataIndex: 'channelPreEqEnable',renderer: rendererAdminUpOrDown},
		{header: I18N.CHANNEL.frequency, width: 60, sortable: false, align: 'center',menuDisabled:true, dataIndex: 'docsIfUpChannelFrequencyForunit'},
		{header: I18N.CMC.label.bandwidth, width: 60, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'docsIfUpChannelWidthForunit'},
		{header: I18N.CHANNEL.snr, width: 60, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'docsIfSigQSignalNoiseForunit',renderer:renderSignalNoiseForunit},
		{header: I18N.CHANNEL.power, width: 60, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'docsIf3SignalPowerForunit'},		
       	{header: I18N.CHANNEL.channelType, width: 60, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'docsIfUpChannelTypeName'},
		{header: I18N.CHANNEL.modulationProfile, width: 130, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'docsIfUpChannelModulationProfileName'/* , renderer:showProfile */},
		{header: I18N.CCMTS.channel.adminStatus, width: 60, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ifAdminStatus',renderer: renderAdminStatus},	
		{header: I18N.CCMTS.channel.status, width: 60, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ifOperStatus',renderer: renderOperStatus},		
		//{header: I18N.CHANNEL.rangingBackoffStart, width: 60, sortable: false, align: 'center',menuDisabled:true,/* hidden:true, */ dataIndex: 'channelRangingBackoffStart'},
		//{header: I18N.CHANNEL.rangingBackoffEnd, width: 60, sortable:false, align : 'center',menuDisabled:true, /* hidden:true, */ dataIndex: 'channelRangingBackoffEnd'},
       	//{header: I18N.CHANNEL.txBackoffStart, width: 60, sortable:false, align: 'center',menuDisabled:true,/* hidden:true, */ dataIndex: 'channelTxBackoffStart'},
       	//{header: I18N.CHANNEL.txBackoffEnd, width: 60, sortable:false, align: 'center',menuDisabled:true,/* hidden:true, */ dataIndex: 'channelTxBackoffEnd'},
       	//{header: I18N.CHANNEL.activeCodes, width: 70, sortable:false, align: 'center',menuDisabled:true,/* hidden:true, */ dataIndex: 'channelScdmaActiveCodes'},	
		//{header: I18N.CHANNEL.codesPerSlot, width: 120, sortable: false, align: 'center',menuDisabled:true,/* hidden:true, */ dataIndex: 'channelScdmaCodesPerSlot'},
		//{header: I18N.CHANNEL.frameSize, width: 120, sortable:false, align : 'center',menuDisabled:true, /* hidden:true, */ dataIndex: 'channelScdmaFrameSize'},
		//{header: I18N.CHANNEL.channelStatus, width: 80, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'docsIfUpChannelStatusName'},
		{header: I18N.CHANNEL.operation, width: 120, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'op', renderer: renderOpeartion}  
		];

	baseStore = new Ext.data.JsonStore({
	    url: ('getUpStreamBaseInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    /*  listeners:{
	    	load : function(){
	    		extZebra();
	    	}
	    }, */
	    //remoteSort: true,//是否支持后台排序 
	    fields: ['cmcPortId','channelId','docsIfUpChannelFrequencyForunit','docsIfUpChannelWidthForunit','docsIfSigQSignalNoiseForunit','docsIf3SignalPowerForunit', 'docsIfUpChannelModulationProfileName','ifSpeedForunit', 
	    		//'useRatio',
	    		'ifMtu', 'ifAdminStatus','ifOperStatus', 'channelSlotSize', 'channelTxTimingOffset', 'channelRangingBackoffStart', 
		     	    'channelRangingBackoffEnd', 'channelTxBackoffStart', 'channelTxBackoffEnd', 'channelScdmaActiveCodes', 'channelScdmaCodesPerSlot', 
		     	    'channelScdmaFrameSize','channelScdmaHoppingSeed', 'docsIfUpChannelTypeName', 'channelCloneFrom', 'docsIfUpChannelStatusName', 'channelPreEqEnable',
		     	     'channelIndex', 'channelFrequency',
		     	    'channelWidth','channelExtMode','docsIfUpChannelPreEqEnableName']
	});
	//baseStore.setDefaultSort('channelId', 'ASC');
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		id: 'extmoreInfoGridContainer', 
		height: h,
		animCollapse: animCollapse, 
		trackMouseOver: trackMouseOver, 
		viewConfig: { forceFit:true},
		border: true, 
		store: baseStore, 
		sm:sm,
		cm: baseCm,title: I18N.text.upChannelBaseInfo,
		renderTo: 'moreInfodetail-div'});
	/* baseGrid.on('rowdblclick', function(baseGrid, rowIndex, e) {
		var record = baseGrid.getStore().getAt(rowIndex);
		//var fieldName = baseGrid.getColumnModel().getDataIndex(0);
		cmcPortId = record.get('cmcPortId');
    	//window.top.createDialog('upStreamConfig', I18N.text.upChannelBaseInfo, 650, 450, 'cmc/channel/showUpStreamBaseInfoConfig.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId, null, true, true);
    	window.top.createDialog('upStreamConfig', I18N.text.upChannelBaseInfo, "normal_16_9", 350, 'cmc/channel/showUpStreamBaseInfoConfig.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId+'&productType='+productType, null, true, true);
	}); */
	baseStore.load();
}

function createStaticList(){
	var w = document.body.clientWidth - 24;
	var h = document.body.clientHeight - 120;
	h = $(window).height() - 120
	if(h < 200){h = 200;}
	var staticColumns = [ 
		{header: I18N.CHANNEL.id, width: 70, sortable: false, align: 'center',menuDisabled:true, dataIndex: 'ctrId'},
		//私有MIB未作出 
		//{header: I18N.CHANNEL.totalCmNum, width: 100, sortable: false, align: 'center',menuDisabled:true, dataIndex: 'cmNumber'},	 
		{header: I18N.CHANNEL.totalMslots, width: 100, sortable: false, align: 'center',menuDisabled:true, dataIndex: 'ctrTotalMslots'},
		{header: I18N.CHANNEL.ucastGrantedMslots, width: 100, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'ctrUcastGrantedMslots'},
       	{header: I18N.CHANNEL.totalCntnMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrTotalCntnMslots'},
       	{header: I18N.CHANNEL.usedCntnMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrUsedCntnMslots'},
       	{header: I18N.CHANNEL.totalMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtTotalMslots'},	
		{header: I18N.CHANNEL.ucastGrantedMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtUcastGrantedMslots'},
		{header: I18N.CHANNEL.totalCntnMslots +'(64bit)', width: 100, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'ctrExtTotalCntnMslots'},
       	{header: I18N.CHANNEL.usedCntnMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtUsedCntnMslots'},
       	{header: I18N.CHANNEL.collCntnMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrCollCntnMslots'},
       	{header: I18N.CHANNEL.totalCntnReqMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrTotalCntnReqMslots'},	
		{header: I18N.CHANNEL.usedCntnReqMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrUsedCntnReqMslots'},
		{header: I18N.CHANNEL.collCntnReqMslots, width: 100, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'ctrCollCntnReqMslots'},
       	{header: I18N.CHANNEL.totalCntnReqDataMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrTotalCntnReqDataMslots'},
       	{header: I18N.CHANNEL.usedCntnReqDataMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrUsedCntnReqDataMslots'},
       	{header: I18N.CHANNEL.collCntnReqDataMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrCollCntnReqDataMslots'},	
		{header: I18N.CHANNEL.totalCntnInitMaintMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrTotalCntnInitMaintMslots'},
		{header: I18N.CHANNEL.usedCntnInitMaintMslots, width: 100, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'ctrUsedCntnInitMaintMslots'},
       	{header: I18N.CHANNEL.collCntnInitMaintMslots, width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrCollCntnInitMaintMslots'},
       	{header: I18N.CHANNEL.collCntnMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtCollCntnMslots'},
       	{header: I18N.CHANNEL.totalCntnReqMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtTotalCntnReqMslots'},	
		{header: I18N.CHANNEL.usedCntnReqMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtUsedCntnReqMslots'},
		{header: I18N.CHANNEL.collCntnReqMslots +'(64bit)', width: 100, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'ctrExtCollCntnReqMslots'},
       	{header: I18N.CHANNEL.totalCntnReqDataMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtTotalCntnReqDataMslots'},
       	{header: I18N.CHANNEL.usedCntnReqDataMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtUsedCntnReqDataMslots'},
       	{header: I18N.CHANNEL.collCntnReqDataMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtCollCntnReqDataMslots'},	
		{header: I18N.CHANNEL.totalCntnInitMaintMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtTotalCntnInitMaintMslots'},
		{header: I18N.CHANNEL.usedCntnInitMaintMslots +'(64bit)', width: 100, sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'ctrExtUsedCntnInitMaintMslots'},
       	{header: I18N.CHANNEL.collCntnInitMaintMslots +'(64bit)', width: 100, sortable:false, align: 'center',menuDisabled:true, dataIndex: 'ctrExtCollCntnInitMaintMslots'}
	];
	staticStore = new Ext.data.JsonStore({
	    url: ('getUpStreamStasticInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    //remoteSort: true,是否支持后台排序 
	    fields: ['ctrId','cmNumber', 'ctrTotalMslots', 'ctrUcastGrantedMslots', 'ctrTotalCntnMslots', 'ctrUsedCntnMslots', 'ctrExtTotalMslots', 'ctrExtUcastGrantedMslots', 
	             	'ctrExtTotalCntnMslots', 'ctrExtUsedCntnMslots', 'ctrCollCntnMslots', 'ctrTotalCntnReqMslots','ctrUsedCntnReqMslots','ctrCollCntnReqMslots',
	             	'ctrTotalCntnReqDataMslots','ctrUsedCntnReqDataMslots','ctrCollCntnReqDataMslots','ctrTotalCntnInitMaintMslots','ctrUsedCntnInitMaintMslots','ctrCollCntnInitMaintMslots',
		     	    'ctrExtCollCntnMslots','ctrExtTotalCntnReqMslots','ctrExtUsedCntnReqMslots','ctrExtCollCntnReqMslots','ctrExtTotalCntnReqDataMslots','ctrExtUsedCntnReqDataMslots',
		     	    'ctrExtCollCntnReqDataMslots','ctrExtTotalCntnInitMaintMslots','ctrExtUsedCntnInitMaintMslots', 'ctrExtCollCntnInitMaintMslots']});
	staticStore.setDefaultSort('ctrId', 'ASC');
	
	var staticCm = new Ext.grid.ColumnModel(staticColumns);
	staticGrid = new Ext.grid.GridPanel({id: 'extStaticGridContainer',  height: h-20,
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true,
		store: staticStore, cm: staticCm,title: I18N.text.upChannelStatisticInfo,
		renderTo: 'statisticInfo-div'});
	/* staticGrid.on('rowdblclick', function(staticGrid, rowIndex, e) {
    });  */ 		
	staticStore.load();
}

function createQualityList(){
	var w = document.body.clientWidth - 24;//减去16是为了多减去一个滚动条的距离;
	var h = document.body.clientHeight - 120;
	h = $(window).height() - 120
	if(h < 200){h = 200;}
	var qualityColumns = [
		{header: I18N.CHANNEL.channel, width: parseInt(w*7/55), sortable: false, align: 'center',menuDisabled:true, dataIndex: 'cmcChannelId',renderer: channelRender},
		//{header: I18N.CHANNEL.includesContention,width:parseInt(w*2/11),sortable:false, align: 'center',menuDisabled:true, dataIndex: 'docsIfSigQIncludesContentionName', renderer: renderSigQIncludesContention},
		//{header: I18N.CHANNEL.unerroreds, width: parseInt(w*2/11),sortable:false, align: 'center',menuDisabled:true, dataIndex: 'docsIfSigQUnerroredsForunit'},
		{header: I18N.CHANNEL.correcteds, width: parseInt(w*2/11),sortable:false, align : 'center',menuDisabled:true,  dataIndex: 'sigQCorrectedsForunit'},
       	{header: I18N.CHANNEL.uncorrectables, width: parseInt(w*2/11), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'sigQUncorrectablesForunit'},
       	{header: I18N.CHANNEL.unerroreds,width: parseInt(w*2/11), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'sigQUnerroredsForunit'},
       	{header: I18N.CHANNEL.snr, width:parseInt(w*7/55), sortable:false, align: 'center',menuDisabled:true, dataIndex: 'sigQSignalNoiseForunit',renderer:renderSignalNoiseForunit}
	];
	qualityStore = new Ext.data.JsonStore({
	    url: ('getUpStreamQualityInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    //remoteSort: true, //是否支持后台排序
	   /*  fields: ['cmcChannelId', 'docsIfSigQIncludesContentionName', 'docsIfSigQUnerroredsForunit', 'docsIfSigQCorrectedsForunit', 'docsIfSigQUncorrectablesForunit', 
	             'docsIfSigQSignalNoiseForunit', 'docsIfSigQMicroreflectionsForunit', 'docsIfSigQEqualizationData','docsIfSigQExtUnerroredsForunit', 'docsIfSigQExtCorrectedsForunit', 'docsIfSigQExtUncorrectablesForunit'] */
	    fields:['cmcChannelId','ifAdminStatus','sigQCorrectedsForunit','sigQUncorrectablesForunit','sigQSignalNoiseForunit','sigQUnerroredsForunit']
	
	});
	
	//qualityStore.setDefaultSort('cmcChannelId', 'ASC');
	
	var qualityCm = new Ext.grid.ColumnModel(qualityColumns);
	qualityGrid = new Ext.grid.GridPanel({id: 'extQualityGridContainer',  height: h,
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true, autoFill : false,
		store: qualityStore, cm: qualityCm,
		title: I18N.text.upChannelSignalQuality,
		viewConfig: { forceFit: true},
		renderTo: 'qualityInfo-div'});
	/* qualityGrid.on('rowdblclick', function(qualityGrid, rowIndex, e) {
    }); */  
	qualityStore.load();
}

function listTypeChanged(){
	switch(Zeta$('upChannelListType').value) {
	case '0':
		Zeta$('statisticInfo-unit').style.display = 'none';
		Zeta$('statisticInfo-div').style.display = 'none';
		Zeta$('qualityInfo-div').style.display = 'none';
		Zeta$('refreshSignalInfo').style.display = 'none';
		Zeta$('refreshBaseinfo').style.display = 'block';
		Zeta$('moreInfodetail-div').style.display = 'block';
		Zeta$('moreInfo-div').style.display = 'block';
		Zeta$('openTd').style.display='block';
		Zeta$('closeTd').style.display='block';
		Zeta$('fullingTd').style.width='490px';
		break;
	case '1':
		Zeta$('moreInfodetail-div').style.display = 'none';
		Zeta$('qualityInfo-div').style.display = 'none';
		Zeta$('moreInfo-div').style.display = 'none';
		Zeta$('refreshBaseinfo').style.display = 'none';
		Zeta$('refreshSignalInfo').style.display = 'none';
		Zeta$('statisticInfo-unit').style.display = 'block';
		Zeta$('statisticInfo-div').style.display = 'block';
		break;
	case '2':
		Zeta$('statisticInfo-unit').style.display = 'none';
		Zeta$('statisticInfo-div').style.display = 'none';
		Zeta$('moreInfodetail-div').style.display = 'none';
		Zeta$('moreInfo-div').style.display = 'none';
		Zeta$('refreshBaseinfo').style.display = 'none';
		Zeta$('refreshSignalInfo').style.display = 'block';		
		Zeta$('qualityInfo-div').style.display = 'block';
		Zeta$('openTd').style.display='none';
		Zeta$('closeTd').style.display='none';
		Zeta$('fullingTd').style.width='650px';
		break;
	}
}

function listContentChanged(value){
	if(value==false){
		Zeta$('moreinfo_button').style.display = 'none';
		Zeta$('baseInfo_button').style.display='block';
		showByChoice(value);
	}else{
		Zeta$('baseInfo_button').style.display='none';
		Zeta$('moreinfo_button').style.display = 'block';
		showByChoice(value);
	}
}

function onloadFunction(){
	//createQualityList();
	//Zeta$('qualityInfo-div').style.display = 'none';
	//createStaticList();
	//Zeta$('statisticInfo-div').style.display = 'none';
	//createMoreInfoList();
	//Zeta$('moreInfodetail-div').style.display = 'block';
}

function showByChoice(value){
	var columnModel=Ext.getCmp("extmoreInfoGridContainer").getColumnModel();
	//var columnNum=[7,10,11,12,13,14,15,16,17,18,19,21,23];
	var columnNum=[7,12,13,14,15,16,17,18,19,21,23];
	for(i=0;i<columnNum.length;i++){
		columnModel.setHidden(columnNum[i],value);
	}
}

function refreshBaseinfo(){
	window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.text.refreshUpchannelTip,'waitingMsg','ext-mb-waiting');
	$.ajax({
		url: 'refreshUpChannelBaseInfo.tv?r=' + Math.random(),
	    type: 'post',
	    data : {
	    	cmcId: cmcId,
	    	productType: productType,
			entityId: entityId,
			cmcMac : cmcMac
		},
	    success: function(response) {
	  	    if(response=="true"){
	  	    	onRefreshClick()  	    		
	  	    	window.parent.closeWaitingDlg();
	  	    	top.afterSaveOrDelete({
	   				title: I18N.RECYLE.tip,
	   				html: '<b class="orangeTxt">' + I18N.text.refreshSuccessTip +'</b>'
	   			});
			}else{
				window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
			}
		}, error: function(response) {
			window.parent.closeWaitingDlg();
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
		}, cache: false
	}); 
}

function refreshSignalInfo(){
	window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.text.refreshUpchannelTip,'waitingMsg','ext-mb-waiting');
	$.ajax({
		url: 'refreshUpChannelSignalQualityInfo.tv?cmcId=' + cmcId+'&productType='+productType ,
	    type: 'post',
	    success: function(response) {	       
	    	if(response=="true"){
  	    		onRefreshClick();
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
	   				title: I18N.RECYLE.tip,
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

function authLoad(){
    if(!refreshDevicePower){
        $("#btn1").attr("disabled",true);
        $("#btn2").attr("disabled",true);
    }
}
</script>
<title><fmt:message bundle="${cmc}" key="text.upChannelList"/> </title>
</head>
<body class="BLANK_WND newBody" onload="onloadFunction();authLoad()" >	
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>
		<div class="edgeAndClearFloat">
			<ul class="whiteTabUl">
				<li><a href="javascript:;" class="first selectedFirst"><span><fmt:message bundle='${cmc}' key='text.baseInfo'/></span></a></li>
				<li><a href="javascript:;" class="last"><span><fmt:message bundle='${cmc}' key='text.signalQuality'/></span></a></li>
			</ul>
			<ol class="upChannelListOl pL20 pB0" id="upChannelListOl">
				<li><a href="javascript:;" class="normalBtn" id="btn1" name="<fmt:message bundle='${cmc}' key='text.refreshData'/>"><span><i class="miniIcoEquipment"></i><fmt:message bundle='${cmc}' key='text.refreshData'/></span></a></li>
 				<li><a href="javascript:;" class="normalBtn" id="btn2" name="<fmt:message bundle='${cmc}' key='text.refreshData'/>"><span><i class="miniIcoEquipment"></i><fmt:message bundle='${cmc}' key='text.refreshData'/></span></a></li>
				<li><a href="javascript:;" class="normalBtn" id="openChannels"><span><i class="miniIcoPlay"></i><fmt:message bundle="${cmc}" key="CMC.select.open"/></span></a></li>
				<li><a href="javascript:;" class="normalBtn" id="closeChannels"><span><i class="miniIcoStop"></i><fmt:message bundle="${cmc}" key="CMC.select.close"/></span></a></li>
			</ol>
		</div>
		<div class="paddingLR10 clearBoth pT10">
			<div class="whiteTabContent">
				<div class="containerLegent">
					<div id="moreInfodetail-div" class="normalTable extZebra"></div>
<%-- 					<dl class="legent">
						<dt class="mR5"><fmt:message bundle="${cmc}" key="CMC.colorExample"/>:</dt>
						<dd><img src="../../images/correct.png" border="0" alt="" /></dd>
						<dd class="mR10"><fmt:message bundle="${cmc}" key="CMC.select.open"/></dd>
						<dd><img src="../../images/wrong.png" border="0" alt="" /></dd>
						<dd><fmt:message bundle="${cmc}" key="CMC.select.close"/></dd>						
					</dl> --%>
				</div>
				<div id="statisticInfo-unit" align="right"style="padding-right: 50px;display:none;"><fmt:message bundle='${cmc}' key='text.unit'/>:Mini-Slots(MS)</div>
			</div>
			<div class="whiteTabContent">
				<div id="qualityInfo-div" class="normalTable extZebra"></div>
			</div>
			<div class="whiteTabContent">
				<div id="statisticInfo-div" class="normalTable extZebra"></div>
			</div>
		</div>
		
		<div id="openLayer" class="channelOpenLayer">
			<div class="edge10">
				<ol class="upChannelListOl">
				     <li><a onclick="goBack()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i><fmt:message bundle='${cmc}' key='CMC.goBack'/></span></a></li>			
				</ol>				
			</div>	
			<div class="horizontalOnePxLine"></div>
			<div class="openLayerSide" id="openLayerSide">
				<ul class="openLayerSideUl">
					<li><a id='liChannel1' name="1" href="javascript:;" class="selected"><fmt:message bundle='${cmc}' key='CHANNEL.channel'/>1</a></li>
					<li><a id='liChannel2' name="2" href="javascript:;"><fmt:message bundle='${cmc}' key='CHANNEL.channel'/>2</a></li>
					<li><a id='liChannel3' name="3" href="javascript:;"><fmt:message bundle='${cmc}' key='CHANNEL.channel'/>3</a></li>
					<li><a id='liChannel4' name="4" href="javascript:;"><fmt:message bundle='${cmc}' key='CHANNEL.channel'/>4</a></li>
				</ul>
			</div>
			<div class="openLayerLine" id="openLayerLine"></div>
			<div class="openLayerMain" id="openLayerMain">
				<div class="edge10">
					<table id="rightTable" class="dataTable" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">				
					     <thead>				
					         <tr>				
					             <th colspan="6" class="txtLeftTh" id="showChannelName"><fmt:message bundle='${cmc}' key='CHANNEL.channel'/>1</th>				
					         </tr>				
					     </thead>				
					     <tbody>				
					         <tr class="light">				
					             <td class="rightBlueTxt" width="10%"><fmt:message bundle="${cmc}" key="CHANNEL.frequency"/>:			
					             </td>				
					             <td width="40%"  id="docsIfUpChannelFrequencyForunit_detail">-</td> 									        			
					             <td class="rightBlueTxt"  width="10%"><fmt:message bundle="${cmc}" key="CMC.label.bandwidth"/>:			
					             </td>				
					             <td width="40%"  id="docsIfUpChannelWidthForunit_detail">-</td>	
					         </tr>
					         <tr class="light">		
					             <%-- <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.snr"/>:				
					             </td> 			
					             <td id="docsIfSigQSignalNoiseForunit_detail">-</td>	--%>				
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.power"/>:			
					             </td>				
					             <td id="docsIf3SignalPowerForunit_detail">-</td> 
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.channelType"/>:				
					             </td>				
					             <td id="docsIfUpChannelTypeName_detail">-</td> 						
					        </tr>		
					       	<tr>		        
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.modulationProfile"/>:			
					             </td>				
					             <td id="docsIfUpChannelModulationProfileName_detail">-</td> 	
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CCMTS.channel.adminStatus"/>:		
					             </td>				
					              <td id="ifAdminStatus_detail"><img nm3ktip="<fmt:message bundle="${cmc}" key="CMC.select.close"/>" class="nm3kTip" src="/images/wrong.png" border="0" align="absmiddle"></td>	
					         </tr>
					         <tr class="light">
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CCMTS.channel.status"/>:		
					             </td>				
					             <td id="ifOperStatus_detail"><img nm3ktip="<fmt:message bundle="${cmc}" key="CMC.select.close"/>" class="nm3kTip" src="/images/wrong.png" border="0" align="absmiddle"></td>
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.rangingBackoffStart"/>:			
					             </td>				
					             <td id="channelRangingBackoffStart_detail">-</td>
					             
					         </tr>
					         <tr> 	
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.rangingBackoffEnd"/>:			
					             </td>				
					             <td id="channelRangingBackoffEnd_detail">-</td> 
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.txBackoffStart"/>:			
					             </td>				
					             <td id="channelTxBackoffStart_detail">-</td>
					         </tr>
					         <tr class="light">							 
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.txBackoffEnd"/>:				
					             </td>				
					             <td id="channelTxBackoffEnd_detail">-</td> 
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.extChannelMode"/>:				
					             </td>				
					             <td id="channelExtMode_detail">-</td>	
					         </tr>
					         <tr> 	
					            <%--  <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.preEqEnable"/>:	
					             </td>
						         <td id="docsIfUpChannelPreEqEnableName_detail">-</td> --%>
					             <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.activeCodes"/>:				
					             </td>				
					             <td id="channelScdmaActiveCodes_detail">-</td>
					             <td class="rightBlueTxt" style="padding:4px 0px;"><fmt:message bundle="${cmc}" key="CHANNEL.frameSize"/>:					             		
					             </td>				
					             <td id="channelScdmaFrameSize_detail">-</td>	
					         </tr>
					         <tr class="light">
					             
					              <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="CHANNEL.codesPerSlot"/>:			
					             </td>				
					             <td id="channelScdmaCodesPerSlot_detail" >-</td>
					             <td></td>
					             <td></td>	
					        </tr> 	
						     
					     </tbody>				
					</table>		
					<div class="noWidthCenterOuter clearBoth pT10">
					     <ol class="upChannelListOl noWidthCenter">		
					         <!-- 按照测试组提的改进建议，去掉开启、关闭按钮 modified by huandongsheng	
					         <li><a href="javascript:;" class="normalBtnBig" onclick="openButton()" id="openChannel"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${cmc}" key="CMC.select.open"/></span></a></li>					
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeButton()" id="closeChannel"><span><i class="miniIcoWrong"></i><fmt:message bundle="${cmc}" key="CMC.select.close"/></span></a></li>
					          -->					
					         <li><a href="javascript:;" class="normalBtnBig" onclick="turnToModify()"><span><i class="miniIcoEdit"></i><fmt:message bundle="${cmc}" key="CMC.text.modify"/></span></a></li>					
					     </ol>					
					</div>
										
					
				</div>		
			</div>
		</div>
<script type="text/javascript">
	$(function(){
		window.createQualityList();
		window.createStaticList();
		window.createMoreInfoList();
		$("#openChannels").attr("disabled", true);
		$("#closeChannels").attr("disabled", true);
		//隐藏按钮2;
		$("#upChannelListOl li:eq(1)").css("display","none");
		
		//点击从设备上获取；
		$("#btn1").click(function(){
			window.refreshBaseinfo();
		});
		//点击从设备上获取；
		$("#btn2").click(function(){
			window.refreshSignalInfo();
		});
		//点击开启;
		$("#openChannels").click(function(){
			window.batchOpenChannels();
		});
		//点击关闭;
		$("#closeChannels").click(function(){
			window.batchCloseChannels();
		});
		
		
		//显示第一个;
		$(".whiteTabContent").css("display","none");
		$(".whiteTabContent:first").css("display","block");
		
		var nm3k = {};
		nm3k.nowIndex = 0;
		//点击上部选项卡,选项卡个数和文字是可以自适应的;
		$(".whiteTabUl li a").click(function(){
			var num = $(".whiteTabUl a").index(this);
			if(num == nm3k.nowIndex){//如果点击的是已经选择的;
				return;
			}
			allNum = $(".whiteTabUl a").length - 1;
			$(".whiteTabUl li a").removeClass("selectedFirst").removeClass("selected").removeClass("selectedLast");
			switch(num){
				case 0://点击的是第一个;
					$(this).addClass("selectedFirst");
				break;
				case allNum://点击的是最后一个;
					$(this).addClass("selectedLast");
				break;
				default:
					$(this).addClass("selected");
				break;
			};//end switch;
			$(".whiteTabContent").eq(nm3k.nowIndex).css("display","none");
			$(".whiteTabContent").eq(num).css("display","block");
			nm3k.nowIndex = num;			
			
			switch(num){
				case 0:
					$("#upChannelListOl li").css("display","none");
					$("#upChannelListOl li:eq(0), #upChannelListOl li:eq(2),#upChannelListOl li:eq(3)").css("display","block");
					break;
				case 1:
					$("#upChannelListOl li").css("display","none");
					$("#upChannelListOl li:eq(1)").css("display","block");
					break;
			}
			setTimeout(function(){
				correctGrid();
			},20);
			
			
		});//end click;		
	});//end document.ready;
	
	function correctGrid(){
		var w = $(window).width()- 20;
		var h = $(window).height() - 120;
		if(w>20 && h>20  && baseGrid !=null && qualityGrid !=null){
			//qualityGrid.setSize(w,h);
			qualityGrid.setWidth(w);
			qualityGrid.setHeight(h);
			//baseGrid.setSize(w,h);
			baseGrid.setWidth(w);
			baseGrid.setHeight(h);
			$(window).resize();
		};//end if;
	}
</script>

<script type="text/javascript" src="/js/jquery/dragMiddle.js"></script>
<script type="text/javascript">
$(window).load(function(){
	setOpenLayerHeight();
	
	$(window).resize(function(){
		var w = $(window).width()- 20;
		var h = $(window).height() - 120;
		if(w>20 & h>20){
			qualityGrid.setSize(w,h);
			baseGrid.setSize(w,h);
		};//end if;
		setOpenLayerHeight();
	});//end resize;
	
	//左侧可以拖拽宽度;
	var o1 = new DragMiddle({ id: "openLayerLine", leftId: "openLayerSide", rightId: "openLayerMain", minWidth: 100, maxWidth:400,leftBar:true });
	o1.init();
});//end load;

//点击查看  详细  链接;
function seeMore(channelId){
	channelDetailInfoLoad();
	//data为channel的Data
	var channelIdLiName = "liChannel"+channelId;
	/*$("li>a","#openLayer > #openLayerSide").removeClass("selected");
	  $("li>a[id="+channelIdLiName+"]","#openLayer > #openLayerSide").addClass("selected");*/
	$("#showChannelName").text("<fmt:message bundle='${cmc}' key='CHANNEL.channel'/>"+channelId);
	
	var $jsChannel = $("#moreInfodetail-div .jsChannelName"),
	    len = $jsChannel.length,
	    $ul = $("#openLayer .openLayerSideUl"),
	    str = '';
	    
	$ul.empty();
	$jsChannel.each(function(i){
		var $me = $(this),
		    num = i+1;
		
		str += '<li>';
		if(num === channelId){
			str += '<a id="liChanne'+ num +'" name="'+ num +'" href="javascript:;" class="selected">'+ $me.text() +'</a>';
		}else{
			str += '<a id="liChanne'+ num +'" name="'+ num +'" href="javascript:;">'+ $me.text() +'</a>';
		}
		str += '</li>';
	});
	$ul.html(str);
	
	channelInfoDetail(channelId);
	
	/* if( true ){
		var $ul = $("#openLayer .openLayerSideUl"),
		    len = $ul.find("li").length;
		if( len == 4 ){
			$ul.find("li:eq(1)~li").remove();	
		}
	} */
	
	$("#openLayer").fadeIn();
}
function channelInfoDetail(channelId){
	var channel = null;
	switch(channelId)
	{
	case 1:
		channel = channel1;
		
	  break;
	case 2:
		channel = channel2;
	  break;
	case 3:
		channel = channel3;
	  break;
	case 4:
		channel = channel4
	  break;
	default:
		//channel = channel1;
	}
	
	//var vtd = $("tbody td[id]","#rightTable");
	var data = channel.data;
	$.each(data,function(i,v){
		$("#"+i+"_detail").text(v);
	})
	//对需要特殊展示的给予修改
	var adminStatus = $("#ifAdminStatus_detail").text();
	//管理状态
	if("1"==adminStatus){
		$("#ifAdminStatus_detail").html(String.format('<img nm3ktip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle>', I18N.COMMON.on));	
	}else{
		$("#ifAdminStatus_detail").html(String.format('<img nm3ktip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle>', I18N.COMMON.off));			
	}
	//运行状态
	if ("1"==$("#ifOperStatus_detail").text()&&"1"==adminStatus)  {
		$("#ifOperStatus_detail").html(String.format('<img nm3ktip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle>', I18N.COMMON.on));	
	} else {
		$("#ifOperStatus_detail").html(String.format('<img nm3ktip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle>', I18N.COMMON.off));	
	}
	//信道模式

	if("2"==$("#channelExtMode_detail").text()){
		$("#channelExtMode_detail").text("V2");
	}else if("3"==$("#channelExtMode_detail").text()){
		$("#channelExtMode_detail").text("V3");
	}else{
		$("#channelExtMode_detail").text("-");
	}
	//信道类型SCDMA/ATDMA
	if("ATDMA"==$("#docsIfUpChannelTypeName_detail").text()){
		$("#channelScdmaActiveCodes_detail").text("-")
		$("#channelScdmaFrameSize_detail").text("-")
		$("#channelScdmaCodesPerSlot_detail").text("-")
	}
	//修改开启或关闭按钮使能状态
	if("1"==adminStatus){
		$("#closeChannel").attr("disabled", false);
		$("#openChannel").attr("disabled", true);
	}else{
		$("#closeChannel").attr("disabled", true);
		$("#openChannel").attr("disabled", false);
	}
	
}
// 修改 按钮
function turnToModify(){
	var channelIdstr = $("li>a.selected","#openLayer > #openLayerSide").attr("name");
	var channelId = parseInt(channelIdstr);
	var channel = null;
	switch(channelId)
	{
	case 1:
		channel = channel1;
	  break;
	case 2:
		channel = channel2;
	  break;
	case 3:
		channel = channel3;
	  break;
	case 4:
		channel = channel4
	  break;
	default:
		//channel = channel1;
	}
	goBack();
	openBaseInfo(channel.data.cmcPortId,channel.data.channelId,channel.data.channelIndex);
}
// 返回  按钮;
function goBack(){
	$("#openLayer").fadeOut();
}
//打开  按钮
function openButton(){
	var channelIdstr = $("li>a.selected","#openLayer > #openLayerSide").attr("name");
	var channelId = parseInt(channelIdstr);
	var channel = null;
	switch(channelId)
	{
	case 1:
		channel = channel1;
	  break;
	case 2:
		channel = channel2;
	  break;
	case 3:
		channel = channel3;
	  break;
	case 4:
		channel = channel4
	  break;
	default:
		//channel = channel1;
	}
	var channelIndexs = new Array();
	channelIndexs.push(channel.data.channelIndex);
	setChannelsStatus("/cmc/channel/batchOpenChannels.tv?cmcId="+cmcId,channelIndexs);
	goBack();
}
//关闭  按钮
function closeButton(){
	var channelIdstr = $("li>a.selected","#openLayer > #openLayerSide").attr("name");
	var channelId = parseInt(channelIdstr);
	var channel = null;
	switch(channelId)
	{
	case 1:
		channel = channel1;
	  break;
	case 2:
		channel = channel2;
	  break;
	case 3:
		channel = channel3;
	  break;
	case 4:
		channel = channel4
	  break;
	default:
		//channel = channel1;
	}
	var channelIndexs = new Array();
	channelIndexs.push(channel.data.channelIndex);
	setChannelsStatus("/cmc/channel/batchCloseChannels.tv?cmcId="+cmcId,channelIndexs);
	goBack();
}
function setOpenLayerHeight(){
	var h2 = $("#openLayer").height() - 54;
	if(h2 > 0){
		$("#openLayerSide, #openLayerLine, #openLayerMain").height(h2);
	}
}

//点击a标签，切换右侧table;
$("#openLayerSide a").live("click",function(){
	$("#openLayerSide a").removeClass("selected");
	$(this).addClass("selected");
	var tit = $(this).text();
	$("#rightTable thead th").text(tit);
	channelInfoDetail(parseInt($(this).attr("name")));
});
</script>
</body>
</html>