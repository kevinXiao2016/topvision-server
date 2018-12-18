<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc" />
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var readCommunity = '<s:property value="snmpCommunityTable.readCommunity"/>';
var writeCommunity = '<s:property value="snmpCommunityTable.writeCommunity"/>';
function cancelClick() {
	window.top.closeWindow('snmpBasicInfo');
}
function saveClick(){
	$.ajax({
    	url:'/cmc/config/modifySnmpBasicInfo.tv?entityId='+ cmcId + "&entityId=" + entityId+ "&topCmFlapInterval=" + topCmFlapInterval,
	  	type:'POST',
	  	dateType:'text',
	  	success:function(response){
	  		if(response == "success"){
	  			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, '');
	  		}else{
	  			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg,'');
	  		}
			cancelClick();
	  	},
	  	error:function(){
	  		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, '');
	  	},
	  	cache:false
    });
}
function dataload(){
	$('#snmpReadCommunity').val(readCommunity);
	$('#snmpWriteCommunity').val(writeCommunity);
}

</script>
</HEAD>
<BODY class=POPUP_WND style="padding-left: 10px;padding-top: 10px;" onload="dataload()">
	<div class=formtip id=tips style="display: none"></div>
	<fieldset style="background-color: #ffffff; width: 350px; height: 150px;">
		<form name="formChanged" id="formChanged">
			<table>
				<tr>
					<td width=120 style="padding: 20px;"><fmt:message bundle="${cmc}" key="CMC.tip.readCommunity" /></td>
					<td><input type=text id="snmpReadCommunity" class=iptxt
						onfocus="inputFocused('snmpReadCommunity', I18N.CMC.tip.snmpReadInput, 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');"
						onclick="clearOrSetTips(this);" style="width: 125px;"></td>
				</tr>
				<tr>
					<td width=120 style="padding: 20px;"><fmt:message bundle="${cmc}" key="CMC.tip.writeCommunity" /></td>
					<td><input type=text id="snmpWriteCommunity" class=iptxt 
						onfocus="inputFocused('snmpWriteCommunity', I18N.CMC.tip.snmpWriteInput, 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');"
						onclick="clearOrSetTips(this);" style="width: 125px;"></td>
				</tr>
			</table>
		</form>
	</fieldset>
	<div align="right" style="padding-top: 10px; padding-right:9px">
		<button id=saveBtn class=BUTTON75 
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onmousedown="this.className='BUTTON_PRESSED75'" onclick="saveClick()">
			<fmt:message bundle="${cmc}" key="CMC.button.save" /></button>
		&nbsp;
		<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onmousedown="this.className='BUTTON_PRESSED75'"
			onclick="cancelClick()"><fmt:message bundle="${cmc}" key="CMC.button.close" /></button>
	</div>
</BODY>
</HTML>