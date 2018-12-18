<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<head>
<title></title>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    import js.tools.ipText
    module workbench
</Zeta:Loader>
<script type="text/javascript">
var snmpVersion = '${snmpParam.version}';
var entityId = '${entity.entityId}';
function addEnterKey(e) {
	var event = window.event || e; // for firefox
    if (event.keyCode == KeyEvent.VK_ENTER) {
        $("#okBt").focus();
    	//okClick();
    }
}
function doOnload() {
    var newIp = new ipV4Input("newIp","span1");
	newIp.width(210);
	newIp.setValue("${entity.ip}")
	//newIp.bgColor("white");
	//newIp.height("18px");
	setIpEnable("newIp",false);
	
	
	var el = Zeta$('snmpVersion');
	for(var i = 0; i< el.options.length; i++) {
		if(el.options[i].value == <s:property value="snmpParam.version"/>) {
			el.selectedIndex = i;
			break;
		}
	}
	
	/* e2 = Zeta$('securityLevel');
	for(var i = 0; i< e2.options.length; i++) {
		if(e2.options[i].value == <s:property value="snmpParam.securityLevel"/>) {
			e2.selectedIndex = i;
			break;
		}
	} */
	
	e3 = Zeta$('authProtocol');
	for(var i = 0; i< e3.options.length; i++) {
		if(e3.options[i].value == '<s:property value="snmpParam.authProtocol"/>') {
			e3.selectedIndex = i;
			break;
		}
	}
	var e4 = Zeta$('privProtocol');
	for (var i = 0; i < e4.options.length; i++) {
	    if (e4.options[i].value == '${snmpParam.privProtocol}') {
	        e4.selectedIndex = i;
	        break;
	    }
	}
	snmpChanged(); 
}
function validateForm() {
	var el = Zeta$('ip');
	if (el.value.trim() == '') {
		el.focus();
		return false;
	}	
	el = Zeta$('community');
	if (el.value.trim() == '') {
		el.focus();
		return false;
	}
	el = Zeta$('name');
	if (el.value.trim() == '') {
		el.focus();
		return false;
	}	
	return true;
}

