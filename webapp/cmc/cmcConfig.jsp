<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<title><fmt:message bundle='${cmc}' key='CMC.title.configinfo'/></title>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/cmc/cmcConfig.js"></script>
<script type="text/javascript">
//后台传递数据
var cmcId = <s:property value="cmcId"/>;
var productType = <s:property value="productType"/>;
cmcIpInfo = ${cmcSystemIpInfo};
cmcLocation = "<s:property value='cmcAttribute.topCcmtsSysLocation' />";
cmcDescr = "<s:property value='cmcAttribute.topCcmtsSysDescr' />";
cmcName = "<s:property value='cmcAttribute.nmName'/>";
cmcContact = "<s:property value='cmcAttribute.topCcmtsSysContact' />";
ccmctSniJson = ${ccmctSniJson};
var dhcpGiAddrList = ${dhcpGiAddrList};
var dhcpBundleList =${dhcpBundleList};
var snmpValues={
		"entityId":"${entity.entityId}",
		"emsIpAddress":"${entity.ip}",
		"readCommunity":"${snmpParam.community}",
		"writeCommunity":"${snmpParam.writeCommunity}"
		};

alloc = <s:property value="topCcmtsDhcpAlloc"/>;
function onRefreshClick() {
	window.location.href=window.location.href;
}
</script>
</head>
<body class=BLANK_WND style="margin: 15px;">
	<table>
	<tr>
	<td valign=top><%@ include file="entity.inc"%></td>
	</tr>
	<tr><td>
		<div align="right" style="padding-right: 5px; padding-top: 8px;">
			<select id="cmcConfigType" style="width: 150px;" onchange="listTypeChanged()">
				<option value="0" <s:if test="type==0">selected</s:if>><fmt:message bundle='${cmc}' key='CMC.text.baseCfg'/></option>
				<option value="1" <s:if test="type==1">selected</s:if>>DHCP</option>
			</select>
		</div>
		</td>
	</tr>
	<tr><td>
	<div id="baseInfo-div" style="display: block;">
	<table width=100% cellspacing=5 cellpadding=0>
		<tr style="pading-top: 15px;">
			<td>
				<fieldset style='width: 100%; height: 80px; margin: 10px'>
					<legend><fmt:message bundle='${cmc}' key='CMC.text.baseCfg'/></legend>
					<table cellspacing=5 cellpadding=0 style='margin-left:20px;'>
						<tr height=28><td width=85>
							<span><fmt:message bundle='${cmc}' key='CCMTS.entityName'/>:</span><font color=red>*</font>
						</td><td width=220>
							<input type=text id="cmcName" value='<s:property value="cmcAttribute.nmName" />' style="width:171px;" />
						</td><td width=85><fmt:message bundle='${cmc}' key='CCMTS.contactPerson'/>:
						</td><td width=220>
							<input type=text id="cmcContact" value='<s:property value="cmcAttribute.topCcmtsSysContact" />' style="width:171px;" />
						</td><td width=100 align=right>
							<span id="tipText_1" class="tipTextClass" style="display:none;"></span>
						</td></tr>
						<tr height=28><td>
							<span><fmt:message bundle='${cmc}' key='CCMTS.entityLocation'/>:</span>
						</td><td>
							<input type=text id="cmcLocation" value='<s:property value="cmcAttribute.topCcmtsSysLocation" />' style="width:171px;" />
						</td><td>
							<span><fmt:message bundle='${cmc}' key='CCMTS.entityDescrib'/>:</span>
						</td><td>
							<input type=text id="cmcDescr" value='<s:property value="cmcAttribute.topCcmtsSysDescr" />' 
								style="border:1px solid #ccc;width:171px;" disabled />
						</td><td width=100 align=right>
							<span id="tipText_2" class="tipTextClass" style="display:none;"></span>
						</td></tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<!--//暂时注释掉DHCP客户端功能
		<tr>
			<td>
				<fieldset style='width: 100%;margin: 10px'>
					<legend>DHCP</legend>
					<div style="width: 90%;height: 28px;">
						<div style="margin-left: 25px;"><fmt:message bundle='${cmc}' key='CMC.title.dhcpClient'/>:
								<input name='dhcpClient' type="radio" onclick="checkDhcpAlloc()" value='1' disabled="disabled"><fmt:message bundle='${cmc}' key='CMC.text.up'/>&nbsp;
								<input name='dhcpClient' type="radio" onclick="checkDhcpAlloc()" value='2'><fmt:message bundle='${cmc}' key='CMC.text.down'/>   
								<span id= option60Id style="display:none; ">OPTION60:<input type="text" size="2" value="<s:property value="dhcpAllocOption60"/>"></span>
						</div>
					</div>
				</fieldset>
			</td>
		</tr>
		-->
		<tr>
			<td>
				<fieldset style='width: 100%;margin: 10px'>
					<legend><fmt:message bundle='${cmc}' key='CMC.text.emsip'/></legend>
					<table cellspacing=5 cellpadding=0 style='margin-left:20px;'>
						<tr height=28><td width=85>
							<fmt:message bundle='${cmc}' key='CMC.text.ipaddress'/>:<font color=red>*</font>
						</td><td width=141>
							<span id="span1"></span>
							<%-- <input id =span1 value='<s:property value="cmcPrimaryVlan.priIpAddr" />'> --%>
						</td><td>
							<fmt:message bundle='${cmc}' key='CMC.text.ipmask'/>:<font color=red>*</font>
						</td><td width=141>
							<span id="span3"></span>
							<%-- <input id =span3 value='<s:property value="cmcPrimaryVlan.priIpMask" />'> --%>
						</td><td width=85>
							<fmt:message bundle='${cmc}' key='CMC.text.gatewayip'/>:<font color=red>*</font>
						</td><td width=141>
							<span id="span2"></span>
						</td></tr>

					</table>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td>
				<fieldset style="width: 100%; margin: 10px;">
					<legend><fmt:message bundle='${cmc}' key='CMC.title.snicfg'/></legend>
					<table cellspacing=5 cellpadding=0 style='margin-left:20px;'>
						<tr height=28><td width=100>
							<fmt:message bundle='${cmc}' key='CMC.text.snimodulationmode'/>:<font color=red>*</font>
						</td><td width=141>
							<select id="sniEthInt" disabled="disabled">
								<option value="1"><fmt:message bundle='${cmc}' key='CMC.text.mainsni'/></option>
								<option value="2"><fmt:message bundle='${cmc}' key='CMC.text.backupsni'/></option>
								<option value="3" selected="selected"><fmt:message bundle='${cmc}' key='CMC.text.mainsnipreferred'/></option>
								<option value="4"><fmt:message bundle='${cmc}' key='CMC.text.backupsnipreferred'/></option>
							</select>
						</td><td>
							<fmt:message bundle='${cmc}' key='CMC.text.mainsnisetting'/>:<font color=red>*</font>
						</td><td width=141>
							<select id="sniMainInt">
								<option value="1"><fmt:message bundle='${cmc}' key='CMC.text.copper'/></option>
								<option value="2"><fmt:message bundle='${cmc}' key='CMC.text.fiber'/></option>
								<option value="3"><fmt:message bundle='${cmc}' key='CMC.text.copperpreferred'/></option>
								<option value="4" selected="selected"><fmt:message bundle='${cmc}' key='CMC.text.fiberpreferred'/></option>
							</select>
						</td><td width=85>
							<fmt:message bundle='${cmc}' key='CMC.text.backupsnisetting'/>:<font color=red>*</font>
						</td><td width=141>
							<select id="sniBackupInt">
								<option value="1"><fmt:message bundle='${cmc}' key='CMC.text.copper'/></option>
								<option value="2"><fmt:message bundle='${cmc}' key='CMC.text.fiber'/></option>
								<option value="3"><fmt:message bundle='${cmc}' key='CMC.text.copperpreferred'/></option>
								<option value="4" selected="selected"><fmt:message bundle='${cmc}' key='CMC.text.fiberpreferred'/></option>
							</select>
						</td></tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td style="padding:10px;">
			<fieldset>
			    <legend><fmt:message bundle='${cmc}' key='CMC.title.emssnmpinfo'/></legend>
			    <table cellspacing="5" width="100%">
			        <tr>
			            <td align="center"><fmt:message bundle='${cmc}' key='CMC.text.emsip'/>:</td>
			            <td>
			                <span id="span9"></span>
			            </td>
			            <td align="center"><fmt:message bundle='${cmc}' key='CMC.text.version'/>:</td>
			            <td>
			            <select id="snmpVersion"  style="width: 180px">
			                <option value="1">V2C</option>
			            </select>
			            </td>
			        </tr>
			        <tr>
			        	<td align="center"><fmt:message bundle='${cmc}' key='CMC.text.rocommunity'/>:</td>
			            <td>
			            <input style="width: 180px" id="readCommunity" value="${snmpParam.community}">
			            </td>
			            <td align="center"><fmt:message bundle='${cmc}' key='CMC.text.rwcommunity'/>:</td>
			            <td>
			                <input type="text" style="width: 180px" id="writeCommunity" value="${snmpParam.writeCommunity}">
			            </td>
			        </tr>
			    </table>
			</fieldset>
			</td>
		</tr>
	</table>
	</div>
	</td></tr>
	<tr>
		<td>
			<div id="dhcpConfigInfo-div" style="display: none;">
				<table width=100% cellspacing=5 cellpadding=0>
					<tr style="pading-top: 15px;">
						<td>
						<fieldset style='width: 100%; height: 320px; margin: 10px'>
								<legend><fmt:message bundle='${cmc}' key='CMC.title.serviceconfig'/></legend>
						<table>
						<tr>
							<td>
								<div id="dhcpBundleListDiv"></div>
							</td>
							<td>
								<table>
									<tr>
										<td align=left><input disabled id=dhcpBundlePolicy name="dhcpBundlePolicy"" style="width:131px;border:1px solid #8bb8f3;"/></td>
									</tr>
									<tr>
										<td>
											<fieldset style='width: 44%; height: 300px; margin: 10px'>
												<legend><fmt:message bundle='${cmc}' key='CMC.text.relaylist'/></legend>
												<table>
													<tr><td><div id="dhcpGiAddrListDiv"></div></td></tr>
												</table>
											</fieldset>
											<fieldset style='width: 20%; height: 300px; margin: 10px'>
												<legend><fmt:message bundle='${cmc}' key='CMC.text.option60list'/></legend>
												<table>
													<tr><td><div id="dhcpOption60ListDiv"></div></td></tr>
												</table>
											</fieldset>
											<fieldset style='width: 24%; height: 300px; margin: 10px'>
												<legend><fmt:message bundle='${cmc}' key='CMC.text.dhcpserverlist'/></legend>
												<table>
													<tr height=28><td><div id="dhcpHelperListDiv"></div></td></tr>
												</table>
											</fieldset>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						</table>
						</fieldset>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr><td>
		<div align="right" style="padding-right: 5px; padding-top: 8px;">
			<span id="refreshErorrText" style="display:none;color:red;"><fmt:message bundle='${cmc}' key='CMC.text.DataGetErrorInfo'/></span>
			<button id="refreshBt" class=BUTTON95
				onMouseOver="this.className='BUTTON_OVER95'"
				onMouseOut="this.className='BUTTON95'"
				onMouseDown="this.className='BUTTON_PRESSED95'"
				onclick="refreshClick()"><fmt:message bundle='${cmc}' key='CMC.label.GetInfoFromEntity'/></button>
			&nbsp;&nbsp;
			<button id="saveBt" class=BUTTON95
				onMouseOver="this.className='BUTTON_OVER95'"
				onMouseOut="this.className='BUTTON95'"
				onMouseDown="this.className='BUTTON_PRESSED95'"
				onclick="saveClick()"><fmt:message bundle='${cmc}' key='CMC.label.savesetting'/></button>
			&nbsp;&nbsp;
			<button id="resetBt" class=BUTTON95
				onMouseOver="this.className='BUTTON_OVER95'"
				onMouseOut="this.className='BUTTON95'"
				onMouseDown="this.className='BUTTON_PRESSED95'"
				onclick="resetClick()"><fmt:message bundle='${cmc}' key='CMC.label.reset'/></button>
			&nbsp;&nbsp;
		</div>
	</td></tr>
	</table>
</body>
<style>
input {
	border: 1px solid #8bb8f3;
}
</style>
</html>