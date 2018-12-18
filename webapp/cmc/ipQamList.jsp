<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>EQAM list</title>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<!-- 自定义css引入 -->
<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css"/>
<!-- 自定义js引入 -->
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>

<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>

<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
var productType ='<s:property value="productType"/>';
var w;
var h;
var inUseIpqamIds = [];
var statusInfoGrid;
var streamMapInfoGrid;
var statusInfoStore;
var streamMapInfoStore;

/**
 * 测试数据
 */
// var getIPQAMOutputStatusInfoList = {data: [{ipqamOutputQAMChannel: "1",ipqamMemoryAddress: "1",ipqamFrequency: "544.000",ipqamUsedUDPPorts: "0",ipqamUsedBandwidth: "0.015",ipqamBandwidthCapacity: "38.440",ipqamPercent: "0.039",ipqamAtten: "0",ipqamSymbolRate: "6.952",ipqamModulation: "QAM64",ipqamBitrateImage: "1"},{ipqamOutputQAMChannel: "2",ipqamMemoryAddress: "0",ipqamFrequency: "552.000",ipqamUsedUDPPorts: "0",ipqamUsedBandwidth: "0.015",ipqamBandwidthCapacity: "38.440",ipqamPercent: "0.039",ipqamAtten: "0",ipqamSymbolRate: "6.952",ipqamModulation: "QAM64",ipqamBitrateImage: "1"}],item_num:2};
//var ipqamStreamMapList = {data: [],item_num:0};
// var userInfo = {data: [{userName: "admin",password: "******",authority: "2",topCcmtsAaaLocalUsrGroup: "administrators"},{userName: "sss",password: "******",authority: "1",topCcmtsAaaLocalUsrGroup: ""},{userName: "aaa",password: "******",authority: "1",topCcmtsAaaLocalUsrGroup: ""}],item_num:3};
// var ipqamIpInfo = {data: [{ipqamIpAddr: "192.168.2.100",ipqamIpMask: "255.255.255.0",ipqamGw: "192.168.2.1",ipqamMacAddr: "00:24:68:4A:00:01"}],item_num:1};
// var ipqamOutputStreamInfoList = {data: [],item_num:0};
//var getIPQAMOutputStatusInfoList = '${iPQAMOutputStatusInfoList}';
//var ipqamStreamMapList = '${ipqamStreamMapList}'
</script>
	
<script type="text/javascript">
/**
 * 函数
 */
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
 /*修改IPQAM的IP信息
 */
 function modifyIpqamIP(){
	 if(ipqamIpCheck()){
		 $.ajax({
	        url: '/cmc/ipQamChannel/modifyCCIpqamIpInfo.tv?cmcId='+cmcId,
	        type: 'POST',
	        //dataType: 'json', 
	        cache: false,
	        data:'cmcIpqamInfo.ipqamIpAddr='+getIpValue("ipqamIp")+'&cmcIpqamInfo.ipqamIpMask='+getIpValue("ipqamIpMask"),//jQuery(ipqamIpInfo).serialize(),
	        success: function(response) {
	        	getIpqamIpInfo();
	            if("success" == response){
					window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.modifySuccess);
	            }else{
	            	window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.modifyFail);
	            	setIpValue("ipqamIp", ip);
	            	setIpValue("ipqamIpMask", mask);
	            }
	        	
	        },error: function(json) {
	        } 
		        
		});
	}
 }
 
function findMask(ip){
	var tmpMask = "192.192.0.0";
	if(ip=="0.0.0.0")
		tmpMask = "0.0.0.0";
	else{
        for(var i = 0; i < sourceIpList.length; i++){
            if(sourceIpList[i]==ip){
            	tmpMask=sourceMaskList[i];
            	break;
        	}
    	}
	}
	return tmpMask;
}
//入参为Ip地址数组
 function localgetIpUnsigned32Value(addr){
 	var unsignedIp = 0;
 	var multiplier = [256*256*256, 256*256, 256, 1]
 	for(var i=4; i>0; i--){
 		unsignedIp += (parseInt(addr[i-1])*multiplier[i-1])>>>0;
 	}
 	return unsignedIp>>>0;
 }
