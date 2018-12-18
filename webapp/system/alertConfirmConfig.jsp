<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<%@include file="/include/ZetaUtil.inc"%>
	<Zeta:Loader>
	library ext
	library jquery
	library zeta
	module platform
    import js.tools.ipText
</Zeta:Loader>
	<head>
<script>
	//save config
	function okClick() {
		var alertConfirmConfig = $('#alertConfirmConfigSelect').val();
		window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
		$.ajax({
			url : '/fault/saveAlertConfirmConfig.tv',
			type : 'POST',
			data : {alertConfirmConfig:alertConfirmConfig},
			success : function(json) {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : '@sys.tip@',
					html : '<b class="orangeTxt">@sys.saved@</b>'
				});
				cancelClick();
			},
			error : function(json) {
				window.top.showErrorDlg();
			},
			cache : false
		});
	}
	//close the dialog
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}

	Ext.onReady(function() {
		$("#alertConfirmConfigSelect").val(String(${alertConfirmConfig}));
	});
	
	
</script>
	</head>
	<body class="openWinBody">
		<div class=formtip id=tips style="display: none"></div>
		<form id="alertConfirmConfigForm" name="alertConfirmConfigForm">
			<div class="openWinHeader">
				<div class="openWinTip">@sys.setAlertConfirmConfig@</div>
				<div class="rightCirIco wheelCirIco"></div>
			</div>
			<div class="edgeTB10LR20 pT40">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="180"><label
								for="alertConfirmConfigSelect">@sys.alertConfirmConfig@:</label></td>
							<td class="pR10" width="300"><select class="normalSel"
								id="alertConfirmConfigSelect">
									<option value="true">@sys.alertConfirmOpen@</option>
									<option value="false">@sys.alertConfirmClose@</option>
							</select></td>
						</tr>
						<tr class="darkZebraTr">
		             	<td colspan="2">
		             		<p class="pL80 pB5">@sys.alertConfirmOpenTip@</p>
		            		<p class="pL80 pB5">@sys.alertConfirmCloseTip@</p>
		             	</td>
		             </tr>
					</tbody>
				</table>
				<div class="noWidthCenterOuter clearBoth" id="buttonPanel">
					<ol class="upChannelListOl pB0 pT40 noWidthCenter">
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="okClick()" id="alertSubmit"> <span> <i
									class="miniIcoData"> </i> @sys.save@
							</span>
						</a></li>
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="cancelClick()"> <span> <i
									class="miniIcoForbid"> </i> @sys.cancel@
							</span>
						</a></li>
					</ol>
				</div>
			</div>
		</form>
	</body>
</Zeta:HTML>