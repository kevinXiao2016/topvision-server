<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module network
    import network.topoFolderProperty
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../fault/TipTextArea.js"></script>
<script type="text/javascript">
var folderId = ${topoFolder.folderId};
var nodeId = ${nodeId};
var superiorId = ${topoFolder.superiorId};
var folderName = '${topoFolder.name}';
var folderUrl = '${topoFolder.url}';
var folderBgPosition = ${topoFolder.backgroundPosition};
var backgroundImg = '${topoFolder.backgroundImg}';
var backgroundColor = '${topoFolder.backgroundColor}';
var linkColor = '${topoFolder.linkColor}';
var linkWidth = '${topoFolder.linkWidth}';
var iconSize = ${topoFolder.iconSize};
var subnetIp = '${topoFolder.subnetIp}';
var subnetMask = '${topoFolder.subnetMask}';
var folderBounds = '${topoFolder.width}x${topoFolder.height}';

<s:if test="topoFolder.folderId>4&&topoFolder.type==5">
var subnetFlag = true;
</s:if><s:else>
var subnetFlag = false;
</s:else>

var second = '@label.seconds@';
var minute = '@label.minutes@';
<%boolean topoEditPower = uc.hasPower("topoEdit");%>


$(function(){
	var t1 = new TipTextArea({
		id: "url"
	});
	t1.init();
	var t2 = new TipTextArea({
		id: "inputNote"
	});
	t2.init();
})
</script>
</head>
<body class="sideMapBg" onload="doOnload()">
	<form id="folderForm" name="folderForm" onsubmit="return false;">
		<input type=hidden name="topoFolder.folderId" value="${topoFolder.folderId}" /> 
		<input type=hidden id="subnetIp" name="topoFolder.subnetIp"	value="${topoFolder.subnetIp}" /> 
		<input type=hidden id="subnetMask" name="topoFolder.subnetMask" value="${topoFolder.subnetMask}" />
		<div class="edge5">
			 <table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			     <thead>
			         <tr>
			             <th colspan="2" class="txtLeftTh">@NETWORK.nodeProperty@</th>
			         </tr>
			     </thead>
			     <tbody>
			     	<tr>
						<td width="100" class="rightBlueTxt">@label.type@</td>
						<td>
							<s:if test="topoFolder.type==6">@cloudy@</s:if> 
							<s:elseif test="topoFolder.type==20">@folder.type20@</s:elseif> 
							<s:else>@NETWORK.topoFolder@</s:else>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@label.name@<font color=red>*</font></td>
						<td><input id="name" class="normalInput"
							<s:if test="topoFolder.folderId<5">readonly</s:if>
							name="topoFolder.name" style="width: 200px;" type=text
							maxlength=24 value="${topoFolder.name}" />
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@NETWORK.href@:</td>
						<td style="padding:5px 0px">
							<textarea id="url" maxLength="64" class="normalInput"  toolTip='@topoLabel.http@<br />@topoLabel.noteTip@' rows=1 name="topoFolder.url" style="width: 200px; height:40px;">${topoFolder.url}</textarea>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@label.note@</td>
						<td style="padding:5px 0px">
							<textarea id="inputNote" maxLength="128" class="normalInput" toolTip='@topoLabel.noteTip128@' rows=2 name="topoFolder.note" style="width: 200px; height:40px;">${topoFolder.note}</textarea>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@label.topoSize@:</td>
						<td>
							<select id="folderSize" name="folderSize">
									<option value="2000x2000">2000 x 2000</option>
									<option value="3000x3000">3000 x 3000</option>
									<option value="4000x4000">4000 x 4000</option>
									<option value="5000x5000">5000 x 5000</option>
							</select>
						</td>
					</tr>
						<%
						    if (topoEditPower) {
						%>
						<tr>
							<td></td>
							<td>
								<a href="javascript:;" class="normalBtn" onclick="onSaveClick()"><span><i class="miniIcoEdit"></i>@RESOURCES/COMMON.modify@</span></a> 
							</td>
						</tr>
						<%
						    }
						%>
						<s:if test="topoFolder.superiorId > 1 && isRegion == null">
							<s:if test="mapNode.icon!=null">
								<tr>
									<td class="rightBlueTxt">@NETWORK.icon@:</td>
									<td><img id="icon"
										src="${mapNode.icon}" border=0
										align=absmiddle /></td>
								</tr>
								<%
								    if (topoEditPower) {
								%>
								<tr>
									<td></td>
									<td>
										<a href="javascript:;" onclick="popIconFile()" class="normalBtn"><span>@RESOURCES/COMMON.select@...</span></a> 
									</td>
								</tr>
								<%
								    }
								%>
							</s:if>
						</s:if>
	
						<s:if test="topoFolder.superiorId>1 && isRegion == null">
							<tr>
								<td class="rightBlueTxt"><input id="fixed" type=checkbox
									<s:if test="mapNode.fixed">checked</s:if>
									onclick="fixNodeLocation(this);" />
								</td>
								<td><label for="fixed">@NETWORK.fixNodeLocation@</label></td>
							</tr>
						</s:if>
			     </tbody>
		     </table>
		     <div class="pT10">
		     	<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
					    <tr>
					        <th colspan="3" class="txtLeftTh">@label.showProperty@</th>
					    </tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="3">
								<span class="pL20">
									<input id="displayEntityLabel" type=checkbox
									<s:if test="topoFolder.displayEntityLabel">checked</s:if>
									onclick="onDisplayEntityLabelClick(this)" />
								</span>
								<label for="displayEntityLabel">@label.showDeviceMark@</label>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<span class="pL20">
									<input id="displayAlertIcon" type=checkbox
								<s:if test="topoFolder.displayAlertIcon">checked</s:if>
								onclick="onDisplayAlertIconClick(this)" />
								</span>
								<label for="displayAlertIcon">@label.showNodeWarnIcon@</label>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<span class="pL20">
									<input id="displayCluetip" type=checkbox
								<s:if test="topoFolder.displayCluetip">checked</s:if>
								onclick="onDisplayCluetipClick(this)" />
								</span>
								<label for="displayCluetip">@label.showFloatWindow@</label>
							</td>
						</tr>
						<tr>
							<td class="rightBlueTxt">
								@label.showNameMode@:
							</td>
							<td colspan="2">
								<input type=radio id="displayLabelName" name="displayLabel"
									<s:if test="topoFolder.displayName">checked</s:if>
									onclick="setDisplayNameLabel(2)" /><label for="displayLabelName">@NETWORK.alias@</label>
						
								<input type=radio id="displayLabelSysName" name="displayLabel"
									<s:if test="topoFolder.displaySysName">checked</s:if>
									onclick="setDisplayNameLabel(1)" /><label for="displayLabelSysName">@NETWORK.host@</label>
							
								<input type=radio id="displayLabelIp" name="displayLabel"
									<s:if test="!topoFolder.displayName && !topoFolder.displaySysName">checked</s:if>
									onclick="setDisplayNameLabel(0)" /><label for="displayLabelIp">@RESOURCES/COMMON.ip@</label>
							</td>
						</tr>
					</tbody>
					<thead>
						<tr>
						    <th colspan="3" class="txtLeftTh">@label.showBgMode@</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="rightBlueTxt">
								<input id="bfid" type=radio <s:if test="!topoFolder.displayGrid && !topoFolder.backgroundFlag">checked</s:if>
								onclick="setBackgroundType('bfid')" />
								<label for="bfid">@label.pureColor@</label>	
							</td>
							<td>
										<div id="bgArea" class="color-area"
									style="width:75px;background:${topoFolder.backgroundColor}">&nbsp;</div>
									
							</td>
                            <td>         
								<%
								    if (topoEditPower) {
								%>
									<a  id="colorBt" onclick="popSelectBackgroundColor()" href="javascript:;" class="normalBtn"><span>@RESOURCES/COMMON.select@...</span></a> 
								<%
								}
								%>
                            </td>
						</tr>
						<tr>
							<td class="rightBlueTxt"><input id="displayGrid" type=radio
									<s:if test="topoFolder.displayGrid">checked</s:if>
									onclick="setBackgroundType('displayGrid')" />
									<label for="displayGrid">@label.gridColor@</label>
							</td>
							<td colspan=2>
									
							</td>
						</tr>
						<tr>
							<td  class="rightBlueTxt">
								<input id="backgroundFlag" type=radio
									<s:if test="!topoFolder.displayGrid && topoFolder.backgroundFlag">checked</s:if>
									onclick="setBackgroundType('backgroundFlag')" />
								<label for="backgroundFlag">@label.imgColor@</label>
							</td>	
							<td colspan="2">
								<textarea id="backgroundImg" class="normalInput" rows=1 disabled="disabled" 
									style="width: 200px; ">${topoFolder.backgroundImg}
								</textarea>
							</td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@label.bgPosition@</td>
							<td>
								<select id="backgroundPosition" style="float:left;width: 75px"
									<s:if test="!topoFolder.backgroundFlag">disabled</s:if>
									onchange="onPositionChanged(this);">
										<option value="0">@GRAPH.defaultArrange@</option>
										<option value="1">@GRAPH.middle@</option>
										<option value="2">@GRAPH.fit@</option>
								</select>
							</td>
							<td>
							<%
							    if (topoEditPower) {
							%>
								<a id="imgBt" onclick="popSelectFile()" href="javascript:;" class="normalBtn" <s:if test="!topoFolder.backgroundFlag">disabled</s:if> ><span>@RESOURCES/COMMON.select@...</span></a> 
							<%
							    }
							%>
							</td>
						</tr>
					</tbody>
				</table>						     	
		     </div>
		     <table class="dataTableRows mT10" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			     <thead>
			         <tr>
			             <th colspan="3" class="txtLeftTh">@label.connProperty@</th>
			         </tr>
			     </thead>
			     <tbody>
			     	<tr>
						<td colspan="3">
							<span class="pL20">
								<input id="displayLink" type=checkbox
								<s:if test="topoFolder.displayLink">checked</s:if>
								onclick="onDisplayLinkClick(this)" />
							</span>
							<label
							for="displayLink">@label.showConn@</label>
						</td>
					</tr>
					<%--<tr>
						<td colspan="3">
							<span class="pL20">
								<input id="displayLinkLabel" type=checkbox
								<s:if test="topoFolder.displayLinkLabel">checked</s:if>
								onclick="onDisplayLinkLabelClick(this)" />
							</span>
							<label	for="displayLinkLabel">@label.showConnMark@</label>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<span class="pL20">
								<input id="linkShadow" type=checkbox
								onclick="onLinkShadowChanged(this)"
								<s:if test="topoFolder.linkShadow">checked</s:if> />
							</span>
							<label for="linkShadow">@label.showConnShadow@</label>
						</td>
					</tr> --%>
					<tr>
						<td class="rightBlueTxt">@label.lineWidth@:</td>
						<td colspan="2">
							<select id="linkWidthBox" style="width: 75px"
								onchange="onLinkWidthChanged(this);">
									<option value="0.75">1px</option>
									<option value="1.0">2px</option>
									<option value="1.2">3px</option>
									<option value="2.0">4px</option>
									<option value="3.0">5px</option>
									<option value="4.0">6px</option>
									<option value="5.0">7px</option>
									<option value="6.0">8px</option>
									<option value="7.0">9px</option>
									<option value="8.0">10px</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@GRAPH.strokeColor@:</td>
						<td>
							<div id="linkArea" class="color-area"
								style="float:left;width:75px;background:${topoFolder.linkColor}"></div>
						</td>
						<td>
							<%
							    if (topoEditPower) {
							%>
							<div >
								<a href="javascript:;" class="normalBtn" onclick="popSelectLinkColor()"><span>@RESOURCES/COMMON.select@...</span></a> 
							</div>
							<%
								}
							%>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
</body>
</Zeta:HTML>
