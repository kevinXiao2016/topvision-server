<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML><HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<fmt:setBundle basename="com.topvision.platform.zetaframework.base.resources" var="zeta"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
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
var mobile='<s:property value="mobile"/>';
var actionId = '<s:property value="actionId"/>';
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function () {
    Zeta$('name').value = name;
    Zeta$('mobile').value = mobile;
    Zeta$('name').focus();
});
function validate() {
	var $name = Zeta$('name').value;
    if(!V.isAnotherName($name) ){
        Zeta$('name').focus();
        return false;
    } else if(Zeta$('mobile').value == ''){
        Zeta$('mobile').focus();
        return false;
    } else{
    	var patrn = /^\d{11}$/;
    	var s = Zeta$('mobile').value;
    	if(!patrn.exec(s)){
    		Zeta$('mobile').focus();
    		return false;
    	}
    }
    return true;
}
function onSendClick() {
    if(!validate()){return;}
    Ext.Ajax.request({url: '../fault/sendTestSms.tv', method:'POST',
       params:{name:Zeta$('name').value, mobile:Zeta$('mobile').value},
       success: function(response) {
         var json = Ext.util.JSON.decode(response.responseText);
         Zeta$("feedback").innerHTML = json.feedback;},
       failure: function() {Zeta$("feedback").innerHTML = I18N.SYSTEM.testFailure;}
    });
}
function okClick() {
    if(!validate()){return;}
    Ext.Ajax.request({url: '../fault/updateSmsAction.tv', method:'POST',
       params:{actionId: actionId, name:Zeta$('name').value, mobile:Zeta$('mobile').value},
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
function lastClick() {
    location.href = '../fault/newAction.jsp';
}
function cancelClick() {
    window.top.closeWindow("modalDlg");
}
function showHelp() {
    window.open('../help/index.jsp?module=newSmsAction', 'help');
}
</script>
</HEAD><body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt"><fmt:message key="ALERT.action" bundle="${fault}"/></b></p>
	    	<p><span id="newMsg"><fmt:message key="ALERT.modifyMessageAction" bundle="${fault}"/></span></p>
	    </div>
	    <div class="rightCirIco phoneCirIco"></div>
	</div>
	<div class="edge10">
	<div class="zebraTableCaption">
     	<div class="zebraTableCaptionTitle"><span><fmt:message key="ALERT.action" bundle="${fault}"/></span></div>
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="240">
	                    <fmt:message key="COMMON.name1" bundle="${resource}"/><font color=red>*</font>:
	                </td>
	                <td>
	                    <input class="normalInput" name="name" id="name" type=text style="width:300px"
				    	 toolTip='<fmt:message key="COMMON.anotherName" bundle="${zeta}"/>'  maxlength="63" />
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <fmt:message key="SYSTEM.mobileHeader" bundle="${resource}"/><font color=red>*</font>:
	                </td>
	                <td>
	                    <input class="normalInput" name="mobile" id="mobile" type="text" style="width:300px"
				    	maxlength="11" value="" toolTip='<fmt:message key="ALERT.phoneNumCannotEmpty" bundle="${fault}"/>' />
	                </td>
	            </tr>
	            <tr>
	                <td colspan="2" class="txtCenter withoutBorderBottom">
	                    <p class="orangeTxt pT20"><fmt:message key="ALERT.sendTestMassage" bundle="${fault}"/></p>
	                </td>
	            </tr>
	            <tr>	                
	                <td colspan="2" class="txtCenter withoutBorderBottom">
	                    <div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB5 pT5 noWidthCenter">
						         <li><a id="sendSms" onclick="onSendClick()" href="javascript:;" class="normalBtnBig"><span><i class="bmenu_export"></i><fmt:message key="ALERT.sendMessage" bundle="${fault}"/></span></a></li>
						     </ol>
						</div>
						<div id=feedback></div>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	</div>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a  onclick="okClick()" id=okBtn href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i><fmt:message key="COMMON.save" bundle="${resource}"/></span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message key="COMMON.cancel" bundle="${resource}"/></span></a></li>
		     </ol>
		</div>
	</div>

</BODY></HTML>