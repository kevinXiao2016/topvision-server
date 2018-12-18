<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module resources
    import /js/jquery/nm3kPassword
</Zeta:Loader>


<script type="text/javascript">
var user = ${userJson};

//除去字符串前后的空格
function trim(s)
{
   s = s.replace(/^\s+/,"");
   return s.replace(/\s+$/,"");
}

//先做输入验证;
function inputValidateFn(str){
	var reg = /^[-_a-zA-Z0-9\u4e00-\u9fa5]{1,16}$/;
	if(user.checkPasswdComplex){//判断密码强度，如果有，那么最少要输入6位;
		reg = /^[-_a-zA-Z0-9\u4e00-\u9fa5]{6,16}$/;		
	}
	if(!reg.test(str)){
		return false;
	}
	return true;
}

function modifyPwd(){
	var oldPwd = $("#oldPwd").val();
	var newPwd = $("#newPwd").val();
	var confirmPwd = $("#confirmPwd").val();
	
	//先验证旧密码，旧密码不受密码强度限制，因为可能限制之前的旧密码就只有1位;
	//var reg = /^[-_a-zA-Z0-9\u4e00-\u9fa5]{1,16}$/;
	//var oldReg = reg.test(oldPwd);
	if( oldPwd.trim() == '' ){//旧密码不通过; 
		$("#oldPwd").focus(); 
		return;
	}
	var newReg = inputValidateFn(newPwd);
	if( !newReg ){//新密码不通过;
		$("#newPwd").focus();
		return;
	}
	var confirmReg = inputValidateFn(confirmPwd);
	if( !confirmReg ){//确认密码不通过;
		$("#confirmPwd").focus();
		return;
	}

	//进行提交前的验证
	if(!validate(oldPwd, newPwd, confirmPwd)){return;}

	$.ajax({
		url: '/system/modifyUserPwd.tv',
    	type: 'POST',dataType: 'json',
    	data: {userId:user.userId, userName:user.userName, checkPasswdComplex: user.checkPasswdComplex, oldPwd:oldPwd, newPwd: newPwd},
   		success: function(json) {
   			if(json.success==false){
   				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.passwordNote1 );
   			}else{
   				top.afterSaveOrDelete({
			      title: I18N.COMMON.tip,
			      html: '<b class="orangeTxt">'+ I18N.SYSTEM.passwordModifySuccess +'</b>'
			    });
   				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.passwordModifySuccess);
   				cancleClick();
   			}
   		}, error: function(json) {
   			//修改密码失败后，提示修改失败
   			window.parent.showMessageDlg(I18N.COMMON.error, I18N.SYSTEM.passwordNote2, 'error');
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	}); 
}

function validate(oldPwd, newPwd, confirmPwd){
	if(oldPwd==''){
		$("#oldPwd").focus();
		return false;
	}
	//判断是否需要进行密码强度校验
	if (user.checkPasswdComplex) {
		if(newPwd=='' || newPwd.length<6){
			top.afterSaveOrDelete({
		      title: I18N.COMMON.tip,
		      html: '<b class="orangeTxt">'+ I18N.SYSTEM.newPasswordSizeNoLessThanSix +'</b>'
		    });
			//window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.newPasswordSizeNoLessThanSix);
			$("#newPwd").focus();
			return false;
		}
	}else{
		if(newPwd==''){
			$("#newPwd").focus();
			return false;
		}
	}
	//判断确认密码是否等于新密码
	if(confirmPwd!=newPwd){
		$("#confirmPwd").focus();
		return false;
	}
	return true;
}

function cancleClick(){
	window.parent.closeWindow("modifyUserPwd");
}

$(document).ready(function(){
	var pass1 = new nm3kPassword({
		id : "oldPwd",
		renderTo : "putPass1",
		toolTip : "@SYSTEM.oldPasswordNotNull@",
		width : 180,
		maxlength : 16,
		firstShowPassword : true
	})
	pass1.init();

	var tip2 = "@SYSTEM.newPasswordNotNull@";
	if(user.checkPasswdComplex){//开启了密码检测;
		tip2 += '<br />@SYSTEM.createUser.note1@';
	}else{
		tip2 += '<br />@SYSTEM.createUser.note3@';
	}
	var pass2 = new nm3kPassword({
		id : "newPwd",
		renderTo : "putPass2",
		width : 180,
		firstShowPassword : true,
		maxlength : 16,
		toolTip : tip2
	})
	pass2.init();
	
	tip2 = "@SYSTEM.confirmPasswordNotNull@<br>@SYSTEM.passwordNote5@";	
	if(user.checkPasswdComplex){//开启了密码检测;
		tip2 += '<br />@SYSTEM.createUser.note1@';
	}else{
		tip2 += '<br />@SYSTEM.createUser.note3@';
	}
	var pass3 = new nm3kPassword({
		id : "confirmPwd",
		renderTo : "putPass3",
		width : 180,
		firstShowPassword : true,
		maxlength : 16,
		toolTip : tip2
	})
	pass3.init();
	
});//end document.ready;
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco flagCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
        <tbody>
            <tr>
                <td class="rightBlueTxt" width="180">
                    @SYSTEM.oldPassword@<font color=red>*</font>
                </td>
                <td id="putPass1">
					<%--<Zeta:Password  width="180px" id="oldPwd" /> --%>
                </td>
            </tr>
            <tr class="darkZebraTr">
                <td class="rightBlueTxt">
                    @SYSTEM.newPassword@<font color=red>*</font>
                </td>
                <td id="putPass2">
                    <%-- <Zeta:Password width="180px" id="newPwd" tooltip="@SYSTEM.newPasswordNotNull@" /> --%>
                </td>
            </tr>
            <tr>
                <td class="rightBlueTxt">
                    @SYSTEM.confirmPassword@<font color=red>*</font>
                </td>
                <td id="putPass3">
                    <%-- <Zeta:Password width="180px" id="confirmPwd" tooltip="@SYSTEM.confirmPasswordNotNull@<br>@SYSTEM.passwordNote5@" /> --%>
                </td>
            </tr>
        </tbody>
    </table>
    <div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
	         <li><a onclick="modifyPwd()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@COMMON.ok@</span></a></li>
	         <li><a onclick="cancleClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
</div>
</body></Zeta:HTML>
