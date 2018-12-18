<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module OnuAuth
    plugin Nm3kDropdownTree
    IMPORT js/nm3k/Nm3kSwitch
    IMPORT gpon/onuauth/GponOnuAuth
    IMPORT gpon/javascript/PonSelector
    IMPORT gpon/javascript/GponAuthAdd
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
#addDiv{ position:absolute; top:100px; left:100px; z-index:2; width:600px;}
#addDivBg{ width:100%; height:100%; overflow:hidden; position:absolute; top:0; left:0;opacity: 0.8; filter: alpha(opacity=85);background: #F7F7F7; z-index:1;}
.x-form-field-trigger-wrap{ width:auto !important;}
.x-form-field-wrap .x-form-trigger{ height:23px;}
#lineProfileId, #srvProfileId{ width:129px !important;}
#gponOnuNoSel input{ height:20px !important;}
</style>
<script type="text/javascript">
var inited = false;
</script>
<body class="openWinBody"  style="overflow:hidden;">
	<div id=bodyFieldset style="padding:10px;">
	<!-- 页面布局开始 -->
    <table width=100%>
        <tr><td >
            <table width="100%">
                <tr height=30px><td>
                
	                <table width="100%">
	                    <tr>
	                    	<td width=50px class="blueTxt txtRight">@EPON/onuAuth.ponSlot@@COMMON.maohao@</td>
	                    	<td width=90px align=left>
	                    	     <select id="slotSel" class="w70 normalSel" onChange="syncSlot()">
	                        	</select>
	                    	</td>
	                    	<td width=70px class="blueTxt txtRight">@COMMON.port@@COMMON.maohao@</td>
	                    	<td width=90px align=left>
	                        	<select id="ponSel" class="w70 normalSel" onChange="syncPon()">
	                        	</select>
	                    	</td>
	                    	<td width=110px class="txtRight blueTxt"><span id="modeText">@OnuAuth.authMode@@COMMON.maohao@</span></td>
	                    	<td width=150px align=left>
		                        <select id="modeSel" class="w190 normalSel" onChange="modeChange()">
		                        	<option value="1">@OnuAuth.snAuth@</option>
		                            <option value="2">@OnuAuth.snPassAuth@</option>
		                            <option value="3">@OnuAuth.loidAuth@</option>
		                            <option value="4">@OnuAuth.loidPassAuth@</option>
		                            <option value="5">@OnuAuth.passAuth@</option>
		                            <option value="6">@OnuAuth.autoAuth@</option>
		                            <option value="7">@OnuAuth.mixAuth@</option>
		                        </select>
		                    </td>
		                    <td class="w80 txtRight blueTxt">@OnuAuth.openAutoFind@@COMMON.maohao@</td>
		                    <td>
		                    	<div id="useAutoFind"></div>
		                    </td>
		                </tr>
	                </table>
                </td></tr>
                <tr height=1px><td>
                    <hr size=1 style="filter:alpha(opacity=100,opacity=5,style=1);width:100%;color:#1973b4;" />
                </td></tr>
                <tr height=30px><td><table>
                    <tr><td><a href="javascript:;" class="normalBtn" onclick="toggleAutoFind()"><span><i class="miniIcoSearch"></i>@OnuAuth.autoFindList@</span></a> </td></tr>
                </table></td></tr>
            </table>
        </td></tr>
        <tr><td>
            <table>
                <tr>
                	<td colspan=2>
                    <div id=autoFind style="display:none;">
 	                   <div id="autoFindOnuDiv"></div>
                    </div>
                	</td>
                </tr>
                <tr><td>
                    <div id=authDiv >
                        <table><tr><td>
                            <div >
                                <div id="authOnuDiv" style="width: 800px;"></div>
                            </div>
                        </td></tr></table>
                    </div>
                </td></tr>
            </table>
        </td></tr>
    </table>
    
    <div id="addDiv" class="displayNone">
		<div class="zebraTableCaption">
   			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@OnuAuth.addAuth@</label></span></div>
		     <table class="zebraTableRows pL10 pR10" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		        <tr>
					<td class="rightBlueTxt">@OnuAuth.authMode@@COMMON.maohao@</td>
					<td><div id="authMode"></div></td>
					<td class="rightBlueTxt w100" >@COMMON.required@PON@COMMON.port@@COMMON.maohao@</td>
					<td class="w180"><div style="float:left" id="gponIndex"></div><div style="float:left; margin-left:2px; width:60px;" id="gponOnuNoSel"></div></td>
				</tr>
		        <tr class="darkZebraTr">
					<td class="rightBlueTxt"><label class="authClazz  sn snpassword">@COMMON.required@</label></>SN@COMMON.maohao@</td>
					<td><input type="text" id="sn" class="normalInput w150 authClazz  sn snpassword mix" tooltip="@OnuAuth.sn.tip@"/></td>
					<td class="rightBlueTxt"><label class="authClazz snpassword password">@COMMON.required@</label>@COMMON.password@@COMMON.maohao@</td>
					<td><input type="text" id="password" class="normalInput w150 authClazz snpassword password mix" maxlength="10"  tooltip="@OnuAuth.password.tip@"/></td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label class="authClazz loid loidpassword">@COMMON.required@</label>LOID@COMMON.maohao@</td>
					<td><input type="text" id="loid" class="normalInput w150 authClazz loid loidpassword mix"  maxlength="24" tooltip="@OnuAuth.loid.tip@"/></td>
					<td class="rightBlueTxt" ><label class="authClazz loidpassword">@COMMON.required@</label>LOID@COMMON.password@@COMMON.maohao@</td>
					<td><input type="text" id="loidPassword" class="normalInput w150 authClazz loidpassword mix"  maxlength="12" tooltip="@OnuAuth.loidPass.tip@"/></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@COMMON.required@@OnuAuth.lineProfile@@COMMON.maohao@</td>
					<td>
						<div style="width:154px;">
							<input type="text" id="lineProfileId" readonly="readonly" class="normalInput w150"  maxlength="4" />
						</div>
					</td>
					<td class="rightBlueTxt">@COMMON.required@@OnuAuth.srvProfile@@COMMON.maohao@</td>
					<td>
						<div style="width:154px;">
							<input type="text" id="srvProfileId" readonly="readonly" class="normalInput w150"  maxlength="4" />
						</div>
					</td>
				</tr>
				</tbody>
			</table>
			<input id="entityId" value="${entityId}" style="display: none;"/>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
			         <li><a href="javascript:;" class="normalBtnBig" onclick="addGponOnuAuth()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
			         <li><a href="javascript:;" class="normalBtnBig" onclick="closeAddLayer()"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
	</div>
    <div id="addDivBg" class="displayNone"></div>
    <div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
	     	 <Zeta:Button id="addAuthBt" onclick="openAddDivFn()" icon="miniIcoAdd">@OnuAuth.addAuth@</Zeta:Button>
	     	 <li><a id="gponAutoAuthConfig" href="javascript:;" onclick="showGponAutoAuth()" class="normalBtnBig"><span><i class="bmenu_eyesClose"></i>@GPON/onuauth.autoAuthConfig@</span></a></li>
	         <li><a id=refreshAuthBt  onclick="refreshGponOnuAuth()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	         <li><a id=saveBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
	     </ol>
	</div>
</div>
<!-- 页面布局结束 -->
</body>
</Zeta:HTML>