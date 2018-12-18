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
    module onu
    CSS css/white/disabledStyle
    import js/jquery/nm3kPassword
    import js/utils/IpUtil
</Zeta:Loader>
<script type="text/javascript">
	var pppoePasswordField;
	var entityId = '${entityId}';
	var onuId = '${onuId}';
	var connectId = '${connectId}';

	$(document).ready(function() {
		pppoePasswordField = new nm3kPassword({
			id : "pppoePasswordField",
			renderTo : 'pppoePasswordTd',
			width : 122,
			value : '',
			firstShowPassword : true,
			disabled : false,
			maxlength : 15,
			toolTip : '@ONU.WAN.PPPoEPasswordTip@'
		});
		pppoePasswordField.init();

		$.ajax({
			url : '/onu/loadWanConnection.tv',
			type : 'POST',
			data : {
				onuId : onuId,
				connectId : connectId
			},
			dateType : 'json',
			success : function(json) {
				if (json) {
					$('#connectId').val(json.connectId);
					$('#connectName').val(json.connectName);
					$('#connectMtu').val(json.connectMtu);
					$('#vlanId').val(json.vlanId);
					$('#vlanPriority').val(json.vlanPriority);
					//$('#connectMode').val(json.connectMode);
					$('#ipModeSelect').val(json.ipMode);
					$('#pppoeUserName').val(json.pppoeUserName);
					$('#pppoePasswordField').val(json.pppoePassword);
					$('#ipv4Address').val(json.ipv4Address);
					$('#ipv4Mask').val(json.ipv4Mask);
					$('#ipv4Gateway').val(json.ipv4Gateway);
					$('#ipv4Dns').val(json.ipv4Dns);
					$('#ipv4DnsAlternative').val(json.ipv4DnsAlternative);
					$('#serviceModeSelect').val(json.serviceMode);
					$('#bindInterface').val(json.bindInterface);
					ipModeSelectChanged();
				}
			},
			error : function() {
			},
			cache : false
		});
	})

	//取消按钮
	function cancelClick() {
		window.parent.closeWindow('modifyWanConnect');
	}

	//判断IP
	function isIP(ip) {
		if(IpUtil.isIpv4Address(ip) && IpUtil.isValidDeviceIp(ip)) {
			return true;
		} else {
			return false; 
		}
	}
	
	function checkMask(mask) { 
		obj=mask; 
		var exp=/^(254|252|248|240|224|192|128|0)\.0\.0\.0|255\.(254|252|248|240|224|192|128|0)\.0\.0|255\.255\.(254|252|248|240|224|192|128|0)\.0|255\.255\.255\.(254|252|248|240|224|192|128|0)$/; 
		var reg = obj.match(exp); 
		if(reg==null) { 
			return false; //"非法"
		} else { 
			return true; //"合法"
		}
	}

	//验证
	function validate() {
		
		//pppoe模式下mtu上限为1492，其余模式上限为1500
        var mtuStart = 64;
        var mtuEnd = 1500;
        if($('#ipModeSelect').val() == 3){
            mtuEnd = 1492;
        }
        
		if($('#ipModeSelect').val() != 0){
			if (!(/^\d+$/.test($('#connectMtu').val()) && $('#connectMtu').val() >= mtuStart && $('#connectMtu').val() <= mtuEnd)) {
				$('#connectMtu').focus();
				return false;
			}
		}
		/* if ($('#connectName').val().length > 32) {
			window.parent.showMessageDlg("@COMMON.error@",
					"@ONU.WAN.ConnectNameTip@");
			return false;
		} */
		if (!(/^\d+$/.test($('#vlanPriority').val()) && $('#vlanPriority').val() >= 0 && $('#vlanPriority').val() <= 7)) {
			$('#vlanPriority').focus();
			return false;
		}
		if (!(/^\d+$/.test($('#vlanId').val()) && $('#vlanId').val() >= 0 && $('#vlanId').val() <= 4094)) {
			$('#vlanId').focus();
			return false;
		}
		if ($('#ipModeSelect').val() == 2) {//Static
			if (!isIP($('#ipv4Address').val())) {
				$('#ipv4Address').focus();
				return false;
			}
			if (!checkMask($('#ipv4Mask').val())) {
				$('#ipv4Mask').focus();
				return false;
			}
			if (!isIP($('#ipv4Gateway').val())) {
				$('#ipv4Gateway').focus();
				return false;
			}
			if (!isIP($('#ipv4Dns').val())) {
				$('#ipv4Dns').focus();
				return false;
			}
			if (!isIP($('#ipv4DnsAlternative').val())) {
				$('#ipv4DnsAlternative').focus();
				return false;
			}
		}
		if ($('#ipModeSelect').val() == 3) {//PPPOE
			if ($('#pppoeUserName').val().length > 31||$('#pppoeUserName').val().length==0) {
				$('#pppoeUserName').focus();
				return false;
			}
			if ($('#pppoePasswordField').val().length > 15||$('#pppoePasswordField').val().length==0) {
				$('#pppoePasswordField').focus();
				return false;
			}
		}
		return true;
	}

	//保存按钮
	function save() {
		if (!validate()) {
			return;
		}
		var connectId = $('#connectId').val();
		/* var connectName = $('#connectName').val(); */
		var connectMtu = $('#connectMtu').val();
		var vlanId = $('#vlanId').val();
		var vlanPriority = $('#vlanPriority').val();
		//var connectMode = $('#connectMode').val();
		var ipMode = $('#ipModeSelect').val();
		var pppoeUserName = $('#pppoeUserName').val();
		var pppoePassword = $('#pppoePasswordField').val();
		var ipv4Address = $('#ipv4Address').val();
		var ipv4Mask = $('#ipv4Mask').val();
		var ipv4Gateway = $('#ipv4Gateway').val();
		var ipv4Dns = $('#ipv4Dns').val();
		var ipv4DnsAlternative = $('#ipv4DnsAlternative').val();
		var serviceMode = $('#serviceModeSelect').val();
		var bindInterface = $('#bindInterface').val();

		window.parent.showWaitingDlg("@COMMON.wait@",'@ONU.WAN.modify@WAN@ONU.WAN.connection@', 'waitingMsg','ext-mb-waiting');
		$.ajax({
			url : '/onu/modifyWanConnect.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				onuId : onuId,
				connectId : connectId,
				//connectName : connectName,
				connectMtu : connectMtu,
				vlanId : vlanId,
				vlanPriority : vlanPriority,
				//connectMode : connectMode,
				ipMode : ipMode,
				pppoeUserName : pppoeUserName,
				pppoePassword : pppoePassword,
				ipv4Address : ipv4Address,
				ipv4Mask : ipv4Mask,
				ipv4Gateway : ipv4Gateway,
				ipv4Dns : ipv4Dns,
				ipv4DnsAlternative : ipv4DnsAlternative,
				serviceMode : serviceMode,
				bindInterface : bindInterface
			},
			dateType : 'text',
			success : function(response) {
				window.parent.closeWaitingDlg();
				if(response == 'success'){
					top.afterSaveOrDelete({
						title : I18N.COMMON.tip,
						html : '<b class="orangeTxt">' + '@ONU.WAN.save@@ONU.WAN.success@' + '</b>'
					});
					window.parent.getFrame("entity-" + onuId).reloadStore();
					cancelClick();
				}else{
					window.parent.showMessageDlg("@COMMON.tip@", '@ONU.WAN.save@@ONU.WAN.failed@');
				}
			},
			error : function() {
				window.parent.showMessageDlg("@COMMON.tip@", '@ONU.WAN.save@@ONU.WAN.failed@');
			},
			cache : false
		});
	}

	//IP分配模式,修改之后，联动
	function ipModeSelectChanged() {
		if($('#ipModeSelect').val() == 0){
			//ip config
			$('#ipv4Mask').attr('disabled', true).addClass("normalInputDisabled").val("");
            $('#ipv4Gateway').attr('disabled', true).addClass("normalInputDisabled").val("");
            $('#ipv4Dns').attr('disabled', true).addClass("normalInputDisabled").val("");
            $('#ipv4Address').attr('disabled', true).addClass("normalInputDisabled").val("");
            $('#ipv4DnsAlternative').attr('disabled', true).addClass("normalInputDisabled").val("");
            //mtu
			$('#connectMtu').attr('disabled', true).addClass("normalInputDisabled").val("");
			//pppoe
			$('#pppoeUserName').attr('disabled', true).addClass("normalInputDisabled").val("");
			pppoePasswordField.setDisabled(true);
            pppoePasswordField.setValue('');
		} else if ($('#ipModeSelect').val() == 2) {
			//ip config
			$('#ipv4Mask').removeAttr('disabled').removeClass("normalInputDisabled");
			$('#ipv4Gateway').removeAttr('disabled').removeClass("normalInputDisabled");
			$('#ipv4Dns').removeAttr('disabled').removeClass("normalInputDisabled");
			$('#ipv4Address').removeAttr('disabled').removeClass("normalInputDisabled");
			$('#ipv4DnsAlternative').removeAttr('disabled').removeClass("normalInputDisabled");
			//mtu
			$('#connectMtu').removeAttr('disabled').removeClass("normalInputDisabled");
			$('#connectMtu').attr('tooltip', '@ONU.WAN.MTUTip@');
			//pppoe
			$('#pppoeUserName').attr('disabled', true).addClass("normalInputDisabled").val("");
			pppoePasswordField.setDisabled(true);
			pppoePasswordField.setValue('');
		} else if ($('#ipModeSelect').val() == 3) {
			//ip config
			$('#ipv4Mask').attr('disabled', true).addClass("normalInputDisabled").val("");
			$('#ipv4Gateway').attr('disabled', true).addClass("normalInputDisabled").val("");
			$('#ipv4Dns').attr('disabled', true).addClass("normalInputDisabled").val("");
			$('#ipv4Address').attr('disabled', true).addClass("normalInputDisabled").val("");
			$('#ipv4DnsAlternative').attr('disabled', true).addClass("normalInputDisabled").val("");
			//mtu
			$('#connectMtu').removeAttr('disabled').removeClass("normalInputDisabled");
			$('#connectMtu').attr('tooltip', '@ONU.WAN.MTUTipPPPoE@');
			//pppoe
			$('#pppoeUserName').removeAttr('disabled').removeClass("normalInputDisabled");
			pppoePasswordField.setDisabled(false);
		} else {
			//ip config
			$('#ipv4Mask').attr('disabled', true).addClass("normalInputDisabled").val("");
			$('#ipv4Gateway').attr('disabled', true).addClass("normalInputDisabled").val("");
			$('#ipv4Dns').attr('disabled', true).addClass("normalInputDisabled").val("");
			$('#ipv4Address').attr('disabled', true).addClass("normalInputDisabled").val("");
			$('#ipv4DnsAlternative').attr('disabled', true).addClass("normalInputDisabled").val("");
		    //mtu			
			$('#connectMtu').removeAttr('disabled').removeClass("normalInputDisabled");
			$('#connectMtu').attr('tooltip', '@ONU.WAN.MTUTip@');
			//pppoe
		    $('#pppoeUserName').attr('disabled', true).addClass("normalInputDisabled").val("");
			pppoePasswordField.setDisabled(true);
			pppoePasswordField.setValue('');
		}
	}
