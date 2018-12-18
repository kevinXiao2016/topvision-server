<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<HTML>
<HEAD>
<fmt:setBundle basename="com.topvision.ems.workbench.resources" var="workbench"/>
<link rel="STYLESHEET" type="text/css" href="../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css">
<style type="text/css">
.entityIcon {
	background-image: url(../images/network/entity.gif) !important;
	valign: middle;
}

.topoFolderIcon {
	background-image: url(../images/network/topoFolder.gif) !important;
}

.topoFolderIcon {
	background-image: url(../images/network/topoFolder.gif) !important;
}

.topoFolderIcon1 {
	background-image: url(../images/network/topoicon.gif) !important;
}

.topoFolderIcon5 {
	background-image: url(../images/network/subnet.gif) !important;
}

.topoFolderIcon6 {
	background-image: url(../images/network/cloudy16.gif) !important;
}

.topoFolderIcon7 {
	background-image: url(../images/network/href.png) !important;
}

.topoFolderIcon20 {
	background-image: url(../images/network/region.gif) !important;
}

.topoLeafIcon {
	background-image: url(../images/network/topoicon.gif) !important;
}

.topoRegionIcon {
	background-image: url(../images/network/region.gif) !important;
}

.ipTextField input {
	ime-mode: disabled;
	width: 49.5px;
	border: 0px;
	text-align: center;
}
</style>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/zetaframework/IpTextField.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
var tree = null;
Ext.onReady(function(){
	var newIp = new ipV4Input("newIp","span1");
	newIp.width(250);
	newIp.bgColor("white");
	newIp.height(18);
	
	var treeLoader = new Ext.tree.TreeLoader({dataUrl: '../topology/loadTopoFolder.tv'});
    tree = new Ext.tree.TreePanel({
    	height: 190,
        el: 'topoTree', useArrows: useArrows, trackMouseOver: trackMouseOver,
        animate: animCollapse, autoScroll: true, border: false,
        lines: true, rootVisible: false, enableDD: false,
        loader: treeLoader
    });

    var root = new Ext.tree.AsyncTreeNode({text: 'Topo Folder Tree', draggable:false, id:'source'});    
    tree.setRootNode(root);  
    tree.render();
    root.expand();
    setTimeout("doOnload()", 500);
    $("#community").css("border","1px solid #8bb8f3");
    $("#name").css("border","1px solid #8bb8f3");
});

function okClick() {
	var node = tree.getSelectionModel().getSelectedNode();
	var itemId = '1';
	var type = 1;
	if (node != null) {
		itemId = node.id;
		type = node.attributes.type;
	}
	var ip = getIpValue("newIp");
	if(!ipIsFilled("newIp")){
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.ipNotNull);
		return;
	}
	if (ip == '0.0.0.0' || ip == '127.0.0.1') {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.ipNotLike + ip + '!');
	}

	var name = Zeta$('name').value.trim();
	if (name == '') {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.deviceNameNotNull);
		return;
	}

    if (name.length > 64) {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.deviceNameTooLong);
		return;
	}
    
    var cmt = Zeta$('community').value.trim();
	if (cmt == '') {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.communityNotNull);
		return;
	}
	if (itemId == '1'){
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.pleaseChooseFolder);
		return;
	}
	var el = Zeta$('entityType');
	$.ajax({url: 'newEntity.tv', type: 'POST',
		data: {'entity.ip': ip, 
			   'entity.name': Zeta$('name').value.trim(),
			   'entity.typeId': el.options[el.selectedIndex].value,
                   'folderId': itemId,
			   'snmpParam.community': Zeta$('community').value.trim()},
	    beforeSend:function(){
		        window.top.showWaitingDlg(I18N.COMMON.waiting, String.format(I18N.WorkBench.refreshingDevice, ip),'waitingMsg','ext-mb-waiting');
		},
		success: function(json) {
			window.top.closeWaitingDlg();
			if (json.exist) {
				window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.deviceExist);
			} else {
				var frame = window.top.getActiveFrame();
				try {
					frame.createEntity(json);
				}catch (err) {
				}
				if(json.type == 10000) {
			   		try {
						window.top.getMenuFrame().doRefresh();
					} catch (err) {
					}
				}
				cancelClick();
			}  
		}, error: function() {
			window.top.closeWaitingDlg();
			window.top.showErrorDlg();
		}, dataType: 'json', cache: false});	
}
function cancelClick() {
	window.top.closeWindow("modalDlg");
}

