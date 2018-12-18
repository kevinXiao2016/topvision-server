<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<STYLE>
v\:* {
	Behavior: url(#default#VML)
}
</STYLE>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<title><fmt:message bundle='${cmc}' key='CM.locateMessage'/></title>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
var cmId = '<s:property value="cmId"/>';
var cmcId = '<s:property value="cmTopologyInfo.cmcId"/>';
var entityId = '<s:property value="cmTopologyInfo.entityId"/>';
var upPortId = '<s:property value="cmTopologyInfo.upPortId"/>';
var downPortId = '<s:property value="cmTopologyInfo.downPortId"/>';
var ponId = '<s:property value="cmTopologyInfo.ponId"/>';
var ip = '<s:property value="cmTopologyInfo.ip"/>';
var mac = '<s:property value="cmTopologyInfo.mac"/>';
var cmcMac = '<s:property value="cmTopologyInfo.cmcMac"/>';

function cancleClick(){
	window.parent.closeWindow('showCmLocationDlg');
}
</script>
</head>
<body class=BLANK_WND>
	<div style="text-align: center; color: red; border: thick; size: 20px;"><fmt:message bundle='${cmc}' key='CMC.text.getCmLocationError'/></div>
	<fieldset
		style="background-color: #ffffff; width: 580px; height: 240px;"
		align="center">
		<table width="100%" height="85%">
			<tr height="85%" style="background-color: white;">
				<td>
					<div>
						<v:RoundRect
							style="position:absolute;z-index:5;left:40;top:40;width:120;height:180;"
							arcsize="0.03">
							<div align="center" style="padding-top: 60px;"><fmt:message bundle='${cmc}' key='CCMTS.entityStyle'/>:PN8602</div>
							<div align="center" style="padding-top: 2px;">
								<fmt:message bundle='${cmc}' key='CCMTS.entityName'/>:
								<s:property value="oltEntity.sysName" />
							</div>
							<div align="center" style="padding-top: 2px;">
								IP：
								<s:property value="cmTopologyInfo.ip" />
							</div>
						</v:RoundRect>
						<v:RoundRect
							style="position:absolute;left:172;top:100;width:60;height:30px;z-index=5"
							stroked="false">
							<div align="center" style="padding-top: 5px;">
								SLOT&nbsp;
								<s:property value="cmTopologyInfo.slot" />
							</div>
						</v:RoundRect>
						<v:RoundRect
							style="position:absolute;left:172;top:124;width:60;height:30px;z-index=5"
							stroked="false">
							<div align="center" style="padding-top: 5px;">
								<fmt:message bundle='${cmc}' key='CMC.label.Pon'/>
								<s:property value="cmTopologyInfo.ponNo" />
							</div>
						</v:RoundRect>
						<v:line strokecolor="#80D1E6" strokeweight="5"
							style="z-index:5;position:absolute;left:130;top:125"
							from="36.5,0" to="124,0">
							<v:stroke EndArrow="Classic" />
						</v:line>
						<v:RoundRect
							style="position:absolute;z-index:5;left:259;top:40;width:110;height:180;"
							arcsize="0.03">
							<div align="left" style="padding-top: 20px; padding-left: 5px;"><fmt:message bundle='${cmc}' key='CCMTS.entityStyle'/>:</div>
							<div align="center" style="padding-top: 2px;">
								<s:property value="cmcAttribute.cmcDeviceStyleString" />
							</div>
							<div align="left" style="padding-top: 2px; padding-left: 5px;"><fmt:message bundle='${cmc}' key='CCMTS.entityName'/>:</div>
							<div align="center" style="padding-top: 2px;">
								<s:property value="cmcAttribute.topCcmtsSysName" />
							</div>
							<div align="left" style="padding-top: 2px; padding-left: 5px;"><fmt:message bundle='${cmc}' key='CCMTS.macAddress'/>:</div>
							<div align="center" style="padding-top: 2px;">
								<s:property value="cmcAttribute.topCcmtsSysMacAddr" />
							</div>
							<div align="left" style="padding-top: 2px; padding-left: 5px;"><fmt:message bundle='${cmc}' key='CCMTS.inlineStatus'/>:</div>
							<div align="center" style="padding-top: 2px;">
								<s:property value="cmcAttribute.topCcmtsSysStatusString" />
							</div>
							<v:shadow on="t" color="black" opacity="52428f"
								offset="1.5pt,1.5pt" />
						</v:RoundRect>
						<v:RoundRect
							style="position:absolute;left:375;top:78;width:70;height:26px;z-index=4"
							stroked="false">
							<div align="center" style="padding-top: 5px;">
								<fmt:message bundle='${cmc}' key='CCMTS.downStreamChannel'/>
								<s:property value="cmTopologyInfo.docsIfDownChannelId" />
							</div>
						</v:RoundRect>
						<v:line strokecolor="#80D1E6" strokeweight="5"
							style="z-index:5;position:absolute;left:340;top:100" from="36,0"
							to="124,0">
							<v:stroke EndArrow="Classic" />
						</v:line>
						<v:line strokecolor="#80D1E6" strokeweight="5"
							style="z-index:5;position:absolute;left:340;top:150" from="36,0"
							to="124,0">
							<v:stroke StartArrow="Classic" />
						</v:line>
						<v:RoundRect
							style="position:absolute;left:390;top:150;width:70;height:26px;z-index=4"
							stroked="false">
							<div align="center" style="padding-top: 5px;">
								<fmt:message bundle='${cmc}' key='CCMTS.upStreamChannel'/>
								<s:property value="cmTopologyInfo.docsIfUpChannelId" />
							</div>
						</v:RoundRect>
						<v:RoundRect
							style="position:absolute;z-index:5;left:472;top:40;width:110;height:180;"
							arcsize="0.03">
							<v:shadow on="t" color="black" opacity="52428f"
								offset="1.5pt,1.5pt" />
							<div align="left" style="padding-top: 20px; padding-left: 5px;"><fmt:message bundle='${cmc}' key='CCMTS.entityStyle'/>:</div>
							<div align="center" style="padding-top: 2px;">CM</div>
							<div align="left" style="padding-top: 2px; padding-left: 5px;"><fmt:message bundle='${cmc}' key='CMC.text.equipmentIP'/>:</div>
							<div align="center" style="padding-top: 2px;">
								<s:property value="cmAttribute.statusInetAddress" />
							</div>
							<div align="left" style="padding-top: 2px; padding-left: 5px;"><fmt:message bundle='${cmc}' key='CCMTS.macAddress'/>:</div>
							<div align="center" style="padding-top: 2px;">
								<s:property value="cmAttribute.statusMacAddress" />
							</div>
							<div align="left" style="padding-top: 2px; padding-left: 5px;"><fmt:message bundle='${cmc}' key='CMC.title.status'/>:</div>
							<div align="center" style="padding-top: 2px;">
								<s:property value="cmAttribute.docsIfCmtsCmStatusValueString" />
							</div>
						</v:RoundRect>
					</div>
				</td>
			</tr>
		</table>
	</fieldset>
	<div align=right
		style="margin-right: 8px; margin-bottom: 15px; margin-top: 15px;">
		<button id=saveBt class=BUTTON75 type="button"
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onMouseDown="this.className='BUTTON_PRESSED75'"
			onclick="cancleClick()"><fmt:message bundle='${cmc}' key='CMC.label.close'/></button>
	</div>
</body>
</html>