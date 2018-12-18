<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	module resources
	CSS css/white/disabledStyle
	IMPORT js/jquery/Nm3kTabBtn
	IMPORT js/dhtmlx/dhtmlxcommon
	IMPORT js/dhtmlx/tree/dhtmlxtree
	IMPORT system/licenseUtils
	IMPORT system/licenseApply
</Zeta:Loader>
<style type="text/css">
	.eastBg .x-panel-body{ background:#fbfbfb;}
	.oneTopLine .x-panel-header{ border-top:1px solid #ccc;}
	.eastBg .x-panel-header{ color:#0165B0; padding:0px 0px 6px 0px; border-color:#D7D7D7; background:#f0f0f0 url("../css/white/pannelTitle.png") repeat-x;}
	.eastBg .x-panel-header-text{ font-size:12px; padding-top:10px;}
	.x-form-field-wrap .x-form-trigger {height:23px;}
</style>
<script type="text/javascript">
var licenseValid = ${licenseValid};

//如果有合法license，并且是单独打开页面，则需要跳转至登录页面
if(window.top == window && licenseValid){
    window.location.href = '/showlogon.tv';
}
</script>
</head>
<body class="whiteToBlack">
<div class=formtip id=tips style="display: none"></div>
    <div id="putMainPart">
		<div class="edge10">
			<p class="flagP clearBoth"><span class="flagInfo">@SYSTEM.baseInfo@</span></p>
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			 <thead>
				 <tr>
					 <th colspan="4"></th>
				 </tr>
			 </thead>
			 <tbody>
				 <tr>
					 <td class="rightBlueTxt">@license.type@</td>
					 <td colspan="3" id="licenseType"></td>
				</tr>
				<tr>
					<td class="rightBlueTxt" width="130">@license.contact@</td>
					 <td width="250">
						<input id="man" type="text" maxlength="32" class="normalInput w240"
						tooltip='@license.contactNotNull@' />
					</td>
				    <td class="rightBlueTxt" width="120">@license.phone@</td>
					<td>
						<input id="phone" maxlength="32" type="text" class="normalInput w240" 
						tooltip='@license.phoneNoError@'/>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@license.mobilePhone@</td>
					<td>
						<input id="mobilePhone" maxlength="32" type="text" class="normalInput w240" 
						tooltip='@license.mobileNotError@' />
					</td> 
					<td class="rightBlueTxt">@license.email@</td>
					<td>
						<input id="email" type="text" maxlength="64" class="normalInput w240" 
						tooltip='@license.emailError@'/>
					</td> 
				</tr>
				<tr>
					<td class="rightBlueTxt">@license.version@</td>
					<td>
						<input id="emsVersion" type="text" disabled class="normalInput w240"/>
					</td>
					<td class="rightBlueTxt">@license.licenseDays@</td>
					<td>
						<input id="numberOfDays" type="text" class="normalInput" maxlength="6" style="width:217px;" 
						tooltip='@license.licenseOfDay@'/>
					</td> 
				</tr>
				<tr>
					<td class="rightBlueTxt">@license.userNum@</td>
					<td>
						<input id="numberOfUsers" type="text" class="normalInput" maxlength="6" style="width:217px;" 
						tooltip='@license.userNumberError@'/>
					</td>
					<td class="rightBlueTxt">@license.collectorNum@</td>
					 <td>
					 	<input id="numberOfEngines" type="text" class="normalInput" maxlength="6" style="width:217px;" 
					 	tooltip='@license.engineNumberError@'/>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@license.company@</td>
					 <td colspan="3">
					 	<input id="company" type="text" class="normalInput" maxlength="64" style="width:618px;" 
					 	tooltip='@license.companyIsEmpty@' />
					</td>
				</tr>
			  </tbody>
			</table>
			
			<div class="mT10">
				<p class="flagP clearBoth"><span class="flagInfo">@license.moduleInfo@</span></p>
				<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				 <thead>
					 <tr>
						 <th colspan="5" class="txtLeftTh">@license.checkModule@</th>
					 </tr>
				 </thead>
				 <tbody>
					<tr>
						<td class="rightBlueTxt">
							<input type="checkbox" id='mobile' checked="checked" />
						</td>
						<td colspan="4"><label class="blueTxt">@license.mobileNM@</label></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">
							<input type="checkbox" id='pnmp' checked="checked" />
						</td>
						<td colspan="4"><label class="blueTxt">PNMP</label></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">
							<input type="checkbox" id="report" checked="checked" />
						</td>
						<td colspan="4"><label class="blueTxt">@license.report@</label></td>
					</tr>
					<tr>
					 	<td class="rightBlueTxt" width="50">
					 		<input id="olt" name="olt" type="checkbox" checked="checked" />
					 	</td>
					 	<td width="335"><label for="olt" class="blueTxt">OLT</label></td>
						<td width="120" class="rightBlueTxt">@license.oltNum@</td>
						<td>
							<input id="oltNum" type="text" maxlength="6" class="normalInput w80" 
							tooltip='@license.oltNumberError@'/>
						</td>
						<td>
							<span class="blueTxt"></span>
						</td>
					</tr>
					<tr>
					 	<td class="rightBlueTxt" width="50">
					 		<input id="onu" name="onu" type="checkbox" checked="checked" />
					 	</td>
					 	<td width="335"><label for="onu" class="blueTxt">ONU</label></td>
						<td width="120" class="rightBlueTxt">@license.onuNum@</td>
						<td width="110">
							<input id="onuNum" type="text" maxlength="6" class="normalInput w80" 
							tooltip='@license.onuNumberError@'/>
						</td>
						<td>
							<span class="blueTxt"></span>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">
							<input id="cmc" name="cmc" type="checkbox" checked="checked" />
						</td>
						<td><label for="cmc" class="blueTxt">@license.topvisionCMTS@</label></td>
						<td class="rightBlueTxt">@license.topvisionCMTSNum@</td>
						<td>
						 	<input id="cmcNum" type="text" maxlength="6" class="normalInput w80" 
						 	tooltip='@license.topvisionCmtsNumberError@'/>
						</td> 
						<td>
							<span class="blueTxt"></span>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">
							<input id="cmts" name="cmts" type="checkbox" checked="checked" />
						</td>
						<td><label for="cmts" class="blueTxt">@license.otherCMTS@</label></td>
						<td class="rightBlueTxt">@license.otherCMTSNum@</td>
						<td>
						 	<input id="cmtsNum" type="text" maxlength="6" class="normalInput w80"
						 	tooltip='@license.otherCmtsNumberError@'/>
						</td> 
						<td>
							<span class="blueTxt"></span>
						</td>
					</tr>
				</tbody>
				</table>
			</div>
		</div>
		<div class="noWidthCenterOuter clearBoth">
	     	<ol class="upChannelListOl pB0 pT10 noWidthCenter">
	     		 <li><a href="javascript:;" class="normalBtnBig" onclick="backFn()"><span><i class="miniIcoArrLeft"></i>@SYSTEM.Back@</span></a></li>
	     		 <li><a href="javascript:;" class="normalBtnBig" onclick="refresh()"><span><i class="miniIcoRefresh"></i>@SYSTEM.Reset@</span></a></li>
	        	 <li><a href="javascript:;" class="normalBtnBig" onclick="saveFn()"><span><i class="miniIcoSaveOK"></i>@COMMON.save@</span></a></li>
	    	 </ol>
		</div>
	</div>
	<div id="putSide">
		<div id="putTree"></div>
	</div>
	<div id="putReport">
		<div id="putTree2"></div>
	</div>
	<iframe id="if_resp" name="ifResp" width="0" height="0" style="opacity:0.1"></iframe>
</body>
</Zeta:HTML>