function snmpChanged() {
	var hei = 335;
	var tmpW = window.parent.getWindow("entityBasicInfo");
    switch (Zeta$('snmpVersion').value) {
        case '0':
        case '1':
        	$(".v2setting").show();
            $(".v3setting").hide();
            tmpW.setHeight(hei);
        	tmpW.body.setHeight(hei);
            break;
        case '3':
        	tmpW.setHeight(hei);
        	tmpW.body.setHeight(hei+100);
        	$(".v2setting").hide();
            $(".v3setting").show();
            authProtocolChange();
            break;
    }
}
function authProtocolChange() {
	var authProtocol = Zeta$('authProtocol').value;
	if(authProtocol == "NOAUTH"){
		$("#authPass").attr("disabled",true);
		$("#privPass").attr("disabled",true);
		$("#v3AuthPass").attr("disabled","true");
		$("#v3PrivPass").attr("disabled","true");
	}else{
		$("#authPass").attr("disabled",false);
		$("#privPass").attr("disabled",false);
		$("#v3AuthPass").removeAttr("disabled");
		$("#v3PrivPass").removeAttr("disabled");
	}
}
function privProtocolChange(){
	var privProtocol = Zeta$('privProtocol').value;
	if(privProtocol == "NOPRIV"){
		$("#privPass").attr("disabled",true);
		$("#v3PrivPass").attr("disabled","true");
	}else{
		$("#privPass").attr("disabled",false);
		$("#v3PrivPass").removeAttr("disabled");
	}
}
function okClick() {
	var requetData = {};
	var version = $("#snmpVersion").val();
	var cmt = Zeta$('community').value.trim();
	var rwCmt = Zeta$('rwcommunity').value.trim();
	var username = Zeta$('username').value.trim();
	var authPro = Zeta$('authProtocol').value.trim();
	var authPass = Zeta$('authPass').value.trim();
	var privPro = Zeta$('privProtocol').value.trim();
	var privPass = Zeta$('privPass').value.trim();
	if(parseInt(version) !=3){
		if (cmt == '') {
			//window.top.showMessageDlg("@COMMON.tip@", "@RESOURCES/WorkBench.communityNotNull@");
			return Zeta$("community").focus();
		}
		if(rwCmt == ''){
			return Zeta$("rwcommunity").focus();
		}
	}else{
		if(username == ''){
			return Zeta$("username").focus();
		}
		if(authPro != 'NOAUTH'){
			if(authPass == ''){
				return Zeta$("authPass").focus();
			}
		}
		if(privPro != 'NOPRIV'){
			if(privPass == ''){
				return Zeta$("privPass").focus();
			}
		}
	}
	
	switch(parseInt(version)){
	case 0://snmpv1
	case 1://snmpv2
		requetData = {
			'entityId': entityId, 
			'readCommunity': Zeta$('community').value.trim(),
			'snmpVersion':version,
			'writeCommunity': Zeta$('rwcommunity').value.trim()
		}
		break;
	case 3://snmpv3
		requetData = {
			'entityId': entityId, 
			'username': Zeta$('username').value.trim(),
			'authProtocol':Zeta$('authProtocol').value.trim(),
			'authPassword': Zeta$('authPass').value.trim(),
			'privProtocol':Zeta$('privProtocol').value.trim(),
			'privPassword': Zeta$('privPass').value.trim(),
			'snmpVersion':version
		}
		break;
	}

	window.top.showWaitingDlg("@COMMON.wait@", "@RESOURCES/WorkBench.saveSnmpParam@");
	$.ajax({url: '/entity/updateEntitySnmpInfo.tv', type: 'POST',
		data:requetData,
		success: function() {
			window.top.showMessageDlg("@COMMON.tip@", "@RESOURCES/WorkBench.saveSnmpParamSuc@");
			cancelClick();
		}, error: function() {
			window.top.showErrorDlg();
		}, cache: false});
}
function cancelClick() {
	window.top.closeWindow("entityBasicInfo");
}
$(function(){
	doOnload();
})
</script>
</head>
<body class="openWinBody" onkeydown="addEnterKey(event || e)">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@td.unknownType@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edge10">
	<table class="mCenter zebraTableRows" >
		<tr>
			<td width=200px class="rightBlueTxt">
				<label for="ip">@td.ipAddress@@COMMON.maohao@<font color=red>*</font>
				</label>
			</td>
			<td>
				<span id="span1" disabled></span>
			</td>
		</tr>
		<tr class="darkZebraTr">
			<td class="rightBlueTxt"><label for="name">@td.deviceName@@COMMON.maohao@<font color=red>*</font></label></td>
			<td><input id="name" name="entity.name" maxlength="32" 
				value = "${entity.name}" class="normalInput" style="width: 210px" tooltip="@onfocus.pleaseInputDeviceName@"  disabled = "true"/>
			</td>
		</tr>
		<tr>
			<td class="rightBlueTxt"><label for="snmpVersion">@td.snmpVersion@@COMMON.maohao@<font color=red>*</font></label>
			</td>
			<td><select id="snmpVersion"  style="width: 210px"  onchange="snmpChanged();">
                        <option value="1">SNMP V2C</option>
                        <option value="3">SNMP V3</option>
                </select>
			</td>
		</tr>
		<tr id="v2Community" class='darkZebraTr v2setting'>
			<td class="rightBlueTxt"><label for="community">@td.community@@COMMON.maohao@<font color=red>*</font></label>
			</td>
			<td><Zeta:Password width="210px" id="community"
				value = "${snmpParam.community}" name="community" maxlength="32" tooltip="@onfocus.pleaseInputCommunity@" />
			</td>
		</tr>
		<tr id="v2WrCommunity" class=v2setting>
			<td class="rightBlueTxt"><label for="community">@td.rwCommunity@@COMMON.maohao@<font color=red>*</font></label>
			</td>
			<td height=20><Zeta:Password width="210px" id="rwcommunity"
				value = "${snmpParam.writeCommunity}" name="rwcommunity" maxlength="32" tooltip="@onfocus.pleaseInputWriteCommunity@" />
			</td>
		</tr>
		<tr style="display: none" id="v3UserName" class='v3setting darkZebraTr'>
			<td class="rightBlueTxt"><label for="username">@td.username@@COMMON.maohao@<font color=red>*</font></label>
			</td>
			<td><input id="username" name="entity.username"  maxlength="32" 
				value = "${snmpParam.username}" style="width: 210px;border:1px solid #8bb8f3;" tooltip="@onfocus.pleaseInputUserName@" />
			</td>
		</tr>
		<tr style="display: none" id="v3AuthProtocol" class=v3setting>
			<td class="rightBlueTxt"><label for="authProtocol">@td.authProtocol@@COMMON.maohao@<font color=red>*</font></label>
			</td>
			<td> 
				<select style="width: 210px;" id="authProtocol"  onchange="authProtocolChange();">
                        <option value="NOAUTH">NOAUTH</option>
                        <option value="MD5">MD5</option>
                        <option value="SHA">SHA</option>
                </select>
			</td>
		</tr>
		<tr style="display: none" id="v3AuthPass" class=v3setting>
			<td class="rightBlueTxt"><label for="authPass">@td.authPass@@COMMON.maohao@<font color=red>*</font></label>
			</td>
			<td><Zeta:Password width="210px" id="authPass"
				name="entity.authPass" value="${snmpParam.authPassword}" maxlength="32" tooltip="@onfocus.pleaseInputauthPass@"/>
			</td>
		</tr>
		<tr style="display: none" id="v3PrivProtocol" class=v3setting>
			<td class="rightBlueTxt"><label for="privProtocol">@td.privProtocol@@COMMON.maohao@<font color=red>*</font></label>
			</td>
			<td>
				<select style="width: 210px;" id="privProtocol" onchange="privProtocolChange();">
                        <option value="NOPRIV">NOPRIV</option>
                        <option value="CBC-DES">CBC-DES</option>
                </select>
			</td>
		</tr>
		<tr style="display: none" id="v3PrivPass" class=v3setting>
			<td class="rightBlueTxt"><label for="privPass">@td.privPass@@COMMON.maohao@<font color=red>*</font></label>
			</td>
			<td><Zeta:Password width="210px" id="privPass"
				name="entity.privPass" maxlength="32" value="${snmpParam.privPassword}" tooltip="@onfocus.pleaseInputprivPass@" />
			</td>
		</tr>
	</table>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="okClick()" icon="miniIcoData">@COMMON.ok@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
