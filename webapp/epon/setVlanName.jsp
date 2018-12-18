<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
	import js.tools.ipText
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var vlanObject = ${oltVlanObject};
if(vlanObject == "false"){
	vlanObject = {oltVlanName : ""};
}
var vlanIndex = '<s:property value = "vlanIndex"/>';
var modifyFlag = '<s:property value = "modifyFlag"/>';
var vlanName = vlanObject.oltVlanName;
var topMcFloodMode = '<s:property value = "topMcFloodMode"/>';
function doOnload(){
	if(modifyFlag == 'true'){
		$("#vlanName").val(vlanName);
		if(topMcFloodMode == 1 || topMcFloodMode == 2 || topMcFloodMode == 3){
			$("#topMcFloodMode").val(topMcFloodMode);
		}else{
			$("#topMcFloodMode").val(2);
		}
	}
	if(modifyFlag == 'false'){
		vlanName = "VLAN"+"_"+vlanIndex;
		$("#vlanName").val(vlanName);
		$("#topMcFloodMode").val(2);
	}
	//对于8602G设备屏蔽always flooding组播控制模式
	if(EntityType.is8602G_OLT(${entity.typeId})){
		$("#topMcFloodMode").html('<option value="2">unknown flooding</option><option value="3">no flooding</option>')
	}else{
		$("#topMcFloodMode").html('<option value="1">always flooding</option><option value="2">unknown flooding</option><option value="3">no flooding</option>')
	}
}
function checkVlanName(){
	var reg = /[\(\)&+]/ig;
	vlanName = $("#vlanName").val();
	if(vlanName && isNotChineseStr(vlanName) && !reg.exec(vlanName)){
		return true;
	}
	return false;
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function saveClick(){
	if(!checkVlanName()){
		 Zeta$("vlanName").focus();
		 return;
	}
	vlanName = $("#vlanName").val();
    topMcFloodMode = $("#topMcFloodMode").val();
	if(modifyFlag == 'false'){
	    showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.addingVlan , 'ext-mb-waiting');
	    $.ajax({
			url:"/epon/vlan/addOltVlan.tv?entityId="+entityId+"&vlanIndex="+vlanIndex+"&oltVlanName="+vlanName+"&topMcFloodMode="+topMcFloodMode,
			method:"post",
			//async: false,
			success:function(text){
				    if(text == "success"){
						window.parent.closeWaitingDlg();
						try{
							window.parent.getFrame("entity-" + entityId).updateOltVlanListJson(modifyFlag);
						}catch(e){}
						
						if(window.parent.getWindow("igmpProxyDetail") != null){
							window.parent.getWindow("igmpProxyDetail").body.dom.firstChild.contentWindow.satVlanList()
						}
						window.parent.closeWindow('setVlanName');
					 }else{
						 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.addVlanEr );
					 }
			},error:function (response) {
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.addVlanEr );
	        }})
	 }else if(modifyFlag=='true'){
	    showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.mdfingVlan , 'ext-mb-waiting');
	    $.ajax({
			url:"/epon/vlan/modifyVlanName.tv?entityId="+entityId+"&vlanIndex="+vlanIndex+"&oltVlanName="+vlanName+"&topMcFloodMode="+topMcFloodMode,
			method:"post",
			//async: false,
			success:function(text){
				if(text == "success"){
					window.parent.closeWaitingDlg();
					try{
						window.parent.getFrame("entity-" + entityId).updateOltVlanListJson(modifyFlag);
					}catch(e){}
					window.parent.closeWindow('setVlanName');
				} else {
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfingVlanEr );
				}
			},error:function (response) {
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfingVlanEr );
	        }})
		 }
}
function cancelClick() {
    window.parent.closeWindow('setVlanName');
}
</script>
</head>
	<body class=openWinBody onload="doOnload()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@VLAN.mdfVlanName@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
				<tr>
					<td class="rightBlueTxt w200">@VLAN.vlanDesc@:</td>
					<td><input type=text id="vlanName" maxlength=31
						class="normalInput w150" tooltip="@VLAN.vlanNameTip@" /></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@VLAN.mvlanCtrMode@:</td>
					<td><select id="topMcFloodMode" class="normalSel w150">
							<!-- <option value="1">always flooding</option>
							<option value="2">unknown flooding</option>
							<option value="3">no flooding</option> -->
					</select></td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="saveClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>