<%@ page language="java" contentType="text/html; charset=utf-8"%>
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
    module network
</Zeta:Loader>
<script type="text/javascript">
var retryTimes = '${upgradeGlobalParam.retryTimes}',
	retryInterval = '${upgradeGlobalParam.retryInterval}',
	writeConfig = '${upgradeGlobalParam.writeConfig}'
function isDigit(s) { 
	var patrn=/^[0-9]{1,20}$/; 
	if (!patrn.exec(s)) 
	return false; 
	return true; 
}

function save(){
	var retryTimes = $("#retryTimes").val();
	var retryInterval = $("#retryInterval").val();
	if(!isDigit(retryTimes) || parseInt(retryTimes) > 30 || parseInt(retryTimes) < 0){
		$("#retryTimes").focus();
		return;
	}
	if(!isDigit(retryInterval) || parseInt(retryInterval, 10) < 30){
		$("#retryInterval").focus();
		return;
	}
	if($("#writeConfig").attr('checked') == true){
		writeConfig = 0;
	}else{
		writeConfig = 1;
	}
	retryInterval = retryInterval * 60 * 1000;
	$.ajax({
		  url: '/upgrade/modifyUpgradeParam.tv',
	      type: 'POST',
          data:{
        	  "retryTimes" : retryTimes,
        	  "retryInterval" : retryInterval,
        	  "writeConfig" : writeConfig
          },
			success : function(response) {
				if (response == "success") {
					window.location.reload();
				} else {
					window.parent.showMessageDlg('@resources/COMMON.tip@', '@batchtopo.savefail@');
				}
			},
			error : function(response) {
				window.parent.showMessageDlg('@resources/COMMON.tip@', '@batchtopo.savefail@');
			},
			cache : false
		});
	}

	$(function() {
		$("#retryTimes").val(retryTimes);
		$("#retryInterval").val(retryInterval / 1000 / 60);
		if (writeConfig == 0) {
			$("#writeConfig").attr('checked', true);
		} else {
			$("#writeConfig").attr('checked', false);
		}
	})
</script>
</head>
<body class="whiteToBlack">
	<div class="formtip" id="tips" style="display: none"></div>
	<div class="edge10">
			<p class="flagP clearBoth"><span class="flagInfo">@batchUpgrade.globalParamConfig@</span></p>
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			 <thead>
				 <tr>
					 <th class="txtRightTh" style="text-align: right"></th>
					 <th colspan="6" style="text-align:left;"></th>
				 </tr>
			 </thead>
			 <tbody>
				 <tr>
					 <td class="rightBlueTxt" width="150">@batchTopo.retry@:
					 </td>
					 <td><input id="retryTimes" type="text" class="normalInput" tooltip='@batchUpgrade.retryTimeTip@'/></td>
					 <td class="rightBlueTxt" width="150">@batchUpgrade.retryInterval@:
					 </td>
					 <td><input id="retryInterval" type="text" class="normalInput" tooltip='@batchUpgrade.retryIntervalTip@'/> @label.minutes@</td>
						<td class="rightBlueTxt" width="150"><input type="checkbox"
							name="writeConfig" id="writeConfig"
							/></td>
						<td><label for="writeConfig">@MENU.saveConfig@</label></td>
					</tr>
				<tr>
					<td colspan="6">
						<div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB5 pT5 noWidthCenter">
						         <li><a  onclick="save()" href="javascript:;" class="normalBtn"><span><i class="miniIcoData"></i>@resources/COMMON.save@</span></a></li>
						     </ol>
						</div>
					</td>
				</tr>
			</tbody>
			</table>
			<div id='dataPolicyConfig' class="pB10"></div>
	</div>
	</body>
</Zeta:HTML>