//获取无符号32位IP值
 function getIpUnsigned32Value(id){
 	var unsignedIp = 0;
 	var multiplier = [256*256*256, 256*256, 256, 1]
 	for(var i=4; i>0; i--){
 		if($("#"+id+"_"+i).val() != ""){
 			unsignedIp += ($("#"+id+"_"+i).val()*multiplier[i-1])>>>0;
 		}
 	}
 	return unsignedIp>>>0;

 }
//检查是否为广播IP，是则返回 true
//ipId,maskId 页面中元素的ID
function broadcastIpCheck(ipId, maskId){
	var ip = getIpUnsigned32Value(ipId);
	var mask = getIpUnsigned32Value(maskId);
	if($("#"+maskId+"_"+4).val() != "" && $("#"+maskId+"_"+4).val() == 255){
		return false;
	}
	if(ip == ((ip&mask) + ~mask)>>>0){
		return true;
	}else{
		return false;
	}
}
 /**
 IP地址检查
 **/
function ipqamIpCheck(){
	var tmpIpqamIP = getIpValue("ipqamIp");//$("#ipqamIp").val();
	var tmpIpqamMask = getIpValue("ipqamIpMask");//$("#ipqamIpMask").val();
	//IP地址字符串分割为字符串数组
	var ipTemp = tmpIpqamIP.split(".");
	var maskTemp = tmpIpqamMask.split(".");
	//计算IP地址32位整型值
	var ipqamIp = localgetIpUnsigned32Value(ipTemp);
	var ipqamMask = localgetIpUnsigned32Value(maskTemp);
	
	//子网掩码
	if("0.0.0.0" == tmpIpqamMask){
        window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.IPmask.IpMaskMustNot0);
        return false;
    }
    if("255.255.255.255" == tmpIpqamMask){
        window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.IPmask.IpMaskMustNot255);
        return false;
    }
    if(checkedIpValue(tmpIpqamMask)){
        var ip = tmpIpqamMask.split(".");
        var ip_binary = (parseInt(ip[0]) + 256).toString(2).substring(1) + (parseInt(ip[1]) + 256).toString(2).substring(1)
                        + (parseInt(ip[2]) + 256).toString(2).substring(1) + (parseInt(ip[3]) + 256).toString(2).substring(1);
        if (ip_binary.indexOf("01") != -1){
        	window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.IPmask.IpMaskError);
            return false;
        }
    }
	
    if( ipqamIp==(ipqamIp&ipqamMask)>>>0 && parseInt(maskTemp[3])!=255){
		window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.IP.ipError1);
		return false;
	}
	//ip地址
	if(checkedIpValue(tmpIpqamIP)){//格式是否正确
		if(!checkIsNomalIp(tmpIpqamIP)){
			//return true;
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.IP.ipError5);
			return false;
		}
		if(checkIsMulticast(tmpIpqamIP)){//是否为组播地址，是则返回true
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.IP.ipError3);
			return 	false;
		}
		if (tmpIpqamIP == "0.0.0.0") {
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.IP.ipError2);
			return false;
		}
		if(broadcastIpCheck("ipqamIp","ipqamIpMask")){
			window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.IP.ipError4);
			return false;
		}
	}else{
		window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.IP.ipError1);
		return false;
	} 
	return true;
}
 /*
 *获取IP地址信息
 */
 function getIpqamIpInfo(){
	 $.ajax({
			url:"/cmc/ipQamChannel/getCCIpqamIpInfo.tv?cmcId=" + cmcId+'&productType='+productType,
			type:"post",
			success:function (response){
				var data = response.data;
				if(data){
					var ip = data.ipqamIpAddr;
					var mask = data.ipqamIpMask;
					setIpValue("ipqamIp", ip);
					setIpValue("ipqamIpMask", mask);	
				}
				
			},error: function(response) {
				//window.parent.showMessageDlg(I18N.RECYLE.tip, "IP "+I18N.text.refreshFailureTip);
			}, cache: false
		});
 }
 function refreshIpqamStatusAndMappings(){
	 window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.ipqam.isSureToRefreshDevice, function (type) {
		if(type=="ok"){
			window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.CMC.ipqam.doingRefreshDevice, 'ext-mb-waiting');
			$.ajax({
				url:"/cmc/ipQamChannel/refreshIpQamMappingsStatus.tv?cmcId=" + cmcId+'&productType='+productType,
				type:"post",
				success:function (response){
					if(response=="success"){
						window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshSuccessTip);
					}else{
						window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
					}
					onRefreshClick();
				},error: function(response) {
					window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.refreshFailureTip);
				}, cache: false
			});
		}
	 })
 }
 function onRefreshClick(){
	 statusInfoStore.reload();
	 streamMapInfoStore.reload();
	 getIpqamIpInfo();
	 //var len = streamMapInfoStore.data.length;
	 //var h = (len+1.5)*600/18;
	 //alert(h);
	 //streamMapInfoGrid.setHeight(h);
 }
 function delMappingsInfoBy(params){
	 var action = 1;
	 window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.ipqam.isSureToDelIpqamMappings, function (type) {
		if(type=="ok"){
		 window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.CMC.ipqam.doingDelIpqamMappings, 'ext-mb-waiting');
			$.ajax({url:"/cmc/ipQamChannel/modifyIPQAMStreamMapList.tv?cmcId=" + cmcId+'&productType='+productType,
					data:{
						cc8800bHttpDSIpqamMappingListStr:params,
						action:action
					},
					method:"post", 
					cache: false,
					success:function (response) {
						if(response == "success"){
				    		window.top.showMessageDlg(I18N.COMMON.waiting,I18N.CMC.ipqam.deleteIpqamSuccessTip);
						 }else{
				    		window.top.showMessageDlg(I18N.COMMON.waiting,I18N.CMC.ipqam.deleteIpqamFailureTip);
						}
						onRefreshClick();
					}, 
					failure:function () {
						 //window.top.showMessageDlg(I18N.COMMON.waiting,'');设备无法连通，请检查网络连接!
					}
			});
		}
	});
 }
