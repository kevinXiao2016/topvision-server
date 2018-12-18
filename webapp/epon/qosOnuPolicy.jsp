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
<!-- ONU侧SP,WRR,SPWRR都支持，但是都不支持带宽 -->
<script type="text/javascript">
/****************
 * 全局变量定义区*
 ****************/
var operationDevicePower= <%=uc.hasPower("operationDevice")%>; 
var entityId = ${entityId};
var onuIndex = ${onuIndex};
//var queue = Ext.decode('<s:property value="QosPortBaseQosPolicyTable"/>');
var queue = ${qosPortBaseQosPolicyTable};
function refreshClick(){
	var params = {
		entityId : entityId
	};
	var url = '/epon/qos/refreshQosOnuPolicy.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.fetchPolicyError)
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.fetchPolicyOk)
			window.location.reload();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.fetchPolicyError)
		},
		params : params
	});
}
/****************
 * 初始区********
 ****************/
$(document).ready(function(){
	//初始化检查
	initcheck();
	/**初始化队列对象**/
	$.each([0,1,2,3,4,5,6,7],function(i,n){
		$("#queue_"+i).find(".weightClass").val(queue.deviceBaseQosPolicyWeightOctetList[i]);
		//var bd = queue.deviceBaseQosPolicySpBandwidthRangeList[i];
		//$("#queue_"+i).find(".bdClass").val(bd==-1?'':bd);
	});
	$("#qosQueuePolicy").val(queue.policyMode);
	//$(".bdClass").css({backgroundColor:'#ffffff'});
	$(".weightClass").css({backgroundColor:'#ffffff'});
	switch(queue.policyMode){
		case 0:
		case 1:
			$(".weightClass").attr("disabled",true).css({backgroundColor:'#EBEADB'});
			break;
		default:
			break;
	}
	startListen();
	//--------ONU侧不支持带宽-----//
	$(".bdClass").attr("disabled",true).hide();
	if(queue.deviceBaseQosPolicyMode != 1){
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
			if (typeof console != 'undefined') 
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
		$("#saveBt").attr("disabled",false);
		//---判断模式，如果所选业务为SP，则禁用权重----//
		if('1' == $("#qosQueuePolicy").val()){
			//----如果是SP，则权重不可用，保存键可用-----//
			$(".weightClass").val('').attr("disabled",true).css({backgroundColor:'#EBEADB'});
		}else{
			//----如果不是SP，则权重可用----//
			$(".weightClass").val(0).attr("disabled",false).css({backgroundColor:'#ffffff'});
		}
	});
	$(".weightClass").bind('keyup',function(){
		var s = this.id.substring(6);
		$("#saveBt").attr("disabled", false);
		if(checkedWeight(s)){
			for(var x=1; x<9; x++){
				if(!checkedWeight(x)){
					$("#saveBt").attr("disabled", true);
					$("#saveBt").mouseout();
					return false;
				}
			}
		}else{
			$("#saveBt").attr("disabled", true);
			$("#saveBt").mouseout();
		}
		var v = $(this).val();
		if(v.indexOf("0") == 0){
			$(this).val(parseFloat(v));
		}
	});
	
	//***为带宽input添加change监听.对于SP业务，只要有改动，则保存按钮可用****//
	$(".bdClass").bind("keyup",function(){
		var bd = $(this).val();
		if(isNaN(bd)){
			//---------提示用户输入不合法---------//
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.invalidChar)
			$(this).val(bd.substring(0,wit.length-1));
			$("#saveBt").attr("disabled",true);
			return;
		}else if(bd>65535){
			$(this).val(65535);
			return false;
		}else if (bd<0){
			$(this).val(0);
			return false;
		}
		//-----turn on the save btn-------//
		$("#saveBt").attr("disabled",false);
	});
	
	//***************TOOLTIP LISTENERS*************//
	$(".weightClass").bind("focus",function(e){
	 	inputFocused(this.id, I18N.QOS.weightTip)
	 });
	 $(".weightClass").bind("blur",function(e){
	 	inputBlured(this.id);
	 	$(this).addClass("weightClass");
	 });
	 $(".bdClass").bind("focus",function(e){
	 	inputFocused(this.id,  I18N.QOS.bdTip)
	 });
	 $(".bdClass").bind("blur",function(e){
	 	inputBlured(this)
	 	$(this).addClass("bdClass")
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
 /**
 * 保存配置
 **/
function saveClick() {
	if($("#qosQueuePolicy").val() != 1){
		$(".weightClass").keyup();
	}
	 	$.each([0,1,2,3,4,5,6,7],function(i,n){
			var weight = $("#queue_"+i).find(".weightClass").val();
	 		//var bd = $("#queue_"+i).find(".bdClass").val();
	 		var bd = 0;
	 		//默认值为0
	 		if( !!!weight || isNaN(parseInt(weight)))
	 			weight = 0;
	 		if( isNaN(parseInt(bd)))
	 			bd = 0;
	 		else if(bd== '')
	 			bd = -1;
			queue.deviceBaseQosPolicyWeightOctetList[i] = weight;
			queue.deviceBaseQosPolicySpBandwidthRangeList[i] = bd;			
		});	
	 	queue.deviceBaseQosPolicyMode = $("#qosQueuePolicy").val();
	 	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.QOS.settingOnuPolicy , 'ext-mb-waiting');
		$.ajax({
			url: '/epon/qos/modifyOnuQosPolicy.tv',
			method:'post',
			cache:false,
			data:'entityId='+entityId+'&onuIndex='+onuIndex+'&deviceBaseQosPolicyWeightOctetList='+queue.deviceBaseQosPolicyWeightOctetList.toString()+'&deviceBaseQosPolicySpBandwidthRangeList='+queue.deviceBaseQosPolicySpBandwidthRangeList.toString()+
				 '&deviceBaseQosPolicyMode='+queue.deviceBaseQosPolicyMode,
			success:function(text){				
				if('success'==text){
					window.parent.showMessageDlg(I18N.COMMON.tip,I18N.QOS.configOk);
					cancelClick();
				}else{
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.cfgError);
				}
			},
			error:function(){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.cfgError);
			}
		});
 }
 function authLoad(){
	 if(!operationDevicePower){
		 $(":input").attr("disabled",true);
		 R.saveBt.setDisabled(true);
	 }
 }