function doOnload() {
	setTimeout(function() {
		ipFocus("newIp",1);
	}, 300);
}

function addEnterKey(e) {
	var event = window.event || e; // for firefox
    if (event.keyCode == KeyEvent.VK_ENTER) {
    	//okClick();会导致okClick执行两次，新建两个设备.按钮会自动监听回车事件
    }
}

Ext.onReady(function(){
	var newIp = new ipV4Input("newIp","span1");
	newIp.width(210);
	newIp.bgColor("white");
	newIp.height("18px");
	newIp.onEnterKey = function(){}
	
	$("#entityType").find("option[value=30001]").remove();
	$("#entityType").find("option[value=40000]").remove();
	$("#entityType").find("option[value=30000]").remove();
	$("#entityType").find("option[value=255]").remove();
	$("#entityType").find("option[value=69]").remove();
	$("#entityType").find("option[value=67]").remove();
	$("#entityType").find("option[value=65]").remove();
	$("#entityType").find("option[value=37]").remove();
	$("#entityType").find("option[value=36]").remove();
	$("#entityType").find("option[value=34]").remove();
	$("#entityType").find("option[value=33]").remove();
	$("#entityType").find("option[value=13000]").remove();
    setTimeout("doOnload()", 500);
});

</script>
</HEAD>
<BODY class=POPUP_WND style="padding: 10px" onload="doOnload()"
	onkeydown="addEnterKey(event)">
	<div class=formtip id=tips style="display: none"></div>
	<table cellspacing=8 cellpadding=0>
		<tr>
			<td width=150px height=20 valign=top><label for="ip"><fmt:message
						key="td.ipAddress" bundle="${workbench}" /> <font color=red>*</font>
			</label></td>
			<td height=20><span id="span1"></span></td>
		</tr>
		<tr>
			<td height=20><label for="community"><fmt:message
						key="td.community" bundle="${workbench}" /><font color=red>*</font>
			</label></td>
			<td height=20><INPUT style="width: 250px" id=community
				name="community" value='' class=iptxt type=password maxlength=32
				onfocus="inputFocused('community', '<fmt:message key="onfocus.pleaseInputCommunity" bundle="${workbench}"/>', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);">
			</td>
		</tr>
		<tr>
			<td height=20><label for="name"><fmt:message
						key="td.deviceName" bundle="${workbench}" /><font color=red>*</font></label></td>
			<td height=20><INPUT style="width: 250px" id=name
				name="entity.name" value='' class=iptxt type=text maxlength=32
				onfocus="inputFocused('name', '<fmt:message key="onfocus.pleaseInputDeviceName" bundle="${workbench}"/>', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);">
			</td>
		</tr>
		<tr>
			<td height=20><fmt:message key="td.deviceType"
					bundle="${workbench}" /></td>
			<td height=20><select id="entityType" name="entityType"
				style="width: 250px">
					<s:iterator value="entityTypes">
						<option value="<s:property value="typeId"/>">
							<s:property value="displayName" />
						</option>
					</s:iterator>
			</select></td>
		</tr>
		<tr>
			<td height=25 colspan=2><fmt:message key="td.chooseFloder"
					bundle="${workbench}" /></td>
		</tr>
		<tr>
			<td class=TREE-CONTAINER colspan=2><div id="topoTree"
					style="width: 100%;"></div></td>
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
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onmouseup="okClick()">
					<fmt:message key="button.ok" bundle="${workbench}" />
				</button>&nbsp;&nbsp;
				<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onclick="cancelClick()">
					<fmt:message key="button.cancel" bundle="${workbench}" />
				</button>
			</td>
		</tr>
	</table>
</BODY>
</HTML>
