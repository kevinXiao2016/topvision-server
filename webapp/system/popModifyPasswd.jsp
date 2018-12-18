<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library ext
	library zeta
    module resources
</Zeta:Loader>
<script type="text/javascript" src="/js/jquery/nm3kPassword.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#confirmPasswd").bind("keydown",addEnterKey);
});
function addEnterKey() {
    if (event.keyCode==KeyEvent.VK_ENTER) {
        okClick();
    }
}
function okClick(checkPasswdComplex) {
	var el = Zeta$('oldPasswd');
	if (el.value.trim() == '') {
		el.focus();
		return;
	}
	var el1 = Zeta$('newPasswd');
	var el2 = Zeta$('confirmPasswd');
	var reg2 = /^[a-zA-Z\d\u4e00-\u9fa5-_]{6,16}$/;	
	if(!reg2.test(el1.value)){
		$("#newPasswd").focus();
		return;
	}
	if( (!reg2.test(el2.value)) || (el1.value != el2.value) ){
		$("#confirmPasswd").focus();
		return;
	}
	
	$.ajax({url: 'setPasswd.tv', type: 'POST',
		data: {oldPasswd: el.value.trim(), newPasswd: el1.value.trim(), confirmPasswd: el2.value.trim()},
		success: function(json) {
			if (json.success) {
				cancelClick();
				top.afterSaveOrDelete({
			      title: '@COMMON.tip@',
			      html: '<b class="orangeTxt">@SYSTEM.passwordModifySuccess@</b>'
			    });
			} else if (json.passwd) {
				window.parent.showMessageDlg("@COMMON.tip@", "@SYSTEM.passwordNote1@");
			} else {
				window.parent.showMessageDlg("@COMMON.error@", "@SYSTEM.passwordNote2@", 'error');
			}
		},
		error: function(json) {
			window.parent.showMessageDlg("@COMMON.error@", "@SYSTEM.passwordNote2@", 'error');
		},
		dataType: 'json', cache: false});
}
function cancelClick() {
	window.top.hideModalDlg();
}

function doOnload() {
	setTimeout("doFocusing()", 500);
}

function doFocusing() {
	Zeta$('oldPasswd').focus();
}
$(function(){
	var pass1 = new nm3kPassword({
		id : "oldPasswd",
		renderTo : "putOldPassword",
		toolTip : "@SYSTEM.oldPasswordNotNull@",
		firstShowPassword : true,
		maxlength : 16,
		width : 180
	})
	pass1.init();
	
	var tip3 = "@SYSTEM.confirmPasswordNotNull@<br>@SYSTEM.passwordNote5@";
		tip3 += '<br />@SYSTEM.createUser.note1@';
	
	var pass3 = new nm3kPassword({
		id : "confirmPasswd",
		renderTo : "putConfirmPass",
		toolTip : tip3,
		firstShowPassword : true,
		maxlength : 16,
		width : 180
	})
	pass3.init();

});//end document.ready;
</script>
</head>
	<body class="openWinBody" onload="doOnload()">
		<div class="openWinHeader">
		    <div class="openWinTip"></div>
		    <div class="rightCirIco wheelCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT40">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="160">
	                	@SYSTEM.oldPassword@<font color=red>*</font>
	                </td>
	                <td id="putOldPassword">
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                	@SYSTEM.newPassword@<font color=red>*</font>
	                </td>
	                <td id="putNewPass">
	                	<script type="text/javascript">	      
		                	var pass2 = new nm3kPassword({
		                		id : "newPasswd",
		                		renderTo : "putNewPass",	                		
		                		firstShowPassword : true,
		                		maxlength : 16,
		                		width : 180,
			                	toolTip : "@SYSTEM.newPasswordNotNull@<br />@SYSTEM.createUser.note1@"		                		
		                	})
		                	pass2.init();
	                	</script>
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">
	                	@SYSTEM.confirmPassword@<font color=red>*</font>
	                </td>
	                <td id="putConfirmPass">
	                </td>
	            </tr>
	        </tbody>
	    </table>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
		    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		        <li>
		            <a href="javascript:;" class="normalBtnBig" 
		            	<s:if test="checkPasswdComplex">onmouseup="okClick(true)"</s:if>
						<s:else>onmouseup="okClick(false)"</s:else>>
		                <span>
		                    <i class="miniIcoData">
		                    </i>
		                    @COMMON.save@
		                </span>
		            </a>
		        </li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig"  onclick="cancelClick()">
		                <span>
		                	 <i class="miniIcoForbid">
		                    </i>
		                    @COMMON.cancel@
		                </span>
		            </a>
		        </li>
		    </ol>
		</div>
	</div>
	<div class=formtip id=tips style="display: none"></div>
</body>
</Zeta:HTML> 
