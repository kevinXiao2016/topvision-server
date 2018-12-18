<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Add Entity</title>
<%@include file="/include/cssStyle.inc" %>
<link rel="stylesheet" type="text/css" href="/css/gui.css"/>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<!-- 自定义css引入 -->
<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css"/>
<!-- 自定义js引入 -->
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<%-- <script type="text/javascript" src="../js/zetaframework/IpTextField.js"></script> --%>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<!-- 内置css定义 -->
<style>
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

/* .ipTextField input {
	ime-mode: disabled;
	width: 47px;
	border: 0px;
	text-align: center;
} */
</style>
<!-- 内置自定义js -->
<script type="text/javascript">
/*******************************************************
 * 变量定义及其初始化
 *******************************************************/
 var uniIdListString =  '<s:property value="uniIdListString"/>';

/*******************************************************
 * 执行语句包括onReady/onload的执行语句，其后为onReady的方法定义
 *******************************************************/
function doOnload(){
	var newIp = new ipV4Input("newIp", "span1");
	newIp.width(210);
	newIp.height(18);
	
	uniIdListString = uniIdListString.slice(0,uniIdListString.length - 1)
	var uniIdList = uniIdListString.split(",");//分割成uniId$uniRealIndex格式

	var position = Zeta$('mainuni');
	var option = document.createElement('option');
	option.value = "";
	option.text = I18N.CMC.select.select;
	try {
		position.add(option, null);
    } catch(ex) {
    	position.add(option);
    }
	for(var i = 0; i < uniIdList.length; i++) {
		var option = document.createElement('option');
		option.value = uniIdList[i].slice(0,1);
		option.text = "uni" + uniIdList[i].slice(2,3)
		try {
			position.add(option, null);
        } catch(ex) {
        	position.add(option);
        }
	}

	var position = Zeta$('backupuni');
	var option = document.createElement('option');
	option.value = "";
	option.text = I18N.CMC.select.select;
	try {
		position.add(option, null);
    } catch(ex) {
    	position.add(option);
    }
	for(var i = 0; i < uniIdList.length; i++) {
		var option = document.createElement('option');
		option.value = uniIdList[i].slice(0,1);
		option.text = "uni" + uniIdList[i].slice(2,3)
		try {
			position.add(option, null);
        } catch(ex) {
        	position.add(option);
        }
	}

	$("#community").css("border","1px solid #8bb8f3");
	$("#name").css("border","1px solid #8bb8f3");
	setTimeout(function(){ipFocus("newIp",1);}, 500);
}
/*******************************************************
 * 方法定义：操作，交互，菜单等归类组织在一起
 *******************************************************/
 function addEntity() {
 	var ip = getIpValue('newIp');
	if (ip == '') {
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.text.IPNotNull);
		return;
	}
	if (ip == '0.0.0.0' || ip == '127.0.0.1') {
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.text.IPNotThing, ip));
		return;
	}

	var name = $('#name').val().trim();
	if (name == '') {
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.text.aliasNotNull);
		return;
	}
	
	var cmt = $('#community').val().trim();
	if (cmt == '') {
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.text.communityNotNull);
		return;
	}
	var uniIdMain = $('#mainuni').val();
	var uniIdBack = $('#backupuni').val();
 	$.ajax({
		url: '/cmc/addAndRelateCmcToOnu.tv?r=' + Math.random(),
		type: 'POST',
		dateType: 'json',
		data: {
			'entity.ip': ip,
			'entity.name': name,
			'entity.typeId': $('#entityType').val(),
			//'folderId': '10',
			'snmpParam.community': cmt,
			'onuId': '1',
			'uniIdMain': uniIdMain,
			'uniIdBack': uniIdBack
		},
		cache: false,
		success: function(json) {
			alert(333);
			alert(json.exist);
		},
		error: function(error) {
			alert(error);
		}
	});
 	cancelClick()
 }
 function cancelClick() {
 	window.parent.closeWindow("createCMC");
 }
</script>
</head>
<body class=POPUP_WND style="margin: 20px;" onload="doOnload()">
	<div class=formtip id=tips style="display: none"></div>
		<fieldset style='width: 426; height:265;background-color:#ffffff;'>
			<div>&nbsp;<b><fmt:message bundle='${cmc}' key='CMC.label.deviceInformation'/>:</b></div>
			<table cellspacing=10 cellpadding=0>
				<tr>
					<td width=100px height=20 valign=top align="right"><label for="newIp"><fmt:message bundle='${cmc}' key='CMC.label.IP'/>: <font color=red>*</font></label>
					</td>
					<td height=20 width="250px" align="center">
						<span id="span1"></span>
					</td>
				</tr>
				<tr>
					<td height=20 align="right"><label for="community"><fmt:message bundle='${cmc}' key='CMC.label.community'/>: <font color=red>*</font></label>
					</td>
					<td height=20 width="250px" align="center"><INPUT style="width: 210px" id=community
						name="community" value='' class=iptxt type=password maxlength=32
						onfocus="inputFocused('community', '<fmt:message key="CMC.tip.inputCommunity" bundle="${cmc}"/>', 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);">
					</td>
				</tr>
				<tr>
					<td height=20 align="right"><label for="name"><fmt:message bundle='${cmc}' key='CMC.label.alias'/>: <font color=red>*</font></label>
					</td>
					<td height=20 width="250px" align="center">
						<INPUT style="width: 210px" id=name
							name="entity.name" value='' class=iptxt type=text maxlength=32
							onfocus="inputFocused('name', '<fmt:message key="CMC.tip.inputDeviceName" bundle="${cmc}"/>', 'iptxt_focused')"
							onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);">
					</td>
				</tr>
				<tr>
					<td height=20 align="right"><fmt:message bundle='${cmc}' key='CCMTS.entityStyle'/>: &nbsp;&nbsp;</td>
					<td height=20 width="250px" align="center">
						<select id="entityType" name="entityType" style="width: 210px">
							<option value="30002">CCMTS_8800B</option>
						</select>
					</td>
				</tr>
			</table>
			<div>&nbsp;<b><fmt:message bundle='${cmc}' key='CMC.label.RelatedUNI'/>:</b></div>
			<table cellspacing=10 cellpadding=0>
				<tr>
					<td width=100px height=20 valign=top align="right"><fmt:message bundle='${cmc}' key='CMC.label.MainPort'/>:&nbsp;&nbsp;</td>
					<td height=20 width="250px" align="center">
						<select id="mainuni" style="width: 210px">
						</select>
					</td>
				</tr>
				<tr>
					<td width=100px height=20 valign=top align="right"><fmt:message bundle='${cmc}' key='CMC.label.BackupPort'/>:&nbsp;&nbsp;</td>
					<td height=20 width="250px" align="center">
						<select id="backupuni" style="width: 210px">
						</select>
					</td>
				</tr>
			</table>
		</fieldset>
	<div style="margin-top:5px" style="margin-left:260px">
		<button id=saveBt class=BUTTON75 type="button"
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onMouseDown="this.className='BUTTON_PRESSED75'"
			onclick="addEntity()"><fmt:message bundle='${cmc}' key='CMC.button.create'/></button>
		&nbsp;&nbsp;
		<button id=cancelBt class=BUTTON75 type="button"
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onMouseDown="this.className='BUTTON_PRESSED75'"
			onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.button.close'/></button>
	</div>
</body>
</html>