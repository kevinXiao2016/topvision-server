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
    module FAULT
</Zeta:Loader>
<script type="text/javascript">
var name='${name}';
var mobile= '${mobile}';
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
         failure: function() {Zeta$("feedback").innerHTML = '@RESOURCES/SYSTEM.testFailure@';}
    });
}
function okClick() {
    if(!validate()){return;}
    Ext.Ajax.request({url: '../fault/newSmsAction.tv', method:'POST',
       params:{name:Zeta$('name').value, mobile:Zeta$('mobile').value},
       success: function(response) {
    	   if (response.responseText == "action exist") {
	   	    	window.top.showMessageDlg('@COMMON.tip@', '@RESOURCES/WorkBench.actionNameExist@');
	   	    	return;
	   	   }
       		try {
       			var frame = window.parent.getActiveFrame();
       			frame.refreshActionList();
       		} catch (err) {
       		}
       		try{
	       		if(window.parent.getWindow("alertSetting")){
	       			var aa = window.parent.getWindow("alertSetting").body.dom.firstChild.contentWindow;
	       			aa.location.href = "../fault/alertSetting.tv?activeNum=1";
	           	}
       		}catch(err){
       			
       		}
       		/* top.afterSaveOrDelete({
   	            title: '@COMMON.tip@',
   	            html: '<b class="orangeTxt">@resources/COMMON.addSuccess@</b>'
   	        }); */
       		cancelClick();
       		top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@resources/COMMON.success@</b>'
   			});
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
</HEAD>
<body class="openWinbody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt">@ALERT.action@</b></p>
	    	<p>
	    		<span id="newMsg">@ALERT.buildNewMessageAction@
	    	</p>
	    </div>
	    <div class="rightCirIco phoneCirIco"></div>
	</div>
	<div class="edge10">
		<div class="zebraTableCaption">
	     	<div class="zebraTableCaptionTitle"><span>@ALERT.action@</span></div>
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt" width="240">
		                    @RESOURCES/COMMON.name@<font color=red>*</font>:
		                </td>
		                <td>
		                    <input class="normalInput w300" name="name" id="name" type=text toolTip='@COMMON.anotherName@'
								 maxlength="63" />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">
		                    @RESOURCES/SYSTEM.mobileHeader@<font color=red>*</font>:
		                </td>
		                <td>
		                    <input class="normalInput w300" name="mobile" id="mobile" type=text toolTip='@ALERT.phoneNumCannotEmpty@'
								 maxlength="11" />
		                </td>
		            </tr>
		            <tr>
				         <td colspan="2" class="txtCenter withoutBorderBottom">
				            	<p class="orangeTxt pT20">@ALERT.sendTestMassage@</p>
				         </td>
				    </tr>
				    <tr>
		            	<td colspan="2" class="withoutBorderBottom">
		            		<div class="noWidthCenterOuter clearBoth">
							     <ol class="upChannelListOl pB10 pT0 noWidthCenter">
							         <li><a onclick="onSendClick()" id=sendSms  href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSend"></i>@ALERT.sendMessage@</span></a></li>
							     </ol>
							</div>
							<div id=feedback></div>
		            	</td>
		            </tr>
		        </tbody>
		    </table>
		</div>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a onclick="lastClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@ALERT.beforeStep@</span></a></li>
		         <li><a id=okBtn onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@RESOURCES/COMMON.finish@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@RESOURCES/COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>