<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    import js.tools.ipText
    module FAULT
</Zeta:Loader>

<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var name='${name}';
var email='${email}';

function isEmail(str){
    var reg = /^[\w-]+(\.[\w-]+)*@([a-z0-9-]+(\.[a-z0-9-]+)*?\.[a-z]{2,6}|(\d{1,3}\.){3}\d{1,3})(:\d{4})?$/;
    return reg.test(str);
}
Ext.onReady(function () {
    Zeta$('name').value = name;
    Zeta$('email').value = email;
    //Zeta$('lastBtn').disabled = (name != null);
    Zeta$('name').focus();
});
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
    Ext.Ajax.request({url: '../fault/newEmailAction.tv', method:'POST',
       params:{name:Zeta$('name').value, email:Zeta$('email').value},
       success: function(response) {
    	   if (response.responseText == "action exist") {
    	    	window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.actionNameExist);
    	    	return;
    	   }
       		try {
       			var frame = window.parent.getActiveFrame();
       			frame.refreshActionList();
       			top.afterSaveOrDelete({
       	            title: '@COMMON.tip@',
       	            html: '<b class="orangeTxt">@resources/COMMON.addSuccess@</b>'
       	        });
       		} catch (err) {
       		}
       		if(window.parent.getWindow("alertSetting")){
       			var aa = window.parent.getWindow("alertSetting").body.dom.firstChild.contentWindow;
       			aa.location.href = "../fault/alertSetting.tv?activeNum=1";
           	}
       		cancelClick();
       		top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@resources/COMMON.success@</b>'
   			});
       }
    });
}
function lastClick() {
    location.href = '../fault/newAction.jsp';
}
function cancelClick() {
    window.top.closeWindow("modalDlg");
}
function showHelp() {
    window.open('../help/index.jsp?module=newEmailAction', 'help');
}
function emailServerClick() {
    window.top.createDialog('mailServerDlg', I18N.SYSTEM.mailServer, 400, 480, 'system/showEmailServer.tv', null, true, true);
}
</script>
</head>
	<body class="openWinbody">
		<div class="openWinHeader">
		    <div class="openWinTip">
		    	<p><b class="orangeTxt">@ALERT.action@</b></p>
		    	<p><span id="newMsg">@ALERT.buildNewEmailAction@</span></p>
		    </div>
		    <div class="rightCirIco alarmCirIco"></div>
		</div>
		<div class="edge10">
			<div class="zebraTableCaption">
	    		<div class="zebraTableCaptionTitle"><span>@ALERT.action@</span></div>
			    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			        <tbody>
			            <tr>
			                <td class="rightBlueTxt" width="200">
			                    @RESOURCES/COMMON.name@<font color=red>*</font>:
			                </td>
			                <td>
			                    <input class="normalInput w300" id="name" toolTip='@COMMON.anotherName@' type=text maxlength="63"/>
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                	@ALERT.email@<font color=red>*</font>:
			                </td>
			                <td>
			                    <input class="normalInput w300" name="email" id="email" toolTip='@ALERT.emailCannotEnmpty@' 
						    	type="text" value="" />
			                </td>
			            </tr>
			            <tr>
			            	<td colspan="2" class="txtCenter withoutBorderBottom">
			            		<p class="orangeTxt pT20">@ALERT.sendTestEmail@</p>
			            	</td>
			            </tr>
			            <tr>
			            	<td colspan="2" class="withoutBorderBottom">
			            		<div class="noWidthCenterOuter clearBoth">
								     <ol class="upChannelListOl pB10 pT0 noWidthCenter">
								         <li><a onclick="onSendClick()" id=sendEmail href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSend"></i>@ALERT.sendEmail@</span></a></li>
								     </ol>
								</div>
								<div id = "feedback"></div>
			            	</td>
			            </tr>
			        </tbody>
			    </table>
			</div>
		    <div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
			         <li><a onclick="lastClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@ALERT.beforeStep@</span></a></li>
			         <li><a  id=okBtn  onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@RESOURCES/COMMON.finish@</span></a></li>
			         <li><a  onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@RESOURCES/COMMON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
</body>
</Zeta:HTML>