<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module univlanprofile
    css css/white/disabledStyle
    import epon.uniportvlan.uniPortVlan
</Zeta:Loader>
<style type="text/css">
body,html{ height:100%;}
.longTxt{ width:182px;}
.openLayerOuter{width: 100%; height: 100%;  overflow: hidden;   position: absolute;   top: 0;   left: 0; display:none;}
.openLayerBg{width: 100%; height: 100%; overflow: hidden; background: #F7F7F7; position: absolute; top: 0; left: 0; opacity: 0.8; filter: alpha(opacity=85);}
.openLayerMainDiv { width: 560px; height: 280px;  overflow: hidden; position: absolute;  top: 100px;  left: 120px;  z-index: 2;  background: #F7F7F7;}

</style>
<script type=""></script>
<script type="text/javascript">
var entityId = '${entityId}';
var uniId = '${uniId}';
var uniIndex = '${uniPortVlan.portIndex}';
var vlanMode = '${uniPortVlan.vlanMode}';
var uniPvid = '${uniPortVlan.vlanPVid}';
var profileId = '${uniBindInfo.bindProfileId}';
var bindProfAttr = '${uniBindInfo.bindProfAttr}';
var profileName = '${uniVlanProfile.profileName}';
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;

$(function(){
	//标记和透传使用同一个页面
	if(vlanMode == 1){
		//标记
		$('#modeValue').text("@PROFILE.modeTag@");
	}else{
		//透传
		$('#modeValue').text("@PROFILE.modeTransparent@");
	}
	//显示关联的模板名称
	if(profileId > 0){
		$("#vlanProfile").text(profileName);
	}else{
		$("#vlanProfile").text("@UNIVLAN.unRelated@");
	}
	
	var $pvid = $("#pvid");	
	var $savePvid = $("#savePivd");
	//点击pvid后面的保存按钮;
	$savePvid.click(function(){
		//先执行验证
		if(!checkInput($pvid.val(), 1, 4094)){
			$pvid.focus();
			return;
		}
		//修改PVID
		modifyUniPvid($pvid.val());
	});
	authLoad();
});//end document.ready;

//关闭切换模式页面;
function closeOpenLayer(){
	$("#openLayerOuter").stop().fadeOut();
}

//显示切换模式页面;
function showChangeMode(){
	$("#pvid2").val($("#pvid").val());
	$("#modeUpdate").val(vlanMode);
	$("#openLayerOuter").stop().fadeIn();
}

function authLoad(){
	if(!refreshDevicePower){
		$("#fetchData").attr("disabled",true);
	}
	if(!operationDevicePower){
		$("#pvid").attr("disabled",true);
		$("#savePivd").attr("disabled",true);
		$("#showChangeMode").attr("disabled",true);
		$("#showVlanProfile").attr("disabled",true);
	}
}

</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">@UNIVLAN.uniVlanInfo@</div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt">@epon/VLAN.portNum@:</td>
	                 <td>
						${uniPortVlan.portString}
	                 </td>
	                 <td></td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td width="240" class="rightBlueTxt">PVID:</td>
	                 <td width="180">
						<input type="text" id="pvid" class="normalInput w180" value="${uniPortVlan.vlanPVid}" maxlength="4" tooltip="@RULE.vlanInputTip@" />
	                 </td>
	                 <td>
	                 	<a id="savePivd" href="javascript:;" class="normalBtn"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a>
	                 </td>
	             </tr>
	             <tr>
	             	<td class="rightBlueTxt">@PROFILE.vlanMode@:</td>
	             	<td id="modeValue">
	             		
	             	</td>
	             	<td>
	             		<a id="showChangeMode" href="javascript:;" class="normalBtn" onclick="showChangeMode()"><span><i class="miniIcoTwoArrOut"></i>@UNIVLAN.changeMode@</span></a>
	             	</td>
	             </tr>
	              <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">@UNIVLAN.profile@:</td>
	                 <td>
	                 	<span class="longTxt wordBreak" id="vlanProfile"> </span>
	                 </td>
	                 <td>
	                 	<a id="showVlanProfile" href="javascript:;" class="normalBtn" onclick="showVlanProfile()"><span><i class="miniIcoTwoArr"></i>@UNIVLAN.viewProfile@</span></a>
	                 </td>
	              </tr>
	         </tbody>
	     </table>
		 <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a id="fetchData" href="javascript:;" class="normalBtnBig" onclick="refreshUniVlanData()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
	
	<div class="openLayerOuter" id="openLayerOuter">
		<div class="openLayerMainDiv">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@UNIVLAN.changeMode@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				             <tr>
				             	<td class="rightBlueTxt" width="160">
				                	@epon/VLAN.portNum@:
								</td>
								<td>
									<span>${uniPortVlan.portString}</span>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">
				                	PVID:
								</td>
								<td>
									<input type="text" id="pvid2" class="normalInput w180" value="" maxlength="4" tooltip="@RULE.vlanInputTip@" />
								</td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">
				                	@PROFILE.vlanMode@:
								</td>
								<td>
									<select id="modeUpdate" class="normalSel w180">
										<option value="0">@PROFILE.modeTransparent@</option>
										<option value="1">@PROFILE.modeTag@</option>
										<option value="2">@PROFILE.modeTranslate@</option>
										<option value="3">@PROFILE.modeAgg@</option>
										<option value="4">Trunk</option>
									</select>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="modifyVlanMode()"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeOpenLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerBg"></div>
	</div>
</body>
</Zeta:HTML>