function showIpqamMappingsPage(mappingId,channelId,action){
	 window.top.createDialog('ipqamMappingsConfig', I18N.CMC.ipqam.cfg, 800, 500, 
				'/cmc/ipQamChannel/showIpQamConfigInfo.tv?cmcId=' + cmcId+'&mappingId='+mappingId +
						'&action='+action +'&channelId='+channelId + '&productType='+productType, null, true, true);
}
//renderer
 function renderPidMap (value, p, record) {
 	var displayValue = [];
 	var displayValueStr = '';
 	if(value == '' || value == 0){
 		return displayValueStr;
 	}
 	var tmp = value.split(',');
 	var num = 0;
 	for(var i = 1; i < tmp.length - 1; i = i + 2){
 		displayValue.push(tmp[i] + '-' + tmp[i + 1]);
 	}
 	displayValueStr =  displayValue.join(',');
 	if(record.data.ipqamStreamType == 1 || record.data.ipqamStreamType == 3){
 		return displayValueStr;
 	}else{
 		return '<font color=gray>' + displayValueStr + '</font>';
 	}
 }
 function renderDataRate(value, p, record){
 	if(record.data.ipqamDataRateEnable == 0){
 		return '<font color=gray>' + value + '</font>';
 	}else{
 		return value;
 	}
 }
 function renderProgramNumberOutput (value, p, record) {
 	if(record.data.ipqamStreamType == 3 || record.data.ipqamStreamType == 1){
 		return '<font color=gray>-</font>';
 	}else{
 		return value;
 	}
 }
 function renderToOperation(value,p,record){
	var mappingId = record.json.mappingId;
	var ipqamOutputQAMChannel = record.json.ipqamOutputQAMChannel;
	if(mappingId==undefined||mappingId==null){
		mappingId=0;
	}
	//1表示删除，2表示修改，3表示增加
	var action = 3;
	
	return String.format("<a href='#' onclick='showIpqamMappingsPage(\"{0}\",\"{1}\",\"{2}\")'><fmt:message bundle='${cmc}' key='CMC.ipqam.addIpqamMappings'/></a>",mappingId,ipqamOutputQAMChannel,action);//+"/"+del;
	 //String.format("<a href='#' onclick='openBaseInfo(\"{0}\",\"{1}\")'/>" +"<fmt:message bundle='${cmc}' key='CMC.ipqam.addIpqamMappings'/></a>", cmcPortId,adminStatus);
 }
 function renderToMappingsOperation(value,p,record){
	var del = "<a href='#' onclick='delMappingsInfoBy(\"{0}\")'/><fmt:message bundle='${cmc}' key='CMC.button.delete'/></a>";
	var js = record.data;
	var arr = [];
	var num = 1;
	 /*由于PID数值以逗号分割，造成在后台处理数据时JSON解析有问题，如人为添加引号处理，则与String的format有冲突。因此不得不多做一步操作
	 *当然,可以不使用String的format方式解决
	 */
	$.each(js,function(i,v){
		arr.push(i + ":#" + v+"#");
	})
	 //action 1表示删除，2表示修改，3表示增加
	arr.push('ipqamAction:'+1);
	arr.push('streamMapNum:' + num);
	 
	var mappingId = record.json.mappingId;
	var action = 2;
	return String.format("<a href='#' onclick='showIpqamMappingsPage(\"{0}\",\"{1}\",\"{2}\")'><fmt:message bundle='${cmc}' key='CMC.label.edit'/></a> / " +
		"<a href='#' onclick='delIpqamInfo(\"{3}\")'><fmt:message bundle='${cmc}' key='CMC.button.delete'/></a>",
			mappingId,0,action,arr);
	/* var param = "";
	 $.each(js,function(i,v){
			param +=i + ":'" + v+"',";
	 })
	 //1表示删除，2表示修改，3表示增加
	param +='ipqamAction:'+1;
	param +=',streamMapNum:' + num;
	 
	 var mappingId = record.json.mappingId;
	 var action = 2;
	 var editStr = "<a href='#' onclick='showIpqamMappingsPage('"+mappingId+"',0,'"+action+"')'><fmt:message bundle='${cmc}' key='CMC.label.edit'/></a> / " +
	 "<a href='#' onclick='delMappingsInfoBy(\'"+param+"\')'><fmt:message bundle='${cmc}' key='CMC.button.delete'/></a>"
	 return editStr; */
 }
 function delIpqamInfo(arr){
	var param = '';
	$.each(arr,function(i,v){
		v = v.replace("#","'");
		param += v;
	})
	param = "{"+param+"}";
	delMappingsInfoBy(param);
 }
 function renderStreamType(value, p, record){
		if(value == 3){
			return 'Data';
		} else if(value == 2){
			return 'DataR';
		} else if(value == 0){
			return 'SPTS';
		} else {
			return 'MPTS';
		}
	}
 function renderActive(value, p, record){
		if(value == 2){
			return 'Delete';
		} else if(value == 1){
			return "<fmt:message bundle='${cmc}' key='CMC.select.open'/>";
		} else {
			return "<fmt:message bundle='${cmc}' key='CMC.select.close'/>";
		}
	}
 function renderDataRateEnable(value, p, record){
		if(value == 1){
			return "<fmt:message bundle='${cmc}' key='CMC.select.open'/>";
		} else {
			return "<fmt:message bundle='${cmc}' key='CMC.select.close'/>";
		}
	}