</script>
<title>@ONU.WAN.modify@WAN@ONU.WAN.connection@</title>
</head>
<body class="openWinBody">
	<div class="edge10 pT5 pB5">
		<ul class="leftFloatUl pB5">
			<li class="blueTxt pT3">WAN ID@COMMON.maohao@</li>
			<li><select id="connectId" class="normalSel w165"  disabled>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
			</select></li>
		</ul>

		<div class="zebraTableCaption clearBoth pT20 pB10">
			<div class="zebraTableCaptionTitle">
				<span>@ONU.WAN.baseInfo@</span>
			</div>
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" id="zebraTable">
				<tr class="withoutBorderBottom">
					<td class="rightBlueTxt" width="130">@ONU.WAN.mtuConnectNum@@COMMON.maohao@</td>
					<td width="170">
						<input class="normalInput w165"	id="connectMtu" tooltip='@ONU.WAN.MTUTip@' />
					</td>
					<td class="rightBlueTxt" width="130">@ONU.WAN.ipDistributeMode@@COMMON.maohao@</td>
					<td><select id="ipModeSelect" class="normalSel w165"onchange="ipModeSelectChanged()">
							<option value="0">Bridge</option>
							<option value="1">DHCP</option>
							<option value="2">Static</option>
							<option value="3">PPPOE</option>
						</select>
					</td>
				</tr>
				<tr class="withoutBorderBottom">
					<td class="rightBlueTxt channelExtModeName">@ONU.WAN.connectName@@COMMON.maohao@</td>
					<td class="channelExtModeName">
						<input disabled	class="normalInput w165" id="connectName" type="text" tooltip='@ONU.WAN.ConnectNameTip@' />
					</td>
					<td class="rightBlueTxt">@ONU.WAN.serviceMode@@COMMON.maohao@</td>
					<td>
						<select id="serviceModeSelect" class="normalSel w165">
							<option value="1">internet</option>
							<option value="2">VOD</option>
							<option value="3">VoIP</option>
							<option value="4">Mgmt</option>
							<option value="5">TR069</option>
						</select>
					</td>
				</tr>
			</table>
		</div>

		<div class="zebraTableCaption pT20 mT5 pB10">
			<div class="zebraTableCaptionTitle">
				<span>@ONU.WAN.vlanInfo@</span>
			</div>
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" id="zebraTable">
				<tr class="withoutBorderBottom">
					<td class="rightBlueTxt" width="130">@ONU.WAN.vlanPriority@@COMMON.maohao@</td>
					<td width="170">
						<input class="normalInput w165" id="vlanPriority" tooltip='@ONU.WAN.VLANPriorityTip@' />
					</td>
					<td class="rightBlueTxt" width="130">VLAN ID@COMMON.maohao@</td>
					<td>
						<input class="normalInput w165" id="vlanId"	tooltip='@ONU.WAN.VLANIDTip@' />
					</td>
				</tr>
			</table>
		</div>

		<div class="zebraTableCaption pT20 mT5 pB10">
			<div class="zebraTableCaptionTitle">
				<span>PPPoE</span>
			</div>
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" id="zebraTable">
				<tr class="withoutBorderBottom">
					<td class="rightBlueTxt" width="130">PPPoE@ONU.WAN.username@@COMMON.maohao@</td>
					<td width="170">
						<input class="normalInput normalInputDisabled w165" id="pppoeUserName" maxlength = 31 disabled="disabled" tooltip='@ONU.WAN.PPPoEUsernameTip@' />
					</td>
					<td class="rightBlueTxt" width="130">PPPoE@ONU.WAN.password@@COMMON.maohao@</td>
					<td id="pppoePasswordTd"></td>
				</tr>
			</table>
		</div>

		<div class="zebraTableCaption pT20 mT5 pB10">
			<div class="zebraTableCaptionTitle">
				<span>@ONU.WAN.ipInfo@</span>
			</div>
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" id="zebraTable">
				<tr class="withoutBorderBottom">
					<td class="rightBlueTxt">@ONU.WAN.ipV4Addr@@COMMON.maohao@</td>
					<td>
						<input class="normalInput normalInputDisabled w165"	id="ipv4Address" disabled="disabled" tooltip='@ONU.WAN.IPV4AddrTip@' />
					</td>
					<td class="rightBlueTxt">@ONU.WAN.ipV4Dns@@COMMON.maohao@</td>
					<td>
						<input class="normalInput normalInputDisabled w165" id="ipv4Dns" disabled="disabled" tooltip='@ONU.WAN.IPV4DNSTip@' />
					</td>
				</tr>
				<tr class="withoutBorderBottom">
					<td class="rightBlueTxt" width="130">@ONU.WAN.ipV4Mask@@COMMON.maohao@</td>
					<td width="170">
						<input class="normalInput normalInputDisabled w165" id="ipv4Mask" disabled="disabled" tooltip='@ONU.WAN.IPV4MaskTip@' />
					</td>
					<td class="rightBlueTxt" width="130">@ONU.WAN.ipV4AlternativeDns@@COMMON.maohao@</td>
					<td>
						<input class="normalInput normalInputDisabled w165"	id="ipv4DnsAlternative" disabled="disabled"	tooltip='@ONU.WAN.IPV4DNS2Tip@' />
					</td>
				</tr>
				<tr class="withoutBorderBottom">
					<td class="rightBlueTxt">@ONU.WAN.ipV4Gateway@@COMMON.maohao@</td>
					<td colspan="3">
						<input class="normalInput normalInputDisabled w165" id="ipv4Gateway" disabled="disabled" tooltip='@ONU.WAN.IPV4GateTip@' />
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div class="noWidthCenterOuter clearBoth" id="buttonPanel">
		<ol class="upChannelListOl pB0 pT0 noWidthCenter">
			<li><a href="javascript:;" class="normalBtnBig"
				onclick="save()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a>
			</li>
			<li><a href="javascript:;" class="normalBtnBig"
				onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a>
			</li>
		</ol>
	</div>
</body>
</Zeta:HTML>
