<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Jquery
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = '${entityId}';
var ponId = '${ponId}';
var ponMacAddrLearnMaxNum = '${ponPortMacAddrLearnMaxNum}';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
function initData() {
	$('#ponMaxNum').val(ponMacAddrLearnMaxNum);
}
function checkValid() {
	var maxLearningNum = parseInt($('#ponMaxNum').val());
	var reg = /^([0-9])+$/;
	if (!(reg.exec(maxLearningNum) && maxLearningNum >= 0 && maxLearningNum <= 32767)) {
		$("#ponMaxNum").focus();
		return false;
	}
	return true;
}
function saveClick() {
	if (!checkValid()) {
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@SERVICE.settingMacMaxLearn@" , 'ext-mb-waiting');
	$.ajax({
		url : '/epon/modifyPonMaxLearnMacNum.tv?r=' + Math.random(),
		data:{	
			entityId : entityId,
			ponId : ponId,
			ponPortMacAddrLearnMaxNum : $('#ponMaxNum').val()
		},
		success : function() {
			window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.setMacMaxLearnOk@" )
			cancelClick();
		},
		error : function() {
			window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.setMacMaxLearnEr@" )
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('ponMacAddrMaxLearningNum');
}
function authLoad(){
	if(!operationDevicePower){
	    $("#ponMaxNum").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
<body class=openWinBody onload="initData();authLoad()">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@SERVICE.ponMacMaxLearn@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edge10 pT20">
		<table class="mCenter zebraTableRows" >
			<tr>
				<td class="rightBlueTxt w240 withoutBorderBottom">@SERVICE.macMaxLearn@:</td>
				<td class="withoutBorderBottom">
					<input id="ponMaxNum" maxlength="5"  class="normalInput w130" tooltip="@SERVICE.range32767@" />
				</td>
			</tr>
		</table>
	</div>
		
	<Zeta:ButtonGroup>
		<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid" >@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>