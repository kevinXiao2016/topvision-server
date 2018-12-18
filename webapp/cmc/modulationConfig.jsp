<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<TITLE>ModulationConfig</TITLE>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmcRes"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript"
	src="../..s/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=c.getUser().getLanguage() %>"></script>
<script type="text/javascript">
function cancelClick() {
	window.top.closeWindow('showModulationConfigDlg');
}
function saveClick() {
	window.parent.showMessageDlg(I18N.entity.alert.TipTitle, I18N.CMC.tip.updateSuccessAfterReset);
	window.top.closeWindow('modulatioFile');
}
</script>
</HEAD>
<BODY class=POPUP_WND onload="donOnload();">
	<div class=formtip id=tips style="display: none"></div>
	<table width=100% cellspacing=0 cellpadding=0>
		<tr>
			<td align=center style="padding-top: 5px;">
				<table cellspacing=5 cellpadding=0>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.usageCode"/>:</td>
						<td><input class=iptxt type=text disabled
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.modControl"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.modulationType"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.preambleLength"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.differEncodeSwitch"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.maxCorrectableBytes"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.fecCorrectCodeSize"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center>ScramblerSeed<fmt:message bundle="${cmcRes}" key="CMC.label.scramblerSeed"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.minSlotCount"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.safeTime"/>:</td>
						<td><input class=iptxt type=text disabled
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.fecStatus"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.Scrambler"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CHANNEL.interleave"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.interSize"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.preambleType"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.trellisCoding"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.scdmaStep"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.spreadSpectrumSwitch"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.scdmaSize"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CHANNEL.channelType"/>:</td>
						<td><input class=iptxt type=text
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td width=80px align=center><fmt:message bundle="${cmcRes}" key="CMC.label.storageType"/>:</td>
						<td><input class=iptxt type=text disabled
							style="width: 180px; align: center" 
							value=""></td>
					</tr>
					<tr>
						<td></td>
						<td></td>
						<td colspan=2 height=41 align=right>
							<button id=saveBtn class=BUTTON75
								onMouseOver="this.className='BUTTON_OVER75'"
								onMouseOut="this.className='BUTTON75'"
								onmousedown="this.className='BUTTON_PRESSED75'"
								onclick="saveClick()"><fmt:message bundle="${cmcRes}" key="CMC.button.save"/></button>&nbsp;
							<button class=BUTTON75
								onMouseOver="this.className='BUTTON_OVER75'"
								onMouseOut="this.className='BUTTON75'"
								onmousedown="this.className='BUTTON_PRESSED75'"
								onclick="cancelClick()"><fmt:message bundle="${cmcRes}" key="CMC.button.cancel"/></button></td>
					</tr>
				</table></td>
		</tr>
	</table>
</BODY>
</HTML>
