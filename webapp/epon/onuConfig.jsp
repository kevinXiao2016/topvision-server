<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
Ext.BLANK_IMAGE_URL = '../images/s.gif';
var entityId = <s:property value="entityId"/>;
var onuId = <s:property value = "onuId"/>;
var sinceOnuName = '<s:property value="oltOnuAttribute.onuName"/>';//用于标识真实值的
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var onuName;//用于设置的

function saveClick() {
	onuName = $("#onuName").val();
	//ONU别名修改提交验证
	showWaitingDlg(I18N.COMMON.wait, I18N.ONU.mdfingOnuAlias , 'ext-mb-waiting');	
	$.ajax({
		url: '/onu/modifyOnuName.tv', 
		data: "entityId="+entityId+"&onuId="+onuId+"&onuName="+onuName,
        success: function(text) {
	        if(text == 'success'){
	        	sinceOnuName = onuName;
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setOnuAliasOk )
				updateOnuJson(onuName);
				cancelClick();
	        }else{
	        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
		    }
        }, error: function(text) {
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setOnuAliasEr ,'error');
    	}, cache: false
    });
}
//ONU别名修改验证
/* function onuNameCheck(){
	$("#saveBt").attr("disabled",false);
	$("#onuName").css("border","1px solid #8bb8f3");
	if($("#onuName").val().length > 32){
		$("#onuName").css("border","1px solid #FF0000");
		$("#saveBt").attr("disabled",true);
		$("#onuName").focus();
		return false;
	}
	if ($("#onuName").val()==sinceOnuName){
		$("#saveBt").attr("disabled",true);
		return false;
	}
	return true;
} */

function cancelClick() {
	window.parent.closeWindow('onuNameConfig');
}
function updateOnuJson(text) {
	try{
		if(window.parent.getFrame("entity-" + entityId) != null)
			window.parent.getFrame("entity-" + entityId).page.changeEntityName(text)
		else if(window.parent.getFrame("onuList"))
			window.parent.getFrame("onuList").onRefreshClick()
	}catch(e){}
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function authLoad(){
	var ids = new Array();
	ids.add("onuName");
	ids.add("saveBt");
	operationAuthInit(operationDevicePower,ids);
}
</script>
</head>
	<body class="openWinBody" onload="authLoad()">
		<div class="openWinHeader">
			<div class="openWinTip">@ONU.plsIptOnuName@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
				<tr>
					<td class="withoutBorderBottom rightBlueTxt w220">@ONU.onuName@:</td>
					<td class="withoutBorderBottom"><input id=onuName maxlength=10
						class="normalInput w150" toolTip='@ONU.plsIptOnuName@' type=text
						value="${oltOnuAttribute.onuName}" /></td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="saveClick()">@COMMON.saveCfg@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>