//过滤
 function filterOper(){
 	//-1表示全选，不过滤
 	if(streamMapInfoStore.isFiltered()){
 		streamMapInfoStore.clearFilter();
 	}
 	var QAMChannel = $("#QAMChannel").val();
 	var channelStatus = $("#channelStatus").val();
 	var streamType = $("#streamType").val();
 	var rateEnable = $("#rateEnable").val();
 	
 	streamMapInfoStore.filterBy(function(record, id){
 		var result = true;
	 	if (QAMChannel!=-1) {
			if(QAMChannel!=record.get("ipqamOutputQAMChannel")){
				result = false;
			}
	 	};
	 	if (channelStatus!=-1) {
	 		if(channelStatus != record.get("ipqamActive")){
	 			result = false;
	 		}
	 	};
	 	if (streamType!=-1) {
			if(streamType != record.get("ipqamStreamType")){
			result = false;
			};
	 	};
	 	if (rateEnable!=-1) {
 			if(rateEnable != record.get("ipqamDataRateEnable")){
 				result = false;
 			};
	 	};
	 	return result;
	});
 }
//批量删除入口
function deleteStreamMap(){
	var selections = streamMapInfoGrid.getSelectionModel().getSelections();
	var num = selections.length;
	if(num<=0){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.loadbalance.selMappingsPlease);
		return false;
	}
	var params = "";
	$.each(selections,function(j,record){
		var tmpParam = "";
		var js = record.data;
		$.each(js,function(i,v){
			tmpParam += i + ':"' + v + '",';
		})
		//1表示删除，2表示修改，3表示增加
		tmpParam += 'ipqamAction:'+1;
		tmpParam ="{"+tmpParam+"};"
		params +=tmpParam;
	})
	delMappingsInfoBy(params);
}
function buildStreamMapToolBar(){
	var streamMapInfoToolbar;
	streamMapInfoToolbar = [
		//{text: '', iconCls:'bmenu_new', id: 'addProgramTbar'},//, handler: addProgram},增加节目
		//{text: '', iconCls:'tbar_add', id: 'addProgramPageTbar', handler: addProgramPage},批量增加节目
		{text: I18N.CMC.ipqam.batchDelMappings, iconCls:"bmenu_delete", id: 'deleteTbar', handler: deleteStreamMap},'-',
		//{text: '', iconCls:'bmenu_undo', id: 'resetTbar'},//, handler: resetModify},清除修改
		//{text: '', iconCls:'tbar_save', id: 'saveTbar', handler: batchModifyChannel},确定
		new Ext.Toolbar.Fill(),
		"-", 		
		I18N.CHANNEL.id+':',
		'<select name="QAMChannel" id=QAMChannel>'+
			'<option value="-1">'+"<fmt:message bundle='${cmc}' key='CMC.select.all'/>"+'</option>'+ 
		'</select>',
        "-", 
		I18N.CMC.label.status+':',
		'<select name="channelStatus" id=channelStatus>'+
			'<option value="-1">'+"<fmt:message bundle='${cmc}' key='CMC.select.all'/>"+'</option>'+ 
			'<option value="0">'+"<fmt:message bundle='${cmc}' key='CMC.select.close'/>"+'</option>'+
			'<option value="1">'+"<fmt:message bundle='${cmc}' key='CMC.select.open'/>"+'</option>'+
		'</select>',
        "-", 
		I18N.CMC.ipqam.streamType+':',
		'<select name="streamType" id=streamType>'+
			'<option value="-1">'+"<fmt:message bundle='${cmc}' key='CMC.select.all'/>"+'</option>'+
			'<option value="0">SPTS</option>'+
			'<option value="1">MPTS</option>'+
			'<option value="2">DataR</option>'+
			'<option value="3">Data</option>'+ 
		'</select>',
        "-", 
		I18N.CMC.ipqam.inputStreamRateEnable+':', 
		'<select name="rateEnable" id=rateEnable>'+
			'<option value="-1">'+"<fmt:message bundle='${cmc}' key='CMC.select.all'/>"+'</option>'+ 
			'<option value="0">'+"<fmt:message bundle='${cmc}' key='CMC.select.close'/>"+'</option>'+
			'<option value="1">'+"<fmt:message bundle='${cmc}' key='CMC.select.open'/>"+'</option>'+
		'</select>',
		"-",
		{text: I18N.CMC.ipqam.filter, iconCls:'bmenu_find', id: 'findTbar', handler: filterOper},
		"-", new Ext.Toolbar.Fill()
	];
	return streamMapInfoToolbar;
}
//输出状态信息表
 function createStatusInfoList(){
 	//var sm = new Ext.grid.CheckboxSelectionModel();
 	var statusInfoColumns = [//sm, 
 		{header: I18N.CHANNEL.id, width: w*2/22,  align: 'center', dataIndex: 'ipqamOutputQAMChannel'},
 		//{header: 'Memory Address', width: 120,  align: 'center', dataIndex: 'ipqamMemoryAddress'},
 		{header: I18N.CHANNEL.frequency+'(MHz)', width: w*2/22,  align: 'center', dataIndex: 'ipqamFrequency'},
 		{header: I18N.CMC.ipqam.ipqamUsedUDPPorts , width: w*3/22,  align: 'center', dataIndex: 'ipqamUsedUDPPorts'},
 		{header: I18N.CMC.ipqam.ipqamUsedBandwidth+'(Mbps)', width: w*3/22,  align: 'center', dataIndex: 'ipqamUsedBandwidth'},
 		{header: I18N.CMC.ipqam.ipqamBandwidthCapacity+'(Mbps)', width: w*2/22,  align: 'center', dataIndex: 'ipqamBandwidthCapacity'},
 		{header: I18N.CMC.ipqam.ipqamPercent+'(%)', width: w*2/22,  align: 'center', dataIndex: 'ipqamPercent'},
 		//{header: I18N.CMC.ipqam.ipqamAtten+'(dBmV)', width: w*2/22,  align: 'center', dataIndex: 'ipqamAtten'},
 		{header: 'SymbolRate(Mbaud)', width: w*3/22,  align: 'center', dataIndex: 'ipqamSymbolRate'},
 		{header: I18N.CHANNEL.modulationType, width: w*2/22,  align: 'center', dataIndex: 'ipqamModulation'},
 		{header: I18N.CHANNEL.operation, width: w*2/22,  align: 'center', dataIndex: '',renderer:renderToOperation}];
 		
 	statusInfoStore = new Ext.data.JsonStore({
 		url: ('/cmc/ipQamChannel/showCCIpqamOutPutStatusList.tv?cmcId=' + cmcId),
//  		proxy:new Ext.data.MemoryProxy(getIPQAMOutputStatusInfoList),
 		totalProperty:"results",
 		root:"data",
 		pruneModifiedRecords: true,
 		fields: ['ipqamOutputQAMChannel', 'ipqamMemoryAddress', 'ipqamFrequency', 'ipqamUsedUDPPorts', 'ipqamUsedBandwidth', 'ipqamBandwidthCapacity',
 		         'ipqamPercent', 'ipqamAtten', 'ipqamSymbolRate','ipqamModulation', 
 		         'ipqamBitrateImage']
 	});

 	var normalToolbar = ["<span style='font-weight:bold'>&nbsp;</span>",
 	                     new Ext.Toolbar.Fill(),		
 	            		I18N.CMC.ipqam.ipqamUsedToalBandwidth+':<label id="ipqamUsedToalBandwidth"></label> Mbps', '-'
 	            		//,
 	           		//{text: '', iconCls:'tbar_refresh', id: 'refreshChannelTbar'//, handler: refresh}刷新
 	            		];
 	
 	var statusInfoCm = new Ext.grid.ColumnModel(statusInfoColumns);
 	
 	statusInfoGrid = new Ext.grid.GridPanel({
 		id: 'statusInfoGridContainer', 
 		width: w-1,
 		height: 300,
 		//autoHeight: true,
 		//maxHeight:350,
 		// animCollapse: animCollapse,
 		// trackMouseOver: trackMouseOver,
 		border: true, 
 		store: statusInfoStore,
 		// clicksToEdit: 1,
 		cm: statusInfoCm,
 		//sm: sm,
 		cls:'normalTable',
 		autofill:true,		
 		title: I18N.CMC.ipqam.statusList,
 		//renderTo: 'statusInfo-div',
 		region: 'center',
 		tbar: normalToolbar
 	});
 	statusInfoStore.on("load",function(store ,records) {
 		var ipqamUsedToalBandwidth = 0;
		var storeData = store.data.items;		
		for(var i = 0; i < storeData.length; i++){
			ipqamUsedToalBandwidth = accAdd(ipqamUsedToalBandwidth, parseFloat(storeData[i].data.ipqamUsedBandwidth));
		}
		//alert(ipqamUsedToalBandwidth)
		$('#ipqamUsedToalBandwidth').html(toDecimal(ipqamUsedToalBandwidth, 3)); 
 	})
 	statusInfoStore.load();
 	
 }

 // 节目流映射表
