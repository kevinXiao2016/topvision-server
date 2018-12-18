<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module  performance
    css  css.performanceMajorStyle
    import performance.js.performanceMajor
</Zeta:Loader>
<style type="text/css">
#button-ct{
	position: absolute;
	top: 105px;
	left: 370px;
}
</style>
<script type="text/javascript">
function cancel(){
	parent.getFrame("eponPerfParamConfig").onRefreshClick();
	window.parent.closeWindow("batchModifyOnePerf");
}

function validate(){
	var result = true;
	if($("#perfSwitch").attr("checked")){
		var reg = /^\d{1,4}$/;
		var value = $("#collectCycle").val();
		if(reg.test(value) && value>=1 && value<=1440){
		}else{
			$("#collectCycle").focus();
			result = false;
		}
	}
	return result;
}

function save(){
	if(!validate()){return};
	var perfTargetName = $("#perfTargetSelect option:selected").attr("id");
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
	$.ajax({
		url: '/epon/perfTarget/batchModifyOltSinglePerfTarget.tv',
    	type: 'POST',
    	data: {perfTargetName: perfTargetName, perfSwitch:$("#perfSwitch").val(), collectCycle:$("#collectCycle").val(), entityIds: $("#ids").val()},
   		success: function() {
   			window.parent.closeWaitingDlg();
   			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
	   			html: '<b class="orangeTxt">@Tip.modifySuccess@</b>'
   	   		});
   			cancel();
   		}, error: function() {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

$(document).ready(function() {
	$("#perfSwitch").bind("click", function(){
		if($(this).attr("checked")){
			$(this).val(1);
		}else{
			$(this).val(0);
		}
	})

});//end document.ready;
</script>
</head>
<body class="openWinBody">
	<div class="formtip" id="tips" style="display: none;"></div>
	<div id="button-ct" style="z-index: 999;"></div>
	<input type="hidden" id="ids" value="<s:property value="entityIds"/>"/>
	<div class="openWinHeader">
		<div class="openWinTip">@Tip.oltBatchOneTitle@</div>
    	<div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
     	<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
            <tbody>
                <tr>
                    <td class="rightBlueTxt" width="200">
                     @Performance.perftarget@:
                    </td>
                    <td>
                     	<!-- <input id="perfName" disabled="disabled" type="text" class="normalInputDisabled"/>-->
                     	<select id="perfTargetSelect" class="normalSel">
                     		<optgroup label="@Performance.service@">
                     			<option id="cpuUsed">@Performance.cpuUsed@</option>
                     			<option id="memUsed">@Performance.memUsed@</option>
                     			<option id="flashUsed">@Performance.flashUsed@</option>
                     			<option id="boardTemp">@Performance.boardTemp@</option>
                     			<option id="fanSpeed">@Performance.fanSpeed@</option>
                     			<option id="optLink">@Performance.optLink@</option>
                     		</optgroup>
                     		<optgroup label="@Performance.flow@">
                     			<option id="sniFlow">@Performance.sniFlow@</option>
                     			<option id="ponFlow">@Performance.ponFlow@</option>
                     			<option id="onuPonFlow">@Performance.onuPonFlow@</option>
                     			<option id="uniFlow">@Performance.uniFlow@</option>
                     		</optgroup>
                     	</select>
						<!-- <input type="hidden" id="perfId" />  -->
                    </td>
                </tr>
                <tr class="darkZebraTr">
                    <td class="rightBlueTxt">
                     @Tip.collectCycle@:
                    </td>
                    <td>
                 	    <input id="collectCycle" type="text" class="normalInput" maxlength="4" toolTip='@Tip.collectTimeRule@' />
				 	    <input type="checkbox" id="perfSwitch" checked="checked" value="1"/>
						<span>@Tip.open@</span>
				    </td>
                </tr>
            </tbody>
        </table>
    </div>
	<div class="noWidthCenterOuter clearBoth">
    	 <ol class="upChannelListOl pB0 pT80 noWidthCenter">
         	<li><a href="javascript:save();" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@Tip.save@</span></a></li>
         	<li><a href="javascript:cancel();" class="normalBtnBig"><span>@Tip.off@</span></a></li>
    	 </ol>
	</div>
</body>
</Zeta:HTML>