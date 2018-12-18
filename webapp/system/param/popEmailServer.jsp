<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	module resources
</Zeta:Loader>
<script type="text/javascript">
var smtpServer = '<s:property value="smtpServer"/>';
var smtpPort = <s:property value="smtpPort"/>;
var senderEmail = '<s:property value="senderEmail"/>';
var requireAuth = <s:property value="requireAuth"/>;
var username = '<s:property value="username"/>';
var password = '<s:property value="password"/>';
var bakEmailServer = <s:property value="bakEmailServer"/>;
var smtpServer1 = '<s:property value="smtpServer1"/>';
var smtpPort1 = <s:property value="smtpPort1"/>;
var requireAuth1 = <s:property value="requireAuth1"/>;
var username1 = '<s:property value="username1"/>';
var password1 = '<s:property value="password1"/>';
//email 验证
function isEmail(str){
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    return reg.test(str);
}
//SMTP 验证
function isValidSMTP(s){
    if(s==''){
        return true;
    }else{
        return /^\w+(\.\w+)+$/.test(s);
    }
}
//用户名验证
function isValidName(s){
    if(s==''){
        return true;
    }else{
        return /^[\w_\-@\.]+$/.test(s);
    }
}
function validate() {
	var smtpServer = Zeta$('smtpServer');
	if (smtpServer.value.trim() == '') {
		$('#smtpServer').focus();
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseEnterSMTPServer);
		return ;
	}else if(!isValidSMTP(smtpServer.value.trim())){
		$('#smtpServer').focus();
	    //window.top.showMessageDlg(I18N.COMMON.tip, '@invalidSMTP@');
	    return ;
	}
	
	smtpPort = Zeta$('smtpPort');
	var port = parseInt($('#smtpPort').val());
	if(port<1 || port > 65535){
		$('#smtpPort').focus();
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseEnterSMTPServerPort);
		return ;
	}

	senderEmail = Zeta$('senderEmail');
	if (!isEmail(senderEmail.value.trim()) && !senderEmail.value.trim() == '') {
		$('#senderEmail').focus();
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.emailFormatError);
		return ;
	}
	
	if (Zeta$('requireAuth').checked) {
		el = Zeta$('username');
		if (el.value.trim() == '') {
			$('#username').focus();
			//window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseEnterUserName);
			return ;
		}
		
		if (!isValidName(el.value.trim())) {
			$('#username').focus();
            //window.top.showMessageDlg(I18N.COMMON.tip, "@invalidSMTPName@");
            return ;
        }
	}

	if (Zeta$('bakEmailServer').checked) {
		el = Zeta$('smtpServer1');
		if (el.value.trim() == '') {
			$('#smtpServer1').focus();
			//window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseEnterBackupSMTPServer);
			return ;
		}else if(!isValidSMTP(el.value.trim())){
			$('#smtpServer1').focus();
		    //window.top.showMessageDlg(I18N.COMMON.tip, '@invalidSMTP@');
	        return ;  
		}
		
		e2 = Zeta$('smtpPort1');
		if (e2.value.trim() == '') {
			$('#smtpPort1').focus();
			//window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseEnterBackupSMTPServerPort);
			return ;
		}		
	}

	if (Zeta$('requireAuth1').checked) {
		el = Zeta$('username1');
		if (el.value.trim() == '') {
			$('#username1').focus();
			//window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseEnterUserName);
			return ;
		}
		
		if (!isValidName(el.value.trim())) {
			$('#username1').focus();
            //window.top.showMessageDlg(I18N.COMMON.tip, "@invalidSMTPName@");
            return ;
        }
	}
	
    return true;
}
function testClick() {
    if(!validate()){return;}
    window.top.showInputDlg(I18N.SYSTEM.emailTest, I18N.SYSTEM.pleaseEnterEmailReceiveAccount, sendTestEmailCallback);
}
function sendTestEmailCallback(type, text) {	
    if (type == "cancel" ) {
        return;
    }
    if(text.trim() == ""){
    	window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseEnterEmailAddress);
    	return;
        }
    if(!isEmail(text.trim())){
    	window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.emailFormatError);
    	return;
        }
    requireAuth = Zeta$('requireAuth').checked;
    bakEmailServer = Zeta$('bakEmailServer').checked;
    requireAuth1 = Zeta$('requireAuth1').checked;
    smtpServer = Zeta$('smtpServer').value;
    smtpPort = Zeta$('smtpPort').value;
    senderEmail = Zeta$('senderEmail').value;
    username = Zeta$('username').value;
    password = Zeta$('password').value;
    smtpServer1 = Zeta$('smtpServer1').value;
    smtpPort1 = Zeta$('smtpPort1').value;
    username1 = Zeta$('username1').value;
    password1 = Zeta$('password1').value;
	$.ajax({url: 'sendTestEmail.tv', type: 'POST',
		data: {smtpServer:smtpServer, smtpPort:smtpPort, senderEmail:senderEmail, 
        requireAuth:requireAuth, username:username, password:password, bakEmailServer:bakEmailServer, 
        smtpServer1:smtpServer1, smtpPort1:smtpPort1, requireAuth1:requireAuth1, 
        username1:username1, password1:password1, testEmail:text},
		success: function() {
			top.afterSaveOrDelete({
		        title: I18N.COMMON.tip,
		        html: '<b class="orangeTxt">'+I18N.SYSTEM.emailSendSuccess+'</b>'
		    });
			//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.emailSendSuccess);
		}, error: function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.emailServerVerificationFail);
		}, dataType: 'plain', cache: false});
}
function okClick() {
    if(!validate()){return;}
	requireAuth = Zeta$('requireAuth').checked;
	bakEmailServer = Zeta$('bakEmailServer').checked;
	requireAuth1 = Zeta$('requireAuth1').checked;
	smtpServer = Zeta$('smtpServer').value;
	smtpPort = Zeta$('smtpPort').value;
	senderEmail = Zeta$('senderEmail').value;
	username = Zeta$('username').value;
	password = Zeta$('password').value;
	smtpServer1 = Zeta$('smtpServer1').value;
	smtpPort1 = Zeta$('smtpPort1').value;
	username1 = Zeta$('username1').value;
	password1 = Zeta$('password1').value;

	$.ajax({url: 'saveEmailServer.tv', type: 'POST',
		data: {smtpServer:smtpServer, smtpPort:smtpPort, senderEmail:senderEmail, 
		   requireAuth:requireAuth, username:username, password:password, 
		   bakEmailServer:bakEmailServer, 
		   smtpServer1:smtpServer1, smtpPort1:smtpPort1, requireAuth1:requireAuth1,
		   username1:username1, password1:password1},
		success: function() {
			top.afterSaveOrDelete({
		      title: '@COMMON.tip@',
		      html: '<b class="orangeTxt">@resources/COMMON.saveSuccess@</b>'
		    });
			cancelClick();
		}, error: function() {
			window.parent.showErrorDlg();
		}, dataType: 'plain', cache: false});	
}
function cancelClick() {
    window.parent.closeWindow("modalDlg");
}
function onCheckbox(check, a1, a2, a3, a4, a5) {
    if(a1 != null) Zeta$(a1).disabled = !check.checked;
    if(a2 != null) Zeta$(a2).disabled = !check.checked;
    if(a3 != null) Zeta$(a3).disabled = !check.checked;
    if(a4 != null) Zeta$(a4).disabled = !(check.checked & Zeta$(a3).checked);
    if(a5 != null) Zeta$(a5).disabled = !(check.checked & Zeta$(a3).checked);
}
function donOnload() {
    Zeta$('requireAuth').checked = requireAuth;
    Zeta$('bakEmailServer').checked = bakEmailServer;
    Zeta$('requireAuth1').checked = requireAuth1;
    Zeta$('smtpServer').value = smtpServer;
	Zeta$('smtpPort').value = smtpPort;
	Zeta$('senderEmail').value = senderEmail;
	Zeta$('username').value = username;
	Zeta$('password').value = password;
	Zeta$('smtpServer1').value = smtpServer1;
	Zeta$('smtpPort1').value = smtpPort1;
	Zeta$('username1').value = username1;
	Zeta$('password1').value = password1;
    onCheckbox(Zeta$('requireAuth'),'username','password');
    onCheckbox(Zeta$('requireAuth1'),'username1','password1');
    onCheckbox(Zeta$('bakEmailServer'),'smtpServer1','smtpPort1','requireAuth1','username1','password1');
    
    //setTimeout('doOnFocused()', 500);
}
function doOnFocused() {
	Zeta$('smtpServer').focus();
}
</script>
</head>
<body class="openWinBody" onload="donOnload();">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
    	<div class="openWinTip">@SYSTEM.mailServerSet@</div>
    	<div class="rightCirIco mailCirIco"></div>
	</div>
	<div class="edgeTB10LR20">
     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
         <tbody>
             <tr>
                 <td class="rightBlueTxt" width="140">
                     @SYSTEM.SMTPServer@<font color=red>*</font>:
                 </td>
                 <td width="210">
                    <input style="width: 180px" class="normalInput" name="smtpServer"
							id="smtpServer" type=text value="" maxlength="25" toolTip="@SYSTEM.enterSmtpServer@"/>
                 </td>
                 <td class="rightBlueTxt" width="140">
                 	@SYSTEM.SMTPServerPort@<font color=red>*</font>:
                 </td>
                 <td>
                 	<input style="width: 180px" class="normalInput"
							onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
							name="smtpPort" id="smtpPort" type=text value="" toolTip="1-65535"/>
                 </td>
             </tr>            
              <tr class="darkZebraTr">
                 <td class="rightBlueTxt">
                     @SYSTEM.sendEmailAddress@
                 </td>
                 <td>
                    <input style="width: 180px" class="normalInput"
							name="senderEmail" id="senderEmail" type=text value="" />
                 </td>
                 <td colspan="2"></td>
             </tr>
             <tr>
                 <td class="rightBlueTxt">
                     <input type=checkbox name="requireAuth"
							id="requireAuth"
							onClick="onCheckbox(this,'username','password');" />
                 </td>
                 <td>
                     <label	for="requireAuth">@SYSTEM.SMTPServerNeedVerification@</label>
                 </td>
                 <td colspan="2"></td>
             </tr>
              <tr class="darkZebraTr">
                 <td class="rightBlueTxt">
                     @SYSTEM.userName@<font color=red>*</font>:
                 </td>
                 <td>
                    <input style="width: 180px" class="normalInput" type=text
							name="username" id="username" value="" maxlength="25" />
                 </td>
                 <td class="rightBlueTxt">
                     @SYSTEM.passWord@
                 </td>
                 <td>
                     <input style="width: 180px" class="normalInput" type=password
							name="password" id="password" value="" maxlength="25" />
                 </td>
             </tr>
              <tr>
                 <td class="rightBlueTxt">
                     <input type=checkbox name="bakEmailServer"
							id="bakEmailServer"
							onClick="onCheckbox(this,'smtpServer1','smtpPort1','requireAuth1','username1','password1');" />
                 </td>
                 <td>
                    <label for="bakEmailServer">@SYSTEM.configBackupEmailServer@</label>
                 </td>
                 <td colspan="2"></td>
             </tr>
             <tr class="darkZebraTr">
                 <td class="rightBlueTxt">
                     @SYSTEM.SMTPServer@<font color=red>*</font>:
                 </td>
                 <td>
                     <input style="width: 180px" class="normalInput"
							name="smtpServer1" id="smtpServer1" type=text value="" maxlength="25" />
                 </td>
                 <td class="rightBlueTxt">
                     @SYSTEM.SMTPServerPort@<font
							color=red>*</font>:
                 </td>
                 <td>
                    <input style="width: 180px" class="normalInput" name="smtpPort1"
							id="smtpPort1"
							onkeyup="this.value=this.value.replace(/[^\d]/g,'')" type=text
							value="" />
                 </td>
             </tr>
             <tr>
                 <td class="rightBlueTxt">
                     <input type=checkbox name="requireAuth1" id="requireAuth1"
							onClick="onCheckbox(this,'username1','password1');" />
                 </td>
                 <td>
                     <label for="requireAuth1">@SYSTEM.SMTPServerNeedVerification@</label>
                 </td>
                 <td colspan="2"></td>
             </tr>
              <tr class="darkZebraTr">
                 <td class="rightBlueTxt">
                     @SYSTEM.userName@<font color=red>*</font>:
                 </td>
                 <td>
                    <input style="width: 180px" class="normalInput" type=text
							name="username1" id="username1" value="" maxlength="25" />
                 </td>
                 <td class="rightBlueTxt">
                     @SYSTEM.passWord@
                 </td>
                 <td>
                     <input style="width: 180px" class="normalInput" type=password
							name="password1" id="password1" value="" maxlength="25" />
                 </td>
             </tr>
         </tbody>
     </table>
</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="testClick()" icon="miniIcoInfo">@SYSTEM.test@</Zeta:Button>
		<Zeta:Button onClick="okClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