//  var comboDataActive = [['1', ''],['0', '']/*,['2', 'Delete']*/];启用  禁用
//  var comboDataStreamType = [['0', 'SPTS'],['1', 'MPTS'],['2', 'DataR'],['3', 'Data']];
//  var comboDataRateEnable = [['1', ''],['0', '']]; 开启  关闭

 function createStreamMapInfoList(){
 	var sm = new Ext.grid.CheckboxSelectionModel();
 	var streamMapInfoColumns = [
 			sm,
 	        //new Ext.grid.RowNumberer({width:30}),
 		    {header: I18N.CHANNEL.id, width: 70,  align: 'center', dataIndex: 'ipqamOutputQAMChannel',menuDisabled:true},
 			{header: I18N.CMC.ipqam.ipqamDestinationIPAddress, width: 100,  align: 'center', dataIndex: 'ipqamDestinationIPAddress',menuDisabled:true},
 			{header: I18N.CMC.ipqam.ipqamUDPPort, width: 100,  align: 'center', dataIndex: 'ipqamUDPPort',
 		    	menuDisabled:true},
 			{header: I18N.CMC.ipqam.streamType, width: 70,  align: 'center', dataIndex: 'ipqamStreamType',
 				menuDisabled:true, renderer: renderStreamType
 				},
 			{header: I18N.CMC.label.status, width: 70,  align: 'center', dataIndex: 'ipqamActive',
 				menuDisabled:true, renderer: renderActive
 				},
 			{header: I18N.CMC.ipqam.ipqamProgramNumberInput, width: 100,  align: 'center', dataIndex: 'ipqamProgramNumberInput',
 		    	menuDisabled:true, renderer: renderProgramNumberOutput
 		    	},
 			{header: I18N.CMC.ipqam.ipqamProgramNumberOutput, width: 100,  align: 'center', dataIndex: 'ipqamProgramNumberOutput',
 		    	menuDisabled:true, renderer: renderProgramNumberOutput
 		    	},
 			{header: 'PMV', width: 70,  align: 'center', dataIndex: 'ipqamPMV',
 		    	menuDisabled:true, renderer: renderProgramNumberOutput
 		    	},
 			{header: I18N.CMC.ipqam.inputStreamRateEnable, width: 120,  align: 'center', dataIndex: 'ipqamDataRateEnable',
 				menuDisabled:true, renderer: renderDataRateEnable
 				},
 			{header: I18N.CMC.ipqam.ipqamDataRate+'(Kbps)', width: 130,  align: 'center', dataIndex: 'ipqamDataRate',
 				menuDisabled:true, renderer: renderDataRate,
 		    	},
 		    {header: I18N.CMC.ipqam.ipqamPidMapString, width: 100,  align: 'center', dataIndex: 'ipqamPidMapString',
 		    	menuDisabled:true,renderer: renderPidMap,
 		    	},
 		    {header:I18N.CHANNEL.operation,width:100,sortable:false,align:'center',dataIndex:'',
 		    	menuDisabled:true,renderer:renderToMappingsOperation
 		    }
 	];
 	
 	// 分组显示
  	var reader=new Ext.data.JsonReader({
 		root:'data',
 		fields: ['ipqamOutputQAMChannel', 'ipqamDestinationIPAddress', 'ipqamUDPPort', 'ipqamActive', 'ipqamStreamType', 'ipqamProgramNumberInput',
 		         'ipqamProgramNumberOutput', 'ipqamPMV', 'ipqamDataRateEnable','ipqamDataRate', 'ipqamPidMapString']
 	});
 	/* streamMapInfoGroupstore=new Ext.data.GroupingStore({   
 		id:'GroupStore',   
 		reader: reader,
 		remoteSort:true,
 		pruneModifiedRecords: true,
 		sortInfo:{field: 'ipqamOutputQAMChannel', direction: 'ASC'},
 		groupField:'ipqamOutputQAMChannel',
 		proxy:new Ext.data.MemoryProxy(ipqamStreamMapListInfo)
 	});
 	streamMapInfoGroupstore.load(); */
 	
 	streamMapInfoStore = new Ext.data.JsonStore({
 		totalProperty:"results",
 		root:"data",
//  		proxy:new Ext.data.MemoryProxy(ipqamStreamMapListInfo),
 		url:'/cmc/ipQamChannel/showIpqamStreamMapList.tv?cmcId=' + cmcId,
 		pruneModifiedRecords: true,
 		id:"streamMapInfoStore",
 		fields: ['mappingId', 'ipqamOutputQAMChannel', 'ipqamDestinationIPAddress', 'ipqamUDPPort', 'ipqamActive', 'ipqamStreamType', 'ipqamProgramNumberInput',
 		         'ipqamProgramNumberOutput', 'ipqamPMV', 'ipqamDataRateEnable','ipqamDataRate', 'ipqamPidMapString']
 	});

 	var streamMapInfoCm = new Ext.grid.ColumnModel(streamMapInfoColumns);
 	
 	streamMapInfoGrid = new Ext.grid.EditorGridPanel({
 		id: 'streamMapInfoGridContainer', 
 		region: 'south',
 		width: w-1,
 		height: h/2,
 		//autoHeight: true,
 		// animCollapse: animCollapse,
 		// trackMouseOver: trackMouseOver,
 		border: false, 
 		store: streamMapInfoStore,
 		//store: streamMapInfoGroupstore,
 		// clicksToEdit: 1,
 		cm: streamMapInfoCm,
 		autofill:true,
 		tbar: buildStreamMapToolBar(),
 		sm: sm,
 		cls:'normalTable',
 		viewConfig: {
 			forceFit: true
//  			,
//  			getRowClass: function(record, index){
//  				for (var i = 0; i < inUseIpqamIds.length; i++) {
//  					if(inUseIpqamIds[i] == record.get('ipqamId')){
//  						return 'mark-red';
//  					}
//  				};
//  				return '';
//  			}
 		},
 		title: I18N.CMC.ipqam.programMappingsList//,
 		//renderTo: 'streamMapInfo-div'//,
 		//view: new Ext.grid.GroupingView()
 	});
 	streamMapInfoStore.on("load",function(store ,records) {
		//toolbar
		var obj = $("#QAMChannel");
		var ipqamChannelIdList = new Array();
		var ipqamInfo = store.data.items;//尽量不用outputStatus
		var lg = ipqamInfo.length;
		for (var i = lg - 1; i >= 0; i--) {
			for(var j=0;j<lg - 1;j++){
				if(ipqamInfo[i].data.ipqamOutputQAMChannel!=ipqamChannelIdList[j]){
					ipqamChannelIdList.unshift(ipqamInfo[i].data.ipqamOutputQAMChannel);
				}
				break;
						
			}
			
		};
		var selectOptionS = "<option value='-1'><fmt:message bundle='${cmc}' key='CMC.select.all'/></option>";
		for (var i = 0;i<=ipqamChannelIdList.length-1;i++) {
			selectOptionS +="<option value="+ipqamChannelIdList[i]+">"+ipqamChannelIdList[i]+"</option>"
		};
		//alert(selectOptionS)
		obj.html(selectOptionS);
		
 	})
 	streamMapInfoStore.load();

 }
