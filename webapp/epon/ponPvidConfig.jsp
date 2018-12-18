<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = '${entityId}';
var ponId = '${ponId}';
var portIndex = '${portIndex}';
var vlanPvid = '${vlanPvid}';

function cancelClick() {
    window.parent.closeWindow('ponPvidConfig');
}

function saveClick(){
	var pvid = $("#pvid").val().trim();
	//先执行验证
	
	if(!V.isInRange(pvid, [1, 4094])){
		$("#pvid").focus();
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/modifyPonPvid.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			ponId : ponId,
			portIndex : portIndex,
			vlanPvid : pvid
		},
		dataType :　'json',
		success : function(result) {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@ELEC.cfgOk@</b>'
       	    });
			cancelClick();
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@ELEC.cfgEr@");
		},
		cache : false
	});
}

function refreshData(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/refreshPonVlanInfo.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			ponId : ponId,
			portIndex : portIndex
		},
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
   			});
			window.location.reload();
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

//输入校验: 输入的整数否在范围内
function checkInput(value,compareStart,compareEnd){
	var reg = /^[1-9]\d*$/;	
	if (reg.exec(value) && parseInt(value) <= compareEnd && parseInt(value) >= compareStart) {
		return true;
	} else {
		return false;
	}
}

$(document).ready(function(){
	if(vlanPvid != null && vlanPvid != ''){
		$("#pvid").val(vlanPvid);
	}else{
		$("#pvid").hide();
		$("#exceptionTip").text("@Optical.couldntGetData@").css("display", "block");
		R.savePvid.hide();
	}
		
	//操作权限控制
	if(!operationDevicePower){
	    $("#pvid").attr("disabled",true);
	    R.savePvid.setDisabled(true);
	    R.refreshData.setDisabled(true);
	}
});

</script>
</head>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">
			@PON.pvidConfigTip@
		</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>

	<div class="edge10 pB20 pT30">
		<table class="mCenter zebraTableRows">
			<tr class="withoutBorderBottom">
				<td class="rightBlueTxt w180">PVID:</td>
				<td>
					<input type="text" id="pvid" class="normalInput w180"  maxlength="4" tooltip="@COMMON.range4094@" />
					<span id="exceptionTip" style="display:none"></span>
				</td>
			</tr>
		</table>
	</div>
	<Zeta:ButtonGroup>
			<Zeta:Button id="refreshData" onClick="refreshData()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="savePvid" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>