</script>
	</head>
<body class="openWinBody" onload="authLoad()">
	<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@QOS.windowOnuTip@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>

	<div class="edge10">
		<div style="width:100%" class="pB10">
			<label class="blueTxt">@QOS.quenePolicy@: </label> 
			<select id="qosQueuePolicy" style="width: 100px;">
				<option value="1">SP</option>
				<option value="2">WRR</option>
				<!-- <option value="3">SPWRR</option> -->
				<option value="11">RR</option>
			</select>
		</div>
		<div class="halfFloatLeft" style="width: 270px;">
			<div class="zebraTableCaption pB10 pL10 pR10 mB5"  id="queue_0">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@0</span></div>
				<table>
					<tr>
						<td class="w80 txtRight">@QOS.weight@</td>
						<td>
							<input  id='weight1' class='weightClass normalInput' maxlength=2 type="text" />
						</td>
					</tr>
				</table>
			</div>
				
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_2">
			<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@2</span></div>
				<table>
					<tr>
						<td class="w80 txtRight">@QOS.weight@</td>
						<td><input  id='weight3' class='weightClass normalInput' maxlength=2  
							type="text" /></td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_4">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@4</span></div>
				<table>
					<tr>
						<td class="w80 txtRight">@QOS.weight@</td>
						<td><input id='weight5'  class='weightClass normalInput' maxlength=2
							type="text" /></td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_6">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@6</span></div>
					<table>
						<tr>
							<td class="w80 txtRight">@QOS.weight@</td>
							<td><input  id='weight7' class='weightClass normalInput' maxlength=2
								type="text" /></td>
						</tr>
					</table>
			</div>
			
		</div>
		
		
		<div class="halfFloatRight"  style="width: 270px;">
			<div class="zebraTableCaption pB10 pL10 pR10 mB5"  id="queue_1">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@1</span></div>
				<table>
					<tr>
						<td class="w80 txtRight">@QOS.weight@</td>
						<td><input id='weight2'  class='weightClass normalInput' maxlength=2
							type="text" /></td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_3">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@3</span></div>
				<table>
					<tr>
						<td class="w80 txtRight">@QOS.weight@</td>
						<td><input id='weight4'  class='weightClass normalInput' maxlength=2 
							type="text" /></td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_5">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@5</span></div>
				<table>
					<tr>
						<td class="w80 txtRight">@QOS.weight@</td>
						<td><input  id='weight6' class='weightClass normalInput' maxlength=2  
							type="text" /></td>
					</tr>
				</table>
			</div>
			
			<div class="zebraTableCaption pB10 pL10 pR10 mB5" id="queue_7">
				<div class="zebraTableCaptionTitle"><span id="aclIndexText">@QOS.quene@7</span></div>
				<table>
					<tr>
						<td class="w80 txtRight">@QOS.weight@</td>
						<td><input  id='weight8' class='weightClass normalInput' maxlength=2 
							type="text" /></td>
					</tr>
				</table>
			</div>
		</div>
	</div>


		<Zeta:ButtonGroup>
			<Zeta:Button onClick="refreshClick()" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()">@COMMON.saveCfg@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
		</Zeta:ButtonGroup>
	<div class=formtip id=tips style="display: none"></div>
</body>
	<script>

/**
 * 关闭当前页面
 */
function cancelClick() {
    window.parent.closeWindow('qosOnuPolicy');
}
</script>
</Zeta:HTML>