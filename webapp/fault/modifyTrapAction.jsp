<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
    module fault
</Zeta:Loader>

<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var actionId = '<s:property value="actionId"/>';
var name='<s:property value="name"/>';
var address='<s:property value="address"/>';
var port='<s:property value="port"/>';
var sysObjectID='<s:property value="sysObjectID"/>';
var community='<s:property value="community"/>';
var content='<s:property value="content"/>';
var oid='<s:property value="oid"/>';
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function () {
    Zeta$('name').value = name;
    Zeta$('address').value = address;
    Zeta$('port').value = port;
    Zeta$('sysObjectID').value = sysObjectID;
    Zeta$('community').value = community;
    Zeta$('content').value = content;
    Zeta$('oid').value = oid;
});

function isNotOid(oid){
    var reg = /[^0-9^.]/;
    return reg.test(oid);
}

function validate() {
    if(Zeta$('name').value==null || Zeta$('name').value=='' || Zeta$('name').value.length > 32  ){
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
    Ext.Ajax.request({url: '../fault/updateTrapAction.tv', method:'POST',
       params:{actionId: actionId, name:Zeta$('name').value, address:Zeta$('address').value, 
       	port:Zeta$('port').value, sysObjectID:Zeta$('sysObjectID').value, community:Zeta$('community').value, content:Zeta$('content').value, oid:Zeta$('oid').value},
       success: function(response) {
       		try {
       			var frame = window.parent.getActiveFrame();
       			frame.refreshActionList();
       			top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: '<b class="orangeTxt">' + I18N.COMMON.modifySuccess +'</b>'
       			});
       		} catch (err) {
       		}   
       		top.afterSaveOrDelete({
   	            title: '@COMMON.tip@',
   	            html: '<b class="orangeTxt">@resources/COMMON.modifySuccess@</b>'
   	        });
       		cancelClick();
       },
       failure: function() {Zeta$("feedback").innerHTML = '';}
    });
}
function cancelClick() {
    window.top.closeWindow("modalDlg");
}
</script>
</HEAD><body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt">@ALERT.action@</b></p>
	    	<p>
	    		<span id="newMsg">@ALERT.modifyTrapAction@</span>
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
			                   	@RESOURCES/COMMON.name1@<font color=red>*</font>:
			                </td>
			                <td width="160">
			                  <input class="normalInput" name="name" id="name" type=text style="width:200"
			                  toolTip='@ALERT.nameCannotEmpty@' />
			                </td>
			                <td class="rightBlueTxt" width="140">
			                    @ALERT.trapAddress@<font color=red>*</font>:
			                </td>
			                <td>
			                  <input class="normalInput" name="address" id="address" type=text style="width:200"
			                   toolTip='@ALERT.trapAddressCannotEmpty@' />
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                   @ALERT.trapPort@<font color=red>*</font>:
			                </td>
			                <td>
			                   <input class="normalInput" name="port" id="port" type=text style="width:200" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" 
			                   toolTip='@ALERT.trapPortCannotEmpty@' />
			                </td>
			                <td class="rightBlueTxt">
			                   @ALERT.objectId@<font color=red>*</font>:
			                </td>
			                <td>
			                   <input class="normalInput" name="sysObjectID" id="sysObjectID" type=text style="width:200" 
			                   toolTip='@ALERT.objectIdCannotEmpty@'
			                    onkeyup="this.value=this.value.replace(/[^\d]/g,'')" />
			                </td>
			            </tr>
			            <tr>
			                <td class="rightBlueTxt">
			                   @ALERT.community@<font color=red>*</font>:
			                </td>
			                <td>
			                  <input class="normalInput" name="community" id="community" type=text style="width:200"
			                	  toolTip='@ALERT.communityCannotEmpty@' />
			                </td>
			                <td class="rightBlueTxt">
			                    @ALERT.messageBinding@<font color=red>*</font>:
			                </td>
			                <td>
			                  <input class="normalInput" name="content" id="content" type=text style="width:200"
			                  toolTip='@ALERT.messageCannotEmpty@' />
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                   @ALERT.snmpTrapOid@<font color=red>*</font>:
			                </td>
			                <td>
			                   <input class="normalInput" name="oid" id="oid" type=text style="width:200" 
    							toolTip='@ALERT.snmpTrapOidCannotEmpty@' />
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
								         <li><a id="sendTrap" onclick="onSendClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoUpdate"></i>
								         @ALERT.sendTrap@
								         </span></a></li>
								     </ol>
								</div>
								<div id="feedback" class="txtCenter"></div>
			                </td>                
			            </tr>
			        </tbody>
			    </table>
		</div>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a id="okBtn" onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@RESOURCES/COMMON.finish@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span>@RESOURCES/COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</BODY></Zeta:HTML>