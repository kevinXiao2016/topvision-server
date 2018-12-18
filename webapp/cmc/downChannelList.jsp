<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Downstream Channel List</title>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css" /> 
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<style type="text/css">
a {text-decoration: underline;}
</style>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-versioncontrol.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/cmc/rendererFunction.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
vcEntityKey = 'cmcId';
var productType ='<s:property value="productType"/>';
var baseStore=null;
var baseGrid;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var maxChanPowerLUT = [600, 600, 560, 540, 520, 510, 500, 490, 490, 480, 480, 470, 470, 460, 460, 450, 450];
var powerLimit = ${powerLimit};
function onRefreshClick() {
	baseStore.reload();
	//每次刷新时需要更新表格头部的选中状态
	var gridEl = baseGrid.getEl();//得到表格的EL对象
	var hd = gridEl.select('div.x-grid3-hd-checker');
	hd.removeClass('x-grid3-hd-checker-on');
	$("#openChannels").attr("disabled", true);
	$("#closeChannels").attr("disabled", true);
}
function renderOpeartion(value, cellmate, record){
    var cmcPortId = record.data.cmcPortId;
    return String.format("<a href='javascript:;' " + 
            "onclick='openBaseInfo(\"{0}\")'><fmt:message bundle='${cmc}' key='CMC.label.edit'/></a>" , 
            cmcPortId);
}
//保留digit位小数
function toDecimal(x, digit){
	var f = parseFloat(x);
	if(isNaN(f)){
		return false;
	}
	var temp = Math.pow(10, digit);
	var f = Math.round(x*temp)/temp;
	var s = f.toString();
	var rs = s.indexOf('.');
	if(rs < 0){
		rs = s.length;
		s += '.';
	}
	while(s.length <= rs + digit){
		s += '0';
	}
	return s;
}
//中心频率
function renderFrequency(value,p,record){  
 	var value = toDecimal(value / 1000000,1);
	return value + " MHz";
}
function refreshBase(){
	window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.text.refreshDownchannelTip,'waitingMsg','ext-mb-waiting');
	$.ajax({
		url:"refreshDownChannelBaseInfo.tv?cmcId=" + cmcId+'&productType='+productType,
		type:"post",
		success:function (response){
			if(response=="true"){
				onRefreshClick();
				//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshSuccessTip);
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
	   				title: I18N.RECYLE.tip,
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
function renderAdminStatus(value, p, record){
	if (record.data.ifAdminStatus == '1') {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle>',
		        I18N.COMMON.on);		
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle>',
		        I18N.COMMON.off);	
	}
}
function renderOperStatus(value, p, record){
	if (record.data.ifAdminStatus == '1' && record.data.ifOperStatus == '1')  {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle>', I18N.COMMON.on);	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle>', I18N.COMMON.off);	
	}
}
function rederIfNullCheck(value, p, record, columnIndex){
	if(value == null || value == ''){
		return "-";
	}else 
		return value;
}

function renderDownChannelWidth(value, p, record){
	if(record.data.docsIfDownChannelWidthForunit == "0.0 MHz"){
		if(record.data.docsIfDownChannelAnnexName == "annexA"){
			return "8.0 MHz";
		}else if(record.data.docsIfDownChannelAnnexName == "annexB"){
			return "6.0 MHz";
		}
	}
	return record.data.docsIfDownChannelWidthForunit;
}

function renderInterleave(value, p, record){
      	if(record.data.docsIfDownChannelAnnexName == "annexA"){ 
      		return "-";
      	}else{
      		return value;
      	} 
	
}

function openBaseInfo(cmcPortId){
	window.top.createDialog('downStreamConfig', I18N.text.downChannelBaseInfo, 800, 500, 'cmc/channel/showDownStreamConfigInfo.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId + '&productType='+productType, null, true, true);
}

function opeartionRender(value, cellmate, record){
	var cmcPortId = record.get('cmcPortId');
	var docsIfDownChannelId = record.get('docsIfDownChannelId');
    return String.format("<a href='#' onclick='openBaseInfo(\"{0}\")'/>" +I18N.CHANNEL.channel+ docsIfDownChannelId + 
		"</a>", cmcPortId);

}

function batchOpenChannels(){
	var selections = baseGrid.getSelectionModel().getSelections();
	var channelIndexs = new Array();
	for(var i = 0; i < selections.length; i++){
		channelIndexs[i] = selections[i].data.channelIndex;
	}
	if(checkChannelFrequency(baseGrid.store.data.items,selections) != true){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.batchOpenDownFrequencyInvalid );
		return;
	}
	if(checkChannelPower(baseGrid.store.data.items,selections) != true){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.batchOpenDownPowerInvalid);
		return;
	}
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.tip.confirmOpenChannels, function (type) {
		if(type=="ok"){
			window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
			$.ajax({
				url:"batchOpenChannels.tv?cmcId="+cmcId,
				type:"post",
				data:{channelIndexs: channelIndexs},
				dataType:"json",
				success:function (response){
					if(response.message == "success"){
						//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.setSuccess);
						top.afterSaveOrDelete({
			   				title: I18N.RECYLE.tip,
			   				html: '<b class="orangeTxt">'+I18N.CMC.tip.setSuccess+'</b>'
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
			window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
			$.ajax({
				url:"/cmc/channel/batchCloseChannels.tv?cmcId="+cmcId,
				type:"post",
				data:{channelIndexs: channelIndexs},
				dataType:"json",
				success:function (response){
					if(response.message == "success"){
						//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.setSuccess);
						top.afterSaveOrDelete({
			   				title: I18N.RECYLE.tip,
			   				html: '<b class="orangeTxt">'+I18N.CMC.tip.setSuccess+'</b>'
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
	}
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
	}
	if(operationDevicePower){
		$("#closeChannels").attr("disabled", status);
	}
}
//Check Frequency
function checkChannelFrequency(data,selections){
	var max = 0;
	var min = 1000000000;
	for(var i = 0; i < selections.length; i++){
		//验证与已经开启的通道的频点不重合
		for(var j = 0; j < data.length; j++){
			//选择的信道为已经开启的信道 ，跳出循环， 判断下一个选中的信道
            if(data[j].data.cmcPortId == selections[i].data.cmcPortId){
                continue;
            }
			if(data[j].data.ifAdminStatus == 1){
				var cutValue = Math.abs(selections[i].data.docsIfDownChannelFrequency - 
						data[j].data.docsIfDownChannelFrequency);
				var widthContainer = data[j].data.docsIfDownChannelWidth/2 + selections[i].data.docsIfDownChannelWidth/2;
				if(cutValue < widthContainer){
					return false;
				}
			}
		}
		//验证一套开启通道的频点之间不重合
		for(var j = 0; j < selections.length; j++){
			if( j != i && selections[j].data.ifAdminStatus != 1){
				var cutValue = Math.abs(selections[i].data.docsIfDownChannelFrequency - 
						selections[j].data.docsIfDownChannelFrequency);
				var widthContainer = selections[j].data.docsIfDownChannelWidth/2 + selections[i].data.docsIfDownChannelWidth/2;
				if(cutValue < widthContainer){
					return false;
				}
			}
		}
	}
	//验证开启之后频点之差不超过192M
	for(var i = 0; i < data.length; i++){
		if(data[i].data.ifAdminStatus == 1){
			if(max < data[i].data.docsIfDownChannelFrequency){
				max = data[i].data.docsIfDownChannelFrequency;
			}
			if(min > data[i].data.docsIfDownChannelFrequency){
				min = data[i].data.docsIfDownChannelFrequency;
			}
		}
	}
	for(var i = 0; i < selections.length; i++){
		if(selections[i].data.ifAdminStatus != 1){
			if(max < selections[i].data.docsIfDownChannelFrequency){
				max = selections[i].data.docsIfDownChannelFrequency;
			}
			if(min > selections[i].data.docsIfDownChannelFrequency){
				min = selections[i].data.docsIfDownChannelFrequency;
			}
		}
	}
	if(max-min>192000000){
		return false;
	}else{
		return true;
	}
}
//验证电平值的合法性
function checkChannelPower(data,selections){
	var max = 0;
	var min = 600;
	var count = 0;//记录开启通道数
	//查找开启通道的（包括已经开启可将要开启的）最大电平值和最小电平值
	for(var i = 0; i < data.length; i++){
		if(data[i].data.ifAdminStatus == 1){
			if(max < data[i].data.docsIfDownChannelPower){
				max = data[i].data.docsIfDownChannelPower;
			}
			if(min > data[i].data.docsIfDownChannelPower){
				min = data[i].data.docsIfDownChannelPower;
			}
			count ++;
		}
	}
	for(var i = 0; i < selections.length; i++){
		if(selections[i].data.ifAdminStatus != 1){
			if(max < selections[i].data.docsIfDownChannelPower){
				max = selections[i].data.docsIfDownChannelPower;
			}
			if(min > selections[i].data.docsIfDownChannelPower){
				min = selections[i].data.docsIfDownChannelPower;
			}
			count ++;
		}
	}

    //验证电平值小于最大可设置电平
    //modify by fanzidong,使用实时值
    if(powerLimit && powerLimit[count] && powerLimit[count].maxPowerTenthdBmV) {
        var curMaxLimit = powerLimit[count].maxPowerTenthdBmV;
        if(max > curMaxLimit){
            return false;
        }

    }
	//验证最大电平值与最小电平值之差小于8dB
	if(max - min > 80){
		return false;
	}else{
		return true;
	}
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
			//alert(this.grid.store.getCount());
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
function createBaseList(){
	$("#openChannels").attr("disabled", true);
	$("#closeChannels").attr("disabled", true);
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 100;
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
	    {header: I18N.CHANNEL.channel, width: parseInt(w/16), algin: 'center', dataIndex: 'docsIfDownChannelId', renderer: opeartionRender},
		{header: I18N.CHANNEL.frequency, width: parseInt(w/10),  align: 'center',dataIndex : 'docsIfDownChannelFrequencyForunit'},//, dataIndex: 'docsIfDownChannelFrequencyForunit'},
		{header: I18N.CMC.label.bandwidth, width: parseInt(w*8/90),  align: 'center', dataIndex: 'docsIfDownChannelWidthForunit'},	
		//{header: I18N.CCMTS.channel.ifMtu, width: parseInt(w/9),  align: 'center', dataIndex: 'ifMtu', renderer: rederIfNullCheck},	
		{header: I18N.CCMTS.channel.adminStatus, width: parseInt(w/16),  align: 'center', dataIndex: 'ifAdminStatus',renderer: renderAdminStatus},	
		{header: I18N.CCMTS.channel.status, width: parseInt(w/16),  align: 'center', dataIndex: 'ifOperStatus',renderer: renderOperStatus},		
       	{header: I18N.CHANNEL.modulationType, width: parseInt(w*2/20),  align: 'center', dataIndex: 'docsIfDownChannelModulationName'},
       	{header: I18N.CHANNEL.interleave, width: parseInt(w/15),  align: 'center', dataIndex: 'docsIfDownChannelInterleaveName',renderer:renderInterleave},
       	{header: I18N.CHANNEL.power, width: parseInt(w*8/90),  align: 'center', dataIndex: 'docsIfDownChannelPowerForunit'},	
		{header: "Annex", width: parseInt(w*8/90),  align: 'center', dataIndex: 'docsIfDownChannelAnnexName'},
		{header: I18N.CHANNEL.operation, width: parseInt(w/16),  align: 'center', dataIndex: 'op', renderer: renderOpeartion}
	];

	baseStore = new Ext.data.JsonStore({
	    url: ('getDownStreamBaseInfo.tv?cmcId=' + cmcId),
	    root: 'data',
	    //remoteSort: true, 
	    fields: ['cmcPortId','docsIfDownChannelId', 'ifName','docsIfDownChannelFrequencyForunit', 'docsIfDownChannelWidthForunit', 'ifSpeedForunit', 'ifDescr', 'ifMtu', 'ifAdminStatus', 'ifOperStatus',
	              'docsIfDownChannelModulationName', 'docsIfDownChannelInterleaveName', 'docsIfDownChannelPowerForunit', 'docsIfDownChannelAnnexName',
	              'channelIndex', 'docsIfDownChannelFrequency',
	              'docsIfDownChannelPower', 'docsIfDownChannelWidth']
	});
	//baseStore.setDefaultSort('docsIfDownChannelId', 'ASC');
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		region: 'center',
		animCollapse: animCollapse, 
		trackMouseOver: trackMouseOver, 
		border: true, 
		store: baseStore, cm: baseCm,
		sm:sm,
		margins:'0px 10px 10px 10px',
		viewConfig:{
			forceFit:true
		},
		cls:'normalTable',
		autofill:true,
		title: I18N.text.downChannelList
		});
	
	var v = new Ext.Viewport({
		layout: 'border',
	    items: [{
	        region: 'north',
	        height: 80,
	        border: false,
	        contentEl: 'topPart'
	    },baseGrid]
	    
		
	});

   /* 	baseGrid.on('rowdblclick', function(baseGrid, rowIndex, e) {
   	      var record = baseGrid.getStore().getAt(rowIndex);
   	      cmcPortId = record.get("cmcPortId");
   	      window.top.createDialog('downStreamConfig', I18N.text.downChannelBaseInfo, "small_16_9", 310, 'cmc/channel/showDownStreamConfigInfo.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId+'&productType='+productType, null, true, true);
   	  }); */

	baseStore.load();
}
function onloadFunction(){
	createBaseList();
	Zeta$('baseInfo-div').style.display = 'block';
}

function authLoad(){
    if(!refreshDevicePower){
	    $("#refreshBase").attr("disabled",true);
    }
}
</script>
</head>
<body class="newBody" onload="onloadFunction();authLoad()">
	<div id="topPart">
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>
		<div class="edge10">
			<ul class="leftFloatUl">
				<li>
					<a id="refreshBase" href="javascript:;" class="normalBtn"  onclick="refreshBase()"><span><i class="miniIcoEquipment"></i><fmt:message bundle='${cmc}' key='text.refreshData'/></span></a>
				</li>
				<li>
					<a id="openChannels"  onclick="batchOpenChannels()" href="javascript:;" class="normalBtn"><span><i class="miniIcoPlay"></i><fmt:message bundle='${cmc}' key='CMC.select.open'/></span></a>
				</li>
				<li>
					<a id="closeChannels"  onclick="batchCloseChannels()" href="javascript:;" class="normalBtn"><span><i class="miniIcoStop"></i><fmt:message bundle='${cmc}' key='CMC.select.close'/></span></a>
				</li>
			</ul>
		</div>
	</div>
	<div>
	<div id="baseInfo-div"></div>
	</div>
</body>
</html>