<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = ${entityId};
var uniId = ${uniId};
var uniIndex = ${uniIndex};
var uniUSUtgPri = '${uniUSUtgPri}';

function cancelClick(){
	window.parent.closeWindow('uniUSUtgPri');
}
function refreshClick(){
	$("#refreshBt").attr("disabled", true).mouseout();
	window.parent.showWaitingDlg(I18N.COMMON.wait , I18N.COMMON.fetching , 'ext-mb-waiting');
	var params = {entityId: entityId, uniId: uniId};
	$.ajax({
		url : '/epon/elec/refreshUniUSUtgPri.tv?&r=' + Math.random(),
		success : function(response) {
			if(response == 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
				location.reload();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
				$("#refreshBt").attr("disabled", false);
			}
		},
		error : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
			$("#refreshBt").attr("disabled", false);
		},
		data: params
	});
}
function saveClick(){
	window.parent.showWaitingDlg(I18N.COMMON.wait , I18N.COMMON.saving , 'ext-mb-waiting');
	var u = $("#priSel").val();
	var params = {entityId : entityId, uniId: uniId, uniUSUtgPri: u};
	$.ajax({
		url : '/onu/setUniUtagPri.tv?&r=' + Math.random(),
		success : function(response) {
			if(response == 'success'){
				/* var pa = "";
				if(window.parent.getFrame("entity-" + entityId) != null){
					pa = window.parent.getFrame("entity-" + entityId);
				}else if(window.parent.getFrame("onuList")){
					pa = window.parent.getFrame("onuList");
				} */
				/* if(pa){
					var tmp = pa.page.grid.getSource();
					pa.entity.onuUniPortList[pa.getNum((parseInt(uniIndex / 256) + (uniIndex % 256)), 4) - 1].uniUSUtgPri = u;
					u = u == 255 ? I18N.ONU.noPri : u;
					tmp[I18N.ONU.uniUSUtgPriSimple] = u;
					pa.page.grid.setSource(tmp);
				} */
				top.afterSaveOrDelete({
                	title:I18N.COMMON.tip,
                	html:I18N.ONU.setUniUSUtgPriSuc
                })
				cancelClick();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setUniUSUtgPriFail);
			}
		},
		error : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setUniUSUtgPriFail);
		},
		data: params
	});
}

$(function(){
	initData();
});
function initData(){
	if(uniIndex){
		var loc = "unknown uniIndex";
		try{
			if(window.parent.getFrame("entity-" + entityId) != null){
				loc = window.parent.getFrame("entity-" + entityId).getLocationByIndex(uniIndex, 'uni');
			}else if(window.parent.getFrame("onuList")){
				loc = window.parent.getFrame("onuList").getLocationByIndex(uniIndex, 'uni');
			}
		}catch (e) {
		}
		$("#uniFont").text(loc);
	}
	$("#priSel").val(uniUSUtgPri);
	$("#saveBt").attr("disabled", false);
	$("#refreshBt").attr("disabled", false);
}

function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#close").attr("disabled",false);
		$("#fetch").attr("disabled",false);
	}
}
</script>
</head>
	<body class=openWinBody onload="authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ONU.uniUSUtgPriConfig@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
				<tr>
					<td class="rightBlueTxt w240">UNI:</td>
					<td id=uniFont></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@ONU.uniUSUtgPri@</td>
					<td><select id=priSel class="normalSel w160">
							<option value=255>@ONU.noPri@</option>
							<option value=0>0</option>
							<option value=1>1</option>
							<option value=2>2</option>
							<option value=3>3</option>
							<option value=4>4</option>
							<option value=5>5</option>
							<option value=6>6</option>
							<option value=7>7</option>
						</select>
					</td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="refreshClick()">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button onClick="saveClick()">@COMMON.save@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
	<style>
</style>
</Zeta:HTML>