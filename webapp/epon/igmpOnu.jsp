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
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = <s:property value="entityId"/>;
var onuIndex = <s:property value="onuIndex"/>;
var value = ${onuValue} ? ${onuValue} : new Array();

function cancelClick(){
	window.parent.closeWindow('igmpOnu');
}
function saveClick(){
	var onuIndexHex = onuIndex.toString(16);
	var slotNo = onuIndexHex.substring(0,onuIndexHex.length-8);
	var ponNo = onuIndexHex.substring(onuIndexHex.length-8,onuIndexHex.length-6);
	var onuNo = onuIndexHex.substring(onuIndexHex.length-6,onuIndexHex.length-4);
	if(ponNo.substring(0,1) == '0'){
		ponNo = ponNo.substring(1);
	}
	if(onuNo.substring(0,1) == '0'){
		onuNo = onuNo.substring(1);
	}
	var onuLoc = slotNo +"/"+ ponNo +":"+ onuNo;
	var mode = parseInt($("#igmpMode").val());
	var fast = parseInt($("input:checked").val());
	var params = {
		entityId : entityId,
		onuIndex : onuIndex,
		onuIgmpMode : mode,
		fastLeaveEnabled : fast
	};
	var url = '/epon/igmp/modifyIgmpMcOnuInfo.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.IGMP.mdfingOnuIgmp,onuLoc), 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				top.nm3kRightClickTips({
    				title: I18N.COMMON.tip,
    				html: String.format(I18N.IGMP.mdfOnuIgmpError , onuLoc)
    			});
				return;
			}
			top.nm3kRightClickTips({
				title: I18N.COMMON.tip,
				html: String.format(I18N.IGMP.mdfOnuIgmpOk , onuLoc)
			});
			window.parent.getFrame("entity-" + entityId).modifyIgmpOnuMode(onuIndex, mode)
			cancelClick();
		},
		failure : function() {
			top.nm3kRightClickTips({
				title: I18N.COMMON.tip,
				html: String.format(I18N.IGMP.mdfOnuIgmpError , onuLoc)
			});
		},
		params : params
	});	
}
function checkedChange(){
	var mode = $("#igmpMode").val()
	if(mode == 3){
		$("#fastLeave").attr("disabled",true)
		var fast = $("input:checked").val()
		if(parseInt(mode)==value[0] && parseInt(fast)==value[1]){
			$("#saveBt").attr("disabled",true)
		}else{
			$("#saveBt").attr("disabled",false)
		}
	}else{
		$("#fastLeave").attr("disabled",false)
		var fast = $("input:checked").val()
		if(parseInt(mode)==value[0] && parseInt(fast)==value[1]){
			$("#saveBt").attr("disabled",true)
		}else{
			$("#saveBt").attr("disabled",false)
		}
	}
}
Ext.onReady(function(){
	$("#igmpMode").val(value[0]);
	if(value[0] == 3){
		$("#fastLeave").attr("disabled",true)
	}
	if(value[1] == 1){
		$("#fastLeave1").attr("checked",true)
	}else if(value[1] == 2){
		$("#fastLeave2").attr("checked",true)
	}
	if(value[2] == 2){
		$("#oltIgmpMode").html(I18N.IGMP.controlMode)
		$("#oltIgmpMode").css("color","green")
	}else if(value[2] == 3){
		$("#oltIgmpMode").html(I18N.COMMON.close)
		$("#oltIgmpMode").css("color","red")
	}else if(value[2] == 4){
		$("#oltIgmpMode").html(I18N.IGMP.proxyMode)
		$("#oltIgmpMode").css("color","blue")
	}
});	//end of Ext.onReady

function authLoad(){
	if(!operationDevicePower){
	    $("#igmpMode").attr("disabled",true);
	    $("[name='fastLeave']").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}

</script>
</head>
	<body class=openWinBody onload="authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@IGMP.setOnuMode@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>

		<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
				<tr>
					<td class="rightBlueTxt w250">@IGMP.onuIgmp@:</td>
					<td><select id="igmpMode" class="normalSel w130"  onchange=checkedChange()>
							<option value="1">@IGMP.controlMode@</option>
							<option value="2">snooping</option>
							<option value="3">@COMMON.close@</option>
					</select></td>
				</tr>
				<tr>
					<td id="fastLeave" class="rightBlueTxt">@IGMP.fastLeave@:</td>
					<td>
						<input id="fastLeave1" type="radio" name="fastLeave" value=1 onclick="checkedChange()" checked /> @COMMON.allow@ 
						<input id="fastLeave2" type="radio" name="fastLeave" value=2 onclick="checkedChange()" /> @COMMON.allowNo@
					</td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>