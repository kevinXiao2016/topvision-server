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
var entityId = '${entityId}';
var mirrorId = '${sniMirrorGroupIndex}';
var mirrorName = '${sniMirrorGroupName}';
function doOnload(){
	$("#mirrorName").val(mirrorName);
}
function checkChange(){
}
function checkMirrorName(){
	var reg1 = /^[a-zA-Z0-9_\s]+$/;
	var mirrorName = $("#mirrorName").val();
	if(mirrorName == "" ||mirrorName == null||mirrorName.length>31){
		return false;
	}else{
		if(reg1.exec(mirrorName)){
			return true;
		}else{
			return false;
		}
	}
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function saveClick(){
	if(!checkMirrorName()){
		 Zeta$("mirrorName").focus();
		 return;
	}
    var mirrorName = $("#mirrorName").val();
    showWaitingDlg(I18N.COMMON.wait, I18N.MIRROR.mdfingMirrorName , 'ext-mb-waiting');
	Ext.Ajax.request({
		url:"/epon/mirror/modifyMirrorName.tv?entityId="+entityId+"&sniMirrorGroupName="+mirrorName+"&sniMirrorGroupIndex="+mirrorId,
		method:"post",
		//async: false,
		success:function(response){
			if(response.responseText == "success"){
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({        
					title: '@COMMON.tip@',        
					html: '<b class="orangeTxt">@MIRROR.mdfMirrorNameOk@</b>'    
				});
				try{
					window.parent.getFrame("entity-" + entityId).updateOltMirrorListJson();
				}catch(e){}
				window.parent.closeWindow('setMirrorName');
		    }else{
		    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.MIRROR.mdfMirrorNameEr );
			}
		},failure:function (response) {
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.MIRROR.mdfMirrorNameEr);
        }})
}
function cancelClick() {
    window.parent.closeWindow('setMirrorName');
}
</script>
</head>
	<body class=openWinBody onload="doOnload()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@MIRROR.modifyMirrorName@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt w240 withoutBorderBottom">@MIRROR.mirrorName@:</td>
						<td class="withoutBorderBottom"><input type=text id="mirrorName" class="normalInput" maxlength="31"  tooltip="@MIRROR.mirrorNameTip2@" /></td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="saveClick()" icon="miniIcoSaveOk">@COMMON.confirm@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>