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
</Zeta:Loader>
<style type="text/css">
body,html{height:100%; overflow:hidden;}
.openLayerProfile{ width:100%; height:100%; overflow:hidden; position:absolute; top:0;left:0;  display:none;}
.openLayerProfileMain{ width:400px; height:240px; overflow:hidden; position:absolute; top:30px; left:100px; z-index:2;  background:#F7F7F7;}
.openLayerProfileBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var onuId = '${onuId}';
	var uniId = '${uniId}';
	var uniIndex = '${uniBindInfo.uniIndex}';
	var profileMode = '${uniVlanProfile.profileMode}';
	var profileId = '${uniBindInfo.bindProfileId}'
	var profileRefCnt = '${uniVlanProfile.profileRefCnt}';
	var bindProfAttr = '${uniBindInfo.bindProfAttr}';
	
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	
	function cancelClick(){
		window.top.closeWindow("uniVlanBindInfo");
	}
	//输入校验: 输入pvid否在范围内
	function checkInput(value){
		var reg = /^[1-9]\d*$/;	
		if (reg.exec(value) && parseInt(value) <= 4094 && parseInt(value) >= 1) {
			return true;
		} else {
			return false;
		}
	}
	//修改uni pvid
	function saveClick(){
		var uniPvid = $("#pvid").val();
		if(!checkInput(uniPvid)){
			$("#pvid").focus();
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/univlanprofile/modifyUniPvid.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId,
				uniPvid : uniPvid
			},
			success : function() {
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@PROFILE.updateSuccess@</b>'
	       	    });
				cancelClick();
				window.top.getFrame("entity-${onuId}").reload();
				
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.updateFailed@");
			},
			cache : false
		}); 
	}
	
	function relateProfile(){
		window.top.createDialog('relateprofile', "@UNIVLAN.viewProfile@", 800, 500, "/epon/univlanprofile/showRelateProfile.tv?entityId="+entityId+"&uniIndex="+uniIndex+"&profileIndex="+profileId+"&bindProfAttr="+bindProfAttr+"&uniId="+uniId, null, true, true);
	}
	
	function closeProfileView(){
		$('#showUniProfile').fadeOut();
	}
	
	function checkRule(){
		window.top.createDialog('profileRule', "@PROFILE.profileRule@", 800, 500, "/epon/univlanprofile/showProfileRule.tv?entityId="+entityId+"&profileIndex="+profileId+"&profileMode="+profileMode+"&profileRefCnt="+profileRefCnt, null, true, true); 
	}
	
	//从设备刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/univlanprofile/refreshBindInfo.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId
			},
			success : function() {
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
	       	    });
				window.location.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
			},
			cache : false
		});
	}
	
	$(DOC).ready(function(){
		if(profileId != null && profileId > 0){  //uni口绑定了模板
			if(profileMode != null && profileMode != ''){
				$('#modeSelect').val(profileMode);
				if(profileMode > 2){
					$('#viewRule').css("display","block"); //查看规则
				}
			}
		}
	});

	function authLoad(){
	    if(!operationDevicePower){
	        $("#pvid").attr("disabled",true);
	        $("#saveBt").attr("disabled",true);
	    }
	    if(!refreshDevicePower){
	        $("#freshData").attr("disabled",true);
	    }
	}
	
	function doRefresh(){
		window.location = window.location.href;
	}
</script>
</head>
<body class="openWinBody" onload="authLoad();">
	<div class="openWinHeader">
		<div class="openWinTip">@UNIVLAN.uniVlanInfo@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
		
	<div class="edgeTB10LR20 pT40">
		<table class="zebraTableRows">
			<tr>
				<td class="rightBlueTxt w150"><label for="ip">@epon/VLAN.portNum@:</label></td>
				<td><input type="text" class="normalInputDisabled w180" id="portId" value="${uniBindInfo.portString}" disabled />
				</td>
				<td colspan="2"></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt w150">PVID:</td>
				<td><input type="text" id="pvid" class="normalInput w180"
					value="${uniBindInfo.bindPvid}" maxlength='4' tooltip="@RULE.vlanInputTip@" />
				</td>
				<td colspan="2"></td>
			</tr>
			<tr>
              	<td class="rightBlueTxt" width="150">
              		@PROFILE.vlanMode@:
				</td>
				<td width="186">
					<select id="modeSelect" class="normalSelDisabled w180" disabled="disabled">
						<option value="0">@PROFILE.modeNone@</option>
						<option value="1" selected>@PROFILE.modeTransparent@</option>
						<option value="2">@PROFILE.modeTag@</option>
						<option value="3">@PROFILE.modeTranslate@</option>
						<option value="4">@PROFILE.modeAgg@</option>
						<option value="5">@PROFILE.modeTrunk@</option>
					</select>
				</td>
				<td width="60"><span id="viewProfile" ><a href="javascript:;" onclick="relateProfile()" class="yellowLink">@UNIVLAN.viewProfile@</a></span></td>
				<td><span id="viewRule" style="display:none"><a href="javascript:;" onclick="checkRule()" class="yellowLink">@UNIVLAN.viewRule@</a></span></td>
			</tr>
		</table>
	</div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT30 noWidthCenter">
	         <li><a id="freshData" href="javascript:;" class="normalBtnBig" onclick='refreshData()'><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	         <li><a href="javascript:;" class="normalBtnBig" id="saveBt" onclick="saveClick()"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
	         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	
		<div class="openLayerProfile" id="showUniProfile">
		<div class="openLayerProfileBg"></div>
	</div>
</body>
</Zeta:HTML>