//说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。 
//调用：accAdd(arg1,arg2) 
//返回值：arg1加上arg2的精确结果 
function accAdd(arg1,arg2){ 
   var r1,r2,m; 
   try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
   try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
   m=Math.pow(10,Math.max(r1,r2));
   return (arg1*m+arg2*m)/m;
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
var ip = '${cmcIpqamInfo.ipqamIpAddr}';
var mask = '${cmcIpqamInfo.ipqamIpMask}';
Ext.onReady(function(){
	w = $(document).width();
    h = $(document).height();
    w = w?w:1080;
    h = h?h:500;

	createStatusInfoList();
	createStreamMapInfoList()
	new Ext.Viewport({
		layout: 'border',
	    items: [{
	        region: 'north',
	        height: 80,
	        border: false,
	        contentEl: 'topPart'
	    },
	    statusInfoGrid,
	    streamMapInfoGrid
	   ]
	});
	
	var ipqamVlanIp = new ipV4Input("ipqamIp","span1");
	var ipqamVlanIpMask = new ipV4Input("ipqamIpMask","span2");
	ipqamVlanIp.width(141);
	ipqamVlanIpMask.width(141);
	setIpValue("ipqamIp", ip);
	setIpValue("ipqamIpMask", mask);
	
	///$("#ipqamIp").attr("name","");
	//$("#ipqamIpMask").attr("name","cmcIpqamInfo.ipqamIpMask");
});
</script>
</head>
<body class="newBody">
	<div id="topPart">
		<div id=header>
			<%@ include file="entity.inc"%>
		</div>
		<div class="edge10">
			<ul class="leftFloatUl">
				<li>
					<a id="refreshBase" href="javascript:;" class="normalBtnBig"  onclick="refreshIpqamStatusAndMappings()">
						<span><i class="miniIcoEquipment"></i><fmt:message bundle='${cmc}' key='text.refreshData'/></span>
					</a>
				</li>
				<li>
					<div id="ipqamIPCfg">
						<form id="ipqamIpInfo" name="ipqamIpInfo" action="#">
					        <table>
					          <tr style="height:28px;">
					            <td width=50px align=right><fmt:message bundle='${cmc}' key='CMC.label.IP'/></td>
					            <td width=150px><span id="span1" name="cmcIpqamInfo.ipqamIp"></span></td>
					            <td width=50px align=right><fmt:message bundle='${cmc}' key='CMC.text.subnetmask'/></td>
					            <td width=150px><span id="span2"></span></td>
					          </tr>
					        </table>
				        </form>
				        
				    </div>
				</li>
				<li>
					<a id="saveIPId" href="javascript:;" class="normalBtnBig"  onclick="modifyIpqamIP()">
						<span><fmt:message bundle='${cmc}' key='CMC.text.modify'/></span>
						</a>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>
