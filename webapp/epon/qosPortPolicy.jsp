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
<script type="text/javascript">
window.onerror = function(){return true;}
/****************
 * 全局变量定义区*
 ****************/
var entityId = ${entityId};
var portIndex = ${portIndex};
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var queue = ${qosPortBaseQosPolicyTable}?${qosPortBaseQosPolicyTable}:{policyMode:'',portBaseQosPolicyWeightOctetList:[],portBaseQosPolicySpBandwidthRangeList:[]};
/****************
 * 初始区********
 ****************/
$( DOC ).ready(function(){
	//-----初始化检查------//
	initcheck();
	/**初始化队列对象**/
	$.each([0,1,2,3,4,5,6,7],function(i,n){
		var wei = queue.portBaseQosPolicyWeightOctetList[i];
		$("#queue_"+i).find(".weightClass").val(wei == -1 || wei > 15 ? '' : wei);
		var bd = queue.portBaseQosPolicySpBandwidthRangeList[i];
		$("#queue_"+i).find(".bdClass").val(bd == -1 ? '' : bd);
	});
	$("#qosQueuePolicy").val(queue.policyMode);
	//-----手动初始化样式------//
	$(".bdClass,.weightClass").css({backgroundColor:'#fff'});
	switch(queue.policyMode){
		case 0:
		case 1:
		case 11:
			$(".weightClass").attr("disabled",true).css({backgroundColor:'#EBEADB'});
			break;
		default:
			break;
	}
	//统一添加事件监听
	startListen();
	$("#qosQueuePolicy").change();
	$(".bdClass").keyup();
	if(queue.policyMode != 1){
		$(".weightClass").keyup();
	}
//END OF DOCUMENT READY
});
 
/**
 * 刚进入页面时的数据检测
 */
function initcheck(){
	switch(queue.deviceBaseQosPolicyMode){
		case 0:
		case 1:
		case 2:
		case 3:
		case 11:
			if (typeof console != 'undefined') 
			break;
		default:
			if (typeof console != 'undefined') 
	}
}
/****************
 * 数据操作区*****
 ****************/

/**
 * 为页面各元素添加监听
 */
