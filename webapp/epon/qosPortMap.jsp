<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script>
/*****************
 * 全局变量定义区***
 ****************/
var entityId =  ${entityId};//有时table为空，即新建，则两个参数一定要单独提出来
var portIndex = ${portIndex};
var PortQosMap = ${qosPortBaseQosMapTable} ? ${qosPortBaseQosMapTable}:{portBaseQosMapOctetList:[]};
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
/**长度为8的列队**/
var queue = new Array(8);
/*****************
 * 初始化区***
 ****************/
$(document).ready(function(){
	/***清空队列**/
	queue = [];
	$.each([0,1,2,3,4,5,6,7],function(i,n){
		 	//不能使用add
			queue[i] = PortQosMap.portBaseQosMapOctetList[i];		 
	});	
	initSelection();//初始化8个队列			
//END OF DOCUMENT READY
});
/**
 * 为8个队列分配cos值
 */
function initSelection() {
	//初始化页面数据
	$.each(queue,function(i,cos){
		try{
			$("#qosQueueMap_"+i).attr('value',cos);
		}catch(e){
			window.parent.showConfirmDlg(I18N.QOS.warn , e)
		}
	});
	/**初始化选择后添加事件监听**/
	startListen();
}

/*********************************************
 			数据操作区
 ********************************************/

 /********************************************
 			 添加监听
  *******************************************/
 function startListen(){
 	$("select").bind('change',function(){
 		$(this).css("color",'green');
 	});
 	
 }
 /********************************************
 			 保存配置 
  *******************************************/
  function saveClick() {
 	 var result = new Array();
 	 //保证顺序的正常
 	 $.each([0,1,2,3,4,5,6,7],function(i,n){
 		 	var obj = $("#qosQueueMap_"+i).val();
 		 	//不能使用add
 			result[i] = obj;		 
 	 });
 	 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving , 'ext-mb-waiting');
 	 $.ajax({   
 		url : '/epon/qos/modifyPortQosMapRule.tv',
 		method:'post',
 		cache:false,
 		data: "entityId=" + entityId +'&portBaseQosMapOctetList='+result.toString()+'&portIndex='+portIndex,
 		success :function(response){
 			if('success' == response){
 				window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.configOk)
 				cancelClick();
 			}else{
 				window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.cfgError)
 			}
 		},
 		error:function(){
 			window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.cfgError)
 		},
 		complete: function (XHR, TS) { XHR = null }
 	 });
  }
 function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait , I18N.COMMON.fetching)
	$.ajax({
        url: '/epon/qos/refreshPortMap.tv',
        data: "&entityId=" + entityId + "&num=" + Math.random(),
        dataType:"text",
        success: function(text) {
	        if(text == 'success'){
            	window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.fetchQueneOk)
            	window.location.reload()
	        }else{
	        	window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.fetchQueneError)
		    }
        }, error: function(text) {
        	window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.fetchQueneError)
	    }, cache: false
	    , complete: function (XHR, TS) { XHR = null }
	})
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#cancelBt").attr("disabled",false);
		R.okBt.setDisabled(true);
	}
}
function cancelClick() {
    window.parent.closeWindow('qosPortMap');
}
</script>
</head>
<body class=openWinBody onload="authLoad();">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@EPON.qosQueneMap@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows">
				<tr>
					<td colspan=4><label for="name">@QOS.priorityPolicy@:COS</label>
				</tr>
				<tr>
					<td class="rightBlueTxt w100"><label for="name">COS 0:</label>
					<td><select id="qosQueueMap_0" class="normalSel w130">
							<option value="0" selected>@QOS.quene@0</option>
							<option value="1">@QOS.quene@1</option>
							<option value="2">@QOS.quene@2</option>
							<option value="3">@QOS.quene@3</option>
							<option value="4">@QOS.quene@4</option>
							<option value="5">@QOS.quene@5</option>
							<option value="6">@QOS.quene@6</option>
							<option value="7">@QOS.quene@7</option>
					</select></td>
					<td class="rightBlueTxt"><label for="name">COS1:</label>
					<td><select id="qosQueueMap_1" class="normalSel w130">
							<option value="0" selected>@QOS.quene@0</option>
							<option value="1">@QOS.quene@1</option>
							<option value="2">@QOS.quene@2</option>
							<option value="3">@QOS.quene@3</option>
							<option value="4">@QOS.quene@4</option>
							<option value="5">@QOS.quene@5</option>
							<option value="6">@QOS.quene@6</option>
							<option value="7">@QOS.quene@7</option>
					</select></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt"><label for="name">COS 2:</label>
					<td><select id="qosQueueMap_2" class="normalSel w130">

							<option value="0" selected>@QOS.quene@0</option>
							<option value="1">@QOS.quene@1</option>
							<option value="2">@QOS.quene@2</option>
							<option value="3">@QOS.quene@3</option>
							<option value="4">@QOS.quene@4</option>
							<option value="5">@QOS.quene@5</option>
							<option value="6">@QOS.quene@6</option>
							<option value="7">@QOS.quene@7</option>
					</select></td>
					<td class="rightBlueTxt"><label for="name">COS 3:</label>
					<td><select id="qosQueueMap_3" class="normalSel w130">
							<option value="0" selected>@QOS.quene@0</option>
							<option value="1">@QOS.quene@1</option>
							<option value="2">@QOS.quene@2</option>
							<option value="3">@QOS.quene@3</option>
							<option value="4">@QOS.quene@4</option>
							<option value="5">@QOS.quene@5</option>
							<option value="6">@QOS.quene@6</option>
							<option value="7">@QOS.quene@7</option>
					</select></td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label for="name">COS 4:</label>
					<td><select id="qosQueueMap_4" class="normalSel w130">
							<option value="0" selected>@QOS.quene@0</option>
							<option value="1">@QOS.quene@1</option>
							<option value="2">@QOS.quene@2</option>
							<option value="3">@QOS.quene@3</option>
							<option value="4">@QOS.quene@4</option>
							<option value="5">@QOS.quene@5</option>
							<option value="6">@QOS.quene@6</option>
							<option value="7">@QOS.quene@7</option>
					</select></td>
					<td class="rightBlueTxt"><label for="name">COS 5:</label>
					<td><select id="qosQueueMap_5" class="normalSel w130">
							<option value="0" selected>@QOS.quene@0</option>
							<option value="1">@QOS.quene@1</option>
							<option value="2">@QOS.quene@2</option>
							<option value="3">@QOS.quene@3</option>
							<option value="4">@QOS.quene@4</option>
							<option value="5">@QOS.quene@5</option>
							<option value="6">@QOS.quene@6</option>
							<option value="7">@QOS.quene@7</option>
					</select></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt"><label for="name">COS 6:</label>
					<td><select id="qosQueueMap_6" class="normalSel w130">
							<option value="0" selected>@QOS.quene@0</option>
							<option value="1">@QOS.quene@1</option>
							<option value="2">@QOS.quene@2</option>
							<option value="3">@QOS.quene@3</option>
							<option value="4">@QOS.quene@4</option>
							<option value="5">@QOS.quene@5</option>
							<option value="6">@QOS.quene@6</option>
							<option value="7">@QOS.quene@7</option>
					</select></td>
					<td class="rightBlueTxt"><label for="name">COS 7:</label>
					<td><select id="qosQueueMap_7" class="normalSel w130">
							<option value="0" selected>@QOS.quene@0</option>
							<option value="1">@QOS.quene@1</option>
							<option value="2">@QOS.quene@2</option>
							<option value="3">@QOS.quene@3</option>
							<option value="4">@QOS.quene@4</option>
							<option value="5">@QOS.quene@5</option>
							<option value="6">@QOS.quene@6</option>
							<option value="7">@QOS.quene@7</option>
					</select></td>
				</tr>
			</table>
		</form>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="okBt" onClick="saveClick()" icon="miniIcoData">@COMMON.saveCfg@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>