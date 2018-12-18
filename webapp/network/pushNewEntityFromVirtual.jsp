<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>
<link rel="STYLESHEET" type="text/css" href="../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../css/ext-all.css">
<link rel="STYLESHEET" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css">
<style type="text/css">
.entityIcon {
	background-image: url(../images/network/entity.gif) !important;
	valign: middle;
}

.topoFolderIcon {
	background-image: url(../images/network/topoFolder.gif) !important;
}

.topoLeafIcon {
	background-image: url(../images/network/topoicon.gif) !important;
}

.topoRegionIcon {
	background-image: url(../images/network/region.gif) !important;
}

.ipTextField input {
	ime-mode: disabled;
	width: 47px;
	border: 0px;
	text-align: center;
}
</style>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
var virtualNetId = ${virtualNetId};
$(document).ready(function(){
	var newIp = new ipV4Input("newIp", "span1");
	newIp.width(210);
	newIp.height(18);
	newIp.bgColor("white");

	
	$("#entityType").bind('propertychange',function(){
		var v = $(this).val();
		switch(v){
			default:
				showAll();
				break;
		}
	});

	setTimeout(function() {
		//Zeta$('ip1').focus();
		ipFocus("newIp",1);
	}, 300);
	$("#community").css("border","1px solid #8bb8f3");
	$("#name").css("border","1px solid #8bb8f3");
});


function showVirtual(){
	$(".isDisplayClass").attr("disabled",true);
	$("input").val("");
}
function showAll(){
	$(".isDisplayClass").attr("disabled",false);
}

function okClick() {
	var ip = getIpValue('newIp');
	var name = Zeta$('name').value.trim();
	var cmt = Zeta$('community').value.trim();
	
	var v = $("#entityType").val();
	if (ip == '') {
		window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />', "<fmt:message bundle="${res}" key="WorkBench.ipNotNull" />");
		return;
	}
	if (ip == '0.0.0.0' || ip == '127.0.0.1') {
		window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />', "<fmt:message bundle="${res}" key="WorkBench.ipNotLike" />" + ip + "!");
		return;
	}
	if (name == '') {
		window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />', "<fmt:message bundle="${res}" key="WorkBench.deviceNameNotNull" />");
		return;
	}
	if (cmt == '') {
		window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />', "<fmt:message bundle="${res}" key="WorkBench.communityNotNull" />");
		return;
	}
	var el = Zeta$('entityType');
	$.ajax({url: '/virtualnet/createProduct.tv', type: 'POST',
		data: {'product.productIp': ip, 
			   'product.productName': Zeta$('name').value.trim(),
			   'product.virtualNetId': virtualNetId,
			   'product.productType': el.options[el.selectedIndex].value,
			   'productSnmp.readCommunity': Zeta$('community').value.trim()
		},
		success: function(json) {
			if (json.exist) {
				window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />', "<fmt:message bundle="${res}" key="WorkBench.deviceExist" />");
			} else {
				window.parent.onRefreshClick();
				cancelClick();
			}
		}, error: function(o) {
			window.top.showErrorDlg();
		}, dataType: 'json', cache: false});	
}
function cancelClick() {
	window.top.closeWindow("modalDlg");
}
function addEnterKey(e) {
	var event = window.event || e; // for firefox
    if (event.keyCode == KeyEvent.VK_ENTER) {
    	//okClick();会导致okClick执行两次，新建两个设备.按钮会自动监听回车事件
    }
}
</script>
</HEAD>
<BODY class=POPUP_WND style="padding: 10px" 
	onkeydown="addEnterKey(event)">
	<div class=formtip id=tips style="display: none"></div>

	<table cellspacing=8 cellpadding=0>
		<tr class="isDisplayClass">
			<td width=100px height=20 valign=top><label for="ip"><fmt:message bundle="${res}" key="COMMON.ip" />: <font color=red>*</font></label>
			</td>
			<td height=20>
				<span id="span1"></span>
			</td>
		</tr>
		<tr class="isDisplayClass">
			<td height=20><label for="community"><fmt:message bundle="${res}" key="topo.virtualDeviceList.newEntity.community" />: <font color=red>*</font></label>
			</td>
			<td height=20><input style="width: 210px" id=community  class="isDisplayClass"
				name="community" value='' class=iptxt type=password maxlength=32
				onfocus="inputFocused('community', '<fmt:message bundle="${res}" key="topo.virtualDeviceList.newEntity.inputSNMP" />', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);">
			</td>
		</tr>
		<tr >
			<td height=20><label for="name"><fmt:message bundle="${res}" key="topo.virtualDeviceList.newEntity.deviceAlias" />: <font color=red>*</font></label>
			</td>
			<td height=20><input style="width: 210px" id=name 
				name="entity.name" value='' class=iptxt type=text maxlength=32
				onfocus="inputFocused('name', '<fmt:message bundle="${res}" key="topo.virtualDeviceList.newEntity.requireDeviceName" />', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);">
			</td>
		</tr>
		<tr>
			<td height=20><fmt:message bundle="${res}" key="MAIN.templateRootNode" />:</td>
			<td height=20><select id="entityType" name="entityType"
				style="width: 210px">
			</select></td>
		</tr>
		<tr>
		</tr>
		<tr>
		</tr>
		<tr>
			<td colspan=2 valign=top align=right>
				<button id="okBt" class=BUTTON75
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onmouseup="okClick()"><fmt:message bundle="${res}" key="COMMON.finish" /></button>&nbsp;&nbsp;
				<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onclick="cancelClick()"><fmt:message bundle="${res}" key="COMMON.cancel" /></button></td>
		</tr>
	</table>
</BODY>
</HTML>
