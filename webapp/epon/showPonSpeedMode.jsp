<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = '${ponPortSpeedEntry.entityId}';
var ponId = '${ponPortSpeedEntry.ponId}';
var ponSpeedMode = '${ponPortSpeedEntry.ponPortSpeedMod}';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;

function cancelClick() {
    window.parent.closeWindow('ponSpeedMode');
}

function saveClick(){
	var ponSpeedMode = $("#portSpeedMode").val();
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving ,'ext-mb-waiting');
	$.ajax({
		url:'/epon/modifyPonSpeedMode.tv',cache:false,
		data:{
			entityId:entityId,ponId:ponId,ponSpeedMode:ponSpeedMode
		},success:function(text){
			if(text == 'success'){
				window.parent.showMessageDlg(I18N.COMMON.message, I18N.EPON.ponSpeedSetSuc);
			}else{
				window.parent.showMessageDlg(I18N.COMMON.message, I18N.EPON.ponSpeedSetFail);
			}
			window.parent.getFrame("entity-" + entityId).updateOltJson('${currentId}','ponSpeedMode',ponSpeedMode);
       	 	cancelClick();
		},error:function(){
			window.parent.showMessageDlg(I18N.COMMON.message, I18N.EPON.ponSpeedSetFail);
       	 	cancelClick();
		}
	})
}

$(document).ready(function(){
	$("#portSpeedMode").val(ponSpeedMode);
	
	//操作权限控制
	if(!operationDevicePower){
	    $("#portSpeedMode").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
});

</script>
</head>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@EPON.ponSpeedModeConfig@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>

	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="rightBlueTxt w220">@EPON.portMode@</td>
					<td><select id="portSpeedMode" class="normalSel w200">
							<option value=2>1G</option>
							<s:if test="portType == 'tengeEpon' ">
								<option value="4">10G</option>
							</s:if>
							<s:else>
								<option value="3">2G</option>
							</s:else>
					</select></td>
				</tr>
				<tr>
					<td colspan=2 class="txtCenter"><b>@EPON.portDesc@</b></td>
				</tr>
			</table>
		</form>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button id="saveBt" onClick="saveClick()">@COMMON.saveCfg@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>