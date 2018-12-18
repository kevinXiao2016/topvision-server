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
    import js/jquery/nm3kPassword
    CSS css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
	var entityId = '${entityId}';
	var onuId = '${onuId}';
	var onuIndex = '${onuIndex}';
	var ssidList = '${ssidList}';
	var passwordField;

	$(function() {
		passwordField = new nm3kPassword({
			id : "passwordField",
			renderTo : 'passwordTd',
			width : 122,
			value : '',
			firstShowPassword : true,
			disabled : true,
			maxlength : 64,
			toolTip: '@ONU.WAN.SSIDPasswordTip@'
		});
		passwordField.init();
		ssidList = ssidList.split(",");
		for (var i = 0; i < ssidList.length; i++) {
			$("#ssid option[value=" + ssidList[i] + "]").remove();
		}
	});

	//加密模式下拉框改变
	function encryptModeChanged() {
		var encryptMode = $('#encryptMode').val();
		if (encryptMode == 0) {
			passwordField.setDisabled(true);
			passwordField.setValue('');
		} else {
			passwordField.setDisabled(false);
		}
	}

	//取消按钮,关闭本窗口
	function cancelClick() {
		window.parent.closeWindow('addWanSsid');
	}

	//验证
	function validate() {
		if ($('#ssidName').val().length > 32||$('#ssidName').val().length ==0) {
			$('#ssidName').focus();
			return false;
		}
		if ($('#passwordField').val().length > 64) {
			$('#passwordField').focus();
			return false;
		}
		if (!(/^\d+$/.test($('#ssidMaxUser').val())	&& $('#ssidMaxUser').val() >= 0 && $('#ssidMaxUser').val() <= 128)) {
			$('#ssidMaxUser').focus();
			return false;
		} 
		return true;
	}

	//保存按钮
	function save() {
		if (!validate()) {
			return;
		}
		var ssid = $('#ssid').val();
		var ssidName = $('#ssidName').val();
		var encryptMode = $('#encryptMode').val();
		var password = $('#passwordField').val();
		var ssidEnnable = $('#ssidEnable').val();
		var ssidBroadcastEnnable = $('#ssidBroadcastEnable').val();
		var ssidMaxUser = $('#ssidMaxUser').val();

		window.parent.showWaitingDlg("@COMMON.wait@", '@ONU.WAN.new@SSID','waitingMsg', 'ext-mb-waiting');
		$.ajax({
			url : '/onu/addWanSsid.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				onuId : onuId,
				ssid : ssid,
				ssidName : ssidName,
				encryptMode : encryptMode,
				password : password,
				ssidEnnable : ssidEnnable,
				ssidBroadcastEnnable : ssidBroadcastEnnable,
				ssidMaxUser : ssidMaxUser,
				onuIndex : onuIndex
			},
			dateType : 'json',
			success : function(response) {
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : I18N.COMMON.tip,
					html : '<b class="orangeTxt">' + '@ONU.WAN.new@@ONU.WAN.success@' + '</b>'
				});
				window.parent.getFrame("entity-" + onuId).refreshGrid();
				cancelClick();
			},
			error : function() {
				window.parent.closeWaitingDlg();
				window.parent.showMessageDlg("@COMMON.tip@","@ONU.WAN.new@@ONU.WAN.failed@");
			},
			cache : false
		});
	}
</script>
<title>@ONU.WAN.new@SSID</title>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip">@ONU.WAN.newSSIDTip@</div>
		<div class="rightCirIco upArrCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT10">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" id="zebraTable">
			<tr>
				<td class="rightBlueTxt" width="150">SSID@COMMON.maohao@</td>
				<td>
					<select id="ssid" class="normalSel w165">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
				</select></td>
				<td class="rightBlueTxt">@ONU.WAN.name@@COMMON.maohao@</td>
				<td>
					<input class="normalInput w165" id="ssidName" maxlength=31 tooltip='@ONU.ssidNameTip@' />
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@ONU.WAN.encryptMode@@COMMON.maohao@</td>
				<td>
					<select id="encryptMode" class="normalSel w165"	onchange="encryptModeChanged()">
						<option value="0">NONE</option>
						<option value="1">WEP</option>
						<option value="2">WPA</option>
						<option value="3">WPA2</option>
						<option value="4">WPA_WPA2</option>
					</select>
				</td>
				<td class="rightBlueTxt">@ONU.WAN.wirelssPassword@@COMMON.maohao@</td>
				<td id="passwordTd"></td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@ONU.WAN.SSIDEnable@@COMMON.maohao@</td>
				<td>
					<select id="ssidEnable" class="normalSel w165">
						<option value="1">@ONU.WAN.open@</option>
						<option value="2">@ONU.WAN.close@</option>
					</select>
				</td>
				<td class="rightBlueTxt">@ONU.WAN.SSIDBroadcastEnable@@COMMON.maohao@</td>
				<td>
					<select id="ssidBroadcastEnable" class="normalSel w165">
						<option value="1">@ONU.WAN.open@</option>
						<option value="2">@ONU.WAN.close@</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@ONU.WAN.maxUserNumber@@COMMON.maohao@</td>
				<td>
					<input class="normalInput w165" id="ssidMaxUser" tooltip = '@ONU.WAN.MaxUserTip@' />
				</td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</div>

	<Zeta:ButtonGroup>
		<Zeta:Button onclick="save()" icon="miniIcoSave" id="saveBtn">@ONU.WAN.save@</Zeta:Button>
		<Zeta:Button onclick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>

</body>
</Zeta:HTML>
