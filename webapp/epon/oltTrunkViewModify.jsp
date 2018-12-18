<%@ page import="com.topvision.platform.util.EscapeUtil" %>
<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
var entityId = <%=request.getParameter("entityId")%>;
var members = '<%= request.getParameter("sniTrunkGroupConfigGroup") %>';
var trunkIndex = <%=request.getParameter("sniTrunkGroupConfigIndex")%>;
var trunkName = unescape('<%=request.getParameter("sniTrunkGroupConfigName")%>');
var policy = <%=request.getParameter("sniTrunkGroupConfigPolicy")%>;
var reg = /^([A-Za-z0-9])+$/;
function cancelClick() {
	window.parent.closeWindow('modifyTrunkAttr');
}
function modifyTrunkAttr() {
	var name = $('#trunkName').val();
	if (name == '' || name.length < 0 || name.length > 31 || !reg.test(name)) {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.TRUNK.trunkNameNotNull ,"error",function(){
			$("#trunkName").focus();
		});
		return;
	}
	window.top.showWaitingDlg(I18N.COMMON.wait,  I18N.COMMON.saving , 'ext-mb-waiting')
	$.ajax({
		url:"/epon/trunk/modifyTrunkGroup.tv?entityId=" + entityId+'&sniTrunkGroupConfigGroup='+members+'&sniTrunkGroupConfigIndex='+trunkIndex,
        method:"post",
        cache:false,
        data : 'sniTrunkGroupConfigName='+$('#trunkName').val()+'&sniTrunkGroupConfigPolicy='+$('#policy').val(),
        success:function (response) {
        	window.parent.closeWaitingDlg()
        	if('success'==response){	        		
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.TRUNK.mdfOk )
	        	window.parent.getFrame("entity-"+entityId).showMSG()
	        	cancelClick()
	        }else{
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.TRUNK.mdfEr )
		    }
        	
        },
   		error:function () {
   			window.parent.closeWaitingDlg()
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.TRUNK.mdfEr )
    	}
	});
}
$(function() {
	$('#trunkName').val(trunkName);
	//$('#policy').get(0).selectedIndex = policy;
	$("#policy").val(policy);
});
</script>
</head>
	<body class=openWinBody>
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@TRUNK.modifyTrunkProperty@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt w240">@TRUNK.trunkName@:</td>
						<td><input id="trunkName" type="text" maxlength=31 class="normalInput w160"
							tooltip="@TRUNK.trunkNameNotNull@" maxlength="31"></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@TRUNK.trunkPolicy@:</td>
						<td><select id="policy" class="normalSel w160">
								<option value="1">srcMac</option>
								<option value="2">destMac</option>
								<option value="3">srcMacNDestMac</option>
								<option value="4">srcIp</option>
								<option value="5">destIp</option>
								<option value="6">srcIpNDestIp</option>
						</select></td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="modifyTrunkAttr()" icon="miniIcoEdit">@COMMON.modify@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>