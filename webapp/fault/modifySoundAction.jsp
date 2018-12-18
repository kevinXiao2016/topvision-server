<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML><HEAD>
<TITLE>New Sound Action</TITLE>
<%@include file="../include/cssStyle.inc"%>
<link rel="STYLESHEET" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="STYLESHEET" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
</HEAD><BODY class=POPUP_WND>
<div class=formtip id=tips style="display: none"></div>
<table width=100% height=100% cellspacing=0 cellpadding=0>
<tr><td class=WIZARD-HEADER>
<table width=100%><tr><td><font style="font-weight:bold"><fmt:message key="ALERT.action" bundle="${fault}"/></font><br><br>&nbsp;&nbsp;<span id="newMsg"><fmt:message key="ALERT.newSoundAction" bundle="${fault}"/></span></td>
<td align=right class=WIZARD-RHEADER><img src="../images/fault/sound.gif" border=0></td></tr></table>
</td></tr>
<tr><td style="padding:5px 10px 0 10px" valign=top>
<table width=100% cellspacing=0 cellpadding=0>
    <tr><td height=30 width=100px><fmt:message key="COMMON.name" bundle="${resource}"/><font color=red>*</font>:</td><td><input class=iptxt name="name" id="name" type=text 
    	style="width:250px" value=""	
		onfocus="inputFocused('name', '<fmt:message key="ALERT.soundActionNameCannotEmpty" bundle="${fault}"/>', 'iptxt_focused')"
		onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);"></td></tr>
    <tr><td height=30><fmt:message key="ALERT.soundFile" bundle="${fault}"/><font color=red>*</font>:</td><td>
    <select id="sounds" name="sounds" style="width:250px">
    <s:iterator value="sounds">
    <option value="<s:property/>"><s:property/></option>
    </s:iterator>
    </select>
    </td></tr>
    <tr><td align=right colspan=2><button id=playBtn class=BUTTON75 
    onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'" 
    onclick="playClick()"><fmt:message key="ALERT.play" bundle="${fault}"/></button></td></tr>
</table>
</td></tr>
<tr><td height=41 style="padding:10px">
<table width=100% cellspacing=0 cellpadding=0>
<tr><td align=right>
<button id=okBtn class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'" onclick="okClick()"><fmt:message key="COMMON.finish" bundle="${resource}"/></button>&nbsp;
<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button>
</td></tr></table>
</td></tr>
</table>
<bgSound src="" id=mybgsound loop=0 autoStart=true>
<script>
var name='<s:property value="name"/>';
var soundFile = '<s:property value="sound"/>';
Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../images/s.gif';
    var el = Zeta$('sounds');
    for (var i = 0; i < el.options.length; i++) {
    	if (el.options[i].value == soundFile) {
    		el.selectedIndex = i;
    		break;
    	}
    }
    Zeta$('name').value = name;
    //Zeta$('lastBtn').disabled = Zeta$('okBtn').disabled = (name != null);
    Zeta$('name').focus();
});
function validate() {
	var el = Zeta$('name');
    var name = el.value;
    if(Zeta$('name').value==null || Zeta$('name').value=='' || Zeta$('name').value.length > 32  ) {
        el.focus();
        return false;
    }
    return true;
}
function playClick() {
	var el = Zeta$('sounds');
    var url = "../epon/sound/alertSounds/" + el.options[el.selectedIndex].value;
    Zeta$('mybgsound').src = url;
    Zeta$('mybgsound').autoStart = true;
}
function okClick() {
    if(!validate()){return;}
    var el = Zeta$('sounds');
    soundFile = el.options[el.selectedIndex].value;
    Ext.Ajax.request({url: '../fault/newSoundAction.tv', method:'POST',
       params:{name: Zeta$('name').value,sound: soundFile},
       success: function(response) {
       		try {
       			var frame = window.parent.getActiveFrame();
       			frame.refreshActionList();
       		} catch (err) {
       		}
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
    window.open('../help/index.jsp?module=newSoundAction', 'help');
}
</script>
</BODY></HTML>