function startListen(){
	//-----为qos队列策略添加change监听-----//
	$("#qosQueuePolicy").bind('change',function(){
		//**带宽直接带过去，不清零***//
		R.saveBt.setDisabled(false);
		//---判断模式，如果所选业务为SP，则禁用权重----//
		if('1' == $("#qosQueuePolicy").val() || '11' == $("#qosQueuePolicy").val()){
			//----如果是SP/RR，则权重不可用，剩余权重不显示，保存键可用-----//
			$(".weightClass").attr("disabled",true).addClass("normalInputDisabled");
		}else{
			//----如果不是SP/RR，则权重可用----//
			$(".weightClass").attr("disabled",false).removeClass("normalInputDisabled");
		}
	});
	//***每当权重值有输入时计算剩余权重，如果为0则可以提交***//
	$(".weightClass").bind('keyup',function(){
		var s = this.id.substring(6);
		R.saveBt.setDisabled(false);
		if(checkedWeight(s)){
			for(var x=1; x<9; x++){
				if(!checkedWeight(x) || !checkedBd(x)){
					R.saveBt.setDisabled(true);
					return false;
				}
			}
		}else{
			R.saveBt.setDisabled(true);
		}
		var v = $(this).val();
		if(v.indexOf("0") == 0){
			$(this).val(parseFloat(v));
		}
	});
	
	//***为带宽input添加change监听.对于SP业务，只要有改动，则保存按钮可用****//
	$(".bdClass").bind("keyup",function(){
		var s = this.id.substring(2);
		R.saveBt.setDisabled(false);
		if(checkedBd(s)){
			for(var x=1; x<9; x++){
				if(!checkedBd(x) || ($("#qosQueuePolicy").val() == 2 && !checkedWeight(x))){
					R.saveBt.setDisabled(true);
					return false;
				}
			}
		}else{
			R.saveBt.setDisabled(true);
		}
		var v = $(this).val();
		if(v.indexOf("0") == 0){
			$(this).val(parseFloat(v));
		}
	});
	
	//***************TOOLTIP LISTENERS*************//
	$(".weightClass").bind("focus", function(e){
	 	inputFocused(this.id, I18N.QOS.weightTip );
	 });
	 $(".weightClass").bind("blur", function(e){
	 	inputBlured(this.id);
	 	$(this).addClass("weightClass");
	 	$(this).keyup();
	 });
	 $(".bdClass").bind("focus", function(e){
	 	inputFocused(this.id, I18N.QOS.bdTip);
	 });
	 $(".bdClass").bind("blur", function(e){
	 	inputBlured(this);
	 	$(this).addClass("bdClass");
	 	$(this).keyup();
	 });
}
function checkedWeight(s){
	$("#weight" + s).css("border", "1px solid #8bb8f3");
	var v = $("#weight" + s).val();
	if(v == ""){
		return true;
	}else if(isNaN(v) || ("" + v).indexOf(".") > -1 || parseInt(v) < 0 || parseInt(v) > 15){
		$("#weight" + s).css("border", "1px solid #ff0000");
		return false;
	}
	return true;
}
function checkedBd(s){
	$("#bd" + s).css("border", "1px solid #8bb8f3");
	var v = $("#bd" + s).val();
	if(v != ""){
		if(isNaN(v) || ("" + v).indexOf(".") > -1 || parseInt(v) < 0 || parseInt(v) > 65535){
			$("#bd" + s).css("border", "1px solid #ff0000");
			return false;
		}
	}
	return true;
}
 /**
 * 保存配置
 **/
 function saveClick() {
	 if($("#qosQueuePolicy").val() == 2){
		for(x=1; x<9; x++){
			if(!checkedWeight(x)){
				$("#weight" + x).focus();
				R.saveBt.setDisabled(true);
				return false;
			}
		}
	 }
	var flag = false;
 	$.each([0,1,2,3,4,5,6,7],function(i,n){
 		var reg = /^[0-9]\d*$/;
 		
 		var weight = $("#queue_"+i).find(".weightClass").val();
 		var bd = $("#queue_"+i).find(".bdClass").val();
 		//---默认值为0----//
 		if (!reg.exec(weight)) {
 			
 		};
 		if( !!!weight || isNaN(parseInt(weight))){
 			weight = 0;
 		}
 		if ( !reg.exec(bd) || parseInt(bd) > 65535 || parseInt(bd) < 0) {
 			flag = true;
 			window.parent.showMessageDlg(I18N.COMMON.tip,I18N.QOS.bdOutRange, null, function(){
				$("#queue_"+i).find(".bdClass").focus();
 			});
 			return false;
 		}
 		/* 
 		if( isNaN(parseInt(bd)))
 			bd = 0;
 		else if(bd== '')
 			bd = -1;
			if(bd>65535){
				window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.bdOutRange )
				$("#queue_"+i).find(".bdClass").focus();
				throw new Error("band out of range")
			} */
		queue.portBaseQosPolicyWeightOctetList[i] = weight;
		queue.portBaseQosPolicySpBandwidthRangeList[i] = bd;			
	});	
 	if(flag){
	 	return;
 	}
 	queue.policyMode = $("#qosQueuePolicy").val();
 	window.top.showWaitingDlg( I18N.COMMON.wait , I18N.COMMON.saving , 'ext-mb-waiting');
	$.ajax({
		url: '/epon/qos/modifyPortQosPolicy.tv',
		method:'post',
		cache:false,
		data:'entityId='+entityId+'&portIndex='+portIndex+'&portBaseQosPolicyWeightOctetList='+queue.portBaseQosPolicyWeightOctetList.toString()+'&portBaseQosPolicySpBandwidthRangeList='+queue.portBaseQosPolicySpBandwidthRangeList.toString()+
			 '&portBaseQosPolicyMode='+queue.policyMode,
		success:function(response){
			if ('success' == response) {
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
 
/*
 * 从设备刷新数据
 */
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait , I18N.COMMON.fetching)
	$.ajax({
		url: '/epon/qos/refreshPortPolicy.tv',
		type: 'POST',
		data: "&entityId=" + entityId + "&num=" + Math.random(),
		dataType:"text",
		success: function(text) {
			if(text == 'success'){
		        window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.fetchPolicyOk)
		        window.location.reload()
			}else{
			    window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.fetchPolicyError)
			}
		}, error: function(text) {
		    window.parent.showMessageDlg(I18N.COMMON.tip , I18N.QOS.fetchPolicyError)
		}, cache: false
		//, complete: function (XHR, TS) { XHR = null }
	});
}
$.ajaxSetup({
	complete : function(XHR, TS){
		XHR = null;
	}
});
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		R.cancelBt.setDisabled(false);
		R.saveBt.setDisabled(true);
	}
}
</script>
<%-- <style type="text/css">
.halfFloatLeft,.halfFloatRight{width: 45%;margin-left:15px;margin-right: 15px;}
</style> --%>
</head>
<body class="openWinBody" onload="authLoad();">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@QOS.windowOltTip@</div>
		<div class="rightCirIco pageCirIco"></div>
	</div>

	<div class="edge16">
		<div style="width:100%" class="pB10">
			<label class="blueTxt">@QOS.quenePolicy@: </label> 
			<select id="qosQueuePolicy" style="width: 100px;">
				<option value="1">SP</option>
				<option value="2">WRR</option>
				<!-- <option value="3">SPWRR</option> -->
				<option value="11">RR</option>
			</select>
		</div>
		<div class="halfFloatLeft" style="width: 300px;">
			<div class="zebraTableCaption pB10 pL10 pR10 mB5"  id="queue_0">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@0</span></div>
				<table>
					<tr>
						<td class="w50 txtRight" style="padding-left: 5px;">@QOS.weight@</td>
						<td>
							<input  id='weight1' class='weightClass normalInput w60' maxlength=2 type="text" />
						</td>
						<td class="w30 txtRight">@QOS.bd@</td>
						<td align=left>
							<input id='bd1' align=left maxlength=5  
							class="bdClass w70 normalInput" type="text" />Kbps
						</td>
					</tr>
				</table>
			</div>
				
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_2">
			<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@2</span></div>
				<table>
					<tr>
						<td class="w50 txtRight" style="padding-left: 5px;">@QOS.weight@</td>
						<td><input  id='weight3' class='weightClass normalInput w60' maxlength=2  
							type="text" /></td>
						<td class="w30 txtRight">@QOS.bd@</td>
						<td align=left>
							<input id='bd3' align=left maxlength=5	class="bdClass w70 normalInput" type="text" />Kbps
						</td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_4">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@4</span></div>
				<table>
					<tr>
						<td class="w50 txtRight" style="padding-left: 5px;">@QOS.weight@</td>
						<td><input id='weight5'  class='weightClass normalInput w60' maxlength=2
							type="text" /></td>
						<td class="w30 txtRight">@QOS.bd@</td>
						<td align=left><input id='bd5'  align=left maxlength=5
							class="bdClass w70 normalInput" type="text" />Kbps</td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_6">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@6</span></div>
					<table>
						<tr>
							<td class="w50 txtRight" style="padding-left: 5px;">@QOS.weight@</td>
							<td><input  id='weight7' class='weightClass normalInput w60' maxlength=2
								type="text" /></td>
							<td class="w30 txtRight">@QOS.bd@</td>
							<td align=left><input  id='bd7' align=left maxlength=5
								class="bdClass w70 normalInput" type="text" />Kbps</td>
						</tr>
					</table>
			</div>
		</div>
		
		<div class="halfFloatRight" style="width: 300px;">
			<div class="zebraTableCaption pB10 pL10 pR10 mB5"  id="queue_1">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@1</span></div>
				<table>
					<tr>
						<td class="w50 txtRight" style="padding-left: 5px;">@QOS.weight@</td>
						<td><input id='weight2'  class='weightClass normalInput w60' maxlength=2
							type="text" /></td>
						<td class="w30 txtRight">@QOS.bd@</td>
						<td><input  id='bd2' type="text" class="bdClass w70 normalInput" maxlength=5 
							/>Kbps</td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_3">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@3</span></div>
				<table>
					<tr>
						<td class="w50 txtRight" style="padding-left: 5px;">@QOS.weight@</td>
						<td><input id='weight4'  class='weightClass normalInput w60' maxlength=2 
							type="text" /></td>
						<td class="w30 txtRight">@QOS.bd@</td>
						<td><input  id='bd4' type="text" class="bdClass w70 normalInput" maxlength=5 />Kbps</td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_5">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@5</span></div>
				<table>
					<tr>
						<td class="w50 txtRight" style="padding-left: 5px;">@QOS.weight@</td>
						<td><input  id='weight6' class='weightClass normalInput w60' maxlength=2  
							type="text" /></td>
						<td class="w30 txtRight">@QOS.bd@</td>
						<td><input  id='bd6' type="text" class="bdClass w70 normalInput" maxlength=5
							/>Kbps</td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_7">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@7</span></div>
				<table>
					<tr>
						<td class="w50 txtRight" style="padding-left: 5px;">@QOS.weight@</td>
						<td><input  id='weight8' class='weightClass normalInput w60' maxlength=2 
							type="text" /></td>
						<td class="w30 txtRight">@QOS.bd@</td>
						<td><input id='bd8' type="text" class="bdClass w70 normalInput" maxlength=5 
							/>Kbps</td>
					</tr>
				</table>
			</div>
		</div>
	</div>

	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoData">@COMMON.saveCfg@</Zeta:Button>
		<Zeta:Button id="cancelBt" onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
	<script>

/**
 * 关闭当前页面
 */
function cancelClick() {
    window.parent.closeWindow('qosPortPolicy');
}
</script>
</Zeta:HTML>