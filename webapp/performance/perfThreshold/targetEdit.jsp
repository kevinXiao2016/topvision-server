<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module performance
</Zeta:Loader>
<style type="text/css">

</style>
<script type="text/javascript">
	var targetId = '${perfTarget.targetId}';
	var regValue = '${perfTarget.regexpValue}';
	var maxValue = '${perfTarget.maxNum}';
	var minValue = '${perfTarget.minNum}';
	var regRule;
	
	function validateInput(maxInput, minInput){
		if(!regRule.test(maxInput)){
			$("#maxValue").focus();
			return false;
		}
		
		if(!regRule.test(minInput)){
			$("#minValue").focus();
			return false;
		}
		if(Number(minInput) > Number(maxInput)){
			$("#maxValue").focus();
			return false;
		}
		return true;
	}
	
	function saveClick(){
		var maxValue = $("#maxValue").val();
		var minValue = $("#minValue").val();
		if(!validateInput(maxValue, minValue)){
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		//校验
		$.ajax({
			url : '/performance/perfThreshold/modifyTarget.tv',
			type : 'post',
			dataType: 'json',
			data : {
				targetId : targetId,
				maxValue : maxValue,
		    	minValue : minValue
			},
			success : function(json) {
			 	top.afterSaveOrDelete({
           	      title: "@COMMON.tip@",
           	      html: '<b class="orangeTxt">@Performance.updateTargetSuccess@</b>'
           	    });
			 	top.getFrame("thresholdTargetManage").doRefresh();
				cancelClick();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@Performance.updateTargetFail@");
			},
			cache : false
		}); 
	}
	
	function cancelClick(){
		window.parent.closeWindow('targetEdit');
	}
	
	function renderValue(regCase){
		//填充值
		var maxTip, minTip;
		switch(regCase){
			case 0 : 
				maxValue = parseInt(maxValue);
				minValue = parseInt(minValue);
				regRule = /^-?\d+$/;
				maxTip = "@Performance.inputMaxValue@" + "," + "@Performance.noDecimal@";
				minTip = "@Performance.inputMinValue@" + "," + "@Performance.noDecimal@";
				$(".openWinTip").text("@Performance.inputNewValue@" + "," + "@Performance.noDecimal@");
				break;
			case 1 : 
				maxValue = parseFloat(maxValue);
				minValue = parseFloat(minValue);
				regRule = /^(-?\d+)(\.\d{0,1})?$/;
				maxTip = "@Performance.inputMaxValue@" + "," + "@Performance.oneDecimal@";
				minTip = "@Performance.inputMinValue@" + "," + "@Performance.oneDecimal@";
				$(".openWinTip").text("@Performance.inputNewValue@" + "," + "@Performance.oneDecimal@");
				break;
			case 2 : 
				maxValue = parseFloat(maxValue);
				minValue = parseFloat(minValue);
				regRule = /^(-?\d+)(\.\d{0,2})?$/;
				maxTip = "@Performance.inputMaxValue@" + "," + "@Performance.twoDecimal@";
				minTip = "@Performance.inputMinValue@" + "," + "@Performance.twoDecimal@";
				$(".openWinTip").text("@Performance.inputNewValue@" + "," + "@Performance.twoDecimal@");
				break;
		}
		$("#maxValue").val(maxValue);
		$("#minValue").val(minValue);
		$("#maxValue").attr("toolTip", maxTip);
		$("#minValue").attr("toolTip", minTip);
	}
	
	$(function(){
		//填充值
		renderValue(parseInt(regValue));
	});
	
</script>
</head>
<body>
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="180">
						@Performance.maxValue@:
	                 </td>
	                 <td>
						<input id="maxValue" type="text" class="w200 normalInput" maxlength='10'/> <span>${perfTarget.unit}</span>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						@Performance.minValue@:
	                 </td>
	                 <td>
						<input id="minValue" type="text" class="w200 normalInput" maxlength='10'/> <span>${perfTarget.unit}</span>
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		         <li><a href="javascript:;" class="normalBtnBig" onclick="saveClick();"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick();"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>
