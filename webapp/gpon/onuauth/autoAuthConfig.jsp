<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	plugin Nm3kDropdownTree
	module OnuAuth
	IMPORT gpon/onuauth/GponAutoAuthConfig
	import gpon.javascript.PonSelector
	css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
#addDiv, #updateDiv{ position:absolute; top:100px; left:50px; z-index:2; width:600px;}
#addDivBg, #updateDivBg{ width:100%; height:100%; overflow:hidden; position:absolute; top:0; left:0;opacity: 0.8; filter: alpha(opacity=85);background: #F7F7F7; z-index:1;}
.x-form-field-trigger-wrap{ width:auto !important;}
.x-form-field-wrap .x-form-trigger{ height:23px;}
#lineProfileId, #srvProfileId, #updateSrvProfileId, #updateLineProfileId{ width:129px !important;}
</style>
<body class="openWinBody">
	<div class="edge10">
		<div id="autoAuthContainer" class="normalTable"></div>
		<ol class="upChannelListOl pB0 pT10 noWidthCenter">
			<li><a href="javascript:;" class="normalBtnBig" onclick="addAutoAuthConfig()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
			<li><a ref="javascript:;" class="normalBtnBig" onclick="refreshGponOnuAuth()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
			<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
		</ol>
	</div>
	
	<div id="addDiv" class="displayNone">
		<div class="zebraTableCaption">
   			<div class="zebraTableCaptionTitle addAutoAuthTem">
   				<span class="blueTxt">
   					<label class="blueTxt">@OnuAuth.addAutoAuthTem@</label>
   				</span>
   			</div>
   			<div class="zebraTableCaptionTitle updateAutoAuthTem">
   				<span class="blueTxt">
   					<label class="blueTxt">@OnuAuth.updateAutoAuthTem@</label>
   				</span>
   			</div>
		     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		         <tbody>
		         <tr>
					<td class="rightBlueTxt w120">@OnuAuth.onuType@@COMMON.maohao@</td>
					<td colspan="1"><select id="onuType" class="normalSel w150">
						<option value="">@COMMON.select@</option>
						<option value="PN8625">PN8625</option>
						<option value="PN8626">PN8626</option>
					</select></td>
					<td class="rightBlueTxt w120">PON@COMMON.port@@COMMON.maohao@</td>
					<td><div id="portTree" class="w150" ></div></td>
				</tr>
		        <tr class="darkZebraTr">
					<td class="rightBlueTxt w120">@OnuAuth.ethNum@@COMMON.maohao@</td>
					<td><input type="text" id="ethNum" class="normalInput w150"  maxlength="2" tooltip="@OnuAuth.portNum.tip@"/></td>
					<td class="rightBlueTxt w120">@OnuAuth.wlanNum@@COMMON.maohao@</td>
					<td><input type="text" id="wlanNum" class="normalInput w150"  maxlength="1" tooltip="@OnuAuth.wlanNum.tip@"/></td>
				</tr>
		        <tr>
					<td class="rightBlueTxt w120">@OnuAuth.catvNum@@COMMON.maohao@</td>
					<td><input type="text" id="catvNum" class="normalInput w150"  maxlength="1" tooltip="@OnuAuth.catvNum.tip@"/></td>
					<td class="rightBlueTxt w120">@OnuAuth.veipNum@@COMMON.maohao@</td>
					<td><input type="text" id="veipNum" class="normalInput w150"  maxlength="1" tooltip="@OnuAuth.veipNum.tip@"/></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt w120">@COMMON.required@@OnuAuth.lineProfile@@COMMON.maohao@</td>
					<td>
                        <div style="width:154px;">
							<input type="text" id="lineProfileId" readonly="readonly" class="normalInput w150"  maxlength="4"/>
						</div>
					</td>
					<td class="rightBlueTxt w120">@COMMON.required@@OnuAuth.srvProfile@@COMMON.maohao@</td>
					<td>
						<div style="width:154px;">
							<input type="text" id="srvProfileId" readonly="readonly" class="normalInput w150"  maxlength="4"/>
						</div>
					</td>
				</tr>  
				</tbody>
			</table>
			<input id="entityId" value="${entityId}" style="display: none;"/>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
			         <li class="addAutoAuthTem"><a href="javascript:;" class="normalBtnBig" onclick="addGponOnuAutoAuth()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
			         <li class="updateAutoAuthTem"><a href="javascript:;" class="normalBtnBig" onclick="updateGponOnuAutoAuth()"><span><i class="miniIcoAdd"></i>@BUTTON.update@</span></a></li>
			         <li><a href="javascript:;" class="normalBtnBig" onclick="closeAddLayer()"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
	</div>
	<div id="addDivBg" class="displayNone"></div>
	
</body>
</Zeta:HTML>