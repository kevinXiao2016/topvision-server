<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<HTML>
<HEAD>
<TITLE></TITLE>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/ui.core.js"></script>
<script type="text/javascript" src="../js/jquery/ui.autocomplete.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
  function showAdvance() {
  	window.parent.doAdvanceSearchClick();
  }
  
  $(document).ready(function(){
  	var data = "Core Selectors "+I18N.searchIndex.Monitor+" Traversing Manipulation CSS COKS C1 C2 C3 C4 C5 C6 C7 C8 C9 C10 C11 Events Effects Ajax Utilities".split(" ");
	$("#searchInput").autocomplete(data,{width:260});
  });  
  </script>
</HEAD>
<BODY>
	<table width=600px align="center">
		<tr>
			<td>
				<table>
					<tr>
						<td height=50px><img src="../images/wizard.gif" border=0>
						</td>
						<td><input id="searchInput" class=iptxt type=text
							style="width: 280px;" value="<s:property value="query"/>">
						</td>
						<td><input type=submit class=BUTTON75
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'" value="<fmt:message key="searchIndex.Search" bundle="${resources}"/>">&nbsp;
							<button class=BUTTON75
								onMouseOver="this.className='BUTTON_OVER75'"
								onMouseOut="this.className='BUTTON75'"
								onMouseDown="this.className='BUTTON_PRESSED75'"
								onclick="showAdvance()"><fmt:message key="searchIndex.Advanced" bundle="${resources}"/>...</button>
						</td>
					</tr>
				</table></td>
		</tr>

		<tr>
			<td height=20px style="border-top: solid 1px blue;">&nbsp;</td>
		</tr>

		<tr>
			<td><br>
			<br>
			<br> <s:if test="entities==null||entities.size==0">
					<font style="font-size: 15px;"><fmt:message key="searchIndex.note1" bundle="${resources}"/>"<s:property
							value="query" />" <fmt:message key="searchIndex.matchingdevice" bundle="${resources}"/></font>
				</s:if> <br>
			<br>
			<br></td>
		</tr>

		<tr>
			<td height=20px style="border-bottom: solid 1px blue;">&nbsp;</td>
		</tr>
	</table>
</BODY>
</HTML>
