<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var ponId = <%= request.getParameter("ponId")%>;
	var currentId = '<%=request.getParameter("currentId")%>';
	var entityId = <%=request.getParameter("entityId")%>;
	var encryptMode = <%=request.getParameter("encryptMode")%>;
	var exchangeTime = <%=request.getParameter("exchangeTime")%>;
	//PON口最大带宽限制,暂时是假数据
	//var ponBandMax = <s:property value = "ponBandMax"/>;
	//var maxMac = <%=request.getParameter("maxMac")%>;
	Ext.BLANK_IMAGE_URL = '../images/s.gif';
	var editFlag ; //记录页面的编辑状态，初始为false，有编辑不成功就修改为false，编辑成功就为true ： 需修改
	function initData() {
		var objSelect = document.getElementById("encryptMode");
	   	for(var i = 0; i < objSelect.options.length; i++) {
		    if (objSelect.options[i].value == encryptMode) {
		        objSelect.options[i].selected = true;
		        break;
	    	}
	    }
		$("#exchangeTime").val(exchangeTime);
		if (encryptMode != 3) {
			$("#exchangeTime").attr("disabled", false);
		}
		//$("#maxMac").val(maxMac);
		//$("#bandMax").val(ponBandMax);
	}
	function saveClick() {
			//基本配置
			var encryptMode2 = $("#encryptMode").val()
			var exchangeTime2 = $("#exchangeTime").val()
			var reg = /^[1-9]\d{0,4}$/;
			// 不加密时，密钥交换时间必须为0。
			if (encryptMode2 == 3) {
				exchangeTime2 = 0
			}else if( !reg.exec(exchangeTime2) || exchangeTime2 < 1 || exchangeTime2 > 65535 ){
				$("#exchangeTime").focus();
				return;
			}
			//var bandMax = $("#bandMax").val();
			/* if(!bandMax || bandMax.indexOf(".") > -1 || bandMax.indexOf("-") > -1 || isNaN(bandMax) 
					|| parseFloat(bandMax) < 1 || parseFloat(bandMax) > 1000){
				$("#bandMax").focus();
				return;
			} */
			window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SERVICE.cfmMdfEncrypt , function(type) {
				if (type == 'no') {return;}
					window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.mdfingEncrypt , 'ext-mb-waiting');
					Ext.Ajax.request({
						url:"/epon/modifyPonPortEncryptMode.tv",
						success: function (response) {
							window.parent.closeWaitingDlg();
							var json = Ext.util.JSON.decode(response.responseText);
							if (json.message) {
								window.parent.showMessageDlg(I18N.COMMON.tip, json.message);
							} else {
								window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.mdfEncryptOk);
								updateOltJson(currentId, 'ponPortEncryptMode', encryptMode2);
								updateOltJson(currentId, 'ponPortEncryptKeyExchangeTime', exchangeTime2);
								cancelClick();
							}
					    }, failure: function (response) {
					    	window.parent.closeWaitingDlg();
					        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.mdfEncryptError)
					        cancelClick();
					    }, params: {entityId : entityId, ponId : ponId, ponPortEncryptMode : encryptMode2, 
						    	ponPortEncryptKeyExchangeTime : exchangeTime2}
						});
					});
	}
	function cancelClick() {
		// 关闭窗口后，面板页面重新计时。
		window.parent.getFrame("entity-" + entityId).timer = 0
		window.parent.closeWindow('ponBaseConfig')
	}
	
	function checkEncrypt() {
		var encrypt = $("#encryptMode")
		var time = $("#exchangeTime")
		if(3 == encrypt.val()){
			time.val(0).attr("disabled", true)
		}else{
			time.attr("disabled", false)
		}
	}
	function updateOltJson(currentId, attr, value) {
		window.parent.getFrame("entity-" + entityId).updateOltJson(currentId, attr, value)
	}
	function checkChange(){
		//var num = $("#maxMac").val();	
		var time = $("#exchangeTime").val()
		var mode = $("#encryptMode").val()
		//var bandMax = $("#bandMax").val()
        var changeFlag = false;
		/* if (num != maxMac) {
			changeFlag = true;
		} */

        if (mode != 3 && time != exchangeTime) {
            changeFlag = true;
        }
		if (mode != encryptMode) {
			changeFlag = true;
		}
		/* if(bandMax != ponBandMax){
			changeFlag = true;
		} */
		if (changeFlag) {
			//$("#saveBt").attr("disabled", false);
			R.saveBt.setDisabled( false );
		} else {
			//$("#saveBt").attr("disabled", true);
			R.saveBt.setDisabled( true );
		}
	}
	function checkValid(id) {
		var value = $("#" + id).val();
		if (id == "exchangeTime") {
			if (isNaN(value) || value < 1 || value > 65535 || value.indexOf(".") > -1 || value.indexOf("-") > -1) {
				//$("#saveBt").attr("disabled", true);
				R.saveBt.setDisabled( true );
				return;
			} else {
				R.saveBt.setDisabled( false );
			}
		} 
		/* if(id == "bandMax"){
			if (isNaN(value) || value < 1 || value > 1000 || value.indexOf(".") > -1 || value.indexOf("-") > -1) {
				$("#saveBt").attr("disabled", true);
				return;
			}
		} */
	}
	function authLoad(){
		if(!operationDevicePower){
			$("#encryptMode").attr("disabled",true);
			$("#exchangeTime").attr("disabled",true);
			R.saveBt.setDisabled(true);
		}
	}
</script>
</head>
	<body class=openWinBody onload="initData();authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@EPON.ponPortconfig@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="w200 rightBlueTxt">@SERVICE.portEncrypt@:</td>
						<td><select id="encryptMode" onchange="checkEncrypt();" class="normalSel w200">
								<option value="1">AES128</option>
								<option value="2">@SERVICE.thirdMix@(CTC)</option>
								<option value="3" selected>@SERVICE.noEncrypt@</option>
						</select></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@SERVICE.keySwapTime@:</td>
						<td><input type=text id="exchangeTime" value="1" class="normalInput w200"
							maxlength="5" tooltip="@SERVICE.range65535@" disabled />&nbsp; @COMMON.S@</td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@BUTTON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@BUTTON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>