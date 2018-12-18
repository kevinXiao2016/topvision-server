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
<HTML><HEAD>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var name='${name}';
var address='${address}';
var port='${port}';
var sysObjectID='${sysObjectID}';
var community='${community}';
var content='${content}';
var oid='${oid}';
Ext.BLANK_IMAGE_URL = '../images/s.gif';
function isNotOid(oid){
    var reg = /[^0-9^.]/;
    return reg.test(oid);
}
Ext.onReady(function () {
    Zeta$('name').value = name;
    Zeta$('address').value = address;
    Zeta$('port').value = port;
    Zeta$('sysObjectID').value = sysObjectID;
    Zeta$('community').value = community;
    Zeta$('content').value = content;
    Zeta$('oid').value = oid;
});
function validate() {
	var $name = Zeta$('name').value;
    if(!V.isAlias($name, [0, 32]) ){
        Zeta$('name').focus();
        return false;
    } else if(Zeta$('address').value == ''){
        Zeta$('address').focus();
        return false;
    }else if(Zeta$('port').value == ''){
        Zeta$('port').focus();
        return false;
    }else if(Zeta$('sysObjectID').value == ''){
        Zeta$('sysObjectID').focus();
        return false;
    }else if(Zeta$('community').value == ''){
        Zeta$('community').focus();
        return false;
    }else if(Zeta$('content').value == ''){
        Zeta$('content').focus();
        return false;
    }else if(Zeta$('oid').value == ''){
        Zeta$('oid').focus();
        return false;
    }else if(isNotOid(Zeta$('oid').value.trim())){
    	window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.snmpOidError);
        return ;
    }
    return true;
}
function onSendClick() {
    if(!validate()){return;}
    Ext.Ajax.request({url: '../fault/sendTestTrap.tv', method:'POST',
       params:{name:Zeta$('name').value, address:Zeta$('address').value, port:Zeta$('port').value, sysObjectID:Zeta$('sysObjectID').value, community:Zeta$('community').value, content:Zeta$('content').value, oid:Zeta$('oid').value},
       success: function(response) {
         var json = Ext.util.JSON.decode(response.responseText);
         Zeta$("feedback").innerHTML = json.feedback;},
       failure: function() {Zeta$("feedback").innerHTML = I18N.SYSTEM.testFailure;}
    });
}
function okClick() {
    if(!validate()){return;}
    Ext.Ajax.request({url: '../fault/newTrapAction.tv', method:'POST',
       params:{name:Zeta$('name').value, address:Zeta$('address').value, 
       	port:Zeta$('port').value, sysObjectID:Zeta$('sysObjectID').value, community:Zeta$('community').value, content:Zeta$('content').value, oid:Zeta$('oid').value},
       success: function(response) {
       		try {
       			var frame = window.parent.getActiveFrame();
       			frame.refreshActionList();
       		} catch (err) {
       		}
       		if(window.parent.getWindow("alertSetting")){
       			var aa = window.parent.getWindow("alertSetting").body.dom.firstChild.contentWindow;
       			aa.location.href = "../fault/alertSetting.tv?activeNum=3";
           	}
       		top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@resources/COMMON.addSuccess@</b>'
            });
       		cancelClick();
       		top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@resources/COMMON.newSuccess@</b>'
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
    window.open('../help/index.jsp?module=newTrapAction', 'help');
}
</script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/nm3kToolTip.js"></script>

</HEAD><body class="openWinbody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt">@ALERT.action@</b></p>
	    	<p>
	    		<span id="newMsg">@ALERT.buildNewTrapAction@</span>
	    	</p>
	    </div>
	    <div class="rightCirIco flagCirIco"></div>
	</div>
	<div class="edge10">
	<div class="zebraTableCaption">
	    <div class="zebraTableCaptionTitle"><span>@ALERT.action@</span></div>	
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt" width="140">
		                    @RESOURCES/COMMON.name@<font color=red>*</font>:
		                </td>
		                <td width="160">
		                  <input class="normalInput w150" name="name" id="name" type="text" toolTip='@ALERT.nameCannotEmpty@' />
		                </td>
		                <td class="rightBlueTxt" width="140">
		                    @ALERT.trapAddress@<font color=red>*</font>:
		                </td>
		                <td>
		                  <input class="normalInput w150" name="address" id="address" type="text" toolTip='@ALERT.trapAddressCannotEmpty@' />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">
		                   @ALERT.trapPort@<font color=red>*</font>:
		                </td>
		                <td>
		                   <input class="normalInput w150" name="port" id="port" type="text" toolTip='@ALERT.trapPortCannotEmpty@'
		    				 onkeyup="this.value=this.value.replace(/[^\d]/g,'')" />
		                </td>
		                <td class="rightBlueTxt">
		                   @ALERT.objectId@<font color=red>*</font>:
		                </td>
		                <td>
		                   <input class="normalInput w150" name="sysObjectID" id="sysObjectID" type="text" toolTip='@ALERT.objectIdCannotEmpty@' 
		    				 onkeyup="this.value=this.value.replace(/[^\d]/g,'')" />
		                </td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt">
		                    @ALERT.community@<font color=red>*</font>:
		                </td>
		                <td>
		                  <input class="normalInput w150" name="community" id="community" type="text" toolTip='@ALERT.communityCannotEmpty@' 
		   			 		 />
		                </td>
		                <td class="rightBlueTxt" >
		                    @ALERT.messageBinding@<font color=red>*</font>:
		                </td>
		                <td>
		                  <input class="normalInput w150" name="content" id="content" type="text" toolTip='@ALERT.messageCannotEmpty@' />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">
		                   @ALERT.snmpTrapOid@<font color=red>*</font>:
		                </td>
		                <td>
		                   <input class="normalInput w150" name="oid" id="oid" type="text" toolTip='@ALERT.snmpTrapOidCannotEmpty@' />
		                </td>
		                <td class="rightBlueTxt">
		                   
		                </td>
		                <td>
		                   
		                </td>
		            </tr>
		            <tr>               
		                <td colspan="4" class="txtCenter withoutBorderBottom">
		                   <p class="orangeTxt pT20">@ALERT.sendTrapTest@</p>
		                </td>
		            </tr>
		            <tr>
		                <td colspan="4" class="withoutBorderBottom">
		                   <div class="noWidthCenterOuter clearBoth">
							     <ol class="upChannelListOl pB10 pT0 noWidthCenter">
							         <li><a  id=sendTrap onclick="onSendClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSend"></i>@ALERT.sendTrap@</span></a></li>
							     </ol>
							</div>
							<div id=feedback></div>
		                </td>                
		            </tr>
		        </tbody>
		    </table>
	</div>
    <div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
	         <li><a  onclick="lastClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@ALERT.beforeStep@</span></a></li>
	         <li><a id=okBtn onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@RESOURCES/COMMON.finish@</span></a></li>
	         <li><a  onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@RESOURCES/COMMON.cancel@</span></a></li>
	     </ol>
	</div>
</div>
</body></Zeta:HTML>
