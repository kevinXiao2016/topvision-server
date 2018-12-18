<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML>
<HEAD>
<%@include file="../../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<link rel="STYLESHEET" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%=cssStyleName%>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var userId = <%= uc.getUser().getUserId() %>
var lang = '<s:property value="lang"/>';
var defaultLang = '<s:property value="defaultLang"/>';
function okClick() {
		var lang = Zeta$('languageSelect').value;
	        $.ajax({
	            url: '/include/changeLanguage.tv?userId='+userId+'&lang='+lang+'&r=' + Math.random(),
	            type: 'POST',
	            dataType:'json',
	            success: function() {
	            }
	        });
	        window.parent.location.reload(); 
	}

function cancelClick() {
		window.parent.closeWindow("modalDlg");
	}
	
function doOnload() {
	Zeta$('defaultLanguage').value = defaultLang;
	if(lang == null || lang == ''){
		Zeta$('languageSelect').value = 'zh_CN';
	}else{
		//Zeta$('languageSelect').value = lang;
		$("#languageSelect").val(lang);
	}
	}
</script>
</HEAD>
<BODY class="openWinBody" onload="doOnload()">
	<div class="openWinHeader">
	    <div class="openWinTip"><fmt:message bundle="${resources}" key="SYSTEM.systemLanguageConfig" /></div>
	    <div class="rightCirIco flagCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">

     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
         <tbody>
             <tr>
                 <td class="rightBlueTxt" width="200">
					<fmt:message bundle="${resources}" key="SYSTEM.defaultLanguage" />
                 </td>
                 <td>
					<input id="defaultLanguage"
							name="defaultLanguage" style="width: 180px" disabled="disabled" class="normalInputDisabled" />
                 </td>
             </tr>
             <tr class="darkZebraTr">
                 <td class="rightBlueTxt">
					<fmt:message bundle="${resources}" key="SYSTEM.selectYourLanguage" />
                 </td>
                 <td>
					<select id="languageSelect" class="normalSel"
						name="languageSelect" style="width: 180px">
							<option value="zh_CN"><fmt:message bundle="${resources}" key="SYSTEM.simpleChinese" /></option>
							<option value="en_US"><fmt:message bundle="${resources}" key="SYSTEM.USenglish" /></option>
					</select>
                 </td>
             </tr>
         </tbody>
     </table>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
	         <li><a  id=saveBtn href="javascript:;" class="normalBtnBig"  onclick="okClick()"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${resources}" key="COMMON.save" /></span></a></li>
	         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${resources}" key="COMMON.cancel" /></span></a></li>
	     </ol>
	</div>
</div>
	

	<div class=formtip id=tips style="display: none"></div>
	
</BODY>
</HTML>
