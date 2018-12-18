<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var onuId =${onuId};
var onuLevel = ${onuLevel};

function saveClick() {
	var onuLevel = $("#onuLevel").val();
	window.parent.showWaitingDlg('@COMMON.wait@', '@ONU/ONU.ServiceLevel2@' , 'ext-mb-waiting');
	$.ajax({
		url: '/onu/saveOnuServerLevel.tv', 
		type: 'POST',
		data: 'onuId='+onuId+'&onuLevel='+onuLevel,
		success: function(text) {
			top.nm3kRightClickTips({
    			title: '@COMMON.tip@',
    			html: '@ONU/ONU.ServiceLevelConfigSuccess@'
    		});
			top.frames["frameonuList"].trapReloadStore();
			cancelClick();
		}, error: function() {
			top.nm3kRightClickTips({
				title: '@COMMON.tip@',
				html: '@ONU/ONU.ServiceLevelConfigFailed@'
			});
		}, cache: false
	});//提交ONURSTP桥模式的修改
}
function cancelClick() {
	window.parent.closeWindow('onuServerLevel');
}

function authLoad(){
	$("#onuLevel").val(onuLevel);
}
</script>
</head>
	<body class=openWinBody onload="authLoad();">
		<div class="openWinHeader">
			<div class="openWinTip">@ONU/ONU.ServiceLevel1@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT80">
			<table class="zebraTableRows">
				<tr>
					<td class="withoutBorderBottom rightBlueTxt w200">@ONU/ONU.ServiceLevel@:</td>
					<td class="withoutBorderBottom">
					<select id="onuLevel" class="normalSel w200">
							<option value="0">@ONU/ONU.Default@</option>
							<option value="1">@ONU/ONU.ImportantONU@</option>
							<option value="2">@ONU/ONU.CommonMDU@</option>
							<option value="3">@ONU/ONU.CommonSFU@</option>
					</select>
					</td>
				</tr>
			</table>
		</div>
		<div class="edgeTB10LR20 pT60">
		<Zeta:ButtonGroup>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.save@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
		</div>
	</body>
</Zeta:HTML>