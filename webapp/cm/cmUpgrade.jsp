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
    library FileUpload
    module CMC
    plugin DropdownTree
    css css/white/disabledStyle
    css css/animate
</Zeta:Loader>
<style type="text/css">
	.animated {-webkit-animation-duration: 2s; animation-duration: 2s;}
</style>
<script type="text/javascript">
var entityId = '${entityId}';
$(function(){
	var errStr = '@tip.singleCmErrTip@';
	$.ajax({
		url: '/cmupgrade/loadUpgradeCmData.tv',
		data: {
			entityId : entityId
		},
		type: 'POST',
		cache: false,
		dataType: 'json', 
		success: function(json) {
			$("#saveBtn").removeAttr("disabled");
			//现将闪烁的提示信息去掉;
			$("#loadTip").remove();
			var loadErr = '@tip.withoutData@',
			    mac = json.cmMac || loadErr,
				modulNum = json.modulNum || loadErr,
				softVersion = json.softVersion || loadErr,
				upgradeFiles = json.ccmtsSfFileInfoList || [];

			//没有加载到mac地址或者没有升级文件;
			if(mac == loadErr || upgradeFiles.length == 0){
				top.showMessageDlg('@COMMON.tip@', errStr, 'ext-mb-question', function(){});
			}
			
			$("#putMac").text(mac);
			$("#putModulNum").text(modulNum);
			$("#putSoftVersion").text(softVersion);
			if(upgradeFiles.length > 0){
				createSel(upgradeFiles);
			}else{
				$("#putUpgradeFile").text(errStr);
			}
		},
		error: function(json) {
			var tip = json.message || errStr;
			top.showMessageDlg('@COMMON.tip@', errStr, 'ext-mb-question', function(){});
		}
	});
});

function cancelClick(){
	top.closeWindow("cmUpgrade");
}
//创建升级文件下拉框;
function createSel(arr){
	var str = '';
	str += '<select class="w200 normalSel" id="upgradeFile">';
  	$.each(arr, function(i, v){
  		str += String.format('<option value="{0}">{0}</option>', v.topCcmtsSfFileName);
  	})
	str += '</select>'; 
	$("#putUpgradeFile").html(str);
}
function saveClick(){
	var cmMac = $("#putMac").html(),
	    fileName = $("#upgradeFile").val();
	
	//验证MAC地址;
	if(!V.isMac(cmMac)){
		top.showMessageDlg("@COMMON.tip@", "COMMON.reqValidMac");	
		return;
	}
	
	if(fileName == "" || fileName == null){
	    $("#upgradeFile").fadeOut(function(){
			$(this).fadeIn();
		})
		return; 
	} 
	
	
	window.parent.showConfirmDlg('@COMMON.tip@', '@tip.applyingCmUpgrade@', function(type) {
        if (type == 'no') {
            return;
        }
		$.ajax({
			url: '/cmupgrade/upgradeSingleCm.tv',
			data: {
				cmMac : cmMac,
				fileName :fileName,
				entityId : window.entityId
			},
			type: 'POST',
			cache: false,
			dataType: 'text', 
			success: function(json) {
				if(json == "success"){
					top.afterSaveOrDelete({
		        		title : '@COMMON.tip@',
		        		html  : '@tip.applySuccess@'
		        	})
				}else if(json == "upgrading"){
					top.showMessageDlg("@COMMON.tip@", "@tip.applying@");
				}else if(json == "failure"){
					top.showMessageDlg("@COMMON.tip@", "@tip.applyFail2@");
				}else{
					top.showMessageDlg("@COMMON.tip@", "@tip.applyFail@");
				}
				cancelClick();
			},
			error: function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.applyFail@");
			}
		});
	})
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p>@SERVICE.selUpgrade@</p>
	    	<p id="loadTip" class="flash animated infinite">@tip.pleaseWait@</p>
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">CM MAC:</td>
	                <td id="putMac">@resources/WorkBench.loading@</td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">modelNum:</td>
	                <td id="putModulNum">@resources/WorkBench.loading@</td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">@CCMTS.softVersion@</td>
	                <td id="putSoftVersion">@resources/WorkBench.loading@</td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">@opt.selectUpgrade@:</td>
	                <td id="putUpgradeFile">@resources/WorkBench.loading@</td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
	        <ol class="upChannelListOl pB0 pT40 noWidthCenter">
	            <li>
	            	<a disabled="disabled" href="javascript:;" class="normalBtnBig" onclick="saveClick()" id="saveBtn">
	            		<span><i class="miniIcoSave"></i>@COMMON.apply@</span>
	            	</a>
	            </li>
	            <li>
	            	<a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
	            		<span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
	            	</a>
	            </li>
	        </ol>
	    </div>
	</div>
</body>
</Zeta:HTML>