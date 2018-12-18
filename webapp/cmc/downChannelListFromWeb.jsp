<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library zeta
    module CMC
    CSS css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
a {text-decoration: underline;}
</style>

<script type="text/javascript" src="/js/tools/authTool.js"></script>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/cmc/rendererFunction.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
vcEntityKey = 'cmcId';
var productType ='<s:property value="productType"/>';
var baseStore=null;
var baseGrid;
var ipqamStore = null;
var ipqamGrid;
var h;
var w;
var showDownChannelIPQAMInfo;
var isIpqamSupported = '${isIpqamSupported}'==''?false:${isIpqamSupported};
var maxIpqamCount = '${maxIpqamCount}'==''?0:${maxIpqamCount};

var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var maxChanPowerLUT = [600, 600, 560, 540, 520, 510, 500, 490, 490, 480, 480, 470, 470, 460, 460, 450, 450];
var powerLimit = ${powerLimit};

//复制JSON数据
function cloneJSON(para){
    var rePara = null;
    var type = Object.prototype.toString.call(para);
    if(type.indexOf("Object") > -1){
        rePara = jQuery.extend(true, {}, para);
    }else if(type.indexOf("Array") > 0){
        rePara = [];
        jQuery.each(para, function(index, obj){
            rePara.push(jQuery.cloneJSON(obj));
        });
    }else{
        rePara = para;
    }
    return rePara;
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
function onRefreshClick() {
	baseStore.reload();
	ipqamStore.reload({callback:function(r,o,s){
		var hh = 600*(r.length + 1.5)/18;
		if(hh>ipqamGrid.maxHeight){
			ipqamGrid.setHeight(ipqamGrid.maxHeight)
		}else{
			ipqamGrid.setHeight(hh);	
		}
		vp.doLayout();
	}});
	//每次刷新时需要更新表格头部的选中状态
	var gridElDocsis = baseGrid.getEl();//得到表格的EL对象
	var gridElIpqam  = ipqamGrid.getEl();
	var hdD = gridElDocsis.select('div.x-grid3-hd-checker');
	var hdI = gridElIpqam.select('div.x-grid3-hd-checker');
	hdD.removeClass('x-grid3-hd-checker-on');
	hdI.removeClass('x-grid3-hd-checker-on');
	var baseSelModel = baseGrid.getSelectionModel();
	baseSelModel.clearSelections();
	var ipqamSelModel = ipqamGrid.getSelectionModel();
	ipqamSelModel.clearSelections();
	
	changeOpenOrCloseButtonStatus();
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
//docsis信道修改入口
function openBaseInfo(cmcPortId,adminStatus){
	window.top.createDialog('downStreamConfig', I18N.text.downChannelBaseInfo, 800, 500, 
			'cmc/channel/showDownStreamConfigInfo.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId + '&productType='+productType, null, true, true);
}
//ipqam信道修改入口
function openIpqamInfo(cmcPortId,adminStatus){
	window.top.createDialog('downStreamConfig', I18N.text.downChannelBaseInfo, 800, 500, 
			'cmc/channel/showDSIpqamConfigInfo.tv?cmcId=' + cmcId+'&cmcPortId='+cmcPortId + '&productType='+productType, null, true, true);
}
//docsis信道名链接入口
function opeartionRender(value, cellmate, record){
	var cmcPortId = record.get('cmcPortId');
	var docsIfDownChannelId = record.get('docsIfDownChannelId');

	return String.format("<a href='#' onclick='openBaseInfo(\"{0}\")'/>" +I18N.CHANNEL.channel+ docsIfDownChannelId + 
				"</a>", cmcPortId);
}
//docsis信道操作入口
function renderOpeartion(value, cellmate, record){
    var cmcPortId = record.data.cmcPortId;
	return String.format("<a href='#' onclick='openBaseInfo(\"{0}\")'/>" +I18N.CMC.label.edit+"</a>", 
			cmcPortId);
}
//ipqam信道名链接入口
function opeartionIpqamRender(value, cellmate, record){
	var cmcPortId = record.get('cmcPortId');
	var docsIfDownChannelId = record.get('docsIfDownChannelId');

	return I18N.CHANNEL.channel+ docsIfDownChannelId;
}
//ipqam信道操作入口
function renderIpqamOpeartion(value, cellmate, record){
	var cmcPortId = record.data.cmcPortId;
	return String.format("<a href='#' onclick='openIpqamInfo(\"{0}\")'/>" + I18N.CMC.label.edit + "</a>", 
			cmcPortId);
}
function batchOpenAsDocsisChannels(){
	var channelIndexs = new Array();
	var selections = [];
	var baseSelections = baseGrid.getSelectionModel().getSelections();
	var bsLength = baseSelections.length;
	for(var i = 0; i < bsLength; i++){
		channelIndexs[i] = baseSelections[i].data.channelIndex;
	}
	//if(isIpqamSupported){
		var ipqamSelections = ipqamGrid.getSelectionModel().getSelections();
		for(var i = 0; i < ipqamSelections.length; i++){
			channelIndexs[bsLength+i] = ipqamSelections[i].data.channelIndex;
			//alert(ipqamSelections[i].data.channelIndex);
		}
		selections = baseSelections.concat(ipqamSelections);	
	//}else{
	//	selections = baseSelections;
	//}
	
	
	if(checkChannelFrequency(baseGrid.store.data.items,selections) != true){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.batchOpenDownFrequencyInvalid );
		return;
	}
	if(checkChannelPower(baseGrid.store.data.items,selections) != true){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.batchOpenDownPowerInvalid);
		return;
	}
	var data = {channelIndexs: channelIndexs};
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.tip.confirmOpenChannels, function (type) {
		if(type=="ok"){
			window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
			$.ajax({
				url:"batchOpenChannels.tv?cmcId="+cmcId,
				type:"post",
				data:data,
				dataType:"json",
				success:function (response){
					if(response.message == "success"){
						//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.setSuccess);
						window.parent.closeWaitingDlg();
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
	var channelIndexs = new Array();
	var selections = [];
	var baseSelections = baseGrid.getSelectionModel().getSelections();
	var bsLength = baseSelections.length;
	for(var i = 0; i < bsLength; i++){
		channelIndexs[i] = baseSelections[i].data.channelIndex;
	}
	//if(isIpqamSupported){
		var ipqamSelections = ipqamGrid.getSelectionModel().getSelections();
		for(var i = 0; i < ipqamSelections.length; i++){
			channelIndexs[bsLength+i] = ipqamSelections[i].data.channelIndex;
			//alert(ipqamSelections[i].data.channelIndex);
		}
		selections = baseSelections.concat(ipqamSelections);	
	//}else{
	//	selections = baseSelections;
	//}
	
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
						window.top.closeWaitingDlg();
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
function batchOpenAsIpqamChannels(){
	var baseSelections = baseGrid.getSelectionModel().getSelections();
	var ipqamSelections;
	if(ipqamGrid==undefined||ipqamGrid==null){
		ipqamSelections = [];
	}else{
		ipqamSelections = ipqamGrid.getSelectionModel().getSelections();
	}
	if(maxIpqamCount!=0){
		var len = baseSelections.length+ipqamGrid.getStore().data.length;
		if(len>maxIpqamCount){
			window.top.showMessageDlg(I18N.RECYLE.tip, String.format(I18N.CHANNEL.ipqamChannelCountLimited,maxIpqamCount));
			return;
		}
	}
	
	var ids = new Array();
	var channelIndexs = new Array();
	var bsLength = baseSelections.length;
	for(var i = 0; i < bsLength; i++){
		channelIndexs[i] = baseSelections[i].data.channelIndex;
		ids[i] =  baseSelections[i].data.docsIfDownChannelId;
	}
	/* for(var i = 0; i < ipqamSelections.length; i++){
		channelIndexs[bsLength+i] = ipqamSelections[i].data.channelIndex;
		ids[bsLength+i] =  baseSelections[i].data.docsIfDownChannelId;
	} */
	var selections = baseSelections.concat(ipqamSelections);
	
	if(checkChannelFrequency(baseGrid.store.data.items,selections) != true){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.batchOpenDownFrequencyInvalid );
		return;
	}
	if(checkChannelPower(baseGrid.store.data.items,selections) != true){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.batchOpenDownPowerInvalid);
		return;
	}
	
	var data = {channelIndexs: channelIndexs,channelAdminstatus:3,channelId:calBitsToNum(ids)};//ids是多个id按位计算的值

	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.tip.confirmOpenChannels, function (type) {
		if(type=="ok"){
			window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
			$.ajax({
				url:"batchOpenChannels.tv?cmcId="+cmcId,
				type:"post",
				data:data,
				dataType:"json",
				success:function (response){
					if(response.message == "SUCCESS"){
						//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.setSuccess);
						top.afterSaveOrDelete({
			   				title: I18N.RECYLE.tip ,
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
//数组的value，换算成按位的整数
function calBitsToNum(value) {
    var tempNum = 0;
    for(var i = 0; i < value.length; i++){
    	tempNum += Math.pow(2, value[i] - 1);
    }
    return tempNum;
}
//修改按钮的可操作状态
function changeOpenOrCloseButtonStatus(){
	var opEnDocsis = true;
	var opEnIpqam = true;
	var eNClose = true;
	if(operationDevicePower){		
		var baseObj = Ext.getCmp("extbaseGridContainer");
		var baseSelections = baseObj.getSelectionModel().getSelections();
		
		if(!isIpqamSupported){
			//ipqamSelections.length==0;
			if(baseSelections.length != 0){
				for(var i = 0; i < baseSelections.length; i++){
					if(baseSelections[i].data.ifAdminStatus != 1){
						opEnDocsis = false;
					}else{
						eNClose = false;
					}
				}
				
			}
			
		}else{
			var ipqamObj = Ext.getCmp("ipqamGridContainer");//$("#openAsIpqamChannels").css("display","display");
			var ipqamSelections = [];
			if(ipqamObj!=undefined&&ipqamObj!=null){
				ipqamSelections =ipqamObj.getSelectionModel().getSelections();				
			}
			if(baseSelections.length!=0&&ipqamSelections.length==0){
				opEnIpqam = false;
				for(var i = 0; i < baseSelections.length; i++){
					if(baseSelections[i].data.ifAdminStatus != 1){
						opEnDocsis = false;
					}else{
						eNClose = false;
					}
				}
			}else if(baseSelections.length!=0&&ipqamSelections!=0){
				
				opEnIpqam = false;
				opEnDocsis = false;
				eNClose = false;
			}else if(baseSelections.length==0&&ipqamSelections!=0){
				opEnDocsis = false;
				eNClose = false;
			}
		}
		$("#openAsDocsisChannels").attr("disabled", opEnDocsis);
		$("#closeChannels").attr("disabled", eNClose);	
		$("#openAsIpqamChannels").attr("disabled",opEnIpqam);
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
	changeOpenOrCloseButtonStatus();
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
	changeOpenOrCloseButtonStatus();
}

Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});
/* function dealloadData(){
	var channelData = baseStore.data.items;
	var data = [];
	var ipqamData = cloneJSON(showDownChannelIPQAMInfo).data;
	//var arr=[];
	if(showDownChannelIPQAMInfo.item_num>=1){
		//for(var i=0;i<channelData.length;i++){
		$.each(channelData,function(i,v){	
			//alert(a);
			data.push(v);
			$.each(ipqamData,function(j,m){
				if(v.data.docsIfDownChannelId==m.docsIfDownChannelId){
					data.pop();
				}
			})
		})
		
		//baseStore.loadData(data);
		//return false;
		//baseStore.removeAll();
		baseStore.data.items = data;
		//baseStore.loadData(data);
	}
} */
// 管理状态
function renderOpenOrClose(value, p, record){
	if(value == "1"){
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle>',I18N.CMC.select.open+"DOCSIS");
	}else if(value == '2'||value=='0'){
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle>',I18N.CMC.select.close);
	}else{
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct2.png" border=0 align=absmiddle>',I18N.CMC.select.open+"EQAM");
	}
}
// 中心频率
function renderFrequency(value,p,record){  
/*
 * var offsetValue = $('#channelOffset').val(); if(offsetValue != undefined &&
 * parseFloat(offsetValue) != 0 && offsetValue.length != 0){ var frequencyTemp =
 * value + parseFloat(offsetValue); return value + '<font color=red>(' +
 * frequencyTemp + ')</font>' + " MHz"; }
 */
 	value = toDecimal(value / 1000000,1);
	return value + " MHz";
}
// 频道带宽
function renderChannelWidth(value,p,record){
	return toDecimal(value / 1000000,1) + " MHz" ;
}
//调制方式
function renderModulation(value,p,record){//下行信道不支持1024配置
// 	var channelType = record.data.channelType;
// 	var modulation = [ "", "unknown", "QAM1024", "QAM64", "QAM256" ];
// 	if(channelType==3){
// 		modulation = [ "", "unknown", "QAM1024", "QAM64", "QAM256" ];
// 	} 
    var modulation = [ "", "unknown", "QAM1024", "QAM64", "QAM256" ];
	return modulation[value];
}
// 电平
function renderPower(value,p,record){
	return value + "@{unitConfigConstant.elecLevelUnit}@";
}
// 交织深度
function renderInterleave(value,p,record){
	var types = [ "-", "-", "-", "(8, 16)", "(16, 8)",
		"(32, 4)", "(64, 2)", "(128, 1)", "-" ];
	if(record.data.docsIfDownChannelAnnex == 3){ 
		return types[value];
	}else{
		return types[value];
	} 
}
// docsis标准
function renderAnnex(value,p,record){
    var types = [ "", "unknown", "other", "annex A", "annex B", "annex C" ];
	return types[value];
}
//符号率
function renderSymRate(value, p, record){
	var symRate = ['', '6.952', '6.900', '6.875', '5.361', '5.057'];
	return symRate[value];
}
//QAM Manager
function renderQAMManager(value, p, record){
	var qamManager = [ "VOD", "Broadcast", "NGOD", "S_VOD"];
	return qamManager[value];
}
//DtsAdjust
function renderDtsAdjust(value, p, record){

	if(value == "1"){
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle title="{0}">',I18N.CMC.select.open+"DOCSIS");
	}else if(value == '2'||value=='0'){
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle title="{0}">',I18N.CMC.select.close);
	}else{
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct2.png" border=0 align=absmiddle title="{0}">',I18N.CMC.select.open+"IPQAM");
	}
	//var dtsAdjust = [I18N.CMC.select.close,I18N.CMC.select.open ];
	//return dtsAdjust[value];
}

function createBaseList(){
	var sm = new Ext.grid.CheckboxSelectionModel({listeners:{
	    'rowselect':function(sm,rowIndex,record){
	        changeOpenOrCloseButtonStatus();
	    },
		'rowdeselect':function(sm,rowIndex,record){
		    changeOpenOrCloseButtonStatus();
	    }
	}}); 
	var baseColumns = [
		sm,
	    {header: I18N.CHANNEL.channel, width: parseInt(w/16), align: 'center', dataIndex: 'docsIfDownChannelId', renderer: opeartionRender
		},{header: I18N.CHANNEL.frequency, width: parseInt(w/10),  align: 'center',dataIndex : 'docsIfDownChannelFrequencyForunit'//,renderer : renderFrequency//
	    },{header: I18N.CMC.label.bandwidth, width: parseInt(w*8/90),  align: 'center',dataIndex : 'docsIfDownChannelWidth',renderer : renderChannelWidth//, dataIndex: 'docsIfDownChannelWidthForunit'
		},//{header: I18N.CCMTS.channel.ifMtu, width: parseInt(w/9),  align: 'center', dataIndex: 'ifMtu', renderer: rederIfNullCheck},	
		{header: I18N.CCMTS.channel.adminStatus, width: parseInt(w/16),  align: 'center', dataIndex: 'ifAdminStatus',renderer : renderOpenOrClose//,renderer: renderAdminStatus
		},{header: I18N.CCMTS.channel.status, width: parseInt(w/16),  align: 'center', dataIndex: 'ifOperStatus',renderer: renderOperStatus
		},{header: I18N.CHANNEL.modulationType, width: parseInt(w*2/20),  align: 'center',dataIndex : 'docsIfDownChannelModulation',renderer : renderModulation//, dataIndex: 'docsIfDownChannelModulationName'
		},{header: I18N.CHANNEL.interleave, width: parseInt(w/15),  align: 'center',dataIndex : 'docsIfDownChannelInterleave',renderer : renderInterleave//, dataIndex: 'docsIfDownChannelInterleaveName'
		},{header: I18N.CHANNEL.power, width: parseInt(w*8/90),  align: 'center',dataIndex : 'docsIfDownChannelPowerForunit'//,renderer : renderPower, dataIndex: 'docsIfDownChannelPowerForunit'
		},{header: 'Annex', width: parseInt(w*8/90),  align: 'center',dataIndex: 'docsIfDownChannelAnnexName'
		},{header: I18N.CHANNEL.operation, width: parseInt(w/16),  align: 'center', dataIndex: 'op', renderer: renderOpeartion
		}
	];

	baseStore = new Ext.data.JsonStore({
	    url: ('getDownStreamBaseInfo.tv?cmcId=' + cmcId),
// 	    proxy : new Ext.data.MemoryProxy(downChannelInfo),
	    root: 'data',
	    //remoteSort: true, 
	    fields: ['cmcPortId','docsIfDownChannelId', 'ifName','docsIfDownChannelFrequencyForunit', 'docsIfDownChannelWidthForunit', 'ifSpeedForunit', 'ifDescr', 'ifMtu', 'ifAdminStatus', 'ifOperStatus',
	              'docsIfDownChannelModulation','docsIfDownChannelModulationName', 'docsIfDownChannelInterleave','docsIfDownChannelInterleaveName', 'docsIfDownChannelPowerForunit','docsIfDownChannelAnnex', 'docsIfDownChannelAnnexName',
	               'channelIndex', 'docsIfDownChannelFrequency',
	              'docsIfDownChannelPower', 'docsIfDownChannelWidth','channelType']
	});
	//baseStore.setDefaultSort('docsIfDownChannelId', 'ASC');
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		region: 'center',
		animCollapse: animCollapse, 
		trackMouseOver: trackMouseOver, 
		border: true, 
		store: baseStore, 
		cm: baseCm,
		sm:sm,
		//forceLayout:true,
		margins:'10px',
		viewConfig:{
			forceFit:true
		},
		cls:'normalTable',
		autofill:true,
		title: I18N.text.downChannelList
		});
	
	
	baseStore.on("load", function(store ,records) {
		//var data = ipqamStore.data.items;		
		store.filterBy(function(record, id){
 			var result = true;
			if(record.get("channelType")==3){
				result = false;
			}
			return result;
		});

	}) 
	baseStore.load();
}
function createIpqamList() {

    var sm = new Ext.grid.CheckboxSelectionModel();

    Ext.override(Ext.grid.GridView,{  
	    
	    onRowSelect : function(row){  
	        this.addRowClass(row, "x-grid3-row-selected");  
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
	var ipqamColumns = [
			sm,
			{header : I18N.CHANNEL.channel,width : parseInt(w/15),align : 'center',dataIndex : 'docsIfDownChannelId', renderer: opeartionIpqamRender
			}, {header : I18N.CHANNEL.frequency,width : parseInt(w/12),id : 'ipqamFrequency',align : 'center',dataIndex : 'docsIfDownChannelFrequency',renderer : renderFrequency//,dataIndex : 'docsIfDownChannelFrequency',renderer : renderFrequency
			}, {header : I18N.CMC.label.bandwidth,width : parseInt(w/15),align : 'center',dataIndex : 'docsIfDownChannelWidth',renderer : renderChannelWidth
			}, {header : I18N.CCMTS.channel.adminStatus,width : parseInt(w/17),align : 'center',dataIndex : 'ifAdminStatus',renderer : renderOpenOrClose
			}, {header : I18N.CHANNEL.modulationType,width : parseInt(w/15),align : 'center',dataIndex : 'docsIfDownChannelModulation',renderer : renderModulation
			}, {header : I18N.CHANNEL.interleave,width : parseInt(w/15),align : 'center',dataIndex : 'docsIfDownChannelInterleave',renderer : renderInterleave
			}, {header : I18N.CHANNEL.power,width : parseInt(w/15),align : 'center',dataIndex : 'downChannelPower',renderer : renderPower
			}, {header : 'Annex',width : parseInt(w/16),align : 'center',dataIndex : 'docsIfDownChannelAnnex',renderer : renderAnnex
			}, {header : 'SymbolRate',width : parseInt(w/17),align : 'center',dataIndex : 'docsIfDownChannelSymRate',renderer: renderSymRate
			}, {header : 'Transp<br>Stream<br>ID',width : parseInt(w/17),align : 'center',dataIndex : 'ipqamTranspStreamID'
			}, {header : 'QAM<br>Group<br>Name',width : parseInt(w/16)/*70*/,align : 'center',dataIndex : 'ipqamQAMGroupName'
			}, {header : 'QAM<br>Manager',width : parseInt(w/16)/*60*/,align : 'center',dataIndex : 'ipqamQAMManager',renderer:  renderQAMManager
			}, {header : 'Original<br>Network<br>ID',width : parseInt(w/16)/*70*/,align : 'center',dataIndex : 'ipqamOriginalNetworkID'
			}, {header : 'Atten',width : parseInt(w/20)/*50*/,align : 'center',dataIndex : 'ipqamAtten'//,renderer: renderGray
			}, {header : 'Dts<br>Adjust',width : parseInt(w/16)/*60*/,align : 'center',dataIndex : 'ipqamDtsAdjust',renderer: renderDtsAdjust
			}/* ,{header: I18N.CHANNEL.operation, width : 100, align: 'center', dataIndex: 'op', renderer: renderIpqamOpeartion
			} */];
	ipqamStore = new Ext.data.JsonStore({
		//totalProperty : "results",
		root : "data",
		//pruneModifiedRecords : true,
		id : "ipqamStore",
		url: ('/cmc/ipQamChannel/getDownStreamIpqamBaseInfo.tv?cmcId=' + cmcId),
		//proxy : new Ext.data.MemoryProxy(showDownChannelIPQAMInfo),
		fields : [ 'cmcPortId','channelIndex','docsIfDownChannelId', 'ifAdminStatus',
				'docsIfDownChannelFrequency','docsIfDownChannelFrequencyForunit', 'docsIfDownChannelWidth',
				'docsIfDownChannelModulation', 'docsIfDownChannelPower',
				'docsIfDownChannelAnnex', 'docsIfDownChannelInterleave',
				'docsIfDownChannelSymRate'/*, 'ipqamChannelMode'*/, 'ipqamTranspStreamID',
				'ipqamQAMGroupName', 'ipqamQAMManager',
				'ipqamOriginalNetworkID', 'ipqamAtten', 'ipqamDtsAdjust','downChannelPower']
	});
	//ipqamStore.setDefaultSort('docsIfDownChannelId', 'ASC');

	ipqamCm = new Ext.grid.ColumnModel({
		columns: ipqamColumns
	});

	ipqamGrid = new Ext.grid.EditorGridPanel({
		id : 'ipqamGridContainer',
		region: 'south',
		height : 600*(showDownChannelIPQAMInfo.data.length + 1.5)/18,
		//autoHeight: true,
		maxHeight:250,
		border : true,
		store : ipqamStore,
		// clicksToEdit: 1,
		//forceLayout:true,
		cm : ipqamCm,
		sm : sm,
		cls:'normalTable',
		autofill : true,
		//title: "",IPQAM信道列表
		viewConfig: {
			forceFit: true
		}
	});
	
	ipqamStore.load({callback:function(r,o,s){
		var hh = 600*(r.length + 1.5)/18;
		if(hh>ipqamGrid.maxHeight){
			ipqamGrid.setHeight(ipqamGrid.maxHeight)
		}else{
			ipqamGrid.setHeight(hh);	
		}
		vp.doLayout();
	}});	
	

}


//var downChannelInfo = ${downChannelsInfo}; 
var downChannelIPQAMList = ${downChannelIPQAMList};

showDownChannelIPQAMInfo  = cloneJSON(downChannelIPQAMList);
//var ipQamListSize = ${downChannelIPQAMListSize};

var vp;
Ext.onReady(function(){
	w = document.body.clientWidth - 30;
	//base Info
	createBaseList();
	//isIpqamSupported = true;
	//check Ipqam or create ipqam Info
	//
	createIpqamList();
	var gridA = [baseGrid,ipqamGrid];
	if(isIpqamSupported){
		$("#ipqamButtonOpen").css("display","display");
	}else{
		ipqamGrid.hide();
		$("#ipqamButtonOpen").css("display","none");
	}
	$("#openAsDocsisChannels").attr("disabled", true);
	$("#openAsIpqamChannels").attr("disabled",true);
	$("#closeChannels").attr("disabled", true);
	//layout with 'border'
	vp = new Ext.Viewport({
		layout: 'border',
	    items: [{
	        region: 'north',
	        height: 80,
	        border: false,
	        contentEl: 'topPart'
	    },
	    gridA
	   ]
	});
});

function authLoad(){
    if(!refreshDevicePower){
        $("#refreshBase").attr("disabled",true);
    }
}
</script>
</head>
<body class="newBody" onload="authLoad()">
	<div id="topPart">
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>
		<div class="edge10">
			<ul class="leftFloatUl">
				<li>
					<a id="refreshBase" href="javascript:;" class="normalBtn"  onclick="refreshBase()">
						<span><i class="miniIcoEquipment"></i>@text.refreshData@</span>
					</a>
				</li>
				<li>
					<a id="openAsDocsisChannels"  onclick="batchOpenAsDocsisChannels()" href="javascript:;" class="normalBtn">
						<span><i class="miniIcoPlay"></i>@CMC.select.open@DOCSIS@CHANNEL.channel@</span>
					</a>
				</li>
				<li id='ipqamButtonOpen'>
					<a id="openAsIpqamChannels"  onclick="batchOpenAsIpqamChannels()" href="javascript:;" class="normalBtn">
						<span><i class="miniIcoPlay"></i>@CMC.select.open@EQAM@CHANNEL.channel@</span>
					</a>
				</li>
				<li>
					<a id="closeChannels"  onclick="batchCloseChannels()" href="javascript:;" class="normalBtn">
						<span><i class="miniIcoStop"></i>@CMC.select.close@</span>
					</a>
				</li>
			</ul>
		</div>
	</div>
	<div>
		<div id="baseInfo-div"></div>
		<div id="ipqamInfo-div"></div>
	</div>
</body>
</Zeta:HTML>