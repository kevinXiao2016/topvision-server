<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<fmt:setBundle basename="com.topvision.platform.zetaframework.base.resources" var="zeta"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>

<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/nm3kToolTip.js"></script>

<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/zetaframework/Validator.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">

var name='<s:property value="name"/>';
var email='<s:property value="email"/>';
var actionId = '<s:property value="actionId"/>';
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function () {
    Zeta$('name').value = name;
    Zeta$('email').value = email;
    Zeta$('name').focus();
});

function isEmail(str){
    var reg = /^[\w-]+(\.[\w-]+)*@([a-z0-9-]+(\.[a-z0-9-]+)*?\.[a-z]{2,6}|(\d{1,3}\.){3}\d{1,3})(:\d{4})?$/;
    return reg.test(str);
}

function validate() {
	
	var $name = Zeta$('name').value;
    if(!V.isAnotherName($name) ){
        Zeta$('name').focus();
        return false;
    } else if(Zeta$('email').value == ''){
        Zeta$('email').focus();
        return false;
    } else if(!isEmail(Zeta$('email').value.trim())){
    	Zeta$('email').focus()
    	//window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.emailWrong);
		return;
        }
    return true;
}

function onSendClick() {
    if(!validate()){return;}
    Ext.Ajax.request({url: '../fault/sendTestEmail.tv', method:'POST',
        params:{name:Zeta$('name').value, email:Zeta$('email').value},
        success: function(response) {
            var json = Ext.util.JSON.decode(response.responseText);
            window.top.showMessageDlg(getI18NString("COMMON.tip"), json.feedback);
        }
    });
}
function okClick() {
    if(!validate()){return;}

    Ext.Ajax.request({url: '../fault/updateEmailAction.tv', method:'POST',
       params:{actionId: actionId, name: Zeta$('name').value, email: Zeta$('email').value},
       success: function(response) {
            if (response.responseText == "action exist") {
	   	    	window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.actionNameExist);
	   	    	return;
  	   		}
           
       		try {
       			var frame = window.parent.getActiveFrame();
       			frame.refreshActionList();
       		} catch (err) {
       		}      
       		top.afterSaveOrDelete({
   	            title: I18N.COMMON.tip,
   				html: '<b class="orangeTxt">' + I18N.COMMON.modifySuccess +'</b>'
   	        });
       		cancelClick();
       },
       failure: function() {Zeta$("feedback").innerHTML = '';}
    });
}
function cancelClick() {
    window.top.closeWindow("modalDlg");
}
function emailServerClick() {
    window.top.createDialog('mailServerDlg', I18N.SYSTEM.mailServer, 400, 480, 'system/showEmailServer.tv', null, true, true);
}
</script>
</head>
<body class="openWinBody">

	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt"><fmt:message key="ALERT.action" bundle="${fault}"/></b></p>
	    	<p><span id="newMsg"><fmt:message key="ALERT.modifyEmailAction" bundle="${fault}"/></span></p>
	    </div>
	    <div class="rightCirIco mailCirIco"></div>
	</div>
	<div class="edge10">
		<div class="zebraTableCaption">
		    <div class="zebraTableCaptionTitle"><span><fmt:message key="ALERT.action" bundle="${fault}"/></span></div>
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt" width="200">
		                    <fmt:message key="COMMON.name1" bundle="${resource}"/><font color=red>*</font>:
		                </td>
		                
		                <td>
		                    <input class="normalInput w300" name="name" id="name" 
					    	type=text  maxlength="63"  toolTip='<fmt:message key="COMMON.anotherName" bundle="${zeta}" />' />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">
		                    <fmt:message key="ALERT.email" bundle="${fault}"/><font color=red>*</font>:
		                </td>
		                <td>
		                    <input class="normalInput w300" name="email" id="email" 
					    	type=text  value="" toolTip='<fmt:message key="ALERT.emailCannotEnmpty" bundle="${fault}"/>' 
					    	/>
		                </td>
		            </tr>
		            <tr>
		                <td colspan="2" class="withoutBorderBottom">
		                    <p class="txtCenter orangeTxt pT20"><fmt:message key="ALERT.sendTestEmail" bundle="${fault}"/></p>
		                </td>
		            </tr>
		            <tr>
		                <td colspan="2" class="withoutBorderBottom">
		                    <div class="noWidthCenterOuter clearBoth">
							     <ol class="upChannelListOl pB10 pT0 noWidthCenter">
							         <li><a onclick="onSendClick()" id=sendEmail href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i><fmt:message key="ALERT.sendEmail" bundle="${fault}"/></span></a></li>
							     </ol>
							</div>
							<div id="feedback"></div>	                    
		                </td>
		            </tr>
		        </tbody>
		    </table>
		</div>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		         <li><a  id=okBtn  onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i><fmt:message key="COMMON.finish" bundle="${resource}"/></span></a></li>
		         <li><a  onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message key="COMMON.cancel" bundle="${resource}"/></span></a></li>
		     </ol>
		</div>
	</div>







</body></html>